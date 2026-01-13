package io.github.daihaowxg.converter.converter;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

import java.util.Date;

/**
 * Spring Converter 接口用法演示。
 * <p>
 * 本演示展示三种使用 Converter 的方式：
 * <ol>
 *     <li>直接使用 Converter（脱离 Spring 容器）</li>
 *     <li>使用 DefaultConversionService（手动注册）</li>
 *     <li>通过 Spring 容器集成</li>
 * </ol>
 */
public class ConverterDemo {

    public static void main(String[] args) {
        System.out.println("=== Spring Converter 接口用法演示 ===\n");

        // 1. 直接使用 Converter
        directUsage();

        // 2. 使用 DefaultConversionService
        conversionServiceUsage();

        // 3. Spring 容器集成
        springIntegration();
    }

    /**
     * 演示 1：直接使用 Converter。
     * <p>
     * 这是最基础的使用方式，不依赖 Spring 容器。
     * Converter 是无状态的，可以直接实例化使用。
     */
    private static void directUsage() {
        System.out.println("【1. 直接使用 Converter】");

        // String → Address
        StringToAddressConverter toAddress = new StringToAddressConverter();
        Address address = toAddress.convert("广东省/深圳市/南山区");
        System.out.println("转换结果: " + address);

        // Address → String
        AddressToStringConverter toString = new AddressToStringConverter();
        String text = toString.convert(new Address("北京市", "朝阳区", "建国路"));
        System.out.println("转换结果: " + text);

        // String → Date
        StringToDateConverter toDate = new StringToDateConverter();
        Date date = toDate.convert("2025-01-13");
        System.out.println("转换结果: " + date);

        System.out.println();
    }

    /**
     * 演示 2：使用 DefaultConversionService。
     * <p>
     * DefaultConversionService 是 Spring 提供的默认实现，
     * 包含常用的类型转换器，也支持注册自定义 Converter。
     */
    private static void conversionServiceUsage() {
        System.out.println("【2. 使用 DefaultConversionService】");

        // 创建 ConversionService
        DefaultConversionService conversionService = new DefaultConversionService();

        // 注册自定义 Converter
        conversionService.addConverter(new StringToAddressConverter());
        conversionService.addConverter(new AddressToStringConverter());
        conversionService.addConverter(new StringToDateConverter());

        // 使用统一 API 进行转换
        Address address = conversionService.convert("上海市/浦东新区/陆家嘴", Address.class);
        System.out.println("转换结果: " + address);

        String text = conversionService.convert(address, String.class);
        System.out.println("转换结果: " + text);

        // 检查是否可以转换
        boolean canConvert = conversionService.canConvert(String.class, Address.class);
        System.out.println("String → Address 可转换: " + canConvert);

        // 演示内置转换器
        System.out.println("\n内置转换器示例:");
        Integer num = conversionService.convert("123", Integer.class);
        System.out.println("  \"123\" → Integer: " + num);

        Boolean bool = conversionService.convert("true", Boolean.class);
        System.out.println("  \"true\" → Boolean: " + bool);

        System.out.println();
    }

    /**
     * 演示 3：通过 Spring 容器集成。
     * <p>
     * 通过配置类注册 Converter 后，Spring 会在属性绑定、
     * @Value 注入等场景自动使用相应的转换器。
     */
    private static void springIntegration() {
        System.out.println("【3. Spring 容器集成】");

        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext()) {

            // 注册配置类
            context.register(ConverterConfig.class);
            context.refresh();

            // 获取 ConversionService
            ConversionService conversionService = context.getBean(ConversionService.class);
            System.out.println("\nConversionService 类型: " + conversionService.getClass().getSimpleName());

            // 使用 ConversionService 进行转换
            Address address = conversionService.convert("浙江省/杭州市/西湖区", Address.class);
            System.out.println("转换结果: " + address);

            Date date = conversionService.convert("2026-06-15", Date.class);
            System.out.println("转换结果: " + date);
        }

        System.out.println();
    }
}
