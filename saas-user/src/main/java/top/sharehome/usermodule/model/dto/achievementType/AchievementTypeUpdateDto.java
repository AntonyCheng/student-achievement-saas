package top.sharehome.usermodule.model.dto.achievementType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 学生科研成果类型信息更新Dto类
 *
 * @author AntonyCheng
 * @since 2023/8/22 23:17:35
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AchievementTypeUpdateDto implements Serializable {

    private static final long serialVersionUID = -8313759477668949681L;

    /**
     * 成果类型ID
     */
    private Long id;

    /**
     * 成果类型名称
     */
    private String name;
}
