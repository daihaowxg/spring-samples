# Session 与 JWT 方案对比

## 概述

Session 和 JWT（JSON Web Token）是两种主流的用户认证与状态管理方案。它们的核心区别在于**状态存储的位置**。

| 方案    | 状态存储位置 | 类型     |
| ------- | ------------ | -------- |
| Session | 服务端       | 有状态   |
| JWT     | 客户端       | 无状态   |

## 工作原理对比

### Session 工作流程

```
┌────────────┐                      ┌────────────┐                     ┌─────────────┐
│   Client   │                      │   Server   │                     │   Storage   │
└─────┬──────┘                      └─────┬──────┘                     └──────┬──────┘
      │  1. POST /login                   │                                   │
      │  (username, password)             │                                   │
      │──────────────────────────────────▶│                                   │
      │                                   │  2. 验证凭据                       │
      │                                   │  3. 创建 Session                  │
      │                                   │───────────────────────────────────▶│
      │                                   │  4. 存储 Session 数据              │
      │  5. Set-Cookie: SESSION_ID=abc123 │◀───────────────────────────────────│
      │◀──────────────────────────────────│                                   │
      │                                   │                                   │
      │  6. GET /api/user                 │                                   │
      │  Cookie: SESSION_ID=abc123        │                                   │
      │──────────────────────────────────▶│                                   │
      │                                   │  7. 查询 Session                  │
      │                                   │───────────────────────────────────▶│
      │                                   │◀───────────────────────────────────│
      │  8. 返回用户数据                   │                                   │
      │◀──────────────────────────────────│                                   │
```

### JWT 工作流程

```
┌────────────┐                      ┌────────────┐
│   Client   │                      │   Server   │
└─────┬──────┘                      └─────┬──────┘
      │  1. POST /login                   │
      │  (username, password)             │
      │──────────────────────────────────▶│
      │                                   │  2. 验证凭据
      │                                   │  3. 生成 JWT（签名）
      │  4. 返回 JWT Token                │
      │◀──────────────────────────────────│
      │                                   │
      │  5. GET /api/user                 │
      │  Authorization: Bearer <jwt>      │
      │──────────────────────────────────▶│
      │                                   │  6. 验证签名 + 解析 Token
      │                                   │  （无需查询存储）
      │  7. 返回用户数据                   │
      │◀──────────────────────────────────│
```

## 核心差异对比

| 维度           | Session                          | JWT                               |
| -------------- | -------------------------------- | --------------------------------- |
| **状态存储**   | 服务端（Redis/DB/内存）          | 客户端（LocalStorage/Cookie）     |
| **服务端压力** | 需要存储和查询 Session           | 无存储，仅验证签名                |
| **扩展性**     | 需要共享存储（如 Redis）         | 天然支持水平扩展                  |
| **安全性**     | Session ID 泄露可被盗用          | Token 泄露同样可被盗用            |
| **注销实现**   | 删除服务端 Session 即可          | 需要额外机制（黑名单/短过期）     |
| **Token 大小** | Cookie 仅存 ID（几十字节）       | 包含 Payload（几百字节到几 KB）    |
| **跨域支持**   | 依赖 Cookie，跨域复杂            | Header 传递，跨域友好             |

## 安全性对比

### Session 安全特性

```java
// Spring Session 配置示例
@Bean
public CookieSerializer cookieSerializer() {
    DefaultCookieSerializer serializer = new DefaultCookieSerializer();
    serializer.setSameSite("Strict");     // 防止 CSRF
    serializer.setUseHttpOnlyCookie(true); // 防止 XSS 读取
    serializer.setUseSecureCookie(true);   // 仅 HTTPS 传输
    return serializer;
}
```

| 攻击类型 | Session 防护                     |
| -------- | -------------------------------- |
| CSRF     | SameSite Cookie + CSRF Token     |
| XSS      | HttpOnly Cookie                  |
| 劫持     | HTTPS + Secure Cookie            |
| 固定攻击 | 登录后重新生成 Session ID        |

