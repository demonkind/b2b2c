package net.shopnc.common.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * Created by hbj on 2015/12/2.
 */
public class UtilsHelper {

	public static boolean isEmpty(String value){
		if (value == null) {
			return true;
		}
		
		if ("".equals(value)) {
			return true;
		}
		
		
		if ("null".equals(value.toString().toLowerCase())) {
			return true;
		}
		
		return false;
	}
	
	public static String getString(Object value) {
		if (value == null) {
			return "";
		}
		
		if ("".equals(value)) {
			return "";
		}
		
		if ("null".equals(value.toString().toLowerCase())) {
			return "";
		}
		
		return String.valueOf(value);
	}
}
