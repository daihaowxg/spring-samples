package io.github.daihaowxg.sample03_resource._02_resource_interface;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * ByteArrayResource 示例
 * <p>
 * ByteArrayResource 是 Spring 提供的内存资源实现类，
 * 它将字节数组包装成 Resource 接口，无需依赖文件系统或网络。
 * <p>
 * <b>典型使用场景：</b>
 * <ul>
 * <li>单元测试中模拟资源，避免依赖外部文件</li>
 * <li>动态生成的内容需要以 Resource 形式传递</li>
 * <li>从数据库或缓存中读取的二进制数据</li>
 * </ul>
 * <p>
 * <b>特点：</b>
 * <ul>
 * <li>资源内容存储在内存中，读取速度快</li>
 * <li>可重复读取（每次 getInputStream() 返回新的流）</li>
 * <li>不支持 getFile() 方法（无底层文件）</li>
 * </ul>
 */
public class ByteArrayResourceDemo {

    public static void main(String[] args) {
        System.out.println("=== ByteArrayResource 演示 ===\n");

        // 1. 从字符串创建资源
        demonstrateFromString();

        // 2. 从字节数组创建资源（带描述）
        demonstrateWithDescription();

        // 3. 演示可重复读取特性
        demonstrateReusability();

        // 4. 典型测试场景
        demonstrateTestScenario();
    }

    /**
     * 演示从字符串创建 ByteArrayResource
     */
    private static void demonstrateFromString() {
        System.out.println("--- 1. 从字符串创建资源 ---");

        String content = "Hello, Spring Resource!\n这是中文内容测试。";
        byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
        @SuppressWarnings("null")
        Resource resource = new ByteArrayResource(bytes);

        System.out.println("资源类型: " + resource.getClass().getSimpleName());
        System.out.println("是否存在: " + resource.exists());
        System.out.println("内容长度: " + bytes.length + " 字节");

        // 读取内容
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            String readContent = reader.lines().collect(Collectors.joining("\n"));
            System.out.println("读取内容: " + readContent);
        } catch (IOException e) {
            System.err.println("读取失败: " + e.getMessage());
        }
        System.out.println();
    }

    /**
     * 演示带描述的 ByteArrayResource
     */
    private static void demonstrateWithDescription() {
        System.out.println("--- 2. 带描述的资源 ---");

        String jsonContent = "{\"name\": \"测试\", \"value\": 123}";
        // 使用带描述的构造函数，便于日志和调试
        @SuppressWarnings("null")
        Resource resource = new ByteArrayResource(
                jsonContent.getBytes(StandardCharsets.UTF_8),
                "JSON 配置数据（来自内存）");

        System.out.println("资源描述: " + resource.getDescription());
        System.out.println("文件名: " + resource.getFilename()); // ByteArrayResource 返回 null
        System.out.println();
    }

    /**
     * 演示 ByteArrayResource 的可重复读取特性（与 InputStreamResource 对比）
     */
    private static void demonstrateReusability() {
        System.out.println("--- 3. 可重复读取特性对比 ---");

        String content = "可重复读取的内容";

        // === 部分 A: ByteArrayResource 可多次读取 ===
        System.out.println("\n>>> ByteArrayResource 可多次读取：");
        @SuppressWarnings("null")
        Resource byteArrayResource = new ByteArrayResource(content.getBytes(StandardCharsets.UTF_8));

        for (int i = 1; i <= 3; i++) {
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(byteArrayResource.getInputStream(), StandardCharsets.UTF_8))) {
                String line = reader.readLine();
                System.out.println("  第 " + i + " 次读取: " + line);
            } catch (IOException e) {
                System.err.println("  第 " + i + " 次读取失败: " + e.getMessage());
            }
        }

        // === 部分 B: InputStreamResource 只能读取一次 ===
        System.out.println("\n>>> InputStreamResource 只能读取一次：");
        // 创建一个 InputStream（这里用 ByteArrayInputStream 模拟）
        java.io.ByteArrayInputStream inputStream = new java.io.ByteArrayInputStream(
                content.getBytes(StandardCharsets.UTF_8));
        @SuppressWarnings("null")
        Resource inputStreamResource = new org.springframework.core.io.InputStreamResource(inputStream);

        for (int i = 1; i <= 3; i++) {
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(inputStreamResource.getInputStream(), StandardCharsets.UTF_8))) {
                String line = reader.readLine();
                System.out.println("  第 " + i + " 次读取: " + (line != null ? line : "(空)"));
            } catch (IOException e) {
                System.err.println("  第 " + i + " 次读取失败: " + e.getMessage());
            } catch (IllegalStateException e) {
                // InputStreamResource 第二次调用 getInputStream() 会抛出此异常
                System.err.println("  第 " + i + " 次读取失败: " + e.getMessage());
            }
        }

        System.out.println("\n>>> 结论: ByteArrayResource 适合需要多次读取的场景，InputStreamResource 只能一次性消费！");
        System.out.println();
    }

    /**
     * 演示单元测试典型场景
     */
    private static void demonstrateTestScenario() {
        System.out.println("--- 4. 单元测试场景示例 ---");

        // 模拟测试：验证一个"解析器"能正确处理 properties 格式
        String propertiesContent = """
                app.name=TestApp
                app.version=1.0.0
                app.encoding=UTF-8
                """;

        byte[] contentBytes = propertiesContent.getBytes(StandardCharsets.UTF_8);

        @SuppressWarnings("null")
        Resource mockResource = new ByteArrayResource(
                contentBytes,
                "模拟的 application.properties");

        System.out.println(">>> 测试场景：使用 ByteArrayResource 模拟配置文件");
        System.out.println("资源描述: " + mockResource.getDescription());
        System.out.println("内容长度: " + contentBytes.length + " 字节");
        System.out.println("是否可读: " + mockResource.isReadable());

        // 模拟解析（实际测试中会调用被测对象）
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(mockResource.getInputStream(), StandardCharsets.UTF_8))) {
            System.out.println("解析结果:");
            reader.lines()
                    .filter(line -> !line.isBlank())
                    .forEach(line -> System.out.println("  " + line));
        } catch (IOException e) {
            System.err.println("解析失败: " + e.getMessage());
        }

        System.out.println("\n>>> 优势：无需创建临时文件，测试更快、更干净！");
    }
}
