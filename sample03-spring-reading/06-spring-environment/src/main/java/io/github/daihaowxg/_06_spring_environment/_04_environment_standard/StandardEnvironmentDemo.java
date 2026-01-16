package io.github.daihaowxg._06_spring_environment._04_environment_standard;

import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;

/**
 * 演示 {@link StandardEnvironment} 默认集成的属性源。
 */
public class StandardEnvironmentDemo {

    public static void main(String[] args) {
        System.out.println("=== StandardEnvironment 默认源演示 ===");

        // 1. 创建标准环境
        StandardEnvironment env = new StandardEnvironment();

        // 2. 查看默认加载的属性源（无需手动配置）
        System.out.println("默认属性源列表：");
        for (PropertySource<?> source : env.getPropertySources()) {
            System.out.println(" - " + source.getName() + " (" + source.getClass().getSimpleName() + ")");
        }

        // 3. 访问 JVM 系统属性 (systemProperties)
        // 这些属性通常通过 java -Dkey=value 传入，或者 System.getProperties()
        System.out.println("\nJVM 系统属性示例:");
        System.out.println("  java.version: " + env.getProperty("java.version"));
        System.out.println("  os.name: " + env.getProperty("os.name"));
        System.out.println("  user.dir: " + env.getProperty("user.dir"));

        // 4. 访问 OS 环境变量 (systemEnvironment)
        // 这些属性是操作系统的环境变量，可以通过 export 命令行设置，或者 System.getenv()
        System.out.println("\nOS 环境变量示例:");
        System.out.println("  PATH: " + truncate(env.getProperty("PATH"), 50));
        System.out.println("  HOME: " + env.getProperty("HOME"));

        // 5. 优先级验证：JVM 属性 > OS 环境变量
        // 演示：如果两者存在同名 key，JVM 属性会覆盖环境变量
        System.out.println("\n优先级说明: StandardEnvironment 中 'systemProperties' 优先级高于 'systemEnvironment'。");
    }

    private static String truncate(String text, int max) {
        if (text == null || text.length() <= max) {
            return text;
        }
        return text.substring(0, max) + "...";
    }
}
