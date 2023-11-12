package top.sharehome.usermodule.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import top.sharehome.issuemodule.common.response.R;
import top.sharehome.issuemodule.common.response.RCodeEnum;
import top.sharehome.issuemodule.utils.meta.BeanMetaDataUtils;
import top.sharehome.issuemodule.utils.throwException.ThrowUtils;
import top.sharehome.usermodule.model.dto.competitionType.CompetitionTypeAddDto;
import top.sharehome.usermodule.model.dto.competitionType.CompetitionTypePageDto;
import top.sharehome.usermodule.model.dto.competitionType.CompetitionTypeUpdateDto;
import top.sharehome.usermodule.model.vo.competitionType.CompetitionTypeInfoVo;
import top.sharehome.usermodule.model.vo.competitionType.CompetitionTypePageVo;
import top.sharehome.usermodule.service.CompetitionTypeService;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 学生学科竞赛类型相关接口
 *
 * @author AntonyCheng
 * @since 2023/8/19 11:23:41
 */
@RestController
@RequestMapping("/competitionType")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CompetitionTypeController {
    @Resource
    private CompetitionTypeService competitionTypeService;

    /**
     * 添加学生学科竞赛类型
     *
     * @param competitionTypeAddDto 学生学科竞赛类型信息Dto类
     * @return 返回添加结果
     */
    @PostMapping("/add")
    public R<String> add(@RequestBody CompetitionTypeAddDto competitionTypeAddDto) {
        if (BeanMetaDataUtils.isAnyMetadataEmpty(competitionTypeAddDto)) {
            ThrowUtils.error(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY);
        }
        competitionTypeService.add(competitionTypeAddDto);
        return R.success("添加成功！");
    }

    /**
     * 批量添加学生学科竞赛类型
     *
     * @param competitionTypeAddDtoList 学生学科竞赛类型信息Dto类列表
     * @return 返回添加结果
     */
    @PostMapping("/addBatch")
    public R<String> addBatch(@RequestBody List<CompetitionTypeAddDto> competitionTypeAddDtoList) {
        if (Objects.isNull(competitionTypeAddDtoList) || competitionTypeAddDtoList.isEmpty()) {
            ThrowUtils.error(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY);
        }
        for (CompetitionTypeAddDto competitionTypeAddDto : competitionTypeAddDtoList) {
            if (BeanMetaDataUtils.isAnyMetadataEmpty(competitionTypeAddDto)) {
                ThrowUtils.error(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY);
            }
        }
        if (new HashSet<>(competitionTypeAddDtoList).size() != competitionTypeAddDtoList.size()) {
            ThrowUtils.error(RCodeEnum.THERE_IS_DUPLICATE_DATA_FOR_BULK_OPERATIONS);
        }
        competitionTypeService.addBatch(competitionTypeAddDtoList);
        return R.success("添加成功！");
    }

    /**
     * 获取已有学生学科竞赛类型列表
     *
     * @return 返回所有已有的学生学科竞赛类型列表
     */
    @GetMapping("/get")
    public R<Map<Long, String>> get() {
        Map<Long, String> result = competitionTypeService.get();
        return R.success(result);
    }

    /**
     * 模糊查询学生学科竞赛类型信息
     *
     * @param current                当前页
     * @param pageSize               页面项数
     * @param competitionTypePageDto 查询参数
     * @return 返回分页查询结果
     */
    @GetMapping("/page/{current}/{pageSize}")
    public R<Page<CompetitionTypePageVo>> pageCompetitionType(@PathVariable("current") Long current, @PathVariable("pageSize") Long pageSize, CompetitionTypePageDto competitionTypePageDto) {
        Page<CompetitionTypePageVo> queryResult = competitionTypeService.pageCompetitionType(current, pageSize, competitionTypePageDto);
        return R.success(queryResult);
    }

    /**
     * 回显学生学科竞赛类型信息
     *
     * @param id 需要回显的学生学科竞赛类型ID
     * @return 学生学科竞赛类型信息
     */
    @GetMapping("/info/{id}")
    public R<CompetitionTypeInfoVo> info(@PathVariable("id") Long id) {
        if (Objects.isNull(id)) {
            ThrowUtils.error(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY);
        }
        CompetitionTypeInfoVo competitionTypeInfoVo = competitionTypeService.info(id);
        return R.success(competitionTypeInfoVo);
    }

    /**
     * 更新学生学科竞赛类型信息
     *
     * @param competitionTypeUpdateDto 更新学生学科竞赛类型信息Dto类
     * @return 返回更新结果
     */
    @PutMapping("/updateCompetitionType")
    public R<String> updateCompetitionType(@RequestBody CompetitionTypeUpdateDto competitionTypeUpdateDto) {
        if (BeanMetaDataUtils.isAnyMetadataEmpty(competitionTypeUpdateDto)) {
            ThrowUtils.error(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY);
        }
        competitionTypeService.updateCompetitionType(competitionTypeUpdateDto);
        return R.success("更新成功！");
    }

    /**
     * 删除学生学科竞赛类型信息
     *
     * @param id 被删除学生学科竞赛类型ID
     * @return 返回删除结果
     */
    @DeleteMapping("/delete/{id}")
    public R<String> delete(@PathVariable("id") Long id) {
        if (Objects.isNull(id)) {
            ThrowUtils.error(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY);
        }
        competitionTypeService.delete(id);
        return R.success("删除成功！");
    }

    /**
     * 批量删除学生学科竞赛类型信息
     *
     * @param ids 被删除学生学科竞赛类型ID列表
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
        competitionTypeService.deleteBatch(ids);
        return R.success("删除成功！");
    }
}
