package com.supermap.desktop.CtrlAction.TextStyle;

import com.supermap.data.GeometryType;
import com.supermap.data.Recordset;
import com.supermap.data.TextStyle;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.event.ActiveFormChangedEvent;
import com.supermap.desktop.event.ActiveFormChangedListener;
import com.supermap.desktop.geometryoperation.EditControllerAdapter;
import com.supermap.desktop.geometryoperation.EditEnvironment;
import com.supermap.desktop.geometryoperation.IEditController;
import com.supermap.desktop.geometryoperation.editor.AbstractEditor;
import com.supermap.desktop.utilities.ListUtilities;
import com.supermap.desktop.utilities.MapUtilities;
import com.supermap.mapping.Layer;
import com.supermap.mapping.Map;
import com.supermap.mapping.Selection;
import com.supermap.ui.GeometryAddedListener;
import com.supermap.ui.GeometryDeletedListener;
import com.supermap.ui.GeometryEvent;
import com.supermap.ui.GeometryModifiedListener;
import com.supermap.ui.GeometrySelectChangedEvent;
import com.supermap.ui.GeometrySelectChangedListener;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.EventObject;

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
				Recordset recordset = getActiveRecordset(map);
				try {
					if (recordset != null && layer.getDataset() == recordset.getDataset() && layer.isEditable()) {
						return true;
					}
				} catch (Exception e) {
					// ignore
				} finally {
					if (recordset != null) {
						recordset.dispose();
					}
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
