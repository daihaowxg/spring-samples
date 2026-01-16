package io.github.daihaowxg._04_spring_convert._06_conversion_service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.format.support.DefaultFormattingConversionService;

/**
 * Spring 配置类：演示 ConversionService 的各种创建方式。
 * <p>
 * <strong>ConversionService 实现类层次：</strong>
 * <pre>
 * ConversionService (接口)
 *     ↑
 * ConfigurableConversionService (接口，增加 addConverter 方法)
 *     ↑
 * GenericConversionService (基础实现，无内置转换器)
 *     ↑
 * DefaultConversionService (包含常用内置转换器)
 *     ↑
 * FormattingConversionService (增加 Formatter 支持)
 *     ↑
 * DefaultFormattingConversionService (包含内置 Formatter)
 * </pre>
 * <p>
 * <strong>选型建议：</strong>
 * <ul>
 *     <li>{@link DefaultConversionService} - 大多数场景的默认选择</li>
 *     <li>{@link GenericConversionService} - 需要完全控制转换器时</li>
 *     <li>{@link DefaultFormattingConversionService} - 需要日期/数字格式化时</li>
 * </ul>
 */
@Configuration
public class ConversionServiceConfig {

    /**
     * 创建 DefaultConversionService 并注册自定义转换器。
     * <p>
     * {@link DefaultConversionService} 预注册了 Spring 内置的常用转换器，包括：
     * <ul>
     *     <li>String ↔ 基本类型（Integer, Long, Boolean 等）</li>
     *     <li>String ↔ 数组</li>
     *     <li>Collection ↔ Collection</li>
     *     <li>Map ↔ Map</li>
     * </ul>
     *
     * @return 配置好的 ConversionService
     */
    @Bean
    public ConversionService conversionService() {
        // 使用 DefaultConversionService，包含内置转换器
        DefaultConversionService conversionService = new DefaultConversionService();

        // 注册自定义转换器
        conversionService.addConverter(new StringToAddressConverter());
        conversionService.addConverter(new AddressToStringConverter());

        System.out.println("[ConversionServiceConfig] 创建 DefaultConversionService");
        System.out.println("  已注册自定义转换器:");
        System.out.println("    - StringToAddressConverter (String → Address)");
        System.out.println("    - AddressToStringConverter (Address → String)");

        return conversionService;
    }

    /**
     * 演示创建 GenericConversionService（无内置转换器）。
     * <p>
     * {@link GenericConversionService} 是最基础的实现，
     * 不包含任何内置转换器，完全由开发者控制。
     * <p>
     * <strong>适用场景：</strong>需要完全自定义转换行为，不需要 Spring 内置转换器。
     *
     * @return 空的 GenericConversionService
     */
    public GenericConversionService createGenericConversionService() {
        GenericConversionService service = new GenericConversionService();
        // 此时只能转换注册过的类型
        service.addConverter(new StringToAddressConverter());
        return service;
    }

    /**
     * 演示创建 DefaultFormattingConversionService。
     * <p>
     * {@link DefaultFormattingConversionService} 在 DefaultConversionService 基础上，
     * 增加了 Formatter 支持，预注册了日期、数字等格式化器。
     * <p>
     * <strong>适用场景：</strong>Web 应用中需要日期/货币本地化格式的场景。
     *
     * @return 支持格式化的 ConversionService
     */
    public ConversionService createFormattingConversionService() {
        DefaultFormattingConversionService service = new DefaultFormattingConversionService();
        service.addConverter(new StringToAddressConverter());
        service.addConverter(new AddressToStringConverter());
        return service;
    }
}
