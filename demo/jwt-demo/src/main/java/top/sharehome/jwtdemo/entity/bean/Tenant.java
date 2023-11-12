package top.sharehome.jwtdemo.entity.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 租户表
 *
 * @author AntonyCheng
 * @since 2023/7/3 23:51:12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tenant implements Serializable {

    private static final long serialVersionUID = 3598781117767276180L;

    /**
     * 租户ID
     */
    private Long id;

    /**
     * 租户账户，推荐手机号码
     */
    private String account;

    /**
     * 租户密码
     */
    private String password;

    /**
     * 租户负责人名字
     */
    private String name;

    /**
     * 租户负责人性别
     */
    private Integer gender;

    /**
     * 租户负责人年龄
     */
    private Integer age;

    /**
     * 租户负责人邮箱
     */
    private String email;

    /**
     * 学校名称
     */
    private String school;

    /**
     * 学院名称
     */
    private String college;

    /**
     * 学院地址
     */
    private String address;

    /**
     * 租户等级（0表示0-500，1表示501-1000，2表示1001-1500，3表示1501-2000，4表示2000人以上）
     */
    private Integer level;

    /**
     * 租户状态（0表示待审核，1表示启用，2表示禁用）
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
