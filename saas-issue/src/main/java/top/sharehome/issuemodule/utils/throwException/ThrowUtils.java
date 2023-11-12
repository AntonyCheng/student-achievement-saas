package top.sharehome.issuemodule.utils.throwException;

import top.sharehome.issuemodule.common.exception_handler.customize.CustomizeReturnException;
import top.sharehome.issuemodule.common.response.R;
import top.sharehome.issuemodule.common.response.RCodeEnum;

/**
 * 抛出工具类
 *
 * @author AntonyCheng
 * @since 2023/7/8 00:30:27
 */

public class ThrowUtils {
    /**
     * 抛出带有自定义返回消息和错误日志的异常
     *
     * @param codeEnum    返回值枚举
     * @param message     自定义返回消息
     * @param description 错误日志
     */
    public static void error(RCodeEnum codeEnum, String message, String description) {
        throw new CustomizeReturnException(R.failure(codeEnum), message, description);
    }

    /**
     * 抛出带有自定义返回消息的异常
     *
     * @param message  自定义返回消息
     * @param codeEnum 返回值枚举
     */
    public static void error(String message, RCodeEnum codeEnum) {
        throw new CustomizeReturnException(R.failure(codeEnum), message, "");
    }

    /**
     * 抛出带有错误日志的异常
     *
     * @param codeEnum    返回值枚举
     * @param description 错误日志
     */
    public static void error(RCodeEnum codeEnum, String description) {
        throw new CustomizeReturnException(R.failure(codeEnum), description);
    }

    /**
     * 抛出异常
     *
     * @param codeEnum 返回值枚举
     */
    public static void error(RCodeEnum codeEnum) {
        throw new CustomizeReturnException(R.failure(codeEnum));
    }

    /**
     * 抛出带有Map的异常
     *
     * @param key      带有Map的Key
     * @param value    带有Map的value
     * @param codeEnum 返回值枚举
     */
    public static void error(String key, String value, RCodeEnum codeEnum) {
        throw new CustomizeReturnException(R.failure(codeEnum).add(key, value));
    }
}
