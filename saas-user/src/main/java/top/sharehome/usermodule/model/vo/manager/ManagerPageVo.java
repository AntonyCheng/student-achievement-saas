package top.sharehome.usermodule.model.vo.manager;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 管理者分页Vo类
 *
 * @author AntonyCheng
 * @since 2023/7/19 21:43:03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManagerPageVo implements Serializable {

    private static final long serialVersionUID = -8702912941855557619L;
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

    /**
     * 管理员所在租户等级（0表示0-500，1表示501-1000，2表示1001-1500，3表示1501-2000，4表示2000人以上）
     */
    private Integer level;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}
