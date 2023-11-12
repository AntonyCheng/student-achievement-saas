package top.sharehome.gatewaydemo.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 登录Vo类
 *
 * @author AntonyCheng
 * @since 2023/7/4 09:05:36
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenVo implements Serializable {

    private static final long serialVersionUID = 787470947292061496L;

    /**
     * 用户登录ID
     */
    private Long id;

    /**
     * 用户账户，推荐手机号码
     */
    private String account;

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
}
