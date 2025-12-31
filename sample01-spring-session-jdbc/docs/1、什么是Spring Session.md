# 什么是 Spring Session

## 简介

**Spring Session** 是 Spring 生态系统中的一个项目，提供了管理用户会话（Session）信息的 API 和实现。它将 Session 从应用服务器中解耦出来，使 Session 管理更加灵活和可扩展。

## 核心功能

### 1. 外部化 Session 存储

Spring Session 支持将 Session 数据存储到外部存储系统中：

| 存储后端       | 说明                             |
| -------------- | -------------------------------- |
| **Redis**      | 最常用，高性能，支持集群         |
| **JDBC**       | 使用关系型数据库存储 Session     |
| **Hazelcast**  | 分布式内存数据网格               |
| **MongoDB**    | NoSQL 文档数据库                 |
| **GemFire**    | 分布式缓存（VMware Tanzu）       |

### 2. 集群 Session 共享

```
┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│   Server A  │     │   Server B  │     │   Server C  │
└──────┬──────┘     └──────┬──────┘     └──────┬──────┘
       │                   │                   │
       └───────────────────┼───────────────────┘
                           │
                    ┌──────▼──────┐
                    │    Redis    │
                    │  (Session)  │
                    └─────────────┘
```

多个应用实例共享同一个 Session 存储，用户请求被负载均衡器分发到任意一台服务器都能访问到相同的 Session 数据。

### 3. 多会话支持

允许在单个浏览器中管理多个用户会话，适用于：
- 同时登录多个账户
- 在不同标签页切换用户身份

### 4. RESTful API 支持

通过 HTTP Header 传递 Session ID，替代传统的 Cookie 方式，适用于：
- 移动端应用
- 前后端分离架构
- 微服务间调用

## 为什么需要 Spring Session？

### 传统方案的痛点

| 问题           | 描述                                               |
| -------------- | -------------------------------------------------- |
| **粘性会话**   | 依赖负载均衡器将用户绑定到固定服务器，扩展性差     |
| **Session 复制** | Tomcat 集群间复制 Session，网络开销大、配置复杂 |
| **单点故障**   | 服务器宕机导致 Session 丢失                        |

### Spring Session 的优势

1. **透明集成** - 基于 Servlet Filter，无需修改业务代码
2. **水平扩展** - 服务器无状态化，轻松添加或移除节点
3. **高可用** - Session 持久化到外部存储，服务重启不丢失
4. **跨语言** - Session 数据存储在 Redis 等通用存储中，可被其他语言读取

## 快速开始

### Maven 依赖

```xml
<!-- Spring Session + Redis -->
<dependency>
    <groupId>org.springframework.session</groupId>
    <artifactId>spring-session-data-redis</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

### 配置

```yaml
# application.yml
spring:
  session:
    store-type: redis          # 使用 Redis 存储
    timeout: 30m               # Session 超时时间
  redis:
    host: localhost
    port: 6379
```

### 启用 Spring Session

```java
@SpringBootApplication
@EnableRedisHttpSession      // 启用 Redis Session
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

## 工作原理

```
     HTTP Request
           │
           ▼
┌──────────────────────┐
│   SessionRepository  │  ← Spring Session 核心接口
│      Filter          │
└──────────┬───────────┘
           │
           ▼
┌──────────────────────┐
│   HttpSession 包装器  │  ← 透明替换 Servlet HttpSession
└──────────┬───────────┘
           │
           ▼
┌──────────────────────┐
│   外部存储 (Redis)    │  ← 实际的 Session 数据存储
└──────────────────────┘
```

1. `SessionRepositoryFilter` 拦截所有请求
2. 使用自定义的 `HttpSession` 实现包装原始请求
3. Session 操作被代理到 `SessionRepository`（如 `RedisIndexedSessionRepository`）
4. Session 数据最终存储到外部系统

## 适用场景

- ✅ **微服务架构** - 服务实例动态伸缩
- ✅ **分布式系统** - 多节点共享会话状态
- ✅ **容器化部署** - Kubernetes 等环境下的无状态服务
- ✅ **高可用要求** - 服务重启或故障转移不影响用户会话

## 相关链接

- [Spring Session 官方文档](https://docs.spring.io/spring-session/reference/)
- [Spring Session GitHub](https://github.com/spring-projects/spring-session)
- [Spring Session Data Redis](https://docs.spring.io/spring-session/reference/guides/boot-redis.html)
