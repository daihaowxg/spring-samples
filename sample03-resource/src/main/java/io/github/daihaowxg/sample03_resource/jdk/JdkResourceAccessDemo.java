package io.github.daihaowxg.sample03_resource.jdk;

/**
 * JDK 资源访问综合演示
 * 
 * 这个类整合了所有 JDK 资源访问方式的演示，展示了：
 * 1. java.io.File - 文件系统访问
 * 2. ClassLoader - classpath 资源访问
 * 3. java.net.URL - 统一资源定位符访问
 * 
 * 通过这些示例，我们可以看到 JDK 原生资源访问的问题：
 * - 不同类型的资源需要使用不同的 API
 * - 代码复杂性高，需要了解多种访问方式
 * - 路径处理规则不统一
 * - 错误处理方式各异
 * 
 * 这正是 Spring Resource 接口要解决的问题：
 * 提供统一、简化的资源访问抽象
 */
public class JdkResourceAccessDemo {

    public static void main(String[] args) {
        runAllDemonstrations();
    }

    public static void runAllDemonstrations() {
        printHeader();

        // 1. File 访问演示
        JdkFileAccessExample.demonstrateFileAccess();

        // 2. ClassLoader 访问演示
        JdkClassLoaderAccessExample.demonstrateClassLoaderAccess();

        // 3. URL 访问演示
        JdkUrlAccessExample.demonstrateUrlAccess();

        printSummary();
    }

    private static void printHeader() {
        System.out.println("\n");
        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                                ║");
        System.out.println("║           JDK 资源访问方式综合演示                              ║");
        System.out.println("║                                                                ║");
        System.out.println("║  本演示展示了 JDK 提供的三种主要资源访问方式：                  ║");
        System.out.println("║  1. java.io.File      - 文件系统访问                           ║");
        System.out.println("║  2. ClassLoader       - classpath 资源访问                     ║");
        System.out.println("║  3. java.net.URL      - 统一资源定位符访问                      ║");
        System.out.println("║                                                                ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        System.out.println();
    }

    private static void printSummary() {
        System.out.println("\n");
        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                                ║");
        System.out.println("║                    JDK 资源访问总结                             ║");
        System.out.println("║                                                                ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        System.out.println();

        System.out.println("【存在的问题】");
        System.out.println();

        System.out.println("1. API 不统一");
        System.out.println("   - 文件系统资源使用 File");
        System.out.println("   - classpath 资源使用 ClassLoader");
        System.out.println("   - 网络资源使用 URL");
        System.out.println("   - 开发者需要记住多套 API");
        System.out.println();

        System.out.println("2. 代码复杂性高");
        System.out.println("   - 需要根据资源类型选择不同的访问方式");
        System.out.println("   - 大量重复的流处理代码");
        System.out.println("   - 异常处理逻辑分散");
        System.out.println();

        System.out.println("3. 路径处理混乱");
        System.out.println("   - File 使用文件系统路径");
        System.out.println("   - ClassLoader 使用相对于 classpath 的路径");
        System.out.println("   - Class.getResource 的路径规则与 ClassLoader 不同");
        System.out.println("   - URL 需要特定的协议前缀");
        System.out.println();

        System.out.println("4. 功能局限性");
        System.out.println("   - File 无法访问 JAR 包内资源");
        System.out.println("   - ClassLoader 无法访问文件系统任意位置");
        System.out.println("   - 缺少统一的资源元数据访问接口");
        System.out.println();

        System.out.println("5. 可移植性差");
        System.out.println("   - 文件路径依赖于操作系统");
        System.out.println("   - 工作目录的变化会影响相对路径");
        System.out.println("   - 开发环境和生产环境（JAR）行为不一致");
        System.out.println();

        System.out.println("【Spring Resource 的解决方案】");
        System.out.println();
        System.out.println("Spring 框架通过 Resource 接口提供了统一的资源访问抽象：");
        System.out.println();
        System.out.println("✓ 统一的 API - 所有资源使用相同的接口");
        System.out.println("✓ 简化的代码 - 隐藏底层实现细节");
        System.out.println("✓ 灵活的路径 - 支持多种路径前缀（classpath:, file:, http: 等）");
        System.out.println("✓ 丰富的功能 - 提供资源存在性检查、可读性检查等");
        System.out.println("✓ 良好的可移植性 - 在不同环境下行为一致");
        System.out.println();

        System.out.println("════════════════════════════════════════════════════════════════");
        System.out.println();
    }
}
