package com.supermap.desktop.geometry.Implements;

import com.supermap.data.GeoLine;
import com.supermap.data.GeoPie;
import com.supermap.data.GeoRegion;
import com.supermap.desktop.geometry.Abstract.AbstractGeometry;
import com.supermap.desktop.geometry.Abstract.IFlatFeature;
import com.supermap.desktop.geometry.Abstract.ILineConvertor;
import com.supermap.desktop.geometry.Abstract.IRegionConvertor;
import com.supermap.desktop.geometry.Abstract.IRegionFeature;

public class DGeoPie extends AbstractGeometry implements IRegionFeature, ILineConvertor, IRegionConvertor {

	private GeoPie geoPie;

	protected DGeoPie(GeoPie geoPie) {
		super(geoPie);
		this.geoPie = geoPie;
	}

	@Override
	public GeoRegion convertToRegion(int segment) {
		return this.geoPie == null ? null : this.geoPie.convertToRegion(segment);
	}

	@Override
	public GeoLine convertToLine(int segment) {
		return this.geoPie == null ? null : this.geoPie.convertToLine(segment);
	}

}
