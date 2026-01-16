package io.github.daihaowxg._06_spring_environment._01_properties_data;

import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;

import java.util.HashMap;
import java.util.Map;

/**
 * 演示 {@link PropertySource} 及其在 {@link MutablePropertySources} 中的优先级逻辑。
 */
public class MapPropertySourceDemo {

    public static void main(String[] args) {
        System.out.println("=== MapPropertySource 优先级演示 ===");

        // 1. 创建两个 Map 数据源
        Map<String, Object> map1 = new HashMap<>();
        map1.put("app.name", "spring-samples-low");
        map1.put("app.version", "1.0");

        Map<String, Object> map2 = new HashMap<>();
        map2.put("app.name", "spring-samples-high"); // 同名 key，优先级更高

        // 2. 包装为 PropertySource
        PropertySource<?> sourceLow = new MapPropertySource("source-low", map1);
        PropertySource<?> sourceHigh = new MapPropertySource("source-high", map2);

        // 3. 使用 MutablePropertySources 管理多个源
        MutablePropertySources propertySources = new MutablePropertySources();

        // 默认 addLast，放在列表末尾
        propertySources.addLast(sourceLow);
        System.out.println("添加 source-low 后 app.name: " + getProperty(propertySources, "app.name"));

        // addFirst，抢占最高优先级
        propertySources.addFirst(sourceHigh);
        System.out.println("添加 source-high 到最前面后 app.name: " + getProperty(propertySources, "app.name"));

        // 4. 遍历查看所有属性源
        System.out.println("\n当前属性源列表（按优先级排序）：");
        for (PropertySource<?> ps : propertySources) {
            System.out.println(" - " + ps.getName());
        }
    }

    /**
     * 模拟 Environment 的查找逻辑：按顺序遍历所有 PropertySource
     */
    private static Object getProperty(MutablePropertySources sources, String key) {
        for (PropertySource<?> source : sources) {
            Object value = source.getProperty(key);
            if (value != null) {
                return value;
            }
        }
        return null;
    }
}
