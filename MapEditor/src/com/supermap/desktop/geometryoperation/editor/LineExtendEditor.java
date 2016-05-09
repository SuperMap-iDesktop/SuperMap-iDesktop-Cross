package com.supermap.desktop.geometryoperation.editor;

import java.awt.Color;
import java.awt.Point;
import java.util.List;

import com.supermap.data.CursorType;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.EditType;
import com.supermap.data.GeoLine;
import com.supermap.data.GeoPoint;
import com.supermap.data.GeoStyle;
import com.supermap.data.Geometrist;
import com.supermap.data.Point2D;
import com.supermap.data.Point2Ds;
import com.supermap.data.Recordset;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.geometryoperation.EditEnvironment;
import com.supermap.desktop.mapeditor.MapEditorProperties;
import com.supermap.desktop.utilties.MapControlUtilties;
import com.supermap.desktop.utilties.MapUtilties;
import com.supermap.mapping.Layer;
import com.supermap.mapping.Selection;

public class LineExtendEditor extends AbstractEditor {

	private static final String TAG_LINEEXTEND = "Tag_ExtendLineEditorBase";

	private GeoLine m_baseLine;
	private GeoLine m_desLine;
	private DatasetVector m_desDataset;

	@Override
	public void activate(EditEnvironment environment) {
		// 按需重写
	}

