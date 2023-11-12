package top.sharehome.usermodule.model.vo.competitionType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 学生学科竞赛类型分页Vo类
 *
 * @author AntonyCheng
 * @since 2023/8/22 22:40:26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompetitionTypePageVo implements Serializable {

    private static final long serialVersionUID = 2400496104040287159L;
    /**
     * 竞赛类型ID
     */
    private Long id;

    /**
     * 竞赛类型名称
     */
    private String name;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}
