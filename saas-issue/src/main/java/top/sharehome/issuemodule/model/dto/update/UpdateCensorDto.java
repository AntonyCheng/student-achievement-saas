package top.sharehome.issuemodule.model.dto.update;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 管理员信息更新Dto类
 *
 * @author AntonyCheng
 * @since 2023/7/22 21:34:52
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCensorDto implements Serializable {

    private static final long serialVersionUID = -1691069612776548619L;

    /**
     * 信息更新ID
     */
    private Long id;

    /**
     * 更新信息审核反馈内容
     */
    private String content;

    /**
     * 审核结果（0表示审核通过并发布，1表示审核不通过）
     */
    private Integer result;
}
