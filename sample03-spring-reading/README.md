# Sample03 - Spring Reading（Spring 源码阅读笔记）

本模块是 Spring 框架核心功能的源码阅读笔记与示例代码集合，涵盖资源访问、元数据、验证器、类型转换四大主题。

---

## 📚 文档目录

### 01 - Spring Resource（资源访问）

学习 Spring 如何封装和访问各种资源（文件、URL、classpath 等）。

- [01-JDK资源访问使用手册](docs/01-spring-resource/01-JDK资源访问使用手册.md)
- [02-Spring资源访问使用手册](docs/01-spring-resource/02-Spring资源访问使用手册.md)
- [03-URI与URL的区别](docs/01-spring-resource/03-URI与URL的区别.md)
- [04-Spring编程式用法介绍](docs/01-spring-resource/04-Spring编程式用法介绍.md)
- [05-ResourcePatternResolver与ResourceLoader的区别](docs/01-spring-resource/05-ResourcePatternResolver与ResourceLoader的区别.md)
- [06-详解Ant风格路径匹配](docs/01-spring-resource/06-详解Ant风格路径匹配.md)
- [07-Spring资源加载-DocumentLoader](docs/01-spring-resource/07-Spring资源加载-DocumentLoader.md)

### 02 - Spring Metadata（元数据）

学习 Spring 如何读取和使用类元数据，以及条件化 Bean 注册机制。

- [01-MetadataReader元数据读取](docs/02-spring-metadata/01-MetadataReader元数据读取.md)
- [02-TypeFilter组件过滤器](docs/02-spring-metadata/02-TypeFilter组件过滤器.md)
- [03-Condition条件化Bean注册](docs/02-spring-metadata/03-Condition条件化Bean注册.md)

### 03 - Spring Validator（验证器）

学习 Spring 的数据验证机制。

- [01-Validator接口](docs/03-spring-validator/01-Validator接口.md)

### 04 - Spring Convert（类型转换）

学习 Spring 强大的类型转换体系，从 PropertyEditor 到现代的 Converter 接口。

- [01-Spring类型转换体系综述](docs/04-spring-convert/01-Spring类型转换体系综述.md)
- [02-Spring类型处理体系分类](docs/04-spring-convert/02-Spring类型处理体系分类.md)
- [03-PropertyEditor属性编辑器](docs/04-spring-convert/03-PropertyEditor属性编辑器.md)
- [04-Converter类型转换器](docs/04-spring-convert/04-Converter类型转换器.md)
- [05-ConverterFactory接口](docs/04-spring-convert/05-ConverterFactory接口.md)
- [06-GenericConverter泛型转换器](docs/04-spring-convert/06-GenericConverter泛型转换器.md)
- [07-ConditionalConverter条件转换器](docs/04-spring-convert/07-ConditionalConverter条件转换器.md)
- [08-ConversionService接口](docs/04-spring-convert/08-ConversionService接口.md)

---

## 🗂️ 代码模块

| 模块 | 说明 | 类型 |
|------|------|------|
| [spring-resource](spring-resource) | 资源访问示例代码 | 单模块 |
| [spring-metadata](spring-metadata) | 元数据读取示例代码 | 单模块 |
| [spring-validator](spring-validator) | 验证器示例代码 | 单模块 |
| [spring-convert](spring-convert) | 类型转换示例代码 | 多模块 |

### spring-convert 子模块

`spring-convert` 采用多模块结构，每个转换器类型都有独立的示例：

- `property-editor` - PropertyEditor 示例
- `converter` - Converter 接口示例
- `converter-factory` - ConverterFactory 接口示例
- `generic-converter` - GenericConverter 接口示例
- `conditional-converter` - ConditionalConverter 接口示例
- `conversion-service` - ConversionService 使用示例

---

## 🛤️ 学习路线

建议按照以下顺序学习：

### 阶段一：资源访问（Spring Resource）
从 JDK 基础资源访问开始，逐步学习 Spring 的资源封装和高级用法。

**学习顺序**：01-JDK基础 → 02-Spring封装 → 03-概念区别 → 04-编程式用法 → 05-接口对比 → 06-路径匹配 → 07-文档加载

### 阶段二：元数据（Spring Metadata）
理解 Spring 如何使用元数据进行组件扫描和条件化配置。

**学习顺序**：01-MetadataReader → 02-TypeFilter → 03-Condition

### 阶段三：数据验证（Spring Validator）
学习 Spring 的验证框架。

**学习内容**：Validator 接口及其使用

### 阶段四：类型转换（Spring Convert）
深入理解 Spring 的类型转换体系，从传统的 PropertyEditor 到现代的 Converter。

**学习顺序**：01-综述 → 02-分类 → 03-PropertyEditor → 04-Converter → 05-ConverterFactory → 06-GenericConverter → 07-ConditionalConverter → 08-ConversionService

---

## 🚀 快速开始

### 运行示例

每个模块都是独立的 Spring Boot 应用，可以单独运行：

```bash
# 运行 spring-resource 示例
cd spring-resource
mvn spring-boot:run

# 运行 spring-metadata 示例
cd spring-metadata
mvn spring-boot:run

# 运行 spring-validator 示例
cd spring-validator
mvn spring-boot:run

# 运行 spring-convert 子模块示例（以 converter 为例）
cd spring-convert/converter
mvn spring-boot:run
```

### 运行测试

```bash
# 在模块根目录运行所有测试
mvn test

# 运行特定模块的测试
mvn test -pl spring-resource
mvn test -pl spring-convert/converter
```

---

## 📖 文档说明

所有文档均采用统一的编号格式：

- **主题级别**：`01-spring-resource`、`02-spring-metadata` 等
- **文档级别**：`01-文档标题.md`、`02-文档标题.md` 等

编号体现了推荐的学习顺序，从基础到高级，从概念到实践。

---

## 🔧 技术栈

- **Java**: 21
- **Spring Boot**: 3.x
- **Maven**: 3.x

---

## 📝 许可证

本项目采用与父项目相同的许可证。

