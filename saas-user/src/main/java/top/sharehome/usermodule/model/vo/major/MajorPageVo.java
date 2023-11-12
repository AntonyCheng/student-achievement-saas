package top.sharehome.usermodule.model.vo.major;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 专业分页Vo类
 *
 * @author AntonyCheng
 * @since 2023/8/1 11:57:42
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MajorPageVo implements Serializable {

    private static final long serialVersionUID = -5623456174218635501L;

    /**
     * 专业ID
     */
    private Long id;

    /**
     * 专业名称
     */
    private String name;

    /**
     * 专业年级
     */
    private String grade;

    /**
     * 专业班级
     */
    private List<Integer> classes;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
