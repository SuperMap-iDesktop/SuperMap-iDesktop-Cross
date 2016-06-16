package com.supermap.desktop.ui.controls.prjcoordsys;

import com.supermap.data.Enum;
import com.supermap.data.GeoCoordSys;
import com.supermap.data.GeoCoordSysType;
import com.supermap.data.GeoSpatialRefType;
import com.supermap.data.PrjCoordSys;
import com.supermap.data.PrjCoordSysType;
import com.supermap.data.Unit;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.ui.controls.prjcoordsys.JDialogPrjCoordSysSettings.CoordSysDefine;
import com.supermap.desktop.utilties.PrjCoordSysUtilities;

import java.text.MessageFormat;

public class PrjCoordSysSettingsUtilties {

	/**
	 * 根据投影定义获取对应的投影对象
	 * 
	 * @return
	 */
	private PrjCoordSysSettingsUtilties() {
		// 工具类不提供构造函数
	}

	public static PrjCoordSys getPrjCoordSys(CoordSysDefine define) {
		PrjCoordSys result = null;

		try {
			// 没有子项，则为具体的投影定义
			if (define.size() == 0 && define.getCoordSysType() == CoordSysDefine.PROJECTION_SYSTEM) {
				if (define.getCoordSysCode() != CoordSysDefine.USER_DEFINED) {
					PrjCoordSysType type = (PrjCoordSysType) Enum.parse(PrjCoordSysType.class, define.getCoordSysCode());
					result = new PrjCoordSys(type);
					result.setName(define.getCaption());
				} else {
					result = define.getPrjCoordSys();
				}
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return result;
	}

	/**
	 * 根据投影定义获取对应的地理坐标系对象
	 * 
	 * @param define
	 * @return
	 */
	public static GeoCoordSys getGeoCoordSys(CoordSysDefine define) {
		GeoCoordSys result = null;

		try {
			if (define.size() == 0 && define.getCoordSysType() == CoordSysDefine.GEOGRAPHY_COORDINATE) {
				if (define.getCoordSysCode() != CoordSysDefine.USER_DEFINED) {
					GeoCoordSysType type = (GeoCoordSysType) Enum.parse(GeoCoordSysType.class, define.getCoordSysCode());
					result = new GeoCoordSys(type, GeoSpatialRefType.SPATIALREF_EARTH_LONGITUDE_LATITUDE);
					result.setName(define.getCaption());
				} else {
					result = define.getGeoCoordSys();
				}
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return result;
	}

	/**
	 * 获取指定投影定义的详细描述信息
	 * 
	 * @param define
	 * @return
	 */
	public static String getDescription(CoordSysDefine define) {
		String description = "";

		try {
			if (define != null) {
				if (define.size() == 0) {
					// 如果当前选中的投影定义没有子项，那么就是具体的投影
					PrjCoordSys prjCoordSys = null;
					if (define.getCoordSysType() == CoordSysDefine.NONE_ERRTH) {
						prjCoordSys = new PrjCoordSys(PrjCoordSysType.PCS_NON_EARTH);
						prjCoordSys.setCoordUnit((Unit) Enum.parse(Unit.class, define.getCoordSysCode()));
					} else if (define.getCoordSysType() == CoordSysDefine.PROJECTION_SYSTEM) {
						prjCoordSys = PrjCoordSysSettingsUtilties.getPrjCoordSys(define);
					} else if (define.getCoordSysType() == CoordSysDefine.GEOGRAPHY_COORDINATE) {
						GeoCoordSys geoCoordSys = PrjCoordSysSettingsUtilties.getGeoCoordSys(define);
						prjCoordSys = new PrjCoordSys(PrjCoordSysType.PCS_EARTH_LONGITUDE_LATITUDE);
						prjCoordSys.setGeoCoordSys(geoCoordSys);
					}

					if (prjCoordSys != null) {
						description = PrjCoordSysUtilities.getDescription(prjCoordSys);
					}
				} else {
					// 如果当前选中的投影定义有子项，那么就是某一类投影的集合
					description = define.getCaption() + System.lineSeparator()
							+ MessageFormat.format(ControlsProperties.getString("String_ObjectsCount"), define.size());
				}
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return description;
	}
}
