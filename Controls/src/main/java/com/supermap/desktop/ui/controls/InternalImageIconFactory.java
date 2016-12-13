package com.supermap.desktop.ui.controls;

import com.supermap.desktop.controls.utilities.ControlsResources;

import javax.swing.*;
import java.net.URL;

public final class InternalImageIconFactory {

	// Define icons for EngineType
	public final static ImageIcon DT_POINT = createIconFromFile("Image_Dataset_Point_Normal.png");

	public final static ImageIcon DT_LINE = createIconFromFile("Image_Dataset_Line_Normal.png");

	public final static ImageIcon DT_REGION = createIconFromFile("Image_Dataset_Region_Normal.png");

	public final static ImageIcon DT_CAD = createIconFromFile("Image_Dataset_CAD_Normal.png");

	public final static ImageIcon DT_GRID = createIconFromFile("Image_Dataset_Grid_Normal.png");

	public final static ImageIcon DT_RELATIONSHIP = createIconFromFile("Image_Dataset_Relationship_Normal.png");

	public final static ImageIcon DT_IMAGE = createIconFromFile("Image_Dataset_Image_Normal.png");

	public final static ImageIcon DT_NETWORK = createIconFromFile("Image_Dataset_Network_Normal.png");

	public final static ImageIcon DT_NETWORK3D = createIconFromFile("Image_Dataset_Network3D_Normal.png");

	public final static ImageIcon DT_LINKTABLE = createIconFromFile("Image_Dataset_LinkTable_Normal.png");

	public final static ImageIcon DT_TABULAR = createIconFromFile("Image_Dataset_Tabular_Normal.png");

	public final static ImageIcon DT_TEXT = createIconFromFile("Image_Dataset_Text_Normal.png");

	public final static ImageIcon DT_LINEM = createIconFromFile("Image_Dataset_LineM_Normal.png");

	public final static ImageIcon DT_TOPOLOGY = createIconFromFile("Image_Dataset_Topology_Normal.png");

	public final static ImageIcon DT_WCS = createIconFromFile("Image_Dataset_WCS_Normal.png");

	public final static ImageIcon DT_WMS = createIconFromFile("Image_Dataset_WMS_Normal.png");

	// add by gouyu 2012-12-25
	public final static ImageIcon DT_GRIDCOLLECTION = createIconFromFile("Image_Dataset_GridCollection_Normal.png");

	public final static ImageIcon DT_IMAGECOLLECTION = createIconFromFile("Image_Dataset_ImageCollection_Normal.png");

	public final static ImageIcon DT_PARAMETRICLINE = createIconFromFile("Image_Dataset_ParametricLine_Normal.png");

	public final static ImageIcon DT_PARAMETRICREGION = createIconFromFile("Image_Dataset_ParametricRegion_Normal.png");

	public final static ImageIcon DT_UNKNOWN = createIconFromFile("Image_Dataset_Unknown_Normal.png");

	public final static ImageIcon DT_POINT3D = createIconFromFile("Image_Dataset_Point3D_Normal.png");

	public final static ImageIcon DT_LINE3D = createIconFromFile("Image_Dataset_Line3D_Normal.png");

	public final static ImageIcon DT_REGION3D = createIconFromFile("Image_Dataset_Region3D_Normal.png");

	// Define icons for workspace
	public final static ImageIcon WORKSPACE = createIconFromFile("Image_WorkspaceDefault_Normal.png");

	public final static ImageIcon WORKSPACE_ORACLE = createIconFromFile("Image_WorkspaceOracle_Normal.png");

	public final static ImageIcon WORkSPACE_SQL = createIconFromFile("Image_WorkspaceSQL_Normal.png");

	public final static ImageIcon WORKSPACE_SMW = createIconFromFile("Image_WorkspaceSMW_Normal.png");

	public final static ImageIcon WORKSPACE_SXW = createIconFromFile("Image_WorkspaceSXW_Normal.png");

	public final static ImageIcon DATASOURCES = createIconFromFile("Image_Datasources_Normal.png");

