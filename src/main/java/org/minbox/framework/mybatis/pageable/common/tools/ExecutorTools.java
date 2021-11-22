package org.minbox.framework.mybatis.pageable.common.tools;

import org.apache.ibatis.executor.Executor;
import org.minbox.framework.mybatis.pageable.common.executor.ExecutorQueryRequest;

import java.sql.SQLException;
import java.util.List;

/**
 * {@link Executor}工具类
 *
 * @author 恒宇少年
 */
public final class ExecutorTools {
    /**
     * 执行查询
     *
     * @param executor 执行器
     * @param request  执行查询请求对象
     * @param <E>      泛型类型
     * @return 查询后的数据列表
     * @throws SQLException 数据库异常
     */
    public static <E> List<E> query(Executor executor, ExecutorQueryRequest request) throws SQLException {
        return executor.query(request.getStatement(),
                request.getParameter(),
                request.getRowBounds(),
                request.getResultHandler(),
                request.getCacheKey(),
                request.getBoundSql());
    }
}
