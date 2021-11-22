package org.minbox.framework.mybatis.pageable.common.tools;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

/**
 * 注解工具类
 *
 * @author 恒宇少年
 */
public class AnnotationTools {
    /**
     * 获取枚举字段的值
     *
     * @param object          需要提取注解对应字段的对象实例
     * @param annotationClass 注解类型
     * @return 枚举字段的值
     */
    public static Object getValue(Object object, Class<? extends Annotation> annotationClass) {
        try {
            List<Field> fields = ReflectionTools.getAllFields(object.getClass());
            if (null != fields) {
                for (Field field : fields) {
                    if (field.isAnnotationPresent(annotationClass)) {
                        field.setAccessible(true);
                        return field.get(object);
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 获取Int类型枚举字段的值
     *
     * @param object          需要提取注解对应字段的对象实例
     * @param annotationClass 注解类型
     * @return 枚举字段的值
     */
    public static Integer getIntValue(Object object, Class<? extends Annotation> annotationClass) {
        Object value = getValue(object, annotationClass);
        return null == value ? 0 : Integer.valueOf(String.valueOf(value));
    }
}
