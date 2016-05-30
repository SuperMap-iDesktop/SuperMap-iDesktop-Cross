package com.supermap.desktop.mapview.geometry.property.geometryNode.parameterizationModels;

import com.supermap.data.GeoEllipticArc;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.geometry.Abstract.IGeometry;

/**
 * @author XiaJT
 */
public class TableModelEllipticArc extends GeometryParameterModel {
	private GeoEllipticArc geometry;
	private static final String[] rows = new String[]{
			ControlsProperties.getString("String_GeoEllipse_Center"),
			"  X",
			"  Y",
			ControlsProperties.getString("String_Geometry_Rotation"),
			ControlsProperties.getString("String_Geometry_SemimajorAxis"),
			ControlsProperties.getString("String_Geometry_SemiminorAxis"),
			ControlsProperties.getString("String_GeoCircle_StartAngle"),
			ControlsProperties.getString("String_GeoCircle_SweepAngle")
	};

	public TableModelEllipticArc(IGeometry iGeometry) {
		geometry = ((GeoEllipticArc) iGeometry.getGeometry());
	}

	@Override
	public Object getValue(int row, int column) {
		if (column == 0) {
			return rows[row];
		} else {
			switch (row) {
				case 0:
					return getValueAtRow0(row, column, true);
				case 1:
					return geometry.getCenter().getX();
				case 2:
					return geometry.getCenter().getY();
				case 3:
					return geometry.getRotation();
				case 4:
					return geometry.getSemimajorAxis();
				case 5:
					return geometry.getSemiminorAxis();
				case 6:
					return geometry.getStartAngle();
				case 7:
					return geometry.getSweepAngle();
			}
		}
		return 0;
	}

	@Override
	public Object getValueAtRow0(int row, int column, boolean isSelected) {
		return getCenterString(geometry.getCenter().getX(), geometry.getCenter().getY(), isSelected);
	}

	@Override
	public int getRowCount() {
		return rows.length;
	}
}
