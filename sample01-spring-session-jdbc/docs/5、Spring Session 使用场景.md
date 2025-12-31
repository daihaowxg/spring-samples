# Spring Session 使用场景

## 概述

Spring Session 主要解决**分布式环境下的会话管理问题**。以下是最常见的使用场景和对应的解决方案。

## 场景一：多实例负载均衡

### 问题

```
                    ┌─────────────┐
                    │   Nginx     │
                    │ (负载均衡)   │
                    └──────┬──────┘
           ┌───────────────┼───────────────┐
           ▼               ▼               ▼
    ┌─────────────┐ ┌─────────────┐ ┌─────────────┐
    │  Server A   │ │  Server B   │ │  Server C   │
    │ Session: ✓  │ │ Session: ✗  │ │ Session: ✗  │
    └─────────────┘ └─────────────┘ └─────────────┘
```

用户首次请求被路由到 Server A 并创建 Session，后续请求被路由到 Server B 时找不到 Session，导致**会话丢失**。

### 解决方案

使用 Spring Session + Redis 集中存储会话：

```yaml
spring:
  session:
    store-type: redis
  redis:
    host: redis-cluster
    port: 6379
```

所有服务器读写同一份 Session 数据，请求可被分发到任意节点。

## 场景二：容器化与 Kubernetes 部署

### 问题

- Pod 可能随时被销毁和重建
- 副本数量动态伸缩
- 内存中的 Session 随 Pod 销毁而丢失

### 解决方案

```yaml
# Kubernetes Deployment
spec:
  replicas: 3
  template:
    spec:
      containers:
        - name: app
          env:
            - name: SPRING_SESSION_STORE_TYPE
              value: redis
            - name: SPRING_REDIS_HOST
              value: redis-service
```

服务无状态化，Session 外置到独立的 Redis 实例或集群。

## 场景三：微服务架构

### 问题

- 网关、业务服务、认证服务分离
- 用户身份需要在多个服务间传递和验证

### 解决方案

```
┌─────────┐     ┌─────────────┐     ┌─────────────┐
│  网关   │────▶│  用户服务   │────▶│  订单服务   │
└────┬────┘     └─────────────┘     └─────────────┘
     │                │                    │
     └────────────────┼────────────────────┘
                      ▼
               ┌─────────────┐
               │    Redis    │
               └─────────────┘
```

- 网关验证 Session 并转发用户标识
- 各服务共享 Redis 中的 Session 数据
- 支持使用 HTTP Header 传递 Session ID

```java
@Bean
public HttpSessionIdResolver sessionIdResolver() {
    return HeaderHttpSessionIdResolver.xAuthToken();
}
```

## 场景四：登录状态保持与单点登录（SSO）

### 问题

- 用户希望"记住我"，长时间保持登录状态
- 多个应用系统需要共享登录状态

### 解决方案

```yaml
spring:
  session:
    timeout: 7d  # 7天过期
    redis:
      namespace: sso  # 统一命名空间
```

多个应用使用相同的 Redis namespace，实现跨应用会话共享。

## 场景五：多账户同时在线

### 问题

用户需要在同一浏览器中同时登录多个账户（如管理员和普通用户）。

### 解决方案

Spring Session 支持多会话管理：

```java
@Controller
public class SessionController {
    
    @GetMapping("/sessions")
    public String listSessions(HttpServletRequest request) {
        HttpSession session = request.getSession();
        // 通过不同的 Session alias 管理多个会话
        return "sessions";
    }
}
```

## 场景六：移动端与前后端分离

### 问题

- 移动端 App 不方便使用 Cookie
- 前端 SPA 通过 AJAX 调用后端 API

### 解决方案

使用 HTTP Header 替代 Cookie 传递 Session ID：

```java
@Configuration
@EnableRedisHttpSession
public class SessionConfig {
    
    @Bean
    public HttpSessionIdResolver sessionIdResolver() {
        // 使用 X-Auth-Token Header
        return HeaderHttpSessionIdResolver.xAuthToken();
    }
}
```

客户端请求示例：

```http
GET /api/user/profile HTTP/1.1
X-Auth-Token: abc123-session-id
```

## 场景对比表

| 场景               | 推荐存储后端 | 关键配置                       |
| ------------------ | ------------ | ------------------------------ |
| 负载均衡多实例     | Redis        | `store-type: redis`            |
| Kubernetes 部署    | Redis        | 环境变量配置 + 外置 Redis      |
| 微服务架构         | Redis        | Header 传递 Session ID         |
| 单点登录           | Redis        | 统一 namespace                 |
| 多账户同时在线     | Redis/JDBC   | 多会话管理 API                 |
| 移动端/前后端分离  | Redis        | `HeaderHttpSessionIdResolver`  |

## 不适用场景

- ⚠️ **单体应用单实例部署** - 使用 Servlet 原生 Session 即可
- ⚠️ **无状态 JWT 认证** - Session 和 JWT 是两种方案，按需选择
- ⚠️ **对延迟极度敏感** - 外部存储引入网络开销，虽然通常在 1ms 以内

## 小结

| 如果你需要... | 则应该使用 Spring Session |
| ------------- | ------------------------- |
| 多节点共享会话 | ✅ |
| 服务无状态化   | ✅ |
| 高可用会话存储 | ✅ |
| 跨应用单点登录 | ✅ |
| 移动端会话支持 | ✅ |
