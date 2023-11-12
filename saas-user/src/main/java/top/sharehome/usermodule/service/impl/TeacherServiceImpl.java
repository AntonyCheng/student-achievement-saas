package top.sharehome.usermodule.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.sharehome.issuemodule.common.constant.CommonConstant;
import top.sharehome.issuemodule.common.exception_handler.customize.CustomizeTransactionException;
import top.sharehome.issuemodule.common.response.RCodeEnum;
import top.sharehome.issuemodule.model.entity.Token;
import top.sharehome.issuemodule.model.vo.token.TokenLoginVo;
import top.sharehome.issuemodule.service.micro.ManagerMicroService;
import top.sharehome.issuemodule.utils.meta.BeanMetaDataUtils;
import top.sharehome.issuemodule.utils.throwException.ThrowUtils;
import top.sharehome.usermodule.mapper.TeacherJobTitleMapper;
import top.sharehome.usermodule.mapper.TeacherMapper;
import top.sharehome.usermodule.mapper.TeacherRoleMapper;
import top.sharehome.usermodule.model.dto.teacher.TeacherAddDto;
import top.sharehome.usermodule.model.dto.teacher.TeacherPageDto;
import top.sharehome.usermodule.model.dto.teacher.TeacherUpdateDto;
import top.sharehome.usermodule.model.entity.Teacher;
import top.sharehome.usermodule.model.entity.TeacherJobTitle;
import top.sharehome.usermodule.model.entity.TeacherRole;
import top.sharehome.usermodule.model.vo.teacher.TeacherInfoVo;
import top.sharehome.usermodule.model.vo.teacher.TeacherPageVo;
import top.sharehome.usermodule.service.TeacherService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService {

    @Resource
    private TeacherMapper teacherMapper;

    @Resource
    private TeacherRoleMapper teacherRoleMapper;

    @Resource
    private TeacherJobTitleMapper teacherJobTitleMapper;

    @Resource
    private ManagerMicroService managerMicroService;

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public void add(TeacherAddDto teacherAddDto, HttpServletRequest request) {
        TokenLoginVo loginUser = (TokenLoginVo) request.getAttribute("loginUser");
        if (Objects.isNull(loginUser)) {
            ThrowUtils.error(RCodeEnum.ABNORMAL_USER_OPERATION);
        }
        LambdaQueryWrapper<Teacher> teacherLambdaQueryWrapper = new LambdaQueryWrapper<>();
        teacherLambdaQueryWrapper.eq(Teacher::getAccount, teacherAddDto.getAccount());
        if (teacherMapper.exists(teacherLambdaQueryWrapper)) {
            ThrowUtils.error(teacherAddDto.getAccount() + RCodeEnum.USERNAME_ALREADY_EXISTS.getMessage(),
                    RCodeEnum.USERNAME_ALREADY_EXISTS);
        }
        Teacher teacher = new Teacher();
        BeanUtils.copyProperties(teacherAddDto, teacher);
        teacher.setPassword(teacherAddDto.getPhone().substring(5));
        int insertResult = teacherMapper.insert(teacher);
        if (insertResult == 0) {
            ThrowUtils.error(RCodeEnum.DB_DATA_ADDITION_FAILED);
        }
        Token token = new Token();
        token.setAccount(teacher.getAccount());
        token.setPassword(teacher.getPassword());
        token.setTenant(loginUser.getTenant());
        token.setIdentity(CommonConstant.LOGIN_IDENTITY_TEACHER);
        managerMicroService.addSinglePerson(token);
    }

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public void addBatch(List<TeacherAddDto> teacherAddDtoList, HttpServletRequest request) {
        TokenLoginVo loginUser = (TokenLoginVo) request.getAttribute("loginUser");
        if (Objects.isNull(loginUser)) {
            ThrowUtils.error(RCodeEnum.ABNORMAL_USER_OPERATION);
        }
        List<Teacher> teachers = teacherAddDtoList.stream().map(teacherAddDto -> {
            LambdaQueryWrapper<Teacher> teacherLambdaQueryWrapper = new LambdaQueryWrapper<>();
            teacherLambdaQueryWrapper.eq(Teacher::getAccount, teacherAddDto.getAccount());
            if (teacherMapper.exists(teacherLambdaQueryWrapper)) {
                ThrowUtils.error(teacherAddDto.getAccount() + RCodeEnum.USERNAME_ALREADY_EXISTS.getMessage(),
                        RCodeEnum.USERNAME_ALREADY_EXISTS);
            }
            Teacher teacher = new Teacher();
            BeanUtils.copyProperties(teacherAddDto, teacher);
            teacher.setPassword(teacherAddDto.getPhone().substring(5));
            return teacher;
        }).collect(Collectors.toList());
        if (!this.saveBatch(teachers)) {
            ThrowUtils.error(RCodeEnum.DB_DATA_ADDITION_FAILED);
        }
        List<Token> tokens = teachers.stream().map(teacher -> {
            Token token = new Token();
            token.setAccount(teacher.getAccount());
            token.setPassword(teacher.getPassword());
            token.setTenant(loginUser.getTenant());
            token.setIdentity(CommonConstant.LOGIN_IDENTITY_TEACHER);
            return token;
        }).collect(Collectors.toList());
        managerMicroService.addMultiplePerson(tokens);
    }

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public void delete(Long id, HttpServletRequest request) {
        TokenLoginVo loginUser = (TokenLoginVo) request.getAttribute("loginUser");
        if (Objects.isNull(loginUser)) {
            ThrowUtils.error(RCodeEnum.ABNORMAL_USER_OPERATION);
        }
        Teacher teacherInDatabase = teacherMapper.selectById(id);
        if (Objects.isNull(teacherInDatabase)) {
            ThrowUtils.error(RCodeEnum.THE_DELETED_TARGET_DOES_NOT_EXIST);
        }
        int deleteResult = teacherMapper.deleteById(id);
        if (deleteResult == 0) {
            ThrowUtils.error(RCodeEnum.DB_DATA_DELETION_FAILED);
        }
        Token token = new Token();
        token.setAccount(teacherInDatabase.getAccount());
        token.setTenant(loginUser.getTenant());
        managerMicroService.deleteSinglePerson(token);
    }

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public void deleteBatch(List<Long> ids, HttpServletRequest request) {
        TokenLoginVo loginUser = (TokenLoginVo) request.getAttribute("loginUser");
        if (Objects.isNull(loginUser)) {
            ThrowUtils.error(RCodeEnum.ABNORMAL_USER_OPERATION);
        }
        List<Token> tokens = ids.stream().map(id -> {
            Teacher teacherInDatabase = teacherMapper.selectById(id);
            if (Objects.isNull(teacherInDatabase)) {
                ThrowUtils.error(RCodeEnum.THE_DELETED_TARGET_DOES_NOT_EXIST);
            }
            Token token = new Token();
            token.setAccount(teacherInDatabase.getAccount());
            token.setTenant(loginUser.getTenant());
            return token;
        }).collect(Collectors.toList());
        if (!this.removeBatchByIds(ids)) {
            ThrowUtils.error(RCodeEnum.DB_DATA_DELETION_FAILED);
        }
        managerMicroService.deleteMultiplePerson(tokens);
    }

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public Page<TeacherPageVo> pageTeacher(Long current, Long pageSize, TeacherPageDto teacherPageDto) {
        // 创建原始分页数据以及返回分页数据
        Page<Teacher> page = new Page<>(current, pageSize);
        Page<TeacherPageVo> returnResult = new Page<>(current, pageSize);

        // 过滤分页对象
        LambdaQueryWrapper<Teacher> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper
                .orderByDesc(Teacher::getCreateTime);

        // 当不存在模糊查询时的分页操作
        if (BeanMetaDataUtils.isAllMetadataEmpty(teacherPageDto)) {
            this.page(page, lambdaQueryWrapper);
            BeanUtils.copyProperties(page, returnResult, "records");
            List<TeacherPageVo> pageVoList = page.getRecords().stream().map(record -> {
                TeacherPageVo teacherPageVo = new TeacherPageVo();
                BeanUtils.copyProperties(record, teacherPageVo);
                return teacherPageVo;
            }).collect(Collectors.toList());
            returnResult.setRecords(pageVoList);
            return returnResult;
        }

        // 当存在模糊查询时的分页操作
        lambdaQueryWrapper
                .like(StringUtils.isNotEmpty(teacherPageDto.getAccount()), Teacher::getAccount, teacherPageDto.getAccount())
                .like(StringUtils.isNotEmpty(teacherPageDto.getName()), Teacher::getName, teacherPageDto.getName())
                .eq(Objects.nonNull(teacherPageDto.getGender()), Teacher::getGender, teacherPageDto.getGender())
                .like(StringUtils.isNotEmpty(teacherPageDto.getPhone()), Teacher::getPhone, teacherPageDto.getPhone())
                .like(Objects.nonNull(teacherPageDto.getRoleId()), Teacher::getRoleIds, teacherPageDto.getRoleId())
                .eq(Objects.nonNull(teacherPageDto.getJobTitleId()), Teacher::getJobTitleName, teacherPageDto.getJobTitleId());
        this.page(page, lambdaQueryWrapper);
        BeanUtils.copyProperties(page, returnResult, "records");
        List<TeacherPageVo> pageVoList = page.getRecords().stream().map(record -> {
            TeacherPageVo teacherPageVo = new TeacherPageVo();
            BeanUtils.copyProperties(record, teacherPageVo);
            return teacherPageVo;
        }).collect(Collectors.toList());
        returnResult.setRecords(pageVoList);
        return returnResult;
    }

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public TeacherInfoVo info(String id) {
        Teacher teacherInDatabase = teacherMapper.selectById(id);
        if (Objects.isNull(teacherInDatabase)) {
            ThrowUtils.error(RCodeEnum.THE_MODIFIED_TARGET_DOES_NOT_EXIST);
        }
        TeacherInfoVo teacherInfoVo = new TeacherInfoVo();
        BeanUtils.copyProperties(teacherInDatabase, teacherInfoVo);
        return teacherInfoVo;
    }

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public void updateStudent(TeacherUpdateDto teacherUpdateDto, HttpServletRequest request) {
        TokenLoginVo loginUser = (TokenLoginVo) request.getAttribute("loginUser");
        if (Objects.isNull(loginUser)) {
            ThrowUtils.error(RCodeEnum.ABNORMAL_USER_OPERATION);
        }
        Teacher teacherInDatabase = teacherMapper.selectById(teacherUpdateDto.getId());
        if (Objects.isNull(teacherInDatabase)) {
            ThrowUtils.error(RCodeEnum.THE_MODIFIED_TARGET_DOES_NOT_EXIST);
        }
        List<TeacherRole> teacherRolesInDatabase = teacherRoleMapper.selectBatchIds(teacherUpdateDto.getRoleIds());
        if (!Objects.equals(teacherUpdateDto.getRoleIds().size(), teacherRolesInDatabase.size())) {
            ThrowUtils.error(RCodeEnum.THE_TEACHER_ROLE_DOES_NOT_EXIST);
        }
        Map<Long, String> teacherRoles = teacherRolesInDatabase.stream().collect(Collectors.toMap(
                TeacherRole::getId, TeacherRole::getName
        ));
        if (Objects.equals(teacherUpdateDto.getAccount(), teacherInDatabase.getAccount())
                && Objects.equals(teacherUpdateDto.getName(), teacherInDatabase.getName())
                && Objects.equals(teacherUpdateDto.getGender(), teacherInDatabase.getGender())
                && Objects.equals(teacherUpdateDto.getPhone(), teacherInDatabase.getPhone())
                && Objects.equals(teacherUpdateDto.getJobTitleId(), teacherInDatabase.getJobTitleId())
                && Objects.equals(JSONUtil.toJsonStr(teacherRoles), teacherInDatabase.getRoleIds())) {
            ThrowUtils.error(RCodeEnum.THE_DATA_HAS_NOT_CHANGED);
        }
        Teacher teacher = new Teacher();
        teacher.setId(teacherUpdateDto.getId());
        teacher.setName(teacherUpdateDto.getName());
        teacher.setGender(teacherUpdateDto.getGender());
        teacher.setPhone(teacherUpdateDto.getPhone());
        teacher.setRoleIds(JSONUtil.toJsonStr(teacherRoles));
        if (!Objects.equals(teacherUpdateDto.getJobTitleId(), teacherInDatabase.getJobTitleId())) {
            TeacherJobTitle teacherJobTitleInDatabase = teacherJobTitleMapper.selectById(teacherUpdateDto.getJobTitleId());
            if (Objects.isNull(teacherJobTitleInDatabase)) {
                ThrowUtils.error(RCodeEnum.THE_TEACHER_JOB_TITLE_DOES_NOT_EXIST);
            }
            teacher.setJobTitleId(teacherUpdateDto.getJobTitleId());
            teacher.setJobTitleName(teacherInDatabase.getJobTitleName());
        }
        if (!Objects.equals(teacherUpdateDto.getAccount(), teacherInDatabase.getAccount())) {
            LambdaQueryWrapper<Teacher> teacherLambdaQueryWrapper = new LambdaQueryWrapper<>();
            teacherLambdaQueryWrapper.eq(Teacher::getAccount, teacherUpdateDto.getAccount());
            Long countResult = teacherMapper.selectCount(teacherLambdaQueryWrapper);
            if (countResult != 0L) {
                ThrowUtils.error(RCodeEnum.USERNAME_ALREADY_EXISTS);
            }
            teacher.setAccount(teacherUpdateDto.getAccount());
            int updateResult = teacherMapper.updateById(teacher);
            if (updateResult == 0) {
                ThrowUtils.error(RCodeEnum.DB_DATA_MODIFICATION_FAILED);
            }
            HashMap<String, Object> oldAndNewMap = new HashMap<>();
            Token token = new Token();
            token.setAccount(teacherInDatabase.getAccount());
            token.setTenant(loginUser.getTenant());
            oldAndNewMap.put("oldToken", token);
            oldAndNewMap.put("newAccount", teacherUpdateDto.getAccount());
            managerMicroService.updateSinglePersonAccount(oldAndNewMap);
        } else {
            int updateResult = teacherMapper.updateById(teacher);
            if (updateResult == 0) {
                ThrowUtils.error(RCodeEnum.DB_DATA_MODIFICATION_FAILED);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public void resetPassword(Long id, HttpServletRequest request) {
        TokenLoginVo loginUser = (TokenLoginVo) request.getAttribute("loginUser");
        if (Objects.isNull(loginUser)) {
            ThrowUtils.error(RCodeEnum.ABNORMAL_USER_OPERATION);
        }
        Teacher teacherInDatabase = teacherMapper.selectById(id);
        if (Objects.isNull(teacherInDatabase)) {
            ThrowUtils.error(RCodeEnum.THE_TEACHER_DOES_NOT_EXIST);
        }
        Teacher teacher = new Teacher();
        teacher.setId(teacherInDatabase.getId());
        teacher.setPassword(teacherInDatabase.getPhone().substring(5));
        int updateResult = teacherMapper.updateById(teacher);
        if (updateResult == 0) {
            ThrowUtils.error(RCodeEnum.DB_DATA_MODIFICATION_FAILED);
        }
        Token token = new Token();
        token.setAccount(teacherInDatabase.getAccount());
        token.setPassword(teacherInDatabase.getPhone().substring(5));
        token.setTenant(loginUser.getTenant());
        managerMicroService.resetSinglePersonPassword(token);
    }

    @Override
    public void resetPasswords(List<Long> ids, HttpServletRequest request) {
        TokenLoginVo loginUser = (TokenLoginVo) request.getAttribute("loginUser");
        if (Objects.isNull(loginUser)) {
            ThrowUtils.error(RCodeEnum.ABNORMAL_USER_OPERATION);
        }
        List<Token> tokens = ids.stream().map(id -> {
            Teacher teacherInDatabase = teacherMapper.selectById(id);
            if (Objects.isNull(teacherInDatabase)) {
                ThrowUtils.error(RCodeEnum.THE_TEACHER_DOES_NOT_EXIST);
            }
            Teacher teacher = new Teacher();
            teacher.setId(teacherInDatabase.getId());
            teacher.setPassword(teacherInDatabase.getPhone().substring(5));
            int updateResult = teacherMapper.updateById(teacher);
            if (updateResult == 0) {
                ThrowUtils.error(RCodeEnum.DB_DATA_MODIFICATION_FAILED);
            }
            Token token = new Token();
            token.setAccount(teacherInDatabase.getAccount());
            token.setPassword(teacherInDatabase.getPhone().substring(5));
            token.setTenant(loginUser.getTenant());
            return token;
        }).collect(Collectors.toList());
        managerMicroService.resetMultiplePersonPassword(tokens);
    }
}
