package top.sharehome.usergatewaymodule.model.dto.tenant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 审核Dto类
 *
 * @author AntonyCheng
 * @since 2023/7/8 19:32:52
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TenantCensorDto implements Serializable {

    private static final long serialVersionUID = -329104637788610309L;
    /**
     * 租户ID
     */
    private Long id;

    /**
     * 租户负责人账号
     */
    private String account;

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
     * 租户负责人邮箱
     */
    private String email;

    /**
     * 注册审核反馈内容
     */
    private String content;

    /**
     * 审核结果（0表示审核通过并发布，1表示审核不通过）
     */
    private Integer result;
}
