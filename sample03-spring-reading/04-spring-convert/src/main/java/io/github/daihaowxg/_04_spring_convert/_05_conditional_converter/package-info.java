/**
 * 第 5 阶段：ConditionalConverter (条件增强)
 * <p>
 * 本包演示了 {@link org.springframework.core.convert.converter.ConditionalConverter} 接口。
 * 它通常与其他 Converter 接口组合使用，在转换执行前增加前置条件检查（如检查注解）。
 * <p>
 * <b>📚 阅读顺序：</b>
 * <ol>
 *   <li>{@link io.github.daihaowxg._04_spring_convert._05_conditional_converter.ConditionalConverterDemo
 *       ConditionalConverterDemo}
 *       - 演示仅当字段存在特定注解时才生效的转换器</li>
 * </ol>
 *
 * @see io.github.daihaowxg._04_spring_convert._06_conversion_service 下一阶段：ConversionService (统一门面)
 */
package io.github.daihaowxg._04_spring_convert._05_conditional_converter;
