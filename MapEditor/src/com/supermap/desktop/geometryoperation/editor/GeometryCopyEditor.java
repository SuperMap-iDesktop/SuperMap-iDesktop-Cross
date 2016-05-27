package com.supermap.desktop.geometryoperation.editor;

import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

import com.supermap.desktop.geometryoperation.EditControllerAdapter;
import com.supermap.desktop.geometryoperation.EditEnvironment;
import com.supermap.desktop.geometryoperation.IEditController;
import com.supermap.desktop.geometryoperation.IEditModel;
import com.supermap.desktop.geometryoperation.NullEditController;
import com.supermap.desktop.geometryoperation.control.MapControlTip;

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
		try {
			if (environment.getEditModel() instanceof GeometryCopyEditModel) {

			}
		} finally {

		}
		environment.setEditModel(null);
		environment.setEditController(NullEditController.instance());
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

		public MapControlTip tip;

		public GeometryCopyEditModel() {
			this.tip = new MapControlTip();
		}
	}
}
