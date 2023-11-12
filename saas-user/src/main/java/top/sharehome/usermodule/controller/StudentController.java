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
import top.sharehome.usermodule.model.dto.student.StudentAddDto;
import top.sharehome.usermodule.model.dto.student.StudentPageDto;
import top.sharehome.usermodule.model.dto.student.StudentUpdateDto;
import top.sharehome.usermodule.model.vo.student.StudentInfoVo;
import top.sharehome.usermodule.model.vo.student.StudentPageVo;
import top.sharehome.usermodule.service.StudentService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

/**
 * 学生相关接口
 *
 * @author AntonyCheng
 * @since 2023/7/27 01:49:19
 */
@RestController
@RequestMapping("/student")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class StudentController {
    @Resource
    private StudentService studentService;

    /**
     * 添加学生信息
     *
     * @param studentAddDto 学生信息Dto类
     * @param request       获取操作者的登录信息
     * @return 返回添加结果
     */
    @PostMapping("/add")
    public R<String> add(@RequestBody StudentAddDto studentAddDto, HttpServletRequest request) {
        verifyStudentAddParams(studentAddDto);
        studentService.add(studentAddDto, request);
        return R.success("添加成功！");
    }

    /**
     * 批量添加学生信息
     *
     * @param studentAddDtoList 学生信息Dto类列表
     * @param request           获取操作者的登录信息
     * @return 返回添加结果
     */
    @PostMapping("/addBatch")
    public R<String> addBatch(@RequestBody List<StudentAddDto> studentAddDtoList, HttpServletRequest request) {
        verifyStudentAddBatchParams(studentAddDtoList);
        studentService.addBatch(studentAddDtoList, request);
        return R.success("添加成功！");
    }

    /**
     * 删除学生信息
     *
     * @param id      被删除学生ID
     * @param request 获取操作者的登陆信息
     * @return 返回删除结果
     */
    @DeleteMapping("/delete/{id}")
    public R<String> delete(@PathVariable("id") Long id, HttpServletRequest request) {
        if (Objects.isNull(id)) {
            ThrowUtils.error(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY);
        }
        studentService.delete(id, request);
        return R.success("删除成功！");
    }

    /**
     * 批量删除学生信息
     *
     * @param ids     被删除的学生IDs
     * @param request 获取操作者的登录信息
     * @return 返回删除结果
     */
    @DeleteMapping("/deleteBatch")
    public R<String> deleteBatch(@RequestBody List<Long> ids, HttpServletRequest request) {
        verifyStudentDeleteBatchParams(ids);
        studentService.deleteBatch(ids, request);
        return R.success("删除成功！");
    }

    /**
     * 模糊查询学生信息
     *
     * @param current        当前页
     * @param pageSize       页面项数
     * @param studentPageDto 查询参数
     * @return 返回分页查询结果
     */
    @GetMapping("/page/{current}/{pageSize}")
    public R<Page<StudentPageVo>> pageStudent(@PathVariable("current") Long current, @PathVariable("pageSize") Long pageSize, StudentPageDto studentPageDto) {
        verifyStudentPageParams(studentPageDto);
        Page<StudentPageVo> queryResult = studentService.pageStudent(current, pageSize, studentPageDto);
        return R.success(queryResult);
    }

    /**
     * 回显学生信息
     *
     * @param id 需要会显的学生ID
     * @return 学生信息
     */
    @GetMapping("/info/{id}")
    public R<StudentInfoVo> info(@PathVariable("id") String id) {
        if (Objects.isNull(id)) {
            ThrowUtils.error(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY);
        }
        StudentInfoVo studentInfoVo = studentService.info(id);
        return R.success(studentInfoVo);
    }

    /**
     * 更新学生信息
     *
     * @param studentUpdateDto 更新后的学生信息
     * @param request          获取操作者的登录信息
     * @return 更新结果
     */
    @PutMapping("/updateStudent")
    public R<String> updateStudent(@RequestBody StudentUpdateDto studentUpdateDto, HttpServletRequest request) {
        verifyStudentUpdateParams(studentUpdateDto);
        studentService.updateStudent(studentUpdateDto, request);
        return R.success("更新成功！");
    }

    /**
     * 重置单个学生密码
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
        studentService.resetPassword(id, request);
        return R.success("重置密码成功！");
    }

    /**
     * 重置多个学生密码
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
        studentService.resetPasswords(ids, request);
        return R.success("重置密码成功！");
    }

    /**
     * 校验添加学生信息参数
     *
     * @param studentAddDto 学生信息添加类
     */
    private void verifyStudentAddParams(StudentAddDto studentAddDto) {
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
    }

    /**
     * 校验批量添加学生信息参数
     *
     * @param studentAddDtoList 学生信息添加类列表
     */
    private void verifyStudentAddBatchParams(List<StudentAddDto> studentAddDtoList) {
        studentAddDtoList.forEach(studentAddDto -> {
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
        if (new HashSet<>(studentAddDtoList).size() != studentAddDtoList.size()) {
            ThrowUtils.error(RCodeEnum.THERE_IS_DUPLICATE_DATA_FOR_BULK_OPERATIONS);
        }
    }

    /**
     * 校验批量删除学生信息参数
     *
     * @param ids 学生信息删除ID列表
     */
    private void verifyStudentDeleteBatchParams(List<Long> ids) {
        // 判空
        if (Objects.isNull(ids) || ids.isEmpty()) {
            ThrowUtils.error(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY);
        }
        // 判重
        if (new HashSet<>(ids).size() != ids.size()) {
            ThrowUtils.error(RCodeEnum.THERE_IS_DUPLICATE_DATA_FOR_BULK_OPERATIONS);
        }
    }

    /**
     * 校验学生模糊查询参数
     *
     * @param studentPageDto 学生模糊查询参数
     */
    private void verifyStudentPageParams(StudentPageDto studentPageDto) {
        if (Objects.nonNull(studentPageDto) && Objects.nonNull(studentPageDto.getGender())) {
            // 校验性别数据格式
            if (!(studentPageDto.getGender() == CommonConstant.GENDER_MAN || studentPageDto.getGender() == CommonConstant.GENDER_WOMAN)) {
                ThrowUtils.error(RCodeEnum.DATA_BINARY_IS_NOT_SATISFIED);
            }
        }
        if (Objects.nonNull(studentPageDto) && Objects.nonNull(studentPageDto.getGender())) {
            try {
                // 校验年级格式
                int grade = Integer.parseInt(studentPageDto.getGradeName());
                if (grade > CommonConstant.MAX_GRADE || grade < CommonConstant.MIN_GRADE) {
                    ThrowUtils.error(RCodeEnum.THE_GRADE_FORMAT_IS_INCORRECT);
                }
            } catch (Exception e) {
                ThrowUtils.error(RCodeEnum.THE_GRADE_FORMAT_IS_INCORRECT);
            }
        }
        if (Objects.nonNull(studentPageDto) && Objects.nonNull(studentPageDto.getStatus())) {
            if (!CommonConstant.STUDENT_STATUS_LIST.contains(studentPageDto.getStatus())) {
                ThrowUtils.error(RCodeEnum.STUDENT_STATUS_FORMAT_VERIFICATION_FAILED);
            }
        }
    }

    /**
     * 校验修改学生信息参数
     *
     * @param studentUpdateDto 学生信息修改类
     */
    private void verifyStudentUpdateParams(StudentUpdateDto studentUpdateDto) {
        // 必要参数判空
        if (BeanMetaDataUtils.isAnyMetadataEmpty(studentUpdateDto,
                "majorId", "classNumber", "teacherId", "proTeacherId", "statusDescription")) {
            ThrowUtils.error(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY);
        }
        // 校验手机格式
        if (!ReUtil.isMatch(CommonConstant.MATCHER_PHONE_REGEX, studentUpdateDto.getPhone())) {
            ThrowUtils.error(studentUpdateDto.getPhone() + RCodeEnum.PHONE_FORMAT_VERIFICATION_FAILED.getMessage(),
                    RCodeEnum.PHONE_FORMAT_VERIFICATION_FAILED);
        }
        // 校验姓名格式
        if (!ReUtil.isMatch(CommonConstant.MATCHER_NAME_REGEX, studentUpdateDto.getName())) {
            ThrowUtils.error(RCodeEnum.NAME_FORMAT_VERIFICATION_FAILED);
        }
        // 校验性别数据格式
        if (!(studentUpdateDto.getGender() == CommonConstant.GENDER_MAN || studentUpdateDto.getGender() == CommonConstant.GENDER_WOMAN)) {
            ThrowUtils.error(RCodeEnum.DATA_BINARY_IS_NOT_SATISFIED);
        }
        // 校验班级数据格式
        if (!Objects.isNull(studentUpdateDto.getClassNumber())) {
            if (studentUpdateDto.getClassNumber() > CommonConstant.MAX_CLASS || studentUpdateDto.getClassNumber() < CommonConstant.MIN_CLASS) {
                ThrowUtils.error(RCodeEnum.THE_CLASS_FORMAT_IS_INCORRECT);
            }
        }
        // 校验学生状态
        if (!CommonConstant.STUDENT_STATUS_LIST.contains(studentUpdateDto.getStatus())) {
            ThrowUtils.error(RCodeEnum.STUDENT_STATUS_FORMAT_VERIFICATION_FAILED);
        }
    }
}
