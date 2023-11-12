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
import top.sharehome.usermodule.mapper.CompetitionMapper;
import top.sharehome.usermodule.mapper.CompetitionTypeMapper;
import top.sharehome.usermodule.model.dto.competitionType.CompetitionTypeAddDto;
import top.sharehome.usermodule.model.dto.competitionType.CompetitionTypePageDto;
import top.sharehome.usermodule.model.dto.competitionType.CompetitionTypeUpdateDto;
import top.sharehome.usermodule.model.entity.Competition;
import top.sharehome.usermodule.model.entity.CompetitionType;
import top.sharehome.usermodule.model.vo.competitionType.CompetitionTypeInfoVo;
import top.sharehome.usermodule.model.vo.competitionType.CompetitionTypePageVo;
import top.sharehome.usermodule.service.CompetitionTypeService;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CompetitionTypeServiceImpl extends ServiceImpl<CompetitionTypeMapper, CompetitionType> implements CompetitionTypeService {

    @Resource
    private CompetitionTypeMapper competitionTypeMapper;

    @Resource
    private CompetitionMapper competitionMapper;

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public void add(CompetitionTypeAddDto competitionTypeAddDto) {
        LambdaQueryWrapper<CompetitionType> competitionTypeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        competitionTypeLambdaQueryWrapper.eq(CompetitionType::getName, competitionTypeAddDto.getName());
        if (competitionTypeMapper.exists(competitionTypeLambdaQueryWrapper)) {
            ThrowUtils.error(RCodeEnum.THE_COMPETITION_TYPE_ALREADY_EXISTS);
        }
        CompetitionType competitionType = new CompetitionType();
        competitionType.setName(competitionTypeAddDto.getName());
        int insertResult = competitionTypeMapper.insert(competitionType);
        if (insertResult == 0) {
            ThrowUtils.error(RCodeEnum.DB_DATA_DELETION_FAILED);
        }
    }

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public void addBatch(List<CompetitionTypeAddDto> competitionTypeAddDtoList) {
        List<CompetitionType> competitionTypes = competitionTypeAddDtoList.stream().map(competitionTypeAddDto -> {
            LambdaQueryWrapper<CompetitionType> competitionTypeLambdaQueryWrapper = new LambdaQueryWrapper<>();
            competitionTypeLambdaQueryWrapper.eq(CompetitionType::getName, competitionTypeAddDto.getName());
            if (competitionTypeMapper.exists(competitionTypeLambdaQueryWrapper)) {
                ThrowUtils.error(RCodeEnum.THE_COMPETITION_TYPE_ALREADY_EXISTS);
            }
            CompetitionType competitionType = new CompetitionType();
            competitionType.setName(competitionTypeAddDto.getName());
            return competitionType;
        }).collect(Collectors.toList());
        if (!this.saveBatch(competitionTypes)) {
            ThrowUtils.error(RCodeEnum.DB_DATA_DELETION_FAILED);
        }
    }

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public Map<Long, String> get() {
        List<CompetitionType> competitionTypes = competitionTypeMapper.selectList(null);
        return competitionTypes.stream().collect(Collectors.toMap(CompetitionType::getId, CompetitionType::getName));
    }

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public Page<CompetitionTypePageVo> pageCompetitionType(Long current, Long pageSize, CompetitionTypePageDto competitionTypePageDto) {
        // 创建原始分页数据以及返回分页数据
        Page<CompetitionType> page = new Page<>(current, pageSize);
        Page<CompetitionTypePageVo> returnResult = new Page<>(current, pageSize);
        // 过滤分页对象
        LambdaQueryWrapper<CompetitionType> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper
                .orderByDesc(CompetitionType::getCreateTime);
        // 当不存在模糊查询时的分页操作
        if (BeanMetaDataUtils.isAllMetadataEmpty(competitionTypePageDto)) {
            this.page(page, lambdaQueryWrapper);
            BeanUtils.copyProperties(page, returnResult, "records");
            List<CompetitionTypePageVo> pageVoList = page.getRecords().stream().map(record -> {
                CompetitionTypePageVo competitionTypePageVo = new CompetitionTypePageVo();
                competitionTypePageVo.setId(record.getId());
                competitionTypePageVo.setName(record.getName());
                competitionTypePageVo.setCreateTime(record.getCreateTime());
                return competitionTypePageVo;
            }).collect(Collectors.toList());
            returnResult.setRecords(pageVoList);
            return returnResult;
        }
        // 当存在模糊查询时的分页操作
        lambdaQueryWrapper
                .like(StringUtils.isNotEmpty(competitionTypePageDto.getName()), CompetitionType::getName, competitionTypePageDto.getName());
        this.page(page, lambdaQueryWrapper);
        BeanUtils.copyProperties(page, returnResult, "records");
        List<CompetitionTypePageVo> pageVoList = page.getRecords().stream().map(record -> {
            CompetitionTypePageVo competitionTypePageVo = new CompetitionTypePageVo();
            competitionTypePageVo.setId(record.getId());
            competitionTypePageVo.setName(record.getName());
            competitionTypePageVo.setCreateTime(record.getCreateTime());
            return competitionTypePageVo;
        }).collect(Collectors.toList());
        returnResult.setRecords(pageVoList);
        return returnResult;
    }

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public CompetitionTypeInfoVo info(Long id) {
        CompetitionType competitionTypeInDatabase = competitionTypeMapper.selectById(id);
        if (Objects.isNull(competitionTypeInDatabase)) {
            ThrowUtils.error(RCodeEnum.THE_MODIFIED_TARGET_DOES_NOT_EXIST);
        }
        CompetitionTypeInfoVo competitionTypeInfoVo = new CompetitionTypeInfoVo();
        competitionTypeInfoVo.setId(competitionTypeInDatabase.getId());
        competitionTypeInfoVo.setName(competitionTypeInDatabase.getName());
        return competitionTypeInfoVo;
    }

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public void updateCompetitionType(CompetitionTypeUpdateDto competitionTypeUpdateDto) {
        CompetitionType competitionTypeInDatabase = competitionTypeMapper.selectById(competitionTypeUpdateDto.getId());
        if (Objects.isNull(competitionTypeInDatabase)) {
            ThrowUtils.error(RCodeEnum.THE_MODIFIED_TARGET_DOES_NOT_EXIST);
        }
        if (Objects.equals(competitionTypeInDatabase.getName(), competitionTypeUpdateDto.getName())) {
            ThrowUtils.error(RCodeEnum.THE_DATA_HAS_NOT_CHANGED);
        }
        LambdaUpdateWrapper<Competition> competitionLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        competitionLambdaUpdateWrapper
                .set(Competition::getTypeName, competitionTypeUpdateDto.getName())
                .eq(Competition::getTypeId, competitionTypeInDatabase.getId());
        competitionMapper.update(null, competitionLambdaUpdateWrapper);
        CompetitionType competitionType = new CompetitionType();
        competitionType.setId(competitionTypeInDatabase.getId());
        competitionType.setName(competitionTypeUpdateDto.getName());
        int updateResult = competitionTypeMapper.updateById(competitionType);
        if (updateResult == 0) {
            ThrowUtils.error(RCodeEnum.DB_DATA_MODIFICATION_FAILED);
        }
    }

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public void delete(Long id) {
        CompetitionType competitionTypeInDatabase = competitionTypeMapper.selectById(id);
        if (Objects.isNull(competitionTypeInDatabase)) {
            ThrowUtils.error(RCodeEnum.THE_DELETED_TARGET_DOES_NOT_EXIST);
        }
        LambdaQueryWrapper<Competition> competitionLambdaQueryWrapper = new LambdaQueryWrapper<>();
        competitionLambdaQueryWrapper.eq(Competition::getTypeId, id);
        if (competitionMapper.exists(competitionLambdaQueryWrapper)) {
            ThrowUtils.error(RCodeEnum.CAN_NOT_DELETE_COMPETITION_TYPE_THAT_ALREADY_HAS_DATA);
        }
        int deleteResult = competitionTypeMapper.deleteById(id);
        if (deleteResult == 0) {
            ThrowUtils.error(RCodeEnum.DB_DATA_DELETION_FAILED);
        }
    }

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public void deleteBatch(List<Long> ids) {
        ids.forEach(id -> {
            CompetitionType competitionTypeInDatabase = competitionTypeMapper.selectById(id);
            if (Objects.isNull(competitionTypeInDatabase)) {
                ThrowUtils.error(RCodeEnum.THE_DELETED_TARGET_DOES_NOT_EXIST);
            }
            LambdaQueryWrapper<Competition> competitionLambdaQueryWrapper = new LambdaQueryWrapper<>();
            competitionLambdaQueryWrapper.eq(Competition::getTypeId, id);
            if (competitionMapper.exists(competitionLambdaQueryWrapper)) {
                ThrowUtils.error(RCodeEnum.CAN_NOT_DELETE_COMPETITION_TYPE_THAT_ALREADY_HAS_DATA);
            }
        });
        if (!this.removeBatchByIds(ids)) {
            ThrowUtils.error(RCodeEnum.DB_DATA_DELETION_FAILED);
        }
    }
}
