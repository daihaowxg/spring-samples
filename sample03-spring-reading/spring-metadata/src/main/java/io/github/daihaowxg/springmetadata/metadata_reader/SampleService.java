package io.github.daihaowxg.springmetadata.metadata_reader;

import org.springframework.stereotype.Service;

/**
 * 示例服务类，用于被 MetadataReader 读取。
 * <p>
 * 特点：
 * - 有 Spring 注解 @Service
 * - 有自定义注解 @CustomAnnotation
 * - 实现接口 Runnable
 * - 继承 Object（默认）
 */
@Service("sampleService")
@CustomAnnotation(module = "metadata-demo", version = "2.0", enabled = true)
public class SampleService implements Runnable {

    @Override
    public void run() {
        System.out.println("SampleService is running...");
    }

    public void doWork() {
        System.out.println("SampleService is doing work...");
    }
}
