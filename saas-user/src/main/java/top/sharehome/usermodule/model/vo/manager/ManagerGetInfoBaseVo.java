package top.sharehome.usermodule.model.vo.manager;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 获取用户信息基类
 *
 * @author AntonyCheng
 * @since 2023/7/19 15:03:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManagerGetInfoBaseVo implements Serializable {
    private static final long serialVersionUID = 3546352696178706186L;
    /**
     * 用户身份（0表示学生，1表示教师，2表示租户负责人，3表示平台超级管理员）
     */
    private String identity;
}
