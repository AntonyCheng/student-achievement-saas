package top.sharehome.usermodule.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import top.sharehome.issuemodule.common.response.R;
import top.sharehome.issuemodule.common.response.RCodeEnum;
import top.sharehome.issuemodule.utils.meta.BeanMetaDataUtils;
import top.sharehome.issuemodule.utils.throwException.ThrowUtils;
import top.sharehome.usermodule.model.dto.achievementType.AchievementTypeAddDto;
import top.sharehome.usermodule.model.dto.achievementType.AchievementTypePageDto;
import top.sharehome.usermodule.model.dto.achievementType.AchievementTypeUpdateDto;
import top.sharehome.usermodule.model.vo.achievementType.AchievementTypeInfoVo;
import top.sharehome.usermodule.model.vo.achievementType.AchievementTypePageVo;
import top.sharehome.usermodule.service.AchievementTypeService;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 学生科研成果类型相关接口
 *
 * @author AntonyCheng
 * @since 2023/8/19 11:26:05
 */
@RestController
@RequestMapping("/achievementType")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AchievementTypeController {
    @Resource
    private AchievementTypeService achievementTypeService;

    /**
     * 添加学生科研成果类型
     *
     * @param achievementTypeAddDto 学生科研成果类型信息Dto类
     * @return 返回添加结果
     */
    @PostMapping("/add")
    public R<String> add(@RequestBody AchievementTypeAddDto achievementTypeAddDto) {
        if (BeanMetaDataUtils.isAnyMetadataEmpty(achievementTypeAddDto)) {
            ThrowUtils.error(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY);
        }
        achievementTypeService.add(achievementTypeAddDto);
        return R.success("添加成功！");
    }

    /**
     * 批量添加学生科研成果类型
     *
     * @param achievementTypeAddDtoList 学生科研成果类型信息Dto类列表
     * @return 返回添加结果
     */
    @PostMapping("/addBatch")
    public R<String> addBatch(@RequestBody List<AchievementTypeAddDto> achievementTypeAddDtoList) {
        verifyAchievementTypeAddBatchParams(achievementTypeAddDtoList);
        achievementTypeService.addBatch(achievementTypeAddDtoList);
        return R.success("添加成功！");
    }

    /**
     * 获取已有学生科研成果类型列表
     *
     * @return 返回所有已有的学生科研成果类型列表
     */
    @GetMapping("/get")
    public R<Map<Long, String>> get() {
        Map<Long, String> result = achievementTypeService.get();
        return R.success(result);
    }

    /**
     * 模糊查询学生科研成果类型信息
     *
     * @param current                当前页
     * @param pageSize               页面项数
     * @param achievementTypePageDto 查询参数
     * @return 返回分页查询结果
     */
    @GetMapping("/page/{current}/{pageSize}")
    public R<Page<AchievementTypePageVo>> pageAchievementType(@PathVariable("current") Long current, @PathVariable("pageSize") Long pageSize, AchievementTypePageDto achievementTypePageDto) {
        Page<AchievementTypePageVo> queryResult = achievementTypeService.pageAchievementType(current, pageSize, achievementTypePageDto);
        return R.success(queryResult);
    }

    /**
     * 回显学生科研成果类型信息
     *
     * @param id 需要回显的学生科研成果类型ID
     * @return 学生科研成果类型信息
     */
    @GetMapping("/info/{id}")
    public R<AchievementTypeInfoVo> info(@PathVariable("id") Long id) {
        if (Objects.isNull(id)) {
            ThrowUtils.error(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY);
        }
        AchievementTypeInfoVo achievementTypeInfoVo = achievementTypeService.info(id);
        return R.success(achievementTypeInfoVo);
    }

    /**
     * 更新学生科研成果类型信息
     *
     * @param achievementTypeUpdateDto 更新学生科研成果类型信息Dto类
     * @return 返回更新结果
     */
    @PutMapping("/updateAchievementType")
    public R<String> updateAchievementType(@RequestBody AchievementTypeUpdateDto achievementTypeUpdateDto) {
        if (BeanMetaDataUtils.isAnyMetadataEmpty(achievementTypeUpdateDto)) {
            ThrowUtils.error(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY);
        }
        achievementTypeService.updateAchievementType(achievementTypeUpdateDto);
        return R.success("更新成功！");
    }

    /**
     * 删除学生科研成果类型信息
     *
     * @param id 被删除学生科研成果类型ID
     * @return 返回删除结果
     */
    @DeleteMapping("/delete/{id}")
    public R<String> delete(@PathVariable("id") Long id) {
        if (Objects.isNull(id)) {
            ThrowUtils.error(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY);
        }
        achievementTypeService.delete(id);
        return R.success("删除成功！");
    }

    /**
     * 批量删除学生科研成果类型信息
     *
     * @param ids 被删除学生科研成果类型ID列表
     * @return 返回删除结果
     */
    @DeleteMapping("/deleteBatch")
    public R<String> deleteBatch(@RequestBody List<Long> ids) {
        verifyAchievementTypePageParams(ids);
        achievementTypeService.deleteBatch(ids);
        return R.success("删除成功！");
    }

    /**
     * 校验批量添加成果类型的参数
     *
     * @param achievementTypeAddDtoList 审核参数类
     */
    private void verifyAchievementTypeAddBatchParams(List<AchievementTypeAddDto> achievementTypeAddDtoList) {
        if (Objects.isNull(achievementTypeAddDtoList) || achievementTypeAddDtoList.isEmpty()) {
            ThrowUtils.error(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY);
        }
        for (AchievementTypeAddDto achievementTypeAddDto : achievementTypeAddDtoList) {
            if (BeanMetaDataUtils.isAnyMetadataEmpty(achievementTypeAddDto)) {
                ThrowUtils.error(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY);
            }
        }
        if (new HashSet<>(achievementTypeAddDtoList).size() != achievementTypeAddDtoList.size()) {
            ThrowUtils.error(RCodeEnum.THERE_IS_DUPLICATE_DATA_FOR_BULK_OPERATIONS);
        }
    }

    /**
     * 校验成果类型分页参数
     *
     * @param ids 审核参数类
     */
    private void verifyAchievementTypePageParams(List<Long> ids) {
        if (Objects.isNull(ids) || ids.isEmpty()) {
            ThrowUtils.error(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY);
        }
        if (new HashSet<>(ids).size() != ids.size()) {
            ThrowUtils.error(RCodeEnum.THERE_IS_DUPLICATE_DATA_FOR_BULK_OPERATIONS);
        }
    }
}
