package com.supermap.desktop.properties;

import java.util.ResourceBundle;

public class SpatialIndexTypeProperties extends Properties {
	public static final String SPATIALINDEXTYPE = "resources.SpatialIndexType";

	public static final String getString(String key) {
		return getString(SPATIALINDEXTYPE, key);
	}

	public static final String getString(String baseName, String key) {
		String result = "";

		ResourceBundle resourceBundle = ResourceBundle.getBundle(baseName, getLocale());
		if (resourceBundle != null) {
			result = resourceBundle.getString(key);
		}
		return result;
	}

	public static final String MultiLevelGrid = "String_MultiLevelGrid";
	public static final String None = "String_None";
	public static final String QTree = "String_QTree";
	public static final String RTree = "String_RTree";
	public static final String Tile = "String_Tile";
}
