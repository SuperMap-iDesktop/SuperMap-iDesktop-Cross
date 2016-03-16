package com.supermap.desktop;

import com.supermap.desktop.utilties.SystemPropertyUtilties;

public class _XMLTag {

	public static final String g_ScriptCodeFlag = "![CDATA[";

	// 配置文件标签名称
	public static final String g_Empty = "";
	public static final String g_NodePluginName = "plugin";
	public static final String g_NodeToolbars = "toolbars";
	public static final String g_NodeToolbar = "toolbar";
	public static final String g_NodeDockbars = "dockbars";
	public static final String g_NodeDockbarGroup = "dockbarGroup";
	public static final String g_NodeDockbar = "dockbar";
	public static final String g_NodeStartMenu = "startMenu";
	public static final String g_NodeStatusbar = "statusbar";
	public static final String g_NodeStatusbars = "statusbars";
	public static final String g_NodeGroup = "group";
	public static final String g_NodeFrameMenus = "frameMenus";
	public static final String g_NodeFrameMenu = "frameMenu";
	public static final String g_NodeContextMenu = "contextMenu";
	public static final String g_NodeContextMenus = "contextMenus";
	public static final String g_NodeProcess = "process";
	public static final String g_NodeProcesses = "processes";
	public static final String g_NodeProcessGroup = "processGroup";

	public static final String g_NodeSubItems = "subItems";
	public static final String g_NodeGalleryItems = "galleryItems";

	public static final String g_NodeColor = "color";
	public static final String g_NodeAutoColor = "autoButton";
	public static final String g_NodeMoreColor = "moreColor";
	public static final String g_NodeTransparencyColor = "transparency";

	// 帮助系统需要使用的节点
	public static final String g_NodeContent = "content";
	public static final String g_NodeIndex = "index";
	public static final String g_NodeControls = "controls";
	public static final String g_NodeBottomButtons = "bottomButtons";

	public static final String g_OnAction = "onAction";
	public static final String g_Runtime = "runtime";
	public static final String g_UIDefinitionName = "uiDefinition";
	public static final String g_TypeName = "type";

	public static final String g_ControlBoxItem = "box";
	public static final String g_ControlButton = "button";
	public static final String g_ControlButtonDropdown = "buttonDropdown";
	public static final String g_ControlDockbarName = "dockbar";
	public static final String g_ControlComboBox = "comboBox";
	public static final String g_ControlEditBox = "textBox";
	public static final String g_ControlLabel = "label";
	public static final String g_ControlSeparator = "separator";

	public static final String g_AttributionName = "name";
	public static final String g_AttributionAuthor = "author";
	public static final String g_AttributionURL = "url";
	public static final String g_AttributionDescription = "description";
	public static final String g_AttributionBundleName = "bundleName";
	public static final String g_AttributionCheckState = "checkState";
	public static final String g_AttributionDocksite = "docksite";
	public static final String g_AttributionDockstate = "dockstate";
	public static final String g_AttributionFloatingLocation = "floatingLocation";
	public static final String g_AttributionFormClass = "formClass";
	public static final String g_AttributionID = "id";
	public static final String g_AttributionIndex = "index";
	public static final String g_AttributionImage = "image";
	public static final String g_AttributionImagePosition = "imagePosition";
	public static final String g_AttributionLabel = "label";
	public static final String g_AttributionLayoutStyle = "layoutStyle";
	public static final String g_AttributionSize = "size";
	public static final String g_AttributionVisible = "visible";
	public static final String g_AttributionDropDownStyle = "dropDownStyle";
	public static final String g_AttributionAutoChangeAction = "autoChangeAction";
	public static final String g_AttributionGroupStyle = "groupStyle";
	public static final String g_AttributionScreenTip = "screenTip";
	public static final String g_AttributionScreenTipImage = "screenTipImage";
	public static final String g_AttributionItemsTextAlignment = "itemsTextAlignment";
	public static final String g_AttributionIsShowTransparentColor = "isShowTransparentColor";

	public static final String g_AttributionWidth = "width";
	public static final String g_AttributionAutoHide = "autoHide";

	public static final String g_AttributionReadOnly = "readOnly";
	public static final String g_AttributionItemSpace = "itemSpace";
	public static final String g_AttributionShortcutKey = "shortcutKey";
	public static final String g_AttributionMaxValue = "maxValue";
	public static final String g_AttributionMinValue = "minValue";
	public static final String g_AttributionValue = "value";
	public static final String g_AttributionIncrement = "increment";

