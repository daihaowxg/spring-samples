# Spring Session 与 JDBC 结合使用

## 概述

**Spring Session JDBC** 使用关系型数据库存储 Session 数据。相比 Redis 方案，它的优势在于：

- 无需额外引入缓存中间件
- 可复用现有数据库基础设施
- Session 数据可直接通过 SQL 查询和分析

## 适用场景

| 场景 | 说明 |
|------|------|
| 中小型应用 | 并发量不高，Session 操作频率较低 |
| 数据合规要求 | Session 数据需要存储在关系型数据库中 |
| 简化架构 | 不希望引入 Redis 等额外组件 |

> [!NOTE]
> JDBC 方案的性能低于 Redis，高并发场景建议使用 Redis 存储。

## 快速开始

### 1. 添加依赖

```xml
<dependency>
    <groupId>org.springframework.session</groupId>
    <artifactId>spring-session-jdbc</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jdbc</artifactId>
</dependency>

<!-- 数据库驱动，以 MySQL 为例 -->
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <scope>runtime</scope>
</dependency>
```

### 2. 配置数据源

```yaml
# application.yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/your_database?useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: your_password
    driver-class-name: com.mysql.cj.jdbc.Driver

  session:
    store-type: jdbc           # 使用 JDBC 存储
    timeout: 30m               # Session 超时时间
    jdbc:
      initialize-schema: always  # 自动创建表结构
      table-name: SPRING_SESSION # 表名前缀（可选）
```

### 3. 启用 Spring Session JDBC

```java
@SpringBootApplication
@EnableJdbcHttpSession       // 启用 JDBC Session
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

> [!TIP]
> 使用 Spring Boot 时，只需配置 `spring.session.store-type=jdbc`，无需显式添加 `@EnableJdbcHttpSession` 注解。

## 数据库表结构

Spring Session JDBC 使用两张表存储 Session 数据：

### SPRING_SESSION（主表）

```sql
CREATE TABLE SPRING_SESSION (
    PRIMARY_ID CHAR(36) NOT NULL,
    SESSION_ID CHAR(36) NOT NULL,
    CREATION_TIME BIGINT NOT NULL,
    LAST_ACCESS_TIME BIGINT NOT NULL,
    MAX_INACTIVE_INTERVAL INT NOT NULL,
    EXPIRY_TIME BIGINT NOT NULL,
    PRINCIPAL_NAME VARCHAR(100),
    CONSTRAINT SPRING_SESSION_PK PRIMARY KEY (PRIMARY_ID)
);

CREATE UNIQUE INDEX SPRING_SESSION_IX1 ON SPRING_SESSION (SESSION_ID);
CREATE INDEX SPRING_SESSION_IX2 ON SPRING_SESSION (EXPIRY_TIME);
CREATE INDEX SPRING_SESSION_IX3 ON SPRING_SESSION (PRINCIPAL_NAME);
```

| 字段 | 说明 |
|------|------|
| `PRIMARY_ID` | 主键 |
| `SESSION_ID` | Session ID |
| `CREATION_TIME` | 创建时间（毫秒时间戳） |
| `LAST_ACCESS_TIME` | 最后访问时间 |
| `MAX_INACTIVE_INTERVAL` | 最大不活跃间隔（秒） |
| `EXPIRY_TIME` | 过期时间 |
| `PRINCIPAL_NAME` | 用户标识（通常是用户名） |

### SPRING_SESSION_ATTRIBUTES（属性表）

```sql
CREATE TABLE SPRING_SESSION_ATTRIBUTES (
    SESSION_PRIMARY_ID CHAR(36) NOT NULL,
    ATTRIBUTE_NAME VARCHAR(200) NOT NULL,
    ATTRIBUTE_BYTES BLOB NOT NULL,
    CONSTRAINT SPRING_SESSION_ATTRIBUTES_PK PRIMARY KEY (SESSION_PRIMARY_ID, ATTRIBUTE_NAME),
    CONSTRAINT SPRING_SESSION_ATTRIBUTES_FK FOREIGN KEY (SESSION_PRIMARY_ID)
        REFERENCES SPRING_SESSION(PRIMARY_ID) ON DELETE CASCADE
);
```

| 字段 | 说明 |
|------|------|
| `SESSION_PRIMARY_ID` | 关联 SPRING_SESSION 主键 |
| `ATTRIBUTE_NAME` | 属性名称 |
| `ATTRIBUTE_BYTES` | 序列化后的属性值（二进制） |

## 配置选项

### 表初始化策略

```yaml
spring:
  session:
    jdbc:
      initialize-schema: always  # 启动时总是执行建表脚本
