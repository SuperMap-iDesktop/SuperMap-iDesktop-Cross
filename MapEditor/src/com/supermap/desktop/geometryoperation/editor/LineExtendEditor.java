package com.supermap.desktop.geometryoperation.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import com.supermap.data.CursorType;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.EditType;
import com.supermap.data.GeoLine;
import com.supermap.data.GeoPoint;
import com.supermap.data.GeoStyle;
import com.supermap.data.Geometrist;
import com.supermap.data.Geometry;
import com.supermap.data.Point2D;
import com.supermap.data.Point2Ds;
import com.supermap.data.Recordset;
import com.supermap.desktop.Application;
import com.supermap.desktop.geometry.Abstract.IGeometry;
import com.supermap.desktop.geometry.Abstract.ILineFeature;
import com.supermap.desktop.geometry.Implements.DGeometryFactory;
import com.supermap.desktop.geometryoperation.EditControllerAdapter;
import com.supermap.desktop.geometryoperation.EditEnvironment;
import com.supermap.desktop.geometryoperation.IEditController;
import com.supermap.desktop.geometryoperation.IEditModel;
import com.supermap.desktop.geometryoperation.NullEditController;
import com.supermap.desktop.geometryoperation.control.MapControlTip;
import com.supermap.desktop.mapeditor.MapEditorProperties;
import com.supermap.desktop.utilities.MapUtilities;
import com.supermap.mapping.Layer;
import com.supermap.ui.Action;
import com.supermap.ui.GeometrySelectedEvent;
import com.supermap.ui.MapControl;
import com.supermap.ui.TrackMode;

public class LineExtendEditor extends AbstractEditor {

	private static final String TAG_LINEEXTEND = "Tag_ExtendLineEditorBase"; // 基线 tracking
	private static final Action MAP_CONTROL_ACTION = Action.SELECT;

