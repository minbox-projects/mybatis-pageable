package org.minbox.framework.mybatis.pageable.request;

/**
 * 自动分页指定的业务逻辑主体函数定义
 *
 * @author 恒宇少年
 */
@FunctionalInterface
public interface LogicFunction {
    void invoke();
}
