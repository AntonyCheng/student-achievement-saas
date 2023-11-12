package top.sharehome.issuemodule.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson2.JSON;
import com.alibaba.nacos.api.exception.NacosException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.sharehome.issuemodule.common.constant.CommonConstant;
import top.sharehome.issuemodule.common.exception_handler.customize.CustomizeTransactionException;
import top.sharehome.issuemodule.common.response.RCodeEnum;
import top.sharehome.issuemodule.config.RabbitMqConfig;
import top.sharehome.issuemodule.mapper.TenantMapper;
import top.sharehome.issuemodule.mapper.TokenMapper;
import top.sharehome.issuemodule.mapper.UpdateMapper;
import top.sharehome.issuemodule.model.dto.tenant.*;
import top.sharehome.issuemodule.model.entity.Tenant;
import top.sharehome.issuemodule.model.entity.Token;
import top.sharehome.issuemodule.model.entity.Update;
import top.sharehome.issuemodule.model.vo.tenant.TenantInfoVo;
import top.sharehome.issuemodule.model.vo.tenant.TenantPageVo;
import top.sharehome.issuemodule.service.TenantService;
import top.sharehome.issuemodule.utils.meta.BeanMetaDataUtils;
import top.sharehome.issuemodule.utils.nacos.NacosConfigUtils;
import top.sharehome.issuemodule.utils.cos.CosUtils;
import top.sharehome.issuemodule.utils.throwException.ThrowUtils;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 租户注册相关接口Service实现类（租户提交注册申请、平台管理员审核租户申请等）
 *
 * @author AntonyCheng
 * @since 2023/7/7 15:41:24
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TenantServiceImpl extends ServiceImpl<TenantMapper, Tenant> implements TenantService {
    @Resource
    private TenantMapper tenantMapper;

    @Resource
    private TokenMapper tokenMapper;

    @Resource
    private UpdateMapper updateMapper;

    @Resource
    private CosUtils cosUtils;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource(name = "noSingletonRabbitTemplate")
    private RabbitTemplate noSingletonRabbitTemplate;

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public void register(TenantRegisterDto tenantRegisterDto) {
        // 与数据库数据进行判重
        LambdaQueryWrapper<Tenant> tenantLambdaQueryWrapper = new LambdaQueryWrapper<>();
        tenantLambdaQueryWrapper.eq(Tenant::getAccount, tenantRegisterDto.getAccount());
        if (tenantMapper.exists(tenantLambdaQueryWrapper)) {
            ThrowUtils.error(RCodeEnum.USERNAME_ALREADY_EXISTS);
        }

        // 生成插入对象
        Tenant insertTenant = new Tenant();
        BeanUtils.copyProperties(tenantRegisterDto, insertTenant);

        // 调用外部接口生成地点坐标
        String result = HttpRequest.get("https://restapi.amap.com/v3/geocode/geo?address=" + tenantRegisterDto.getSchool() + tenantRegisterDto.getCollege() + "&key=712fabab9fb2f8776a0161e064bf76b3").execute().body();
        String coordinate = (String) ((Map<?, ?>) ((List<?>) ((Map<?, ?>) JSON.parse(result)).get("geocodes")).get(0)).get("location");
        insertTenant.setCoordinate(coordinate);

        // 插入目标数据库
        int insertResult = tenantMapper.insert(insertTenant);
        if (insertResult == 0) {
            ThrowUtils.error(RCodeEnum.DB_DATA_ADDITION_FAILED);
        }
    }

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public void censor(TenantCensorDto tenantCensorDto) {
        Tenant tenantInDatabase = tenantMapper.selectById(tenantCensorDto.getId());
        if (Objects.isNull(tenantInDatabase)) {
            ThrowUtils.error(RCodeEnum.USER_ACCOUNT_DOES_NOT_EXIST);
        }
        if (tenantInDatabase.getStatus() != CommonConstant.TENANT_STATUS_CENSOR) {
            ThrowUtils.error(RCodeEnum.ACCOUNT_HAS_BEEN_REVIEWED);
        }
        LambdaUpdateWrapper<Tenant> tenantLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        tenantLambdaUpdateWrapper.eq(Tenant::getId, tenantCensorDto.getId());
        Tenant tenant = new Tenant();
        if (Objects.equals(tenantCensorDto.getResult(), CommonConstant.CENSOR_PASS)) {
            tenant.setIp(tenantCensorDto.getIp());
            tenant.setPort(tenantCensorDto.getPort());
            tenant.setDbIp(tenantCensorDto.getDbIp());
            tenant.setDbPort(tenantCensorDto.getDbPort());
            tenant.setStatus(CommonConstant.TENANT_STATUS_ENABLE);
            int updateResult = tenantMapper.update(tenant, tenantLambdaUpdateWrapper);
            if (updateResult == 0) {
                ThrowUtils.error(RCodeEnum.DB_DATA_MODIFICATION_FAILED);
            }
            Token newToken = new Token();
            newToken.setAccount(tenantInDatabase.getAccount());
            newToken.setPassword(tenantInDatabase.getPassword());
            newToken.setTenant(tenantInDatabase.getId());
            newToken.setIdentity(CommonConstant.LOGIN_IDENTITY_MANAGER);
            int insertResult = tokenMapper.insert(newToken);
            if (insertResult == 0) {
                ThrowUtils.error(RCodeEnum.DB_DATA_ADDITION_FAILED);
            }

            String to = tenantCensorDto.getEmail();
            String subject = "xxx科技有限公司平台信息反馈";
            String content = StringUtils.isEmpty(tenantCensorDto.getContent()) ?
                    "如有疑问，请联系邮箱“wushiyuankeji@163.com”" : tenantCensorDto.getContent();
            String template = "<body>\n" +
                    "<h1>xxx科技有限公司</h1>\n" +
                    "<p style=\"font-size: x-large\">恭喜您！您的注册申请已经审核通过，注册账号为“<b>" + tenantCensorDto.getAccount() + "</b>”。</p>\n" +
                    "<p style=\"font-size: x-large\">请使用注册时输入的密码进行登陆，请点击<b><a href=\"http://localhost:80\">这里</a></b>进行访问！</p>\n" +
                    "<p><b>备注：</b>" + content + "</p>\n" +
                    "</body>";
            HashMap<String, Object> rabbitMqResult = new HashMap<>();
            rabbitMqResult.put("to", to);
            rabbitMqResult.put("subject", subject);
            rabbitMqResult.put("content", template);
            noSingletonRabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
                @Override
                public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                    if (!ack) {
                        ThrowUtils.error(RCodeEnum.PROVIDER_TO_EXCHANGE_ERROR);
                    }
                }
            });
            noSingletonRabbitTemplate.setReturnsCallback(new RabbitTemplate.ReturnsCallback() {
                @Override
                public void returnedMessage(ReturnedMessage returnedMessage) {
                    ThrowUtils.error(RCodeEnum.EXCHANGE_TO_QUEUE_ERROR, returnedMessage.getMessage().toString());
                }
            });
            noSingletonRabbitTemplate.convertAndSend(RabbitMqConfig.MAIL_MODULE_HTML_EXCHANGE, "mail_module_html.TenantServiceImpl", JSON.toJSONString(rabbitMqResult));
            LambdaUpdateWrapper<Tenant> tempLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            tempLambdaUpdateWrapper.eq(Tenant::getStatus, CommonConstant.TENANT_STATUS_ENABLE);
            List<Tenant> tenants = tenantMapper.selectList(tempLambdaUpdateWrapper);
            try {
                Boolean publishConfig = new NacosConfigUtils().publishYamlConfig(tenants);
                if (!publishConfig) {
                    ThrowUtils.error(RCodeEnum.FAILED_TO_PUBLISH_NACOS_CONFIGURATION_FILE);
                }
            } catch (NacosException e) {
                ThrowUtils.error(RCodeEnum.FAILED_TO_PUBLISH_NACOS_CONFIGURATION_FILE);
            }
        }

        if (Objects.equals(tenantCensorDto.getResult(), CommonConstant.CENSOR_NOT_PASS)) {
            tenant.setIp("0");
            tenant.setPort("0");
            tenant.setDbIp("0");
            tenant.setDbPort("0");
            tenant.setStatus(CommonConstant.TENANT_STATUS_CENSOR_DISABLE);
            int updateResult = tenantMapper.update(tenant, tenantLambdaUpdateWrapper);
            if (updateResult == 0) {
                ThrowUtils.error(RCodeEnum.DB_DATA_MODIFICATION_FAILED);
            }

            String to = tenantCensorDto.getEmail();
            String subject = "xxx科技有限公司平台信息反馈";
            String content = StringUtils.isEmpty(tenantCensorDto.getContent()) ?
                    "如有疑问，请联系邮箱“wushiyuankeji@163.com”" : tenantCensorDto.getContent();
            String template = "<body>\n" +
                    "<h1>xxx科技有限公司</h1>\n" +
                    "<p style=\"font-size: x-large\">很抱歉！您的注册申请已经审核驳回，注册账号为“<b>" + tenantCensorDto.getAccount() + "</b>”，详情请查看备注内容。</p>\n" +
                    "<p><b>备注：</b>" + content + "</p>\n" +
                    "</body>";
            HashMap<String, Object> rabbitMqResult = new HashMap<>();
            rabbitMqResult.put("to", to);
            rabbitMqResult.put("subject", subject);
            rabbitMqResult.put("content", template);
            noSingletonRabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
                @Override
                public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                    if (!ack) {
                        ThrowUtils.error(RCodeEnum.PROVIDER_TO_EXCHANGE_ERROR);
                    }
                }
            });
            noSingletonRabbitTemplate.setReturnsCallback(new RabbitTemplate.ReturnsCallback() {
                @Override
                public void returnedMessage(ReturnedMessage returnedMessage) {
                    ThrowUtils.error(RCodeEnum.EXCHANGE_TO_QUEUE_ERROR, returnedMessage.getMessage().toString());
                }
            });
            noSingletonRabbitTemplate.convertAndSend(RabbitMqConfig.MAIL_MODULE_HTML_EXCHANGE, "mail_module_html.TenantServiceImpl", JSON.toJSONString(rabbitMqResult));
        }
    }

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public Page<TenantPageVo> pageTenant(Integer current, Integer pageSize, TenantPageDto tenantPageDto) {
        // 创建原始分页数据以及返回分页数据
        Page<Tenant> page = new Page<>(current, pageSize);
        Page<TenantPageVo> returnResult = new Page<>(current, pageSize);

        // 过滤分页对象
        LambdaQueryWrapper<Tenant> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper
                .orderByDesc(Tenant::getCreateTime)
                .orderByAsc(Tenant::getStatus);

        // 当不存在模糊查询时的分页操作
        if (BeanMetaDataUtils.isAllMetadataEmpty(tenantPageDto)) {
            this.page(page, lambdaQueryWrapper);
            BeanUtils.copyProperties(page, returnResult, "records");
            List<TenantPageVo> pageVoList = page.getRecords().stream().map(record -> {
                TenantPageVo tenantPageVo = new TenantPageVo();
                BeanUtils.copyProperties(record, tenantPageVo);
                LambdaUpdateWrapper<Token> tokenLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                tokenLambdaUpdateWrapper.eq(Token::getTenant, record.getId());
                Long number = tokenMapper.selectCount(tokenLambdaUpdateWrapper);
                tenantPageVo.setNumber(number);
                return tenantPageVo;
            }).collect(Collectors.toList());
            returnResult.setRecords(pageVoList);
            return returnResult;
        }

        // 当存在模糊查询时的分页操作
        lambdaQueryWrapper
                .like(StringUtils.isNotEmpty(tenantPageDto.getAccount()), Tenant::getAccount, tenantPageDto.getAccount())
                .like(StringUtils.isNotEmpty(tenantPageDto.getName()), Tenant::getName, tenantPageDto.getName())
                .eq(Objects.nonNull(tenantPageDto.getGender()), Tenant::getGender, tenantPageDto.getGender())
                .like(StringUtils.isNotEmpty(tenantPageDto.getEmail()), Tenant::getEmail, tenantPageDto.getEmail())
                .like(StringUtils.isNotEmpty(tenantPageDto.getSchool()), Tenant::getSchool, tenantPageDto.getSchool())
                .like(StringUtils.isNotEmpty(tenantPageDto.getCollege()), Tenant::getCollege, tenantPageDto.getCollege())
                .like(StringUtils.isNotEmpty(tenantPageDto.getAddress()), Tenant::getAddress, tenantPageDto.getAddress())
                .like(StringUtils.isNotEmpty(tenantPageDto.getIp()), Tenant::getIp, tenantPageDto.getIp())
                .like(StringUtils.isNotEmpty(tenantPageDto.getPort()), Tenant::getPort, tenantPageDto.getPort())
                .like(StringUtils.isNotEmpty(tenantPageDto.getDbIp()), Tenant::getDbIp, tenantPageDto.getDbIp())
                .like(StringUtils.isNotEmpty(tenantPageDto.getDbPort()), Tenant::getDbPort, tenantPageDto.getDbPort())
                .eq(Objects.nonNull(tenantPageDto.getLevel()), Tenant::getLevel, tenantPageDto.getLevel())
                .eq(Objects.nonNull(tenantPageDto.getStatus()), Tenant::getStatus, tenantPageDto.getStatus());
        this.page(page, lambdaQueryWrapper);
        BeanUtils.copyProperties(page, returnResult, "records");
        List<TenantPageVo> pageVoList = page.getRecords().stream().map(record -> {
            TenantPageVo tenantPageVo = new TenantPageVo();
            BeanUtils.copyProperties(record, tenantPageVo);
            LambdaUpdateWrapper<Token> tokenLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            tokenLambdaUpdateWrapper.eq(Token::getTenant, record.getId());
            Long number = tokenMapper.selectCount(tokenLambdaUpdateWrapper);
            tenantPageVo.setNumber(number);
            return tenantPageVo;
        }).collect(Collectors.toList());
        returnResult.setRecords(pageVoList);
        return returnResult;
    }

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public void delete(Long id) {
        Tenant tenantInDatabase = tenantMapper.selectById(id);
        if (Objects.isNull(tenantInDatabase)) {
            ThrowUtils.error(RCodeEnum.THE_DELETED_TARGET_DOES_NOT_EXIST);
        }
        if (tenantInDatabase.getStatus() == CommonConstant.TENANT_STATUS_CENSOR) {
            ThrowUtils.error(RCodeEnum.USER_ACCOUNT_IS_CENSOR);
        }
        LambdaQueryWrapper<Update> updateLambdaQueryWrapper = new LambdaQueryWrapper<>();
        updateLambdaQueryWrapper.eq(Update::getTenant, tenantInDatabase.getId());
        updateMapper.delete(updateLambdaQueryWrapper);
        LambdaUpdateWrapper<Token> tokenLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        tokenLambdaUpdateWrapper.eq(Token::getTenant, id);
        int deleteTokenResult = tokenMapper.delete(tokenLambdaUpdateWrapper);
        if (tenantInDatabase.getStatus() != CommonConstant.TENANT_STATUS_CENSOR_DISABLE && deleteTokenResult == 0) {
            ThrowUtils.error(RCodeEnum.DB_DATA_DELETION_FAILED);
        }
        int deleteTenantResult = tenantMapper.deleteById(id);
        if (deleteTenantResult == 0) {
            ThrowUtils.error(RCodeEnum.DB_DATA_DELETION_FAILED);
        }
        List<Token> loginTokens = tokenMapper.selectList(tokenLambdaUpdateWrapper);
        List<String> tokens = loginTokens.stream().map(token -> "token_" + token.getId()).collect(Collectors.toList());
        redisTemplate.delete(tokens);
        LambdaUpdateWrapper<Tenant> tenantLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        tenantLambdaUpdateWrapper.eq(Tenant::getStatus, CommonConstant.TENANT_STATUS_ENABLE);
        List<Tenant> tenants = tenantMapper.selectList(tenantLambdaUpdateWrapper);
        try {
            Boolean publishConfig = new NacosConfigUtils().publishYamlConfig(tenants);
            if (!publishConfig) {
                ThrowUtils.error(RCodeEnum.FAILED_TO_PUBLISH_NACOS_CONFIGURATION_FILE);
            }
        } catch (NacosException e) {
            ThrowUtils.error(RCodeEnum.FAILED_TO_PUBLISH_NACOS_CONFIGURATION_FILE);
        }
        cosUtils.deleteInCos(tenantInDatabase.getPicture());
    }

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public void deleteBatch(List<Long> ids) {
        List<Tenant> tenantsInDatabase = tenantMapper.selectBatchIds(ids);
        if (tenantsInDatabase.size() != ids.size()) {
            ThrowUtils.error(RCodeEnum.THE_DELETED_TARGET_DOES_NOT_EXIST);
        }
        List<String> pictureUrls = tenantsInDatabase.stream().map(tenantInDatabase -> {
            if (tenantInDatabase.getStatus() == CommonConstant.TENANT_STATUS_CENSOR) {
                ThrowUtils.error(RCodeEnum.USER_ACCOUNT_IS_CENSOR);
            }
            return tenantInDatabase.getPicture();
        }).collect(Collectors.toList());
        for (Tenant tenantInDatabase : tenantsInDatabase) {
            LambdaQueryWrapper<Update> updateLambdaQueryWrapper = new LambdaQueryWrapper<>();
            updateLambdaQueryWrapper.eq(Update::getTenant, tenantInDatabase.getId());
            updateMapper.delete(updateLambdaQueryWrapper);
            LambdaUpdateWrapper<Token> tokenLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            tokenLambdaUpdateWrapper.eq(Token::getTenant, tenantInDatabase.getId());
            int deleteTokenResult = tokenMapper.delete(tokenLambdaUpdateWrapper);
            if (tenantInDatabase.getStatus() != CommonConstant.TENANT_STATUS_CENSOR_DISABLE && deleteTokenResult == 0) {
                ThrowUtils.error(RCodeEnum.DB_DATA_DELETION_FAILED);
            }
            List<Token> loginTokens = tokenMapper.selectList(tokenLambdaUpdateWrapper);
            List<String> tokens = loginTokens.stream().map(token -> "token_" + token.getId()).collect(Collectors.toList());
            redisTemplate.delete(tokens);
        }
        int deleteTenantResult = tenantMapper.deleteBatchIds(ids);
        if (deleteTenantResult == 0) {
            ThrowUtils.error(RCodeEnum.DB_DATA_DELETION_FAILED);
        }
        LambdaUpdateWrapper<Tenant> tenantLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        tenantLambdaUpdateWrapper.eq(Tenant::getStatus, CommonConstant.TENANT_STATUS_ENABLE);
        List<Tenant> tenants = tenantMapper.selectList(tenantLambdaUpdateWrapper);
        try {
            Boolean publishConfig = new NacosConfigUtils().publishYamlConfig(tenants);
            if (!publishConfig) {
                ThrowUtils.error(RCodeEnum.FAILED_TO_PUBLISH_NACOS_CONFIGURATION_FILE);
            }
        } catch (NacosException e) {
            ThrowUtils.error(RCodeEnum.FAILED_TO_PUBLISH_NACOS_CONFIGURATION_FILE);
        }
        for (String pictureUrl : pictureUrls) {
            cosUtils.deleteInCos(pictureUrl);
        }
    }

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public TenantInfoVo info(Long id) {
        Tenant tenantInDatabase = tenantMapper.selectById(id);
        if (Objects.isNull(tenantInDatabase)) {
            ThrowUtils.error(RCodeEnum.THE_MODIFIED_TARGET_DOES_NOT_EXIST);
        }
        TenantInfoVo tenantInfoVo = new TenantInfoVo();
        BeanUtils.copyProperties(tenantInDatabase, tenantInfoVo);
        return tenantInfoVo;
    }

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public void updateInfo(TenantUpdateInfoDto tenantUpdateInfoDto) {
        Tenant tenantInDatabase = tenantMapper.selectById(tenantUpdateInfoDto.getId());
        if (Objects.isNull(tenantInDatabase)) {
            ThrowUtils.error(RCodeEnum.THE_MODIFIED_TARGET_DOES_NOT_EXIST);
        }
        if (Objects.equals(tenantUpdateInfoDto.getIp(), tenantInDatabase.getIp())
                && Objects.equals(tenantUpdateInfoDto.getPort(), tenantInDatabase.getPort())
                && Objects.equals(tenantUpdateInfoDto.getLevel(), tenantInDatabase.getLevel())
                && Objects.equals(tenantUpdateInfoDto.getDbIp(), tenantInDatabase.getDbIp())
                && Objects.equals(tenantUpdateInfoDto.getDbPort(), tenantInDatabase.getDbPort())) {
            ThrowUtils.error(RCodeEnum.THE_DATA_HAS_NOT_CHANGED);
        }
        Tenant tenant = new Tenant();
        BeanUtils.copyProperties(tenantUpdateInfoDto, tenant);
        int updateResult = tenantMapper.updateById(tenant);
        if (updateResult == 0) {
            ThrowUtils.error(RCodeEnum.DB_DATA_MODIFICATION_FAILED);
        }
        LambdaUpdateWrapper<Tenant> tenantLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        tenantLambdaUpdateWrapper.eq(Tenant::getStatus, CommonConstant.TENANT_STATUS_ENABLE);
        List<Tenant> tenants = tenantMapper.selectList(tenantLambdaUpdateWrapper);
        try {
            Boolean publishConfig = new NacosConfigUtils().publishYamlConfig(tenants);
            if (!publishConfig) {
                ThrowUtils.error(RCodeEnum.FAILED_TO_PUBLISH_NACOS_CONFIGURATION_FILE);
            }
        } catch (NacosException e) {
            ThrowUtils.error(RCodeEnum.FAILED_TO_PUBLISH_NACOS_CONFIGURATION_FILE);
        }
    }

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public void updateStatus(String id) {
        Tenant tenantInDatabase = tenantMapper.selectById(id);
        if (Objects.isNull(tenantInDatabase)) {
            ThrowUtils.error(RCodeEnum.THE_MODIFIED_TARGET_DOES_NOT_EXIST);
        }
        if (!(Objects.equals(tenantInDatabase.getStatus(), CommonConstant.TENANT_STATUS_ENABLE)
                || Objects.equals(tenantInDatabase.getStatus(), CommonConstant.TENANT_STATUS_DISABLE))) {
            ThrowUtils.error(RCodeEnum.THE_USER_STATUS_CANNOT_BE_CHANGED);
        }
        if (Objects.equals(tenantInDatabase.getStatus(), CommonConstant.TENANT_STATUS_ENABLE)) {
            tenantInDatabase.setStatus(CommonConstant.TENANT_STATUS_DISABLE);
            tenantMapper.updateById(tenantInDatabase);
            Token updateToken = new Token();
            updateToken.setStatus(CommonConstant.LOGIN_STATUS_DISABLE);
            LambdaUpdateWrapper<Token> tokenLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            tokenLambdaUpdateWrapper.eq(Token::getTenant, id);
            int updateResult = tokenMapper.update(updateToken, tokenLambdaUpdateWrapper);
            if (updateResult == 0) {
                ThrowUtils.error(RCodeEnum.DB_DATA_MODIFICATION_FAILED);
            }
            LambdaUpdateWrapper<Tenant> tenantLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            tenantLambdaUpdateWrapper.eq(Tenant::getStatus, CommonConstant.TENANT_STATUS_ENABLE);
            List<Tenant> tenants = tenantMapper.selectList(tenantLambdaUpdateWrapper);
            try {
                Boolean publishConfig = new NacosConfigUtils().publishYamlConfig(tenants);
                if (!publishConfig) {
                    ThrowUtils.error(RCodeEnum.FAILED_TO_PUBLISH_NACOS_CONFIGURATION_FILE);
                }
            } catch (NacosException e) {
                ThrowUtils.error(RCodeEnum.FAILED_TO_PUBLISH_NACOS_CONFIGURATION_FILE);
            }
            List<Token> loginTokens = tokenMapper.selectList(tokenLambdaUpdateWrapper);
            List<String> tokens = loginTokens.stream().map(token -> "token_" + token.getId()).collect(Collectors.toList());
            redisTemplate.delete(tokens);
        } else {
            tenantInDatabase.setStatus(CommonConstant.TENANT_STATUS_ENABLE);
            tenantMapper.updateById(tenantInDatabase);
            Token updateToken = new Token();
            updateToken.setStatus(CommonConstant.LOGIN_STATUS_ENABLE);
            LambdaUpdateWrapper<Token> tokenLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            tokenLambdaUpdateWrapper.eq(Token::getTenant, id);
            int updateResult = tokenMapper.update(updateToken, tokenLambdaUpdateWrapper);
            if (updateResult == 0) {
                ThrowUtils.error(RCodeEnum.DB_DATA_MODIFICATION_FAILED);
            }
            LambdaUpdateWrapper<Tenant> tenantLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            tenantLambdaUpdateWrapper.eq(Tenant::getStatus, CommonConstant.TENANT_STATUS_ENABLE);
            List<Tenant> tenants = tenantMapper.selectList(tenantLambdaUpdateWrapper);
            try {
                Boolean publishConfig = new NacosConfigUtils().publishYamlConfig(tenants);
                if (!publishConfig) {
                    ThrowUtils.error(RCodeEnum.FAILED_TO_PUBLISH_NACOS_CONFIGURATION_FILE);
                }
            } catch (NacosException e) {
                ThrowUtils.error(RCodeEnum.FAILED_TO_PUBLISH_NACOS_CONFIGURATION_FILE);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public void getPasswordEmail(TenantPasswordEmailDto tenantPasswordEmailDto) {
        LambdaQueryWrapper<Tenant> tenantLambdaQueryWrapper = new LambdaQueryWrapper<>();
        tenantLambdaQueryWrapper.eq(Tenant::getAccount, tenantPasswordEmailDto.getAccount());
        Tenant tenantInDatabase = tenantMapper.selectOne(tenantLambdaQueryWrapper);
        if (Objects.isNull(tenantInDatabase)) {
            ThrowUtils.error(RCodeEnum.CONTACT_YOUR_ADMINISTRATOR_TO_RESET_YOUR_PASSWORD);
        }
        if (!Objects.equals(tenantInDatabase.getEmail(), tenantPasswordEmailDto.getEmail())) {
            ThrowUtils.error(RCodeEnum.THE_MAILBOX_IS_NOT_BOUND_TO_THE_ACCOUNT);
        }
        Long ttl = redisTemplate.getExpire("find_password_code_" + tenantPasswordEmailDto.getAccount() + "_" + tenantPasswordEmailDto.getEmail());
        if (Objects.nonNull(ttl) && ttl > TimeUnit.MINUTES.toSeconds(9)) {
            ThrowUtils.error(RCodeEnum.DO_NOT_GET_VERIFICATION_CODES_FREQUENTLY);
        }
        String code = String.valueOf(RandomUtil.randomInt(1000000, 1999999)).substring(1);
        redisTemplate.opsForValue().set("find_password_code_" + tenantPasswordEmailDto.getAccount() + "_" + tenantPasswordEmailDto.getEmail(), code, Duration.ofMinutes(10));

        String to = tenantPasswordEmailDto.getEmail();
        String subject = "xxx科技有限公司平台信息反馈";
        String content = "如有疑问，请联系邮箱“wushiyuankeji@163.com”";
        String template = "<body>\n" +
                "<h1>xxx科技有限公司</h1>\n" +
                "<p style=\"font-size: x-large\">你的找回密码验证码为<b>" + code + "</b>，操作账号为“<b>" + tenantPasswordEmailDto.getAccount() + "</b>”。</p>\n" +
                "<p style=\"font-size: x-large\">此验证码十分钟内有效，请注意时间！</p>" +
                "<p><b>备注：</b>" + content + "</p>\n" +
                "</body>";
        HashMap<String, Object> rabbitMqResult = new HashMap<>();
        rabbitMqResult.put("to", to);
        rabbitMqResult.put("subject", subject);
        rabbitMqResult.put("content", template);
        noSingletonRabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                if (!ack) {
                    ThrowUtils.error(RCodeEnum.PROVIDER_TO_EXCHANGE_ERROR);
                }
            }
        });
        noSingletonRabbitTemplate.setReturnsCallback(new RabbitTemplate.ReturnsCallback() {
            @Override
            public void returnedMessage(ReturnedMessage returnedMessage) {
                ThrowUtils.error(RCodeEnum.EXCHANGE_TO_QUEUE_ERROR, returnedMessage.getMessage().toString());
            }
        });
        noSingletonRabbitTemplate.convertAndSend(RabbitMqConfig.MAIL_MODULE_HTML_EXCHANGE, "mail_module_html.TenantServiceImpl", JSON.toJSONString(rabbitMqResult));
    }

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public void findPassword(TenantFindPasswordDto tenantFindPasswordDto) {
        String codeInRedis = (String) redisTemplate.opsForValue().get("find_password_code_" + tenantFindPasswordDto.getAccount() + "_" + tenantFindPasswordDto.getEmail());
        if (!Objects.equals(tenantFindPasswordDto.getCode(), codeInRedis)) {
            ThrowUtils.error(RCodeEnum.The_verification_code_is_invalid);
        }
        LambdaQueryWrapper<Tenant> tenantLambdaQueryWrapper = new LambdaQueryWrapper<>();
        tenantLambdaQueryWrapper.eq(Tenant::getAccount, tenantFindPasswordDto.getAccount());
        Tenant tenantInDatabase = tenantMapper.selectOne(tenantLambdaQueryWrapper);
        if (Objects.equals(tenantInDatabase.getPassword(), tenantFindPasswordDto.getPassword())) {
            if (Objects.nonNull(redisTemplate.opsForValue().get("find_password_code_" + tenantFindPasswordDto.getAccount() + "_" + tenantFindPasswordDto.getEmail()))) {
                redisTemplate.delete("find_password_code_" + tenantFindPasswordDto.getAccount() + "_" + tenantFindPasswordDto.getEmail());
            }
            ThrowUtils.error(RCodeEnum.THE_UPDATE_PASSWORD_CANNOT_BE_THE_SAME_AS_THE_ORIGINAL_PASSWORD);
        }
        LambdaUpdateWrapper<Tenant> tenantLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        tenantLambdaUpdateWrapper
                .eq(Tenant::getId, tenantInDatabase.getId())
                .set(Tenant::getPassword, tenantFindPasswordDto.getPassword());
        int updateTenantResult = tenantMapper.update(null, tenantLambdaUpdateWrapper);
        if (updateTenantResult == 0) {
            ThrowUtils.error(RCodeEnum.DB_DATA_MODIFICATION_FAILED);
        }
        LambdaUpdateWrapper<Token> tokenLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        tokenLambdaUpdateWrapper.eq(Token::getAccount, tenantInDatabase.getAccount());
        Token tokenInDatabase = tokenMapper.selectOne(tokenLambdaUpdateWrapper);
        if (Objects.isNull(tokenInDatabase)) {
            ThrowUtils.error(RCodeEnum.USER_ACCOUNT_DOES_NOT_EXIST);
        }
        tokenLambdaUpdateWrapper.set(Token::getPassword, tenantFindPasswordDto.getPassword());
        int updateTokenResult = tokenMapper.update(null, tokenLambdaUpdateWrapper);
        if (updateTokenResult == 0) {
            ThrowUtils.error(RCodeEnum.DB_DATA_MODIFICATION_FAILED);
        }
        HashMap<String, Object> rabbitMqManagerResult = new HashMap<>();
        rabbitMqManagerResult.put("tid", tenantInDatabase.getId());
        rabbitMqManagerResult.put("password", tenantFindPasswordDto.getPassword());
        noSingletonRabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                if (!ack) {
                    ThrowUtils.error(RCodeEnum.PROVIDER_TO_EXCHANGE_ERROR);
                }
            }
        });
        noSingletonRabbitTemplate.setReturnsCallback(new RabbitTemplate.ReturnsCallback() {
            @Override
            public void returnedMessage(ReturnedMessage returnedMessage) {
                ThrowUtils.error(RCodeEnum.EXCHANGE_TO_QUEUE_ERROR, returnedMessage.getMessage().toString());
            }
        });
        noSingletonRabbitTemplate.convertAndSend(tenantInDatabase.getId() + "_manager_update_exchange", tenantInDatabase.getId() + "_manager_update.UpdateServiceImpl", JSON.toJSONString(rabbitMqManagerResult));
        if (Objects.nonNull(redisTemplate.opsForValue().get("token_" + tokenInDatabase.getId()))) {
            redisTemplate.delete("token_" + tokenInDatabase.getId());
        }
        if (Objects.nonNull(redisTemplate.opsForValue().get("find_password_code_" + tenantFindPasswordDto.getAccount() + "_" + tenantFindPasswordDto.getEmail()))) {
            redisTemplate.delete("find_password_code_" + tenantFindPasswordDto.getAccount() + "_" + tenantFindPasswordDto.getEmail());
        }
    }
}
