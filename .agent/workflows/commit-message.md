---
description: 生成符合 Conventional Commits 规范的 Git Commit Message（中文）
---

# Git Commit Message 生成器

## 前置条件

执行此 workflow 前，确保：
- 当前工作区有 Git 仓库
- 有待提交的变更（已暂存或未暂存）

---

## 执行步骤

### 步骤 1：定位 Git 仓库

根据用户当前打开的文件，自动定位其所属的 Git 仓库根目录：

```bash
# 获取当前文件所在目录的 Git 仓库根目录
git -C <当前文件所在目录> rev-parse --show-toplevel
```

**后续所有 Git 命令都必须在此仓库根目录下执行。**

---

### 步骤 2：检查变更状态

// turbo
```bash
git status --porcelain
```

根据输出判断：

| 情况 | 处理方式 |
|------|----------|
| 无任何变更 | 终止并提示「没有检测到变更，无需生成 commit message」 |
| 有未暂存变更，无已暂存变更 | 提示用户「检测到未暂存的变更，是否执行 `git add -A` 全部暂存？」，确认后执行 |
| 有已暂存变更（无论是否有未暂存变更） | 继续下一步，**仅处理已暂存的变更**，未暂存的变更不提交 |

---

### 步骤 3：获取已暂存的变更内容

// turbo
```bash
# 获取已暂存文件列表
git diff --cached --name-status

# 获取详细差异（用于分析变更内容）
git diff --cached
```

---

### 步骤 4：分析变更并确定 Type

根据变更的文件和内容，智能判断 commit type：

| Type       | 判断依据 |
|------------|----------|
| `feat`     | 新增功能代码、新增 API 接口、新增业务逻辑 |
| `fix`      | 修复 bug、修正错误逻辑、异常处理修复 |
| `docs`     | 仅修改 `.md`、`README`、注释、API 文档 |
| `style`    | 代码格式化、空格、缩进、分号（不影响逻辑） |
| `refactor` | 重构代码、提取方法、重命名变量（非 feat/fix） |
| `perf`     | 性能优化、缓存优化、SQL 优化 |
| `test`     | 新增或修改测试代码 |
| `build`    | 修改 `pom.xml`、`build.gradle`、`package.json`、构建脚本 |
| `ci`       | 修改 CI/CD 配置（`.github/workflows`、`Jenkinsfile`） |
| `chore`    | 其他杂项（`.gitignore`、IDE 配置、工具脚本） |
| `revert`   | 回滚之前的 commit |

**如果变更涉及多种类型，选择最主要的类型。**

---

### 步骤 5：确定 Scope

基于变更文件的目录结构，自动推荐 scope：

1. 如果所有变更文件都在同一个模块/目录下，使用该目录名作为 scope
2. 如果是多模块项目（如 Maven 多模块），使用子模块名
3. 如果变更跨越多个不相关模块，scope 可省略

**常见 scope 示例**：
- `auth`：认证授权模块
- `user`：用户模块
- `api`：API 接口层
- `dao`：数据访问层
- `config`：配置相关
- `deps`：依赖更新

---

### 步骤 6：生成 Subject

用一句话简洁描述本次变更的内容：

- **语言**：中文
- **长度**：建议不超过 50 个字符
- **格式**：动词开头，如「添加」「修复」「优化」「重构」「更新」
- **不要**：以句号结尾

---

### 步骤 7：智能判断是否需要 Body

根据以下条件判断是否需要生成 body：

| 条件 | 是否生成 Body |
|------|---------------|
| 变更文件数 ≥ 5 | 是 |
| diff 行数 ≥ 100 | 是 |
| 涉及核心业务逻辑修改 | 是 |
| 涉及 API 变更 | 是 |
| 简单的配置修改、依赖更新 | 否 |
| 文档修改 | 否 |

**Body 格式**：
- 解释「为什么」做这个变更
- 描述「做了什么」核心改动
- 每行不超过 72 个字符

---

### 步骤 8：检测 Breaking Change

如果检测到以下情况，自动在 footer 添加 `BREAKING CHANGE:` 标记：

- 删除或重命名 public 方法/类
- 修改 API 接口签名（参数、返回值）
- 修改配置项名称或格式
- 移除已废弃（@Deprecated）的代码
- 数据库表结构变更

**Footer 格式**：
```
BREAKING CHANGE: <描述破坏性变更的影响>
```

---

### 步骤 9：组装并输出 Commit Message

按照 Conventional Commits 规范组装最终的 commit message：

**精简格式（无 body）**：
```
<type>(<scope>): <subject>
```

**完整格式（有 body）**：
```
<type>(<scope>): <subject>

<body>

<footer>
```

---

### 步骤 10：展示结果

将生成的 commit message 展示给用户：

1. 使用代码块格式展示，方便复制
2. 简要说明为什么选择这个 type 和 scope
3. 如果有 Breaking Change，特别提醒用户注意

**不要自动执行 `git commit`，由用户自行决定是否使用。**

---

## 输出示例

### 示例 1：简单变更

```
build(deps): 升级 Spring Boot 版本至 3.2.0
```

### 示例 2：功能开发

```
feat(auth): 添加 JWT Token 自动刷新功能

- 在 Token 过期前 5 分钟自动触发刷新
- 新增 RefreshTokenFilter 拦截器
- 配置项 jwt.refresh-threshold 控制刷新阈值
```

### 示例 3：Breaking Change

```
refactor(api): 重构用户查询接口参数结构

将 GET /api/users 的查询参数从 URL params 改为 RequestBody

BREAKING CHANGE: 前端需要修改调用方式，将 URL 参数改为 JSON body
```

---

## Type 速查表

| Type       | Emoji | 中文说明               |
|------------|-------|------------------------|
| `feat`     | ✨    | 新功能                 |
| `fix`      | 🐛    | Bug 修复               |
| `docs`     | 📝    | 文档                   |
| `style`    | 💄    | 代码格式               |
| `refactor` | ♻️    | 重构                   |
| `perf`     | ⚡    | 性能优化               |
| `test`     | ✅    | 测试                   |
| `build`    | 📦    | 构建/依赖              |
| `ci`       | 👷    | CI/CD                  |
| `chore`    | 🔧    | 杂项                   |
| `revert`   | ⏪    | 回滚                   |
