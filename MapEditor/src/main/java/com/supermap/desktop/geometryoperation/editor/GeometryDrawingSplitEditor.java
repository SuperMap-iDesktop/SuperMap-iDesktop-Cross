package com.supermap.desktop.geometryoperation.editor;

/**
 * @author lixiaoyao
 */

import com.supermap.data.CursorType;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.GeoStyle;
import com.supermap.data.Geometry;
import com.supermap.data.GeometryType;
import com.supermap.data.Recordset;
import com.supermap.desktop.Application;
import com.supermap.desktop.core.recordset.RecordsetAddNew;
import com.supermap.desktop.core.recordset.RecordsetDelete;
import com.supermap.desktop.geometry.Abstract.IGeometry;
import com.supermap.desktop.geometry.Abstract.ILineFeature;
import com.supermap.desktop.geometry.Abstract.IRegionFeature;
import com.supermap.desktop.geometry.Implements.DGeometryFactory;
import com.supermap.desktop.geometryoperation.EditControllerAdapter;
import com.supermap.desktop.geometryoperation.EditEnvironment;
import com.supermap.desktop.geometryoperation.IEditController;
import com.supermap.desktop.geometryoperation.IEditModel;
import com.supermap.desktop.geometryoperation.NullEditController;
import com.supermap.desktop.geometryoperation.control.MapControlTip;
import com.supermap.desktop.mapeditor.MapEditorProperties;
import com.supermap.desktop.utilities.*;
import com.supermap.mapping.Layer;
import com.supermap.ui.Action;
import com.supermap.ui.TrackMode;
import com.supermap.ui.TrackedEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public abstract class GeometryDrawingSplitEditor extends AbstractEditor {
	private static final String Tag_GeometrySplit = "Tag_GeometrySplit";
	public abstract String getTagTip();

	public abstract String getSplitTip();

	public abstract Action getMapControlAction();

	public abstract TrackMode getTrackMode();

	public abstract boolean splitGeometry(EditEnvironment environment,Geometry geometry, Geometry splitGeometry, Map<Geometry, Map<String, Object>> resultGeometry, Map<String, Object> values, GeoStyle geoStyle, double tolerance);

	private IEditController regionSplitController = new EditControllerAdapter() {
//		@Override
//		public void mouseClicked(EditEnvironment environment, MouseEvent e) {
//			if (SwingUtilities.isRightMouseButton(e)) {
//				environment.stopEditor();
//			}
//		}

		@Override
		public void mousePressed(EditEnvironment environment, MouseEvent e) {
			GeometryDrawingSplitModel editModel = (GeometryDrawingSplitModel) environment.getEditModel();
			if (!editModel.isTracking && e.getButton() == MouseEvent.BUTTON1) {
				editModel.isTracking = true;
				editModel.setTipMessage(MapEditorProperties.getString("String_RightClickToEnd"));
			} else if (editModel.isTracking && e.getButton() == MouseEvent.BUTTON3) {
				runGeometrySplit(environment);
				editModel.isTracking=false;
			}else if (!editModel.isTracking &&e.getButton() == MouseEvent.BUTTON3){
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
		GeometryDrawingSplitModel editModel;
		if (environment.getEditModel() instanceof GeometryDrawingSplitModel) {
			editModel = (GeometryDrawingSplitModel) environment.getEditModel();
		} else {
			editModel = new GeometryDrawingSplitModel();
			environment.setEditModel(editModel);
		}
		environment.setEditController(this.regionSplitController);

		editModel.oldMapControlAction = environment.getMapControl().getAction();
		editModel.oldTrackMode = environment.getMapControl().getTrackMode();
		environment.getMapControl().setAction(getMapControlAction());
		environment.getMapControl().setTrackMode(getTrackMode());
		editModel.tip.bind(environment.getMapControl());
		initializeSrcGeometrys(environment);
	}

	@Override
	public void deactivate(EditEnvironment environment) {
		if (environment.getEditModel() instanceof GeometryDrawingSplitModel) {
			GeometryDrawingSplitModel editModel = (GeometryDrawingSplitModel) environment.getEditModel();

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
				IRegionFeature.class, ILineFeature.class);
	}

	@Override
	public boolean check(EditEnvironment environment) {
		return environment.getEditor() instanceof GeometryDrawingSplitEditor;
	}

	/**
	 * 将选中能够进行分割的对象红高亮显示
	 * @param environment
	 */
	private void initializeSrcGeometrys(EditEnvironment environment) {
		if (!(environment.getEditModel() instanceof GeometryDrawingSplitModel)) {
			return;
		}

		GeoStyle style = new GeoStyle();
		style.setLineColor(Color.RED);
		style.setFillOpaqueRate(0);

		List<Layer> layers = MapUtilities.getLayers(environment.getMap());

		for (Layer layer : layers) {
			if (layer.isEditable()
					&& layer.getDataset() != null
					&& layer.getDataset() instanceof DatasetVector
					&& (layer.getDataset().getType() == DatasetType.LINE || layer.getDataset().getType() == DatasetType.REGION || layer.getDataset().getType() == DatasetType.CAD)
					&& layer.getSelection().getCount() > 0) {
				Recordset recordset = layer.getSelection().toRecordset();

				try {
					while (!recordset.isEOF()) {
						IGeometry geometry = DGeometryFactory.create(recordset.getGeometry());
						if (geometry instanceof ILineFeature || geometry instanceof IRegionFeature) {
							GeometryUtilities.setGeometryStyle(geometry.getGeometry(), style);
							environment.getMap().getTrackingLayer().add(geometry.getGeometry(), getTagTip());
						}

						geometry.dispose();
						geometry = null;
						recordset.moveNext();
					}
				} catch (Exception ex) {
					Application.getActiveApplication().getOutput().output(ex);
				} finally {
					if (recordset != null) {
						recordset.close();
						recordset.dispose();
					}
				}
			}
			environment.getMap().refreshTrackingLayer();
		}
	}

	/**
	 * 绘制线或面结束后，获取所绘制的对象
	 */
	private void mapControlTracked(EditEnvironment environment, TrackedEvent e) {
		if (!(environment.getEditModel() instanceof GeometryDrawingSplitModel)) {
			return;
		}
		GeometryDrawingSplitModel editModel = (GeometryDrawingSplitModel) environment.getEditModel();

		try {
			editModel.geometry = e.getGeometry();
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private void runGeometrySplit(EditEnvironment environment) {
		GeometryDrawingSplitModel editModel = (GeometryDrawingSplitModel) environment.getEditModel();
		try {
			environment.getMapControl().getEditHistory().batchBegin();
			editModel.setTipMessage(MapEditorProperties.getString(getSplitTip()));
			MapUtilities.clearTrackingObjects(environment.getMap(), getTagTip());
			List<Layer> layers = MapUtilities.getLayers(environment.getMap());
			Map<Geometry, Map<String, Object>> resultGeometrys = new HashMap<Geometry, Map<String, Object>>();

			for (Layer layer : layers) {
				if (layer.isEditable() && (layer.getDataset() instanceof DatasetVector) && layer.getSelection().getCount() > 0 && editModel.geometry != null) {
					Recordset recordset = null;//当前选中的记录集集合
					Recordset newRecordset = null;//保存分割后的记录集对象
					resultGeometrys.clear();

					try {
						recordset = layer.getSelection().toRecordset();
						newRecordset = ((DatasetVector) layer.getDataset()).getRecordset(false, CursorType.DYNAMIC);
						RecordsetDelete delete = new RecordsetDelete(newRecordset.getDataset(), environment.getMapControl().getEditHistory());
						delete.begin();

						while (!recordset.isEOF()) {
							Geometry geometry = recordset.getGeometry();
							GeoStyle geoStyle = null;

							if (layer.getDataset().getType() == DatasetType.CAD) {// CAD上面弧面，弧线等，需要被转换成面线对象才能被分割
								Geometry oldGeo = geometry;
								IGeometry dGeometry = DGeometryFactory.create(geometry);
								if (dGeometry instanceof ILineFeature) {
									geometry = ((ILineFeature) dGeometry).convertToLine(environment.getGeometryConverToSegment());
								} else if (dGeometry instanceof IRegionFeature) {
									geometry = ((IRegionFeature) dGeometry).convertToRegion(environment.getGeometryConverToSegment());
								} else {
									geoStyle = null;
								}
								if (geometry != null) {
									geoStyle = oldGeo.getStyle() == null ? null : oldGeo.getStyle().clone();
									if (geometry != oldGeo) {
										oldGeo.dispose();
									}
								}
							}

							Map<String, Object> values = RecordsetUtilities.getFieldValues(recordset);
							boolean result = false;

							if (geometry.getType() == GeometryType.GEOLINE) {
								if (recordset.getDataset().getTolerance().getNodeSnap() == 0) {
									recordset.getDataset().getTolerance().setDefault();
								}
							}
							result = splitGeometry(environment,geometry, editModel.geometry, resultGeometrys, values, geoStyle, recordset.getDataset().getTolerance().getNodeSnap());

							if (result) {
								delete.delete(recordset.getID());
							} else {
								String msg = MessageFormat.format(MapEditorProperties.getString("String_GeometryOperation_SplitFailed"), layer.getCaption(), recordset.getID());
								Application.getActiveApplication().getOutput().output(msg);
							}
							recordset.moveNext();
						}

						if (delete != null) {// 更新数据集
							delete.update();
							layer.getSelection().clear();
						}
						RecordsetAddNew addNew = new RecordsetAddNew(newRecordset, environment.getMapControl().getEditHistory());
						addNew.begin();
						if (resultGeometrys.size() > 0) {
							for (Geometry tempGeometry : resultGeometrys.keySet()) {
								addNew.addNew(tempGeometry, resultGeometrys.get(tempGeometry));
							}
						}
						addNew.update();
						environment.getActiveEditableLayer().getSelection().clear();
						environment.getActiveEditableLayer().getSelection().addRange(ArrayUtilities.convertToInt(addNew.getAddHistoryIDs().toArray(new Integer[addNew.getAddHistoryIDs().size()])));
						TabularUtilities.refreshTabularForm(newRecordset.getDataset());
						Application.getActiveApplication().getOutput().output(MapEditorProperties.getString("String_GeometryOperation_SplitSuccessed"));
					} catch (Exception ex) {
						Application.getActiveApplication().getOutput().output(ex);
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
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		} finally {
			environment.getMapControl().getEditHistory().batchEnd();
			environment.getMap().refreshTrackingLayer();
			environment.getMapControl().getMap().refresh();
		}
	}

	private void clear(EditEnvironment environment) {
		if (!(environment.getEditModel() instanceof GeometryDrawingSplitModel)) {
			return;
		}
		GeometryDrawingSplitModel editModel = (GeometryDrawingSplitModel) environment.getEditModel();
		editModel.clear();
		MapUtilities.clearTrackingObjects(environment.getMap(), Tag_GeometrySplit);
	}

	private class GeometryDrawingSplitModel implements IEditModel {
		public Action oldMapControlAction = Action.SELECT2;
		public TrackMode oldTrackMode = TrackMode.EDIT;
		public MapControlTip tip = new MapControlTip();
		private JLabel tipLabel = new JLabel(MapEditorProperties.getString(getSplitTip()));
		public boolean isTracking = false;
		public Geometry geometry = null;

		public GeometryDrawingSplitModel() {
			this.tip.addLabel(this.tipLabel);
		}

		public void setTipMessage(String tipMessage) {
			this.tipLabel.setText(tipMessage);
			this.tipLabel.repaint();
		}

		public void clear() {
			this.oldMapControlAction = Action.SELECT2;
			this.oldTrackMode = TrackMode.EDIT;
			this.isTracking = false;
			this.geometry = null;
			this.tipLabel.setText(MapEditorProperties.getString(getSplitTip()));
		}
	}

}
