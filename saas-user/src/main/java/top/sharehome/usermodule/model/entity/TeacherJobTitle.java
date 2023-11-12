package top.sharehome.usermodule.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 教师职称类
 *
 * @author AntonyCheng
 * @since 2023/7/18 15:41:24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "t_teacher_job_title")
public class TeacherJobTitle implements Serializable {
    private static final long serialVersionUID = -4441907356920354130L;
    /**
     * 教师职称ID
     */
    @TableId(value = "job_title_id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 教师职称名称(助教、讲师、副教授、教授)
     */
    @TableField(value = "job_title_name")
    private String name;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 逻辑删除（0表示未删除，1表示已删除）
     */
    @TableField(value = "is_deleted", fill = FieldFill.INSERT)
    @TableLogic
    private Integer isDeleted;

    public static final String COL_JOB_TITLE_ID = "job_title_id";

    public static final String COL_JOB_TITLE_NAME = "job_title_name";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_UPDATE_TIME = "update_time";

    public static final String COL_IS_DELETED = "is_deleted";
}