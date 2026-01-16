package io.github.daihaowxg._03_spring_validator._01_validator;

import org.springframework.validation.Errors;
import org.springframework.validation.SmartValidator;
import org.springframework.validation.ValidationUtils;

/**
 * User 对象的智能验证器
 * <p>
 * 实现 Spring {@link SmartValidator} 接口，演示分组验证（Validation Hints）功能。
 * <p>
 * 验证分组：
 * <ul>
 *   <li>{@link BasicValidation}: 基础验证（用户名、邮箱）</li>
 *   <li>{@link FullValidation}: 完整验证（所有字段）</li>
 * </ul>
 */
public class SmartUserValidator implements SmartValidator {

    /**
     * 基础验证分组标记接口
     */
    public interface BasicValidation {
    }

    /**
     * 完整验证分组标记接口
     */
    public interface FullValidation {
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.isAssignableFrom(clazz);
    }

    /**
     * 默认验证（无分组），执行完整验证
     */
    @Override
    public void validate(Object target, Errors errors) {
        validate(target, errors, FullValidation.class);
    }

    /**
     * 带分组的验证
     *
     * @param target          待验证对象
     * @param errors          错误收集器
     * @param validationHints 验证分组（Hints）
     */
    @Override
    public void validate(Object target, Errors errors, Object... validationHints) {
        User user = (User) target;

        // 检查是否需要执行某类验证
        boolean basicValidation = containsHint(validationHints, BasicValidation.class);
        boolean fullValidation = containsHint(validationHints, FullValidation.class);

        // 基础验证：用户名和邮箱（BasicValidation 或 FullValidation 都需要）
        if (basicValidation || fullValidation) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "username.required", "用户名不能为空");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "email.required", "邮箱不能为空");

            String username = user.getUsername();
            if (username != null && (username.length() < 3 || username.length() > 20)) {
                errors.rejectValue("username", "username.length", "用户名长度必须在 3-20 个字符之间");
            }

            String email = user.getEmail();
            if (email != null && !email.contains("@")) {
                errors.rejectValue("email", "email.invalid", "邮箱格式不正确");
            }
        }

        // 完整验证：额外验证年龄（仅 FullValidation）
        if (fullValidation) {
            Integer age = user.getAge();
            if (age == null) {
                errors.rejectValue("age", "age.required", "年龄不能为空");
            } else if (age < 0 || age > 150) {
                errors.rejectValue("age", "age.range", "年龄必须在 0-150 之间");
            }
        }
    }

    /**
     * 检查 validationHints 是否包含指定的分组类
     */
    private boolean containsHint(Object[] hints, Class<?> hintClass) {
        if (hints == null || hints.length == 0) {
            return false;
        }
        for (Object hint : hints) {
            if (hintClass.equals(hint) || hintClass.isInstance(hint)) {
                return true;
            }
        }
        return false;
    }
}
