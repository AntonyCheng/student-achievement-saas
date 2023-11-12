package top.sharehome.issuemodule.service.micro.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import top.sharehome.issuemodule.common.constant.CommonConstant;
import top.sharehome.issuemodule.common.exception_handler.customize.CustomizeReturnException;
import top.sharehome.issuemodule.common.exception_handler.customize.CustomizeTransactionException;
import top.sharehome.issuemodule.common.response.R;
import top.sharehome.issuemodule.common.response.RCodeEnum;
import top.sharehome.issuemodule.mapper.TenantMapper;
import top.sharehome.issuemodule.mapper.TokenMapper;
import top.sharehome.issuemodule.mapper.UpdateMapper;
import top.sharehome.issuemodule.model.entity.Tenant;
import top.sharehome.issuemodule.model.entity.Token;
import top.sharehome.issuemodule.model.entity.Update;
import top.sharehome.issuemodule.service.TokenService;
import top.sharehome.issuemodule.service.micro.ManagerMicroService;
import top.sharehome.issuemodule.utils.throwException.ThrowUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 用户信息录入Service实现类（微服务）
 *
 * @author AntonyCheng
 * @since 2023/7/17 21:24:25
 */
@DubboService
public class ManagerMicroServiceImpl implements ManagerMicroService {

    @Resource
    private TokenMapper tokenMapper;

    @Resource
    private TokenService tokenService;

    @Resource
    private TenantMapper tenantMapper;

