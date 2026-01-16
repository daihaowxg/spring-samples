package io.github.daihaowxg._01_spring_resource._05_utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * EncodedResource 示例
 * <p>
 * EncodedResource 是 Spring 对 Resource 的包装类，
 * 用于将字符编码信息与资源绑定在一起。
 * <p>
 * <b>解决的问题：</b>
 * <ul>
 * <li>Resource.getInputStream() 返回字节流，需要手动指定编码</li>
 * <li>不同资源可能有不同的编码，容易遗漏或写错</li>
 * <li>中文、日文等非 ASCII 字符容易出现乱码</li>
 * </ul>
 * <p>
 * <b>EncodedResource 的优势：</b>
 * <ul>
 * <li>自动管理字符编码，减少样板代码</li>
 * <li>提供 getReader() 方法，直接返回字符流</li>
 * <li>编码信息与资源绑定，便于传递和复用</li>
 * </ul>
 */
public class EncodedResourceDemo {

    public static void main(String[] args) {
        System.out.println("=== EncodedResource 演示 ===\n");

        // 1. 基础用法：指定 UTF-8 编码
        demonstrateBasicUsage();

        // 2. 对比：不使用 EncodedResource vs 使用 EncodedResource
        demonstrateComparison();

        // 3. 获取编码信息
        demonstrateEncodingInfo();
    }

    /**
     * 演示 EncodedResource 的基础用法
     */
    private static void demonstrateBasicUsage() {
        System.out.println("--- 1. EncodedResource 基础用法 ---");

        Resource resource = new ClassPathResource("sample.txt");
        // 包装为 EncodedResource，指定 UTF-8 编码
        EncodedResource encodedResource = new EncodedResource(resource, StandardCharsets.UTF_8);

        System.out.println("原始资源: " + resource.getDescription());
        System.out.println("指定编码: " + encodedResource.getCharset());

        // 使用 getReader() 直接获取字符流，无需手动处理编码
        try (Reader reader = encodedResource.getReader()) {
            BufferedReader bufferedReader = new BufferedReader(reader);
            String content = bufferedReader.lines().limit(3).collect(Collectors.joining("\n"));
            System.out.println("读取内容（前3行）:\n" + content);
        } catch (IOException e) {
            System.err.println("读取失败: " + e.getMessage());
        }
        System.out.println();
    }

    /**
     * 对比传统方式与 EncodedResource 方式
     */
    private static void demonstrateComparison() {
        System.out.println("--- 2. 传统方式 vs EncodedResource ---");

        // Resource resource = new ClassPathResource("sample.txt");

        // 传统方式：每次读取都要手动指定编码
        System.out.println(">>> 传统方式（需要手动指定编码）:");
        System.out.println("""
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
                    // ...
                }""");

        // EncodedResource 方式：编码与资源绑定
        System.out.println("\n>>> EncodedResource 方式（编码已绑定）:");
        System.out.println("""
                EncodedResource encoded = new EncodedResource(resource, StandardCharsets.UTF_8);
                try (Reader reader = encoded.getReader()) {
                    // 无需再指定编码
                }""");

        System.out.println("\n>>> 优势：编码信息随资源一起传递，不会遗漏！");
        System.out.println();
    }

    /**
     * 演示获取编码信息
     */
    private static void demonstrateEncodingInfo() {
        System.out.println("--- 3. 编码信息查询 ---");

        Resource resource = new ClassPathResource("sample.txt");

        // 使用 Charset 创建
        EncodedResource utf8Resource = new EncodedResource(resource, StandardCharsets.UTF_8);
        System.out.println("使用 Charset 创建:");
        System.out.println("  getCharset(): " + utf8Resource.getCharset());
        System.out.println("  getEncoding(): " + utf8Resource.getEncoding());

        // 使用字符串编码名创建
        EncodedResource gbkResource = new EncodedResource(resource, "GBK");
        System.out.println("\n使用字符串编码名创建:");
        System.out.println("  getCharset(): " + gbkResource.getCharset());
        System.out.println("  getEncoding(): " + gbkResource.getEncoding());

        // 不指定编码（使用系统默认）
        EncodedResource defaultResource = new EncodedResource(resource);
        System.out.println("\n不指定编码（系统默认）:");
        System.out.println("  getCharset(): " + defaultResource.getCharset());
        System.out.println("  系统默认编码: " + Charset.defaultCharset());

        // 获取被包装的原始资源
        System.out.println("\n获取原始资源:");
        System.out.println("  getResource(): " + utf8Resource.getResource().getClass().getSimpleName());
        System.out.println();
    }
}
