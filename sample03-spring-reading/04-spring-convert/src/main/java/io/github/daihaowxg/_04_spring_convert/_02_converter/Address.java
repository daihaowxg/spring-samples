package io.github.daihaowxg._04_spring_convert._02_converter;

/**
 * 自定义地址类型，用于演示 Converter 的使用。
 * <p>
 * 该类包含省、市、街道三个属性，Converter 可以将
 * "省/市/街道" 格式的字符串转换为该对象，反之亦然。
 */
public class Address {

    private String province;
    private String city;
    private String street;

    public Address() {
    }

    public Address(String province, String city, String street) {
        this.province = province;
        this.city = city;
        this.street = street;
    }

    // region Getters and Setters

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    // endregion

    @Override
    public String toString() {
        return "Address{province='" + province + "', city='" + city + "', street='" + street + "'}";
    }
}
