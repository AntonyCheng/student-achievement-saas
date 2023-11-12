package top.sharehome.issuemodule.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.sharehome.issuemodule.model.dto.token.TokenLoginDto;
import top.sharehome.issuemodule.model.entity.Token;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户登陆相关接口Service（用户登陆、用户登出等）
 *
 * @author AntonyCheng
 * @since 2023/7/7 15:41:24
 */
public interface TokenService extends IService<Token> {
    /**
     * 登陆接口
     *
     * @param tokenLoginDto 获取用户的账号密码
     * @return token
     */
    String login(TokenLoginDto tokenLoginDto);

    /**
     * 退出接口
     *
     * @param request 获取请求头
     */
    void logout(HttpServletRequest request);
}
