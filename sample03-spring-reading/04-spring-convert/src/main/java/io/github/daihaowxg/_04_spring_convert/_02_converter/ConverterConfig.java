package io.github.daihaowxg._04_spring_convert._02_converter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;

import java.util.HashSet;
import java.util.Set;

/**
 * Spring 配置类：注册自定义 Converter。
 * <p>
 * 通过 {@link FormattingConversionServiceFactoryBean} 创建 {@link ConversionService}，
 * 并注册自定义的 Converter 实现。
 *
 * <p><strong>ConversionService 体系</strong>：
 * <ul>
 *     <li>{@code ConversionService} - 类型转换的统一入口接口</li>
 *     <li>{@code GenericConversionService} - 基础实现</li>
 *     <li>{@code DefaultConversionService} - 默认实现，包含常用转换器</li>
 *     <li>{@code FormattingConversionService} - 支持格式化的实现</li>
 * </ul>
 */
@Configuration
public class ConverterConfig {

    /**
     * 配置 ConversionService，注册自定义 Converter。
     * <p>
     * FormattingConversionServiceFactoryBean 会创建一个 FormattingConversionService，
     * 它继承自 DefaultConversionService，已包含常用的类型转换器。
     *
     * @return ConversionService 工厂 Bean
     */
    @Bean
    public FormattingConversionServiceFactoryBean conversionService() {
        FormattingConversionServiceFactoryBean factory = new FormattingConversionServiceFactoryBean();

        // 注册自定义 Converter
        Set<Object> converters = new HashSet<>();
        converters.add(new StringToAddressConverter());
        converters.add(new AddressToStringConverter());
        converters.add(new StringToDateConverter());
        factory.setConverters(converters);

        System.out.println("[ConverterConfig] 已注册自定义 Converter:");
        System.out.println("  - StringToAddressConverter");
        System.out.println("  - AddressToStringConverter");
        System.out.println("  - StringToDateConverter");

        return factory;
    }
}
