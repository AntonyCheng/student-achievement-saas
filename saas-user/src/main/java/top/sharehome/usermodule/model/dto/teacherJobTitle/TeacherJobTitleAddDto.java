package top.sharehome.usermodule.model.dto.teacherJobTitle;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 添加教师职称信息Dto类
 *
 * @author AntonyCheng
 * @since 2023/8/19 14:35:37
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherJobTitleAddDto implements Serializable {

    private static final long serialVersionUID = -3965027700820493379L;

    /**
     * 教师职称名称(助教、讲师、副教授、教授)
     */
    private String name;
}