	public final static ImageIcon DATASOURCE_SDB = createIconFromFile("Image_Datasource_SDBPlus_Normal.png");

	public final static ImageIcon DATASOURCE_UDB = createIconFromFile("Image_Datasource_UDB_Normal.png");

	public final static ImageIcon DATASOURCE_SQL = createIconFromFile("Image_Datasource_SQLPlus_Normal.png");

	public final static ImageIcon DATASOURCE_ORACLE = createIconFromFile("Image_Datasource_OraclePlus_Normal.png");

	public final static ImageIcon DATASOURCE_IMAGEPLUGINS = createIconFromFile("Image_Datasource_ImagePlugins_Normal.png");

	public final static ImageIcon DATASOURCE_OGC = createIconFromFile("Image_Datasource_OGC_Normal.png");

	public final static ImageIcon DATASOURCE_GOOGLEMAPS = createIconFromFile("Image_Datasource_GoogleMaps_Normal.png");

	public final static ImageIcon DATASOURCE_DB2 = createIconFromFile("Image_Datasource_DB2_Normal.png");

	public final static ImageIcon DATASOURCE_KINGBASE = createIconFromFile("Image_Datasource_Kingbase_Normal.png");

	public final static ImageIcon DATASOURCE_POSTGRESQL = createIconFromFile("Image_Datasource_PostgreSQL_Normal.png");

	// add by gouyu 2012-12-25
	public final static ImageIcon DATASOURCE_BAIDUMAPS = createIconFromFile("Image_Datasource_BaiduMaps_Normal.png");

	public final static ImageIcon DATASOURCE_DMPLUS = createIconFromFile("Image_Datasource_DMPlus_Normal.png");

	public final static ImageIcon DATASOURCE_ISERVERREST = createIconFromFile("Image_Datasource_iServerRest_Normal.png");

//	public final static ImageIcon DATASOURCE_MAPWORLD = createIconFromFile("Image_Datasource_MapWorld_Normal.png");

	public final static ImageIcon DATASOURCE_NETCDF = createIconFromFile("Image_Datasource_NetCDF_Normal.png");

	public final static ImageIcon DATASOURCE_ORACLESPATIAL = createIconFromFile("Image_Datasource_OracleSpatial_Normal.png");

	public final static ImageIcon DATASOURCE_PCI = createIconFromFile("Image_Datasource_PCI_Normal.png");

	public final static ImageIcon DATASOURCE_SUPERMAPCLOUD = createIconFromFile("Image_Datasource_SuperMapCloud_Normal.png");

	public final static ImageIcon DATASOURCE_VECTORFILE = createIconFromFile("Image_Datasource_VectorFile_Normal.png");

	public final static ImageIcon DATASOURCE_DEFAULT = createIconFromFile("Image_DatasourceDefault.png");

	public final static ImageIcon RESOURCES = createIconFromFile("Image_Resources_Normal.png");

	public final static ImageIcon SYMBOLLINELIB = createIconFromFile("Image_SymbolLine_Normal.png");

	public final static ImageIcon SYMBOLFillLIB = createIconFromFile("Image_SymbolFill_Normal.png");

	public final static ImageIcon SYMBOLMARKERLIB = createIconFromFile("Image_SymbolMarkerNormal.png");

	public final static ImageIcon MAPS = createIconFromFile("Image_Maps_Normal.png");

	public final static ImageIcon MAP = createIconFromFile("Image_Map_Normal.png");

	// Define icons for theme
	public final static ImageIcon THEME_UNIQUE = createIconFromFile("Image_Layer_ThemeUnique.png");

	public final static ImageIcon THEME_GRAPH = createIconFromFile("Image_Layer_ThemeGraph.png");

	public final static ImageIcon THEME_LABEL_COMPLICATED = createIconFromFile("Image_Layer_complicatedOfLabelTheme.png");

	public final static ImageIcon THEME_LABEL = createIconFromFile("Image_Layer_unifromStyleOfLabelTheme.png");

	public final static ImageIcon THEME_LABEL_RANGE = createIconFromFile("Image_Layer_rangesStyleOfLabelTheme.png");

