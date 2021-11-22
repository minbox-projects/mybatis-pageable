package org.minbox.framework.mybatis.pageable.dialect.support;

import org.apache.ibatis.mapping.BoundSql;
import org.minbox.framework.mybatis.pageable.Page;
import org.minbox.framework.mybatis.pageable.dialect.AbstractDialect;
import org.minbox.framework.mybatis.pageable.dialect.PageParameterSortMapping;

import java.util.Arrays;
import java.util.List;

/**
 * SQLServer数据库方言分页实现类
 *
 * @author 恒宇少年
 */
public class SqlServerDialect extends AbstractDialect {
    private static final String SQL_SERVER_PAGEABLE_SQL_FORMAT = "%s OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

    @Override
    public List<PageParameterSortMapping> getSortParameterMapping() {
        return Arrays.asList(
                new PageParameterSortMapping().setParameterName(PARAM_PAGE_OFFSET).setTypeClass(Long.class),
                new PageParameterSortMapping().setParameterName(PARAM_PAGE_SIZE).setTypeClass(Integer.class)
        );
    }

    @Override
    public String getPageSql(BoundSql boundSql, Page page) {
        return String.format(SQL_SERVER_PAGEABLE_SQL_FORMAT, boundSql.getSql());
    }
}
