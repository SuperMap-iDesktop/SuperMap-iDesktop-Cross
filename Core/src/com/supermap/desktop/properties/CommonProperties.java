package com.supermap.desktop.properties;

import java.util.ResourceBundle;

public class CommonProperties extends Properties {
	public static final String COMMON = "resources.Common";

	public static final String getString(String key) {
		return getString(COMMON, key);
	}

	public static final String getString(String baseName, String key) {
		String result = "";

		ResourceBundle resourceBundle = ResourceBundle.getBundle(baseName, getLocale());
		if (resourceBundle != null) {
			result = resourceBundle.getString(key);
		}
		return result;
	}

	public static final String Reset = "String_Button_Reset";
	public static final String Apply = "String_Button_Apply";
	public static final String Cancel = "String_Button_Cancel";
	public static final String BeingCanceled = "String_Button_BeingCanceled";
	public static final String Close = "String_Button_Close";
	public static final String OK = "String_Button_OK";
	public static final String Index = "String_Index";
	public static final String Name = "String_Name";
	public static final String PixelFormat = "String_PixelFormat";
	public static final String Button_Setting = "String_Button_Setting";
	public static final String NoValue = "String_Label_NoValue";
	public static final String FieldName = "String_FieldName";
	public static final String Caption = "String_Field_Caption";
	public static final String FieldType = "String_FieldType";
	public static final String FieldValue = "String_FieldValue";
	public static final String Length = "String_Length";
	public static final String Add = "String_Add";
	public static final String Delete = "String_Delete";
	public static final String Modify = "String_Modify";
	public static final String True = "String_True";
	public static final String False = "String_False";
	public static final String NULL = "String_NULL";
	public static final String Label_Datasource = "String_Label_Datasource";
	public static final String Label_Dataset = "String_Label_Dataset";
	public static final String Type = "String_Type";
	public static final String Unknown = "String_Unknown";
	public static final String Total = "String_SumTotal";
	public static final String DatasetGrid = "String_DatasetType_Grid";
	public static final String DatasetVector = "String_DatasetVector";
	public static final String Extremum = "String_Extremum";
	public static final String DefaultValue = "String_DefaultValue";
	public static final String IsRequired = "String_IsRequired";
	public static final String Next = "String_Button_Next";
	public static final String CloseDialog = "String_CheckBox_CloseDialog";
	public static final String Create = "String_Create";




}
