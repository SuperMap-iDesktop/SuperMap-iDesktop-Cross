package com.supermap.desktop.geometryoperation.editor;

import com.supermap.data.GeoLine;
import com.supermap.data.Point2D;
import com.supermap.data.Point2Ds;
import com.supermap.desktop.mapeditor.MapEditorProperties;
import com.supermap.desktop.utilities.ListUtilities;

import java.util.ArrayList;

/**
 * 首尾连接线
 *
 * @author highsad
 */
public class JointLineDirectionEditor extends JointLineEditorBase {
    @Override
    protected String getTitle() {
        return MapEditorProperties.getString("String_GeometryOperation_JointLineDirection");
    }

    @Override
    protected GeoLine jointTwoLines(GeoLine baseLine, GeoLine targetLine, Boolean isOriginalBaseLine) {
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

                targetPoints = targetLine.getPart(0);
                basePoints = addTargetPoint(basePoints, targetPoints);
                resultLine = new GeoLine(basePoints);
            }
        }

        return resultLine;
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
        ListUtilities.addArray(points, basePoints.toArray());
        Point2Ds targetPointNew = new Point2Ds(targetPoints.toArray());

        if (points.get(points.size() - 1).equals(targetPointNew.getItem(0))) {
            targetPointNew.remove(0);
        }

        ListUtilities.addArray(points, targetPointNew.toArray());
        return new Point2Ds(points.toArray(new Point2D[points.size()]));
    }
}
