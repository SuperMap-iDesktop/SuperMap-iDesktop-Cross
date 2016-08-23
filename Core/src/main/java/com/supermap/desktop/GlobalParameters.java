package com.supermap.desktop;

import com.supermap.data.AltitudeMode;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.utilities.AltitudeModeUtilities;
import com.supermap.desktop.utilities.DoubleUtilities;
import com.supermap.desktop.utilities.FileUtilities;
import com.supermap.desktop.utilities.PathUtilities;
import com.supermap.desktop.utilities.StringUtilities;
import com.supermap.desktop.utilities.XmlUtilities;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.math.BigDecimal;
import java.util.HashMap;

/**
 * 　　　　　　　　┏┓　　　┏┓+ +
 * 　　　　　　　┏┛┻━━━┛┻┓ + +
 * 　　　　　　　┃　　　　　　　┃
 * 　　　　　　　┃　　　━　　　┃ ++ + + +
 * 　　　　　　 ████━████ ┃+
 * 　　　　　　　┃　　　　　　　┃ +
 * 　　　　　　　┃　　　┻　　　┃
 * 　　　　　　　┃　　　　　　　┃ + +
 * 　　　　　　　┗━┓　　　┏━┛
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃ + + + +
 * 　　　　　　　　　┃　　　┃　　　　Code is far away from bug with the animal protecting
 * 　　　　　　　　　┃　　　┃ + 　　　　神兽保佑,代码无bug
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃　　+
 * 　　　　　　　　　┃　 　　┗━━━┓ + +
 * 　　　　　　　　　┃ 　　　　　　　┣┓
 * 　　　　　　　　　┃ 　　　　　　　┏┛
 * 　　　　　　　　　┗┓┓┏━┳┓┏┛ + + + +
 * 　　　　　　　　　　┃┫┫　┃┫┫
 * 　　　　　　　　　　┗┻┛　┗┻┛+ + + +
 * <p>
 * 初始化资源，节点名称不能有 "_" 字符
 */

public class GlobalParameters {

	private static String startupXml;
	private static final String startupFileName = "SuperMap.Desktop.Startup.xml";

