package top.sharehome.usermodule.model.dto.achievementType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 学生科研成果类型分页Dto类
 *
 * @author AntonyCheng
 * @since 2023/8/22 23:20:12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AchievementTypePageDto implements Serializable {

    private static final long serialVersionUID = 6972057842153076709L;

    /**
     * 成果类型名称
     */
    private String name;
}
