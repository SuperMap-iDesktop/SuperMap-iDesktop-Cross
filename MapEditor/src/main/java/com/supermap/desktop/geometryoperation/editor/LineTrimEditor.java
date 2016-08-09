package com.supermap.desktop.geometryoperation.editor;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.geometry.Abstract.IGeometry;
import com.supermap.desktop.geometry.Abstract.ILineFeature;
import com.supermap.desktop.geometry.Implements.DGeometryFactory;
import com.supermap.desktop.geometryoperation.*;
import com.supermap.desktop.geometryoperation.control.MapControlTip;
import com.supermap.desktop.mapeditor.MapEditorProperties;
import com.supermap.desktop.utilities.MapUtilities;
import com.supermap.mapping.Layer;
import com.supermap.ui.Action;
import com.supermap.ui.GeometrySelectedEvent;
import com.supermap.ui.MapControl;
import com.supermap.ui.TrackMode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LineTrimEditor extends AbstractEditor {

	private static final String TAG_LINEEXTEND = "Tag_TrimLineEditorBase"; // 基线 tracking
	private static final Action MAP_CONTROL_ACTION = Action.SELECT;

	private IEditController lineTrimEditController = new EditControllerAdapter() {

		@Override
		public void geometrySelected(EditEnvironment environment, GeometrySelectedEvent arg0) {
			if (!(environment.getEditModel() instanceof LineTrimEditModel)) {
				return;
			}

			LineTrimEditModel editModel = (LineTrimEditModel) environment.getEditModel();
			MapControl mapControl = environment.getMapControl();

			// 获取基线
			if (editModel.baseLine == null) {
				initialBaseLine(mapControl, editModel);
			} else {
				Layer activeEditableLayer = mapControl.getActiveEditableLayer();

				if (activeEditableLayer != null && activeEditableLayer.getSelection() != null && activeEditableLayer.getSelection().getCount() > 0) {
					GeoLine desLine = getDesLine(activeEditableLayer, editModel);

					if (desLine != null) {
						trim(mapControl, (DatasetVector) activeEditableLayer.getDataset(), desLine, editModel);
					}
				} else {
					Application.getActiveApplication().getOutput().output(MapEditorProperties.getString("String_LineEditor_SelectExtendLine_NotEditable"));
				}
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
		LineTrimEditModel editModel;
		if (environment.getEditModel() instanceof LineTrimEditModel) {
			editModel = (LineTrimEditModel) environment.getEditModel();
		} else {
			editModel = new LineTrimEditModel();
			environment.setEditModel(editModel);
		}
		environment.setEditController(this.lineTrimEditController);

		editModel.oldMapControlAction = environment.getMapControl().getAction();
		editModel.oldTrackMode = environment.getMapControl().getTrackMode();
		environment.getMapControl().setAction(MAP_CONTROL_ACTION);
		environment.getMapControl().setTrackMode(TrackMode.TRACK);
		editModel.tip.bind(environment.getMapControl());
	}

	@Override
	public void deactivate(EditEnvironment environment) {
		if (environment.getEditModel() instanceof LineTrimEditModel) {
			LineTrimEditModel editModel = (LineTrimEditModel) environment.getEditModel();

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
		return environment.getEditProperties().getEditableDatasetTypes().contains(DatasetType.LINE)
				|| environment.getEditProperties().getEditableDatasetTypes().contains(DatasetType.CAD);
	}

	@Override
	public boolean check(EditEnvironment environment) {
		return environment.getEditor() instanceof LineTrimEditor;
	}

	private void trim(MapControl mapControl, DatasetVector dataset, GeoLine desLine, LineTrimEditModel editModel) {
		Recordset recordset = null;

		try {
			Point2Ds baseLinePoints = new Point2Ds();
			Point2Ds desLinePoints = new Point2Ds();

			for (int i = 0; i < editModel.baseLine.getPartCount(); i++) {
				baseLinePoints.addRange(editModel.baseLine.getPart(i).toArray());
			}

			for (int i = 0; i < desLine.getPartCount(); i++) {
				desLinePoints.addRange(desLine.getPart(i).toArray());
			}

			// 检验要进行操作的线是否合法
			ArrayList<IntersectPoint> intersectPoints = new ArrayList<IntersectPoint>();

			if (!trimValid(desLine, baseLinePoints, desLinePoints, intersectPoints)) {
				desLine.dispose();
				return;
			}

			// 不用考虑捕捉点
			Point2D pnt2D = mapControl.getMap().pixelToMap(mapControl.getMousePosition(true));

			if (dataset.getTolerance().getNodeSnap() == 0) {
				dataset.getTolerance().setDefault();
			}

			// 根据垂足所在的段的索引，获取交点索引
			Object[] result = EditorUtilities.getMinDistance(pnt2D, desLinePoints, dataset.getTolerance().getNodeSnap());
			Integer segment = (Integer) result[2];
			int key = -1;

			for (int i = 0; i < intersectPoints.size(); i++) {
				if (segment >= intersectPoints.get(i).getSegment() && segment < intersectPoints.get(i + 1).getSegment()) {
					key = i;
					break;
				} else if (segment == intersectPoints.get(i + 1).getSegment()) {
					key = i + 1;
					break;
				}
			}

			List<GeoLine> geoLines = new ArrayList<GeoLine>();
			if (dataset.getTolerance().getNodeSnap() == 0) {
				dataset.getTolerance().setDefault();
			}

			if (key == 0) {
				GeoPoint geoSplitPoint = new GeoPoint(desLinePoints.getItem(intersectPoints.get(key + 1).getSegment()));
				GeoLine[] geoLines1 = Geometrist.splitLine(desLine, geoSplitPoint, dataset.getTolerance().getNodeSnap());

				if (geoLines1.length >= 2) {
					geoLines.add(geoLines1[1]);
				}
				geoSplitPoint.dispose();
			} else if (key == intersectPoints.size() - 2) {
				GeoPoint geoSplitPoint = new GeoPoint(desLinePoints.getItem(intersectPoints.get(key).getSegment()));
				GeoLine[] geoLines1 = Geometrist.splitLine(desLine, geoSplitPoint, dataset.getTolerance().getNodeSnap());

				if (geoLines1.length >= 2) {
					geoLines.add(geoLines1[0]);
				}
				geoSplitPoint.dispose();
			} else {
				GeoPoint geoSplitPoint = new GeoPoint(desLinePoints.getItem(intersectPoints.get(key).getSegment()));
				GeoLine[] geoLines1 = Geometrist.splitLine(desLine, geoSplitPoint, dataset.getTolerance().getNodeSnap());
				geoSplitPoint.dispose();

				if (geoLines1.length >= 2) {
					geoSplitPoint = new GeoPoint(desLinePoints.getItem(intersectPoints.get(key + 1).getSegment()));
					GeoLine[] geoLines2 = Geometrist.splitLine(geoLines1[1], geoSplitPoint, dataset.getTolerance().getNodeSnap());
					if (geoLines2.length >= 2) {
						geoLines.add(geoLines1[0]);
						geoLines.add(geoLines2[1]);
					}
					geoSplitPoint.dispose();
				}
			}

			if (geoLines.size() >= 1) {
				GeoStyle style = desLine.getStyle();
				recordset = dataset.getRecordset(false, CursorType.DYNAMIC);

				mapControl.getEditHistory().batchBegin();
				// 删除原来的线对象
				recordset.moveFirst();
				recordset.seekID(desLine.getID());

				HashMap<String, Object> values = new HashMap<String, Object>();
				FieldInfos fieldInfos = recordset.getFieldInfos();
				Object[] fieldValues = recordset.getValues();
				for (int i = 0; i < fieldValues.length; i++) {
					if (!fieldInfos.get(i).isSystemField()) {
						values.put(fieldInfos.get(i).getName(), fieldValues[i]);
					}
				}

				mapControl.getEditHistory().add(EditType.DELETE, recordset, true);
				recordset.delete();

				// 添加修剪打断后的线对象
				for (int i = 0; i < geoLines.size(); i++) {
					GeoLine geoLine = geoLines.get(i);
					geoLine.setStyle(style);
					recordset.addNew(geoLine, values);
					recordset.update();
					mapControl.getEditHistory().add(EditType.ADDNEW, recordset, true);
					geoLine.dispose();
				}
				mapControl.getEditHistory().batchEnd();

				fieldInfos.dispose();
				desLine.dispose();
				desLine = null;
				geoLines = null;
				mapControl.getMap().refresh();
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			if (recordset != null) {
				recordset.close();
				recordset.dispose();
			}
		}
	}

	boolean trimValid(GeoLine desLine, Point2Ds baseLinePoints, Point2Ds desLinePoints, ArrayList<IntersectPoint> intersectPoints) {
		boolean bResult = false;

		try {
			if (baseLinePoints != null && desLinePoints != null) {
				for (int desIndex = 0; desIndex < desLinePoints.getCount() - 1; desIndex++) {
					for (int baseIndex = 0; baseIndex < baseLinePoints.getCount() - 1; baseIndex++) {

						// 会出现起始点和终点重合的情况，这里做一下处理
						if (desLinePoints.getItem(desIndex).equals(desLinePoints.getItem(desIndex + 1))
								|| baseLinePoints.getItem(baseIndex).equals(baseLinePoints.getItem(baseIndex + 1))) {
							continue;
						}

						Point2D intersectionPoint = Geometrist.intersectLine(baseLinePoints.getItem(baseIndex), baseLinePoints.getItem(baseIndex + 1),
								desLinePoints.getItem(desIndex), desLinePoints.getItem(desIndex + 1), false);

						if (intersectionPoint != null && !intersectionPoint.isEmpty()) {
							double distance = calculateDistance(desLinePoints, intersectionPoint, desIndex);

							Boolean touchStart = Geometrist.hasTouch(new GeoPoint(desLinePoints.getItem(desIndex)), new GeoPoint(intersectionPoint));
							Boolean touchEnd = Geometrist.hasTouch(new GeoPoint(desLinePoints.getItem(desIndex + 1)), new GeoPoint(intersectionPoint));

							// 如果不与端点重合才添加，添加完成，之前的索引要重新排序
							if (!touchStart && !touchEnd) {
								IntersectPoint intersectPoint = new IntersectPoint(intersectionPoint, desIndex, distance, false);
								intersectPoints.add(intersectPoint);
							} else if (touchStart) {
								IntersectPoint intersectPoint = new IntersectPoint(intersectionPoint, desIndex, distance, true);
								intersectPoints.add(intersectPoint);
							} else if (touchEnd) {
								IntersectPoint intersectPoint = new IntersectPoint(intersectionPoint, desIndex + 1, distance, true);
								intersectPoints.add(intersectPoint);
							}
						}
					}
				}

				if (intersectPoints.size() > 0) {
					sortPointsByLine(desLinePoints, intersectPoints);
					// 添加起点和终点
					if (intersectPoints.get(0).getSegment() != 0) {
						IntersectPoint intersectPoint = new IntersectPoint(desLinePoints.getItem(0), 0, 0, true);
						intersectPoints.add(0, intersectPoint);
					}

					if (intersectPoints.get(intersectPoints.size() - 1).getSegment() != desLinePoints.getCount() - 1) {
						IntersectPoint intersectPoint = new IntersectPoint(desLinePoints.getItem(desLinePoints.getCount() - 1), desLinePoints.getCount() - 1,
								desLine.getLength(), true);
						intersectPoints.add(intersectPoint);
					}
				}

				if (intersectPoints.size() > 2) {
					bResult = true;
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return bResult;
	}

	private double calculateDistance(Point2Ds desLinePoints, Point2D endPoint, int segment) {
		double distance = 0.0;

		try {
			for (int i = 0; i < segment; i++) {
				distance += EditorUtilities.compouteTwoPointDistance(desLinePoints.getItem(i), desLinePoints.getItem(i + 1));
			}

			if (segment == 0) {
				distance += EditorUtilities.compouteTwoPointDistance(desLinePoints.getItem(0), endPoint);
			} else {
				distance += EditorUtilities.compouteTwoPointDistance(desLinePoints.getItem(segment - 1), endPoint);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return distance;
	}

	Point2Ds sortPointsByLine(Point2Ds desLinePoints, ArrayList<IntersectPoint> intersectPoints) {
		Point2Ds points = null;

		try {
			// 调整交点顺序
			for (int outer = intersectPoints.size(); outer > 0; outer--) {
				for (int inner = 0; inner < outer - 1; inner++) {
					if (intersectPoints.get(inner).getDistance() > intersectPoints.get(inner + 1).getDistance()) {
						IntersectPoint temp = intersectPoints.get(inner);
						intersectPoints.set(inner, intersectPoints.get(inner + 1));
						intersectPoints.set(inner + 1, temp);
					}
				}
			}

			IntersectPoint intersectPoint = intersectPoints.get(0);

			for (int i = 0; i < intersectPoints.size(); i++) {
				intersectPoint = intersectPoints.get(i);

				// 增加插入点和更新交点Segment
				if (!intersectPoint.isExistPoint()) {
					desLinePoints.insert(intersectPoint.getSegment() + 1, intersectPoint.getIntersectPoint2D());

					for (int j = i; j < intersectPoints.size(); j++) {
						intersectPoint = intersectPoints.get(j);
						intersectPoint.setSegment(intersectPoint.getSegment() + 1);
						intersectPoints.set(j, intersectPoint);
					}
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return points;
	}

	private void initialBaseLine(MapControl mapControl, LineTrimEditModel editModel) {
		MapUtilities.clearTrackingObjects(mapControl.getMap(), TAG_LINEEXTEND);
		getBaseLine(mapControl, editModel);

		if (editModel.baseLine != null) {
			editModel.baseLine.setStyle(getBaseLineStyle());
			mapControl.getMap().getTrackingLayer().add(editModel.baseLine, TAG_LINEEXTEND);
			mapControl.getMap().refreshTrackingLayer();
			editModel.labelTip.setText(MapEditorProperties.getString("String_LineEditor_SelectTrimLine"));
		}
	}

	private void getBaseLine(MapControl mapControl, LineTrimEditModel editModel) {
		try {
			List<Layer> layers = MapUtilities.getLayers(mapControl.getMap());

			for (Layer layer : layers) {

				if (layer.getSelection().getCount() > 0 && layer.getDataset() != null
						&& (layer.getDataset().getType() == DatasetType.LINE || layer.getDataset().getType() == DatasetType.CAD)) {
					Recordset recordset = layer.getSelection().toRecordset();

					if (recordset != null) {
						try {

							// 单选，取到一条线就可以撤了
							while (!recordset.isEOF() && editModel.baseLine == null) {
								IGeometry geometry = DGeometryFactory.create(recordset.getGeometry());

								if (geometry instanceof ILineFeature) {
									editModel.baseLine = ((ILineFeature) geometry).convertToLine(120);
								}

								recordset.moveNext();
							}
						} finally {
							if (recordset != null) {
								recordset.close();
								recordset.dispose();
							}
						}
					}

					if (editModel.baseLine != null) {
						editModel.baseLayer = layer;
						layer.getSelection().clear();
						break;
					}
				}
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	private GeoLine getDesLine(Layer activeEditableLayer, LineTrimEditModel editModel) {
		GeoLine desLine = null;
		Recordset recordset = null;

		try {
			// 获取目标线
			recordset = activeEditableLayer.getSelection().toRecordset();

			if (recordset != null) {
				while (!recordset.isEOF() && desLine == null) {
					Geometry geometry = recordset.getGeometry();

					if (geometry instanceof GeoLine) {
						desLine = (GeoLine) geometry;
					}

					recordset.moveNext();
				}
			}

			if (desLine == null) {
				Application.getActiveApplication().getOutput().output(MapEditorProperties.getString("String_LineEditor_SelectTrimLine_ERROR"));
			} else {

				// 如果选中的基线和目标线是同一个对象就什么都不做
				if (editModel.baseLayer == activeEditableLayer && desLine.getID() == editModel.baseLine.getID()) {
					Application.getActiveApplication().getOutput().output(MapEditorProperties.getString("String_LineEditor_SelectTrimLine_ShouldNotBaseLine"));
					desLine.dispose();
					desLine = null;
				} else if (desLine.getPartCount() > 1) {
					Application.getActiveApplication().getOutput().output(MapEditorProperties.getString("String_LineEditor_SelectTrimLine_ERROR"));
					desLine.dispose();
					desLine = null;
				}
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			if (recordset != null) {
				recordset.close();
				recordset.dispose();
			}
		}
		return desLine;
	}

	private GeoStyle getBaseLineStyle() {
		GeoStyle style = new GeoStyle();
		style.setLineWidth(0.6);
		style.setLineColor(Color.RED);
		return style;
	}

	/**
	 * 编辑结束清理资源
	 */
	private void clear(EditEnvironment environment) {
		if (environment.getEditModel() instanceof LineTrimEditModel) {
			((LineTrimEditModel) environment.getEditModel()).clear();
		}

		MapUtilities.clearTrackingObjects(environment.getMap(), TAG_LINEEXTEND);
	}

	private class IntersectPoint {

		private Point2D intersectPoint2D;
		private int segment;
		private double distance;
		private boolean isExistPoint;

		public IntersectPoint(Point2D intersectPoint2D, int segment, double distance, boolean isExistPoint) {
			this.intersectPoint2D = intersectPoint2D;
			this.segment = segment;
			this.distance = distance;
			this.isExistPoint = isExistPoint;
		}

		public Point2D getIntersectPoint2D() {
			return this.intersectPoint2D;
		}

		public int getSegment() {
			return this.segment;
		}

		public void setSegment(int segment) {
			this.segment = segment;
		}

		public double getDistance() {
			return this.distance;
		}

		public boolean isExistPoint() {
			return this.isExistPoint;
		}
	}

	private class LineTrimEditModel implements IEditModel {
		public GeoLine baseLine;
		public Layer baseLayer; // 基线所在图层

		public Action oldMapControlAction = Action.SELECT2;
		public TrackMode oldTrackMode = TrackMode.EDIT;

		public MapControlTip tip = new MapControlTip();
		public JLabel labelTip = new JLabel(MapEditorProperties.getString("String_LineEditor_SelectBaseLine"));

		public LineTrimEditModel() {
			this.tip.addLabel(this.labelTip);
		}

		public void clear() {
			if (this.baseLine != null) {
				this.baseLine.dispose();
				this.baseLine = null;
			}
		}
	}
}
