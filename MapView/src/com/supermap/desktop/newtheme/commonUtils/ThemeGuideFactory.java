package com.supermap.desktop.newtheme.commonUtils;

import com.supermap.data.ColorGradientType;
import com.supermap.data.CursorType;
import com.supermap.data.Dataset;
import com.supermap.data.DatasetGrid;
import com.supermap.data.DatasetVector;
import com.supermap.data.GeoStyle;
import com.supermap.data.Point2D;
import com.supermap.data.Recordset;
import com.supermap.data.StatisticMode;
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
import com.supermap.desktop.newtheme.themeLabel.ThemeLabelRangeContainer;
import com.supermap.desktop.newtheme.themeLabel.ThemeLabelUniformContainer;
import com.supermap.desktop.newtheme.themeRange.ThemeRangeContainer;
import com.supermap.desktop.newtheme.themeUnique.ThemeUniqueContainer;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.LayersTree;
import com.supermap.desktop.ui.controls.TreeNodeData;
import com.supermap.mapping.GraduatedMode;
import com.supermap.mapping.Layer;
import com.supermap.mapping.MapClosedEvent;
import com.supermap.mapping.MapClosedListener;
import com.supermap.mapping.RangeMode;
import com.supermap.mapping.ThemeCustom;
import com.supermap.mapping.ThemeDotDensity;
import com.supermap.mapping.ThemeGraduatedSymbol;
import com.supermap.mapping.ThemeGraph;
import com.supermap.mapping.ThemeGraphItem;
import com.supermap.mapping.ThemeGraphType;
import com.supermap.mapping.ThemeGridRange;
import com.supermap.mapping.ThemeGridUnique;
import com.supermap.mapping.ThemeLabel;
import com.supermap.mapping.ThemeRange;
import com.supermap.mapping.ThemeType;
import com.supermap.mapping.ThemeUnique;
import com.supermap.ui.MapControl;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;

public class ThemeGuideFactory {

