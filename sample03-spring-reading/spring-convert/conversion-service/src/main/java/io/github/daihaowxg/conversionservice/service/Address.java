package io.github.daihaowxg.conversionservice.service;

/**
 * 自定义地址类型，用于演示 ConversionService 的类型转换。
 * <p>
 * 格式约定：省份/城市/区县（使用 "/" 分隔）
 */
public class Address {

    private String province;
    private String city;
    private String district;

    public Address() {
    }

    public Address(String province, String city, String district) {
        this.province = province;
        this.city = city;
        this.district = district;
    }

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

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    @Override
    public String toString() {
        return "Address{" +
                "province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                '}';
    }
}
