# DispatcherServlet 调试指南

> **文档创建时间**：2026-01-20  
> **适用版本**：Spring Boot 3.5.9 / Spring Framework 6.x  
> **配套 Demo**：[UserController.java](../src/main/java/io/github/daihaowxg/_05_spring_web/controller/UserController.java)

---

## 📌 一、调试前准备

### 1.1 IDEA 源码关联

1. 打开 `pom.xml`，按 `Cmd + 点击` Spring 依赖，下载源码
2. 或者在 IDEA 中：`File → Project Structure → Libraries → Download Sources`

### 1.2 推荐断点设置

| 调试目标 | 类名 | 方法/行号 |
|----------|------|-----------|
| **请求分发入口** | `DispatcherServlet` | `doDispatch()` |
| **Handler 查找** | `AbstractHandlerMapping` | `getHandler()` |
| **Handler 执行** | `RequestMappingHandlerAdapter` | `handleInternal()` |
| **参数解析** | `HandlerMethodArgumentResolverComposite` | `resolveArgument()` |
| **返回值处理** | `HandlerMethodReturnValueHandlerComposite` | `handleReturnValue()` |

---

## 🔍 二、核心调试流程

### 2.1 请求分发主流程

```
GET http://localhost:8080/api/users/1
```

**调试步骤**：

1. 在 `DispatcherServlet.doDispatch()` 打断点（约第 1062 行）
2. 发送请求，观察以下关键变量：
   - `request` - HttpServletRequest 对象
   - `mappedHandler` - HandlerExecutionChain（包含 Handler + 拦截器链）
   - `mv` - ModelAndView 返回值

**核心代码片段**：
```java
// DispatcherServlet.doDispatch() 简化版
protected void doDispatch(HttpServletRequest request, HttpServletResponse response) {
    HandlerExecutionChain mappedHandler = null;
    ModelAndView mv = null;
    
    // 1. 查找 Handler
    mappedHandler = getHandler(processedRequest);  // ← 断点
    
    // 2. 获取 HandlerAdapter
    HandlerAdapter ha = getHandlerAdapter(mappedHandler.getHandler());
    
    // 3. 执行拦截器 preHandle
    if (!mappedHandler.applyPreHandle(processedRequest, response)) {
        return;
    }
    
    // 4. 执行 Handler（Controller 方法）
    mv = ha.handle(processedRequest, response, mappedHandler.getHandler());  // ← 断点
    
    // 5. 执行拦截器 postHandle
    mappedHandler.applyPostHandle(processedRequest, response, mv);
    
    // 6. 处理结果（视图渲染或直接返回）
    processDispatchResult(processedRequest, response, mappedHandler, mv, dispatchException);
}
```

---

### 2.2 @PathVariable 参数解析流程

**测试请求**：
```
GET http://localhost:8080/api/users/1
```

**调试路径**：
```
DispatcherServlet.doDispatch()
  └─ RequestMappingHandlerAdapter.handleInternal()
       └─ ServletInvocableHandlerMethod.invokeAndHandle()
            └─ InvocableHandlerMethod.invokeForRequest()
                 └─ InvocableHandlerMethod.getMethodArgumentValues()
                      └─ HandlerMethodArgumentResolverComposite.resolveArgument()
                           └─ PathVariableMethodArgumentResolver.resolveArgument()
```

**关键断点**：`PathVariableMethodArgumentResolver.resolveName()` 方法

**观察变量**：
- `uriTemplateVariables` - 路径变量 Map，如 `{id=1}`
- `parameter` - 方法参数信息（类型、名称）

---

### 2.3 @RequestBody JSON 反序列化流程

**测试请求**：
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"name":"王五","email":"wangwu@example.com"}'
```

**调试路径**：
```
DispatcherServlet.doDispatch()
  └─ RequestMappingHandlerAdapter.handleInternal()
       └─ RequestResponseBodyMethodProcessor.resolveArgument()
            └─ AbstractMessageConverterMethodArgumentResolver.readWithMessageConverters()
                 └─ MappingJackson2HttpMessageConverter.read()
                      └─ ObjectMapper.readValue()  ← Jackson 反序列化
```

**关键断点**：`RequestResponseBodyMethodProcessor.resolveArgument()` 方法

**观察变量**：
- `inputMessage` - HTTP 请求体
- `targetType` - 目标类型（User.class）
- `converters` - 可用的消息转换器列表

---

### 2.4 @ResponseBody JSON 序列化流程

**调试路径**：
```
DispatcherServlet.doDispatch()
  └─ RequestMappingHandlerAdapter.handleInternal()
       └─ ServletInvocableHandlerMethod.invokeAndHandle()
            └─ HandlerMethodReturnValueHandlerComposite.handleReturnValue()
                 └─ RequestResponseBodyMethodProcessor.handleReturnValue()
                      └─ AbstractMessageConverterMethodProcessor.writeWithMessageConverters()
                           └─ MappingJackson2HttpMessageConverter.write()
                                └─ ObjectMapper.writeValue()  ← Jackson 序列化
```

**关键断点**：`RequestResponseBodyMethodProcessor.handleReturnValue()` 方法

---

## 🛠️ 三、常用调试技巧

### 3.1 条件断点

在 IDEA 中右键断点 → `Edit Breakpoint` → 添加条件：

```java
// 只在请求特定 URL 时暂停
request.getRequestURI().contains("/api/users")

// 只在特定参数值时暂停
id.equals(1L)
```

### 3.2 表达式求值

使用 `Evaluate Expression`（快捷键 `Alt + F8`）查看：

```java
// 查看当前请求的所有参数
request.getParameterMap()

// 查看 Handler 方法信息
((HandlerMethod) mappedHandler.getHandler()).getMethod().getName()

// 查看所有注册的 HandlerMapping
this.handlerMappings
```

### 3.3 调用栈分析

使用 `Frames` 面板查看调用栈，快速定位：
- 请求从哪个 Filter 进入
- 经过了哪些拦截器
- 在哪个阶段出现异常

---

## 📝 四、测试用例

### 4.1 API 测试命令

```bash
# 获取所有用户
curl http://localhost:8080/api/users

# 获取单个用户（调试 @PathVariable）
curl http://localhost:8080/api/users/1

# 创建用户（调试 @RequestBody）
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"name":"王五","email":"wangwu@example.com"}'

# 更新用户（调试 @PathVariable + @RequestBody）
curl -X PUT http://localhost:8080/api/users/1 \
  -H "Content-Type: application/json" \
  -d '{"name":"张三更新","email":"zhangsan_new@example.com"}'

# 删除用户
curl -X DELETE http://localhost:8080/api/users/1

# 搜索用户（调试 @RequestParam）
curl "http://localhost:8080/api/users/search?name=张"
```

---

## 🎯 五、学习检验

完成本指南后，你应该能够回答：

- [ ] `DispatcherServlet.doDispatch()` 的 5 个核心步骤是什么？
- [ ] `HandlerExecutionChain` 包含哪些内容？
- [ ] `@PathVariable` 的值是从哪里取出来的？
- [ ] `@RequestBody` 是由哪个类负责 JSON 反序列化的？
- [ ] 拦截器的 `preHandle`/`postHandle`/`afterCompletion` 分别在什么时机执行？

---

## 📚 下一步

- [06-HandlerMapping详解.md](./06-HandlerMapping详解.md)
- [07-HandlerAdapter详解.md](./07-HandlerAdapter详解.md)（待创建）