	private GlobalParameters() {
		// do nothing
	}

/**
 * 获取或设置一个值，指示是否显示启动画面，初始值为true。
 *
 * @return
 */
/*
	private static Boolean g_showSplash = true;

	public static Boolean isShowSplash() {
		return g_showSplash;
	}

	public static void setShowSplash(Boolean value) {
		g_showSplash = value;
	}

	*//**
	 * 获取或设置启动画面的图像路径，仅在ShowSplash为true时有效，初始值为""。
	 *
	 * @return
	 *//*
	private static String g_splashFilePath = "";

	public static String getSplashFilePath() {
		return g_splashFilePath;
	}

	public static void setSplashFilePath(String value) {
		g_splashFilePath = value;
	}

	*//**
	 * 获取或设置一个值，指示是否自动新建窗口浏览数据集数据，初始值为true。
	 *//*
	private static Boolean g_autoNewWindow = true;

	public static Boolean isAutoNewWindow() {
		return g_autoNewWindow;
	}

	public static void setAutoNewWindow(Boolean value) {
		g_autoNewWindow = value;
	}

	*//**
	 * 获取或设置一个值，指示是否自动关闭没有图层的地图窗口，初始值为false。
	 *//*
	private static Boolean g_autoCloseWindow = false;

	public static Boolean isAutoCloseWindow() {
		return g_autoCloseWindow;
	}

	public static void setAutoCloseWindow(Boolean value) {
		g_autoCloseWindow = value;
	}

	*//**
	 * 获取或设置一个值，指示窗口关闭时是否提示保存，初始值为true。
	 *//*
	private static Boolean g_showFormClosingInfo = true;

	public static Boolean isShowFormClosingInfo() {
		return g_showFormClosingInfo;
	}

	public static void setShowFormClosingInfo(Boolean value) {
		g_showFormClosingInfo = value;
	}

	*//**
	 * 获取或设置一个值，指示工作空间关闭时是否提示保存，初始值为true。
	 *//*
	private static Boolean g_showWorkspaceClosingInfo = true;

	public static Boolean isShowWorkspaceClosingInfo() {
		return g_showWorkspaceClosingInfo;
	}

	public static void setShowWorkspaceClosingInfo(Boolean value) {
		g_showWorkspaceClosingInfo = value;
	}

	*//**
	 * 获取或设置一个值，指示有输出提示时是否自动弹出输出窗口，初始值为true。
	 *//*
	private static Boolean g_autoShowOutputPanel = true;

	public static Boolean isAutoShowOutputPanel() {
		return g_autoShowOutputPanel;
	}

	public static void setAutoShowOutputPanel(Boolean value) {
		g_autoShowOutputPanel = value;
	}

	*//**
	 * 获取或设置一个值，指示是否自动隐藏系统字段，初始值为false。
	 *//*
	private static Boolean g_hideSysFields = false;

	public static Boolean isHideSysFields() {
		return g_hideSysFields;
	}

	public static void setHideSysFields(Boolean value) {
		g_hideSysFields = value;
	}

	*//**
	 * 获取或设置一个值，指示是否参加用户体验计划，初始值为true。
	 *//*
	private static Boolean g_launchUserExperiencePlan = true;

	public static Boolean isLaunchUserExperiencePlan() {
		return g_launchUserExperiencePlan;
	}

	public static void setLaunchUserExperiencePlan(Boolean value) {
		g_launchUserExperiencePlan = value;
	}

	*//**
	 * 获取或设置一个值，指示是否开启自动更新，初始值为true
	 *//*
	private static Boolean g_launchAutoUpdate = true;

	public static Boolean isLaunchAutoUpdate() {
		return g_launchAutoUpdate;
	}

	public static void setLaunchAutoUpdate(Boolean value) {
		g_launchAutoUpdate = value;
	}

	*//**
	 * 获取或设置一个值，指示新建场景时是否自动加载框架数据，初始值为false。
	 *//*
	private static Boolean g_autoLoadFrameData = false;

	public static Boolean isAutoLoadFrameData() {
		return g_autoLoadFrameData;
	}

	public static void setAutoLoadFrameData(Boolean value) {
		g_autoLoadFrameData = value;
	}

	*//**
	 * 获取或设置一个值，指示是否显示导航条，初始值为true。
	 *//*
	private static Boolean g_showNavigationBar = false;

	public static Boolean isShowNavigationBar() {
		return g_showNavigationBar;
	}

	public static void setShowNavigationBar(Boolean value) {
		g_showNavigationBar = value;
	}

	*//**
	 * 获取或设置一个值，指示是否显示工具提示，初始值为true。
	 *//*
	private static Boolean g_showScreenTip = true;

	public static Boolean isShowScreenTip() {
		return g_showScreenTip;
	}

	public static void setShowScreenTip(Boolean value) {
		g_showScreenTip = value;
	}

	*/
	/**
	 * 获取或设置桌面标题，初始值为：SuperMap iDesktop 7C。
	 */
	private static String desktopTitle = "";

	public static String getDesktopTitle() {
		if (StringUtilities.isNullOrEmpty(desktopTitle)) {
			return CoreProperties.getString("String_MessageBox_Title");
		}
		return desktopTitle;
	}

	public static void setDesktopTitle(String title) {
		if (title.equals(CoreProperties.getString("String_MessageBox_Title"))) {
			title = "";
		}
		desktopTitle = title;
	}

	/**
	 * 获取或设置数值的可见小数位数，初始值为4。
	 *//*
	private static int m_nDecimalPlaces = 4;

	public static int getDecimalPlaces() {
		return m_nDecimalPlaces;
	}

	public static void setDecimalPlaces(int value) {
		m_nDecimalPlaces = value;
	}

	*//**
	 * 获取或设置文件缓存路径，初始值为：./Cache/DatasetCache/。
	 *//*
	public static String getFileCacheFolder() {
		return Environment.getFileCacheFolder();
	}

	public static void setFileCacheFolder(String value) {
		Environment.setFileCacheFolder(value);
	}

	*/
	//region isOutPutLog
	/**
	 * 获取或设置一个值，指示是否生成运行日志，初始值为false。
	 */
	private static boolean outputToLog = false;

	/**
	 * 是否输出log日志 默认为false
	 *
	 * @return 是否输出
	 */
	public static boolean isOutPutToLog() {
		return outputToLog;
	}

