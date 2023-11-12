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
import top.sharehome.usermodule.mapper.HonorMapper;
import top.sharehome.usermodule.mapper.HonorTypeMapper;
import top.sharehome.usermodule.model.dto.honorType.HonorTypeAddDto;
import top.sharehome.usermodule.model.dto.honorType.HonorTypePageDto;
import top.sharehome.usermodule.model.dto.honorType.HonorTypeUpdateDto;
import top.sharehome.usermodule.model.entity.Honor;
import top.sharehome.usermodule.model.entity.HonorType;
import top.sharehome.usermodule.model.vo.honorType.HonorTypeInfoVo;
import top.sharehome.usermodule.model.vo.honorType.HonorTypePageVo;
import top.sharehome.usermodule.service.HonorTypeService;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class HonorTypeServiceImpl extends ServiceImpl<HonorTypeMapper, HonorType> implements HonorTypeService {

    @Resource
    private HonorTypeMapper honorTypeMapper;

    @Resource
    private HonorMapper honorMapper;

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public void add(HonorTypeAddDto honorTypeAddDto) {
        LambdaQueryWrapper<HonorType> honorTypeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        honorTypeLambdaQueryWrapper.eq(HonorType::getName, honorTypeAddDto.getName());
        if (honorTypeMapper.exists(honorTypeLambdaQueryWrapper)) {
            ThrowUtils.error(RCodeEnum.THE_HONOR_TYPE_ALREADY_EXISTS);
        }
        HonorType honorType = new HonorType();
        honorType.setName(honorTypeAddDto.getName());
        int insertResult = honorTypeMapper.insert(honorType);
        if (insertResult == 0) {
            ThrowUtils.error(RCodeEnum.DB_DATA_DELETION_FAILED);
        }
    }

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public void addBatch(List<HonorTypeAddDto> honorTypeAddDtoList) {
        List<HonorType> honorTypes = honorTypeAddDtoList.stream().map(honorTypeAddDto -> {
            LambdaQueryWrapper<HonorType> honorTypeLambdaQueryWrapper = new LambdaQueryWrapper<>();
            honorTypeLambdaQueryWrapper.eq(HonorType::getName, honorTypeAddDto.getName());
            if (honorTypeMapper.exists(honorTypeLambdaQueryWrapper)) {
                ThrowUtils.error(RCodeEnum.THE_HONOR_TYPE_ALREADY_EXISTS);
            }
            HonorType honorType = new HonorType();
            honorType.setName(honorTypeAddDto.getName());
            return honorType;
        }).collect(Collectors.toList());
        if (!this.saveBatch(honorTypes)) {
            ThrowUtils.error(RCodeEnum.DB_DATA_DELETION_FAILED);
        }
    }

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public Map<Long, String> get() {
        List<HonorType> honorTypes = honorTypeMapper.selectList(null);
        return honorTypes.stream().collect(Collectors.toMap(HonorType::getId, HonorType::getName));
    }

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public Page<HonorTypePageVo> pageHonorType(Long current, Long pageSize, HonorTypePageDto honorTypePageDto) {
        // 创建原始分页数据以及返回分页数据
        Page<HonorType> page = new Page<>(current, pageSize);
        Page<HonorTypePageVo> returnResult = new Page<>(current, pageSize);
        // 过滤分页对象
        LambdaQueryWrapper<HonorType> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper
                .orderByDesc(HonorType::getCreateTime);
        // 当不存在模糊查询时的分页操作
        if (BeanMetaDataUtils.isAllMetadataEmpty(honorTypePageDto)) {
            this.page(page, lambdaQueryWrapper);
            BeanUtils.copyProperties(page, returnResult, "records");
            List<HonorTypePageVo> pageVoList = page.getRecords().stream().map(record -> {
                HonorTypePageVo honorTypePageVo = new HonorTypePageVo();
                honorTypePageVo.setId(record.getId());
                honorTypePageVo.setName(record.getName());
                honorTypePageVo.setCreateTime(record.getCreateTime());
                return honorTypePageVo;
            }).collect(Collectors.toList());
            returnResult.setRecords(pageVoList);
            return returnResult;
        }
        // 当存在模糊查询时的分页操作
        lambdaQueryWrapper
                .like(StringUtils.isNotEmpty(honorTypePageDto.getName()), HonorType::getName, honorTypePageDto.getName());
        this.page(page, lambdaQueryWrapper);
        BeanUtils.copyProperties(page, returnResult, "records");
        List<HonorTypePageVo> pageVoList = page.getRecords().stream().map(record -> {
            HonorTypePageVo honorTypePageVo = new HonorTypePageVo();
            honorTypePageVo.setId(record.getId());
            honorTypePageVo.setName(record.getName());
            honorTypePageVo.setCreateTime(record.getCreateTime());
            return honorTypePageVo;
        }).collect(Collectors.toList());
        returnResult.setRecords(pageVoList);
        return returnResult;
    }

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public HonorTypeInfoVo info(Long id) {
        HonorType honorTypeInDatabase = honorTypeMapper.selectById(id);
        if (Objects.isNull(honorTypeInDatabase)) {
            ThrowUtils.error(RCodeEnum.THE_MODIFIED_TARGET_DOES_NOT_EXIST);
        }
        HonorTypeInfoVo honorTypeInfoVo = new HonorTypeInfoVo();
        honorTypeInfoVo.setId(honorTypeInDatabase.getId());
        honorTypeInfoVo.setName(honorTypeInDatabase.getName());
        return honorTypeInfoVo;
    }

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public void updateHonorType(HonorTypeUpdateDto honorTypeUpdateDto) {
        HonorType honorTypeInDatabase = honorTypeMapper.selectById(honorTypeUpdateDto.getId());
        if (Objects.isNull(honorTypeInDatabase)) {
            ThrowUtils.error(RCodeEnum.THE_MODIFIED_TARGET_DOES_NOT_EXIST);
        }
        if (Objects.equals(honorTypeInDatabase.getName(), honorTypeUpdateDto.getName())) {
            ThrowUtils.error(RCodeEnum.THE_DATA_HAS_NOT_CHANGED);
        }
        LambdaUpdateWrapper<Honor> honorLambdaUpdateWrapper = new LambdaUpdateWrapper<Honor>();
        honorLambdaUpdateWrapper
                .set(Honor::getTypeName, honorTypeUpdateDto.getName())
                .eq(Honor::getTypeId, honorTypeInDatabase.getId());
        honorMapper.update(null, honorLambdaUpdateWrapper);
        HonorType honorType = new HonorType();
        honorType.setId(honorTypeInDatabase.getId());
        honorType.setName(honorTypeUpdateDto.getName());
        int updateResult = honorTypeMapper.updateById(honorType);
        if (updateResult == 0) {
            ThrowUtils.error(RCodeEnum.DB_DATA_MODIFICATION_FAILED);
        }
    }

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public void delete(Long id) {
        HonorType honorTypeInDatabase = honorTypeMapper.selectById(id);
        if (Objects.isNull(honorTypeInDatabase)) {
            ThrowUtils.error(RCodeEnum.THE_DELETED_TARGET_DOES_NOT_EXIST);
        }
        LambdaQueryWrapper<Honor> honorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        honorLambdaQueryWrapper.eq(Honor::getTypeId, id);
        if (honorMapper.exists(honorLambdaQueryWrapper)) {
            ThrowUtils.error(RCodeEnum.CAN_NOT_DELETE_HONOR_TYPE_THAT_ALREADY_HAS_DATA);
        }
        int deleteResult = honorTypeMapper.deleteById(id);
        if (deleteResult == 0) {
            ThrowUtils.error(RCodeEnum.DB_DATA_DELETION_FAILED);
        }
    }

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public void deleteBatch(List<Long> ids) {
        ids.forEach(id -> {
            HonorType honorTypeInDatabase = honorTypeMapper.selectById(id);
            if (Objects.isNull(honorTypeInDatabase)) {
                ThrowUtils.error(RCodeEnum.THE_DELETED_TARGET_DOES_NOT_EXIST);
            }
            LambdaQueryWrapper<Honor> honorLambdaQueryWrapper = new LambdaQueryWrapper<>();
            honorLambdaQueryWrapper.eq(Honor::getTypeId, id);
            if (honorMapper.exists(honorLambdaQueryWrapper)) {
                ThrowUtils.error(RCodeEnum.CAN_NOT_DELETE_HONOR_TYPE_THAT_ALREADY_HAS_DATA);
            }
        });
        if (!this.removeBatchByIds(ids)) {
            ThrowUtils.error(RCodeEnum.DB_DATA_DELETION_FAILED);
        }
    }
}
