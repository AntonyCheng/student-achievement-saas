package top.sharehome.usermodule.model.vo.major;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 专业回显信息Vo类
 *
 * @author AntonyCheng
 * @since 2023/8/1 13:58:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MajorInfoVo implements Serializable {

    private static final long serialVersionUID = 1256103421615377026L;

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
