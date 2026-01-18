/**
 * 第 5 阶段：资源工具类
 * <p>
 * 本包演示 Spring Resource 相关工具类的用法，简化日常开发。
 * <p>
 * <b>📚 阅读顺序：</b>
 * <ol>
 * <li>{@link io.github.daihaowxg._01_spring_resource._05_utils.ResourceUtilsDemo
 * ResourceUtilsDemo}
 * - 静态工具类，路径解析与转换</li>
 * <li>{@link io.github.daihaowxg._01_spring_resource._05_utils.EncodedResourceDemo
 * EncodedResourceDemo}
 * - 带编码的资源包装器，解决乱码问题</li>
 * </ol>
 * <p>
 * <b>💡 学习重点：</b>
 * <ul>
 * <li>ResourceUtils 常用方法：isUrl(), getURL(), getFile()</li>
 * <li>EncodedResource 解决字符编码问题</li>
 * </ul>
 *
 * @see io.github.daihaowxg._01_spring_resource._04_pattern_resolver 上一阶段：批量加载
 * @see io.github.daihaowxg._01_spring_resource._06_advanced 下一阶段：高级主题
 */
@NonNullApi
package io.github.daihaowxg._01_spring_resource._05_utils;

import org.springframework.lang.NonNullApi;
