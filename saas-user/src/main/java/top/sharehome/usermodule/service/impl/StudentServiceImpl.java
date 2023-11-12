package top.sharehome.usermodule.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
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
import top.sharehome.usermodule.mapper.MajorMapper;
import top.sharehome.usermodule.mapper.StudentMapper;
import top.sharehome.usermodule.mapper.TeacherMapper;
import top.sharehome.usermodule.model.dto.student.StudentAddDto;
import top.sharehome.usermodule.model.dto.student.StudentPageDto;
import top.sharehome.usermodule.model.dto.student.StudentUpdateDto;
import top.sharehome.usermodule.model.entity.Major;
import top.sharehome.usermodule.model.entity.Student;
import top.sharehome.usermodule.model.entity.Teacher;
import top.sharehome.usermodule.model.vo.major.MajorPageVo;
import top.sharehome.usermodule.model.vo.student.StudentInfoVo;
import top.sharehome.usermodule.model.vo.student.StudentPageVo;
import top.sharehome.usermodule.service.StudentService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {

    @Resource
    private StudentMapper studentMapper;

    @Resource
    private MajorMapper majorMapper;

    @Resource
    private TeacherMapper teacherMapper;

    @Resource
    private ManagerMicroService managerMicroService;

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public void add(StudentAddDto studentAddDto, HttpServletRequest request) {
        TokenLoginVo loginUser = (TokenLoginVo) request.getAttribute("loginUser");
        if (Objects.isNull(loginUser)) {
            ThrowUtils.error(RCodeEnum.ABNORMAL_USER_OPERATION);
        }
        LambdaQueryWrapper<Student> studentLambdaQueryWrapper = new LambdaQueryWrapper<>();
        studentLambdaQueryWrapper.eq(Student::getAccount, studentAddDto.getAccount());
        if (studentMapper.exists(studentLambdaQueryWrapper)) {
            ThrowUtils.error(studentAddDto.getAccount() + RCodeEnum.USERNAME_ALREADY_EXISTS.getMessage(),
                    RCodeEnum.USERNAME_ALREADY_EXISTS);
        }
        Major majorInDatabase = majorMapper.selectById(studentAddDto.getMajorId());
        if (Objects.isNull(majorInDatabase)) {
            ThrowUtils.error(RCodeEnum.THE_MAJOR_DOES_NOT_EXIST);
        }
        Student student = new Student();
        BeanUtils.copyProperties(studentAddDto, student);
        student.setPassword(studentAddDto.getPhone().substring(5));
        student.setMajorName(majorInDatabase.getName());
        student.setGradeName(majorInDatabase.getGrade());
        int insertResult = studentMapper.insert(student);
        if (insertResult == 0) {
            ThrowUtils.error(RCodeEnum.DB_DATA_ADDITION_FAILED);
        }
        Token token = new Token();
        token.setAccount(student.getAccount());
        token.setPassword(student.getPassword());
        token.setTenant(loginUser.getTenant());
        token.setIdentity(CommonConstant.LOGIN_IDENTITY_STUDENT);
        managerMicroService.addSinglePerson(token);
    }

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public void addBatch(List<StudentAddDto> studentAddDtoList, HttpServletRequest request) {
        TokenLoginVo loginUser = (TokenLoginVo) request.getAttribute("loginUser");
        if (Objects.isNull(loginUser)) {
            ThrowUtils.error(RCodeEnum.ABNORMAL_USER_OPERATION);
        }
        List<Long> majorIdList = studentAddDtoList.stream().map(StudentAddDto::getMajorId).distinct().collect(Collectors.toList());
        if (majorIdList.size() != 1) {
            ThrowUtils.error(RCodeEnum.CAN_NOT_BULK_ADD_STUDENT_INFORMATION_FOR_MULTIPLE_MAJORS);
        }
        Major majorInDatabase = majorMapper.selectById(majorIdList.get(0));
        if (Objects.isNull(majorInDatabase)) {
            ThrowUtils.error(RCodeEnum.THE_MAJOR_DOES_NOT_EXIST);
        }
        List<Student> students = studentAddDtoList.stream().map(studentAddDto -> {
            LambdaQueryWrapper<Student> studentLambdaQueryWrapper = new LambdaQueryWrapper<>();
            studentLambdaQueryWrapper.eq(Student::getAccount, studentAddDto.getAccount());
            if (studentMapper.exists(studentLambdaQueryWrapper)) {
                ThrowUtils.error(studentAddDto.getAccount() + RCodeEnum.USERNAME_ALREADY_EXISTS.getMessage(),
                        RCodeEnum.USERNAME_ALREADY_EXISTS);
            }
            Student student = new Student();
            BeanUtils.copyProperties(studentAddDto, student);
            student.setPassword(studentAddDto.getPhone().substring(5));
            student.setMajorName(majorInDatabase.getName());
            student.setGradeName(majorInDatabase.getGrade());
            return student;
        }).collect(Collectors.toList());
        if (!this.saveBatch(students)) {
            ThrowUtils.error(RCodeEnum.DB_DATA_ADDITION_FAILED);
        }
        List<Token> tokens = students.stream().map(student -> {
            Token token = new Token();
            token.setAccount(student.getAccount());
            token.setPassword(student.getPassword());
            token.setTenant(loginUser.getTenant());
            token.setIdentity(CommonConstant.LOGIN_IDENTITY_STUDENT);
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
        Student studentInDatabase = studentMapper.selectById(id);
        if (Objects.isNull(studentInDatabase)) {
            ThrowUtils.error(RCodeEnum.THE_DELETED_TARGET_DOES_NOT_EXIST);
        }
        int deleteResult = studentMapper.deleteById(id);
        if (deleteResult == 0) {
            ThrowUtils.error(RCodeEnum.DB_DATA_DELETION_FAILED);
        }
        Token token = new Token();
        token.setAccount(studentInDatabase.getAccount());
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
            Student studentInDatabase = studentMapper.selectById(id);
            if (Objects.isNull(studentInDatabase)) {
                ThrowUtils.error(RCodeEnum.THE_DELETED_TARGET_DOES_NOT_EXIST);
            }
            Token token = new Token();
            token.setAccount(studentInDatabase.getAccount());
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
    public Page<StudentPageVo> pageStudent(Long current, Long pageSize, StudentPageDto studentPageDto) {
        // 创建原始分页数据以及返回分页数据
        Page<Student> page = new Page<>(current, pageSize);
        Page<StudentPageVo> returnResult = new Page<>(current, pageSize);

        // 过滤分页对象
        LambdaQueryWrapper<Student> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper
                .orderByDesc(Student::getCreateTime);

        // 当不存在模糊查询时的分页操作
        if (BeanMetaDataUtils.isAllMetadataEmpty(studentPageDto)) {
            this.page(page, lambdaQueryWrapper);
            BeanUtils.copyProperties(page, returnResult, "records");
            List<StudentPageVo> pageVoList = page.getRecords().stream().map(record -> {
                StudentPageVo studentPageVo = new StudentPageVo();
                BeanUtils.copyProperties(record, studentPageVo);
                return studentPageVo;
            }).collect(Collectors.toList());
            returnResult.setRecords(pageVoList);
            return returnResult;
        }

        // 当存在模糊查询时的分页操作
        lambdaQueryWrapper
                .like(StringUtils.isNotEmpty(studentPageDto.getAccount()), Student::getAccount, studentPageDto.getAccount())
                .like(StringUtils.isNotEmpty(studentPageDto.getName()), Student::getName, studentPageDto.getName())
                .eq(Objects.nonNull(studentPageDto.getGender()), Student::getGender, studentPageDto.getGender())
                .like(StringUtils.isNotEmpty(studentPageDto.getMajorName()), Student::getMajorName, studentPageDto.getMajorName())
                .like(StringUtils.isNotEmpty(studentPageDto.getGradeName()), Student::getGradeName, studentPageDto.getGradeName())
                .eq(Objects.nonNull(studentPageDto.getClassNumber()), Student::getClassNumber, studentPageDto.getClassNumber())
                .like(StringUtils.isNotEmpty(studentPageDto.getPhone()), Student::getPhone, studentPageDto.getPhone())
                .like(StringUtils.isNotEmpty(studentPageDto.getTeacherName()), Student::getTeacherName, studentPageDto.getTeacherName())
                .like(StringUtils.isNotEmpty(studentPageDto.getProTeacherName()), Student::getProTeacherName, studentPageDto.getProTeacherName())
                .eq(Objects.nonNull(studentPageDto.getStatus()), Student::getStatus, studentPageDto.getStatus());
        this.page(page, lambdaQueryWrapper);
        BeanUtils.copyProperties(page, returnResult, "records");
        List<StudentPageVo> pageVoList = page.getRecords().stream().map(record -> {
            StudentPageVo studentPageVo = new StudentPageVo();
            BeanUtils.copyProperties(record, studentPageVo);
            return studentPageVo;
        }).collect(Collectors.toList());
        returnResult.setRecords(pageVoList);
        return returnResult;
    }

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public StudentInfoVo info(String id) {
        Student studentInDatabase = studentMapper.selectById(id);
        if (Objects.isNull(studentInDatabase)) {
            ThrowUtils.error(RCodeEnum.THE_MODIFIED_TARGET_DOES_NOT_EXIST);
        }
        StudentInfoVo studentInfoVo = new StudentInfoVo();
        BeanUtils.copyProperties(studentInDatabase, studentInfoVo);
        return studentInfoVo;
    }

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public void updateStudent(StudentUpdateDto studentUpdateDto, HttpServletRequest request) {
        TokenLoginVo loginUser = (TokenLoginVo) request.getAttribute("loginUser");
        if (Objects.isNull(loginUser)) {
            ThrowUtils.error(RCodeEnum.ABNORMAL_USER_OPERATION);
        }
        Student studentInDatabase = studentMapper.selectById(studentUpdateDto.getId());
        if (Objects.isNull(studentInDatabase)) {
            ThrowUtils.error(RCodeEnum.THE_MODIFIED_TARGET_DOES_NOT_EXIST);
        }
        if (Objects.equals(studentUpdateDto.getAccount(), studentInDatabase.getAccount())
                && Objects.equals(studentUpdateDto.getName(), studentInDatabase.getName())
                && Objects.equals(studentUpdateDto.getGender(), studentInDatabase.getGender())
                && Objects.equals(studentUpdateDto.getMajorId(), studentInDatabase.getMajorId())
                && Objects.equals(studentUpdateDto.getClassNumber(), studentInDatabase.getClassNumber())
                && Objects.equals(studentUpdateDto.getPhone(), studentInDatabase.getPhone())
                && Objects.equals(studentUpdateDto.getTeacherId(), studentInDatabase.getTeacherId())
                && Objects.equals(studentUpdateDto.getProTeacherId(), studentInDatabase.getProTeacherId())
                && Objects.equals(studentUpdateDto.getStatus(), studentInDatabase.getStatus())
                && Objects.equals(studentUpdateDto.getStatusDescription(), studentInDatabase.getStatusDescription())) {
            ThrowUtils.error(RCodeEnum.THE_DATA_HAS_NOT_CHANGED);
        }
        Student student = new Student();
        student.setId(studentUpdateDto.getId());
        student.setName(studentUpdateDto.getName());
        student.setGender(studentUpdateDto.getGender());
        student.setPhone(studentUpdateDto.getPhone());
        student.setStatus(studentUpdateDto.getStatus());
        student.setStatusDescription(studentUpdateDto.getStatusDescription());
        if (!Objects.equals(studentUpdateDto.getMajorId(), studentInDatabase.getMajorId())) {
            Major majorInDatabase = majorMapper.selectById(studentUpdateDto.getMajorId());
            if (Objects.isNull(majorInDatabase)) {
                ThrowUtils.error(RCodeEnum.THE_MAJOR_DOES_NOT_EXIST);
            }
            student.setMajorId(studentUpdateDto.getMajorId());
            student.setMajorName(majorInDatabase.getName());
            student.setGradeName(majorInDatabase.getGrade());
            if (!majorInDatabase.getClasses().contains(studentUpdateDto.getClassNumber().toString())) {
                ThrowUtils.error(RCodeEnum.THE_CLASS_DOES_NOT_EXIST);
            }
            student.setClassNumber(studentUpdateDto.getClassNumber());
        }
        if (!Objects.equals(studentUpdateDto.getTeacherId(), studentInDatabase.getTeacherId())) {
            Teacher teacherInDatabase = teacherMapper.selectById(studentUpdateDto.getTeacherId());
            if (Objects.isNull(teacherInDatabase)) {
                ThrowUtils.error(RCodeEnum.THE_TEACHER_DOES_NOT_EXIST);
            }
            student.setTeacherId(studentUpdateDto.getTeacherId());
            student.setTeacherName(studentInDatabase.getTeacherName());
        }
        if (!Objects.equals(studentUpdateDto.getProTeacherId(), studentInDatabase.getProTeacherId())) {
            Teacher teacherInDatabase = teacherMapper.selectById(studentUpdateDto.getProTeacherId());
            if (Objects.isNull(teacherInDatabase)) {
                ThrowUtils.error(RCodeEnum.THE_TEACHER_DOES_NOT_EXIST);
            }
            student.setProTeacherId(studentUpdateDto.getProTeacherId());
            student.setProTeacherName(studentInDatabase.getProTeacherName());
        }
        if (!Objects.equals(studentUpdateDto.getAccount(), studentInDatabase.getAccount())) {
            LambdaQueryWrapper<Student> studentLambdaQueryWrapper = new LambdaQueryWrapper<>();
            studentLambdaQueryWrapper.eq(Student::getAccount, studentUpdateDto.getAccount());
            Long countResult = studentMapper.selectCount(studentLambdaQueryWrapper);
            if (countResult != 0L) {
                ThrowUtils.error(RCodeEnum.USERNAME_ALREADY_EXISTS);
            }
            student.setAccount(studentUpdateDto.getAccount());
            int updateResult = studentMapper.updateById(student);
            if (updateResult == 0) {
                ThrowUtils.error(RCodeEnum.DB_DATA_MODIFICATION_FAILED);
            }
            HashMap<String, Object> oldAndNewMap = new HashMap<>();
            Token token = new Token();
            token.setAccount(studentInDatabase.getAccount());
            token.setTenant(loginUser.getTenant());
            oldAndNewMap.put("oldToken", token);
            oldAndNewMap.put("newAccount", studentUpdateDto.getAccount());
            managerMicroService.updateSinglePersonAccount(oldAndNewMap);
        } else {
            int updateResult = studentMapper.updateById(student);
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
        Student studentInDatabase = studentMapper.selectById(id);
        if (Objects.isNull(studentInDatabase)) {
            ThrowUtils.error(RCodeEnum.THE_STUDENT_DOES_NOT_EXIST);
        }
        Student student = new Student();
        student.setId(studentInDatabase.getId());
        student.setPassword(studentInDatabase.getPhone().substring(5));
        int updateResult = studentMapper.updateById(student);
        if (updateResult == 0) {
            ThrowUtils.error(RCodeEnum.DB_DATA_MODIFICATION_FAILED);
        }
        Token token = new Token();
        token.setAccount(studentInDatabase.getAccount());
        token.setPassword(studentInDatabase.getPhone().substring(5));
        token.setTenant(loginUser.getTenant());
        managerMicroService.resetSinglePersonPassword(token);
    }

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public void resetPasswords(List<Long> ids, HttpServletRequest request) {
        TokenLoginVo loginUser = (TokenLoginVo) request.getAttribute("loginUser");
        if (Objects.isNull(loginUser)) {
            ThrowUtils.error(RCodeEnum.ABNORMAL_USER_OPERATION);
        }
        List<Token> tokens = ids.stream().map(id -> {
            Student studentInDatabase = studentMapper.selectById(id);
            if (Objects.isNull(studentInDatabase)) {
                ThrowUtils.error(RCodeEnum.THE_STUDENT_DOES_NOT_EXIST);
            }
            Student student = new Student();
            student.setId(studentInDatabase.getId());
            student.setPassword(studentInDatabase.getPhone().substring(5));
            int updateResult = studentMapper.updateById(student);
            if (updateResult == 0) {
                ThrowUtils.error(RCodeEnum.DB_DATA_MODIFICATION_FAILED);
            }
            Token token = new Token();
            token.setAccount(studentInDatabase.getAccount());
            token.setPassword(studentInDatabase.getPhone().substring(5));
            token.setTenant(loginUser.getTenant());
            return token;
        }).collect(Collectors.toList());
        managerMicroService.resetMultiplePersonPassword(tokens);
    }
}
