package top.sharehome.usermodule.model.dto.teacher;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 教师信息更新Dto类
 *
 * @author AntonyCheng
 * @since 2023/8/10 11:14:03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherUpdateDto implements Serializable {

    private static final long serialVersionUID = 4498727908004426195L;

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
     * 教师角色ID列表
     */
    private List<Long> roleIds;

    /**
     * 教师职称ID
     */
    private Long jobTitleId;
}
