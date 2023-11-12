package top.sharehome.usermodule.model.dto.competitionType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 学生学科竞赛类型信息更新Dto类
 *
 * @author AntonyCheng
 * @since 2023/8/22 22:46:29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompetitionTypeUpdateDto implements Serializable {

    private static final long serialVersionUID = -5181146424959763928L;

    /**
     * 竞赛类型ID
     */
    private Long id;

    /**
     * 竞赛类型名称
     */
    private String name;
}
