package io.github.daihaowxg.propertyeditor.editor;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期格式 PropertyEditor：将字符串转换为 Date 对象。
 * <p>
 * 支持 "yyyy-MM-dd" 格式的双向转换：
 * <ul>
 *     <li>{@code setAsText("2025-01-13")} → {@code Date} 对象</li>
 *     <li>{@code getAsText()} → {@code "2025-01-13"}</li>
 * </ul>
 *
 * <p><strong>注意</strong>：{@code SimpleDateFormat} 非线程安全，
 * 每次 setAsText/getAsText 调用都会创建新实例。生产环境建议使用
 * {@code java.time.format.DateTimeFormatter}。</p>
 */
public class DateEditor extends PropertyEditorSupport {

    private static final String DATE_PATTERN = "yyyy-MM-dd";

    /**
     * 将 "yyyy-MM-dd" 格式的字符串转换为 Date 对象。
     *
     * @param text 待转换的字符串
     * @throws IllegalArgumentException 如果日期格式不正确
     */
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (text == null || text.isBlank()) {
            setValue(null);
            return;
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
            sdf.setLenient(false);  // 严格模式，不允许 2025-02-30 这样的日期
            Date date = sdf.parse(text.trim());
            setValue(date);

            System.out.println("  [DateEditor] setAsText: \"" + text + "\" → " + date);
        } catch (ParseException e) {
            throw new IllegalArgumentException(
                    "日期格式错误，期望 '" + DATE_PATTERN + "'，实际: " + text, e);
        }
    }

    /**
     * 将 Date 对象转换为 "yyyy-MM-dd" 格式的字符串。
     *
     * @return 格式化后的日期字符串
     */
    @Override
    public String getAsText() {
        Date date = (Date) getValue();
        if (date == null) {
            return "";
        }

        SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
        String result = sdf.format(date);

        System.out.println("  [DateEditor] getAsText: " + date + " → \"" + result + "\"");
        return result;
    }
}
