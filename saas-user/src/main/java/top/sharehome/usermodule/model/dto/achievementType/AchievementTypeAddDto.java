package top.sharehome.usermodule.model.dto.achievementType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 添加学生科研成果类型信息Dto类
 *
 * @author AntonyCheng
 * @since 2023/8/22 23:14:18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AchievementTypeAddDto implements Serializable {

    private static final long serialVersionUID = -4868293019881988826L;

    /**
     * 成果类型名称
     */
    private String name;
}