	@Override
	public void deactivate(EditEnvironment environment) {
		// 按需重写
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

	// 延伸算法
	private void extend(IFormMap formMap, Point point) {
		Recordset recordset = null;
		try {
			if (this.m_baseLine == null) {
				MapControlUtilties.clearTrackingObjects(formMap.getMapControl(), TAG_LINEEXTEND);
				this.m_baseLine = updateBaseLine(formMap);
				return;
			}

			this.m_desLine = updateDesLine(formMap);
			if (this.m_desLine == null) {
				return;
			}

			Point2Ds baseLinePoints = new Point2Ds();
			Point2Ds desLinePoints = new Point2Ds();
			int nBaseLinePntCount = 0;
			for (int i = 0; i < this.m_baseLine.getPartCount(); i++) {
				baseLinePoints.addRange(this.m_baseLine.getPart(i).toArray());
				nBaseLinePntCount += this.m_baseLine.getPart(i).getCount();
			}
			int nDesLinePntCount = 0;
			for (int i = 0; i < this.m_desLine.getPartCount(); i++) {
				desLinePoints.addRange(this.m_desLine.getPart(i).toArray());
				nDesLinePntCount += this.m_desLine.getPart(i).getCount();
			}

			// 如果目标线是一段封闭的线，则不理
			GeoPoint pointStart = new GeoPoint(desLinePoints.getItem(0));
			GeoPoint pointEnd = new GeoPoint(desLinePoints.getItem(nDesLinePntCount - 1));
			if (Geometrist.isIdentical(pointStart, pointEnd)) {
				m_desLine.dispose();
				m_desLine = null;

				pointStart.dispose();
				pointEnd.dispose();
				return;
			}
			pointStart.dispose();
			pointEnd.dispose();

			Point2D pnt2D = formMap.getMapControl().getMap().pixelToMap(point);
			Point2D perpendicularFoot = Point2D.getEMPTY();
			int segment = -1;

			if (this.m_desDataset.getTolerance().getNodeSnap() == 0) {
				this.m_desDataset.getTolerance().setDefault();
			}
			EditorUtilties.getMinDistance(pnt2D, desLinePoints, this.m_desDataset.getTolerance().getNodeSnap(), perpendicularFoot, segment);

			// 记录是延伸目标线段的前半段的第一段还是后半段的最后一段
			boolean bExtendFirstPart = true;
			double dForeLength = 0.0;
			for (int i = 0; i < segment; i++) {
				dForeLength += compouteTwoPointDistance(desLinePoints.getItem(i), desLinePoints.getItem(i + 1));
			}
			dForeLength += compouteTwoPointDistance(desLinePoints.getItem(segment), perpendicularFoot);
			double dTotalLength = m_desLine.getLength();
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
				if (baseLinePoints.getItem(i) == baseLinePoints.getItem(i + 1)) // 自由曲线时，有很多的重复节点
					continue;
				pntIntersection = Geometrist.intersectLine(baseLinePoints.getItem(i), baseLinePoints.getItem(i + 1), desLinePoints.getItem(nStartPntNumber),
						desLinePoints.getItem(nEndPntNumber), true);
				if (pntIntersection != Point2D.getEMPTY()) {
					// 交点在基线的延长线上，不作为可用点
					boolean bValid = EditorUtilties.isPointInLineRect(pntIntersection, baseLinePoints.getItem(i), baseLinePoints.getItem(i + 1), m_desDataset
							.getTolerance().getNodeSnap());
					// 交点是所要延伸的线上的点，不作为可用交点
					if (bValid
							&& !Geometrist.isPointOnLine(pntIntersection, desLinePoints.getItem(nStartPntNumber), desLinePoints.getItem(nEndPntNumber), false)) {
						pntIntersections.add(pntIntersection);
					}
				}
			}
			if (pntIntersections.getCount() == 0)// 无交点，不理
			{
				this.m_desLine.dispose();
				this.m_desLine = null;
				return;
			}
			// 找到最合适的交点,因为可能有多个交点，但只有离合法端点最近的一个才是所求的
			double dDistance = 99999999999999999999.0;
			int nKey = -1;
			if (bExtendFirstPart) // 延伸第一段线段
			{
				double dTemp1 = 0.0;
				double dTemp2 = 0.0;
				for (int i = 0; i < pntIntersections.getCount(); i++) {
					dTemp1 = compouteTwoPointDistance(desLinePoints.getItem(nStartPntNumber), pntIntersections.getItem(i));
					dTemp2 = compouteTwoPointDistance(desLinePoints.getItem(nEndPntNumber), pntIntersections.getItem(i));
					if (dTemp1 < dTemp2 && dDistance > dTemp1) {
						dDistance = dTemp1;
						nKey = i;
					}
				}
				if (nKey != -1) {
					desLinePoints.setItem(nStartPntNumber, pntIntersections.getItem(nKey));
				}
			} else // 延伸最后一段线段
			{
				double dTemp1 = 0.0;
				double dTemp2 = 0.0;
				for (int i = 0; i < pntIntersections.getCount(); i++) {
					dTemp1 = compouteTwoPointDistance(desLinePoints.getItem(nStartPntNumber), pntIntersections.getItem(i));
					dTemp2 = compouteTwoPointDistance(desLinePoints.getItem(nEndPntNumber), pntIntersections.getItem(i));
					if (dTemp1 > dTemp2 && dDistance > dTemp2) {
						dDistance = dTemp1;
						nKey = i;
					}
				}
				if (nKey != -1) {
					desLinePoints.setItem(nEndPntNumber, pntIntersections.getItem(nKey));
				}
			}

			// ////////////////////////////////////////////////////////////////////////
			if (nKey != -1) {
				// 延伸线对象
				GeoLine newLine = new GeoLine(desLinePoints);
				recordset = this.m_desDataset.getRecordset(false, CursorType.DYNAMIC);
				recordset.moveFirst();
				recordset.seekID(this.m_desLine.getID());
				formMap.getMapControl().getEditHistory().add(EditType.MODIFY, recordset, true);
				recordset.edit();
				recordset.setGeometry(newLine);
				recordset.update();
				newLine.dispose();
				formMap.getMapControl().getMap().refresh();
			}

			m_desLine.dispose();
			m_desLine = null;
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		if (recordset != null) {
			recordset.close();
			recordset.dispose();
		}
	}

	private double compouteTwoPointDistance(Point2D pntStart, Point2D pntEnd) {
		double dDistance = -1;
		double dOffsetX = pntStart.getX() - pntEnd.getX();
		double dOffsetY = pntStart.getY() - pntEnd.getY();
		dDistance = Math.sqrt(dOffsetX * dOffsetX + dOffsetY * dOffsetY);
		return dDistance;
	}

	// 采用的思路为：将目标线与基线的所有交点都插入到目标线中，构造一个新的线。
	// 取得包含选中点的那段线段的两个端点，以头和第一交点构造一段线，或尾与第二
	// 交点构造一段线，或以上两段线都构造。将构造好的线加入，然后删除原来的目标线。
	GeoLine updateBaseLine(IFormMap formMap) {
		GeoLine baseLine = null;
		Recordset recordset = null;
		try {
			List<Layer> layers = MapUtilties.getLayers(formMap.getMapControl().getMap());
			for (Layer layer : layers) {
				if (layer.getSelection().getCount() > 0 && layer.getDataset() != null
						&& (layer.getDataset().getType() == DatasetType.LINE || layer.getDataset().getType() == DatasetType.CAD)) {
					recordset = layer.getSelection().toRecordset();

					if (recordset != null) {
						while (!recordset.isEOF() && baseLine == null) {
							baseLine = (GeoLine) recordset.getGeometry();
							layer.getSelection().clear();
							recordset.moveNext();
						}
						recordset.dispose();
					}

					if (baseLine != null) {
						break;
					}
				}
			}

			if (baseLine != null) {
				GeoStyle style = new GeoStyle();
				style.setLineWidth(0.6);
				style.setLineColor(Color.RED);
				baseLine.setStyle(style);
				formMap.getMapControl().getMap().getTrackingLayer().add(baseLine, TAG_LINEEXTEND);
				formMap.getMapControl().getMap().refreshTrackingLayer();
				// UserTip.Text = Properties.MapEditorResources.String_LineEditor_SelectExtendLine;
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			if (recordset != null) {
				recordset.close();
				recordset.dispose();
			}
		}

		return baseLine;
	}

	private GeoLine updateDesLine(IFormMap formMap) {
		GeoLine desLine = null;
		Recordset recordset = null;
		try {
			// 获取目标线
			Selection[] selection = formMap.getMapControl().getMap().findSelection(true);
			recordset = formMap.getMapControl().getActiveEditableLayer().getSelection().toRecordset();
			if (recordset != null) {
				this.m_desDataset = recordset.getDataset();
				while (!recordset.isEOF() && desLine == null) {
					desLine = (GeoLine) recordset.getGeometry();
					recordset.moveNext();
				}
			}

			if (desLine != null && desLine.getID() != m_baseLine.getID()) {
				if (desLine.getPartCount() > 1) {
					Application.getActiveApplication().getOutput().output(MapEditorProperties.getString("String_LineEditor_SelectExtendLine_ERROR"));
					if (desLine != null) {
						desLine.dispose();
					}
				}
			} else {
				if (desLine != null) {
					desLine.dispose();
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
}
