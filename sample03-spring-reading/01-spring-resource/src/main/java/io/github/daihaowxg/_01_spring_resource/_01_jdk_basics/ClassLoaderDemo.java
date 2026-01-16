package io.github.daihaowxg._01_spring_resource._01_jdk_basics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * 演示使用 ClassLoader 访问 classpath 资源
 * 
 * 优点：
 * - 可以访问 classpath 中的资源
 * - 可以访问 JAR 包内的资源
 * - 路径相对于 classpath，具有良好的可移植性
 * 
 * 缺点：
 * - 只能访问 classpath 资源，无法访问文件系统中的任意文件
 * - API 相对底层，需要手动处理流的关闭
 * - 路径规则容易混淆（是否以 / 开头）
 */
public class ClassLoaderDemo {

    public static void main(String[] args) {
        System.out.println("\n========== 2. 使用 ClassLoader 访问资源 ==========");

        // 示例 1: 使用 getResourceAsStream 读取文本文件
        demonstrateGetResourceAsStream();

        // 示例 2: 使用 getResource 获取 URL
        demonstrateGetResource();

        // 示例 3: 读取 properties 文件
        demonstrateReadProperties();

        // 示例 4: Class.getResource vs ClassLoader.getResource
        demonstrateClassVsClassLoaderResource();
    }

    /**
     * 演示使用 getResourceAsStream 读取资源
     */
    private static void demonstrateGetResourceAsStream() {
        System.out.println("\n--- 示例 2.1: 使用 getResourceAsStream 读取文本文件 ---");

        // 获取当前线程的类加载器
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        // 读取 classpath 根目录下的 sample.txt
        // 注意: ClassLoader.getResourceAsStream 不需要前导 /
        try (InputStream inputStream = classLoader.getResourceAsStream("sample.txt")) {
            if (inputStream != null) {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(inputStream, StandardCharsets.UTF_8));

                System.out.println("成功读取 sample.txt:");
                String line;
                int lineNumber = 1;
                while ((line = reader.readLine()) != null && lineNumber <= 3) {
                    System.out.println("  第 " + lineNumber + " 行: " + line);
                    lineNumber++;
                }
            } else {
                System.out.println("无法找到资源: sample.txt");
            }
        } catch (IOException e) {
            System.err.println("读取资源失败: " + e.getMessage());
        }
    }

    /**
     * 演示使用 getResource 获取资源 URL
     */
    private static void demonstrateGetResource() {
        System.out.println("\n--- 示例 2.2: 使用 getResource 获取资源 URL ---");

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        // 获取资源的 URL
        URL resourceUrl = classLoader.getResource("sample.txt");

        if (resourceUrl != null) {
            System.out.println("资源 URL: " + resourceUrl);
            System.out.println("协议: " + resourceUrl.getProtocol());
            System.out.println("路径: " + resourceUrl.getPath());

            // 通过 URL 读取内容
            try (InputStream inputStream = resourceUrl.openStream()) {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                String firstLine = reader.readLine();
                System.out.println("第一行内容: " + firstLine);
            } catch (IOException e) {
                System.err.println("读取资源失败: " + e.getMessage());
            }
        } else {
            System.out.println("无法找到资源: sample.txt");
        }
    }

    /**
     * 演示读取 properties 文件
     */
    private static void demonstrateReadProperties() {
        System.out.println("\n--- 示例 2.3: 读取 properties 文件 ---");

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        // 读取嵌套目录中的 properties 文件
        try (InputStream inputStream = classLoader.getResourceAsStream("data/config.properties")) {
            if (inputStream != null) {
                Properties properties = new Properties();
                properties.load(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

                System.out.println("成功加载 config.properties:");
                System.out.println("  app.name = " + properties.getProperty("app.name"));
                System.out.println("  app.version = " + properties.getProperty("app.version"));
                System.out.println("  app.description = " + properties.getProperty("app.description"));
            } else {
                System.out.println("无法找到资源: data/config.properties");
            }
        } catch (IOException e) {
            System.err.println("读取 properties 失败: " + e.getMessage());
        }
    }

    /**
     * 演示 Class.getResource 和 ClassLoader.getResource 的区别
     */
    private static void demonstrateClassVsClassLoaderResource() {
        System.out.println("\n--- 示例 2.4: Class.getResource vs ClassLoader.getResource ---");

        // ClassLoader.getResource: 路径相对于 classpath 根目录，不需要前导 /
        ClassLoader classLoader = ClassLoaderDemo.class.getClassLoader();
        URL url1 = classLoader.getResource("sample.txt");
        System.out.println("ClassLoader.getResource(\"sample.txt\"): " + url1);

        URL url1_err = classLoader.getResource("/sample.txt");
        System.out.println("ClassLoader.getResource(\"/sample.txt\"): " + url1_err);
        System.out.println("  (ClassLoader 不支持 / 开头，所以返回 null)");

        // Class.getResource:
        // - 以 / 开头: 相对于 classpath 根目录
        // - 不以 / 开头: 相对于当前类所在的包
        URL url2 = ClassLoaderDemo.class.getResource("/sample.txt");
        System.out.println("Class.getResource(\"/sample.txt\"): " + url2);

        URL url3 = ClassLoaderDemo.class.getResource("sample.txt");
        System.out.println("Class.getResource(\"sample.txt\"): " + url3);
        System.out.println("  (相对于当前包路径，所以找不到)");

        System.out.println("\n路径规则总结:");
        System.out.println("1. ClassLoader.getResource(path): 必须不以 / 开头，始终相对于 classpath 根");
        System.out.println("2. Class.getResource(/path): 以 / 开头，相对于 classpath 根");
        System.out.println("3. Class.getResource(path): 不以 / 开头，相对于当前类所在包");
    }
}
