package com.supermap.desktop.utilties;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.stream.events.StartDocument;

import com.supermap.desktop.Application;

public class StringUtilties {
	private StringUtilties() {
		// 工具类不提供构造函数
	}

	public static boolean isNullOrEmptyString(Object obj) {
		if (obj == null) {
			return true;
		}

		if (!(obj instanceof String)) {
			return true;
		}

		if (isTrimString((String) obj)) {
			return true;
		}
		
		return ((String) obj).isEmpty();
	}

	public static boolean isNullOrEmpty(String str) {
		return str == null || str.isEmpty();
	}

	public static boolean stringEquals(String str1, String str2) {
		return stringEquals(str1, str2, true);
	}

	private static boolean isTrimString(String str){
		return 0==str.trim().length();
	}
	/**
	 * 判断字符串是否数值
	 * 
	 * @param str
	 * @return true:是数值 ；false：不是数值
	 */
	public static boolean isNumber(String str) {
		Pattern pattern = Pattern.compile("-?[0-9]*.?[0-9]*");
		Matcher match = pattern.matcher(str);
		return match.matches();
	}

	/**
	 * 判断字符串是否为整数
	 * @param str
	 * @return
	 */
    public static boolean isNumeric(String str){  
        for (int i = str.length();--i>=0;){    
         if (!Character.isDigit(str.charAt(i))){  
          return false;  
         }  
        }  
        return true;  
      }  
	
	public static boolean stringEquals(String str1, String str2, boolean ignoreCase) {
		boolean result = false;

		try {
			// 同时为空，视为相同。null 和 空字符视为相同。
			if (isNullOrEmpty(str1) && isNullOrEmpty(str2)) {
				return true;
			}

			// 执行到这里时，表示不同时为空
			// 如果 str1 不为空，正常比较，得到结果
			// else str1 为空，那么 str2 一定不为空，result 保持 false 即可
			if (!isNullOrEmpty(str1)) {
				if (ignoreCase) {
					result = str1.equalsIgnoreCase(str2);
				} else {
					result = str1.equals(str2);
				}
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return result;
	}
}
