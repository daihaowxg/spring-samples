# Spring 资源加载 - DocumentLoader

> 最后更新时间：2026-01-07

### 背景信息
在 Spring 框架中，资源的定位（Resource）与解析是应用启动的基础。当涉及到基于 XML 的配置时，Spring 需要一套标准机制将底层的 IO 流转化为内存中的 DOM 对象。`DocumentLoader` 接口正是这一解析链路中的关键环节，它不仅负责解析 XML，还承担了验证（Validation）和实体解析（Entity Resolution）的职责。

### 使用场景
- **Spring 容器启动**：`XmlBeanDefinitionReader` 使用它来加载 Bean 定义文件。
- **自定义配置扩展**：当开发者需要编写自定义的 XML 解析器或在非 Spring 容器环境下重用 Spring 的 XML 加载能力时。
- **配置一致性校验**：在需要严格校验 XML Schema (XSD) 或 DTD 的场景下。

### 主要功能
1. **加载与解析 XML**：通过 `loadDocument` 方法将 `InputSource` 转化为 `org.w3c.dom.Document`。
2. **支持验证模式**：支持 `VALIDATION_DTD` 或 `VALIDATION_XSD`，确保配置文件符合预定义的约束。
3. **命名空间感知 (Namespace Aware)**：支持开启命名空间解析，这对于处理 Spring 中复杂的 XML Schema 集合至关重要。
4. **自定义实体解析与错误处理**：允许注入 `EntityResolver` 处理外部实体下载，以及 `ErrorHandler` 捕获解析阶段的警告或错误。

### 代码示例
以下是使用 `DefaultDocumentLoader` 的典型编程式示例：

```java
public class DocumentLoaderDemo {
    public static void main(String[] args) {
        try {
            // 1. 定义资源
            Resource resource = new ClassPathResource("sample-beans.xml");

            // 2. 实例化默认加载器
            DefaultDocumentLoader documentLoader = new DefaultDocumentLoader();

            // 3. 执行解析
            // 参数列表: InputSource, EntityResolver, ErrorHandler, ValidationMode, NamespaceAware
            Document document = documentLoader.loadDocument(
                new InputSource(resource.getInputStream()), null, null, 0, true);

            // 4. 操作 DOM 对象
            System.out.println("Root Node: " + document.getDocumentElement().getNodeName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

### 最佳实践
- **复用 DefaultDocumentLoader**：在大多数场景下，直接使用 `DefaultDocumentLoader` 即可，它是高度线程安全的且功能完备。
- **开启命名空间支持**：在处理现代 Spring XSD 配置时，`namespaceAware` 务必设置为 `true`。
- **配合 XmlBeanDefinitionReader 使用**：在 Spring 容器外手动解析 Bean 时，建议通过 `XmlBeanDefinitionReader` 进行高层封装，而不是直接操作底层的 `DocumentLoader`。

### 注意事项
- **内存占用**：由于是 DOM 解析，大型 XML 会在内存中构建完整的树结构，应避免解析超大规模的数据文件。
- **环境依赖**：XSD 验证通常需要联网下载或从本地缓存获取 Schema 文件，确保 `EntityResolver` 能正确处理网络环境受限的情况。
