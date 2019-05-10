package com.cs.mybatis.utils;

import com.google.gson.Gson;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.springframework.util.StringUtils;
import org.xml.sax.InputSource;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.sql.*;
import java.text.*;
import java.util.Date;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * 
 * @author heckerstone
 * @date 2012-11-23
 * @description 常用方法组装成工具类
 *
 */

@SuppressWarnings({ "unchecked", "rawtypes" })
public class Tool {

	private static final String DEFAULT_PATTERN = "yyyyMMddHHmmss";

	static Log log = LogFactory.getLog(Tool.class);
	@SuppressWarnings("unused")
	private static Gson json=new Gson();

	public static final String replace(String line, String oldString,
			String newString) {
		if (line == null) {
			return null;
		}
		int i = 0;
		if ((i = line.indexOf(oldString, i)) >= 0) {
			char[] line2 = line.toCharArray();
			char[] newString2 = newString.toCharArray();
			int oLength = oldString.length();
			StringBuffer buf = new StringBuffer(line2.length);
			buf.append(line2, 0, i).append(newString2);
			i += oLength;
			int j = i;
			while ((i = line.indexOf(oldString, i)) > 0) {
				buf.append(line2, j, i - j).append(newString2);
				i += oLength;
				j = i;
			}
			buf.append(line2, j, line2.length - j);
			return buf.toString();
		}
		return line;
	}

	public Tool() {
	}

	/**
	 * 以li分割str字符串，返回字符串数组
	 */
	public static String[] explode(String li, String str) {
		if (str == null) {
			String[] rs = new String[0];
			return rs;
		}
		int num = 1;
		String temp = str;
		for (int i = temp.indexOf(li); i > -1; i = temp.indexOf(li)) {
			temp = temp.substring(i + li.length());
			num++;
		}
		// if (num == 1)
		// return new String[0];
		int j = 0;
		temp = str;
		String[] rs = new String[num];
		for (int i = 1; i < num; i++) {
			int p = temp.indexOf(li);
			rs[j] = temp.substring(0, p);
			temp = temp.substring(p + li.length());
			j++;
		}
		rs[j] = temp;
		return rs;
	}

	/**
	 * 以li分割str字符串，返回字符串数组
	 */
	public static String[] split(String li, String str) {
		if ((str == null) || (str.trim().length() == 0))
			str = null;
		return explode(li, str);
	}

	/**
	 * 以li分割str字符串，返回字符串数组
	 */
	public static String[] explode_new(String li, String str) {
		StringTokenizer st = new StringTokenizer(str, li);
		int rssize = 0;
		if (str.startsWith(li))
			rssize++;
		if (str.endsWith(li))
			rssize++;
		String[] rs = new String[st.countTokens() + rssize];
		int i = 0;
		if (str.startsWith(li)) {
			rs[i] = "";
			i++;
		}
		while (st.hasMoreTokens()) {
			rs[i] = st.nextToken();
			i++;
		}
		if (str.endsWith(li)) {
			rs[i] = "";
		}
		return rs;
	}

	/**
	 * Joins the elements of the provided array into a single string containing
	 * a list of CSV elements.
	 * 
	 * @param list
	 *            The list of values to join together.
	 * @param separator
	 *            The separator character.
	 * @return The CSV test.
	 */
	public static String join(String separator, String[] list) {
		if (list == null)
			list = new String[0];
		StringBuffer csv = new StringBuffer();
		for (int i = 0; i < list.length; i++) {
			if (i > 0) {
				csv.append(separator);
			}
			csv.append(list[i]);
		}
		return csv.toString();
	}

	/**
	 * 将str字符串转换成数字
	 */
	public static int StrToInt(String str) {
		int rs = 0;
		if (str != null) {
			try {
				Integer in = new Integer(str);
				rs = in.intValue();
			} catch (NumberFormatException e) {
				rs = 0;
			}
		}
		return rs;
	}

	/**
	 * 格式化日期，如果不是两位，拼‘0’
	 */
	public static String getTwoDate(int rq) {
		String temp = "" + rq;
		if (rq > 0 && rq < 10)
			temp = "0" + rq;
		return temp;
	}

	/**
	 * 计算并返回两个日期之间的秒数 2006年1月1日
	 */
	public static int subSecond(Date d1, Date d2) {

		long mss = d2.getTime() - d1.getTime();
		long ss = mss / 1000;

		return (int) ss;
	}

	/**
	 * 计算并返回两个日期之间的天数
	 */
	public static int subDate(Date d1, Date d2) {
		// GregorianCalendar gc1= new GregorianCalendar();
		// GregorianCalendar gc2= new GregorianCalendar();
		// gc1.setTime(d1);
		// gc2.setTime(d2);
		// gc1.computFields();
		long mss = d2.getTime() - d1.getTime();
		long ss = mss / 1000;
		long ms = ss / 60;
		long hs = ms / 60;
		long ds = hs / 24;
		return (int) ds;
	}

	// 从服务器上取得当前日期
	// 格式：2002年04月25日 星期四
	public static String get_current_date() {
		String[] week = new String[7];
		week[0] = "日";
		week[1] = "一";
		week[2] = "二";
		week[3] = "三";
		week[4] = "四";
		week[5] = "五";
		week[6] = "六";
		Date d1 = new Date();
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(d1);
		return gc.get(Calendar.YEAR) + "年" + (gc.get(Calendar.MONTH) + 1) + "月"
		+ gc.get(Calendar.DAY_OF_MONTH) + "日 星期"
		+ week[gc.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY];
	}

	public static String stringOfTime() {
		return stringOfTime(new Date());
	}

	public static String stringOfTime(Date date) {
		Format formatter = new SimpleDateFormat("HH:mm");
		return formatter.format(date);
	}

	/**
	 * 当前日期字符串
	 */
	public static String stringOfDateTime() {
		return stringOfDateTime(new Date());
	}

	public static String stringOfDateTime(Date date) {
		Format formatter = new SimpleDateFormat("yyyy-MM-dd");
		// HH:mm:ss
		return formatter.format(date);
	}

	public static String stringTime() {
		return stringTime(new Date());
	}

	public static String stringTime(Date date) {
		Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return formatter.format(date);
	}

	// 将日期字符串转换为日期变量,如果有问题,返回当前日期
	public static Date stringToDateTime(String str) {
		try {
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return (Date) formatter.parse(str);
		} catch (ParseException e) {
			return new Date();
		}
	}

	// 将日期字符串转换为日期变量,如果有问题,返回当前日期
	public static Date stringToDate(String str) {
		try {
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			return (Date) formatter.parse(str);
		} catch (ParseException e) {
			return new Date();
		}
	}

	// 根据日期格式字符串将日期字符串转换为日期变量,如果有问题,返回当前日期
	public static Date stringToDate(String str, String formatstring) {
		try {
			DateFormat formatter = new SimpleDateFormat(formatstring);
			return (Date) formatter.parse(str);
		} catch (ParseException e) {
			return new Date();
		}
	}

	/**
	 * 当前日期字符串
	 */

	public static String stringOfCnDateTime() {
		return stringOfCnDateTime(new Date());
	}

	public static String stringOfCnDateTime(Date date) {
		Format formatter = new SimpleDateFormat("yyyy年M月d日 H时m分s秒");
		return formatter.format(date);
	}

	/**
	 * 当前日期字符串
	 */
	public static String stringOfCnDate() {
		return stringOfCnDate(new Date());
	}

	public static String stringOfCnDate(Date date) {
		Format formatter = new SimpleDateFormat("yyyy年M月d日");
		return formatter.format(date);
	}

	/**
	 * 当前日期字符串
	 */
	public static String stringOfDate() {
		return stringOfDate(new Date());
	}

	/**
	 * 当前日期字符串
	 * 
	 * @return 2013-04-22 23:59:59
	 */
	public static String getStringCurrentTimeDate() {
		String newDate = stringOfDate(new Date());
		newDate += " 23:59:59";
		return newDate;
	}

	public static String stringOfDate(Date date) {
		Format formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.format(date);
	}

	/**
	 * 当前日期字符串
	 */
	public static String stringOfDate1() {
		return stringOfDate1(new Date());
	}

	public static String stringOfDate1(Date date) {
		Format formatter = new SimpleDateFormat("yyyyMMdd");
		return formatter.format(date);
	}

	/**
	 * 根据时间格式字符串获得当前日期字符串
	 */
	public static String getStringOfDate(String formatstring) {
		return stringOfDate(new Date(), formatstring);
	}

	public static String stringOfDate(Date date, String formatstring) {
		Format formatter = new SimpleDateFormat(formatstring);
		return formatter.format(date);
	}

