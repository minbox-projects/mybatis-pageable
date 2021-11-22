package org.minbox.framework.mybatis.pageable.request;

import org.minbox.framework.mybatis.pageable.Page;

/**
 * {@link ThreadLocal}上下文类
 * <p>
 * 用于处理多线程下分页对象的实例安全性问题
 *
 * @author 恒宇少年
 */
public class PageLocalContext {
    private static final ThreadLocal<Page<?>> PAGE_THREAD_LOCAL = new ThreadLocal<>();

    public static Page<?> getPageInstance() {
        return PAGE_THREAD_LOCAL.get();
    }

    public static boolean isStarting() {
        return PAGE_THREAD_LOCAL.get() != null;
    }

    public static void startPaging(Page<?> page) {
        PAGE_THREAD_LOCAL.set(page);
    }

    public static void stopPaging() {
        PAGE_THREAD_LOCAL.remove();
    }
}
