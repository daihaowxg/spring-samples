package io.github.daihaowxg.sample03_resource.spring;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Spring Resource 基础用法示例
 * 
 * 演示了 Resource 接口的不同实现类：
 * 1. FileSystemResource - 物理文件系统资源
 * 2. ClassPathResource - 类路径资源
 * 3. UrlResource - 网络/URL 资源
 */
public class SpringResourceAccessExample {

    public static void demonstrateResourceBasics() {
        System.out.println("--- 1. Spring Resource 基础用法演示 ---");

        // 1. ClassPathResource - 访问类路径资源
        // 相比 JDK ClassLoader，路径处理更直观
        Resource classPathResource = new ClassPathResource("application.properties");
        displayResourceInfo("ClassPathResource (classpath)", classPathResource);

        // 2. FileSystemResource - 访问文件系统资源
        // 这里的路径需要根据实际环境调整，通常可以使用项目根录下的文件
        Resource fileResource = new FileSystemResource("pom.xml");
        displayResourceInfo("FileSystemResource (file system)", fileResource);

        // 3. UrlResource - 访问网络资源
        try {
            Resource urlResource = new UrlResource("https://www.baidu.com");
            displayResourceInfo("UrlResource (http)", urlResource);
        } catch (Exception e) {
            System.err.println("创建 UrlResource 失败: " + e.getMessage());
        }

        System.out.println();
    }

    private static void displayResourceInfo(String type, Resource resource) {
        System.out.println("【" + type + "】");
        System.out.println("资源是否存在: " + resource.exists());
        System.out.println("资源是否可读: " + resource.isReadable());
        System.out.println("文件名: " + resource.getFilename());
        System.out.println("描述信息: " + resource.getDescription());

        if (resource.exists() && resource.isReadable()) {
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
                System.out.print("内容预览 (前 2 行): ");
                String line;
                int count = 0;
                while ((line = reader.readLine()) != null && count < 2) {
                    System.out.println(line);
                    count++;
                }
            } catch (Exception e) {
                System.out.println("读取资源内容失败: " + e.getMessage());
            }
        }
        System.out.println("------------------------------------");
    }
}
