package com.supermap.desktop.netservices.iserver;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.event.EventListenerList;

import org.apache.http.HttpEntity;
//import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;
import com.supermap.data.WorkspaceConnectionInfo;
import com.supermap.data.WorkspaceType;
import com.supermap.desktop.Application;
import com.supermap.desktop.core.FileSize;
import com.supermap.desktop.core.compress.CompressEvent;
import com.supermap.desktop.core.compress.CompressListener;
import com.supermap.desktop.core.compress.Compressor;
import com.supermap.desktop.core.http.HttpPostEvent;
import com.supermap.desktop.core.http.HttpPostFile;
import com.supermap.desktop.core.http.HttpPostListener;
import com.supermap.desktop.netservices.NetServicesProperties;
import com.supermap.desktop.utilties.FileUtilties;
import com.supermap.desktop.utilties.PathUtilties;
import com.supermap.desktop.utilties.StringUtilties;

public class ServerRelease {
	private static final String RELEASE_SERVER = "http://{0}:{1}/iserver/manager/workspaces.rjson?token='{2}'";
	private static final String TOKEN_SERVER = "http://{0}:{1}/iserver/services/security/tokens.rjson";
	private static final String UPLOAD_TASKS = "http://{0}:{1}/iserver/manager/filemanager/uploadtasks.rjson?token='{2}'";
	private static final String SERVER_DATA_DIR = "../../Desktop";
	private static final String CLOUDY_CACHE = "./CloudyCache";

	private WorkspaceConnectionInfo connectionInfo;
	// @formatter:off
    //文件型工作空间发布到本地服务器，需要关闭工作空间，不然由于数据源的独占导致服务错误
    //文件型工作空间发布到远程服务器，需要上传工作空间，不存在独占，不需要关闭
    //数据库型的工作空间，它的数据源也必须是数据库型的数据源才能正确读取数据，并且，此数据源非独占
	// @formatter:on
	private int hostType;
	private String host;
	private String port;
	private boolean isEditable;
	private String adminName;
	private String adminPassword;
	private int servicesType;
	private String workspacePath;
	private ArrayList<File> files; // 需要压缩上传的文件以及文件夹（在工作空间同级目录下）
	private String remoteFilePath; // 文件型工作空间+文件型数据源发布到远程服务器上之后的工作空间路径
	private boolean isCancel = false;
	private String resultURL = "";
	private EventListenerList listenerList = new EventListenerList();

	private HttpPostListener httpPostListener = new HttpPostListener() {

		@Override
		public void httpPost(HttpPostEvent e) {
			httpPosting(e);
		}
	};

	private CompressListener compressListener = new CompressListener() {

		@Override
		public void compressing(CompressEvent e) {
			fileCompressing(e);
		}
	};

	public ServerRelease() {
		this.files = new ArrayList<File>();
	}

	public WorkspaceConnectionInfo getConnectionInfo() {
		return this.connectionInfo;
	}

	public void setConnectionInfo(WorkspaceConnectionInfo connectionInfo) {
		this.connectionInfo = connectionInfo;
	}

	public int getHostType() {
		return this.hostType;
	}

	public void setHostType(int hostType) {
		this.hostType = hostType;
	}

