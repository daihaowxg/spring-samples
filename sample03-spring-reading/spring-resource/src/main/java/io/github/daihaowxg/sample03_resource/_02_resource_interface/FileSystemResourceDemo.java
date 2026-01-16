package io.github.daihaowxg.sample03_resource._02_resource_interface;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * FileSystemResource 演示
 * <p>
 * FileSystemResource 用于访问文件系统中的资源。
 * <p>
 * <b>特点：</b>
 * <ul>
 * <li>支持绝对路径和相对路径</li>
 * <li>可获取底层 File 对象</li>
 * <li>适合访问服务器本地文件</li>
 * </ul>
 */
public class FileSystemResourceDemo {

    public static void main(String[] args) {
        showDemo();
    }

    public static void showDemo() {
        System.out.println("=== FileSystemResource 演示 ===");

        // 创建 FileSystemResource（相对于当前工作目录）
        Resource resource = new FileSystemResource("pom.xml");

        System.out.println("资源路径: pom.xml (相对路径)");
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
