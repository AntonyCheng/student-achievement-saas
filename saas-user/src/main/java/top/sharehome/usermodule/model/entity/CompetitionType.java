package top.sharehome.usermodule.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 学生学科竞赛类型类
 *
 * @author AntonyCheng
 * @since 2023/7/18 15:41:24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "t_competition_type")
public class CompetitionType implements Serializable {
    private static final long serialVersionUID = 31593215368942754L;
    /**
     * 竞赛类型ID
     */
    @TableId(value = "competition_type_id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 竞赛类型名称
     */
    @TableField(value = "competition_type_name")
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

    public static final String COL_COMPETITION_TYPE_ID = "competition_type_id";

    public static final String COL_COMPETITION_TYPE_NAME = "competition_type_name";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_UPDATE_TIME = "update_time";

    public static final String COL_IS_DELETED = "is_deleted";
}