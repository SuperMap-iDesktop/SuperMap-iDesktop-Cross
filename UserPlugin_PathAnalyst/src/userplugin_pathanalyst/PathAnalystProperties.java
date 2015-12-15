package userplugin_pathanalyst;

import java.util.ResourceBundle;

import com.supermap.desktop.properties.Properties;

public class PathAnalystProperties extends Properties {
	public static final String PATH_ANALYST="resources.PathAnalyst";
	
	public static final String getString(String key) {
		return getString(PATH_ANALYST, key);
	}

	public static final String getString(String baseName, String key) {
		String result = "";

		ResourceBundle resourceBundle = ResourceBundle.getBundle(baseName,
				getLocale());
		if (resourceBundle != null) {
			result = resourceBundle.getString(key);
		}
		return result;
	}
}
