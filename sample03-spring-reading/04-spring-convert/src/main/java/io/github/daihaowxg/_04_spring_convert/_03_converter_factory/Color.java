package io.github.daihaowxg._04_spring_convert._03_converter_factory;

/**
 * 演示用枚举类型：颜色。
 * <p>
 * 用于展示 ConverterFactory 如何将字符串转换为枚举类型。
 * 支持的值：RED、GREEN、BLUE
 */
public enum Color {
    RED("红色"),
    GREEN("绿色"),
    BLUE("蓝色");

    private final String chineseName;

    Color(String chineseName) {
        this.chineseName = chineseName;
    }

    public String getChineseName() {
        return chineseName;
    }

    @Override
    public String toString() {
        return name() + "(" + chineseName + ")";
    }
}
