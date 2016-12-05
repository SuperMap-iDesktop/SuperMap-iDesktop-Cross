package com.supermap.desktop.geometryoperation.editor;

import com.supermap.data.GeoLine;
import com.supermap.data.GeoRegion;
import com.supermap.data.GeoStyle;
import com.supermap.data.Geometrist;
import com.supermap.data.Geometry;
import com.supermap.data.GeometryType;
import com.supermap.desktop.Application;

import java.util.Map;

/**
 * @author lixiaoyao
 */
public class LineSplitByDrawing implements IDrawingSplit {
	@Override
	public boolean SplitGeometry(Geometry geometry, Geometry splitGeometry, Map<Geometry, Map<String, Object>> resultGeometry, Map<String, Object> values, GeoStyle geoStyle, double tolerance) {
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
				for (GeoLine resultGeoLine : resultLines) {
					if (resultGeoLine != null) {
						if (geoStyle != null) {
							resultGeoLine.setStyle(geoStyle.clone());//设置风格
						}
						resultGeometry.put(resultGeoLine, values);
					}
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex.toString());
		}
		return result;
	}
}
