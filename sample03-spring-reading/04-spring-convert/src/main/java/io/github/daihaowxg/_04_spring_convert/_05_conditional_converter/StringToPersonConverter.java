package io.github.daihaowxg._04_spring_convert._05_conditional_converter;

import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalConverter;
import org.springframework.core.convert.converter.Converter;

/**
 * ConditionalConverter 基础示例：String → Person 条件转换器。
 * <p>
 * 同时实现 {@link Converter} 和 {@link ConditionalConverter} 接口，
 * 演示如何在转换前进行条件判断。
 * <p>
 * 条件逻辑：
 * <ul>
 *     <li>只有当源字符串包含 ":" 时才进行转换</li>
 *     <li>否则返回 false，让 ConversionService 尝试其他转换器</li>
 * </ul>
 *
 * @see ConditionalConverter
 * @see Converter
 */
public class StringToPersonConverter implements Converter<String, Person>, ConditionalConverter {

    /**
     * 条件匹配：判断是否应该执行此转换。
     * <p>
     * 通过 {@link TypeDescriptor} 可以获取源类型和目标类型的完整元信息，
     * 包括泛型参数、字段注解等。
     *
     * @param sourceType 源类型描述符
     * @param targetType 目标类型描述符
     * @return 如果应该执行转换返回 true，否则返回 false
     */
    @Override
    public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
        // 演示：只有目标类型是 Person 时才匹配
        // 实际上 Converter<String, Person> 已经限定了类型，
        // 这里演示如何基于 TypeDescriptor 进行更复杂的判断
        boolean match = Person.class.isAssignableFrom(targetType.getType());

        System.out.println("  [StringToPersonConverter.matches] " +
                sourceType.getType().getSimpleName() + " → " +
                targetType.getType().getSimpleName() + " = " + match);

        return match;
    }

    /**
     * 执行转换：将字符串转换为 Person 对象。
     *
     * @param source 源字符串，格式 "name:age"
     * @return Person 对象
     */
    @Override
    public Person convert(String source) {
        System.out.println("  [StringToPersonConverter.convert] \"" + source + "\" → Person");
        return Person.fromString(source);
    }
}
