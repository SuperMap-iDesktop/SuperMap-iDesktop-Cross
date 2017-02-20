package com.supermap.desktop.geometryoperation.editor;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.geometryoperation.EditEnvironment;

import java.awt.*;
import java.util.Map;

/**
 * @author lixiaoyao
 */
public class RegionSplitByDrawing implements IDrawingSplit {
	private static final String Tag_GeometrySplit = "Tag_GeometrySplit";
	@Override
	public boolean SplitGeometry(EditEnvironment environment, Geometry geometry, Geometry splitGeometry, Map<Geometry, Map<String, Object>> resultGeometry, Map<String, Object> values, GeoStyle geoStyle, double tolerance)
	{
		boolean result = false;
		GeoRegion resultGeoRegion1 = new GeoRegion();
		GeoRegion resultGeoRegion2 = new GeoRegion();
		GeoRegion tempGeoRegion = (GeoRegion) geometry;

		GeoStyle style1 = new GeoStyle();
		style1.setLineColor(Color.RED);
		style1.setFillOpaqueRate(0);

		GeoStyle style2 = new GeoStyle();
		style2.setLineColor(Color.BLUE);
		style2.setFillOpaqueRate(0);

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
				resultGeoRegion1.setStyle(style1);
				environment.getMap().getTrackingLayer().add(resultGeoRegion1, Tag_GeometrySplit);
				resultGeoRegion2.setStyle(style2);
				environment.getMap().getTrackingLayer().add(resultGeoRegion2, Tag_GeometrySplit);
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