	public final static ImageIcon THEME_RANGE = createIconFromFile("Image_Layer_ThemeRange.png");

	public final static ImageIcon THEME_CUSTOM = createIconFromFile("Image_Layer_ThemeCustom.png");

	public final static ImageIcon THEME_DOTDENSITY = createIconFromFile("Image_Layer_ThemeDotdensity.png");

	public final static ImageIcon THEME_GRADUATEDSYMBOL = createIconFromFile("Image_Layer_ThemeGraduated.png");

	public final static ImageIcon THEME_GRIDRANGE = createIconFromFile("Image_Layer_ThemeGridRange.png");

	public final static ImageIcon THEME_GRIDUNIQUE = createIconFromFile("Image_Layer_ThemeGridUnique.png");

	// define icons for themeGuide
	public final static ImageIcon THEMEGUIDE_UNIQUE = createIconFromFile("Image_ThemeGuide_ThemeUnique.png");

	public final static ImageIcon THEMEGUIDE_GRAPH = createIconFromFile("Image_ThemeGuide_ThemeGraph.png");

	public final static ImageIcon THEMEGUIDE_LABEL = createIconFromFile("Image_ThemeGuide_ThemeLabel.png");

	public final static ImageIcon THEMEGUIDE_RANGE = createIconFromFile("Image_ThemeGuide_ThemeRange.png");

	public final static ImageIcon THEMEGUIDE_CUSTOM = createIconFromFile("Image_ThemeGuide_ThemeCustom.png");

	public final static ImageIcon THEMEGUIDE_DOTDENSITY = createIconFromFile("Image_ThemeGuide_ThemeDotdensity.png");

	public final static ImageIcon THEMEGUIDE_GRADUATEDSYMBOL = createIconFromFile("Image_ThemeGuide_ThemeGraduated.png");

	// 标签专题图统一风格
	public final static ImageIcon THEMEGUIDE_UNIFORM = createIconFromFile("Image_ThemeGuide_unifromStyleOfLabelTheme.png");

	// 标签专题图分段风格
	public final static ImageIcon THEMEGUIDE_RANGES = createIconFromFile("Image_ThemeGuide_rangesStyleOfLabelTheme.png");

	// 标签专题图复合风格
	public final static ImageIcon THEMEGUIDE_COMPLICATED = createIconFromFile("Image_ThemeGuide_complicatedStyleOfLabelTheme.png");

	// 标签专题图矩阵风格
	public final static ImageIcon THEMEGUIDE_MATRIX = createIconFromFile("Image_ThemeGuide_matrixStyleOfLabelTheme.png");

	// 专题图模板
	public final static ImageIcon THEMEGUIDE_MODULE = createIconFromFile("Image_ThemeGuide_themeModule.png");

	// 栅格单值专题图
	public final static ImageIcon THEMEGUIDE_GRIDUNIQUE = createIconFromFile("Image_ThemeGuide_ThemeGridUnique.png");

	// 栅格分段专题图
	public final static ImageIcon THEMEGUIDE_GRIDRANGE = createIconFromFile("Image_ThemeGuide_ThemeGridRange.png");

	// 专题图示例图片
	public final static ImageIcon THEMEGUIDE_PREVIEW_UNIQUE = createIconFromFile("Image_ThemeGuidePreview_ThemeUnique.png");

	public final static ImageIcon THEMEGUIDE_PREVIEW_RANGE = createIconFromFile("Image_ThemeGuidePreview_ThemeRanges.png");

	public final static ImageIcon THEMEGUIDE_PREVIEW_GRAPH = createIconFromFile("Image_ThemeGuidePreview_ThemeGraph.png");

	public final static ImageIcon THEMEGUIDE_PREVIEW_LABEL = createIconFromFile("Image_ThemeGuidePreview_ThemeLabel.png");

	public final static ImageIcon THEMEGUIDE_PREVIEW_CUSTOM = createIconFromFile("Image_ThemeGuidePreview_ThemeCustom.png");

	public final static ImageIcon THEMEGUIDE_PREVIEW_DOTDENSITY = createIconFromFile("Image_ThemeGuidePreview_ThemeDotdensity.png");

