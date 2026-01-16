package io.github.daihaowxg.sample03_resource._05_utils;

import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;

/**
 * ResourceUtils 工具类示例
 * <p>
 * ResourceUtils 是 Spring 提供的静态工具类，
 * 用于简化资源路径的解析和转换操作。
 * <p>
 * <b>常用方法：</b>
 * <ul>
 * <li>{@link ResourceUtils#isUrl(String)} - 判断是否为 URL</li>
 * <li>{@link ResourceUtils#getURL(String)} - 获取 URL 对象</li>
 * <li>{@link ResourceUtils#getFile(String)} - 转换为 File 对象</li>
 * <li>{@link ResourceUtils#extractJarFileURL(URL)} - 提取 JAR 文件 URL</li>
 * </ul>
 * <p>
 * <b>常用常量：</b>
 * <ul>
 * <li>{@link ResourceUtils#CLASSPATH_URL_PREFIX} = "classpath:"</li>
 * <li>{@link ResourceUtils#FILE_URL_PREFIX} = "file:"</li>
 * <li>{@link ResourceUtils#JAR_URL_PREFIX} = "jar:"</li>
 * <li>{@link ResourceUtils#URL_PROTOCOL_FILE} = "file"</li>
 * </ul>
 */
public class ResourceUtilsDemo {

    public static void main(String[] args) {
        System.out.println("=== ResourceUtils 工具类演示 ===\n");

        // 1. isUrl() 判断路径是否为 URL
        demonstrateIsUrl();

        // 2. getURL() 获取 URL 对象
        demonstrateGetUrl();

        // 3. getFile() 转换为 File 对象
        demonstrateGetFile();

        // 4. 常用常量
        demonstrateConstants();
    }

    /**
     * 演示 isUrl() 方法
     */
    private static void demonstrateIsUrl() {
        System.out.println("--- 1. isUrl() 判断路径是否为 URL ---");

        String[] paths = {
                "classpath:application.properties",
                "file:/path/to/file.txt",
                "https://example.com/resource",
                "/absolute/path/file.txt",
                "relative/path/file.txt",
                "jar:file:/path/to/lib.jar!/config.xml"
        };

        for (String path : paths) {
            boolean isUrl = ResourceUtils.isUrl(path);
            System.out.printf("  %-45s -> %s%n", path, isUrl ? "✓ 是 URL" : "✗ 不是 URL");
        }
        System.out.println();
    }

    /**
     * 演示 getURL() 方法
     */
    private static void demonstrateGetUrl() {
        System.out.println("--- 2. getURL() 获取 URL 对象 ---");

        // classpath: 资源
        try {
            URL url = ResourceUtils.getURL("classpath:sample.txt");
            System.out.println("classpath:sample.txt");
            System.out.println("  URL: " + url);
            System.out.println("  协议: " + url.getProtocol());
        } catch (FileNotFoundException e) {
            System.err.println("  资源不存在: " + e.getMessage());
        }

        // file: 资源
        try {
            URL url = ResourceUtils.getURL("file:pom.xml");
            System.out.println("\nfile:pom.xml");
            System.out.println("  URL: " + url);
        } catch (FileNotFoundException e) {
            System.err.println("  文件不存在: " + e.getMessage());
        }

        // http: 资源（仅解析，不验证存在性）
        try {
            URL url = ResourceUtils.getURL("https://example.com/data.json");
            System.out.println("\nhttps://example.com/data.json");
            System.out.println("  URL: " + url);
            System.out.println("  (注意: getURL 只解析协议，不验证资源是否存在)");
        } catch (FileNotFoundException e) {
            System.err.println("  异常: " + e.getMessage());
        }
        System.out.println();
    }

    /**
     * 演示 getFile() 方法
     */
    private static void demonstrateGetFile() {
        System.out.println("--- 3. getFile() 转换为 File 对象 ---");

        // 从 classpath 资源获取 File
        try {
            File file = ResourceUtils.getFile("classpath:sample.txt");
            System.out.println("classpath:sample.txt");
            System.out.println("  File: " + file.getAbsolutePath());
            System.out.println("  存在: " + file.exists());
        } catch (FileNotFoundException e) {
            System.err.println("  无法转换为 File: " + e.getMessage());
        }

        // 从 file: URL 获取 File
        try {
            File file = ResourceUtils.getFile("file:pom.xml");
            System.out.println("\nfile:pom.xml");
            System.out.println("  File: " + file.getAbsolutePath());
            System.out.println("  存在: " + file.exists());
        } catch (FileNotFoundException e) {
            System.err.println("  无法转换为 File: " + e.getMessage());
        }

        // 注意：JAR 包内的资源无法转换为 File
        System.out.println("\n>>> 注意: 当资源在 JAR 包内时，getFile() 会抛出异常！");
        System.out.println(">>> 此时应使用 Resource.getInputStream() 读取内容。");
        System.out.println();
    }

    /**
     * 演示常用常量
     */
    private static void demonstrateConstants() {
        System.out.println("--- 4. ResourceUtils 常用常量 ---");

        System.out.println("路径前缀:");
        System.out.println("  CLASSPATH_URL_PREFIX = \"" + ResourceUtils.CLASSPATH_URL_PREFIX + "\"");
        System.out.println("  FILE_URL_PREFIX      = \"" + ResourceUtils.FILE_URL_PREFIX + "\"");
        System.out.println("  JAR_URL_PREFIX       = \"" + ResourceUtils.JAR_URL_PREFIX + "\"");
        System.out.println("  WAR_URL_PREFIX       = \"" + ResourceUtils.WAR_URL_PREFIX + "\"");

        System.out.println("\n协议名称:");
        System.out.println("  URL_PROTOCOL_FILE    = \"" + ResourceUtils.URL_PROTOCOL_FILE + "\"");
        System.out.println("  URL_PROTOCOL_JAR     = \"" + ResourceUtils.URL_PROTOCOL_JAR + "\"");
        System.out.println("  URL_PROTOCOL_WSJAR   = \"" + ResourceUtils.URL_PROTOCOL_WSJAR + "\"");
        System.out.println("  URL_PROTOCOL_VFSZIP  = \"" + ResourceUtils.URL_PROTOCOL_VFSZIP + "\"");

        System.out.println("\n>>> 使用常量可避免魔法字符串，提高代码可读性和可维护性。");
    }
}
