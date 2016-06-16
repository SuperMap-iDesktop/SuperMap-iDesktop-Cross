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
import com.supermap.desktop.ui.controls.comboBox.SearchItemValueGetter;
import com.supermap.desktop.ui.controls.prjcoordsys.JDialogPrjCoordSysSettings.CoordSysDefine;
import com.supermap.desktop.utilties.PrjCoordSysTypeUtilties;
import com.supermap.desktop.utilties.PrjCoordSysUtilties;

import javax.swing.*;
import java.awt.*;
import java.text.MessageFormat;

public class PrjCoordSysSettingsUtilties {
	private static Dimension labelPreferredSize = new Dimension(20, 23);
	private static ListCellRenderer<Enum> enumComboBoxItemRender = null;
	private static SearchItemValueGetter<Enum> searchItemValueGetter = null;
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
						description = PrjCoordSysUtilties.getDescription(prjCoordSys);
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

	public static ListCellRenderer<Enum> getEnumComboBoxItemRender() {
		if (enumComboBoxItemRender == null) {
			enumComboBoxItemRender = new ListCellRenderer<Enum>() {
				@Override
				public Component getListCellRendererComponent(JList<? extends Enum> list, Enum value, int index, boolean isSelected, boolean cellHasFocus) {
					JLabel jLabel = new JLabel();
					jLabel.setOpaque(true);
					jLabel.setPreferredSize(labelPreferredSize);
					jLabel.setText(" " + PrjCoordSysTypeUtilties.getDescribe(value.name()));
					if (isSelected) {
						jLabel.setBackground(list.getSelectionBackground());
					} else {
						jLabel.setBackground(list.getBackground());
					}
					return jLabel;
				}
			};
		}
		return enumComboBoxItemRender;
	}

	public static SearchItemValueGetter<Enum> getSearchItemValueGetter() {
		if (searchItemValueGetter == null) {
			searchItemValueGetter = new SearchItemValueGetter<Enum>() {
				@Override
				public String getSearchString(Enum item) {
					return PrjCoordSysTypeUtilties.getDescribe(item.name());
				}
			};
		}
		return searchItemValueGetter;
	}
}
