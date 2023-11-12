package top.sharehome.admingatewaymodule.utils;

import top.sharehome.admingatewaymodule.common.exception_handler.customize.CustomizeReturnException;
import top.sharehome.admingatewaymodule.common.response.R;
import top.sharehome.admingatewaymodule.common.response.RCodeEnum;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 对于对象元素据操作的一些工具
 *
 * @author AntonyCheng
 * @since 2023/7/2 23:04:57
 */

public class BeanMetaDataUtils {
    /**
     * 判断一个对象中的字段是否含有空值
     *
     * @param obj 需要判断的对象
     * @return 判断结果
     */
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
                return true;
            }
            if (fieldValue instanceof CharSequence && ((CharSequence) fieldValue).length() == 0) {
                return true;
            }
            if (fieldValue instanceof Collection && ((Collection<?>) fieldValue).isEmpty()) {
                return true;
            }
            if (fieldValue.getClass().isArray() && Array.getLength(fieldValue) == 0) {
                return true;
            }
            if (fieldValue instanceof Map<?, ?> && ((Map<?, ?>) fieldValue).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断一个对象中的字段是否含有空值（可排除非判断值）
     *
     * @param obj 需要判断的对象
     * @return 判断结果
     */
    public static Boolean isAnyMetadataEmpty(Object obj, String... exclude) {
        Class<?> aClass = obj.getClass();
        Field[] fields = aClass.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            List<String> excludeList = Arrays.stream(exclude).collect(Collectors.toList());
            if (excludeList.contains(field.getName())) {
                continue;
            }
            Object fieldValue = null;
            try {
                fieldValue = field.get(obj);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if (fieldValue == null) {
                return true;
            }
            if (fieldValue instanceof CharSequence && ((CharSequence) fieldValue).length() == 0) {
                return true;
            }
            if (fieldValue instanceof Collection && ((Collection<?>) fieldValue).isEmpty()) {
                return true;
            }
            if (fieldValue.getClass().isArray() && Array.getLength(fieldValue) == 0) {
                return true;
            }
            if (fieldValue instanceof Map<?, ?> && ((Map<?, ?>) fieldValue).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断一个对象中的字段的值全部为空
     *
     * @param obj 需要判断的对象
     * @return 判断结果
     */
    public static boolean isAllMetadataEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if ("serialVersionUID".equals(field.getName())) {
                continue;
            }
            field.setAccessible(true);
            Object fieldValue = null;
            try {
                fieldValue = field.get(obj);
            } catch (IllegalAccessException e) {
                throw new CustomizeReturnException(R.failure(RCodeEnum.PARAMETER_FORMAT_MISMATCH));
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
            if (fieldValue instanceof Map<?, ?> && ((Map<?, ?>) fieldValue).isEmpty()) {
                continue;
            }
            return false;
        }
        return true;
    }
}
