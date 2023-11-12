package top.sharehome.issuemodule.model.dto.tenant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 租户找回密码Dto类
 *
 * @author AntonyCheng
 * @since 2023/7/24 20:59:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TenantPasswordEmailDto implements Serializable {

    private static final long serialVersionUID = -992209799365569854L;

    /**
     * 租户账号
     */
    private String account;

    /**
     * 租户邮箱
     */
    private String email;

}