	private IEditController lineExtendEditController = new EditControllerAdapter() {

		@Override
		public void geometrySelected(EditEnvironment environment, GeometrySelectedEvent arg0) {
			if (!(environment.getEditModel() instanceof LineExtendEditModel)) {
				return;
			}

			LineExtendEditModel editModel = (LineExtendEditModel) environment.getEditModel();
			MapControl mapControl = environment.getMapControl();

			// 获取基线
			if (editModel.baseLine == null) {
				initialBaseLine(mapControl, editModel);
			} else {
				Layer activeEditableLayer = mapControl.getActiveEditableLayer();

				if (activeEditableLayer != null && activeEditableLayer.getSelection() != null && activeEditableLayer.getSelection().getCount() > 0) {
					GeoLine desLine = getDesLine(activeEditableLayer, editModel);

					if (desLine != null) {
						extend(mapControl, (DatasetVector) activeEditableLayer.getDataset(), desLine, editModel);
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

	public LineExtendEditor() {
		super();
	}

	@Override
	public void activate(EditEnvironment environment) {
		LineExtendEditModel editModel;
		if (environment.getEditModel() instanceof LineExtendEditModel) {
			editModel = (LineExtendEditModel) environment.getEditModel();
		} else {
			editModel = new LineExtendEditModel();
			environment.setEditModel(editModel);
		}
		environment.setEditController(this.lineExtendEditController);

		editModel.oldMapControlAction = environment.getMapControl().getAction();
		editModel.oldTrackMode = environment.getMapControl().getTrackMode();
		environment.getMapControl().setAction(MAP_CONTROL_ACTION);
		environment.getMapControl().setTrackMode(TrackMode.TRACK);
		editModel.tip.bind(environment.getMapControl());
	}

	@Override
	public void deactivate(EditEnvironment environment) {
		if (environment.getEditModel() instanceof LineExtendEditModel) {
			LineExtendEditModel editModel = (LineExtendEditModel) environment.getEditModel();

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
		return environment.getEditor() instanceof LineExtendEditor;
	}

	private GeoStyle getBaseLineStyle() {
		GeoStyle style = new GeoStyle();
		style.setLineWidth(0.6);
		style.setLineColor(Color.RED);
		return style;
	}

	private void getBaseLine(MapControl mapControl, LineExtendEditModel editModel) {
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

			if (editModel.baseLine == null) {
				Application.getActiveApplication().getOutput().output(MapEditorProperties.getString("String_LineEditor_SelectExtendLine_BaseLineError"));
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	private GeoLine getDesLine(Layer activeEditableLayer, LineExtendEditModel editModel) {
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
				Application.getActiveApplication().getOutput().output(MapEditorProperties.getString("String_LineEditor_SelectExtendLine_ERROR"));
			} else {

				// 如果选中的基线和目标线是同一个对象就什么都不做
				if (editModel.baseLayer == activeEditableLayer && desLine.getID() == editModel.baseLine.getID()) {
					Application.getActiveApplication().getOutput()
							.output(MapEditorProperties.getString("String_LineEditor_SelectExtendLine_ShouldNotBaseLine"));
					desLine.dispose();
					desLine = null;
				} else if (desLine.getPartCount() > 1) {
					Application.getActiveApplication().getOutput().output(MapEditorProperties.getString("String_LineEditor_SelectExtendLine_ERROR"));
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

	private void initialBaseLine(MapControl mapControl, LineExtendEditModel editModel) {
		MapUtilities.clearTrackingObjects(mapControl.getMap(), TAG_LINEEXTEND);
		getBaseLine(mapControl, editModel);

		if (editModel.baseLine != null) {
			editModel.baseLine.setStyle(getBaseLineStyle());
			mapControl.getMap().getTrackingLayer().add(editModel.baseLine, TAG_LINEEXTEND);
			mapControl.getMap().refreshTrackingLayer();
			editModel.labelTip.setText(MapEditorProperties.getString("String_LineEditor_SelectExtendLine"));
		}
	}

	// 采用的思路为：将目标线与基线的所有交点都插入到目标线中，构造一个新的线。
	// 取得包含选中点的那段线段的两个端点，以头和第一交点构造一段线，或尾与第二
	// 交点构造一段线，或以上两段线都构造。将构造好的线加入，然后删除原来的目标线。
	private void extend(MapControl mapControl, DatasetVector dataset, GeoLine desLine, LineExtendEditModel editModel) {
		Recordset recordset = null;

		try {
			Point2Ds baseLinePoints = new Point2Ds();
			Point2Ds desLinePoints = new Point2Ds();
			int nBaseLinePntCount = 0;
			for (int i = 0; i < editModel.baseLine.getPartCount(); i++) {
				baseLinePoints.addRange(editModel.baseLine.getPart(i).toArray());
				nBaseLinePntCount += editModel.baseLine.getPart(i).getCount();
			}
			int nDesLinePntCount = 0;
			for (int i = 0; i < desLine.getPartCount(); i++) {
				desLinePoints.addRange(desLine.getPart(i).toArray());
				nDesLinePntCount += desLine.getPart(i).getCount();
			}

			// 如果目标线是一段封闭的线，则不理
			GeoPoint pointStart = new GeoPoint(desLinePoints.getItem(0));
			GeoPoint pointEnd = new GeoPoint(desLinePoints.getItem(nDesLinePntCount - 1));
			if (Geometrist.isIdentical(pointStart, pointEnd)) {
				Application.getActiveApplication().getOutput().output(MapEditorProperties.getString("String_LineEditor_SelectExtendLine_Closed"));
				desLine.dispose();
				desLine = null;

				pointStart.dispose();
				pointEnd.dispose();
				return;
			}
			pointStart.dispose();
			pointEnd.dispose();

			Point2D pnt2D = mapControl.getMap().pixelToMap(mapControl.getMousePosition(true));

			if (dataset.getTolerance().getNodeSnap() == 0) {
				dataset.getTolerance().setDefault();
			}
			Object[] result = EditorUtilties.getMinDistance(pnt2D, desLinePoints, dataset.getTolerance().getNodeSnap());
			Point2D perpendicularFoot = (Point2D) result[1];
			Integer segment = (Integer) result[2];

			// 记录是延伸目标线段的前半段的第一段还是后半段的最后一段
			boolean bExtendFirstPart = true;
			double dForeLength = 0.0;
			for (int i = 0; i < segment; i++) {
				dForeLength += EditorUtilties.compouteTwoPointDistance(desLinePoints.getItem(i), desLinePoints.getItem(i + 1));
			}
			dForeLength += EditorUtilties.compouteTwoPointDistance(desLinePoints.getItem(segment), perpendicularFoot);
			double dTotalLength = desLine.getLength();
			if ((dForeLength >= (dTotalLength / 2.0))) {
				// 记录后半段的最后一段线段的两个端点点号
				bExtendFirstPart = false;
			}

			// 求出目标线和基线的所有交点
			Point2Ds pntIntersections = new Point2Ds();
			Point2D pntIntersection = Point2D.getEMPTY();

			int nStartPntNumber = 0;
			int nEndPntNumber = 1;
			if (!bExtendFirstPart) {
				nStartPntNumber = nDesLinePntCount - 2;
				nEndPntNumber = nDesLinePntCount - 1;
			}

			for (int i = 0; i < nBaseLinePntCount - 1; i++) {
				if (baseLinePoints.getItem(i).equals(baseLinePoints.getItem(i + 1))) // 自由曲线、B样条等，有很多的重复节点
					continue;
				pntIntersection = Geometrist.intersectLine(baseLinePoints.getItem(i), baseLinePoints.getItem(i + 1), desLinePoints.getItem(nStartPntNumber),
						desLinePoints.getItem(nEndPntNumber), true);
				if (pntIntersection != Point2D.getEMPTY()) {
					// 交点在基线的延长线上，不作为可用点
					boolean bValid = EditorUtilties.isPointInLineRect(pntIntersection, baseLinePoints.getItem(i), baseLinePoints.getItem(i + 1), dataset
							.getTolerance().getNodeSnap());
					// 交点是所要延伸的线上的点，不作为可用交点
					if (bValid
							&& !Geometrist.isPointOnLine(pntIntersection, desLinePoints.getItem(nStartPntNumber), desLinePoints.getItem(nEndPntNumber), false)) {
						pntIntersections.add(pntIntersection);
					}
				}
			}

			// 无交点，不理
			if (pntIntersections.getCount() == 0) {
				Application.getActiveApplication().getOutput().output(MapEditorProperties.getString("String_LineEditor_SelectExtendLine_NoIntersection"));
				desLine.dispose();
				desLine = null;
				return;
			}

			// 找到最合适的交点,因为可能有多个交点，但只有离合法端点最近的一个才是所求的
			double dDistance = 99999999999999999999.0;
			int nKey = -1;
			if (bExtendFirstPart) {

				// 延伸第一段线段
				double dTemp1 = 0.0;
				double dTemp2 = 0.0;
				for (int i = 0; i < pntIntersections.getCount(); i++) {
					dTemp1 = EditorUtilties.compouteTwoPointDistance(desLinePoints.getItem(nStartPntNumber), pntIntersections.getItem(i));
					dTemp2 = EditorUtilties.compouteTwoPointDistance(desLinePoints.getItem(nEndPntNumber), pntIntersections.getItem(i));
					if (dTemp1 < dTemp2 && dDistance > dTemp1) {
						dDistance = dTemp1;
						nKey = i;
					}
				}
				if (nKey != -1) {
					desLinePoints.setItem(nStartPntNumber, pntIntersections.getItem(nKey));
				}
			} else {

				// 延伸最后一段线段
				double dTemp1 = 0.0;
				double dTemp2 = 0.0;
				for (int i = 0; i < pntIntersections.getCount(); i++) {
					dTemp1 = EditorUtilties.compouteTwoPointDistance(desLinePoints.getItem(nStartPntNumber), pntIntersections.getItem(i));
					dTemp2 = EditorUtilties.compouteTwoPointDistance(desLinePoints.getItem(nEndPntNumber), pntIntersections.getItem(i));
					if (dTemp1 > dTemp2 && dDistance > dTemp2) {
						dDistance = dTemp1;
						nKey = i;
					}
				}
				if (nKey != -1) {
					desLinePoints.setItem(nEndPntNumber, pntIntersections.getItem(nKey));
				}
			}

			if (nKey != -1) {
				// 延伸线对象
				GeoLine newLine = new GeoLine(desLinePoints);
				newLine.setStyle(desLine.getStyle());
				recordset = dataset.getRecordset(false, CursorType.DYNAMIC);
				recordset.moveFirst();
				recordset.seekID(desLine.getID());
				mapControl.getEditHistory().add(EditType.MODIFY, recordset, true);
				recordset.edit();
				recordset.setGeometry(newLine);
				recordset.update();
				newLine.dispose();
				mapControl.getMap().refresh();
				mapControl.revalidate();
			}

			desLine.dispose();
			desLine = null;
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		if (recordset != null) {
			recordset.close();
			recordset.dispose();
		}
	}

	/**
	 * 编辑结束清理资源
	 */
	private void clear(EditEnvironment environment) {
		if (environment.getEditModel() instanceof LineExtendEditModel) {
			((LineExtendEditModel) environment.getEditModel()).clear();
		}

		MapUtilities.clearTrackingObjects(environment.getMap(), TAG_LINEEXTEND);
	}

	private class LineExtendEditModel implements IEditModel {
		public GeoLine baseLine;
		public Layer baseLayer; // 基线所在图层

		public Action oldMapControlAction = Action.SELECT2;
		public TrackMode oldTrackMode = TrackMode.EDIT;

		public MapControlTip tip = new MapControlTip();
		public JLabel labelTip = new JLabel(MapEditorProperties.getString("String_LineEditor_SelectBaseLine"));

		public LineExtendEditModel() {
			this.tip.getContentPanel().setLayout(new BorderLayout());
			this.tip.getContentPanel().add(this.labelTip, BorderLayout.CENTER);
			this.tip.getContentPanel().setSize(200, 20);
			this.tip.getContentPanel().setBackground(new Color(255, 255, 255, 150));
		}

		public void clear() {
			if (this.baseLine != null) {
				this.baseLine.dispose();
				this.baseLine = null;
			}
		}
	}
}
