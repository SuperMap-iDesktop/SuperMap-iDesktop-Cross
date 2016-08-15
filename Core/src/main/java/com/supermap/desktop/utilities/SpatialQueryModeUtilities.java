package com.supermap.desktop.utilities;

import com.supermap.data.SpatialQueryMode;
import com.supermap.desktop.properties.CoreProperties;

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
			return SpatialQueryMode.DISJOINT;
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


	public String toString(SpatialQueryMode spatialQueryMode) {
		if (spatialQueryMode == SpatialQueryMode.CONTAIN) {
			return CoreProperties.getString("String_SpatialQuery_ContainCHS");
		} else if (spatialQueryMode == SpatialQueryMode.CROSS) {
			return CoreProperties.getString("String_SpatialQuery_CrossCHS");
		} else if (spatialQueryMode == SpatialQueryMode.DISJOINT) {
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
}
