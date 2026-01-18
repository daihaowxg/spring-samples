/**
 * 第 4 阶段：ResourcePatternResolver 批量加载
 * <p>
 * 本包演示了 ResourcePatternResolver 接口的用法，它支持通配符批量加载资源。
 * <p>
 * <b>📚 阅读顺序：</b>
 * <ol>
 * <li>{@link io.github.daihaowxg._01_spring_resource._04_pattern_resolver.ResourcePatternResolverDemo
 * ResourcePatternResolverDemo}
 * - Ant 风格路径匹配 + classpath* 跨 JAR 扫描</li>
 * </ol>
 * <p>
 * <b>💡 学习重点：</b>
 * <ul>
 * <li>Ant 风格通配符：* 和 ** 的区别</li>
 * <li>classpath: vs classpath*: 的区别（单 JAR vs 跨 JAR）</li>
 * <li>PathMatchingResourcePatternResolver 的使用</li>
 * </ul>
 *
 * @see io.github.daihaowxg._01_spring_resource._03_resource_loader
 *      上一阶段：ResourceLoader
 * @see io.github.daihaowxg._01_spring_resource._05_utils 下一阶段：工具类
 */
package io.github.daihaowxg._01_spring_resource._04_pattern_resolver;
