package com.supermap.desktop.geometry.Implements;

import com.supermap.data.GeoLine;
import com.supermap.data.GeoRectangle;
import com.supermap.data.GeoRegion;
import com.supermap.desktop.geometry.Abstract.AbstractGeometry;
import com.supermap.desktop.geometry.Abstract.IFlatFeature;
import com.supermap.desktop.geometry.Abstract.ILineConvertor;
import com.supermap.desktop.geometry.Abstract.IRegionConvertor;
import com.supermap.desktop.geometry.Abstract.IRegionFeature;

public class DGeoRectangle extends AbstractGeometry implements IRegionFeature, ILineConvertor, IRegionConvertor {

	private GeoRectangle geoRectangle;

	protected DGeoRectangle(GeoRectangle geoRectangle) {
		super(geoRectangle);
		this.geoRectangle = geoRectangle;
	}

	/**
	 * @param segment
	 *            本类本参数无效
	 * @return
	 */
	@Override
	public GeoRegion convertToRegion(int segment) {
		return this.geoRectangle == null ? null : this.geoRectangle.convertToRegion();
	}

	/**
	 * @param segment
	 *            本类本参数无效
	 * @return
	 */
	@Override
	public GeoLine convertToLine(int segment) {
		return this.geoRectangle == null ? null : this.geoRectangle.convertToLine();
	}
}
