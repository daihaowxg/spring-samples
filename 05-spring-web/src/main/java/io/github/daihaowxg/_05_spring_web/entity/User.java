package io.github.daihaowxg._05_spring_web.entity;

/**
 * 用户实体类
 * <p>
 * 用于演示 Spring Web MVC 的请求处理流程：
 * <ul>
 * <li>@RequestBody JSON 反序列化</li>
 * <li>@ResponseBody JSON 序列化</li>
 * </ul>
 *
 * @author daihaowxg
 * @since 2026-01-19
 */
public class User {

    private Long id;
    private String name;
    private String email;

    public User() {
    }

    public User(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public User(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
