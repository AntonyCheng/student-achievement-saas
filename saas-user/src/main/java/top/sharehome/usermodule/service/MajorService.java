package top.sharehome.usermodule.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import top.sharehome.usermodule.model.dto.major.MajorAddDto;
import top.sharehome.usermodule.model.dto.major.MajorPageDto;
import top.sharehome.usermodule.model.dto.major.MajorUpdateDto;
import top.sharehome.usermodule.model.entity.Major;
import com.baomidou.mybatisplus.extension.service.IService;
import top.sharehome.usermodule.model.vo.major.MajorInfoVo;
import top.sharehome.usermodule.model.vo.major.MajorPageVo;

import java.util.List;
import java.util.Map;

public interface MajorService extends IService<Major> {

    /**
     * 添加专业信息
     *
     * @param majorAddDto 专业信息Dto类
     */
    void add(MajorAddDto majorAddDto);

    /**
     * 批量添加专业信息
     *
     * @param majorAddDtoList 专业信息Dto类列表
     */
    void addBatch(List<MajorAddDto> majorAddDtoList);

    /**
     * 获取已有年级列表
     *
     * @return 返回所有已有的年级列表
     */
    List<String> getGrades();

    /**
     * 根据年级获取专业ID和名称
     *
     * @param grade 指定的年级
     * @return 返回专业ID和名称
     */
    Map<Long, String> getMajor(String grade);

    /**
     * 根据专业ID获取该专业的班级
     * @param major 指定的专业ID
     * @return 返回班级列表
     */
    List<Integer> getClasses(Long major);

    /**
     * 模糊查询专业信息
     *
     * @param current      当前页
     * @param pageSize     页面项数
     * @param majorPageDto 查询参数
     * @return 返回分页查询结果
     */
    Page<MajorPageVo> pageMajor(Long current, Long pageSize, MajorPageDto majorPageDto);

    /**
     * 回显专业信息
     * @param id 需要回显的专业ID
     * @return 专业信息
     */
    MajorInfoVo info(Long id);

    /**
     * 更新专业信息
     *
     * @param majorUpdateDto 更新专业信息Dto类
     */
    void updateMajor(MajorUpdateDto majorUpdateDto);

    /**
     * 删除专业信息
     *
     * @param id 被删除专业ID
     */
    void delete(Long id);

    /**
     * 批量删除专业信息
     * @param ids 被删除专业ID列表
     */
    void deleteBatch(List<Long> ids);
}
