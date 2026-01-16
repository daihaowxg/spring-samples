package io.github.daihaowxg._01_spring_resource._03_resource_loader;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * ApplicationContext 作为 ResourceLoader 示例
 * <p>
 * ApplicationContext 接口继承自 ResourcePatternResolver，
 * 而 ResourcePatternResolver 又继承自 ResourceLoader。
 * 因此，ApplicationContext 本身就是一个完整的 ResourceLoader 实现。
 * <p>
 * <b>继承关系：</b>
 * 
 * <pre>
 * ResourceLoader
 *     ↓
 * ResourcePatternResolver
 *     ↓
 * ApplicationContext
 * </pre>
 * <p>
 * <b>使用方式对比：</b>
 * <ul>
 * <li>方式一：直接使用 ApplicationContext.getResource()</li>
 * <li>方式二：通过 ResourceLoaderAware 注入（见 ResourceLoaderAwareDemo）</li>
 * <li>方式三：@Autowired 注入 ResourceLoader（推荐）</li>
 * </ul>
 */
@Configuration
public class ApplicationContextAsResourceLoaderDemo {

    public static void main(String[] args) {
        System.out.println("=== ApplicationContext 作为 ResourceLoader 演示 ===\n");

        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                ApplicationContextAsResourceLoaderDemo.class)) {

            // 1. 直接使用 ApplicationContext 加载资源
            demonstrateDirectUsage(context);

            // 2. 使用模式匹配加载多个资源
            demonstratePatternMatching(context);

            // 3. 对比不同获取方式
            demonstrateComparison(context);
        }
    }

    /**
     * 演示直接使用 ApplicationContext 加载资源
     */
    private static void demonstrateDirectUsage(ApplicationContext context) {
        System.out.println("--- 1. 直接使用 ApplicationContext.getResource() ---");

        // ApplicationContext 可以直接作为 ResourceLoader 使用
        Resource resource = context.getResource("classpath:sample.txt");

        System.out.println("资源路径: classpath:sample.txt");
        System.out.println("资源类型: " + resource.getClass().getSimpleName());
        System.out.println("是否存在: " + resource.exists());

        if (resource.exists()) {
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
                String content = reader.lines().limit(3).collect(Collectors.joining("\n"));
                System.out.println("内容预览:\n" + content);
            } catch (IOException e) {
                System.err.println("读取失败: " + e.getMessage());
            }
        }
        System.out.println();
    }

    /**
     * 演示模式匹配加载多个资源
     */
    private static void demonstratePatternMatching(ApplicationContext context) {
        System.out.println("--- 2. 使用 getResources() 加载多个资源 ---");

        try {
            // ApplicationContext 继承了 ResourcePatternResolver，支持通配符
            Resource[] resources = context.getResources("classpath*:*.txt");

            System.out.println("匹配 'classpath*:*.txt' 的资源:");
            for (Resource r : resources) {
                System.out.println("  - " + r.getFilename() + " [" + r.getClass().getSimpleName() + "]");
            }
        } catch (IOException e) {
            System.err.println("加载资源失败: " + e.getMessage());
        }
        System.out.println();
    }

    /**
     * 对比不同的 ResourceLoader 获取方式
     */
    private static void demonstrateComparison(ApplicationContext context) {
        System.out.println("--- 3. 获取 ResourceLoader 的三种方式对比 ---");

        System.out.println("""
                ┌─────────────────────────────────────────────────────────────────────┐
                │ 方式                        │ 适用场景                              │
                ├─────────────────────────────────────────────────────────────────────┤
                │ ApplicationContext 直接使用 │ 已持有 context 引用时最简单           │
                │                             │ 适合 main 方法、测试代码              │
                ├─────────────────────────────────────────────────────────────────────┤
                │ ResourceLoaderAware         │ 需要在 Bean 初始化时使用              │
                │                             │ 回调式注入，Spring 自动调用           │
                ├─────────────────────────────────────────────────────────────────────┤
                │ @Autowired ResourceLoader   │ 推荐方式，简洁且符合依赖注入原则      │
                │                             │ 解耦，便于测试                        │
                └─────────────────────────────────────────────────────────────────────┘
                """);

        // 验证 context 就是 ResourceLoader
        System.out.println(">>> 验证: ApplicationContext 的类层次结构");
        System.out.println("context instanceof ResourceLoader: " +
                (context instanceof org.springframework.core.io.ResourceLoader));
        System.out.println("context instanceof ResourcePatternResolver: " +
                (context instanceof org.springframework.core.io.support.ResourcePatternResolver));

        System.out.println("\n>>> 结论: ApplicationContext 天然就是一个功能完整的 ResourceLoader！");
    }
}