	public static void setOutputToLog(boolean value) {
		outputToLog = value;
	}
	//endregion

	//region logFolder
	private static String logFolder = "./Log/Desktop";

	/**
	 * 获取日志输出路径，默认路径为jar包所在位置/bin/Log/Desktop
	 *
	 * @return 当前设置的相对路径
	 */
	public static String getLogFolder() {
		return logFolder;
	}

	/**
	 * 设置日志输出路径
	 * 如果value为空，则使用配置文件中的值
	 *
	 * @param value 日志路径
	 */
	public static void setLogFolder(String value) {
		if (value != null) {
			logFolder = value;
		}
		System.setProperty("com.supermap.desktop.log4j.home", logFolder);
	}
	//endregion

	//region LogInformation
	private static boolean isLogInformation = true;

	public static void setLogInformation(boolean value) {
		isLogInformation = value;
	}

	public static boolean isLogInformation() {
		return isLogInformation;
	}
	//endregion

	//region logException
	private static boolean isLogException = false;

	public static void setLogException(boolean isLogException) {
		GlobalParameters.isLogException = isLogException;
	}

	public static boolean isLogException() {
		return GlobalParameters.isLogException;
	}
	//endregion
	/**
	 * 获取或设置一个值，指示是否自动检查工作空间版本，初始值为true。
	 *//*
	private static Boolean g_autoCheckWorkspaceVersion = true;

	public static Boolean isAutoCheckWorkspaceVersion() {
		return g_autoCheckWorkspaceVersion;
	}

	public static void setAutoCheckWorkspaceVersion(Boolean value) {
		g_autoCheckWorkspaceVersion = value;
	}

	*//**
	 * 获取或设置程序输出信息的类型，初始值为InfoType.Information。
	 *//*
	private static InfoType m_outputInfoType;

	public static InfoType getOutputInfoType() {
		return m_outputInfoType;
	}

	public static void setOutputInfoType(InfoType value) {
		m_outputInfoType = value;
	}

	*//**
	 * 获取或设置一个值，指示是否显示启动画面，初始值为true。
	 *//*
	private static Boolean g_useRebackItemCount = true;

	public static Boolean isUseRebackItemCount() {
		return g_useRebackItemCount;
	}

	public static void setUseRebackItemCount(Boolean value) {
		g_useRebackItemCount = value;
	}

	*//**
	 * 获取或设置一个值，指示是否显示启动画面，初始值为true。
	 *//*
	private static int g_rebackItemCount = 1000000;

	public static int getRebackItemCount() {
		return g_rebackItemCount;
	}

	public static void setRebackItemCount(int value) {
		g_rebackItemCount = value;
	}

	*//**
	 * 获取或设置一个值，指示是否限制可回退次数，初始值为true。
	 *//*
	private static Boolean g_useRebackTimes = true;

	public static Boolean isUseRebackTimes() {
		return g_useRebackTimes;
	}

	public static void setUseRebackTimes(Boolean value) {
		g_useRebackTimes = value;
	}

	*//**
	 * 获取或设置最大回退次数，仅在UseRebackTimes为true时有效，初始值为1000。
	 *//*
	private static int g_rebackTimes = 1000;

	public static int getRebackTimes() {
		return g_rebackTimes;
	}

	public static void setRebackTimes(int value) {
		g_rebackTimes = value;
	}

	*//**
	 * 获取或设置一个值，指示是否显示启动画面，初始值为true。
	 *//*
	private static int g_maxVisibleNodeCount = 3600000;

	public static int getMaxVisibleNodeCount() {
		return g_maxVisibleNodeCount;
	}

	public static void setMaxVisibleNodeCount(int value) {
		g_maxVisibleNodeCount = value;
	}

	*//**
	 * 获取或设置对象选择模式，初始值为0。
	 *//*
	private static int g_positiveSelect = 0;

	public static int getPositiveSelect() {
		return g_positiveSelect;
	}

	public static void setPositiveSelect(int value) {
		g_positiveSelect = value;
	}

	*//**
	 * 获取或设置一个值，指示对象绘制时是否开启参数化绘制，初始值为true。
	 *//*
	private static Boolean g_parameterEdit = true;

	public static Boolean isParameterEdit() {
		return g_parameterEdit;
	}

	public static void setParameterEdit(Boolean value) {
		g_parameterEdit = value;
	}

	*/
	/**
	 * 获取或设置一个值，指示专题图是否即时刷新，初始值为true。
	 *//*
	private static Boolean g_themeInstantRefresh = true;

	public static Boolean isThemeInstantRefresh() {
		return g_themeInstantRefresh;
	}

	public static void setThemeInstantRefresh(Boolean value) {
		g_themeInstantRefresh = value;
	}*/


