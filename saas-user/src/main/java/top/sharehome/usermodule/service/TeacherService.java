package top.sharehome.usermodule.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import top.sharehome.usermodule.model.dto.teacher.TeacherAddDto;
import top.sharehome.usermodule.model.dto.teacher.TeacherPageDto;
import top.sharehome.usermodule.model.dto.teacher.TeacherUpdateDto;
import top.sharehome.usermodule.model.entity.Teacher;
import com.baomidou.mybatisplus.extension.service.IService;
import top.sharehome.usermodule.model.vo.teacher.TeacherInfoVo;
import top.sharehome.usermodule.model.vo.teacher.TeacherPageVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface TeacherService extends IService<Teacher> {

    /**
     * 添加教师信息
     *
     * @param teacherAddDto 教师信息Dto类
     * @param request       获取该操作的登陆者的信息
     */
    void add(TeacherAddDto teacherAddDto, HttpServletRequest request);

    /**
     * 批量添加教师信息
     *
     * @param teacherAddDtoList 教师信息Dto类列表
     * @param request           获取操作者的登录信息
     */
    void addBatch(List<TeacherAddDto> teacherAddDtoList, HttpServletRequest request);

    /**
     * 删除教师信息
     *
     * @param id      被删除教师ID
     * @param request 获取操作者的登陆信息
     */
    void delete(Long id, HttpServletRequest request);

    /**
     * 批量删除教师信息
     *
     * @param ids     被删除的教师IDs
     * @param request 获取操作者的登录信息
     */
    void deleteBatch(List<Long> ids, HttpServletRequest request);

    /**
     * 模糊查询教师信息
     *
     * @param current        当前页
     * @param pageSize       页面项数
     * @param teacherPageDto 查询参数
     * @return 返回分页查询结果
     */
    Page<TeacherPageVo> pageTeacher(Long current, Long pageSize, TeacherPageDto teacherPageDto);

    /**
     * 回显教师信息
     *
     * @param id 需要会显的教师ID
     * @return 教师信息
     */
    TeacherInfoVo info(String id);

    /**
     * 更新教师信息
     *
     * @param teacherUpdateDto 更新后的教师信息
     * @param request          获取操作者的登录信息
     */
    void updateStudent(TeacherUpdateDto teacherUpdateDto, HttpServletRequest request);

    /**
     * 重置多个教师密码
     *
     * @param id      需要重置的用户ID
     * @param request 获取操作者的登录信息
     */
    void resetPassword(Long id, HttpServletRequest request);

    /**
     * 重置多个教师密码
     *
     * @param ids     需要重置的用户ID列表
     * @param request 获取操作者的登录信息
     */
    void resetPasswords(List<Long> ids, HttpServletRequest request);
}
