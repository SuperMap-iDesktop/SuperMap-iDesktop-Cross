package com.supermap.desktop.utilities;

import java.util.ArrayList;
import java.util.HashMap;

import com.supermap.data.CoordSysTranslator;
import com.supermap.data.GeoCoordSys;
import com.supermap.data.GeoCoordSysType;
import com.supermap.data.GeoLine;
import com.supermap.data.GeoRegion;
import com.supermap.data.PrjCoordSys;
import com.supermap.data.PrjCoordSysType;
import com.supermap.data.PrjParameter;
import com.supermap.data.Projection;
import com.supermap.data.ProjectionType;
import com.supermap.data.Rectangle2D;
import com.supermap.desktop.Application;
import com.supermap.desktop.properties.CoreProperties;

public class PrjCoordSysUtilities {

	/**
	 * 获取所有地理坐标系类型及相应的字符串表示字典
	 * 
	 * @return
	 */
	private PrjCoordSysUtilities() {
		// 工具类不提供构造函数
	}

	public static HashMap<GeoCoordSysType, String> getGeoCoordSysTypeList() {
		HashMap<GeoCoordSysType, String> coordSysTypeList = new HashMap<GeoCoordSysType, String>();

		coordSysTypeList.put(GeoCoordSysType.GCS_ADINDAN, GeoCoordSysType.GCS_ADINDAN.toString());
		return coordSysTypeList;
	}

