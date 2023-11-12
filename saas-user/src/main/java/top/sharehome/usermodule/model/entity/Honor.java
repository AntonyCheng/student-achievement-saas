package top.sharehome.usermodule.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 学生在校荣誉类
 *
 * @author AntonyCheng
 * @since 2023/7/18 15:41:24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "t_honor")
public class Honor implements Serializable {
    private static final long serialVersionUID = -776682133562205544L;
    /**
     * 荣誉ID
     */
    @TableId(value = "honor_id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 荣誉类型ID
     */
    @TableField(value = "honor_type_id")
    private Long typeId;

    /**
     * 荣誉类型名称
     */
    @TableField(value = "honor_type_name")
    private String typeName;

    /**
     * 荣誉第一作者ID
     */
    @TableField(value = "honor_author_id")
    private Long authorId;

    /**
     * 荣誉第一作者姓名
     */
    @TableField(value = "honor_author_name")
    private String authorName;

    /**
     * 荣誉指导教师ID
     */
    @TableField(value = "honor_teacher_id")
    private Long teacherId;

    /**
     * 荣誉指导教师姓名
     */
    @TableField(value = "honor_teacher_name")
    private String teacherName;

    /**
     * 荣誉审核教师ID
     */
    @TableField(value = "honor_censor_id")
    private Long censorId;

    /**
     * 荣誉审核教师姓名
     */
    @TableField(value = "honor_censor_name")
    private String censorName;

    /**
     * 荣誉其他成员JSON字段（包含每个成员的ID和姓名）
     */
    @TableField(value = "honor_members")
    private String members;

    /**
     * 荣誉佐证材料URL
     */
    @TableField(value = "honor_evidence")
    private String evidence;

    /**
     * 荣誉概述
     */
    @TableField(value = "honor_overview")
    private String overview;

    /**
     * 荣誉审核反馈
     */
    @TableField(value = "honor_feedback")
    private String feedback;

    /**
     * 荣誉审核状态（0表示待审核，1表示审核通过，2表示审核不通过）
     */
    @TableField(value = "honor_status")
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

    public static final String COL_HONOR_ID = "honor_id";

    public static final String COL_HONOR_TYPE_ID = "honor_type_id";

    public static final String COL_HONOR_TYPE_NAME = "honor_type_name";

    public static final String COL_HONOR_AUTHOR_ID = "honor_author_id";

    public static final String COL_HONOR_AUTHOR_NAME = "honor_author_name";

    public static final String COL_HONOR_TEACHER_ID = "honor_teacher_id";

    public static final String COL_HONOR_TEACHER_NAME = "honor_teacher_name";

    public static final String COL_HONOR_CENSOR_ID = "honor_censor_id";

    public static final String COL_HONOR_CENSOR_NAME = "honor_censor_name";

    public static final String COL_HONOR_MEMBERS = "honor_members";

    public static final String COL_HONOR_EVIDENCE = "honor_evidence";

    public static final String COL_HONOR_OVERVIEW = "honor_overview";

    public static final String COL_HONOR_FEEDBACK = "honor_feedback";

    public static final String COL_HONOR_STATUS = "honor_status";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_UPDATE_TIME = "update_time";

    public static final String COL_IS_DELETED = "is_deleted";
}