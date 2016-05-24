package com.supermap.desktop.CtrlAction.TextStyle;

import com.supermap.data.GeometryType;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.geometryoperation.EditEnvironment;
import com.supermap.desktop.geometryoperation.editor.AbstractEditor;
import com.supermap.desktop.utilties.ListUtilties;
import com.supermap.mapping.Layer;

public class TextStyleEditor extends AbstractEditor {

	@Override
	public boolean enble(EditEnvironment environment) {
		boolean isEditable = false;
		if (Application.getActiveApplication().getActiveForm() instanceof IFormMap) {
			Layer activeLayer = ((IFormMap) Application.getActiveApplication().getActiveForm()).getActiveLayers()[0];
			if (activeLayer.isEditable()) {
				isEditable = true;
			}
		}

		return isEditable && environment.getEditProperties().getSelectedGeometryCount() >= 1
				&& ListUtilties.isListContainAny(environment.getEditProperties().getSelectedGeometryTypes(), GeometryType.GEOTEXT, GeometryType.GEOTEXT3D);

	}
	
}