	public static final String g_AttributionGalleryItemWidth = "galleryItemWidth";
	public static final String g_AttributionGalleryMaxRows = "galleryMaxRows";
	public static final String g_AttributionGalleryMaxColumns = "galleryMaxColumns";
	public static final String g_AttributionCheckBoxStyle = "checkBoxStyle";
	public static final String g_AttributionControl = "control";
	public static final String g_AttributionCodeType = "codeType";
	public static final String g_AttributionCustomProperty = "customProperty";
	public static final String g_AttributionMenuStrip = "menuStrip";
	public static final String g_AttributionUpDownStyle = "upDownStyle";

	public static final String g_ValueFalse = "false";
	public static final String g_ValueTrue = "true";

	public static final String g_ValueDocked = "docked";
	public static final String g_ValueFloating = "floating";
	public static final String g_ValueVertical = "vertical";
	public static final String g_ValueHorizontal = "horizontal";
	public static final String g_ValueTab = "tab";

	public static final String g_ValueDocument = "document";
	public static final String g_ValueTop = "top";
	public static final String g_ValueBottom = "bottom";
	public static final String g_ValueLeft = "left";
	public static final String g_ValueRight = "right";

	public static final String g_ValueSimple = "simple";
	public static final String g_ValueDropDown = "dropDown";
	public static final String g_ValueDropDownList = "dropDownList";

	public static final String g_ValueIndeterminate = "indeterminate";
	public static final String g_ValueChecked = "checked";
	public static final String g_ValueUnchecked = "unchecked";

	public static final String g_ValueNormal = "normal";
	public static final String g_ValueLarge = "large";

	public static final String g_ValueCheckBox = "checkBox";
	public static final String g_ValueRadioButton = "radioButton";

	public static final String g_ValueCodeType_VB = "vb";
	public static final String g_ValueCodeType_CS = "cs";

	// startup里面使用的一些标签
	public static final String g_AttributionDefault = "default";
	public static final String g_AttributionXMLNamespace = "xmlns";
	public static final String g_AttributionHelpOnlineRoot = "helpOnlineRoot";
	public static final String g_AttributionHelpLocalRoot = "helpLocalRoot";
	public static final String g_AttributionHelpURL = "helpURL";

	public static final String g_AttributionDepth = "depth";
	public static final String g_AttributionRed = "r";
	public static final String g_AttributionGreed = "g";
	public static final String g_AttributionBlue = "b";

	public static final String FILE_STARTUP_XML = "../Configuration/SuperMap.Desktop.Startup.xml";
	public static final String RECENT_FILE_XML = "../Configuration/SuperMap.Desktop.RecentFile.xml";
	public static final String g_WorkEnvironmentsXML = "../Configuration/SuperMap.Desktop.WorkEnvironments.xml";
	public static final String g_FileLogoBMP = "Logo_Desktop.gif";
	public static final String g_FileScriptConfig = "Default/SuperMap.Desktop.Script.config";
	public static final String g_FileConfigExt = "*.config";
	public static final String g_FolderWorkEnvironment = "../WorkEnvironment/";
	public static final String g_FolderThemeStyle = "../Templates/ThemeStyle/";

	public static final String g_ValueXMLNamespace = "http://www.supermap.com.cn/desktop";

	public static final String g_NodeStartup = "startup";
	public static final String g_AttributionEnabled = "enabled";
	// <splash enabled="true"></splash>
	public static final String g_NodeSplash = "splash";

	public static final String g_NodeScript = "script";
	public static final String g_NodeBackgroundimage = "backgroundimage";
	public static final String g_NodeSplashItems = "splashItems";
	public static final String g_NodeMessage = "message";
	public static final String g_NodeImage = "image";

	public static final String g_AttributionLocation = "location";
	public static final String g_AttributionSplashFontName = "fontName";
	public static final String g_AttributionSplashFontSize = "fontSize";
	public static final String g_AttributionBold = "bold";
	public static final String g_AttributionTextColor = "textColor";

	public static final String g_AttributionTransparentColor = "transparentColor";
	public static final String g_AttributionForMessage = "forMessage";

	public static final String g_ValueLargeImagePostfix = ".large";
	public static final String g_NodeWorkEnvironment = "workEnvironment";
	private static String g_strWorkEnvironment;

	/**
	 * linux上使用“Linux”中的配置文件，其他的系统则使用默认的环境
	 * 
	 * @return
	 */

	public static String getG_strWorkEnvironment() {
		if (_XMLTag.g_strWorkEnvironment == null) {
			if (SystemPropertyUtilties.isWindows()) {
				_XMLTag.g_strWorkEnvironment = "Default";
			} else {
				_XMLTag.g_strWorkEnvironment = "Linux";
			}
		}
		return _XMLTag.g_strWorkEnvironment;

	}

