package org.minbox.framework.mybatis.pageable.request;

import org.minbox.framework.mybatis.pageable.Page;
import org.minbox.framework.mybatis.pageable.common.PagingStatus;
import org.minbox.framework.mybatis.pageable.common.exception.ExceptionCode;
import org.minbox.framework.mybatis.pageable.common.exception.PageableException;

/**
 * {@link ThreadLocal}上下文类
 * <p>
 * 用于处理多线程下分页对象的实例安全性问题
 *
 * @author 恒宇少年
 */
public class PageLocalContext {
    private static final ThreadLocal<PageLocal> PAGE_THREAD_LOCAL = new ThreadLocal<>();

    public static Page<?> getPageInstance() {
        PageLocal pageLocal = PAGE_THREAD_LOCAL.get();
        if (pageLocal == null) {
            throw new PageableException(ExceptionCode.PAGE_LOCAL_NOT_FOUND);
        }
        return pageLocal.getPage();
    }

    /**
     * 判定是否启动了分页
     *
     * @return 线程副本中存在 {@link PageLocal}对象并且是{@link PagingStatus#STARTED}状态时返回"true"
     */
    public static boolean isStarting() {
        PageLocal pageLocal = PAGE_THREAD_LOCAL.get();
        return pageLocal != null && PagingStatus.STARTED == pageLocal.getPagingStatus();
    }

    public static void startPaging() {
        PageLocal pageLocal = PAGE_THREAD_LOCAL.get();
        if (!isStarting()) {
            pageLocal.setPagingStatus(PagingStatus.STARTED);
        }
    }

    static void startPaging(Page<?> page) {
        if (!isStarting()) {
            PAGE_THREAD_LOCAL.set(PageLocal.instance(page));
        }
    }

    public static void skipPaging() {
        PageLocal pageLocal = PAGE_THREAD_LOCAL.get();
        pageLocal.setPagingStatus(PagingStatus.SKIPPING);
    }

    static void stopPaging() {
        PAGE_THREAD_LOCAL.remove();
    }
}
