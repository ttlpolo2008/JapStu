package com.learning.japstu.japstu.utils;

public class StringUtil {


	public static Boolean isNullOrEmpty(Object value) {
		if (value == null || "".equals(value.toString())) {
			return true;
		}
		return false;
	}
}