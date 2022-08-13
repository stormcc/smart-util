/**
 * 
 */
/**
 * @author xunyu
 *
 */
package github.com.stormcc.util;

import github.com.stormcc.exception.InputParameterException;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
public final class DateUtil {
	private DateUtil(){}
	private static final String defaultDateFormat = "yyyyMMdd";
	
	public static String dateToString(Date date, String format){
        if(date == null){
        	return "";
        }
        if(format == null || format.trim().length() == 0){
        	format = defaultDateFormat;
        }
       
        SimpleDateFormat sdf = new SimpleDateFormat(format);
      
        return sdf.format(date);
    }

	public static Date currentBefore(long offsetMs){
		return new Date(System.currentTimeMillis()-offsetMs);
	}

    public static Date yesterday(){
	    return new Date(System.currentTimeMillis()-24*60*60*1000);
    }

	public static Date yesterdayZero(){
		Date date = new Date(System.currentTimeMillis()-24*60*60*1000);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		return calendar.getTime();
	}

	public static Date plusDayZeroHourMinuteSecond(Integer plusDay){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DATE, plusDay);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		return calendar.getTime();
	}

	public static Date subtractDayZeroHourMinuteSecond(Integer plusDay){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DATE, plusDay * -1);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		return calendar.getTime();
	}

	public static String yesterdayString(){
		Date date = new Date(System.currentTimeMillis()-24*60*60*1000);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.format(date);
	}


	public static int skeletonDate(Date date, String format){
        if ( date == null ){
            return 0;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return Integer.parseInt(sdf.format(date));
    }

	public static String getIntervalDate(int days, String format){
		 if(format == null || format.trim().length() == 0){
	        format = defaultDateFormat;
	     }
	
		 String realDate = "";
		 try{
			 Calendar calendar = Calendar.getInstance();
			 calendar.add(Calendar.DATE, days); 
			 Date date = calendar.getTime();
			 realDate = new SimpleDateFormat(format).format(date);
		 }catch (Exception e) {
			 log.error("days is:{}, format is:{}", days, format);
			 return null;
		 }
		
		 return realDate;
	}
	
	/**
	 * 
	 * @param days
	 * @param format
	 * @param dateStr yyyyMMdd
	 * @return
	 */
	public static String getIntervalDate(int days, String format, String dateStr){
		 if(format == null || format.trim().length() == 0){
	        format = defaultDateFormat;
	     }
	
		 String realDate = "";
		 try{
			 Date today = new SimpleDateFormat(format).parse(dateStr);
			 Calendar calendar = Calendar.getInstance();
			 calendar.setTime(today);
			 calendar.add(Calendar.DATE, days); 
			 Date date = calendar.getTime();
			 realDate = new SimpleDateFormat(format).format(date);
		 }catch (Exception e) {
			 log.error("days is:{}, format is:{}, dateStr is:{}", days, format, dateStr);
			 return null;
		 }
		
		 return realDate;
	}
	
	/**
	 * 获取当前日期是周几
	 * @param format
	 * @param dateStr
	 * @return
	 */
	public static int getWeek(String format, String dateStr){
		 if(format == null || format.trim().length() == 0){
	        format = defaultDateFormat;
	     }	
		 int week = 0;
		 try{
			 Date today = new SimpleDateFormat(format).parse(dateStr);
			 Calendar calendar = Calendar.getInstance();
			 calendar.setTime(today);
			 week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		     if(week<0){
		    	 week = 0;
		     }
		 }catch (Exception e) {
			 log.error("format is:{}, dateStr is:{}", format, dateStr);
			 return -1;
		 }
		 return week;
	}
	
	/**
	 * 日期字符串转date
	 * @param str
	 * @param format
	 * @return
	 */
	public static Date strToDate(String str, String format) {
		if ( str == null ){
			throw new InputParameterException("str is null");
		}
		if ( format == null ){
			throw new InputParameterException("format is null");
		}
		
	 	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
	 	try {
	 		// 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2007-02-29会被接受，并转换成2007-03-01
	 		simpleDateFormat.setLenient(false);
	 		return simpleDateFormat.parse(str);
	 	} catch (ParseException e) {
	 		// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
			String msg = String.format("format is:{}, str is:{}", format, str);
			throw new InputParameterException(msg);
	 	}
	}
	
	/**
	 * 获取两个日期间相隔的天数
	 * @param startDate
	 * @param endDate
	 * @param format
	 * @return
	 */
	public static Integer getDateDuration(String startDate, String endDate, String format){
		if ( startDate == null
				|| endDate == null){
			return null;
		}
		 if ( format == null
				 || format.trim().length() == 0){
			format = defaultDateFormat;
		 }
		
		SimpleDateFormat sformat = new SimpleDateFormat(format);
		try {
			Date start = sformat.parse(startDate);
			Date end = sformat.parse(endDate);
			
			long time1 = start.getTime();  
            long time2 = end.getTime();  
            long diff =  time2 - time1;  
            Long day = diff / (24 * 60 * 60 * 1000); 
            if(day != null){
            	return day.intValue();
            }
		} catch (ParseException e) {
			log.error("startDate is:{}, endDate is:{}, format is:{}", startDate, endDate, format);
			return null;
		}
		
		return null;
	}
	
	/**
	 * 获取与某个日期相隔n天的日期
	 * @param date
	 * @param days
	 * @param format
	 * @return
	 */
	public static Date getDate(Date date, int days, String format){
		 if(format == null || format.trim().length() == 0){
	        return null;
	     }
	
		 Date nDate = null;
		 try{
			 Calendar calendar = Calendar.getInstance();
			 calendar.setTime(date);
			 calendar.add(Calendar.DATE, days); 
			 nDate = calendar.getTime();
		 }catch (Exception e) {
			 e.printStackTrace();
		 }
		
		 return nDate;
	}

	/**
	 * 获取与某个日期相隔n天的日期
	 * @param date1
	 * @param date2
	 * @param format
	 * @return 正数：date1>date2,0:date1==date2,负数:date1<date2
	 */
	public static Long compareDate(String date1, String date2, String format){
		 if(format == null || format.trim().length() == 0){
	        return null;
	     }
		 try {
			SimpleDateFormat sformat = new SimpleDateFormat(format);
			Date d1 = sformat.parse(date1);
			Date d2 = sformat.parse(date2);
				
			long time1 = d1.getTime();  
	        long time2 = d2.getTime();  
	      
			return time1-time2;
		 }catch (Exception e) {
			 e.printStackTrace();
		 }
		
		 return null;
	}

    public static Date parseDate(String s){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return simpleDateFormat.parse(s);
        } catch (ParseException e) {
            return null;
        }
    }

	public static Date shortStringToDate(String strDate) {
		Date strtodate = null;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(defaultDateFormat);
		try {
			strtodate = simpleDateFormat.parse(strDate);
		} catch (ParseException e) {
			log.error("parse Date failed, exception is:{}", LogExceptionStackUtil.logExceptionStack(e));
			return null;
		}
		return strtodate;
	}

	public static List<Date> getDateRangeList(Date dBegin, Date dEnd) {
	    if ( (null == dBegin) || (null == dEnd) ){
	        return null;
        }
		List<Date> lDate = new ArrayList();
		Calendar calBegin = zero(dBegin);
        lDate.add(calBegin.getTime());
        Calendar calEnd = zero(dEnd);
		// 测试此日期是否在指定日期之后
		while ( calEnd.after(calBegin) ) {
			// 根据日历的规则，为给定的日历字段添加或减去指定的时间量
			calBegin.add(Calendar.DAY_OF_MONTH, 1);
			lDate.add(calBegin.getTime());
		}
		return lDate;
	}

	private static Calendar zero(Date date){
        Calendar calBegin = Calendar.getInstance();
        calBegin.setTime(date);
        calBegin.set(Calendar.HOUR_OF_DAY, 0);
        calBegin.set(Calendar.MINUTE, 0);
        calBegin.set(Calendar.SECOND, 0);
        calBegin.set(Calendar.MILLISECOND, 0);
        return calBegin;
    }

	public static List<Date> dateList(String start, String end) {
		SimpleDateFormat sdf = new SimpleDateFormat(defaultDateFormat);
		Date dBegin = null;
		try {
			dBegin = sdf.parse(start);
		} catch (ParseException e) {
			log.error("exception:{}", LogExceptionStackUtil.logExceptionStack(e));
			return null;
		}
		Date dEnd = null;
		try {
			dEnd = sdf.parse(end);
		} catch (ParseException e) {
			log.error("exception:{}", LogExceptionStackUtil.logExceptionStack(e));
			return null;
		}
		return getDateRangeList(dBegin, dEnd);
	}

	public static List<Date> getDateRangeList(String start, String end) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date dBegin = null;
		try {
			dBegin = sdf.parse(start);
		} catch (ParseException e) {
			log.error("exception:{}", LogExceptionStackUtil.logExceptionStack(e));
			return null;
		}
		Date dEnd = null;
		try {
			dEnd = sdf.parse(end);
		} catch (ParseException e) {
			log.error("exception:{}", LogExceptionStackUtil.logExceptionStack(e));
			return null;
		}
        return getDateRangeList( dBegin,  dEnd);
	}

	public static List<Date> getDateThinRangeList(String start, String end) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date dBegin = null;
		try {
			dBegin = sdf.parse(start);
		} catch (ParseException e) {
			log.error("exception:{}", LogExceptionStackUtil.logExceptionStack(e));
			return null;
		}
		Date dEnd = null;
		try {
			dEnd = sdf.parse(end);
		} catch (ParseException e) {
			log.error("exception:{}", LogExceptionStackUtil.logExceptionStack(e));
			return null;
		}
		return getDateRangeList( dBegin,  dEnd);
	}

    public static Date firstDay(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        return calendar.getTime();
    }

    public static Date lastDay(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return calendar.getTime();
    }

    public static List<Date> everyDayInMonth(Date date){
	    List<Date> dateList = new ArrayList<>(31);
        Date d1 = firstDay( date);
        Date d2 = lastDay(date);
        Date tmp = d1;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d1);
        dateList.add(tmp);
        while(tmp.getTime()<d2.getTime()) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            tmp = calendar.getTime();
            dateList.add(tmp);
        }
        return dateList;
    }

    public static List<Date> everyFirstDayOfMonthInYear(Date date){
	    List<Date> dateList = new ArrayList<>(12);
        Calendar calendar ;
        for (int i=0; i<12; i++) {
            calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.MONTH, i);
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
            dateList.add(calendar.getTime());
        }
        return dateList;
    }

    public static List<Date> oneDayOfOneYearInLastYears(Date date, int last){
	    List<Date> dateList = new ArrayList<>(last);
        Calendar calendar ;
        for (int i=0; i<last; i++) {
            calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.MONTH, 0);
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
            calendar.add(Calendar.DAY_OF_MONTH, -365*i);
            dateList.add(calendar.getTime());
        }
        return dateList;
    }

	public static Date lastDayOfMonth(final Date date) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		final int last = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		cal.set(Calendar.DAY_OF_MONTH, last);
		return cal.getTime();
	}

	public static Date firstDayOfMonth(int year, int month){
		Calendar cal = Calendar.getInstance();
		//设置年份
		cal.set(Calendar.YEAR, year);
		//设置月份
		cal.set(Calendar.MONTH, month-1);
		//获取某月最小天数
		int firstDay = cal.getMinimum(Calendar.DATE);
		//设置日历中月份的最小天数
		cal.set(Calendar.DAY_OF_MONTH, firstDay);
		return cal.getTime();
	}

	public static Date firstDayOfMonthZeroHourMinuteSecond(int year, int month){
		Calendar cal = Calendar.getInstance();
		//设置年份
		cal.set(Calendar.YEAR, year);
		//设置月份
		cal.set(Calendar.MONTH, month-1);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		//获取某月最小天数
		int firstDay = cal.getMinimum(Calendar.DATE);
		//设置日历中月份的最小天数
		cal.set(Calendar.DAY_OF_MONTH, firstDay);
		return cal.getTime();
	}

	/**
	 * 获取指定年月的最后一天
	 * @param year
	 * @param month
	 * @return
	 */
	public static Date lastDayOfMonth(int year, int month) {
		Calendar cal = Calendar.getInstance();
		//设置年份
		cal.set(Calendar.YEAR, year);
		//设置月份
		cal.set(Calendar.MONTH, month-1);
		//获取某月最大天数
		int lastDay = cal.getActualMaximum(Calendar.DATE);
		//设置日历中月份的最大天数
		cal.set(Calendar.DAY_OF_MONTH, lastDay);
		return cal.getTime();
	}

	public static Date lastDayOfMonthZeroHourMinuteSecond(int year, int month) {
		Calendar cal = Calendar.getInstance();
		//设置年份
		cal.set(Calendar.YEAR, year);
		//设置月份
		cal.set(Calendar.MONTH, month-1);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		//获取某月最大天数
		int lastDay = cal.getActualMaximum(Calendar.DATE);
		//设置日历中月份的最大天数
		cal.set(Calendar.DAY_OF_MONTH, lastDay);
		return cal.getTime();
	}
}