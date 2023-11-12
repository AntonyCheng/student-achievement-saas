package top.sharehome.usermodule.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.sharehome.usermodule.model.entity.Student;

@Mapper
public interface StudentMapper extends BaseMapper<Student> {
}