	//region 加载StartUp文件后存放于resources中，属性对应的路径为key值，以'_'隔开。如_startup_splash_script。
	private static HashMap<String, NamedNodeMap> resources;

	public static void initResource() {
		if (resources != null) {
			return;
		}
		String appDataPath = FileUtilities.getAppDataPath();
		String defaultFilePath = appDataPath + "Startup" + File.separator + startupFileName;
		String startUpFile = PathUtilities.getFullPathName(_XMLTag.FILE_STARTUP_XML, false);
		String loadXmlPath = null;
		// loadXmlPath为加载的配置文件，startupXml为保存的路径
		if (appDataPath == null) {
			if (new File(startUpFile).exists()) {
				startupXml = startUpFile;
			} else {
				return;
			}
		} else {
			if (!new File(defaultFilePath).exists()) {
				if (new File(startUpFile).exists()) {
					//
					loadXmlPath = FileUtilities.copyFile(startUpFile, defaultFilePath, true) ? startUpFile : defaultFilePath;
					startupXml = defaultFilePath;
				} else {
					// 路径获得，文件不存在，包内文件也不存在，留着路径后面保存时直接保存
					startupXml = defaultFilePath;
					return;
				}
			} else {
				// 文件存在直接读取
				startupXml = defaultFilePath;
			}
		}
		if (loadXmlPath == null) {
			loadXmlPath = startupXml;
		}

		Document startupDoc = XmlUtilities.getDocument(loadXmlPath);
		resources = new HashMap<>();
		if (startupDoc != null) {
			NodeList childNodes = startupDoc.getChildNodes();
			for (int i = 0; i < childNodes.getLength(); i++) {
				if (childNodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
					addResources("", childNodes.item(i));
				}
			}
		}
		init();
	}

	private static void addResources(String info, Node node) {
		NodeList childNodes = node.getChildNodes();
		info = info + "_" + node.getNodeName();
		if (getNodeChildCount(node) > 0) {
			for (int i = 0; i < childNodes.getLength(); i++) {
				if (childNodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
					addResources(info, childNodes.item(i));
				}
			}
		} else if (!StringUtilities.isNullOrEmpty(node.getNodeName()) && node.getNodeType() == Node.ELEMENT_NODE) {
			resources.put(info, node.getAttributes());
		}
	}


	/**
	 * 获得节点非空子节点个数
	 *
	 * @param node 需要计算的节点
	 * @return 非空子节点个数
	 */
	private static int getNodeChildCount(Node node) {
		int count = 0;
		for (int i = 0; i < node.getChildNodes().getLength(); i++) {
			if (node.getChildNodes().item(i).getNodeType() == Node.ELEMENT_NODE) {
				++count;
			}
		}
		return count;
	}

	/**
	 * 重新加载资源文件
	 */
	public static void reloadResources() {
		resources = null;
		initResource();
	}

	/**
	 * 根据节点路径和属性名称来得到对应的值
	 *
	 * @param nodePath      节点路径，以'_'隔开，如:_startup_splash_script
	 * @param resourcesName 属性名称
	 * @return 值
	 */
	private static String getValue(String nodePath, String resourcesName) {
		String result = null;
		if (resources != null) {
			NamedNodeMap nodeMap = resources.get(nodePath);
			if (nodeMap != null) {
				Node node = nodeMap.getNamedItem(resourcesName);
				if (node != null) {
					result = node.getNodeValue();
				}
			}
		}
		return result;
	}

	//endregion
	private static void init() {
		initLogInfo();
		initCamera();
		initDesktopTitle();
		initWorkspaceInfo();
		initThemeRefresh();
		initMaxVisibleVertex();
		initEdit();
		// TODO: 2016/3/29 新增节点在此初始化
	}


