package top.sharehome.usermodule.model.dto.teacher;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 添加教师信息Dto类
 *
 * @author AntonyCheng
 * @since 2023/7/26 20:56:39
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherAddDto implements Serializable {
    private static final long serialVersionUID = 985373065045282266L;

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
}
