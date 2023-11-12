package top.sharehome.admingatewaymodule.model.dto.tenant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 注册Dto类
 *
 * @author AntonyCheng
 * @since 2023/7/8 09:06:13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TenantRegisterDto implements Serializable {

    private static final long serialVersionUID = 1131299434508263454L;

    /**
     * 租户账户，推荐手机号码
     */
    private String account;

    /**
     * 租户密码
     */
    private String password;

    /**
     * 租户确认密码
     */
    private String confirmPassword;

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
     * 租户等级（0表示0-500，1表示501-1000，2表示1001-1500，3表示1501-2000，4表示2000人以上）
     */
    private Integer level;
}
