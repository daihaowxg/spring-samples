package io.github.daihaowxg.conversionservice.service;

import org.springframework.core.convert.converter.Converter;

/**
 * Address → String 类型转换器。
 * <p>
 * 将 Address 对象转换为格式化的字符串（例如 "广东省/深圳市/南山区"）。
 * <p>
 * 这是 {@link StringToAddressConverter} 的逆向转换器，
 * 两者配合实现 Address 与 String 之间的双向转换。
 */
public class AddressToStringConverter implements Converter<Address, String> {

    private static final String DELIMITER = "/";

    @Override
    public String convert(Address source) {
        if (source == null) {
            return null;
        }

        return String.join(DELIMITER,
                source.getProvince(),
                source.getCity(),
                source.getDistrict()
        );
    }
}
