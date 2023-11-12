package top.sharehome.usermodule.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import top.sharehome.issuemodule.common.response.R;
import top.sharehome.issuemodule.common.response.RCodeEnum;
import top.sharehome.issuemodule.utils.meta.BeanMetaDataUtils;
import top.sharehome.issuemodule.utils.throwException.ThrowUtils;
import top.sharehome.usermodule.model.dto.honorType.HonorTypeAddDto;
import top.sharehome.usermodule.model.dto.honorType.HonorTypePageDto;
import top.sharehome.usermodule.model.dto.honorType.HonorTypeUpdateDto;
import top.sharehome.usermodule.model.vo.honorType.HonorTypeInfoVo;
import top.sharehome.usermodule.model.vo.honorType.HonorTypePageVo;
import top.sharehome.usermodule.service.HonorTypeService;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 学生在校荣誉类型相关接口
 *
 * @author AntonyCheng
 * @since 2023/8/19 00:12:33
 */
@RestController
@RequestMapping("/honorType")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class HonorTypeController {
    @Resource
    private HonorTypeService honorTypeService;

    /**
     * 添加学生在校荣誉类型
     *
     * @param honorTypeAddDto 学生在校荣誉类型信息Dto类
     * @return 返回添加结果
     */
    @PostMapping("/add")
    public R<String> add(@RequestBody HonorTypeAddDto honorTypeAddDto) {
        if (BeanMetaDataUtils.isAnyMetadataEmpty(honorTypeAddDto)) {
            ThrowUtils.error(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY);
        }
        honorTypeService.add(honorTypeAddDto);
        return R.success("添加成功！");
    }

    /**
     * 批量添加学生在校荣誉类型
     *
     * @param honorTypeAddDtoList 学生在校荣誉类型信息Dto类列表
     * @return 返回添加结果
     */
    @PostMapping("/addBatch")
    public R<String> addBatch(@RequestBody List<HonorTypeAddDto> honorTypeAddDtoList) {
        if (Objects.isNull(honorTypeAddDtoList) || honorTypeAddDtoList.isEmpty()) {
            ThrowUtils.error(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY);
        }
        for (HonorTypeAddDto honorTypeAddDto : honorTypeAddDtoList) {
            if (BeanMetaDataUtils.isAnyMetadataEmpty(honorTypeAddDto)) {
                ThrowUtils.error(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY);
            }
        }
        if (new HashSet<>(honorTypeAddDtoList).size() != honorTypeAddDtoList.size()) {
            ThrowUtils.error(RCodeEnum.THERE_IS_DUPLICATE_DATA_FOR_BULK_OPERATIONS);
        }
        honorTypeService.addBatch(honorTypeAddDtoList);
        return R.success("添加成功！");
    }

    /**
     * 获取已有学生在校荣誉类型列表
     *
     * @return 返回所有已有的学生在校荣誉类型列表
     */
    @GetMapping("/get")
    public R<Map<Long, String>> get() {
        Map<Long, String> result = honorTypeService.get();
        return R.success(result);
    }

    /**
     * 模糊查询学生在校荣誉类型信息
     *
     * @param current          当前页
     * @param pageSize         页面项数
     * @param honorTypePageDto 查询参数
     * @return 返回分页查询结果
     */
    @GetMapping("/page/{current}/{pageSize}")
    public R<Page<HonorTypePageVo>> pageHonorType(@PathVariable("current") Long current, @PathVariable("pageSize") Long pageSize, HonorTypePageDto honorTypePageDto) {
        Page<HonorTypePageVo> queryResult = honorTypeService.pageHonorType(current, pageSize, honorTypePageDto);
        return R.success(queryResult);
    }

    /**
     * 回显学生在校荣誉类型信息
     *
     * @param id 需要回显的学生在校荣誉类型ID
     * @return 学生在校荣誉类型信息
     */
    @GetMapping("/info/{id}")
    public R<HonorTypeInfoVo> info(@PathVariable("id") Long id) {
        if (Objects.isNull(id)) {
            ThrowUtils.error(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY);
        }
        HonorTypeInfoVo honorTypeInfoVo = honorTypeService.info(id);
        return R.success(honorTypeInfoVo);
    }

    /**
     * 更新学生在校荣誉类型信息
     *
     * @param honorTypeUpdateDto 更新学生在校荣誉类型信息Dto类
     * @return 返回更新结果
     */
    @PutMapping("/updateHonorType")
    public R<String> updateHonorType(@RequestBody HonorTypeUpdateDto honorTypeUpdateDto) {
        if (BeanMetaDataUtils.isAnyMetadataEmpty(honorTypeUpdateDto)) {
            ThrowUtils.error(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY);
        }
        honorTypeService.updateHonorType(honorTypeUpdateDto);
        return R.success("更新成功！");
    }

    /**
     * 删除学生在校荣誉类型信息
     *
     * @param id 被删除学生在校荣誉类型ID
     * @return 返回删除结果
     */
    @DeleteMapping("/delete/{id}")
    public R<String> delete(@PathVariable("id") Long id) {
        if (Objects.isNull(id)) {
            ThrowUtils.error(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY);
        }
        honorTypeService.delete(id);
        return R.success("删除成功！");
    }

    /**
     * 批量删除学生在校荣誉类型信息
     *
     * @param ids 被删除学生在校荣誉类型ID列表
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
        honorTypeService.deleteBatch(ids);
        return R.success("删除成功！");
    }
}