### JWT 安全特性

```java
// JWT 生成示例
String jwt = Jwts.builder()
    .setSubject(userId)
    .setIssuedAt(new Date())
    .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1小时
    .signWith(SignatureAlgorithm.HS256, secretKey)
    .compact();
```

| 攻击类型 | JWT 防护                         |
| -------- | -------------------------------- |
| 篡改     | 签名验证（HS256/RS256）          |
| 过期     | exp 声明 + 短过期时间            |
| 泄露     | HTTPS + 合理的存储位置           |
| 重放     | jti 声明 + 黑名单                |

## 注销机制对比

### Session 注销

```java
@PostMapping("/logout")
public void logout(HttpSession session) {
    session.invalidate(); // 服务端删除，立即生效
}
```

### JWT 注销（需要额外实现）

```java
@PostMapping("/logout")
public void logout(@RequestHeader("Authorization") String token) {
    String jwt = token.substring(7);
    String jti = extractJti(jwt);
    // 方案1：黑名单
    blacklistService.add(jti, getRemainingExpiration(jwt));
    // 方案2：Token 版本号（需要查库验证）
}
```

## 适用场景

### 推荐使用 Session

- ✅ **传统 Web 应用** - 服务端渲染，页面跳转
- ✅ **需要即时注销** - 后台管理、金融系统
- ✅ **会话数据较多** - 购物车、多步骤表单
- ✅ **已有 Redis 基础设施** - Spring Session + Redis 成熟方案

### 推荐使用 JWT

- ✅ **纯 API 服务** - 移动端、第三方集成
- ✅ **微服务间调用** - 服务间信任传递
- ✅ **Serverless 架构** - 无持久化存储
- ✅ **跨域场景** - 多子域名、前后端分离

## 混合方案

实际项目中，两者可以结合使用：

```
┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│    Web 端   │     │   Gateway   │     │  微服务集群  │
│  (Session)  │────▶│   (转换)    │────▶│   (JWT)     │
└─────────────┘     └─────────────┘     └─────────────┘
```

- **Web 端**：使用 Session，支持复杂会话管理和即时注销
- **Gateway**：验证 Session 后，生成短期 JWT 传递给内部服务
- **微服务**：使用 JWT 进行服务间认证，无需共享存储

## 决策流程图

```
                    开始选择
                       │
                       ▼
              ┌─────────────────┐
              │ 需要即时注销？   │
              └────────┬────────┘
                       │
           ┌───────────┴───────────┐
           │ Yes                   │ No
           ▼                       ▼
      ┌─────────┐          ┌─────────────────┐
      │ Session │          │ 有服务端存储？   │
      └─────────┘          └────────┬────────┘
                                    │
                        ┌───────────┴───────────┐
                        │ Yes                   │ No
                        ▼                       ▼
                   ┌─────────┐             ┌─────────┐
                   │ Session │             │   JWT   │
                   └─────────┘             └─────────┘
```

## 性能对比

| 操作         | Session（Redis）       | JWT                    |
| ------------ | ---------------------- | ---------------------- |
| 认证延迟     | ~1-5ms（网络往返）     | ~0.1ms（本地计算）     |
| 服务端内存   | Session 数据 * 用户数  | 无                     |
| 带宽开销     | Cookie ~50 bytes       | Token ~500+ bytes      |
| 水平扩展     | 需要共享存储           | 无需额外组件           |

## 总结

| 如果你...                    | 选择      |
| ---------------------------- | --------- |
| 构建传统 Web 应用            | Session   |
| 需要复杂会话管理             | Session   |
| 需要即时注销用户             | Session   |
| 构建 RESTful API             | JWT       |
| 需要跨服务认证               | JWT       |
| 使用 Serverless 架构         | JWT       |
| 希望两者优势兼得             | 混合方案  |