	public static String getDescription(PrjCoordSys prjCoordSys) {
		String result = "";
		try {
			if (prjCoordSys.getType() == PrjCoordSysType.PCS_NON_EARTH) {
				result = CoreProperties.getString("String_NoProjectionParameter") + "----" + prjCoordSys.getCoordUnit().toString();
			} else {
				String[] earthFrameOfReferenceinfos = new String[] { CoreProperties.getString("String_GeoCoordSys_GeodeticCoordinateSystem"),
						CoreProperties.getString("String_GeoCoordSys_ReferenceSpheroid"), CoreProperties.getString("String_GeoSpheroid_Axis"),
						CoreProperties.getString("String_GeoSpheroid_Flatten") };
				ArrayList<String> infoLabels = new ArrayList<String>();
				if (prjCoordSys.getType() == PrjCoordSysType.PCS_EARTH_LONGITUDE_LATITUDE) {
					for (String string : earthFrameOfReferenceinfos) {
						infoLabels.add(string);
					}
				} else {
					String[] prjInfo = new String[] { CoreProperties.getString("String_Projection_ProjectionType"),
							CoreProperties.getString("String_PrjParameter_CenterMeridian"), CoreProperties.getString("String_PrjParameter_CentralParallel"),
							CoreProperties.getString("String_PrjParameter_StandardParallel1"),
							CoreProperties.getString("String_PrjParameter_StandardParallel2"), CoreProperties.getString("String_PrjParameter_FalseEasting"),
							CoreProperties.getString("String_PrjParameter_FalseNorthing"), CoreProperties.getString("String_PrjParameter_ScaleFactor"),
							CoreProperties.getString("String_PrjParameter_Azimuth"), CoreProperties.getString("String_PrjParameter_FirstPointLongitude"),
							CoreProperties.getString("String_PrjParameter_SecondPointLongitude"), CoreProperties.getString("String_GeoCoordSys_Name") };

					for (String string : prjInfo) {
						infoLabels.add(string);
					}

					for (String string : earthFrameOfReferenceinfos) {
						infoLabels.add(string);
					}
				}
				result = setInformation(infoLabels, prjCoordSys);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return result;
	}

	private static String setInformation(ArrayList<String> infos, PrjCoordSys prj) {
		String text = "";
		try {
			if (infos != null && prj != null) {
				for (String info : infos) {

					// @formatter:off
					/*
					 * 设置固定长度，对齐第二列，由于需要对齐汉字，汉字的自宽并不确定，因此右边使用横向制表符来对齐边缘。
					 * 水平制表符的宽度是固定的，在文本中使用水平制表符 '\t'，会定位到下一个制表符的起始位置。
					 * 在进行中文字符格式化的时候，字体不同会导致中文字宽的不确定，要右对齐就需要使用水平制表符，对齐到
					 * 某一个制表符起始位置处。因此在进行格式化的时候，需要保证所有需要对齐的字符串长度在相同的制表符宽度之内
					 */
					// @formatter:on
					text += String.format("%-10s\t", info);
					if (info.equals(CoreProperties.getString("String_GeoCoordSys_GeodeticCoordinateSystem"))) {
						text += prj.getGeoCoordSys().getGeoDatum().getName();
					} else if (info.equals(CoreProperties.getString("String_GeoCoordSys_ReferenceSpheroid"))) {
						text += prj.getGeoCoordSys().getGeoDatum().getGeoSpheroid().getName();
					} else if (info.equals(CoreProperties.getString("String_GeoSpheroid_Axis"))) {
						text += Double.toString(prj.getGeoCoordSys().getGeoDatum().getGeoSpheroid().getAxis());
					} else if (info.equals(CoreProperties.getString("String_GeoSpheroid_Flatten"))) {
						text += Double.toString(prj.getGeoCoordSys().getGeoDatum().getGeoSpheroid().getFlatten());
					} else if (info.equals(CoreProperties.getString("String_Projection_ProjectionType"))) {
						text += prj.getProjection().getType().toString();
					} else if (info.equals(CoreProperties.getString("String_PrjParameter_CenterMeridian"))) {
						text += Double.toString(prj.getPrjParameter().getCentralMeridian());
					} else if (info.equals(CoreProperties.getString("String_PrjParameter_CentralParallel"))) {
						text += Double.toString(prj.getPrjParameter().getCentralParallel());
					} else if (info.equals(CoreProperties.getString("String_PrjParameter_StandardParallel1"))) {
						text += Double.toString(prj.getPrjParameter().getStandardParallel1());
					} else if (info.equals(CoreProperties.getString("String_PrjParameter_StandardParallel2"))) {
						text += Double.toString(prj.getPrjParameter().getStandardParallel2());
					} else if (info.equals(CoreProperties.getString("String_PrjParameter_FalseEasting"))) {
						text += Double.toString(prj.getPrjParameter().getFalseEasting());
					} else if (info.equals(CoreProperties.getString("String_PrjParameter_FalseNorthing"))) {
						text += Double.toString(prj.getPrjParameter().getFalseNorthing());
					} else if (info.equals(CoreProperties.getString("String_PrjParameter_ScaleFactor"))) {
						text += Double.toString(prj.getPrjParameter().getScaleFactor());
					} else if (info.equals(CoreProperties.getString("String_PrjParameter_Azimuth"))) {
						text += Double.toString(prj.getPrjParameter().getAzimuth());
					} else if (info.equals(CoreProperties.getString("String_PrjParameter_FirstPointLongitude"))) {
						text += Double.toString(prj.getPrjParameter().getFirstPointLongitude());
					} else if (info.equals(CoreProperties.getString("String_PrjParameter_SecondPointLongitude"))) {
						text += Double.toString(prj.getPrjParameter().getSecondPointLongitude());
					} else if (info.equals(CoreProperties.getString("String_GeoCoordSys_Name"))) {
						text += prj.getGeoCoordSys().getName();
					}
					text += System.lineSeparator();
				}
				text = text.substring(0, text.length() - 2);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return text;
	}

	/**
	 * 根据指定的 bounds 以及地理坐标系来获取一个 Albers 投影坐标系，用来做平常坐标的长度计算
	 * 
	 * @param bounds
	 * @return
	 */
	public static PrjCoordSys getAlbersPrjCoordSys(Rectangle2D bounds, GeoCoordSys geoCoordSys) {
		PrjCoordSys prj = null;

		try {
			if (geoCoordSys != null) {
				prj = new PrjCoordSys();
				prj.setGeoCoordSys(geoCoordSys);

				prj.setProjection(new Projection(ProjectionType.PRJ_ALBERS));
				PrjParameter prjParameter = new PrjParameter();
				prjParameter.setCentralMeridian((bounds.getLeft() + bounds.getRight()) / 2.0);
				prjParameter.setStandardParallel1(bounds.getTop());
				prjParameter.setStandardParallel2(bounds.getBottom());
				prj.setPrjParameter(prjParameter);
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return prj;
	}

	/**
	 * 将指定的 geoLine 对象用指定的投影坐标系统进行转换
	 * 
	 * @param geoLine
	 * @param prjCoordSys
	 */
	public static void convertGeoLine(GeoLine geoLine, PrjCoordSys prjCoordSys) {
		try {
			if (geoLine != null && prjCoordSys != null) {
				for (int i = 0; i < geoLine.getPartCount(); i++) {
					CoordSysTranslator.forward(geoLine.getPart(i), prjCoordSys);
				}
			}
		} catch (IllegalArgumentException e) {
			Application.getActiveApplication().getOutput().output(e.getMessage());
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	/**
	 * 用默认 Albers投影方式在指定的地理坐标系下将 geoLine 从地理坐标转换为投影坐标
	 * 
	 * @param geoLine
	 * @param geoCoordSys
	 */
	public static void convertGeoLine(GeoLine geoLine, GeoCoordSys geoCoordSys) {
		if (geoLine != null && geoCoordSys != null) {
			convertGeoLine(geoLine, getAlbersPrjCoordSys(geoLine.getBounds(), geoCoordSys));
		}
	}

	/**
	 * 将指定的 geoRegion 对象用指定的投影坐标系统进行转换
	 * 
	 * @param geoRegion
	 * @param prjCoordSys
	 */
	public static void convertGeoRegion(GeoRegion geoRegion, PrjCoordSys prjCoordSys) {
		try {
			if (geoRegion != null && prjCoordSys != null) {
				for (int i = 0; i < geoRegion.getPartCount(); i++) {
					CoordSysTranslator.forward(geoRegion.getPart(i), prjCoordSys);
				}
			}
		} catch (IllegalArgumentException e) {
			Application.getActiveApplication().getOutput().output(e.getMessage());
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	/**
	 * 用默认 Albers投影方式在指定的地理坐标系下将 geoRegion 从地理坐标转换为投影坐标
	 * 
	 * @param geoRegion
	 * @param geoCoordSys
	 */
	public static void convertGeoRegion(GeoRegion geoRegion, GeoCoordSys geoCoordSys) {
		if (geoRegion != null && geoCoordSys != null) {
			convertGeoRegion(geoRegion, getAlbersPrjCoordSys(geoRegion.getBounds(), geoCoordSys));
		}
	}
}
