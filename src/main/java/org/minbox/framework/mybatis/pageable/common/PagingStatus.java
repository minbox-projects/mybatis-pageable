package org.minbox.framework.mybatis.pageable.common;

/**
 * 分页状态
 *
 * @author 恒宇少年
 */
public enum PagingStatus {
    /**
     * 已开启分页
     */
    STARTED,
    /**
     * 跳过本次分页
     */
    SKIPPING
}
