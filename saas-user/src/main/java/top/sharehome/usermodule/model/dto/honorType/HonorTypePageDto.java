package top.sharehome.usermodule.model.dto.honorType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 学生在校荣誉类型分页Dto类
 *
 * @author AntonyCheng
 * @since 2023/8/22 17:00:09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HonorTypePageDto implements Serializable {

    private static final long serialVersionUID = 6155476650714386344L;

    /**
     * 荣誉类型名称
     */
    private String name;
}
