package io.github.daihaowxg._04_spring_convert._02_converter;

import org.springframework.core.convert.converter.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * String → Date 转换器。
 * <p>
 * 支持 "yyyy-MM-dd" 格式的字符串转换为 {@link Date} 对象。
 *
 * <p><strong>线程安全说明</strong>：
 * 与 PropertyEditor 不同，Converter 实例可以安全地在多线程环境中共享。
 * 但内部使用的 {@link SimpleDateFormat} 不是线程安全的，因此每次转换
 * 都创建新实例。生产环境建议使用 {@link LocalDate} + {@link DateTimeFormatter}。
 */
public class StringToDateConverter implements Converter<String, Date> {

    private static final String DATE_PATTERN = "yyyy-MM-dd";

    /**
     * 将 "yyyy-MM-dd" 格式的字符串转换为 Date 对象。
     *
     * @param source 待转换的字符串
     * @return Date 对象
     * @throws IllegalArgumentException 如果格式不正确
     */
    @Override
    public Date convert(String source) {
        if (source == null || source.isBlank()) {
            return null;
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
            sdf.setLenient(false);
            Date date = sdf.parse(source.trim());

            System.out.println("  [StringToDateConverter] \"" + source + "\" → " + date);
            return date;
        } catch (ParseException e) {
            throw new IllegalArgumentException(
                    "日期格式错误，期望 '" + DATE_PATTERN + "'，实际: " + source, e);
        }
    }
}
