package top.sharehome.issuemodule.common.meta_handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 自动字段填充器
 *
 * @author AntonyCheng
 * @since 2023/7/7 15:41:24
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    /**
     * 需要处理的字段名——updateTime
     */
    public static final String UPDATE_TIME = "updateTime";

    /**
     * 需要处理的字段名——createTime
     */
    public static final String CREATE_TIME = "createTime";

    /**
     * 需要处理的字段名——isDeleted
     */
    public static final String IS_DELETED = "isDeleted";

    /**
     * 需要处理的字段名——status
     */
    public static final String STATUS = "status";

    /**
     * 插入时自动填充的字段
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        if (metaObject.hasSetter(UPDATE_TIME)) {
            metaObject.setValue(UPDATE_TIME, LocalDateTime.now());
        }

        if (metaObject.hasSetter(CREATE_TIME)) {
            metaObject.setValue(CREATE_TIME, LocalDateTime.now());
        }

        if (metaObject.hasSetter(IS_DELETED)) {
            metaObject.setValue(IS_DELETED, 0);
        }

        if (metaObject.hasSetter(STATUS)) {
            metaObject.setValue(STATUS, 0);
        }
    }

    /**
     * 更新时自动填充的字段
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        if (metaObject.hasSetter(UPDATE_TIME)) {
            metaObject.setValue(UPDATE_TIME, LocalDateTime.now());
        }
    }
}
