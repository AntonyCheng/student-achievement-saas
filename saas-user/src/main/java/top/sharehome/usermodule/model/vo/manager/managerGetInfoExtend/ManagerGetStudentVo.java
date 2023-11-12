package top.sharehome.usermodule.model.vo.manager.managerGetInfoExtend;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import top.sharehome.usermodule.model.vo.manager.ManagerGetInfoBaseVo;

import java.io.Serializable;

/**
 * 获取学生信息Vo类
 *
 * @author AntonyCheng
 * @since 2023/7/19 14:56:11
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManagerGetStudentVo extends ManagerGetInfoBaseVo implements Serializable {

    private static final long serialVersionUID = 7375974444932261848L;

    /**
     * 学生ID
     */
    private Long id;

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
     * 学生专业ID
     */
    private Long majorId;

    /**
     * 学生年级
     */
    private String grade;

    /**
     * 学生专业名称
     */
    private String major;

    /**
     * 学生所在班级
     */
    private Integer classNumber;

    /**
     * 学生电话
     */
    private String phone;

    /**
     * 学生导员ID
     */
    private Long teacherId;

    /**
     * 学生导员姓名
     */
    private String teacherName;

    /**
     * 学生指导教师ID
     */
    private Long proTeacherId;

    /**
     * 学生指导教师姓名
     */
    private String proTeacherName;

    /**
     * 学生状态(0表示正常，1表示转专业，2表示参军，3表示离校)
     */
    private Integer status;

}
