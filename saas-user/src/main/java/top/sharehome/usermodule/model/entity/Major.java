package top.sharehome.usermodule.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 学生专业类
 *
 * @author AntonyCheng
 * @since 2023/7/18 15:41:24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "t_major")
public class Major implements Serializable {
    private static final long serialVersionUID = -8706800413334490283L;
    /**
     * 专业ID
     */
    @TableId(value = "major_id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 专业名称
     */
    @TableField(value = "major_name")
    private String name;

    /**
     * 专业年级
     */
    @TableField(value = "major_grade")
    private String grade;

    /**
     * 专业班级
     */
    @TableField(value = "major_classes")
    private String classes;

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

    public static final String COL_MAJOR_ID = "major_id";

    public static final String COL_MAJOR_NAME = "major_name";

    public static final String COL_MAJOR_GRADE = "major_grade";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_UPDATE_TIME = "update_time";

    public static final String COL_IS_DELETED = "is_deleted";
}