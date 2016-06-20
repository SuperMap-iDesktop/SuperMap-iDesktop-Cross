package com.supermap.desktop.geometryoperation.editor;

import com.supermap.data.Geometry;
import com.supermap.desktop.geometry.Abstract.IMultiPartFeature;
import com.supermap.desktop.geometry.Implements.DGeoCompound;
import com.supermap.desktop.geometry.Implements.DGeoRegion;

/**
 * GeoRegion 保护性分解的结果不能释放，不知道是否组件的缺陷，待确认。
 * 
 * @author highsad
 *
 */
public class ProtectedDecomposeEditor extends DecomposeEditor {

	// 保护性分解重写这个
	@Override
	protected Geometry[] divide(IMultiPartFeature<?> geometry) {
		if (geometry instanceof DGeoCompound) {
			return ((DGeoCompound) geometry).protectedDivide();
		} else if (geometry instanceof DGeoRegion) {
			return ((DGeoRegion) geometry).protectedDivide();
		} else {
			return geometry.divide();
		}
	}
}
