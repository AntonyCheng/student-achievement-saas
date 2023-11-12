package top.sharehome.issuemodule.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 租户信息更新类
 *
 * @author AntonyCheng
 * @since 2023/7/7 15:41:24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "t_update")
public class Update implements Serializable {
    private static final long serialVersionUID = -3374307129812345014L;
    /**
     * 修改请求ID
     */
    @TableId(value = "update_id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 发送修改请求的租户ID
     */
    @TableField(value = "update_tenant")
    private Long tenant;

    /**
     * 修改后的管理员账号
     */
    @TableField(value = "update_account")
    private String account;

    /**
     * 修改后的管理员姓名
     */
    @TableField(value = "update_name")
    private String name;

    /**
     * 修改后的管理员性别
     */
    @TableField(value = "update_gender")
    private Integer gender;

    /**
     * 修改后的管理员邮件
     */
    @TableField(value = "update_email")
    private String email;

    /**
     * 修改后的管理员照片URL
     */
    @TableField(value = "update_picture")
    private String picture;

    /**
     * 修改后的租户等级
     */
    @TableField(value = "update_level")
    private Integer level;

    /**
     * 请求状态（0表示待审核，1表示审核通过，2表示审核不通过）
     */
    @TableField(value = "update_status", fill = FieldFill.INSERT)
    private Integer status;

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

    public static final String COL_UPDATE_ID = "update_id";

    public static final String COL_UPDATE_TENANT = "update_tenant";

    public static final String COL_UPDATE_ACCOUNT = "update_account";

    public static final String COL_UPDATE_NAME = "update_name";

    public static final String COL_UPDATE_GENDER = "update_gender";

    public static final String COL_UPDATE_EMAIL = "update_email";

    public static final String COL_UPDATE_PICTURE = "update_picture";

    public static final String COL_UPDATE_LEVEL = "update_level";

    public static final String COL_UPDATE_STATUS = "update_status";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_UPDATE_TIME = "update_time";

    public static final String COL_IS_DELETED = "is_deleted";
}