package com.supermap.desktop.geometryoperation.editor;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import com.supermap.data.CursorType;
import com.supermap.data.DatasetVector;
import com.supermap.data.EditType;
import com.supermap.data.GeoCompound;
import com.supermap.data.GeoStyle;
import com.supermap.data.Geometry;
import com.supermap.data.Point2D;
import com.supermap.data.Recordset;
import com.supermap.desktop.Application;
import com.supermap.desktop.geometryoperation.EditControllerAdapter;
import com.supermap.desktop.geometryoperation.EditEnvironment;
import com.supermap.desktop.geometryoperation.IEditController;
import com.supermap.desktop.geometryoperation.IEditModel;
import com.supermap.desktop.geometryoperation.NullEditController;
import com.supermap.desktop.geometryoperation.control.MapControlTip;
import com.supermap.desktop.mapeditor.MapEditorProperties;
import com.supermap.desktop.utilties.ArrayUtilties;
import com.supermap.desktop.utilties.GeoStyleUtilties;
import com.supermap.desktop.utilties.MapUtilties;
import com.supermap.desktop.utilties.RecordsetUtilties;
import com.supermap.desktop.utilties.TabularUtilties;
import com.supermap.mapping.Layer;
import com.supermap.ui.Action;
import com.supermap.ui.TrackMode;

public class GeometryCopyEditor extends AbstractEditor {

	private static final String TAG_GEOMETRYCOPY = "Tag_GeometryCopy";
	private static final Action MAPCONTROL_ACTION = Action.CREATEPOINT;
	private static final TrackMode MAPCONTROL_TRACKMODE = TrackMode.TRACK;

