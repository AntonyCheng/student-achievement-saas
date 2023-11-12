package top.sharehome.usermodule.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 教师信息类
 *
 * @author AntonyCheng
 * @since 2023/7/18 15:41:24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "t_teacher")
public class Teacher implements Serializable {
    private static final long serialVersionUID = 8646703824700401903L;
    /**
     * 教师ID
     */
    @TableId(value = "teacher_id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 教师账户
     */
    @TableField(value = "teacher_account")
    private String account;

    /**
     * 教师密码
     */
    @TableField(value = "teacher_password")
    private String password;

    /**
     * 教师姓名
     */
    @TableField(value = "teacher_name")
    private String name;

    /**
     * 教师性别（0表示女性，1表示男性）
     */
    @TableField(value = "teacher_gender")
    private Integer gender;

    /**
     * 教师电话
     */
    @TableField(value = "teacher_phone")
    private String phone;

    /**
     * 教师角色ID JSON 字段
     */
    @TableField(value = "teacher_role_ids")
    private String roleIds;

    /**
     * 教师职称ID
     */
    @TableField(value = "teacher_job_title_id")
    private Long jobTitleId;

    /**
     * 教师职称名称
     */
    @TableField(value = "teacher_job_title_name")
    private Long jobTitleName;

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

    public static final String COL_TEACHER_ID = "teacher_id";

    public static final String COL_TEACHER_ACCOUNT = "teacher_account";

    public static final String COL_TEACHER_PASSWORD = "teacher_password";

    public static final String COL_TEACHER_NAME = "teacher_name";

    public static final String COL_TEACHER_GENDER = "teacher_gender";

    public static final String COL_TEACHER_PHONE = "teacher_phone";

    public static final String COL_TEACHER_ROLE_IDS = "teacher_role_ids";

    public static final String COL_TEACHER_JOB_TITLE_ID = "teacher_job_title_id";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_UPDATE_TIME = "update_time";

    public static final String COL_IS_DELETED = "is_deleted";
}