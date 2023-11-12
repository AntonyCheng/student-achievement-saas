package top.sharehome.usermodule.model.dto.honorType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 学生在校荣誉类型信息更新Dto类
 *
 * @author AntonyCheng
 * @since 2023/8/22 17:04:33
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HonorTypeUpdateDto implements Serializable {

    private static final long serialVersionUID = 5611599454482063063L;

    /**
     * 荣誉类型ID
     */
    private Long id;

    /**
     * 荣誉类型名称
     */
    private String name;
}
