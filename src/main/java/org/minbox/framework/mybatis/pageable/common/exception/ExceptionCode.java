package org.minbox.framework.mybatis.pageable.common.exception;

import lombok.Getter;

/**
 * 异常码定义枚举
 *
 * @author 恒宇少年
 */
@Getter
public enum ExceptionCode {
    PAGE_INDEX_FAILED("分页参数：当前页码参数传递错误，请传递大于0的正整数."),
    PAGE_SIZE_FAILED("分页参数：每页条数参数传递错误，请传递大于0的正整数."),
    DIALECT_NOT_FOUND("数据库方言暂未支持.");
    private String message;

    ExceptionCode(String message) {
        this.message = message;
    }
}
