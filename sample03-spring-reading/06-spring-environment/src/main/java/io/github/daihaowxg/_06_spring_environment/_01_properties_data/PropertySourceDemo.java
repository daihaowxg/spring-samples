package io.github.daihaowxg._06_spring_environment._01_properties_data;

import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.ResourcePropertySource;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * PropertySource 多种实现类演示
 * 
 * 演示 Map, Properties, 以及外部 Resource 如何被包装为属性源
 */
public class PropertySourceDemo {

    public static void main(String[] args) throws IOException {
        System.out.println("=== PropertySource 实现类全景演示 ===\n");

        // 1. MapPropertySource: 包装 Map
        Map<String, Object> map = new HashMap<>();
        map.put("user.name", "wxg");
        MapPropertySource mapSource = new MapPropertySource("map-source", map);
        printSource(mapSource);

        // 2. PropertiesPropertySource: 包装 Properties 对象
        Properties props = new Properties();
        props.setProperty("db.url", "jdbc:mysql://localhost:3306/mydb");
        PropertiesPropertySource propsSource = new PropertiesPropertySource("props-source", props);
        printSource(propsSource);

        // 3. ResourcePropertySource: 包装外部资源（如 .properties 文件）
        // 它内部实际上加载了资源并转换成了 Map 结构
        ResourcePropertySource resourceSource = new ResourcePropertySource("res-source", 
                new ClassPathResource("demo.properties"));
        printSource(resourceSource);

        // 4. 其他重要子类说明（无需手动实例化）
        // - SystemEnvironmentPropertySource: 专门用于 OS 环境变量，支持 getenv() 的宽松匹配。
        // - CommandLinePropertySource: 专门用于解析 --key=value 形式的命令行参数。
    }

    private static void printSource(org.springframework.core.env.PropertySource<?> ps) {
        System.out.println("源名称: " + ps.getName());
        System.out.println("源类型: " + ps.getClass().getSimpleName());
        // 获取所有定义的 key (如果是 EnumerablePropertySource)
        if (ps instanceof org.springframework.core.env.EnumerablePropertySource<?> eps) {
            for (String name : eps.getPropertyNames()) {
                System.out.println("  [Key]: " + name + " -> [Value]: " + ps.getProperty(name));
            }
        }
        System.out.println("-----------------------------------");
    }
}
