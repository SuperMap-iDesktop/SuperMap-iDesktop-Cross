package com.supermap.desktop.utilities;

import com.supermap.desktop.Application;
import com.supermap.desktop.properties.CoreProperties;

import java.text.MessageFormat;

/**
 * Created by Administrator on 2015/11/19.
 */
public class BrowseUtilities {

	private BrowseUtilities() {
		// 工具类不提供构造函数
	}

	public static void openUrl(String url) {
		Runtime runtime = Runtime.getRuntime();
		try {
			CursorUtilities.setWaitCursor();
			if (SystemPropertyUtilities.isWindows()) {
				runtime.exec("rundll32 url.dll,FileProtocolHandler " + url);
			} else {
				String[] browsers = { "epiphany", "firefox", "mozilla", "konqueror", "netscape", "opera", "links", "lynx" };

				StringBuffer cmd = new StringBuffer();
				for (int i = 0; i < browsers.length; i++)
					cmd.append((i == 0 ? "" : " || ") + browsers[i] + " \"" + url + "\" ");
				runtime.exec(new String[] { "sh", "-c", cmd.toString() });
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(MessageFormat.format(CoreProperties.getString("String_NotFoundSupportBrowse"), url));
		} finally {
			CursorUtilities.setDefaultCursor();
		}
	}
}
