# Spring 编码与空安全规范

本规则适用于所有 Spring 相关的 Java 源代码，旨在确保代码符合 Spring 6.x+ 的最新标准，并避免常见的空指针与配置错误。

---

## 🎯 1. 适用范围
- `src/main/java/**/*.java`

## 🛡️ 2. 空安全性与健壮性 (Null Safety & Robustness)
- **防御性编程**：在公有方法的入口处，优先使用 `org.springframework.util.Assert` 进行断言检查（如 `Assert.notNull(param, "...")`），做到“快速失败”，避免 `NullPointerException` 蔓延。
- **空对象模式 (Null Object Pattern)**：严禁直接传递 `null` 作为方法参数。若需表示“无行为”或“默认行为”，应传递预定义的空实现（如 `DefaultHandler`、`ResourceLoaderSupport`）或空集合（`Collections.emptyList()`）。
- **显式语义化**：
  - 返回值可能为空时，优先返回 `java.util.Optional<T>` 而非 `null`，以强制调用方处理空情况。
  - 对于集合类返回值，**严禁返回 null**，必须返回空集合。
- **包级约束认知**：在 Spring 6 环境下，必须感知包路径下的 `@NonNullApi` 状态。若包已声明非空，所有参数均视为必须非空，除非参数被显式标注为 `@Nullable`。

## ⚙️ 3. 资源加载规范
- **路径前缀明确化**：在获取 Resource 时，必须显式使用前缀（如 classpath:、file:），避免依赖特定加载器的默认逻辑。
- **IO 闭环**：所有从 Resource.getInputStream() 获取的流，必须使用 Java 21 的 try-with-resources 语法确保自动关闭。
- **编码一致性**：读取资源文本时，必须显式指定 StandardCharsets.UTF_8。

## 🧪 4. 示例代码编写 (Sample Code)
- **自说明性**：每个示例类的类头必须包含详细的 Javadoc，并使用 {@link ...} 链接到对应的 Spring 核心类。
- **无状态设计**：示例代码应尽量编写为可直接运行的 main 方法，且不应产生持久化的副作用。

## 📝 5. 日志与反馈
- **SLF4J 优先**：禁止使用 System.out.println（除非是纯粹的控制台演示示例），应优先使用 SLF4J 记录日志。
- **中文控制台输出**：对于面向教学的演示输出，控制台提示信息应使用中文。
