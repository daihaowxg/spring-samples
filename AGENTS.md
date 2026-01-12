# AGENTS.md

这是一个Spring示例项目，用于展示Spring框架各种特性的使用方法。

## 构建和测试命令

### Maven命令
```bash
# 清理并编译所有模块
mvn clean compile

# 运行所有测试
mvn test

# 运行指定模块的所有测试
mvn test -pl sample01-spring-session-jdbc

# 运行单个测试类
mvn test -Dtest=Sample01SpringSessionApplicationTests

# 运行单个测试方法
mvn test -Dtest=Sample01SpringSessionApplicationTests#contextLoads

# 打包所有模块
mvn clean package

# 跳过测试打包
mvn clean package -DskipTests

# 安装到本地仓库
mvn clean install

# 运行Spring Boot应用（在特定模块目录下）
cd sample01-spring-session-jdbc
mvn spring-boot:run
```

## 代码风格指南

### Java代码规范

#### 导入顺序
1. JDK标准库（java.*，javax.*）
2. 第三方库（org.springframework.*等）
3. 项目内部包

#### 包命名
- 使用小写字母，单词用下划线分隔
- 格式：`io.github.daihaowxg.{模块名}`
- 示例：`io.github.daihaowxg.sample01_spring_session.controller`

#### 类命名
- 使用PascalCase（首字母大写驼峰）
- 主应用类：{SampleName}Application（如Sample01SpringSessionApplication）
- 控制器：{功能}Controller（如SessionController）
- 配置类：{功能}Config（如SecurityConfig）
- Demo类：{功能}Demo或{功能}Example

#### 方法命名
- 使用camelCase（首字母小写驼峰）
- 动词开头，如get、set、create、update、delete、show
- 布尔返回值方法以is、has、can等开头

#### 字段命名
- 使用camelCase
- 布尔字段以is、has、can等开头
- 使用Lombok注解简化getter/setter

#### 注释
- 类和方法使用Javadoc风格注释（中文）
- 复杂逻辑添加行内注释说明
- 示例注释使用`//`注释

#### 格式化
- 使用制表符缩进
- 左大括号不换行（K&R风格）
- 每行最多120个字符
- 方法之间空一行
- 类内部相关成员之间空一行

### 注解使用

#### Spring注解
- `@SpringBootApplication`：主应用类
- `@RestController`：REST控制器
- `@RequestMapping`：类级别请求映射
- `@GetMapping/@PostMapping/@PutMapping/@DeleteMapping/@PatchMapping`：方法级别请求映射
- `@RequestParam`：请求参数
- `@Component`：通用组件
- `@Configuration`：配置类
- `@ComponentScan`：组件扫描

#### Lombok注解
- `@Data`：自动生成getter/setter/toString/equals/hashCode
- `@Slf4j`：日志
- `@RequiredArgsConstructor`：构造器注入

#### 测试注解
- `@SpringBootTest`：Spring Boot集成测试
- `@Test`：JUnit 5测试方法

### 异常处理
- 使用try-catch-finally处理异常
- 记录错误信息到System.err或日志
- 不要在finally块中抛出异常
- 适当处理检查型异常

### 资源文件
- Spring Boot配置使用application.properties或application.yml
- 日志配置使用logback-spring.xml（如需要）
- 测试资源放在src/test/resources目录

### 测试规范
- 测试类命名：{ClassName}Tests
- 测试方法命名：{scenario}或{method}_{expectedBehavior}
- 使用@SpringBootTest进行集成测试
- 测试方法简单验证功能，不需要复杂断言

### 特定模式
- Spring Boot应用主类必须包含main方法调用SpringApplication.run()
- REST控制器返回Map<String, Object>或自定义对象
- 使用HttpSession时通过方法参数注入
- 资源加载使用ResourceLoader或Resource接口
- 使用try-with-resources管理IO资源

### 禁止事项
- 不要使用System.out进行生产日志（使用日志框架）
- 不要在代码中硬编码敏感信息（使用配置文件）
- 不要忽略异常（至少记录日志）
- 不要使用过时的API（优先使用新的Spring API）

### 项目结构
```
sampleXX-{module-name}/
├── src/
│   ├── main/
│   │   ├── java/io/github/daihaowxg/{module_name}/
│   │   │   ├── controller/
│   │   │   ├── config/
│   │   │   ├── service/
│   │   │   └── {ModuleName}Application.java
│   │   └── resources/
│   │       └── application.{properties|yml}
│   └── test/
│       ├── java/io/github/daihaowxg/{module_name}/
│       │   └── {ModuleName}ApplicationTests.java
│       └── resources/
└── pom.xml
```

### IDE配置
- VSCode配置了自动编译和空值分析
- Java版本：21
- Maven自动更新构建配置
