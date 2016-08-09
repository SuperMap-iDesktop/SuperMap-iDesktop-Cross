package com.supermap.desktop.netservices.iserver;

import com.supermap.desktop.netservices.NetServicesProperties;

public class ServiceType {
	public static final int NONE = 0x0000; // 无服务
	public static final int RESTDATA = 0x0001; // REST-数据服务
	public static final int RESTMAP = 0x0002; // REST-地图服务
	public static final int RESTREALSPACE = 0x0004; // REST-三维场景服务
	public static final int RESTSPATIALANALYST = 0x0008; // REST-空间分析服务
	public static final int RESTTRANSPORTATIONANALYST = 0x0010; // REST-交通网络分析服务
	public static final int RESTTRAFFICTRANSFERANALYST = 0x0020; // REST-交通换乘分析服务
	public static final int WCS111 = 0x0040; // OGC-网络覆盖服务1.1.1
	public static final int WCS112 = 0x0080; // OGC-网络覆盖服务1.1.2
	public static final int WFS100 = 0x0100; // OGC-Web要素服务1.0.0
	public static final int WMS111 = 0x0200; // OGC-Web地图服务1.1.1
	public static final int WMS130 = 0x0400; // OGC-Web地图服务1.3.0
	public static final int WMTS100 = 0x0800; // OGC-地图瓦片服务1.0.0
	public static final int WMTSCHINA = 0x1000; // OGC-中国地图瓦片服务
	public static final int WPS100 = 0x2000; // OGC-网络处理服务

	public static String serviceTypeLocalized(String serviceType) {
		String result = "";

		if (serviceType.equalsIgnoreCase("RESTDATA")) {
			result = NetServicesProperties.getString("String_iServer_ServicesType_RestData");
		} else if (serviceType.equalsIgnoreCase("RESTMAP")) {
			result = NetServicesProperties.getString("String_iServer_ServicesType_RestMap");
		} else if (serviceType.equalsIgnoreCase("RESTREALSPACE")) {
			result = NetServicesProperties.getString("String_iServer_ServicesType_RestRealspace");
		} else if (serviceType.equalsIgnoreCase("RESTSPATIALANALYST")) {
			result = NetServicesProperties.getString("String_iServer_ServicesType_RestSpatialAnalyst");
		} else if (serviceType.equalsIgnoreCase("RESTTRANSPORTATIONANALYST")) {
			result = NetServicesProperties.getString("String_iServer_ServicesType_RestTransprotationAnalyst");
		} else if (serviceType.equalsIgnoreCase("WCS111")) {
			result = NetServicesProperties.getString("String_iServer_ServicesType_WCS111");
		} else if (serviceType.equalsIgnoreCase("WCS112")) {
			result = NetServicesProperties.getString("String_iServer_ServicesType_WCS112");
		} else if (serviceType.equalsIgnoreCase("WFS100")) {
			result = NetServicesProperties.getString("String_iServer_ServicesType_WFS100");
		} else if (serviceType.equalsIgnoreCase("WMS111")) {
			result = NetServicesProperties.getString("String_iServer_ServicesType_WMS111");
		} else if (serviceType.equalsIgnoreCase("WMS130")) {
			result = NetServicesProperties.getString("String_iServer_ServicesType_WMS130");
		} else if (serviceType.equalsIgnoreCase("WMTS100")) {
			result = NetServicesProperties.getString("String_iServer_ServicesType_WMTS100");
		} else if (serviceType.equalsIgnoreCase("WMTSCHINA")) {
			result = NetServicesProperties.getString("String_iServer_ServicesType_WMTSCHINA");
		} else if (serviceType.equalsIgnoreCase("WPS100")) {
			result = NetServicesProperties.getString("String_iServer_ServicesType_WPS100");
		} else {
			result = serviceType;
		}
		return result;
	}
}
