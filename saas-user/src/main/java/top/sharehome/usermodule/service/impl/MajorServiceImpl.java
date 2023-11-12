package top.sharehome.usermodule.service.impl;

import cn.hutool.json.JSONUtil;
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
import top.sharehome.usermodule.mapper.MajorMapper;
import top.sharehome.usermodule.mapper.StudentMapper;
import top.sharehome.usermodule.model.dto.major.MajorAddDto;
import top.sharehome.usermodule.model.dto.major.MajorPageDto;
import top.sharehome.usermodule.model.dto.major.MajorUpdateDto;
import top.sharehome.usermodule.model.entity.Major;
import top.sharehome.usermodule.model.entity.Student;
import top.sharehome.usermodule.model.vo.major.MajorInfoVo;
import top.sharehome.usermodule.model.vo.major.MajorPageVo;
import top.sharehome.usermodule.service.MajorService;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class MajorServiceImpl extends ServiceImpl<MajorMapper, Major> implements MajorService {

    @Resource
    private MajorMapper majorMapper;

    @Resource
    private StudentMapper studentMapper;

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public void add(MajorAddDto majorAddDto) {
        LambdaQueryWrapper<Major> majorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        majorLambdaQueryWrapper
                .eq(Major::getGrade, majorAddDto.getGrade())
                .eq(Major::getName, majorAddDto.getName());
        if (majorMapper.exists(majorLambdaQueryWrapper)) {
            ThrowUtils.error(RCodeEnum.THE_MAJOR_ALREADY_EXISTS_IN_THAT_GRADE);
        }
        Major major = new Major();
        major.setName(majorAddDto.getName());
        major.setGrade(majorAddDto.getGrade());
        major.setClasses(JSONUtil.toJsonStr(majorAddDto.getClasses()));
        int insertResult = majorMapper.insert(major);
        if (insertResult == 0) {
            ThrowUtils.error(RCodeEnum.DB_DATA_ADDITION_FAILED);
        }
    }

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public void addBatch(List<MajorAddDto> majorAddDtoList) {
        List<Major> majors = majorAddDtoList.stream().map(majorAddDto -> {
            LambdaQueryWrapper<Major> majorLambdaQueryWrapper = new LambdaQueryWrapper<>();
            majorLambdaQueryWrapper
                    .eq(Major::getGrade, majorAddDto.getGrade())
                    .eq(Major::getName, majorAddDto.getName());
            if (majorMapper.exists(majorLambdaQueryWrapper)) {
                ThrowUtils.error(RCodeEnum.THE_MAJOR_ALREADY_EXISTS_IN_THAT_GRADE);
            }
            Major major = new Major();
            major.setName(majorAddDto.getName());
            major.setGrade(majorAddDto.getGrade());
            major.setClasses(JSONUtil.toJsonStr(majorAddDto.getClasses()));
            return major;
        }).collect(Collectors.toList());
        if (!this.saveBatch(majors)) {
            ThrowUtils.error(RCodeEnum.DB_DATA_ADDITION_FAILED);
        }
    }

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public List<String> getGrades() {
        List<Major> majors = majorMapper.selectList(null);
        return majors.stream().map(Major::getGrade).distinct().collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public Map<Long, String> getMajor(String grade) {
        LambdaQueryWrapper<Major> majorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        majorLambdaQueryWrapper.eq(Major::getGrade, grade);
        List<Major> majors = majorMapper.selectList(majorLambdaQueryWrapper);
        if (Objects.isNull(majors) || majors.isEmpty()) {
            ThrowUtils.error(RCodeEnum.THIS_GRADE_DOES_NOT_CONTAIN_ANY_MAJORS);
        }
        return majors.stream().collect(Collectors.toMap(Major::getId, Major::getName));
    }

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public List<Integer> getClasses(Long major) {
        Major majorInDatabase = majorMapper.selectById(major);
        if (Objects.isNull(majorInDatabase)) {
            ThrowUtils.error(RCodeEnum.THIS_MAJOR_DOES_NOT_CONTAIN_ANY_CLASSES);
        }
        return JSONUtil.toList(majorInDatabase.getClasses(), Integer.class);
    }

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public Page<MajorPageVo> pageMajor(Long current, Long pageSize, MajorPageDto majorPageDto) {
        // 创建原始分页数据以及返回分页数据
        Page<Major> page = new Page<>(current, pageSize);
        Page<MajorPageVo> returnResult = new Page<>(current, pageSize);

        // 过滤分页对象
        LambdaQueryWrapper<Major> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper
                .orderByDesc(Major::getCreateTime);

        // 当不存在模糊查询时的分页操作
        if (BeanMetaDataUtils.isAllMetadataEmpty(majorPageDto)) {
            this.page(page, lambdaQueryWrapper);
            BeanUtils.copyProperties(page, returnResult, "records");
            List<MajorPageVo> pageVoList = page.getRecords().stream().map(record -> {
                MajorPageVo majorPageVo = new MajorPageVo();
                majorPageVo.setId(record.getId());
                majorPageVo.setName(record.getName());
                majorPageVo.setGrade(record.getGrade());
                majorPageVo.setCreateTime(record.getCreateTime());
                majorPageVo.setClasses(JSONUtil.toList(record.getClasses(), Integer.class));
                return majorPageVo;
            }).collect(Collectors.toList());
            returnResult.setRecords(pageVoList);
            return returnResult;
        }

        // 当存在模糊查询时的分页操作
        lambdaQueryWrapper
                .eq(StringUtils.isNotEmpty(majorPageDto.getGrade()), Major::getGrade, majorPageDto.getGrade())
                .like(StringUtils.isNotEmpty(majorPageDto.getName()), Major::getName, majorPageDto.getName());
        this.page(page, lambdaQueryWrapper);
        BeanUtils.copyProperties(page, returnResult, "records");
        List<MajorPageVo> pageVoList = page.getRecords().stream().map(record -> {
            MajorPageVo majorPageVo = new MajorPageVo();
            majorPageVo.setId(record.getId());
            majorPageVo.setName(record.getName());
            majorPageVo.setGrade(record.getGrade());
            majorPageVo.setCreateTime(record.getCreateTime());
            majorPageVo.setClasses(JSONUtil.toList(record.getClasses(), Integer.class));
            return majorPageVo;
        }).collect(Collectors.toList());
        returnResult.setRecords(pageVoList);
        return returnResult;
    }

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public MajorInfoVo info(Long id) {
        Major majorInDatabase = majorMapper.selectById(id);
        if (Objects.isNull(majorInDatabase)) {
            ThrowUtils.error(RCodeEnum.THE_MODIFIED_TARGET_DOES_NOT_EXIST);
        }
        MajorInfoVo majorInfoVo = new MajorInfoVo();
        majorInfoVo.setId(majorInDatabase.getId());
        majorInfoVo.setName(majorInDatabase.getName());
        majorInfoVo.setGrade(majorInDatabase.getGrade());
        majorInfoVo.setClasses(JSONUtil.toList(majorInDatabase.getClasses(), Integer.class));
        return majorInfoVo;
    }

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public void updateMajor(MajorUpdateDto majorUpdateDto) {
        Major majorInDatabase = majorMapper.selectById(majorUpdateDto.getId());
        if (Objects.isNull(majorInDatabase)) {
            ThrowUtils.error(RCodeEnum.THE_MODIFIED_TARGET_DOES_NOT_EXIST);
        }
        if (Objects.equals(majorInDatabase.getGrade(), majorUpdateDto.getGrade())
                && Objects.equals(majorInDatabase.getName(), majorUpdateDto.getName())
                && Objects.equals(majorInDatabase.getClasses(), JSONUtil.toJsonStr(majorUpdateDto.getClasses()))) {
            ThrowUtils.error(RCodeEnum.THE_DATA_HAS_NOT_CHANGED);
        }
        if (!(Objects.equals(majorUpdateDto.getName(), majorInDatabase.getName())
                && Objects.equals(majorUpdateDto.getGrade(), majorInDatabase.getGrade()))) {
            LambdaQueryWrapper<Major> majorLambdaQueryWrapper = new LambdaQueryWrapper<>();
            majorLambdaQueryWrapper
                    .eq(Major::getGrade, majorUpdateDto.getGrade())
                    .eq(Major::getName, majorUpdateDto.getName());
            if (majorMapper.exists(majorLambdaQueryWrapper)) {
                ThrowUtils.error(RCodeEnum.THE_MAJOR_ALREADY_EXISTS_IN_THAT_GRADE);
            }
        }
        Major major = new Major();
        BeanUtils.copyProperties(majorUpdateDto, major, "classes");
        major.setClasses(JSONUtil.toJsonStr(majorUpdateDto.getClasses()));
        int updateResult = majorMapper.updateById(major);
        if (updateResult == 0) {
            ThrowUtils.error(RCodeEnum.DB_DATA_MODIFICATION_FAILED);
        }
        LambdaUpdateWrapper<Student> studentLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        studentLambdaUpdateWrapper
                .set(Student::getClassNumber, null)
                .set(Student::getMajorName, majorUpdateDto.getName())
                .set(Student::getGradeName, majorUpdateDto.getGrade())
                .eq(Student::getMajorId, majorUpdateDto.getId());
        studentMapper.update(null, studentLambdaUpdateWrapper);
    }

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public void delete(Long id) {
        Major majorInDatabase = majorMapper.selectById(id);
        if (Objects.isNull(majorInDatabase)) {
            ThrowUtils.error(RCodeEnum.THE_DELETED_TARGET_DOES_NOT_EXIST);
        }
        LambdaQueryWrapper<Student> studentLambdaQueryWrapper = new LambdaQueryWrapper<>();
        studentLambdaQueryWrapper.eq(Student::getMajorId, id);
        if (studentMapper.exists(studentLambdaQueryWrapper)) {
            ThrowUtils.error(RCodeEnum.CAN_NOT_DELETE_MAJOR_THAT_ALREADY_HAS_STUDENTS);
        }
        int deleteResult = majorMapper.deleteById(id);
        if (deleteResult == 0) {
            ThrowUtils.error(RCodeEnum.DB_DATA_DELETION_FAILED);
        }
    }

    @Override
    @Transactional(rollbackFor = CustomizeTransactionException.class)
    public void deleteBatch(List<Long> ids) {
        List<Major> majorsInDatabase = majorMapper.selectBatchIds(ids);
        if (majorsInDatabase.size() != ids.size()) {
            ThrowUtils.error(RCodeEnum.THE_DELETED_TARGET_DOES_NOT_EXIST);
        }
        majorsInDatabase.forEach(majorInDatabase -> {
            LambdaQueryWrapper<Student> studentLambdaQueryWrapper = new LambdaQueryWrapper<>();
            studentLambdaQueryWrapper.eq(Student::getMajorId, majorInDatabase.getId());
            if (studentMapper.exists(studentLambdaQueryWrapper)) {
                ThrowUtils.error(RCodeEnum.CAN_NOT_DELETE_MAJOR_THAT_ALREADY_HAS_STUDENTS);
            }
        });
        if (!this.removeBatchByIds(ids)) {
            ThrowUtils.error(RCodeEnum.DB_DATA_DELETION_FAILED);
        }
    }
}
