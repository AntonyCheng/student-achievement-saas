package top.sharehome.usermodule.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import top.sharehome.usermodule.model.dto.teacherJobTitle.TeacherJobTitleAddDto;
import top.sharehome.usermodule.model.dto.teacherJobTitle.TeacherJobTitlePageDto;
import top.sharehome.usermodule.model.dto.teacherJobTitle.TeacherJobTitleUpdateDto;
import top.sharehome.usermodule.model.entity.TeacherJobTitle;
import top.sharehome.usermodule.model.vo.teacherJobTitle.TeacherJobTitleInfoVo;
import top.sharehome.usermodule.model.vo.teacherJobTitle.TeacherJobTitlePageVo;

import java.util.List;
import java.util.Map;

public interface TeacherJobTitleService extends IService<TeacherJobTitle> {

    /**
     * 添加教师职称
     *
     * @param teacherJobTitleAddDto 教师职称信息Dto类
     */
    void add(TeacherJobTitleAddDto teacherJobTitleAddDto);

    /**
     * 批量添加教师职称
     *
     * @param teacherJobTitleAddDtoList 教师职称信息Dto类列表
     */
    void addBatch(List<TeacherJobTitleAddDto> teacherJobTitleAddDtoList);

    /**
     * 获取已有教师职称列表
     *
     * @return 返回所有已有的教师职称列表
     */
    Map<Long, String> get();

    /**
     * 模糊查询教师职称信息
     *
     * @param current                当前页
     * @param pageSize               页面项数
     * @param teacherJobTitlePageDto 查询参数
     * @return 返回分页查询结果
     */
    Page<TeacherJobTitlePageVo> pageTeacherJobTitle(Long current, Long pageSize, TeacherJobTitlePageDto teacherJobTitlePageDto);

    /**
     * 回显教师职称信息
     *
     * @param id 需要回显的教师职称ID
     * @return 教师职称信息
     */
    TeacherJobTitleInfoVo info(Long id);

    /**
     * 更新教师职称信息
     *
     * @param teacherJobTitleUpdateDto 更新教师职称信息Dto类
     */
    void updateTeacherJobTitle(TeacherJobTitleUpdateDto teacherJobTitleUpdateDto);

    /**
     * 删除教师职称信息
     *
     * @param id 被删除教师职称ID
     */
    void delete(Long id);

    /**
     * 批量删除教师职称信息
     *
     * @param ids 被删除教师职称ID列表
     */
    void deleteBatch(List<Long> ids);
}
