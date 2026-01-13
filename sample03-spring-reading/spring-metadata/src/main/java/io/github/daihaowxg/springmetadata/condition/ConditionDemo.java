package io.github.daihaowxg.springmetadata.condition;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.MapPropertySource;

import java.util.Map;

/**
 * Condition 接口用法演示。
 * <p>
 * Condition 是 Spring 条件化 Bean 注册的核心接口。
 * 通过 @Conditional 注解将 Condition 与 Bean 定义关联。
 * <p>
 * 核心方法：
 * {@code boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata)}
 * - context: 提供 BeanFactory、Environment、ClassLoader、ResourceLoader 等
 * - metadata: 提供被 @Conditional 标注的类/方法的注解信息
 * <p>
 * Spring Boot 内置了大量 Condition 实现：
 * - @ConditionalOnProperty: 根据属性值判断
 * - @ConditionalOnClass: 根据类是否存在判断
 * - @ConditionalOnBean: 根据 Bean 是否存在判断
 * - @ConditionalOnMissingBean: 根据 Bean 是否不存在判断
 * - @ConditionalOnWebApplication: 根据是否为 Web 应用判断
 */
public class ConditionDemo {

    public static void main(String[] args) {
        System.out.println("=== Condition 接口用法演示 ===\n");

        // 1️⃣ 演示 @Conditional 基本用法
        System.out.println("【1. @Conditional 基本用法 - OnMacOsCondition】");
        demoBasicCondition();

        // 2️⃣ 演示自定义条件注解
        System.out.println("\n【2. 自定义条件注解 - @ConditionalOnMyProperty】");
        demoCustomConditionalAnnotation();

        // 3️⃣ 演示 ConditionContext 的能力
        System.out.println("\n【3. ConditionContext 提供的能力】");
        demoConditionContext();

        System.out.println("\n=== 演示结束 ===");
    }

    /**
     * 演示 @Conditional 基本用法
     */
    private static void demoBasicCondition() {
        try (var context = new AnnotationConfigApplicationContext(MacOsConfig.class)) {
            // 检查 Bean 是否被注册
            boolean hasBean = context.containsBean("macOsOnlyService");
            System.out.println("macOsOnlyService Bean 是否存在: " + hasBean);

            if (hasBean) {
                String service = context.getBean("macOsOnlyService", String.class);
                System.out.println("Bean 值: " + service);
            }
        }
    }

    /**
     * 演示自定义条件注解
     */
    private static void demoCustomConditionalAnnotation() {

        // 这里的三个 context 彼此之间是隔离的

        // 场景 1：属性存在且匹配
        System.out.println("\n场景 1：设置 feature.enabled=true");
        try (var context = new AnnotationConfigApplicationContext()) {
            // 添加自定义属性
            context.getEnvironment().getPropertySources().addFirst(
                    new MapPropertySource("test", Map.of("feature.enabled", "true")));
            context.register(PropertyConditionConfig.class);
            context.refresh();
            System.out.println("featureService Bean 是否存在: " + context.containsBean("featureService"));
        }

        // 场景 2：属性存在但不匹配
        System.out.println("\n场景 2：设置 feature.enabled=false");
        try (var context = new AnnotationConfigApplicationContext()) {
            context.getEnvironment().getPropertySources().addFirst(
                    new MapPropertySource("test", Map.of("feature.enabled", "false")));
            context.register(PropertyConditionConfig.class);
            context.refresh();
            System.out.println("featureService Bean 是否存在: " + context.containsBean("featureService"));
        }

        // 场景 3：属性不存在
        System.out.println("\n场景 3：属性 feature.enabled 不存在");
        try (var context = new AnnotationConfigApplicationContext()) {
            context.register(PropertyConditionConfig.class);
            context.refresh();
            System.out.println("featureService Bean 是否存在: " + context.containsBean("featureService"));
        }
    }

    /**
     * 演示 ConditionContext 的能力
     */
    private static void demoConditionContext() {
        System.out.println("""
                ConditionContext 提供以下能力：

                1. getRegistry()      -> BeanDefinitionRegistry
                   - 检查/注册 BeanDefinition

                2. getBeanFactory()   -> ConfigurableListableBeanFactory
                   - 检查 Bean 是否已存在
                   - 获取 Bean 实例

                3. getEnvironment()   -> Environment
                   - 读取配置属性
                   - 检查 Profile

                4. getResourceLoader() -> ResourceLoader
                   - 加载资源文件

                5. getClassLoader()   -> ClassLoader
                   - 检查类是否存在（@ConditionalOnClass 的实现基础）
                """);
    }

    // ========== 配置类 ==========

    /**
     * 演示 @Conditional 的配置类
     */
    @Configuration
    static class MacOsConfig {

        @Bean
        @Conditional(OnMacOsCondition.class) // 只在 macOS 上注册
        public String macOsOnlyService() {
            return "This service runs on macOS";
        }
    }

    /**
     * 演示自定义条件注解的配置类
     */
    @Configuration
    static class PropertyConditionConfig {

        @Bean
        @ConditionalOnMyProperty(value = "feature.enabled", havingValue = "true") // 当属性 feature.enabled 存在且值为 true 时注册
        public String featureService() {
            return "Feature is enabled";
        }
    }
}
