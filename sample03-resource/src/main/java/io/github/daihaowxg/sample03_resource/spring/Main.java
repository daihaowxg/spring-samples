package io.github.daihaowxg.sample03_resource.spring;

/**
 * Spring Resource 访问综合演示
 * 
 * 本演示展示了 Spring Resource 提供的统一、简化的资源访问方式。
 * 相比 JDK 原生 API，Spring Resource 的优势：
 * 1. 统一接口：无论是文件、类路径还是网络资源，都使用 Resource 接口。
 * 2. 自动识别：通过 ResourceLoader 配合前缀（classpath:, file:, http:）自动选择实现。
 * 3. 使用便捷：提供了丰富的方法如 exists(), isReadable(), getFilename() 等。
 */
public class Main {

    public static void main(String[] args) {
        runAllDemonstrations();
    }

    public static void runAllDemonstrations() {
        printHeader();

        // 1. 演示 Resource 基础用法 (不同实现类)
        SpringResourceAccessExample.demonstrateResourceBasics();

        // 2. 演示 ResourceLoader (策略模式识别前缀)
        ResourceLoaderExample.demonstrateResourceLoader();

        printSummary();
    }

    private static void printHeader() {
        System.out.println("\n");
        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                                ║");
        System.out.println("║           Spring Resource 资源访问方式综合演示                ║");
        System.out.println("║                                                                ║");
        System.out.println("║  本演示展示了 Spring 提供的统一资源访问抽象：                  ║");
        System.out.println("║  1. Resource 接口     - 统一的资源访问抽象                     ║");
        System.out.println("║  2. ResourceLoader    - 自动识别前缀的资源加载器               ║");
        System.out.println("║                                                                ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        System.out.println();
    }

    private static void printSummary() {
        System.out.println("\n");
        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                                ║");
        System.out.println("║                  Spring Resource 访问总结                      ║");
        System.out.println("║                                                                ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        System.out.println();

        System.out.println("【为什么使用 Spring Resource？】");
        System.out.println();

        System.out.println("1. 统一的抽象接口");
        System.out.println("   - 屏蔽了底层资源的物理差异（文件、类路径、URL 等）。");
        System.out.println("   - 开发者只需要面向 Resource 接口编程，代码更通用。");
        System.out.println();

        System.out.println("2. 强大的 ResourceLoader 策略");
        System.out.println("   - 支持常见的协议前缀：classpath:, file:, http:, ftp: 等。");
        System.out.println("   - 应用代码不依赖具体的资源加载逻辑，耦合度更低。");
        System.out.println();

        System.out.println("3. 丰富的元数据支持");
        System.out.println("   - 提供了 exists(), isReadable(), isOpen(), getDescription() 等实用方法。");
        System.out.println("   - 解决了 JDK 中某些资源存在性检查困难的问题。");
        System.out.println();

        System.out.println("4. 简化了 IO 代码");
        System.out.println("   - Resource.getInputStream() 取代了繁琐的手动流打开操作。");
        System.out.println("   - 配合 Spring 的 ResourceUtils 或 EncodedResource 使用更佳。");
        System.out.println();

        System.out.println("════════════════════════════════════════════════════════════════");
        System.out.println();
    }
}
