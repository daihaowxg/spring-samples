/**
 * 第三阶段：环境隔离 (Profiles)
 * <p>
 * 本阶段学习如何通过 {@link org.springframework.core.env.Environment} 接口提供的
 * Profiles 支持来实现不同开发环境（如 dev, prod）的配置隔离。
 * <p>
 * 阅读顺序：
 * <ol>
 *   <li>{@link ProfileDemo} - 演示激活 Profile 和条件判断</li>
 * </ol>
 *
 * @see org.springframework.core.env.Environment
 * @see org.springframework.context.annotation.Profile
 */
@NonNullApi
package io.github.daihaowxg._06_spring_environment._03_profiles;

import org.springframework.lang.NonNullApi;
