package io.github.daihaowxg._04_spring_convert._03_converter_factory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.support.FormattingConversionService;

/**
 * Spring 配置类：注册自定义的 ConverterFactory。
 * <p>
 * 通过 {@link FormattingConversionService} 注册 ConverterFactory，
 * 使其在 Spring 容器中全局生效。
 */
@Configuration
public class ConverterFactoryConfig {

    /**
     * 创建并配置 ConversionService。
     * <p>
     * 注册自定义的 {@link StringToEnumConverterFactory}，使其可以转换所有枚举类型。
     *
     * @return 配置好的 FormattingConversionService
     */
    @Bean
    public FormattingConversionService conversionService() {
        FormattingConversionService conversionService = new FormattingConversionService();

        // 注册自定义的 ConverterFactory
        conversionService.addConverterFactory(new StringToEnumConverterFactory());

        System.out.println("[ConverterFactoryConfig] 已注册自定义 ConverterFactory:");
        System.out.println("  - StringToEnumConverterFactory → 支持所有 Enum 类型的转换");

        return conversionService;
    }
}
