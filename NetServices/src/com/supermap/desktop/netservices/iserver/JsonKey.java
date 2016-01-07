package com.supermap.desktop.netservices.iserver;

/**
 * 发布 iServer 服务的各种响应结果的 Key值
 * 
 * @author highsad
 *
 */
public class JsonKey {

	public class CreateUploadResponse {
		public static final String SUCCESS = "succeed";
		public static final String POST_RESULT_TYPE = "postResultType";
		public static final String NEW_RESOURCE_LOCATION = "newResourceLocation";
		public static final String NEW_RESOURCE_ID = "newResourceID";
	}

	public class ReleaseWorkspaceResponse {
		public static final String SERVICE_ADDRESS = "serviceAddress";
		public static final String SERVICE_TYPE = "serviceType";
	}

	public class RestType {
		public static final String RESTDATA = "RESTDATA";
		public static final String RESTMAP = "RESTMAP";
		public static final String RESTREALSPACE = "RESTREALSPACE";
		public static final String RESTSPATIALANALYST = "RESTSPATIALANALYST";
		public static final String RESTTRANSPORTATIONANALYST = "RESTTRANSPORTATIONANALYST";
		public static final String WCS111 = "WCS111";
		public static final String WCS112 = "WCS112";
		public static final String WFS100 = "WFS100";
		public static final String WMS111 = "WMS111";
		public static final String WMS130 = "WMS130";
		public static final String WMTS100 = "WMTS100";
		public static final String WMTSCHINA = "WMTSCHINA";
		public static final String WPS100 = "WPS100";
	}
}
