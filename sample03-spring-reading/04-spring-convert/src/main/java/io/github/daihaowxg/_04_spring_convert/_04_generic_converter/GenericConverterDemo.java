package io.github.daihaowxg._04_spring_convert._04_generic_converter;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.convert.support.GenericConversionService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * GenericConverter 接口用法演示。
 * <p>
 * 本演示展示三种使用 GenericConverter 的方式：
 * <ol>
 *     <li>直接使用 GenericConverter（脱离 Spring 容器）</li>
 *     <li>使用 GenericConversionService</li>
 *     <li>通过 Spring 容器集成 GenericConverter</li>
 * </ol>
 */
public class GenericConverterDemo {

    public static void main(String[] args) {
        System.out.println("=== GenericConverter 接口用法演示 ===\n");

        // 1. 直接使用 GenericConverter
        directUsage();

        // 2. 使用 GenericConversionService
        conversionServiceUsage();

        // 3. Spring 容器集成
        springIntegration();
    }

    /**
     * 演示 1：直接使用 GenericConverter。
     * <p>
     * 这是最基础的使用方式，不依赖 Spring 容器。
     */
    private static void directUsage() {
        System.out.println("【1. 直接使用 GenericConverter】");

        NumberToMoneyConverter converter = new NumberToMoneyConverter();

        // 查看支持的转换类型
        System.out.println("支持的转换类型:");
        converter.getConvertibleTypes().forEach(pair ->
                System.out.println("  " + pair.getSourceType().getSimpleName() +
                        " → " + pair.getTargetType().getSimpleName()));
        System.out.println();

        // 执行转换
        Object money1 = converter.convert(100,
                TypeDescriptor.valueOf(Integer.class),
                TypeDescriptor.valueOf(Money.class));
        System.out.println("转换结果: " + money1);

        Object money2 = converter.convert(199.99,
                TypeDescriptor.valueOf(Double.class),
                TypeDescriptor.valueOf(Money.class));
        System.out.println("转换结果: " + money2);

        Object money3 = converter.convert(new BigDecimal("1234567.89"),
                TypeDescriptor.valueOf(BigDecimal.class),
                TypeDescriptor.valueOf(Money.class));
        System.out.println("转换结果: " + money3);

        System.out.println();
    }

    /**
     * 演示 2：使用 GenericConversionService。
     * <p>
     * GenericConversionService 是 Spring 类型转换的核心，
     * 可以注册多种转换器并统一调用。
     */
    private static void conversionServiceUsage() {
        System.out.println("【2. 使用 GenericConversionService】");

        // 创建 ConversionService（包含 Spring 内置转换器）
        GenericConversionService conversionService = new DefaultConversionService();

        // 注册自定义 GenericConverter
        conversionService.addConverter(new NumberToMoneyConverter());
        conversionService.addConverter(new StringToCollectionConverter(conversionService));

        // Number → Money 转换
        Money money = conversionService.convert(888L, Money.class);
        System.out.println("转换结果: " + money);

        // String → List<Integer> 转换
        TypeDescriptor sourceType = TypeDescriptor.valueOf(String.class);
        TypeDescriptor targetType = TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(Integer.class));
        @SuppressWarnings("unchecked")
        List<Integer> intList = (List<Integer>) conversionService.convert("1,2,3,4,5", sourceType, targetType);
        System.out.println("转换结果: " + intList);

        // String → Set<String> 转换
        TypeDescriptor setType = TypeDescriptor.collection(Set.class, TypeDescriptor.valueOf(String.class));
        @SuppressWarnings("unchecked")
        Set<String> stringSet = (Set<String>) conversionService.convert("apple,banana,orange", sourceType, setType);
        System.out.println("转换结果: " + stringSet);

        System.out.println();
    }

    /**
     * 演示 3：通过 Spring 容器集成 GenericConverter。
     * <p>
     * 通过配置类注册 GenericConverter 后，
     * 在 Spring Bean 中注入 ConversionService 即可使用。
     */
    private static void springIntegration() {
        System.out.println("【3. Spring 容器集成】");

        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext()) {

            // 注册配置类
            context.register(GenericConverterConfig.class);
            context.refresh();

            // 获取 ConversionService
            ConversionService conversionService = context.getBean(ConversionService.class);

            // 使用容器中的 ConversionService 进行转换
            System.out.println("\n使用容器中的 ConversionService:");
            Money money = conversionService.convert(9999, Money.class);
            System.out.println("转换结果: " + money);

            // 复杂类型转换
            TypeDescriptor sourceType = TypeDescriptor.valueOf(String.class);
            TypeDescriptor targetType = TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(Double.class));
            @SuppressWarnings("unchecked")
            List<Double> doubleList = (List<Double>) conversionService.convert("1.1,2.2,3.3", sourceType, targetType);
            System.out.println("转换结果: " + doubleList);
        }

        System.out.println();
    }
}
