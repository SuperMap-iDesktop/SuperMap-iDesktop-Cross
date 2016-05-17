package com.supermap.desktop.geometryoperation.editor;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.supermap.data.CursorType;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.EditType;
import com.supermap.data.GeoLine;
import com.supermap.data.GeoStyle;
import com.supermap.data.Geometry;
import com.supermap.data.Point2D;
import com.supermap.data.Point2Ds;
import com.supermap.data.Recordset;
import com.supermap.desktop.Application;
import com.supermap.desktop.core.recordset.RecordsetDelete;
import com.supermap.desktop.geometry.Abstract.IGeometry;
import com.supermap.desktop.geometry.Abstract.ILineFeature;
import com.supermap.desktop.geometry.Abstract.IMultiPartFeature;
import com.supermap.desktop.geometry.Implements.DGeometryFactory;
import com.supermap.desktop.geometryoperation.EditEnvironment;
import com.supermap.desktop.geometryoperation.JDialogFieldOperationSetting;
import com.supermap.desktop.mapeditor.MapEditorProperties;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.utilties.CursorUtilties;
import com.supermap.desktop.utilties.ListUtilties;
import com.supermap.desktop.utilties.MapUtilties;
import com.supermap.desktop.utilties.TabularUtilties;
import com.supermap.mapping.Layer;
import com.supermap.mapping.Selection;

public class JointLineEditor extends AbstractEditor {

	private int OperationType = 1; // 1. 临近点相连。 2. 首尾相连。

	@Override
	public void activate(EditEnvironment environment) {
		JDialogFieldOperationSetting formCombination = new JDialogFieldOperationSetting(MapEditorProperties.getString("String_GeometryOperation_Combination"),
				environment.getMap(), DatasetType.LINE);

		if (formCombination.showDialog() == DialogResult.OK) {
			CursorUtilties.setWaitCursor();
			jointLine(environment, formCombination.getEditLayer(), formCombination.getPropertyData());
			TabularUtilties.refreshTabularForm((DatasetVector) formCombination.getEditLayer().getDataset());
		}
	}

	@Override
	public boolean enble(EditEnvironment environment) {
		return false;
	}

