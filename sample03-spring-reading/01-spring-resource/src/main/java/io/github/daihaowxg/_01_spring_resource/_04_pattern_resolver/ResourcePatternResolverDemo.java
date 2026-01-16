package io.github.daihaowxg._01_spring_resource._04_pattern_resolver;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;

/**
 * ResourcePatternResolver 示例
 * <p>
 * ResourcePatternResolver 扩展了 ResourceLoader，
 * 它支持 Ant 风格的路径模式匹配（如 ** / *.xml）。
 * 最常用的实现是 PathMatchingResourcePatternResolver。
 */
public class ResourcePatternResolverDemo {

    public static void main(String[] args) {
        showDemo();
    }

    public static void showDemo() {
        System.out.println("=== ResourcePatternResolver (模式解析) 演示 ===");
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

        try {
            // 加载所有匹配模式的资源
            // 示例：加载类路径下所有 resources 目录下的 txt 文件
            Resource[] resources = resolver.getResources("classpath*:*.txt");
            System.out.println("匹配 'classpath*:*.txt' 的资源数量: " + resources.length);
            for (Resource r : resources) {
                System.out.println(" - " + r.getFilename() + " [" + r.getDescription() + "]");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println();
    }
}
