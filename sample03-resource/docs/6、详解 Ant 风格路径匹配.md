# 详解 Ant 风格路径匹配

> 最后更新时间：2026-01-07

## 1. 什么是 Ant 风格？
Ant 风格（Ant-style）源自 Apache Ant 构建工具，是一种简单而强大的通配符路径匹配规则。Spring 框架广泛采用这种风格来过滤资源、定义 URL 拦截规则等。

## 2. 三大核心通配符

| 符号 | 描述 | 示例 | 匹配结果 |
| :--- | :--- | :--- | :--- |
| **`?`** | 匹配**单个**字符 | `t?st.java` | 匹配 `test.java`, `tast.java`，但不匹配 `tst.java` |
| **`*`** | 匹配**零个或多个**字符（**不跨目录**） | `*.xml` | 匹配 `a.xml`, `config.xml` |
| **`**`** | 匹配**零个或多个目录** | `**/beans.xml` | 匹配 `a/beans.xml`, `a/b/c/beans.xml` |

## 3. 常见组合示例

*   **`com/example/demo/**/service/*Service.java`**: 匹配 `com.example.demo` 及其任何层次的子包下，所有名为 `service` 的包中以 `Service` 结尾的类。
    *   例如：`com/example/demo/user/service/UserService.java`
    *   例如：`com/example/demo/order/api/service/OrderService.java`
*   **`classpath*:mapper/*-mapper.xml`**: 匹配类路径下 `mapper` 目录中所有以 `-mapper.xml` 结尾的文件。

## 4. 在 Spring 中的应用
1.  **资源加载**: 在 `ResourcePatternResolver` 中扫描配置文件。
2.  **MVC 拦截器**: 在 `WebMvcConfigurer` 中配置路径拦截规则（如 `/api/**`）。
3.  **安全配置**: 在 Spring Security 中定义授权路径。

## 5. 注意事项
*   **贪婪匹配**: `**` 是递归的，匹配范围非常广，使用时需注意性能影响。
*   **优先级**: 通常精确匹配优于通配符匹配。
