package io.github.daihaowxg._02_spring_metadata._01_annotation_metadata;

import io.github.daihaowxg._02_spring_metadata._01_annotation_metadata.bean.MyBean;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Component;

import java.util.Map;

public class AnnotationMetadataReflectionDemo {

    public static void main(String[] args) throws Exception {
        // 获取 AnnotationMetadata
        AnnotationMetadata annotationMetadata = AnnotationMetadata.introspect(MyBean.class);

        // org.springframework.core.type.StandardAnnotationMetadata
        System.out.println("AnnotationMetadata impl class is " + annotationMetadata.getClass());

        // 检查 MyBean 类是否被 @Component 注解标记
        boolean isComponent = annotationMetadata.hasAnnotation(Component.class.getName());
        System.out.println("MyBean is a @Component: " + isComponent);

        // 获取 MyBean 类上的注解属性
        if (isComponent) {
            Map<String, Object> annotationAttributes = annotationMetadata.getAnnotationAttributes(Component.class.getName());
            if (annotationAttributes != null) {
                System.out.println("@Component value is " + annotationAttributes.get("value"));
            }
        }
    }
}
