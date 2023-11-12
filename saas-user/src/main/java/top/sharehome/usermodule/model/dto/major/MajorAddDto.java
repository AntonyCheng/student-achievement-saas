package top.sharehome.usermodule.model.dto.major;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 添加专业信息Dto类
 *
 * @author AntonyCheng
 * @since 2023/7/27 21:29:37
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MajorAddDto implements Serializable {

    private static final long serialVersionUID = 13551121241396962L;

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
