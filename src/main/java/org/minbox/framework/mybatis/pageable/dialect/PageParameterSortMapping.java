package org.minbox.framework.mybatis.pageable.dialect;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 分页参数排序映射实体
 *
 * @author 恒宇少年
 */
@Data
@Accessors(chain = true)
public class PageParameterSortMapping {
    /**
     * 参数名称
     */
    private String parameterName;
    /**
     * 参数类型
     */
    private Class typeClass;
}
