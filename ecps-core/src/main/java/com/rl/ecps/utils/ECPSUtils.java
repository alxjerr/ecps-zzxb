package com.rl.ecps.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.http.HttpServletResponse;

public class ECPSUtils {
	
	public static String readProp(String key){
		Properties prop = new Properties();
		
		InputStream in = ECPSUtils.class.getClassLoader().getResourceAsStream("ecps_prop.properties");
		String value = null;
		try {
			prop.load(in);
			value = prop.getProperty(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

	public static void printJSON(String result,HttpServletResponse response){
		response.setCharacterEncoding("UTF-8");
		response.setContentType("html/text;charset=UTF-8");
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
