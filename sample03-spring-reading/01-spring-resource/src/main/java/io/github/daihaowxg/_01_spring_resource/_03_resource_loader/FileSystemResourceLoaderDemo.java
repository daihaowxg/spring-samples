package io.github.daihaowxg._01_spring_resource._03_resource_loader;

import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 * FileSystemResourceLoader 示例
 * <p>
 * FileSystemResourceLoader 与 DefaultResourceLoader 的区别：
 * 当不带任何前缀时，DefaultResourceLoader 默认从类路径加载，
 * 而 FileSystemResourceLoader 默认从文件系统加载。
 */
public class FileSystemResourceLoaderDemo {

    public static void main(String[] args) {
        showDemo();
    }

    public static void showDemo() {
        System.out.println("=== FileSystemResourceLoader 演示 ===");
        ResourceLoader loader = new FileSystemResourceLoader();

        // 1. 不带前缀 - 默认为文件系统资源
        Resource fileRes = loader.getResource("pom.xml");
        System.out.println("不带前缀 (pom.xml): " + fileRes.getClass().getSimpleName());
        System.out.println("是否存在: " + fileRes.exists());

        // 2. 带 classpath: 前缀 - 强制从类路径加载
        Resource classRes = loader.getResource("classpath:application.properties");
        System.out.println("带 classpath: 前缀: " + classRes.getClass().getSimpleName());

        System.out.println();
    }
}
