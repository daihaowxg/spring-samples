package io.github.daihaowxg._06_spring_environment._01_properties_data;

import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;

import java.util.HashMap;
import java.util.Map;

/**
 * 演示 {@link MutablePropertySources} 的 API 操作及其对优先级的实时影响。
 *
 * <p>核心演示点：
 * <ul>
 *     <li>addFirst/addLast: 头尾插入</li>
 *     <li>addBefore/addAfter: 相对位置插入</li>
 *     <li>replace: 替换现有源</li>
 *     <li>remove: 移除源</li>
 * </ul>
 */
public class MutablePropertySourcesDemo {

    public static void main(String[] args) {
        // 准备三个数据源
        MapPropertySource p1 = createSource("p1", "value-1");
        MapPropertySource p2 = createSource("p2", "value-2");
        MapPropertySource p3 = createSource("p3", "value-3");

        MutablePropertySources sources = new MutablePropertySources();

        // 1. 基础添加 (LIFO 栈式 vs FIFO 队列式)
        sources.addFirst(p1); // [p1]
        System.out.println("1. addFirst(p1) -> " + sources);
        
        sources.addLast(p3);  // [p1, p3]
        System.out.println("2. addLast(p3)  -> " + sources);

        // 2. 相对位置插入
        sources.addBefore("p3", p2); // [p1, p2, p3]
        System.out.println("3. addBefore(p3, p2) -> " + sources);

        // 验证优先级：获取 "key" 属性，应返回排在最前面的 p1 的值
        System.out.println("   当前优先级最高的是: " + sources.iterator().next().getName());
        System.out.println("   获取 'key' = " + getProperty(sources, "key"));

        // 3. 替换操作 (保持顺序，改变内容)
        MapPropertySource pNew = createSource("p1", "value-1-new"); // 同名
        sources.replace("p1", pNew); 
        System.out.println("4. replace(p1, pNew) -> " + sources);
        System.out.println("   获取 'key' = " + getProperty(sources, "key"));

        // 4. 移除操作
        sources.remove("p1"); // [p2, p3]
        System.out.println("5. remove(p1)   -> " + getNames(sources));
        System.out.println("   p1被移除后，p2上位，获取 'key' = " + getProperty(sources, "key"));
    }

    private static MapPropertySource createSource(String name, String value) {
        Map<String, Object> map = new HashMap<>();
        map.put("key", value); // 所有源都包含同名 key，用于测试优先级
        map.put("origin", name);
        return new MapPropertySource(name, map);
    }

    private static String getProperty(MutablePropertySources sources, String key) {
        for (PropertySource<?> ps : sources) {
            if (ps.containsProperty(key)) {
                return ps.getProperty(key).toString();
            }
        }
        return "null";
    }

    private static String getNames(MutablePropertySources sources) {
        StringBuilder sb = new StringBuilder("[");
        for (PropertySource<?> ps : sources) {
            sb.append(ps.getName()).append(", ");
        }
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 2);
        }
        sb.append("]");
        return sb.toString();
    }
}
