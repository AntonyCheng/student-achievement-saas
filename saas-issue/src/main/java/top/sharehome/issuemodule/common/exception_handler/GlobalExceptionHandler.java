package top.sharehome.issuemodule.common.exception_handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import top.sharehome.issuemodule.common.exception_handler.customize.CustomizeFileException;
import top.sharehome.issuemodule.common.exception_handler.customize.CustomizeReturnException;
import top.sharehome.issuemodule.common.exception_handler.customize.CustomizeTransactionException;
import top.sharehome.issuemodule.common.response.R;
import top.sharehome.issuemodule.common.response.RCodeEnum;

import java.io.Serializable;

/**
 * 全局异常处理器
 *
 * @author AntonyCheng
 * @since 2023/7/7 15:41:24
 */
@ResponseBody
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler implements Serializable {
    private static final long serialVersionUID = 7498903008120666695L;

    private static final String UNKNOWN_EXCEPTION_MESSAGE = "系统出现未知错误，全局异常拦截器已拦截";

    /**
     * 处理全局异常
     *
     * @param exception 全局异常
     * @return 返回处理结果
     */
    @ExceptionHandler(Exception.class)
    public R<?> globalExceptionHandler(Exception exception) {
        exception.printStackTrace();
        log.error("GlobalExceptionHandler:{},Description:{}", exception.getClass(), UNKNOWN_EXCEPTION_MESSAGE);
        return R.failure(RCodeEnum.SYSTEM_UNKNOWN_EXCEPTION);
    }

    /**
     * 特定异常处理：参数格式前后端不匹配问题
     *
     * @param exception 特定异常
     * @return 返回处理结果
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public R<?> methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception) {
        String message = exception.getMessage();
        if (StringUtils.isEmpty(message)) {
            return globalExceptionHandler(exception);
        }
        if (message.contains("Failed to convert value of type")
                && message.contains("to required type")
                && message.contains("nested exception is")) {
//            exception.printStackTrace();
            log.error("MethodArgumentTypeMismatchException:{},Description:{}", exception.getClass(), message);
            return R.failure(RCodeEnum.PARAMETER_FORMAT_MISMATCH);
        }
        return globalExceptionHandler(exception);
    }

    /**
     * 特定异常处理器：POST请求未带有请求体问题
     *
     * @param exception 特定异常
     * @return 返回处理结果
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public R<?> httpMessageNotReadableExceptionHandler(HttpMessageNotReadableException exception) {
        String message = exception.getMessage();
        if (StringUtils.isEmpty(message)) {
            return globalExceptionHandler(exception);
        }
        if (message.contains("Required request body is missing:")) {
//            exception.printStackTrace();
            log.error("HttpMessageNotReadableException:{},Description:{}", message, message);
            return R.failure(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY);
        }
        return globalExceptionHandler(exception);
    }

    /**
     * 特定异常处理器：REST请求方式有误
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public R<?> httpRequestMethodNotSupportedExceptionHandler(HttpRequestMethodNotSupportedException exception) {
        String message = exception.getMessage();
        if (StringUtils.isEmpty(message)) {
            return globalExceptionHandler(exception);
        }
        if (message.contains("Request method")
                && message.contains("not supported")) {
//            exception.printStackTrace();
            log.error("HttpRequestMethodNotSupportedException:{},Description:{}", message, message);
            return R.failure(RCodeEnum.REST_REQUEST_IS_DONE_IN_THE_WRONG_WAY);
        }
        return globalExceptionHandler(exception);
    }

    /**
     * 处理自定义返回异常
     *
     * @param exception 自定义返回异常
     * @return 返回处理结果
     */
    @ExceptionHandler(value = {CustomizeReturnException.class})
    public R<?> returnExceptionHandler(CustomizeReturnException exception) {
//        exception.printStackTrace();
        log.error("CustomizeReturnException:{},Description:{}", CustomizeReturnException.class, exception.getDescription());
        return exception.getFailure();
    }

    /**
     * 处理自定义事物异常
     *
     * @param exception 自定义事物异常
     * @return 返回处理结果
     */
    @ExceptionHandler(value = {CustomizeTransactionException.class})
    public R<String> returnTransactionExceptionHandler(CustomizeTransactionException exception) {
//        exception.printStackTrace();
        log.error("CustomizeTransactionException:{},Description:{}", CustomizeReturnException.class, exception.getDescription());
        return exception.getFailure();
    }

    /**
     * 处理自定义文件异常
     *
     * @param exception 自定义文件异常
     * @return 返回处理结果
     */
    @ExceptionHandler(value = {CustomizeFileException.class})
    public R<?> returnFileExceptionHandler(CustomizeFileException exception) {
//        exception.printStackTrace();
        log.error("CustomizeFileException:{},Description:{}", CustomizeReturnException.class, exception.getDescription());
        return exception.getFailure();
    }
}
