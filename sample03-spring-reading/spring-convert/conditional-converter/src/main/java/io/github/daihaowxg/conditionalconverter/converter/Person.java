package io.github.daihaowxg.conditionalconverter.converter;

import java.util.Objects;

/**
 * 自定义领域对象：人员信息。
 * <p>
 * 用于演示 ConditionalConverter 的条件转换功能。
 * 支持从格式 "name:age" 的字符串进行转换。
 */
public class Person {

    private String name;
    private int age;

    public Person() {
    }

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    /**
     * 从字符串解析 Person 对象。
     * <p>
     * 字符串格式："name:age"，例如 "张三:25"
     *
     * @param text 待解析字符串
     * @return Person 对象
     * @throws IllegalArgumentException 如果格式不正确
     */
    public static Person fromString(String text) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("输入字符串不能为空");
        }

        String[] parts = text.split(":");
        if (parts.length != 2) {
            throw new IllegalArgumentException("格式错误，期望格式: name:age, 实际: " + text);
        }

        String name = parts[0].trim();
        int age;
        try {
            age = Integer.parseInt(parts[1].trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("年龄必须是数字: " + parts[1]);
        }

        return new Person(name, age);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return age == person.age && Objects.equals(name, person.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }

    @Override
    public String toString() {
        return "Person{name='" + name + "', age=" + age + "}";
    }
}
