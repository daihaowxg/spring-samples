package io.github.daihaowxg._02_spring_metadata._04_condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * 自定义 Condition：检查操作系统类型。
 * <p>
 * 当操作系统为 macOS 时，条件为 true。
 * 可用于根据操作系统类型注册不同的 Bean。
 */
public class OnMacOsCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        // 从环境中获取操作系统名称
        String osName = context.getEnvironment().getProperty("os.name", "");

        // macOS 的系统属性值通常是 "Mac OS X"
        boolean isMacOs = osName.toLowerCase().contains("mac");

        System.out.println("  [OnMacOsCondition] os.name = " + osName + ", 匹配结果: " + isMacOs);

        return isMacOs;
    }
}
