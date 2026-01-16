/**
 * 第 1 阶段：JDK 原生资源访问方式
 * <p>
 * 本包演示了 JDK 提供的三种资源访问方式，理解这些是掌握 Spring Resource 的基础。
 * <p>
 * <b>📚 阅读顺序：</b>
 * <ol>
 * <li>{@link io.github.daihaowxg.sample03_resource._01_jdk_basics.FileDemo
 * FileDemo}
 * - java.io.File 访问文件系统资源</li>
 * <li>{@link io.github.daihaowxg.sample03_resource._01_jdk_basics.ClassLoaderDemo
 * ClassLoaderDemo}
 * - ClassLoader 访问类路径资源</li>
 * <li>{@link io.github.daihaowxg.sample03_resource._01_jdk_basics.UrlDemo
 * UrlDemo}
 * - java.net.URL 统一资源访问</li>
 * </ol>
 * <p>
 * <b>💡 学习重点：</b>
 * <ul>
 * <li>理解三种方式各自的适用场景和局限性</li>
 * <li>体会 JDK 原生 API 的不统一性（这是 Spring Resource 要解决的问题）</li>
 * </ul>
 *
 * @see io.github.daihaowxg.sample03_resource._02_resource_interface 下一阶段：Spring
 *      Resource 接口
 */
package io.github.daihaowxg.sample03_resource._01_jdk_basics;
