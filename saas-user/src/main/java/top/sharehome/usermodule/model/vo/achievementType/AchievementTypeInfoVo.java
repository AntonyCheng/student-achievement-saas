package top.sharehome.usermodule.model.vo.achievementType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 学生科研成果类型回显信息Vo类
 *
 * @author AntonyCheng
 * @since 2023/8/22 23:16:20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AchievementTypeInfoVo implements Serializable {

    private static final long serialVersionUID = -7746626284469220484L;

    /**
     * 成果类型ID
     */
    private Long id;

    /**
     * 成果类型名称
     */
    private String name;
}
