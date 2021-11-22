package org.minbox.framework.mybatis.pageable.dialect.support;

import org.minbox.framework.mybatis.pageable.dialect.AbstractDialect;
import org.minbox.framework.mybatis.pageable.dialect.PageParameterSortMapping;

import java.util.Arrays;
import java.util.List;

/**
 * MySQL数据库方言分页实现类
 *
 * @author 恒宇少年
 */
public class MySqlDialect extends AbstractDialect {
    @Override
    public List<PageParameterSortMapping> getSortParameterMapping() {
        return Arrays.asList(
                new PageParameterSortMapping().setParameterName(PARAM_PAGE_SIZE).setTypeClass(Integer.class),
                new PageParameterSortMapping().setParameterName(PARAM_PAGE_OFFSET).setTypeClass(Long.class)
        );
    }
}
