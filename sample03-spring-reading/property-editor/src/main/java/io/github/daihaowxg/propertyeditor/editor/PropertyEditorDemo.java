package io.github.daihaowxg.propertyeditor.editor;

import org.springframework.beans.SimpleTypeConverter;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Date;

/**
 * PropertyEditor 接口用法演示。
 * <p>
 * 本演示展示三种使用 PropertyEditor 的方式：
 * <ol>
 *     <li>直接使用 PropertyEditor（脱离 Spring 容器）</li>
 *     <li>使用 Spring 的 SimpleTypeConverter</li>
 *     <li>通过 Spring 容器集成 PropertyEditor</li>
 * </ol>
 */
public class PropertyEditorDemo {

    public static void main(String[] args) {
        System.out.println("=== PropertyEditor 接口用法演示 ===\n");

        // 1. 直接使用 PropertyEditor
        directUsage();

        // 2. 使用 Spring 的 SimpleTypeConverter
        simpleTypeConverterUsage();

        // 3. Spring 容器集成
        springIntegration();
    }

    /**
     * 演示 1：直接使用 PropertyEditor。
     * <p>
     * 这是最基础的使用方式，不依赖 Spring 容器。
     */
    private static void directUsage() {
        System.out.println("【1. 直接使用 PropertyEditor】");

        // 创建 AddressEditor 实例
        AddressEditor addressEditor = new AddressEditor();

        // String → Address
        addressEditor.setAsText("广东省/深圳市/南山区");
        Address address = (Address) addressEditor.getValue();
        System.out.println("转换结果: " + address);

        // Address → String
        addressEditor.setValue(new Address("北京市", "朝阳区", "建国路"));
        String text = addressEditor.getAsText();
        System.out.println("转换结果: " + text);

        // DateEditor 演示
        System.out.println();
        DateEditor dateEditor = new DateEditor();
        dateEditor.setAsText("2025-01-13");
        Date date = (Date) dateEditor.getValue();
        System.out.println("转换结果: " + date);

        System.out.println();
    }

    /**
     * 演示 2：使用 Spring 的 SimpleTypeConverter。
     * <p>
     * SimpleTypeConverter 是一个便捷工具，可以注册多个 PropertyEditor，
     * 然后统一调用 convertIfNecessary 进行转换。
     */
    private static void simpleTypeConverterUsage() {
        System.out.println("【2. 使用 Spring SimpleTypeConverter】");

        SimpleTypeConverter converter = new SimpleTypeConverter();

        // 注册自定义 PropertyEditor
        converter.registerCustomEditor(Address.class, new AddressEditor());
        converter.registerCustomEditor(Date.class, new DateEditor());

        // 使用统一的 API 进行转换
        Address address = converter.convertIfNecessary("上海市/浦东新区/陆家嘴", Address.class);
        System.out.println("转换结果: " + address);

        Date date = converter.convertIfNecessary("2026-06-15", Date.class);
        System.out.println("转换结果: " + date);

        System.out.println();
    }

    /**
     * 演示 3：通过 Spring 容器集成 PropertyEditor。
     * <p>
     * 通过 CustomEditorConfigurer 注册 PropertyEditor 后，
     * Spring 在进行属性绑定时会自动使用对应的 Editor。
     */
    private static void springIntegration() {
        System.out.println("【3. Spring 容器集成】");

        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext()) {

            // 注册配置类和示例 Bean
            context.register(PropertyEditorConfig.class);
            context.register(AddressHolder.class);

            // 刷新容器
            context.refresh();

            // 获取 Bean
            AddressHolder holder = context.getBean(AddressHolder.class);
            System.out.println("AddressHolder Bean: " + holder);

            // 演示：使用 BeanWrapper 进行属性设置
            System.out.println("\n使用 BeanWrapper 进行属性设置:");
            org.springframework.beans.BeanWrapper bw =
                    new org.springframework.beans.BeanWrapperImpl(new AddressHolder());
            // 注册 PropertyEditor（BeanWrapper 从容器继承配置）
            bw.registerCustomEditor(Address.class, new AddressEditor());
            bw.setPropertyValue("address", "浙江省/杭州市/西湖区");
            AddressHolder result = (AddressHolder) bw.getWrappedInstance();
            System.out.println("设置后的 AddressHolder: " + result);
        }

        System.out.println();
    }

    /**
     * 演示用的示例 Bean，持有一个 Address 属性。
     */
    @org.springframework.stereotype.Component
    public static class AddressHolder {
        private Address address;

        public Address getAddress() {
            return address;
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        @Override
        public String toString() {
            return "AddressHolder{address=" + address + "}";
        }
    }
}
