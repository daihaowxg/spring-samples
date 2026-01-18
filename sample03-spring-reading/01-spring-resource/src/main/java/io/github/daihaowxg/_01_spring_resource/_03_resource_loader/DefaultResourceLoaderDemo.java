package io.github.daihaowxg._01_spring_resource._03_resource_loader;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

/**
 * DefaultResourceLoader 示例
 * <p>
 * DefaultResourceLoader 是 ResourceLoader 的默认实现。
 * 它根据资源路径的前缀来决定如何加载资源：
 * 1. classpath: - 加载类路径下的资源
 * 2. / - 加载文件系统或上下文路径下的资源（取决于具体实现）
 * 3. http: / https: - 加载网络资源
 */
public class DefaultResourceLoaderDemo {

    public static void main(String[] args) {
        showDemo();
    }

    public static void showDemo() {
        System.out.println("=== DefaultResourceLoader 演示 ===");
        ResourceLoader loader = new DefaultResourceLoader();

        // 1. 加载类路径资源
        loadAndDisplay(loader, "classpath:application.properties");

        // 2. 加载文件系统资源 (使用 file: 前缀)
        loadAndDisplay(loader, "file:pom.xml");

        // 3. 加载网络资源
        loadAndDisplay(loader, "https://dist.apache.org/repos/dist/test/test.txt");

        System.out.println();
    }

    private static void loadAndDisplay(ResourceLoader loader, String location) {
        try {
            Resource resource = loader.getResource(location);
            System.out.printf("位置: %s%n", location);
            System.out.printf("资源实现类: %s%n", resource.getClass().getSimpleName());
            System.out.printf("是否存在: %b%n", resource.exists());

            if (resource.exists()) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
                    String content = reader.lines().limit(2).collect(Collectors.joining("\n"));
                    System.out.println("内容摘要: " + content);
                }
            }
        } catch (Exception e) {
            System.err.printf("加载资源 [%s] 失败: %s%n", location, e.getMessage());
        }
        System.out.println("--------------------");
    }
}
