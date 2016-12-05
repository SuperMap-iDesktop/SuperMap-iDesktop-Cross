package com.supermap.desktop.geometryoperation.editor;

import com.supermap.data.GeoStyle;
import com.supermap.data.Geometry;

import java.util.Map;

/**
 * @author lixiaoyao
 */
public interface IDrawingSplit {
	public boolean SplitGeometry(Geometry geometry, Geometry splitGeometry, Map<Geometry, Map<String, Object>> resultGeometry, Map<String, Object> values, GeoStyle geoStyle, double tolerance);
}
