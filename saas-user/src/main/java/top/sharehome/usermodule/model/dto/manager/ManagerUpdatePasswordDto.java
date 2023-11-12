package top.sharehome.usermodule.model.dto.manager;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 管理员更新密码Dto类
 *
 * @author AntonyCheng
 * @since 2023/7/24 14:34:31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManagerUpdatePasswordDto implements Serializable {

    private static final long serialVersionUID = -7608493201828620890L;

    /**
     * 管理员账号
     */
    private Long id;

    /**
     * 管理员密码
     */
    private String password;

    /**
     * 管理员更新后的密码
     */
    private String updatePassword;

    /**
     * 管理员确认更新后的密码
     */
    private String confirmUpdatePassword;
}
