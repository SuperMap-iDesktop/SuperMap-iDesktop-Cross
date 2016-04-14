package com.supermap.desktop.geometry.Implements;

import java.util.ArrayList;
import java.util.List;

import com.supermap.data.GeoCompound;
import com.supermap.data.Geometry;
import com.supermap.desktop.geometry.Abstract.AbstractGeometry;
import com.supermap.desktop.geometry.Abstract.IGeometry;
import com.supermap.desktop.geometry.Abstract.IMultiPartFeature;
import com.supermap.desktop.utilties.ListUtilties;

public class DGeoCompound extends AbstractGeometry implements IMultiPartFeature<Geometry> {

	private GeoCompound geoCompound;

	protected DGeoCompound(GeoCompound geoCompound) {
		super(geoCompound);
		this.geoCompound = geoCompound;
	}

	@Override
	public int getPartCount() {
		return this.geoCompound == null ? -1 : this.geoCompound.getPartCount();
	}

	@Override
	public Geometry getPart(int index) {
		return this.geoCompound == null ? null : this.geoCompound.getPart(index);
	}

	@Override
	public void addPart(Geometry part) {
		if (this.geoCompound != null) {
			this.geoCompound.addPart(part);
		}
	}

	// @formatter:off
	/*
	 * CAD上的组合对象都是复杂对象，通过Divide能够分解到最简单对象
	 * 只有一种情况，CAD上的岛洞数据是面对象需要再次分解，所以对CAD复杂对象深度分解的结果存在岛洞数据的情况需要单独再处理
	 * 如果每一个对象不一次性分解完成，操作历史记录的时候会出现回退的时候多了一个中间对象
	 */
	// @formatter:on
	@Override
	public Geometry[] divide() {
		if (this.geoCompound != null) {
			List<Geometry> geometries = new ArrayList<>();
			Geometry[] temp = this.geoCompound.divide(false);

			for (int i = 0; i < temp.length; i++) {
				IGeometry geometry = DGeometryFactory.create(temp[i]);

				if (geometry instanceof DGeoRegion) {
					ListUtilties.addArray(geometries, ((DGeoRegion) geometry).divide());
				} else {
					geometries.add(geometry.getGeometry());
				}
			}
			return geometries.toArray(new Geometry[geometries.size()]);
		}

		return null;
	}

	/**
	 * 保护性分解，保持岛洞关系
	 * 
	 * @return
	 */
	public Geometry[] protectedDivide() {
		if (this.geoCompound != null) {
			return this.geoCompound.divide(false);
		}

		return null;
	}
}
