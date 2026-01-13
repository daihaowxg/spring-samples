package io.github.daihaowxg.conditionalconverter.converter;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.convert.support.GenericConversionService;

import java.util.Date;

/**
 * ConditionalConverter 接口用法演示。
 * <p>
 * 本演示展示三种使用 ConditionalConverter 的方式：
 * <ol>
 *     <li>直接使用 ConditionalConverter（脱离 Spring 容器）</li>
 *     <li>使用 GenericConversionService</li>
 *     <li>通过 Spring 容器集成</li>
 * </ol>
 */
public class ConditionalConverterDemo {

    public static void main(String[] args) {
        System.out.println("=== ConditionalConverter 接口用法演示 ===\n");

        // 1. 直接使用 ConditionalConverter
        directUsage();

        // 2. 使用 GenericConversionService
        conversionServiceUsage();

        // 3. 使用注解感知的日期转换
        annotationAwareUsage();

        // 4. Spring 容器集成
        springIntegration();
    }

    /**
     * 演示 1：直接使用 ConditionalConverter。
     */
    private static void directUsage() {
        System.out.println("【1. 直接使用 ConditionalConverter】");

        StringToPersonConverter converter = new StringToPersonConverter();

        // 测试 matches 方法
        TypeDescriptor stringType = TypeDescriptor.valueOf(String.class);
        TypeDescriptor personType = TypeDescriptor.valueOf(Person.class);
        TypeDescriptor intType = TypeDescriptor.valueOf(Integer.class);

        System.out.println("测试 matches 方法:");
        converter.matches(stringType, personType);  // 应该返回 true
        converter.matches(stringType, intType);     // 应该返回 false

        // 测试 convert 方法
        System.out.println("\n测试 convert 方法:");
        Person person = converter.convert("张三:25");
        System.out.println("转换结果: " + person);

        System.out.println();
    }

    /**
     * 演示 2：使用 GenericConversionService。
     */
    private static void conversionServiceUsage() {
        System.out.println("【2. 使用 GenericConversionService】");

        GenericConversionService conversionService = new DefaultConversionService();
        conversionService.addConverter(new StringToPersonConverter());

        // 转换 String → Person
        System.out.println("通过 ConversionService 转换:");
        Person person = conversionService.convert("李四:30", Person.class);
        System.out.println("转换结果: " + person);

        // 验证 canConvert（会触发 matches）
        System.out.println("\n验证 canConvert:");
        boolean canConvert = conversionService.canConvert(String.class, Person.class);
        System.out.println("canConvert(String, Person) = " + canConvert);

        System.out.println();
    }

    /**
     * 演示 3：注解感知的日期转换。
     */
    private static void annotationAwareUsage() {
        System.out.println("【3. 注解感知的日期转换】");

        GenericConversionService conversionService = new DefaultConversionService();
        conversionService.addConverter(new AnnotationAwareDateConverter());

        // 模拟带 @DateFormat 注解的字段
        System.out.println("模拟带有 @DateFormat 注解的转换:");
        try {
            // 创建带有 @DateFormat 注解的 TypeDescriptor
            TypeDescriptor sourceType = TypeDescriptor.valueOf(String.class);
            // 获取 SampleBean.createDate 字段的 TypeDescriptor（包含注解信息）
            TypeDescriptor targetWithAnnotation = new TypeDescriptor(
                    SampleBean.class.getDeclaredField("createDate"));

            Object date = conversionService.convert("2025-01-13", sourceType, targetWithAnnotation);
            System.out.println("转换结果: " + date);

            // 带时间的格式
            System.out.println("\n模拟带有 @DateFormat(\"yyyy-MM-dd HH:mm:ss\") 的转换:");
            TypeDescriptor targetWithTimeAnnotation = new TypeDescriptor(
                    SampleBean.class.getDeclaredField("updateDate"));
            Object dateTime = conversionService.convert("2025-01-13 10:30:45",
                    sourceType, targetWithTimeAnnotation);
            System.out.println("转换结果: " + dateTime);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        System.out.println();
    }

    /**
     * 演示 4：通过 Spring 容器集成。
     */
    private static void springIntegration() {
        System.out.println("【4. Spring 容器集成】");

        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext()) {

            context.register(ConditionalConverterConfig.class);
            context.refresh();

            ConversionService conversionService = context.getBean(ConversionService.class);

            System.out.println("\n使用容器中的 ConversionService:");
            Person person = conversionService.convert("王五:35", Person.class);
            System.out.println("转换结果: " + person);
        }

        System.out.println();
    }

    /**
     * 用于演示的示例 Bean，包含带注解的日期字段。
     */
    static class SampleBean {
        @DateFormat("yyyy-MM-dd")
        Date createDate;

        @DateFormat("yyyy-MM-dd HH:mm:ss")
        Date updateDate;
    }
}
