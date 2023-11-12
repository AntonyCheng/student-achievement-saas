package top.sharehome.usermodule.model.dto.competitionType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 添加学生学科竞赛类型信息Dto类
 *
 * @author AntonyCheng
 * @since 2023/8/22 22:39:13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompetitionTypeAddDto implements Serializable {

    private static final long serialVersionUID = 1353560462504126864L;

    /**
     * 竞赛类型名称
     */
    private String name;
}
