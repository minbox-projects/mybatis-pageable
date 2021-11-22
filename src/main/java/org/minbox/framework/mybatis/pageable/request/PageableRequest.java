package org.minbox.framework.mybatis.pageable.request;

import lombok.extern.slf4j.Slf4j;
import org.minbox.framework.mybatis.pageable.DefaultPage;
import org.minbox.framework.mybatis.pageable.Page;
import org.minbox.framework.mybatis.pageable.common.Assert;
import org.minbox.framework.mybatis.pageable.common.annotations.PageIndex;
import org.minbox.framework.mybatis.pageable.common.annotations.PageSize;
import org.minbox.framework.mybatis.pageable.common.exception.ExceptionCode;
import org.minbox.framework.mybatis.pageable.common.tools.AnnotationTools;

/**
 * 分页请求对象
 *
 * @author 恒宇少年
 */
@Slf4j
public class PageableRequest extends AbstractPageableRequest {
    private static int DEFAULT_PAGE_INDEX = 1;
    private static int DEFAULT_PAGE_SIZE = 10;

    private PageableRequest(int pageIndex, int pageSize) {
        super(pageIndex, pageSize);
    }

    public static Pageable of(int pageIndex, int pageSize) {
        Assert.isGtValue(pageIndex, 1, ExceptionCode.PAGE_INDEX_FAILED);
        Assert.isGtValue(pageSize, 1, ExceptionCode.PAGE_SIZE_FAILED);
        return new PageableRequest(pageIndex, pageSize);
    }

    public static Pageable of(Object pageableObject) {
        Integer pageIndex = AnnotationTools.getIntValue(pageableObject, PageIndex.class);
        Integer pageSize = AnnotationTools.getIntValue(pageableObject, PageSize.class);
        return new PageableRequest(pageIndex == null ? DEFAULT_PAGE_INDEX : pageIndex,
                pageSize == null ? DEFAULT_PAGE_SIZE : pageSize);
    }

    @Override
    public <T> Page<T> request(LogicFunction function) {
        if (PageLocalContext.isStarting()) {
            try {
                function.invoke();
                return (Page<T>) PageLocalContext.getPageInstance();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            } finally {
                if (PageLocalContext.isStarting()) {
                    PageLocalContext.stopPaging();
                }
            }
        } else {
            log.warn("There is no page replica instance in PageLocalContext.");
        }
        return DefaultPage.instance(this);
    }

    @Override
    public Pageable next() {
        return of(pageIndex + 1, pageSize);
    }

    @Override
    public Pageable previous() {
        return pageIndex == 1 ? this : of(pageIndex - 1, pageSize);
    }

    @Override
    public Pageable first() {
        return of(1, pageSize);
    }
}
