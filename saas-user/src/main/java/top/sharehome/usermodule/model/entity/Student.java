package top.sharehome.usermodule.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 学生信息类
 *
 * @author AntonyCheng
 * @since 2023/7/18 15:41:24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "t_student")
public class Student implements Serializable {
    private static final long serialVersionUID = 1460302623995492469L;
    /**
     * 学生ID
     */
    @TableId(value = "student_id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 学生账号
     */
    @TableField(value = "student_account")
    private String account;

    /**
     * 学生密码
     */
    @TableField(value = "student_password")
    private String password;

    /**
     * 学生姓名
     */
    @TableField(value = "student_name")
    private String name;

    /**
     * 学生性别（0表示女性，1表示男性）
     */
    @TableField(value = "student_gender")
    private Integer gender;

    /**
     * 学生专业ID
     */
    @TableField(value = "student_major_id")
    private Long majorId;

    /**
     * 学生专业名称
     */
    @TableField(value = "student_major_name")
    private String majorName;

    /**
     * 学生年级名称
     */
    @TableField(value = "student_grade_name")
    private String gradeName;

    /**
     * 学生所在班级
     */
    @TableField(value = "student_class_number")
    private Integer classNumber;

    /**
     * 学生电话
     */
    @TableField(value = "student_phone")
    private String phone;

    /**
     * 学生导员ID
     */
    @TableField(value = "student_teacher_id")
    private Long teacherId;

    /**
     * 学生导员姓名
     */
    @TableField(value = "student_teacher_name")
    private String teacherName;

    /**
     * 学生指导教师ID
     */
    @TableField(value = "student_pro_teacher_id")
    private Long proTeacherId;

    /**
     * 学生指导教师姓名
     */
    @TableField(value = "student_pro_teacher_name")
    private String proTeacherName;

    /**
     * 学生状态(0表示正常，1表示转专业，2表示参军，3表示离校)
     */
    @TableField(value = "student_status", fill = FieldFill.INSERT)
    private Integer status;

    /**
     * 学生状态描述
     */
    @TableField(value = "student_status_description")
    private String statusDescription;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 逻辑删除（0表示未删除，1表示已删除）
     */
    @TableField(value = "is_deleted", fill = FieldFill.INSERT)
    @TableLogic
    private Integer isDeleted;

    public static final String COL_STUDENT_ID = "student_id";

    public static final String COL_STUDENT_ACCOUNT = "student_account";

    public static final String COL_STUDENT_PASSWORD = "student_password";

    public static final String COL_STUDENT_NAME = "student_name";

    public static final String COL_STUDENT_GENDER = "student_gender";

    public static final String COL_STUDENT_MAJOR_ID = "student_major_id";

    public static final String COL_STUDENT_CLASS_NUMBER = "student_class_number";

    public static final String COL_STUDENT_PHONE = "student_phone";

    public static final String COL_STUDENT_TEACHER_ID = "student_teacher_id";

    public static final String COL_STUDENT_TEACHER_NAME = "student_teacher_name";

    public static final String COL_STUDENT_PRO_TEACHER_ID = "student_pro_teacher_id";

    public static final String COL_STUDENT_PRO_TEACHER_NAME = "student_pro_teacher_name";

    public static final String COL_STUDENT_STATUS = "student_status";

    public static final String COL_STUDENT_STATUS_DESCRIPTION = "student_status_description";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_UPDATE_TIME = "update_time";

    public static final String COL_IS_DELETED = "is_deleted";
}