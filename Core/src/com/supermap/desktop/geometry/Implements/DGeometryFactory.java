package com.supermap.desktop.geometry.Implements;

import com.supermap.data.DatasetType;
import com.supermap.data.GeoArc;
import com.supermap.data.GeoBSpline;
import com.supermap.data.GeoCardinal;
import com.supermap.data.GeoChord;
import com.supermap.data.GeoCircle;
import com.supermap.data.GeoCompound;
import com.supermap.data.GeoCurve;
import com.supermap.data.GeoEllipse;
import com.supermap.data.GeoEllipticArc;
import com.supermap.data.GeoLine;
import com.supermap.data.GeoLine3D;
import com.supermap.data.GeoLineM;
import com.supermap.data.GeoPie;
import com.supermap.data.GeoPoint;
import com.supermap.data.GeoPoint3D;
import com.supermap.data.GeoRectangle;
import com.supermap.data.GeoRegion;
import com.supermap.data.GeoRegion3D;
import com.supermap.data.GeoRoundRectangle;
import com.supermap.data.GeoText;
import com.supermap.data.Geometry;
import com.supermap.data.GeometryType;
import com.supermap.desktop.geometry.Abstract.IGeometry;

public class DGeometryFactory {
	public static IGeometry create(Geometry geometry) {
		if (geometry != null) {
			GeometryType type = geometry.getType();

			if (type == GeometryType.GEOTEXT) {
				return new DGeoText((GeoText) geometry);
			}
			if (type == GeometryType.GEOREGION) {
				return new DGeoRegion((GeoRegion) geometry);
			}
			if (type == GeometryType.GEORECTANGLE) {
				return new DGeoRectangle((GeoRectangle) geometry);
			}
			if (type == GeometryType.GEOROUNDRECTANGLE) {
				return new DGeoRoundRectangle((GeoRoundRectangle) geometry);
			}
			if (type == GeometryType.GEOCIRCLE) {
				return new DGeoCircle((GeoCircle) geometry);
			}
			if (type == GeometryType.GEOELLIPSE) {
				return new DGeoEllipse((GeoEllipse) geometry);
			}
			if (type == GeometryType.GEOPIE) {
				return new DGeoPie((GeoPie) geometry);
			}
			if (type == GeometryType.GEOLINE) {
				return new DGeoLine((GeoLine) geometry);
			}
			if (type == GeometryType.GEOARC) {
				return new DGeoArc((GeoArc) geometry);
			}
			if (type == GeometryType.GEOELLIPTICARC) {
				return new DGeoEllipticArc((GeoEllipticArc) geometry);
			}
			if (type == GeometryType.GEOCURVE) {
				return new DGeoCurve((GeoCurve) geometry);
			}
			if (type == GeometryType.GEOBSPLINE) {
				return new DGeoBSpline((GeoBSpline) geometry);
			}
			if (type == GeometryType.GEOLINEM) {
				return new DGeoLineM((GeoLineM) geometry);
			}
			if (type == GeometryType.GEOCARDINAL) {
				return new DGeoCardinal((GeoCardinal) geometry);
			}
			if (type == GeometryType.GEOCHORD) {
				return new DGeoChord((GeoChord) geometry);
			}
			if (type == GeometryType.GEOCOMPOUND) {
				return new DGeoCompound((GeoCompound) geometry);
			}
			if (type == GeometryType.GEOPOINT) {
				return new DGeoPoint((GeoPoint) geometry);
			}
			if (type == GeometryType.GEOPOINT3D) {
				return new DGeoPoint3D((GeoPoint3D) geometry);
			}
			if (type == GeometryType.GEOLINE3D) {
				return new DGeoLine3D((GeoLine3D) geometry);
			}
			if (type == GeometryType.GEOREGION3D) {
				return new DGeoRegion3D((GeoRegion3D) geometry);
			}
		}
		return new DGeoNormal(geometry);
	}

	public static IGeometry createNew(GeometryType type) {
		if (type == GeometryType.GEOTEXT) {
			return new DGeoText(new GeoText());
		}
		if (type == GeometryType.GEOREGION) {
			return new DGeoRegion(new GeoRegion());
		}
		if (type == GeometryType.GEORECTANGLE) {
			return new DGeoRectangle(new GeoRectangle());
		}
		if (type == GeometryType.GEOROUNDRECTANGLE) {
			return new DGeoRoundRectangle(new GeoRoundRectangle());
		}
		if (type == GeometryType.GEOCIRCLE) {
			return new DGeoCircle(new GeoCircle());
		}
		if (type == GeometryType.GEOELLIPSE) {
			return new DGeoEllipse(new GeoEllipse());
		}
		if (type == GeometryType.GEOPIE) {
			return new DGeoPie(new GeoPie());
		}
		if (type == GeometryType.GEOLINE) {
			return new DGeoLine(new GeoLine());
		}
		if (type == GeometryType.GEOARC) {
			return new DGeoArc(new GeoArc());
		}
		if (type == GeometryType.GEOELLIPTICARC) {
			return new DGeoEllipticArc(new GeoEllipticArc());
		}
		if (type == GeometryType.GEOCURVE) {
			return new DGeoCurve(new GeoCurve());
		}
		if (type == GeometryType.GEOBSPLINE) {
			return new DGeoBSpline(new GeoBSpline());
		}
		if (type == GeometryType.GEOLINEM) {
			return new DGeoLineM(new GeoLineM());
		}
		if (type == GeometryType.GEOCARDINAL) {
			return new DGeoCardinal(new GeoCardinal());
		}
		if (type == GeometryType.GEOCHORD) {
			return new DGeoChord(new GeoChord());
		}
		if (type == GeometryType.GEOCOMPOUND) {
			return new DGeoCompound(new GeoCompound());
		}
		if (type == GeometryType.GEOPOINT) {
			return new DGeoPoint(new GeoPoint());
		}
		if (type == GeometryType.GEOLINE3D) {
			return new DGeoLine3D(new GeoLine3D());
		}
		if (type == GeometryType.GEOREGION3D) {
			return new DGeoRegion3D(new GeoRegion3D());
		}
		return null;
	}

	/**
	 * 创建一个适用指定数据集类型的几何对象
	 * 
	 * @param datasetType
	 * @return
	 */
	public static IGeometry createNew(DatasetType datasetType) {
		if (datasetType == DatasetType.TEXT) {
			return new DGeoText(new GeoText());
		}
		if (datasetType == DatasetType.REGION) {
			return new DGeoRegion(new GeoRegion());
		}
		if (datasetType == DatasetType.CAD) {
			return new DGeoCompound(new GeoCompound());
		}
		if (datasetType == DatasetType.LINE) {
			return new DGeoLine(new GeoLine());
		}
		if (datasetType == DatasetType.LINEM) {
			return new DGeoLineM(new GeoLineM());
		}
		if (datasetType == DatasetType.POINT) {
			return new DGeoPoint(new GeoPoint());
		}
		if (datasetType == DatasetType.LINE3D) {
			return new DGeoLine3D(new GeoLine3D());
		}
		if (datasetType == DatasetType.REGION3D) {
			return new DGeoRegion3D(new GeoRegion3D());
		}
		return null;
	}
}
