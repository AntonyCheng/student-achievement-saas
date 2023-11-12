package top.sharehome.issuemodule.model.dto.update;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 租户更新信息分页Dto类
 *
 * @author AntonyCheng
 * @since 2023/7/25 10:01:06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePageDto implements Serializable {

    private static final long serialVersionUID = 5641967214543434816L;

    /**
     * 修改后的管理员账号
     */
    private String account;

    /**
     * 修改后的管理员姓名
     */
    private String name;

    /**
     * 修改后的管理员性别
     */
    private Integer gender;

    /**
     * 修改后的管理员邮件
     */
    private String email;

    /**
     * 修改后的租户等级
     */
    private Integer level;

    /**
     * 请求状态（0表示待审核，1表示审核通过，2表示审核不通过）
     */
    private Integer status;
}
