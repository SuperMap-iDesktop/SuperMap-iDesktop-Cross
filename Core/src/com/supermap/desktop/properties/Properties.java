package com.supermap.desktop.properties;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * 在 equinox 框架里，ResourceBundle 的查找是基于 getBundle 方法所在 的代码文件的，而不是基于调用代码所在文件的，也就是说，如果在 Core 里封装了 getBundle 等相关方法，然后在其他工程调用这个方法，将不能正确获取资源文件。 因此，其余工程各自实现 getString
 * 方法。
 * 
 * @author wuxb
 */
public abstract class Properties {
	private static Locale locale = Locale.getDefault();

	public static final Locale getLocale() {
		return locale;
	}

	public static void setLocale(String language, String country) {
		locale = new Locale(language, country);
	}
}
