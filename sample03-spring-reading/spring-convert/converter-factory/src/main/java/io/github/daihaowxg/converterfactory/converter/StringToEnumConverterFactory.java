package io.github.daihaowxg.converterfactory.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

/**
 * ConverterFactory 实现示例：将字符串转换为任意枚举类型。
 * <p>
 * <b>核心价值</b>：一个工厂为所有枚举类型提供转换器，避免为每个枚举类型单独编写 Converter。
 * <pre>
 * ConverterFactory&lt;String, Enum&gt;
 *     └── getConverter(Color.class)  → Converter&lt;String, Color&gt;
 *     └── getConverter(Priority.class) → Converter&lt;String, Priority&gt;
 *     └── getConverter(XxxEnum.class) → Converter&lt;String, XxxEnum&gt;
 * </pre>
 *
 * @see org.springframework.core.convert.converter.ConverterFactory
 * @see org.springframework.core.convert.support.StringToEnumConverterFactory Spring 内置实现
 */
public class StringToEnumConverterFactory implements ConverterFactory<String, Enum> {

    /**
     * 根据目标枚举类型返回对应的 Converter。
     * <p>
     * 这里的关键设计是：工厂只需实现一次，即可为所有 Enum 子类提供转换器。
     *
     * @param targetType 目标枚举类型
     * @param <T>        枚举类型泛型
     * @return 字符串到目标枚举的转换器
     */
    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public <T extends Enum> Converter<String, T> getConverter(Class<T> targetType) {
        return new StringToEnumConverter<>(targetType);
    }

    /**
     * 内部 Converter 实现：将字符串转换为指定的枚举类型。
     *
     * @param <T> 枚举类型泛型
     */
    private static class StringToEnumConverter<T extends Enum<T>> implements Converter<String, T> {

        private final Class<T> enumType;

        StringToEnumConverter(Class<T> enumType) {
            this.enumType = enumType;
        }

        /**
         * 执行转换：String → Enum。
         * <p>
         * 转换逻辑：
         * <ol>
         *     <li>去除字符串两端空格</li>
         *     <li>转为大写（支持大小写不敏感）</li>
         *     <li>使用 {@link Enum#valueOf} 进行转换</li>
         * </ol>
         *
         * @param source 源字符串
         * @return 转换后的枚举值
         * @throws IllegalArgumentException 如果字符串不匹配任何枚举值
         */
        @Override
        public T convert(String source) {
            if (source == null || source.isBlank()) {
                return null;
            }
            String trimmed = source.trim().toUpperCase();
            T result = Enum.valueOf(enumType, trimmed);
            System.out.println("  [StringToEnumConverter] \"" + source + "\" → " + result);
            return result;
        }
    }
}
