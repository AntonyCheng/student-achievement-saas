package top.sharehome.usermodule.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import top.sharehome.issuemodule.common.response.R;
import top.sharehome.issuemodule.common.response.RCodeEnum;
import top.sharehome.issuemodule.utils.meta.BeanMetaDataUtils;
import top.sharehome.issuemodule.utils.throwException.ThrowUtils;
import top.sharehome.usermodule.model.dto.teacherRole.TeacherRoleAddDto;
import top.sharehome.usermodule.model.dto.teacherRole.TeacherRolePageDto;
import top.sharehome.usermodule.model.dto.teacherRole.TeacherRoleUpdateDto;
import top.sharehome.usermodule.model.vo.teacherRole.TeacherRoleInfoVo;
import top.sharehome.usermodule.model.vo.teacherRole.TeacherRolePageVo;
import top.sharehome.usermodule.service.TeacherRoleService;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 教师角色相关接口
 *
 * @author AntonyCheng
 * @since 2023/8/18 23:42:54
 */
@RestController
@RequestMapping("/teacherRole")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TeacherRoleController {
    @Resource
    private TeacherRoleService teacherRoleService;

    /**
     * 添加教师角色
     *
     * @param teacherRoleAddDto 教师角色信息Dto类
     * @return 返回添加结果
     */
    @PostMapping("/add")
    public R<String> add(@RequestBody TeacherRoleAddDto teacherRoleAddDto) {
        if (BeanMetaDataUtils.isAnyMetadataEmpty(teacherRoleAddDto)) {
            ThrowUtils.error(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY);
        }
        teacherRoleService.add(teacherRoleAddDto);
        return R.success("添加成功！");
    }

    /**
     * 批量添加教师角色
     *
     * @param teacherRoleAddDtoList 教师角色信息Dto类列表
     * @return 返回添加结果
     */
    @PostMapping("/addBatch")
    public R<String> addBatch(@RequestBody List<TeacherRoleAddDto> teacherRoleAddDtoList) {
        if (Objects.isNull(teacherRoleAddDtoList) || teacherRoleAddDtoList.isEmpty()) {
            ThrowUtils.error(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY);
        }
        for (TeacherRoleAddDto teacherRoleAddDto : teacherRoleAddDtoList) {
            if (BeanMetaDataUtils.isAnyMetadataEmpty(teacherRoleAddDto)) {
                ThrowUtils.error(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY);
            }
        }
        if (new HashSet<>(teacherRoleAddDtoList).size() != teacherRoleAddDtoList.size()) {
            ThrowUtils.error(RCodeEnum.THERE_IS_DUPLICATE_DATA_FOR_BULK_OPERATIONS);
        }
        teacherRoleService.addBatch(teacherRoleAddDtoList);
        return R.success("添加成功！");
    }

    /**
     * 获取已有教师角色列表
     *
     * @return 返回所有已有的教师角色列表
     */
    @GetMapping("/get")
    public R<Map<Long, String>> get() {
        Map<Long, String> result = teacherRoleService.get();
        return R.success(result);
    }

    /**
     * 模糊查询教师角色信息
     *
     * @param current            当前页
     * @param pageSize           页面项数
     * @param teacherRolePageDto 查询参数
     * @return 返回分页查询结果
     */
    @GetMapping("/page/{current}/{pageSize}")
    public R<Page<TeacherRolePageVo>> pageTeacherRole(@PathVariable("current") Long current, @PathVariable("pageSize") Long pageSize, TeacherRolePageDto teacherRolePageDto) {
        Page<TeacherRolePageVo> queryResult = teacherRoleService.pageTeacherRole(current, pageSize, teacherRolePageDto);
        return R.success(queryResult);
    }

    /**
     * 回显教师角色信息
     *
     * @param id 需要回显的教师角色ID
     * @return 教师角色信息
     */
    @GetMapping("/info/{id}")
    public R<TeacherRoleInfoVo> info(@PathVariable("id") Long id) {
        if (Objects.isNull(id)) {
            ThrowUtils.error(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY);
        }
        TeacherRoleInfoVo teacherRoleInfoVo = teacherRoleService.info(id);
        return R.success(teacherRoleInfoVo);
    }

    /**
     * 更新教师角色信息
     *
     * @param teacherRoleUpdateDto 更新教师角色信息Dto类
     * @return 返回更新结果
     */
    @PutMapping("/updateTeacherRole")
    public R<String> updateTeacherRole(@RequestBody TeacherRoleUpdateDto teacherRoleUpdateDto) {
        if (BeanMetaDataUtils.isAnyMetadataEmpty(teacherRoleUpdateDto)) {
            ThrowUtils.error(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY);
        }
        teacherRoleService.updateTeacherRole(teacherRoleUpdateDto);
        return R.success("更新成功！");
    }

    /**
     * 删除教师角色信息
     *
     * @param id 被删除教师角色ID
     * @return 返回删除结果
     */
    @DeleteMapping("/delete/{id}")
    public R<String> delete(@PathVariable("id") Long id) {
        if (Objects.isNull(id)) {
            ThrowUtils.error(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY);
        }
        teacherRoleService.delete(id);
        return R.success("删除成功！");
    }

    /**
     * 批量删除教师角色信息
     *
     * @param ids 被删除教师角色ID列表
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
        teacherRoleService.deleteBatch(ids);
        return R.success("删除成功！");
    }
}
