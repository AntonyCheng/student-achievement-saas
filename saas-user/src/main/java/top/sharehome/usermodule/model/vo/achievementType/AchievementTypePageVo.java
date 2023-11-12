package top.sharehome.usermodule.model.vo.achievementType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 学生科研成果类型分页Vo类
 *
 * @author AntonyCheng
 * @since 2023/8/22 23:15:28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AchievementTypePageVo implements Serializable {

    private static final long serialVersionUID = 8728875009186358567L;

    /**
     * 成果类型ID
     */
    private Long id;

    /**
     * 成果类型名称
     */
    private String name;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
