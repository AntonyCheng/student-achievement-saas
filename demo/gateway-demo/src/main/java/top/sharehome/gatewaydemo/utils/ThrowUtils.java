package top.sharehome.gatewaydemo.utils;


import top.sharehome.gatewaydemo.common.exception_handler.customize.CustomizeReturnException;
import top.sharehome.gatewaydemo.common.response.R;
import top.sharehome.gatewaydemo.common.response.RCodeEnum;

/**
 * 抛出工具类
 *
 * @author AntonyCheng
 * @since 2023/7/8 00:30:27
 */

public class ThrowUtils {
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
}
