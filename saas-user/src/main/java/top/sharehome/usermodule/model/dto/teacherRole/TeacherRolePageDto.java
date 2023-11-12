package top.sharehome.usermodule.model.dto.teacherRole;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 教师角色分页Dto类
 *
 * @author AntonyCheng
 * @since 2023/8/21 22:13:11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherRolePageDto implements Serializable {

    private static final long serialVersionUID = 1352756979303729598L;

    /**
     * 教师角色名称(导员、授课教师、行政教师)
     */
    private String name;
}
