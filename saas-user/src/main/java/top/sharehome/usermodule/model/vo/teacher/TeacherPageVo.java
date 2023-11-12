package top.sharehome.usermodule.model.vo.teacher;

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
 * 学生分页Vo类
 *
 * @author AntonyCheng
 * @since 2023/8/10 09:29:19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherPageVo implements Serializable {

    private static final long serialVersionUID = -4082595223389329351L;

    /**
     * 教师ID
     */
    private Long id;

    /**
     * 教师账户
     */
    private String account;

    /**
     * 教师姓名
     */
    private String name;

    /**
     * 教师性别（0表示女性，1表示男性）
     */
    private Integer gender;

    /**
     * 教师电话
     */
    private String phone;

    /**
     * 教师角色ID JSON 字段
     */
    private String roleIds;

    /**
     * 教师职称ID
     */
    private Long jobTitleId;

    /**
     * 教师职称名称
     */
    private Long jobTitleName;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
