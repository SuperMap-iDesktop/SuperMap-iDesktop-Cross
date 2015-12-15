package com.supermap.desktop.utilties;

import com.supermap.data.SpatialIndexType;
import com.supermap.desktop.Application;
import com.supermap.desktop.properties.SpatialIndexTypeProperties;

public class SpatialIndexTypeUtilties {
	private SpatialIndexTypeUtilties() {
		// 工具类不提供构造函数
	}

	public static String toString(SpatialIndexType data) {
		String result = "";

		try {
			if (data == SpatialIndexType.NONE) {
				result = SpatialIndexTypeProperties.getString(SpatialIndexTypeProperties.None);
			} else if (data == SpatialIndexType.MULTI_LEVEL_GRID) {
				result = SpatialIndexTypeProperties.getString(SpatialIndexTypeProperties.MultiLevelGrid);
			} else if (data == SpatialIndexType.QTREE) {
				result = SpatialIndexTypeProperties.getString(SpatialIndexTypeProperties.QTree);
			} else if (data == SpatialIndexType.RTREE) {
				result = SpatialIndexTypeProperties.getString(SpatialIndexTypeProperties.RTree);
			} else if (data == SpatialIndexType.TILE) {
				result = SpatialIndexTypeProperties.getString(SpatialIndexTypeProperties.Tile);
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return result;
	}

	public static SpatialIndexType valueOf(String text) {
		SpatialIndexType result = SpatialIndexType.NONE;

		try {
			if (text.equalsIgnoreCase(SpatialIndexTypeProperties.getString(SpatialIndexTypeProperties.None))) {
				result = SpatialIndexType.NONE;
			} else if (text.equalsIgnoreCase(SpatialIndexTypeProperties.getString(SpatialIndexTypeProperties.MultiLevelGrid))) {
				result = SpatialIndexType.MULTI_LEVEL_GRID;
			} else if (text.equalsIgnoreCase(SpatialIndexTypeProperties.getString(SpatialIndexTypeProperties.QTree))) {
				result = SpatialIndexType.QTREE;
			} else if (text.equalsIgnoreCase(SpatialIndexTypeProperties.getString(SpatialIndexTypeProperties.RTree))) {
				result = SpatialIndexType.RTREE;
			} else if (text.equalsIgnoreCase(SpatialIndexTypeProperties.getString(SpatialIndexTypeProperties.Tile))) {
				result = SpatialIndexType.TILE;
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return result;
	}
}
