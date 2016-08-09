package com.supermap.desktop.geometry.Implements;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.geometry.Abstract.*;
import com.supermap.desktop.utilities.GeometryUtilities;

public class DGeoRegion extends AbstractGeometry implements IMultiPartFeature<Point2Ds>, IRegionFeature, IRegionConvertor, ILineConvertor, IReverse {

	private GeoRegion geoRegion;

	protected DGeoRegion(GeoRegion geoRegion) {
		super(geoRegion);
		this.geoRegion = geoRegion;
	}

	/**
	 * @param segment
	 *            本类本参数无效
	 * @return
	 */
	@Override
	public GeoLine convertToLine(int segment) {
		return this.geoRegion == null ? null : this.geoRegion.convertToLine();
	}

	/**
	 * 返回自己
	 *
	 * @param segment
	 *            本类本参数无效
	 * @return
	 */
	@Override
	public GeoRegion convertToRegion(int segment) {
		return this.geoRegion;
	}

	@Override
	public int getPartCount() {
		return this.geoRegion == null ? -1 : this.geoRegion.getPartCount();
	}

	@Override
	public Point2Ds getPart(int index) {
		return this.geoRegion == null ? null : this.geoRegion.getPart(index);
	}

	@Override
	public void addPart(Point2Ds part) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addPart(Geometry geometry) {
		IGeometry dGeometry = DGeometryFactory.create(geometry);

		try {
			if (this.geoRegion != null && dGeometry instanceof IRegionConvertor) {
				GeoRegion geoRegion = ((IRegionConvertor) dGeometry).convertToRegion(60);

				if (geoRegion != null) {
					for (int i = 0; i < geoRegion.getPartCount(); i++) {
						this.geoRegion.addPart(geoRegion.getPart(i));
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

	public Geometry[] divide() {
		if (this.geoRegion != null) {
			GeoStyle geoStyle = this.geoRegion.getStyle();
			Geometry[] geometries = new Geometry[this.geoRegion.getPartCount()];

			for (int i = 0; i < this.geoRegion.getPartCount(); i++) {
				GeoRegion region = new GeoRegion(this.geoRegion.getPart(i));
				region.setStyle(geoStyle == null ? null : geoStyle.clone());
				geometries[i] = region;
			}
			return geometries;
		}

		return null;
	}

	@Override
	public boolean setPart(int partIndex, Point2Ds part) {
		return this.geoRegion != null && this.geoRegion.setPart(partIndex, part);
	}

	/**
	 * 保护性分解，维持岛洞关系
	 *
	 * @return
	 */
	public Geometry[] protectedDivide() {
		return this.geoRegion == null ? null : this.geoRegion.protectedDecompose();
	}

	@Override
	public Geometry reverse() {
		GeoRegion reverseRegion = new GeoRegion();
		reverseRegion.setStyle(this.geoRegion.getStyle());

		for (int i = 0; i < this.geoRegion.getPartCount(); i++) {
			Point2Ds point2Ds = GeometryUtilities.reverse(this.geoRegion.getPart(i));
			reverseRegion.addPart(point2Ds);
		}
		return reverseRegion;
	}
}
