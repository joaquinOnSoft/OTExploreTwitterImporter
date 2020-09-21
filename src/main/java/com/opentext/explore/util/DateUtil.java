package com.opentext.explore.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 
 * @author Joaquín Garzón
 * @since 20.2
 */
public class DateUtil {
	/**
	 * Return current time in UTC format, e.g.
	 * 2020-05-21T16:30:52.123Z
	 * @return current time in UTC format
	 */
	public static String nowToUTC() {
		return Instant.now().toString() ;
	}

	public static String dateToUTC(Date d) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");  
		return dateFormat.format(d);  
	}	
	
	public static String getDateOneWeekAgo() {
		LocalDate date = LocalDate.now().minusDays(7);
		return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	}	
	
	/**
	 * Generate a Date object from a string in UTC format 
	 * @param utc - String which contains a date in UTC format, e.g.
	 * "2020-05-21T16:30:52.123Z"
	 * @return Date object from a string in UTC format
	 * @throws ParseException 
	 */	
	public static Date utcToDate(String utc) throws ParseException {
		return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(utc);  
	}
}
