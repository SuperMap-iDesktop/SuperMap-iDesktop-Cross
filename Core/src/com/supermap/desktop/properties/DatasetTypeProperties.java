package com.supermap.desktop.properties;

import java.util.ResourceBundle;

public class DatasetTypeProperties extends Properties {
	public static final String DATASETTYPE = "resources.DatasetType";


	public static final String getString(String key) {
		return getString(DATASETTYPE, key);
	}

	public static final String getString(String baseName, String key) {
		String result = "";

		ResourceBundle resourceBundle = ResourceBundle.getBundle(baseName, getLocale());
		if (resourceBundle != null) {
			result = resourceBundle.getString(key);
		}
		return result;
	}

	public static final String CAD = "String_DatasetType_CAD";
	public static final String Grid = "String_DatasetType_Grid";
	public static final String GridCollection = "String_DatasetType_GridCollection";
	public static final String Image = "String_DatasetType_Image";
	public static final String ImageCollection = "String_DatasetType_ImageCollection";
	public static final String Line = "String_DatasetType_Line";
	public static final String Line3D = "String_DatasetType_Line3D";
	public static final String LinkTable = "String_DatasetType_LinkTable";
	public static final String Network = "String_DatasetType_Network";
	public static final String Network3D = "String_DatasetType_Network3D";
	public static final String ParametricLine = "String_DatasetType_ParametricLine";
	public static final String ParametricRegion = "String_DatasetType_ParametricRegion";
	public static final String Point = "String_DatasetType_Point";
	public static final String Point3D = "String_DatasetType_Point3D";
	public static final String Region = "String_DatasetType_Region";
	public static final String Region3D = "String_DatasetType_Region3D";
	public static final String LineM = "String_DatasetType_LineM";
	public static final String Tabular = "String_DatasetType_Tabular";
	public static final String Template = "String_DatasetType_Template";
	public static final String Text = "String_DatasetType_Text";
	public static final String Topology = "String_DatasetType_Topology";
	public static final String Unknown = "String_DatasetType_Unknown";
	public static final String WCS = "String_DatasetType_WCS";
	public static final String WMS = "String_DatasetType_WMS";
	public static final String Model = "String_DatasetType_Model";
	public static final String Texture = "String_DatasetType_Texture";
	public static final String All = "String_DatasetType_All";
	public static final String VOLUME = "String_DatasetType_Volume";
}
