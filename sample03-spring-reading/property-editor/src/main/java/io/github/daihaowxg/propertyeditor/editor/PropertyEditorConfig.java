package io.github.daihaowxg.propertyeditor.editor;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.factory.config.CustomEditorConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.beans.PropertyEditor;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Spring 配置类：注册自定义 PropertyEditor。
 * <p>
 * 通过 {@link CustomEditorConfigurer} 将自定义的 PropertyEditor
 * 注册到 Spring 容器中，使其能够自动处理类型转换。
 */
@Configuration
public class PropertyEditorConfig {

    /**
     * 注册 CustomEditorConfigurer，用于配置自定义 PropertyEditor。
     * <p>
     * CustomEditorConfigurer 是一个 BeanFactoryPostProcessor，
     * 会在 Bean 实例化之前注册 PropertyEditor。
     *
     * @return CustomEditorConfigurer 实例
     */
    @Bean
    public static CustomEditorConfigurer customEditorConfigurer() {
        CustomEditorConfigurer configurer = new CustomEditorConfigurer();

        // 使用 customEditors 注册 PropertyEditor 类（每次使用创建新实例）
        Map<Class<?>, Class<? extends PropertyEditor>> customEditors = new HashMap<>();
        customEditors.put(Address.class, AddressEditor.class);
        configurer.setCustomEditors(customEditors);

        System.out.println("[PropertyEditorConfig] 已注册自定义 PropertyEditor:");
        System.out.println("  - Address.class → AddressEditor.class");

        return configurer;
    }

    /**
     * 演示：使用 Spring 内置的 CustomDateEditor。
     * <p>
     * Spring 提供了多个内置 PropertyEditor，如：
     * <ul>
     *     <li>CustomDateEditor - 日期转换</li>
     *     <li>CustomNumberEditor - 数字转换</li>
     *     <li>StringTrimmerEditor - 字符串修剪</li>
     *     <li>URLEditor - URL 转换</li>
     * </ul>
     *
     * @return CustomDateEditor 实例
     */
    @Bean
    public CustomDateEditor customDateEditor() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        // 第二个参数：是否允许空值
        return new CustomDateEditor(dateFormat, true);
    }
}
