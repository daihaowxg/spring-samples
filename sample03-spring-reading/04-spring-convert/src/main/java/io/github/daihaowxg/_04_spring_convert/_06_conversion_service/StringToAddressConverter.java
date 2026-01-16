package io.github.daihaowxg._04_spring_convert._06_conversion_service;

import org.springframework.core.convert.converter.Converter;

/**
 * String → Address 类型转换器。
 * <p>
 * 将格式化的字符串（例如 "广东省/深圳市/南山区"）转换为 Address 对象。
 * <p>
 * 格式规范：
 * <ul>
 *     <li>使用 "/" 作为分隔符</li>
 *     <li>顺序：省份/城市/区县</li>
 *     <li>必须包含三个部分</li>
 * </ul>
 * <p>
 * 这是一个典型的 {@link Converter} 实现，演示如何将字符串解析为自定义对象。
 */
public class StringToAddressConverter implements Converter<String, Address> {

    private static final String DELIMITER = "/";

    @Override
    public Address convert(String source) {
        if (source == null || source.isBlank()) {
            return null;
        }

        String[] parts = source.split(DELIMITER);
        if (parts.length != 3) {
            throw new IllegalArgumentException(
                    "地址格式错误，期望格式: 省份/城市/区县，实际: " + source);
        }

        return new Address(
                parts[0].trim(),
                parts[1].trim(),
                parts[2].trim()
        );
    }
}
