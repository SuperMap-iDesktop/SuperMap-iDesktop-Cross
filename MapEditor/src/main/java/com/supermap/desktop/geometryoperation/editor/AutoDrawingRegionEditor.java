package com.supermap.desktop.geometryoperation.editor;

import com.supermap.data.CursorType;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.EditType;
import com.supermap.data.GeoRegion;
import com.supermap.data.Geometrist;
import com.supermap.data.Geometry;
import com.supermap.data.Recordset;
import com.supermap.desktop.Application;
import com.supermap.desktop.geometry.Abstract.IRegionFeature;
import com.supermap.desktop.geometryoperation.EditControllerAdapter;
import com.supermap.desktop.geometryoperation.EditEnvironment;
import com.supermap.desktop.geometryoperation.IEditController;
import com.supermap.desktop.geometryoperation.IEditModel;
import com.supermap.desktop.geometryoperation.NullEditController;
import com.supermap.desktop.geometryoperation.control.MapControlTip;
import com.supermap.desktop.mapeditor.MapEditorProperties;
import com.supermap.desktop.utilities.ListUtilities;
import com.supermap.desktop.utilities.MapUtilities;
import com.supermap.mapping.Layer;
import com.supermap.ui.Action;
import com.supermap.ui.TrackMode;
import com.supermap.ui.TrackedEvent;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * @author lixiaoyao
 */
public class AutoDrawingRegionEditor extends AbstractEditor {

	private static final String TAG_SOURCE = "Tag_AutoDrawingRegion";
	private static final Action MAP_CONTROL_ACTION = Action.CREATEPOLYGON;
	private static final TrackMode TRACK_MODE = TrackMode.TRACK;

	private IEditController autoDrawingRegionEditControler = new EditControllerAdapter() {
		@Override
		public void mouseClicked(EditEnvironment environment, MouseEvent e) {
			if (SwingUtilities.isRightMouseButton(e)) {
				environment.stopEditor();
			}
		}

		@Override
		public void mousePressed(EditEnvironment environment, MouseEvent e) {
			AutoDrawingRegionModel editModel = (AutoDrawingRegionModel) environment.getEditModel();
			if (!editModel.isTracking && e.getButton() == MouseEvent.BUTTON1) {
				editModel.isTracking = true;
				editModel.setTipMessage(MapEditorProperties.getString("String_RightClickToEnd"));
			} else if (editModel.isTracking && e.getButton() == MouseEvent.BUTTON3) {
				runAutoDrawingRegion(environment);
				environment.stopEditor();
			}
		}

		@Override
		public void tracked(EditEnvironment environment, TrackedEvent e) {
			mapControlTracked(environment, e);
		}
	};

	@Override
	public void activate(EditEnvironment environment) {
		AutoDrawingRegionModel editModel;
		if (environment.getEditModel() instanceof AutoDrawingRegionModel) {
			editModel = (AutoDrawingRegionModel) environment.getEditModel();
		} else {
			editModel = new AutoDrawingRegionModel();
			environment.setEditModel(editModel);
		}
		environment.setEditController(this.autoDrawingRegionEditControler);

		editModel.oldMapControlAction = environment.getMapControl().getAction();
		editModel.oldTrackMode = environment.getMapControl().getTrackMode();
		environment.getMapControl().setAction(MAP_CONTROL_ACTION);
		environment.getMapControl().setTrackMode(TRACK_MODE);
		editModel.tip.bind(environment.getMapControl());
	}

	@Override
	public void deactivate(EditEnvironment environment) {
		if (environment.getEditModel() instanceof AutoDrawingRegionModel) {
			AutoDrawingRegionModel editModel = (AutoDrawingRegionModel) environment.getEditModel();

			try {
				environment.getMapControl().setAction(editModel.oldMapControlAction);
				environment.getMapControl().setTrackMode(editModel.oldTrackMode);
				clear(environment);
			} finally {
				editModel.tip.unbind();
				environment.setEditController(NullEditController.instance());
				environment.setEditModel(null);
			}
		}
	}

	@Override
	public boolean enble(EditEnvironment environment) {
		return environment.getEditProperties().getEditableSelectedGeometryCount() > 0
				&& ListUtilities.isListContainAny(environment.getEditProperties().getEditableSelectedGeometryTypeFeatures(),
				IRegionFeature.class);
	}

	@Override
	public boolean check(EditEnvironment environment) {
		return environment.getEditor() instanceof AutoDrawingRegionEditor;
	}

