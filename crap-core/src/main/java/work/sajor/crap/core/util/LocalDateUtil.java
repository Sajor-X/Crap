package work.sajor.crap.core.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * <p>
 * LocalDate / LocalDateTime 工具类
 * </p>
 *
 * @author Sajor
 * @since 2022-11-29
 */
public class LocalDateUtil {

    // ------------------------------ LocalDateTime ------------------------------

    /**
     * 毫秒时间戳
     */
    public static LocalDateTime toTime(Long milliseconds) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(milliseconds), ZoneId.systemDefault());
    }

    /**
     * 转换时间
     * todo 支持其他格式 yyyy-MM-dd / yyyy-MM-ddTHH:mm:ss
     *
     * @param time yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static LocalDateTime toTime(String time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        try {
            return LocalDateTime.parse(time, formatter);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // ------------------------------ LocalDate ------------------------------

    /**
     * 转换日期
     * todo 支持其他格式 yyyy-MM-dd HH:mm:ss / yyyy-MM-ddTHH:mm:ss
     *
     * @param date yyyy-MM-dd
     * @return
     */
    public static LocalDate toDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            return LocalDate.parse(date, formatter);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 毫秒时间戳
     */
    public static LocalDate toDate(Long milliseconds) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(milliseconds), ZoneId.systemDefault()).toLocalDate();
    }

    // ------------------------------ String ------------------------------

    /**
     * 当前时间
     *
     * @param time true : 返回时间格式 / false:返回日格式
     */
    public static String toString(boolean time) {
        return time ? toString(LocalDateTime.now()) : toString(LocalDate.now());
    }

    /**
     * 时间字符串
     */
    public static String toString(LocalDateTime time) {
        return time == null ? "" : time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 日期字符串
     */
    public static String toString(LocalDate date) {
        return date == null ? "" : date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    /**
     * 获取下划线分隔的时间字符串, 用于临时文件的文件名前缀
     * todo 随机数后缀
     */
    public static String toFileString() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss"));
    }

    // ------------------------------ 毫秒时间戳 ------------------------------

    /**
     * 毫秒时间戳
     */
    public static Long toLong(LocalDate date) {
        return Date.from(date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()).getTime();
    }

    /**
     * 毫秒时间戳
     */
    public static Long toLong(LocalDateTime time) {
        return Date.from(time.atZone(ZoneId.systemDefault()).toInstant()).getTime();
    }

}
