package io.github.daihaowxg.springmetadata.condition;

import jakarta.annotation.Nonnull;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * 自定义 Condition：检查指定的属性是否存在且为 true。
 * <p>
 * 这是一个简化版的 @ConditionalOnProperty 实现。
 * 属性名通过注解属性传递。
 */
public class OnPropertyCondition implements Condition {

    @Override
    public boolean matches(@Nonnull ConditionContext context, AnnotatedTypeMetadata metadata) {
        // 从注解中获取属性名
        var attributes = metadata.getAnnotationAttributes(ConditionalOnMyProperty.class.getName());
        if (attributes == null) {
            return false;
        }

        String propertyName = (String) attributes.get("value");
        String havingValue = (String) attributes.get("havingValue");
        boolean matchIfMissing = (boolean) attributes.get("matchIfMissing");

        // 从环境中获取属性值
        String propertyValue = context.getEnvironment().getProperty(propertyName);

        boolean result;
        if (propertyValue == null) {
            // 属性不存在
            result = matchIfMissing;
            System.out
                    .println("  [OnPropertyCondition] 属性 " + propertyName + " 不存在, matchIfMissing = " + matchIfMissing);
        } else if (havingValue.isEmpty()) {
            // 只检查属性是否存在
            result = true;
            System.out.println("  [OnPropertyCondition] 属性 " + propertyName + " 存在, 值 = " + propertyValue);
        } else {
            // 检查属性值是否匹配
            result = havingValue.equals(propertyValue);
            System.out.println("  [OnPropertyCondition] 属性 " + propertyName + " = " + propertyValue + ", 期望 = "
                    + havingValue + ", 匹配: " + result);
        }

        return result;
    }
}
