# WebMvcConfigurer 详解

> **文档创建时间**：2026-01-20
> **适用版本**：Spring Framework 6.x / Spring Boot 3.x
> **前置阅读**：[11-Spring Boot Web自动配置原理详解.md](./11-Spring Boot Web自动配置原理详解.md)

---

## 📌 一、一句话定义

**WebMvcConfigurer** 是 Spring MVC 提供的一个**配置回调接口**。它允许你在不改变 Spring Boot 默认自动配置的前提下，通过“增量定制”的方式修改 MVC 的核心行为（如添加拦截器、配置跨域、自定义参数解析器等）。

---

## 🏗️ 二、设计哲学：默认方法与委托

在 Java 8 之前，开发者通常需要继承 `WebMvcConfigurerAdapter` 类。但从 Spring 5.0 开始，由于支持了 Java 8 的 `default` 方法，该适配器类已被废弃。

**现在的推荐做法**：直接实现 `WebMvcConfigurer` 接口。

```java
public interface WebMvcConfigurer {
    // 这里所有的 20+ 个方法都是 default 的
    // 你只需要重写你关心的那一个
    default void addInterceptors(InterceptorRegistry registry) { }
    default void addResourceHandlers(ResourceHandlerRegistry registry) { }
    // ...
}
```

---

## 🔍 三、核心常用配置方法

### 3.1 拦截器配置 (addInterceptors)
这是最常用的功能，用于注册自定义的 `HandlerInterceptor`。

```java
@Override
public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new LocaleChangeInterceptor())
            .addPathPatterns("/api/**")        // 拦截路径
            .excludePathPatterns("/api/login"); // 排除路径
}
```

### 3.2 静态资源映射 (addResourceHandlers)
自定义静态资源的访问规则（例如将 `/myres/**` 映射到本地磁盘目录）。

```java
@Override
public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/uploads/**")
            .addResourceLocations("file:/var/www/uploads/");
}
```

### 3.3 跨域配置 (addCorsMappings)
全局配置跨域规则，避免在每个 Controller 上写 `@CrossOrigin`。

```java
@Override
public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/api/**")
            .allowedOrigins("https://example.com")
            .allowedMethods("GET", "POST", "PUT", "DELETE");
}
```

### 3.4 参数解析器 (addArgumentResolvers)
注册自定义的参数解析逻辑（例如自动解析 JWT Token 注入 User 对象）。

```java
@Override
public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.add(new CurrentUserArgumentResolver());
}
```

### 3.5 视图控制器 (addViewControllers)
对于单纯的路径转发（不需要调 Service 逻辑），可以直接映射视图名。

```java
@Override
public void addViewControllers(ViewControllerRegistry registry) {
    registry.addViewController("/home").setViewName("index");
}
```

---

## ⚙️ 四、底层原理：它是如何生效的？

在 **`WebMvcAutoConfiguration`** 的内部，有一个非常关键的设计：

1. **自动扫描**：Spring Boot 会自动找到容器中所有实现了 `WebMvcConfigurer` 接口的 Bean。
2. **组合调用**：它内部封装了一个 `WebMvcConfigurerComposite`（组合对象）。
3. **注入代理**：它会将这些 Configurer 中的配置信息，“喂”给真正的 MVC 组件（如 `DelegatingWebMvcConfiguration`）。

**关键结论**：你可以定义**多个**实现 `WebMvcConfigurer` 的类，Spring Boot 会将它们全部收集起来并依次调用，配置是**累加**的。

---

## ⚖️ 五、与 @EnableWebMvc 的恩怨情仇

| 方式 | 效果 | 推荐场景 |
| :--- | :--- | :--- |
| **仅实现接口** | **增量修改**。保留 Spring Boot 所有的自动配置（如默认的消息转换器、静态资源路径等）。 | **99% 的 Spring Boot 应用。** |
| **实现接口 + @EnableWebMvc** | **完全接管**。Spring Boot 的所有 Web 自动配置失效（全盘清空），你必须手动配置所有东西。 | 需要完全控制核心组件流水线极特殊场景。 |

---

## 🧪 六、调试建议

### 6.1 确认配置是否生效
- 在你的 `addInterceptors` 等方法内打断点，看启动时是否被调用。
- 调试 `WebMvcAutoConfiguration.DelegatingWebMvcConfiguration` 类的 `setConfigurers` 方法。

### 6.2 断点建议
- `WebMvcConfigurerComposite`: 查看 Spring 到到底收集到了几个配置类。

---

## 🎯 七、学习检验

- [ ] 为什么现在官方不再推荐继承 `WebMvcConfigurerAdapter`？
- [ ] 如果你在项目中定义了两个 `WebMvcConfigurer` 实现类，它们的执行顺序是由什么决定的？（提示：`@Order`）
- [ ] 既然可以通过配置文件修改端口和 ContextPath，为什么还需要 `WebMvcConfigurer`？
- [ ] 如何利用 `WebMvcConfigurer` 解决前后端分离开发中的跨域问题？

---

## 📚 下一步建议

- [05-拦截器链调试指南.md](./05-拦截器链调试指南.md) —— 了解通过 `WebMvcConfigurer` 注册的拦截器在底层是如何串联执行的。
