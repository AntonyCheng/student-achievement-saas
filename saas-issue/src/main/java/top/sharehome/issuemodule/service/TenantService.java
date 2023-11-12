package top.sharehome.issuemodule.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import top.sharehome.issuemodule.model.dto.tenant.*;
import top.sharehome.issuemodule.model.entity.Tenant;
import top.sharehome.issuemodule.model.vo.tenant.TenantInfoVo;
import top.sharehome.issuemodule.model.vo.tenant.TenantPageVo;

import java.util.List;

/**
 * 租户注册相关接口Service（租户提交注册申请、平台管理员审核租户申请等）
 *
 * @author AntonyCheng
 * @since 2023/7/7 15:41:24
 */
public interface TenantService extends IService<Tenant> {

    /**
     * 租户注册接口
     *
     * @param tenantRegisterDto 注册参数类
     */
    void register(TenantRegisterDto tenantRegisterDto);

    /**
     * 审核租户注册信息接口
     * todo：暂时配合portainer进行后端容器的创建，所以需要手动创建后在进行填写审核表
     *
     * @param tenantCensorDto 审核必要参数
     */
    void censor(TenantCensorDto tenantCensorDto);

    /**
     * 模糊查询租户注册信息
     *
     * @param current       当前页
     * @param pageSize      页面项数
     * @param tenantPageDto 查询参数
     * @return 返回分页查询结果
     */
    Page<TenantPageVo> pageTenant(Integer current, Integer pageSize, TenantPageDto tenantPageDto);

    /**
     * 删除租户注册信息（单个）
     *
     * @param id 被删除租户ID
     */
    void delete(Long id);

    /**
     * 删除租户注册信息（批量）
     *
     * @param ids 被删除租户IDs
     */
    void deleteBatch(List<Long> ids);

    /**
     * 回显租户注册信息
     *
     * @param id 需要回显的租户ID
     * @return 租户注册信息
     */
    TenantInfoVo info(Long id);

    /**
     * 更新租户注册信息
     *
     * @param tenantUpdateInfoDto 租户信息更新参数
     */
    void updateInfo(TenantUpdateInfoDto tenantUpdateInfoDto);

    /**
     * 更新租户状态信息
     *
     * @param id 需要更新的租户ID
     */
    void updateStatus(String id);

    /**
     * 获取找回密码验证邮件
     *
     * @param tenantPasswordEmailDto 验证邮件所需Dto类
     */
    void getPasswordEmail(TenantPasswordEmailDto tenantPasswordEmailDto);

    /**
     * 找回密码
     *
     * @param tenantFindPasswordDto 找回密码必要参数
     */
    void findPassword(TenantFindPasswordDto tenantFindPasswordDto);
}
