# Spring JDBC、Spring Data JDBC、MyBatis、JPA 之间的关系

## 概述

这四种技术都是 Java 生态中用于数据库访问的方案，但它们处于不同的抽象层级，解决的问题也不同。

## 抽象层级图

```
┌────────────────────────────────────────────────────────────────┐
│                        应用层                                   │
└───────────────────────────┬────────────────────────────────────┘
                            │
        ┌───────────────────┼───────────────────┐
        ▼                   ▼                   ▼
┌───────────────┐   ┌───────────────┐   ┌───────────────┐
│  Spring Data  │   │     JPA       │   │    MyBatis    │
│     JDBC      │   │  (Hibernate)  │   │               │
│   Repository  │   │  Repository   │   │    Mapper     │
└───────┬───────┘   └───────┬───────┘   └───────┬───────┘
        │                   │                   │
        │           ┌───────┴───────┐           │
        │           ▼               │           │
        │   ┌───────────────┐       │           │
        │   │  Hibernate    │       │           │
        │   │  (ORM 实现)    │       │           │
        │   └───────┬───────┘       │           │
        │           │               │           │
        ▼           ▼               │           ▼
┌─────────────────────────────┐     │   ┌───────────────┐
│       Spring JDBC           │     │   │ JDBC Driver   │
│     (JdbcTemplate)          │     │   │   Manager     │
└───────────────┬─────────────┘     │   └───────┬───────┘
                │                   │           │
                ▼                   ▼           ▼
┌────────────────────────────────────────────────────────────────┐
│                        JDBC API                                 │
│                  (java.sql.Connection)                          │
└────────────────────────────────────────────────────────────────┘
                            │
                            ▼
┌────────────────────────────────────────────────────────────────┐
│                       数据库驱动                                 │
│               (MySQL Connector, PostgreSQL Driver)              │
└────────────────────────────────────────────────────────────────┘
                            │
                            ▼
┌────────────────────────────────────────────────────────────────┐
│                         数据库                                   │
│                  (MySQL, PostgreSQL, Oracle)                    │
└────────────────────────────────────────────────────────────────┘
```

## 四种技术详解

### 1. Spring JDBC

**核心组件**：`JdbcTemplate`、`NamedParameterJdbcTemplate`

**特点**：
- 对原生 JDBC 的轻量封装
- 简化资源管理（连接获取/释放）
- 统一异常处理（转为 `DataAccessException`）
- 需手写完整 SQL

```java
// 典型使用
String sql = "SELECT * FROM users WHERE id = ?";
User user = jdbcTemplate.queryForObject(sql, rowMapper, id);
```

---

### 2. Spring Data JDBC

**核心组件**：`CrudRepository`、`JdbcAggregateTemplate`

**特点**：
- Spring Data 家族成员
- Repository 接口自动生成 CRUD
- 简单对象映射（无复杂 ORM 特性）
- **基于 Spring JDBC 构建**

```java
// 只需定义接口
public interface UserRepository extends CrudRepository<User, Long> {
    List<User> findByName(String name);
}
```

---

### 3. JPA（Java Persistence API）

**核心组件**：`EntityManager`、`Repository`（Spring Data JPA）

**特点**：
- Java EE 标准规范
- 完整 ORM 框架（Hibernate 是最常见实现）
- 实体状态管理、缓存、懒加载
- **不依赖 Spring JDBC，直接使用 JDBC**

```java
// 实体定义
@Entity
@Table(name = "users")
public class User {
    @Id @GeneratedValue
    private Long id;
    private String name;
}

// Repository
public interface UserRepository extends JpaRepository<User, Long> {}
```

---

### 4. MyBatis

**核心组件**：`SqlSession`、`Mapper`

**特点**：
- 半自动化 ORM（SQL 需手写）
- SQL 与 Java 代码分离（XML 或注解）
- 灵活的结果映射
- **不依赖 Spring JDBC，直接使用 JDBC**

```java
// Mapper 接口
@Mapper
public interface UserMapper {
    @Select("SELECT * FROM users WHERE id = #{id}")
    User findById(Long id);
}
```

---

## 核心对比

