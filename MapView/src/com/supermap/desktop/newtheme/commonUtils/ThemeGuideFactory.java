package com.supermap.desktop.newtheme.commonUtils;

import com.supermap.data.ColorGradientType;
import com.supermap.data.Dataset;
import com.supermap.data.DatasetGrid;
import com.supermap.data.DatasetVector;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IDockbar;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.newtheme.commonPanel.ThemeChangePanel;
import com.supermap.desktop.newtheme.commonPanel.ThemeMainContainer;
import com.supermap.desktop.newtheme.themeGraph.ThemeGraphContainer;
import com.supermap.desktop.newtheme.themeGridRange.ThemeGridRangeContainer;
import com.supermap.desktop.newtheme.themeGridUnique.ThemeGridUniqueContainer;
import com.supermap.desktop.newtheme.themeLabel.ThemeLabelRangeContainer;
import com.supermap.desktop.newtheme.themeLabel.ThemeLabelUniformContainer;
import com.supermap.desktop.newtheme.themeRange.ThemeRangeContainer;
import com.supermap.desktop.newtheme.themeUnique.ThemeUniqueContainer;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.LayersTree;
import com.supermap.desktop.ui.controls.TreeNodeData;
import com.supermap.mapping.*;
import com.supermap.ui.MapControl;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.MessageFormat;
import java.util.HashMap;

public class ThemeGuideFactory {

	private static final String THEME_MAIN_CONTAINER_CLASS = "com.supermap.desktop.newtheme.commonPanel.ThemeMainContainer";
	private static IDockbar dockbarThemeContainer;
	private static transient ThemeMainContainer container;
	public static HashMap<String, ThemeChangePanel> themeTypeContainer = new HashMap<String, ThemeChangePanel>();

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

