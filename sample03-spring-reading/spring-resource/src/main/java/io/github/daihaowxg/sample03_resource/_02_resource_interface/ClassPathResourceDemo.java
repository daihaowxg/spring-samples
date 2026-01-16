package io.github.daihaowxg.sample03_resource._02_resource_interface;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * ClassPathResource 演示
 * <p>
 * ClassPathResource 用于访问类路径下的资源，相比 JDK 的 ClassLoader 方式更加直观。
 * <p>
 * <b>特点：</b>
 * <ul>
 * <li>路径相对于类路径根目录</li>
 * <li>支持 JAR 包内资源访问</li>
 * <li>Spring 推荐的类路径资源访问方式</li>
 * </ul>
 */
public class ClassPathResourceDemo {

    public static void main(String[] args) {
        showDemo();
    }

    public static void showDemo() {
        System.out.println("=== ClassPathResource 演示 ===");

        // 创建 ClassPathResource
        Resource resource = new ClassPathResource("application.properties");

        System.out.println("资源路径: classpath:application.properties");
        System.out.println("资源类型: " + resource.getClass().getSimpleName());
        System.out.println("是否存在: " + resource.exists());
        System.out.println("是否可读: " + resource.isReadable());
        System.out.println("文件名: " + resource.getFilename());
        System.out.println("描述信息: " + resource.getDescription());

        if (resource.exists() && resource.isReadable()) {
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
                System.out.println("内容预览 (前 3 行):");
                String line;
                int count = 0;
                while ((line = reader.readLine()) != null && count < 3) {
                    System.out.println("  " + line);
                    count++;
                }
            } catch (Exception e) {
                System.err.println("读取资源内容失败: " + e.getMessage());
            }
        }

        System.out.println();
    }
}
