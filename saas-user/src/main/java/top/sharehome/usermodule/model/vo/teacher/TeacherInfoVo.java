package top.sharehome.usermodule.model.vo.teacher;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 教师信息回显Vo类
 *
 * @author AntonyCheng
 * @since 2023/8/10 10:14:37
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherInfoVo implements Serializable {

    private static final long serialVersionUID = 3088180911846289940L;

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
     * 教师角色ID JSON 字段
     */
    private String roleIds;

    /**
     * 教师职称ID
     */
    private Long jobTitleId;

    /**
     * 教师职称名称
     */
    private Long jobTitleName;
}
