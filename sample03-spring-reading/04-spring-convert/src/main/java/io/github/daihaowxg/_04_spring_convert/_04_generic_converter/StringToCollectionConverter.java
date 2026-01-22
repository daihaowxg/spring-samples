package io.github.daihaowxg._04_spring_convert._04_generic_converter;

import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;

import java.util.*;

/**
 * ConditionalGenericConverter 进阶示例：将逗号分隔的字符串转换为 Collection。
 * <p>
 * 支持的转换：
 * <ul>
 *     <li>{@code "1,2,3"} → {@code List<Integer>}</li>
 *     <li>{@code "a,b,c"} → {@code Set<String>}</li>
 *     <li>{@code "1.1,2.2,3.3"} → {@code List<Double>}</li>
 * </ul>
 * <p>
 * 关键特性：
 * <ul>
 *     <li>使用 {@code TypeDescriptor.getElementTypeDescriptor()} 获取集合元素类型</li>
 *     <li>实现 {@code matches()} 方法进行条件匹配</li>
 *     <li>依赖 {@code ConversionService} 递归转换元素</li>
 * </ul>
 */
public class StringToCollectionConverter implements ConditionalGenericConverter {

    private final ConversionService conversionService;

    /**
     * 创建转换器，需要 ConversionService 来递归转换集合元素。
     *
     * @param conversionService Spring 转换服务
     */
    public StringToCollectionConverter(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    /**
     * 声明支持的转换类型对。
     *
     * @return String → Collection 的类型对
     */
    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return Collections.singleton(new ConvertiblePair(String.class, Collection.class));
    }

    /**
     * 条件匹配：判断是否能处理特定的转换请求。
     * <p>
     * 只有当目标 Collection 的元素类型已知，且 ConversionService 能转换该元素类型时，
     * 才返回 true。
     *
     * @param sourceType 源类型描述符
     * @param targetType 目标类型描述符
     * @return 如果可以处理返回 true
     */
    @Override
    public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
        TypeDescriptor elementType = targetType.getElementTypeDescriptor();
        if (elementType == null) {
            // 没有指定元素类型，默认可以处理（元素类型为 String）
            return true;
        }
        // 检查 ConversionService 是否能将 String 转换为目标元素类型
        return this.conversionService.canConvert(String.class, elementType.getType());
    }

    /**
     * 执行转换：将逗号分隔的字符串转换为指定类型的 Collection。
     *
     * @param source     源字符串
     * @param sourceType 源类型描述符
     * @param targetType 目标类型描述符（包含集合元素类型信息）
     * @return 转换后的 Collection
     */
    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        if (source == null) {
            return null;
        }

        String text = (String) source;
        if (text.isBlank()) {
            return createCollection(targetType.getType(), 0);
        }

        String[] elements = text.split(",");
        Collection<Object> result = createCollection(targetType.getType(), elements.length);

        // 获取目标集合的元素类型
        TypeDescriptor elementType = targetType.getElementTypeDescriptor();
        Class<?> targetElementClass = (elementType != null) ? elementType.getType() : String.class;

        for (String element : elements) {
            String trimmed = element.trim();
            Object converted;
            if (targetElementClass == String.class) {
                converted = trimmed;
            } else {
                converted = this.conversionService.convert(trimmed, targetElementClass);
            }
            result.add(converted);
        }

        System.out.println("  [StringToCollectionConverter] \"" + text + "\" → " +
                result.getClass().getSimpleName() + "<" + targetElementClass.getSimpleName() + ">" + result);

        return result;
    }

    /**
     * 根据目标类型创建对应的 Collection 实例。
     */
    private Collection<Object> createCollection(Class<?> collectionType, int initialCapacity) {
        if (Set.class.isAssignableFrom(collectionType)) {
            return new LinkedHashSet<>(initialCapacity);
        } else if (List.class.isAssignableFrom(collectionType) || Collection.class == collectionType) {
            return new ArrayList<>(initialCapacity);
        } else {
            throw new IllegalArgumentException("不支持的集合类型: " + collectionType);
        }
    }
}
