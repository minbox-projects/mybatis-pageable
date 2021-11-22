package org.minbox.framework.mybatis.pageable.dialect.support;

import org.apache.ibatis.mapping.BoundSql;
import org.minbox.framework.mybatis.pageable.Page;
import org.minbox.framework.mybatis.pageable.dialect.AbstractDialect;
import org.minbox.framework.mybatis.pageable.dialect.PageParameterSortMapping;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * DB2数据库方言分页实现类
 *
 * @author 恒宇少年
 */
public class Db2Dialect extends AbstractDialect {
    /**
     * DB2数据库分页SQL语句
     */
    private static final String DB2_PAGEABLE_SQL_FORMAT =
            "SELECT * FROM (SELECT PAGEABLE_.*,ROWNUMBER() OVER() AS ROW_ID FROM ( " +
                    "%s ) AS PAGEABLE_) PAGEABLE_ WHERE ROW_ID BETWEEN ? AND ?";

    @Override
    public List<PageParameterSortMapping> getSortParameterMapping() {
        return Arrays.asList(
                new PageParameterSortMapping().setParameterName(PARAM_PAGE_OFFSET).setTypeClass(Long.class),
                new PageParameterSortMapping().setParameterName(PARAM_PAGE_SIZE).setTypeClass(Integer.class)
        );
    }

    @Override
    public Object getPageParameter(Object parameter, Page page) {
        Map pageParameter = (Map) super.getPageParameter(parameter, page);
        pageParameter.put(PARAM_PAGE_OFFSET, page.getOffset() + 1);
        return pageParameter;
    }

    @Override
    public String getPageSql(BoundSql boundSql, Page page) {
        return String.format(DB2_PAGEABLE_SQL_FORMAT, boundSql.getSql());
    }
}
