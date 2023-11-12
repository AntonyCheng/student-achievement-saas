package top.sharehome.usergatewaymodule.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户登录信息类
 *
 * @author AntonyCheng
 * @since 2023/7/7 15:41:24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Token implements Serializable {
    private static final long serialVersionUID = -1830710406849863596L;
    /**
     * 用户登录ID
     */
    private Long id;

    /**
     * 用户账户，推荐手机号码
     */
    private String account;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户所在的租户ID
     */
    private Long tenant;

    /**
     * 用户身份（0表示学生，1表示老师，2表示租户负责人，3表示平台超级管理员）
     */
    private String identity;

    /**
     * 用户的租户容器IP地址
     */
    private String ip;

    /**
     * 用户的租户容器端口
     */
    private String port;

    /**
     * 租户状态（0表示启用，1表示禁用）
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 逻辑删除（0表示未删除，1表示已删除）
     */
    private Integer isDeleted;

    public static final String COL_TOKEN_ID = "token_id";

    public static final String COL_TOKEN_ACCOUNT = "token_account";

    public static final String COL_TOKEN_PASSWORD = "token_password";

    public static final String COL_TOKEN_TENANT = "token_tenant";

    public static final String COL_TOKEN_IDENTITY = "token_identity";

    public static final String COL_TOKEN_IP = "token_ip";

    public static final String COL_TOKEN_PORT = "token_port";

    public static final String COL_TOKEN_STATUS = "token_status";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_UPDATE_TIME = "update_time";

    public static final String COL_IS_DELETED = "is_deleted";
}