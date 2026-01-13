package io.github.daihaowxg.conditionalconverter.converter;

import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalConverter;
import org.springframework.core.convert.converter.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ConditionalConverter 进阶示例：注解感知的日期转换器。
 * <p>
 * 演示 ConditionalConverter 的核心能力：<b>基于 TypeDescriptor 读取目标字段的注解</b>。
 * <p>
 * 工作原理：
 * <ol>
 *     <li>{@code matches()} 方法检查目标类型是否有 {@code @DateFormat} 注解</li>
 *     <li>如果有注解，使用注解指定的格式进行转换</li>
 *     <li>如果没有注解，使用默认格式 "yyyy-MM-dd"</li>
 * </ol>
 *
 * @see DateFormat
 * @see ConditionalConverter
 */
public class AnnotationAwareDateConverter implements Converter<String, Date>, ConditionalConverter {

    /**
     * 默认日期格式。
     */
    private static final String DEFAULT_PATTERN = "yyyy-MM-dd";

    /**
     * 用于保存当前转换使用的格式（通过 matches 传递给 convert）。
     * <p>
     * 注意：在实际生产环境中，应考虑线程安全问题，
     * 这里为了演示简化处理。
     */
    private final ThreadLocal<String> currentPattern = new ThreadLocal<>();

    /**
     * 条件匹配：判断是否应该处理此转换。
     * <p>
     * 检查目标字段是否有 {@code @DateFormat} 注解，
     * 并记录注解中指定的格式。
     *
     * @param sourceType 源类型描述符
     * @param targetType 目标类型描述符
     * @return 始终返回 true（此转换器处理所有 String → Date 转换）
     */
    @Override
    public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
        // 检查目标类型上是否有 @DateFormat 注解
        DateFormat annotation = targetType.getAnnotation(DateFormat.class);

        String pattern;
        if (annotation != null) {
            pattern = annotation.value();
            System.out.println("  [AnnotationAwareDateConverter.matches] 发现 @DateFormat(\"" + pattern + "\")");
        } else {
            pattern = DEFAULT_PATTERN;
            System.out.println("  [AnnotationAwareDateConverter.matches] 未发现注解，使用默认格式: " + pattern);
        }

        // 保存格式供 convert 方法使用
        currentPattern.set(pattern);

        return true;
    }

    /**
     * 执行转换：将字符串转换为 Date 对象。
     *
     * @param source 源字符串
     * @return Date 对象
     * @throws IllegalArgumentException 如果日期格式不正确
     */
    @Override
    public Date convert(String source) {
        String pattern = currentPattern.get();
        if (pattern == null) {
            pattern = DEFAULT_PATTERN;
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            Date date = sdf.parse(source);
            System.out.println("  [AnnotationAwareDateConverter.convert] \"" + source +
                    "\" (格式: " + pattern + ") → " + date);
            return date;
        } catch (ParseException e) {
            throw new IllegalArgumentException("日期解析失败, 输入: \"" + source +
                    "\", 期望格式: " + pattern, e);
        } finally {
            currentPattern.remove(); // 清理 ThreadLocal
        }
    }
}
