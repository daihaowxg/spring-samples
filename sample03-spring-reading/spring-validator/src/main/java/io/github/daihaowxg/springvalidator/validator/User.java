package io.github.daihaowxg.springvalidator.validator;

/**
 * 用户领域模型
 * <p>
 * 用于演示 Spring Validator 接口的验证目标对象。
 */
public class User {

    private String username;
    private String email;
    private Integer age;

    public User() {
    }

    public User(String username, String email, Integer age) {
        this.username = username;
        this.email = email;
        this.age = age;
    }

    // Getters and Setters

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{username='" + username + "', email='" + email + "', age=" + age + "}";
    }
}
