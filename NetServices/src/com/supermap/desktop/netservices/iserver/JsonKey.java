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

	public class ReleaseWorkspaceResponseError {
		public static final String ERROR = "error";
		public static final String ERROR_CODE = "code";
		public static final String ERROR_MESSAGE = "errorMsg";
		public static final String SUCCESS = "succeed";
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

	public class ReleasePostBody {
		public static final String WORKSPACECONNECTIONINFO = "workspaceConnectionInfo";
		public static final String SERVICES_TYPES = "servicesTypes";
		public static final String IS_DATA_EDITABLE = "isDataEditable";
		public static final String TRANSPORTATION_ANALYST_SETTING = "transportationAnalystSetting";
	}

	public class GetToken {
		public static final String USERNAME = "userName";
		public static final String PASSWORD = "password";
		public static final String CLIENTTYPE = "clientType";
		public static final String EXPIRATION = "expiration";
	}

	public class TransportationAnalystSetting {
		public static final String WEIGHTFIELDINFOS = "weightFieldInfos";
		public static final String WORKSPACE_CONNECTSTRING = "workspaceConnectString";
		public static final String DATASOURCE_NAME = "datasourceName";
		public static final String DATASET_NAME = "datasetName";
		public static final String NODEID_FIELD = "nodeIDField";
		public static final String EDGEID_FIELD = "edgeIDField";
		public static final String FROMNODEID_FIELD = "fromNodeIDField";
		public static final String TONODEID_FIELD = "toNodeIDField";
	}

	public class WeightFieldInfos {
		public static final String NAME = "name";
		public static final String FORWARD_WEIGHTFIELD = "forwardWeightField";
		public static final String BACK_WEIGHTFIELD = "backWeightField";
	}
}
