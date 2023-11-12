package top.sharehome.issuemodule.model.dto.tenant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 租户找回密码Dto类
 *
 * @author AntonyCheng
 * @since 2023/7/24 23:03:59
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TenantFindPasswordDto implements Serializable {

    private static final long serialVersionUID = -992209799365569854L;

    /**
     * 租户账号
     */
    private String account;

    /**
     * 租户邮箱
     */
    private String email;

    /**
     * 找回邮箱验证码
     */
    private String code;

    /**
     * 新密码
     */
    private String password;

    /**
     * 确认新密码
     */
    private String confirmPassword;
}
