package com.supermap.desktop.geometryoperation.editor;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import com.supermap.data.CursorType;
import com.supermap.data.DatasetVector;
import com.supermap.data.EditType;
import com.supermap.data.FieldInfos;
import com.supermap.data.GeoLine;
import com.supermap.data.Geometry;
import com.supermap.data.GeometryType;
import com.supermap.data.Point2D;
import com.supermap.data.Recordset;
import com.supermap.desktop.Application;
import com.supermap.desktop.geometry.Abstract.ICompoundFeature;
import com.supermap.desktop.geometry.Abstract.ILine3DFeature;
import com.supermap.desktop.geometry.Abstract.ILineFeature;
import com.supermap.desktop.geometry.Abstract.IPoint3DFeature;
import com.supermap.desktop.geometry.Abstract.IPointFeature;
import com.supermap.desktop.geometry.Abstract.IRegion3DFeature;
import com.supermap.desktop.geometry.Abstract.IRegionFeature;
import com.supermap.desktop.geometryoperation.EditControllerAdapter;
import com.supermap.desktop.geometryoperation.EditEnvironment;
import com.supermap.desktop.geometryoperation.IEditController;
import com.supermap.desktop.geometryoperation.IEditModel;
import com.supermap.desktop.geometryoperation.NullEditController;
import com.supermap.desktop.geometryoperation.control.MapControlTip;
import com.supermap.desktop.mapeditor.MapEditorProperties;
import com.supermap.desktop.utilties.ListUtilties;
import com.supermap.desktop.utilties.MapUtilties;
import com.supermap.desktop.utilties.TabularUtilties;
import com.supermap.mapping.Layer;
import com.supermap.ui.Action;
import com.supermap.ui.TrackMode;
import com.supermap.ui.TrackingEvent;

public class MirrorEditor extends AbstractEditor {

	private static final String TAG_MIRROR = "tag_mirror_geometry";
	private static final Action MAP_CONTROL_ACTION = Action.CREATELINE;
	private static final TrackMode MAP_CONTROL_TRACKMODE = TrackMode.TRACK;

	private IEditController mirrorEditController = new EditControllerAdapter() {

		@Override
		public void tracking(EditEnvironment environment, TrackingEvent e) {
			mapControlTracking(environment, e);
		}

		@Override
		public void mousePressed(EditEnvironment environment, MouseEvent e) {
			MirrorEditModel editModel = (MirrorEditModel) environment.getEditModel();

			if (!editModel.isTracking && e.getButton() == MouseEvent.BUTTON1) {
				editModel.isTracking = true;
				editModel.setTipMessage(MapEditorProperties.getString("String_LeftClickToEnd"));
			} else if (editModel.isTracking && e.getButton() == MouseEvent.BUTTON1) {
				mirror(environment);
				environment.stopEditor();
			}
		}

		@Override
		public void mouseClicked(EditEnvironment environment, MouseEvent e) {
			if (SwingUtilities.isRightMouseButton(e)) {
				environment.stopEditor();
			}
		}
	};

	@Override
	public void activate(EditEnvironment environment) {
		MirrorEditModel editModel;
		if (environment.getEditModel() instanceof MirrorEditModel) {
			editModel = (MirrorEditModel) environment.getEditModel();
		} else {
			editModel = new MirrorEditModel();
			environment.setEditModel(editModel);
		}
		environment.setEditController(this.mirrorEditController);

		editModel.oldMapControlAction = environment.getMapControl().getAction();
		editModel.oldTrackMode = environment.getMapControl().getTrackMode();
		environment.getMapControl().setAction(MAP_CONTROL_ACTION);
		environment.getMapControl().setTrackMode(MAP_CONTROL_TRACKMODE);
		editModel.tip.bind(environment.getMapControl());
	}

