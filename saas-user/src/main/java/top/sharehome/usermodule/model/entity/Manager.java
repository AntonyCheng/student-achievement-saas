package top.sharehome.usermodule.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 管理员类
 *
 * @author AntonyCheng
 * @since 2023/7/18 15:41:24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "t_manager")
public class Manager implements Serializable {
    private static final long serialVersionUID = -3735741368053264136L;
    /**
     * 管理员ID
     */
    @TableId(value = "manager_id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 管理员所在租户ID
     */
    @TableField(value = "manager_tenant")
    private Long tenant;

    /**
     * 管理员账号
     */
    @TableField(value = "manager_account")
    private String account;

    /**
     * 管理员密码
     */
    @TableField(value = "manager_password")
    private String password;

    /**
     * 管理员姓名
     */
    @TableField(value = "manager_name")
    private String name;

    /**
     * 管理员性别（0表示女性，1表示男性）
     */
    @TableField(value = "manager_gender")
    private Integer gender;

    /**
     * 管理员邮件
     */
    @TableField(value = "manager_email")
    private String email;

    /**
     * 管理员图片
     */
    @TableField(value = "manager_picture")
    private String picture;

    /**
     * 管理员所在学校
     */
    @TableField(value = "manager_school")
    private String school;

    /**
     * 管理员所在学院
     */
    @TableField(value = "manager_college")
    private String college;

    /**
     * 管理员所在租户等级（0表示0-500，1表示501-1000，2表示1001-1500，3表示1501-2000，4表示2000人以上）
     */
    @TableField(value = "manager_level")
    private Integer level;

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

    public static final String COL_ADMIN_ID = "manager_id";

    public static final String COL_ADMIN_TENANT = "manager_tenant";

    public static final String COL_ADMIN_ACCOUNT = "manager_account";

    public static final String COL_ADMIN_PASSWORD = "manager_password";

    public static final String COL_ADMIN_NAME = "manager_name";

    public static final String COL_ADMIN_GENDER = "manager_gender";

    public static final String COL_ADMIN_EMAIL = "manager_email";

    public static final String COL_ADMIN_PICTURE = "manager_picture";

    public static final String COL_ADMIN_SCHOOL = "manager_school";

    public static final String COL_ADMIN_COLLEGE = "manager_college";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_UPDATE_TIME = "update_time";

    public static final String COL_IS_DELETED = "is_deleted";
}