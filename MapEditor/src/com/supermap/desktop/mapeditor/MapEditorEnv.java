package com.supermap.desktop.mapeditor;

import com.supermap.desktop.geometryoperation.GeometryEditManager;

public class MapEditorEnv {

	private static GeometryEditManager geometryEditManager;

	public static GeometryEditManager getGeometryEditManager() {
		if (geometryEditManager == null) {
			geometryEditManager = new GeometryEditManager();
		}
		return geometryEditManager;
	}
}
