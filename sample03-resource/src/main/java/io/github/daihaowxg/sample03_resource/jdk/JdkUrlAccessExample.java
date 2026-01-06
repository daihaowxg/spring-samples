package io.github.daihaowxg.sample03_resource.jdk;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

/**
 * 演示使用 java.net.URL 访问资源
 * 
 * 优点：
 * - 支持多种协议（http, https, file, jar 等）
 * - 可以访问网络资源
 * - 统一的资源访问接口
 * 
 * 缺点：
 * - 需要处理 URL 格式和协议
 * - 对于本地文件，需要转换为 file:// URL
 * - 错误处理相对复杂
 */
public class JdkUrlAccessExample {

    public static void main(String[] args) {
        demonstrateUrlAccess();
    }

    public static void demonstrateUrlAccess() {
        System.out.println("\n========== 3. 使用 java.net.URL 访问资源 ==========");

        // 示例 1: 访问 file:// 协议资源
        demonstrateFileProtocol();

        // 示例 2: 从 ClassLoader 获取 URL 并访问
        demonstrateClassLoaderUrl();

        // 示例 3: 访问网络资源（HTTP）
        demonstrateHttpProtocol();

        // 示例 4: URL 的常用方法
        demonstrateUrlMethods();
    }

    /**
     * 演示使用 file:// 协议访问本地文件
     */
    private static void demonstrateFileProtocol() {
        System.out.println("\n--- 示例 3.1: 使用 file:// 协议访问本地文件 ---");

        try {
            // 构造 file:// URL
            String currentDir = System.getProperty("user.dir");
            File file = new File(currentDir, "target/classes/sample.txt");
            if (!file.exists()) {
                file = new File(currentDir, "sample03-resource/target/classes/sample.txt");
            }

            URL fileUrl = file.toURI().toURL();

            System.out.println("URL: " + fileUrl);
            System.out.println("协议: " + fileUrl.getProtocol());

            // 尝试读取内容
            try (InputStream inputStream = fileUrl.openStream()) {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                String firstLine = reader.readLine();
                if (firstLine != null) {
                    System.out.println("成功读取第一行: " + firstLine);
                }
            } catch (IOException e) {
                System.out.println("无法读取文件（可能尚未编译）: " + e.getMessage());
            }

        } catch (MalformedURLException e) {
            System.err.println("URL 格式错误: " + e.getMessage());
        }
    }

    /**
     * 演示从 ClassLoader 获取 URL 并访问资源
     */
    private static void demonstrateClassLoaderUrl() {
        System.out.println("\n--- 示例 3.2: 从 ClassLoader 获取 URL ---");

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL resourceUrl = classLoader.getResource("sample.txt");

        if (resourceUrl != null) {
            System.out.println("资源 URL: " + resourceUrl);
            System.out.println("协议: " + resourceUrl.getProtocol());
            System.out.println("主机: " + resourceUrl.getHost());
            System.out.println("路径: " + resourceUrl.getPath());

            try (InputStream inputStream = resourceUrl.openStream()) {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(inputStream, StandardCharsets.UTF_8));

                System.out.println("文件内容（前3行）:");
                for (int i = 0; i < 3; i++) {
                    String line = reader.readLine();
                    if (line != null) {
                        System.out.println("  " + line);
                    }
                }
            } catch (IOException e) {
                System.err.println("读取资源失败: " + e.getMessage());
            }
        } else {
            System.out.println("无法找到资源");
        }
    }

    /**
     * 演示访问 HTTP 网络资源
     */
    private static void demonstrateHttpProtocol() {
        System.out.println("\n--- 示例 3.3: 访问 HTTP 网络资源 ---");

        try {
            // 访问一个公共 API（示例）
            URL url = new URI("https://api.github.com/zen").toURL();
            System.out.println("URL: " + url);

            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(3000); // 3秒超时
            connection.setReadTimeout(3000);

            try (InputStream inputStream = connection.getInputStream()) {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(inputStream, StandardCharsets.UTF_8));

                System.out.println("GitHub Zen:");
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("  " + line);
                }
            }

        } catch (MalformedURLException | URISyntaxException e) {
            System.err.println("URL 格式错误: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("网络访问失败（这是正常的，可能没有网络连接）: " + e.getMessage());
        }
    }

    /**
     * 演示 URL 的常用方法
     */
    private static void demonstrateUrlMethods() {
        System.out.println("\n--- 示例 3.4: URL 的常用方法 ---");

        try {
            URL url = new URI("https://example.com:8080/path/to/resource?query=value#fragment").toURL();

            System.out.println("完整 URL: " + url);
            System.out.println("协议 (Protocol): " + url.getProtocol());
            System.out.println("主机 (Host): " + url.getHost());
            System.out.println("端口 (Port): " + url.getPort());
            System.out.println("路径 (Path): " + url.getPath());
            System.out.println("查询 (Query): " + url.getQuery());
            System.out.println("片段 (Fragment): " + url.getRef());
            System.out.println("文件 (File): " + url.getFile());

            System.out.println("\nURL 使用场景:");
            System.out.println("1. 访问网络资源（HTTP/HTTPS）");
            System.out.println("2. 访问本地文件（file://）");
            System.out.println("3. 访问 JAR 包内资源（jar:file://）");
            System.out.println("4. 提供统一的资源访问抽象");

        } catch (MalformedURLException | URISyntaxException e) {
            System.err.println("URL 格式错误: " + e.getMessage());
        }
    }
}
