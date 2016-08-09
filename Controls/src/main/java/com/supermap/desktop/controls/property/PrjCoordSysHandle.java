package com.supermap.desktop.controls.property;

import com.supermap.data.CoordSysTransMethod;
import com.supermap.data.CoordSysTransParameter;
import com.supermap.data.PrjCoordSys;

/**
 * 抽象投影相关的功能，数据源、数据集等不同的对象投影设置以及投影转换的方式不同
 * 
 * @author highsad
 *
 */
public abstract class PrjCoordSysHandle {
	protected PrjCoordSys prj;

	protected PrjCoordSysHandle(PrjCoordSys prj) {
		this.prj = prj;
	}

	public PrjCoordSys getPrj() {
		return prj;
	}

	public abstract void change(PrjCoordSys targetPrj);

	public abstract void convert(CoordSysTransMethod method, CoordSysTransParameter parameter, PrjCoordSys targetPrj);
}
