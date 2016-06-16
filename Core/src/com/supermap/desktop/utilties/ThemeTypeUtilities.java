package com.supermap.desktop.utilties;

import java.util.HashMap;
import java.util.Iterator;

import com.supermap.desktop.Application;
import com.supermap.desktop.properties.ThemeTypeProperties;
import com.supermap.mapping.ThemeType;

public class ThemeTypeUtilities {
	private ThemeTypeUtilities() {
		// 工具类不提供构造函数
	}

	private static HashMap<ThemeType, String> themeType = new HashMap<ThemeType, String>();
	private static HashMap<ThemeType, String> themeTypeName = new HashMap<ThemeType, String>();

	private static void initializeThemeTypeName() {
		themeTypeName.put(ThemeType.CUSTOM, ThemeTypeProperties.getString(ThemeTypeProperties.THEMECUSTOM));
		themeTypeName.put(ThemeType.DOTDENSITY, ThemeTypeProperties.getString("String_ThemeDotDensity"));
		themeTypeName.put(ThemeType.GRADUATEDSYMBOL, ThemeTypeProperties.getString("String_ThemeGraduatedSymbol"));
		themeTypeName.put(ThemeType.GRAPH, ThemeTypeProperties.getString(ThemeTypeProperties.THEMEGRAPH));
		themeTypeName.put(ThemeType.GRIDRANGE, ThemeTypeProperties.getString("String_ThemeGridRange"));
		themeTypeName.put(ThemeType.GRIDUNIQUE, ThemeTypeProperties.getString("String_ThemeGridUnique"));
		themeTypeName.put(ThemeType.LABEL, ThemeTypeProperties.getString(ThemeTypeProperties.THEMELABEL));
		themeTypeName.put(ThemeType.RANGE, ThemeTypeProperties.getString(ThemeTypeProperties.THEMERANGE));
		themeTypeName.put(ThemeType.UNIQUE, ThemeTypeProperties.getString(ThemeTypeProperties.THEMEUNIQUE));
	}

	private static void initializeThemeType() {
		String themeImageParentPath = "/com/supermap/desktop/controlsresources/LegendControl/";
		themeType.put(ThemeType.CUSTOM, themeImageParentPath + "Image_Layer_ThemeCustom.png");
		themeType.put(ThemeType.DOTDENSITY, themeImageParentPath + "Image_Layer_ThemeDotdensity.png");
		themeType.put(ThemeType.GRADUATEDSYMBOL, themeImageParentPath + "Image_Layer_ThemeGraduated.png");
		themeType.put(ThemeType.GRAPH, themeImageParentPath + "Image_Layer_ThemeGraph.png");
		themeType.put(ThemeType.GRIDRANGE, themeImageParentPath + "Image_Layer_ThemeGridRange.png");
		themeType.put(ThemeType.GRIDUNIQUE, themeImageParentPath + "Image_Layer_ThemeGridUnique.png");
		themeType.put(ThemeType.LABEL, themeImageParentPath + "Image_Layer_ThemeLabel.png");
		themeType.put(ThemeType.RANGE, themeImageParentPath + "Image_Layer_ThemeRange.png");
		themeType.put(ThemeType.UNIQUE, themeImageParentPath + "Image_Layer_ThemeUnique.png");
	}

	/**
	 * 根据专题图类型得到对应图标
	 * 
	 * @param type
	 * @return
	 */
	public static String getImageIconPath(ThemeType type) {
		String resultIcon = "";
		try {
			initializeThemeType();
			if (null == type) {
				return "";
			} else {
				Iterator<?> iterator = themeType.entrySet().iterator();
				while (iterator.hasNext()) {
					@SuppressWarnings("rawtypes")
					java.util.Map.Entry<?, ?> entry = (java.util.Map.Entry<?, ?>) iterator.next();
					if (type == entry.getKey()) {
						resultIcon = (String) entry.getValue();
					}
				}
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return resultIcon;
	}

	/**
	 * 根据专题图中文名得到对应图标
	 * 
	 * @param themeTypeName
	 * @return
	 */
	public static String getImageIconPath(String themeTypeName) {
		return getImageIconPath(getThemeName(themeTypeName));
	}

	/**
	 * 根据专题图类型得到专题图名称
	 * 
	 * @param type
	 * @return
	 */
	public static String getThemeName(ThemeType type) {
		String resultName = "";
		try {
			initializeThemeTypeName();
			if (null == type) {
				return "";
			} else {
				Iterator<?> iterator = themeTypeName.entrySet().iterator();
				while (iterator.hasNext()) {
					java.util.Map.Entry<?, ?> entry = (java.util.Map.Entry<?, ?>) iterator.next();
					if (type == entry.getKey()) {
						resultName = (String) entry.getValue();
					}
				}
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return resultName;
	}

	/**
	 * 根据专题图类型得到专题图名称
	 * 
	 * @param type
	 * @return
	 */
	public static ThemeType getThemeName(String themeTypeNameStr) {
		ThemeType resultType = null;
		try {
			initializeThemeTypeName();
			Iterator<?> iterator = themeTypeName.entrySet().iterator();
			while (iterator.hasNext()) {
				java.util.Map.Entry<?, ?> entry = (java.util.Map.Entry<?, ?>) iterator.next();
				if (entry.getValue().equals(themeTypeNameStr)) {
					resultType = (ThemeType) entry.getKey();
				}
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return resultType;
	}

}
