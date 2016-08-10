package com.supermap.desktop.geometry.Implements;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.geometry.Abstract.*;
import com.supermap.desktop.utilities.GeometryUtilities;

public class DGeoLine3D extends AbstractGeometry
		implements ILine3DFeature, IMultiPartFeature<Point3Ds>, IRegion3DConvertor, IRegionConvertor, ILine3DConvertor, ILineConvertor, IReverse {

	private GeoLine3D geoLine3D;

	protected DGeoLine3D(GeoLine3D geoLine3D) {
		super(geoLine3D);
		this.geoLine3D = geoLine3D;
	}

	// @formatter:off
	/*
	 * 本类 segment 参数无效
	 */
	// @formatter:on
	@Override
	public GeoLine convertToLine(int segment) {
		GeoLine geoLine = null;

		if (this.geoLine3D != null) {
			geoLine = new GeoLine();

			for (int i = 0; i < this.geoLine3D.getPartCount(); i++) {
				geoLine.addPart(this.geoLine3D.getPart(i).toPoint2Ds());
			}
		}
		return geoLine;
	}

	@Override
	public int getPartCount() {
		return this.geoLine3D == null ? -1 : this.geoLine3D.getPartCount();
	}

	@Override
	public Point3Ds getPart(int index) {
		return this.geoLine3D == null ? null : this.geoLine3D.getPart(index);
	}

	@Override
	public void addPart(Point3Ds part) {
		if (this.geoLine3D != null) {
			this.geoLine3D.addPart(part);
		}
	}

	@Override
	public void addPart(Geometry geometry) {
		IGeometry dGeometry = DGeometryFactory.create(geometry);

		try {
			if (this.geoLine3D != null && dGeometry instanceof ILine3DConvertor) {
				GeoLine3D geoLine3D = ((ILine3DConvertor) dGeometry).convertToLine3D(60);

				if (geoLine3D != null) {
					for (int i = 0; i < geoLine3D.getPartCount(); i++) {
						this.geoLine3D.addPart(geoLine3D.getPart(i));
					}
				}
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			if (dGeometry != null) {
				dGeometry.dispose();
			}
		}
	}

	@Override
	public Geometry[] divide() {
		if (this.geoLine3D != null) {
			Geometry[] geometries = new Geometry[this.geoLine3D.getPartCount()];

			for (int i = 0; i < geoLine3D.getPartCount(); i++) {
				geometries[i] = new GeoLine3D(this.geoLine3D.getPart(i));
			}
			return geometries;
		}
		return null;
	}

	@Override
	public boolean setPart(int partIndex, Point3Ds part) {
		return this.geoLine3D != null && this.geoLine3D.setPart(partIndex, part);
	}

	// @formatter:off
	/*
	 * 本类 segment 参数无效
	 */
	// @formatter:on
	@Override
	public GeoLine3D convertToLine3D(int segment) {
		return this.geoLine3D;
	}

	// @formatter:off
	/*
	 * 本类 segment 参数无效
	 */
	// @formatter:on
	@Override
	public GeoRegion convertToRegion(int segment) {
		GeoRegion geoRegion = null;
		GeoLine line = convertToLine(segment);

		if (line != null) {
			geoRegion = line.convertToRegion();
		}
		return geoRegion;
	}

	// @formatter:off
	/*
	 * 本类 segment 参数无效
	 */
	// @formatter:on
	@Override
	public GeoRegion3D convertToRegion3D(int segment) {
		return this.geoLine3D == null ? null : this.geoLine3D.convertToRegion();
	}

	@Override
	public Geometry reverse() {
		GeoLine3D reverseLine3D = new GeoLine3D();
		reverseLine3D.setStyle3D(this.geoLine3D.getStyle3D());

		for (int i = 0; i < this.geoLine3D.getPartCount(); i++) {
			Point3Ds point3Ds = GeometryUtilities.reverse(this.geoLine3D.getPart(i));
			reverseLine3D.addPart(point3Ds);
		}
		return reverseLine3D;
	}

}
