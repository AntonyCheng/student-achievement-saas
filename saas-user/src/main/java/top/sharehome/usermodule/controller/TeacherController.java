package top.sharehome.usermodule.controller;

import cn.hutool.core.util.ReUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import top.sharehome.issuemodule.common.constant.CommonConstant;
import top.sharehome.issuemodule.common.response.R;
import top.sharehome.issuemodule.common.response.RCodeEnum;
import top.sharehome.issuemodule.utils.meta.BeanMetaDataUtils;
import top.sharehome.issuemodule.utils.throwException.ThrowUtils;
import top.sharehome.usermodule.model.dto.teacher.TeacherAddDto;
import top.sharehome.usermodule.model.dto.teacher.TeacherPageDto;
import top.sharehome.usermodule.model.dto.teacher.TeacherUpdateDto;
import top.sharehome.usermodule.model.vo.teacher.TeacherInfoVo;
import top.sharehome.usermodule.model.vo.teacher.TeacherPageVo;
import top.sharehome.usermodule.service.TeacherService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

/**
 * 教师相关接口
 *
 * @author AntonyCheng
 * @since 2023/7/26 20:46:46
 */
@RestController
@RequestMapping("/teacher")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TeacherController {
    @Resource
    private TeacherService teacherService;

    /**
     * 添加教师信息
     *
     * @param teacherAddDto 教师信息Dto类
     * @param request       获取操作者的登录信息
     * @return 返回添加结果
     */
    @PostMapping("/add")
    public R<String> add(@RequestBody TeacherAddDto teacherAddDto, HttpServletRequest request) {
        verifyTeacherAddParams(teacherAddDto);
        teacherService.add(teacherAddDto, request);
        return R.success("添加成功！");
    }

    /**
     * 批量添加教师信息
     *
     * @param teacherAddDtoList 教师信息Dto类列表
     * @param request           获取操作者的登录信息
     * @return 返回添加结果
     */
    @PostMapping("/addBatch")
    public R<String> addBatch(@RequestBody List<TeacherAddDto> teacherAddDtoList, HttpServletRequest request) {
        verifyTeacherAddBatchParams(teacherAddDtoList);
        teacherService.addBatch(teacherAddDtoList, request);
        return R.success("添加成功！");
    }

    /**
     * 删除教师信息
     *
     * @param id      被删除教师ID
     * @param request 获取操作者的登陆信息
     * @return 返回删除结果
     */
    @DeleteMapping("/delete/{id}")
    public R<String> delete(@PathVariable("id") Long id, HttpServletRequest request) {
        if (Objects.isNull(id)) {
            ThrowUtils.error(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY);
        }
        teacherService.delete(id, request);
        return R.success("删除成功！");
    }

    /**
     * 批量删除教师信息
     *
     * @param ids     被删除的教师IDs
     * @param request 获取操作者的登录信息
     * @return 返回删除结果
     */
    @DeleteMapping("/deleteBatch")
    public R<String> deleteBatch(@RequestBody List<Long> ids, HttpServletRequest request) {
        if (Objects.isNull(ids) || ids.isEmpty()) {
            ThrowUtils.error(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY);
        }
        if (new HashSet<>(ids).size() != ids.size()) {
            ThrowUtils.error(RCodeEnum.THERE_IS_DUPLICATE_DATA_FOR_BULK_OPERATIONS);
        }
        teacherService.deleteBatch(ids, request);
        return R.success("删除成功！");
    }

    /**
     * 模糊查询教师信息
     *
     * @param current        当前页
     * @param pageSize       页面项数
     * @param teacherPageDto 查询参数
     * @return 返回分页查询结果
     */
    @GetMapping("/page/{current}/{pageSize}")
    public R<Page<TeacherPageVo>> pageTeacher(@PathVariable("current") Long current, @PathVariable("pageSize") Long pageSize, TeacherPageDto teacherPageDto) {
        if (Objects.nonNull(teacherPageDto) && Objects.nonNull(teacherPageDto.getGender())) {
            // 校验性别数据格式
            if (!(teacherPageDto.getGender() == CommonConstant.GENDER_MAN || teacherPageDto.getGender() == CommonConstant.GENDER_WOMAN)) {
                ThrowUtils.error(RCodeEnum.DATA_BINARY_IS_NOT_SATISFIED);
            }
        }
        Page<TeacherPageVo> queryResult = teacherService.pageTeacher(current, pageSize, teacherPageDto);
        return R.success(queryResult);
    }

    /**
     * 回显教师信息
     *
     * @param id 需要会显的教师ID
     * @return 教师信息
     */
    @GetMapping("/info/{id}")
    public R<TeacherInfoVo> info(@PathVariable("id") String id) {
        if (Objects.isNull(id)) {
            ThrowUtils.error(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY);
        }
        TeacherInfoVo teacherInfoVo = teacherService.info(id);
        return R.success(teacherInfoVo);
    }

    /**
     * 更新教师信息
     *
     * @param teacherUpdateDto 更新后的教师信息
     * @param request          获取操作者的登录信息
     * @return 更新结果
     */
    @PutMapping("/updateTeacher")
    public R<String> updateTeacher(@RequestBody TeacherUpdateDto teacherUpdateDto, HttpServletRequest request) {
        verifyStudentUpdateParams(teacherUpdateDto);
        teacherService.updateStudent(teacherUpdateDto, request);
        return R.success("更新成功！");
    }

    /**
     * 重置单个教师密码
     *
     * @param id      需要重置的用户ID
     * @param request 获取操作者的登录信息
     * @return 重置结果
     */
    @PutMapping("/resetPassword/{id}")
    public R<String> resetPassword(@PathVariable("id") Long id, HttpServletRequest request) {
        if (Objects.isNull(id)) {
            ThrowUtils.error(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY);
        }
        teacherService.resetPassword(id, request);
        return R.success("重置密码成功！");
    }

    /**
     * 重置多个教师密码
     *
     * @param ids     需要重置的用户ID列表
     * @param request 获取操作者的登录信息
     * @return 重置结果
     */
    @PutMapping("/resetPasswords")
    public R<String> resetPasswords(@RequestBody List<Long> ids, HttpServletRequest request) {
        if (Objects.isNull(ids) || ids.isEmpty()) {
            ThrowUtils.error(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY);
        }
        if (new HashSet<>(ids).size() != ids.size()) {
            ThrowUtils.error(RCodeEnum.THERE_IS_DUPLICATE_DATA_FOR_BULK_OPERATIONS);
        }
        teacherService.resetPasswords(ids, request);
        return R.success("重置密码成功！");
    }

    /**
     * 校验添加教师信息参数
     *
     * @param teacherAddDto 教师信息添加类
     */
    private void verifyTeacherAddParams(TeacherAddDto teacherAddDto) {
        // 判空
        if (BeanMetaDataUtils.isAnyMetadataEmpty(teacherAddDto)) {
            ThrowUtils.error(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY);
        }
        // 校验手机格式
        if (!ReUtil.isMatch(CommonConstant.MATCHER_PHONE_REGEX, teacherAddDto.getPhone())) {
            ThrowUtils.error(teacherAddDto.getPhone() + RCodeEnum.PHONE_FORMAT_VERIFICATION_FAILED.getMessage(),
                    RCodeEnum.PHONE_FORMAT_VERIFICATION_FAILED);
        }
        // 校验姓名格式
        if (!ReUtil.isMatch(CommonConstant.MATCHER_NAME_REGEX, teacherAddDto.getName())) {
            ThrowUtils.error(RCodeEnum.NAME_FORMAT_VERIFICATION_FAILED);
        }
        // 校验性别数据格式
        if (!(teacherAddDto.getGender() == CommonConstant.GENDER_MAN || teacherAddDto.getGender() == CommonConstant.GENDER_WOMAN)) {
            ThrowUtils.error(RCodeEnum.DATA_BINARY_IS_NOT_SATISFIED);
        }
    }

    /**
     * 校验批量添加教师信息参数
     *
     * @param teacherAddDtoList 教师信息添加类列表
     */
    private void verifyTeacherAddBatchParams(List<TeacherAddDto> teacherAddDtoList) {
        teacherAddDtoList.forEach(studentAddDto -> {
            // 判空
            if (BeanMetaDataUtils.isAnyMetadataEmpty(studentAddDto)) {
                ThrowUtils.error(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY);
            }
            // 校验手机格式
            if (!ReUtil.isMatch(CommonConstant.MATCHER_PHONE_REGEX, studentAddDto.getPhone())) {
                ThrowUtils.error(studentAddDto.getPhone() + RCodeEnum.PHONE_FORMAT_VERIFICATION_FAILED.getMessage(),
                        RCodeEnum.PHONE_FORMAT_VERIFICATION_FAILED);
            }
            // 校验姓名格式
            if (!ReUtil.isMatch(CommonConstant.MATCHER_NAME_REGEX, studentAddDto.getName())) {
                ThrowUtils.error(RCodeEnum.NAME_FORMAT_VERIFICATION_FAILED);
            }
            // 校验性别数据格式
            if (!(studentAddDto.getGender() == CommonConstant.GENDER_MAN || studentAddDto.getGender() == CommonConstant.GENDER_WOMAN)) {
                ThrowUtils.error(RCodeEnum.DATA_BINARY_IS_NOT_SATISFIED);
            }
        });
        // 判重
        if (new HashSet<>(teacherAddDtoList).size() != teacherAddDtoList.size()) {
            ThrowUtils.error(RCodeEnum.THERE_IS_DUPLICATE_DATA_FOR_BULK_OPERATIONS);
        }
    }

    /**
     * 校验修改教师信息参数
     *
     * @param teacherUpdateDto 教师信息修改类
     */
    private void verifyStudentUpdateParams(TeacherUpdateDto teacherUpdateDto) {
        // 必要参数判空
        if (BeanMetaDataUtils.isAnyMetadataEmpty(teacherUpdateDto,
                "roleIds", "jobTitleId")) {
            ThrowUtils.error(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY);
        }
        // 校验手机格式
        if (!ReUtil.isMatch(CommonConstant.MATCHER_PHONE_REGEX, teacherUpdateDto.getPhone())) {
            ThrowUtils.error(teacherUpdateDto.getPhone() + RCodeEnum.PHONE_FORMAT_VERIFICATION_FAILED.getMessage(),
                    RCodeEnum.PHONE_FORMAT_VERIFICATION_FAILED);
        }
        // 校验姓名格式
        if (!ReUtil.isMatch(CommonConstant.MATCHER_NAME_REGEX, teacherUpdateDto.getName())) {
            ThrowUtils.error(RCodeEnum.NAME_FORMAT_VERIFICATION_FAILED);
        }
        // 校验性别数据格式
        if (!(teacherUpdateDto.getGender() == CommonConstant.GENDER_MAN || teacherUpdateDto.getGender() == CommonConstant.GENDER_WOMAN)) {
            ThrowUtils.error(RCodeEnum.DATA_BINARY_IS_NOT_SATISFIED);
        }
    }
}
