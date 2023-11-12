package top.sharehome.issuemodule.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.jsonwebtoken.JwsHeader;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.sharehome.issuemodule.common.constant.CommonConstant;
import top.sharehome.issuemodule.common.exception_handler.customize.CustomizeTransactionException;
import top.sharehome.issuemodule.common.response.RCodeEnum;
import top.sharehome.issuemodule.mapper.TokenMapper;
import top.sharehome.issuemodule.model.dto.token.TokenLoginDto;
import top.sharehome.issuemodule.model.entity.Token;
import top.sharehome.issuemodule.model.vo.token.TokenLoginVo;
import top.sharehome.issuemodule.service.TokenService;
import top.sharehome.issuemodule.utils.jwt.JwtUtils;
import top.sharehome.issuemodule.utils.throwException.ThrowUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 用户登陆相关接口Service实现类（用户登陆、用户登出等）
 *
 * @author AntonyCheng
 * @since 2023/7/7 15:41:24
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TokenServiceImpl extends ServiceImpl<TokenMapper, Token> implements TokenService {
    @Resource
    private JwtUtils jwtUtils;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private TokenMapper tokenMapper;

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public String login(TokenLoginDto tokenLoginDto) {
        LambdaQueryWrapper<Token> tokenLambdaQueryWrapper = new LambdaQueryWrapper<>();
        tokenLambdaQueryWrapper.eq(Token::getAccount, tokenLoginDto.getAccount());
        Token tokenInDataBase = tokenMapper.selectOne(tokenLambdaQueryWrapper);
        if (Objects.isNull(tokenInDataBase)) {
            ThrowUtils.error(RCodeEnum.USER_ACCOUNT_DOES_NOT_EXIST,
                    String.format("用户“%s”账号不存在", tokenLoginDto.getAccount()));
        }
        if (tokenInDataBase.getStatus() == CommonConstant.LOGIN_STATUS_DISABLE) {
            ThrowUtils.error(RCodeEnum.USER_ACCOUNT_BANNED,
                    String.format("用户“%s”账户已被禁用", tokenLoginDto.getAccount()));
        }
        if (!Objects.equals(tokenLoginDto.getPassword(), tokenInDataBase.getPassword())) {
            ThrowUtils.error(RCodeEnum.WRONG_USER_PASSWORD,
                    String.format("用户“%s”账户密码错误", tokenLoginDto.getAccount()));
        }
        TokenLoginVo tokenLoginVo = new TokenLoginVo();
        BeanUtils.copyProperties(tokenInDataBase, tokenLoginVo);
        return jwtUtils.createLoginToken(tokenLoginVo);
    }

    @Override
    public void logout(HttpServletRequest request) {
        String token = request.getHeader(CommonConstant.TOKEN_HEADER);
        JwsHeader header = jwtUtils.getHeaderInLoginToken(token);
        Long tokenId = jwtUtils.aesDecLong((String) header.get("uid"));
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String redisLoginToken = valueOperations.get("token_" + tokenId);
        if (Objects.isNull(redisLoginToken)) {
            ThrowUtils.error(RCodeEnum.USER_LOGIN_HAS_EXPIRED);
        }
        Long redisTokenTime = jwtUtils.aesDecLong((String) jwtUtils.getJwsInLoginToken(redisLoginToken).getHeader().get("sk"));
        Long userTokenTime = jwtUtils.aesDecLong((String) header.get("sk"));
        if (redisTokenTime > userTokenTime) {
            ThrowUtils.error(RCodeEnum.USER_LOGIN_HAS_EXPIRED);
        }
        redisTemplate.delete("token_" + tokenId);
    }
}
