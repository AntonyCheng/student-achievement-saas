package top.sharehome.usermodule.model.dto.honorType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 添加学生在校荣誉类型信息Dto类
 *
 * @author AntonyCheng
 * @since 2023/8/22 16:26:03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HonorTypeAddDto implements Serializable {

    private static final long serialVersionUID = -7143363848923500877L;

    /**
     * 荣誉类型名称
     */
    private String name;
}
