package com.supermap.desktop.utilties;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetVector;
import com.supermap.data.SpatialIndexType;
import com.supermap.desktop.Application;
import com.supermap.desktop.properties.SpatialIndexTypeProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SpatialIndexTypeUtilities {
	private SpatialIndexTypeUtilities() {
		// 工具类不提供构造函数
	}

	public static final SpatialIndexType[] ALL_SPATIAL_INDEX_TYPE = new SpatialIndexType[]{
			SpatialIndexType.NONE,
			SpatialIndexType.RTREE,
			SpatialIndexType.QTREE,
			SpatialIndexType.MULTI_LEVEL_GRID,
			SpatialIndexType.TILE
	};

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

	/**
	 * 获得数据集支持的空间索引类型
	 *
	 * @param dataset 数据集
	 * @return 支持的类型
	 */
	public static String[] getSupportSpatialIndexType(Dataset dataset) {
		if (dataset == null || !(dataset instanceof DatasetVector)) {
			return null;
		}
		List<String> supportSpatialIndex = new ArrayList<>();
		for (SpatialIndexType spatialIndexType : ALL_SPATIAL_INDEX_TYPE) {
			if (((DatasetVector) dataset).isSpatialIndexTypeSupported(spatialIndexType)) {
				supportSpatialIndex.add(SpatialIndexTypeUtilities.toString(spatialIndexType));
			}
		}
		return supportSpatialIndex.toArray(new String[supportSpatialIndex.size()]);
	}

	/**
	 * 获得数据集都支持的空间索引类型
	 *
	 * @param datasets 数据集
	 * @return 支持的类型
	 */
	public static String[] getSupportSpatialIndexTypes(List<Dataset> datasets) {
		if (datasets.size() <= 0) {
			return new String[0];
		}
		HashMap<String, Integer> hashMap = new LinkedHashMap();

		hashMap.put(SpatialIndexTypeUtilities.toString(SpatialIndexType.NONE), 0);
		hashMap.put(SpatialIndexTypeUtilities.toString(SpatialIndexType.RTREE), 0);
		hashMap.put(SpatialIndexTypeUtilities.toString(SpatialIndexType.QTREE), 0);
		hashMap.put(SpatialIndexTypeUtilities.toString(SpatialIndexType.MULTI_LEVEL_GRID), 0);
		hashMap.put(SpatialIndexTypeUtilities.toString(SpatialIndexType.TILE), 0);

		String[] supportSpatialIndexType;
		for (Dataset dataset : datasets) {
			supportSpatialIndexType = getSupportSpatialIndexType(dataset);
			for (String s : supportSpatialIndexType) {
				hashMap.put(s, hashMap.get(s) + 1);
			}
		}

		List<String> result = new ArrayList<>();

		for (Map.Entry entry : hashMap.entrySet()) {
			if (Integer.valueOf(String.valueOf(entry.getValue())) == datasets.size()) {
				result.add(String.valueOf(entry.getKey()));
			}
		}
		return result.toArray(new String[result.size()]);
	}
}
