# 揭秘 Spring 编程式用法：从 Main 方法掌控容器

## 1. 什么是编程式用法？
如果你把 Spring Boot 比作**“全自动挡汽车”**（一键启动，自动匹配环境），那么这种在 `main` 方法中直接操作 API 的写法就是**“手动挡”**。

在 Spring Boot 中，`SpringApplication.run()` 掩盖了所有复杂的启动逻辑。而编程式用法则是直接调用 Spring Framework 的核心类，手动点火、挂挡、加速。

## 2. 核心主角：`AnnotationConfigApplicationContext`
这是现代 Spring 环境中最核心的容器实现类。它的名字就暴露了它的功能：
*   **AnnotationConfig**: 基于注解配置（不再需要老旧的 XML）。
*   **ApplicationContext**: 应用上下文（即我们常说的 Spring 容器）。

### 典型的“三部曲”语法：
```java
// 1. 初始化容器：传入配置类，容器会自动执行扫描、实例化 Bean 等操作
try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MyConfig.class)) {
    
    // 2. 获取 Bean：从容器中“索取”已经初始化好的实例
    MyService service = context.getBean(MyService.class);
    
    // 3. 执行逻辑
    service.execute();
    
} // 4. 自动关闭：由于使用了 try-with-resources，执行完后会自动释放容器资源
```

## 3. 为什么你在 Web 开发中很少见到它？
在标准的 Web 项目（如 Spring MVC 或 Spring Boot）中：
1.  **容器由 Web 容器（如 Tomcat）代为启动**：Spring 的 `ContextLoaderListener` 帮你在后台偷偷完成了 `new ApplicationContext`。
2.  **依赖注入（DI）替代了手动获取**：你只需要写一个 `@Autowired`，Spring 就会把各个 Bean 串联起来，你不需要主动去 `context.getBean()`。

**但是在 `main` 方法这个“外部世界”里，你还没进入 Spring 的管理范围，所以必须先创建一个容器入口。**

## 4. 为什么要学这种写法？

### A. 它是理解 Spring Boot 的钥匙
当你运行 `SpringApplication.run(App.class)` 时，Spring Boot 内部其实就在做类似的事情：
*   判断环境（是 Web 还是普通应用）。
*   实例化对应的 `ApplicationContext` 实现。
*   把你传入的启动类注册为配置类。

### B. 极简的实验环境
如果你只想测试一个 Spring 的功能（比如 `ResourceLoader`、`ResourceLoaderAware` 或 `Bean` 的生命周期），你不需要创建一个庞大的 Spring Boot 项目。一个 `main` 方法 + 几行代码就能在 1 秒钟内启动一个完整的 Spring 环境。

### C. Aware 接口的“降临时刻”
正如此前示例中的 `ResourceLoaderAware`，只有当你通过这种方式启动容器并获取 Bean 时，你才能清晰地看到 Spring 是如何在初始化过程中，通过回调接口把容器内部的组件（如 `ResourceLoader`）“递给”你的对象的。

## 5. 核心 API 速查
*   `context.getBean(Class)`: 最常用的索取方法。
*   `context.register(Class)`: 动态添加配置类。
*   `context.refresh()`: 手动启动或刷新容器（如果创建容器时没传入参数，就需要手动调用）。
*   `context.scan("package.path")`: 编程式指定包扫描范围。

## 6. 总结
*   **Spring Boot** = 自动扫描 + 自动配置 + 默认容器启动。
*   **编程式 Spring** = 手动创建容器 + 手动选择配置 + 手动获取入口 Bean。

下次当你需要写一个**轻量级工具（CLI）**、**单元测试**，或者**想在不启动 Web 服务器的情况下调试 Spring 功能**时，这种写法就是你的首选。
