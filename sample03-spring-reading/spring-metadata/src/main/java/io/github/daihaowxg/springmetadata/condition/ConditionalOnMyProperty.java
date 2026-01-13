package io.github.daihaowxg.springmetadata.condition;

import org.springframework.context.annotation.Conditional;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义条件注解：根据属性值决定是否注册 Bean。
 * <p>
 * 这是一个简化版的 @ConditionalOnProperty 实现。
 * 通过 @Conditional 关联自定义的 Condition 实现类。
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Conditional(OnPropertyCondition.class) // 关联 Condition 实现
public @interface ConditionalOnMyProperty {

    /**
     * 属性名称
     */
    String value();

    /**
     * 期望的属性值。如果为空字符串，则只检查属性是否存在。
     */
    String havingValue() default "";

    /**
     * 当属性不存在时，是否匹配。默认为 false。
     */
    boolean matchIfMissing() default false;
}