	public final static ImageIcon THEMEGUIDE_PREVIEW_GRADUATEDSYMBOL = createIconFromFile("Image_ThemeGuidePreview_ThemeGraduated.png");

	// 标签专题图四种风格的示例图片
	public final static ImageIcon THEMEGUIDE_PREVIEW_UNIFORM = createIconFromFile("Image_ThemeGuidePreview_unifromStyleOfLabelTheme.png");

	public final static ImageIcon THEMEGUIDE_PREVIEW_RANGES = createIconFromFile("Image_ThemeGuidePreview_rangesStyleOfLabelTheme.png");

	public final static ImageIcon THEMEGUIDE_PREVIEW_COMPLICATED = createIconFromFile("Image_complicatedStyleOfLabelTheme_.png");

	public final static ImageIcon THEMEGUIDE_PREVIEW_MATRIX = createIconFromFile("Image_ThemeGuidePreview_matrixStyleOfLabelTheme.png");

	// 导航面板的箭头
	public final static ImageIcon ThemeGuide_Arrow = createIconFromFile("Image_ThemeGuide_arrow.png");

	// 导航条上的图片
	// 未选中时的状态
	public final static ImageIcon THEMEGUIDE_STARTGUIDEPAGE = createIconFromFile("Image_ThemeGuide_StartGuidePage.png");
	// 选中时的状态
	public final static ImageIcon THEMEGUIDE_STARTGUIDEPAGE_DIM = createIconFromFile("Image_ThemeGuide_StartGuidePage_Dim.png");

	public final static ImageIcon THEMEGUIDE_BASICSETTING = createIconFromFile("Image_ThemeGuide_basicSetting.png");
	public final static ImageIcon THEMEGUIDE_BASICSETTING_DIM = createIconFromFile("Image_ThemeGuide_basicSetting_Dim.png");

	public final static ImageIcon THEMEGUIDE_ATTRIBUTESETTING = createIconFromFile("Image_ThemeGuide_attributeSetting.png");
	public final static ImageIcon THEMEGUIDE_ATTRIBUTESETTING_DIM = createIconFromFile("Image_ThemeGuide_attributeSetting_Dim.png");

	public final static ImageIcon THEMEGUIDE_ADVANCEDSETTING = createIconFromFile("Image_ThemeGuide_advancedSetting.png");
	public final static ImageIcon THEMEGUIDE_ADVANCEDSETTING_DIM = createIconFromFile("Image_ThemeGuide_advancedSetting_Dim.png");

	// Define icons for scenes
	public final static ImageIcon SCENES = createIconFromFile("Image_Scenes_Normal.png");

	public final static ImageIcon SCENE = createIconFromFile("Image_Scene_Normal.png");

	// Define icons for layouts

	public final static ImageIcon LAYOUTS = createIconFromFile("Image_Layouts_Normal.png");

	public final static ImageIcon LAYOUT = createIconFromFile("Image_Layout_Normal.png");

	// Define icons for actions
	public final static ImageIcon VISIBLE = createIconFromFile("Image_Layer_Visible.png");

	public final static ImageIcon INVISIBLE = createIconFromFile("Image_Layer_Unvisible.png");

	public final static ImageIcon SELECTABLE = createIconFromFile("Image_Layer_Selectable.png");

	public final static ImageIcon UNSELECTABLE = createIconFromFile("Image_Layer_Unselectable.png");

	public final static ImageIcon EDITABLE = createIconFromFile("Image_Layer_Editable.png");

	public final static ImageIcon UNEDITABLE = createIconFromFile("Image_Layer_Uneditable.png");

	public final static ImageIcon CHECKED = createIconFromFile("Image_Layer_Checked.png");

	public final static ImageIcon UNCHECKED = createIconFromFile("Image_Layer_Unchecked.png");

	public final static ImageIcon UNSNAPABLE = createIconFromFile("Image_Layer_Unsnapable.png");

	public final static ImageIcon SNAPABLE = createIconFromFile("Image_Layer_Snapable.png");

