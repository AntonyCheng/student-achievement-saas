package top.sharehome.usermodule.model.vo.teacherRole;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 教师角色分页Vo类
 *
 * @author AntonyCheng
 * @since 2023/8/21 22:14:39
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherRolePageVo implements Serializable {

    private static final long serialVersionUID = 4939746410854443462L;

    /**
     * 教师角色ID
     */
    private Long id;

    /**
     * 教师角色名称(导员、授课教师、行政教师)
     */
    private String name;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
