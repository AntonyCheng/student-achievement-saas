package top.sharehome.usermodule.controller;

import cn.hutool.core.util.ReUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import top.sharehome.issuemodule.common.constant.CommonConstant;
import top.sharehome.issuemodule.common.response.R;
import top.sharehome.issuemodule.common.response.RCodeEnum;
import top.sharehome.issuemodule.model.vo.token.TokenLoginVo;
import top.sharehome.issuemodule.utils.meta.BeanMetaDataUtils;
import top.sharehome.issuemodule.utils.throwException.ThrowUtils;
import top.sharehome.usermodule.model.dto.manager.ManagerAddUpdateDto;
import top.sharehome.usermodule.model.dto.manager.ManagerPageDto;
import top.sharehome.usermodule.model.dto.manager.ManagerUpdatePasswordDto;
import top.sharehome.usermodule.model.vo.manager.ManagerGetInfoBaseVo;
import top.sharehome.usermodule.model.vo.manager.ManagerInfoVo;
import top.sharehome.usermodule.model.vo.manager.ManagerPageVo;
import top.sharehome.usermodule.service.ManagerService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 管理员相关接口
 *
 * @author AntonyCheng
 * @since 2023/7/18 15:35:29
 */
@RestController
@RequestMapping("/manager")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ManagerController {
    @Resource
    private ManagerService managerService;

    /**
     * 获取用户的详细信息
     *
     * @param request 获取loginUser
     * @return 用户的详细信息
     */
    @GetMapping("/getUserInfo")
    public R<? extends ManagerGetInfoBaseVo> getUserInfo(HttpServletRequest request) {
        TokenLoginVo loginUser = (TokenLoginVo) request.getAttribute("loginUser");
        ManagerGetInfoBaseVo userInfo = managerService.getUserInfo(loginUser);
        return R.success(userInfo);
    }

    /**
     * 模糊查询管理员信息
     *
     * @param current        当前页
     * @param pageSize       页面项数
     * @param managerPageDto 查询参数
     * @return 返回分页查询结果
     */
    @GetMapping("/page/{current}/{pageSize}")
    public R<Page<ManagerPageVo>> pageManager(@PathVariable("current") Long current, @PathVariable("pageSize") Long pageSize, ManagerPageDto managerPageDto) {
        // 校验参数
        verifyManagerPageParams(managerPageDto);
        Page<ManagerPageVo> queryResult = managerService.pageManager(current, pageSize, managerPageDto);
        return R.success(queryResult);
    }

    /**
     * 回显管理员信息
     *
     * @param id 需要回显的管理员ID
     * @return 管理员信息
     */
    @GetMapping("/info/{id}")
    public R<ManagerInfoVo> info(@PathVariable("id") String id) {
        if (Objects.isNull(id)) {
            ThrowUtils.error(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY);
        }
        ManagerInfoVo managerInfoVo = managerService.info(id);
        return R.success(managerInfoVo, "回显信息成功！");
    }

    /**
     * 提交更改管理员信息申请
     *
     * @param managerAddUpdateDto 更新管理员信息Dto类
     * @return 返回提交申请结果
     */
    @PostMapping("/addUpdateManagerInfo")
    public R<String> addUpdateManagerInfo(@RequestBody ManagerAddUpdateDto managerAddUpdateDto) {
        verifyManagerUpdateInfoParams(managerAddUpdateDto);
        managerService.addUpdateManagerInfo(managerAddUpdateDto);
        return R.success("提交更改申请成功，请等候管理员审核后进行邮箱反馈！");
    }

    /**
     * 更新管理员密码
     *
     * @param managerUpdatePasswordDto 管理员更新密码Dto类
     * @return 返回更新结果
     */
    @PostMapping("/updatePassword")
    public R<String> updatePassword(@RequestBody ManagerUpdatePasswordDto managerUpdatePasswordDto) {
        verifyUpdatePasswordParams(managerUpdatePasswordDto);
        managerService.updatePassword(managerUpdatePasswordDto);
        return R.success("更新密码成功，请重新登陆!");
    }

    /**
     * 校验管理员信息分页参数
     *
     * @param managerPageDto 管理员分页类
     */
    private void verifyManagerPageParams(ManagerPageDto managerPageDto) {
        if (Objects.nonNull(managerPageDto) && Objects.nonNull(managerPageDto.getGender())) {
            // 校验性别数据格式
            if (!(managerPageDto.getGender() == CommonConstant.GENDER_MAN
                    || managerPageDto.getGender() == CommonConstant.GENDER_WOMAN)) {
                ThrowUtils.error(RCodeEnum.DATA_BINARY_IS_NOT_SATISFIED);
            }
        }
        if (Objects.nonNull(managerPageDto) && Objects.nonNull(managerPageDto.getLevel())) {
            // 校验租户等级
            if (!CommonConstant.LEVEL_LIST.contains(managerPageDto.getLevel())) {
                ThrowUtils.error(RCodeEnum.TENANT_LEVEL_FORMAT_VERIFICATION_FAILED);
            }
        }
    }

    /**
     * 校验更改管理员信息参数
     *
     * @param managerAddUpdateDto 管理员信息更改类
     */
    private void verifyManagerUpdateInfoParams(ManagerAddUpdateDto managerAddUpdateDto) {
        // 判空
        if (BeanMetaDataUtils.isAnyMetadataEmpty(managerAddUpdateDto)) {
            ThrowUtils.error(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY);
        }
        // 校验账户格式
        if (!ReUtil.isMatch(CommonConstant.MATCHER_PHONE_REGEX, managerAddUpdateDto.getAccount())) {
            ThrowUtils.error(RCodeEnum.USERNAME_CONTAINS_SPECIAL_CHARACTERS);
        }
        // 校验姓名格式
        if (!ReUtil.isMatch(CommonConstant.MATCHER_NAME_REGEX, managerAddUpdateDto.getName())) {
            ThrowUtils.error(RCodeEnum.NAME_FORMAT_VERIFICATION_FAILED);
        }
        // 校验邮箱格式
        if (!ReUtil.isMatch(CommonConstant.MATCHER_EMAIL_REGEX, managerAddUpdateDto.getEmail())) {
            ThrowUtils.error(RCodeEnum.EMAIL_FORMAT_VERIFICATION_FAILED);
        }
        // 校验性别数据格式
        if (!(managerAddUpdateDto.getGender() == CommonConstant.GENDER_MAN || managerAddUpdateDto.getGender() == CommonConstant.GENDER_WOMAN)) {
            ThrowUtils.error(RCodeEnum.DATA_BINARY_IS_NOT_SATISFIED);
        }
    }

    /**
     * 校验管理员更改密码参数
     *
     * @param managerUpdatePasswordDto 管理员更改密码Dto类
     */
    private void verifyUpdatePasswordParams(ManagerUpdatePasswordDto managerUpdatePasswordDto) {
        // 判空
        if (BeanMetaDataUtils.isAnyMetadataEmpty(managerUpdatePasswordDto)) {
            ThrowUtils.error(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY);
        }
        // 判两次密码是否相同
        if (!Objects.equals(managerUpdatePasswordDto.getUpdatePassword(), managerUpdatePasswordDto.getConfirmUpdatePassword())) {
            ThrowUtils.error(RCodeEnum.PASSWORD_AND_CONFIRM_PASSWORD_ARE_NOT_THE_SAME);
        }
    }
}
