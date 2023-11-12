package top.sharehome.usermodule.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import top.sharehome.issuemodule.model.vo.token.TokenLoginVo;
import top.sharehome.usermodule.model.dto.manager.ManagerAddUpdateDto;
import top.sharehome.usermodule.model.dto.manager.ManagerPageDto;
import top.sharehome.usermodule.model.dto.manager.ManagerUpdatePasswordDto;
import top.sharehome.usermodule.model.entity.Manager;
import top.sharehome.usermodule.model.vo.manager.ManagerGetInfoBaseVo;
import top.sharehome.usermodule.model.vo.manager.ManagerInfoVo;
import top.sharehome.usermodule.model.vo.manager.ManagerPageVo;

public interface ManagerService extends IService<Manager> {

    /**
     * 获取用户的登录信息
     *
     * @param loginUser 登陆凭证中的登录信息
     * @return 用户的登录信息
     */
    <T extends ManagerGetInfoBaseVo> T getUserInfo(TokenLoginVo loginUser);

    /**
     * 模糊查询管理员信息
     *
     * @param current        当前页
     * @param pageSize       页面项数
     * @param managerPageDto 查询参数
     * @return 返回分页查询结果
     */
    Page<ManagerPageVo> pageManager(Long current, Long pageSize, ManagerPageDto managerPageDto);

    /**
     * 回显管理员信息
     *
     * @param id 需要会显的管理员ID
     * @return 管理员信息
     */
    ManagerInfoVo info(String id);

    /**
     * 提交更改管理员信息申请
     *
     * @param managerAddUpdateDto 更新管理员信息Dto类
     */
    void addUpdateManagerInfo(ManagerAddUpdateDto managerAddUpdateDto);

    /**
     * 更新管理员密码
     *
     * @param managerUpdatePasswordDto 管理员更新密码Dto类
     */
    void updatePassword(ManagerUpdatePasswordDto managerUpdatePasswordDto);
}
