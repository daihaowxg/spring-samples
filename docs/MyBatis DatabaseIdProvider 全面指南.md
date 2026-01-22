# MyBatis DatabaseIdProvider 全面指南

本文档总结了关于 MyBatis `databaseIdProvider` 的核心概念、配置方式及最佳实践。

## 1. 核心概念

`DatabaseIdProvider` 是 MyBatis 提供的一种机制，用于支持多数据库厂商（Multi-Vendor）的 SQL 适配。

*   **作用**：根据当前连接的数据库类型（Product Name），自动加载 Mapper XML 中对应的 SQL 语句。
*   **原理**：MyBatis 从 JDBC `Connection` 中获取 `DatabaseMetaData.getDatabaseProductName()`，将其与配置的属性进行匹配（Alias），得到一个 `databaseId` 标识。
*   **应用场景**：当一套代码需要同时支持 MySQL、Oracle、PostgreSQL/Kingbase 等多种数据库，且 SQL 语法存在差异时。

## 2. 配置方式

配置 `DatabaseIdProvider` 主要有三种方式，推荐优先级：**Spring Boot 配置 > Java Bean 配置 > XML 配置**。

### 2.1 方式一：Spring Boot 配置文件 (最简推荐)

利用 `mybatis-spring-boot-starter` 的自动配置能力，直接在 `application.yml` 或 `application.properties` 中定义映射。

```yaml
mybatis:
  configuration:
    database-id-provider:
      property:
        MySQL: mysql
        Oracle: oracle
        "PostgreSQL": postgresql
        "KingbaseES": kingbase
        SQL Server: sqlserver
```

*   **Key (左侧)**: 数据库厂商名称（`DatabaseMetaData.getDatabaseProductName()` 返回的字符串）。
*   **Value (右侧)**: 自定义的 `databaseId` 别名（在 Mapper XML 中使用的标识）。

### 2.2 方式二：Java Bean 编程式配置

如果需要更复杂的控制，或者不使用 Spring Boot 的自动配置，可以手动注册 `VendorDatabaseIdProvider` Bean。

```java
@Configuration
public class MyBatisConfig {

    @Bean
    public DatabaseIdProvider databaseIdProvider() {
        VendorDatabaseIdProvider databaseIdProvider = new VendorDatabaseIdProvider();
        Properties properties = new Properties();
        properties.setProperty("MySQL", "mysql");
        properties.setProperty("Oracle", "oracle");
        properties.setProperty("PostgreSQL", "postgresql");
        properties.setProperty("KingbaseES", "kingbase"); // 国产金仓
        databaseIdProvider.setProperties(properties);
        return databaseIdProvider;
    }
}
```

**注意**：在手动配置 `SqlSessionFactory` 时，必须显式将此 Provider 注入到 Factory 中：

```java
// 伪代码示例
sqlSessionFactoryBean.setDatabaseIdProvider(databaseIdProvider);
```

### 2.3 方式三：XML 配置 (mybatis-config.xml)

传统 Spring 或原生 MyBatis 项目使用。

```xml
<configuration>
    <databaseIdProvider type="DB_VENDOR">
        <property name="MySQL" value="mysql"/>
        <property name="Oracle" value="oracle"/>
        <property name="PostgreSQL" value="postgresql"/>
    </databaseIdProvider>
</configuration>
```

## 3. 使用方式 (Mapper XML)

在 Mapper XML 文件中，使用 `databaseId` 属性来区分不同数据库的 SQL 实现。

### 3.1 基础用法

```xml
<!-- 默认通用 SQL (当没有匹配到 specific databaseId 时使用) -->
<select id="selectTime" resultType="string">
    SELECT NOW()
</select>

<!-- MySQL 专用实现 -->
<select id="selectTime" resultType="string" databaseId="mysql">
    SELECT NOW()
</select>

<!-- Oracle 专用实现 -->
<select id="selectTime" resultType="string" databaseId="oracle">
    SELECT SYSDATE FROM DUAL
</select>
```

### 3.2 动态 SQL 中的用法

也可以在 `<if>` 或 `<choose>` 标签中使用 `_databaseId` 内置变量。

```xml
<select id="selectUser" resultType="User">
    SELECT * FROM users
    <where>
        <if test="_databaseId == 'mysql'">
            AND name LIKE CONCAT('%', #{name}, '%')
        </if>
        <if test="_databaseId == 'oracle'">
            AND name LIKE '%' || #{name} || '%'
        </if>
    </where>
</select>
```

## 4. 常见问题与最佳实践

### 4.1 厂商名称匹配规则
*   `VendorDatabaseIdProvider` 默认只截取 Product Name 的第一部分（例如 "KingbaseES V8" 可能匹配不到 "KingbaseES"）。
*   **解决方法**：MyBatis 实际上是做**包含匹配**（`name.contains(key)`），所以配置 "Kingbase" 通常能匹配 "KingbaseES V8"。

### 4.2 标识符规范
建议统一使用**小写**作为 `databaseId`（如 `mysql`, `oracle`, `postgresql`），保持风格统一，避免大小写敏感问题带来的困扰。

### 4.3 验证是否生效
可以通过以下日志确认 Provider 是否正确加载：
```text
DEBUG [main] - Logging initialized using 'class org.apache.ibatis.logging.stdout.StdOutImplAdapter' for type 'org.apache.ibatis.logging.stdout.StdOutImpl'
...
DEBUG [main] - Registered plugin: '...'
```
或者在启动时打印：
```java
@Autowired
SqlSessionFactory sqlSessionFactory;

@PostConstruct
public void check() {
    System.out.println("DatabaseId: " + sqlSessionFactory.getConfiguration().getDatabaseId());
}
```

### 4.4 国产化数据库适配 (如人大金仓 Kingbase)
Kingbase 底层通常基于 PostgreSQL，JDBC 驱动有时会返回 "PostgreSQL" 作为厂商名，有时返回 "KingbaseES"。
建议同时配置：
```properties
PostgreSQL=postgresql
KingbaseES=postgresql  # 将金仓也映射为 PG 语法（如果兼容的话）
# 或者
KingbaseES=kingbase    # 如果有专用语法
```
