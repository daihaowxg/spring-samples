package io.github.daihaowxg.converter.converter;

import org.springframework.core.convert.converter.Converter;

/**
 * String → Address 转换器。
 * <p>
 * 实现 Spring 的 {@link Converter} 接口，将 "省/市/街道" 格式的字符串
 * 转换为 {@link Address} 对象。
 *
 * <p><strong>与 PropertyEditor 的区别</strong>：
 * <ul>
 *     <li>Converter 是线程安全的，可以作为单例使用</li>
 *     <li>Converter 支持任意类型间转换，不仅限于 String</li>
 *     <li>Converter 通过 ConversionService 统一管理</li>
 * </ul>
 */
public class StringToAddressConverter implements Converter<String, Address> {

    /**
     * 将字符串转换为 Address 对象。
     *
     * @param source 待转换的字符串，格式为 "省/市/街道"
     * @return Address 对象
     * @throws IllegalArgumentException 如果格式不正确
     */
    @Override
    public Address convert(String source) {
        if (source == null || source.isBlank()) {
            return null;
        }

        String[] parts = source.split("/");
        if (parts.length != 3) {
            throw new IllegalArgumentException(
                    "地址格式错误，期望 '省/市/街道'，实际: " + source);
        }

        Address address = new Address(
                parts[0].trim(),
                parts[1].trim(),
                parts[2].trim()
        );

        System.out.println("  [StringToAddressConverter] \"" + source + "\" → " + address);
        return address;
    }
}
