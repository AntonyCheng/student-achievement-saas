package top.sharehome.usermodule.model.dto.competitionType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 学生学科竞赛类型分页Dto类
 *
 * @author AntonyCheng
 * @since 2023/8/22 22:43:03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompetitionTypePageDto implements Serializable {

    private static final long serialVersionUID = 4229807763380897135L;

    /**
     * 竞赛类型名称
     */
    private String name;
}
