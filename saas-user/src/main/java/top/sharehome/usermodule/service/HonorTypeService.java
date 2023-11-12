package top.sharehome.usermodule.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import top.sharehome.usermodule.model.dto.honorType.HonorTypeAddDto;
import top.sharehome.usermodule.model.dto.honorType.HonorTypePageDto;
import top.sharehome.usermodule.model.dto.honorType.HonorTypeUpdateDto;
import top.sharehome.usermodule.model.entity.HonorType;
import top.sharehome.usermodule.model.vo.honorType.HonorTypeInfoVo;
import top.sharehome.usermodule.model.vo.honorType.HonorTypePageVo;

import java.util.List;
import java.util.Map;

public interface HonorTypeService extends IService<HonorType> {

    /**
     * 添加学生在校荣誉类型
     *
     * @param honorTypeAddDto 学生在校荣誉类型信息Dto类
     */
    void add(HonorTypeAddDto honorTypeAddDto);

    /**
     * 批量添加学生在校荣誉类型
     *
     * @param honorTypeAddDtoList 学生在校荣誉类型信息Dto类列表
     */
    void addBatch(List<HonorTypeAddDto> honorTypeAddDtoList);

    /**
     * 获取已有学生在校荣誉类型列表
     *
     * @return 返回所有已有的学生在校荣誉类型列表
     */
    Map<Long, String> get();

    /**
     * 模糊查询学生在校荣誉类型信息
     *
     * @param current          当前页
     * @param pageSize         页面项数
     * @param honorTypePageDto 查询参数
     * @return 返回分页查询结果
     */
    Page<HonorTypePageVo> pageHonorType(Long current, Long pageSize, HonorTypePageDto honorTypePageDto);

    /**
     * 回显学生在校荣誉类型信息
     *
     * @param id 需要回显的学生在校荣誉类型ID
     * @return 学生在校荣誉类型信息
     */
    HonorTypeInfoVo info(Long id);

    /**
     * 更新学生在校荣誉类型信息
     *
     * @param honorTypeUpdateDto 更新学生在校荣誉类型信息Dto类
     */
    void updateHonorType(HonorTypeUpdateDto honorTypeUpdateDto);

    /**
     * 删除学生在校荣誉类型信息
     *
     * @param id 被删除学生在校荣誉类型ID
     */
    void delete(Long id);

    /**
     * 批量删除学生在校荣誉类型信息
     *
     * @param ids 被删除学生在校荣誉类型ID列表
     */
    void deleteBatch(List<Long> ids);
}