	/**
	 * 计算并返回给定年月的最后一天
	 */
	public static String lastDateOfMonth(int year, int month) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.set(Calendar.YEAR, year);
		gc.set(Calendar.MONTH, month - 1);
		int maxDate = gc.getActualMaximum(Calendar.DAY_OF_MONTH);
		gc.set(Calendar.DATE, maxDate);
		return stringOfDate(gc.getTime());
	}

	/**
	 * 计算并返回日期中的星期几
	 */

	public static int weekOfDate(Date d1) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(d1);
		return gc.get(Calendar.DAY_OF_WEEK);
	}

	public static String amorPmTime() {

		return amorPmTime(new Date());
	}

	public static String amorPmTime(Date d1) {

		SimpleDateFormat formatter2 = new SimpleDateFormat(" a hh:mm");
		return formatter2.format(d1);
	}

	/**
	 * 返回日期中的星期几
	 */
	public static String weekOfDate() {
		String[] week = new String[7];
		week[0] = "日";
		week[1] = "一";
		week[2] = "二";
		week[3] = "三";
		week[4] = "四";
		week[5] = "五";
		week[6] = "六";
		return week[weekOfDate(new Date())];
	}

	/**
	 * 计算并返回日期中的日
	 */
	public static int dayOfDate(Date d1) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(d1);
		return gc.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 计算并返回日期中的月
	 */
	public static int monthOfDate(Date d1) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(d1);
		return gc.get(Calendar.MONTH) + 1;
	}

	/**
	 * 计算并返回日期中的年
	 */
	public static int yearOfDate(Date d1) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(d1);
		return gc.get(Calendar.YEAR);
	}

	/**
	 * 计算并返回日期中的时
	 */
	public static int hourOfDate(Date d1) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(d1);
		return gc.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * 计算并返回日期中的分
	 */
	public static int minuteOfDate(Date d1) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(d1);
		return gc.get(Calendar.MINUTE);
	}

	/**
	 * 计算并返回日期中的秒
	 */
	public static int secondOfDate(Date d1) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(d1);
		return gc.get(Calendar.SECOND);
	}

	/**
	 * 计算数月后的日期
	 */
	public static Date addDateByMonth(Date d, int mcount) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(d);
		gc.add(Calendar.MONTH, mcount);
		gc.add(Calendar.DATE, -1);
		return new Date(gc.getTime().getTime());
	}

	/**
	 * 计算数日后的日期
	 */
	public static Date addDateByDay(Date d, int dcount) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(d);
		gc.add(Calendar.DATE, dcount);
		return new Date(gc.getTime().getTime());
	}

	/**
	 * 计算数秒后的日期
	 */
	public static Date addDateBySecond(Date d, int scount) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(d);
		gc.add(Calendar.SECOND, scount);
		return gc.getTime();
	}

	/**
	 * zhurx 20040224 输入的字符转换为时间类型
	 */
	public static java.sql.Date isTime(String shijian) {
		java.sql.Date time = null;
		try {
			time = java.sql.Date.valueOf(shijian);
			return time;
		} catch (IllegalArgumentException myException) {
			return time;
		}
	}

	/**
	 * zhurx 20040306 输入的字符为yyyy-MM-dd HH:mm:ss类型 转换为：java.util.Date
	 */

	public static Date isDateTime(String datestr) {
		// Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// return formatter.format(date);
		Date rdatetime = null;
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			// DateFormat formatter = new DateFormat("yyyy-MM-dd HH:mm:ss");
			ParsePosition pos = new ParsePosition(0);
			rdatetime = formatter.parse(datestr, pos);
			return rdatetime;
		} catch (IllegalArgumentException myException) {
			return rdatetime;
		}

	}

	/**
	 * 根据开始时间（格式：yyyy-MM）和结束时间（格式：yyyy-MM）返回时间段内的所有时间（格式：yyyy-MM）列表 根据当前时间 Date
	 * 获取 当前年月份
	 *
	 * @return
	 */
	public static String getNowYYYYMMDate() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	public static String getFirstYYYYMMDate() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
		String dateString = formatter.format(currentTime);

		return dateString + "-02";
	}

	// ///////////////////////////////////////////日期计算方法///////////////////////////////////////////////////

	/**
	 * 将代表日期的字符串分割为代表年月的整形数组
	 * 
	 * @param date
	 * @return
	 */
	public static int[] splitYMD(String date) {
		date = date.replace("-", "");
		int[] ymd = { 0, 0 };
		ymd[0] = Integer.parseInt(date.substring(0, 4));
		ymd[1] = Integer.parseInt(date.substring(4, 6));
		return ymd;
	}

	/**
	 * 将代表日期的字符串分割为代表年月的整形数组
	 * 
	 * @param date
	 * @param interval
	 *            间隔符，如'-'、'.'等
	 * @return
	 */
	public static int[] splitYMD(String date, String interval) {
		date = date.replace(interval, "");
		int[] ymd = { 0, 0 };
		ymd[0] = Integer.parseInt(date.substring(0, 4));
		ymd[1] = Integer.parseInt(date.substring(4, 6));
		return ymd;
	}

	/**
	 * 判断时间date1是否在时间date2之前
	 * 
	 * @param date1
	 * @param date2
	 * @param formatstring
	 * @return
	 */
	public static boolean isDateBefore(String date1, String date2,
			String formatstring) {
		try {
			// DateFormat df = DateFormat.getDateTimeInstance();
			DateFormat df = new SimpleDateFormat(formatstring);
			return df.parse(date1).before(df.parse(date2));
		} catch (ParseException e) {
			//System.out.print(e.getMessage());
			return false;
		}
	}

	/**
	 * 判断当前时间是否在时间date2之前
	 * 
	 * @param date2
	 * @return
	 */
	public static boolean isDateBefore(String date2) {
		try {
			Date date1 = new Date();
			DateFormat df = DateFormat.getDateTimeInstance();
			return date1.before(df.parse(date2));
		} catch (ParseException e) {
			//System.out.print("[SYS] " + e.getMessage());
			return false;
		}
	}

	/**
	 * 将不足两位的月份或日期补足为两位
	 * 
	 * @param decimal
	 * @return
	 */
	public static String formatMonthDay(int decimal) {
		DecimalFormat df = new DecimalFormat("00");
		return df.format(decimal);
	}

	/**
	 * 将不足四位的年份补足为四位
	 * 
	 * @param decimal
	 * @return
	 */
	public static String formatYear(int decimal) {
		DecimalFormat df = new DecimalFormat("0000");
		return df.format(decimal);
	}

	/**
	 * 计算两个日期之间相隔的天数
	 * 
	 * @param begin
	 * @param end
	 * @return
	 * @throws ParseException
	 */
	public static long countDay(String begin, String end) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date beginDate, endDate;
		long day = 0;
		try {
			beginDate = format.parse(begin);
			endDate = format.parse(end);
			day = (endDate.getTime() - beginDate.getTime())
					/ (24 * 60 * 60 * 1000);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return day;
	}

	// 获取上月第一天日期
	public static String getPreviousMonthFirst() {
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar lastDate = Calendar.getInstance();
		lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
		lastDate.add(Calendar.MONTH, -1);// 减一个月，变为下月的1号
		// lastDate.add(Calendar.DATE,-1);//减去一天，变为当月最后一天
		str = sdf.format(lastDate.getTime());
		return str;
	}

	public static String HtmlSelect(String value[], String text[],
			String selected) {
		StringBuffer htmlSelect = new StringBuffer();
		try {
			for (int i = 0; i < text.length; i++) {

				if (selected != null && selected.trim().equals(value[i].trim())) {
					htmlSelect.append("<OPTION value=\"");
					htmlSelect.append(value[i]);
					htmlSelect.append("\"  selected>");
					htmlSelect.append(text[i]);
					htmlSelect.append(" </OPTION>");
				} else {
					htmlSelect.append("<OPTION value=\"");
					htmlSelect.append(value[i]);
					htmlSelect.append("\" >");
					htmlSelect.append(text[i]);
					htmlSelect.append(" </OPTION>");
				}
			}

		} catch (Exception e) {

			log.error("异常:" + e.getMessage());
		}

		return htmlSelect.toString();

	}

	/**
	 * 将double类型的数值保留小数点后两位输出
	 */
	public static double round(double d) {
		DecimalFormat nf = new DecimalFormat("0.00");
		return Double.parseDouble(nf.format(d));
	}

	public static synchronized int getNextSn(Connection con, String tname) {
		return getNextSn_oracle(con, tname);
	}

	public static synchronized int getNextSn_oracle(Connection con, String tname) {
		String SELECT = "SELECT " + tname + "_seq.nextval FROM dual";
		int iResult = 0;
		Statement pstmt = null;
		try {
			pstmt = con.createStatement();
			ResultSet rs = pstmt.executeQuery(SELECT);
			if (rs.next()) {
				iResult = rs.getInt(1);
			}
			rs = null;
		} catch (SQLException sqle) {
			log.debug("Tool.getNextSn_oracle:" + sqle.getMessage());
		} finally {
			try {
				pstmt.close();
			} catch (Exception e) {
				log.debug("Tool.executeSql:" + e.getMessage());
			}
		}
		return iResult;
	}

	public static synchronized int getNextSn_mysql(Connection con,
			String tname, String keycol) {
		String SELECT = "SELECT max(" + keycol + ") FROM " + tname;
		int iResult = 0;
		Statement pstmt = null;
		try {
			pstmt = con.createStatement();
			ResultSet rs = pstmt.executeQuery(SELECT);
			if (rs.next()) {
				iResult = rs.getInt(1);
			}
			rs = null;
		} catch (SQLException sqle) {
			log.debug("Tool.getNextSn_mysql:" + sqle.getMessage());
		} finally {
			try {
				pstmt.close();
			} catch (Exception e) {

				log.debug("Tool.executeSql:" + e.getMessage());
			}
		}
		log.debug(tname + ":" + keycol + ":" + (iResult + 1));
		return (iResult + 1);
	}

	public static Date getDate() {
		return new Date();
	}

	public static String[] getYearList(int startY, int len) {
		String[] list = new String[len];
		for (int i = startY; i < len + startY; i++)
			list[i - startY] = "" + (i + 1);
		return list;
	}

	public static String[] getMonDayList(int len) {
		String[] list = new String[len];
		for (int i = 0; i < len; i++) {
			if (i < 9)
				list[i] = "0" + (1 + i);
			else
				list[i] = "" + (1 + i);
		}
		return list;
	}

	public static boolean inArray(String str, String[] arr) {
		for (int i = 0; i < arr.length; i++) {
			if (arr[i].equals(str)) {
				return true;
			}
		}
		return false;
	}

	public static boolean inArray(Object o, List arr) {
		int size = arr.size();
		for (int i = 0; i < size; i++) {
			if (arr.get(i).equals(o)) {
				return true;
			}
		}
		return false;
	}

	public static String getFilePath(HttpServletRequest request) {
		HttpSession session = request.getSession();
		String realpath = session.getServletContext().getRealPath("/");
		realpath = realpath.substring(0, realpath.length() - 1);
		return realpath;
	}

	public static void setVal(int idx, int type, ResultSet rs,
			PreparedStatement pstmt) throws SQLException {
		switch (type) {
		case Types.TINYINT:
		case Types.SMALLINT:
			pstmt.setShort(idx, rs.getShort(idx));
			break;
		case Types.INTEGER:
			pstmt.setInt(idx, rs.getInt(idx));
			break;
		case Types.BIGINT:
			pstmt.setLong(idx, rs.getLong(idx));
			break;
		case Types.BOOLEAN:
			pstmt.setBoolean(idx, rs.getBoolean(idx));
			break;
		case Types.CHAR:
		case Types.VARCHAR:
			pstmt.setString(idx, rs.getString(idx));
			break;
		case Types.DATE:
			pstmt.setDate(idx, rs.getDate(idx));
			break;
		case Types.DECIMAL:
			pstmt.setBigDecimal(idx, rs.getBigDecimal(idx));
			break;
		case Types.DOUBLE:
			pstmt.setDouble(idx, rs.getDouble(idx));
			break;
		case Types.FLOAT:
			pstmt.setFloat(idx, rs.getFloat(idx));
			break;
		case Types.NUMERIC:
			pstmt.setFloat(idx, rs.getFloat(idx));
			break;
		case Types.REAL:
			pstmt.setFloat(idx, rs.getFloat(idx));
			break;
		case Types.TIME:
			pstmt.setTime(idx, rs.getTime(idx));
			break;
		case Types.TIMESTAMP:
			pstmt.setTimestamp(idx, rs.getTimestamp(idx));
			break;
		default:
			pstmt.setString(idx, rs.getString(idx));
		}
	}

	public static String toUtf8String(String s) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c >= 0 && c <= 255) {
				sb.append(c);
			} else {
				byte[] b;
				try {
					b = Character.toString(c).getBytes("utf-8");
				} catch (Exception ex) {
					////System.out.println(ex);
					b = new byte[0];
				}
				for (int j = 0; j < b.length; j++) {
					int k = b[j];
					if (k < 0)
						k += 256;
					sb.append("%" + Integer.toHexString(k).toUpperCase());
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 从Session中取出UserInfo
	 * 
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */

	// 获得本机ip
	public static String getip() {
		@SuppressWarnings({ "unused" })
		String lname = null;
		String lip = null;
		try {
			InetAddress add = InetAddress.getLocalHost();
			lip = add.getHostAddress();
			lname = add.getHostName();
			// ////System.out.println(add.getHostName()+": "+add.getHostAddress());
			return lip;
		} catch (Exception e) {
			// //System.out.print(e.getMessage());
			return lip;
		}
	}

	// 通过IP获取网卡地址
	//	public static void main(String args[]) {
	//		// //System.out.println(System.getProperty("user.timezone"));
	//		// //System.out.println(TimeZone.getDefault());
	//		String[] idlist = explode(",", "rh,02,03");
	//		//System.out.println(idlist[0]);
	//		//System.out.println(idlist[1]);
	//		//System.out.println(idlist[2]);
	//		// TimeZone tz = TimeZone.getTimeZone("ETC/GMT-8");
	//		// //System.out.println(tz);
	//		// //System.out.println(stringOfDateTime());
	//		Calendar canlendar = Calendar.getInstance(); // java.util包
	//		canlendar.add(Calendar.DATE, -7); // 日期减 如果不够减会将月变动
	//		@SuppressWarnings("unused")
	//		String result = (new SimpleDateFormat("yyyy-MM-dd")).format(canlendar
	//				.getTime());
	//		//System.out.println(Tool.getStringCurrentTimeDate());
	//	}

	/**
	 * 将String格式的时间2013-005-30 00:00:00转换成Timestamp时间
	 */
	public static Timestamp changeStringToTimestamp(String changeTime) {
		SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date11 = null;
		try {
			date11 = df1.parse(changeTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String time = df1.format(date11);
		Timestamp ts = Timestamp.valueOf(time);
		////System.out.println(ts);
		return ts;
	}

	/**
	 * TimeStamp转String
	 */
	public static String changeTimeStampToString(Timestamp changeTime) {
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String tsStr = ""; 
		tsStr = sdf.format(changeTime); 
		return tsStr;
	}

	/**
	 * 返回当前系统时间的三天前日期,格式为2013-05027
	 */
	public static Timestamp returnThreeDaysBeforeCurrentTime(int param) {
		Calendar canlendar = Calendar.getInstance(); // java.util包
		canlendar.add(Calendar.DATE, param); // 日期减 如果不够减会将月变动
		String result = (new SimpleDateFormat("yyyy-MM-dd")).format(canlendar
				.getTime());
		result += " 00:00:00";
		Timestamp tt = Tool.changeStringToTimestamp(result);
		return tt;
	}

	/**
	 * 获取几天前的日期
	 * 
	 * @return date
	 */
	public static String GetDateBefore(int days) {
		Calendar canlendar = Calendar.getInstance(); // java.util包
		canlendar.add(Calendar.DATE, days); // 日期减 如果不够减会将月变动
		String result = (new SimpleDateFormat("yyyy-MM-dd")).format(canlendar
				.getTime());
		result += " 00:00:00";
		return result;
	}

	/**
	 * 
	 * 
	 * @Method: getMacAddressIP
	 * 
	 * @Description: TODO
	 * 
	 * @param @param remotePcIP
	 * @param @return
	 * 
	 * @return String
	 * 
	 * @throws
	 */
	public static String getMacAddressIP(String remotePcIP) {
		String str = "";
		String macAddress = "";
		try {
			Process pp = Runtime.getRuntime().exec("nbtstat -A " + remotePcIP);
			InputStreamReader ir = new InputStreamReader(pp.getInputStream());
			LineNumberReader input = new LineNumberReader(ir);
			for (int i = 1; i < 100; i++) {
				str = input.readLine();
				if (str != null) {
					if (str.indexOf("MAC Address") > 1) {
						macAddress = str.substring(
								str.indexOf("MAC Address") + 14, str.length());
						break;
					}
				}
			}
		} catch (IOException ex) {
		}
		return macAddress;
	}

	// 通过机器名获取网卡地址
	public static String getMacAddressName(String remotePcIP) {
		String str = "";
		String macAddress = "";
		try {
			Process pp = Runtime.getRuntime().exec("nbtstat -a " + remotePcIP);
			InputStreamReader ir = new InputStreamReader(pp.getInputStream());
			LineNumberReader input = new LineNumberReader(ir);
			for (int i = 1; i < 100; i++) {
				str = input.readLine();
				if (str != null) {
					if (str.indexOf("MAC Address") > 1) {
						macAddress = str.substring(
								str.indexOf("MAC Address") + 14, str.length());
						break;
					}
				}
			}
		} catch (IOException ex) {
		}
		return macAddress;
	}

	public static boolean deleteFile(String sPath) {
		boolean flag = false;
		File file = new File(sPath);
		flag = false;
		file = new File(sPath);
		////System.out.println(sPath + "   " + file.isFile());
		// 路径为文件且不为空则进行删除
		if (file.isFile() && file.exists()) {
			file.delete();
			flag = true;
		}
		return flag;
	}

	// 将指定目录下的文件名字变为大写或小写：u--变为大写，l-变为小写
	public static void changePathName(String path, String up) {
		// //System.out.println("->->->changepathname Begin...");
		File d = new File(path); // 取得当前文件夹下所有文件和目录的列表
		File lists[] = d.listFiles();
		String pathss = new String("");
		// 对当前目录下面所有文件进行检索
		for (int i = 0; i < lists.length; i++) {
			if (lists[i].isFile()) {
				String filename = lists[i].getName();
				if (up.equals("u"))
					filename = upCase(filename);
				else
					filename = lowerCase(filename);

				String toName = new String(path + filename);
				File tempf = new File(toName);
				lists[i].renameTo(tempf);
				// //System.out.println("new fullfilename is:" + toName);
			} else {
				pathss = path;
				// 进入下一级目录
				pathss = pathss + lists[i].getName() + "\\";
				// 递归遍历所有目录
				changePathName(pathss, up);
			}
		}
		// //System.out.println("->->->changepathname End...");
	}

	public static String lowerCase(String filename) {
		// //System.out.println("=>to lowerCase Begin...");
		String tempstr = new String("");
		char tempch = ' ';
		for (int i = 0; i < filename.length(); i++) {
			tempch = filename.charAt(i);
			if (64 < filename.charAt(i) && filename.charAt(i) < 91)// 是大写字母
				tempch += 32;
			tempstr += tempch;
		}
		// //System.out.println("new filename is:" + tempstr);
		// //System.out.println("=>tolowerCase End...");
		return tempstr;
	}

	public static String upCase(String filename) {
		// //System.out.println("=>to upCase Begin...");
		String tempstr = new String("");
		char tempch = ' ';
		for (int i = 0; i < filename.length(); i++) {
			tempch = filename.charAt(i);
			if (97 < filename.charAt(i) && filename.charAt(i) < 122)// 是大写字母
				tempch -= 32;
			tempstr += tempch;
		}
		return tempstr;
	}

	/* 人民币大写转换 */

	/**
	 * 判断是否是数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean pandNumber(String str) {
		int length = str.length();
		int v = 0;

		for (int i = 0; i < length; i++) {
			v = str.charAt(i);
			if (v < 49 || v > 57)
				return false;
		}
		return true;
	}

	public static String[] StringNumber(String str) {
		int length = str.length();
		char v = 0;
		StringBuffer string = new StringBuffer();
		StringBuffer number = new StringBuffer();
		for (int i = 0; i < length; i++) {
			v = str.charAt(i);
			if (v < 49 || v > 57)
				string.append(v);
			else
				number.append(v);
		}
		return new String[] { string.toString(), number.toString() };
	}

	/**
	 * MD5加密方法
	 * 
	 * @param s
	 * @return
	 */
	public final static String MD5(String s) {
		if (s == null)
			return null;
		char hexDigits[] = { 'D', 'E', 'F', '0', 'A', 'B', 'C', '1', '2', '3',
				'4', '5', '6', '7', '8', '9' };
		try {
			byte[] strTemp = s.getBytes();
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();

			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Object convert(String value, Class t)

	{
		if (value == null) {
			if (t.equals(Boolean.class) || t.equals(Boolean.TYPE)) {
				value = "false";
				return new Boolean(value);

			} else {
				return null;
			}
		}

		if (t.equals(Boolean.class) || t.equals(Boolean.TYPE)) {

			if (value.equals("1") || value.equalsIgnoreCase("on")
					|| value.equalsIgnoreCase("true"))
				value = "true";
			else
				value = "false";

			return new Boolean(value);
		}

		if (t.equals(Byte.class) || t.equals(Byte.TYPE))
			return new Byte(value);
		if (t.equals(Character.class) || t.equals(Character.TYPE))
			return value.length() <= 0 ? null : new Character(value.charAt(0));
		if (t.equals(Short.class) || t.equals(Short.TYPE))
			return new Short(value);
		if (t.equals(Integer.class) || t.equals(Integer.TYPE))
			return new Integer(value);
		if (t.equals(Float.class) || t.equals(Float.TYPE))
			return new Float(value);
		if (t.equals(Long.class) || t.equals(Long.TYPE))
			return new Long(value);
		if (t.equals(Double.class) || t.equals(Double.TYPE))
			return new Double(value);
		if (t.equals(String.class))
			return value;
		if (t.equals(File.class))
			return new File(value);
		return null;

	}

	public static int maxIntArray(int[] args) {
		if (args == null)
			return 0;
		int max = 0;
		for (int i = 0; i < args.length; i++) {
			max += args[i];

		}
		return max;
	}

	public static boolean ArrayCheck(String[] Array, String str) {
		if (Array == null)
			return false;
		for (int i = 0; i < Array.length; i++) {
			if (str == null || Array[i] == null) {
				return false;
			} else {
				if (str.trim().equals(Array[i].trim()))
					return true;
			}

		}
		return false;
	}

	public static boolean ArrayCheck(List Array, Object str) {
		if (Array == null)
			return false;
		int size = Array.size();
		for (int i = 0; i < size; i++) {
			if (str == null || Array.get(i) == null) {
				return false;
			} else {
				if (str.equals(Array.get(i)))
					return true;
			}

		}
		return false;
	}

	public static String ObjectOutput(Object Object) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos;

		try {
			oos = new ObjectOutputStream(baos);
			oos.writeObject(Object);
			String out = new String(baos.toByteArray());

			return out;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}

	public static Object ObjectInput(String str) {

		try {
			ByteArrayInputStream input = new ByteArrayInputStream(
					str.getBytes());
			ObjectInputStream in = new ObjectInputStream(input);
			return in.readObject();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}

	}

	public static long getQuot(String time1, String time2) {
		long quot = 0;
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date1 = ft.parse(time1);
			Date date2 = ft.parse(time2);
			quot = date1.getTime() - date2.getTime();
			quot = quot / 1000 / 60 / 60 / 24;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return quot;
	}

	/**
	 * 取diff天之前或之后的日期
	 * 
	 * @param diff
	 *            时间跨度
	 * @param aorb
	 *            "a"之前，"b"之后
	 * @return
	 */
	public static String dateOfSomeDayDiff(int diff, String aorb) {
		@SuppressWarnings("unused")
		String dd = "";
		Date date = null;
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
		long day = (long) diff * 24 * 60 * 60 * 1000;
		long current = System.currentTimeMillis();
		if ("a".equalsIgnoreCase(aorb))
			date = new Date(current + day);
		if ("b".equalsIgnoreCase(aorb))
			date = new Date(current - day);
		return dd = ft.format(date);
	}

	/**
	 * 取固定长度的字符串
	 * 
	 * @param resource
	 *            原串
	 * @param length
	 *            长度
	 * @return
	 */
	public static String fixedLengthString(String resource, int length) {
		if (resource.length() < length)
			return resource;

		return resource.subSequence(0, length) + "...";
	}

	/**
	 * 随机生成16进制颜色码
	 * 
	 * @return
	 */
	public static String getRandomColor() {
		String s = "123456789ABCDEF";
		StringBuffer sb = new StringBuffer();
		Random rs = new Random();
		for (int i = 0; i < 6; i++) {
			sb.append(s.charAt(rs.nextInt(s.length())));
		}
		return sb.toString();
	}

	// 将日期字符串转换为日期变量,如果有问题,返回当前日期
	public static Date stringToDateNY(String str) {
		try {
			DateFormat formatter = new SimpleDateFormat("yyyy-MM");
			return (Date) formatter.parse(str);
		} catch (ParseException e) {
			return new Date();
		}
	}

	public static String getLastYYYYMMDate(String datas) {
		Date currentTime = stringToDateNY(datas);
		Calendar cal = new GregorianCalendar();
		cal.setTime(currentTime);
		// //System.out.println(currentTime+"   "+datas+"   month:     "+cal.MONTH+"   "+Calendar.MONTH);
		cal.add(Calendar.MONTH, -1);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
		String dateString = formatter.format(cal.getTime());
		return dateString;
	}

	public static String getLastYYYYMMDateYear(String datas) {
		Date currentTime = stringToDateNY(datas);
		Calendar cal = new GregorianCalendar();
		cal.setTime(currentTime);
		// //System.out.println(currentTime+"   "+datas+"   month:     "+cal.MONTH+"   "+Calendar.MONTH);
		cal.add(Calendar.YEAR, -1);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
		String dateString = formatter.format(cal.getTime());
		return dateString;
	}

	/**
	 * 根据开始时间（格式：yyyy-MM）和结束时间（格式：yyyy-MM）返回时间段内的所有时间（格式：yyyy-MM）列表
	 * 
	 * @param begintime
	 * @param endtime
	 * @return
	 */
	public static List<String> getYM(String begintime, String endtime) {
		// //System.out.println("getYM:begintime  endtime:--------"+begintime+"----"+endtime);
		List<String> list = null;
		int[] bt = Tool.splitYMD(begintime);
		int[] et = Tool.splitYMD(endtime);
		int yearnum = et[0] - bt[0];
		if (yearnum >= 0) {
			int byear = bt[0];// 开始年
			int bmonth = bt[1];// 开始月
			int eyear = et[0];// 结束年
			int emonth = et[1];// 结束月
			list = new ArrayList();
			for (int i = 0; i <= yearnum; i++) {
				if (yearnum == 0) {
					for (int j = bmonth; j <= emonth; j++) {
						list.add(String.valueOf(byear + i) + "-"
								+ Tool.formatMonthDay(j));
					}
				} else {
					if ((byear + i) == eyear) {
						for (int j = 1; j <= emonth; j++) {
							list.add(String.valueOf(byear + i) + "-"
									+ Tool.formatMonthDay(bmonth++));
						}
					} else {
						for (int j = bmonth; j <= 12; j++) {
							list.add(String.valueOf(byear + i) + "-"
									+ Tool.formatMonthDay(bmonth++));
						}
						bmonth = 1;
					}
				}
			}
		}
		return list;
	}

	public static String replaceToNull(String resource, String[] replace) {
		if (null == replace)
			return resource;
		String temp = resource;
		for (int i = 0; i < replace.length; i++) {
			temp = temp.replaceAll(replace[i], " ");
		}
		return temp;
	}

	public static String stareKeyword(String res, String keyword) {
		String tmp = res;
		tmp = tmp.replaceAll(keyword, "<font color=\"red\">" + keyword
				+ "</font>");
		return tmp;
	}

	/**
	 * 将数组分割为1,2,3的格式
	 * 
	 * 返回字符：String
	 */

	static public String getPlListForString(String[] reource) {
		// //System.out.println(reource);
		if (reource == null)
			return "";
		StringBuffer tmp = new StringBuffer();
		String shenhe = "";
		for (int i = 0; i < reource.length; i++) {
			tmp.append("'").append(reource[i].trim()).append("',");
		}
		shenhe = tmp.toString().substring(0, tmp.length() - 1);

		return shenhe;

	}

	/**
	 * 格式转换，将12,34,56 格式转换为 like '12%' or like '34%' or like '56%'
	 * 
	 * @param oldtype
	 * @return
	 */
	public static String typLikeChange(String oldtype, String keyname) {

		if (oldtype == null || oldtype.equals(""))
			oldtype = "0";

		StringBuffer newString = new StringBuffer(" (");
		String[] typS = oldtype.split(",");
		for (int i = 0; i < typS.length; i++) {
			if (i == 0) {
				newString.append(keyname).append(" like '").append(typS[i])
				.append("%' ");
			} else {
				newString.append(" or ").append(keyname).append(" like '")
				.append(typS[i]).append("%'");
			}
		}
		newString.append(") ");
		return newString.toString();

	}

	/**
	 * 格式转换，将12,34,56 格式转换为'12','34','56'
	 * 
	 * @param oldtype
	 * @return
	 */
	public static String typChange(String oldtype) {
		StringBuffer newString = new StringBuffer("");
		String[] typS = oldtype.split(",");
		for (int i = 0; i < typS.length; i++) {
			if (i == 0) {
				newString.append("'").append(typS[i]).append("'");
			} else {
				newString.append(",'").append(typS[i]).append("'");
			}
		}
		return newString.toString();
	}

	static public String getPlStrListForString(List<String> reource) {
		// //System.out.println(reource);
		if (reource == null)
			return "";
		StringBuffer tmp = new StringBuffer();
		String shenhe = "";
		for (int i = 0; i < reource.size(); i++) {
			tmp.append("'").append(reource.get(i)).append("',");
		}
		shenhe = tmp.toString().substring(0, tmp.length() - 1);

		return shenhe;

	}

	/**
	 * 将数组分割为1,2,3的格式
	 * 
	 * 返回字符：String
	 */

	public static char toLowerCase(char c) {
		int i = (int) c;
		if (i >= 65 && i <= 90) {// 65-90是A-Z的ASCII编码
			c = (char) (i + 32);// A是65，a是97正好差32
		}
		return c;
	}

	public static String html(String content) {

		if (content == null)
			return "";

		String html = content;

		html = StringUtils.replace(html, "'", "&apos;");

		html = StringUtils.replace(html, "\"", "\"");

		html = StringUtils.replace(html, "\t", "  ");// 替换跳格

		html = StringUtils.replace(html, " ", " ");// 替换空格

		html = StringUtils.replace(html, "%", "%");

		html = StringUtils.replace(html, "<", "<");

		html = StringUtils.replace(html, ">", ">");

		return html;

	}

	/**
	 * 判断字符串是否符合日期格式
	 *
	 * @return
	 */
	public static boolean isDate(String strDate) {
		SimpleDateFormat sdf = new SimpleDateFormat(
				"yyyy-MM-dd");
		SimpleDateFormat sdf1 = new SimpleDateFormat(
				"yyyy/MM/dd");
		sdf.setLenient(false);
		sdf1.setLenient(false);
		try {
			sdf.parse(strDate);
			return true;
		} catch (ParseException ex) {
			try {
				sdf1.parse(strDate);
				return true;
			} catch (ParseException e) {
				return false;
			}
		}
	}

	public static boolean isNumber(String str) {
		try {
			new BigDecimal(str);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	/**
	 * 数据库通用uuid
	 */
	public static String getStringUUid() {
		return UUID.randomUUID().toString();

	}

	public static String getToGbk(String str) {
		String strs = "";

		try {
			if (!"".equals(str)) {
				strs = new String(str.getBytes("iso-8859-1"), "GBK");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return strs;
	}

	/**
	 * 返回当前年
	 */

	public static String getNowYear() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
		return formatter.format(currentTime);

	}

	public static Integer getNowMonth() {
		String date = getNowYYYYMMDate();
		String month = date.substring(5, 7);

		return Integer.parseInt(month);
	}

	public static List getYearList(String begintime, String endTime) {
		List time_list = new ArrayList();

		if (begintime != null && endTime != null) {
			Date date1 = stringToDate2(begintime);
			Date date2 = stringToDate2(endTime);
			Calendar cal1 = new GregorianCalendar();
			cal1.setTime(date1);
			Calendar cal2 = new GregorianCalendar();
			cal2.setTime(date2);
			int c = cal2.get(Calendar.YEAR) - cal1.get(Calendar.YEAR);
			for (int i = 0; i <= c; i++) {
				if (i == 0) {
					cal1.add(Calendar.YEAR, 0);
				} else {
					cal1.add(Calendar.YEAR, 1);
				}
				SimpleDateFormat format = new SimpleDateFormat("yyyy");
				time_list.add(format.format(cal1.getTime()));
			}
		}
		return time_list;
	}

	public static List getTimeList(String begintime, String endTime) {
		List time_list = new ArrayList();

		if (begintime != null && endTime != null) {
			Date date1 = stringToDate1(begintime);
			Date date2 = stringToDate1(endTime);
			Calendar cal1 = new GregorianCalendar();
			cal1.setTime(date1);
			Calendar cal2 = new GregorianCalendar();
			cal2.setTime(date2);
			int c = (cal2.get(Calendar.YEAR) - cal1.get(Calendar.YEAR)) * 12
					+ cal2.get(Calendar.MONTH) - cal1.get(Calendar.MONTH);
			cal1.add(Calendar.MONTH, -1);
			for (int i = 0; i <= c; i++) {
				cal1.add(Calendar.MONTH, 1);
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
				time_list.add(format.format(cal1.getTime()));
			}
		}
		return time_list;
	}

	// 将日期字符串转换为日期变量,如果有问题,返回当前日期
	public static Date stringToDate1(String str) {
		try {
			DateFormat formatter = new SimpleDateFormat("yyyy-MM");
			return (Date) formatter.parse(str);
		} catch (ParseException e) {
			return new Date();
		}
	}

	public static Date stringToDate2(String str) {
		try {
			DateFormat formatter = new SimpleDateFormat("yyyy");
			return (Date) formatter.parse(str);
		} catch (ParseException e) {
			return new Date();
		}
	}

	// 将用逗号隔开的字符串转为数组;
	public static String[] split(String source) {
		if (source == null || source.trim().equals(""))
			return null;
		StringTokenizer commaToker = new StringTokenizer(source, ",");
		String[] result = new String[commaToker.countTokens()];
		int i = 0;
		while (commaToker.hasMoreTokens()) {
			result[i] = commaToker.nextToken();
			i++;
		}

		return result;
	}

	/**
	 * 
	 * 功能：克隆对象的值到另一个对象<br>
	 * 
	 * @param source
	 * @param target
	 * @return
	 */
	// public static Object cloneObj(Object source, Object target) {
	// Field[] fields = target.getClass().getDeclaredFields();
	// for (Field field : fields) {
	// try {
	// Method method1 = source.getClass().getDeclaredMethod(
	// "get" + field.getName().substring(0, 1).toUpperCase()
	// + field.getName().substring(1), new Class[0]);
	// Method method2 = target.getClass()
	// .getDeclaredMethod(
	// "set"
	// + field.getName().substring(0, 1)
	// .toUpperCase()
	// + field.getName().substring(1),
	// field.getType());
	// method2.invoke(target, method1.invoke(source, null));
	// } catch (SecurityException e) {
	// e.printStackTrace();
	// } catch (NoSuchMethodException e) {
	// e.printStackTrace();
	// } catch (IllegalArgumentException e) {
	// e.printStackTrace();
	// } catch (IllegalAccessException e) {
	// e.printStackTrace();
	// } catch (InvocationTargetException e) {
	// e.printStackTrace();
	// }
	// }
	// return target;
	// }

	/**
	 * 
	 * 功能：Map中的key对应实体field进行赋值<br>
	 * 
	 * @param source
	 * @param target
	 * @return
	 */
	public static Object cloneMapToObj(Map source, Object target) {
		Field[] fields = target.getClass().getDeclaredFields();
		for (Field field : fields) {

			if (source.get(field.getName().toLowerCase()) == null)
				continue;
			try {
				Method method2 = target.getClass()
						.getDeclaredMethod(
								"set"
										+ field.getName().substring(0, 1)
										.toUpperCase()
										+ field.getName().substring(1),
										field.getType());
				// //System.out.println(field.getType().toString()+"   "+field.getType().toString().equals(String.class.toString()));
				if (field.getType().toString().equals(String.class.toString())) {
					method2.invoke(target,
							source.get(field.getName().toLowerCase())
							.toString());
				}
				if (field.getType().toString().equals(int.class.toString())
						|| field.getType().toString()
						.equals(Integer.class.toString())) {
					method2.invoke(
							target,
							Integer.valueOf(source.get(
									field.getName().toLowerCase()).toString()));
				}
				if (field.getType().toString().equals(long.class.toString())
						|| field.getType().toString()
						.equals(Long.class.toString())) {
					method2.invoke(
							target,
							Long.valueOf(source.get(
									field.getName().toLowerCase()).toString()));
				}
				if (field.getType().toString().equals(double.class.toString())
						|| field.getType().toString()
						.equals(Double.class.toString())) {
					method2.invoke(
							target,
							Double.valueOf(source.get(
									field.getName().toLowerCase()).toString()));
				}
				if (field.getType().toString().equals(Date.class.toString())) {
					try {
						method2.invoke(target, new SimpleDateFormat(
								"yyyy-MM-dd").parse(source.get(
										field.getName().toLowerCase()).toString()));
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}

				if (field.getType().toString().equals(boolean.class.toString())) {
					method2.invoke(
							target,
							Boolean.parseBoolean(source.get(
									field.getName().toLowerCase()).toString()));
				}
				// method2.invoke(target, source.get(field.getName()));
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return target;
	}

	/**
	 * 生成一个随机的文件名，编码规则：年月日时分秒+两位随机数
	 * 
	 * @return
	 */
	public static String getRandFileName() {
		String filename = "";
		Date d = new Date();
		// System.currentTimeMillis();
		filename = stringDateTime2(d) + getRandom(10, 99);
		return filename;
	}

	public static String stringDateTime2(Date date) {
		Format formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		return formatter.format(date);
	}

	/**
	 * 随机生成min～max范围内的一个整数
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	public static int getRandom(int min, int max) {
		Random random = new Random();
		int ran = Math.abs(random.nextInt());
		int returnRan = ran % (max - min + 1) + min;
		return returnRan;
	}

	/**
	 * 将date格式2012-04改为：2012年01-04月份
	 * 
	 * @param date
	 * @return
	 */

	public static String getDateTime(String date) {
		String returndate = date.substring(0, 4) + "年01" + date.substring(4, 7);
		return returndate;
	}

	/**
	 * 格式转换，将List<Map> 中某个字段格式转换为'12','34','56'
	 *
	 * @return
	 */
	public static String ListMaptypChange(List dataList, String key) {
		StringBuffer newString = new StringBuffer("");
		for (int i = 0; dataList != null && i < dataList.size(); i++) {
			Map map = (HashMap) dataList.get(i);
			if (map.get(key) != null && !map.get(key).equals("")) {
				if (i == 0) {
					newString.append("'").append(map.get(key)).append("'");
				} else {
					newString.append(",'").append(map.get(key)).append("'");
				}
			}
		}
		return newString.toString();
	}

	/**
	 * 日期格式判断，如：2010-01，则返回2010-01，如果为2010-03，则返回2010-02
	 */

	public static String dateCheckNext(String checkDate, int inry) {
		String returnDate = "";
		if (checkDate != null && !checkDate.equals("")) {
			Date date1 = stringToDate(checkDate, "yyyy-MM");
			Calendar cal1 = new GregorianCalendar();
			cal1.setTime(date1);
			cal1.add(Calendar.MONTH, inry);
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
			returnDate = format.format(cal1.getTime());
		}
		return returnDate;
	}

	/**
	 * 将String转换为bigDecimal 不需要单位转换的，无小数
	 * 
	 * @param c
	 * @return
	 */
	public static BigDecimal objectTOBigDecimal(Object c) {
		if (c != null && !c.equals("")) {
			BigDecimal num = new BigDecimal(c.toString());
			BigDecimal num1 = BigDecimal.valueOf(num.doubleValue());
			num1 = num1.setScale(0, BigDecimal.ROUND_HALF_UP);
			return num1;
		} else {
			return new BigDecimal("0");

		}
	}

	/**
	 * 格式转换，将12,34,56 格式转换为 like '12%' or like '34%' or like '56%'
	 *
	 * @return
	 */
	public static String typLikeChangeList(List hylist, String keyname) {
		StringBuffer newString = new StringBuffer(" (");

		for (int i = 0; i < hylist.size(); i++) {
			if (i == 0) {
				newString.append(keyname).append(" like '")
				.append(hylist.get(i)).append("%' ");
			} else {
				newString.append(" or ").append(keyname).append(" like '")
				.append(hylist.get(i)).append("%'");
			}
		}
		newString.append(") ");
		// //System.out.println("  hy:        "+newString.toString());
		return newString.toString();
	}

	/** 加密用户输入的密码 */
	public static String enCodePass(String password) {
		BASE64Encoder encoder = new BASE64Encoder();
		String enCodeWords = encoder.encode(password.getBytes());
		return enCodeWords;
	}

	/** 获取oracle时间类型,返回值类型为Timestamp */
	public static Timestamp currentDate() {
		return new Timestamp(new Date().getTime());
	}

	/**
	 * 获取timestamp时间,去掉最后的毫秒
	 */
	public static String getCurrentDate() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = sdf.format(date);
		return time;
	}

	/**
	 * 获取(20131110111213)
	 */
	public static String getTimeStr() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String time = sdf.format(date);
		return time;
	}

	/***
	 * 获取10随机码,用作唯一标示
	 */
	public static String getCNCode() {
		try {
			Random random = new Random();
			String str_s = "";
			for (int i = 0; i < 10; i++) {
				int choice = random.nextInt(2) % 2 == 0 ? 65 : 97; // ȡ�ô�д����Сд
				char str = (char) (choice + random.nextInt(26));
				str_s += str;
			}
			return str_s;
		} catch (Exception e) {
			// e.printStackTrace();
			return "";
		}
	}

	// 获取项目路径
	public static String getPorjectPath(String projectName) {
		String nowpath; // 格式如: D:/java/software/apache-tomcat-6.0.14/bin
		String tempdir;
		nowpath = System.getProperty("user.dir");
		tempdir = nowpath.replace("bin", "webapps"); // 将bin用webapps替换
		tempdir += "/" + projectName; // 得到路径形如:D:/java/software/apache-tomcat-6.0.14/webapps/sz_pro
		tempdir = tempdir.replace("\\", "/");
		return tempdir;
	}


	/**
	 * 将一个 JavaBean 对象转化为一个  Map
	 * @param bean 要转化的JavaBean 对象
	 * @return 转化出来的  Map 对象
	 * @throws IntrospectionException 如果分析类属性失败
	 * @throws IllegalAccessException 如果实例化 JavaBean 失败
	 * @throws InvocationTargetException 如果调用属性的 setter 方法失败
	 */
	public static Map convertBean(Object bean)
			throws IntrospectionException, IllegalAccessException, InvocationTargetException {
		Class type = bean.getClass();
		Map returnMap = new HashMap();
		BeanInfo beanInfo = Introspector.getBeanInfo(type);

		PropertyDescriptor[] propertyDescriptors =  beanInfo.getPropertyDescriptors();
		for (int i = 0; i< propertyDescriptors.length; i++) {
			PropertyDescriptor descriptor = propertyDescriptors[i];
			String propertyName = descriptor.getName();
			if (!propertyName.equals("class")) {
				Method readMethod = descriptor.getReadMethod();
				Object result = readMethod.invoke(bean, new Object[0]);
				if (result != null) {
					returnMap.put(propertyName, result);
				} else {
					returnMap.put(propertyName, "");
				}
			}
		}
		return returnMap;
	}

	/**
	 * 56短信接口(短信发送,单条发送)
	 * @return
	 * @throws Exception
	 */
	public static int invokeHttpTest(String content) throws Exception 
	{
		URL url = new URL(
				"http://api.56dxw.com/sms/HttpInterface.aspx?comid=496&username=sdjh"
						+ "&userpwd=sdjh_wlb&handtel=18613604946&sendcontent="+content+""
						+ "&sendtime=定时时间&smsnumber=10690");
		HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
		httpCon.connect();
		BufferedReader in = new BufferedReader(new InputStreamReader(
				httpCon.getInputStream()));
		String line = in.readLine();
		////System.out.println(" </p>     result:   " + line);
		int i_ret = httpCon.getResponseCode();
		String sRet = httpCon.getResponseMessage();
		////System.out.println("sRet   is:   " + sRet);
		////System.out.println("i_ret   is:   " + i_ret);
		return 0;
	}

	/**
	 * 56短信接口(多条发送)
	 * 多条发送url:http://api.56dxw.com/sms/HttpInterfaceMore.aspx?comid=企业ID&username=用户名&userpwd=密码&handtel=手机号&sendcontent=内容限制为70个字，小灵通60个字&sendtime=定时时间&smsnumber=所用平台
	 * 需要循环,才能实现多条发送
	 * sendtime为定时时间,无则空
	 */
	public static boolean invokeHttpMulitySend(String content,String [] phones) throws Exception 
	{
		boolean flag=false;
		String phonenumber="";
		try {
			if (null != phones && phones.length > 0) {
				//签名【万正驾航自动化运营系统报送】
				content=content+"【万正驾航自动化运营系统报送】";
				for (int i = 0; i < phones.length; i++) {
					if(phonenumber.equals("")){
						phonenumber=phones[i];
					}else{
						phonenumber+=","+phones[i];
					}
				}
				content=URLEncoder.encode(content,"GBK");
				URL url = new URL("http://api.56dxw.com/sms/HttpInterfaceMore.aspx?comid=1713&username=sdjh&userpwd=sdjh_wlb&handtel="+phonenumber+"&sendcontent="+ content+ "&sendtime=&smsnumber=10690");
				//				////System.out.println("http://api.56dxw.com/sms/HttpInterfaceMore.aspx?comid=496&username=sdjh&userpwd=sdjh_wlb&handtel=18615341234&sendcontent="+ content+ "&sendtime=&smsnumber=10690");
				HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
				httpCon.connect();
				BufferedReader in = new BufferedReader(new InputStreamReader(httpCon.getInputStream()));
				String line = in.readLine();
				////System.out.println(" </p>     result:   " + line);
				int i_ret = httpCon.getResponseCode();
				String sRet = httpCon.getResponseMessage();
				////System.out.println("sRet   is:   " + sRet);
				////System.out.println("i_ret   is:   " + i_ret);
				if (i_ret > 0) {
					flag = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 判断短信内容是否超出170,超出则截取
	 */
	public static void checkContent(String content,int byteNum,String [] phones){
		int length=content.length();
		int ceil=(int) Math.ceil(length/byteNum);
		String con="";
		try {
			for(int i=0;i<ceil;i++){
				if(i==0){
					con=content.substring(0, byteNum);
					con=con+"【万正驾航自动化运营系统报送】";
					invokeHttpMulitySend(con,phones);
					Thread.sleep(6000);
				}else{
					int beginIndex=i*byteNum+1;
					int endIndex=(i+1)*byteNum;
					if((i*byteNum+1)<content.length()&&i*byteNum<content.length()){
						con=content.substring(beginIndex, endIndex);
						con=con+"【万正驾航自动化运营系统报送】";
						invokeHttpMulitySend(con,phones);
						Thread.sleep(6000);
					}else{
						con=content.substring(byteNum+1, content.length());
						con=con+"【万正驾航自动化运营系统报送】";
						invokeHttpMulitySend(con,phones);
						Thread.sleep(6000);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	} 

	/**
	 * 将一个 Map 对象转化为一个 JavaBean
	 * @param type 要转化的类型
	 * @param map 包含属性值的 map
	 * @return 转化出来的 JavaBean 对象
	 * @throws IntrospectionException
	 *             如果分析类属性失败
	 * @throws IllegalAccessException
	 *             如果实例化 JavaBean 失败
	 * @throws InstantiationException
	 *             如果实例化 JavaBean 失败
	 * @throws InvocationTargetException
	 *             如果调用属性的 setter 方法失败
	 */
	public static Object convertMap(Class type, Map map) {
		BeanInfo beanInfo;
		Object obj=null;
		try {
			// 获取类属性
			beanInfo = Introspector.getBeanInfo(type);
			obj = type.newInstance(); // 创建 JavaBean 对象
			// 给 JavaBean 对象的属性赋值
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (int i = 0; i < propertyDescriptors.length; i++) {
				PropertyDescriptor descriptor = propertyDescriptors[i];
				String propertyName = descriptor.getName();
				if (map.containsKey(propertyName)) {
					// 下面一句可以 try 起来，这样当一个属性赋值失败的时候就不会影响其他属性赋值。
					try {
						Object value = map.get(propertyName);
						Object[] args = new Object[1];
						args[0] = value;
						if(descriptor.getPropertyType().toString().equals(Timestamp.class.toString())){
							SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
							SimpleDateFormat sdf2=new SimpleDateFormat("yyyy.MM.dd");
							Date newDate=sdf2.parse(value.toString());
							String newTime=sdf.format(newDate);
							Timestamp ts = Timestamp.valueOf(newTime+" 00:00:00");
							args[0]=ts;
						}
						descriptor.getWriteMethod().invoke(obj, args);
					} catch (Exception e) {
						log.error("Tool工具类中的方法convertMap异常,异常Exception信息为:"+e);
					}
				}
			}
		} catch (IntrospectionException e) {
			log.error("Tool工具类中的方法convertMap异常,异常IntrospectionException信息为:"+e);
		} catch (InstantiationException e) {
			log.error("Tool工具类中的方法convertMap异常,异常InstantiationException信息为:"+e);
		} catch (IllegalAccessException e) {
			log.error("Tool工具类中的方法convertMap异常,异常IllegalAccessException信息为:"+e);
		} catch (IllegalArgumentException e) {
			log.error("Tool工具类中的方法convertMap异常,异常IllegalArgumentException信息为:"+e);
		} 
		return obj;
	}

	/** 
	 * 将map转换成Javabean 
	 * @param javabean javaBean 
	 * @param data map数据 
	 */ 
	public static Object toJavaBean(Object javabean, Map<String, String> data) {
		Method[] methods = javabean.getClass().getDeclaredMethods();
		for (Method method : methods) {
			try {
				if (method.getName().startsWith("set")) {
					String field = method.getName();
					field = field.substring(field.indexOf("set") + 3);
					field = field.toLowerCase().charAt(0) + field.substring(1);
					method.invoke(javabean, new Object[] { data.get(field) });
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return javabean;
	}

	/**
	 * 生成流水号
	 */
	public static String genSerialNumber(){
		String date = new SimpleDateFormat("yyyyMMdd").format(new Date());  
		String seconds = new SimpleDateFormat("HHmmss").format(new Date());  
		return date+"00001000"+getTwo()+"00"+seconds+getTwo();
	}

	/**
	 * 产生随机的2位数
	 * @return
	 */
	public static String getTwo(){
		Random rad=new Random();

		String result  = rad.nextInt(100) +"";

		if(result.length()==1){
			result = "0" + result;
		}
		return result;
	}

	/**
	 * 创建指定数量的随机字符串
	 * @param numberFlag 是否是数字
	 * @param length
	 * @return
	 */
	public static String createRandom(boolean numberFlag, int length){
		String retStr = "";
		String strTable = numberFlag ? "1234567890" : "1234567890abcdefghijkmnpqrstuvwxyz";
		int len = strTable.length();
		boolean bDone = true;
		do {
			retStr = "";
			int count = 0;
			for (int i = 0; i < length; i++) {
				double dblR = Math.random() * len;
				int intR = (int) Math.floor(dblR);
				char c = strTable.charAt(intR);
				if (('0' <= c) && (c <= '9')) {
					count++;
				}
				retStr += strTable.charAt(intR);
			}
			if (count >= 2) {
				bDone = false;
			}
		} while (bDone);

		return retStr;
	}

	public static boolean checkEmail(String email)
	{// 验证邮箱的正则表达式 
		String format = "\\p{Alpha}\\w{2,15}[@][a-z0-9]{3,}[.]\\p{Lower}{2,}";
		//p{Alpha}:内容是必选的，和字母字符[\p{Lower}\p{Upper}]等价。如：200896@163.com不是合法的。
		//w{2,15}: 2~15个[a-zA-Z_0-9]字符；w{}内容是必选的。 如：dyh@152.com是合法的。
		//[a-z0-9]{3,}：至少三个[a-z0-9]字符,[]内的是必选的；如：dyh200896@16.com是不合法的。
		//[.]:'.'号时必选的； 如：dyh200896@163com是不合法的。
		//p{Lower}{2,}小写字母，两个以上。如：dyh200896@163.c是不合法的。
		if (email.matches(format))
		{ 
			return true;// 邮箱名合法，返回true 
		}
		else
		{
			return false;// 邮箱名不合法，返回false
		}
	} 

	/**
	 * 大陆号码或香港号码均可
	 */
	public static boolean isPhoneLegal(String str)throws PatternSyntaxException {
		return isChinaPhoneLegal(str) || isHKPhoneLegal(str);
	}

	/**
	 * 大陆手机号码11位数，匹配格式：前三位固定格式+后8位任意数
	 * 此方法中前三位格式有：
	 * 13+任意数
	 * 15+除4的任意数
	 * 18+除1和4的任意数
	 * 17+除9的任意数
	 * 147
	 */
	public static boolean isChinaPhoneLegal(String str) throws PatternSyntaxException {
		String regExp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher(str);
		return m.matches();
	}

	/**
	 * 香港手机号码8位数，5|6|8|9开头+7位任意数
	 */
	public static boolean isHKPhoneLegal(String str)throws PatternSyntaxException {
		String regExp = "^(5|6|8|9)\\d{7}$";
		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher(str);
		return m.matches();
	}

	//判断字符串是否有长度，不等于null同时长度大于0，则为true
	//这里重载两个hasLength方法，其中CharSequence是String的父类，是接口
	public static boolean hasLength(CharSequence str)
	{
		return str != null && str.length() > 0;
	}

	public static boolean hasLength(String str)
	{
		return hasLength(((CharSequence) (str)));
	}

	//如果为空则直接返回false，如果字符串中有一个不是空白，则表示
	//有内容，返回true
	public static boolean hasText(CharSequence str)
	{
		if(!hasLength(str))
			return false;
		int strLen = str.length();
		//遍历字符序列，如果其中有一个不是空白，则返回true
		for(int i = 0; i < strLen; i++)
			if(!Character.isWhitespace(str.charAt(i)))
				return true;

		return false;
	}

	/**
	 * 元转换成分
	 * @return
	 */
	public static  String getMoney(String amount) {
		if(amount==null){
			return "";
		}
		// 金额转化为分为单位
		String currency =  amount.replaceAll("\\$|\\￥|\\,", "");  //处理包含, ￥ 或者$的金额  
		int index = currency.indexOf(".");  
		int length = currency.length();  
		Long amLong = 0l;  
		if(index == -1){  
			amLong = Long.valueOf(currency+"00");  
		}else if(length - index >= 3){  
			amLong = Long.valueOf((currency.substring(0, index+3)).replace(".", ""));  
		}else if(length - index == 2){  
			amLong = Long.valueOf((currency.substring(0, index+2)).replace(".", "")+0);  
		}else{  
			amLong = Long.valueOf((currency.substring(0, index+1)).replace(".", "")+"00");  
		}  
		return amLong.toString(); 
	}

	/**
	 * description: 解析微信通知xml
	 * 
	 * @param xml
	 * @return
	 * @author ex_yangxiaoyi
	 * @see
	 */
	@SuppressWarnings( { "unused"})
	public static Map parseXmlToList2(String xml) {
		Map retMap = new HashMap();
		try {
			StringReader read = new StringReader(xml);
			// 创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
			InputSource source = new InputSource(read);
			// 创建一个新的SAXBuilder
			SAXBuilder sb = new SAXBuilder();
			// 通过输入源构造一个Document
			Document doc = (Document) sb.build(source);
			Element root = doc.getRootElement();// 指向根节点
			List<Element> es = root.getChildren();
			if (es != null && es.size() != 0) {
				for (Element element : es) {
					retMap.put(element.getName(), element.getValue());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retMap;
	}

	public static boolean hasText(String str)
	{
		return hasText(((CharSequence) (str)));
	}


	public static String changeString(String time) {
		Date dat=new Date(time);
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss"); 
		String st=formatter.format(dat);
		return st;
	}

	public static Timestamp str2Timestamp(String timeStr)
	{
		Timestamp result = null;
		Date d = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			d = sdf.parse(timeStr);
			result = new Timestamp(d.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * @Description 字符串转timestamp yyyy-MM-dd
	 * @param timeStr
	 * @return
	 * Create at: 2018年9月21日 上午9:17:33
	 * @author: shenbinbin
	 * Revision:
	 *    2018年9月21日 上午9:17:33 - first revision by shenbinbin
	 *        
	 */
	public static Timestamp string2Timestamp(String timeStr)
	{
		Timestamp result = null;
		Date d = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			d = sdf.parse(timeStr);
			result = new Timestamp(d.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 获取客户端ip
	 * @param request
	 * @return
	 */
	public static String getIp2(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if(ip!=null&&!ip.equals("") && !"unKnown".equalsIgnoreCase(ip)){
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if(index != -1){
                return ip.substring(0,index);
            }else{
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if(ip!=null&&!ip.equals("") && !"unKnown".equalsIgnoreCase(ip)){
            return ip;
        }
        return request.getRemoteAddr();
    }
	
	@SuppressWarnings("unused")
	public static String getHeadersToken(HttpServletRequest request) {
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            if(key.contains("authorization")) {
            	return request.getHeader(key);
            }
        }
        return "";
	}
	
	@SuppressWarnings("unused")
	public static String createToken() {
		UUID uuid = UUID.randomUUID();
        // 得到对象产生的ID
        String token = uuid.toString();
        // 转换为大写
        token = token.toUpperCase();
        // 替换 “-”变成空格
        token = token.replaceAll("-", "");
        System.out.println(token);
        return token;
	}

}