	private static void initDesktopTitle() {
		String value = getValue("_startup_mainForm", "text");
		if (value != null) {
			if (value.equals(CoreProperties.getString("String_Old_Title"))) {
				value = "";
			}
			setDesktopTitle(value);
		}
	}


	private static void initLogInfo() {
		// 日志路径
//		String value = getValue("_startup_log", "logFolder");
		setLogFolder(PathUtilities.getFullPathName(getLogFolder(), false));
		boolean booleanValue;

		// 日志是否输出
		String value = getValue("_startup_log", "outputToLog");
		if (value != null) {
			booleanValue = Boolean.valueOf(value);
			setOutputToLog(booleanValue);
		}

		// 操作日志
		value = getValue("_startup_InfoType", "Information");
		if (value != null) {
			booleanValue = Boolean.valueOf(value);
			setLogInformation(booleanValue);
		}

		// 异常日志
		value = getValue("_startup_InfoType", "Exception");
		if (value != null) {
			booleanValue = Boolean.valueOf(value);
			setLogException(booleanValue);
		}
	}

	//region 相机相关设置
	private static void initCamera() {
		String value;
		value = getValue("_startup_camera", "altitude");
		if (value != null && DoubleUtilities.isDouble(value)) {
			setCameraAltitude(Double.valueOf(value));
		}
		value = getValue("_startup_camera", "altitudeMode");
		if (value != null) {
			setCameraAltitudeMode(AltitudeModeUtilities.getAltitudeMode(value));
		}
		value = getValue("_startup_camera", "heading");
		if (value != null) {
			setCameraHeading(Double.valueOf(value));
		}
		value = getValue("_startup_camera", "latitude");
		if (value != null && DoubleUtilities.isDouble(value)) {
			setCameraLatitude(Double.valueOf(value));
		}
		value = getValue("_startup_camera", "longitude");
		if (value != null && DoubleUtilities.isDouble(value)) {
			setCameraLongitude(Double.valueOf(value));
		}

		value = getValue("_startup_camera", "tilt");
		if (value != null && DoubleUtilities.isDouble(value)) {
			setCameraTilt(Double.valueOf(value));
		}
	}


	//region 相机高度模式
	private static AltitudeMode cameraAltitudeMode = AltitudeMode.ABSOLUTE;

	private static void setCameraAltitudeMode(AltitudeMode altitudeMode) {
		cameraAltitudeMode = altitudeMode;
	}

	public static AltitudeMode getCameraAltitudeMode() {
		return cameraAltitudeMode;
	}
	//endregion

	//region 相机方位角
	private static double cameraHeading = 0;

	private static void setCameraHeading(Double aDouble) {
		cameraHeading = aDouble;
	}

	public static double getCameraHeading() {
		return cameraHeading;
	}
	//endregion

	//region 相机倾斜角
	private static double cameraTilt = 0;

	private static void setCameraTilt(Double aDouble) {
		cameraTilt = aDouble;
	}

	public static double getCameraTilt() {
		return cameraTilt;
	}
	//endregion

	//region 相机维度
	private static double cameraLatitude = 0.0;

	private static void setCameraLatitude(Double aDouble) {
		cameraLatitude = aDouble;
	}

	public static double getCameraLatitude() {
		return cameraLatitude;
	}
	//endregion

	//region 相机经度
	private static double cameraLongitude = 0;

	private static void setCameraLongitude(Double aDouble) {
		cameraLongitude = aDouble;
	}

	public static double getCameraLongitude() {
		return cameraLongitude;
	}
	//endregion

	//region 相机高度
	private static double cameraAltitude = 10288740;

	public static void setCameraAltitude(Double aDouble) {
		cameraAltitude = aDouble;
	}

	public static double getCameraAltitude() {
		return cameraAltitude;
	}
	//endregion
	//endregion

	//region 工作空间相关提示
	private static void initWorkspaceInfo() {
		initIsShowDataInNewWindow();
		initIsShowFormClosingInfo();
		initAutoCloseEmptyWindow();
		initCloseMemoryDatasourceNotify();
		initWorkspaceCloseNotify();
		initWorkspaceRecovery();
//		initIsSaveSymbol();
//		initSymbolSaveTime();
//		initWorkspaceAutoSave();
//		initWorkspaceAutoSaveTime();
	}


