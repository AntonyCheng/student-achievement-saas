package top.sharehome.jwtdemo.service;

import top.sharehome.jwtdemo.entity.dto.LoginDto;

import javax.servlet.http.HttpServletRequest;

/**
 * 登陆，注册和退出服务
 *
 * @author AntonyCheng
 * @since 2023/7/2 22:52:03
 */

public interface AccountService {
    String login(LoginDto loginDto);

    String logout(HttpServletRequest request);
}
