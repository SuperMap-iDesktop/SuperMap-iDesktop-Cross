package com.supermap.desktop.geometryoperation.editor;

import com.supermap.data.GeometryType;
import com.supermap.desktop.geometryoperation.EditEnvironment;
import com.supermap.desktop.utilties.ListUtilities;
import com.supermap.ui.Action;

public class ModifyNodeEditor extends AbstractEditor {

	@Override
	public void activate(EditEnvironment environment) {
		environment.getMapControl().setAction(Action.VERTEXEDIT);
	}

	@Override
	public void deactivate(EditEnvironment environment) {
		environment.getMapControl().setAction(Action.SELECT2);
	}

	@Override
	public boolean enble(EditEnvironment environment) {
		return environment.getEditProperties().getSelectedGeometryCount() == 1
				&& ListUtilities.isListOnlyContain(environment.getEditProperties().getSelectedGeometryTypes(), GeometryType.GEOLINE, GeometryType.GEOLINE3D,
						GeometryType.GEOREGION);
	}

	@Override
	public boolean check(EditEnvironment environment) {
		return environment.getMapControl().getAction() == Action.VERTEXEDIT;
	}
}
