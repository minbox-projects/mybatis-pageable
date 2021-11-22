package org.minbox.framework.mybatis.pageable;

import java.util.List;

/**
 * 分页数据的接口定义
 *
 * @author 恒宇少年
 */
public interface Page<T> {
    /**
     * 获取指定泛型的分页后的数据列表
     *
     * @return 分页数据列表
     */
    List<T> getData();

    /**
     * 获取计算后的总页数
     *
     * @return 总页数
     */
    long getTotalPages();

    /**
     * 获取没分页的数据总条数
     *
     * @return 总条数
     */
    long getTotalElements();

    /**
     * 获取当前分页的页码
     *
     * @return 当前页码
     */
    int getPageIndex();

    /**
     * 获取每页的数据条数
     *
     * @return 数据条数
     */
    int getPageSize();

    /**
     * 当前页开始位置
     *
     * @return 开始位置
     */
    long getOffset();

    /**
     * 当前页的结束位置
     *
     * @return 结束位置
     */
    long getEndRow();

    /**
     * 是否存在下一页
     *
     * @return 是否有下一页，true：存在，false：不存在
     */
    boolean hasNext();

    /**
     * 是否存在上一页
     *
     * @return 是否有上一页，true：存在，false：不存在
     */
    boolean hasPrevious();

    /**
     * 是否为首页
     *
     * @return 是否为首页，true：首页，false：非首页
     */
    boolean isFirst();

    /**
     * 是否为末页
     *
     * @return 是否为末页，true：末页，false：非末页
     */
    boolean isLast();
}
