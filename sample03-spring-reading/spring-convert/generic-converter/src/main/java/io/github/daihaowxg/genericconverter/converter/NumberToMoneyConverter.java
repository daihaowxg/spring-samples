package io.github.daihaowxg.genericconverter.converter;

import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * GenericConverter 核心示例：将多种数字类型转换为 Money。
 * <p>
 * 支持的源类型：
 * <ul>
 *     <li>{@code Integer} → {@code Money}</li>
 *     <li>{@code Long} → {@code Money}</li>
 *     <li>{@code Double} → {@code Money}</li>
 *     <li>{@code BigDecimal} → {@code Money}</li>
 * </ul>
 * <p>
 * 这是 GenericConverter 的典型应用场景：<b>多对一转换</b>。
 * 如果使用 Converter 接口，需要为每种数字类型编写一个独立的转换器。
 */
public class NumberToMoneyConverter implements GenericConverter {

    /**
     * 声明支持的转换类型对。
     * <p>
     * 返回的 Set 包含所有支持的 (源类型, 目标类型) 对。
     *
     * @return 可转换类型对集合
     */
    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        Set<ConvertiblePair> pairs = new HashSet<>();
        pairs.add(new ConvertiblePair(Integer.class, Money.class));
        pairs.add(new ConvertiblePair(Long.class, Money.class));
        pairs.add(new ConvertiblePair(Double.class, Money.class));
        pairs.add(new ConvertiblePair(BigDecimal.class, Money.class));
        return pairs;
    }

    /**
     * 执行类型转换。
     *
     * @param source     源对象（数字类型）
     * @param sourceType 源类型描述符，包含类型元信息
     * @param targetType 目标类型描述符
     * @return 转换后的 Money 对象
     */
    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        if (source == null) {
            return null;
        }

        BigDecimal amount;

        // 根据源类型进行转换
        Class<?> sourceClass = sourceType.getType();
        if (sourceClass == Integer.class) {
            amount = BigDecimal.valueOf((Integer) source);
        } else if (sourceClass == Long.class) {
            amount = BigDecimal.valueOf((Long) source);
        } else if (sourceClass == Double.class) {
            amount = BigDecimal.valueOf((Double) source);
        } else if (sourceClass == BigDecimal.class) {
            amount = (BigDecimal) source;
        } else {
            throw new IllegalArgumentException("不支持的源类型: " + sourceClass);
        }

        Money money = new Money(amount);
        System.out.println("  [NumberToMoneyConverter] " + source + " (" + sourceClass.getSimpleName() + ") → " + money);

        return money;
    }
}
