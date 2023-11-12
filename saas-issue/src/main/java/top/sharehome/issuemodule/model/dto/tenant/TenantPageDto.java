package top.sharehome.issuemodule.model.dto.tenant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 租户分页Dto类
 *
 * @author AntonyCheng
 * @since 2023/7/13 11:57:22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TenantPageDto implements Serializable {

    private static final long serialVersionUID = -5767487716050547786L;

    /**
     * 租户账户，推荐手机号码
     */
    private String account;

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
     * 租户容器IP地址
     */
    private String ip;

    /**
     * 租户容器端口
     */
    private String port;

    /**
     * 租户数据库IP地址
     */
    private String dbIp;

    /**
     * 租户数据库端口
     */
    private String dbPort;

    /**
     * 租户等级（0表示0-500，1表示501-1000，2表示1001-1500，3表示1501-2000，4表示2000人以上）
     */
    private Integer level;

    /**
     * 租户状态（0表示待审核，1表示启用，2表示禁用，3表示审核不通过）
     */
    private Integer status;
}
