package top.sharehome.usermodule.model.vo.competitionType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 学生学科竞赛类型回显信息Vo类
 *
 * @author AntonyCheng
 * @since 2023/8/22 22:45:02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompetitionTypeInfoVo implements Serializable {

    private static final long serialVersionUID = -7363295894705234829L;

    /**
     * 竞赛类型ID
     */
    private Long id;

    /**
     * 竞赛类型名称
     */
    private String name;
}
