package top.sharehome.admingatewaymodule.model.dto.tenant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 租户信息更新Dto类
 *
 * @author AntonyCheng
 * @since 2023/7/14 10:09:32
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TenantUpdateInfoDto implements Serializable {
    private static final long serialVersionUID = -3449421968542128510L;

    /**
     * 租户ID
     */
    private Long id;

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

}
