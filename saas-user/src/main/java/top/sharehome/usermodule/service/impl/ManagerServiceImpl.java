package top.sharehome.usermodule.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.sharehome.issuemodule.model.entity.Tenant;
import top.sharehome.issuemodule.model.entity.Update;
import top.sharehome.issuemodule.model.vo.token.TokenLoginVo;
import top.sharehome.issuemodule.service.micro.ManagerMicroService;
import top.sharehome.issuemodule.common.constant.CommonConstant;
import top.sharehome.issuemodule.common.exception_handler.customize.CustomizeTransactionException;
import top.sharehome.issuemodule.common.response.RCodeEnum;
import top.sharehome.usermodule.mapper.MajorMapper;
import top.sharehome.usermodule.mapper.ManagerMapper;
import top.sharehome.usermodule.mapper.StudentMapper;
import top.sharehome.usermodule.mapper.TeacherMapper;
import top.sharehome.usermodule.model.dto.manager.ManagerAddUpdateDto;
import top.sharehome.usermodule.model.dto.manager.ManagerPageDto;
import top.sharehome.usermodule.model.dto.manager.ManagerUpdatePasswordDto;
import top.sharehome.usermodule.model.entity.Major;
import top.sharehome.usermodule.model.entity.Manager;
import top.sharehome.usermodule.model.entity.Student;
import top.sharehome.usermodule.model.entity.Teacher;
import top.sharehome.usermodule.model.vo.manager.ManagerGetInfoBaseVo;
import top.sharehome.usermodule.model.vo.manager.ManagerInfoVo;
import top.sharehome.usermodule.model.vo.manager.ManagerPageVo;
import top.sharehome.usermodule.model.vo.manager.managerGetInfoExtend.ManagerGetManagerVo;
import top.sharehome.usermodule.model.vo.manager.managerGetInfoExtend.ManagerGetStudentVo;
import top.sharehome.usermodule.model.vo.manager.managerGetInfoExtend.ManagerGetTeacherVo;
import top.sharehome.usermodule.service.ManagerService;
import top.sharehome.issuemodule.utils.meta.BeanMetaDataUtils;
import top.sharehome.issuemodule.utils.throwException.ThrowUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ManagerServiceImpl extends ServiceImpl<ManagerMapper, Manager> implements ManagerService {

    @Resource
    private ManagerMapper managerMapper;

    @Resource
    private TeacherMapper teacherMapper;

    @Resource
    private StudentMapper studentMapper;

    @Resource
    private MajorMapper majorMapper;

    @DubboReference
    private ManagerMicroService managerMicroService;

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public <T extends ManagerGetInfoBaseVo> T getUserInfo(TokenLoginVo loginUser) {
        if (Objects.equals(loginUser.getIdentity(), CommonConstant.LOGIN_IDENTITY_MANAGER)) {
            LambdaQueryWrapper<Manager> managerLambdaQueryWrapper = new LambdaQueryWrapper<>();
            managerLambdaQueryWrapper.eq(Manager::getAccount, loginUser.getAccount());
            Manager manager = managerMapper.selectOne(managerLambdaQueryWrapper);
            if (Objects.isNull(manager)) {
                Tenant tenant = managerMicroService.initManager(loginUser.getTenant());
                if (Objects.isNull(tenant)) {
                    ThrowUtils.error(RCodeEnum.EXCEPTION_ON_THE_PLATFORM_MANAGEMENT_SIDE);
                }
                manager = new Manager();
                manager.setTenant(tenant.getId());
                manager.setAccount(tenant.getAccount());
                manager.setPassword(tenant.getPassword());
                manager.setName(tenant.getName());
                manager.setGender(tenant.getGender());
                manager.setEmail(tenant.getEmail());
                manager.setPicture(tenant.getPicture());
                manager.setSchool(tenant.getSchool());
                manager.setCollege(tenant.getCollege());
                manager.setLevel(tenant.getLevel());
                int insertResult = managerMapper.insert(manager);
                if (insertResult == 0) {
                    ThrowUtils.error(RCodeEnum.DB_DATA_ADDITION_FAILED);
                }
            }
            ManagerGetManagerVo managerGetManagerVo = new ManagerGetManagerVo();
            BeanUtils.copyProperties(manager, managerGetManagerVo);
            managerGetManagerVo.setIdentity(CommonConstant.LOGIN_IDENTITY_MANAGER);
            return (T) managerGetManagerVo;
        } else if (Objects.equals(loginUser.getIdentity(), CommonConstant.LOGIN_IDENTITY_TEACHER)) {
            LambdaQueryWrapper<Teacher> teacherLambdaQueryWrapper = new LambdaQueryWrapper<>();
            teacherLambdaQueryWrapper.eq(Teacher::getAccount, loginUser.getAccount());
            Teacher teacher = teacherMapper.selectOne(teacherLambdaQueryWrapper);
            if (Objects.isNull(teacher)) {
                ThrowUtils.error(RCodeEnum.THE_USER_ACCOUNT_INFORMATION_IS_ABNORMAL);
            }
            ManagerGetTeacherVo managerGetTeacherVo = new ManagerGetTeacherVo();
            BeanUtils.copyProperties(teacher, managerGetTeacherVo);
            managerGetTeacherVo.setIdentity(CommonConstant.LOGIN_IDENTITY_TEACHER);
            return (T) managerGetTeacherVo;
        } else if (Objects.equals(loginUser.getIdentity(), CommonConstant.LOGIN_IDENTITY_STUDENT)) {
            LambdaQueryWrapper<Student> studentLambdaQueryWrapper = new LambdaQueryWrapper<>();
            studentLambdaQueryWrapper.eq(Student::getAccount, loginUser.getAccount());
            Student student = studentMapper.selectOne(studentLambdaQueryWrapper);
            if (Objects.isNull(student)) {
                ThrowUtils.error(RCodeEnum.THE_USER_ACCOUNT_INFORMATION_IS_ABNORMAL);
            }
            ManagerGetStudentVo managerGetStudentVo = new ManagerGetStudentVo();
            BeanUtils.copyProperties(student, managerGetStudentVo);
            LambdaQueryWrapper<Major> majorLambdaQueryWrapper = new LambdaQueryWrapper<>();
            majorLambdaQueryWrapper.eq(Major::getId, student.getMajorId());
            Major major = majorMapper.selectOne(majorLambdaQueryWrapper);
            managerGetStudentVo.setMajor(major.getName());
            managerGetStudentVo.setGrade(major.getGrade());
            managerGetStudentVo.setIdentity(CommonConstant.LOGIN_IDENTITY_STUDENT);
            return (T) managerGetStudentVo;
        } else {
            ThrowUtils.error(RCodeEnum.THE_IDENTITY_OF_THE_LOGON_IS_ABNORMAL);
            return null;
        }
    }

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public Page<ManagerPageVo> pageManager(Long current, Long pageSize, ManagerPageDto managerPageDto) {
        // 创建原始分页数据以及返回分页数据
        Page<Manager> page = new Page<>(current, pageSize);
        Page<ManagerPageVo> returnResult = new Page<>(current, pageSize);

        // 过滤分页对象
        LambdaQueryWrapper<Manager> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper
                .orderByDesc(Manager::getCreateTime);

        // 当不存在模糊查询时的分页操作
        if (BeanMetaDataUtils.isAllMetadataEmpty(managerPageDto)) {
            this.page(page, lambdaQueryWrapper);
            BeanUtils.copyProperties(page, returnResult, "records");
            List<ManagerPageVo> pageVoList = page.getRecords().stream().map(record -> {
                ManagerPageVo managerPageVo = new ManagerPageVo();
                BeanUtils.copyProperties(record, managerPageVo);
                return managerPageVo;
            }).collect(Collectors.toList());
            returnResult.setRecords(pageVoList);
            return returnResult;
        }

        // 当存在模糊查询时的分页操作
        lambdaQueryWrapper
                .like(StringUtils.isNotEmpty(managerPageDto.getAccount()), Manager::getAccount, managerPageDto.getAccount())
                .like(StringUtils.isNotEmpty(managerPageDto.getName()), Manager::getName, managerPageDto.getName())
                .eq(Objects.nonNull(managerPageDto.getGender()), Manager::getGender, managerPageDto.getGender())
                .like(StringUtils.isNotEmpty(managerPageDto.getEmail()), Manager::getEmail, managerPageDto.getEmail())
                .like(StringUtils.isNotEmpty(managerPageDto.getSchool()), Manager::getSchool, managerPageDto.getSchool())
                .like(StringUtils.isNotEmpty(managerPageDto.getCollege()), Manager::getCollege, managerPageDto.getCollege())
                .eq(Objects.nonNull(managerPageDto.getLevel()), Manager::getLevel, managerPageDto.getLevel());
        this.page(page, lambdaQueryWrapper);
        BeanUtils.copyProperties(page, returnResult, "records");
        List<ManagerPageVo> pageVoList = page.getRecords().stream().map(record -> {
            ManagerPageVo managerPageVo = new ManagerPageVo();
            BeanUtils.copyProperties(record, managerPageVo);
            return managerPageVo;
        }).collect(Collectors.toList());
        returnResult.setRecords(pageVoList);
        return returnResult;
    }

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public ManagerInfoVo info(String id) {
        Manager managerInDatabase = managerMapper.selectById(id);
        if (Objects.isNull(managerInDatabase)) {
            ThrowUtils.error(RCodeEnum.THE_MODIFIED_TARGET_DOES_NOT_EXIST);
        }
        ManagerInfoVo managerInfoVo = new ManagerInfoVo();
        BeanUtils.copyProperties(managerInDatabase, managerInfoVo);
        return managerInfoVo;
    }

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public void addUpdateManagerInfo(ManagerAddUpdateDto managerAddUpdateDto) {
        Manager managerInDatabase = managerMapper.selectById(managerAddUpdateDto.getId());
        if (Objects.isNull(managerInDatabase)) {
            ThrowUtils.error(RCodeEnum.THE_MODIFIED_TARGET_DOES_NOT_EXIST);
        }
        if (!Objects.equals(managerInDatabase.getPassword(), managerAddUpdateDto.getPassword())) {
            ThrowUtils.error(RCodeEnum.PASSWORD_VERIFICATION_FAILED);
        }
        if (Objects.equals(managerInDatabase.getAccount(), managerAddUpdateDto.getAccount())
                && Objects.equals(managerInDatabase.getName(), managerAddUpdateDto.getName())
                && Objects.equals(managerInDatabase.getGender(), managerAddUpdateDto.getGender())
                && Objects.equals(managerInDatabase.getEmail(), managerAddUpdateDto.getEmail())
                && Objects.equals(managerInDatabase.getPicture(), managerAddUpdateDto.getPicture())) {
            ThrowUtils.error(RCodeEnum.THE_DATA_HAS_NOT_CHANGED);
        }
        Update managerUpdate = new Update();
        BeanUtils.copyProperties(managerAddUpdateDto, managerUpdate, "id");
        managerMicroService.addUpdateManagerInfo(managerUpdate);
    }

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public void updatePassword(ManagerUpdatePasswordDto managerUpdatePasswordDto) {
        Manager managerInDatabase = managerMapper.selectById(managerUpdatePasswordDto.getId());
        if (Objects.isNull(managerInDatabase)) {
            ThrowUtils.error(RCodeEnum.USER_ACCOUNT_DOES_NOT_EXIST);
        }
        if (!Objects.equals(managerInDatabase.getPassword(),managerUpdatePasswordDto.getPassword())){
            ThrowUtils.error(RCodeEnum.WRONG_USER_PASSWORD);
        }
        if (Objects.equals(managerInDatabase.getPassword(),managerUpdatePasswordDto.getUpdatePassword())){
            ThrowUtils.error(RCodeEnum.THE_UPDATE_PASSWORD_CANNOT_BE_THE_SAME_AS_THE_ORIGINAL_PASSWORD);
        }
        Tenant tenantUpdatePassword = new Tenant();
        tenantUpdatePassword.setId(managerInDatabase.getTenant());
        tenantUpdatePassword.setPassword(managerUpdatePasswordDto.getUpdatePassword());
        managerMicroService.updatePassword(tenantUpdatePassword);
    }
}
