package top.sharehome.jwtdemo.service.impl;

import cn.hutool.core.date.LocalDateTimeUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import top.sharehome.jwtdemo.entity.bean.Login;
import top.sharehome.jwtdemo.entity.dto.LoginDto;
import top.sharehome.jwtdemo.entity.vo.LoginVo;
import top.sharehome.jwtdemo.service.AccountService;
import top.sharehome.jwtdemo.utils.JwtUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Objects;

/**
 * @author AntonyCheng
 * @since 2023/7/2 23:03:25
 */
@Service
public class AccountServiceImpl implements AccountService {

    @Resource
    private JwtUtils jwtUtils;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    public static final String AUTHORIZATION_HEADER = "Authorization";


    @Override
    public String login(LoginDto loginDto) {
        System.out.println("正在查询LOGIN_TABLE...");
        Login login = new Login(1L, "admin", "admin", 1L, 2, 0, LocalDateTimeUtil.of(1), LocalDateTimeUtil.of(2), 0);
        if (!ObjectUtils.notEqual(login.getStatus(), 1)) {
            return "账号无权限访问！";
        }
        System.out.println("已经查询到loginUser = " + login);
        if (Objects.isNull(login)) {
            return "账号或者密码错误！";
        }
        if (ObjectUtils.notEqual(loginDto.getPassword(), login.getPassword())
                || ObjectUtils.notEqual(loginDto.getAccount(), login.getAccount())) {
            return "账号或者密码错误！";
        }
        LoginVo loginVo = new LoginVo();
        BeanUtils.copyProperties(login, loginVo);
        return jwtUtils.createLoginToken(loginVo);
    }

    @Override
    public String logout(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION_HEADER);
        Jws<Claims> jws = jwtUtils.getJwsInLoginToken(token);
        String loginId = String.valueOf(((LinkedHashMap) jws.getBody().get("loginBody")).get("id"));
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String redisLoginToken = valueOperations.get("login_" + loginId);
        Long redisTokenTime = jwtUtils.aesDecTimestamp((String) jwtUtils.getJwsInLoginToken(redisLoginToken).getHeader().get("sk"));
        Long userTokenTime = jwtUtils.aesDecTimestamp((String) jws.getHeader().get("sk"));
        if (Objects.isNull(redisLoginToken) || redisTokenTime > userTokenTime) {
            return "登陆身份已过期，请重新登录！";
        }
        redisTemplate.delete("login_" + loginId);
        return "退出成功！";
    }
}
