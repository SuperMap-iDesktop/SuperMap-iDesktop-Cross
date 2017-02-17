package com.supermap.desktop.geometryoperation.editor;

import com.supermap.data.GeoLine;
import com.supermap.data.GeoRegion;
import com.supermap.data.GeoStyle;
import com.supermap.data.Geometrist;
import com.supermap.data.Geometry;
import com.supermap.data.GeometryType;
import com.supermap.desktop.Application;
import com.supermap.desktop.geometryoperation.EditEnvironment;

import java.awt.*;
import java.util.Map;

/**
 * @author lixiaoyao
 */
public class LineSplitByDrawing implements IDrawingSplit {
    private static final String Tag_GeometrySplit = "Tag_GeometrySplit";
    @Override
    public boolean SplitGeometry(EditEnvironment environment, Geometry geometry, Geometry splitGeometry, Map<Geometry, Map<String, Object>> resultGeometry, Map<String, Object> values, GeoStyle geoStyle, double tolerance) {
        boolean result = false;
        GeoLine tempGeoLine = (GeoLine) geometry;
        GeoLine resultLines[] = null;

        GeoStyle style1 = new GeoStyle();
        style1.setLineWidth(0.6);
        style1.setLineColor(Color.RED);

        GeoStyle style2 = new GeoStyle();
        style2.setLineWidth(0.6);
        style2.setLineColor(Color.BLUE);

        try {
            if (splitGeometry.getType() == GeometryType.GEOLINE) {
                resultLines = Geometrist.splitLine(tempGeoLine, (GeoLine) splitGeometry, tolerance);
            } else if (splitGeometry.getType() == GeometryType.GEOREGION) {
                resultLines = Geometrist.splitLine(tempGeoLine, (GeoRegion) splitGeometry, tolerance);
            }
            if (resultLines != null && resultLines.length >= 2) {
                result = true;
                for (int i = 0; i < resultLines.length; i++) {
                    if (i % 2 == 0) {
                        resultLines[i].setStyle(style1);
                    } else {
                        resultLines[i].setStyle(style2);
                    }
                    environment.getMap().getTrackingLayer().add(resultLines[i], Tag_GeometrySplit);

                    if (resultLines[i] != null) {
                        if (geoStyle != null) {
                            resultLines[i].setStyle(geoStyle.clone());//设置风格
                        }
                        resultGeometry.put(resultLines[i], values);
                    }
                }

            }
        } catch (Exception ex) {
            Application.getActiveApplication().getOutput().output(ex.toString());
        }
        return result;
    }
}
