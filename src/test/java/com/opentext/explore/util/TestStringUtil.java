package com.opentext.explore.util;

import org.junit.Test;

import junit.framework.TestCase;

public class TestStringUtil extends TestCase{
	@Test
	public void testStringToArrayString() {
		String[] array = StringUtil.stringToArrayString("Hola,Adios");
		assertNotNull(array);
		assertEquals(2, array.length);
		assertEquals("Hola", array[0]);
		assertEquals("Adios", array[1]);
	}
}
