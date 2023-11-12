package top.sharehome.usermodule.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import top.sharehome.issuemodule.common.response.R;
import top.sharehome.issuemodule.common.response.RCodeEnum;
import top.sharehome.issuemodule.utils.meta.BeanMetaDataUtils;
import top.sharehome.issuemodule.utils.throwException.ThrowUtils;
import top.sharehome.usermodule.model.dto.teacherJobTitle.TeacherJobTitleAddDto;
import top.sharehome.usermodule.model.dto.teacherJobTitle.TeacherJobTitlePageDto;
import top.sharehome.usermodule.model.dto.teacherJobTitle.TeacherJobTitleUpdateDto;
import top.sharehome.usermodule.model.vo.teacherJobTitle.TeacherJobTitleInfoVo;
import top.sharehome.usermodule.model.vo.teacherJobTitle.TeacherJobTitlePageVo;
import top.sharehome.usermodule.service.TeacherJobTitleService;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 教师职称相关接口
 *
 * @author AntonyCheng
 * @since 2023/8/19 00:04:43
 */
@RestController
@RequestMapping("/teacherJobTitle")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TeacherJobTitleController {
    @Resource
    private TeacherJobTitleService teacherJobTitleService;

    /**
     * 添加教师职称
     *
     * @param teacherJobTitleAddDto 教师职称信息Dto类
     * @return 返回添加结果
     */
    @PostMapping("/add")
    public R<String> add(@RequestBody TeacherJobTitleAddDto teacherJobTitleAddDto) {
        if (BeanMetaDataUtils.isAnyMetadataEmpty(teacherJobTitleAddDto)) {
            ThrowUtils.error(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY);
        }
        teacherJobTitleService.add(teacherJobTitleAddDto);
        return R.success("添加成功！");
    }

    /**
     * 批量添加教师职称
     *
     * @param teacherJobTitleAddDtoList 教师职称信息Dto类列表
     * @return 返回添加结果
     */
    @PostMapping("/addBatch")
    public R<String> addBatch(@RequestBody List<TeacherJobTitleAddDto> teacherJobTitleAddDtoList) {
        if (Objects.isNull(teacherJobTitleAddDtoList) || teacherJobTitleAddDtoList.isEmpty()) {
            ThrowUtils.error(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY);
        }
        for (TeacherJobTitleAddDto teacherJobTitleAddDto : teacherJobTitleAddDtoList) {
            if (BeanMetaDataUtils.isAnyMetadataEmpty(teacherJobTitleAddDto)) {
                ThrowUtils.error(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY);
            }
        }
        if (new HashSet<>(teacherJobTitleAddDtoList).size() != teacherJobTitleAddDtoList.size()) {
            ThrowUtils.error(RCodeEnum.THERE_IS_DUPLICATE_DATA_FOR_BULK_OPERATIONS);
        }
        teacherJobTitleService.addBatch(teacherJobTitleAddDtoList);
        return R.success("添加成功！");
    }

    /**
     * 获取已有教师职称列表
     *
     * @return 返回所有已有的教师职称列表
     */
    @GetMapping("/get")
    public R<Map<Long, String>> get() {
        Map<Long, String> result = teacherJobTitleService.get();
        return R.success(result);
    }

    /**
     * 模糊查询教师职称信息
     *
     * @param current                当前页
     * @param pageSize               页面项数
     * @param teacherJobTitlePageDto 查询参数
     * @return 返回分页查询结果
     */
    @GetMapping("/page/{current}/{pageSize}")
    public R<Page<TeacherJobTitlePageVo>> pageTeacherJobTitle(@PathVariable("current") Long current, @PathVariable("pageSize") Long pageSize, TeacherJobTitlePageDto teacherJobTitlePageDto) {
        Page<TeacherJobTitlePageVo> queryResult = teacherJobTitleService.pageTeacherJobTitle(current, pageSize, teacherJobTitlePageDto);
        return R.success(queryResult);
    }

    /**
     * 回显教师职称信息
     *
     * @param id 需要回显的教师职称ID
     * @return 教师职称信息
     */
    @GetMapping("/info/{id}")
    public R<TeacherJobTitleInfoVo> info(@PathVariable("id") Long id) {
        if (Objects.isNull(id)) {
            ThrowUtils.error(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY);
        }
        TeacherJobTitleInfoVo teacherJobTitleInfoVo = teacherJobTitleService.info(id);
        return R.success(teacherJobTitleInfoVo);
    }

    /**
     * 更新教师职称信息
     *
     * @param teacherJobTitleUpdateDto 更新教师职称信息Dto类
     * @return 返回更新结果
     */
    @PutMapping("/updateTeacherJobTitle")
    public R<String> updateTeacherJobTitle(@RequestBody TeacherJobTitleUpdateDto teacherJobTitleUpdateDto) {
        if (BeanMetaDataUtils.isAnyMetadataEmpty(teacherJobTitleUpdateDto)) {
            ThrowUtils.error(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY);
        }
        teacherJobTitleService.updateTeacherJobTitle(teacherJobTitleUpdateDto);
        return R.success("更新成功！");
    }

    /**
     * 删除教师职称信息
     *
     * @param id 被删除教师职称ID
     * @return 返回删除结果
     */
    @DeleteMapping("/delete/{id}")
    public R<String> delete(@PathVariable("id") Long id) {
        if (Objects.isNull(id)) {
            ThrowUtils.error(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY);
        }
        teacherJobTitleService.delete(id);
        return R.success("删除成功！");
    }

    /**
     * 批量删除教师职称信息
     *
     * @param ids 被删除教师职称ID列表
     * @return 返回删除结果
     */
    @DeleteMapping("/deleteBatch")
    public R<String> deleteBatch(@RequestBody List<Long> ids) {
        if (Objects.isNull(ids) || ids.isEmpty()) {
            ThrowUtils.error(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY);
        }
        if (new HashSet<>(ids).size() != ids.size()) {
            ThrowUtils.error(RCodeEnum.THERE_IS_DUPLICATE_DATA_FOR_BULK_OPERATIONS);
        }
        teacherJobTitleService.deleteBatch(ids);
        return R.success("删除成功！");
    }
}
