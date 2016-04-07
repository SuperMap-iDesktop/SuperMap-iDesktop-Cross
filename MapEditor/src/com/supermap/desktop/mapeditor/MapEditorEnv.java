package com.supermap.desktop.mapeditor;

import com.supermap.desktop.geometryoperation.EditState;

public class MapEditorEnv {

	private static EditState editState;

	public static EditState getEditState() {
		if (editState == null) {
			editState = new EditState(null);
		}
		return editState;
	}
}
