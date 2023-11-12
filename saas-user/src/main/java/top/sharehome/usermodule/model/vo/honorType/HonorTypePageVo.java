package top.sharehome.usermodule.model.vo.honorType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 学生在校荣誉类型分页Vo类
 *
 * @author AntonyCheng
 * @since 2023/8/22 17:01:37
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HonorTypePageVo implements Serializable {

    private static final long serialVersionUID = -4972748522599960827L;

    /**
     * 荣誉类型ID
     */
    private Long id;

    /**
     * 荣誉类型名称
     */
    private String name;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
