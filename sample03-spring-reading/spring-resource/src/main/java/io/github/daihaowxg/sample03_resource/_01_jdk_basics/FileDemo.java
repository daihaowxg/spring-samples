package io.github.daihaowxg.sample03_resource._01_jdk_basics;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

/**
 * 演示使用 java.io.File 访问文件系统资源
 * 
 * 优点：
 * - 简单直观，适合访问文件系统中的资源
 * - 提供了丰富的文件操作方法
 * 
 * 缺点：
 * - 只能访问文件系统中的资源，无法访问 classpath 中的资源
 * - 无法访问 JAR 包内的资源
 * - 路径依赖于操作系统和当前工作目录
 */
public class FileDemo {

    public static void main(String[] args) {
        System.out.println("\n========== 1. 使用 java.io.File 访问资源 ==========");

        // 示例 1: 访问绝对路径文件
        demonstrateAbsolutePathAccess();

        // 示例 2: 访问相对路径文件
        demonstrateRelativePathAccess();

        // 示例 3: 尝试访问 classpath 资源（会失败）
        demonstrateClasspathAccessFailure();
    }

    /**
     * 演示使用绝对路径访问文件
     */
    private static void demonstrateAbsolutePathAccess() {
        System.out.println("\n--- 示例 1.1: 使用绝对路径访问文件 ---");

        // 获取项目根目录下的文件
        String userHome = System.getProperty("user.home");
        File file = new File(userHome, "Desktop/test.txt");

        System.out.println("文件路径: " + file.getAbsolutePath());
        System.out.println("文件是否存在: " + file.exists());
        System.out.println("是否为文件: " + file.isFile());

        if (file.exists()) {
            System.out.println("文件大小: " + file.length() + " 字节");
            readFileContent(file);
        } else {
            System.out.println("注意: 文件不存在，这是正常的演示行为");
        }
    }

    /**
     * 演示使用相对路径访问文件
     */
    private static void demonstrateRelativePathAccess() {
        System.out.println("\n--- 示例 1.2: 使用相对路径访问文件 ---");

        // 相对路径依赖于当前工作目录
        String currentDir = System.getProperty("user.dir");
        System.out.println("当前工作目录: " + currentDir);

        // 尝试访问 target/classes 下的资源（编译后的位置）
        // 改进：为了支持从父目录运行，我们尝试多个可能的路径
        File file = new File("target/classes/sample.txt");
        if (!file.exists()) {
            file = new File("sample03-spring-resource/target/classes/sample.txt");
        }

        System.out.println("尝试探测的文件路径: " + file.getAbsolutePath());
        System.out.println("文件是否存在: " + file.exists());

        if (file.exists()) {
            readFileContent(file);
        } else {
            System.out.println("提示: 文件可能尚未编译到 target/classes 目录");
        }
    }

    /**
     * 演示 File 无法直接访问 classpath 资源的局限性
     */
    private static void demonstrateClasspathAccessFailure() {
        System.out.println("\n--- 示例 1.3: File 无法直接访问 classpath 资源 ---");

        // 尝试使用相对路径访问 classpath 资源（通常会失败）
        File file = new File("sample.txt");
        System.out.println("尝试访问: sample.txt");
        System.out.println("文件路径: " + file.getAbsolutePath());
        System.out.println("文件是否存在: " + file.exists());

        System.out.println("\n问题总结:");
        System.out.println("1. File 依赖于文件系统路径，无法直接访问 classpath 资源");
        System.out.println("2. 在 JAR 包中运行时，File 无法访问打包在 JAR 内的资源");
        System.out.println("3. 路径的可移植性差，依赖于操作系统和工作目录");
    }

    /**
     * 读取文件内容的辅助方法
     */
    private static void readFileContent(File file) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            System.out.println("文件内容:");
            String line;
            int lineNumber = 1;
            while ((line = reader.readLine()) != null && lineNumber <= 3) {
                System.out.println("  第 " + lineNumber + " 行: " + line);
                lineNumber++;
            }
            if (lineNumber > 3) {
                System.out.println("  ... (省略更多内容)");
            }
        } catch (IOException e) {
            System.err.println("读取文件失败: " + e.getMessage());
        }
    }
}
