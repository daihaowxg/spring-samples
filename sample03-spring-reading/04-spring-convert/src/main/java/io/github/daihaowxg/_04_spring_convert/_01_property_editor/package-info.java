/**
 * 第 1 阶段：PropertyEditor (JDK 标准转换)
 * <p>
 * 本包演示了基于 JavaBeans {@link java.beans.PropertyEditor} 的类型转换机制。
 * 这是 Spring 早期（及 data binding）的核心转换方式，主要用于 String &lt;-&gt; Object。
 * <p>
 * <b>📚 阅读顺序：</b>
 * <ol>
 *   <li>{@link io.github.daihaowxg._04_spring_convert._01_property_editor.PropertyEditorDemo
 *       PropertyEditorDemo}
 *       - 演示自定义 Editor 并注册到 Spring DataBinder</li>
 * </ol>
 *
 * @see io.github.daihaowxg._04_spring_convert._02_converter 下一阶段：Converter (Spring 3.0+ 标准)
 */
package io.github.daihaowxg._04_spring_convert._01_property_editor;
