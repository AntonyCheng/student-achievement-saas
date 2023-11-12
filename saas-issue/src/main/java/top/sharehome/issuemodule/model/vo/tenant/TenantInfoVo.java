package top.sharehome.issuemodule.model.vo.tenant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 租户信息回显Vo类
 *
 * @author AntonyCheng
 * @since 2023/7/14 16:56:40
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TenantInfoVo implements Serializable {

    private static final long serialVersionUID = 2725041585646188430L;

    /**
     * 租户ID
     */
    private Long id;

    /**
     * 租户账户，推荐手机号码
     */
    private String account;

    /**
     * 租户负责人名字
     */
    private String name;

    /**
     * 租户学校名称
     */
    private String school;

    /**
     * 租户学院名称
     */
    private String college;

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
