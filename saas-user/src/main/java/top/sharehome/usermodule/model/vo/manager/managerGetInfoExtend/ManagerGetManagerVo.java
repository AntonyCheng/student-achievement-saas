package top.sharehome.usermodule.model.vo.manager.managerGetInfoExtend;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import top.sharehome.usermodule.model.vo.manager.ManagerGetInfoBaseVo;

import java.io.Serializable;

/**
 * 获取管理员信息Vo类
 *
 * @author AntonyCheng
 * @since 2023/7/19 14:55:12
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManagerGetManagerVo extends ManagerGetInfoBaseVo implements Serializable {

    private static final long serialVersionUID = 18223041563348962L;
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
     * 管理员所在学校
     */
    private String school;

    /**
     * 管理员所在学院
     */
    private String college;
}
