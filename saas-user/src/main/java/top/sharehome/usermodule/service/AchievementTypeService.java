package top.sharehome.usermodule.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import top.sharehome.usermodule.model.dto.achievementType.AchievementTypeAddDto;
import top.sharehome.usermodule.model.dto.achievementType.AchievementTypePageDto;
import top.sharehome.usermodule.model.dto.achievementType.AchievementTypeUpdateDto;
import top.sharehome.usermodule.model.entity.AchievementType;
import top.sharehome.usermodule.model.vo.achievementType.AchievementTypeInfoVo;
import top.sharehome.usermodule.model.vo.achievementType.AchievementTypePageVo;

import java.util.List;
import java.util.Map;

public interface AchievementTypeService extends IService<AchievementType> {

    /**
     * 添加学生科研成果类型
     *
     * @param achievementTypeAddDto 学生科研成果类型信息Dto类
     */
    void add(AchievementTypeAddDto achievementTypeAddDto);

    /**
     * 批量添加学生科研成果类型
     *
     * @param achievementTypeAddDtoList 学生科研成果类型信息Dto类列表
     */
    void addBatch(List<AchievementTypeAddDto> achievementTypeAddDtoList);

    /**
     * 获取已有学生科研成果类型列表
     *
     * @return 返回所有已有的学生科研成果类型列表
     */
    Map<Long, String> get();

    /**
     * 模糊查询学生科研成果类型信息
     *
     * @param current                当前页
     * @param pageSize               页面项数
     * @param achievementTypePageDto 查询参数
     * @return 返回分页查询结果
     */
    Page<AchievementTypePageVo> pageAchievementType(Long current, Long pageSize, AchievementTypePageDto achievementTypePageDto);

    /**
     * 回显学生科研成果类型信息
     *
     * @param id 需要回显的学生科研成果类型ID
     * @return 学生科研成果类型信息
     */
    AchievementTypeInfoVo info(Long id);

    /**
     * 更新学生科研成果类型信息
     *
     * @param achievementTypeUpdateDto 更新学生科研成果类型信息Dto类
     */
    void updateAchievementType(AchievementTypeUpdateDto achievementTypeUpdateDto);

    /**
     * 删除学生科研成果类型信息
     *
     * @param id 被删除学生科研成果类型ID
     */
    void delete(Long id);

    /**
     * 批量删除学生科研成果类型信息
     *
     * @param ids 被删除学生科研成果类型ID列表
     */
    void deleteBatch(List<Long> ids);
}
