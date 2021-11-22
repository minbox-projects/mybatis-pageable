package org.minbox.framework.mybatis.pageable.common;

import org.minbox.framework.mybatis.pageable.common.exception.ExceptionCode;
import org.minbox.framework.mybatis.pageable.common.exception.PageableException;

/**
 * 判定工具类
 *
 * @author 恒宇少年
 */
public class Assert {
    public static void notNull(Object object, ExceptionCode code, String... params) {
        if (object == null) {
            throw new PageableException(code, params);
        }
    }

    public static void isGtValue(long value, long decisionValue, ExceptionCode code, String... params) {
        if (value < decisionValue) {
            throw new PageableException(code, params);
        }
    }
}
