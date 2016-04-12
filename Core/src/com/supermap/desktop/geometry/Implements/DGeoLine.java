package com.supermap.desktop.geometry.Implements;

import com.supermap.data.GeoLine;
import com.supermap.data.GeoRegion;
import com.supermap.data.Point2Ds;
import com.supermap.desktop.geometry.Abstract.AbstractGeometry;
import com.supermap.desktop.geometry.Abstract.ILineConvertor;
import com.supermap.desktop.geometry.Abstract.ILineFeature;
import com.supermap.desktop.geometry.Abstract.IMultiPartFeature;
import com.supermap.desktop.geometry.Abstract.IRegionConvertor;

public class DGeoLine extends AbstractGeometry implements IMultiPartFeature, ILineFeature, IRegionConvertor, ILineConvertor {

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

}
