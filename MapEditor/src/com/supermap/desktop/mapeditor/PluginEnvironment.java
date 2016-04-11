package com.supermap.desktop.mapeditor;

import com.supermap.desktop.geometryoperation.EditManager;

public class PluginEnvironment {

	private static EditManager geometryEditManager;

	public static EditManager getGeometryEditManager() {
		if (geometryEditManager == null) {
			geometryEditManager = new EditManager();
		}
		return geometryEditManager;
	}
}
