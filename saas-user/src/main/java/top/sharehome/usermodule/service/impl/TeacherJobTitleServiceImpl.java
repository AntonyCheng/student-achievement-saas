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
import top.sharehome.usermodule.mapper.TeacherJobTitleMapper;
import top.sharehome.usermodule.mapper.TeacherMapper;
import top.sharehome.usermodule.model.dto.teacherJobTitle.TeacherJobTitleAddDto;
import top.sharehome.usermodule.model.dto.teacherJobTitle.TeacherJobTitlePageDto;
import top.sharehome.usermodule.model.dto.teacherJobTitle.TeacherJobTitleUpdateDto;
import top.sharehome.usermodule.model.entity.Teacher;
import top.sharehome.usermodule.model.entity.TeacherJobTitle;
import top.sharehome.usermodule.model.vo.teacherJobTitle.TeacherJobTitleInfoVo;
import top.sharehome.usermodule.model.vo.teacherJobTitle.TeacherJobTitlePageVo;
import top.sharehome.usermodule.service.TeacherJobTitleService;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TeacherJobTitleServiceImpl extends ServiceImpl<TeacherJobTitleMapper, TeacherJobTitle> implements TeacherJobTitleService {
    @Resource
    private TeacherJobTitleMapper teacherJobTitleMapper;

    @Resource
    private TeacherMapper teacherMapper;

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public void add(TeacherJobTitleAddDto teacherJobTitleAddDto) {
        LambdaQueryWrapper<TeacherJobTitle> teacherJobTitleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        teacherJobTitleLambdaQueryWrapper.eq(TeacherJobTitle::getName, teacherJobTitleAddDto.getName());
        if (teacherJobTitleMapper.exists(teacherJobTitleLambdaQueryWrapper)) {
            ThrowUtils.error(RCodeEnum.THE_TEACHER_JOB_TITLE_ALREADY_EXISTS);
        }
        TeacherJobTitle teacherJobTitle = new TeacherJobTitle();
        teacherJobTitle.setName(teacherJobTitleAddDto.getName());
        int insertResult = teacherJobTitleMapper.insert(teacherJobTitle);
        if (insertResult == 0) {
            ThrowUtils.error(RCodeEnum.DB_DATA_DELETION_FAILED);
        }
    }

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public void addBatch(List<TeacherJobTitleAddDto> teacherJobTitleAddDtoList) {
        List<TeacherJobTitle> teacherJobTitles = teacherJobTitleAddDtoList.stream().map(teacherJobTitleAddDto -> {
            LambdaQueryWrapper<TeacherJobTitle> teacherJobTitleLambdaQueryWrapper = new LambdaQueryWrapper<>();
            teacherJobTitleLambdaQueryWrapper.eq(TeacherJobTitle::getName, teacherJobTitleAddDto.getName());
            if (teacherJobTitleMapper.exists(teacherJobTitleLambdaQueryWrapper)) {
                ThrowUtils.error(RCodeEnum.THE_TEACHER_JOB_TITLE_ALREADY_EXISTS);
            }
            TeacherJobTitle teacherJobTitle = new TeacherJobTitle();
            teacherJobTitle.setName(teacherJobTitleAddDto.getName());
            return teacherJobTitle;
        }).collect(Collectors.toList());
        if (!this.saveBatch(teacherJobTitles)) {
            ThrowUtils.error(RCodeEnum.DB_DATA_DELETION_FAILED);
        }
    }

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public Map<Long, String> get() {
        List<TeacherJobTitle> teacherJobTitles = teacherJobTitleMapper.selectList(null);
        return teacherJobTitles.stream().collect(Collectors.toMap(TeacherJobTitle::getId, TeacherJobTitle::getName));
    }

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public Page<TeacherJobTitlePageVo> pageTeacherJobTitle(Long current, Long pageSize, TeacherJobTitlePageDto teacherJobTitlePageDto) {
        // 创建原始分页数据以及返回分页数据
        Page<TeacherJobTitle> page = new Page<>(current, pageSize);
        Page<TeacherJobTitlePageVo> returnResult = new Page<>(current, pageSize);
        // 过滤分页对象
        LambdaQueryWrapper<TeacherJobTitle> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper
                .orderByDesc(TeacherJobTitle::getCreateTime);
        // 当不存在模糊查询时的分页操作
        if (BeanMetaDataUtils.isAllMetadataEmpty(teacherJobTitlePageDto)) {
            this.page(page, lambdaQueryWrapper);
            BeanUtils.copyProperties(page, returnResult, "records");
            List<TeacherJobTitlePageVo> pageVoList = page.getRecords().stream().map(record -> {
                TeacherJobTitlePageVo teacherJobTitlePageVo = new TeacherJobTitlePageVo();
                teacherJobTitlePageVo.setId(record.getId());
                teacherJobTitlePageVo.setName(record.getName());
                teacherJobTitlePageVo.setCreateTime(record.getCreateTime());
                return teacherJobTitlePageVo;
            }).collect(Collectors.toList());
            returnResult.setRecords(pageVoList);
            return returnResult;
        }
        // 当存在模糊查询时的分页操作
        lambdaQueryWrapper
                .like(StringUtils.isNotEmpty(teacherJobTitlePageDto.getName()), TeacherJobTitle::getName, teacherJobTitlePageDto.getName());
        this.page(page, lambdaQueryWrapper);
        BeanUtils.copyProperties(page, returnResult, "records");
        List<TeacherJobTitlePageVo> pageVoList = page.getRecords().stream().map(record -> {
            TeacherJobTitlePageVo teacherJobTitlePageVo = new TeacherJobTitlePageVo();
            teacherJobTitlePageVo.setId(record.getId());
            teacherJobTitlePageVo.setName(record.getName());
            teacherJobTitlePageVo.setCreateTime(record.getCreateTime());
            return teacherJobTitlePageVo;
        }).collect(Collectors.toList());
        returnResult.setRecords(pageVoList);
        return returnResult;
    }

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public TeacherJobTitleInfoVo info(Long id) {
        TeacherJobTitle teacherJobTitleInDatabase = teacherJobTitleMapper.selectById(id);
        if (Objects.isNull(teacherJobTitleInDatabase)) {
            ThrowUtils.error(RCodeEnum.THE_MODIFIED_TARGET_DOES_NOT_EXIST);
        }
        TeacherJobTitleInfoVo teacherJobTitleInfoVo = new TeacherJobTitleInfoVo();
        teacherJobTitleInfoVo.setId(teacherJobTitleInDatabase.getId());
        teacherJobTitleInfoVo.setName(teacherJobTitleInDatabase.getName());
        return teacherJobTitleInfoVo;
    }

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public void updateTeacherJobTitle(TeacherJobTitleUpdateDto teacherJobTitleUpdateDto) {
        TeacherJobTitle teacherJobTitleInDatabase = teacherJobTitleMapper.selectById(teacherJobTitleUpdateDto.getId());
        if (Objects.isNull(teacherJobTitleInDatabase)) {
            ThrowUtils.error(RCodeEnum.THE_MODIFIED_TARGET_DOES_NOT_EXIST);
        }
        if (Objects.equals(teacherJobTitleInDatabase.getName(), teacherJobTitleUpdateDto.getName())) {
            ThrowUtils.error(RCodeEnum.THE_DATA_HAS_NOT_CHANGED);
        }
        LambdaUpdateWrapper<Teacher> teacherLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        teacherLambdaUpdateWrapper
                .set(Teacher::getJobTitleName, teacherJobTitleUpdateDto.getName())
                .eq(Teacher::getJobTitleId, teacherJobTitleInDatabase.getId());
        teacherMapper.update(null, teacherLambdaUpdateWrapper);
        TeacherJobTitle teacherJobTitle = new TeacherJobTitle();
        teacherJobTitle.setId(teacherJobTitleInDatabase.getId());
        teacherJobTitle.setName(teacherJobTitleUpdateDto.getName());
        int updateResult = teacherJobTitleMapper.updateById(teacherJobTitle);
        if (updateResult == 0) {
            ThrowUtils.error(RCodeEnum.DB_DATA_MODIFICATION_FAILED);
        }
    }

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public void delete(Long id) {
        TeacherJobTitle teacherJobTitleInDatabase = teacherJobTitleMapper.selectById(id);
        if (Objects.isNull(teacherJobTitleInDatabase)) {
            ThrowUtils.error(RCodeEnum.THE_DELETED_TARGET_DOES_NOT_EXIST);
        }
        LambdaUpdateWrapper<Teacher> teacherLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        teacherLambdaUpdateWrapper
                .set(Teacher::getJobTitleName, null)
                .set(Teacher::getJobTitleId, null)
                .eq(Teacher::getJobTitleId, teacherJobTitleInDatabase.getId());
        teacherMapper.update(null, teacherLambdaUpdateWrapper);
        int deleteResult = teacherJobTitleMapper.deleteById(id);
        if (deleteResult == 0) {
            ThrowUtils.error(RCodeEnum.DB_DATA_DELETION_FAILED);
        }
    }

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public void deleteBatch(List<Long> ids) {
        ids.forEach(id -> {
            TeacherJobTitle teacherJobTitleInDatabase = teacherJobTitleMapper.selectById(id);
            if (Objects.isNull(teacherJobTitleInDatabase)) {
                ThrowUtils.error(RCodeEnum.THE_DELETED_TARGET_DOES_NOT_EXIST);
            }
            LambdaUpdateWrapper<Teacher> teacherLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            teacherLambdaUpdateWrapper
                    .set(Teacher::getJobTitleName, null)
                    .set(Teacher::getJobTitleId, null)
                    .eq(Teacher::getJobTitleId, teacherJobTitleInDatabase.getId());
            teacherMapper.update(null, teacherLambdaUpdateWrapper);
        });
        if (!this.removeBatchByIds(ids)) {
            ThrowUtils.error(RCodeEnum.DB_DATA_DELETION_FAILED);
        }
    }
}
