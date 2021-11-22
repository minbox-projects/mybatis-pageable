package org.minbox.framework.mybatis.pageable.request;

import org.minbox.framework.mybatis.pageable.DefaultPage;
import org.minbox.framework.mybatis.pageable.Page;
import org.minbox.framework.mybatis.pageable.common.Assert;
import org.minbox.framework.mybatis.pageable.common.exception.ExceptionCode;

/**
 * {@link Pageable}分页接口抽象实现类
 *
 * @author 恒宇少年
 */
public abstract class AbstractPageableRequest implements Pageable {
    /**
     * 分页当前页码
     */
    protected int pageIndex;
    /**
     * 分页每页条数
     */
    protected int pageSize;

    public AbstractPageableRequest(int pageIndex, int pageSize) {
        Assert.isGtValue(pageIndex, 1, ExceptionCode.PAGE_INDEX_FAILED);
        Assert.isGtValue(pageSize, 1, ExceptionCode.PAGE_SIZE_FAILED);
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        Page page = DefaultPage.instance(this);
        PageLocalContext.startPaging(page);
    }

    @Override
    public int getPageIndex() {
        return this.pageIndex;
    }

    @Override
    public int getPageSize() {
        return this.pageSize;
    }

    @Override
    public long getOffset() {
        return (long) (this.pageIndex - 1) * (long) this.pageSize;
    }

    @Override
    public long getEndRow() {
        return this.pageIndex * this.pageSize;
    }
}
