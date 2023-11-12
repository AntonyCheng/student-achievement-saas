package top.sharehome.usermodule.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import top.sharehome.usermodule.model.dto.student.StudentAddDto;
import top.sharehome.usermodule.model.dto.student.StudentPageDto;
import top.sharehome.usermodule.model.dto.student.StudentUpdateDto;
import top.sharehome.usermodule.model.entity.Student;
import top.sharehome.usermodule.model.vo.student.StudentInfoVo;
import top.sharehome.usermodule.model.vo.student.StudentPageVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface StudentService extends IService<Student> {

    /**
     * 添加学生信息
     *
     * @param studentAddDto 学生信息Dto类
     * @param request       获取操作者的登录信息
     */
    void add(StudentAddDto studentAddDto, HttpServletRequest request);


    /**
     * 批量添加学生信息
     *
     * @param studentAddDtoList 学生信息Dto类列表
     * @param request           获取操作者的登录信息
     */
    void addBatch(List<StudentAddDto> studentAddDtoList, HttpServletRequest request);

    /**
     * 删除学生信息
     *
     * @param id      被删除学生ID
     * @param request 获取操作者的登陆信息
     */
    void delete(Long id, HttpServletRequest request);

    /**
     * 批量删除学生信息
     *
     * @param ids     被删除的学生IDs
     * @param request 获取操作者的登录信息
     */
    void deleteBatch(List<Long> ids, HttpServletRequest request);

    /**
     * 模糊查询学生信息
     *
     * @param current        当前页
     * @param pageSize       页面项数
     * @param studentPageDto 查询参数
     * @return 返回分页查询结果
     */
    Page<StudentPageVo> pageStudent(Long current, Long pageSize, StudentPageDto studentPageDto);

    /**
     * 回显学生信息
     *
     * @param id 需要会显的学生ID
     * @return 学生信息
     */
    StudentInfoVo info(String id);

    /**
     * 更新学生信息
     *
     * @param studentUpdateDto 更新后的学生信息
     * @param request          获取操作者的登录信息
     */
    void updateStudent(StudentUpdateDto studentUpdateDto, HttpServletRequest request);

    /**
     * 重置单个学生密码
     *
     * @param id      需要重置的用户ID
     * @param request 获取操作者的登录信息
     */
    void resetPassword(Long id, HttpServletRequest request);

    /**
     * 重置多个学生密码
     *
     * @param ids     需要重置的用户ID列表
     * @param request 获取操作者的登录信息
     */
    void resetPasswords(List<Long> ids, HttpServletRequest request);
}
