# SOP：从 Main 方法获取 Spring Bean

> 本文档提供了在 `main()` 方法中手动创建 Spring 容器并获取 Bean 的标准操作流程。适用于编写轻量级工具、CLI 程序、单元测试、或调试 Spring 功能等场景。

---

## 1. 核心模式（可直接复制）

```java
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

public class MyDemo {

    // 1️⃣ 内部配置类：定义扫描范围
    @Configuration
    @ComponentScan("your.package.path")  // 替换为你的包路径
    static class Config {
    }

    public static void main(String[] args) {
        // 2️⃣ 创建容器 + 获取 Bean + 执行逻辑 + 自动关闭
        try (AnnotationConfigApplicationContext context = 
                new AnnotationConfigApplicationContext(Config.class)) {
            
            MyService service = context.getBean(MyService.class);
            service.doSomething();
        }
    }
}
```

---

## 2. 步骤分解

| 步骤 | 操作 | 说明 |
|------|------|------|
| **1** | 定义配置类 | 使用 `@Configuration` + `@ComponentScan` 指定要扫描的包 |
| **2** | 创建容器 | `new AnnotationConfigApplicationContext(Config.class)` |
| **3** | 获取 Bean | `context.getBean(YourBean.class)` |
| **4** | 执行逻辑 | 调用 Bean 的方法 |
| **5** | 关闭容器 | 使用 `try-with-resources` 自动释放资源 |

---

## 3. 配置类位置选择

### 方案 A：内部静态类（推荐用于 Demo/测试）

```java
public class MyDemo {
    
    @Configuration
    @ComponentScan("io.github.example")
    static class Config { }
    
    public static void main(String[] args) {
        try (var context = new AnnotationConfigApplicationContext(Config.class)) {
            // ...
        }
    }
}
```

**优点**：代码内聚，一个文件即可运行。

### 方案 B：独立配置类（推荐用于正式项目）

```java
// AppConfig.java
@Configuration
@ComponentScan("io.github.example")
public class AppConfig { }

// Main.java
public class Main {
    public static void main(String[] args) {
        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            // ...
        }
    }
}
```

**优点**：配置可复用，职责分离。

---

## 4. 常用 API 速查

```java
// 获取 Bean
MyBean bean = context.getBean(MyBean.class);
MyBean bean = context.getBean("beanName", MyBean.class);

// 动态注册配置类（需手动 refresh）
context.register(AnotherConfig.class);
context.refresh();

// 编程式指定扫描包（需手动 refresh）
context.scan("com.example.service");
context.refresh();

// 获取环境变量
String value = context.getEnvironment().getProperty("key");
```

---

## 5. 使用 Aware 接口获取容器组件

如果你的 Bean 需要获取容器内部组件（如 `ResourceLoader`、`ApplicationContext`），可实现对应的 `Aware` 接口：

```java
@Component
public class MyResourceBean implements ResourceLoaderAware {
    
    private ResourceLoader resourceLoader;
    
    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;  // 容器自动注入
    }
    
    public void loadResource(String location) {
        Resource resource = resourceLoader.getResource(location);
        // ...
    }
}
```

**常用 Aware 接口**：

| 接口 | 注入对象 |
|------|----------|
| `ResourceLoaderAware` | `ResourceLoader` |
| `ApplicationContextAware` | `ApplicationContext` |
| `BeanFactoryAware` | `BeanFactory` |
| `EnvironmentAware` | `Environment` |
| `BeanNameAware` | Bean 自身的名称 |

---

## 6. 完整示例模板

```java
package io.github.example;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
public class MyDemo {

    @Configuration
    @ComponentScan("io.github.example")
    static class Config { }

    public static void main(String[] args) {
        System.out.println("=== Demo 开始 ===");
        
        try (AnnotationConfigApplicationContext context = 
                new AnnotationConfigApplicationContext(Config.class)) {
            
            MyDemo demo = context.getBean(MyDemo.class);
            demo.run();
        }
        
        System.out.println("=== Demo 结束 ===");
    }

    public void run() {
        System.out.println("Bean 方法执行中...");
        // 你的业务逻辑
    }
}
```

---

## 7. Checklist（执行前检查）

- [ ] 配置类已添加 `@Configuration` 注解
- [ ] `@ComponentScan` 的包路径正确
- [ ] 目标 Bean 已添加 `@Component`（或 `@Service`、`@Repository` 等）
- [ ] 使用 `try-with-resources` 确保容器正常关闭
- [ ] `getBean()` 的类型与实际 Bean 类型匹配

---

## 8. 常见问题

### Q1: `NoSuchBeanDefinitionException`

**原因**：Bean 不在扫描范围内，或缺少 `@Component` 注解。

**解决**：检查 `@ComponentScan` 路径和 Bean 的注解。

### Q2: 为什么不直接 `new MyService()`？

**原因**：手动 `new` 的对象不在 Spring 容器管理范围内，无法享受依赖注入、AOP、生命周期回调等功能。

### Q3: 容器创建时没传配置类怎么办？

```java
AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
context.register(Config.class);
context.refresh();  // 必须手动调用
```

---

> **最后提示**：这种编程式用法是理解 Spring Boot 内部机制的钥匙。`SpringApplication.run()` 本质上也在做类似的事情——只是帮你封装了更多自动化逻辑。
