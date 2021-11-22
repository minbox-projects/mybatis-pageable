package org.minbox.framework.mybatis.pageable.dialect.support;

import org.apache.ibatis.mapping.BoundSql;
import org.minbox.framework.mybatis.pageable.Page;
import org.minbox.framework.mybatis.pageable.dialect.AbstractDialect;
import org.minbox.framework.mybatis.pageable.dialect.PageParameterSortMapping;

import java.util.Arrays;
import java.util.List;

/**
 * Oracle数据方言分页实现类
 *
 * @author 恒宇少年
 */
public class OracleDialect extends AbstractDialect {
    private static final String ORACLE_PAGEABLE_SQL_FORMAT = "SELECT * FROM ( " +
            "SELECT PAGEABLE_.*,ROWNUM ROW_ID FROM ( %s )" +
            " PAGEABLE_ WHERE ROWNUM <= ?  ) WHERE ROW_ID > ? ";

    @Override
    public List<PageParameterSortMapping> getSortParameterMapping() {
        return Arrays.asList(
                new PageParameterSortMapping().setParameterName(PARAM_PAGE_END).setTypeClass(Long.class),
                new PageParameterSortMapping().setParameterName(PARAM_PAGE_OFFSET).setTypeClass(Long.class)
        );
    }

    @Override
    public String getPageSql(BoundSql boundSql, Page page) {
        return String.format(ORACLE_PAGEABLE_SQL_FORMAT, boundSql.getSql());
    }
}
