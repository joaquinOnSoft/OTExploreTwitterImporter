package com.opentext.explore.util;

public class StringUtil {
	public static String[] stringToArrayString(String value) {
		if(value != null) {
			return value.split(",");
		}
		else {
			return new String[] {};
		}

	}
}
