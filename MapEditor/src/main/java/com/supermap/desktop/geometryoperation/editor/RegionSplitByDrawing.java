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
public class RegionSplitByDrawing implements IDrawingSplit {

	@Override
	public boolean SplitGeometry(Geometry geometry, Geometry splitGeometry, Map<Geometry, Map<String, Object>> resultGeometry, Map<String, Object> values, GeoStyle geoStyle, double tolerance)
	{
		boolean result = false;
		GeoRegion resultGeoRegion1 = new GeoRegion();
		GeoRegion resultGeoRegion2 = new GeoRegion();
		GeoRegion tempGeoRegion = (GeoRegion) geometry;

		boolean resultSplit=false;
		try {
			if (splitGeometry.getType()== GeometryType.GEOLINE) {
				resultSplit = Geometrist.splitRegion(tempGeoRegion, (GeoLine)splitGeometry, resultGeoRegion1, resultGeoRegion2);
			}
			else if (splitGeometry.getType()==GeometryType.GEOREGION){
				resultSplit = Geometrist.splitRegion(tempGeoRegion, (GeoRegion)splitGeometry, resultGeoRegion1, resultGeoRegion2);
			}
			if (resultSplit) {
				result = true;
				if (geoStyle != null) {
					resultGeoRegion1.setStyle(geoStyle.clone());//设置风格
					resultGeoRegion2.setStyle(geoStyle.clone());
				}
				resultGeometry.put(resultGeoRegion1, values);
				resultGeometry.put(resultGeoRegion2, values);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex.toString());
		}
		return result;
	}
}
