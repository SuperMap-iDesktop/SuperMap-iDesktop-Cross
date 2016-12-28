package com.supermap.desktop.newtheme.commonUtils;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IDockbar;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.newtheme.commonPanel.ThemeChangePanel;
import com.supermap.desktop.newtheme.commonPanel.ThemeMainContainer;
import com.supermap.desktop.newtheme.themeCustom.ThemeCustomContainer;
import com.supermap.desktop.newtheme.themeDotDensity.ThemeDotDensityContainer;
import com.supermap.desktop.newtheme.themeGraduatedSymbol.ThemeGraduatedSymbolContainer;
import com.supermap.desktop.newtheme.themeGraph.ThemeGraphContainer;
import com.supermap.desktop.newtheme.themeGridRange.ThemeGridRangeContainer;
import com.supermap.desktop.newtheme.themeGridUnique.ThemeGridUniqueContainer;
import com.supermap.desktop.newtheme.themeLabel.ThemeLabelComplicatedContainer;
import com.supermap.desktop.newtheme.themeLabel.ThemeLabelRangeContainer;
import com.supermap.desktop.newtheme.themeLabel.ThemeLabelUniformContainer;
import com.supermap.desktop.newtheme.themeRange.ThemeRangeContainer;
import com.supermap.desktop.newtheme.themeUnique.ThemeUniqueContainer;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.LayersTree;
import com.supermap.desktop.ui.controls.TreeNodeData;
import com.supermap.mapping.*;
import com.supermap.ui.MapControl;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.MessageFormat;
import java.util.HashMap;

public class ThemeGuideFactory {

	private static final String THEME_MAIN_CONTAINER_CLASS = "com.supermap.desktop.newtheme.commonPanel.ThemeMainContainer";
	private static IDockbar dockbarThemeContainer;
	private static transient ThemeMainContainer container;
	public static HashMap<Layer, ThemeChangePanel> themeTypeContainer = new HashMap<Layer, ThemeChangePanel>();
	private static final String THEMETYPE_UNIQUE = "ThemeType_Unique";
	private static final String THEMETYPE_RANGE = "ThemeType_Range";
	private static final String THEMETYPE_LABEL_UNIFORM = "ThemeType_Label_Uniform";
	private static final String THEMETYPE_LABEL_RANGE = "ThemeType_Label_Range";
	private static final String THEMETYPE_LABEL_COMPLICATED = "ThemeType_Label_Complicated";
	private static final String THEMETYPE_GRAPH = "ThemeType_Graph";
	private static final String THEMETYPE_GRID_UNIQUE = "ThemeType_Grid_Unique";
	private static final String THEMETYPE_GRID_RANGE = "ThemeType_Grid_Range";
	private static final String THEMETYPE_GRADUATEDSYMBOL = "ThemeType_GraduatedSymbol";
	private static final String THEMETYPE_DOTDENSITY = "ThemeType_DotDensity";
	private static final String THEMETYPE_CUSTOM = "ThemeType_Custom";

	/**
	 * 界面替换
	 *
	 * @param
	 */

	private ThemeGuideFactory() {
		// 工具类不提供构造函数
	}

