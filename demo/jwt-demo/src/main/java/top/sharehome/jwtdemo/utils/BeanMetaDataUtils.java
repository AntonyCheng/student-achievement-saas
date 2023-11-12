package top.sharehome.jwtdemo.utils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

/**
 * 对于对象元素据操作的一些工具
 *
 * @author AntonyCheng
 * @since 2023/7/2 23:04:57
 */

public class BeanMetaDataUtils {

    public static Boolean isAnyMetadataEmpty(Object obj) {
        Class<?> aClass = obj.getClass();
        Field[] fields = aClass.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Object fieldValue = null;
            try {
                fieldValue = field.get(obj);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if (fieldValue == null) {
                continue;
            }
            if (fieldValue instanceof CharSequence && ((CharSequence) fieldValue).length() == 0) {
                continue;
            }
            if (fieldValue instanceof Collection && ((Collection<?>) fieldValue).isEmpty()) {
                continue;
            }
            if (fieldValue.getClass().isArray() && Array.getLength(fieldValue) == 0) {
                continue;
            }
            if (fieldValue instanceof Map<?, ?>) {
                return ((Map<?, ?>) fieldValue).isEmpty();
            }
            return false;
        }
        return true;
    }

}
