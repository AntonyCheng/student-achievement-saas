package top.sharehome.usermodule.model.dto.teacherJobTitle;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 教师职称分页Dto类
 *
 * @author AntonyCheng
 * @since 2023/8/19 15:09:02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherJobTitlePageDto implements Serializable {

    private static final long serialVersionUID = 4521529548009167930L;

    /**
     * 教师职称名称(助教、讲师、副教授、教授)
     */
    private String name;
}