	public final static ImageIcon ALWAYS_RENDER = createIconFromFile("Image_Layer3DManager_AlwaysRender.png");

	public final static ImageIcon UNALWAYS_RENDER = createIconFromFile("Image_Layer3DManager_UnAlwaysRender.png");

	// Define icon for scene
	public final static ImageIcon GENERAL_LAYERS = createIconFromFile("Image_Layer3DManager_GeneralLayers.png");

	public final static ImageIcon SCREEN_LAYER3D = createIconFromFile("Image_Layer3DManager_ScreenLayer3D.png");

	public final static ImageIcon TERRAIN_LAYER = createIconFromFile("Image_Layer3DManager_TerrainCache.png");

	public final static ImageIcon TERRAIN_LAYERS = createIconFromFile("Image_Layer3DManager_TerrainLayers.png");

	public final static ImageIcon LAYER3D_IMAGEFILE = createIconFromFile("Image_Layer3DManager_ImageCache2D.png");

	public final static ImageIcon FEATURE3DS = createIconFromFile("Image_Layer3DManager_Feature3Ds.png");

	public final static ImageIcon FEATURE3D = createIconFromFile("Image_Layer3DManager_GeoPlacemark.png");

	public final static ImageIcon IMAGECACHE2D = createIconFromFile("Image_Layer3DManager_ImageCache2D.png");

	public final static ImageIcon LAYER3DKML = createIconFromFile("Image_Layer3DManager_KMLLayer.png");

	public final static ImageIcon LAYER3DMAP = createIconFromFile("Image_Layer3DManager_ImageLayer3DMap.png");

	public final static ImageIcon SCREEN_LAYER_GEOMETRY = createIconFromFile("Image_DatasetImage_Normal.png");

	public final static ImageIcon LAYER3D_MODEL = createIconFromFile("Image_Layer3DManager_Model.png");

	public final static ImageIcon LAYER3DS = createIconFromFile("Image_Layer3DManager_GeneralLayers.png");

	public final static ImageIcon THEME3D_LABEL = createIconFromFile("Image_Layer3DManager_LabelUniform.png");

	public final static ImageIcon THEME3D_UNIQUE = createIconFromFile("Image_Layer3DManager_VectorUnique.png");

	public final static ImageIcon THEME3D_RANGE = createIconFromFile("Image_Layer3DManager_VectorRanges.png");

	// Define icon for WorkspaceTree Operation
	public final static ImageIcon DATASOURCE_NODE_VISIBLE = createIconFromFile("Image_Datasources_Enable.png");

	public final static ImageIcon DATASOURCE_NODE_INVISIBLE = createIconFromFile("Image_Datasources_Disable.png");

	public final static ImageIcon MAP_NODE_VISIBLE = createIconFromFile("Image_Maps_Normal.png");

	public final static ImageIcon MAP_NODE_INVISIBLE = createIconFromFile("Image_Maps_Disable.png");

	public final static ImageIcon SCENE_NODE_VISIBLE = createIconFromFile("Image_Scenes_Normal.png");

	public final static ImageIcon SCENE_NODE_INVISIBLE = createIconFromFile("Image_Scenes_Disable.png");

	public final static ImageIcon LAYOUT_NODE_VISIBLE = createIconFromFile("Image_Layouts_Normal.png");

	public final static ImageIcon LAYOUT_NODE_INVISIBLE = createIconFromFile("Image_Layouts_Disable.png");

	public final static ImageIcon RESOURCE_NODE_VISIBLE = createIconFromFile("Image_Resources_Normal.png");

	public final static ImageIcon RESOURCE_NODE_INVISIBLE = createIconFromFile("Image_Resources_Disable.png");

	public final static ImageIcon LAYER3D_VECTOR_FILE = createIconFromFile("Image_Layer3DManager_VectorModelCache.png");

	public final static ImageIcon LAYER3D_VECTOR_FILE_BACK = createIconFromFile("Image_Layer3DManager_VectorFile_BackGround.png");

	public final static ImageIcon SELECT_ALL = createIconFromFile("Image_SelectAll.png");

