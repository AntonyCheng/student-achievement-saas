package top.sharehome.usermodule.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import top.sharehome.usermodule.model.dto.teacherRole.TeacherRoleAddDto;
import top.sharehome.usermodule.model.dto.teacherRole.TeacherRolePageDto;
import top.sharehome.usermodule.model.dto.teacherRole.TeacherRoleUpdateDto;
import top.sharehome.usermodule.model.entity.TeacherRole;
import top.sharehome.usermodule.model.vo.teacherRole.TeacherRoleInfoVo;
import top.sharehome.usermodule.model.vo.teacherRole.TeacherRolePageVo;

import java.util.List;
import java.util.Map;

public interface TeacherRoleService extends IService<TeacherRole> {

    /**
     * 添加教师角色
     *
     * @param teacherRoleAddDto 教师角色信息Dto类
     */
    void add(TeacherRoleAddDto teacherRoleAddDto);

    /**
     * 批量添加教师角色
     *
     * @param teacherRoleAddDtoList 教师角色信息Dto类列表
     */
    void addBatch(List<TeacherRoleAddDto> teacherRoleAddDtoList);

    /**
     * 获取已有教师角色列表
     *
     * @return 返回所有已有的教师角色列表
     */
    Map<Long, String> get();

    /**
     * 模糊查询教师角色信息
     *
     * @param current            当前页
     * @param pageSize           页面项数
     * @param teacherRolePageDto 查询参数
     * @return 返回分页查询结果
     */
    Page<TeacherRolePageVo> pageTeacherRole(Long current, Long pageSize, TeacherRolePageDto teacherRolePageDto);

    /**
     * 回显教师角色信息
     *
     * @param id 需要回显的教师角色ID
     * @return 教师角色信息
     */
    TeacherRoleInfoVo info(Long id);

    /**
     * 更新教师角色信息
     *
     * @param teacherRoleUpdateDto 更新教师角色信息Dto类
     */
    void updateTeacherRole(TeacherRoleUpdateDto teacherRoleUpdateDto);

    /**
     * 删除教师角色信息
     *
     * @param id 被删除教师角色ID
     */
    void delete(Long id);

    /**
     * 批量删除教师角色信息
     *
     * @param ids 被删除教师角色ID列表
     */
    void deleteBatch(List<Long> ids);
}

