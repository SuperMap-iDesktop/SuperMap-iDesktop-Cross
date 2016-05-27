package com.supermap.desktop.geometryoperation.editor;

import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

import com.supermap.desktop.geometryoperation.EditControllerAdapter;
import com.supermap.desktop.geometryoperation.EditEnvironment;
import com.supermap.desktop.geometryoperation.IEditController;
import com.supermap.desktop.geometryoperation.IEditModel;

public class GeometryCopyEditor extends AbstractEditor {

	private IEditController geometryCopyController = new EditControllerAdapter() {
		@Override
		public void mouseClicked(EditEnvironment environment, MouseEvent e) {
			if (SwingUtilities.isRightMouseButton(e)) {
				environment.stopEditor();
			}
		}
	};

	@Override
	public void activate(EditEnvironment environment) {
		GeometryCopyEditModel editModel = null;
		if (environment.getEditModel() instanceof GeometryCopyEditModel) {
			editModel = (GeometryCopyEditModel) environment.getEditModel();
		} else {
			editModel = new GeometryCopyEditModel();
			environment.setEditModel(editModel);
		}
		environment.setEditController(this.geometryCopyController);
	}

	@Override
	public void deactivate(EditEnvironment environment) {
		// 按需重写
	}

	@Override
	public boolean enble(EditEnvironment environment) {
		return false;
	}

	@Override
	public boolean check(EditEnvironment environment) {
		return environment.getEditor() instanceof GeometryCopyEditor;
	}

	private class GeometryCopyEditModel implements IEditModel {

	}
}
