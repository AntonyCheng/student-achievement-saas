package top.sharehome.usermodule.model.dto.student;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 学生分页Dto类
 *
 * @author AntonyCheng
 * @since 2023/8/2 19:44:14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentPageDto implements Serializable {

    private static final long serialVersionUID = 5228281363827717373L;

    /**
     * 学生账号
     */
    private String account;

    /**
     * 学生姓名
     */
    private String name;

    /**
     * 学生性别（0表示女性，1表示男性）
     */
    private Integer gender;

    /**
     * 学生专业名称
     */
    private String majorName;

    /**
     * 学生年级名称
     */
    private String gradeName;

    /**
     * 学生所在班级
     */
    private Integer classNumber;

    /**
     * 学生电话
     */
    private String phone;

    /**
     * 学生导员姓名
     */
    private String teacherName;

    /**
     * 学生指导教师姓名
     */
    private String proTeacherName;

    /**
     * 学生状态(0表示正常，1表示转专业，2表示参军，3表示离校)
     */
    private Integer status;
}
