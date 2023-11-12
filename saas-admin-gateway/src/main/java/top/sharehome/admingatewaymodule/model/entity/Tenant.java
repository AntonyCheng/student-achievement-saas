package top.sharehome.admingatewaymodule.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 租户信息类
 *
 * @author AntonyCheng
 * @since 2023/7/7 15:41:24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tenant implements Serializable {
    private static final long serialVersionUID = 8325289684103014427L;
    /**
     * 租户ID
     */
    private Long id;

    /**
     * 租户账户，推荐手机号码
     */
    private String account;

    /**
     * 租户密码
     */
    private String password;

    /**
     * 租户负责人名字
     */
    private String name;

    /**
     * 租户负责人性别（0表示女性，1表示男性）
     */
    private Integer gender;

    /**
     * 租户负责人邮箱
     */
    private String email;

    /**
     * 租户负责人照片
     */
    private String picture;

    /**
     * 租户学校名称
     */
    private String school;

    /**
     * 租户学院名称
     */
    private String college;

    /**
     * 租户学院地址
     */
    private String address;

    /**
     * 租户学院坐标
     */
    private String coordinate;

    /**
     * 租户容器IP地址
     */
    private String ip;

    /**
     * 租户容器端口
     */
    private String port;

    /**
     * 租户等级（0表示0-500，1表示501-1000，2表示1001-1500，3表示1501-2000，4表示2000人以上）
     */
    private Integer level;

    /**
     * 租户状态（0表示待审核，1表示启用，2表示禁用，3表示审核不通过）
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

    public static final String COL_TENANT_ID = "tenant_id";

    public static final String COL_TENANT_ACCOUNT = "tenant_account";

    public static final String COL_TENANT_PASSWORD = "tenant_password";

    public static final String COL_TENANT_NAME = "tenant_name";

    public static final String COL_TENANT_GENDER = "tenant_gender";

    public static final String COL_TENANT_AGE = "tenant_age";

    public static final String COL_TENANT_EMAIL = "tenant_email";

    public static final String COL_TENANT_PICTURE = "tenant_picture";

    public static final String COL_TENANT_SCHOOL = "tenant_school";

    public static final String COL_TENANT_COLLEGE = "tenant_college";

    public static final String COL_TENANT_ADDRESS = "tenant_address";

    public static final String COL_TENANT_IP = "tenant_ip";

    public static final String COL_TENANT_PORT = "tenant_port";

    public static final String COL_TENANT_LEVEL = "tenant_level";

    public static final String COL_TENANT_STATUS = "tenant_status";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_UPDATE_TIME = "update_time";

    public static final String COL_IS_DELETED = "is_deleted";
}