package top.sharehome.usermodule.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import top.sharehome.usermodule.model.dto.competitionType.CompetitionTypeAddDto;
import top.sharehome.usermodule.model.dto.competitionType.CompetitionTypePageDto;
import top.sharehome.usermodule.model.dto.competitionType.CompetitionTypeUpdateDto;
import top.sharehome.usermodule.model.entity.CompetitionType;
import top.sharehome.usermodule.model.vo.competitionType.CompetitionTypeInfoVo;
import top.sharehome.usermodule.model.vo.competitionType.CompetitionTypePageVo;

import java.util.List;
import java.util.Map;

public interface CompetitionTypeService extends IService<CompetitionType> {

    /**
     * 添加学生学科竞赛类型
     *
     * @param competitionTypeAddDto 学生学科竞赛类型信息Dto类
     */
    void add(CompetitionTypeAddDto competitionTypeAddDto);

    /**
     * 批量添加学生学科竞赛类型
     *
     * @param competitionTypeAddDtoList 学生学科竞赛类型信息Dto类列表
     */
    void addBatch(List<CompetitionTypeAddDto> competitionTypeAddDtoList);

    /**
     * 获取已有学生学科竞赛类型列表
     *
     * @return 返回所有已有的学生学科竞赛类型列表
     */
    Map<Long, String> get();

    /**
     * 模糊查询学生学科竞赛类型信息
     *
     * @param current                当前页
     * @param pageSize               页面项数
     * @param competitionTypePageDto 查询参数
     * @return 返回分页查询结果
     */
    Page<CompetitionTypePageVo> pageCompetitionType(Long current, Long pageSize, CompetitionTypePageDto competitionTypePageDto);

    /**
     * 回显学生学科竞赛类型信息
     *
     * @param id 需要回显的学生学科竞赛类型ID
     * @return 学生学科竞赛类型信息
     */
    CompetitionTypeInfoVo info(Long id);

    /**
     * 更新学生学科竞赛类型信息
     *
     * @param competitionTypeUpdateDto 更新学生学科竞赛类型信息Dto类
     */
    void updateCompetitionType(CompetitionTypeUpdateDto competitionTypeUpdateDto);

    /**
     * 删除学生学科竞赛类型信息
     *
     * @param id 被删除学生学科竞赛类型ID
     */
    void delete(Long id);

    /**
     * 批量删除学生学科竞赛类型信息
     *
     * @param ids 被删除学生学科竞赛类型ID列表
     */
    void deleteBatch(List<Long> ids);
}
