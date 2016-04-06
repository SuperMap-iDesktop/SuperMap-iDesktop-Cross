package com.supermap.desktop.geometry.Implements;

import com.supermap.data.GeoLine;
import com.supermap.data.GeoLineM;
import com.supermap.data.GeoRegion;
import com.supermap.data.Point2Ds;
import com.supermap.data.PointMs;
import com.supermap.desktop.geometry.Abstract.AbstractGeometry;
import com.supermap.desktop.geometry.Abstract.ILineConvertor;
import com.supermap.desktop.geometry.Abstract.ILineFeature;
import com.supermap.desktop.geometry.Abstract.IMultiPartFeature;
import com.supermap.desktop.geometry.Abstract.IRegionConvertor;

public class DGeoLineM extends AbstractGeometry implements IMultiPartFeature, ILineFeature, ILineConvertor, IRegionConvertor {

	private GeoLineM geoLineM;

	protected DGeoLineM(GeoLineM geoLineM) {
		super(geoLineM);
		this.geoLineM = geoLineM;
	}

	/**
	 * @param segment
	 *            本类本参数无效
	 * @return
	 */
	@Override
	public GeoRegion convertToRegion(int segment) {
		return this.geoLineM == null ? null : this.geoLineM.convertToRegion();
	}

	/**
	 * @param segment
	 *            本类本参数无效
	 * @return
	 */
	@Override
	public GeoLine convertToLine(int segment) {
		return this.geoLineM == null ? null : this.geoLineM.convertToLine();
	}

	@Override
	public int getPartCount() {
		return this.geoLineM == null ? -1 : this.geoLineM.getPartCount();
	}

	@Override
	public Point2Ds getPart(int index) {
		GeoLine geoLine = convertToLine(0);
		return geoLine == null ? null : geoLine.getPart(index);
	}

	public PointMs getPartM(int index) {
		return this.geoLineM == null ? null : this.geoLineM.getPart(index);
	}
}
