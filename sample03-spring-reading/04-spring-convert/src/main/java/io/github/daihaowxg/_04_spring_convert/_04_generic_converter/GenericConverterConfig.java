package io.github.daihaowxg._04_spring_convert._04_generic_converter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.convert.support.GenericConversionService;

/**
 * Spring 配置类：注册自定义 GenericConverter 到 ConversionService。
 * <p>
 * 演示如何将 GenericConverter 集成到 Spring 的类型转换体系中。
 */
@Configuration
public class GenericConverterConfig {

    /**
     * 创建并配置 ConversionService。
     * <p>
     * 使用 {@link DefaultConversionService} 作为基础，
     * 它已经包含了 Spring 内置的所有转换器。
     *
     * @return 配置好的 ConversionService
     */
    @Bean
    public ConversionService conversionService() {
        GenericConversionService conversionService = new DefaultConversionService();

        // 注册自定义 GenericConverter
        conversionService.addConverter(new NumberToMoneyConverter());
        conversionService.addConverter(new StringToCollectionConverter(conversionService));

        System.out.println("[GenericConverterConfig] 已注册自定义 GenericConverter:");
        System.out.println("  - NumberToMoneyConverter (Integer/Long/Double/BigDecimal → Money)");
        System.out.println("  - StringToCollectionConverter (String → Collection<T>)");

        return conversionService;
    }
}
