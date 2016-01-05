package com.supermap.desktop.controls;

import java.util.ResourceBundle;

import com.supermap.desktop.properties.Properties;

public class ControlsProperties extends Properties {
	public static final String CONTROLS = "resources.Controls";

	public static final String getString(String key) {
		return getString(CONTROLS, key);
	}

	public static final String getString(String baseName, String key) {
		String result = "";

		ResourceBundle resourceBundle = ResourceBundle.getBundle(baseName, getLocale());
		if (resourceBundle != null) {
			result = resourceBundle.getString(key);
		}
		return result;
	}

	// ----------------------------------------------------------------------------------------
	public static final String BundleName = "ui_resources";
	public static final String DatasourcesNodeName = "DatasourcesNodeName";
	public static final String MapsNodeName = "MapsNodeName";
	public static final String ScenesNodeName = "ScenesNodeName";
	public static final String LayoutsNodeName = "LayoutsNodeName";
	public static final String ResourcesNodeName = "ResourcesNodeName";
	public static final String SymbolMarkerLibNodeName = "SymbolMarkerLibNodeName";
	public static final String SymbolLineLibNodeName = "SymbolLineLibNodeName";
	public static final String SymbolFillLibNodeName = "SymbolFillLibNodeName";
	public static final String WorkspaceNodeDefaultName = "WorkspaceNodeDefaultName";
	public static final String TerrainLayersNodeName = "TerrainLayersNodeName";
	public static final String Layer3DsNodeName = "Layer3DsNodeName";
	public static final String ScreenLayerNodeName = "ScreenLayerNodeName";
	public static final String UnsupportOperate = "UnsupportOperate";
	public static final String Label_Left = "String_LabelLeft";
	public static final String Label_Top = "String_LabelTop";
	public static final String Label_Right = "String_LabelRight";
	public static final String Label_Bottom = "String_LabelBottom";
}
