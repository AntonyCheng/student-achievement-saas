package top.sharehome.admingatewaymodule.common.exception_handler.customize;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.sharehome.admingatewaymodule.common.response.R;

/**
 * 自定义返回错误异常类
 *
 * @author AntonyCheng
 * @since 2023/7/7 15:41:24
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CustomizeReturnException extends RuntimeException {
    private String description;
    private R<?> failure;

    public <T> CustomizeReturnException(R<T> failure) {
        this.failure = failure;
        this.description = "";
    }

    public <T> CustomizeReturnException(R<T> failure, String description) {
        this.failure = failure;
        this.description = description;
    }
}

