package top.sharehome.usermodule.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import top.sharehome.issuemodule.common.constant.CommonConstant;
import top.sharehome.issuemodule.common.response.R;
import top.sharehome.issuemodule.common.response.RCodeEnum;
import top.sharehome.issuemodule.utils.meta.BeanMetaDataUtils;
import top.sharehome.issuemodule.utils.throwException.ThrowUtils;
import top.sharehome.usermodule.model.dto.major.MajorAddDto;
import top.sharehome.usermodule.model.dto.major.MajorPageDto;
import top.sharehome.usermodule.model.dto.major.MajorUpdateDto;
import top.sharehome.usermodule.model.vo.major.MajorInfoVo;
import top.sharehome.usermodule.model.vo.major.MajorPageVo;
import top.sharehome.usermodule.service.MajorService;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 专业相关接口
 *
 * @author AntonyCheng
 * @since 2023/7/27 21:24:57
 */
@RestController
@RequestMapping("/major")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MajorController {
    @Resource
    private MajorService majorService;

    /**
     * 添加专业信息
     *
     * @param majorAddDto 专业信息Dto类
     * @return 返回添加结果
     */
    @PostMapping("/add")
    public R<String> add(@RequestBody MajorAddDto majorAddDto) {
        verifyMajorAddParams(majorAddDto);
        majorService.add(majorAddDto);
        return R.success("添加成功！");
    }

    /**
     * 批量添加专业信息
     *
     * @param majorAddDtoList 专业信息Dto类列表
     * @return 返回添加结果
     */
    @PostMapping("/addBatch")
    public R<String> addBatch(@RequestBody List<MajorAddDto> majorAddDtoList) {
        verifyMajorAddBatchParams(majorAddDtoList);
        majorService.addBatch(majorAddDtoList);
        return R.success("添加成功！");
    }

    /**
     * 获取已有年级列表
     *
     * @return 返回所有已有的年级列表
     */
    @GetMapping("/getGrades")
    public R<List<String>> getGrades() {
        List<String> grades = majorService.getGrades();
        return R.success(grades);
    }

    /**
     * 根据年级获取专业ID和名称
     *
     * @param grade 指定的年级
     * @return 返回专业ID和名称
     */
    @GetMapping("/getMajor/{grade}")
    public R<Map<Long, String>> getMajor(@PathVariable("grade") String grade) {
        if (Objects.isNull(grade)) {
            ThrowUtils.error(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY);
        }
        Map<Long, String> result = majorService.getMajor(grade);
        return R.success(result);
    }

    /**
     * 根据专业ID获取该专业的班级
     *
     * @param major 指定的专业ID
     * @return 返回班级列表
     */
    @GetMapping("/getClasses/{major}")
    public R<List<Integer>> getClasses(@PathVariable("major") Long major) {
        if (Objects.isNull(major)) {
            ThrowUtils.error(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY);
        }
        List<Integer> classes = majorService.getClasses(major);
        return R.success(classes);
    }

    /**
     * 模糊查询专业信息
     *
     * @param current      当前页
     * @param pageSize     页面项数
     * @param majorPageDto 查询参数
     * @return 返回分页查询结果
     */
    @GetMapping("/page/{current}/{pageSize}")
    public R<Page<MajorPageVo>> pageMajor(@PathVariable("current") Long current, @PathVariable("pageSize") Long pageSize, MajorPageDto majorPageDto) {
        if (Objects.nonNull(majorPageDto) && Objects.nonNull(majorPageDto.getGrade())) {
            try {
                int grade = Integer.parseInt(majorPageDto.getGrade());
                if (grade > CommonConstant.MAX_GRADE || grade < CommonConstant.MIN_GRADE) {
                    ThrowUtils.error(RCodeEnum.THE_GRADE_FORMAT_IS_INCORRECT);
                }
            } catch (Exception e) {
                ThrowUtils.error(RCodeEnum.THE_GRADE_FORMAT_IS_INCORRECT);
            }
        }
        Page<MajorPageVo> queryResult = majorService.pageMajor(current, pageSize, majorPageDto);
        return R.success(queryResult);
    }

    /**
     * 回显专业信息
     *
     * @param id 需要回显的专业ID
     * @return 专业信息
     */
    @GetMapping("/info/{id}")
    public R<MajorInfoVo> info(@PathVariable("id") Long id) {
        if (Objects.isNull(id)) {
            ThrowUtils.error(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY);
        }
        MajorInfoVo majorInfoVo = majorService.info(id);
        return R.success(majorInfoVo);
    }

    /**
     * 更新专业信息
     *
     * @param majorUpdateDto 更新专业信息Dto类
     * @return 返回更新结果
     */
    @PutMapping("/updateMajor")
    public R<String> updateMajor(@RequestBody MajorUpdateDto majorUpdateDto) {
        verifyMajorUpdateParams(majorUpdateDto);
        majorService.updateMajor(majorUpdateDto);
        return R.success("更新成功！");
    }

    /**
     * 删除专业信息
     *
     * @param id 被删除专业ID
     * @return 返回删除结果
     */
    @DeleteMapping("/delete/{id}")
    public R<String> delete(@PathVariable("id") Long id) {
        if (Objects.isNull(id)) {
            ThrowUtils.error(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY);
        }
        majorService.delete(id);
        return R.success("删除成功！");
    }

    /**
     * 批量删除专业信息
     *
     * @param ids 被删除专业ID列表
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
        majorService.deleteBatch(ids);
        return R.success("删除成功！");
    }

    /**
     * 校验添加专业信息参数
     *
     * @param majorAddDto 专业信息添加类
     */
    private void verifyMajorAddParams(MajorAddDto majorAddDto) {
        // 判空
        if (BeanMetaDataUtils.isAnyMetadataEmpty(majorAddDto)) {
            ThrowUtils.error(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY);
        }
        // 校验年级范围
        try {
            int grade = Integer.parseInt(majorAddDto.getGrade());
            if (grade > CommonConstant.MAX_GRADE || grade < CommonConstant.MIN_GRADE) {
                ThrowUtils.error(RCodeEnum.THE_GRADE_FORMAT_IS_INCORRECT);
            }
        } catch (Exception e) {
            ThrowUtils.error(RCodeEnum.THE_GRADE_FORMAT_IS_INCORRECT);
        }
        // 校验班级
        majorAddDto.getClasses().forEach(classNumber -> {
            if (classNumber > CommonConstant.MAX_CLASS || classNumber < CommonConstant.MIN_CLASS) {
                ThrowUtils.error(RCodeEnum.THE_CLASS_FORMAT_IS_INCORRECT);
            }
        });
    }

    /**
     * 校验批量添加专业信息参数
     *
     * @param majorAddDtoList 专业信息添加类列表
     */
    private void verifyMajorAddBatchParams(List<MajorAddDto> majorAddDtoList) {
        // 判空
        if (Objects.isNull(majorAddDtoList) || majorAddDtoList.isEmpty()) {
            ThrowUtils.error(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY);
        }
        try {
            majorAddDtoList.forEach(majorAddDto -> {
                int grade = Integer.parseInt(majorAddDto.getGrade());
                if (grade > CommonConstant.MAX_GRADE || grade < CommonConstant.MIN_GRADE) {
                    ThrowUtils.error(RCodeEnum.THE_GRADE_FORMAT_IS_INCORRECT);
                }
                majorAddDto.getClasses().forEach(aclass -> {
                    if (aclass > CommonConstant.MAX_CLASS || aclass < CommonConstant.MIN_CLASS) {
                        ThrowUtils.error(RCodeEnum.THE_CLASS_FORMAT_IS_INCORRECT);
                    }
                });
            });
        } catch (Exception e) {
            ThrowUtils.error(RCodeEnum.THE_GRADE_FORMAT_IS_INCORRECT);
        }
        if (new HashSet<>(majorAddDtoList).size() != majorAddDtoList.size()) {
            ThrowUtils.error(RCodeEnum.THERE_IS_DUPLICATE_DATA_FOR_BULK_OPERATIONS);
        }
    }

    /**
     * 校验专业信息更新参数
     *
     * @param majorUpdateDto 更新专业信息类
     */
    private void verifyMajorUpdateParams(MajorUpdateDto majorUpdateDto) {
        // 判空
        if (BeanMetaDataUtils.isAnyMetadataEmpty(majorUpdateDto)) {
            ThrowUtils.error(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY);
        }
        // 校验年级范围
        try {
            int grade = Integer.parseInt(majorUpdateDto.getGrade());
            if (grade > CommonConstant.MAX_GRADE || grade < CommonConstant.MIN_GRADE) {
                ThrowUtils.error(RCodeEnum.THE_GRADE_FORMAT_IS_INCORRECT);
            }
        } catch (Exception e) {
            ThrowUtils.error(RCodeEnum.THE_GRADE_FORMAT_IS_INCORRECT);
        }
        // 校验班级
        majorUpdateDto.getClasses().forEach(classNumber -> {
            if (classNumber > CommonConstant.MAX_CLASS || classNumber < CommonConstant.MIN_CLASS) {
                ThrowUtils.error(RCodeEnum.THE_CLASS_FORMAT_IS_INCORRECT);
            }
        });
    }
}
