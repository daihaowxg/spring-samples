package io.github.daihaowxg.conversionservice.service;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.format.support.DefaultFormattingConversionService;

import java.util.List;

/**
 * Spring ConversionService 接口用法演示。
 * <p>
 * ConversionService 是 Spring 类型转换体系的<strong>统一入口</strong>，
 * 负责管理所有的 Converter、ConverterFactory、GenericConverter。
 * <p>
 * 本演示展示 ConversionService 的核心功能：
 * <ol>
 *     <li>核心 API：canConvert() 和 convert()</li>
 *     <li>三种实现类对比</li>
 *     <li>内置转换器演示</li>
 *     <li>自定义转换器注册</li>
 *     <li>Spring 容器集成</li>
 * </ol>
 */
public class ConversionServiceDemo {

    public static void main(String[] args) {
        System.out.println("=== Spring ConversionService 接口用法演示 ===\n");

        // 1. 核心 API 演示
        demonstrateCoreApi();

        // 2. 实现类对比
        compareImplementations();

        // 3. 内置转换器
        demonstrateBuiltInConverters();

        // 4. Spring 容器集成
        springIntegration();
    }

    /**
     * 演示 1：ConversionService 核心 API。
     * <p>
     * ConversionService 接口定义了两组核心方法：
     * <ul>
     *     <li>{@code canConvert()} - 检查是否支持指定的类型转换</li>
     *     <li>{@code convert()} - 执行类型转换</li>
     * </ul>
     */
    private static void demonstrateCoreApi() {
        System.out.println("【1. ConversionService 核心 API】");

        // 创建 ConversionService 并注册自定义转换器
        DefaultConversionService cs = new DefaultConversionService();
        cs.addConverter(new StringToAddressConverter());
        cs.addConverter(new AddressToStringConverter());

        // ======= canConvert() =======
        System.out.println("\n--- canConvert() 方法 ---");

        // 检查内置转换
        boolean canStringToInteger = cs.canConvert(String.class, Integer.class);
        System.out.println("String → Integer: " + canStringToInteger);

        // 检查自定义转换
        boolean canStringToAddress = cs.canConvert(String.class, Address.class);
        System.out.println("String → Address: " + canStringToAddress);

        // 检查不存在的转换
        boolean canAddressToInteger = cs.canConvert(Address.class, Integer.class);
        System.out.println("Address → Integer: " + canAddressToInteger);

        // ======= convert() =======
        System.out.println("\n--- convert() 方法 ---");

        // 基础转换：指定目标类型
        Address address = cs.convert("广东省/深圳市/南山区", Address.class);
        System.out.println("String → Address: " + address);

        // 反向转换
        String text = cs.convert(address, String.class);
        System.out.println("Address → String: " + text);

        // 使用 TypeDescriptor（高级用法）
        System.out.println("\n--- TypeDescriptor 高级用法 ---");
        Object result = cs.convert(
                "北京市/朝阳区/建国路",
                TypeDescriptor.valueOf(String.class),
                TypeDescriptor.valueOf(Address.class)
        );
        System.out.println("使用 TypeDescriptor 转换: " + result);

        System.out.println();
    }

