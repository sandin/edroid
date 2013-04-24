package com.edroid.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;


public class DateTimeUtils {
	
    /**
     * 获取当前日期, 格式 0000-00-00
     * 
     * @param date
     * @return
     */
	public static String getYMD(Date date){
		String pattern="yyyy-MM-dd";
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		String dt = sdf.format(date);
		return dt;
	}
	
	/**
	 * 
	 * @return  当前日期和时间  格式：20110101121212000
	 */
	public static String getTimeStampStr() {
		return getDateTime("yyyyMMddHHmmssSSS");
	}
	
	/**
	 * 
	 * @return  当前日期和时间  格式：2011-01-01 12:12:12
	 */
	public static String getCurrentDateAndTime() {
		return getDateTime("yyyy-MM-dd HH:mm:ss");
	}
	
	/**
	 * 
	 * @return  获取当前日期  格式：2011-01-01
	 */
	public static String getCurrentDate() {
		return getDateTime("yyyy-MM-dd");
	}
	
	/**
	 * 
	 * @return  获取当前时间 格式  12:12:12
 	 */
	public static String getCurrentTime() {
		String date = getDateTime("HH:mm:ss");
		return date;
	}

	
	/**
	 * 
	 * @return	获取当前时间戳 
	 */
	public static long getTimestamp() {
		Date dateNow = new Date();
		return dateNow.getTime();
	}

	/**
	 * 将时间的string转换为Date类型
	 * @param str 例2011-01-01 12:12:12
	 * @return 时间类型对象
	 */
	public static Date StringToDate(String str){
		DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");         
		Date date = null;    
		try {    
		    date = format1.parse(str);   
		} catch (ParseException e) {
		    e.printStackTrace();    
		}    
		return date;  
	}
	
