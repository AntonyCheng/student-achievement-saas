package top.sharehome.issuemodule.controller;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import top.sharehome.issuemodule.common.response.R;
import top.sharehome.issuemodule.common.response.RCodeEnum;
import top.sharehome.issuemodule.model.dto.token.TokenLoginDto;
import top.sharehome.issuemodule.service.TokenService;
import top.sharehome.issuemodule.utils.meta.BeanMetaDataUtils;
import top.sharehome.issuemodule.utils.throwException.ThrowUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 登陆相关接口（登陆、登出等）
 *
 * @author AntonyCheng
 * @since 2023/7/7 15:41:24
 */
@RestController
@RequestMapping("/token")
@CrossOrigin
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TokenController {
    @Resource
    private TokenService tokenService;

    /**
     * 用户登陆接口
     *
     * @param tokenLoginDto 获取用户的账号密码
     * @return 返回登陆结果
     */
    @PostMapping("/login")
    public R<String> login(@RequestBody TokenLoginDto tokenLoginDto) {
        //校验参数
        verifyLoginParams(tokenLoginDto);
        String loginToken = tokenService.login(tokenLoginDto);
        return R.success(loginToken, "登陆成功");
    }

    /**
     * 用户退出接口
     *
     * @param request 获取请求头
     * @return 返回退出结果
     */
    @DeleteMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        tokenService.logout(request);
        return R.success("退出成功");
    }

    /**
     * 校验登陆参数
     */
    private void verifyLoginParams(TokenLoginDto tokenLoginDto) {
        // 判空
        if (BeanMetaDataUtils.isAnyMetadataEmpty(tokenLoginDto)) {
            ThrowUtils.error(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY, "账号或密码空缺");
        }
        if (tokenLoginDto.getAccount().contains(" ")
                || tokenLoginDto.getPassword().contains(" ")) {
            ThrowUtils.error(RCodeEnum.ACCOUNT_OR_PASSWORDS_CANNOT_CONTAIN_SPACES);
        }
    }
}
