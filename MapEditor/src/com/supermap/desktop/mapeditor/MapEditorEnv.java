package com.supermap.desktop.mapeditor;

import com.supermap.desktop.geometryoperation.GeometryEdit;

public class MapEditorEnv {

	private static GeometryEdit editState;

	public static GeometryEdit getEditState() {
		if (editState == null) {
			editState = new GeometryEdit(null);
		}
		return editState;
	}
}
