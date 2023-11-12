package top.sharehome.usermodule.model.vo.honorType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 学生在校荣誉类型回显信息Vo类
 *
 * @author AntonyCheng
 * @since 2023/8/22 17:03:14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HonorTypeInfoVo implements Serializable {

    private static final long serialVersionUID = 2364610336139244388L;

    /**
     * 荣誉类型ID
     */
    private Long id;

    /**
     * 荣誉类型名称
     */
    private String name;
}
