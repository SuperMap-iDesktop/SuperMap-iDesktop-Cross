package com.supermap.desktop.geometryoperation.editor;

/**
 * @author lixiaoyao
 */

import com.supermap.data.CursorType;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.EditType;
import com.supermap.data.GeoLine;
import com.supermap.data.GeoRegion;
import com.supermap.data.GeoStyle;
import com.supermap.data.Geometry;
import com.supermap.data.GeometryType;
import com.supermap.data.Recordset;
import com.supermap.desktop.Application;
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
import com.supermap.desktop.utilities.GeometryUtilities;
import com.supermap.desktop.utilities.ListUtilities;
import com.supermap.desktop.utilities.MapUtilities;
import com.supermap.desktop.utilities.RecordsetUtilities;
import com.supermap.desktop.utilities.TabularUtilities;
import com.supermap.mapping.Layer;
import com.supermap.ui.Action;
import com.supermap.ui.TrackMode;
import com.supermap.ui.TrackedEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;


public abstract class GeometryDrawingSplitEditor extends AbstractEditor {

	public abstract String getTagTip();

	public abstract String getSplitTip();

	public abstract Action getMapControlAction();

	public abstract TrackMode getTrackMode();
	/**
	 * 针对面对象进行分割
	 */
	public abstract boolean runSplitRegion(GeoRegion sourceRegion, Geometry splitGeometry, GeoRegion resultRegion1, GeoRegion resultRegion2);
	/**
	 * 针对线对象进行分割
	 */
	public abstract GeoLine[] runSplitLine(GeoLine sourceLine, Geometry splitGeometry, Double tolerance);


