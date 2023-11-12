package top.sharehome.usermodule.model.vo.student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 学生信息回显Vo类
 *
 * @author AntonyCheng
 * @since 2023/8/4 00:21:35
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentInfoVo implements Serializable {

    private static final long serialVersionUID = 6972206664288516271L;

    /**
     * 学生ID（无需展示）
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
     * 学生专业ID（下拉框选择获取）
     */
    private Long majorId;

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
     * 学生导员ID（搜索获取）
     */
    private Long teacherId;

    /**
     * 学生导员姓名
     */
    private String teacherName;

    /**
     * 学生指导教师ID（搜索获取）
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

    /**
     * 学生状态描述
     */
    private String statusDescription;
}
