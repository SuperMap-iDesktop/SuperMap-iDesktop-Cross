package com.supermap.desktop.utilities;

import com.supermap.data.SpatialIndexInfo;

import java.util.List;

/**
 * 空间索引信息公共类
 *
 * @author xiajt
 */
public class SpatialIndexInfoUtilities {

	private static final int DOUBLE_PRECISION = 6;

	private SpatialIndexInfoUtilities() {

	}

	private static int POW = 10;

	public static String getSpatialIndexInfoX(List<SpatialIndexInfo> spatialIndexInfos) {
		double result = 0;
		boolean isDifferent = false;
		for (int i = 0; i < spatialIndexInfos.size(); i++) {
			double value = spatialIndexInfos.get(i).getGridCenter().getX();
			if (i == 0) {
				result = value;
			} else if (!DoubleUtilities.equals(result, value, POW)) {
				isDifferent = true;
				break;
			}
		}
		if (isDifferent) {
			return "";
		} else {
			return DoubleUtilities.toString(result, DOUBLE_PRECISION);
		}
	}

	public static String getSpatialIndexInfoY(List<SpatialIndexInfo> spatialIndexInfos) {
		double result = 0;
		boolean isDifferent = false;
		for (int i = 0; i < spatialIndexInfos.size(); i++) {
			double value = spatialIndexInfos.get(i).getGridCenter().getY();
			if (i == 0) {
				result = value;
			} else if (!DoubleUtilities.equals(result, value, POW)) {
				isDifferent = true;
				break;
			}
		}
		if (isDifferent) {
			return "";
		} else {
			return DoubleUtilities.toString(result, DOUBLE_PRECISION);
		}
	}

	public static String getSpatialIndexInfoGrid0(List<SpatialIndexInfo> spatialIndexInfos) {
		double result = 0;
		boolean isDifferent = false;
		for (int i = 0; i < spatialIndexInfos.size(); i++) {
			double value = spatialIndexInfos.get(i).getGridSize0();
			if (i == 0) {
				result = value;
			} else {

				if (!DoubleUtilities.equals(result, value, POW)) {
					isDifferent = true;
					break;
				}
			}
		}
		if (isDifferent) {
			return "";
		} else {
			return DoubleUtilities.toString(result, DOUBLE_PRECISION);
		}
	}

	public static String getSpatialIndexInfoGrid1(List<SpatialIndexInfo> spatialIndexInfos) {
		double result = 0;
		boolean isDifferent = false;
		for (int i = 0; i < spatialIndexInfos.size(); i++) {
			double value = spatialIndexInfos.get(i).getGridSize1();
			if (i == 0) {
				result = value;
			} else if (!DoubleUtilities.equals(result, value, POW)) {
				isDifferent = true;
				break;
			}
		}
		if (isDifferent) {
			return "";
		} else {
			return DoubleUtilities.toString(result, DOUBLE_PRECISION);
		}
	}

	public static String getSpatialIndexInfoGrid2(List<SpatialIndexInfo> spatialIndexInfos) {
		double result = 0;
		boolean isDifferent = false;
		for (int i = 0; i < spatialIndexInfos.size(); i++) {
			double value = spatialIndexInfos.get(i).getGridSize2();
			if (i == 0) {
				result = value;
			} else if (!DoubleUtilities.equals(result, value, POW)) {
				isDifferent = true;
				break;
			}
		}
		if (isDifferent) {
			return "";
		} else {
			return DoubleUtilities.toString(result, DOUBLE_PRECISION);
		}
	}

	public static String getSpatialIndexInfoTileField(List<SpatialIndexInfo> spatialIndexInfos) {
		String result = "";
		for (int i = 0; i < spatialIndexInfos.size(); i++) {
			String value = spatialIndexInfos.get(i).getTileField();
			if (i == 0) {
				result = value;
			} else if (!StringUtilities.stringEquals(result, value)) {
				result = "";
				break;
			}
		}
		return result;
	}

	public static String getSpatialIndexInfoTileWidth(List<SpatialIndexInfo> spatialIndexInfos) {
		double result = 0;
		boolean isDifferent = false;
		for (int i = 0; i < spatialIndexInfos.size(); i++) {
			double value = spatialIndexInfos.get(i).getTileWidth();
			if (i == 0) {
				result = value;
			} else if (!DoubleUtilities.equals(result, value, POW)) {
				isDifferent = true;
				break;
			}
		}
		if (isDifferent) {
			return "";
		} else {
			return DoubleUtilities.toString(result, DOUBLE_PRECISION);
		}
	}

	public static String getSpatialIndexInfoTileHeight(List<SpatialIndexInfo> spatialIndexInfos) {
		double result = 0;
		boolean isDifferent = false;
		for (int i = 0; i < spatialIndexInfos.size(); i++) {
			double value = spatialIndexInfos.get(i).getTileHeight();
			if (i == 0) {
				result = value;
			} else if (!DoubleUtilities.equals(result, value, POW)) {
				isDifferent = true;
				break;
			}
		}
		if (isDifferent) {
			return "";
		} else {
			return DoubleUtilities.toString(result, DOUBLE_PRECISION);
		}
	}
}
