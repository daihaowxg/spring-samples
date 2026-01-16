package io.github.daihaowxg.sample03_resource._02_resource_interface;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;

/**
 * UrlResource 演示
 * <p>
 * UrlResource 用于访问基于 URL 的资源（HTTP、HTTPS、FTP 等）。
 * <p>
 * <b>特点：</b>
 * <ul>
 * <li>支持多种网络协议</li>
 * <li>底层使用 java.net.URL</li>
 * <li>适合访问远程资源</li>
 * </ul>
 */
public class UrlResourceDemo {

    public static void main(String[] args) {
        showDemo();
    }

    public static void showDemo() {
        System.out.println("=== UrlResource 演示 ===");

        try {
            // 创建 UrlResource
            Resource resource = new UrlResource("https://www.baidu.com");

            System.out.println("资源路径: https://www.baidu.com");
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
                    System.out.println("读取资源内容失败（网络问题）: " + e.getMessage());
                }
            }

        } catch (MalformedURLException e) {
            System.err.println("创建 UrlResource 失败: " + e.getMessage());
        }

        System.out.println();
    }
}
