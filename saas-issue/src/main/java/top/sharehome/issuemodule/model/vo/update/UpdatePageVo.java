package top.sharehome.issuemodule.model.vo.update;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 租户更新信息查询Vo类
 *
 * @author AntonyCheng
 * @since 2023/7/25 10:21:28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePageVo implements Serializable {

    private static final long serialVersionUID = 481839958409378897L;

    /**
     * 修改请求ID
     */
    private Long id;

    /**
     * 发送修改请求的租户ID
     */
    private Long tenant;

    /**
     * 修改前的管理员账号
     */
    private String beforeAccount;

    /**
     * 修改后的管理员账号
     */
    private String account;

    /**
     * 修改前的管理员姓名
     */
    private String beforeName;

    /**
     * 修改后的管理员姓名
     */
    private String name;

    /**
     * 修改前的管理员性别
     */
    private Integer beforeGender;

    /**
     * 修改后的管理员性别
     */
    private Integer gender;

    /**
     * 修改前的管理员邮件
     */
    private String beforeEmail;

    /**
     * 修改后的管理员邮件
     */
    private String email;

    /**
     * 修改后的管理员照片URL
     */
    private String beforePicture;

    /**
     * 修改后的管理员照片URL
     */
    private String picture;

    /**
     * 修改前的租户等级
     */
    private Integer beforeLevel;

    /**
     * 修改后的租户等级
     */
    private Integer level;

    /**
     * 租户所在学校学院
     */
    private String schoolCollege;

    /**
     * 请求状态（0表示待审核，1表示审核通过，2表示审核不通过）
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
