package top.sharehome.usermodule.model.dto.teacherJobTitle;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 教师职称信息更新Dto类
 *
 * @author AntonyCheng
 * @since 2023/8/19 15:18:07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherJobTitleUpdateDto implements Serializable {

    private static final long serialVersionUID = 300237118617425158L;

    /**
     * 教师职称ID
     */
    private Long id;

    /**
     * 教师职称名称(助教、讲师、副教授、教授)
     */
    private String name;
}
