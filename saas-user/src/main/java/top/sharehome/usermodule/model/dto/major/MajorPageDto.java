package top.sharehome.usermodule.model.dto.major;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 专业分页Dto类
 *
 * @author AntonyCheng
 * @since 2023/8/1 10:55:39
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MajorPageDto implements Serializable {

    private static final long serialVersionUID = 3964051457729101987L;
    /**
     * 专业名称
     */
    private String name;

    /**
     * 专业年级
     */
    private String grade;
}
