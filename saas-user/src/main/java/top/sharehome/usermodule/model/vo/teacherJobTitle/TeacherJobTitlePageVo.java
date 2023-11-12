package top.sharehome.usermodule.model.vo.teacherJobTitle;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 教师职称分页Vo类
 *
 * @author AntonyCheng
 * @since 2023/8/19 14:57:24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherJobTitlePageVo implements Serializable {

    private static final long serialVersionUID = 8210180269060046025L;

    /**
     * 教师职称ID
     */
    private Long id;

    /**
     * 教师职称名称(助教、讲师、副教授、教授)
     */
    private String name;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
