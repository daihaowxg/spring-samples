package io.github.daihaowxg.sample03_resource.spring;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.lang.NonNull;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Spring ResourceLoader 用法示例
 * 
 * 演示了 ResourceLoader 的强大之处：
 * 只需要提供资源路径字符串，ResourceLoader 会根据前缀自动识别并创建对应的 Resource 实现。
 */
public class ResourceLoaderExample {

    public static void demonstrateResourceLoader() {
        System.out.println("--- 2. ResourceLoader 策略模式演示 ---");

        ResourceLoader resourceLoader = new DefaultResourceLoader();

        // 1. 使用 classpath: 前缀 -> 自动创建 ClassPathResource
        loadAndDisplay(resourceLoader, "classpath:application.properties", "类路径资源");

        // 2. 使用 file: 前缀 -> 自动创建 FileUrlResource (底层依然是文件访问)
        loadAndDisplay(resourceLoader, "file:pom.xml", "文件系统资源");

        // 3. 使用 http: 前缀 -> 自动创建 UrlResource
        loadAndDisplay(resourceLoader, "https://www.baidu.com", "网络资源");

        // 4. 不带前缀 -> 默认取决于 ResourceLoader 的实现 (DefaultResourceLoader 默认为 classpath)
        loadAndDisplay(resourceLoader, "application.properties", "无前缀资源 (默认为 classpath)");

        System.out.println();
    }

    /**
     * 加载并显示资源信息
     *
     * @param loader   ResourceLoader 实例
     * @param location 资源路径
     * @param label    资源描述标签
     */
    private static void loadAndDisplay(ResourceLoader loader, @NonNull String location, @NonNull String label) {
        Resource resource = loader.getResource(location);
        System.out.println("路径: [" + location + "] -> 识别类型: " + resource.getClass().getSimpleName());
        System.out.println("描述: " + label);
        System.out.println("资源是否存在: " + resource.exists());

        if (resource.exists()) {
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
                String firstLine = reader.readLine();
                System.out.println("内容预览: " + (firstLine != null ? firstLine : "空文件"));
            } catch (Exception e) {
                System.out.println("读取失败: " + e.getMessage());
            }
        }
        System.out.println("------------------------------------");
    }
}
