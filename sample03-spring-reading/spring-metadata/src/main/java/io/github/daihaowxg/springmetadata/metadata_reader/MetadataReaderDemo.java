package io.github.daihaowxg.springmetadata.metadata_reader;

import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * MetadataReader 用法演示。
 * <p>
 * MetadataReader 是 Spring 提供的低级 API，用于在不加载类到 JVM 的情况下读取 .class 文件的元数据。
 * 这是 Spring 组件扫描的底层机制。
 * <p>
 * 核心组件：
 * - MetadataReaderFactory: 创建 MetadataReader 的工厂
 * - MetadataReader: 读取单个类的元数据
 * - ClassMetadata: 类的基本信息（类名、父类、接口等）
 * - AnnotationMetadata: 类的注解信息
 */
public class MetadataReaderDemo {

    public static void main(String[] args) throws IOException {
        System.out.println("=== MetadataReader 用法演示 ===\n");

        // 1️⃣ 创建 MetadataReaderFactory
        // SimpleMetadataReaderFactory: 每次请求都创建新的 ASM ClassReader
        // CachingMetadataReaderFactory: 带缓存，适合批量扫描
        SimpleMetadataReaderFactory factory = new SimpleMetadataReaderFactory();

        // 2️⃣ 获取 MetadataReader
        // 参数是类的全限定名（会自动转换为资源路径）
        MetadataReader metadataReader = factory.getMetadataReader(SampleService.class.getName());

        // 3️⃣ 读取类元数据
        System.out.println("【1. 类元数据 (ClassMetadata)】");
        demoClassMetadata(metadataReader);

        // 4️⃣ 读取注解元数据
        System.out.println("\n【2. 注解元数据 (AnnotationMetadata)】");
        demoAnnotationMetadata(metadataReader);

        // 5️⃣ 实际应用场景：判断是否为 Spring 组件
        System.out.println("\n【3. 实际应用：判断是否为 Spring 组件】");
        demoSpringComponentCheck(factory);

        System.out.println("\n=== 演示结束 ===");
    }

    /**
     * 演示读取类元数据
     */
    private static void demoClassMetadata(MetadataReader metadataReader) {
        ClassMetadata classMetadata = metadataReader.getClassMetadata();

        System.out.println("类名: " + classMetadata.getClassName());
        System.out.println("父类: " + classMetadata.getSuperClassName());
        System.out.println("接口: " + String.join(", ", classMetadata.getInterfaceNames()));
        System.out.println("是否接口: " + classMetadata.isInterface());
        System.out.println("是否抽象类: " + classMetadata.isAbstract());
        System.out.println("是否 final: " + classMetadata.isFinal());
        System.out.println("是否独立类: " + classMetadata.isIndependent());
    }

    /**
     * 演示读取注解元数据
     */
    private static void demoAnnotationMetadata(MetadataReader metadataReader) {
        AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();

        // 获取所有直接标注的注解类型
        Set<String> annotationTypes = annotationMetadata.getAnnotationTypes();
        System.out.println("直接标注的注解: " + annotationTypes);

        // 检查是否存在特定注解
        boolean hasService = annotationMetadata.hasAnnotation(Service.class.getName());
        System.out.println("是否有 @Service: " + hasService);

        boolean hasCustom = annotationMetadata.hasAnnotation(CustomAnnotation.class.getName());
        System.out.println("是否有 @CustomAnnotation: " + hasCustom);

        // 读取注解属性值
        if (hasService) {
            Map<String, Object> serviceAttrs = annotationMetadata.getAnnotationAttributes(Service.class.getName());
            System.out.println("@Service 属性: " + serviceAttrs);
        }

        if (hasCustom) {
            Map<String, Object> customAttrs = annotationMetadata.getAnnotationAttributes(CustomAnnotation.class.getName());
            System.out.println("@CustomAnnotation 属性: " + customAttrs);
        }

        // 检查元注解（注解的注解）
        // @Service 上有 @Component，可以通过 hasMetaAnnotation 检查
        boolean hasComponent = annotationMetadata.hasMetaAnnotation("org.springframework.stereotype.Component");
        System.out.println("是否有元注解 @Component（通过 @Service 继承）: " + hasComponent);
    }

    /**
     * 实际应用场景：模拟 Spring 组件扫描的判断逻辑
     */
    private static void demoSpringComponentCheck(SimpleMetadataReaderFactory factory) throws IOException {
        String[] classNames = {
                SampleService.class.getName(),
                CustomAnnotation.class.getName(),  // 这是注解，不是组件
                MetadataReaderDemo.class.getName() // 这是普通类，没有 @Component
        };

        for (String className : classNames) {
            MetadataReader reader = factory.getMetadataReader(className);
            AnnotationMetadata metadata = reader.getAnnotationMetadata();

            // 判断是否为 Spring 组件：有 @Component 或其派生注解
            boolean isComponent = metadata.hasAnnotation("org.springframework.stereotype.Component")
                    || metadata.hasMetaAnnotation("org.springframework.stereotype.Component");

            // 判断是否可实例化：非接口、非抽象类
            ClassMetadata classMetadata = reader.getClassMetadata();
            boolean isInstantiable = !classMetadata.isInterface()
                    && !classMetadata.isAbstract()
                    && classMetadata.isIndependent();

            String status = isComponent && isInstantiable ? "✅ 是可注册的组件" : "❌ 不是组件";
            System.out.println(getSimpleName(className) + ": " + status);
        }
    }

    private static String getSimpleName(String className) {
        int lastDot = className.lastIndexOf('.');
        return lastDot > 0 ? className.substring(lastDot + 1) : className;
    }
}
