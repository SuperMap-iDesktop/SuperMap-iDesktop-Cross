package com.supermap.desktop.geometryoperation.editor;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.geometryoperation.EditEnvironment;
import com.supermap.desktop.geometryoperation.RegionAndLineHighLightStyle;

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
                        resultLines[i].setStyle(RegionAndLineHighLightStyle.getLineStyleRed());
                    } else {
                        resultLines[i].setStyle(RegionAndLineHighLightStyle.getLineStyleBlue());
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
