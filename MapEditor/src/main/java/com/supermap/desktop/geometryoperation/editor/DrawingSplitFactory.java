package com.supermap.desktop.geometryoperation.editor;

import com.supermap.data.Geometry;
import com.supermap.data.GeometryType;

/**
 * @author lixiaoyao
 */
public class DrawingSplitFactory {
	public static IDrawingSplit getGeometry(Geometry geometry){
		IDrawingSplit iDrawingSplit=null;
		if (geometry.getType()== GeometryType.GEOLINE){
			iDrawingSplit= new LineSplitByDrawing();
		}else if (geometry.getType()==GeometryType.GEOREGION){
			iDrawingSplit= new RegionSplitByDrawing();
		}
		return iDrawingSplit;
	}
}