	public static void setG_strWorkEnvironment(String g_strWorkEnvironment) {
		_XMLTag.g_strWorkEnvironment = g_strWorkEnvironment;
	}

	public static final String g_NodeEdit = "edit";
	public static final String g_AttributionRebackItemCountDefine = "rebackitemcountdefine";
	public static final String g_AttributionRebackItemCount = "rebackitemcount";
	public static final String g_AttributionRebackTimesDefine = "rebacktimesdefine";
	public static final String g_AttributionRebackTimes = "rebacktimes";
	public static final String g_AttributionParamterEdit = "parameteredit";
	public static final String g_AttributionPositiveSelect = "positiveselect";
	public static final String g_AttributionReverseSelect = "reverseselect";
	private static int g_nReverseSelect;

	public static int getG_nReverseSelect() {
		_XMLTag.g_nReverseSelect = 1;
		return _XMLTag.g_nReverseSelect;
	}

	public static void setG_nReverseSelect(int g_nReverseSelect) {
		_XMLTag.g_nReverseSelect = g_nReverseSelect;
	}

	public static final String g_NodeTheme = "theme";
	public static final String g_AttributionThemeRefresh = "refresh";

	// precision
	public static final String g_Precision = "precision";
	public static final String g_DecimalPlaces = "DecimalPlaces";

	public static final String g_AttributionScreenTipShow = "screenTipShow";
	public static final String g_AttributionShowNavigation = "showNavigationBar";

	// <!--SQL查询语句-->
	public static final String g_NodeSQLDatasetName = "SQLDatasetName";
	public static final String g_AttributionDatasetName = "datasetName";
	public static final String g_NodeSQLQueryFields = "SQLQueryFields";
	public static final String g_AttributionQueryFields = "queryFields";
	public static final String g_NodeSQLQueryCondition = "SQLQueryCondition";
	public static final String g_AttributionQueryCondition = "queryCondition";
	public static final String g_NodeSQLQueryGroup = "SQLQueryGroup";
	public static final String g_AttributionGroupFields = "groupFields";
	// <!--字段、排序类型之间以“,”分隔开-->
	public static final String g_NodeSQLOrderFields = "SQLOrderFields";
	public static final String g_AttributionOrderFields = "orderFields";
	public static final String g_NodeSQLOrderType = "SQLOrderType";
	public static final String g_AttributionOrderType = "orderType";

	// <!-- 最大可见节点数目-->
	public static final String g_NodemaxVisibleVertex = "maxVisibleVertex";
	public static final String g_AttributionMaxCount = "maxCount";

	// <!-- 捕捉参数-->
	public static final String g_NodeSnapSettingParameter = "MapControlSnapSettingParameter";
	public static final String g_AttributionFixedAngle = "FixedAngle";
	public static final String g_AttributionFixedLength = "FixedLength";
	public static final String g_AttributionIsSnappedLineBroken = "IsSnappedLineBroken";
	public static final String g_AttributionMaxSnappedCount = "MaxSnappedCount";
	public static final String g_AttributionMinSnappedLength = "MinSnappedLength";
	public static final String g_AttributionTolerance = "Tolerance";

	// <!-- 捕捉对象-->
	public static final String g_NodeSnapSettingModes = "MapControlSnapSettingModes";
	public static final String g_AttributionPointOnEndpoint = "PointOnEndpoint";
	public static final String g_AttributionPointOnPoint = "PointOnPoint";
	public static final String g_AttributionPointOnLine = "PointOnLine";
	public static final String g_AttributionPointWithHorizontalOrVertical = "PointWithHorizontalOrVertical";
	public static final String g_AttributionPointOnMidpoint = "PointOnMidpoint";
	public static final String g_AttributionPointOnExtension = "PointOnExtension";
	public static final String g_AttributionLineWithFixedAngle = "LineWithFixedAngle";
	public static final String g_AttributionLineWithFixedLength = "LineWithFixedLength";
	public static final String g_AttributionLineWithIntersection = "LineWithIntersection";
	public static final String g_AttributionLineWithHorizontalOrVertical = "LineWithHorizontalOrVertical";
	public static final String g_AttributionLineWithParallel = "LineWithParallel";
	public static final String g_AttributionLineWithPerpendicular = "LineWithPerpendicular";
	public static final String g_assemblyTitle = "// AssemblyTitle ";
	public static final String g_assemblyEnd = "; ";
	public static final String g_NodeUserColor = "userColor";
	public static final String g_NodeInfoTypeName = "InfoType";
	public static final String g_AttributionInfoTypeInformation = "Information";
	public static final String g_AttributionInfoTypeException = "Exception";
	public static final String g_AttributionPanelShow = "panelShow";
}