	private void runAutoDrawingRegion(EditEnvironment environment) {
		AutoDrawingRegionModel editModel = (AutoDrawingRegionModel) environment.getEditModel();
		Geometry resultGeometry = editModel.geometry;
		Recordset targetRecordset = null;

		try {
			environment.getMapControl().getEditHistory().batchBegin();
			editModel.setTipMessage(MapEditorProperties.getString("string_GeometryOperation_AutoDrawingRegion"));
			MapUtilities.clearTrackingObjects(environment.getMap(), TAG_SOURCE);
			List<Layer> layers = MapUtilities.getLayers(environment.getMap());

			for (Layer layer : layers) {
				if (layer.isEditable() && (layer.getDataset().getType() == DatasetType.CAD || layer.getDataset().getType() == DatasetType.REGION)
						&& layer.getSelection().getCount() > 0 && editModel.geometry != null) {
					Recordset recordset = null;
					try {
						recordset = layer.getSelection().toRecordset();

						while (!recordset.isEOF()) {
							Geometry geometry = recordset.getGeometry();

							boolean result = false;
							GeoRegion resultGeoRegion1 = new GeoRegion();
							GeoRegion resultGeoRegion2 = new GeoRegion();
							result = Geometrist.splitRegion((GeoRegion) resultGeometry, (GeoRegion) geometry, resultGeoRegion1, resultGeoRegion2);

							if (result) {
								if (Geometrist.hasTouch(resultGeoRegion1, geometry)) {//保留没有重合的面对象
									if (Geometrist.isIdentical(resultGeoRegion1, geometry)) {
										resultGeometry = resultGeoRegion2;
									} else {
										resultGeometry = resultGeoRegion1;
									}
								} else if (Geometrist.hasTouch(resultGeoRegion2, geometry)) {
									if (Geometrist.isIdentical(resultGeoRegion2, geometry)) {
										resultGeometry = resultGeoRegion1;
									} else {
										resultGeometry = resultGeoRegion2;
									}
								}
							}
							recordset.moveNext();
						}

						layer.getSelection().clear();
					} catch (Exception ex) {
						Application.getActiveApplication().getOutput().output(ex);
					} finally {
						if (recordset != null) {
							recordset.close();
							recordset.dispose();
						}
					}
				}
			}

			if (environment.getFormMap().getMapControl().getActiveEditableLayer() != null) {
				targetRecordset = ((DatasetVector) environment.getFormMap().getMapControl().getActiveEditableLayer().getDataset()).getRecordset(false, CursorType.DYNAMIC);
				targetRecordset.addNew(resultGeometry);
				targetRecordset.update();
				environment.getMapControl().getEditHistory().add(EditType.ADDNEW, targetRecordset, true);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		} finally {
			environment.getMapControl().getEditHistory().batchEnd();
			environment.getMapControl().getMap().refresh();

			if (targetRecordset != null) {
				targetRecordset.close();
				targetRecordset.dispose();
			}
		}
	}

	/**
	 * 绘制面结束后，获取所绘制的对象
	 */
	private void mapControlTracked(EditEnvironment environment, TrackedEvent e) {
		if (!(environment.getEditModel() instanceof AutoDrawingRegionModel)) {
			return;
		}
		AutoDrawingRegionModel editModel = (AutoDrawingRegionModel) environment.getEditModel();

		try {
			editModel.geometry = e.getGeometry();
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private void clear(EditEnvironment environment) {
		if (!(environment.getEditModel() instanceof AutoDrawingRegionModel)) {
			return;
		}
		AutoDrawingRegionModel editModel = (AutoDrawingRegionModel) environment.getEditModel();
		editModel.clear();
		MapUtilities.clearTrackingObjects(environment.getMap(), TAG_SOURCE);
	}

	private class AutoDrawingRegionModel implements IEditModel {
		public com.supermap.ui.Action oldMapControlAction = com.supermap.ui.Action.SELECT2;
		public TrackMode oldTrackMode = TrackMode.EDIT;
		public MapControlTip tip = new MapControlTip();
		private JLabel tipLabel = new JLabel(MapEditorProperties.getString("string_GeometryOperation_AutoDrawingRegion"));
		public boolean isTracking = false;
		public Geometry geometry = null;

		public AutoDrawingRegionModel() {
			this.tip.addLabel(this.tipLabel);
		}

		public void setTipMessage(String tipMessage) {
			this.tipLabel.setText(tipMessage);
			this.tipLabel.repaint();
		}

		public void clear() {
			this.oldMapControlAction = com.supermap.ui.Action.SELECT2;
			this.oldTrackMode = TrackMode.EDIT;
			this.isTracking = false;
			this.geometry = null;
			this.tipLabel.setText(MapEditorProperties.getString("string_GeometryOperation_AutoDrawingRegion"));
		}
	}
}
