package com.supermap.desktop.utilities;

import com.supermap.data.DatasetType;
import com.supermap.data.SpatialQueryMode;
import com.supermap.desktop.properties.CoreProperties;

import java.util.ArrayList;

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

	public static SpatialQueryMode[] getSupportSpatialQueryModes(DatasetType searchLayerType, DatasetType currentLayerType) {
		if (currentLayerType == DatasetType.CAD) {
			return new SpatialQueryMode[]{CONTAIN, CROSS, DISJOINT, IDENTITY, INTERSECT, OVERLAP, TOUCH, WITHIN};
		}
		if (searchLayerType == DatasetType.CAD) {
			// CAD图层只能查询CAD图层
			return new SpatialQueryMode[0];
		}
		int srcDim = getDatasetTypeValue(searchLayerType);
		int queryDim = getDatasetTypeValue(currentLayerType);
		if (srcDim == -1 || queryDim == -1) {
			return new SpatialQueryMode[0];
		}
		ArrayList<SpatialQueryMode> spatialQueryModes = new ArrayList<>();
		if (srcDim <= queryDim) {
			spatialQueryModes.add(CONTAIN);
		}
		if (srcDim != 0 && queryDim == 1) {
			spatialQueryModes.add(CROSS);
		}
		spatialQueryModes.add(DISJOINT);
		if (srcDim == queryDim) {
			spatialQueryModes.add(IDENTITY);
		}
		spatialQueryModes.add(INTERSECT);
		if (srcDim != 0 && srcDim == queryDim) {
			spatialQueryModes.add(OVERLAP);
		}
		if (srcDim != 0 || queryDim != 0) {
			spatialQueryModes.add(TOUCH);
		}
		if (srcDim >= queryDim) {
			spatialQueryModes.add(WITHIN);
		}
		return spatialQueryModes.toArray(new SpatialQueryMode[spatialQueryModes.size()]);
	}

	// 组件不提供接口，按照.net表现出来的选项不正确
//	public static SpatialQueryMode[] getSupportSpatialQueryModes(DatasetType searchLayerType, DatasetType currentLayerType) {
//		SpatialQueryMode[] result = null;
//		if (currentLayerType == DatasetType.CAD && searchLayerType == DatasetType.CAD) {
//			return new SpatialQueryMode[]{CONTAIN, CROSS, DISJOINT, IDENTITY, INTERSECT, OVERLAP, TOUCH, WITHIN};
//		}
//		if (searchLayerType == DatasetType.CAD) {
//			return new SpatialQueryMode[0];
//		}
//
//		int typeValue = getDatasetTypeValue(currentLayerType);
//		int datasetTypeValue = getDatasetTypeValue(searchLayerType);
//		int i = (datasetTypeValue << 4) + typeValue;
//		switch (i) {
//			case  0x11:
//				// 点点
//				result = new SpatialQueryMode[]{CONTAIN, DISJOINT, IDENTITY, INTERSECT, WITHIN};
//				break;
//			case 0x12:
//				// 点线
//				result = new SpatialQueryMode[]{DISJOINT, INTERSECT, TOUCH, WITHIN};
//				break;
//			case 0x13:
//				// 点面
//				result = new SpatialQueryMode[]{DISJOINT, INTERSECT, TOUCH, WITHIN};
//				break;
//			case 0x14:
//				// 点文本
//				result = new SpatialQueryMode[]{CONTAIN,INTERSECT};
//				break;
//			case 0x21:
//				// 线点
//				result = new SpatialQueryMode[]{CONTAIN,DISJOINT,INTERSECT,TOUCH};
//				break;
//			case 0x22:
//				// 线线
//				result = new SpatialQueryMode[]{CONTAIN, CROSS, DISJOINT, IDENTITY, INTERSECT, OVERLAP, TOUCH, WITHIN};
//				break;
//			case 0x23:
//				// 线面
//				result = new SpatialQueryMode[]{CROSS, DISJOINT, INTERSECT, TOUCH, WITHIN};
//				break;
//			case 0x24:
//				// 线文本
//				result = new SpatialQueryMode[]{CONTAIN, INTERSECT};
//				break;
//			case 0x31:
//				// 面点
//				result = new SpatialQueryMode[]{CONTAIN, DISJOINT, INTERSECT, TOUCH};
//				break;
//			case 0x32:
//				// 面线
//				result = new SpatialQueryMode[]{CONTAIN, DISJOINT, INTERSECT, TOUCH};
//				break;
//			case 0x33:
//				// 面面
//				result = new SpatialQueryMode[]{CONTAIN, DISJOINT, IDENTITY, INTERSECT, OVERLAP, TOUCH, WITHIN};
//				break;
//			case 0x34:
//				// 面文本
//				result = new SpatialQueryMode[]{CONTAIN,INTERSECT};
//				break;
//			case 0x41:
//				// 文本点
//				result = new SpatialQueryMode[]{CONTAIN, INTERSECT};
//				break;
//				// 文本
//			case 0x42:
//				// 文本 线
//				result = new SpatialQueryMode[]{CONTAIN, INTERSECT};
//				break;
//			case 0x43:
//				// 文本 面
//				result = new SpatialQueryMode[]{CONTAIN, INTERSECT};
//				break;
//			case 0x44:
//				// 你不该来的！
////				result = new SpatialQueryMode[]{};
//				break;
//		}
//		return result;
//	}

	public static int getDatasetTypeValue(DatasetType datasetType) {
		if (datasetType == DatasetType.POINT || datasetType == DatasetType.POINT3D) {
			return 0;
		}
		if (datasetType == DatasetType.LINE || datasetType == DatasetType.NETWORK
				|| datasetType == DatasetType.NETWORK3D || datasetType == DatasetType.LINEM || datasetType == DatasetType.LINE3D) {
			return 1;
		}
		if (datasetType == DatasetType.REGION || datasetType == DatasetType.TEXT || datasetType == DatasetType.REGION3D) {
			return 2;
		}
//		if (datasetType == DatasetType.TEXT) {
//			return 4;
//		}
//		if (datasetType == DatasetType.CAD) {
//			return 4;
//		}
		return -1;
//		throw new UnsupportedOperationException(datasetType.name());
	}
}
