# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 项目概述

这是一个 Spring 框架示例项目集合，用于展示 Spring 框架各种特性的使用方法和源码学习。项目采用 Maven 多模块结构，每个模块独立运行和测试。

## 技术栈

- **Java**: 21
- **Spring Boot**: 3.5.9
- **Maven**: 3.x
- **Lombok**: 用于简化 Java 代码

## 模块结构

项目包含 5 个主模块：

### sample01-spring-session-jdbc
展示 Spring Session 与 JDBC 集成的 Session 存储
- Spring Boot 应用（spring-boot-starter-parent）
- 使用 Spring Session JDBC
- MySQL 数据库支持

### sample02-spring-security
展示 Spring Security 安全框架的使用
- Spring Boot 应用（spring-boot-starter-parent）
- Spring Security 集成
- Spring Web 支持

### sample03-spring-reading
Spring 框架核心功能源码阅读笔记与示例代码集合
- **注意**：大部分子模块继承 spring-boot-starter-parent（06-spring-environment 除外）
- 多模块结构，包含 6 个子主题：
  - `01-spring-resource`：资源访问
  - `02-spring-metadata`：元数据读取
  - `03-spring-validator`：数据验证
  - `04-spring-convert`：类型转换（单模块，内部文档详细讲解）
  - `06-spring-environment`：环境与属性配置（仅使用 spring-context）

### sample05-spring-web
Spring Web 源码学习模块
- Spring Boot 应用（spring-boot-starter-parent）
- Spring Web 框架深度源码解析
- 详细的调试指南和原理文档

### sample04-spring-boot
基础的 Spring Boot 示例

## 文档结构

### 根目录 docs/
存放跨模块的共享文档

### sample03-spring-reading/docs/
每个子主题下都有对应的文档目录：
- `01-spring-resource/`：资源访问文档（7 篇）
- `02-spring-metadata/`：元数据文档（3 篇）
- `03-spring-validator/`：验证器文档
- `04-spring-convert/`：类型转换文档（8 篇）
- `06-spring-environment/`：环境配置文档

## 构建命令

### 根级别命令

```bash
# 编译所有模块
mvn clean compile

# 测试所有模块
mvn test

# 打包所有模块
mvn clean package

# 跳过测试打包
mvn clean package -DskipTests
```

### 指定模块操作

```bash
# 仅构建指定模块
mvn clean compile -pl sample01-spring-session-jdbc

# 运行指定模块的测试
mvn test -pl sample01-spring-session-jdbc

# 运行单个测试类
mvn test -Dtest=Sample01SpringSessionApplicationTests

# 运行单个测试方法
mvn test -Dtest=Sample01SpringSessionApplicationTests#contextLoads

# 运行 sample05-spring-web 模块
mvn test -pl sample05-spring-web
```

### Spring Boot 应用运行

```bash
# 在应用模块目录下运行
cd sample01-spring-session-jdbc
mvn spring-boot:run

# 运行 sample05-spring-web 模块
cd sample05-spring-web
mvn spring-boot:run
```

### sample03-spring-reading 特定命令

```bash
# sample03 的子模块都是独立的 Spring Boot 应用
cd sample03-spring-reading/01-spring-resource
mvn spring-boot:run

# 运行 spring-convert 示例
cd sample03-spring-reading/04-spring-convert
mvn spring-boot:run
```

## 包命名规范

由于 Maven 模块名称包含连字符，实际的 Java 包名会将连字符替换为下划线：
- 模块名：`sample01-spring-session-jdbc`
- 包名：`io.github.daihaowxg.sample01_spring_session`
- 模块名：`sample05-spring-web`
- 包名：`io.github.daihaowxg._05_spring_web`

## 项目结构模式

### Spring Boot 应用模块（sample01, sample02, sample04, sample05-spring-web）
```
sampleXX-{name}/
├── src/
│   ├── main/
│   │   ├── java/io/github/daihaowxg/{package_name}/
│   │   │   ├── controller/
│   │   │   ├── config/
│   │   │   ├── service/
│   │   │   └── {ModuleName}Application.java
│   │   └── resources/
│   │       └── application.{properties|yml}
│   └── test/
│       ├── java/io/github/daihaowxg/{package_name}/
│       │   └── {ModuleName}ApplicationTests.java
│       └── resources/
└── pom.xml
```

### sample03-spring-reading 结构
```
sample03-spring-reading/
├── docs/                           # 文档目录
│   ├── 01-spring-resource/         # 资源访问文档
│   ├── 02-spring-metadata/         # 元数据文档
│   ├── 03-spring-validator/        # 验证器文档
│   ├── 04-spring-convert/          # 类型转换文档
│   └── 06-spring-environment/      # 环境配置文档
├── 01-spring-resource/             # 资源访问示例
├── 02-spring-metadata/             # 元数据示例
├── 03-spring-validator/            # 验证器示例
├── 04-spring-convert/             # 类型转换示例（单模块）
└── 06-spring-environment/          # 环境配置示例
```

### sample05-spring-web 结构
```
sample05-spring-web/
├── docs/                           # Spring Web 源码学习文档
│   ├── Spring Web 源码学习规划.md
│   ├── 00-Servlet基础.md
│   ├── 01-DispatcherServlet初始化流程.md
│   ├── 02-DispatcherServlet属性详解.md
│   ├── 03-DispatcherServlet源码深度解析.md
│   └── ...
└── src/                            # 示例代码
```

## 重要注意事项

### 包名与模块名的关系
Maven 模块名使用连字符（`-`），但 Java 包名使用下划线（`_`）：
- `sample01-spring-session-jdbc` → `sample01_spring_session`
- `sample02-spring-security` → `sample02_spring_security`
- `sample05-spring-web` → `_05_spring_web`（注意前面的下划线）

### sample03 的特殊性
- 大部分子模块继承 `spring-boot-starter-parent`（01-05）
- `06-spring-environment` 例外：仅使用纯 Spring 框架（spring-context）
- 文档采用统一编号格式（01-、02- 等）体现学习顺序
- `04-spring-convert` 是单模块，但内部文档详细讲解了多种转换器

### sample05-spring-web 的特殊性
- 核心模块，包名前缀有下划线：`_05_spring_web`
- 包含完整的 Spring Web 源码分析文档（12+ 篇）

### 代码风格
- 使用 Lombok 注解简化代码（`@Data`, `@Slf4j`, `@RequiredArgsConstructor`）
- Javadoc 使用中文注释
- 左大括号不换行（K&R 风格）
- 每行最多 120 字符
- 方法之间空一行