	private static final String THEME_MAIN_CONTAINER_CLASS = "com.supermap.desktop.newtheme.commonPanel.ThemeMainContainer";
	private static IDockbar dockbarThemeContainer;
	private static transient ThemeMainContainer container;
	public static HashMap<String, ThemeChangePanel> themeTypeContainer = new HashMap<String, ThemeChangePanel>();
	private static final String THEMETYPE_UNIQUE = "ThemeType_Unique";
	private static final String THEMETYPE_RANGE = "ThemeType_Range";
	private static final String THEMETYPE_LABEL_UNIFORM = "ThemeType_Label_Uniform";
	private static final String THEMETYPE_LABEL_RANGE = "ThemeType_Label_Range";
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
				container = (ThemeMainContainer) dockbarThemeContainer.getComponent();
				container.setPanel(panel);
			}
			if (null != panel.getCurrentLayer() && panel.getCurrentLayer().isDisposed()) {
				panel.setCurrentLayer(layer);
			}
			layerPropertyChange(panel);
			getMapControl().getMap().addMapClosedListener(new MapClosedListener() {

				@Override
				public void mapClosed(MapClosedEvent arg0) {
					if (null != arg0.getMap()) {
						container.setLayerPropertyChanged(false);
						themeTypeContainer.clear();
						// 移除事件
						HashMap<String, ThemeChangePanel> themeContainers = ThemeGuideFactory.themeTypeContainer;
						Iterator<?> iterator = themeContainers.entrySet().iterator();
						while (iterator.hasNext()) {
							java.util.Map.Entry<?, ?> entry = (java.util.Map.Entry<?, ?>) iterator.next();
							((ThemeChangePanel) entry.getValue()).unregistActionListener();
						}
					}
				}
			});
		} catch (ClassNotFoundException e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		if (null != container) {
			return container;
		}
		return null;
	}

	/**
	 * 根据专题图图层获取名称
	 * 
	 * @param layer
	 * @return
	 */
	public static String getThemeTypeString(Layer layer) {
		return layer.getName() + "@" + getThemeType(layer);
	}

	private static String getThemeType(Layer layer) {
		String name = "";
		if (null != layer && !layer.isDisposed() && null != layer.getTheme()) {
			int count = -1;
			if (layer.getTheme() instanceof ThemeLabel) {
				count = ((ThemeLabel) layer.getTheme()).getCount();
			}
			name = getThemeTypeString(layer.getTheme().getType(), count);
		}
		return name;
	}

	private static String getThemeTypeString(ThemeType themetype, int count) {
		String result = "";
		if (themetype == ThemeType.UNIQUE) {
			result = THEMETYPE_UNIQUE;
		}
		if (themetype == ThemeType.RANGE) {
			result = THEMETYPE_RANGE;
		}
		if (themetype == ThemeType.LABEL && count == 0) {
			result = THEMETYPE_LABEL_UNIFORM;
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
			ThemeUnique themeUnique = ThemeUnique.makeDefault((DatasetVector) getDataset(), expression, ColorGradientType.GREENORANGEVIOLET);
			if (null != themeUnique) {
				for (int i = 0; i < themeUnique.getCount(); i++) {
					GeoStyle textStyle = themeUnique.getItem(i).getStyle();
					textStyle.setLineColor(Color.GRAY);
				}
				success = true;
				ThemeUniqueContainer themeUniqueContainer = new ThemeUniqueContainer((DatasetVector) getDataset(), themeUnique, layer);
				themeTypeContainer.put(themeUniqueContainer.getThemeUniqueLayer().getName() + "@" + THEMETYPE_UNIQUE, themeUniqueContainer);
				addPanelToThemeMainContainer(themeUniqueContainer, null);
				getDockbarThemeContainer().setVisible(true);
			} else {
				success = false;
				UICommonToolkit.showMessageDialog(MapViewProperties.getString("String_Theme_UpdataFailed"));
			}
		}
		return success;
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
				ThemeRangeContainer themeRangeContainer = new ThemeRangeContainer((DatasetVector) getDataset(), themeRange, layer);
				themeTypeContainer.put(themeRangeContainer.getThemeRangeLayer().getName() + "@" + THEMETYPE_RANGE, themeRangeContainer);
				addPanelToThemeMainContainer(themeRangeContainer, null);
				getDockbarThemeContainer().setVisible(true);
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
			ThemeLabelUniformContainer themeLabelUniformContainer = new ThemeLabelUniformContainer((DatasetVector) getDataset(), themeLabel, layer);
			themeTypeContainer.put(themeLabelUniformContainer.getThemeLabelLayer().getName() + "@" + THEMETYPE_LABEL_UNIFORM, themeLabelUniformContainer);
			addPanelToThemeMainContainer(themeLabelUniformContainer, null);
			getDockbarThemeContainer().setVisible(true);
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
				ThemeLabelRangeContainer themeLabelRangeContainer = new ThemeLabelRangeContainer((DatasetVector) getDataset(), themeLabel, layer);
				themeTypeContainer.put(themeLabelRangeContainer.getThemeLabelLayer().getName() + "@" + THEMETYPE_LABEL_RANGE, themeLabelRangeContainer);
				addPanelToThemeMainContainer(themeLabelRangeContainer, null);
				getDockbarThemeContainer().setVisible(true);
				success = true;
			} else {
				success = false;
				UICommonToolkit.showMessageDialog(MapViewProperties.getString("String_Theme_UpdataFailed"));
			}
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
					ThemeGridUniqueContainer themeUniqueContainer = new ThemeGridUniqueContainer((DatasetGrid) getDataset(), themeUnique);
					themeTypeContainer.put(themeUniqueContainer.getThemeUniqueLayer().getName() + "@" + THEMETYPE_GRID_UNIQUE, themeUniqueContainer);
					addPanelToThemeMainContainer(themeUniqueContainer, null);
					getDockbarThemeContainer().setVisible(true);
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
				ThemeGridRange themeUnique = ThemeGridRange.makeDefault(datasetGrid, RangeMode.EQUALINTERVAL, 5, ColorGradientType.GREENORANGEVIOLET);

				if (null != themeUnique) {
					success = true;
					ThemeGridRangeContainer themeGridRangeContainer = new ThemeGridRangeContainer((DatasetGrid) getDataset(), themeUnique);
					themeTypeContainer.put(themeGridRangeContainer.getThemeRangeLayer().getName() + "@" + THEMETYPE_GRID_RANGE, themeGridRangeContainer);
					addPanelToThemeMainContainer(themeGridRangeContainer, null);
					getDockbarThemeContainer().setVisible(true);
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
			ThemeGraphContainer themeGraphContainer = new ThemeGraphContainer(datasetVector, themeGraph, layer);
			themeTypeContainer.put(themeGraphContainer.getThemeGraphLayer().getName() + "@" + THEMETYPE_GRAPH, themeGraphContainer);
			addPanelToThemeMainContainer(themeGraphContainer, null);
			getDockbarThemeContainer().setVisible(true);
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
			if (null != themeGraduated) {
				successed = true;
				themeGraduated.setExpression(expression);
				ThemeGraduatedSymbolContainer themeGraduatedContainer = new ThemeGraduatedSymbolContainer((DatasetVector) getDataset(), themeGraduated, layer);
				themeTypeContainer.put(themeGraduatedContainer.getCurrentLayer().getName() + "@" + THEMETYPE_GRADUATEDSYMBOL, themeGraduatedContainer);
				addPanelToThemeMainContainer(themeGraduatedContainer, null);
				getDockbarThemeContainer().setVisible(true);
			}
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
			double maxValue = getMaxValue((DatasetVector) getDataset(), "SmID");
			themeDotDensity.setValue(maxValue / 1000);
			ThemeDotDensityContainer themeDotDensityContainer = new ThemeDotDensityContainer((DatasetVector) getDataset(), themeDotDensity, layer);
			themeTypeContainer.put(themeDotDensityContainer.getCurrentLayer().getName() + "@" + THEMETYPE_DOTDENSITY, themeDotDensityContainer);
			addPanelToThemeMainContainer(themeDotDensityContainer, null);
			getDockbarThemeContainer().setVisible(true);
		}
		return successed;
	}
	
	public static boolean buildCustomTheme(Layer layer){
		boolean successed = false;
		if (null!=getDataset()) {
			ThemeCustom themeCustom = new ThemeCustom();
			successed = true;
			ThemeCustomContainer themeCustomContainer = new ThemeCustomContainer((DatasetVector) getDataset(), themeCustom, layer);
			themeTypeContainer.put(themeCustomContainer.getCurrentLayer().getName()+"@"+THEMETYPE_CUSTOM, themeCustomContainer);
			addPanelToThemeMainContainer(themeCustomContainer, null);
			getDockbarThemeContainer().setVisible(true);
		}
		return successed;
	}

	/**
	 * 计算矢量数据集中某个字段的最大值
	 * 
	 * @param datasetVector
	 * @return
	 */
	public static double getMaxValue(DatasetVector datasetVector, String expression) {
		Recordset recordset = datasetVector.getRecordset(false, CursorType.STATIC);
		double maxValue = recordset.statistic(expression, StatisticMode.MAX);
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
			addPanelToThemeMainContainer(themeTypeContainer.get(layer.getName() + "@" + themeType), layer);
		} else {
			ThemeChangePanel themeContainer = null;
			if (THEMETYPE_UNIQUE.equals(themeType)) {
				// 单值专题图
				themeContainer = new ThemeUniqueContainer(layer);
				initThemePanel(layer, themeType, themeContainer);
				return;
			}
			if (THEMETYPE_RANGE.equals(themeType)) {
				// 分段专题图
				themeContainer = new ThemeRangeContainer(layer);
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
				themeContainer = new ThemeLabelRangeContainer(layer);
				initThemePanel(layer, themeType, themeContainer);
				return;
			}
			if (THEMETYPE_GRID_UNIQUE.equals(themeType)) {
				// 栅格单值专题图
				themeContainer = new ThemeGridUniqueContainer(layer);
				initThemePanel(layer, themeType, themeContainer);
				return;
			}
			if (THEMETYPE_GRID_RANGE.equals(themeType)) {
				// 栅格分段专题图
				themeContainer = new ThemeGridRangeContainer(layer);
				initThemePanel(layer, themeType, themeContainer);
				return;
			}
			if (THEMETYPE_GRAPH.equals(themeType)) {
				// 统计专题图
				themeContainer = new ThemeGraphContainer(layer);
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
				//自定义专题图
				themeContainer = new ThemeCustomContainer(layer);
				initThemePanel(layer, themeType, themeContainer);
				return;
			}
		}
	}

	private static boolean hasThemeContainer(Layer layer) {
		return null != themeTypeContainer.get(getThemeTypeString(layer));
	}

	private static void initThemePanel(Layer layer, String themeType, ThemeChangePanel themeContainer) {
		themeTypeContainer.put(layer.getName() + "@" + themeType, themeContainer);
		if (null == container) {
			container = (ThemeMainContainer) getDockbarThemeContainer().getComponent();
		}
		container.setPanel(themeContainer);
		layerPropertyChange(themeContainer);
	}

	private static Dataset getDataset() {
		Dataset dataset = null;
		IFormMap formMap = (IFormMap) Application.getActiveApplication().getActiveForm();
		Layer layer = null;
		if (0 < formMap.getActiveLayers().length) {
			layer = formMap.getActiveLayers()[0];
		} else {
			layer = formMap.getMapControl().getMap().getLayers().get(0);
		}
		dataset = layer.getDataset();
		return dataset;
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