	public static IDockbar getDockbarThemeContainer() {
		try {
			if (null == dockbarThemeContainer) {
				dockbarThemeContainer = Application.getActiveApplication().getMainFrame().getDockbarManager().get(Class.forName(THEME_MAIN_CONTAINER_CLASS));
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return dockbarThemeContainer;
	}

	private static ThemeMainContainer addPanelToThemeMainContainer(final ThemeChangePanel panel, Layer layer) {
		try {
			dockbarThemeContainer = Application.getActiveApplication().getMainFrame().getDockbarManager().get(Class.forName(THEME_MAIN_CONTAINER_CLASS));
			if (dockbarThemeContainer != null) {
				container = (ThemeMainContainer) dockbarThemeContainer.getInnerComponent();
				container.setPanel(panel);
			}
			if (null != panel.getCurrentLayer() && panel.getCurrentLayer().isDisposed()) {
				panel.setCurrentLayer(layer);
			}
			layerPropertyChange(panel);
		} catch (ClassNotFoundException e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		if (null != container) {
			return container;
		}
		return null;
	}

	private static String getThemeType(Layer layer) {
		String name = "";
		boolean isComplicated = false;
		if (null != layer && !layer.isDisposed() && null != layer.getTheme()) {
			int count = -1;
			if (layer.getTheme() instanceof ThemeLabel) {
				count = ((ThemeLabel) layer.getTheme()).getCount();
				if (count == 0 && null != ((ThemeLabel) layer.getTheme()).getUniformMixedStyle()) {
					isComplicated = true;
				}
			}
			name = getThemeTypeString(layer.getTheme().getType(), count, isComplicated);
		}
		return name;
	}

	private static String getThemeTypeString(ThemeType themetype, int count, boolean isComplicated) {
		String result = "";
		if (themetype == ThemeType.UNIQUE) {
			result = THEMETYPE_UNIQUE;
		}
		if (themetype == ThemeType.RANGE) {
			result = THEMETYPE_RANGE;
		}
		if (themetype == ThemeType.LABEL && count == 0 && !isComplicated) {
			result = THEMETYPE_LABEL_UNIFORM;
		}
		if (themetype == ThemeType.LABEL && count == 0 && isComplicated) {
			result = THEMETYPE_LABEL_COMPLICATED;
		}
		if (themetype == ThemeType.LABEL && count > 0) {
			result = THEMETYPE_LABEL_RANGE;
		}
		if (themetype == ThemeType.GRIDUNIQUE) {
			result = THEMETYPE_GRID_UNIQUE;
		}
		if (themetype == ThemeType.GRIDRANGE) {
			result = THEMETYPE_GRID_RANGE;
		}
		if (themetype == ThemeType.GRAPH) {
			result = THEMETYPE_GRAPH;
		}
		if (themetype == ThemeType.GRADUATEDSYMBOL) {
			result = THEMETYPE_GRADUATEDSYMBOL;
		}
		if (themetype == ThemeType.DOTDENSITY) {
			result = THEMETYPE_DOTDENSITY;
		}
		if (themetype == ThemeType.CUSTOM) {
			result = THEMETYPE_CUSTOM;
		}
		return result;
	}

	private static void layerPropertyChange(final ThemeChangePanel panel) {
		panel.addPropertyChangeListener("ThemeChange", new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (!container.getCheckBoxRefreshAtOnce().isSelected()) {
					container.getButtonApply().setEnabled(true);
					container.setLayerPropertyChanged(true);
				}
			}
		});
	}


	/**
	 * 获取MapControl
	 *
	 * @return
	 */
	public static MapControl getMapControl() {
		MapControl mapControl = null;
		if (Application.getActiveApplication().getActiveForm() instanceof IFormMap) {
			IFormMap formMap = (IFormMap) Application.getActiveApplication().getActiveForm();
			if (null != formMap.getMapControl()) {
				mapControl = formMap.getMapControl();
			}
		}
		return mapControl;
	}

	/**
	 * 初始化专题图
	 *
	 * @param layer 源图层
	 * @return 专题图图层
	 */
	private static Layer initCurrentTheme(Layer layer, Theme theme) {
		Layer result = null;
		if (null != getMapControl()) {

			result = ThemeUtil.arrayNewThemeLayer(getMapControl().getMap(), layer, theme);
			UICommonToolkit.getLayersManager().getLayersTree().reload();
			if (null != result) {
				final int selectRow = getMapControl().getMap().getLayers().indexOf(result.getName());
				/**
				 * 屏蔽掉BasicTreeUI.completeEditing()方法获取图层树时的空指针异常
				 */
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						UICommonToolkit.getLayersManager().getLayersTree().setSelectionInterval(selectRow, selectRow);
					}
				});

			}
			if (null != layer) {
				// 复制关联表信息到新图层中
				for (int i = 0; i < layer.getDisplayFilter().getJoinItems().getCount(); i++) {
					result.getDisplayFilter().getJoinItems().add(layer.getDisplayFilter().getJoinItems().get(i));
				}
			}
			((ThemeMainContainer) getDockbarThemeContainer().getInnerComponent()).unregistActionListener();
			getMapControl().getMap().refresh();
		}
		return result;
	}

	/**
	 * 新建单值专题图
	 */
	public static boolean buildUniqueTheme(Layer layer) {
		boolean success = false;
		if (null != getDataset()) {
			String expression = "SmUserID";
			if (ThemeUtil.isCountBeyond((DatasetVector) getDataset(), expression)) {
				// 字段记录数大于3000条时建议不做专题图
				UICommonToolkit.showMessageDialog(MapViewProperties.getString("String_ThemeGridUnique_MessageBoxInfo"));
				return success;
			}
			expression = hasJoinItems(layer, expression);
			ThemeUnique themeUnique = ThemeUnique.makeDefault((DatasetVector) getDataset(), expression, ColorGradientType.BLUERED);
			if (null != themeUnique) {
				for (int i = 0; i < themeUnique.getCount(); i++) {
					GeoStyle textStyle = themeUnique.getItem(i).getStyle();
					textStyle.setLineColor(Color.GRAY);
				}
				success = true;
				Layer themeUniqueLayer = initCurrentTheme(layer, themeUnique);
				ThemeChangePanel themeUniqueContainer = new ThemeUniqueContainer(themeUniqueLayer, true);
				setDockbarActive(themeUniqueLayer, themeUniqueContainer);
			} else {
				success = false;
				UICommonToolkit.showMessageDialog(MapViewProperties.getString("String_Theme_UpdataFailed"));
			}
		}
		return success;
	}

	private static void setDockbarActive(Layer themeLayer, ThemeChangePanel themeUniqueContainer) {
		((ThemeMainContainer) getDockbarThemeContainer().getInnerComponent()).registActionListener();
		themeTypeContainer.put(themeLayer, themeUniqueContainer);
		addPanelToThemeMainContainer(themeUniqueContainer, null);
		container.getTextFieldThemeLayer().setText(themeLayer.getCaption());
		getDockbarThemeContainer().setVisible(true);
		getDockbarThemeContainer().active();
	}

	/**
	 * 新建分段专题图
	 */
	public static boolean buildRangeTheme(Layer layer) {
		boolean success = false;
		if (null != getDataset()) {
			String expression = "SmID";
			expression = hasJoinItems(layer, expression);
			ThemeRange themeRange = ThemeRange.makeDefault((DatasetVector) getDataset(), expression, RangeMode.EQUALINTERVAL, 5, ColorGradientType.GREENRED,
					null, 0.1);
			if (null != themeRange) {
				success = true;
				themeRange.setPrecision(0.1);
				for (int i = 0; i < themeRange.getCount(); i++) {
					GeoStyle textStyle = themeRange.getItem(i).getStyle();
					textStyle.setLineColor(Color.GRAY);
				}
				Layer themeRangeLayer = initCurrentTheme(layer, themeRange);
				ThemeChangePanel themeRangeContainer = new ThemeRangeContainer(themeRangeLayer, true);
				setDockbarActive(themeRangeLayer, themeRangeContainer);
			} else {
				success = false;
				UICommonToolkit.showMessageDialog(MapViewProperties.getString("String_Theme_UpdataFailed"));
			}
		}
		return success;
	}

	/**
	 * 新建统一风格标签专题图
	 */
	public static void buildLabelUniformTheme(Layer layer) {
		if (null != getDataset()) {
			String expression = "SmUserID";
			expression = hasJoinItems(layer, expression);
			ThemeLabel themeLabel = new ThemeLabel();
			themeLabel.setLabelExpression(expression);
			themeLabel.setMaxLabelLength(8);
			Layer themeLabelUniformLayer = initCurrentTheme(layer, themeLabel);
			ThemeChangePanel themeLabelUniformContainer = new ThemeLabelUniformContainer(themeLabelUniformLayer);
			setDockbarActive(themeLabelUniformLayer, themeLabelUniformContainer);
		}
	}

	/**
	 * 新建分段风格标签专题图
	 */
	public static boolean buildLabelRangeTheme(Layer layer) {
		boolean success = false;
		if (null != getDataset()) {
			String expression = "SmID";
			expression = hasJoinItems(layer, expression);
			ThemeLabel themeLabel = ThemeLabel.makeDefault((DatasetVector) getDataset(), expression, RangeMode.EQUALINTERVAL, 5, ColorGradientType.GREENRED);
			if (null != themeLabel && themeLabel.getCount() >= 2) {
				themeLabel.setMaxLabelLength(8);
				themeLabel.setNumericPrecision(1);
				themeLabel.setLabelExpression(expression);
				Layer themeLabelRangeLayer = initCurrentTheme(layer, themeLabel);
				ThemeChangePanel themeLabelRangeContainer = new ThemeLabelRangeContainer(themeLabelRangeLayer, true);
				setDockbarActive(themeLabelRangeLayer, themeLabelRangeContainer);
				success = true;
			} else {
				success = false;
				UICommonToolkit.showMessageDialog(MapViewProperties.getString("String_Theme_UpdataFailed"));
			}
		}
		return success;
	}

	/**
	 * 新建复合风格标签专题图
	 *
	 * @param layer
	 * @return
	 */
	public static boolean buildLabelComplicatedTheme(Layer layer) {
		boolean success = false;
		if (null != getDataset()) {
			String expression = "SmID";
			expression = hasJoinItems(layer, expression);
			ThemeLabel themeLabel = new ThemeLabel();
			success = true;
			TextStyle[] textStyles = new TextStyle[4];
			textStyles[0] = new TextStyle();
			textStyles[0].setForeColor(Color.RED);
			textStyles[1] = new TextStyle();
			textStyles[1].setForeColor(Color.GREEN);
			textStyles[2] = new TextStyle();
			textStyles[2].setForeColor(Color.BLUE);
			textStyles[3] = new TextStyle();
			textStyles[3].setForeColor(Color.PINK);
			int[] splitIndexes = {1, 3, 7};
			MixedTextStyle textStyle = new MixedTextStyle();
			textStyle.setSeparatorEnabled(false);
			textStyle.setStyles(textStyles);
			textStyle.setSplitIndexes(splitIndexes);
			themeLabel.setUniformMixedStyle(textStyle);
			themeLabel.setLabelExpression(expression);
			themeLabel.setMaxLabelLength(8);
			Layer themeLabelComplicatedLayer = initCurrentTheme(layer, themeLabel);
			ThemeChangePanel themeLabelComplicatedContainer = new ThemeLabelComplicatedContainer(themeLabelComplicatedLayer);
			setDockbarActive(themeLabelComplicatedLayer, themeLabelComplicatedContainer);
		}
		return success;
	}

	/**
	 * 新建栅格单值专题图
	 */
	public static boolean buildGridUniqueTheme() {
		boolean success = false;
		if (null != getDataset()) {
			DatasetGrid datasetGrid = (DatasetGrid) getDataset();
			datasetGrid.buildStatistics();
			try {
				ThemeGridUnique themeUnique = ThemeGridUnique.makeDefault(datasetGrid, ColorGradientType.GREENORANGEVIOLET);
				if (themeUnique.getCount() > 3000) {
					UICommonToolkit.showMessageDialog(MapViewProperties.getString("String_ThemeUniqueMaxCount"));
					return false;
				}
				if (null != themeUnique) {
					success = true;
					Layer themeGridUniqueLayer = initCurrentTheme(getActiveLayer(), themeUnique);
					ThemeChangePanel themeUniqueContainer = new ThemeGridUniqueContainer(themeGridUniqueLayer, true);
					setDockbarActive(themeGridUniqueLayer, themeUniqueContainer);
				} else {
					success = false;
					UICommonToolkit.showMessageDialog(MapViewProperties.getString("String_Theme_UpdataFailed"));
				}
			} catch (Exception e) {
				success = false;
				Application
						.getActiveApplication()
						.getOutput()
						.output(MessageFormat.format(MapViewProperties.getString("String_ThemeGridUnique_MessagePixelFormat"), datasetGrid.getName() + "@"
								+ datasetGrid.getDatasource().getAlias()));
			}
		}
		return success;
	}

	/**
	 * 新建栅格分段专题图
	 */
	public static boolean buildGridRangeTheme() {
		boolean success = false;
		if (null != getDataset()) {
			DatasetGrid datasetGrid = (DatasetGrid) getDataset();
			try {
				ThemeGridRange themeRange = ThemeGridRange.makeDefault(datasetGrid, RangeMode.EQUALINTERVAL, 5, ColorGradientType.GREENORANGEVIOLET);

				if (null != themeRange) {
					success = true;
					Layer themeGridRangeLayer = initCurrentTheme(getActiveLayer(), themeRange);
					ThemeChangePanel themeGridRangeContainer = new ThemeGridRangeContainer(themeGridRangeLayer, true);
					setDockbarActive(themeGridRangeLayer, themeGridRangeContainer);
				} else {
					success = false;
					UICommonToolkit.showMessageDialog(MapViewProperties.getString("String_Theme_UpdataFailed"));
				}
			} catch (Exception e) {
				e.printStackTrace();
				success = false;
				Application
						.getActiveApplication()
						.getOutput()
						.output(MessageFormat.format(MapViewProperties.getString("String_ThemeGridRange_MessagePixelFormat"), datasetGrid.getName() + "@"
								+ datasetGrid.getDatasource().getAlias()));
			}
		}
		return success;
	}

	/**
	 * 新建统计专题图
	 *
	 * @param layer
	 * @return
	 */
	public static boolean buildGraphTheme(Layer layer) {
		boolean success = false;
		if (null != getDataset()) {
			success = true;
			DatasetVector datasetVector = (DatasetVector) getDataset();
			ThemeGraph themeGraph = new ThemeGraph();
			String expression = "SmID";
			expression = hasJoinItems(layer, expression);
			ThemeGraphItem themeGraphItem = new ThemeGraphItem();
			themeGraphItem.setGraphExpression(expression);
			// 默认设置为SmID避免显示异常
			themeGraphItem.setCaption(expression);
			themeGraph.insert(0, themeGraphItem);
			// 默认设置为三维环状图，避免显示异常
			themeGraph.setGraphType(ThemeGraphType.PIE3D);
			// 默认设置一个坐标轴风格，避免显示异常
			themeGraph.getAxesTextStyle().setFontHeight(6);
			// 通过地图实际大小来计算最佳的专题图最大值
			Point pointStart = new Point(0, 0);
			Point pointEnd = new Point(0, (int) (getMapControl().getSize().getWidth() / 4));
			Point2D point2DStart = getMapControl().getMap().pixelToMap(pointStart);
			Point2D point2DEnd = getMapControl().getMap().pixelToMap(pointEnd);
			themeGraph.setMaxGraphSize(Math.sqrt(Math.pow(point2DEnd.getX() - point2DStart.getX(), 2) + Math.pow(point2DEnd.getY() - point2DStart.getY(), 2)));
			themeGraph.setBarWidth(themeGraph.getMaxGraphSize() / 10);
			themeGraph.setAxesDisplayed(false);
			Layer themeGraphLayer = initCurrentTheme(layer, themeGraph);
			ThemeChangePanel themeGraphContainer = new ThemeGraphContainer(themeGraphLayer, true);
			setDockbarActive(themeGraphLayer, themeGraphContainer);
		}
		return success;
	}

	/**
	 * 新建等级符号专题图
	 *
	 * @param layer
	 * @return
	 */
	public static boolean buildGraduatedSymbolTheme(Layer layer) {
		boolean successed = false;
		if (null != getDataset()) {
			String expression = "SmID";
			expression = hasJoinItems(layer, expression);
			ThemeGraduatedSymbol themeGraduated = ThemeGraduatedSymbol.makeDefault((DatasetVector) getDataset(), expression, GraduatedMode.CONSTANT);
			if (null == themeGraduated) {
				themeGraduated = new ThemeGraduatedSymbol();
			}
			successed = true;
			themeGraduated.setExpression(expression);
			Layer themeGraduatedLayer = initCurrentTheme(layer, themeGraduated);
			ThemeChangePanel themeGraduatedContainer = new ThemeGraduatedSymbolContainer(themeGraduatedLayer);
			setDockbarActive(themeGraduatedLayer, themeGraduatedContainer);
		}
		return successed;
	}

	/**
	 * 新建点密度专题图
	 *
	 * @param layer
	 * @return
	 */
	public static boolean buildDotDensityTheme(Layer layer) {
		boolean successed = false;
		if (null != getDataset()) {
			ThemeDotDensity themeDotDensity = new ThemeDotDensity();
			successed = true;
			String expression = "SmID";
			expression = hasJoinItems(layer, expression);
			themeDotDensity.setDotExpression(expression);
			GeoStyle geoStyle = new GeoStyle();
			geoStyle.setMarkerSize(new Size2D(2, 2));
			geoStyle.setLineColor(Color.PINK);
			themeDotDensity.setStyle(geoStyle);
			double maxValue = getMaxValue((DatasetVector) getDataset(), "SmID", layer.getDisplayFilter().getJoinItems());
			themeDotDensity.setValue(maxValue / 1000);
			Layer themeDotDensityLayer = initCurrentTheme(layer, themeDotDensity);
			ThemeChangePanel themeDotDensityContainer = new ThemeDotDensityContainer(themeDotDensityLayer);
			setDockbarActive(themeDotDensityLayer, themeDotDensityContainer);
		}
		return successed;
	}

	/**
	 * 自定义专题图
	 *
	 * @param layer
	 * @return
	 */
	public static boolean buildCustomTheme(Layer layer) {
		boolean successed = false;
		if (null != getDataset()) {
			ThemeCustom themeCustom = new ThemeCustom();
			successed = true;
			Layer themeCustomLayer = initCurrentTheme(layer, themeCustom);
			ThemeChangePanel themeCustomContainer = new ThemeCustomContainer(themeCustomLayer);
			setDockbarActive(themeCustomLayer, themeCustomContainer);
		}
		return successed;
	}

	/**
	 * 计算矢量数据集中某个字段的最大值,不支持多字段计算最大值
	 *
	 * @param datasetVector
	 * @return
	 */
	public static double getMaxValue(DatasetVector datasetVector, String expression, JoinItems joinItems) {

		double maxValue = 0;
		QueryParameter parameter = new QueryParameter();
		parameter.setAttributeFilter(expression);
		parameter.setCursorType(CursorType.DYNAMIC);
		parameter.setHasGeometry(true);
		parameter.setResultFields(new String[]{expression});
		Recordset recordset = datasetVector.query(parameter);
		if (recordset.getFieldInfos().get(expression).getType() == FieldType.INT64) {
			// 屏蔽掉64位整形数据,组件不支持
			return maxValue;
		}
		if (null != recordset.getFieldInfos().get(expression) && recordset.getFieldInfos().get(expression).getType() != FieldType.TEXT) {
			maxValue = recordset.statistic(expression, StatisticMode.MAX);
		}
		return maxValue;
	}

	private static String hasJoinItems(Layer layer, String expression) {
		if (layer.getDisplayFilter().getJoinItems().getCount() > 0) {
			expression = getDataset().getName() + "." + expression;
		}
		return expression;
	}

	/**
	 * 根据图层名称获得展开的节点
	 *
	 * @param tree
	 * @param layerName
	 */
	public static TreePath getSelectionPath(LayersTree tree, String layerName) {
		TreePath layerPath = null;
		MutableTreeNode treeNode = (MutableTreeNode) tree.getModel().getRoot();
		for (int i = 0; i < treeNode.getChildCount(); i++) {
			DefaultMutableTreeNode childDatasetTreeNode = (DefaultMutableTreeNode) treeNode.getChildAt(i);
			TreeNodeData selectedNodeData = (TreeNodeData) childDatasetTreeNode.getUserObject();
			Layer layer = (Layer) selectedNodeData.getData();
			if (layer.getName().equals(layerName)) {
				layerPath = new TreePath(childDatasetTreeNode.getPath());
			}
		}
		return layerPath;
	}

	/**
	 * 修改专题图
	 */
	public static void resetTheme(Layer layer, String themeType) {
		if (hasThemeContainer(layer)) {
			addPanelToThemeMainContainer(themeTypeContainer.get(layer), layer);
		} else {
			ThemeChangePanel themeContainer = null;
			if (THEMETYPE_UNIQUE.equals(themeType)) {
				// 单值专题图
				themeContainer = new ThemeUniqueContainer(layer, false);
				initThemePanel(layer, themeType, themeContainer);
				return;
			}
			if (THEMETYPE_RANGE.equals(themeType)) {
				// 分段专题图
				themeContainer = new ThemeRangeContainer(layer, false);
				initThemePanel(layer, themeType, themeContainer);
				return;
			}
			if (THEMETYPE_LABEL_UNIFORM.equals(themeType)) {
				// 统一风格标签专题图
				themeContainer = new ThemeLabelUniformContainer(layer);
				initThemePanel(layer, themeType, themeContainer);
				return;
			}
			if (THEMETYPE_LABEL_RANGE.equals(themeType)) {
				// 分段风格标签专题图
				themeContainer = new ThemeLabelRangeContainer(layer, false);
				initThemePanel(layer, themeType, themeContainer);
				return;
			}
			if (THEMETYPE_LABEL_COMPLICATED.equals(themeType)) {
				// 复合风格标签专题图
				themeContainer = new ThemeLabelComplicatedContainer(layer);
				initThemePanel(layer, themeType, themeContainer);
				return;
			}
			if (THEMETYPE_GRID_UNIQUE.equals(themeType)) {
				// 栅格单值专题图
				themeContainer = new ThemeGridUniqueContainer(layer, false);
				initThemePanel(layer, themeType, themeContainer);
				return;
			}
			if (THEMETYPE_GRID_RANGE.equals(themeType)) {
				// 栅格分段专题图
				themeContainer = new ThemeGridRangeContainer(layer, false);
				initThemePanel(layer, themeType, themeContainer);
				return;
			}
			if (THEMETYPE_GRAPH.equals(themeType)) {
				// 统计专题图
				themeContainer = new ThemeGraphContainer(layer, false);
				initThemePanel(layer, themeType, themeContainer);
				return;
			}
			if (THEMETYPE_GRADUATEDSYMBOL.equals(themeType)) {
				// 等级符号专题图
				themeContainer = new ThemeGraduatedSymbolContainer(layer);
				initThemePanel(layer, themeType, themeContainer);
				return;
			}
			if (THEMETYPE_DOTDENSITY.equals(themeType)) {
				// 点密度专题图
				themeContainer = new ThemeDotDensityContainer(layer);
				initThemePanel(layer, themeType, themeContainer);
				return;
			}
			if (THEMETYPE_CUSTOM.equals(themeType)) {
				// 自定义专题图
				themeContainer = new ThemeCustomContainer(layer);
				initThemePanel(layer, themeType, themeContainer);
				return;
			}
		}
	}

	private static boolean hasThemeContainer(Layer layer) {
		return null != themeTypeContainer.get(layer);
	}

	private static void initThemePanel(Layer layer, String themeType, ThemeChangePanel themeContainer) {
		themeTypeContainer.put(layer, themeContainer);
		if (null == container) {
			container = (ThemeMainContainer) getDockbarThemeContainer().getInnerComponent();
		}
		container.setPanel(themeContainer);
		layerPropertyChange(themeContainer);
	}

	private static Layer getActiveLayer() {
		IFormMap formMap = (IFormMap) Application.getActiveApplication().getActiveForm();
		Layer layer = null;
		if (0 < formMap.getActiveLayers().length) {
			layer = formMap.getActiveLayers()[0];
		} else {
			layer = formMap.getMapControl().getMap().getLayers().get(0);
		}
		return layer;
	}

	private static Dataset getDataset() {
		return getActiveLayer().getDataset();
	}

	/**
	 * 修改专题图图层
	 *
	 * @param layer
	 */
	public static void modifyTheme(Layer layer) {
		if (null != layer && null != layer.getDataset()) {
			resetTheme(layer, getThemeType(layer));
		}
	}
}
