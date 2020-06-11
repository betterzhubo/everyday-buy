package com.seasoning.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


/**
 * 处理时间的类
 */
@Slf4j
public class TimeUtil {
	public static long SECOND = 1000L;
	public static long MINUTE = 60 * SECOND;
	public static long HOUR = 60 * MINUTE;
	public static long DAY = 24 * HOUR;
	public static long MONTH = 30 * DAY;
	public static final long LONG_AGO = 946656000000l;

	/** 自然一月值 */
	public static final int NATURE_JANUARY = 1;
	/** 自然十二月值 */
	public static final int NATURE_DECEMBER = 12;

	// public static DateFormat df = new
	// SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	// public static final String DATE_PATTERN_DATE_TIME2 = "yyyyMMddHHmmss";
	// public static final String DATE_PATTERN_DATE_TIME =
	// "yyyy-MM-dd HH:mm:ss";
	public static final String PATTERN_yyyy_MM_dd = "yyyy-MM-dd";
	public static final String PATTERN_yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";
	public static final String PATTERN_yyyyMMddHHmmss = "yyyyMMddHHmmss";
	public static final String PATTERN_yyyyMMdd = "yyyyMMdd";
	public static final String PATTERN_FORMAT5 = "yyyy/MM/dd HH:mm:ss";
	public static final String PATTERN_yyyyMM = "yyyyMM";

