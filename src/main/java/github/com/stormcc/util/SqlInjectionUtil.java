package github.com.stormcc.util;

import org.apache.commons.text.StringEscapeUtils;

import java.util.regex.Pattern;

/**
 * Create By: Jimmy Song
 * Create At: 2025-04-14 11:00
 */
public final class SqlInjectionUtil {
    // 定义危险关键字的正则表达式
    private static final Pattern DANGEROUS_KEYWORDS = Pattern.compile(
            "(?i)" + // 忽略大小写
                    "\\b(SELECT|INSERT|UPDATE|DELETE|DROP|ALTER|TRUNCATE|SLEEP|UNION|EXEC|EXECUTE|DECLARE|CAST|CONVERT|CHR|CHAR|ASCII|ORD|HEX|BIN|LIKE|IN|BETWEEN|ISNULL|IFNULL|CASE|WHEN|THEN|ELSE|END|JOIN|INNER|OUTER|LEFT|RIGHT|FULL|CROSS|GROUP|BY|HAVING|ORDER|LIMIT|OFFSET|DISTINCT|EXISTS|NOT|AND|OR|XOR|LIKE|REGEXP|RLIKE|IN|ALL|ANY|SOME|EXISTS|INTERSECT|MINUS|EXCEPT|NATURAL|STRAIGHT_JOIN|PROCEDURE|FUNCTION|TRIGGER|VIEW|INDEX|TABLE|DATABASE|SCHEMA|USER|PASSWORD|GRANT|REVOKE|PRIVILEGES|ROLE|SESSION|SYSTEM|GLOBAL|LOCAL|TRANSACTION|COMMIT|ROLLBACK|SAVEPOINT|LOCK|UNLOCK|FLUSH|RESET|PURGE|ANALYZE|OPTIMIZE|CHECK|REPAIR|BACKUP|RESTORE|SHUTDOWN|START|STOP|KILL|SHOW|DESCRIBE|EXPLAIN|HELP|USE|SET|GET|CALL|DO|HANDLER|LOAD|IMPORT|EXPORT|RENAME|REPLACE|MERGE|UPSERT|VALUES|DEFAULT|AUTO_INCREMENT|SERIAL|IDENTITY|SEQUENCE|CURSOR|FETCH|NEXT|PRIOR|FIRST|LAST|ABSOLUTE|RELATIVE|FORWARD|BACKWARD|CURRENT|ROW|ROWS|ONLY|OF|KEY|PRIMARY|FOREIGN|UNIQUE|INDEX|REFERENCES|MATCH|SIMPLE|PARTIAL|FULLTEXT|SPATIAL|HASH|BTREE|RTREE|GIN|GIST|BRIN|ARRAY|JSON|XML|BLOB|CLOB|TEXT|ENUM|SET|BIT|TINYINT|SMALLINT|MEDIUMINT|INT|INTEGER|BIGINT|FLOAT|DOUBLE|DECIMAL|NUMERIC|REAL|BOOLEAN|DATE|TIME|DATETIME|TIMESTAMP|YEAR|INTERVAL|ZONE|WITH|AS|ON|OFF|CASCADE|RESTRICT|NO|ACTION|DEFERRABLE|INITIALLY|IMMEDIATE|DEFERRED|CONSTRAINT|CHECK|ASSERTION|DOMAIN|COLLATION|CHARACTER|SET|COLLATE|ENCODING|OWNER|TABLESPACE|UNLOGGED|TEMP|TEMPORARY|UNION|ALL|DISTINCT|INTERSECT|EXCEPT|MINUS|NATURAL|STRAIGHT_JOIN|PROCEDURE|FUNCTION|TRIGGER|VIEW|INDEX|TABLE|DATABASE|SCHEMA|USER|PASSWORD|GRANT|REVOKE|PRIVILEGES|ROLE|SESSION|SYSTEM|GLOBAL|LOCAL|TRANSACTION|COMMIT|ROLLBACK|SAVEPOINT|LOCK|UNLOCK|FLUSH|RESET|PURGE|ANALYZE|OPTIMIZE|CHECK|REPAIR|BACKUP|RESTORE|SHUTDOWN|START|STOP|KILL|SHOW|DESCRIBE|EXPLAIN|HELP|USE|SET|GET|CALL|DO|HANDLER|LOAD|IMPORT|EXPORT|RENAME|REPLACE|MERGE|UPSERT|VALUES|DEFAULT|AUTO_INCREMENT|SERIAL|IDENTITY|SEQUENCE|CURSOR|FETCH|NEXT|PRIOR|FIRST|LAST|ABSOLUTE|RELATIVE|FORWARD|BACKWARD|CURRENT|ROW|ROWS|ONLY|OF|KEY|PRIMARY|FOREIGN|UNIQUE|INDEX|REFERENCES|MATCH|SIMPLE|PARTIAL|FULLTEXT|SPATIAL|HASH|BTREE|RTREE|GIN|GIST|BRIN|ARRAY|JSON|XML|BLOB|CLOB|TEXT|ENUM|SET|BIT|TINYINT|SMALLINT|MEDIUMINT|INT|INTEGER|BIGINT|FLOAT|DOUBLE|DECIMAL|NUMERIC|REAL|BOOLEAN|DATE|TIME|DATETIME|TIMESTAMP|YEAR|INTERVAL|ZONE|WITH|AS|ON|OFF|CASCADE|RESTRICT|NO|ACTION|DEFERRABLE|INITIALLY|IMMEDIATE|DEFERRED|CONSTRAINT|CHECK|ASSERTION|DOMAIN|COLLATION|CHARACTER|SET|COLLATE|ENCODING|OWNER|TABLESPACE|UNLOGGED|TEMP|TEMPORARY|UNION|ALL|DISTINCT|INTERSECT|EXCEPT|MINUS|NATURAL|STRAIGHT_JOIN|PROCEDURE|FUNCTION|TRIGGER|VIEW|INDEX|TABLE|DATABASE|SCHEMA|USER|PASSWORD|GRANT|REVOKE|PRIVILEGES|ROLE|SESSION|SYSTEM|GLOBAL|LOCAL|TRANSACTION|COMMIT|ROLLBACK|SAVEPOINT|LOCK|UNLOCK|FLUSH|RESET|PURGE|ANALYZE|OPTIMIZE|CHECK|REPAIR|BACKUP|RESTORE|SHUTDOWN|START|STOP|KILL|SHOW|DESCRIBE|EXPLAIN|HELP|USE|SET|GET|CALL|DO|HANDLER|LOAD|IMPORT|EXPORT|RENAME|REPLACE|MERGE|UPSERT|VALUES|DEFAULT|AUTO_INCREMENT|SERIAL|IDENTITY|SEQUENCE|CURSOR|FETCH|NEXT|PRIOR|FIRST|LAST|ABSOLUTE|RELATIVE|FORWARD|BACKWARD|CURRENT|ROW|ROWS|ONLY|OF|KEY|PRIMARY|FOREIGN|UNIQUE|INDEX|REFERENCES|MATCH|SIMPLE|PARTIAL|FULLTEXT|SPATIAL|HASH|BTREE|RTREE|GIN|GIST|BRIN|ARRAY|JSON|XML|BLOB|CLOB|TEXT|ENUM|SET|BIT|TINYINT|SMALLINT|MEDIUMINT|INT|INTEGER|BIGINT|FLOAT|DOUBLE|DECIMAL|NUMERIC|REAL|BOOLEAN|DATE|TIME|DATETIME|TIMESTAMP|YEAR|INTERVAL|ZONE|WITH|AS|ON|OFF|CASCADE|RESTRICT|NO|ACTION|DEFERRABLE|INITIALLY|IMMEDIATE|DEFERRED|CONSTRAINT|CHECK|ASSERTION|DOMAIN|COLLATION|CHARACTER|SET|COLLATE|ENCODING|OWNER|TABLESPACE|UNLOGGED|TEMP|TEMPORARY)\\b"
    );

    public static boolean containsDangerousKeywords(String input) {
        return DANGEROUS_KEYWORDS.matcher(sanitizeInput(input)).find();
    }

    public static String sanitizeInput(String input) {
        return StringEscapeUtils.escapeJava(input); // 转义特殊字符
    }
}
