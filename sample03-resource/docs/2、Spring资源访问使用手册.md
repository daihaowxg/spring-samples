# Spring 资源访问使用手册

本手册介绍了 Spring 框架提供的 `Resource` 接口及其相关用法。相比 JDK 原生的资源访问方式，Spring Resource 提供了更统一、更强大且更易用的抽象。

## 1. 核心接口：Resource

Spring 通过 `org.springframework.core.io.Resource` 接口抽象了所有类型的资源访问。

### 主要实现类
- **ClassPathResource**：访问类路径下的资源（classpath:）。
- **FileSystemResource**：访问文件系统中的资源（file:）。
- **UrlResource**：访问基于 URL 的资源（http:, https:, ftp: 等）。
- **ByteArrayResource / InputStreamResource**：访问内存或流中的资源。

### 常用方法
- `exists()`：检查资源是否存在。
- `isReadable()`：检查资源是否可读。
- `getURL()`：获取资源的 URL 对象。
- `getFile()`：获取资源的 File 对象（仅限文件系统资源）。
- `getInputStream()`：打开资源的输入流。
- `getFilename()`：获取资源的文件名。
- `getDescription()`：获取资源的描述信息（用于错误日志）。

## 2. 策略加载：ResourceLoader

`ResourceLoader` 是加载资源的中央策略接口。它最强大的地方在于支持**协议前缀**。

### 路径协议前缀
| 前缀 | 示例 | 对应的实现类 |
| :--- | :--- | :--- |
| **classpath:** | `classpath:config.xml` | `ClassPathResource` |
| **file:** | `file:/path/to/file` | `FileSystemResource` |
| **http(s):** | `https://example.com` | `UrlResource` |
| **(无)** | `config.xml` | 取决于加载器（默认为 classpath） |

## 3. 代码示例

项目中的 `io.github.daihaowxg.sample03_resource.spring` 包包含了完整的演示代码：

- **SpringResourceAccessExample**：演示了如何手动实例化不同的 `Resource` 实现类并调用其方法。
- **ResourceLoaderExample**：演示了如何通过 `ResourceLoader` 根据带有前缀的路径字符串动态加载资源。
- **SpringResourceAccessDemo**：综合演示，建议从运行此类开始。

## 4. 为什么优先使用 Spring Resource？

1. **解耦**：代码只依赖于 `Resource` 接口，而不需要关心资源到底是在磁盘上还是在 JAR 包里。
2. **灵活**：只需更改配置（路径字符串），即可在不同环境（本地文件 vs 类路径资源）间切换。
3. **安全**：Spring 处理了复杂的路径解析逻辑，避免了 JDK 原生 API 中常见的路径处理陷阱。
4. **易用**：提供了比 `java.net.URL` 或 `ClassLoader` 更贴近业务需求的方法。
