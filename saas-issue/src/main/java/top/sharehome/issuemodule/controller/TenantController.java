package top.sharehome.issuemodule.controller;

import cn.hutool.core.util.ReUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import top.sharehome.issuemodule.common.constant.CommonConstant;
import top.sharehome.issuemodule.common.response.R;
import top.sharehome.issuemodule.common.response.RCodeEnum;
import top.sharehome.issuemodule.model.dto.tenant.*;
import top.sharehome.issuemodule.model.vo.tenant.TenantInfoVo;
import top.sharehome.issuemodule.model.vo.tenant.TenantPageVo;
import top.sharehome.issuemodule.service.TenantService;
import top.sharehome.issuemodule.utils.meta.BeanMetaDataUtils;
import top.sharehome.issuemodule.utils.throwException.ThrowUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * 租户相关接口（租户提交注册申请、平台管理员审核租户申请等）
 *
 * @author AntonyCheng
 * @since 2023/7/7 15:47:10
 */
@RestController
@RequestMapping("/tenant")
@CrossOrigin
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TenantController {
    @Resource
    private TenantService tenantService;

    /**
     * 租户注册接口
     *
     * @param tenantRegisterDto 注册参数类
     * @return 返回注册结果
     */
    @PostMapping("/register")
    public R<String> register(@RequestBody TenantRegisterDto tenantRegisterDto) {
        // 校验参数
        verifyRegisterParams(tenantRegisterDto);
        tenantService.register(tenantRegisterDto);
        return R.success("注册成功！审核结果会发至您的邮箱！");
    }

    /**
     * 审核租户注册信息接口
     * todo：暂时配合portainer进行后端容器的创建，所以需要手动创建后在进行填写审核表
     *
     * @param tenantCensorDto 审核必要参数
     * @return 审核结果
     */
    @PostMapping("/censor")
    public R<String> censor(@RequestBody TenantCensorDto tenantCensorDto) {
        // 校验参数
        verifyCensorParams(tenantCensorDto);
        tenantService.censor(tenantCensorDto);
        return R.success("审核完成，反馈邮件已经发出！");
    }

    /**
     * 删除租户注册信息（单个）
     *
     * @param id 被删除租户ID
     * @return 删除结果
     */
    @DeleteMapping("/delete/{id}")
    public R<String> delete(@PathVariable("id") Long id) {
        if (Objects.isNull(id)) {
            ThrowUtils.error(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY);
        }
        tenantService.delete(id);
        return R.success("删除成功！");
    }

    /**
     * 删除租户注册信息（批量）
     *
     * @param ids 被删除租户IDs
     * @return 删除结果
     */
    @DeleteMapping("/deleteBatch")
    public R<String> deleteBatch(@RequestBody List<Long> ids) {
        if (Objects.isNull(ids) || ids.isEmpty()) {
            ThrowUtils.error(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY);
        }
        tenantService.deleteBatch(ids);
        return R.success("删除成功！");
    }

    /**
     * 模糊查询租户注册信息
     *
     * @param current       当前页
     * @param pageSize      页面项数
     * @param tenantPageDto 查询参数
     * @return 返回分页查询结果
     */
    @GetMapping("/page/{current}/{pageSize}")
    public R<Page<TenantPageVo>> pageTenant(@PathVariable("current") Integer current, @PathVariable("pageSize") Integer pageSize, TenantPageDto tenantPageDto) {
        // 校验参数
        verifyTenantPageParams(tenantPageDto);
        Page<TenantPageVo> pageResult = tenantService.pageTenant(current, pageSize, tenantPageDto);
        return R.success(pageResult);
    }

    /**
     * 回显租户注册信息
     *
     * @param id 需要回显的租户ID
     * @return 租户注册信息
     */
    @GetMapping("/info/{id}")
    public R<TenantInfoVo> info(@PathVariable("id") Long id) {
        if (Objects.isNull(id)) {
            ThrowUtils.error(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY);
        }
        TenantInfoVo tenantInfoVo = tenantService.info(id);
        return R.success(tenantInfoVo, "回显信息成功！");
    }

    /**
     * 更新租户注册信息
     *
     * @param tenantUpdateInfoDto 租户信息更新参数
     * @return 返回更新结果
     */
    @PutMapping("/updateInfo")
    public R<String> updateInfo(@RequestBody TenantUpdateInfoDto tenantUpdateInfoDto) {
        verifyTenantUpdateInfoParams(tenantUpdateInfoDto);
        tenantService.updateInfo(tenantUpdateInfoDto);
        return R.success("更新成功！");
    }

    /**
     * 更新租户状态信息
     *
     * @param id 需要更新的租户ID
     * @return 返回更新结果
     */
    @PutMapping("/updateStatus/{id}")
    public R<String> updateStatus(@PathVariable("id") String id) {
        if (Objects.isNull(id)) {
            ThrowUtils.error(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY);
        }
        tenantService.updateStatus(id);
        return R.success("更新成功！");
    }

    /**
     * 获取找回密码验证邮件
     *
     * @param tenantPasswordEmailDto 验证邮件所需Dto类
     * @return 返回邮件发送情况
     */
    @PostMapping("/getPasswordEmail")
    public R<String> getPasswordEmail(@RequestBody TenantPasswordEmailDto tenantPasswordEmailDto) {
        if (BeanMetaDataUtils.isAnyMetadataEmpty(tenantPasswordEmailDto)) {
            ThrowUtils.error(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY);
        }
        tenantService.getPasswordEmail(tenantPasswordEmailDto);
        return R.success("邮件已经发出，请查看后填写验证码！");
    }

    /**
     * 找回密码
     *
     * @param tenantFindPasswordDto 找回密码必要参数
     * @return 返回找回结果
     */
    @PostMapping("/findPassword")
    public R<String> findPassword(@RequestBody TenantFindPasswordDto tenantFindPasswordDto) {
        verifyFindPasswordParams(tenantFindPasswordDto);
        tenantService.findPassword(tenantFindPasswordDto);
        return R.success("密码修改成功！");
    }

    /**
     * 校验注册参数
     *
     * @param tenantRegisterDto 注册参数类
     */
    private void verifyRegisterParams(TenantRegisterDto tenantRegisterDto) {
        // 判空
        if (BeanMetaDataUtils.isAnyMetadataEmpty(tenantRegisterDto)) {
            ThrowUtils.error(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY);
        }
        // 判两次密码是否相同
        if (!Objects.equals(tenantRegisterDto.getPassword(), tenantRegisterDto.getConfirmPassword())) {
            ThrowUtils.error(RCodeEnum.PASSWORD_AND_CONFIRM_PASSWORD_ARE_NOT_THE_SAME);
        }
        // 校验账号密码是否存在空格
        if (tenantRegisterDto.getAccount().contains(" ") || tenantRegisterDto.getPassword().contains(" ")) {
            ThrowUtils.error(RCodeEnum.ACCOUNT_OR_PASSWORDS_CANNOT_CONTAIN_SPACES);
        }
        // 校验账户长度
        if (tenantRegisterDto.getAccount().length() > CommonConstant.ACCOUNT_LE_LENGTH || tenantRegisterDto.getAccount().length() < CommonConstant.ACCOUNT_GE_LENGTH) {
            ThrowUtils.error(RCodeEnum.ACCOUNT_LENGTH_DO_NOT_MATCH);
        }
        // 校验密码长度
        if (tenantRegisterDto.getPassword().length() > CommonConstant.PASSWORD_LE_LENGTH || tenantRegisterDto.getPassword().length() < CommonConstant.PASSWORD_GE_LENGTH) {
            ThrowUtils.error(RCodeEnum.PASSWORD_LENGTH_DO_NOT_MATCH);
        }
        // 校验账户格式
        if (!ReUtil.isMatch(CommonConstant.MATCHER_PHONE_REGEX, tenantRegisterDto.getAccount())) {
            ThrowUtils.error(RCodeEnum.USERNAME_CONTAINS_SPECIAL_CHARACTERS);
        }
        // 校验姓名格式
        if (!ReUtil.isMatch(CommonConstant.MATCHER_NAME_REGEX, tenantRegisterDto.getName())) {
            ThrowUtils.error(RCodeEnum.NAME_FORMAT_VERIFICATION_FAILED);
        }
        // 校验邮箱格式
        if (!ReUtil.isMatch(CommonConstant.MATCHER_EMAIL_REGEX, tenantRegisterDto.getEmail())) {
            ThrowUtils.error(RCodeEnum.EMAIL_FORMAT_VERIFICATION_FAILED);
        }
        // 校验性别数据格式
        if (!(tenantRegisterDto.getGender() == CommonConstant.GENDER_MAN || tenantRegisterDto.getGender() == CommonConstant.GENDER_WOMAN)) {
            ThrowUtils.error(RCodeEnum.DATA_BINARY_IS_NOT_SATISFIED);
        }
        // 校验租户等级
        if (!CommonConstant.LEVEL_LIST.contains(tenantRegisterDto.getLevel())) {
            ThrowUtils.error(RCodeEnum.TENANT_LEVEL_FORMAT_VERIFICATION_FAILED);
        }
    }

    /**
     * 校验租户审核参数
     *
     * @param tenantCensorDto 审核参数类
     */
    private void verifyCensorParams(TenantCensorDto tenantCensorDto) {
        if (tenantCensorDto.getResult() == CommonConstant.CENSOR_PASS) {
            // 判空（反馈内容除外）
            if (BeanMetaDataUtils.isAnyMetadataEmpty(tenantCensorDto, "content")) {
                ThrowUtils.error(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY);
            }
            // 校验容器IP格式
            if (!ReUtil.isMatch(CommonConstant.MATCHER_IP_REGEX, tenantCensorDto.getIp())) {
                ThrowUtils.error(RCodeEnum.IP_FORMAT_VERIFICATION_FAILED);
            }
            // 校验容器PORT格式
            if (!ReUtil.isMatch(CommonConstant.MATCHER_PORT_REGEX, tenantCensorDto.getPort())) {
                ThrowUtils.error(RCodeEnum.PORT_FORMAT_VERIFICATION_FAILED);
            }
            // 校验数据库IP格式
            if (!ReUtil.isMatch(CommonConstant.MATCHER_IP_REGEX, tenantCensorDto.getDbIp())) {
                ThrowUtils.error(RCodeEnum.IP_FORMAT_VERIFICATION_FAILED);
            }
            // 校验数据库PORT格式
            if (!ReUtil.isMatch(CommonConstant.MATCHER_PORT_REGEX, tenantCensorDto.getDbPort())) {
                ThrowUtils.error(RCodeEnum.PORT_FORMAT_VERIFICATION_FAILED);
            }
            // 校验邮箱格式
            if (!ReUtil.isMatch(CommonConstant.MATCHER_EMAIL_REGEX, tenantCensorDto.getEmail())) {
                ThrowUtils.error(RCodeEnum.EMAIL_FORMAT_VERIFICATION_FAILED);
            }
        } else {
            // 判空（反馈内容除外）
            if (BeanMetaDataUtils.isAnyMetadataEmpty(tenantCensorDto, "content", "ip", "port", "dbIp", "dbPort")) {
                ThrowUtils.error(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY);
            }
            // 校验邮箱格式
            if (!ReUtil.isMatch(CommonConstant.MATCHER_EMAIL_REGEX, tenantCensorDto.getEmail())) {
                ThrowUtils.error(RCodeEnum.EMAIL_FORMAT_VERIFICATION_FAILED);
            }
            // 判断审核结果数据格式
            if (tenantCensorDto.getResult() != CommonConstant.CENSOR_NOT_PASS) {
                ThrowUtils.error(RCodeEnum.DATA_BINARY_IS_NOT_SATISFIED);
            }
        }
    }

    /**
     * 校验租户查询参数
     *
     * @param tenantPageDto 查询参数类
     */
    private void verifyTenantPageParams(TenantPageDto tenantPageDto) {
        if (Objects.nonNull(tenantPageDto) && Objects.nonNull(tenantPageDto.getGender())) {
            // 校验性别数据格式
            if (!(tenantPageDto.getGender() == CommonConstant.GENDER_MAN || tenantPageDto.getGender() == CommonConstant.GENDER_WOMAN)) {
                ThrowUtils.error(RCodeEnum.DATA_BINARY_IS_NOT_SATISFIED);
            }
        }
        if (Objects.nonNull(tenantPageDto) && Objects.nonNull(tenantPageDto.getLevel())) {
            // 校验租户等级
            if (!CommonConstant.LEVEL_LIST.contains(tenantPageDto.getLevel())) {
                ThrowUtils.error(RCodeEnum.TENANT_LEVEL_FORMAT_VERIFICATION_FAILED);
            }
        }
        if (Objects.nonNull(tenantPageDto) && Objects.nonNull(tenantPageDto.getStatus())) {
            // 校验租户状态
            if (!CommonConstant.TENANT_STATUS_LIST.contains(tenantPageDto.getStatus())) {
                ThrowUtils.error(RCodeEnum.TENANT_STATUS_FORMAT_VERIFICATION_FAILED);
            }
        }
    }

    /**
     * 校验租户信息更改参数
     *
     * @param tenantUpdateInfoDto 信息更改类
     */
    private void verifyTenantUpdateInfoParams(TenantUpdateInfoDto tenantUpdateInfoDto) {
        // 判空
        if (BeanMetaDataUtils.isAnyMetadataEmpty(tenantUpdateInfoDto)) {
            ThrowUtils.error(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY);
        }
        // 校验容器IP格式
        if (!ReUtil.isMatch(CommonConstant.MATCHER_IP_REGEX, tenantUpdateInfoDto.getIp())) {
            ThrowUtils.error(RCodeEnum.IP_FORMAT_VERIFICATION_FAILED);
        }
        // 校验容器PORT格式
        if (!ReUtil.isMatch(CommonConstant.MATCHER_PORT_REGEX, tenantUpdateInfoDto.getPort())) {
            ThrowUtils.error(RCodeEnum.PORT_FORMAT_VERIFICATION_FAILED);
        }
        // 校验数据库IP格式
        if (!ReUtil.isMatch(CommonConstant.MATCHER_IP_REGEX, tenantUpdateInfoDto.getDbIp())) {
            ThrowUtils.error(RCodeEnum.IP_FORMAT_VERIFICATION_FAILED);
        }
        // 校验数据库PORT格式
        if (!ReUtil.isMatch(CommonConstant.MATCHER_PORT_REGEX, tenantUpdateInfoDto.getDbPort())) {
            ThrowUtils.error(RCodeEnum.PORT_FORMAT_VERIFICATION_FAILED);
        }
        // 校验租户等级
        if (!CommonConstant.LEVEL_LIST.contains(tenantUpdateInfoDto.getLevel())) {
            ThrowUtils.error(RCodeEnum.TENANT_LEVEL_FORMAT_VERIFICATION_FAILED);
        }
    }

    /**
     * 校验找回密码参数
     *
     * @param tenantFindPasswordDto 找回密码参数类
     */
    private void verifyFindPasswordParams(TenantFindPasswordDto tenantFindPasswordDto) {
        if (BeanMetaDataUtils.isAnyMetadataEmpty(tenantFindPasswordDto)) {
            ThrowUtils.error(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY);
        }
        if (!Objects.equals(tenantFindPasswordDto.getPassword(), tenantFindPasswordDto.getConfirmPassword())) {
            ThrowUtils.error(RCodeEnum.PASSWORD_AND_CONFIRM_PASSWORD_ARE_NOT_THE_SAME);
        }
    }
}