	/**
	 * 获取当前时间，格式 0000-00-00 00:00:00
	 * @param pattern
	 * @return
	 */
	public static String getDateTime(String pattern) {
		if (null == pattern || "".equals(pattern)) {
			pattern = "yyyy-MM-dd HH:mm:ss";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		String dt = sdf.format(new Date());
		return dt;
	} 
	/**
	 * 将时间的Date转换为String类型
	 * 
	 * @param date 
	 * @param pattern 指定返回的日期字符串的格式 ,如果没有指定则默认为"yyyy-MM-dd HH:mm:ss"
	 * @return String 返回为指定pattern格式的字符串
	 * @exception NullPointerException - 如果给定的模式为 null
	 * @exception IllegalArgumentException - 如果给定的模式无效
	 */
	public static String dateToString(Date date,String ...pattern){
	    if (date == null) {
	        return null;
	        //throw new IllegalArgumentException("arg0(date) cann't be null.");
	    }
		String pattern2="yyyy-MM-dd HH:mm:ss";
		if(pattern.length>0){
 			pattern2=pattern[0];
 		}
		DateFormat format1 = new SimpleDateFormat(pattern2);         
		
		return format1.format(date);  
	}
	/**
	 * 生成日期列表,包含开始日期和结束日期
	 * @param start	开始日期	
	 * @param end 结束日期
	 * @param pattern 指定返回的日期的格式 ,如果未指定则默认为"yyyy-MM-dd"
	 * @return 包含开始日期和结束日期的日期列表,包含开始日期和结束日期,返回的日期格式为指定的pattern
	 */
	@SuppressWarnings("deprecation")
	public static List<String> getDateList(Date start,Date end,String ...pattern){
		List<String> ret=new ArrayList<String>();
		String pattern2="yyyy-MM-dd";
		if(pattern.length>0){
 			pattern2=pattern[0];
 		}
		while(start.compareTo(end)<=0){
 			ret.add(dateToString(start,pattern2));
 			start.setDate(start.getDate()+1);
		}
		return ret;
	}
	/**
	 * 将时间的string转换为Date类型,注意本类中的另一个方法与此方法首字母的不同
	 * @param datestr 例2011-01-01 12:12:12
	 * @param pattern 指定转换的格式,如果不存在,则默认为yyyy-MM-dd HH:mm:ss
	 * @return 时间类型对象
	 */
	public static Date stringToDate(String datestr,String ...pattern){
		String pattern2="yyyy-MM-dd HH:mm:ss";
		if(pattern.length>0){
			pattern2=pattern[0];
		}
		DateFormat format1 = new SimpleDateFormat(pattern2);         
		Date date = null;    
		try {    
		    date = format1.parse(datestr);   
		} catch (ParseException e) {
		    e.printStackTrace();    
		}    
		return date;  
	}
	
	/**
	 * 获取上个月的今天
	 * @return  当前日期和时间  格式：2011-01-01 12:12:12
	 */
	public static String getLastMonthDateAndTime() {
		String year = getDateTime("yyyy");
		String month = getDateTime("MM");
		if(Integer.parseInt(month) > 1){
			month = String.valueOf((Integer.parseInt(month)-1));
			if(month.length() == 1){
				month = "0" + month;
			}
		}else{
			month = "12";
			year = String.valueOf(Integer.parseInt(year)-1);
		}
		String time = getDateTime("-dd HH:mm:ss");
		time = year + "-" + month + time;
		return time;
	}
	
	/**
	 * 根据当前时间生成流水号
	 * @param prefix 例"R"
	 * @return  当前日期和时间  格式：2011-01-01 12:12:12
	 */
	public static String getSwiftnumber(String prefix) {
		String swiftnumber = prefix;
		String date = getCurrentDate();
		String[] temp = date.split("-");
		swiftnumber += (temp[0] + temp[1] + temp[2]);
		return swiftnumber;
	}
	
	public static boolean timeMatch(String startdate){
		String datereg="(([\\d]{2}(([02468][048])|[13579][26])\\-"// 闰年
			+ "((((0[13578])|(1[02]))\\-(([012][\\d])|(3[01])))"
			+ "|(((0[469])|(11))\\-(([012][\\d])|30))"
			+ "|(02\\-[012][\\d])))" // 月日

			+ "|([\\d]{2}(([02468][^0^4^8])|([13579][^2^6]))\\-"// 非闰年
			+ "((((0[13578])|(1[02]))\\-(([012][\\d])|(3[01])))"
			+ "|(((0[469])|(11))\\-(([012][\\d])|30))"
			+ "|(02\\-(([01][\\d])|(2[0-8]))))))";
		return Pattern.matches(datereg, startdate);
	}
	
	
	/**
	 * 获取明天日期
	 * 
	 * TODO: 要这么复杂么？需要重写
	 * @return
	 */
	public static String getTomorrow() {
		String year = getDateTime("yyyy");
		String month = getDateTime("MM");
		String day = getDateTime("dd");
		if("01".equals(month) || "03".equals(month) || "05".equals(month) || "07".equals(month) || "08".equals(month) || "10".equals(month)){
			if(Integer.parseInt(day) >= 31){
				month = String.valueOf(Integer.parseInt(month) + 1) ;
				if(month.length() == 1){
					month = "0" + month;
				}
				day = "01";
			}else{
				day = String.valueOf(Integer.parseInt(day) + 1);
				if(day.length() == 1){
					day = "0" + day;
				}
			}
		}else if("04".equals(month) || "06".equals(month) || "09".equals(month) || "11".equals(month)){
			if(Integer.parseInt(day) >= 30){
				month = String.valueOf(Integer.parseInt(month) + 1) ;
				if(month.length() == 1){
					month = "0" + month;
				}
				day = "01";
			}else{
				day = String.valueOf(Integer.parseInt(day) + 1);
				if(day.length() == 1){
					day = "0" + day;
				}
			}
		}else if("02".equals(month)){
			int intyear = Integer.parseInt(year);
			if((intyear % 4 == 0) && (intyear % 100 != 0)||(intyear % 400==0)){
				if(Integer.parseInt(day) >= 29){
					month = String.valueOf(Integer.parseInt(month) + 1) ;
					if(month.length() == 1){
						month = "0" + month;
					}
					day = "01";
				}else{
					day = String.valueOf(Integer.parseInt(day) + 1);
					if(day.length() == 1){
						day = "0" + day;
					}
				}
			}else{
				if(Integer.parseInt(day) >= 28){
					month = String.valueOf(Integer.parseInt(month) + 1) ;
					if(month.length() == 1){
						month = "0" + month;
					}
					day = "01";
				}else{
					day = String.valueOf(Integer.parseInt(day) + 1);
					if(day.length() == 1){
						day = "0" + day;
					}
				}
			}
			
		}else if("12".equals(month)){
			if(Integer.parseInt(day) >= 31){
				year = String.valueOf(Integer.parseInt(year) + 1) ;
				month = "01";
				day = "01";
			}else{
				day = String.valueOf(Integer.parseInt(day) + 1);
				if(day.length() == 1){
					day = "0" + day;
				}
			}
		}
		String time = getDateTime(" HH:mm:ss");
		time = year + "-" + month + "-" + day + time;
		return time;
	}
}