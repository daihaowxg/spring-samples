# DispatcherServlet 属性详解

> **文档依据**：`org.springframework.web.servlet.DispatcherServlet` 源码定义
> **文档用途**：辅助源码阅读，理解组件查找键名及 request 域属性

---

## 🔑 一、Spring Bean 名称常量

DispatcherServlet 在初始化时，会尝试从 Spring 容器中查找具有特定名称的 Bean。如果找到了，就使用该 Bean 作为对应的组件；如果没找到（且 `detectAll...` 属性为 `false`），则可能会报错或使用默认策略。

| 常量名 | Bean 名称值 | 描述 |
| :--- | :--- | :--- |
| `MULTIPART_RESOLVER_BEAN_NAME` | `multipartResolver` | 文件上传解析器 |
| `LOCALE_RESOLVER_BEAN_NAME` | `localeResolver` | 本地化（国际化）解析器 |
| `THEME_RESOLVER_BEAN_NAME` | `themeResolver` | **[已弃用]** 主题解析器（Spring 6.0+） |
| `HANDLER_MAPPING_BEAN_NAME` | `handlerMapping` | 处理器映射器（仅在 `detectAllHandlerMappings=false` 时生效） |
| `HANDLER_ADAPTER_BEAN_NAME` | `handlerAdapter` | 处理器适配器（仅在 `detectAllHandlerAdapters=false` 时生效） |
| `HANDLER_EXCEPTION_RESOLVER_BEAN_NAME` | `handlerExceptionResolver` | 异常解析器（仅在 `detectAllHandlerExceptionResolvers=false` 时生效） |
| `REQUEST_TO_VIEW_NAME_TRANSLATOR_BEAN_NAME` | `viewNameTranslator` | 视图名转换器（当 Controller 返回 `void` 或 `null` 时自动生成视图名） |
| `VIEW_RESOLVER_BEAN_NAME` | `viewResolver` | 视图解析器（仅在 `detectAllViewResolvers=false` 时生效） |
| `FLASH_MAP_MANAGER_BEAN_NAME` | `flashMapManager` | FlashMap 管理器（用于重定向传参） |

---

## 📦 二、Standard Request Attributes (请求域属性)

在请求处理过程中，DispatcherServlet 会将一些核心上下文对象放入 `HttpServletRequest` 的属性中（`request.setAttribute`），以便视图层（如 JSP、Thymeleaf）或过滤器、拦截器能够获取到这些信息。

| 常量名 | 属性 Key (Class Name + Suffix) | 描述 |
| :--- | :--- | :--- |
| `WEB_APPLICATION_CONTEXT_ATTRIBUTE` | `...DispatcherServlet.CONTEXT` | 当前的 `WebApplicationContext` 容器实例 |
| `LOCALE_RESOLVER_ATTRIBUTE` | `...DispatcherServlet.LOCALE_RESOLVER` | 当前请求使用的 `LocaleResolver` |
| `THEME_RESOLVER_ATTRIBUTE` | `...DispatcherServlet.THEME_RESOLVER` | **[已弃用]** 当前请求使用的 `ThemeResolver` |
| `THEME_SOURCE_ATTRIBUTE` | `...DispatcherServlet.THEME_SOURCE` | **[已弃用]** 当前请求使用的 `ThemeSource` |
| `INPUT_FLASH_MAP_ATTRIBUTE` | `...DispatcherServlet.INPUT_FLASH_MAP` | **只读 Map**。包含从上一次重定向传递过来的 Flash 属性（即 `RedirectAttributes`） |
| `OUTPUT_FLASH_MAP_ATTRIBUTE` | `...DispatcherServlet.OUTPUT_FLASH_MAP` | **可写 FlashMap**。用于保存要把传递给下一次请求的属性 |
| `FLASH_MAP_MANAGER_ATTRIBUTE` | `...DispatcherServlet.FLASH_MAP_MANAGER` | 当前使用的 `FlashMapManager` 实例 |
| `EXCEPTION_ATTRIBUTE` | `...DispatcherServlet.EXCEPTION` | 如果发生异常并由 `HandlerExceptionResolver`处理过，异常对象会被放在这里（通常用于错误页面展示） |

---

## ⚙️ 三、配置属性 (Configuration Flags)

这些 `boolean` 类型的成员变量控制着 DispatcherServlet 的初始化行为和运行时逻辑。可以通过 `web.xml` 的 `<init-param>` 或 Java Config 进行配置。

| 属性名 | 默认值 | 描述 |
| :--- | :--- | :--- |
| `detectAllHandlerMappings` | `true` | **true**: 检测容器中**所有** `HandlerMapping` 类型的 Bean<br>**false**: 只检测名为 `handlerMapping` 的 Bean |
| `detectAllHandlerAdapters` | `true` | **true**: 检测容器中**所有** `HandlerAdapter` 类型的 Bean<br>**false**: 只检测名为 `handlerAdapter` 的 Bean |
| `detectAllHandlerExceptionResolvers` | `true` | **true**: 检测容器中**所有** `HandlerExceptionResolver` 类型的 Bean<br>**false**: 只检测名为 `handlerExceptionResolver` 的 Bean |
| `detectAllViewResolvers` | `true` | **true**: 检测容器中**所有** `ViewResolver` 类型的 Bean<br>**false**: 只检测名为 `viewResolver` 的 Bean |
| `throwExceptionIfNoHandlerFound` | `true` | 当找不到处理请求的 Handler 时：<br>**true**: 抛出 `NoHandlerFoundException`<br>**false**: 发送 404 响应（通常配合 `web.xml` 的 default servlet 使用）<br>*(注：源码默认值可能依赖具体版本，需结合上下文，Spring Boot 中通常配置为 false)* |
| `cleanupAfterInclude` | `true` | 是否在 include 请求结束后清理 request attributes |

---

## 🧩 四、核心策略组件 (Runtime Components)

这些是 DispatcherServlet 在运行时持有的核心组件引用，在 `onRefresh` 阶段被初始化。

```java
// 主要组件，可能为空（Nullable）直到初始化完成
private MultipartResolver multipartResolver;
private LocaleResolver localeResolver;
private ThemeResolver themeResolver; // Deprecated
private List<HandlerMapping> handlerMappings;
private List<HandlerAdapter> handlerAdapters;
private List<HandlerExceptionResolver> handlerExceptionResolvers;
private RequestToViewNameTranslator viewNameTranslator;
private FlashMapManager flashMapManager;
private List<ViewResolver> viewResolvers;
```

---

## 📝 五、日志相关

| 常量名 | 值 | 描述 |
| :--- | :--- | :--- |
| `PAGE_NOT_FOUND_LOG_CATEGORY` | `org.springframework.web.servlet.PageNotFound` | 专门用于记录 404 错误的日志类别。可以通过将此包名的日志级别设为 `WARN` 或 `DEBUG` 来控制 404 日志的输出。 |

