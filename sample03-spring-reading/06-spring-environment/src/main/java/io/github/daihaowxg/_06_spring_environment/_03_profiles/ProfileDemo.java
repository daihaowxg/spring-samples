package io.github.daihaowxg._06_spring_environment._03_profiles;

import org.springframework.core.env.StandardEnvironment;

/**
 * 演示 {@link org.springframework.core.env.Environment} 的 Profiles 管理功能。
 */
public class ProfileDemo {

    public static void main(String[] args) {
        System.out.println("=== Environment Profiles 演示 ===");

        // 1. 创建标准环境（默认包含 JVM 属性和环境变量）
        StandardEnvironment env = new StandardEnvironment();

        // 2. 默认 Profile 情况
        System.out.println("默认激活的 Profiles: " + String.join(", ", env.getDefaultProfiles()));
        System.out.println("当前激活的 Profiles: " + String.join(", ", env.getActiveProfiles()));

        // 3. 设置并激活 Profiles
        env.setActiveProfiles("dev", "test");
        System.out.println("\n手动设置 Profiles 为 dev, test");
        System.out.println("当前激活的 Profiles: " + String.join(", ", env.getActiveProfiles()));

        // 4. 判断逻辑 (acceptsProfiles)
        // 支持逻辑表达式，例如 "dev & !prod"
        System.out.println("\nProfile 匹配测试:");
        System.out.println("  是否激活 dev? " + env.acceptsProfiles(p -> p.test("dev")));
        System.out.println("  是否激活 prod? " + env.acceptsProfiles(p -> p.test("prod")));
        
        // 注意：在 Spring 5.1 之后推荐使用 Profiles 类型进行判断，这里使用 lambda 模拟
        System.out.println("  是否激活 (dev | prod)? " + env.acceptsProfiles(p -> p.test("dev") || p.test("prod")));
    }
}
