package org.minbox.framework.mybatis.pageable.common.executor;

import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

/**
 * 执行分页查询的请求实体
 *
 * @author 恒宇少年
 */
@Data
@Accessors(chain = true)
public class ExecutorQueryRequest {
    private MappedStatement statement;
    private Object parameter;
    private RowBounds rowBounds;
    private ResultHandler resultHandler;
    private CacheKey cacheKey;
    private BoundSql boundSql;
}
