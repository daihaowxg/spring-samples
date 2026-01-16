package io.github.daihaowxg._04_spring_convert._03_converter_factory;

/**
 * 演示用枚举类型：优先级。
 * <p>
 * 用于展示 ConverterFactory 如何用同一个工厂转换多种枚举类型。
 * 支持的值：LOW、MEDIUM、HIGH
 */
public enum Priority {
    LOW("低"),
    MEDIUM("中"),
    HIGH("高");

    private final String chineseName;

    Priority(String chineseName) {
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
