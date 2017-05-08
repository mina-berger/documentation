package com.mina.selenium;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SeleniumUtil {
	public static boolean match(String regex, String text){
		if(regex == null){
			return text == null;
		}
		Matcher matcher = Pattern.compile(regex).matcher(text);
		return matcher.matches();
	}

}
