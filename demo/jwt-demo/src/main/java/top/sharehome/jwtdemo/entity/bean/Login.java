package top.sharehome.jwtdemo.entity.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 登录表
 *
 * @author AntonyCheng
 * @since 2023/7/3 23:03:33
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Login implements Serializable {

    private static final long serialVersionUID = -7133348009445269555L;

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
     * 用户身份（0表示学生，1表示老师，2表示租户负责人）
     */
    private Integer identity;

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
     * 逻辑删除
     */
    private Integer isDeleted;
}
