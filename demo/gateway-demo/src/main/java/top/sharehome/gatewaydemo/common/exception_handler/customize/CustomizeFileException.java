package top.sharehome.gatewaydemo.common.exception_handler.customize;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.sharehome.gatewaydemo.common.response.R;

/**
 * 自定义文件错误异常类
 *
 * @author AntonyCheng
 * @since 2023/7/7 15:41:24
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CustomizeFileException extends RuntimeException {
    private String description;
    private R<?> failure;

    public <T> CustomizeFileException(R<T> failure) {
        this.failure = failure;
        this.description = "";
    }

    public <T> CustomizeFileException(R<T> failure, String description) {
        this.failure = failure;
        this.description = description;
    }
}


