package io.github.daihaowxg.sample07_config_driven_strategy.dto;

/**
 * 动态策略执行结果
 *
 * @param funcId 功能编号
 * @param configuredBeanName 配置表中声明的 Bean 名称
 * @param appliedBeanName 实际执行的 Bean 名称
 * @param fallback 是否回退到默认实现
 * @param result 执行结果
 */
public record DemoResult(
    String funcId,
    String configuredBeanName,
    String appliedBeanName,
    boolean fallback,
    String result
) {
}