	public String getHost() {
		return this.host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public boolean isEditable() {
		return isEditable;
	}

	public void setEditable(boolean isEditable) {
		this.isEditable = isEditable;
	}

	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	public String getAdminPassword() {
		return adminPassword;
	}

	public void setAdminPassword(String adminPassword) {
		this.adminPassword = adminPassword;
	}

	public int getServicesType() {
		return this.servicesType;
	}

	public void setServicesType(int servicesType) {
		this.servicesType = servicesType;
	}

	public String getWorkspacePath() {
		return this.workspacePath;
	}

	public void setWorkspacePath(String workspacePath) {
		this.workspacePath = workspacePath;
	}

	public ArrayList<File> getFiles() {
		return this.files;
	}

	// public void setFiles(ArrayList<String> files) {
	// this.files = files;
	// }

	public String getResultURL() {
		return resultURL;
	}

	public String getWorkDirectory() {
		String workDirectory = "";
		File file = new File(this.workspacePath);

		if (!StringUtilties.isNullOrEmpty(this.workspacePath) && file.exists()) {
			workDirectory = file.getParent();
		}

		return workDirectory;
	}

	public boolean canCancel() {
		return (this.hostType == HostType.REMOTE && (this.connectionInfo.getType() == WorkspaceType.SMW || this.connectionInfo.getType() == WorkspaceType.SMWU
				|| this.connectionInfo.getType() == WorkspaceType.SXW || this.connectionInfo.getType() == WorkspaceType.SXWU));
	}

	public Boolean release() {
		Boolean result = false;

		try {
			if (!this.isCancel) {
				closeCurrentFileWorkspace();
				if (this.hostType == HostType.REMOTE
						&& (this.connectionInfo.getType() == WorkspaceType.SMW || this.connectionInfo.getType() == WorkspaceType.SMWU
								|| this.connectionInfo.getType() == WorkspaceType.SXW || this.connectionInfo.getType() == WorkspaceType.SXWU)) {
					result = releaseWithUploading();
				} else {
					fireFunctionProgress(0, 0, "...", NetServicesProperties.getString("String_Releasing"));// 正在发布
					result = releaseWithoutUploading();
					fireFunctionProgress(100, 100, "...", NetServicesProperties.getString("String_ReleaseCompleted"));// 发布成功
				}
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return result;
	}

	public static void clearTmp() {
		File zipCacheDirectory = new File(PathUtilties.getFullPathName(CLOUDY_CACHE + File.pathSeparator + "iServerZipCache", true));
		FileUtilties.delete(zipCacheDirectory);
	}

	public void addFunctionProgressListener(FunctionProgressListener listener) {
		this.listenerList.add(FunctionProgressListener.class, listener);
	}

	public void removeFunctionProgressListener(FunctionProgressListener listener) {
		this.listenerList.remove(FunctionProgressListener.class, listener);
	}

	private Boolean releaseWithUploading() {
		Boolean result = false;
		try {
			if (this.isCancel) {
				return false;
			}
			closeCurrentFileWorkspace();
			// 将需要上传的数据打包
			if (this.isCancel) {
				return false;
			}
			String dataPath = zipWorkspaceData();
			fireFunctionProgress(0, 49, "...", NetServicesProperties.getString("String_ZipCompleted"));// 正在进行上传预处理
			if (!StringUtilties.isNullOrEmpty(dataPath)) {
				// 创建一个上传任务
				if (this.isCancel) {
					return false;
				}
				String uploadURL = createUploadTask();
				if (!StringUtilties.isNullOrEmpty(uploadURL)) {
					// 开始上传
					if (this.isCancel) {
						return false;
					}
					String workspaceConnection = uploadTask(uploadURL, new File(dataPath));
					fireFunctionProgress(0, 99, "...", NetServicesProperties.getString("String_UploadCompleted"));// 正在发布服务
					// 发布服务
					if (this.isCancel) {
						return false;
					}
					result = releaseServerUploadFile(workspaceConnection);
					fireFunctionProgress(100, 100, "...", NetServicesProperties.getString("String_ReleaseCompleted"));
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return result;
	}

	private String uploadTask(String uploadURL, File dataFile) {
		String workspaceConnection = "";

		try {
			Application.getActiveApplication().getOutput().output(NetServicesProperties.getString("String_Uploading"));

			String fileName = MessageFormat.format("{0}/{1}", this.SERVER_DATA_DIR, dataFile.getName());
			HttpPostFile httpPostFile = new HttpPostFile(
					MessageFormat.format("{0}.Json?overwrite=true&unzip=true&toFile={1}&token={2}", uploadURL, fileName, getToken()));

			String response = httpPostFile.post(dataFile);
			if (!StringUtilties.isNullOrEmpty(response)) {
				JSONObject responseJson = JSONObject.parseObject(response);

				String dataDirectory = responseJson.getString("filePath");
				// 服务器返回的解压缩之后的路径，经常改动，有时候带了工作空间文件名，有时候没带，这里随着 iserver的版本更改可能有问题，到时候再处理
				workspaceConnection = MessageFormat.format("{0}/{1}/{2}", SERVER_DATA_DIR, dataDirectory, new File(this.connectionInfo.getServer()).getName());
				Application.getActiveApplication().getOutput().output(NetServicesProperties.getString("String_UploadCompleted"));
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}

		return workspaceConnection;
	}

	private void httpPosting(HttpPostEvent e) {
		int currentProgress = new Double(FileSize.divide(FileSize.multiply(e.getPostedSize(), 100), e.getTotalSize())).intValue();
		int totalProgress = 50 + (int) (currentProgress * 0.49);
		String currentSpeedString = MessageFormat.format(NetServicesProperties.getString("String_UploadSpeedUnit"), e.getSpeed());
		String currentMessage = MessageFormat.format(NetServicesProperties.getString("String_UploadingInfo"), e.getPostedSize().ToStringClever(),
				e.getTotalSize().ToStringClever(), currentSpeedString);
		fireFunctionProgress(currentProgress, totalProgress, currentMessage, NetServicesProperties.getString("String_Uploading"));
	}

	private boolean releaseServerUploadFile(String workspaceConnection) throws IOException {
		this.remoteFilePath = workspaceConnection;
		return releaseWithoutUploading();
	}

	private String zipWorkspaceData() {
		String zipPath = "";

		try {
			Application.getActiveApplication().getOutput().output(NetServicesProperties.getString("String_ZippingData"));
			File zipCacheDirectory = new File(PathUtilties.getFullPathName(CLOUDY_CACHE + File.pathSeparator + "iServerZipCache", true));
			if (!zipCacheDirectory.exists() || !zipCacheDirectory.isDirectory()) {
				zipCacheDirectory.mkdir();
			}

			String workspaceName = FileUtilties.getFileNameWithoutExtension(new File(this.workspacePath)) + new Date().hashCode();

			Compressor compressor = new Compressor(this.getWorkDirectory(), zipCacheDirectory.getPath(), workspaceName, this.files);
			compressor.addCompressingListener(this.compressListener);
			compressor.compress();
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(NetServicesProperties.getString("String_ZipDataFailed"));
			Application.getActiveApplication().getOutput().output(ex);
		} finally {
			Application.getActiveApplication().getOutput().output(NetServicesProperties.getString("String_ZipCompleted"));
		}

		return zipPath;
	}

	private void fileCompressing(CompressEvent e) {
		e.setCancel(this.isCancel);
		if (!e.isCancel()) {
			int totalProgress = new Double(e.getPercent() * 0.49).intValue();
			fireFunctionProgress(e.getPercent(), totalProgress,
					MessageFormat.format(NetServicesProperties.getString("String_ZipDataInfo"), e.getCurrentEntry(), e.getTotalEntry()),
					NetServicesProperties.getString("String_ZippingData"));
		}
	}

	private String createUploadTask() {
		String uploadTask = "";
		CloseableHttpClient httpClient = HttpClients.createDefault();

		try {
			Application.getActiveApplication().getOutput().output(NetServicesProperties.getString("String_PreUploading"));
			HttpPost httpPost = new HttpPost(MessageFormat.format(UPLOAD_TASKS, this.host, this.port, getToken()));
			httpPost.setConfig(RequestConfig.custom().setConnectTimeout(300000).build());

			CloseableHttpResponse response = httpClient.execute(httpPost);
			try {
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					 HttpEntity responseEntity = response.getEntity();
					
					 if (responseEntity != null) {
					 JSONObject responseJson = JSONObject.parseObject(EntityUtils.toString(responseEntity));
					
					 if (responseJson.containsKey(JsonKey.CreateUploadResponse.SUCCESS)
					 && Boolean.valueOf(responseJson.get(JsonKey.CreateUploadResponse.SUCCESS).toString())) {
					 uploadTask = responseJson.get(JsonKey.CreateUploadResponse.NEW_RESOURCE_LOCATION).toString();
					 }
					 }
				} else {
					outputHttpStatus(response.getStatusLine().getStatusCode());
				}
			} finally {
				response.close();
			}
		} catch (IOException e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				Application.getActiveApplication().getOutput().output(e);
			}
		}
		return uploadTask;
	}

	private boolean releaseWithoutUploading() throws IOException {
		Boolean result = false;
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;

		try {
			String token = getToken();
			String releaseURL = MessageFormat.format(RELEASE_SERVER, this.host, this.port, token);
			HttpPost httpPost = new HttpPost(releaseURL);

			String postData = getEntityBody();
			StringEntity stringEntity = new StringEntity(postData, ContentType.APPLICATION_JSON);
			httpPost.setEntity(stringEntity);

			response = httpClient.execute(httpPost);
			this.resultURL = EntityUtils.toString(response.getEntity());
			if (!StringUtilties.isNullOrEmpty(this.resultURL)) {
				result = true;
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);

		} finally {
			httpClient.close();
			if (response != null) {
				response.close();
			}
		}
		return result;
	}

	private String getEntityBody() {
		StringBuilder entityBody = new StringBuilder();
		entityBody.append("{");
		entityBody.append("\r\n");
		entityBody.append(getWorkspaceConnectionString());
		entityBody.append(",");
		entityBody.append("\r\n");
		entityBody.append(getServicesTypes());
		if ((this.servicesType & ServiceType.RESTDATA) == ServiceType.RESTDATA || (this.servicesType & ServiceType.WFS100) == ServiceType.WFS100
				|| (this.servicesType & ServiceType.WCS111) == ServiceType.WCS111 || (this.servicesType & ServiceType.WCS112) == ServiceType.WCS112) {
			entityBody.append(",");
			entityBody.append("\r\n");
			entityBody.append(getIsDataEditable());
		}
		entityBody.append("\r\n");
		entityBody.append("}");

		return entityBody.toString();
	}

	private String getWorkspaceConnectionString() {
		StringBuilder workspaceConnection = new StringBuilder();
		workspaceConnection.append("\"workspaceConnectionInfo\":\"");
		if (this.connectionInfo != null) {
			if (this.connectionInfo.getType() == WorkspaceType.SMW || this.connectionInfo.getType() == WorkspaceType.SMWU
					|| this.connectionInfo.getType() == WorkspaceType.SXW || this.connectionInfo.getType() == WorkspaceType.SXWU) {
				String filePath = "";
				if (this.hostType == HostType.LOCAL) {
					filePath = this.connectionInfo.getServer();
					// filePath = this.connectionInfo.getServer().replace(, Path.AltDirectorySeparatorChar);
				} else {
					filePath = this.remoteFilePath;
					// filePath = this.remoteFilePath.replace(Path.DirectorySeparatorChar, Path.AltDirectorySeparatorChar);
				}
				if (StringUtilties.isNullOrEmpty(this.connectionInfo.getPassword())) {
					workspaceConnection.append(filePath);
				} else {
					workspaceConnection.append("server=" + filePath);
					workspaceConnection.append(";");
					workspaceConnection.append("password=" + this.connectionInfo.getPassword());
				}
				workspaceConnection.append("\"");
			} else if (this.connectionInfo.getType() == WorkspaceType.ORACLE || this.connectionInfo.getType() == WorkspaceType.SQL) {
				String server = this.connectionInfo.getServer();
				// String server = this.connectionInfo.getServer().replace(Path.DirectorySeparatorChar, Path.AltDirectorySeparatorChar);
				String workspaceType = "";
				String driverBase = "null";
				if (this.connectionInfo.getType() == WorkspaceType.ORACLE) {
					workspaceType = "ORACLE";
					driverBase = "null";
				} else if (this.connectionInfo.getType() == WorkspaceType.SQL) {
					workspaceType = "SQL";
					driverBase = this.connectionInfo.getDriver();
				}
				workspaceConnection.append("server=" + server);
				workspaceConnection.append(";");
				workspaceConnection.append("username=" + this.connectionInfo.getUser());
				workspaceConnection.append(";");
				workspaceConnection.append("password=" + this.connectionInfo.getPassword());
				workspaceConnection.append(";");
				workspaceConnection.append("type=" + workspaceType);
				workspaceConnection.append(";");
				workspaceConnection.append("database=" + this.connectionInfo.getDatabase());
				workspaceConnection.append(";");
				workspaceConnection.append("name=" + this.connectionInfo.getName());
				workspaceConnection.append(";");
				workspaceConnection.append("driver=" + driverBase);
				workspaceConnection.append("\"");
			}
		}

		return workspaceConnection.toString();
	}

	private String getServicesTypes() {
		StringBuilder servicesTypes = new StringBuilder();
		servicesTypes.append("\"servicesTypes\":[");
		if ((this.servicesType & ServiceType.RESTDATA) == ServiceType.RESTDATA) {
			appendComma(servicesTypes);
			servicesTypes.append("\"RESTDATA\"");
		}
		if ((this.servicesType & ServiceType.RESTMAP) == ServiceType.RESTMAP) {
			// 分两行追加只是为了让数据结构看起来更清晰
			appendComma(servicesTypes);
			servicesTypes.append("\"RESTMAP\"");
		}
		if ((this.servicesType & ServiceType.RESTREALSPACE) == ServiceType.RESTREALSPACE) {
			appendComma(servicesTypes);
			servicesTypes.append("\"RESTREALSPACE\"");
		}
		if ((this.servicesType & ServiceType.RESTSPATIALANALYST) == ServiceType.RESTSPATIALANALYST) {
			appendComma(servicesTypes);
			servicesTypes.append("\"RESTSPATIALANALYST\"");
		}
		if ((this.servicesType & ServiceType.RESTTRANSPORTATIONANALYST) == ServiceType.RESTTRANSPORTATIONANALYST) {
			appendComma(servicesTypes);
			servicesTypes.append("\"RESTTRANSPORTATIONANALYST\"");
		}
		if ((this.servicesType & ServiceType.WCS111) == ServiceType.WCS111) {
			appendComma(servicesTypes);
			servicesTypes.append("\"WCS111\"");
		}
		if ((this.servicesType & ServiceType.WCS112) == ServiceType.WCS112) {
			appendComma(servicesTypes);
			servicesTypes.append("\"WCS112\"");
		}
		if ((this.servicesType & ServiceType.WFS100) == ServiceType.WFS100) {
			appendComma(servicesTypes);
			servicesTypes.append("\"WFS100\"");
		}
		if ((this.servicesType & ServiceType.WMS111) == ServiceType.WMS111) {
			appendComma(servicesTypes);
			servicesTypes.append("\"WMS111\"");
		}
		if ((this.servicesType & ServiceType.WMS130) == ServiceType.WMS130) {
			appendComma(servicesTypes);
			servicesTypes.append("\"WMS130\"");
		}
		if ((this.servicesType & ServiceType.WMTS100) == ServiceType.WMTS100) {
			appendComma(servicesTypes);
			servicesTypes.append("\"WMTS100\"");
		}
		if ((this.servicesType & ServiceType.WMTSCHINA) == ServiceType.WMTSCHINA) {
			appendComma(servicesTypes);
			servicesTypes.append("\"WMTSCHINA\"");
		}
		if ((this.servicesType & ServiceType.WPS100) == ServiceType.WPS100) {
			appendComma(servicesTypes);
			servicesTypes.append("\"WPS100\"");
		}
		servicesTypes.append("]");

		return servicesTypes.toString();
	}

	// 拼凑请求体的时候是否需要逗号
	private void appendComma(StringBuilder servicesTypes) {
		if (servicesTypes != null && servicesTypes.length() > 0 && servicesTypes.charAt(servicesTypes.length() - 1) != '[') {
			servicesTypes.append(",");
		}
	}

	private String getIsDataEditable() {
		StringBuilder isDataEditable = new StringBuilder();
		isDataEditable.append("\"isDataEditable\":");
		isDataEditable.append(String.valueOf(this.isEditable).toLowerCase());

		return isDataEditable.toString();
	}

	private String getToken() {
		String token = "";
		CloseableHttpClient httpClient = HttpClients.createDefault();

		try {
			HttpPost httpPost = new HttpPost(MessageFormat.format(TOKEN_SERVER, this.host, this.port));
			httpPost.setConfig(RequestConfig.custom().setConnectTimeout(120000).build());
			String postData = "{\"userName\":\"" + this.adminName + "\",\"password\":\"" + this.adminPassword + "\",\"clientType\":\"RequestIP"
					+ "\",\"expiration\":60}";
			StringEntity entity = new StringEntity(postData, ContentType.APPLICATION_JSON);
			httpPost.setEntity(entity);

			CloseableHttpResponse response = httpClient.execute(httpPost);
			try {
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					token = EntityUtils.toString(response.getEntity());
				} else {
					outputHttpStatus(response.getStatusLine().getStatusCode());
				}
			} finally {
				response.close();
			}
		} catch (IOException e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				Application.getActiveApplication().getOutput().output(e);
			}
		}
		return token;
	}

	// 文件型工作空间+文件型数据源发布到本地服务器，需要关闭工作空间，不然由于数据源的独占导致服务错误
	// 文件型工作空间+文件型数据源发布到远程服务器，需要上传工作空间以及数据源，不存在独占，不需要关闭
	// 数据库型工作空间，发布到远程服务器，它的数据源也必须是数据库型的数据源才能正确读取数据，并且，此数据源非独占，不需要关闭
	// 数据库型工作空间+文件型数据源，发布到本地服务器，数据源独占，需要关闭工作空间以及数据源
	// 数据库型工作空间+数据库型数据源，发布到本地服务器，数据源非独占，不需要关闭工作空间以及数据源
	// 这里先默认简单处理
	// 数据库型数据源不关闭，其他关闭
	private void closeCurrentFileWorkspace() {
		try {
			if (this.hostType == HostType.LOCAL) {
				if (this.connectionInfo.getType() == WorkspaceType.SMW || this.connectionInfo.getType() == WorkspaceType.SMWU
						|| this.connectionInfo.getType() == WorkspaceType.SXW || this.connectionInfo.getType() == WorkspaceType.SXWU) {
					Application.getActiveApplication().getWorkspace().close();
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private void outputHttpStatus(int httpStatus) {
		if (httpStatus == HttpStatus.SC_UNAUTHORIZED) {
			Application.getActiveApplication().getOutput().output(NetServicesProperties.getString("String_iServer_MessageStatusCode_Unauthorized"));
		}
		if (httpStatus == HttpStatus.SC_NOT_FOUND) {
			Application.getActiveApplication().getOutput().output(NetServicesProperties.getString("NetServicesResources.String_IserverNeedUpdate"));
		}
	}

	private void fireFunctionProgress(int currentProgress, int totalProgress, String currentMessage, String totalMessage) {
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == FunctionProgressListener.class) {
				((FunctionProgressListener) listeners[i + 1])
						.functionProgress(new FunctionProgressEvent(this, totalProgress, currentProgress, currentMessage, totalMessage));
			}
		}
	}
}
