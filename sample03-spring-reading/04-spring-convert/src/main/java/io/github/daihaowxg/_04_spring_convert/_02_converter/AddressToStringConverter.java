package io.github.daihaowxg._04_spring_convert._02_converter;

import org.springframework.core.convert.converter.Converter;

/**
 * Address → String 反向转换器。
 * <p>
 * 将 {@link Address} 对象转换为 "省/市/街道" 格式的字符串。
 * 演示 Converter 可以实现任意方向的类型转换。
 */
public class AddressToStringConverter implements Converter<Address, String> {

    /**
     * 将 Address 对象转换为字符串。
     *
     * @param source Address 对象
     * @return "省/市/街道" 格式的字符串
     */
    @Override
    public String convert(Address source) {
        if (source == null) {
            return "";
        }

        String result = source.getProvince() + "/" + source.getCity() + "/" + source.getStreet();

        System.out.println("  [AddressToStringConverter] " + source + " → \"" + result + "\"");
        return result;
    }
}
