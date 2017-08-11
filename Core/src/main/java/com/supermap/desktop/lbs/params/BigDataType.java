package com.supermap.desktop.lbs.params;

/**
 * Created by caolp on 2017-06-24.
 */
public class BigDataType {
	private String resultName;
	private String message;


	public static BigDataType KernelDensity = new BigDataType("KernelDensity", "map-kernelDensity");//密度分析
	public static BigDataType Heatmap = new BigDataType("Heatmap", "mongodb");//热度图
	public static BigDataType SummaryMesh = new BigDataType("SummaryMesh","map-summaryMesh");//点聚合分析
	public static BigDataType VectorClip = new BigDataType("VectorClip", "map-overlayAnalystGeo");//矢量裁剪分析
	public static BigDataType SummaryRegion = new BigDataType("SummaryRegion", "map-summaryRegion");//范围汇总分析
	public static BigDataType SingleQuery = new BigDataType("SingleQuery", "map-spatialQueryGeo");//单对象查询分析

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
