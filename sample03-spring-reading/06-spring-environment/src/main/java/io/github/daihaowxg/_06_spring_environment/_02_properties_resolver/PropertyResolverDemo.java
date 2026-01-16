package io.github.daihaowxg._06_spring_environment._02_properties_resolver;

import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertyResolver;
import org.springframework.core.env.PropertySourcesPropertyResolver;

import java.util.HashMap;
import java.util.Map;

/**
 * 演示 {@link PropertyResolver} 的占位符解析和基本属性访问功能。
 */
public class PropertyResolverDemo {

    public static void main(String[] args) {
        System.out.println("=== PropertyResolver 占位符解析演示 ===");

        // 1. 准备原始数据
        Map<String, Object> map = new HashMap<>();
        map.put("user.home", "/Users/spring");
        map.put("app.name", "spring-demo");
        map.put("app.description", "Welcome to ${app.name} at ${user.home}");

        // 2. 构造 PropertyResolver (通常使用 PropertySourcesPropertyResolver)
        MutablePropertySources propertySources = new MutablePropertySources();
        propertySources.addLast(new MapPropertySource("my-map", map));

        PropertyResolver resolver = new PropertySourcesPropertyResolver(propertySources);

        // 3. 基本属性访问
        System.out.println("app.name: " + resolver.getProperty("app.name"));
        
        // 4. 解析包含占位符的属性
        // 注意：PropertyResolver 会递归地解析 ${...}
        String description = resolver.getProperty("app.description");
        System.out.println("app.description (解析前): ${app.name} at ${user.home}");
        System.out.println("app.description (解析后): " + description);

        // 5. 显式解析占位符
        String rawText = "Check this: ${unknown.key:Default Value}";
        String resolvedText = resolver.resolvePlaceholders(rawText);
        System.out.println("\n显式解析占位符 (带默认值):");
        System.out.println("  Raw: " + rawText);
        System.out.println("  Resolved: " + resolvedText);

        // 6. 类型转换演示 (PropertyResolver 默认支持基本类型转换)
        map.put("app.port", "8080");
        Integer port = resolver.getProperty("app.port", Integer.class);
        System.out.println("\n类型转换演示:");
        System.out.println("  app.port (String -> Integer): " + port);
    }
}