	private void jointLine(EditEnvironment environment, Layer editLayer, Map<String, Object> propertyData) {
		GeoLine baseGeoLine = null;
		environment.getMapControl().getEditHistory().batchBegin();

		try {
			int partLineCounts = 0;
			boolean isOriginalBaseLine = true;
			ArrayList<Integer> deleteIDs = new ArrayList<Integer>();
			// 统一管理后面从记录集中取出来的GeoLine对象的释放
			ArrayList<GeoLine> releaseGeoLines = new ArrayList<GeoLine>();
			ArrayList<Layer> layers = MapUtilties.getLayers(environment.getMapControl().getMap());

			for (Layer layer : layers) {
				Selection selection = null;

				if (layer.getDataset() instanceof DatasetVector) {
					selection = new Selection(layer.getSelection());
					layer.getSelection().clear();
				}

				if (selection != null && selection.getCount() > 0) {
					Recordset recordset = ((DatasetVector) layer.getDataset()).getRecordset(false, CursorType.DYNAMIC);

					for (int i = 0; i < selection.getCount(); i++) {
						recordset.seekID(selection.get(i));
						IGeometry geometry = DGeometryFactory.create(recordset.getGeometry());
						GeoLine currentLine = null;

						if (geometry instanceof ILineFeature) {
							currentLine = ((ILineFeature) geometry).convertToLine(120);

							if (currentLine.getPartCount() > 1) {
								partLineCounts++;
								continue;
							}

							baseGeoLine = jointTwoLines(baseGeoLine, currentLine, isOriginalBaseLine);
							if (baseGeoLine != null && currentLine != null && !baseGeoLine.equals(currentLine)) {
								isOriginalBaseLine = false;
							}

							if (editLayer.getDataset().equals(layer.getDataset()) && currentLine != null) {
								deleteIDs.add(selection.get(i));
							}
							releaseGeoLines.add(currentLine);
						}
					}
					selection.dispose();
					if (recordset != null) {
						recordset.dispose();
					}
				}
			}

			if (!isOriginalBaseLine) {
				editLayer.getSelection().clear();
				Recordset resultRecordset = ((DatasetVector) editLayer.getDataset()).getRecordset(false, CursorType.DYNAMIC);
				GeoStyle style = null;
				RecordsetDelete delete = new RecordsetDelete((DatasetVector) editLayer.getDataset(), environment.getMapControl().getEditHistory());
				delete.update();

				if (deleteIDs.size() > 0) {
					for (int id : deleteIDs) {
						if (resultRecordset.seekID(id)) {
							// 记录结果图层中某一个对象的风格
							if (style == null) {
								Geometry geometry = resultRecordset.getGeometry();
								if (geometry.getStyle() != null) {
									style = geometry.getStyle().clone();
								}
								if (geometry != null) {
									geometry.dispose();
								}
							}

							delete.delete(id);
							// FormMap.MapControl.EditHistory.Add(EditType.Delete, resultRecordset, true);
							// resultRecordset.Delete();
						}
					}
				}
				delete.update();

				baseGeoLine.setStyle(style);
				boolean isSucceed = false;

				if (resultRecordset.addNew(baseGeoLine, propertyData)) {
					if (resultRecordset.update()) {
						editLayer.getSelection().add(resultRecordset.getID());
						environment.getMapControl().getEditHistory().add(EditType.ADDNEW, resultRecordset, true);
						isSucceed = true;

						if (partLineCounts > 0) {
							Application.getActiveApplication().getOutput()
									.output(MessageFormat.format(MapEditorProperties.getString("String_GeometryEdit_ConnectLineErrorOutPut"), partLineCounts));
						}
						Application
								.getActiveApplication()
								.getOutput()
								.output(MessageFormat.format(MapEditorProperties.getString("String_GeometryEdit_ConnectLineTrueOutPut"),
										releaseGeoLines.size(), editLayer.getName()));
					}
				}

				if (!isSucceed) {
					Application.getActiveApplication().getOutput().output(MapEditorProperties.getString("String_GeometryEdit_ConnectLineUpdateFaleOutPut"));
				}

				environment.getMapControl().getEditHistory().batchEnd();
				if (resultRecordset != null) {
					resultRecordset.close();
					resultRecordset.dispose();
				}
			} else {
				environment.getMapControl().getEditHistory().batchCancel();

				if (partLineCounts > 0) {
					Application.getActiveApplication().getOutput()
							.output(MessageFormat.format(MapEditorProperties.getString("String_GeometryEdit_ConnectLineErrorOutPut"), partLineCounts));
				}
				Application.getActiveApplication().getOutput().output(MapEditorProperties.getString("String_GeometryEdit_ConnectLineFalseOutPut"));
			}

			if (baseGeoLine != null) {
				baseGeoLine.dispose();
			}

			releaseGeometryList(releaseGeoLines);
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private GeoLine jointTwoLines(GeoLine baseLine, GeoLine targetLine, Boolean isOriginalBaseLine) {
		GeoLine resultLine = null;

		if (baseLine == null) {
			if (targetLine != null) {
				resultLine = targetLine;
			}
		} else {
			if (targetLine == null) {
				resultLine = baseLine;
			} else {
				Point2Ds basePoints = baseLine.getPart(0);
				Point2Ds targetPoints = targetLine.getPart(0);

				// 不保持线方向，连接方式为最近两点相连
				if (this.OperationType == 1) {
					Double distanceStartToStart = getDistance(basePoints.getItem(0), targetPoints.getItem(0));
					Double distanceStartToEnd = getDistance(basePoints.getItem(0), targetPoints.getItem(targetPoints.getCount() - 1));
					Double distanceEndToStart = getDistance(basePoints.getItem(basePoints.getCount() - 1), targetPoints.getItem(0));
					Double distanceEndToEnd = getDistance(basePoints.getItem(basePoints.getCount() - 1), targetPoints.getItem(targetPoints.getCount() - 1));
					Double minimumDistance = Math.min(distanceStartToStart, distanceStartToEnd);
					minimumDistance = Math.min(minimumDistance, distanceEndToStart);
					minimumDistance = Math.min(minimumDistance, distanceEndToEnd);
					if (minimumDistance.equals(distanceStartToStart)) {
						if (targetLine.reverse()) {
							// targetLine[0].AddRange(basePoints.ToArray());
							// basePoints = targetLine[0];
							basePoints = addTargetPoint(targetLine.getPart(0), basePoints);
						}
					} else if (minimumDistance.equals(distanceStartToEnd)) {
						// targetLine[0].AddRange(basePoints.ToArray());
						// basePoints = targetLine[0];
						basePoints = addTargetPoint(targetLine.getPart(0), basePoints);
					} else if (minimumDistance.equals(distanceEndToEnd)) {
						if (targetLine.reverse()) {
							targetPoints = targetLine.getPart(0);
							// basePoints.AddRange(targetPoints.ToArray());
							basePoints = addTargetPoint(basePoints, targetPoints);
						}
					} else if (minimumDistance.equals(distanceEndToStart)) {
						// basePoints.AddRange(targetPoints.ToArray());
						basePoints = addTargetPoint(basePoints, targetPoints);
					}

					resultLine = new GeoLine(basePoints);
				}
				// 保持线方向，首尾相连
				else if (this.OperationType == 2) {
					targetPoints = targetLine.getPart(0);
					// basePoints.AddRange(targetPoints.ToArray());
					basePoints = addTargetPoint(basePoints, targetPoints);
					resultLine = new GeoLine(basePoints);
				}
			}
		}

		return resultLine;
	}

	private double getDistance(Point2D point1, Point2D point2) {
		return Math.sqrt(Math.pow((point1.getX() - point2.getX()), 2) + Math.pow((point1.getY() - point2.getY()), 2));
	}

	/**
	 * 连接线对象功能，线对象的首尾节点有重合需要删除一个点
	 * 
	 * @param basePoints
	 * @param targetPoints
	 * @return
	 */
	private Point2Ds addTargetPoint(Point2Ds basePoints, Point2Ds targetPoints) {
		ArrayList<Point2D> points = new ArrayList<>();
		ListUtilties.addArray(points, basePoints.toArray());
		Point2Ds targetPointNew = new Point2Ds(targetPoints.toArray());

		if (OperationType == 0) {
			if (points.get(points.size() - 1).equals(targetPointNew.getItem(0))) {
				targetPointNew.remove(0);
			}
		} else {
			if (points.get(points.size() - 1).equals(targetPointNew.getItem(0)) || points.get(0).equals(targetPointNew.getItem(0))) {
				targetPointNew.remove(0);
			} else if (points.get(points.size() - 1).equals(targetPointNew.getItem(targetPointNew.getCount() - 1))
					|| points.get(0).equals(targetPointNew.getItem(targetPointNew.getCount() - 1))) {
				targetPointNew.remove(targetPointNew.getCount() - 1);
			}
		}

		ListUtilties.addArray(points, targetPointNew.toArray());
		return new Point2Ds(points.toArray(new Point2D[points.size()]));
	}

	protected void releaseGeometryList(ArrayList<GeoLine> lines) {
		if (lines != null) {
			if (lines.size() > 0) {
				for (int i = lines.size() - 1; i >= 0; i--) {
					GeoLine line = lines.get(i);
					if (line != null) {
						line.dispose();
						lines.remove(i);
					}
				}
			}
		}
	}
}
