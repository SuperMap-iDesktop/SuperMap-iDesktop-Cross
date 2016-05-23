package com.supermap.desktop.geometryoperation.editor;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;

import com.supermap.data.CoordSysTransMethod;
import com.supermap.data.CoordSysTransParameter;
import com.supermap.data.CoordSysTranslator;
import com.supermap.data.CursorType;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.FieldInfos;
import com.supermap.data.GeoLine;
import com.supermap.data.GeoRegion;
import com.supermap.data.GeoStyle;
import com.supermap.data.Geometrist;
import com.supermap.data.Geometry;
import com.supermap.data.GeometryType;
import com.supermap.data.Recordset;
import com.supermap.desktop.Application;
import com.supermap.desktop.core.recordset.RecordsetAddNew;
import com.supermap.desktop.core.recordset.RecordsetDelete;
import com.supermap.desktop.geometry.Abstract.IGeometry;
import com.supermap.desktop.geometry.Abstract.ILineFeature;
import com.supermap.desktop.geometry.Abstract.IPointFeature;
import com.supermap.desktop.geometry.Abstract.IRegionFeature;
import com.supermap.desktop.geometry.Implements.DGeometryFactory;
import com.supermap.desktop.geometryoperation.EditControllerAdapter;
import com.supermap.desktop.geometryoperation.EditEnvironment;
import com.supermap.desktop.geometryoperation.IEditController;
import com.supermap.desktop.geometryoperation.IEditModel;
import com.supermap.desktop.geometryoperation.NullEditController;
import com.supermap.desktop.geometryoperation.control.MapControlTip;
import com.supermap.desktop.mapeditor.MapEditorProperties;
import com.supermap.desktop.utilties.ArrayUtilties;
import com.supermap.desktop.utilties.MapControlUtilties;
import com.supermap.desktop.utilties.MapUtilties;
import com.supermap.desktop.utilties.TabularUtilties;
import com.supermap.mapping.Layer;
import com.supermap.ui.Action;
import com.supermap.ui.GeometrySelectedEvent;
import com.supermap.ui.TrackMode;

public class SplitByGeometryEditor extends AbstractEditor {

	private static final Action MAP_CONTROL_ACTION = Action.SELECT;
	private static final String g_strSplitLayer = "GeometrySplit";

	private IEditController splitByGeometryEditController = new EditControllerAdapter() {

		@Override
		public void geometrySelected(EditEnvironment environment, GeometrySelectedEvent arg0) {

		}
	};

	@Override
	public void activate(EditEnvironment environment) {
		SplitByGeometryEditModel editModel;
		if (environment.getEditModel() instanceof SplitByGeometryEditModel) {
			editModel = (SplitByGeometryEditModel) environment.getEditModel();
		} else {
			editModel = new SplitByGeometryEditModel();
			environment.setEditModel(editModel);
		}
		environment.setEditController(this.splitByGeometryEditController);

		editModel.oldMapControlAction = environment.getMapControl().getAction();
		editModel.oldTrackMode = environment.getMapControl().getTrackMode();
		environment.getMapControl().setAction(MAP_CONTROL_ACTION);
		environment.getMapControl().setTrackMode(TrackMode.TRACK);
		editModel.tip.bind(environment.getMapControl());
	}

