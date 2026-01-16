package io.github.daihaowxg._04_spring_convert._05_conditional_converter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.convert.support.GenericConversionService;

/**
 * Spring 配置类：注册自定义的 ConditionalConverter。
 * <p>
 * 通过配置类将 ConditionalConverter 注册到 ConversionService，
 * 使其可以在 Spring 应用中全局使用。
 */
@Configuration
public class ConditionalConverterConfig {

    /**
     * 创建并配置 ConversionService。
     * <p>
     * 注册自定义的 ConditionalConverter：
     * <ul>
     *     <li>{@link StringToPersonConverter} - String → Person 条件转换</li>
     *     <li>{@link AnnotationAwareDateConverter} - 注解感知的日期转换</li>
     * </ul>
     *
     * @return 配置好的 ConversionService
     */
    @Bean
    public ConversionService conversionService() {
        GenericConversionService conversionService = new DefaultConversionService();

        // 注册自定义的 ConditionalConverter
        conversionService.addConverter(new StringToPersonConverter());
        conversionService.addConverter(new AnnotationAwareDateConverter());

        System.out.println("[ConditionalConverterConfig] 已注册自定义 ConditionalConverter:");
        System.out.println("  - StringToPersonConverter (String → Person)");
        System.out.println("  - AnnotationAwareDateConverter (String → Date, 支持 @DateFormat 注解)");

        return conversionService;
    }
}
