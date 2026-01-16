package io.github.daihaowxg._04_spring_convert._01_property_editor;

import java.beans.PropertyEditorSupport;

/**
 * 自定义 PropertyEditor：将字符串转换为 Address 对象。
 * <p>
 * 支持 "省/市/街道" 格式的双向转换：
 * <ul>
 *     <li>{@code setAsText("广东省/深圳市/南山区")} → {@code Address("广东省", "深圳市", "南山区")}</li>
 *     <li>{@code getAsText()} → {@code "广东省/深圳市/南山区"}</li>
 * </ul>
 */
public class AddressEditor extends PropertyEditorSupport {

    /**
     * 将字符串转换为 Address 对象。
     * <p>
     * 期望格式：省/市/街道（使用 "/" 分隔）
     *
     * @param text 待转换的字符串
     * @throws IllegalArgumentException 如果格式不正确
     */
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (text == null || text.isBlank()) {
            setValue(null);
            return;
        }

        String[] parts = text.split("/");
        if (parts.length != 3) {
            throw new IllegalArgumentException(
                    "地址格式错误，期望 '省/市/街道'，实际: " + text);
        }

        Address address = new Address(
                parts[0].trim(),
                parts[1].trim(),
                parts[2].trim()
        );
        setValue(address);

        System.out.println("  [AddressEditor] setAsText: \"" + text + "\" → " + address);
    }

    /**
     * 将 Address 对象转换为字符串。
     *
     * @return "省/市/街道" 格式的字符串
     */
    @Override
    public String getAsText() {
        Address address = (Address) getValue();
        if (address == null) {
            return "";
        }

        String result = address.getProvince() + "/" + address.getCity() + "/" + address.getStreet();
        System.out.println("  [AddressEditor] getAsText: " + address + " → \"" + result + "\"");
        return result;
    }
}