	@Override
	public void deactivate(EditEnvironment environment) {
		if (environment.getEditModel() instanceof SplitByGeometryEditModel) {
			SplitByGeometryEditModel editModel = (SplitByGeometryEditModel) environment.getEditModel();

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

	private void splitByGeometry(EditEnvironment environment) {
		boolean result = false;
		Recordset recordset = null;
		SplitByGeometryEditModel editModel = (SplitByGeometryEditModel) environment.getEditModel();

		try {
			// 获取用于分割的对象
			ArrayList<Layer> layers = MapUtilties.getLayers(environment.getMap());
			for (Layer layer : layers) {
				if (layer.getSelection().getCount() == 1) {
					Recordset selectrecordset = layer.getSelection().toRecordset();
					IGeometry selecteometry = null;

					try {
						selecteometry = DGeometryFactory.create(selectrecordset.getGeometry());// 单选
						selectrecordset.close();
						selectrecordset.dispose();

						if (editModel.splitGeometry == null && selecteometry != null) {
							if (selecteometry instanceof IPointFeature) {
								editModel.splitGeometry = selecteometry.getGeometry();
							} else if (selecteometry instanceof ILineFeature) {
								editModel.splitGeometry = ((ILineFeature) selecteometry).convertToLine(120);
							} else if (selecteometry instanceof IRegionFeature) {
								editModel.splitGeometry = ((IRegionFeature) selecteometry).convertToRegion(120);
							} else {
								editModel.splitGeometry = null;
							}

							if (editModel.splitGeometry != null) {
								editModel.selectedLayer = layer;
								break;
							}
						}
					}

					finally {
						if (recordset != null) {
							recordset.close();
							recordset.dispose();
						}

						if (editModel.splitGeometry != selecteometry && selecteometry != null) {
							selecteometry.dispose();
						}
					}
				}
			}

			if (editModel.splitGeometry != null) {
				try {
					environment.getMapControl().getEditHistory().batchBegin();

					Map<Geometry, Map<String, Object>> resultGeometrys = new HashMap<Geometry, Map<String, Object>>();
					Geometry geometry;// 被分割的每一个对象
					editModel.selectionCount = 0;

					for (Layer layer : editModel.forEraseGeometryIDs.keySet()) {
						resultGeometrys.clear();
						DatasetVector datasetVector = (DatasetVector) layer.getDataset();
						recordset = datasetVector.getRecordset(false, CursorType.DYNAMIC);
						GeoStyle geoStyle = null;
						RecordsetDelete delete = new RecordsetDelete(datasetVector, environment.getMapControl().getEditHistory());
						delete.begin();

						for (Integer id : editModel.forEraseGeometryIDs.get(layer)) {
							// 如果不在一个图层上有可能ID一样的
							if ((editModel.selectedLayer != layer) || id != editModel.splitGeometry.getID()) {
								Geometry dynamicGeometry = null;
								// 判断下地图是经过投影转换的，
								// 当前图层的数据集投影和地图不一致，则需要将基线对象投影到被分割数据集一致才能进行分割
								if (environment.getMap().isDynamicProjection()
										&& !layer.getDataset().getPrjCoordSys().equals(editModel.selectedLayer.getDataset().getPrjCoordSys())) {
									if (editModel.splitGeometry.getType() == GeometryType.GEOLINE) {
										dynamicGeometry = new GeoLine((GeoLine) editModel.splitGeometry);
									} else if (editModel.splitGeometry.getType() == GeometryType.GEOREGION) {
										dynamicGeometry = new GeoRegion((GeoRegion) editModel.splitGeometry);
									}

									CoordSysTransParameter param = null;
									try {
										CoordSysTranslator.convert(dynamicGeometry, editModel.selectedLayer.getDataset().getPrjCoordSys(),
												layer.getDataset().getPrjCoordSys(), param, CoordSysTransMethod.MTH_COORDINATE_FRAME);
									} finally {
										if (param != null) {
											param.dispose();
										}
									}
								}

								recordset.seekID(id);
								geometry = recordset.getGeometry();
								geoStyle = null;
								// CAD上面弧面，弧线等，需要被转换成面线对象才能被分割
								if (layer.getDataset().getType() == DatasetType.CAD)

								{
									Geometry oldGeo = geometry;
									IGeometry dGeometry = DGeometryFactory.create(geometry);
									if (dGeometry instanceof ILineFeature) {
										geometry = ((ILineFeature) dGeometry).convertToLine(120);
									} else if (dGeometry instanceof IRegionFeature) {
										geometry = ((IRegionFeature) dGeometry).convertToRegion(120);
									} else {
										geometry = null;
										geoStyle = null;
									}
									if (geometry != null) {
										geoStyle = oldGeo.getStyle().clone();
										if (geometry != oldGeo) {
											oldGeo.dispose();// 转换前的几何对象应该及时释放掉。
										}
									}
								}

								if (geometry != null) {
									Map<String, Object> values = new HashMap<>();
									FieldInfos fieldInfos = recordset.getFieldInfos();
									Object[] fieldValues = recordset.getValues();
									for (int i = 0; i < fieldValues.length; i++) {
										if (!fieldInfos.get(i).isSystemField()) {
											values.put(fieldInfos.get(i).getName(), fieldValues[i]);
										}
									}

									if (geometry.getType() == GeometryType.GEOREGION)// 面
									{
										if (dynamicGeometry != null) {
											result = splitRegion((GeoRegion) geometry, dynamicGeometry, resultGeometrys, values, geoStyle);
										} else {
											result = splitRegion((GeoRegion) geometry, editModel.splitGeometry, resultGeometrys, values, geoStyle);
										}

										if (result) {
											delete.delete(recordset.getID());
										}
									} else if (geometry.getType() == GeometryType.GEOLINE)// 线
									{
										if (recordset.getDataset().getTolerance().getNodeSnap() == 0) {
											recordset.getDataset().getTolerance().setDefault();
										}

										if (dynamicGeometry != null) {
											result = splitLine(environment, (GeoLine) geometry, dynamicGeometry, resultGeometrys, values,
													recordset.getDataset().getTolerance().getNodeSnap(), geoStyle);
										} else {
											result = splitLine(environment, (GeoLine) geometry, editModel.splitGeometry, resultGeometrys, values,
													recordset.getDataset().getTolerance().getNodeSnap(), geoStyle);
										}

										if (result) {
											delete.delete(recordset.getID());
										}
									}
								}
								// 将临时投影的基线释放
								if (dynamicGeometry != null) {
									dynamicGeometry.dispose();
									dynamicGeometry = null;
								}

								if (geometry != null) {
									geometry.dispose();
									geometry = null;
								}
							}
						}

						delete.update();
						recordset.close();
						recordset.dispose();

						// 向数据集追加结果对象
						if (resultGeometrys.size() > 0)

						{
							recordset = null;
							layer.getSelection().clear();
							List<Integer> addHistoryIDs = new ArrayList<Integer>();
							recordset = ((DatasetVector) layer.getDataset()).getRecordset(true, CursorType.DYNAMIC);
							RecordsetAddNew addNew = new RecordsetAddNew(recordset, environment.getMapControl().getEditHistory());
							addNew.begin();
							for (Geometry g : resultGeometrys.keySet()) {
								// 对于分割线对象与被分割线对象邻接的情况，分割会成功但是分割的结果有一个空对象
								// 追加要数据集失败，所有要判断一下结果对象是否能够追加
								Boolean isCanApend = true;
								Geometry geometryClone = g.clone();
								if (geometryClone.getType() == GeometryType.GEOLINE) {
									GeoLine geoLine = null;
									try {
										geoLine = (GeoLine) geometryClone;
										if (geoLine != null) {
											isCanApend = geoLine.getPartCount() > 0;
										}
									} finally {

									}
								}

								if (isCanApend) {
									addNew.addNew(g, resultGeometrys.get(g));
								}
							}
							addNew.update();
							addHistoryIDs = addNew.getAddHistoryIDs();
							TabularUtilties.refreshTabularForm(recordset.getDataset());
							if (addHistoryIDs.size() > 0) {
								layer.getSelection().addRange(ArrayUtilties.convertToInt(addHistoryIDs.toArray(new Integer[addHistoryIDs.size()])));
							}
							editModel.selectionCount += addHistoryIDs.size();
						}
					}
					environment.getMapControl().getEditHistory().batchEnd();
					environment.getMap().refresh();
					environment.getMapControl().refreshAtTracked();
					if (recordset != null) {
						recordset.dispose();
					}

					if (result) {
						Application.getActiveApplication().getOutput().output(MapEditorProperties.getString("String_Successed_Message"));
					} else {
						Application.getActiveApplication().getOutput().output(MapEditorProperties.getString("String_Failed_Message"));
					}
				} catch (Exception ex) {
					Application.getActiveApplication().getOutput().output(ex);
				}
			} else {
				Application.getActiveApplication().getOutput().output(MapEditorProperties.getString("String_Failed_Message"));
				Application.getActiveApplication().getOutput().output(MapEditorProperties.getString("String_NotCorrectGeometry"));
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private boolean splitRegion(GeoRegion region, Geometry baseLine, Map<Geometry, Map<String, Object>> resultGeometry, Map<String, Object> values,
			GeoStyle geoStyle) {
		boolean result = false;
		Recordset recordset = null;
		try {
			GeoRegion targetRegion1 = new GeoRegion();
			GeoRegion targetRegion2 = new GeoRegion();
			try {
				result = Geometrist.splitRegion(region, baseLine, targetRegion1, targetRegion2);
			} catch (Exception ex) {
				Application.getActiveApplication().getOutput().output(ex);
			}

			if (result && targetRegion1 != null && targetRegion2 != null) {
				if (geoStyle != null) {
					targetRegion1.setStyle(geoStyle.clone());
					targetRegion2.setStyle(geoStyle.clone());
				}
				resultGeometry.put(targetRegion1, values);
				resultGeometry.put(targetRegion2, values);

			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		} finally {
			if (recordset != null) {
				recordset.dispose();
			}
		}
		return result;
	}

	private boolean splitLine(EditEnvironment environment, GeoLine desLine, Geometry baseLine, Map<Geometry, Map<String, Object>> resultGeometrys,
			Map<String, Object> values, Double tolerance, GeoStyle geoStyle) {
		Boolean result = false;
		try {
			GeoLine[] curLines = null;

			GeoStyle style1 = new GeoStyle();
			style1.setLineWidth(0.6);
			style1.setLineColor(Color.RED);

			GeoStyle style2 = new GeoStyle();
			style2.setLineWidth(0.6);
			style2.setLineColor(Color.BLUE);

			curLines = Geometrist.splitLine(desLine, baseLine, tolerance);
			if (curLines.length >= 2) {
				result = true;

				for (int i = 0; i < curLines.length; i++) {
					GeoStyle beforeStyle = curLines[i].getStyle();
					if (i % 2 == 0) {
						curLines[i].setStyle(style1);
					} else {
						curLines[i].setStyle(style2);
					}
					environment.getMap().getTrackingLayer().add(curLines[i], g_strSplitLayer);

					if (geoStyle != null) {
						curLines[i].setStyle(geoStyle.clone());
					} else {
						curLines[i].setStyle(beforeStyle);
					}
					resultGeometrys.put(curLines[i], values);
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return result;
	}

	@Override
	public boolean enble(EditEnvironment environment) {
		return false;
	}

	private void clear(EditEnvironment environment) {
		if (environment.getEditModel() instanceof SplitByGeometryEditModel) {
			((SplitByGeometryEditModel) environment.getEditModel()).clear();
		}
	}

	private class SplitByGeometryEditModel implements IEditModel {

		public Action oldMapControlAction = Action.SELECT2;
		public TrackMode oldTrackMode = TrackMode.EDIT;

		public Geometry splitGeometry = null;
		public Layer selectedLayer = null;
		public Map<Layer, List<Integer>> forEraseGeometryIDs = new HashMap<>();
		public int selectionCount = 0;

		public MapControlTip tip = new MapControlTip();
		public JLabel labelTip = new JLabel(MapEditorProperties.getString("String_SplitByGeometry_SelectTarget"));

		public void clear() {

		}
	}
}
