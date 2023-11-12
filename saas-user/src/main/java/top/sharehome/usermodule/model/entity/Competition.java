package top.sharehome.usermodule.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 学生学科竞赛类
 *
 * @author AntonyCheng
 * @since 2023/7/18 15:41:24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "t_competition")
public class Competition implements Serializable {
    private static final long serialVersionUID = 8314584237367772117L;
    /**
     * 竞赛ID
     */
    @TableId(value = "competition_id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 竞赛类型ID
     */
    @TableField(value = "competition_type_id")
    private Long typeId;

    /**
     * 竞赛类型名称
     */
    @TableField(value = "competition_type_name")
    private String typeName;

    /**
     * 竞赛第一作者ID
     */
    @TableField(value = "competition_author_id")
    private Long authorId;

    /**
     * 竞赛第一作者姓名
     */
    @TableField(value = "competition_author_name")
    private String authorName;

    /**
     * 竞赛指导教师ID
     */
    @TableField(value = "competition_teacher_id")
    private Long teacherId;

    /**
     * 竞赛指导教师姓名
     */
    @TableField(value = "competition_teacher_name")
    private String teacherName;

    /**
     * 竞赛审核教师ID
     */
    @TableField(value = "competition_censor_id")
    private Long censorId;

    /**
     * 竞赛审核教师姓名
     */
    @TableField(value = "competition_censor_name")
    private String censorName;

    /**
     * 竞赛其他成员JSON字段（包含每个成员的ID和姓名）
     */
    @TableField(value = "competition_members")
    private String members;

    /**
     * 竞赛佐证材料URL
     */
    @TableField(value = "competition_evidence")
    private String evidence;

    /**
     * 竞赛概述
     */
    @TableField(value = "competition_overview")
    private String overview;

    /**
     * 竞赛审核反馈
     */
    @TableField(value = "competition_feedback")
    private String feedback;

    /**
     * 竞赛审核状态（0表示待审核，1表示审核通过，2表示审核不通过）
     */
    @TableField(value = "competition_status")
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

    public static final String COL_COMPETITION_ID = "competition_id";

    public static final String COL_COMPETITION_TYPE_ID = "competition_type_id";

    public static final String COL_COMPETITION_TYPE_NAME = "competition_type_name";

    public static final String COL_COMPETITION_AUTHOR_ID = "competition_author_id";

    public static final String COL_COMPETITION_AUTHOR_NAME = "competition_author_name";

    public static final String COL_COMPETITION_TEACHER_ID = "competition_teacher_id";

    public static final String COL_COMPETITION_TEACHER_NAME = "competition_teacher_name";

    public static final String COL_COMPETITION_CENSOR_ID = "competition_censor_id";

    public static final String COL_COMPETITION_CENSOR_NAME = "competition_censor_name";

    public static final String COL_COMPETITION_MEMBERS = "competition_members";

    public static final String COL_COMPETITION_EVIDENCE = "competition_evidence";

    public static final String COL_COMPETITION_OVERVIEW = "competition_overview";

    public static final String COL_COMPETITION_FEEDBACK = "competition_feedback";

    public static final String COL_COMPETITION_STATUS = "competition_status";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_UPDATE_TIME = "update_time";

    public static final String COL_IS_DELETED = "is_deleted";
}