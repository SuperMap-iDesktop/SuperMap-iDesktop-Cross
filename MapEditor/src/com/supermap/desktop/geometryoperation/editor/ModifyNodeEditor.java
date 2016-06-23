package com.supermap.desktop.geometryoperation.editor;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

import com.supermap.data.GeometryType;
import com.supermap.desktop.geometryoperation.EditControllerAdapter;
import com.supermap.desktop.geometryoperation.EditEnvironment;
import com.supermap.desktop.geometryoperation.IEditController;
import com.supermap.desktop.geometryoperation.NullEditController;
import com.supermap.desktop.utilities.ListUtilities;
import com.supermap.ui.Action;

public class ModifyNodeEditor extends AbstractEditor {

	private IEditController modifyNodeEditController = new EditControllerAdapter() {

		@Override
		public void mouseClicked(EditEnvironment environment, MouseEvent e) {
			if (SwingUtilities.isRightMouseButton(e)) {
				environment.stopEditor();
			}
		}
	};

	@Override
	public void activate(EditEnvironment environment) {
		environment.getMapControl().setAction(Action.VERTEXEDIT);
		environment.setEditController(this.modifyNodeEditController);
		environment.getMap().refresh();
	}

	@Override
	public void deactivate(EditEnvironment environment) {
		environment.getMapControl().setAction(Action.SELECT2);
		environment.setEditController(NullEditController.instance());
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