	private static ThemeMainContainer addPanelToThemeMainContainer(final ThemeChangePanel panel) {
		try {
			dockbarThemeContainer = Application.getActiveApplication().getMainFrame().getDockbarManager().get(Class.forName(THEME_MAIN_CONTAINER_CLASS));
			if (dockbarThemeContainer != null) {
				container = (ThemeMainContainer) dockbarThemeContainer.getComponent();
				container.setPanel(panel);
			}
			panel.addPropertyChangeListener("ThemeChange", new PropertyChangeListener() {

				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					if (!container.getCheckBoxRefreshAtOnce().isSelected()) {
						container.getButtonApply().setEnabled(true);
						container.setLayerPropertyChanged(true);
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
	public static boolean buildUniqueTheme() {
		boolean success = false;
		if (null != getDataset()) {
			String expression = "SmUserID";
			if (UniqueValueCountUtil.isCountBeyond((DatasetVector) getDataset(), expression)) {
				// 字段记录数大于3000条时建议不做专题图
				JOptionPane.showMessageDialog(null, MapViewProperties.getString("String_ThemeGridUnique_MessageBoxInfo"),
						CoreProperties.getString("String_MessageBox_Title"), JOptionPane.INFORMATION_MESSAGE);
				return success;
			}
			ThemeUnique themeUnique = ThemeUnique.makeDefault((DatasetVector) getDataset(), getDataset().getName() + "." + expression,
					ColorGradientType.GREENORANGEVIOLET);
			if (null != themeUnique) {
				success = true;
				ThemeUniqueContainer themeUniqueContainer = new ThemeUniqueContainer((DatasetVector) getDataset(), themeUnique);
				themeTypeContainer.put(themeUniqueContainer.getThemeUniqueLayer().getCaption(), themeUniqueContainer);
				addPanelToThemeMainContainer(themeUniqueContainer);
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
	public static boolean buildRangeTheme() {
		boolean success = false;
		if (null != getDataset()) {
			ThemeRange themeRange = ThemeRange.makeDefault((DatasetVector) getDataset(), getDataset().getName() + "." + "SmID", RangeMode.EQUALINTERVAL, 5,
					ColorGradientType.GREENRED, null, 0.1);
			if (null != themeRange) {
				success = true;
				themeRange.setPrecision(0.1);
				ThemeRangeContainer themeRangeContainer = new ThemeRangeContainer((DatasetVector) getDataset(), themeRange);
				themeTypeContainer.put(themeRangeContainer.getThemeRangeLayer().getCaption(), themeRangeContainer);
				addPanelToThemeMainContainer(themeRangeContainer);
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
	public static void buildLabelUnformTheme() {
		if (null != getDataset()) {
			ThemeLabel themeLabel = new ThemeLabel();
			themeLabel.setMaxLabelLength(8);
			ThemeLabelUniformContainer themeLabelUniformContainer = new ThemeLabelUniformContainer((DatasetVector) getDataset(), themeLabel);
			themeTypeContainer.put(themeLabelUniformContainer.getThemeLabelLayer().getCaption(), themeLabelUniformContainer);
			addPanelToThemeMainContainer(themeLabelUniformContainer);
			getDockbarThemeContainer().setVisible(true);
		}
	}

	/**
	 * 新建分段风格标签专题图
	 */
	public static boolean buildLabelRangeTheme() {
		boolean success = false;
		if (null != getDataset()) {
			ThemeLabel themeLabel = ThemeLabel.makeDefault((DatasetVector) getDataset(), getDataset().getName() + "." + "SmID", RangeMode.EQUALINTERVAL, 5,
					ColorGradientType.GREENRED);
			if (null != themeLabel && themeLabel.getCount() >= 2) {
				themeLabel.setMaxLabelLength(8);
				ThemeLabelRangeContainer themeLabelRangeContainer = new ThemeLabelRangeContainer((DatasetVector) getDataset(), themeLabel);
				themeTypeContainer.put(themeLabelRangeContainer.getThemeLabelLayer().getCaption(), themeLabelRangeContainer);
				addPanelToThemeMainContainer(themeLabelRangeContainer);
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
			int rowCount = datasetGrid.getRowBlockCount();
			int columCount = datasetGrid.getColumnBlockCount();
			datasetGrid.buildStatistics();
			try {
				if (rowCount * columCount > 3000) {
					UICommonToolkit.showMessageDialog(MapViewProperties.getString("String_ThemeUniqueMaxCount"));
					return success;
				}
				ThemeGridUnique themeUnique = ThemeGridUnique.makeDefault(datasetGrid, ColorGradientType.GREENORANGEVIOLET);
				if (null != themeUnique) {
					success = true;
					ThemeGridUniqueContainer themeUniqueContainer = new ThemeGridUniqueContainer((DatasetGrid) getDataset(), themeUnique);
					themeTypeContainer.put(themeUniqueContainer.getThemeUniqueLayer().getCaption(), themeUniqueContainer);
					addPanelToThemeMainContainer(themeUniqueContainer);
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
					themeTypeContainer.put(themeGridRangeContainer.getThemeRangeLayer().getCaption(), themeGridRangeContainer);
					addPanelToThemeMainContainer(themeGridRangeContainer);
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
	 * 新建统计专题图
	 */
	public static boolean buildGraphTheme() {
		boolean success = false;
		if (null != getDataset()) {
			if (UniqueValueCountUtil.isCountBeyond((DatasetVector) getDataset(), "SmID")) {
				// 字段记录数大于3000条时建议不做专题图
				JOptionPane.showMessageDialog(null, MapViewProperties.getString("String_ThemeGridUnique_MessageBoxInfo"),
						CoreProperties.getString("String_MessageBox_Title"), JOptionPane.INFORMATION_MESSAGE);
				return success;
			} else {
				success = true;
				DatasetVector datasetVector = (DatasetVector) getDataset();
				ThemeGraph themeGraph = new ThemeGraph();
				ThemeGraphItem themeGraphItem = new ThemeGraphItem();
				themeGraphItem.setGraphExpression(datasetVector.getName() + "." + "SmID");
				themeGraphItem.setCaption("SmID");
				themeGraph.insert(0, themeGraphItem);
				themeGraph.setGraphType(ThemeGraphType.PIE3D);
				themeGraph.setMaxGraphSize(1);
				themeGraph.setAxesDisplayed(false);
				ThemeGraphContainer themeGraphContainer = new ThemeGraphContainer(datasetVector, themeGraph);
				themeTypeContainer.put(themeGraphContainer.getThemeGraphLayer().getCaption(), themeGraphContainer);
				addPanelToThemeMainContainer(themeGraphContainer);
				getDockbarThemeContainer().setVisible(true);
			}
		}
		return success;
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
	 * 修改单值专题图
	 *
	 * @param layer
	 */
	public static void resetUniqueTheme(Layer layer) {
		if (null != layer.getDataset() && null != themeTypeContainer.get(layer.getCaption())) {
			addPanelToThemeMainContainer(themeTypeContainer.get(layer.getCaption()));
		} else if (null != layer.getDataset()) {
			ThemeUniqueContainer themeUniqueContainer = new ThemeUniqueContainer(layer);
			themeTypeContainer.put(layer.getCaption(), themeUniqueContainer);
		}
	}

	/**
	 * 修改分段专题图
	 *
	 * @param layer
	 */
	public static void resetRangeTheme(Layer layer) {
		if (null != layer.getDataset() && null != themeTypeContainer.get(layer.getCaption())) {
			addPanelToThemeMainContainer(themeTypeContainer.get(layer.getCaption()));
		} else if (null != layer.getDataset()) {
			ThemeRangeContainer themeRangeContainer = new ThemeRangeContainer(layer);
			themeTypeContainer.put(layer.getCaption(), themeRangeContainer);
		}
	}

	/**
	 * 修改标签统一风格专题图
	 *
	 * @param layer
	 */
	public static void resetLabelUniform(Layer layer) {
		if (null != layer.getDataset() && null != themeTypeContainer.get(layer.getCaption())) {
			addPanelToThemeMainContainer(themeTypeContainer.get(layer.getCaption()));
		} else if (null != layer.getDataset()) {
			ThemeLabelUniformContainer themeLabelUniformContainer = new ThemeLabelUniformContainer(layer);
			themeTypeContainer.put(layer.getCaption(), themeLabelUniformContainer);
		}
	}

	/**
	 * 修改标签分段风格专题图
	 *
	 * @param layer
	 */
	public static void resetLabelRange(Layer layer) {
		if (null != layer.getDataset() && null != themeTypeContainer.get(layer.getCaption())) {
			addPanelToThemeMainContainer(themeTypeContainer.get(layer.getCaption()));
		} else if (null != layer.getDataset()) {
			ThemeLabelRangeContainer themeLabelRangeContainer = new ThemeLabelRangeContainer(layer);
			themeTypeContainer.put(layer.getCaption(), themeLabelRangeContainer);
		}
	}

	/**
	 * 修改栅格单值专题图
	 *
	 * @param layer
	 */
	public static void resetGridUnique(Layer layer) {
		if (null != layer.getDataset() && null != themeTypeContainer.get(layer.getCaption())) {
			addPanelToThemeMainContainer(themeTypeContainer.get(layer.getCaption()));
		} else if (null != layer.getDataset()) {
			ThemeGridUniqueContainer themeGridUniqueContainer = new ThemeGridUniqueContainer(layer);
			themeTypeContainer.put(layer.getCaption(), themeGridUniqueContainer);
		}
	}

	/**
	 * 修改栅格分段专题图
	 *
	 * @returnff
	 */
	public static void resetGridRange(Layer layer) {
		if (null != layer.getDataset() && null != themeTypeContainer.get(layer.getCaption())) {
			addPanelToThemeMainContainer(themeTypeContainer.get(layer.getCaption()));
		} else if (null != layer.getDataset()) {
			ThemeGridRangeContainer themeGridRangeContainer = new ThemeGridRangeContainer(layer);
			themeTypeContainer.put(layer.getCaption(), themeGridRangeContainer);
		}
	}

	public static void resetGraph(Layer layer) {
		if (null != layer.getDataset() && null != themeTypeContainer.get(layer.getCaption())) {
			addPanelToThemeMainContainer(themeTypeContainer.get(layer.getCaption()));
		} else if (null != layer.getDataset()) {
			ThemeGraphContainer themeGraphContainer = new ThemeGraphContainer(layer);
			themeTypeContainer.put(layer.getCaption(), themeGraphContainer);
		}
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
	 * 根据
	 *
	 * @param layer
	 */
	public static void modifyTheme(Layer layer) {
		if (null != layer) {
			if (layer.getTheme() instanceof ThemeUnique) {
				resetUniqueTheme(layer);
			} else if (layer.getTheme() instanceof ThemeRange) {
				resetRangeTheme(layer);
			} else if ((layer.getTheme() instanceof ThemeLabel) && ((ThemeLabel) layer.getTheme()).getCount() > 0) {
				resetLabelRange(layer);
			} else if ((layer.getTheme() instanceof ThemeLabel) && ((ThemeLabel) layer.getTheme()).getCount() == 0) {
				resetLabelUniform(layer);
			} else if ((layer.getTheme() instanceof ThemeGridUnique)) {
				resetGridUnique(layer);
			} else if (layer.getTheme() instanceof ThemeGridRange) {
				resetGridRange(layer);
			} else if (layer.getTheme() instanceof ThemeGraph) {
				resetGraph(layer);
			}
		}
	}
}
