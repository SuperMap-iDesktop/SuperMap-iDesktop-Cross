package com.supermap.desktop.geometry.Implements;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.geometry.Abstract.*;
import com.supermap.desktop.utilities.GeometryUtilities;

public class DGeoRegion3D extends AbstractGeometry
		implements IRegion3DFeature, IMultiPartFeature<Point3Ds>, IRegion3DConvertor, IRegionConvertor, ILine3DConvertor, ILineConvertor, IReverse {

	private GeoRegion3D geoRegion3D;

	protected DGeoRegion3D(GeoRegion3D geoRegion3D) {
		super(geoRegion3D);
		this.geoRegion3D = geoRegion3D;
	}

	@Override
	public int getPartCount() {
		return this.geoRegion3D == null ? -1 : this.geoRegion3D.getPartCount();
	}

	@Override
	public Point3Ds getPart(int index) {
		return this.geoRegion3D == null ? null : this.geoRegion3D.getPart(index);
	}

	@Override
	public void addPart(Point3Ds part) {
		if (this.geoRegion3D != null) {
			this.geoRegion3D.addPart(part);
		}
	}

	@Override
	public void addPart(Geometry geometry) {
		IGeometry dGeometry = DGeometryFactory.create(geometry);

		try {
			if (this.geoRegion3D != null && dGeometry instanceof IRegion3DConvertor) {
				GeoRegion3D geoRegion3D = ((IRegion3DConvertor) dGeometry).convertToRegion3D(60);

				if (geoRegion3D != null) {
					for (int i = 0; i < geoRegion3D.getPartCount(); i++) {
						this.geoRegion3D.addPart(geoRegion3D.getPart(i));
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
		if (this.geoRegion3D != null) {
			Geometry[] geometries = new Geometry[this.geoRegion3D.getPartCount()];

			for (int i = 0; i < geoRegion3D.getPartCount(); i++) {
				geometries[i] = new GeoRegion3D(this.geoRegion3D.getPart(i));
			}
			return geometries;
		}
		return null;
	}

	@Override
	public boolean setPart(int partIndex, Point3Ds part) {
		return this.geoRegion3D != null && this.geoRegion3D.setPart(partIndex, part);
	}

	// @formatter:off
	/*
	 * 本类 segment 参数无效
	 */
	// @formatter:on
	@Override
	public GeoRegion convertToRegion(int segment) {
		GeoRegion geoRegion = null;

		if (this.geoRegion3D != null) {
			geoRegion = new GeoRegion();

			for (int i = 0; i < this.geoRegion3D.getPartCount(); i++) {
				geoRegion.addPart(this.geoRegion3D.getPart(i).toPoint2Ds());
			}
		}
		return geoRegion;
	}

	// @formatter:off
	/*
	 * 返回自己
	 * 本类 segment 参数无效
	 */
	// @formatter:on
	@Override
	public GeoRegion3D convertToRegion3D(int segment) {
		return this.geoRegion3D;
	}

	// @formatter:off
	/*
	 * 本类 segment 参数无效
	 */
	// @formatter:on
	@Override
	public GeoLine convertToLine(int segment) {
		GeoLine geoLine = null;
		GeoRegion geoRegion = convertToRegion(segment);

		if (geoRegion != null) {
			geoLine = geoRegion.convertToLine();
		}
		return geoLine;
	}

	// @formatter:off
	/*
	 * 本类 segment 参数无效
	 */
	// @formatter:on
	@Override
	public GeoLine3D convertToLine3D(int segment) {
		return this.geoRegion3D == null ? null : this.geoRegion3D.convertToLine();
	}

	@Override
	public Geometry reverse() {
		GeoRegion3D reverseRegion3D = new GeoRegion3D();
		reverseRegion3D.setStyle3D(this.geoRegion3D.getStyle3D());

		for (int i = 0; i < this.geoRegion3D.getPartCount(); i++) {
			Point3Ds point3Ds = GeometryUtilities.reverse(this.geoRegion3D.getPart(i));
			reverseRegion3D.addPart(point3Ds);
		}
		return reverseRegion3D;
	}
}
