package io.github.daihaowxg.converterfactory.converter;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.support.FormattingConversionService;

/**
 * ConverterFactory 接口用法演示。
 * <p>
 * 本演示展示三种使用 ConverterFactory 的方式：
 * <ol>
 *     <li>直接使用 ConverterFactory（脱离 Spring 容器）</li>
 *     <li>通过 ConversionService 使用</li>
 *     <li>Spring 容器集成</li>
 * </ol>
 */
public class ConverterFactoryDemo {

    public static void main(String[] args) {
        System.out.println("=== ConverterFactory 接口用法演示 ===\n");

        // 1. 直接使用 ConverterFactory
        directUsage();

        // 2. 通过 ConversionService 使用
        conversionServiceUsage();

        // 3. Spring 容器集成
        springIntegration();
    }

    /**
     * 演示 1：直接使用 ConverterFactory。
     * <p>
     * 这是最基础的使用方式，展示 ConverterFactory 如何为不同目标类型生成 Converter。
     */
    private static void directUsage() {
        System.out.println("【1. 直接使用 ConverterFactory】");

        // 创建 ConverterFactory 实例
        StringToEnumConverterFactory factory = new StringToEnumConverterFactory();

        // 获取 Color 类型的 Converter
        Converter<String, Color> colorConverter = factory.getConverter(Color.class);
        Color red = colorConverter.convert("red");
        Color green = colorConverter.convert("GREEN");
        System.out.println("转换结果: " + red + ", " + green);

        // 获取 Priority 类型的 Converter（同一个工厂！）
        Converter<String, Priority> priorityConverter = factory.getConverter(Priority.class);
        Priority high = priorityConverter.convert("high");
        Priority low = priorityConverter.convert("LOW");
        System.out.println("转换结果: " + high + ", " + low);

        System.out.println();
    }

    /**
     * 演示 2：通过 ConversionService 使用。
     * <p>
     * ConversionService 是 Spring 类型转换的统一入口，
     * 注册 ConverterFactory 后可通过 convert() 方法直接使用。
     */
    private static void conversionServiceUsage() {
        System.out.println("【2. 通过 ConversionService 使用】");

        // 创建 ConversionService 并注册 ConverterFactory
        FormattingConversionService conversionService = new FormattingConversionService();
        conversionService.addConverterFactory(new StringToEnumConverterFactory());

        // 使用统一的 API 进行转换
        Color blue = conversionService.convert("blue", Color.class);
        Priority medium = conversionService.convert("medium", Priority.class);

        System.out.println("Color 转换结果: " + blue);
        System.out.println("Priority 转换结果: " + medium);

        // 检查是否支持转换
        boolean canConvertColor = conversionService.canConvert(String.class, Color.class);
        boolean canConvertPriority = conversionService.canConvert(String.class, Priority.class);
        System.out.println("支持 String → Color: " + canConvertColor);
        System.out.println("支持 String → Priority: " + canConvertPriority);

        System.out.println();
    }

    /**
     * 演示 3：Spring 容器集成。
     * <p>
     * 通过配置类注册 ConverterFactory，从容器获取 ConversionService 使用。
     */
    private static void springIntegration() {
        System.out.println("【3. Spring 容器集成】");

        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext()) {

            // 注册配置类
            context.register(ConverterFactoryConfig.class);
            context.refresh();

            // 从容器获取 ConversionService
            ConversionService conversionService = context.getBean(ConversionService.class);

            // 使用容器中的 ConversionService 进行转换
            Color color = conversionService.convert("RED", Color.class);
            Priority priority = conversionService.convert("HIGH", Priority.class);

            System.out.println("从容器获取的 ConversionService 转换结果:");
            System.out.println("  Color: " + color);
            System.out.println("  Priority: " + priority);
        }

        System.out.println();
    }
}
