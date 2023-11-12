package top.sharehome.usermodule.model.dto.major;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 专业信息更新Dto类
 *
 * @author AntonyCheng
 * @since 2023/8/1 14:26:51
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MajorUpdateDto implements Serializable {

    private static final long serialVersionUID = -914821731342191019L;

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
}
