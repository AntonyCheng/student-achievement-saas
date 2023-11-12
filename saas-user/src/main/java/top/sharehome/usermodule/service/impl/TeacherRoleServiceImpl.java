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
import top.sharehome.usermodule.mapper.TeacherMapper;
import top.sharehome.usermodule.mapper.TeacherRoleMapper;
import top.sharehome.usermodule.model.dto.teacherRole.TeacherRoleAddDto;
import top.sharehome.usermodule.model.dto.teacherRole.TeacherRolePageDto;
import top.sharehome.usermodule.model.dto.teacherRole.TeacherRoleUpdateDto;
import top.sharehome.usermodule.model.entity.Teacher;
import top.sharehome.usermodule.model.entity.TeacherRole;
import top.sharehome.usermodule.model.vo.teacherRole.TeacherRoleInfoVo;
import top.sharehome.usermodule.model.vo.teacherRole.TeacherRolePageVo;
import top.sharehome.usermodule.service.TeacherRoleService;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TeacherRoleServiceImpl extends ServiceImpl<TeacherRoleMapper, TeacherRole> implements TeacherRoleService {

    @Resource
    private TeacherRoleMapper teacherRoleMapper;

    @Resource
    private TeacherMapper teacherMapper;

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public void add(TeacherRoleAddDto teacherRoleAddDto) {
        LambdaQueryWrapper<TeacherRole> teacherRoleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        teacherRoleLambdaQueryWrapper.eq(TeacherRole::getName, teacherRoleAddDto.getName());
        if (teacherRoleMapper.exists(teacherRoleLambdaQueryWrapper)) {
            ThrowUtils.error(RCodeEnum.THE_TEACHER_ROLE_ALREADY_EXISTS);
        }
        TeacherRole teacherRole = new TeacherRole();
        teacherRole.setName(teacherRoleAddDto.getName());
        int insertResult = teacherRoleMapper.insert(teacherRole);
        if (insertResult == 0) {
            ThrowUtils.error(RCodeEnum.DB_DATA_DELETION_FAILED);
        }
    }

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public void addBatch(List<TeacherRoleAddDto> teacherRoleAddDtoList) {
        List<TeacherRole> teacherRoles = teacherRoleAddDtoList.stream().map(teacherRoleAddDto -> {
            LambdaQueryWrapper<TeacherRole> teacherRoleLambdaQueryWrapper = new LambdaQueryWrapper<>();
            teacherRoleLambdaQueryWrapper.eq(TeacherRole::getName, teacherRoleAddDto.getName());
            if (teacherRoleMapper.exists(teacherRoleLambdaQueryWrapper)) {
                ThrowUtils.error(RCodeEnum.THE_TEACHER_ROLE_ALREADY_EXISTS);
            }
            TeacherRole teacherRole = new TeacherRole();
            teacherRole.setName(teacherRoleAddDto.getName());
            return teacherRole;
        }).collect(Collectors.toList());
        if (!this.saveBatch(teacherRoles)) {
            ThrowUtils.error(RCodeEnum.DB_DATA_DELETION_FAILED);
        }
    }

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public Map<Long, String> get() {
        List<TeacherRole> teacherRoles = teacherRoleMapper.selectList(null);
        return teacherRoles.stream().collect(Collectors.toMap(TeacherRole::getId, TeacherRole::getName));
    }

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public Page<TeacherRolePageVo> pageTeacherRole(Long current, Long pageSize, TeacherRolePageDto teacherRolePageDto) {
        // 创建原始分页数据以及返回分页数据
        Page<TeacherRole> page = new Page<>(current, pageSize);
        Page<TeacherRolePageVo> returnResult = new Page<>(current, pageSize);
        // 过滤分页对象
        LambdaQueryWrapper<TeacherRole> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper
                .orderByDesc(TeacherRole::getCreateTime);
        // 当不存在模糊查询时的分页操作
        if (BeanMetaDataUtils.isAllMetadataEmpty(teacherRolePageDto)) {
            this.page(page, lambdaQueryWrapper);
            BeanUtils.copyProperties(page, returnResult, "records");
            List<TeacherRolePageVo> pageVoList = page.getRecords().stream().map(record -> {
                TeacherRolePageVo teacherRolePageVo = new TeacherRolePageVo();
                teacherRolePageVo.setId(record.getId());
                teacherRolePageVo.setName(record.getName());
                teacherRolePageVo.setCreateTime(record.getCreateTime());
                return teacherRolePageVo;
            }).collect(Collectors.toList());
            returnResult.setRecords(pageVoList);
            return returnResult;
        }
        // 当存在模糊查询时的分页操作
        lambdaQueryWrapper
                .like(StringUtils.isNotEmpty(teacherRolePageDto.getName()), TeacherRole::getName, teacherRolePageDto.getName());
        this.page(page, lambdaQueryWrapper);
        BeanUtils.copyProperties(page, returnResult, "records");
        List<TeacherRolePageVo> pageVoList = page.getRecords().stream().map(record -> {
            TeacherRolePageVo teacherRolePageVo = new TeacherRolePageVo();
            teacherRolePageVo.setId(record.getId());
            teacherRolePageVo.setName(record.getName());
            teacherRolePageVo.setCreateTime(record.getCreateTime());
            return teacherRolePageVo;
        }).collect(Collectors.toList());
        returnResult.setRecords(pageVoList);
        return returnResult;
    }

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public TeacherRoleInfoVo info(Long id) {
        TeacherRole teacherRoleInDatabase = teacherRoleMapper.selectById(id);
        if (Objects.isNull(teacherRoleInDatabase)) {
            ThrowUtils.error(RCodeEnum.THE_MODIFIED_TARGET_DOES_NOT_EXIST);
        }
        TeacherRoleInfoVo teacherRoleInfoVo = new TeacherRoleInfoVo();
        teacherRoleInfoVo.setId(teacherRoleInDatabase.getId());
        teacherRoleInfoVo.setName(teacherRoleInDatabase.getName());
        return teacherRoleInfoVo;
    }

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public void updateTeacherRole(TeacherRoleUpdateDto teacherRoleUpdateDto) {
        TeacherRole teacherRoleInDatabase = teacherRoleMapper.selectById(teacherRoleUpdateDto.getId());
        if (Objects.isNull(teacherRoleInDatabase)) {
            ThrowUtils.error(RCodeEnum.THE_MODIFIED_TARGET_DOES_NOT_EXIST);
        }
        if (Objects.equals(teacherRoleInDatabase.getName(), teacherRoleUpdateDto.getName())) {
            ThrowUtils.error(RCodeEnum.THE_DATA_HAS_NOT_CHANGED);
        }
        String fromStr = "\"" + teacherRoleInDatabase.getId() + "\":\"" + teacherRoleInDatabase.getName() + "\"";
        String toStr = "\"" + teacherRoleUpdateDto.getId() + "\":\"" + teacherRoleUpdateDto.getName() + "\"";
        LambdaUpdateWrapper<Teacher> teacherLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        teacherLambdaUpdateWrapper
                .setSql("t_teacher.teacher_role_ids = REPLACE(t_teacher.teacher_role_ids," + fromStr + "," + toStr + ")")
                .like(Teacher::getRoleIds, teacherRoleInDatabase.getId());
        teacherMapper.update(null, teacherLambdaUpdateWrapper);
        TeacherRole teacherRole = new TeacherRole();
        teacherRole.setId(teacherRoleInDatabase.getId());
        teacherRole.setName(teacherRoleUpdateDto.getName());
        int updateResult = teacherRoleMapper.updateById(teacherRole);
        if (updateResult == 0) {
            ThrowUtils.error(RCodeEnum.DB_DATA_MODIFICATION_FAILED);
        }
    }

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public void delete(Long id) {
        TeacherRole teacherRoleInDatabase = teacherRoleMapper.selectById(id);
        if (Objects.isNull(teacherRoleInDatabase)) {
            ThrowUtils.error(RCodeEnum.THE_DELETED_TARGET_DOES_NOT_EXIST);
        }
        LambdaUpdateWrapper<Teacher> teacherLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        teacherLambdaUpdateWrapper
                .set(Teacher::getRoleIds, null)
                .like(Teacher::getRoleIds, teacherRoleInDatabase.getId());
        teacherMapper.update(null, teacherLambdaUpdateWrapper);
        int deleteResult = teacherRoleMapper.deleteById(id);
        if (deleteResult == 0) {
            ThrowUtils.error(RCodeEnum.DB_DATA_DELETION_FAILED);
        }
    }

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public void deleteBatch(List<Long> ids) {
        ids.forEach(id -> {
            TeacherRole teacherRoleInDatabase = teacherRoleMapper.selectById(id);
            if (Objects.isNull(teacherRoleInDatabase)) {
                ThrowUtils.error(RCodeEnum.THE_DELETED_TARGET_DOES_NOT_EXIST);
            }
            LambdaUpdateWrapper<Teacher> teacherLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            teacherLambdaUpdateWrapper
                    .set(Teacher::getRoleIds, null)
                    .eq(Teacher::getRoleIds, teacherRoleInDatabase.getId());
            teacherMapper.update(null, teacherLambdaUpdateWrapper);
        });
        if (!this.removeBatchByIds(ids)) {
            ThrowUtils.error(RCodeEnum.DB_DATA_DELETION_FAILED);
        }
    }
}
