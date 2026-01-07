package io.github.daihaowxg.sample03_resource.resource_loader;

import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * ResourceLoaderAware 示例
 * <p>
 * 如果一个 Bean 实现了 ResourceLoaderAware 接口，
 * Spring 容器在初始化时会自动将该容器的 ResourceLoader 注入进去。
 * 这是在 Spring 环境中获取 ResourceLoader 的推荐方式。
 */
@Component
public class ResourceLoaderAwareDemo implements ResourceLoaderAware {

    private ResourceLoader resourceLoader;

    @Configuration
    @ComponentScan("io.github.daihaowxg.sample03_resource.resource_loader")
    static class Config {
    }

    public static void main(String[] args) {
        System.out.println("=== ResourceLoaderAware 演示 ===");
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class)) {
            ResourceLoaderAwareDemo demo = context.getBean(ResourceLoaderAwareDemo.class);
            demo.loadResource("classpath:sample.txt");
        }
        System.out.println();
    }

    @Override
    public void setResourceLoader(@NonNull ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
        System.out.println(
                ">>> 已通过 ResourceLoaderAware 自动注入 ResourceLoader: " + resourceLoader.getClass().getSimpleName());
    }

    public void loadResource(@NonNull String location) {
        if (resourceLoader != null) {
            Resource resource = resourceLoader.getResource(location);
            System.out.println("使用注入的 Loader 加载 [" + location + "] -> " + resource.getDescription());
        }
    }
}