	@Override
	public void deactivate(EditEnvironment environment) {
		if (environment.getEditModel() instanceof MirrorEditModel) {
			MirrorEditModel editModel = (MirrorEditModel) environment.getEditModel();

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
				&& ListUtilties.isListContainAny(environment.getEditProperties().getEditableSelectedGeometryTypeFeatures(), IPointFeature.class,
						ILineFeature.class, IRegionFeature.class, ICompoundFeature.class, IPoint3DFeature.class, ILine3DFeature.class, IRegion3DFeature.class);
	}

	@Override
	public boolean check(EditEnvironment environment) {
		return environment.getEditor() instanceof MirrorEditor;
	}

	private void mapControlTracking(EditEnvironment environment, TrackingEvent e) {
		if (!(environment.getEditModel() instanceof MirrorEditModel)) {
			return;
		}
		MirrorEditModel editModel = (MirrorEditModel) environment.getEditModel();

		try {
			MapUtilties.clearTrackingObjects(environment.getMap(), TAG_MIRROR);
			GeoLine geoLine = (GeoLine) e.getGeometry();
			// 判断一下，当前绘制的线长度为0时，不执行镜像
			editModel.point1 = geoLine.getPart(0).getItem(0);
			editModel.point2 = geoLine.getPart(0).getItem(1);

			if (editModel.point1.equals(editModel.point2)) {
				return;
			}

			List<Layer> layers = MapUtilties.getLayers(environment.getMap());
			for (Layer layer : layers) {
				if (layer.isEditable() && (layer.getDataset() instanceof DatasetVector) && layer.getSelection().getCount() > 0) {
					Recordset recordset = null;
					try {
						recordset = layer.getSelection().toRecordset();
						while (!recordset.isEOF()) {
							Geometry g = recordset.getGeometry();
							if (g.getType() != GeometryType.GEOTEXT) {
								Geometry newGeometry = g.mirror(editModel.point1, editModel.point2);
								environment.getMap().getTrackingLayer().add(newGeometry, TAG_MIRROR);
								environment.getMap().refreshTrackingLayer();
								newGeometry.dispose();
							}
							g.dispose();
							recordset.moveNext();
						}
					} finally {
						if (recordset != null) {
							recordset.close();
							recordset.dispose();
						}
					}
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private void mirror(EditEnvironment environment) {
		MirrorEditModel editModel = (MirrorEditModel) environment.getEditModel();

		try {
			environment.getMapControl().getEditHistory().batchBegin();
			editModel.setTipMessage(MapEditorProperties.getString("String_GeometryOperation_MirrorInfo"));
			MapUtilties.clearTrackingObjects(environment.getMap(), TAG_MIRROR);

			if (editModel.point1 != Point2D.getEMPTY() && editModel.point2 != Point2D.getEMPTY() && !editModel.point1.equals(editModel.point2)) {
				List<Layer> layers = MapUtilties.getLayers(environment.getMap());

				for (Layer layer : layers) {
					if (layer.isEditable() && (layer.getDataset() instanceof DatasetVector) && layer.getSelection().getCount() > 0) {
						Recordset recordset = null;
						Recordset newRecordset = null;

						try {
							recordset = layer.getSelection().toRecordset();
							newRecordset = ((DatasetVector) layer.getDataset()).getRecordset(false, CursorType.DYNAMIC);

							while (!recordset.isEOF()) {
								Geometry geometry = recordset.getGeometry();
								if (geometry.getType() != GeometryType.GEOTEXT) {
									Geometry newGeometry = geometry.mirror(editModel.point1, editModel.point2);
									Map<String, Object> values = new HashMap<String, Object>();
									FieldInfos fieldInfos = recordset.getFieldInfos();
									Object[] fieldValues = recordset.getValues();

									for (int i = 0; i < fieldValues.length; i++) {
										if (!fieldInfos.get(i).isSystemField()) {
											values.put(fieldInfos.get(i).getName(), fieldValues[i]);
										}
									}

									newRecordset.addNew(newGeometry, values);
									newRecordset.update();
									environment.getMapControl().getEditHistory().add(EditType.ADDNEW, newRecordset, true);
									geometry.dispose();
									newGeometry.dispose();
								}
								recordset.moveNext();
							}
							TabularUtilties.refreshTabularForm(newRecordset.getDataset());
						} finally {
							if (recordset != null) {
								recordset.close();
								recordset.dispose();
							}

							if (newRecordset != null) {
								newRecordset.close();
								newRecordset.dispose();
							}
						}
					}
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		} finally {
			environment.getMapControl().getEditHistory().batchEnd();
			environment.getMap().refresh();
		}
	}

	/**
	 * 编辑结束清理资源
	 */
	private void clear(EditEnvironment environment) {
		if (!(environment.getEditModel() instanceof MirrorEditModel)) {
			return;
		}

		MirrorEditModel editModel = (MirrorEditModel) environment.getEditModel();
		editModel.clear();

		MapUtilties.clearTrackingObjects(environment.getMap(), TAG_MIRROR);
	}

	private class MirrorEditModel implements IEditModel {

		public Action oldMapControlAction = Action.SELECT2;
		public TrackMode oldTrackMode = TrackMode.EDIT;
		public MapControlTip tip = new MapControlTip();
		private JLabel tipLabel = new JLabel(MapEditorProperties.getString("String_GeometryOperation_MirrorInfo"));
		public boolean isTracking = false;
		public Geometry srcGeometry = null;

		public Point2D point1 = Point2D.getEMPTY();
		public Point2D point2 = Point2D.getEMPTY();

		public MirrorEditModel() {
			this.tip.getContentPanel().setLayout(new BoxLayout(this.tip.getContentPanel(), BoxLayout.Y_AXIS));
			this.tip.getContentPanel().add(this.tipLabel);
			this.tip.getContentPanel().setSize(100, 20);
			this.tip.getContentPanel().setBackground(new Color(255, 255, 255, 150));
		}

		public void setTipMessage(String tipMessage) {
			this.tipLabel.setText(tipMessage);
			this.tipLabel.repaint();
		}

		public void clear() {
			this.oldMapControlAction = Action.SELECT2;
			this.oldTrackMode = TrackMode.EDIT;
			this.isTracking = false;
			this.tipLabel.setText(MapEditorProperties.getString("String_GeometryOperation_MirrorInfo"));
			this.point1 = Point2D.getEMPTY();
			this.point2 = Point2D.getEMPTY();

			if (this.srcGeometry != null) {
				this.srcGeometry.dispose();
				this.srcGeometry = null;
			}
		}
	}
}
