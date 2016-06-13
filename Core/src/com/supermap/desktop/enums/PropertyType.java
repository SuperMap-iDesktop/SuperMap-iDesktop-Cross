package com.supermap.desktop.enums;

public enum PropertyType {
	// @formatter:off
	WORKSPACE,  // 工作空间基本属性
	DATASOURCE, // 数据源基本属性
	DATASET, // 数据集基本属性
	VECTOR, // 矢量数据属性
	GRID, // 栅格数据属性
	IMAGE, // 影像数据属性
	PRJCOORDSYS, // 投影信息
	RECORDSET, // 属性表结构
	GEOMETRY_REOCRD, // 对象属性信息
	GEOMETRY_SPATIAL, // 对象空间信息
	GEOMETRY_NODE, // 对象节点信息
	GEOMETRY_TEXT;// 文本属性信息
	// @formatter:on

	public static boolean isGeometryPropertyType(PropertyType propertyType) {
		return propertyType == null || propertyType == GEOMETRY_TEXT || propertyType == PropertyType.GEOMETRY_NODE
				|| propertyType == PropertyType.GEOMETRY_REOCRD || propertyType == PropertyType.GEOMETRY_SPATIAL;
	}
}
