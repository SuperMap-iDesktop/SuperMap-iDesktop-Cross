package com.supermap.desktop.geometry.Implements;

import com.supermap.data.GeoLine;
import com.supermap.data.GeoRegion;
import com.supermap.data.Geometry;
import com.supermap.data.Point2Ds;
import com.supermap.desktop.Application;
import com.supermap.desktop.geometry.Abstract.AbstractGeometry;
import com.supermap.desktop.geometry.Abstract.IGeometry;
import com.supermap.desktop.geometry.Abstract.ILineConvertor;
import com.supermap.desktop.geometry.Abstract.IMultiPartFeature;
import com.supermap.desktop.geometry.Abstract.IRegionConvertor;
import com.supermap.desktop.geometry.Abstract.IRegionFeature;
import com.supermap.desktop.geometry.Abstract.IReverse;
import com.supermap.desktop.utilities.GeometryUtilities;

public class DGeoRegion extends AbstractGeometry implements IMultiPartFeature<Point2Ds>, IRegionFeature, IRegionConvertor, ILineConvertor, IReverse {

	private GeoRegion geoRegion;

	protected DGeoRegion(GeoRegion geoRegion) {
		super(geoRegion);
		this.geoRegion = geoRegion;
	}

	/**
	 * @param segment 本类本参数无效
	 * @return
	 */
	@Override
	public GeoLine convertToLine(int segment) {
		return this.geoRegion == null ? null : this.geoRegion.convertToLine();
	}

	/**
	 * 返回自己
	 *
	 * @param segment 本类本参数无效
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
			Geometry[] geometries = new Geometry[this.geoRegion.getPartCount()];

			for (int i = 0; i < this.geoRegion.getPartCount(); i++) {
				geometries[i] = new GeoRegion(this.geoRegion.getPart(i));
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

		for (int i = 0; i < this.geoRegion.getPartCount(); i++) {
			Point2Ds point2Ds = GeometryUtilities.reverse(this.geoRegion.getPart(i));
			reverseRegion.addPart(point2Ds);
		}
		return reverseRegion;
	}
}
