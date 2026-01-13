package io.github.daihaowxg.springmetadata.type_filter;

import io.github.daihaowxg.springmetadata.metadata_reader.CustomAnnotation;
import io.github.daihaowxg.springmetadata.metadata_reader.SampleService;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.RegexPatternTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * TypeFilter 用法演示。
 * <p>
 * TypeFilter 是 Spring 组件扫描的过滤器接口，用于判断一个类是否应该被包含或排除。
 * 它与 MetadataReader 配合使用，在不加载类的情况下进行过滤判断。
 * <p>
 * 核心接口方法：
 * {@code boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory)}
 * <p>
 * 内置实现：
 * - AnnotationTypeFilter: 匹配带有指定注解的类
 * - AssignableTypeFilter: 匹配指定类型或其子类
 * - RegexPatternTypeFilter: 基于正则表达式匹配类名
 * - AspectJTypeFilter: 基于 AspectJ 表达式匹配
 */
public class TypeFilterDemo {

    public static void main(String[] args) throws IOException {
        System.out.println("=== TypeFilter 用法演示 ===\n");

        SimpleMetadataReaderFactory factory = new SimpleMetadataReaderFactory();

        // 待测试的类列表
        List<Class<?>> testClasses = List.of(
                SampleService.class, // @Service + @CustomAnnotation + implements Runnable
                CustomAnnotation.class, // 注解类型
                TypeFilterDemo.class, // 普通类，无注解
                ArrayList.class // JDK 类，实现 List 接口
        );

        // 1️⃣ AnnotationTypeFilter - 按注解过滤
        System.out.println("【1. AnnotationTypeFilter - 按注解过滤】");
        demoAnnotationTypeFilter(factory, testClasses);

        // 2️⃣ AssignableTypeFilter - 按类型过滤
        System.out.println("\n【2. AssignableTypeFilter - 按类型过滤】");
        demoAssignableTypeFilter(factory, testClasses);

        // 3️⃣ RegexPatternTypeFilter - 按正则表达式过滤
        System.out.println("\n【3. RegexPatternTypeFilter - 按正则表达式过滤】");
        demoRegexPatternTypeFilter(factory, testClasses);

        // 4️⃣ 自定义 TypeFilter - 组合条件
        System.out.println("\n【4. 自定义 TypeFilter - 组合条件】");
        demoCustomTypeFilter(factory, testClasses);

        // 5️⃣ 模拟 @ComponentScan 过滤逻辑
        System.out.println("\n【5. 模拟 @ComponentScan 过滤逻辑】");
        demoComponentScanSimulation(factory, testClasses);

        System.out.println("\n=== 演示结束 ===");
    }

    /**
     * 演示 AnnotationTypeFilter：匹配带有指定注解的类
     */
    private static void demoAnnotationTypeFilter(MetadataReaderFactory factory, List<Class<?>> testClasses)
            throws IOException {
        // 匹配 @Service 注解
        TypeFilter serviceFilter = new AnnotationTypeFilter(Service.class);

        // 匹配 @CustomAnnotation 注解
        TypeFilter customAnnoFilter = new AnnotationTypeFilter(CustomAnnotation.class);

        System.out.println("匹配 @Service 注解：");
        for (Class<?> clazz : testClasses) {
            MetadataReader reader = factory.getMetadataReader(clazz.getName());
            boolean matches = serviceFilter.match(reader, factory);
            System.out.println("  " + clazz.getSimpleName() + ": " + (matches ? "✅" : "❌"));
        }

        System.out.println("匹配 @CustomAnnotation 注解：");
        for (Class<?> clazz : testClasses) {
            MetadataReader reader = factory.getMetadataReader(clazz.getName());
            boolean matches = customAnnoFilter.match(reader, factory);
            System.out.println("  " + clazz.getSimpleName() + ": " + (matches ? "✅" : "❌"));
        }
    }

    /**
     * 演示 AssignableTypeFilter：匹配指定类型或其子类/实现类
     */
    private static void demoAssignableTypeFilter(MetadataReaderFactory factory, List<Class<?>> testClasses)
            throws IOException {
        // 匹配 Runnable 的实现类
        TypeFilter runnableFilter = new AssignableTypeFilter(Runnable.class);

        // 匹配 List 的实现类
        TypeFilter listFilter = new AssignableTypeFilter(List.class);

        System.out.println("匹配 Runnable 实现类：");
        for (Class<?> clazz : testClasses) {
            MetadataReader reader = factory.getMetadataReader(clazz.getName());
            boolean matches = runnableFilter.match(reader, factory);
            System.out.println("  " + clazz.getSimpleName() + ": " + (matches ? "✅" : "❌"));
        }

        System.out.println("匹配 List 实现类：");
        for (Class<?> clazz : testClasses) {
            MetadataReader reader = factory.getMetadataReader(clazz.getName());
            boolean matches = listFilter.match(reader, factory);
            System.out.println("  " + clazz.getSimpleName() + ": " + (matches ? "✅" : "❌"));
        }
    }

