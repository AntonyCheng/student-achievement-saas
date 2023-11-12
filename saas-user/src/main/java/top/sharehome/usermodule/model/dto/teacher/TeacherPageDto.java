package top.sharehome.usermodule.model.dto.teacher;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 教师分页Dto类
 *
 * @author AntonyCheng
 * @since 2023/8/7 10:54:54
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherPageDto implements Serializable {

    private static final long serialVersionUID = -378270507085154104L;

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
     * 教师角色名称
     */
    private Long roleId;

    /**
     * 教师职称名称
     */
    private Long jobTitleId;
}