    @Resource
    private UpdateMapper updateMapper;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource(name = "noSingletonRabbitTemplate")
    private RabbitTemplate noSingletonRabbitTemplate;

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public Tenant initManager(Long managerTenantId) throws CustomizeReturnException {
        Tenant tenant = tenantMapper.selectById(managerTenantId);
        return Objects.nonNull(tenant) ? tenant : null;
    }

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public void addUpdateManagerInfo(Update managerUpdate) throws CustomizeReturnException {
        LambdaQueryWrapper<Update> updateLambdaQueryWrapper = new LambdaQueryWrapper<>();
        updateLambdaQueryWrapper
                .eq(Update::getTenant, managerUpdate.getTenant())
                .eq(Update::getStatus, CommonConstant.MANAGER_INFO_UPDATE_STATUS_CENSOR);
        Update updateInDatabase = updateMapper.selectOne(updateLambdaQueryWrapper);
        if (Objects.nonNull(updateInDatabase)) {
            ThrowUtils.error("rMessage",RCodeEnum.USER_INFORMATION_IS_STILL_UNDER_REVIEW.getMessage(),
                    RCodeEnum.USER_INFORMATION_IS_STILL_UNDER_REVIEW);
        }
        LambdaUpdateWrapper<Tenant> tenantLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        tenantLambdaUpdateWrapper.ne(Tenant::getId, managerUpdate.getTenant())
                .eq(Tenant::getAccount, managerUpdate.getAccount());
        if (tenantMapper.exists(tenantLambdaUpdateWrapper)) {
            ThrowUtils.error("rMessage",RCodeEnum.USERNAME_ALREADY_EXISTS.getMessage(),
                    RCodeEnum.USERNAME_ALREADY_EXISTS);
        }
        int insertResult = updateMapper.insert(managerUpdate);
        if (insertResult == 0) {
            ThrowUtils.error("rMessage",RCodeEnum.DB_DATA_MODIFICATION_FAILED.getMessage(),
                    RCodeEnum.DB_DATA_MODIFICATION_FAILED);
        }
    }

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public void updatePassword(Tenant tenantUpdatePassword) throws CustomizeReturnException {
        Tenant tenantInDatabase = tenantMapper.selectById(tenantUpdatePassword.getId());
        if (Objects.isNull(tenantInDatabase)) {
            ThrowUtils.error("rMessage",RCodeEnum.USER_ACCOUNT_DOES_NOT_EXIST.getMessage(),
                    RCodeEnum.USER_ACCOUNT_DOES_NOT_EXIST);
        }
        LambdaUpdateWrapper<Tenant> tenantLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        tenantLambdaUpdateWrapper
                .eq(Tenant::getId, tenantUpdatePassword.getId())
                .set(Tenant::getPassword, tenantUpdatePassword.getPassword());
        int updateTenantResult = tenantMapper.update(null, tenantLambdaUpdateWrapper);
        if (updateTenantResult == 0) {
            ThrowUtils.error("rMessage",RCodeEnum.DB_DATA_MODIFICATION_FAILED.getMessage(),
                    RCodeEnum.DB_DATA_MODIFICATION_FAILED);
        }
        LambdaUpdateWrapper<Token> tokenLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        tokenLambdaUpdateWrapper.eq(Token::getAccount, tenantInDatabase.getAccount());
        Token tokenInDatabase = tokenMapper.selectOne(tokenLambdaUpdateWrapper);
        if (Objects.isNull(tokenInDatabase)) {
            ThrowUtils.error("rMessage",RCodeEnum.USER_ACCOUNT_DOES_NOT_EXIST.getMessage(),
                    RCodeEnum.USER_ACCOUNT_DOES_NOT_EXIST);
        }
        tokenLambdaUpdateWrapper.set(Token::getPassword, tenantUpdatePassword.getPassword());
        int updateTokenResult = tokenMapper.update(null, tokenLambdaUpdateWrapper);
        if (updateTokenResult == 0) {
            ThrowUtils.error("rMessage",RCodeEnum.DB_DATA_MODIFICATION_FAILED.getMessage(),
                    RCodeEnum.DB_DATA_MODIFICATION_FAILED);
        }

        HashMap<String, Object> rabbitMqManagerResult = new HashMap<>();
        rabbitMqManagerResult.put("tid", tenantUpdatePassword.getId());
        rabbitMqManagerResult.put("password", tenantUpdatePassword.getPassword());
        noSingletonRabbitTemplate.convertAndSend(tenantUpdatePassword.getId() + "_manager_update_exchange", tenantUpdatePassword.getId() + "_manager_update.UpdateServiceImpl", JSON.toJSONString(rabbitMqManagerResult));

        if (Objects.nonNull(redisTemplate.opsForValue().get("token_" + tokenInDatabase.getId()))) {
            redisTemplate.delete("token_" + tokenInDatabase.getId());
        }
    }

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public void addSinglePerson(Token signUser) throws CustomizeReturnException {
        LambdaQueryWrapper<Token> tokenLambdaQueryWrapper = new LambdaQueryWrapper<>();
        tokenLambdaQueryWrapper.eq(Token::getAccount, signUser.getAccount());
        Token tokenInDatabase = tokenMapper.selectOne(tokenLambdaQueryWrapper);
        if (Objects.nonNull(tokenInDatabase)) {
            ThrowUtils.error("rMessage",signUser.getAccount() + RCodeEnum.USERNAME_ALREADY_EXISTS.getMessage(),
                    RCodeEnum.USERNAME_ALREADY_EXISTS);
        }
        int insertResult = tokenMapper.insert(signUser);
        if (insertResult == 0) {
            ThrowUtils.error("rMessage",RCodeEnum.DB_DATA_ADDITION_FAILED.getMessage(),
                    RCodeEnum.DB_DATA_ADDITION_FAILED);
        }
    }

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public void addMultiplePerson(List<Token> multipleUser) throws CustomizeReturnException {
        multipleUser.forEach(token -> {
            LambdaQueryWrapper<Token> tokenLambdaQueryWrapper = new LambdaQueryWrapper<>();
            tokenLambdaQueryWrapper.eq(Token::getAccount, token.getAccount());
            if (tokenMapper.exists(tokenLambdaQueryWrapper)) {
                ThrowUtils.error("rMessage",token.getAccount() + RCodeEnum.USERNAME_ALREADY_EXISTS.getMessage(),
                        RCodeEnum.USERNAME_ALREADY_EXISTS);
            }
        });
        if (!tokenService.saveBatch(multipleUser)) {
            ThrowUtils.error("rMessage",RCodeEnum.DB_DATA_ADDITION_FAILED.getMessage(),
                    RCodeEnum.DB_DATA_ADDITION_FAILED);
        }
    }

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public void deleteSinglePerson(Token singleUser) throws CustomizeReturnException {
        LambdaQueryWrapper<Token> tokenLambdaQueryWrapper = new LambdaQueryWrapper<>();
        tokenLambdaQueryWrapper.eq(Token::getAccount, singleUser.getAccount());
        Token tokenInDatabase = tokenMapper.selectOne(tokenLambdaQueryWrapper);
        if (Objects.isNull(tokenInDatabase)) {
            ThrowUtils.error("rMessage",RCodeEnum.USER_ACCOUNT_DOES_NOT_EXIST.getMessage(),
                    RCodeEnum.USER_ACCOUNT_DOES_NOT_EXIST);
        }
        if (!Objects.equals(tokenInDatabase.getTenant(), singleUser.getTenant())) {
            ThrowUtils.error("rMessage",RCodeEnum.ACCESS_UNAUTHORIZED.getMessage(),
                    RCodeEnum.ACCESS_UNAUTHORIZED);
        }
        int deleteResult = tokenMapper.delete(tokenLambdaQueryWrapper);
        if (deleteResult == 0) {
            ThrowUtils.error("rMessage",RCodeEnum.DB_DATA_DELETION_FAILED.getMessage(),
                    RCodeEnum.DB_DATA_DELETION_FAILED);
        }
        redisTemplate.delete("token_" + tokenInDatabase.getId());
    }

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public void deleteMultiplePerson(List<Token> multipleUsers) throws CustomizeReturnException {
        List<Long> ids = multipleUsers.stream().map(account -> {
            LambdaQueryWrapper<Token> tokenLambdaQueryWrapper = new LambdaQueryWrapper<>();
            tokenLambdaQueryWrapper.eq(Token::getAccount, account.getAccount());
            Token tokenInDatabase = tokenMapper.selectOne(tokenLambdaQueryWrapper);
            if (Objects.isNull(tokenInDatabase)) {
                ThrowUtils.error("rMessage",RCodeEnum.USER_ACCOUNT_DOES_NOT_EXIST.getMessage(),
                        RCodeEnum.USER_ACCOUNT_DOES_NOT_EXIST);
            }
            if (!Objects.equals(tokenInDatabase.getTenant(), account.getTenant())) {
                ThrowUtils.error("rMessage",RCodeEnum.ACCESS_UNAUTHORIZED.getMessage(),
                        RCodeEnum.ACCESS_UNAUTHORIZED);
            }
            return tokenInDatabase.getId();
        }).collect(Collectors.toList());
        if (!tokenService.removeBatchByIds(ids)) {
            ThrowUtils.error("rMessage",RCodeEnum.DB_DATA_DELETION_FAILED.getMessage(),
                    RCodeEnum.DB_DATA_DELETION_FAILED);
        }
        List<String> keys = ids.stream().map(id -> "token_" + id).collect(Collectors.toList());
        redisTemplate.delete(keys);
    }

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public void updateSinglePersonAccount(Map<String, Object> oldAndNewMap) throws CustomizeReturnException {
        Token oldToken = (Token) oldAndNewMap.get("oldToken");
        Token tokenInDatabase = tokenMapper.selectOne(new LambdaQueryWrapper<Token>().eq(Token::getAccount, oldToken.getAccount()));
        if (Objects.isNull(tokenInDatabase)) {
            ThrowUtils.error("rMessage",RCodeEnum.USER_ACCOUNT_DOES_NOT_EXIST.getMessage(),
                    RCodeEnum.USER_ACCOUNT_DOES_NOT_EXIST);
        }
        if (!Objects.equals(oldToken.getTenant(), tokenInDatabase.getTenant())) {
            ThrowUtils.error("rMessage",RCodeEnum.ACCESS_UNAUTHORIZED.getMessage(),
                    RCodeEnum.ACCESS_UNAUTHORIZED);
        }
        String newAccount = (String) oldAndNewMap.get("newAccount");
        if (tokenMapper.exists(new LambdaQueryWrapper<Token>().eq(Token::getAccount, newAccount))) {
            ThrowUtils.error("rMessage",RCodeEnum.USERNAME_ALREADY_EXISTS.getMessage(),
                    RCodeEnum.USERNAME_ALREADY_EXISTS);
        }
        LambdaUpdateWrapper<Token> tokenLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        tokenLambdaUpdateWrapper.set(Token::getAccount, newAccount)
                .eq(Token::getAccount, oldToken.getAccount());
        int updateResult = tokenMapper.update(null, tokenLambdaUpdateWrapper);
        if (updateResult == 0) {
            ThrowUtils.error("rMessage",RCodeEnum.DB_DATA_MODIFICATION_FAILED.getMessage(),
                    RCodeEnum.DB_DATA_MODIFICATION_FAILED);
        }
        redisTemplate.delete("token_" + tokenInDatabase.getId());
    }

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public void resetSinglePersonPassword(Token singleUser) throws CustomizeReturnException {
        LambdaQueryWrapper<Token> tokenLambdaQueryWrapper = new LambdaQueryWrapper<>();
        tokenLambdaQueryWrapper.eq(Token::getAccount, singleUser.getAccount());
        Token tokenInDatabase = tokenMapper.selectOne(tokenLambdaQueryWrapper);
        if (Objects.isNull(tokenInDatabase)) {
            ThrowUtils.error("rMessage",RCodeEnum.USER_ACCOUNT_DOES_NOT_EXIST.getMessage(),
                    RCodeEnum.USER_ACCOUNT_DOES_NOT_EXIST);
        }
        if (!Objects.equals(tokenInDatabase.getTenant(), singleUser.getTenant())) {
            ThrowUtils.error("rMessage",RCodeEnum.ACCESS_UNAUTHORIZED.getMessage(),
                    RCodeEnum.ACCESS_UNAUTHORIZED);
        }
        singleUser.setId(tokenInDatabase.getId());
        int updateResult = tokenMapper.updateById(singleUser);
        if (updateResult == 0) {
            ThrowUtils.error("rMessage",RCodeEnum.DB_DATA_MODIFICATION_FAILED.getMessage(),
                    RCodeEnum.DB_DATA_MODIFICATION_FAILED);
        }
        redisTemplate.delete("token_" + tokenInDatabase.getId());
    }

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public void resetMultiplePersonPassword(List<Token> multipleUsers) throws CustomizeReturnException {
        List<Token> tokens = multipleUsers.stream().peek(account -> {
            LambdaQueryWrapper<Token> tokenLambdaQueryWrapper = new LambdaQueryWrapper<>();
            tokenLambdaQueryWrapper.eq(Token::getAccount, account.getAccount());
            Token tokenInDatabase = tokenMapper.selectOne(tokenLambdaQueryWrapper);
            if (Objects.isNull(tokenInDatabase)) {
                ThrowUtils.error("rMessage",RCodeEnum.USER_ACCOUNT_DOES_NOT_EXIST.getMessage(),
                        RCodeEnum.USER_ACCOUNT_DOES_NOT_EXIST);
            }
            if (!Objects.equals(tokenInDatabase.getTenant(), account.getTenant())) {
                ThrowUtils.error("rMessage",RCodeEnum.ACCESS_UNAUTHORIZED.getMessage(),
                        RCodeEnum.ACCESS_UNAUTHORIZED);
            }
            account.setId(tokenInDatabase.getId());
        }).collect(Collectors.toList());
        if (!tokenService.updateBatchById(tokens)) {
            ThrowUtils.error("rMessage",RCodeEnum.DB_DATA_MODIFICATION_FAILED.getMessage(),
                    RCodeEnum.DB_DATA_MODIFICATION_FAILED);
        }
        List<String> keys = tokens.stream().map(token -> "token_" + token.getId()).collect(Collectors.toList());
        redisTemplate.delete(keys);
    }
}
