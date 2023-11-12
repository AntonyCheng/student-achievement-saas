package top.sharehome.issuemodule.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import top.sharehome.issuemodule.model.dto.update.UpdateCensorDto;
import top.sharehome.issuemodule.model.dto.update.UpdatePageDto;
import top.sharehome.issuemodule.model.entity.Update;
import top.sharehome.issuemodule.model.vo.update.UpdatePageVo;

import java.util.List;

public interface UpdateService extends IService<Update> {

    /**
     * 审核租户注册信息接口
     *
     * @param updateCensorDto 审核必要参数
     */
    void censor(UpdateCensorDto updateCensorDto);

    /**
     * 模糊查询租户更新信息接口
     *
     * @param current       当前页
     * @param pageSize      页面项数
     * @param updatePageDto 查询参数
     * @return 返回分页查询结果
     */
    Page<UpdatePageVo> pageUpdate(Integer current, Integer pageSize, UpdatePageDto updatePageDto);

    /**
     * 删除租户更新信息（单个）
     *
     * @param id 被删除租户更新信息ID
     */
    void delete(Long id);

    /**
     * 删除租户更新信息（批量）
     *
     * @param ids 被删除租户更新信息IDs
     */
    void deleteBatch(List<Long> ids);
}
