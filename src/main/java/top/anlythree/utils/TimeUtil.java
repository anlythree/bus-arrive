package top.anlythree.utils;

import top.anlythree.utils.exceptions.AException;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * LocalDate格式或LocalDateTime格式和String类型的转换
 *
 * @author wangli
 * @date 2020/5/16 9:07
 */
public class TimeUtil {

    /**
     * 通用日期格式
     */
    private final static DateTimeFormatter FMT_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * 通用时间格式
     */
    private final static DateTimeFormatter FMT_DATE_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 时间(日期)格式转成String类型
     * @param localDate
     * @return
     */
    public static String dateToString(LocalDate localDate){
        return localDate.toString();
    }

    /**
     * 时间(时间)格式转成String类型
     * @param localDateTime
     * @return
     */
    public static String timeToString(LocalDateTime localDateTime){
        return localDateTime.format(FMT_DATE_TIME);
    }

    /**
     * String格式转成LocalDate类型
     * @param localDate
     * @return
     */
    public static LocalDate stringToDate (String localDate){
        return LocalDate.parse(localDate, FMT_DATE);
    }

    /**
     * String格式转成LocalDateTime类型
     * @param localDateTime
     * @return
     */
    public static LocalDateTime stringToTime (String localDateTime){
        return LocalDateTime.parse(localDateTime,FMT_DATE_TIME);
    }

    /**
     * 时分格式转成LocalDateTime类型
     * @param localDateTime
     * @return
     */
    public static LocalDateTime onlyTimeStrToTime(String localDateTime){
        String dateStr = TimeUtil.dateToString(LocalDate.now());
        return LocalDateTime.parse(dateStr+" "+localDateTime+":00",FMT_DATE_TIME);
    }

    /**
     * LocalDate格式转成LocalDateTime格式，时间区域取值 00：00：00
     * @param localDate
     * @return
     */
    public static LocalDateTime dateToTime(LocalDate localDate){
        return stringToTime(dateToString(localDate)+" 00:00:00");
    }

    /**
     * LocalDateTime格式转成LocalDate格式，时间格式直接去掉
     * @param localDateTime
     * @return
     */
    public static LocalDate timeToDate(LocalDateTime localDateTime){
        return localDateTime.toLocalDate();
    }

    /**
     *  根据时间格式分离出日期和时间，（时间只到分钟，不到秒）
     * @param dateTime
     * @return
     */
    public static String[] getDateAndTimeByTime(LocalDateTime dateTime){
        String time = timeToString(dateTime);
        String[] s = time.split(" ");
        s[1] = s[1].substring(0,s[1].lastIndexOf(":"));
        return s;
    }

    /**
     * 返回当前时间到传入时间之间的时间间隔
     * isNegative() 返回是否是否早于当前时间
     *
     * @param endTime
     * @return
     */
    public static Duration timeInterval(LocalDateTime endTime) {
        return timeInterval(LocalDateTime.now(),endTime);
    }

    /**
     * 返回当前时间到传入时间之间的时间间隔
     * isNegative() 返回是否是否早于当前时间
     *
     * @param startTime
     * @return
     */
    public static Duration timeInterval(LocalDateTime startTime,LocalDateTime endTime) {
        return Duration.between(startTime,endTime);
    }
}
