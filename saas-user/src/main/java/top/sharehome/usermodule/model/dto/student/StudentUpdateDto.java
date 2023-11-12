package top.sharehome.usermodule.model.dto.student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 学生信息更新Dto类
 *
 * @author AntonyCheng
 * @since 2023/8/4 09:53:35
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentUpdateDto implements Serializable {

    private static final long serialVersionUID = 4557416089652882360L;

    /**
     * 学生ID（无需展示）
     */
    private Long id;

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
     * 学生专业ID（下拉框选择获取）
     */
    private Long majorId;

    /**
     * 学生所在班级
     */
    private Integer classNumber;

    /**
     * 学生电话
     */
    private String phone;

    /**
     * 学生导员ID（搜索获取）
     */
    private Long teacherId;

    /**
     * 学生指导教师ID（搜索获取）
     */
    private Long proTeacherId;

    /**
     * 学生状态(0表示正常，1表示转专业，2表示参军，3表示离校)
     */
    private Integer status;

    /**
     * 学生状态描述
     */
    private String statusDescription;
}
