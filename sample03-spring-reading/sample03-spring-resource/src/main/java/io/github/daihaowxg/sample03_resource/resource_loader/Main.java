package io.github.daihaowxg.sample03_resource.resource_loader;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * ResourceLoader 示例运行入口
 */
@Configuration
@ComponentScan("io.github.daihaowxg.sample03_resource.resource_loader")
public class Main {
    public static void main(String[] args) {
        // 1. 演示基础的 ResourceLoader 实现
        DefaultResourceLoaderDemo.showDemo();
        FileSystemResourceLoaderDemo.showDemo();
        ResourcePatternResolverDemo.showDemo();

        // 2. 演示 Spring 容器环境下的 ResourceLoaderAware
        System.out.println("=== Spring 容器环境演示 ===");
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Main.class)) {
            ResourceLoaderAwareDemo awareDemo = context.getBean(ResourceLoaderAwareDemo.class);
            awareDemo.loadResource("classpath:sample.txt");
        }
    }
}
