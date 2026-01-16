package io.github.daihaowxg._03_spring_validator._01_validator;

import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

/**
 * Spring Validator 接口演示
 * <p>
 * 演示 {@link org.springframework.validation.Validator} 和
 * {@link org.springframework.validation.SmartValidator} 的使用方式。
 * <p>
 * 运行方式：
 * <pre>
 * mvn exec:java -Dexec.mainClass="io.github.daihaowxg._03_spring_validator._01_validator.ValidatorDemo"
 * </pre>
 */
public class ValidatorDemo {

    public static void main(String[] args) {
        System.out.println("=== Spring Validator 接口用法演示 ===\n");

        // 1. 基础 Validator 演示
        demoBasicValidator();

        // 2. SmartValidator 演示
        demoSmartValidator();
    }

    /**
     * 演示基础 Validator 用法
     */
    private static void demoBasicValidator() {
        System.out.println("【1. Validator 基础用法 - UserValidator】");
        System.out.println("─".repeat(50));

        UserValidator validator = new UserValidator();

        // 场景 1：验证有效用户
        System.out.println("\n场景 1：验证有效用户");
        User validUser = new User("zhangsan", "zhangsan@example.com", 25);
        Errors validErrors = validateAndPrint(validator, validUser);
        System.out.println("  验证通过: " + !validErrors.hasErrors());

        // 场景 2：验证空字段
        System.out.println("\n场景 2：验证空字段");
        User emptyUser = new User("", "", null);
        Errors emptyErrors = validateAndPrint(validator, emptyUser);
        printErrors(emptyErrors);

        // 场景 3：验证非法值
        System.out.println("\n场景 3：验证非法值");
        User invalidUser = new User("ab", "invalid-email", 200);
        Errors invalidErrors = validateAndPrint(validator, invalidUser);
        printErrors(invalidErrors);

        System.out.println();
    }

    /**
     * 演示 SmartValidator 分组验证
     */
    private static void demoSmartValidator() {
        System.out.println("【2. SmartValidator 分组验证 - SmartUserValidator】");
        System.out.println("─".repeat(50));

        SmartUserValidator validator = new SmartUserValidator();

        // 用于测试的用户（用户名和邮箱有效，但年龄为空）
        User user = new User("zhangsan", "zhangsan@example.com", null);

        // 场景 1：BasicValidation - 只验证用户名和邮箱
        System.out.println("\n场景 1：BasicValidation（仅验证用户名、邮箱）");
        Errors basicErrors = new BeanPropertyBindingResult(user, "user");
        validator.validate(user, basicErrors, SmartUserValidator.BasicValidation.class);
        System.out.println("  待验证对象: " + user);
        System.out.println("  验证通过: " + !basicErrors.hasErrors());
        if (basicErrors.hasErrors()) {
            printErrors(basicErrors);
        }

        // 场景 2：FullValidation - 验证所有字段
        System.out.println("\n场景 2：FullValidation（验证所有字段，包括年龄）");
        Errors fullErrors = new BeanPropertyBindingResult(user, "user");
        validator.validate(user, fullErrors, SmartUserValidator.FullValidation.class);
        System.out.println("  待验证对象: " + user);
        System.out.println("  验证通过: " + !fullErrors.hasErrors());
        if (fullErrors.hasErrors()) {
            printErrors(fullErrors);
        }

        // 场景 3：默认验证（无 Hints）- 执行完整验证
        System.out.println("\n场景 3：默认验证（无分组）→ 执行 FullValidation");
        Errors defaultErrors = new BeanPropertyBindingResult(user, "user");
        validator.validate(user, defaultErrors);
        System.out.println("  待验证对象: " + user);
        System.out.println("  验证通过: " + !defaultErrors.hasErrors());
        if (defaultErrors.hasErrors()) {
            printErrors(defaultErrors);
        }

        System.out.println();
    }

    /**
     * 执行验证并返回 Errors 对象
     */
    private static Errors validateAndPrint(UserValidator validator, User user) {
        Errors errors = new BeanPropertyBindingResult(user, "user");
        System.out.println("  待验证对象: " + user);

        // 检查 supports
        if (!validator.supports(user.getClass())) {
            System.out.println("  ⚠ 验证器不支持该类型");
            return errors;
        }

        validator.validate(user, errors);
        return errors;
    }

    /**
     * 打印所有验证错误
     */
    private static void printErrors(Errors errors) {
        for (FieldError error : errors.getFieldErrors()) {
            System.out.printf("  ✗ 字段 [%s]: %s (错误码: %s)%n",
                    error.getField(),
                    error.getDefaultMessage(),
                    error.getCode());
        }
    }
}