	private IEditController geometryCopyController = new EditControllerAdapter() {

		public void mousePressed(EditEnvironment environment, MouseEvent e) {
			if (SwingUtilities.isLeftMouseButton(e)) {
				GeometryCopyEditModel editModel = (GeometryCopyEditModel) environment.getEditModel();

				if (editModel.basePoint == null || editModel.basePoint.equals(Point2D.getEMPTY())) {
					editModel.basePoint = getMousePointOnMap(environment, e.getPoint());
					editModel.tip.unbind();
				} else {
					geometryCopy(environment, e);
				}
			}
		}

		@Override
		public void mouseClicked(EditEnvironment environment, MouseEvent e) {
			if (SwingUtilities.isRightMouseButton(e)) {
				environment.stopEditor();
			}
		}

		@Override
		public void mouseMoved(EditEnvironment environment, MouseEvent e) {
			mapControl_mouseMoved(environment, e);
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

		editModel.oldAction = environment.getMapControl().getAction();
		editModel.oldTrackMode = environment.getMapControl().getTrackMode();
		environment.getMapControl().setAction(MAPCONTROL_ACTION);
		environment.getMapControl().setTrackMode(MAPCONTROL_TRACKMODE);
		editModel.tip.bind(environment.getMapControl());
		initializeSrc(environment);
	}

	@Override
	public void deactivate(EditEnvironment environment) {
		if (environment.getEditModel() instanceof GeometryCopyEditModel) {
			GeometryCopyEditModel editModel = (GeometryCopyEditModel) environment.getEditModel();

			try {
				environment.getMapControl().setAction(editModel.oldAction);
				environment.getMapControl().setTrackMode(editModel.oldTrackMode);
				clear(environment);
			} finally {
				editModel.tip.unbind();
				environment.setEditModel(null);
				environment.setEditController(NullEditController.instance());
			}
		}
	}

	@Override
	public boolean enble(EditEnvironment environment) {
		return environment.getEditProperties().getEditableSelectedGeometryCount() > 0;
	}

	@Override
	public boolean check(EditEnvironment environment) {
		return environment.getEditor() instanceof GeometryCopyEditor;
	}

	private void initializeSrc(EditEnvironment environment) {
		GeometryCopyEditModel editModel = (GeometryCopyEditModel) environment.getEditModel();
		editModel.trackingGeoCompound = new GeoCompound();
		ArrayList<Layer> layers = MapUtilties.getLayers(environment.getMap());

		for (Layer layer : layers) {
			if (layer.isEditable() && layer.getSelection() != null && layer.getSelection().getCount() > 0) {
				editModel.copyGeometries.put(layer, new ArrayList<Integer>());
				Recordset recordset = layer.getSelection().toRecordset();

				if (recordset == null) {
					continue;
				}

				while (!recordset.isEOF()) {
					Geometry geometry = recordset.getGeometry();

					if (geometry != null) {
						editModel.copyGeometries.get(layer).add(recordset.getID());
						editModel.trackingGeoCompound.addPart(geometry);
						geometry.dispose();
					}
					recordset.moveNext();
				}
				recordset.close();
				recordset.dispose();
			}
		}
	}

	private void mapControl_mouseMoved(EditEnvironment environment, MouseEvent e) {
		GeometryCopyEditModel editModel = (GeometryCopyEditModel) environment.getEditModel();

		try {
			if (editModel.basePoint != null && !editModel.basePoint.equals(Point2D.getEMPTY())) {
				Point2D currentPoint = getMousePointOnMap(environment, e.getPoint());
				Double offsetX = currentPoint.getX() - editModel.basePoint.getX();
				Double offsetY = currentPoint.getY() - editModel.basePoint.getY();

				if (editModel.trackingGeoCompound != null) {
					editModel.trackingGeoCompound.offset(offsetX, offsetY);
					GeoStyleUtilties.setGeometryStyle(editModel.trackingGeoCompound, getTrackingStyle());

					int index = environment.getMap().getTrackingLayer().indexOf(TAG_GEOMETRYCOPY);
					if (index >= 0) {
						environment.getMap().getTrackingLayer().set(index, editModel.trackingGeoCompound);
					} else {
						environment.getMap().getTrackingLayer().add(editModel.trackingGeoCompound, TAG_GEOMETRYCOPY);
					}
					editModel.trackingGeoCompound.offset(-1 * offsetX, -1 * offsetY);
					environment.getMap().refreshTrackingLayer();
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private void geometryCopy(EditEnvironment environment, MouseEvent e) {
		GeometryCopyEditModel editModel = (GeometryCopyEditModel) environment.getEditModel();
		environment.getMapControl().getEditHistory().batchBegin();

		try {
			Point2D mousePointOnMap = getMousePointOnMap(environment, e.getPoint());

			if (editModel.basePoint.equals(Point2D.getEMPTY())) {
				editModel.basePoint = new Point2D(mousePointOnMap.getX(), mousePointOnMap.getY());
			} else if (editModel.copyGeometries != null) {
				double offsetX = mousePointOnMap.getX() - editModel.basePoint.getX();
				double offsetY = mousePointOnMap.getY() - editModel.basePoint.getY();

				for (Layer layer : editModel.copyGeometries.keySet()) {
					List<Integer> selectionIDs = new ArrayList<Integer>();
					List<Integer> selectedDs = editModel.copyGeometries.get(layer);
					Recordset recordset = ((DatasetVector) layer.getDataset()).query(
							ArrayUtilties.convertToInt(selectedDs.toArray(new Integer[selectedDs.size()])), CursorType.DYNAMIC);

					for (Integer id : selectedDs) {
						recordset.seekID(id);
						Geometry geometry = recordset.getGeometry();
						if (geometry != null) {
							geometry.offset(offsetX, offsetY);

							Map<String, Object> values = RecordsetUtilties.getFieldValues(recordset);

							if (recordset.addNew(geometry, values)) {
								recordset.update();
								selectionIDs.add(recordset.getID());
							}
							geometry.dispose();
						}
					}
					recordset.close();
					recordset.dispose();

					if (selectionIDs.size() > 0) {
						int[] toSelected = ArrayUtilties.convertToInt(selectionIDs.toArray(new Integer[selectionIDs.size()]));
						layer.getSelection().clear();
						layer.getSelection().addRange(toSelected);
						Recordset toSelectedRecordset = ((DatasetVector) layer.getDataset()).query(toSelected, CursorType.DYNAMIC);
						environment.getMapControl().getEditHistory().add(EditType.ADDNEW, toSelectedRecordset, false);

						// 刷新一下桌面的属性表窗口
						TabularUtilties.refreshTabularForm(toSelectedRecordset.getDataset());
						toSelectedRecordset.close();
						toSelectedRecordset.dispose();
					}
				}

				environment.getMap().refresh();
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		} finally {
			environment.getMapControl().getEditHistory().batchEnd();
		}
	}

	private GeoStyle getTrackingStyle() {
		GeoStyle trackingStyle = new GeoStyle();
		trackingStyle.setLineSymbolID(2);
		trackingStyle.setFillOpaqueRate(0);
		return trackingStyle;
	}

	/**
	 * 获取当前鼠标在地图上的坐标点，如果有捕捉，返回捕捉点坐标，没有捕捉，返回鼠标点对应的地图坐标
	 * 
	 * @param environment
	 * @param mousePointOnMapControl
	 * @return
	 */
	private Point2D getMousePointOnMap(EditEnvironment environment, Point mousePointOnMapControl) {
		Point2D mousePointOnMap = Point2D.getEMPTY();
		mousePointOnMap = EditorUtilties.getSnapModePoint(environment.getMapControl());

		if (mousePointOnMap == null || mousePointOnMap.equals(Point2D.getEMPTY())) {
			mousePointOnMap = environment.getMap().pixelToMap(mousePointOnMapControl);
		}
		return mousePointOnMap;
	}

	private void clear(EditEnvironment environment) {
		GeometryCopyEditModel editModel = (GeometryCopyEditModel) environment.getEditModel();
		editModel.clear();
		MapUtilties.clearTrackingObjects(environment.getMap(), TAG_GEOMETRYCOPY);
	}

	private class GeometryCopyEditModel implements IEditModel {

		public MapControlTip tip;
		private JLabel labelMsg = new JLabel(MapEditorProperties.getString("String_Tip_Edit_CopyObj"));
		public GeoCompound trackingGeoCompound;
		public Map<Layer, List<Integer>> copyGeometries = new HashMap<>();
		public Point2D basePoint = Point2D.getEMPTY();

		public Action oldAction = Action.SELECT2;
		public TrackMode oldTrackMode = TrackMode.EDIT;

		public GeometryCopyEditModel() {
			this.tip = new MapControlTip();
			this.tip.getContentPanel().setLayout(new BoxLayout(this.tip.getContentPanel(), BoxLayout.Y_AXIS));
			this.tip.getContentPanel().add(this.labelMsg);
			this.tip.getContentPanel().setSize(120, 21);
			this.tip.getContentPanel().setBackground(new Color(255, 255, 255, 150));
		}

		public void setMsg(String msg) {
			this.labelMsg.setText(msg);
			this.labelMsg.repaint();
		}

		public void clear() {
			setMsg(MapEditorProperties.getString("String_Tip_Edit_CopyObj"));
			this.basePoint = Point2D.getEMPTY();
			this.oldAction = Action.SELECT2;
			this.oldTrackMode = TrackMode.EDIT;

			if (this.trackingGeoCompound != null) {
				this.trackingGeoCompound.dispose();
				this.trackingGeoCompound = null;
			}

			if (this.copyGeometries != null) {
				this.copyGeometries.clear();
			}
		}
	}
}
