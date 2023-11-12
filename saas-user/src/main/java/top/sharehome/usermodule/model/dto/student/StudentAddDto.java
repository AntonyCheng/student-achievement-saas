package top.sharehome.usermodule.model.dto.student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 添加学生信息Dto类
 *
 * @author AntonyCheng
 * @since 2023/7/26 21:04:47
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentAddDto implements Serializable {

    private static final long serialVersionUID = 154848040182361263L;

    /**
     * 学生账号
     */
    private String account;

    /**
     * 学生姓名
     */
    private String name;

    /**
     * 学生性别（0表示女性，1表示男性）
     */
    private Integer gender;

    /**
     * 学生电话
     */
    private String phone;

    /**
     * 学生专业ID
     */
    private Long majorId;
}
