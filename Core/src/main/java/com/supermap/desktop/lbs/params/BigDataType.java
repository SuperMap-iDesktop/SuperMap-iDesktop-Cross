package com.supermap.desktop.lbs.params;

/**
 * Created by caolp on 2017-06-24.
 */
public class BigDataType {
	private String resultName;
	private String message;


	public static BigDataType KernelDensity = new BigDataType("KernelDensity", "map-kernelDensity");
	public static BigDataType Heatmap = new BigDataType("Heatmap", "mongodb");
	public static BigDataType GridRegionAggregation = new BigDataType("GridRegionAggregation","map-summaryMesh");
	public static BigDataType OverlayAnalystGeo = new BigDataType("OverlayAnalystGeo", "map-overlayAnalystGeo");


	private BigDataType(String resultName, String message) {
		this.resultName = resultName;
		this.message = message;
	}

	public String getResultName() {
		return resultName;
	}

	public String getMessage() {
		return message;
	}
}
