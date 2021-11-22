package org.minbox.framework.mybatis.pageable.dialect;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.minbox.framework.mybatis.pageable.Page;

import java.util.List;

/**
 * 数据库方言接口定义
 * <p>
 * 不同数据库实现该接口
 *
 * @author 恒宇少年
 */
public interface Dialect {
    /**
     * 获取分页sql
     *
     * @param boundSql boundSql 对象
     * @param page     分页响应对象实例
     * @return 获取分页sql
     */
    String getPageSql(BoundSql boundSql, Page page);

    /**
     * 获取总条数sql
     *
     * @param boundSql boundSql 对象
     * @return 获取查询总数sql
     */
    String getCountSql(BoundSql boundSql);

    /**
     * 将分页参数映射添加到BoundSql内的parameterMappings参数集合内
     * 并将分页参数添加到Parameter集合内
     *
     * @param parameter 分页查询时请求参数集合
     * @param page      分页响应对象实例
     * @return 获取分页参数列表
     */
    Object getPageParameter(Object parameter, Page page);

    /**
     * 添加分页的参数映射
     *
     * @param pageBoundSql 分页的BoundSql实例
     * @param statement    MappedStatement对象实例
     * @param page         分页响应对象实例
     */
    void addPageMapping(BoundSql pageBoundSql, MappedStatement statement, Page page);

    /**
     * 获取分页参数排序映射列表
     * 用于分页参数映射
     *
     * @return 获取排序后的分页参数映射列表
     */
    List<PageParameterSortMapping> getSortParameterMapping();
}
