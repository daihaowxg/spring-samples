# ResourcePatternResolver 与 ResourceLoader 的区别

> 最后更新时间：2026-01-07

## 1. 核心定位
*   **ResourceLoader**: “点对点”的精确查找。就像根据经纬度找一个明确的目的地。
*   **ResourcePatternResolver**: “网状”的模糊匹配。就像在地毯式搜索一片区域。

## 2. 核心功能对比

| 特性 | ResourceLoader | ResourcePatternResolver |
| :--- | :--- | :--- |
| **返回结果** | 单个 `Resource` | **数组 `Resource[]`** |
| **匹配方式** | 仅支持精确路径 | 支持 **Ant 风格** 通配符 (如 `**/*.xml`) |
| **杀手锏** | 基础资源加载 | 支持 **`classpath*:`** 前缀 |

## 3. 为什么需要 PatternResolver？

### A. 从“找一个”到“找一群”
`ResourceLoader` 的 `getResource` 方法只能返回一个资源。当你需要扫描某个包下所有的 XML 文件或所有的配置文件时，单个返回值无法满足需求。而 `ResourcePatternResolver` 提供的 `getResources` 专门用于批量加载。

### B. 突破隔离的 `classpath*:`
*   **普通 `classpath:`**: 只在类路径下寻找第一个匹配的资源。
*   **特有 `classpath*:`**: 扫描所有 Jar 包和目录，返回所有同名的资源。这对于加载多模块项目中的碎片化配置（如 MyBatis Mapper）至关重要。

## 4. 总结
如果你清楚资源的**确切位置**，使用 `ResourceLoader` 性能更好；如果你需要**搜索、扫描或批量处理**符合某种规则的资源，则必须使用 `ResourcePatternResolver`。
