# spring-boot-starter-jdbc 与 spring-boot-starter-data-jdbc 的区别

## 概述

这两个 Starter 都与 JDBC 数据库访问相关，但它们属于不同的抽象层级，提供的功能也不同。

## 快速对比

| 维度 | spring-boot-starter-jdbc | spring-boot-starter-data-jdbc |
|------|--------------------------|-------------------------------|
| **定位** | 底层 JDBC 操作 | Spring Data 风格的 ORM |
| **核心组件** | `JdbcTemplate` | `CrudRepository` / `JdbcAggregateTemplate` |
| **编程模型** | 手写 SQL | Repository 接口 + 约定方法 |
| **对象映射** | 手动处理 `ResultSet` | 自动实体映射 |
| **依赖关系** | 无 | 包含 starter-jdbc |

## spring-boot-starter-jdbc

### 定位

提供 **JdbcTemplate** 和基础的数据源自动配置，适合直接操作 SQL。

### 包含内容

- `spring-jdbc`（JdbcTemplate、NamedParameterJdbcTemplate）
- `HikariCP`（默认连接池）
- 数据源自动配置

### 典型使用

```java
@Repository
public class UserDao {

    private final JdbcTemplate jdbcTemplate;

    public UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public User findById(Long id) {
        String sql = "SELECT id, name, email FROM users WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
            new User(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("email")
            ), id);
    }

    public int save(User user) {
        String sql = "INSERT INTO users (name, email) VALUES (?, ?)";
        return jdbcTemplate.update(sql, user.getName(), user.getEmail());
    }
}
```

### 特点

- ✅ 完全控制 SQL 语句
- ✅ 轻量级，无额外抽象层
- ✅ 适合复杂查询、存储过程调用
- ❌ 需要手动编写 RowMapper
- ❌ CRUD 操作重复代码较多

---

## spring-boot-starter-data-jdbc

### 定位

**Spring Data JDBC** 是 Spring Data 家族成员，提供类似 JPA 的开发体验，但更加轻量。

### 包含内容

- 包含 `spring-boot-starter-jdbc` 的所有内容
- `spring-data-jdbc`（Repository 抽象）
- `spring-data-relational`（关系型数据库公共抽象）

### 典型使用

```java
// 实体类
@Table("users")
public class User {

    @Id
    private Long id;
    private String name;
    private String email;

    // getters, setters...
}

// Repository 接口 - 无需实现
public interface UserRepository extends CrudRepository<User, Long> {

    List<User> findByName(String name);

    @Query("SELECT * FROM users WHERE email = :email")
    Optional<User> findByEmail(@Param("email") String email);
}

// 使用
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUser(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }
}
```

### 特点

- ✅ Repository 接口自动生成 CRUD 方法
- ✅ 方法名自动转换为 SQL（`findByName` → `WHERE name = ?`）
- ✅ 无需编写 RowMapper，自动对象映射
- ✅ 比 JPA 更简单，无 Session/缓存/懒加载复杂性
- ❌ 不支持延迟加载
- ❌ 不支持脏检查，每次 `save()` 都是全量更新

---

## 依赖关系图

```
┌─────────────────────────────────────────┐
│   spring-boot-starter-data-jdbc         │
│                                         │
│   ┌─────────────────────────────────┐   │
│   │  spring-boot-starter-jdbc       │   │
│   │                                 │   │
│   │   ┌─────────────────────────┐   │   │
│   │   │  spring-jdbc            │   │   │
│   │   │  (JdbcTemplate)         │   │   │
│   │   └─────────────────────────┘   │   │
│   │   ┌─────────────────────────┐   │   │
│   │   │  HikariCP               │   │   │
│   │   └─────────────────────────┘   │   │
│   └─────────────────────────────────┘   │
│                                         │
│   ┌─────────────────────────────────┐   │
│   │  spring-data-jdbc               │   │
│   │  (CrudRepository, @Query...)    │   │
│   └─────────────────────────────────┘   │
└─────────────────────────────────────────┘
```

> [!IMPORTANT]
> `spring-boot-starter-data-jdbc` **已经包含** `spring-boot-starter-jdbc`，因此两者只需引入一个即可。

## 如何选择

```
需要 Repository 抽象？
        │
        ├── 是 → spring-boot-starter-data-jdbc
        │
        └── 否 → 需要直接使用 JdbcTemplate？
                        │
                        ├── 是 → spring-boot-starter-jdbc
                        │
                        └── 否 → 考虑 spring-boot-starter-data-jpa
```

### 推荐场景

| Starter | 适用场景 |
|---------|---------|
| `starter-jdbc` | 复杂 SQL、存储过程、遗留系统改造、精细控制 |
| `starter-data-jdbc` | 标准 CRUD 业务、快速开发、领域驱动设计（DDD） |

## 在本项目中的使用

当前 `pom.xml` 同时引入了两者：

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jdbc</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jdbc</artifactId>
</dependency>
```

> [!TIP]
> 由于 `starter-data-jdbc` 已包含 `starter-jdbc`，可以移除单独的 `starter-jdbc` 依赖以简化配置。

## 相关链接

- [Spring Boot JDBC 官方文档](https://docs.spring.io/spring-boot/docs/current/reference/html/data.html#data.sql.jdbc)
- [Spring Data JDBC 官方文档](https://docs.spring.io/spring-data/jdbc/docs/current/reference/html/)
- [Spring Data JDBC vs JPA](https://spring.io/blog/2018/09/17/introducing-spring-data-jdbc)
