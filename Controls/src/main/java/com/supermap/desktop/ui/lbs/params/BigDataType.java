package com.supermap.desktop.ui.lbs.params;

/**
 * Created by caolp on 2017-06-24.
 */
public enum BigDataType {
	KernelDensity,
	Heatmap,
	GridRegionAggregation,
	OverlayAnalystGeo;

	@Override
	public String toString() {
		String result = null;
		switch (this) {
			case KernelDensity:
				result = "map-kernelDensity";
				break;
			case Heatmap:
				result = "mongodb";
				break;
			case GridRegionAggregation:
				result = "map-summaryMesh";
				break;
			case OverlayAnalystGeo:
				result = "map-overlayAnalystGeo";
				break;
			default:
				break;
		}
		return result;
	}
}
