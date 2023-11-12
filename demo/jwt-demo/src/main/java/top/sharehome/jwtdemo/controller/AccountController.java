package top.sharehome.jwtdemo.controller;

import org.springframework.web.bind.annotation.*;
import top.sharehome.jwtdemo.entity.Response;
import top.sharehome.jwtdemo.entity.dto.LoginDto;
import top.sharehome.jwtdemo.service.AccountService;
import top.sharehome.jwtdemo.utils.BeanMetaDataUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 登陆，注册以及退出
 *
 * @author AntonyCheng
 * @since 2023/7/2 22:40:12
 */
@RestController
@RequestMapping("/account")
@CrossOrigin
public class AccountController {
    @Resource
    private AccountService accountService;

    @PostMapping("/login")
    public Response<String> login(@RequestBody LoginDto loginDto) {
        if (BeanMetaDataUtils.isAnyMetadataEmpty(loginDto)) {
            System.out.println("参数不足");
        }
        String loginResult = accountService.login(loginDto);
        return new Response<String>(200, "OK", loginResult);
    }

    @GetMapping("/logout")
    public Response<String> logout(HttpServletRequest request) {
        String logoutResult = accountService.logout(request);
        return new Response<String>(200, "OK", logoutResult);
    }



}
