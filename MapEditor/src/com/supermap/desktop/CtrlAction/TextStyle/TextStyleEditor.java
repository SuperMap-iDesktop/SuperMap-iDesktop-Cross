package com.supermap.desktop.CtrlAction.TextStyle;

import java.awt.event.*;
import java.util.EventObject;

import javax.swing.*;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.event.*;
import com.supermap.desktop.geometryoperation.*;
import com.supermap.desktop.geometryoperation.editor.AbstractEditor;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.LayersTree;
import com.supermap.desktop.ui.controls.TextStyleDialog;
import com.supermap.desktop.utilities.*;
import com.supermap.mapping.*;
import com.supermap.ui.*;

public class TextStyleEditor extends AbstractEditor {

	private TextStyleDialog dialog;
	private IEditController editController = new EditControllerAdapter() {
		@Override
		public void geometrySelectChanged(EditEnvironment environment, GeometrySelectChangedEvent arg0) {
			resetRecordset(environment);
		}

		@Override
		public void undone(EditEnvironment environment, EventObject arg0) {
			resetRecordset(environment);
		}
	};
	private MouseAdapter layerMouseListener;
	private GeometryDeletedListener geometryDeletedListener;
	private GeometryAddedListener geometryAddedListener;
	private GeometryModifiedListener geometryModifiedListener;
	private LayersTree layersTree;

	@Override
	public boolean enble(EditEnvironment environment) {
		boolean isEditable = false;
		isEditable = isLayerEditabled(isEditable);

		return isEditable && environment.getEditProperties().getSelectedGeometryCount() >= 1
				&& ListUtilities.isListContainAny(environment.getEditProperties().getSelectedGeometryTypes(), GeometryType.GEOTEXT, GeometryType.GEOTEXT3D);

	}

	private boolean isLayerEditabled(boolean isEditable) {
		if (Application.getActiveApplication().getActiveForm() instanceof IFormMap
				&& ((IFormMap) Application.getActiveApplication().getActiveForm()).getActiveLayers().length > 0) {
			Layer activeLayer = ((IFormMap) Application.getActiveApplication().getActiveForm()).getActiveLayers()[0];

			if (activeLayer != null && !activeLayer.isDisposed() && activeLayer.isEditable()) {
				isEditable = true;
			}
		}
		return isEditable;
	}

	@Override
	public void activate(final EditEnvironment environment) {
		this.layersTree = UICommonToolkit.getLayersManager().getLayersTree();
		if (ListUtilities.isListContainAny(environment.getEditProperties().getSelectedGeometryTypes(), GeometryType.GEOTEXT, GeometryType.GEOTEXT3D)) {
			environment.setEditController(this.editController);
			dialog = TextStyleDialog.createInstance();
			if (null != getActiveRecordset(environment)) {
				dialog.showDialog(getActiveRecordset(environment));
			}
		}
		this.layerMouseListener = new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (null != dialog) {
					dialog.enabled(isLayerEditabled(false));
				}
			}
		};
		this.geometryAddedListener = new GeometryAddedListener() {

			@Override
			public void geometryAdded(GeometryEvent arg0) {
				resetRecordset(environment);
			}
		};
		this.geometryDeletedListener = new GeometryDeletedListener() {

			@Override
			public void geometryDeleted(GeometryEvent arg0) {
				resetRecordset(environment);
			}
		};
		this.geometryModifiedListener = new GeometryModifiedListener() {

			@Override
			public void geometryModified(GeometryEvent arg0) {
				resetRecordset(environment);
			}
		};

		registEvents(environment);
	}

	private void removeEvents(EditEnvironment environment) {
		layersTree.removeMouseListener(layerMouseListener);
		environment.getMapControl().removeGeometryAddedListener(geometryAddedListener);
		environment.getMapControl().removeGeometryDeletedListener(geometryDeletedListener);
		environment.getMapControl().removeGeometryModifiedListener(geometryModifiedListener);
	}

	private void registEvents(EditEnvironment environment) {
		removeEvents(environment);
		this.layersTree.addMouseListener(layerMouseListener);
		environment.getMapControl().addGeometryAddedListener(geometryAddedListener);
		environment.getMapControl().addGeometryDeletedListener(geometryDeletedListener);
		environment.getMapControl().addGeometryModifiedListener(geometryModifiedListener);
		Application.getActiveApplication().getMainFrame().getFormManager().addActiveFormChangedListener(new ActiveFormChangedListener() {

			@Override
			public void activeFormChanged(final ActiveFormChangedEvent e) {
				if (e.getNewActiveForm() instanceof IFormMap) {
					resetRecordset((IFormMap) e.getNewActiveForm());
					((IFormMap) e.getNewActiveForm()).getMapControl().addGeometrySelectChangedListener(new GeometrySelectChangedListener() {

						@Override
						public void geometrySelectChanged(GeometrySelectChangedEvent arg0) {
							resetRecordset((IFormMap) e.getNewActiveForm());
							dialog.enabled(isLayerEditabled(false));
						}
					});
				} else {
					removeDialog();
				}
				if (null == e.getNewActiveForm()) {
					// 销毁
					if (null != dialog) {
						dialog.disposeInfo();
					}
				}
			}
		});
	}

	private void removeDialog() {
		if (null != dialog) {
			TextStyle textStyle = dialog.getTempTextStyle();
			textStyle = null;
			((JPanel) dialog.getContentPane()).removeAll();
			((JPanel) dialog.getContentPane()).updateUI();
		}
	}

	private void resetRecordset(IFormMap formMap) {
		if (null != dialog && null != getActiveRecordset(formMap)) {
			dialog.showDialog(getActiveRecordset(formMap));
		} else if (null == getActiveRecordset(formMap)) {
			removeDialog();
		}
	}

	private void resetRecordset(EditEnvironment environment) {
		if (null != dialog && null != getActiveRecordset(environment)) {
			dialog.showDialog(getActiveRecordset(environment));
		} else if (null == getActiveRecordset(environment)) {
			removeDialog();
		}
	}

	@Override
	public void deactivate(EditEnvironment environment) {
		removeEvents(environment);
	}

	private Recordset getActiveRecordset(IFormMap formMap) {
		Recordset recordset = null;
		if (formMap.getMapControl().getMap().findSelection(true).length > 0) {
			Selection selection = MapUtilities.getActiveMap().findSelection(true)[0];
			recordset = selection.toRecordset();
		}
		return recordset;
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
