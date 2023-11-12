package top.sharehome.usermodule.model.vo.teacherRole;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 教师角色回显信息Vo类
 *
 * @author AntonyCheng
 * @since 2023/8/21 22:16:07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherRoleInfoVo implements Serializable {

    private static final long serialVersionUID = 6364666966080041471L;

    /**
     * 教师角色ID
     */
    private Long id;

    /**
     * 教师角色名称(导员、授课教师、行政教师)
     */
    private String name;
}
