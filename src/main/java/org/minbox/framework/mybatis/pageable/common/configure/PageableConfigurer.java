package org.minbox.framework.mybatis.pageable.common.configure;

import org.apache.ibatis.plugin.Interceptor;

import java.util.List;

/**
 * 分页查询配置接口定义
 *
 * @author 恒宇少年
 */
public interface PageableConfigurer {
    /**
     * 获取分页插件执行之前需要执行的插件列表
     *
     * @return {@link Interceptor}
     */
    List<Interceptor> getPrePlugins();

    /**
     * 获取分页插件执行后需要执行的插件列表
     *
     * @return {@link Interceptor}
     */
    List<Interceptor> getPostPlugins();
}
