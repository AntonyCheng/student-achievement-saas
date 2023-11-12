package top.sharehome.issuemodule.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import top.sharehome.issuemodule.common.constant.CommonConstant;
import top.sharehome.issuemodule.common.response.R;
import top.sharehome.issuemodule.common.response.RCodeEnum;
import top.sharehome.issuemodule.model.dto.update.UpdateCensorDto;
import top.sharehome.issuemodule.model.dto.update.UpdatePageDto;
import top.sharehome.issuemodule.model.vo.update.UpdatePageVo;
import top.sharehome.issuemodule.service.UpdateService;
import top.sharehome.issuemodule.utils.meta.BeanMetaDataUtils;
import top.sharehome.issuemodule.utils.throwException.ThrowUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * 信息更新相关接口
 *
 * @author AntonyCheng
 * @since 2023/7/22 20:55:24
 */
@RestController
@RequestMapping("/update")
@CrossOrigin
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UpdateController {
    @Resource
    private UpdateService updateService;

    /**
     * 审核租户更新信息接口
     *
     * @param updateCensorDto 审核必要参数
     * @return 审核结果
     */
    @PostMapping("/censor")
    public R<String> censor(@RequestBody UpdateCensorDto updateCensorDto) {
        // 校验参数
        verifyCensorParams(updateCensorDto);
        updateService.censor(updateCensorDto);
        return R.success("审核完成，反馈邮件已经发出！");
    }

    /**
     * 模糊查询租户更新信息接口
     *
     * @param current       当前页
     * @param pageSize      页面项数
     * @param updatePageDto 查询参数
     * @return 返回分页查询结果
     */
    @GetMapping("/page/{current}/{pageSize}")
    public R<Page<UpdatePageVo>> pageUpdate(@PathVariable("current") Integer current, @PathVariable("pageSize") Integer pageSize, UpdatePageDto updatePageDto) {
        // 校验参数
        verifyUpdatePageParams(updatePageDto);
        Page<UpdatePageVo> pageResult = updateService.pageUpdate(current, pageSize, updatePageDto);
        return R.success(pageResult);
    }

    /**
     * 删除租户更新信息（单个）
     *
     * @param id 被删除租户更新信息ID
     * @return 删除结果
     */
    @DeleteMapping("/delete/{id}")
    public R<String> delete(@PathVariable("id") Long id) {
        if (Objects.isNull(id)) {
            ThrowUtils.error(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY);
        }
        updateService.delete(id);
        return R.success("删除成功！");
    }

    /**
     * 删除租户更新信息（批量）
     *
     * @param ids 被删除租户更新信息IDs
     * @return 删除结果
     */
    @DeleteMapping("/deleteBatch")
    public R<String> deleteBatch(@RequestBody List<Long> ids) {
        if (Objects.isNull(ids) || ids.isEmpty()) {
            ThrowUtils.error(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY);
        }
        updateService.deleteBatch(ids);
        return R.success("删除成功！");
    }

    /**
     * 校验租户更新信息分页参数
     *
     * @param updatePageDto 审核参数类
     */
    private void verifyUpdatePageParams(UpdatePageDto updatePageDto) {
        if (Objects.nonNull(updatePageDto) && Objects.nonNull(updatePageDto.getGender())) {
            // 校验性别数据格式
            if (!(updatePageDto.getGender() == CommonConstant.GENDER_MAN || updatePageDto.getGender() == CommonConstant.GENDER_WOMAN)) {
                ThrowUtils.error(RCodeEnum.DATA_BINARY_IS_NOT_SATISFIED);
            }
        }
        if (Objects.nonNull(updatePageDto) && Objects.nonNull(updatePageDto.getLevel())) {
            // 校验租户等级
            if (!CommonConstant.LEVEL_LIST.contains(updatePageDto.getLevel())) {
                ThrowUtils.error(RCodeEnum.TENANT_LEVEL_FORMAT_VERIFICATION_FAILED);
            }
        }
        if (Objects.nonNull(updatePageDto) && Objects.nonNull(updatePageDto.getStatus())) {
            // 校验租户状态
            if (!CommonConstant.MANAGER_INFO_UPDATE_STATUS_LIST.contains(updatePageDto.getStatus())) {
                ThrowUtils.error(RCodeEnum.TENANT_UPDATE_INFORMATION_STATUS_FORMAT_VALIDATION_FAILED);
            }
        }
    }

    /**
     * 校验租户审核参数
     *
     * @param updateCensorDto 审核参数类
     */
    private void verifyCensorParams(UpdateCensorDto updateCensorDto) {
        // 判空（反馈内容除外）
        if (BeanMetaDataUtils.isAnyMetadataEmpty(updateCensorDto, "content")) {
            ThrowUtils.error(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY);
        }
        // 判断审核结果数据格式
        if (!(updateCensorDto.getResult() == CommonConstant.CENSOR_PASS || updateCensorDto.getResult() == CommonConstant.CENSOR_NOT_PASS)) {
            ThrowUtils.error(RCodeEnum.DATA_BINARY_IS_NOT_SATISFIED);
        }
    }
}
