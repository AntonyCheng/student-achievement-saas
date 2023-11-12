package top.sharehome.issuemodule.service.impl;

import com.alibaba.fastjson2.JSON;
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
import top.sharehome.issuemodule.common.exception_handler.customize.CustomizeReturnException;
import top.sharehome.issuemodule.common.exception_handler.customize.CustomizeTransactionException;
import top.sharehome.issuemodule.common.response.RCodeEnum;
import top.sharehome.issuemodule.config.RabbitMqConfig;
import top.sharehome.issuemodule.mapper.TenantMapper;
import top.sharehome.issuemodule.mapper.TokenMapper;
import top.sharehome.issuemodule.mapper.UpdateMapper;
import top.sharehome.issuemodule.model.dto.update.UpdateCensorDto;
import top.sharehome.issuemodule.model.dto.update.UpdatePageDto;
import top.sharehome.issuemodule.model.entity.Tenant;
import top.sharehome.issuemodule.model.entity.Token;
import top.sharehome.issuemodule.model.entity.Update;
import top.sharehome.issuemodule.model.vo.update.UpdatePageVo;
import top.sharehome.issuemodule.service.UpdateService;
import top.sharehome.issuemodule.utils.meta.BeanMetaDataUtils;
import top.sharehome.issuemodule.utils.throwException.ThrowUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UpdateServiceImpl extends ServiceImpl<UpdateMapper, Update> implements UpdateService {

    @Resource
    private UpdateMapper updateMapper;

    @Resource
    private TenantMapper tenantMapper;

    @Resource
    private TokenMapper tokenMapper;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource(name = "noSingletonRabbitTemplate")
    private RabbitTemplate noSingletonRabbitTemplate;

    @Override
    @Transactional(rollbackFor = CustomizeReturnException.class)
    public void censor(UpdateCensorDto updateCensorDto) {
        Update updateInDatabase = updateMapper.selectById(updateCensorDto.getId());
        if (Objects.isNull(updateInDatabase)) {
            ThrowUtils.error(RCodeEnum.DATA_DOES_NOT_EXIST);
        }
        if (updateInDatabase.getStatus() != CommonConstant.MANAGER_INFO_UPDATE_STATUS_CENSOR) {
            ThrowUtils.error(RCodeEnum.DATA_HAS_BEEN_REVIEWED);
        }
        LambdaUpdateWrapper<Tenant> tenantLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        tenantLambdaUpdateWrapper.ne(Tenant::getId, updateInDatabase.getTenant())
                .eq(Tenant::getAccount, updateInDatabase.getAccount());
        if (tenantMapper.exists(tenantLambdaUpdateWrapper)) {
            ThrowUtils.error(RCodeEnum.USERNAME_ALREADY_EXISTS);
        }
        LambdaUpdateWrapper<Update> updateLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        updateLambdaUpdateWrapper.eq(Update::getId, updateCensorDto.getId());
        Update update = new Update();
        if (Objects.equals(updateCensorDto.getResult(), CommonConstant.CENSOR_PASS)) {
            update.setStatus(CommonConstant.MANAGER_INFO_UPDATE_STATUS_ENABLE);
            int updateUpdateResult = updateMapper.update(update, updateLambdaUpdateWrapper);
            if (updateUpdateResult == 0) {
                ThrowUtils.error(RCodeEnum.DB_DATA_MODIFICATION_FAILED);
            }
            Tenant tenantInDatabase = tenantMapper.selectById(updateInDatabase.getTenant());
            if (Objects.isNull(tenantInDatabase)) {
                ThrowUtils.error(RCodeEnum.DATA_DOES_NOT_EXIST);
            }
            String tenantOriginalAccount = tenantInDatabase.getAccount();
            tenantInDatabase.setAccount(updateInDatabase.getAccount());
            tenantInDatabase.setName(updateInDatabase.getName());
            tenantInDatabase.setGender(updateInDatabase.getGender());
            tenantInDatabase.setEmail(updateInDatabase.getEmail());
            tenantInDatabase.setPicture(updateInDatabase.getPicture());
            tenantInDatabase.setLevel(updateInDatabase.getLevel());
            int updateTenantResult = tenantMapper.updateById(tenantInDatabase);
            if (updateTenantResult == 0) {
                ThrowUtils.error(RCodeEnum.DB_DATA_MODIFICATION_FAILED);
            }
            LambdaUpdateWrapper<Token> tokenLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            tokenLambdaUpdateWrapper.eq(Token::getAccount, tenantOriginalAccount);
            Token tokenInDatabase = tokenMapper.selectOne(tokenLambdaUpdateWrapper);
            tokenInDatabase.setAccount(updateInDatabase.getAccount());
            int updateTokenResult = tokenMapper.updateById(tokenInDatabase);
            if (updateTokenResult == 0) {
                ThrowUtils.error(RCodeEnum.DB_DATA_MODIFICATION_FAILED);
            }

            HashMap<String, Object> rabbitMqManagerResult = new HashMap<>();
            rabbitMqManagerResult.put("tid", updateInDatabase.getTenant());
            rabbitMqManagerResult.put("account", updateInDatabase.getAccount());
            rabbitMqManagerResult.put("name", updateInDatabase.getName());
            rabbitMqManagerResult.put("gender", updateInDatabase.getGender());
            rabbitMqManagerResult.put("email", updateInDatabase.getEmail());
            rabbitMqManagerResult.put("picture", updateInDatabase.getPicture());
            rabbitMqManagerResult.put("level", updateInDatabase.getLevel());
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
            noSingletonRabbitTemplate.convertAndSend(updateInDatabase.getTenant() + "_manager_update_exchange", updateInDatabase.getTenant() + "_manager_update.UpdateServiceImpl", JSON.toJSONString(rabbitMqManagerResult));

            if (Objects.nonNull(redisTemplate.opsForValue().get("token_" + tokenInDatabase.getId()))) {
                redisTemplate.delete("token_" + tokenInDatabase.getId());
            }

            String to = updateInDatabase.getEmail();
            String subject = "xxx科技有限公司平台信息反馈";
            String content = StringUtils.isEmpty(updateCensorDto.getContent()) ?
                    "如有疑问，请联系邮箱“wushiyuankeji@163.com”" : updateCensorDto.getContent();
            String template = "<body>\n" +
                    "<h1>xxx科技有限公司</h1>\n" +
                    "<p style=\"font-size: x-large\">恭喜您！您的信息更改申请已经审核通过，审核后账号为“<b>" + updateInDatabase.getAccount() + "</b>”。</p>\n" +
                    "<p style=\"font-size: x-large\">现已经刷新登陆状态，请输入原有密码重新登陆，请点击<b><a href=\"http://localhost:80\">这里</a></b>进行访问！</p>\n" +
                    "<p><b>备注：</b>" + content + "</p>\n" +
                    "</body>";
            HashMap<String, Object> rabbitMqMailResult = new HashMap<>();
            rabbitMqMailResult.put("to", to);
            rabbitMqMailResult.put("subject", subject);
            rabbitMqMailResult.put("content", template);
            noSingletonRabbitTemplate.convertAndSend(RabbitMqConfig.MAIL_MODULE_HTML_EXCHANGE, "mail_module_html.UpdateServiceImpl", JSON.toJSONString(rabbitMqMailResult));
        } else {
            update.setStatus(CommonConstant.MANAGER_INFO_UPDATE_STATUS_CENSOR_DISABLE);
            int updateUpdateResult = updateMapper.update(update, updateLambdaUpdateWrapper);
            if (updateUpdateResult == 0) {
                ThrowUtils.error(RCodeEnum.DB_DATA_MODIFICATION_FAILED);
            }
            String to = updateInDatabase.getEmail();
            String subject = "xxx科技有限公司平台信息反馈";
            String content = StringUtils.isEmpty(updateCensorDto.getContent()) ?
                    "如有疑问，请联系邮箱“wushiyuankeji@163.com”" : updateCensorDto.getContent();
            String template = "<body>\n" +
                    "<h1>xxx科技有限公司</h1>\n" +
                    "<p style=\"font-size: x-large\">很遗憾！您的信息更改申请审核未通过，未通过审核的账号为“<b>" + updateInDatabase.getAccount() + "</b>”。</p>\n" +
                    "<p style=\"font-size: x-large\">具体原因请见备注...</p>\n" +
                    "<p><b>备注：</b>" + content + "</p>\n" +
                    "</body>";
            HashMap<String, Object> rabbitMqMailResult = new HashMap<>();
            rabbitMqMailResult.put("to", to);
            rabbitMqMailResult.put("subject", subject);
            rabbitMqMailResult.put("content", template);
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
            noSingletonRabbitTemplate.convertAndSend(RabbitMqConfig.MAIL_MODULE_HTML_EXCHANGE, "mail_module_html.UpdateServiceImpl", JSON.toJSONString(rabbitMqMailResult));
        }
    }

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public Page<UpdatePageVo> pageUpdate(Integer current, Integer pageSize, UpdatePageDto updatePageDto) {
        // 创建原始分页数据以及返回分页数据
        Page<Update> page = new Page<>(current, pageSize);
        Page<UpdatePageVo> returnResult = new Page<>(current, pageSize);

        // 过滤分页对象
        LambdaQueryWrapper<Update> updateLambdaQueryWrapper = new LambdaQueryWrapper<>();
        updateLambdaQueryWrapper
                .orderByDesc(Update::getCreateTime)
                .orderByAsc(Update::getStatus);

        // 当不存在模糊查询时的分页操作
        if (BeanMetaDataUtils.isAllMetadataEmpty(updatePageDto)) {
            this.page(page, updateLambdaQueryWrapper);
            BeanUtils.copyProperties(page, returnResult, "records");
            List<UpdatePageVo> pageVoList = page.getRecords().stream().map(record -> {
                UpdatePageVo updatePageVo = new UpdatePageVo();
                BeanUtils.copyProperties(record, updatePageVo);
                if (record.getStatus() != CommonConstant.MANAGER_INFO_UPDATE_STATUS_CENSOR) {
                    return updatePageVo;
                }
                Tenant tenantInDatabase = tenantMapper.selectById(record.getTenant());
                updatePageVo.setBeforeAccount(tenantInDatabase.getAccount());
                updatePageVo.setBeforeName(tenantInDatabase.getName());
                updatePageVo.setBeforeGender(tenantInDatabase.getGender());
                updatePageVo.setBeforeEmail(tenantInDatabase.getEmail());
                updatePageVo.setBeforePicture(tenantInDatabase.getPicture());
                updatePageVo.setBeforeLevel(tenantInDatabase.getLevel());
                updatePageVo.setSchoolCollege(tenantInDatabase.getSchool() + tenantInDatabase.getCollege());
                return updatePageVo;
            }).collect(Collectors.toList());
            returnResult.setRecords(pageVoList);
            return returnResult;
        }
        updateLambdaQueryWrapper
                .like(StringUtils.isNotEmpty(updatePageDto.getAccount()), Update::getAccount, updatePageDto.getAccount())
                .like(StringUtils.isNotEmpty(updatePageDto.getName()), Update::getName, updatePageDto.getName())
                .eq(Objects.nonNull(updatePageDto.getGender()), Update::getGender, updatePageDto.getGender())
                .like(StringUtils.isNotEmpty(updatePageDto.getEmail()), Update::getEmail, updatePageDto.getEmail())
                .eq(Objects.nonNull(updatePageDto.getLevel()), Update::getLevel, updatePageDto.getLevel())
                .eq(Objects.nonNull(updatePageDto.getStatus()), Update::getStatus, updatePageDto.getStatus());
        this.page(page, updateLambdaQueryWrapper);
        BeanUtils.copyProperties(page, returnResult, "records");
        List<UpdatePageVo> pageVoList = page.getRecords().stream().map(record -> {
            UpdatePageVo updatePageVo = new UpdatePageVo();
            BeanUtils.copyProperties(record, updatePageVo);
            if (record.getStatus() != CommonConstant.MANAGER_INFO_UPDATE_STATUS_CENSOR) {
                return updatePageVo;
            }
            Tenant tenantInDatabase = tenantMapper.selectById(record.getTenant());
            updatePageVo.setBeforeAccount(tenantInDatabase.getAccount());
            updatePageVo.setBeforeName(tenantInDatabase.getName());
            updatePageVo.setBeforeGender(tenantInDatabase.getGender());
            updatePageVo.setBeforeEmail(tenantInDatabase.getEmail());
            updatePageVo.setBeforePicture(tenantInDatabase.getPicture());
            updatePageVo.setBeforeLevel(tenantInDatabase.getLevel());
            updatePageVo.setSchoolCollege(tenantInDatabase.getSchool() + tenantInDatabase.getCollege());
            return updatePageVo;
        }).collect(Collectors.toList());
        returnResult.setRecords(pageVoList);
        return returnResult;
    }

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public void delete(Long id) {
        Update updateInDatabase = updateMapper.selectById(id);
        if (Objects.isNull(updateInDatabase)) {
            ThrowUtils.error(RCodeEnum.THE_DELETED_TARGET_DOES_NOT_EXIST);
        }
        if (updateInDatabase.getStatus() == CommonConstant.MANAGER_INFO_UPDATE_STATUS_CENSOR) {
            ThrowUtils.error(RCodeEnum.USER_ACCOUNT_IS_CENSOR);
        }
        int deleteResult = updateMapper.deleteById(id);
        if (deleteResult == 0) {
            ThrowUtils.error(RCodeEnum.DB_DATA_DELETION_FAILED);
        }
    }

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public void deleteBatch(List<Long> ids) {
        List<Update> updatesInDatabase = updateMapper.selectBatchIds(ids);
        if (updatesInDatabase.size() != ids.size()) {
            ThrowUtils.error(RCodeEnum.THE_DELETED_TARGET_DOES_NOT_EXIST);
        }
        for (Update updateInDatabase : updatesInDatabase) {
            if (updateInDatabase.getStatus() == CommonConstant.MANAGER_INFO_UPDATE_STATUS_CENSOR) {
                ThrowUtils.error(RCodeEnum.USER_ACCOUNT_IS_CENSOR);
            }
        }
        int deleteResult = updateMapper.deleteBatchIds(ids);
        if (deleteResult == 0) {
            ThrowUtils.error(RCodeEnum.DB_DATA_DELETION_FAILED);
        }
    }
}