	//region 自动新建窗口浏览数据集数据
	private static boolean isShowDataInNewWindow = true;

	private static void initIsShowDataInNewWindow() {
		String value = getValue("_startup_dataWindow", "autoNewWindow");
		if (value != null) {
			boolean result = Boolean.valueOf(value);
			setIsShowDataInNewWindow(result);
		}
	}

	public static boolean isShowDataInNewWindow() {
		return isShowDataInNewWindow;
	}


	public static void setIsShowDataInNewWindow(boolean isShowDataInNewWindow) {
		GlobalParameters.isShowDataInNewWindow = isShowDataInNewWindow;
	}

	//endregion
	//region 自动关闭空窗口
	private static boolean isAutoCloseEmptyWindow = false;

	private static void initAutoCloseEmptyWindow() {
		String value = getValue("_startup_dataWindow", "autoCloseEmptyWindow");
		if (value != null) {
			boolean result = Boolean.valueOf(value);
			setIsAutoCloseEmptyWindow(result);
		}
	}

	public static boolean isAutoCloseEmptyWindow() {
		return isAutoCloseEmptyWindow;
	}


	public static void setIsAutoCloseEmptyWindow(boolean isAutoCloseEmptyWindow) {
		GlobalParameters.isAutoCloseEmptyWindow = isAutoCloseEmptyWindow;
	}

	//endregion
	//region 关闭窗口提示保存
	private static void initIsShowFormClosingInfo() {
		String value = getValue("_startup_dataWindow", "showCloseInfoForm");
		if (value != null) {
			boolean result = Boolean.valueOf(value);
			setIsShowFormClosingInfo(result);
		}
	}

	private static boolean isShowFormClosingInfo = true;

	public static void setIsShowFormClosingInfo(boolean isShowFormClosingInfo) {
		GlobalParameters.isShowFormClosingInfo = isShowFormClosingInfo;
	}

	public static boolean isShowFormClosingInfo() {
		return isShowFormClosingInfo;
	}
	//endregion
	//region 关闭工作空间提示有内存数据源

	private static boolean isCloseMemoryDatasourceNotify = true;

	private static void initCloseMemoryDatasourceNotify() {
		String value = getValue("_startup_workspace", "closeMemoryDatasourceNotify");
		if (value != null) {
			boolean result = Boolean.valueOf(value);
			setIsCloseMemoryDatasourceNotify(result);
		}
	}

	public static boolean isCloseMemoryDatasourceNotify() {
		return isCloseMemoryDatasourceNotify;
	}

	public static void setIsCloseMemoryDatasourceNotify(boolean isCloseMemoryDatasourceNotify) {
		GlobalParameters.isCloseMemoryDatasourceNotify = isCloseMemoryDatasourceNotify;
	}

	//endregion
	//region 关闭工作空间提示保存
	private static boolean isWorkspaceCloseNotify = true;

	private static void initWorkspaceCloseNotify() {
		String value = getValue("_startup_workspace", "closenotify");
		if (value != null) {
			boolean result = Boolean.valueOf(value);
			setIsWorkspaceCloseNotify(result);
		}
	}

	public static boolean isWorkspaceCloseNotify() {
		return isWorkspaceCloseNotify;
	}

	public static void setIsWorkspaceCloseNotify(boolean isWorkspaceCloseNotify) {
		GlobalParameters.isWorkspaceCloseNotify = isWorkspaceCloseNotify;
	}


	//endregion
	//region 工作空间崩溃恢复

	//region 是否恢复工作空间
	private static boolean isWorkspaceRecovery = true;

	private static void initWorkspaceRecovery() {
		String value = getValue("_startup_workspace", "workspaceRecovery");
		if (value != null) {
			setIsWorkspaceRecovery(Boolean.valueOf(value));
		}
	}

	public static boolean isWorkspaceRecovery() {
		return isWorkspaceRecovery;
	}

	public static void setIsWorkspaceRecovery(boolean isWorkspaceRecovery) {
		GlobalParameters.isWorkspaceRecovery = isWorkspaceRecovery;
	}
	//endregion

