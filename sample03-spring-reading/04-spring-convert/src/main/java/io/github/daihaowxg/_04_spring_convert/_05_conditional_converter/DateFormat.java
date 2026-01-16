package io.github.daihaowxg._04_spring_convert._05_conditional_converter;

import java.lang.annotation.*;

/**
 * 自定义日期格式注解。
 * <p>
 * 用于演示 ConditionalConverter 的<b>注解感知</b>能力。
 * 转换器可以读取此注解并根据指定的格式进行日期转换。
 * <p>
 * 示例用法：
 * <pre>
 * public class Order {
 *     &#64;DateFormat("yyyy-MM-dd")
 *     private Date createDate;
 *
 *     &#64;DateFormat("yyyy-MM-dd HH:mm:ss")
 *     private Date updateDate;
 * }
 * </pre>
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DateFormat {

    /**
     * 日期格式模式。
     * <p>
     * 使用 {@link java.text.SimpleDateFormat} 的格式规范。
     *
     * @return 日期格式模式，默认 "yyyy-MM-dd"
     */
    String value() default "yyyy-MM-dd";
}