    /**
     * 演示 2：ConversionService 实现类对比。
     * <p>
     * 三种常用实现：
     * <ol>
     *     <li>{@link GenericConversionService} - 基础实现，无内置转换器</li>
     *     <li>{@link DefaultConversionService} - 默认实现，包含常用转换器</li>
     *     <li>{@link DefaultFormattingConversionService} - 增加 Formatter 支持</li>
     * </ol>
     */
    private static void compareImplementations() {
        System.out.println("【2. ConversionService 实现类对比】");

        // === GenericConversionService ===
        System.out.println("\n--- GenericConversionService（空白画布）---");
        GenericConversionService generic = new GenericConversionService();
        System.out.println("String → Integer 支持: " + generic.canConvert(String.class, Integer.class));
        // 注册后才能转换
        generic.addConverter(new StringToAddressConverter());
        System.out.println("注册后 String → Address 支持: " + generic.canConvert(String.class, Address.class));

        // === DefaultConversionService ===
        System.out.println("\n--- DefaultConversionService（推荐使用）---");
        DefaultConversionService defaults = new DefaultConversionService();
        System.out.println("String → Integer 支持: " + defaults.canConvert(String.class, Integer.class));
        System.out.println("String → Boolean 支持: " + defaults.canConvert(String.class, Boolean.class));
        System.out.println("String → Long 支持: " + defaults.canConvert(String.class, Long.class));

        // === DefaultFormattingConversionService ===
        System.out.println("\n--- DefaultFormattingConversionService（含 Formatter）---");
        DefaultFormattingConversionService formatting = new DefaultFormattingConversionService();
        System.out.println("String → Integer 支持: " + formatting.canConvert(String.class, Integer.class));
        // FormattingConversionService 继承自 DefaultConversionService，
        // 额外增加了 @DateTimeFormat、@NumberFormat 等注解支持

        System.out.println();
    }

    /**
     * 演示 3：DefaultConversionService 内置转换器。
     * <p>
     * Spring 内置了丰富的转换器，开箱即用。
     */
    private static void demonstrateBuiltInConverters() {
        System.out.println("【3. 内置转换器演示】");

        DefaultConversionService cs = new DefaultConversionService();

        // 字符串 → 基本类型
        System.out.println("\n--- 字符串 → 基本类型 ---");
        System.out.println("\"123\" → Integer: " + cs.convert("123", Integer.class));
        System.out.println("\"3.14\" → Double: " + cs.convert("3.14", Double.class));
        System.out.println("\"true\" → Boolean: " + cs.convert("true", Boolean.class));
        System.out.println("\"100\" → Long: " + cs.convert("100", Long.class));

        // 字符串 → 枚举
        System.out.println("\n--- 字符串 → 枚举 ---");
        System.out.println("\"SECONDS\" → TimeUnit: " + cs.convert("SECONDS", java.util.concurrent.TimeUnit.class));

        // 数组转换
        System.out.println("\n--- 数组转换 ---");
        String[] array = cs.convert("a,b,c", String[].class);
        System.out.println("\"a,b,c\" → String[]: " + java.util.Arrays.toString(array));

        // 集合转换（需要 TypeDescriptor 指定泛型）
        System.out.println("\n--- 集合转换 ---");
        @SuppressWarnings("unchecked")
        List<Integer> list = (List<Integer>) cs.convert(
                new String[]{"1", "2", "3"},
                TypeDescriptor.valueOf(String[].class),
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(Integer.class))
        );
        System.out.println("String[] → List<Integer>: " + list);

        System.out.println();
    }

    /**
     * 演示 4：Spring 容器集成。
     * <p>
     * 在 Spring 应用中，ConversionService 通常由容器管理：
     * <ul>
     *     <li>通过配置类注册自定义转换器</li>
     *     <li>注入 ConversionService 使用</li>
     * </ul>
     */
    private static void springIntegration() {
        System.out.println("【4. Spring 容器集成】");

        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext()) {

            // 注册配置类
            context.register(ConversionServiceConfig.class);
            context.refresh();

            // 从容器获取 ConversionService
            ConversionService cs = context.getBean(ConversionService.class);

            System.out.println("\n从容器获取的 ConversionService 类型: " + cs.getClass().getSimpleName());

            // 使用容器管理的 ConversionService 进行转换
            Address address = cs.convert("浙江省/杭州市/西湖区", Address.class);
            System.out.println("转换结果: " + address);

            // 反向转换
            String text = cs.convert(new Address("江苏省", "苏州市", "姑苏区"), String.class);
            System.out.println("反向转换: " + text);

            // 内置转换器仍然可用
            Integer num = cs.convert("999", Integer.class);
            System.out.println("内置转换 String → Integer: " + num);
        }

        System.out.println();
        System.out.println("=== 演示结束 ===");
    }
}
