package org.minbox.framework.mybatis.pageable;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.minbox.framework.mybatis.pageable.request.Pageable;

import java.util.List;

/**
 * {@link Page}接口的默认实现
 *
 * @author 恒宇少年
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DefaultPage<T> implements Page<T> {
    private Pageable pageable;
    private List<T> data;
    private long totalElements;

    private DefaultPage(Pageable pageable) {
        this.pageable = pageable;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    @Override
    public List<T> getData() {
        return this.data;
    }

    @Override
    public long getTotalPages() {
        long totalPage = getTotalElements() / getPageSize();
        return getTotalElements() % getPageSize() == 0 ? totalPage : totalPage + 1;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    @Override
    public long getTotalElements() {
        return this.totalElements;
    }

    @Override
    public int getPageIndex() {
        return this.pageable.getPageIndex();
    }

    @Override
    public int getPageSize() {
        return this.pageable.getPageSize();
    }

    @Override
    public long getOffset() {
        return this.pageable.getOffset();
    }

    @Override
    public long getEndRow() {
        return this.pageable.getEndRow();
    }

    @Override
    public boolean hasNext() {
        return getTotalPages() > getPageIndex();
    }

    @Override
    public boolean hasPrevious() {
        return this.pageable.getPageIndex() > 1;
    }

    @Override
    public boolean isFirst() {
        return this.pageable.getPageIndex() == 1;
    }

    @Override
    public boolean isLast() {
        return getTotalPages() == getPageIndex();
    }

    /**
     * 实例化分页结果接口实例
     *
     * @param pageable 分页接口实例
     * @return {@link Page} 分页结果接口实现类实例
     */
    public static Page instance(Pageable pageable) {
        return new DefaultPage<>(pageable);
    }
}
