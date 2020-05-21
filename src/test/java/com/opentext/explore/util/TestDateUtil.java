package com.opentext.explore.util;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Test;

import junit.framework.TestCase;

public class TestDateUtil extends TestCase {
	
	private int subStringToInt(String str, int init, int end) {
		return Integer.parseInt(str.substring(init, end));
	}
	
	@Test
	public void testNowToUTC() {
		Calendar now = GregorianCalendar.getInstance();
		
		String dateStr = DateUtil.nowToUTC();
		
		assertNotNull(dateStr);
		assertEquals(now.get(Calendar.YEAR), subStringToInt(dateStr, 0, 4));
		assertEquals(now.get(Calendar.MONTH) + 1, subStringToInt(dateStr, 5, 7));
		assertEquals(now.get(Calendar.DAY_OF_MONTH), subStringToInt(dateStr, 8, 10));		
	}

	@Test
	public void testUtcToDate() {
		final String utcRef = "2020-05-21T16:30:52Z";
		
		try {
			Date utc = DateUtil.utcToDate(utcRef);
			assertEquals(utcRef, DateUtil.dateToUTC(utc));
		} catch (ParseException e) {
			fail(e.getMessage());
		}
	}	
	
	@Test
	public void testDateToUTC() {
		Calendar now = GregorianCalendar.getInstance();
		String nowStr = DateUtil.dateToUTC(now.getTime());
		
		assertNotNull(nowStr);
		assertEquals(now.get(Calendar.YEAR), subStringToInt(nowStr, 0, 4));
		assertEquals(now.get(Calendar.MONTH) + 1, subStringToInt(nowStr, 5, 7));
		assertEquals(now.get(Calendar.DAY_OF_MONTH), subStringToInt(nowStr, 8, 10));		
	}
}
