package org.minbox.framework.mybatis.pageable.interceptor;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.scripting.xmltags.DynamicContext;
import org.apache.ibatis.scripting.xmltags.DynamicSqlSource;
import org.apache.ibatis.scripting.xmltags.SqlNode;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.minbox.framework.mybatis.pageable.DefaultPage;
import org.minbox.framework.mybatis.pageable.Page;
import org.minbox.framework.mybatis.pageable.common.executor.ExecutorQueryRequest;
import org.minbox.framework.mybatis.pageable.common.tools.ExecutorTools;
import org.minbox.framework.mybatis.pageable.common.tools.MappedStatementTools;
import org.minbox.framework.mybatis.pageable.common.tools.ReflectionTools;
import org.minbox.framework.mybatis.pageable.dialect.Dialect;
import org.minbox.framework.mybatis.pageable.dialect.DialectDynamicFactory;
import org.minbox.framework.mybatis.pageable.request.PageLocalContext;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author 恒宇少年
 */
@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class})
})
public class MyBatisExecutePageableInterceptor implements Interceptor {
    /**
     * 项目配置数据库方言的类名全限定名
     */
    private String dialect;
    /**
     * 缓存数据库方言对象到内存
     */
    private Dialect dialectCache;

    /**
     * 拦截器方法
     *
     * @param invocation 拦截器拦截方法封装对象
     * @return 拦截的目标方法执行后的返回值
     * @throws Throwable 执行异常
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        if (!PageLocalContext.isStarting()) {
            return invocation.proceed();
        }

        // 获取Executor对象query方法的参数列表
        final Object[] args = invocation.getArgs();
        // MappedStatement对象实例
        MappedStatement statement = (MappedStatement) args[0];

        // 本次拦截的执行器对象
        Executor executor = (Executor) invocation.getTarget();

        // 获取threadLocal内保存的分页响应对象
        DefaultPage pageable = (DefaultPage) PageLocalContext.getPageInstance();

        /*
         * 缓存对象优先
         * 如果每次生成存在性能瓶颈
         * 数据库方言
         * 根据不同的数据库方言生成分页sql、查询总数量sql、参数映射、参数重装等操作
         */
        if (dialectCache == null) {
            dialectCache = DialectDynamicFactory.newInstance(statement, this.dialect);
        }

        // 构建请求对象
        ExecutorQueryRequest queryRequest = buildExecutorQueryRequest(executor, args);

        // 执行分页查询 & 设置到分页响应对象
        pageable.setData(executeQuery(executor, queryRequest, pageable, dialectCache));
        // 查询总数量 & 设置到分页响应对象
        pageable.setTotalElements(executeCount(executor, queryRequest, dialectCache));

