package io.github.daihaowxg._03_spring_validator._01_validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * User 对象的自定义验证器
 * <p>
 * 实现 Spring {@link Validator} 接口，演示最基础的验证逻辑。
 * <p>
 * 验证规则：
 * <ul>
 *   <li>username: 不能为空，长度 3-20 个字符</li>
 *   <li>email: 不能为空，必须包含 @ 符号</li>
 *   <li>age: 如果存在，必须在 0-150 之间</li>
 * </ul>
 */
public class UserValidator implements Validator {

    /**
     * 判断该验证器是否支持验证给定的类
     *
     * @param clazz 待验证的类
     * @return 如果支持验证则返回 true
     */
    @Override
    public boolean supports(Class<?> clazz) {
        // 验证 User 类及其子类
        return User.class.isAssignableFrom(clazz);
    }

    /**
     * 执行验证逻辑
     *
     * @param target 待验证的对象
     * @param errors 用于收集验证错误的对象
     */
    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;

        // 1. 使用 ValidationUtils 工具类检查空值
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "username.required", "用户名不能为空");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "email.required", "邮箱不能为空");

        // 2. 自定义验证逻辑：用户名长度
        String username = user.getUsername();
        if (username != null && (username.length() < 3 || username.length() > 20)) {
            errors.rejectValue("username", "username.length", "用户名长度必须在 3-20 个字符之间");
        }

        // 3. 自定义验证逻辑：邮箱格式（简单检查）
        String email = user.getEmail();
        if (email != null && !email.contains("@")) {
            errors.rejectValue("email", "email.invalid", "邮箱格式不正确");
        }

        // 4. 自定义验证逻辑：年龄范围
        Integer age = user.getAge();
        if (age != null && (age < 0 || age > 150)) {
            errors.rejectValue("age", "age.range", "年龄必须在 0-150 之间");
        }
    }
}