```

| 值 | 说明 |
|----|------|
| `always` | 每次启动都执行建表脚本 |
| `embedded` | 仅嵌入式数据库时执行（默认） |
| `never` | 从不执行，需手动建表 |

### 自定义表名

```yaml
spring:
  session:
    jdbc:
      table-name: MY_SESSION  # 表名变为 MY_SESSION 和 MY_SESSION_ATTRIBUTES
```

### 清理过期 Session

Spring Session JDBC 默认每分钟清理一次过期 Session：

```yaml
spring:
  session:
    jdbc:
      cleanup-cron: "0 * * * * *"  # Cron 表达式，默认每分钟执行
```

## 完整示例

### 项目结构

```
src/main/java/
└── com/example/demo/
    ├── DemoApplication.java
    └── controller/
        └── SessionController.java
```

### DemoApplication.java

```java
package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
```

### SessionController.java

```java
package com.example.demo.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/session")
public class SessionController {

    @PostMapping("/set")
    public Map<String, Object> setAttribute(
            @RequestParam String key,
            @RequestParam String value,
            HttpSession session) {

        session.setAttribute(key, value);

        Map<String, Object> result = new HashMap<>();
        result.put("sessionId", session.getId());
        result.put("key", key);
        result.put("value", value);
        return result;
    }

    @GetMapping("/get")
    public Map<String, Object> getAttribute(
            @RequestParam String key,
            HttpSession session) {

        Object value = session.getAttribute(key);

        Map<String, Object> result = new HashMap<>();
        result.put("sessionId", session.getId());
        result.put("key", key);
        result.put("value", value);
        return result;
    }

    @DeleteMapping("/invalidate")
    public Map<String, Object> invalidateSession(HttpSession session) {
        String sessionId = session.getId();
        session.invalidate();

        Map<String, Object> result = new HashMap<>();
        result.put("message", "Session invalidated");
        result.put("oldSessionId", sessionId);
        return result;
    }
}
```

### application.yml

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/demo?useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: 123456

  session:
    store-type: jdbc
    timeout: 30m
    jdbc:
      initialize-schema: always
```

## 测试验证

启动应用后，测试 Session 功能：

```bash
# 设置 Session 属性
curl -X POST "http://localhost:8080/session/set?key=username&value=alice" \
  -c cookies.txt

# 获取 Session 属性
curl "http://localhost:8080/session/get?key=username" \
  -b cookies.txt

# 查看数据库中的 Session 数据
mysql> SELECT * FROM SPRING_SESSION;
mysql> SELECT * FROM SPRING_SESSION_ATTRIBUTES;
```

## 与 Redis 方案对比

| 维度 | JDBC | Redis |
|------|------|-------|
| **性能** | 较低（磁盘 IO） | 高（内存存储） |
| **并发能力** | 有限 | 高 |
| **依赖** | 关系型数据库 | Redis 服务 |
| **数据持久性** | 强 | 可配置（RDB/AOF） |
| **运维复杂度** | 低 | 中等 |
| **适用场景** | 中小型应用 | 高并发分布式系统 |

## 常见问题

### 1. 表不自动创建

确保配置了正确的初始化策略：

```yaml
spring.session.jdbc.initialize-schema: always
```

### 2. 序列化异常

Session 中存储的对象必须实现 `Serializable` 接口：

```java
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    // ...
}
```

### 3. 过期 Session 未清理

检查清理任务是否正常运行，可调整 Cron 表达式：

```yaml
spring.session.jdbc.cleanup-cron: "0 */5 * * * *"  # 每 5 分钟清理一次
```

## 相关链接

- [Spring Session JDBC 官方文档](https://docs.spring.io/spring-session/reference/guides/boot-jdbc.html)
- [Spring Session JDBC 参考](https://docs.spring.io/spring-session/reference/configuration/jdbc.html)
