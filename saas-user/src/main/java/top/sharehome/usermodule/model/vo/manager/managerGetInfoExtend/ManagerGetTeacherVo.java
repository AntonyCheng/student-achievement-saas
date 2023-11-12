package top.sharehome.usermodule.model.vo.manager.managerGetInfoExtend;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import top.sharehome.usermodule.model.vo.manager.ManagerGetInfoBaseVo;

import java.io.Serializable;

/**
 * 获取教师信息Vo类
 *
 * @author AntonyCheng
 * @since 2023/7/19 14:55:47
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManagerGetTeacherVo extends ManagerGetInfoBaseVo implements Serializable {

    private static final long serialVersionUID = -7522776288749260999L;

    /**
     * 教师ID
     */
    private Long id;

    /**
     * 教师账户
     */
    private String account;

    /**
     * 教师姓名
     */
    private String name;

    /**
     * 教师性别（0表示女性，1表示男性）
     */
    private Integer gender;

    /**
     * 教师电话
     */
    private String phone;

    /**
     * 教师角色ID，名称 JSON 字段
     */
    private String roleIds;

    /**
     * 教师职称名称
     */
    private Long jobTitleName;

    /**
     * 教师职称ID
     */
    private Long jobTitleId;
}
