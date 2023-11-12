package top.sharehome.usermodule.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 学生科研成果类
 *
 * @author AntonyCheng
 * @since 2023/7/18 15:41:24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "t_achievement")
public class Achievement implements Serializable {
    private static final long serialVersionUID = -145868350506144564L;
    /**
     * 成果ID
     */
    @TableId(value = "achievement_id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 成果类型ID
     */
    @TableField(value = "achievement_type_id")
    private Long typeId;

    /**
     * 成果类型名称
     */
    @TableField(value = "achievement_type_name")
    private String typeName;

    /**
     * 成果第一作者ID
     */
    @TableField(value = "achievement_author_id")
    private Long authorId;

    /**
     * 成果第一作者姓名
     */
    @TableField(value = "achievement_author_name")
    private String authorName;

    /**
     * 成果指导教师ID
     */
    @TableField(value = "achievement_teacher_id")
    private Long teacherId;

    /**
     * 成果指导教师姓名
     */
    @TableField(value = "achievement_teacher_name")
    private String teacherName;

    /**
     * 成果审核教师ID
     */
    @TableField(value = "achievement_censor_id")
    private Long censorId;

    /**
     * 成果审核教师姓名
     */
    @TableField(value = "achievement_censor_name")
    private String censorName;

    /**
     * 成果其他成员JSON字段（包含每个成员的ID和姓名）
     */
    @TableField(value = "achievement_members")
    private String members;

    /**
     * 成果佐证材料URL
     */
    @TableField(value = "achievement_evidence")
    private String evidence;

    /**
     * 成果概述
     */
    @TableField(value = "achievement_overview")
    private String overview;

    /**
     * 成果审核反馈
     */
    @TableField(value = "achievement_feedback")
    private String feedback;

    /**
     * 成果审核状态（0表示待审核，1表示审核通过，2表示审核不通过）
     */
    @TableField(value = "achievement_status", fill = FieldFill.INSERT)
    private Integer status;

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

    public static final String COL_ACHIEVEMENT_ID = "achievement_id";

    public static final String COL_ACHIEVEMENT_TYPE_ID = "achievement_type_id";

    public static final String COL_ACHIEVEMENT_TYPE_NAME = "achievement_type_name";

    public static final String COL_ACHIEVEMENT_AUTHOR_ID = "achievement_author_id";

    public static final String COL_ACHIEVEMENT_AUTHOR_NAME = "achievement_author_name";

    public static final String COL_ACHIEVEMENT_TEACHER_ID = "achievement_teacher_id";

    public static final String COL_ACHIEVEMENT_TEACHER_NAME = "achievement_teacher_name";

    public static final String COL_ACHIEVEMENT_CENSOR_ID = "achievement_censor_id";

    public static final String COL_ACHIEVEMENT_CENSOR_NAME = "achievement_censor_name";

    public static final String COL_ACHIEVEMENT_MEMBERS = "achievement_members";

    public static final String COL_ACHIEVEMENT_EVIDENCE = "achievement_evidence";

    public static final String COL_ACHIEVEMENT_OVERVIEW = "achievement_overview";

    public static final String COL_ACHIEVEMENT_FEEDBACK = "achievement_feedback";

    public static final String COL_ACHIEVEMENT_STATUS = "achievement_status";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_UPDATE_TIME = "update_time";

    public static final String COL_IS_DELETED = "is_deleted";
}