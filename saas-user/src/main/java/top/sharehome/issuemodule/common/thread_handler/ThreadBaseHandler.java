package top.sharehome.issuemodule.common.thread_handler;

/**
 * 基于ThreadLocal的封装类，用于使用ThreadLocal在线程中传输值
 *
 * @author AntonyCheng
 * @since 2023/7/7 15:41:24
 */
public class ThreadBaseHandler {
    private static ThreadLocal<Object> threadLocal = new ThreadLocal<>();

    /**
     * 在ThreadLocal中设置值
     *
     * @param obj 需要设置的值
     */
    public static void setCurrentId(Object obj) {
        threadLocal.set(obj);

    }

    /**
     * 在ThreadLocal中获取值
     *
     * @return 返回获取的值
     */
    public static Object getCurrentId() {
        return threadLocal.get();
    }

    /**
     * 销毁ThreadLocal中的值
     */
    public static void removeCurrentId() {
        threadLocal.remove();
    }

}
