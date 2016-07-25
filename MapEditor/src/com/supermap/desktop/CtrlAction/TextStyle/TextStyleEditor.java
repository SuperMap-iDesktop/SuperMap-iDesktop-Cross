package com.supermap.desktop.CtrlAction.TextStyle;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.EventObject;

import javax.swing.*;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.event.*;
import com.supermap.desktop.geometryoperation.*;
import com.supermap.desktop.geometryoperation.editor.AbstractEditor;
import com.supermap.desktop.utilities.*;
import com.supermap.mapping.*;
import com.supermap.ui.*;

public class TextStyleEditor extends AbstractEditor {

	private TextStyleDialog dialog;
	private IEditController editController = new EditControllerAdapter() {
		@Override
		public void geometrySelectChanged(EditEnvironment environment, GeometrySelectChangedEvent arg0) {
			resetRecordset(environment.getMap());
		}

		@Override
		public void undone(EditEnvironment environment, EventObject arg0) {
			resetRecordset(environment.getMap());
		}
	};
	private MouseAdapter layerMouseListener;
	private GeometryDeletedListener geometryDeletedListener;
	private GeometryAddedListener geometryAddedListener;
	private GeometryModifiedListener geometryModifiedListener;

	@Override
	public boolean enble(EditEnvironment environment) {
		boolean editable = isEditable(environment.getMap());
		if (null != dialog) {
			dialog.enabled(editable);
		}
		return editable && environment.getEditProperties().getSelectedGeometryCount() >= 1
				&& ListUtilities.isListContainAny(environment.getEditProperties().getSelectedGeometryTypes(), GeometryType.GEOTEXT, GeometryType.GEOTEXT3D);

	}

	private boolean isEditable(Map map) {
		try {
			ArrayList<Layer> layers = MapUtilities.getLayers(map);
			for (Layer layer : layers) {
				if (layer.getDataset() == getActiveRecordset(map).getDataset() && layer.isEditable()) {
					return true;
				}
			}
		} catch (Exception ignore) {
			// 地图dispose没接口判断
		}
		return false;
	}

	@Override
	public void activate(final EditEnvironment environment) {
		if (ListUtilities.isListContainAny(environment.getEditProperties().getSelectedGeometryTypes(), GeometryType.GEOTEXT, GeometryType.GEOTEXT3D)) {
			environment.setEditController(this.editController);
			dialog = TextStyleDialog.createInstance(environment);
			if (null != getActiveRecordset(environment.getMap())) {
				dialog.showDialog(getActiveRecordset(environment.getMap()));
			}
		}
		this.layerMouseListener = new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (null != dialog) {
					dialog.enabled(isEditable(environment.getMap()));
				}
			}
		};
		this.geometryAddedListener = new GeometryAddedListener() {

			@Override
			public void geometryAdded(GeometryEvent arg0) {
				resetRecordset(environment.getMap());
			}
		};
		this.geometryDeletedListener = new GeometryDeletedListener() {

			@Override
			public void geometryDeleted(GeometryEvent arg0) {
				resetRecordset(environment.getMap());
			}
		};
		this.geometryModifiedListener = new GeometryModifiedListener() {

			@Override
			public void geometryModified(GeometryEvent arg0) {
				resetRecordset(environment.getMap());
			}
		};

		registEvents(environment);
	}

	private void removeEvents(EditEnvironment environment) {
		if (null != environment.getMapControl()) {
			environment.getMapControl().removeGeometryAddedListener(geometryAddedListener);
			environment.getMapControl().removeGeometryDeletedListener(geometryDeletedListener);
			environment.getMapControl().removeGeometryModifiedListener(geometryModifiedListener);
		}
	}

	private void registEvents(EditEnvironment environment) {
		removeEvents(environment);
		environment.getMapControl().addGeometryAddedListener(geometryAddedListener);
		environment.getMapControl().addGeometryDeletedListener(geometryDeletedListener);
		environment.getMapControl().addGeometryModifiedListener(geometryModifiedListener);
		Application.getActiveApplication().getMainFrame().getFormManager().addActiveFormChangedListener(new ActiveFormChangedListener() {

			@Override
			public void activeFormChanged(final ActiveFormChangedEvent e) {
				if (e.getNewActiveForm() instanceof IFormMap) {
					resetRecordset(((IFormMap) e.getNewActiveForm()).getMapControl().getMap());
					((IFormMap) e.getNewActiveForm()).getMapControl().addGeometrySelectChangedListener(new GeometrySelectChangedListener() {

						@Override
						public void geometrySelectChanged(GeometrySelectChangedEvent arg0) {
							resetRecordset(((IFormMap) e.getNewActiveForm()).getMapControl().getMap());
							dialog.enabled(isEditable(((IFormMap) e.getNewActiveForm()).getMapControl().getMap()));
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

	private void resetRecordset(Map map) {
		if (null != dialog && null != getActiveRecordset(map)) {
			dialog.showDialog(getActiveRecordset(map));
		} else if (null == getActiveRecordset(map)) {
			removeDialog();
		}
	}


	@Override
	public void deactivate(EditEnvironment environment) {
		removeEvents(environment);
	}

	private Recordset getActiveRecordset(Map map) {
		Recordset recordset = null;
		if (map.findSelection(true).length > 0) {
			Selection selection = MapUtilities.getActiveMap().findSelection(true)[0];
			recordset = selection.toRecordset();
		}
		return recordset;
	}
}
