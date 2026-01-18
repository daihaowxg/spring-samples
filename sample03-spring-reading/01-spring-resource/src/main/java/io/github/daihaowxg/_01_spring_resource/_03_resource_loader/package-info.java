/**
 * 第 3 阶段：ResourceLoader 策略加载
 * <p>
 * 本包演示了 Spring ResourceLoader 接口的用法，它是加载单个资源的中央策略接口。
 * <p>
 * <b>📚 阅读顺序：</b>
 * <ol>
 * <li>{@link io.github.daihaowxg._01_spring_resource._03_resource_loader.DefaultResourceLoaderDemo
 * DefaultResourceLoaderDemo}
 * - 默认实现，支持 classpath:/file:/http: 前缀</li>
 * <li>{@link io.github.daihaowxg._01_spring_resource._03_resource_loader.FileSystemResourceLoaderDemo
 * FileSystemResourceLoaderDemo}
 * - 无前缀时默认从文件系统加载</li>
 * <li>{@link io.github.daihaowxg._01_spring_resource._03_resource_loader.ResourceLoaderAwareDemo
 * ResourceLoaderAwareDemo}
 * - 通过回调接口获取 ResourceLoader</li>
 * <li>{@link io.github.daihaowxg._01_spring_resource._03_resource_loader.ApplicationContextAsResourceLoaderDemo
 * ApplicationContextAsResourceLoaderDemo}
 * - ApplicationContext 本身就是 ResourceLoader</li>
 * </ol>
 * <p>
 * <b>💡 学习重点：</b>
 * <ul>
 * <li>协议前缀（classpath:, file:, http:）的作用</li>
 * <li>在 Spring 容器中获取 ResourceLoader 的三种方式</li>
 * </ul>
 *
 * @see io.github.daihaowxg._01_spring_resource._02_resource_interface
 *      上一阶段：Resource 接口
 * @see io.github.daihaowxg._01_spring_resource._04_pattern_resolver 下一阶段：批量加载
 */
package io.github.daihaowxg._01_spring_resource._03_resource_loader;