	public final static ImageIcon SELECT_PREVIOUS = createIconFromFile("Image_SelectPrevious.png");

	public final static ImageIcon COLOR_SCHEME_EDITOR_ADD_KEY_COLOR = createIconFromFile("Image_AddKeyColor.png");

	public final static ImageIcon COLOR_SCHEME_EDITOR_EDIT_KEY_COLOER = createIconFromFile("Image_EditKeyColoer.png");

	public final static ImageIcon COLOR_SCHEME_EDITOR_REMOVE_KEY_COLOR = createIconFromFile("Image_RemoveKeyColor.PNG");

	public final static ImageIcon COLOR_SCHEME_EDITOR_MOVE_FIRST = createIconFromFile("Image_MoveFirst.png");

	public final static ImageIcon COLOR_SCHEME_EDITOR_MOVE_UP = createIconFromFile("Image_MoveUp.png");

	public final static ImageIcon COLOR_SCHEME_EDITOR_MOVE_DOWN = createIconFromFile("Image_MoveDown.png");

	public final static ImageIcon COLOR_SCHEME_EDITOR_MOVE_LAST = createIconFromFile("Image_MoveLast.png");

	public final static ImageIcon COLOR_PICK = createIconFromFile("Image_ColorPick_StrawCursor.png");

	public final static ImageIcon CURSOR_ICON = createIconFromFile("Image_ColorPick_Icon.png");

	public final static ImageIcon OTHER_COLOR = createIconFromFile("Image_OtherColor_Icon.png");

	public final static ImageIcon CONCIAL = createIconFromFile("Image_FillGradien_Conical.png");

	public final static ImageIcon LINEAR = createIconFromFile("Image_FillGradien_Linear.png");

	public final static ImageIcon RADIAL = createIconFromFile("Image_FillGradien_Radial.png");

	public final static ImageIcon SQUARE = createIconFromFile("Image_FillGradien_Square.png");

	public final static ImageIcon NONE = createIconFromFile("Image_FillGradien_None.png");

	public final static ImageIcon Merge = createIconFromFile("Image_ThemeGuide_RangeLabel_Merge.png");

	public final static ImageIcon Split = createIconFromFile("Image_ThemeGuide_RangeLabel_Split.png");

	public final static ImageIcon Rever = createIconFromFile("Image_ThemeGuide_RangeLabel_Rever.png");

	public final static ImageIcon STYLE = createIconFromFile("Image_ThemeGuide_RangeLabel_Style.png");

	public final static ImageIcon LAYER_GROUP = createIconFromFile("Image_LayerGroup.png");

	public final static ImageIcon REGION_STYLE = createIconFromFile("Image_ThemeGuide_GeoRegionStyle.png");

	public final static ImageIcon LINE_STYLE = createIconFromFile("Image_ThemeGuide_GeoLineStyle.png");

	public final static ImageIcon POINT_STYLE = createIconFromFile("Image_ThemeGuide_GeoPointStyle.png");

	public final static ImageIcon ADD_ITEM = createIconFromFile("Image_ThemeGuide_AddItem.png");

	public final static ImageIcon FOREGROUND_COLOR = createIconFromFile("ForegroundColor.png");

	public final static ImageIcon MOVE_TO_FRIST = createIconFromFile("Image_MoveToFrist.png");

	public final static ImageIcon MOVE_TO_FORWARD = createIconFromFile("Image_MoveToForward.png");

	public final static ImageIcon MOVE_TO_NEXT = createIconFromFile("Image_MoveToNext.png");

	public final static ImageIcon MOVE_TO_LAST = createIconFromFile("Image_MoveToLast.png");

	private InternalImageIconFactory() {
		// 工具类不提供构造函数
	}

	private static ImageIcon createIconFromFile(String fileName) {
		String strUrl = "/controlsresources/controlsImage/" + fileName;
		URL url = ControlsResources.getResourceURL(strUrl);
		ImageIcon icon = null;
		if (url != null) {
			icon = new ImageIcon(url);
		}
		return icon;
	}

}