| 维度 | Spring JDBC | Spring Data JDBC | JPA (Hibernate) | MyBatis |
|------|-------------|------------------|-----------------|---------|
| **抽象层级** | 低 | 中 | 高 | 中 |
| **SQL 控制** | 完全手写 | 自动生成 + 可选手写 | JPQL/自动生成 | 完全手写 |
| **对象映射** | 手动 RowMapper | 自动（简单） | 自动（复杂） | 自动 + XML 配置 |
| **缓存** | 无 | 无 | 一/二级缓存 | 二级缓存 |
| **懒加载** | 无 | 无 | 支持 | 支持 |
| **事务管理** | Spring 管理 | Spring 管理 | JPA + Spring | Spring 管理 |
| **学习曲线** | 低 | 低 | 高 | 中 |

## 依赖关系

```
┌─────────────────────────────────────────────────────────────────┐
│                                                                 │
│   Spring Data JDBC ──────────→ Spring JDBC ──────→ JDBC API    │
│                                                                 │
│   Spring Data JPA ───────────→ Hibernate ─────────→ JDBC API   │
│                                                                 │
│   MyBatis ───────────────────────────────────────→ JDBC API    │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

| 框架 | 对 Spring JDBC 的依赖 |
|------|----------------------|
| Spring Data JDBC | ✅ 依赖 |
| JPA / Hibernate | ❌ 不依赖（直接使用 JDBC） |
| MyBatis | ❌ 不依赖（直接使用 JDBC） |

## 如何选择

```
项目类型？
    │
    ├── 需要完全控制 SQL
    │       │
    │       ├── 简单场景 → Spring JDBC (JdbcTemplate)
    │       │
    │       └── 复杂映射 + XML 管理 → MyBatis
    │
    └── 希望自动生成 CRUD
            │
            ├── 简单领域模型，无需缓存/懒加载 → Spring Data JDBC
            │
            └── 复杂关联，需要 ORM 完整特性 → JPA (Hibernate)
```

### 推荐场景

| 技术 | 适用场景 |
|------|---------|
| **Spring JDBC** | 遗留系统改造、存储过程调用、极简场景 |
| **Spring Data JDBC** | DDD 领域驱动设计、微服务、轻量 CRUD |
| **JPA** | 企业级应用、复杂实体关系、需要缓存优化 |
| **MyBatis** | 复杂查询、报表系统、DBA 主导的项目 |

## 可以混合使用吗？

**可以**，但需要注意：

1. **Spring JDBC + JPA**：可在同一项目中使用，适合用 JdbcTemplate 执行复杂 SQL
2. **Spring Data JDBC + Spring Data JPA**：不推荐混用，两者对实体管理方式不同
3. **MyBatis + JPA**：技术上可行，但会增加复杂度，通常选择其一

> [!WARNING]
> 混用多种持久化框架时，务必注意事务边界和连接管理，避免资源泄漏。

## Spring Boot Starter 对应关系

| 技术 | Starter |
|------|---------|
| Spring JDBC | `spring-boot-starter-jdbc` |
| Spring Data JDBC | `spring-boot-starter-data-jdbc` |
| JPA (Hibernate) | `spring-boot-starter-data-jpa` |
| MyBatis | `mybatis-spring-boot-starter`（第三方） |

## 总结

```
                    ┌─────────────────────────────────────┐
                    │          选择指南                    │
                    └─────────────────────────────────────┘

    ┌───────────────────────────────────────────────────────────┐
    │  SQL 控制力：MyBatis ≈ Spring JDBC > Spring Data JDBC > JPA │
    │  开发效率：JPA > Spring Data JDBC > MyBatis > Spring JDBC   │
    │  学习成本：Spring JDBC < Spring Data JDBC < MyBatis < JPA   │
    │  灵活性：  MyBatis > Spring JDBC > Spring Data JDBC > JPA   │
    └───────────────────────────────────────────────────────────┘
```

## 相关链接

- [Spring JDBC 官方文档](https://docs.spring.io/spring-framework/docs/current/reference/html/data-access.html#jdbc)
- [Spring Data JDBC 官方文档](https://docs.spring.io/spring-data/jdbc/docs/current/reference/html/)
- [Spring Data JPA 官方文档](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [MyBatis 官方文档](https://mybatis.org/mybatis-3/)
- [MyBatis-Spring-Boot-Starter](https://mybatis.org/spring-boot-starter/mybatis-spring-boot-autoconfigure/)