	//region 是否保存符号库
//	private static boolean isSaveSymbol = true;
//
//	public static void initIsSaveSymbol() {
//		String value = getValue("_startup_workspace", "symbolRecovery");
//		if (value != null) {
//			setIsSaveSymbol(Boolean.valueOf(value));
//		}
//	}
//
//	public static boolean isSaveSymbol() {
//		return isSaveSymbol;
//	}
//
//	public static void setIsSaveSymbol(boolean isSaveSymbol) {
//		GlobalParameters.isSaveSymbol = isSaveSymbol;
//	}
	//endregion

//	private static int symbolSaveTime = 60;

//	private static void initSymbolSaveTime() {
//		String value = getValue("_startup_workspace", "symbolRecoveryTime");
//		if (value != null) {
//			setSymbolSaveTime(Integer.valueOf(value));
//		}
//	}
//
//	public static void setSymbolSaveTime(int symbolSaveTime) {
//		GlobalParameters.symbolSaveTime = symbolSaveTime;
//	}
//
//	public static int getSymbolSaveTime() {
//		return symbolSaveTime;
//	}
	//endregion

	//region 自动保存工作空间暂时关闭
//	private static boolean isWorkspaceAutoSave = true;

//	private static void initWorkspaceAutoSave() {
//		String value = getValue("_startup_workspace", "workspaceAutoSave");
//		if (value != null) {
//			setIsWorkspaceAutoSave(Boolean.valueOf(value));
//		}
//	}
//
//	public static boolean isWorkspaceAutoSave() {
//		return isWorkspaceAutoSave;
//	}
//
//	public static void setIsWorkspaceAutoSave(boolean isWorkspaceAutoSave) {
//		GlobalParameters.isWorkspaceAutoSave = isWorkspaceAutoSave;
//	}
	//endregion

	//region 自动保存工作空间时间
//	private static int workspaceAutoSaveTime = 10;
//
//	private static void initWorkspaceAutoSaveTime() {
//		String value = getValue("_startup_workspace", "workspaceAutoSaveTime");
//		if (value != null) {
//			setWorkspaceAutoSaveTime(Integer.valueOf(value));
//		}
//	}
//
//	public static int getWorkspaceAutoSaveTime() {
//		return workspaceAutoSaveTime;
//	}
//
//	public static void setWorkspaceAutoSaveTime(int workspaceAutoSaveTime) {
//		GlobalParameters.workspaceAutoSaveTime = workspaceAutoSaveTime;
//	}
	//endregion
	//endregion

	//region 专题图自动刷新
	private static void initThemeRefresh() {
		String value = getValue("_startup_theme", "refresh");
		if (value != null) {
			setThemeRefresh(Boolean.valueOf(value));
		}
	}

	private static boolean themeRefresh = true;

	public static boolean isThemeRefresh() {
		return themeRefresh;
	}

	public static void setThemeRefresh(boolean themeRefresh) {
		GlobalParameters.themeRefresh = themeRefresh;
	}
	//endregion

	//region 最大可见节点数
	private static int maxVisibleVertex = 3600000;

	private static void initMaxVisibleVertex() {
		String value = getValue("_startup_maxVisibleVertex", "maxCount");
		if (value != null) {
			setMaxVisibleVertex(Integer.valueOf(value));
		}
	}

	public static int getMaxVisibleVertex() {
		return maxVisibleVertex;
	}

	public static void setMaxVisibleVertex(int maxVisibleVertex) {
		GlobalParameters.maxVisibleVertex = maxVisibleVertex;
	}
	//endregion

	//region 编辑回退设置
	private static void initEdit() {
		initPositiveselect();
	}

	//region 选择模式
	private static int positiveSelect = 0;

	private static void initPositiveselect() {
		String value = getValue("_startup_edit", "positiveselect");
		if (value != null) {
			setPositiveSelect(Integer.valueOf(value));
		}
	}

	public static int getPositiveSelect() {
		return positiveSelect;
	}

	public static void setPositiveSelect(int positiveSelect) {
		GlobalParameters.positiveSelect = positiveSelect;
	}
	//endregion

	//endregion

