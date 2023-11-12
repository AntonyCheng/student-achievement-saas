package top.sharehome.issuemodule.common.exception_handler.customize;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.sharehome.issuemodule.common.response.R;
import top.sharehome.issuemodule.common.response.RCodeEnum;

/**
 * 自定义事物错误异常类
 *
 * @author AntonyCheng
 * @since 2023/7/7 15:41:24
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CustomizeTransactionException extends RuntimeException {

    private final String description = "数据库事物处理出现错误，回滚到该异常";

    private final R<String> failure = R.failure(RCodeEnum.ERRORS_OCCURRED_IN_THE_DATABASE_SERVICE);

}
