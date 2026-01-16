# Spring 源码学习路径

这份文档规划了 `sample03-spring-reading` 项目的 Spring 源码学习路线。我们将按照“基础设施 -> IoC 核心 -> Bean 生命周期 -> AOP -> 数据访问 -> Web 开发 -> 安全架构”的顺序逐步深入。

## 第一阶段：基础设施 (Infrastructure)

**目标**：理解 Spring 独立于容器之外的通用能力，这些是 Spring 运行的基石。

| 模块 | 关键组件 | 说明 |
| :--- | :--- | :--- |
| **01-spring-resource** | `Resource`, `ResourceLoader` | 统一资源加载策略，屏蔽底层资源差异。 |
| **02-spring-metadata** | `AnnotationMetadata`, `MetadataReader` | 不加载类的情况下读取类的注解和元数据。 |
| **03-spring-validator** | `Validator`, `DataBinder` | 数据校验与绑定体系。 |
| **04-spring-convert** | `Converter`, `GenericConverter`, `ConversionService` | 强大的类型转换体系 (Spring 3.0+)。 |
| **05-spring-expression** | `SpelExpressionParser` | SpEL 表达式语言，用于动态求值。 |
| **06-spring-environment** | `Environment`, `PropertySource` | 环境抽象，管理 Profile 和配置属性。 |

## 第二阶段：IoC 容器核心 (IoC Core)

**目标**：掌握 Bean 的定义、解析、注册以及容器的基本工作原理。

| 模块 | 关键组件 | 说明 |
| :--- | :--- | :--- |
| **10-spring-beandefinition** | `BeanDefinition` | Bean 的元数据模型，容器的“图纸”。 |
| **11-spring-register** | `BeanDefinitionRegistry` | Bean 定义的注册表。 |
| **12-spring-factory** | `BeanFactory`, `DefaultListableBeanFactory` | 容器的根基，负责 Bean 的生产和管理。 |
| **13-spring-context** | `ApplicationContext` | 应用上下文，集成了 Event、Resource 等功能的高级容器。 |

## 第三阶段：Bean 生命周期 (Bean Lifecycle)

**目标**：深入理解 Bean 从创建到销毁的全过程，以及 Spring 提供的扩展点。

| 模块 | 关键组件 | 说明 |
| :--- | :--- | :--- |
| **20-spring-creation** | `InstantiationStrategy` | Bean 的实例化策略（如反射、CGLIB）。 |
| **21-spring-populate** | `AutowiredAnnotationBeanPostProcessor` | 属性填充与依赖注入的核心。 |
| **22-spring-initialize** | `BeanPostProcessor`, `InitializingBean` | 初始化回调与前后置处理。 |
| **23-spring-cycle** | 三级缓存 | 循环依赖的解决机制。 |

## 第四阶段：AOP 与 动态代理 (AOP & Proxy)

**目标**：学习 Spring 如何实现面向切面编程，理解代理机制。

| 模块 | 关键组件 | 说明 |
| :--- | :--- | :--- |
| **30-spring-proxy** | `ProxyFactory`, `AopProxy` | 代理工厂，屏蔽 JDK 和 CGLIB 的差异。 |
| **31-spring-advisor** | `Advisor`, `Pointcut`, `Advice` | 切面、切点与通知的定义。 |
| **32-spring-auto-proxy** | `AbstractAutoProxyCreator` | 自动代理创建器，将 AOP 集成到 Bean 生命周期中。 |

## 第五阶段：高级特性 (Advanced)

**目标**：补充完善 Spring 容器的高级功能。

| 模块 | 关键组件 | 说明 |
| :--- | :--- | :--- |
| **40-spring-event** | `ApplicationEvent`, `ApplicationListener` | 观察者模式的实现，容器事件机制。 |
| **41-spring-resources-pattern** | `PathMatchingResourcePatternResolver` | 支持 Ant 风格路径匹配的高级资源加载。 |

## 第六阶段：数据访问与事务 (Data Access & Transaction)

**目标**：理解 Spring 是如何简化 JDBC 操作以及统一事务管理的。

| 模块 | 关键组件 | 说明 |
| :--- | :--- | :--- |
| **50-spring-jdbc** | `JdbcTemplate`, `DataSourceUtils` | JDBC 核心模板类与资源连接工具。 |
| **51-spring-tx** | `PlatformTransactionManager`, `@Transactional` | 事务管理器抽象与基于 AOP 的声明式事务。 |

## 第七阶段：Web 开发 (Web MVC)

**目标**：掌握 Spring MVC 的请求处理流程与核心组件。

| 模块 | 关键组件 | 说明 |
| :--- | :--- | :--- |
| **60-spring-web** | `WebApplicationContext` | Web 环境下的 ApplicationContext 初始化与刷新。 |
| **61-spring-mvc** | `DispatcherServlet`, `HandlerMapping` | 前端控制器模式实现，请求分发与视图解析。 |

## 第八阶段：安全框架 (Spring Security)

**目标**：理解 Spring Security 的核心架构与认证授权机制。

| 模块 | 关键组件 | 说明 |
| :--- | :--- | :--- |
| **70-spring-security-core** | `Authentication`, `SecurityContext` | 核心安全上下文与认证对象模型。 |
| **71-spring-security-web** | `FilterChainProxy`, `SecurityFilterChain` | 基于 Filter 链的 Web 安全拦截机制。 |
