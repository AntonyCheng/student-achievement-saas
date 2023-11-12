package top.sharehome.usermodule.model.dto.teacherRole;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 教师角色信息更新Dto类
 *
 * @author AntonyCheng
 * @since 2023/8/21 22:17:22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherRoleUpdateDto implements Serializable {

    private static final long serialVersionUID = 5638831335587183073L;

    /**
     * 教师角色ID
     */
    private Long id;

    /**
     * 教师角色名称(导员、授课教师、行政教师)
     */
    private String name;
}