        // 分页查询后的列表数据
        return pageable.getData();

    }

    /**
     * 构建执行器查询时的请求对象
     *
     * @param executor 拦截的执行对象实例
     * @param args     本次拦截Executor.query方法的参数数组
     * @return 执行器查询请求对象
     */
    private ExecutorQueryRequest buildExecutorQueryRequest(Executor executor, Object[] args) {
        MappedStatement statement = (MappedStatement) args[0];
        Object parameter = args[1];
        RowBounds rowBounds = RowBounds.DEFAULT;
        ResultHandler resultHandler = (ResultHandler) args[3];
        CacheKey cacheKey;
        BoundSql boundSql;
        if (args.length == 4) {
            boundSql = statement.getBoundSql(parameter);
            cacheKey = executor.createCacheKey(statement, parameter, rowBounds, boundSql);
        } else {
            cacheKey = (CacheKey) args[4];
            // 使用传递的BoundSQL
            boundSql = (BoundSql) args[5];
        }
        return new ExecutorQueryRequest()
                .setStatement(statement)
                .setParameter(parameter)
                .setRowBounds(rowBounds)
                .setResultHandler(resultHandler)
                .setBoundSql(boundSql)
                .setCacheKey(cacheKey);
    }

    /**
     * 获取总数量
     *
     * @param executor 执行器
     * @param request  执行器所需的请求对象
     * @param dialect  数据库方言
     * @return 查询总数量
     * @throws SQLException 数据库异常
     */
    private Long executeCount(Executor executor, ExecutorQueryRequest request, Dialect dialect) throws SQLException {
        /*
         *  获取查询总数量的MappedStatement实例
         *  会先从缓存内获取，缓存不存在时创建后写入缓存并返回
         */
        MappedStatement statement = MappedStatementTools.initOrGetCountStatement(request.getStatement());

        // 原始查询的boundSql
        BoundSql boundSql = request.getBoundSql();

        // 根据不同数据库方言获取统计sql
        String countSql = dialect.getCountSql(boundSql);
        Object parameter = request.getParameter();

        // 创建查询总数量的boundSql对象实例
        BoundSql countBoundSql = new BoundSql(statement.getConfiguration(), countSql, boundSql.getParameterMappings(), parameter);
        setAdditionalParameter(statement, request.getParameter(), countBoundSql);

        // 创建缓存key
        CacheKey countKey = executor.createCacheKey(statement, parameter, RowBounds.DEFAULT, countBoundSql);

        /*
         * 为了不影响原始的ExecutorQueryRequest对象的值
         * 所以重新构建分页查询数据列表的执行请求对象
         */
        ExecutorQueryRequest countQueryRequest = new ExecutorQueryRequest()
                .setBoundSql(countBoundSql)
                .setStatement(statement)
                .setRowBounds(request.getRowBounds())
                .setCacheKey(countKey)
                .setParameter(parameter)
                .setResultHandler(request.getResultHandler());

        // 执行查询count
        Object countResultList = ExecutorTools.query(executor, countQueryRequest);
        return ((Number) ((List) countResultList).get(0)).longValue();
    }

    /**
     * 获取分页后的数据
     *
     * @param executor 执行器
     * @param request  执行器所需的请求对象
     * @param pageable 分页相应对象
     * @param dialect  数据库方言
     * @return 数据列表
     * @throws SQLException 数据库异常
     */
    private List executeQuery(Executor executor, ExecutorQueryRequest request, Page pageable, Dialect dialect) throws SQLException {
        /*
         * 获取执行分页查询时的参数
         */
        MappedStatement statement = request.getStatement();
        BoundSql boundSql = request.getBoundSql();

        // 根据不同的数据库方言获取分页sql
        String dataSql = dialect.getPageSql(request.getBoundSql(), pageable);
        /*
         * 根据不同数据库方言获取分页所需的参数集合
         * 处理后的参数包含未处理之前的参数列表
         */
        Object pageParameter = dialect.getPageParameter(request.getParameter(), pageable);

        // 实例化分页的绑定sql对象
        BoundSql pageBoundSql = new BoundSql(statement.getConfiguration(), dataSql, boundSql.getParameterMappings(), pageParameter);
        setAdditionalParameter(statement, request.getParameter(), pageBoundSql);

        /*
         * 根据不同的数据库方言设置不同的参数映射
         * 处理后包含未处理之前的参数映射列表
         */
        dialect.addPageMapping(pageBoundSql, statement, pageable);
        /*
         * 创建缓存key
         * 并且将缓存key设置到本次执行对象内
         */
        CacheKey dataKey = executor.createCacheKey(statement, pageParameter, request.getRowBounds(), pageBoundSql);

        /*
         * 为了不影响原始的ExecutorQueryRequest对象的值
         * 所以重新构建分页查询数据列表的执行请求对象
         */
        ExecutorQueryRequest pageQueryRequest = new ExecutorQueryRequest()
                .setBoundSql(pageBoundSql)
                .setStatement(statement)
                .setRowBounds(request.getRowBounds())
                .setCacheKey(dataKey)
                .setParameter(pageParameter)
                .setResultHandler(request.getResultHandler());
        // 执行查询
        return ExecutorTools.query(executor, pageQueryRequest);
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        this.dialect = properties.getProperty("dialect");
    }

    /**
     * 设置附加参数
     *
     * @param statement       statement对象
     * @param parameterObject 参数对象
     * @param boundSql        绑定sql对象
     */
    private void setAdditionalParameter(MappedStatement statement, Object parameterObject, BoundSql boundSql) {
        try {
            SqlSource sqlSource = statement.getSqlSource();
            if (sqlSource instanceof DynamicSqlSource) { // 动态Sql
                DynamicContext context = new DynamicContext(statement.getConfiguration(), parameterObject);
                Field rootSqlNodeField = ReflectionTools.getField(DynamicSqlSource.class, "rootSqlNode");
                if (!rootSqlNodeField.isAccessible()) {
                    rootSqlNodeField.setAccessible(true);
                }
                SqlNode rootSqlNode = (SqlNode) rootSqlNodeField.get(sqlSource);
                rootSqlNode.apply(context);
                Map<String, Object> bindings = context.getBindings();
                bindings.forEach(boundSql::setAdditionalParameter);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("设置附加参数异常");
        }
    }
}
