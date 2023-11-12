package top.sharehome.usermodule.model.dto.manager;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 添加更新管理员信息Dto类
 *
 * @author AntonyCheng
 * @since 2023/7/21 09:42:47
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManagerAddUpdateDto implements Serializable {

    private static final long serialVersionUID = 3639684323427423681L;

    /**
     * 管理员ID
     */
    private Long id;

    /**
     * 管理员所在租户ID
     */
    private Long tenant;

    /**
     * 管理员账号
     */
    private String account;

    /**
     * 管理员密码
     */
    private String password;

    /**
     * 管理员姓名
     */
    private String name;

    /**
     * 管理员性别（0表示女性，1表示男性）
     */
    private Integer gender;

    /**
     * 管理员邮件
     */
    private String email;

    /**
     * 管理员图片
     */
    private String picture;

    /**
     * 租户等级
     */
    private Integer level;

}
