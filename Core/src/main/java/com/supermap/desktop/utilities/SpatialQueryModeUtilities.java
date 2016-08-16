package com.supermap.desktop.utilities;

import com.supermap.data.DatasetType;
import com.supermap.data.SpatialQueryMode;
import com.supermap.desktop.properties.CoreProperties;

import static com.supermap.data.SpatialQueryMode.CONTAIN;
import static com.supermap.data.SpatialQueryMode.CROSS;
import static com.supermap.data.SpatialQueryMode.DISJOINT;
import static com.supermap.data.SpatialQueryMode.IDENTITY;
import static com.supermap.data.SpatialQueryMode.INTERSECT;
import static com.supermap.data.SpatialQueryMode.OVERLAP;
import static com.supermap.data.SpatialQueryMode.TOUCH;
import static com.supermap.data.SpatialQueryMode.WITHIN;

/**
 * @author XiaJT
 */
public class SpatialQueryModeUtilities {
	private SpatialQueryModeUtilities() {

	}

	public static SpatialQueryMode getValue(String s) {
		if (s.equals(CoreProperties.getString("String_SpatialQuery_ContainCHS"))) {
			return SpatialQueryMode.CONTAIN;
		} else if (s.equals(CoreProperties.getString("String_SpatialQuery_CrossCHS"))) {
			return SpatialQueryMode.CROSS;
		} else if (s.equals(CoreProperties.getString("String_SpatialQuery_DisjointCHS"))) {
			return DISJOINT;
		} else if (s.equals(CoreProperties.getString("String_SpatialQuery_IdentityCHS"))) {
			return SpatialQueryMode.IDENTITY;
		} else if (s.equals(CoreProperties.getString("String_SpatialQuery_IntersectCHS"))) {
			return SpatialQueryMode.INTERSECT;
		} else if (s.equals(CoreProperties.getString("String_SpatialQuery_OverlapCHS"))) {
			return SpatialQueryMode.OVERLAP;
		} else if (s.equals(CoreProperties.getString("String_SpatialQuery_TouchCHS"))) {
			return SpatialQueryMode.TOUCH;
		} else if (s.equals(CoreProperties.getString("String_SpatialQuery_WithinCHS"))) {
			return SpatialQueryMode.WITHIN;
		}
		return SpatialQueryMode.NONE;
	}


	public static String toString(SpatialQueryMode spatialQueryMode) {
		if (spatialQueryMode == SpatialQueryMode.CONTAIN) {
			return CoreProperties.getString("String_SpatialQuery_ContainCHS");
		} else if (spatialQueryMode == SpatialQueryMode.CROSS) {
			return CoreProperties.getString("String_SpatialQuery_CrossCHS");
		} else if (spatialQueryMode == DISJOINT) {
			return CoreProperties.getString("String_SpatialQuery_DisjointCHS");
		} else if (spatialQueryMode == SpatialQueryMode.IDENTITY) {
			return CoreProperties.getString("String_SpatialQuery_IdentityCHS");
		} else if (spatialQueryMode == SpatialQueryMode.INTERSECT) {
			return CoreProperties.getString("String_SpatialQuery_IntersectCHS");
		} else if (spatialQueryMode == SpatialQueryMode.OVERLAP) {
			return CoreProperties.getString("String_SpatialQuery_OverlapCHS");
		} else if (spatialQueryMode == SpatialQueryMode.TOUCH) {
			return CoreProperties.getString("String_SpatialQuery_TouchCHS");
		} else if (spatialQueryMode == SpatialQueryMode.WITHIN) {
			return CoreProperties.getString("String_SpatialQuery_WithinCHS");
		}
		return "";
	}

	public static SpatialQueryMode[] getSupportSpatialQueryModes(DatasetType datasetType1, DatasetType datasetType2) {
		SpatialQueryMode[] result = null;
		int i = getDatasetTypeValue(datasetType1) | getDatasetTypeValue(datasetType2);
		switch (i) {
			case 1:
				// 点点
				result = new SpatialQueryMode[]{CONTAIN, DISJOINT, IDENTITY, INTERSECT, WITHIN};
				break;
			case 2:
				// 线线
				result = new SpatialQueryMode[]{CONTAIN, CROSS, DISJOINT, IDENTITY, INTERSECT, OVERLAP, TOUCH, WITHIN};
				break;
			case 3:
				// 点线
				result = new SpatialQueryMode[]{DISJOINT, INTERSECT, TOUCH, WITHIN};
				break;
			case 4:
				// 面面
				result = new SpatialQueryMode[]{CONTAIN, DISJOINT, IDENTITY, INTERSECT, OVERLAP, TOUCH, WITHIN};
				break;
			case 5:
				// 面点
				result = new SpatialQueryMode[]{CONTAIN, DISJOINT, INTERSECT, TOUCH};
				break;
			case 6:
				// 面线
				result = new SpatialQueryMode[]{CONTAIN, DISJOINT, INTERSECT, TOUCH};
				break;
			case 8:
				// 文本
				// 你不该来的！
//				result = new SpatialQueryMode[]{};
				break;
			case 9:
				// 文本点
				result = new SpatialQueryMode[]{CONTAIN, INTERSECT};
				break;
			case 10:
				// 文本 线
				result = new SpatialQueryMode[]{CONTAIN, INTERSECT};
				break;
			case 12:
				// 文本 面
				result = new SpatialQueryMode[]{CONTAIN, INTERSECT};
				break;

		}
		return result;
	}

	private static int getDatasetTypeValue(DatasetType datasetType) {
		if (datasetType == DatasetType.POINT) {
			return 1;
		}
		if (datasetType == DatasetType.LINE || datasetType == DatasetType.NETWORK) {
			return 2;
		}
		if (datasetType == DatasetType.REGION) {
			return 4;
		}
		if (datasetType == DatasetType.TEXT) {
			return 8;
		}
		throw new UnsupportedOperationException(datasetType.name());
	}
}
