/**
 * 第 2 阶段：Spring Resource 接口及其实现类
 * <p>
 * 本包演示了 Spring Resource 接口的核心用法，这是 Spring 资源抽象的基础。
 * <p>
 * <b>📚 阅读顺序：</b>
 * <ol>
 * <li>{@link io.github.daihaowxg._01_spring_resource._02_resource_interface.ClassPathResourceDemo
 * ClassPathResourceDemo}
 * - 类路径资源（最常用）</li>
 * <li>{@link io.github.daihaowxg._01_spring_resource._02_resource_interface.FileSystemResourceDemo
 * FileSystemResourceDemo}
 * - 文件系统资源</li>
 * <li>{@link io.github.daihaowxg._01_spring_resource._02_resource_interface.UrlResourceDemo
 * UrlResourceDemo}
 * - 网络资源</li>
 * <li>{@link io.github.daihaowxg._01_spring_resource._02_resource_interface.ByteArrayResourceDemo
 * ByteArrayResourceDemo}
 * - 内存资源（常用于测试）</li>
 * </ol>
 * <p>
 * <b>💡 学习重点：</b>
 * <ul>
 * <li>Resource 接口的统一 API（exists(), isReadable(), getInputStream() 等）</li>
 * <li>不同实现类的适用场景</li>
 * </ul>
 *
 * @see io.github.daihaowxg._01_spring_resource._01_jdk_basics 上一阶段：JDK 原生方式
 * @see io.github.daihaowxg._01_spring_resource._03_resource_loader
 *      下一阶段：ResourceLoader
 */
package io.github.daihaowxg._01_spring_resource._02_resource_interface;