	/**
     * 
     * 描述:获取上个月的最后一天.
     * 
     * @return 时间搓
     */
    public static long getLastMonthDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return calendar.getTimeInMillis();
    }
    /**
     * 
     * 描述:获取上个月的第一天.
     * 
     * @return 时间搓
     */
    public static long getFirstMonthDate() {
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(new Date());
    	calendar.add(Calendar.MONTH, -1);
    	calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
    	return calendar.getTimeInMillis();
    }

	/**
	 * 根据时间搓获取时间差
	 * @return 天，时，分，秒
	 */
	public static String getTimeDifference(long beginTime,long endTime) {
		long l= beginTime - endTime;
		long day=l/(24*60*60*1000);
		long hour=(l/(60*60*1000)-day*24);
		long min=((l/(60*1000))-day*24*60-hour*60);
		long s=(l/1000-day*24*60*60-hour*60*60-min*60);
		return day+"天"+hour+"小时"+min+"分"+s+"秒";
	}
	
	/**
	 * 根据时间搓获取时间差
	 * @return 天
	 */
	public static String getDayDifference(long beginTime,long endTime) {
		long l= beginTime - endTime;
		long day=l/(24*60*60*1000);
		return day+"天";
	}
	
	public static int getToday() {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int ret = year * 10000 + (month + 1) * 100 + day;
		return ret;
	}

	/**
	 * 当天还剩多少秒
	 * 
	 * @return 秒数
	 */
	public static int spaceTime() {
		Calendar c = Calendar.getInstance();
		long now = c.getTimeInMillis();
		c.add(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		long millis = c.getTimeInMillis() - now;
		return (int) (millis / 1000);
	}

	/**
	 * 时区修正值<br>
	 * 东时区为正值 <br>
	 * 西时区是负值
	 * 
	 * @return 时区偏移值(毫秒)
	 */
	private static long getTimeZoneRawOffset() {
		return TimeZone.getDefault().getRawOffset();
	}

	/**
	 * @param lastTime 检查时间
	 * @param time 小时数
	 * @return true: 对比传入时间超过time分钟返回 false否则true
	 * */
	public static boolean checkTime(Date lastTime,int time) {
		return DateUtils.addMinutes(lastTime, time).before(new Date());
	}

	/**
	 * @param beginTime 开始时间
	 * @param endTime 结束时间
	 * @param time 分钟
	 * @return true: 对比lastTime超过12小时 false 对比lastTime不到12小时
	 * */
	public static boolean checkTime(Date beginTime,Date endTime,int time) {
		return DateUtils.addMinutes(endTime, time).before(beginTime);
	}

	public static String format(Date date, String pattern) {
		return DateFormatUtils.format(date, pattern);
		// return (new SimpleDateFormat(pattern)).format(date);
	}

	public static Date parse(String str, String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		Date date = null;
		try {
			date = format.parse(str);
		} catch (ParseException e) {
			log.error(e.getMessage(), e);
		}
		return date;
	}

	public static int subDateToMonth(long minDate, long maxDate) {
		long time = maxDate - minDate;
		time = time > 0 ? time : 0;
		return (int) ((time) / MONTH);
	}

	public static int subDateToMonth(Date minDate, Date maxDate) {
		return subDateToMonth(minDate.getTime(), maxDate.getTime());
	}

	public static int subDateToDay(long minDate, long maxDate) {
		long time = maxDate - minDate;
		time = time > 0 ? time : 0;
		return (int) ((time) / DAY);
	}

	public static int subDateToDay(Date minDate, Date maxDate) {
		return subDateToDay(minDate.getTime(), maxDate.getTime());
	}

	public static int getIntervalDay(Date minDate, Date maxDate) {
		// 需要时区修正
		return (int) ((maxDate.getTime() + getTimeZoneRawOffset()) / DAY - (minDate.getTime() + getTimeZoneRawOffset()) / DAY);
	}

	public static int SubDateToHour(long minDate, long maxDate) {
		long time = maxDate - minDate;
		time = time > 0 ? time : 0;
		return (int) (time / HOUR);
	}

	public static int subDateToMin(long minDate, long maxDate) {
		long time = maxDate - minDate;
		time = time > 0 ? time : 0;
		return (int) (time / MINUTE);
	}

	public static int subDateToMin(Date minDate, Date maxDate) {
		return subDateToMin(minDate.getTime(), maxDate.getTime());
	}

	public static int subDateToSecond(Date minDate, Date maxDate) {
		return subDateToSecond(minDate.getTime(), maxDate.getTime());
	}

	public static int subDateToSecond(long minDate, long maxDate) {
		long time = maxDate - minDate;
		time = time > 0 ? time : 0;
		return (int) (time / SECOND);
	}

	public static long subDateToMilliSecond(Date minDate, Date maxDate) {
		return subDateToMilliSecond(minDate.getTime(), maxDate.getTime());
	}

	public static long subDateToMilliSecond(long minDate, long maxDate) {
		long time = maxDate - minDate;
		time = time > 0 ? time : 0;
		return time;
	}

	public static int getYear() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.YEAR);
	}

	/**
	 * 获取当前系统时间点(0-24小时)
	 * 
	 * @return 当前的小时数
	 */
	public static int getHour() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		return calendar.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * 获取当前系统时间（yyyy_MM_dd_HH_mm_ss）
	 * @param @return    设定文件 
	 * @return String    返回类型 
	 */
	public static String getSystemTime(){
		Date now = new Date(); 
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dateFormat.format(now);
	}
	// ===============================================================
	// 更新刷新时间为21点
	// ===============================================================
	public static int getHourOfDay(Calendar calendar) {
		return calendar.get(Calendar.HOUR_OF_DAY);
	}

	public static int getCurHourOfDay() {
		return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
	}

	// public static int getFormatDate(long date) {
	// return Integer.parseInt(formatDateByPattern(new Date(date), "yyyyMMdd"));
	// }

	/**
	 * 检测date是不是long ago的事件 误差在1s以内的都算long ago的时间
	 * 
	 * @param date 日期
	 * @return true/false
	 */
	public static boolean isLongAgo(Date date) {
		long time = date.getTime();
		return Math.abs(time - LONG_AGO) <= SECOND;
	}

	/**
	 * 判断两个日期是不是同一个月份
	 * 
	 * @param cal1 日期1
	 * @param cal2 日期2
	 * @return true/false
	 */
	public static boolean isSameMonth(Calendar cal1, Calendar cal2) {
		int year1 = cal1.get(Calendar.YEAR);
		int year2 = cal2.get(Calendar.YEAR);
		int month1 = cal1.get(Calendar.MONTH);
		int month2 = cal2.get(Calendar.MONTH);
		return (year1 == year2) && (month1 == month2);
	}

	public static boolean isSameMonthByDate(Date date1, Date date2) {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);
		return isSameMonth(cal1, cal2);
	}

	public static boolean isSameMonth(Date date1, Date date2) {
		return DateUtils.truncatedEquals(date1, date2, Calendar.MONTH);
	}

	/* 仅限1年内有效,返回值为相差几天，比如 2013-10-10 10:00 和2013-10-11 01:00 为相差一天 */
	public static int sub2Days(Date min, Date max) {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(min);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(max);
		return cal2.get(Calendar.DAY_OF_YEAR) - cal1.get(Calendar.DAY_OF_YEAR);

	}

	/**
	 * 是否是同一个星期
	 * 
	 * @param date1 日期1
	 * @param date2 日期2
	 * @return true/false
	 */
	public static boolean isSameWeek(Date date1, Date date2) {
		Date monday1 = getMondayOfThisWeek(date1);
		Date monday2 = getMondayOfThisWeek(date2);
		return DateUtils.isSameDay(monday1, monday2);
	}

	/**
	 * 返回本周周一的时间<br>
	 * 按[周一为一周的第一天]计算
	 * @param date 日期
	 * @return 本周周一的时间
	 */
	public static Date getMondayOfThisWeek(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int weekday1 = cal.get(Calendar.DAY_OF_WEEK);
		if (weekday1 == Calendar.SUNDAY) {
			weekday1 = Calendar.SATURDAY + 1;
		}
		return DateUtils.addDays(cal.getTime(), -weekday1 + Calendar.MONDAY);
	}

	/**
	 * 返回带验证日期是否与当前日期为同一个星期
	 * @param date 验证日期
	 * @return true.同一个星期
	 */
	public static boolean isSameWeek(Date date) {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(new Date());
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date);

		int week1 = cal1.get(Calendar.WEEK_OF_YEAR);
		int week2 = cal2.get(Calendar.WEEK_OF_YEAR);
		return week1 == week2;
	}

	/**
	 * 根据传过来的时,分 获取当前时间戳
	 * 
	 * @param time 格式 HH:mm
	 * @return 时间戳
	 */
	public static long getCurTime(String time) {
		String strs[] = time.split(":");
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, Integer.valueOf(strs[0]));
		cal.set(Calendar.MINUTE, Integer.valueOf(strs[1]));
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTimeInMillis();
	}

	/**
	 * 根据传过来的时间算出到当月末相差几天 不包含传过来的那个时间，例如：2016-01-07 到 2016-01-31 = 24
	 * 
	 * @param date 日期
	 * @return 距离月末天数
	 */
	public static int getMonthEnd(Date date) {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date);

		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date);
		cal2.set(Calendar.DAY_OF_MONTH, cal2.getActualMaximum(Calendar.DAY_OF_MONTH));

		return cal2.get(Calendar.DAY_OF_MONTH) - cal1.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 根据参数获取相应的时间
	 * @param date 日期
	 * @param addDay 天数
	 * @param hour 小时数
	 * @param minute 分钟数
	 * @return 时间戳
	 */
	public static long getTimeByPara(Date date, int addDay, int hour, int minute) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, addDay);
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTimeInMillis();
	}
	
	/**
	 * 获取一天的开始时间，比如2016-02-24 00:00:00
	 * @param date 日期
	 * @param addDay 添加的天数不算date当天 比如2016-02-24 00:00:00 +2天，得到的时间为2016-02-26 00:00:00
	 * @return 时间戳
	 */
	public static long getTimeStart(Date date, int addDay) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, addDay);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTimeInMillis();
	}
	
	/**
	 * date时间是否在huor之后
	 * @param date 日期
	 * @param hour 小时数
	 * @return true/false
	 */
	public static boolean isHuorAfter(Date date, int hour){
		Date now = new Date();
		Calendar c1 = Calendar.getInstance();
		c1.setTime(now);
		c1.set(Calendar.HOUR_OF_DAY, hour);
		long fourTime = DateUtils.truncate(c1.getTime(), Calendar.HOUR_OF_DAY).getTime();
		long modifyTime = date.getTime();
		if(modifyTime < fourTime && now.getTime() >= fourTime){
			return true;
		}
		return false;
	}
	public static String localDateTimeFormat(LocalDateTime date){
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(PATTERN_FORMAT5);
		return dtf.format(date);
	}
	public static LocalDateTime parse(String strTime){
		Instant instant = Instant.ofEpochMilli(Long.parseLong(strTime));
		return LocalDateTime.ofInstant(instant, ZoneOffset.of("+8"));
	}
}
