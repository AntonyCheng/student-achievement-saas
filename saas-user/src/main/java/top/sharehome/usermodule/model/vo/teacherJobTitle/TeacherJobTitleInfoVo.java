package top.sharehome.usermodule.model.vo.teacherJobTitle;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 教师职称回显信息Vo类
 *
 * @author AntonyCheng
 * @since 2023/8/19 15:16:19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherJobTitleInfoVo implements Serializable {

    private static final long serialVersionUID = -1949697879937787812L;

    /**
     * 教师职称ID
     */
    private Long id;

    /**
     * 教师职称名称(助教、讲师、副教授、教授)
     */
    private String name;
}
