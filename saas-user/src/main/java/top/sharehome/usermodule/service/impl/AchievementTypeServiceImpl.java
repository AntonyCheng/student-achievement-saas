package top.sharehome.usermodule.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.sharehome.issuemodule.common.exception_handler.customize.CustomizeTransactionException;
import top.sharehome.issuemodule.common.response.RCodeEnum;
import top.sharehome.issuemodule.utils.meta.BeanMetaDataUtils;
import top.sharehome.issuemodule.utils.throwException.ThrowUtils;
import top.sharehome.usermodule.mapper.AchievementMapper;
import top.sharehome.usermodule.mapper.AchievementTypeMapper;
import top.sharehome.usermodule.model.dto.achievementType.AchievementTypeAddDto;
import top.sharehome.usermodule.model.dto.achievementType.AchievementTypePageDto;
import top.sharehome.usermodule.model.dto.achievementType.AchievementTypeUpdateDto;
import top.sharehome.usermodule.model.entity.Achievement;
import top.sharehome.usermodule.model.entity.AchievementType;
import top.sharehome.usermodule.model.vo.achievementType.AchievementTypeInfoVo;
import top.sharehome.usermodule.model.vo.achievementType.AchievementTypePageVo;
import top.sharehome.usermodule.service.AchievementTypeService;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class AchievementTypeServiceImpl extends ServiceImpl<AchievementTypeMapper, AchievementType> implements AchievementTypeService {

    @Resource
    private AchievementTypeMapper achievementTypeMapper;

    @Resource
    private AchievementMapper achievementMapper;

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public void add(AchievementTypeAddDto achievementTypeAddDto) {
        LambdaQueryWrapper<AchievementType> achievementTypeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        achievementTypeLambdaQueryWrapper.eq(AchievementType::getName, achievementTypeAddDto.getName());
        if (achievementTypeMapper.exists(achievementTypeLambdaQueryWrapper)) {
            ThrowUtils.error(RCodeEnum.THE_ACHIEVEMENT_TYPE_ALREADY_EXISTS);
        }
        AchievementType achievementType = new AchievementType();
        achievementType.setName(achievementTypeAddDto.getName());
        int insertResult = achievementTypeMapper.insert(achievementType);
        if (insertResult == 0) {
            ThrowUtils.error(RCodeEnum.DB_DATA_DELETION_FAILED);
        }
    }

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public void addBatch(List<AchievementTypeAddDto> achievementTypeAddDtoList) {
        List<AchievementType> achievementTypes = achievementTypeAddDtoList.stream().map(achievementTypeAddDto -> {
            LambdaQueryWrapper<AchievementType> achievementTypeLambdaQueryWrapper = new LambdaQueryWrapper<>();
            achievementTypeLambdaQueryWrapper.eq(AchievementType::getName, achievementTypeAddDto.getName());
            if (achievementTypeMapper.exists(achievementTypeLambdaQueryWrapper)) {
                ThrowUtils.error(RCodeEnum.THE_ACHIEVEMENT_TYPE_ALREADY_EXISTS);
            }
            AchievementType achievementType = new AchievementType();
            achievementType.setName(achievementTypeAddDto.getName());
            return achievementType;
        }).collect(Collectors.toList());
        if (!this.saveBatch(achievementTypes)) {
            ThrowUtils.error(RCodeEnum.DB_DATA_DELETION_FAILED);
        }
    }

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public Map<Long, String> get() {
        List<AchievementType> achievementTypes = achievementTypeMapper.selectList(null);
        return achievementTypes.stream().collect(Collectors.toMap(AchievementType::getId, AchievementType::getName));
    }

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public Page<AchievementTypePageVo> pageAchievementType(Long current, Long pageSize, AchievementTypePageDto achievementTypePageDto) {
        // 创建原始分页数据以及返回分页数据
        Page<AchievementType> page = new Page<>(current, pageSize);
        Page<AchievementTypePageVo> returnResult = new Page<>(current, pageSize);
        // 过滤分页对象
        LambdaQueryWrapper<AchievementType> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper
                .orderByDesc(AchievementType::getCreateTime);
        // 当不存在模糊查询时的分页操作
        if (BeanMetaDataUtils.isAllMetadataEmpty(achievementTypePageDto)) {
            this.page(page, lambdaQueryWrapper);
            BeanUtils.copyProperties(page, returnResult, "records");
            List<AchievementTypePageVo> pageVoList = page.getRecords().stream().map(record -> {
                AchievementTypePageVo achievementTypePageVo = new AchievementTypePageVo();
                achievementTypePageVo.setId(record.getId());
                achievementTypePageVo.setName(record.getName());
                achievementTypePageVo.setCreateTime(record.getCreateTime());
                return achievementTypePageVo;
            }).collect(Collectors.toList());
            returnResult.setRecords(pageVoList);
            return returnResult;
        }
        // 当存在模糊查询时的分页操作
        lambdaQueryWrapper
                .like(StringUtils.isNotEmpty(achievementTypePageDto.getName()), AchievementType::getName, achievementTypePageDto.getName());
        this.page(page, lambdaQueryWrapper);
        BeanUtils.copyProperties(page, returnResult, "records");
        List<AchievementTypePageVo> pageVoList = page.getRecords().stream().map(record -> {
            AchievementTypePageVo achievementTypePageVo = new AchievementTypePageVo();
            achievementTypePageVo.setId(record.getId());
            achievementTypePageVo.setName(record.getName());
            achievementTypePageVo.setCreateTime(record.getCreateTime());
            return achievementTypePageVo;
        }).collect(Collectors.toList());
        returnResult.setRecords(pageVoList);
        return returnResult;
    }

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public AchievementTypeInfoVo info(Long id) {
        AchievementType achievementTypeInDatabase = achievementTypeMapper.selectById(id);
        if (Objects.isNull(achievementTypeInDatabase)) {
            ThrowUtils.error(RCodeEnum.THE_MODIFIED_TARGET_DOES_NOT_EXIST);
        }
        AchievementTypeInfoVo achievementTypeInfoVo = new AchievementTypeInfoVo();
        achievementTypeInfoVo.setId(achievementTypeInDatabase.getId());
        achievementTypeInfoVo.setName(achievementTypeInDatabase.getName());
        return achievementTypeInfoVo;
    }

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public void updateAchievementType(AchievementTypeUpdateDto achievementTypeUpdateDto) {
        AchievementType achievementTypeInDatabase = achievementTypeMapper.selectById(achievementTypeUpdateDto.getId());
        if (Objects.isNull(achievementTypeInDatabase)) {
            ThrowUtils.error(RCodeEnum.THE_MODIFIED_TARGET_DOES_NOT_EXIST);
        }
        if (Objects.equals(achievementTypeInDatabase.getName(), achievementTypeUpdateDto.getName())) {
            ThrowUtils.error(RCodeEnum.THE_DATA_HAS_NOT_CHANGED);
        }
        LambdaUpdateWrapper<Achievement> achievementLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        achievementLambdaUpdateWrapper
                .set(Achievement::getTypeName, achievementTypeUpdateDto.getName())
                .eq(Achievement::getTypeId, achievementTypeInDatabase.getId());
        achievementMapper.update(null, achievementLambdaUpdateWrapper);
        AchievementType achievementType = new AchievementType();
        achievementType.setId(achievementTypeInDatabase.getId());
        achievementType.setName(achievementTypeUpdateDto.getName());
        int updateResult = achievementTypeMapper.updateById(achievementType);
        if (updateResult == 0) {
            ThrowUtils.error(RCodeEnum.DB_DATA_MODIFICATION_FAILED);
        }
    }

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public void delete(Long id) {
        AchievementType achievementTypeInDatabase = achievementTypeMapper.selectById(id);
        if (Objects.isNull(achievementTypeInDatabase)) {
            ThrowUtils.error(RCodeEnum.THE_DELETED_TARGET_DOES_NOT_EXIST);
        }
        LambdaQueryWrapper<Achievement> achievementLambdaQueryWrapper = new LambdaQueryWrapper<>();
        achievementLambdaQueryWrapper.eq(Achievement::getTypeId, id);
        if (achievementMapper.exists(achievementLambdaQueryWrapper)) {
            ThrowUtils.error(RCodeEnum.CAN_NOT_DELETE_ACHIEVEMENT_TYPE_THAT_ALREADY_HAS_DATA);
        }
        int deleteResult = achievementTypeMapper.deleteById(id);
        if (deleteResult == 0) {
            ThrowUtils.error(RCodeEnum.DB_DATA_DELETION_FAILED);
        }
    }

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public void deleteBatch(List<Long> ids) {
        ids.forEach(id -> {
            AchievementType achievementTypeInDatabase = achievementTypeMapper.selectById(id);
            if (Objects.isNull(achievementTypeInDatabase)) {
                ThrowUtils.error(RCodeEnum.THE_DELETED_TARGET_DOES_NOT_EXIST);
            }
            LambdaQueryWrapper<Achievement> achievementLambdaQueryWrapper = new LambdaQueryWrapper<>();
            achievementLambdaQueryWrapper.eq(Achievement::getTypeId, id);
            if (achievementMapper.exists(achievementLambdaQueryWrapper)) {
                ThrowUtils.error(RCodeEnum.CAN_NOT_DELETE_ACHIEVEMENT_TYPE_THAT_ALREADY_HAS_DATA);
            }
        });
        if (!this.removeBatchByIds(ids)) {
            ThrowUtils.error(RCodeEnum.DB_DATA_DELETION_FAILED);
        }
    }
}
