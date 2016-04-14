package com.supermap.desktop.geometryoperation.editor;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import com.supermap.desktop.geometry.Abstract.ICompoundFeature;
import com.supermap.desktop.geometry.Abstract.IRegionFeature;
import com.supermap.desktop.geometryoperation.EditEnvironment;
import com.supermap.desktop.utilties.ListUtilties;
import com.supermap.ui.MapControl;

public class EraseEditor extends AbstractEditor {

	private MouseListener mouseListener = new MouseAdapter() {

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub

		}
	};

	@Override
	public void activate(EditEnvironment environment) {
		MapControl mapControl = environment.getMapControl();
		mapControl.addMouseListener(this.mouseListener);
	}

	@Override
	public void deactivate(EditEnvironment environment) {
		MapControl mapControl = environment.getMapControl();
		mapControl.removeMouseListener(this.mouseListener);
	}

	@Override
	public boolean enble(EditEnvironment environment) {
		return environment.getEditProperties().getEditableSelectedGeometryCount() > 0
				&& ListUtilties.isListContainAny(environment.getEditProperties().getSelectedGeometryFeatures(), IRegionFeature.class, ICompoundFeature.class);
	}

	@Override
	public boolean check(EditEnvironment environment) {
		return environment.getEditor() instanceof EraseEditor;
	}
}
