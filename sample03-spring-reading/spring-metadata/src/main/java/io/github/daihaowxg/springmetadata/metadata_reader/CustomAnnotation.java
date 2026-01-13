package io.github.daihaowxg.springmetadata.metadata_reader;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解，用于演示 MetadataReader 读取注解属性。
 * <p>
 * 注意：Retention 必须是 RUNTIME 或 CLASS，才能被 ASM 读取。
 * 推荐使用 RUNTIME，因为它同时支持 ASM 读取和反射读取。
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomAnnotation {

    /**
     * 模块名称
     */
    String module() default "";

    /**
     * 版本号
     */
    String version() default "1.0";

    /**
     * 是否启用
     */
    boolean enabled() default true;
}