	private IEditController regionSplitController = new EditControllerAdapter() {
		@Override
		public void mouseClicked(EditEnvironment environment, MouseEvent e) {
			if (SwingUtilities.isRightMouseButton(e)) {
				environment.stopEditor();
			}
		}

		@Override
		public void mousePressed(EditEnvironment environment, MouseEvent e) {
			GeometryDrawingSplitModel editModel = (GeometryDrawingSplitModel) environment.getEditModel();
			if (!editModel.isTracking && e.getButton() == MouseEvent.BUTTON1) {
				editModel.isTracking = true;
				editModel.setTipMessage(MapEditorProperties.getString("String_RightClickToEnd"));
			} else if (editModel.isTracking && e.getButton() == MouseEvent.BUTTON3) {
				runRegionSplit(environment);
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
		initializeSrcRegions(environment);
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
	 * 将选中能够进行分割的对象高亮显示
	 * @param environment
	 */
	private void initializeSrcRegions(EditEnvironment environment) {
		if (!(environment.getEditModel() instanceof GeometryDrawingSplitModel)) {
			return;
		}

		GeoStyle style = new GeoStyle();
		style.setLineColor(Color.RED);
		style.setFillOpaqueRate(0);

		//GeometryDrawingSplitModel editModel = (GeometryDrawingSplitModel) environment.getEditModel();
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
						//Geometry geometry=recordset.getGeometry();
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

	private void runRegionSplit(EditEnvironment environment) {
		GeometryDrawingSplitModel editModel = (GeometryDrawingSplitModel) environment.getEditModel();
		try {
			environment.getMapControl().getEditHistory().batchBegin();
			editModel.setTipMessage(MapEditorProperties.getString(getSplitTip()));
			MapUtilities.clearTrackingObjects(environment.getMap(), getTagTip());

			List<Layer> layers = MapUtilities.getLayers(environment.getMap());
			for (Layer layer : layers) {
				if (layer.isEditable() && (layer.getDataset() instanceof DatasetVector) && layer.getSelection().getCount() > 0) {
					Recordset recordset = null;//选中的记录集集合
					Recordset newRecordset = null;//保存分割后的记录集对象
					RecordsetDelete delete = null;//保存要删除的记录集集合

					try {
						recordset = layer.getSelection().toRecordset();
						newRecordset = ((DatasetVector) layer.getDataset()).getRecordset(false, CursorType.DYNAMIC);
						Recordset.BatchEditor editor = newRecordset.getBatch();
						editor.setMaxRecordCount(2000);
						editor.begin();

						if (layer.isEditable()) {
							delete = new RecordsetDelete(newRecordset.getDataset(), environment.getMapControl().getEditHistory());
							delete.begin();
						}

						while (!recordset.isEOF()) {
							Geometry geometry = recordset.getGeometry();
							GeoStyle geoStyle = null;
							// CAD上面弧面，弧线等，需要被转换成面线对象才能被分割
							if (layer.getDataset().getType() == DatasetType.CAD) {
								Geometry oldGeo = geometry;
								IGeometry dGeometry = DGeometryFactory.create(geometry);
								if (dGeometry instanceof ILineFeature) {
									geometry = ((ILineFeature) dGeometry).convertToLine(120);
								} else if (dGeometry instanceof IRegionFeature) {
									geometry = ((IRegionFeature) dGeometry).convertToRegion(120);
								}
								if (geometry != null) {
									geoStyle = oldGeo.getStyle() == null ? null : oldGeo.getStyle().clone();
									if (geometry != oldGeo) {
										oldGeo.dispose();// 转换前的几何对象应该及时释放掉。
									}
								}
							}

							Map<String, Object> values = RecordsetUtilities.getFieldValues(recordset);

							if (geometry.getType() == GeometryType.GEOREGION) {
								GeoRegion resultGeoRegion1 = new GeoRegion();
								GeoRegion resultGeoRegion2 = new GeoRegion();
								GeoRegion tempGeoRegion = (GeoRegion) geometry;

								boolean resultSplit = runSplitRegion(tempGeoRegion, editModel.geometry, resultGeoRegion1, resultGeoRegion2);
								if (resultSplit) {
									Geometry newGeometry = (Geometry) resultGeoRegion1;
									if (layer.getDataset().getType() == DatasetType.CAD && geoStyle != null) {
										newGeometry.setStyle(geoStyle.clone());//设置风格
									}
									newRecordset.addNew(newGeometry, values);
									environment.getMapControl().getEditHistory().add(EditType.ADDNEW, newRecordset, true);
									newGeometry = (Geometry) resultGeoRegion2;
									if (layer.getDataset().getType() == DatasetType.CAD && geoStyle != null) {
										newGeometry.setStyle(geoStyle.clone());//设置风格
									}
									newRecordset.addNew(newGeometry, values);
									environment.getMapControl().getEditHistory().add(EditType.ADDNEW, newRecordset, true);
									geometry.dispose();
									delete.delete(recordset.getID());
									String msg = MessageFormat.format(MapEditorProperties.getString("String_GeometryOperation_SplitSuccessed"), layer.getCaption(), recordset.getID());
									Application.getActiveApplication().getOutput().output(msg);
								} else {//如果分割失败就输出错误信息，并保留原对象
									String msg = MessageFormat.format(MapEditorProperties.getString("String_GeometryOperation_SplitFailed"), layer.getCaption(), recordset.getID());
									Application.getActiveApplication().getOutput().output(msg);
								}
								resultGeoRegion1.dispose();
								resultGeoRegion2.dispose();
							} else if (geometry.getType() == GeometryType.GEOLINE) {
								GeoLine tempGeoLine = (GeoLine) geometry;
								GeoLine resultLines[] = runSplitLine(tempGeoLine, editModel.geometry, recordset.getDataset().getTolerance().getNodeSnap());
								if (resultLines != null && resultLines.length >= 2) {
									for (GeoLine resultGeoLine : resultLines) {
										Geometry newGeometry = (Geometry) resultGeoLine;
										if (layer.getDataset().getType() == DatasetType.CAD && geoStyle != null) {
											newGeometry.setStyle(geoStyle.clone());//设置风格
										}
										newRecordset.addNew(newGeometry, values);
										environment.getMapControl().getEditHistory().add(EditType.ADDNEW, newRecordset, true);
										geometry.dispose();
										delete.delete(recordset.getID());
									}
									String msg = MessageFormat.format(MapEditorProperties.getString("String_GeometryOperation_SplitSuccessed"), layer.getCaption(), recordset.getID());
									Application.getActiveApplication().getOutput().output(msg);
								} else {
									String msg = MessageFormat.format(MapEditorProperties.getString("String_GeometryOperation_SplitFailed"), layer.getCaption(), recordset.getID());
									Application.getActiveApplication().getOutput().output(msg);
								}
								tempGeoLine.dispose();
								resultLines = null;
							}
							recordset.moveNext();
						}
						if (delete != null) {
							delete.update();
							editor.update();
							layer.getSelection().clear();
						}
						TabularUtilities.refreshTabularForm(newRecordset.getDataset());
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
			environment.getMapControl().getMap().refresh();
		}
	}

	private void clear(EditEnvironment environment) {
		if (!(environment.getEditModel() instanceof GeometryDrawingSplitModel)) {
			return;
		}
		GeometryDrawingSplitModel editModel = (GeometryDrawingSplitModel) environment.getEditModel();
		editModel.clear();
		MapUtilities.clearTrackingObjects(environment.getMap(), getTagTip());
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
