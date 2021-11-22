package org.minbox.framework.mybatis.pageable.common.exception;

/**
 * 自动分页运行时异常
 *
 * @author 恒宇少年
 */
public class PageableException extends RuntimeException {
    public PageableException(ExceptionCode code, String... params) {
        super(String.format(code.getMessage(), params));
    }
}
