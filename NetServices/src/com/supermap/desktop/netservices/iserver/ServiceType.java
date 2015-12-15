package com.supermap.desktop.netservices.iserver;

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
}
