package com.supermap.desktop.utilties;

import com.supermap.data.DatasetType;
import com.supermap.desktop.Application;
import com.supermap.desktop.properties.DatasetTypeProperties;

public class DatasetTypeUtilties {
	private DatasetTypeUtilties() {
		// 工具类不提供构造函数
	}

	public static String toString(DatasetType datasetType) {
		String result = "";

		try {
			if (datasetType == DatasetType.CAD) {
				result = DatasetTypeProperties.getString(DatasetTypeProperties.CAD);
			} else if (datasetType == DatasetType.GRID) {
				result = DatasetTypeProperties.getString(DatasetTypeProperties.Grid);
			} else if (datasetType == DatasetType.GRIDCOLLECTION) {
				result = DatasetTypeProperties.getString(DatasetTypeProperties.GridCollection);
			} else if (datasetType == DatasetType.IMAGE) {
				result = DatasetTypeProperties.getString(DatasetTypeProperties.Image);
			} else if (datasetType == DatasetType.IMAGECOLLECTION) {
				result = DatasetTypeProperties.getString(DatasetTypeProperties.ImageCollection);
			} else if (datasetType == DatasetType.LINE) {
				result = DatasetTypeProperties.getString(DatasetTypeProperties.Line);
			} else if (datasetType == DatasetType.LINE3D) {
				result = DatasetTypeProperties.getString(DatasetTypeProperties.Line3D);
			} else if (datasetType == DatasetType.LINEM) {
				result = DatasetTypeProperties.getString(DatasetTypeProperties.LineM);
			} else if (datasetType == DatasetType.LINKTABLE) {
				result = DatasetTypeProperties.getString(DatasetTypeProperties.LinkTable);
			} else if (datasetType == DatasetType.MODEL) {
				result = DatasetTypeProperties.getString(DatasetTypeProperties.Model);
			} else if (datasetType == DatasetType.NETWORK) {
				result = DatasetTypeProperties.getString(DatasetTypeProperties.Network);
			} else if (datasetType == DatasetType.NETWORK3D) {
				result = DatasetTypeProperties.getString(DatasetTypeProperties.Network3D);
			} else if (datasetType == DatasetType.PARAMETRICLINE) {
				result = DatasetTypeProperties.getString(DatasetTypeProperties.ParametricLine);
			} else if (datasetType == DatasetType.PARAMETRICREGION) {
				result = DatasetTypeProperties.getString(DatasetTypeProperties.ParametricRegion);
			} else if (datasetType == DatasetType.POINT) {
				result = DatasetTypeProperties.getString(DatasetTypeProperties.Point);
			} else if (datasetType == DatasetType.POINT3D) {
				result = DatasetTypeProperties.getString(DatasetTypeProperties.Point3D);
			} else if (datasetType == DatasetType.REGION) {
				result = DatasetTypeProperties.getString(DatasetTypeProperties.Region);
			} else if (datasetType == DatasetType.REGION3D) {
				result = DatasetTypeProperties.getString(DatasetTypeProperties.Region3D);
			} else if (datasetType == DatasetType.TABULAR) {
				result = DatasetTypeProperties.getString(DatasetTypeProperties.Tabular);
			} else if (datasetType == DatasetType.TEXT) {
				result = DatasetTypeProperties.getString(DatasetTypeProperties.Text);
			} else if (datasetType == DatasetType.TEXTURE) {
				result = DatasetTypeProperties.getString(DatasetTypeProperties.Texture);
			} else if (datasetType == DatasetType.TOPOLOGY) {
				result = DatasetTypeProperties.getString(DatasetTypeProperties.Topology);
			} else if (datasetType == DatasetType.WCS) {
				result = DatasetTypeProperties.getString(DatasetTypeProperties.WCS);
			} else if (datasetType == DatasetType.WMS) {
				result = DatasetTypeProperties.getString(DatasetTypeProperties.WMS);
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return result;
	}

	public static DatasetType valueOf(String text) {
		DatasetType result = DatasetType.POINT;

		try {
			if (text.equalsIgnoreCase(DatasetTypeProperties.getString(DatasetTypeProperties.CAD))) {
				result = DatasetType.CAD;
			} else if (text.equalsIgnoreCase(DatasetTypeProperties.getString(DatasetTypeProperties.Grid))) {
				result = DatasetType.GRID;
			} else if (text.equalsIgnoreCase(DatasetTypeProperties.getString(DatasetTypeProperties.GridCollection))) {
				result = DatasetType.GRIDCOLLECTION;
			} else if (text.equalsIgnoreCase(DatasetTypeProperties.getString(DatasetTypeProperties.Image))) {
				result = DatasetType.IMAGE;
			} else if (text.equalsIgnoreCase(DatasetTypeProperties.getString(DatasetTypeProperties.ImageCollection))) {
				result = DatasetType.IMAGECOLLECTION;
			} else if (text.equalsIgnoreCase(DatasetTypeProperties.getString(DatasetTypeProperties.Line))) {
				result = DatasetType.LINE;
			} else if (text.equalsIgnoreCase(DatasetTypeProperties.getString(DatasetTypeProperties.Line3D))) {
				result = DatasetType.LINE3D;
			} else if (text.equalsIgnoreCase(DatasetTypeProperties.getString(DatasetTypeProperties.LineM))) {
				result = DatasetType.LINEM;
			} else if (text.equalsIgnoreCase(DatasetTypeProperties.getString(DatasetTypeProperties.LinkTable))) {
				result = DatasetType.LINKTABLE;
			} else if (text.equalsIgnoreCase(DatasetTypeProperties.getString(DatasetTypeProperties.Model))) {
				result = DatasetType.MODEL;
			} else if (text.equalsIgnoreCase(DatasetTypeProperties.getString(DatasetTypeProperties.Network))) {
				result = DatasetType.NETWORK;
			} else if (text.equalsIgnoreCase(DatasetTypeProperties.getString(DatasetTypeProperties.Network3D))) {
				result = DatasetType.NETWORK3D;
			} else if (text.equalsIgnoreCase(DatasetTypeProperties.getString(DatasetTypeProperties.ParametricLine))) {
				result = DatasetType.PARAMETRICLINE;
			} else if (text.equalsIgnoreCase(DatasetTypeProperties.getString(DatasetTypeProperties.ParametricRegion))) {
				result = DatasetType.PARAMETRICREGION;
			} else if (text.equalsIgnoreCase(DatasetTypeProperties.getString(DatasetTypeProperties.Point))) {
				result = DatasetType.POINT;
			} else if (text.equalsIgnoreCase(DatasetTypeProperties.getString(DatasetTypeProperties.Point3D))) {
				result = DatasetType.POINT3D;
			} else if (text.equalsIgnoreCase(DatasetTypeProperties.getString(DatasetTypeProperties.Region))) {
				result = DatasetType.REGION;
			} else if (text.equalsIgnoreCase(DatasetTypeProperties.getString(DatasetTypeProperties.Region3D))) {
				result = DatasetType.REGION3D;
			} else if (text.equalsIgnoreCase(DatasetTypeProperties.getString(DatasetTypeProperties.Tabular))) {
				result = DatasetType.TABULAR;
			} else if (text.equalsIgnoreCase(DatasetTypeProperties.getString(DatasetTypeProperties.Text))) {
				result = DatasetType.TEXT;
			} else if (text.equalsIgnoreCase(DatasetTypeProperties.getString(DatasetTypeProperties.Texture))) {
				result = DatasetType.TEXTURE;
			} else if (text.equalsIgnoreCase(DatasetTypeProperties.getString(DatasetTypeProperties.Topology))) {
				result = DatasetType.TOPOLOGY;
			} else if (text.equalsIgnoreCase(DatasetTypeProperties.getString(DatasetTypeProperties.WCS))) {
				result = DatasetType.WCS;
			} else if (text.equalsIgnoreCase(DatasetTypeProperties.getString(DatasetTypeProperties.WMS))) {
				result = DatasetType.WMS;
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return result;
	}
}