    /**
     * 演示 RegexPatternTypeFilter：基于正则表达式匹配类名
     */
    private static void demoRegexPatternTypeFilter(MetadataReaderFactory factory, List<Class<?>> testClasses)
            throws IOException {
        // 匹配类名以 "Service" 结尾的类（全限定名）
        TypeFilter serviceNameFilter = new RegexPatternTypeFilter(Pattern.compile(".*Service$"));

        // 匹配类名包含 "Demo" 的类
        TypeFilter demoNameFilter = new RegexPatternTypeFilter(Pattern.compile(".*Demo.*"));

        System.out.println("匹配类名以 'Service' 结尾：");
        for (Class<?> clazz : testClasses) {
            MetadataReader reader = factory.getMetadataReader(clazz.getName());
            boolean matches = serviceNameFilter.match(reader, factory);
            System.out.println("  " + clazz.getSimpleName() + ": " + (matches ? "✅" : "❌"));
        }

        System.out.println("匹配类名包含 'Demo'：");
        for (Class<?> clazz : testClasses) {
            MetadataReader reader = factory.getMetadataReader(clazz.getName());
            boolean matches = demoNameFilter.match(reader, factory);
            System.out.println("  " + clazz.getSimpleName() + ": " + (matches ? "✅" : "❌"));
        }
    }

    /**
     * 演示自定义 TypeFilter：实现复杂的过滤逻辑
     */
    private static void demoCustomTypeFilter(MetadataReaderFactory factory, List<Class<?>> testClasses)
            throws IOException {

        // 自定义过滤器：必须同时满足以下条件：
        // 1. 有 @Service 注解
        // 2. 实现 Runnable 接口
        // 3. 不是抽象类
        TypeFilter customFilter = (metadataReader, metadataReaderFactory) -> {
            var classMetadata = metadataReader.getClassMetadata();
            var annotationMetadata = metadataReader.getAnnotationMetadata();

            boolean hasServiceAnnotation = annotationMetadata.hasAnnotation(Service.class.getName())
                    || annotationMetadata.hasMetaAnnotation(Service.class.getName());

            boolean implementsRunnable = false;
            for (String interfaceName : classMetadata.getInterfaceNames()) {
                if (Runnable.class.getName().equals(interfaceName)) {
                    implementsRunnable = true;
                    break;
                }
            }

            boolean isNotAbstract = !classMetadata.isAbstract();

            return hasServiceAnnotation && implementsRunnable && isNotAbstract;
        };

        System.out.println("自定义过滤器（@Service + Runnable + 非抽象类）：");
        for (Class<?> clazz : testClasses) {
            MetadataReader reader = factory.getMetadataReader(clazz.getName());
            boolean matches = customFilter.match(reader, factory);
            System.out.println("  " + clazz.getSimpleName() + ": " + (matches ? "✅" : "❌"));
        }
    }

    /**
     * 模拟 @ComponentScan 的 includeFilters 和 excludeFilters 逻辑
     */
    private static void demoComponentScanSimulation(MetadataReaderFactory factory, List<Class<?>> testClasses)
            throws IOException {

        // 模拟配置：
        // includeFilters = @Filter(type = ANNOTATION, classes = Service.class)
        // excludeFilters = @Filter(type = REGEX, pattern = ".*Demo.*")

        List<TypeFilter> includeFilters = List.of(
                new AnnotationTypeFilter(Service.class));

        List<TypeFilter> excludeFilters = List.of(
                new RegexPatternTypeFilter(Pattern.compile(".*Demo.*")));

        System.out.println("模拟 @ComponentScan：");
        System.out.println("  includeFilters: @Service");
        System.out.println("  excludeFilters: 类名匹配 .*Demo.*");
        System.out.println("结果：");

        for (Class<?> clazz : testClasses) {
            MetadataReader reader = factory.getMetadataReader(clazz.getName());

            // 检查是否被 include（至少匹配一个 includeFilter）
            boolean included = false;
            for (TypeFilter filter : includeFilters) {
                if (filter.match(reader, factory)) {
                    included = true;
                    break;
                }
            }

            // 检查是否被 exclude（匹配任意一个 excludeFilter）
            boolean excluded = false;
            for (TypeFilter filter : excludeFilters) {
                if (filter.match(reader, factory)) {
                    excluded = true;
                    break;
                }
            }

            // 最终判断：included 且 不被 excluded
            boolean finalResult = included && !excluded;
            String status = finalResult ? "✅ 将被注册" : "❌ 不会注册";
            String reason = !included ? "(未匹配 includeFilter)" : excluded ? "(被 excludeFilter 排除)" : "";
            System.out.println("  " + clazz.getSimpleName() + ": " + status + " " + reason);
        }
    }
}
