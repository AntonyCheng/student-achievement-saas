package top.sharehome.usermodule.model.dto.teacherRole;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 添加教师角色信息Dto类
 *
 * @author AntonyCheng
 * @since 2023/8/21 22:11:52
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherRoleAddDto implements Serializable {

    private static final long serialVersionUID = -3043311648448129572L;

    /**
     * 教师角色名称(导员、授课教师、行政教师)
     */
    private String name;
}
