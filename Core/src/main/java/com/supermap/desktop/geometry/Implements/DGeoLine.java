package com.supermap.desktop.geometry.Implements;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.geometry.Abstract.*;
import com.supermap.desktop.utilities.GeometryUtilities;

public class DGeoLine extends AbstractGeometry implements IMultiPartFeature<Point2Ds>, ILineFeature, IRegionConvertor, ILineConvertor, IReverse {

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
		if (this.geoLine != null) {
			this.geoLine.addPart(part);
		}
	}

	@Override
	public boolean setPart(int partIndex, Point2Ds part) {
		return this.geoLine != null && this.geoLine.setPart(partIndex, part);
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
			GeoStyle geoStyle = this.geoLine.getStyle();
			Geometry[] geometries = new Geometry[this.geoLine.getPartCount()];

			for (int i = 0; i < this.geoLine.getPartCount(); i++) {
				GeoLine line = new GeoLine(this.geoLine.getPart(i));
				line.setStyle(geoStyle == null ? null : geoStyle.clone());
				geometries[i] = line;
			}
			return geometries;
		}

		return null;
	}

	@Override
	public Geometry reverse() {
		GeoLine reverseLine = new GeoLine();
		reverseLine.setStyle(this.geoLine.getStyle());

		for (int i = 0; i < this.geoLine.getPartCount(); i++) {
			Point2Ds point2Ds = GeometryUtilities.reverse(this.geoLine.getPart(i));
			reverseLine.addPart(point2Ds);
		}
		return reverseLine;
	}

}
