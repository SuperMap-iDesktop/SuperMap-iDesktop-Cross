package com.supermap.desktop.CtrlAction.TextStyle;

import java.awt.event.*;
import java.util.*;

import javax.swing.*;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.geometryoperation.*;
import com.supermap.desktop.geometryoperation.editor.AbstractEditor;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.TextStyleDialog;
import com.supermap.desktop.utilities.*;
import com.supermap.mapping.*;
import com.supermap.ui.GeometrySelectChangedEvent;

public class TextStyleEditor extends AbstractEditor {

	private TextStyleDialog dialog;
	private IEditController editController = new EditControllerAdapter() {
		@Override
		public void geometrySelectChanged(EditEnvironment environment, GeometrySelectChangedEvent arg0) {
			resetRecordset(environment);
		}
	};

	@Override
	public boolean enble(EditEnvironment environment) {
		boolean isEditable = false;
		if (Application.getActiveApplication().getActiveForm() instanceof IFormMap
				&& ((IFormMap) Application.getActiveApplication().getActiveForm()).getActiveLayers().length > 0) {
			Layer activeLayer = ((IFormMap) Application.getActiveApplication().getActiveForm()).getActiveLayers()[0];

			if (activeLayer != null && !activeLayer.isDisposed() && activeLayer.isEditable()) {
				isEditable = true;
			}
		}

		return isEditable && environment.getEditProperties().getSelectedGeometryCount() >= 1
				&& ListUtilities.isListContainAny(environment.getEditProperties().getSelectedGeometryTypes(), GeometryType.GEOTEXT, GeometryType.GEOTEXT3D);

	}

	@Override
	public void activate(EditEnvironment environment) {
		if (ListUtilities.isListOnlyContain(environment.getEditProperties().getSelectedGeometryTypes(), GeometryType.GEOTEXT)) {
			environment.setEditController(this.editController);
			dialog = TextStyleDialog.createInstance();
			if (null != getActiveRecordset(environment)) {
				dialog.showDialog(getActiveRecordset(environment));
			}
		}
		UICommonToolkit.getLayersManager().getLayersTree().addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				dialog.enabled(((IFormMap) Application.getActiveApplication().getActiveForm()).getActiveLayers()[0].isEditable());
			}
		});
	}
	private void resetRecordset(EditEnvironment environment) {
		if (null != dialog && null != getActiveRecordset(environment)) {
			dialog.showDialog(getActiveRecordset(environment));
		} else if (null == getActiveRecordset(environment)) {
			((JPanel) dialog.getContentPane()).removeAll();
			((JPanel) dialog.getContentPane()).updateUI();
		}
	}
	@Override
	public void deactivate(EditEnvironment environment) {
		resetRecordset(environment);
	}

	private Recordset getActiveRecordset(EditEnvironment environment) {
		Recordset recordset = null;
		if (environment.getMap().findSelection(true).length > 0) {
			Selection selection = MapUtilities.getActiveMap().findSelection(true)[0];
			recordset = selection.toRecordset();
		}
		return recordset;
	}
}
