package top.sharehome.gatewaydemo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 登录Dto类
 *
 * @author AntonyCheng
 * @since 2023/7/4 09:21:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenLoginDto implements Serializable {

    private static final long serialVersionUID = -6038556286815942406L;

    /**
     * 用户账户，推荐手机号码
     */
    private String account;

    /**
     * 用户密码
     */
    private String password;
}
