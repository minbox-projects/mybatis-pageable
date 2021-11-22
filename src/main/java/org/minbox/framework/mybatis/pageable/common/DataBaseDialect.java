package org.minbox.framework.mybatis.pageable.common;

import lombok.Getter;
import org.minbox.framework.mybatis.pageable.dialect.Dialect;
import org.minbox.framework.mybatis.pageable.dialect.support.*;

/**
 * 数据库方言枚举
 *
 * @author 恒宇少年
 */
@Getter
public enum DataBaseDialect {
    MYSQL(MySqlDialect.class),
    DB2(Db2Dialect.class),
    HSQL(HSqlDialect.class),
    ORACLE(OracleDialect.class),
    POSTGRES(PostgresDialect.class),
    SQLSERVER(SqlServerDialect.class),
    INFORMIX(InFoxMixDialect.class);
    private Class<? extends Dialect> value;

    DataBaseDialect(Class<? extends Dialect> value) {
        this.value = value;
    }
}
