package com.supermap.desktop.geometry.Implements;

import com.supermap.data.GeoLine;
import com.supermap.data.GeoRegion;
import com.supermap.data.Geometry;
import com.supermap.data.Point2Ds;
import com.supermap.desktop.Application;
import com.supermap.desktop.geometry.Abstract.AbstractGeometry;
import com.supermap.desktop.geometry.Abstract.IGeometry;
import com.supermap.desktop.geometry.Abstract.ILineConvertor;
import com.supermap.desktop.geometry.Abstract.ILineFeature;
import com.supermap.desktop.geometry.Abstract.IMultiPartFeature;
import com.supermap.desktop.geometry.Abstract.IRegionConvertor;

public class DGeoLine extends AbstractGeometry implements IMultiPartFeature<Point2Ds>, ILineFeature, IRegionConvertor, ILineConvertor {

	private GeoLine geoLine;

	protected DGeoLine(GeoLine geoLine) {
		super(geoLine);
		this.geoLine = geoLine;
	}

	// @formatter:off
	/*
	 * 
	 * 本类 segment 参数无效
	 */
	// @formatter:on
	@Override
	public GeoRegion convertToRegion(int segment) {
		return this.geoLine == null ? null : this.geoLine.convertToRegion();
	}

	@Override
	public int getPartCount() {
		return this.geoLine == null ? -1 : this.geoLine.getPartCount();
	}

	@Override
	public Point2Ds getPart(int index) {
		return this.geoLine == null ? null : this.geoLine.getPart(index);
	}

	// @formatter:off
	/*
	 * 返回自己
	 * 本类 segment 参数无效
	 */
	// @formatter:on
	@Override
	public GeoLine convertToLine(int segment) {
		return this.geoLine;
	}

	@Override
	public void addPart(Point2Ds part) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addPart(Geometry geometry) {
		IGeometry dGeometry = DGeometryFactory.create(geometry);

		try {
			if (this.geoLine != null && dGeometry instanceof ILineConvertor) {
				GeoLine geoLine = ((ILineConvertor) dGeometry).convertToLine(60);

				if (geoLine != null) {
					for (int i = 0; i < geoLine.getPartCount(); i++) {
						this.geoLine.addPart(geoLine.getPart(i));
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
		if (this.geoLine != null) {
			Geometry[] geometries = new Geometry[this.geoLine.getPartCount()];

			for (int i = 0; i < this.geoLine.getPartCount(); i++) {
				geometries[i] = new GeoLine(this.geoLine.getPart(i));
			}
			return geometries;
		}

		return null;
	}
}