	public static void save() {
		if (StringUtilities.isNullOrEmpty(startupXml)) {
			return;
		}
		Document emptyDocument = XmlUtilities.getEmptyDocument();
		if (emptyDocument != null) {
			Element startup = emptyDocument.createElement("startup");
			emptyDocument.appendChild(startup);


			startup.appendChild(emptyDocument.createComment(CoreProperties.getString("String_WorkspaceComment")));
			Element workspace = emptyDocument.createElement("workspace");
			workspace.setAttribute("closenotify", String.valueOf(isWorkspaceCloseNotify));
			workspace.setAttribute("closeMemoryDatasourceNotify", String.valueOf(isCloseMemoryDatasourceNotify));
			workspace.setAttribute("workspaceRecovery", String.valueOf(isWorkspaceRecovery));
//			workspace.setAttribute("symbolRecovery", String.valueOf(isSaveSymbol));
//			workspace.setAttribute("symbolRecoveryTime", String.valueOf(symbolSaveTime));
//			workspace.setAttribute("workspaceAutoSave", String.valueOf(isWorkspaceAutoSave));
//			workspace.setAttribute("workspaceAutoSaveTime", String.valueOf(workspaceAutoSaveTime));

			startup.appendChild(workspace);

			startup.appendChild(emptyDocument.createComment(CoreProperties.getString("String_dataWindowComment")));
			Element dataWindow = emptyDocument.createElement("dataWindow");
			dataWindow.setAttribute("autoCloseEmptyWindow", String.valueOf(isAutoCloseEmptyWindow));
			dataWindow.setAttribute("autoNewWindow", String.valueOf(isShowDataInNewWindow));
			dataWindow.setAttribute("showCloseInfoForm", String.valueOf(isShowFormClosingInfo));
			startup.appendChild(dataWindow);

			startup.appendChild(emptyDocument.createComment(CoreProperties.getString("String_cameraComment")));
			Element camera = emptyDocument.createElement("camera");
			camera.setAttribute("altitude", new BigDecimal(cameraAltitude).toString());
			camera.setAttribute("altitudeMode", cameraAltitudeMode.name());
			camera.setAttribute("heading", String.valueOf(cameraHeading));
			camera.setAttribute("latitude", String.valueOf(cameraLatitude));
			camera.setAttribute("longitude", String.valueOf(cameraLongitude));
			camera.setAttribute("tilt", String.valueOf(cameraTilt));
			startup.appendChild(camera);

			startup.appendChild(emptyDocument.createComment(CoreProperties.getString("String_mainFormComment")));
			Element mainForm = emptyDocument.createElement("mainForm");
			mainForm.setAttribute("icon", "");
			mainForm.setAttribute("text", desktopTitle);
			startup.appendChild(mainForm);

			startup.appendChild(emptyDocument.createComment(CoreProperties.getString("String_logComment")));
			Element log = emptyDocument.createElement("log");
			log.setAttribute("logFolder", logFolder);
			log.setAttribute("outputToLog", String.valueOf(outputToLog));
			startup.appendChild(log);

			startup.appendChild(emptyDocument.createComment(CoreProperties.getString("String_InfoTypeComment")));
			Element infoType = emptyDocument.createElement("InfoType");
			infoType.setAttribute("Exception", String.valueOf(isLogException));
			infoType.setAttribute("Information", String.valueOf(isLogInformation));
			startup.appendChild(infoType);

			startup.appendChild(emptyDocument.createComment(CoreProperties.getString("String_themeComment")));
			Element theme = emptyDocument.createElement("theme");
			theme.setAttribute("refresh", String.valueOf(themeRefresh));
			startup.appendChild(theme);

			startup.appendChild(emptyDocument.createComment(CoreProperties.getString("String_maxVisibleVertexComment")));
			Element maxVisibleVertexNode = emptyDocument.createElement("maxVisibleVertex");
			maxVisibleVertexNode.setAttribute("maxCount", new BigDecimal(maxVisibleVertex).toString());
			startup.appendChild(maxVisibleVertexNode);

			startup.appendChild(emptyDocument.createComment(CoreProperties.getString("String_editComment")));
			Element edit = emptyDocument.createElement("edit");
			edit.setAttribute("positiveselect", String.valueOf(positiveSelect));
			startup.appendChild(edit);

			XmlUtilities.saveXml(startupXml, emptyDocument, "UTF-8");

		}
	}


}
