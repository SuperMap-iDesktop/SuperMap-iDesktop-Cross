package com.supermap.desktop.newtheme;

import com.supermap.data.ColorGradientType;
import com.supermap.data.Dataset;
import com.supermap.data.DatasetGrid;
import com.supermap.data.DatasetVector;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IDockbar;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.mapview.MapViewProperties;
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

import net.infonode.properties.propertymap.ref.ThisPropertyMapRef;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.MessageFormat;
import java.util.HashMap;

public class ThemeGuideFactory {

	private static final String THEME_MAIN_CONTAINER_CLASS = "com.supermap.desktop.newtheme.ThemeMainContainer";
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
				dockbarThemeContainer = Application.getActiveApplication().getMainFrame().getDockbarManager()
						.get(Class.forName(THEME_MAIN_CONTAINER_CLASS));
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return dockbarThemeContainer;
	}

	private static ThemeMainContainer addPanelToThemeMainContainer(ThemeChangePanel panel) {
		try {
			dockbarThemeContainer = Application.getActiveApplication().getMainFrame().getDockbarManager()
					.get(Class.forName(THEME_MAIN_CONTAINER_CLASS));
			if (dockbarThemeContainer != null) {
				container = (ThemeMainContainer) dockbarThemeContainer.getComponent();
				container.setPanel(panel);
			}
			panel.addPropertyChangeListener("ThemeChange", new PropertyChangeListener() {

				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					if (!container.getCheckBoxRefreshAtOnce().isSelected()) {
						container.getButtonApply().setEnabled(true);
					}
				}
			});
		} catch (ClassNotFoundException e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			if (null == getDockbarThemeContainer()) {
				container.unregistActionListener();
			}
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
	static MapControl getMapControl() {
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
	public static void buildUniqueTheme() {
		String expression = "SmUserID";
		if (UniqueValueCountUtil.isCountBeyond((DatasetVector) getDataset(), expression)) {
			// 字段记录数大于3000条时建议不做专题图
			JOptionPane.showMessageDialog(null, MapViewProperties.getString("String_ThemeGridUnique_MessageBoxInfo"),
					CoreProperties.getString("String_MessageBox_Title"), JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		ThemeUnique themeUnique = ThemeUnique.makeDefault((DatasetVector) getDataset(), getDataset().getName() + "." + expression,
				ColorGradientType.GREENORANGEVIOLET);
		if (null != themeUnique) {
			ThemeUniqueContainer themeUniqueContainer = new ThemeUniqueContainer((DatasetVector) getDataset(), themeUnique);
			themeTypeContainer.put(themeUniqueContainer.getThemeUniqueLayer().getCaption(), themeUniqueContainer);
			addPanelToThemeMainContainer(themeUniqueContainer);
			getDockbarThemeContainer().setVisible(true);
		} else {
			UICommonToolkit.showMessageDialog(MapViewProperties.getString("String_Theme_UpdataFailed"));
		}

	}

	/**
	 * 新建分段专题图
	 */
	public static void buildRangeTheme() {
		ThemeRange themeRange = ThemeRange.makeDefault((DatasetVector) getDataset(), getDataset().getName() + "." + "SmID", RangeMode.EQUALINTERVAL, 5,
				ColorGradientType.GREENRED, null, 0.1);
		if (null != themeRange) {
			themeRange.setPrecision(0.1);
			ThemeRangeContainer themeRangeContainer = new ThemeRangeContainer((DatasetVector) getDataset(), themeRange);
			themeTypeContainer.put(themeRangeContainer.getThemeRangeLayer().getCaption(), themeRangeContainer);
			addPanelToThemeMainContainer(themeRangeContainer);
			getDockbarThemeContainer().setVisible(true);
		} else {
			UICommonToolkit.showMessageDialog(MapViewProperties.getString("String_Theme_UpdataFailed"));
		}
	}

	/**
	 * 新建统一风格标签专题图
	 */
	public static void buildLabelUnformTheme() {
		ThemeLabel themeLabel = new ThemeLabel();
		themeLabel.setMaxLabelLength(8);
		ThemeLabelUniformContainer themeLabelUniformContainer = new ThemeLabelUniformContainer((DatasetVector) getDataset(), themeLabel);
		themeTypeContainer.put(themeLabelUniformContainer.getThemeLabelLayer().getCaption(), themeLabelUniformContainer);
		addPanelToThemeMainContainer(themeLabelUniformContainer);
		getDockbarThemeContainer().setVisible(true);
	}

	/**
	 * 新建分段风格标签专题图
	 */
	public static void buildLabelRangeTheme() {
		ThemeLabel themeLabel = ThemeLabel.makeDefault((DatasetVector) getDataset(), getDataset().getName() + "." + "SmID", RangeMode.EQUALINTERVAL, 5,
				ColorGradientType.GREENRED);
		if (null != themeLabel && themeLabel.getCount() >= 2) {
			themeLabel.setMaxLabelLength(8);
			ThemeLabelRangeContainer themeLabelRangeContainer = new ThemeLabelRangeContainer((DatasetVector) getDataset(), themeLabel);
			themeTypeContainer.put(themeLabelRangeContainer.getThemeLabelLayer().getCaption(), themeLabelRangeContainer);
			addPanelToThemeMainContainer(themeLabelRangeContainer);
			getDockbarThemeContainer().setVisible(true);
		} else {
			UICommonToolkit.showMessageDialog(MapViewProperties.getString("String_Theme_UpdataFailed"));
		}
	}

	/**
	 * 新建栅格单值专题图
	 */
	public static void buildGridUniqueTheme() {
		DatasetGrid datasetGrid = (DatasetGrid) getDataset();
		int rowCount = datasetGrid.getRowBlockCount();
		int columCount = datasetGrid.getColumnBlockCount();
		datasetGrid.buildStatistics();
		try {
			if (rowCount * columCount > 3000) {
				UICommonToolkit.showMessageDialog(MapViewProperties.getString("String_ThemeUniqueMaxCount"));
				return;
			}
			ThemeGridUnique themeUnique = ThemeGridUnique.makeDefault(datasetGrid, ColorGradientType.GREENORANGEVIOLET);
			if (null != themeUnique) {
				ThemeGridUniqueContainer themeUniqueContainer = new ThemeGridUniqueContainer((DatasetGrid) getDataset(), themeUnique);
				themeTypeContainer.put(themeUniqueContainer.getThemeUniqueLayer().getCaption(), themeUniqueContainer);
				addPanelToThemeMainContainer(themeUniqueContainer);
				getDockbarThemeContainer().setVisible(true);
			} else {
				UICommonToolkit.showMessageDialog(MapViewProperties.getString("String_Theme_UpdataFailed"));
			}
		} catch (Exception e) {
			Application
					.getActiveApplication()
					.getOutput()
					.output(MessageFormat.format(MapViewProperties.getString("String_ThemeGridUnique_MessagePixelFormat"), datasetGrid.getName() + "@"
							+ datasetGrid.getDatasource().getAlias()));
		}
	}

	/**
	 * 新建栅格分段专题图
	 */
	public static void buildGridRangeTheme() {
		DatasetGrid datasetGrid = (DatasetGrid) getDataset();
		try {
			ThemeGridRange themeUnique = ThemeGridRange.makeDefault(datasetGrid, RangeMode.EQUALINTERVAL, 5, ColorGradientType.GREENORANGEVIOLET);
			if (null != themeUnique) {
				ThemeGridRangeContainer themeGridRangeContainer = new ThemeGridRangeContainer((DatasetGrid) getDataset(), themeUnique);
				themeTypeContainer.put(themeGridRangeContainer.getThemeRangeLayer().getCaption(), themeGridRangeContainer);
				addPanelToThemeMainContainer(themeGridRangeContainer);
				getDockbarThemeContainer().setVisible(true);
			} else {
				UICommonToolkit.showMessageDialog(MapViewProperties.getString("String_Theme_UpdataFailed"));
			}
		} catch (Exception e) {
			Application
					.getActiveApplication()
					.getOutput()
					.output(MessageFormat.format(MapViewProperties.getString("String_ThemeGridUnique_MessagePixelFormat"), datasetGrid.getName() + "@"
							+ datasetGrid.getDatasource().getAlias()));
		}
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
		}else {
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
		}else {
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
		}else {
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
		}else {
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
		}else {
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
		}else {
			ThemeGridRangeContainer themeGridRangeContainer = new ThemeGridRangeContainer(layer);
			themeTypeContainer.put(layer.getCaption(), themeGridRangeContainer);
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
			}
		}
	}
}
