package com.supermap.desktop.newtheme;

import java.awt.GridBagConstraints;
import java.text.MessageFormat;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;

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
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.LayersTree;
import com.supermap.desktop.ui.controls.TreeNodeData;
import com.supermap.mapping.Layer;
import com.supermap.mapping.Layers;
import com.supermap.mapping.Map;
import com.supermap.mapping.RangeMode;
import com.supermap.mapping.ThemeGridRange;
import com.supermap.mapping.ThemeGridUnique;
import com.supermap.mapping.ThemeLabel;
import com.supermap.mapping.ThemeRange;
import com.supermap.mapping.ThemeUnique;
import com.supermap.ui.MapControl;

public class ThemeGuideFactory {

	private static final String THEME_MAIN_CONTAINER_CLASS = "com.supermap.desktop.newtheme.ThemeMainContainer";
	private static IDockbar dockbarThemeContainer;

	/**
	 * 界面替换
	 * 
	 * @param panel
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dockbarThemeContainer;
	}

	private static ThemeMainContainer addPanelToThemeMainContainer(JPanel panel) {
		ThemeMainContainer container = null;
		try {
			dockbarThemeContainer = Application.getActiveApplication().getMainFrame().getDockbarManager()
					.get(Class.forName(THEME_MAIN_CONTAINER_CLASS));
			if (dockbarThemeContainer != null) {
				container = (ThemeMainContainer) dockbarThemeContainer.getComponent();
				if (null != container.getPanelThemeInfo()) {
					container.remove(container.getPanelThemeInfo());
				}
				for (int i = container.getComponents().length - 1; i >= 0; i--) {
					if (container.getComponent(i) instanceof JPanel) {
						container.remove(container.getComponent(i));
					}
				}
				container.add(
						panel,
						new GridBagConstraintsHelper(0, 1, 2, 1).setWeight(3, 3).setInsets(5).setAnchor(GridBagConstraints.CENTER).setIpad(0, 0)
								.setFill(GridBagConstraints.BOTH));
				container.repaint();
			}

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
		} else {
			ThemeUnique themeUnique = ThemeUnique.makeDefault((DatasetVector) getDataset(), expression, ColorGradientType.GREENORANGEVIOLET);
			if (null != themeUnique) {
				ThemeUniqueContainer themeUniqueContainer = new ThemeUniqueContainer((DatasetVector) getDataset(), themeUnique);
				ThemeMainContainer container = addPanelToThemeMainContainer(themeUniqueContainer);
				Layers layers = getMapControl().getMap().getLayers();
				for (int i = 0; i < layers.getCount(); i++) {
					Layer tempLayer = layers.get(i);
					container.addItemToComboBox(tempLayer);
				}
				container.getComboBoxThemeLayer().setSelectedItem(themeUniqueContainer.getThemeUniqueLayer().getName());
				getDockbarThemeContainer().setVisible(true);
			} else {
				UICommonToolkit.showMessageDialog(MapViewProperties.getString("String_Theme_UpdataFailed"));
			}
		}
	}

	/**
	 * 新建分段专题图
	 */
	public static void buildRangeTheme() {
		ThemeRange themeRange = ThemeRange.makeDefault((DatasetVector) getDataset(), "SmID", RangeMode.EQUALINTERVAL, 5, ColorGradientType.GREENRED, null, 0.1);
		if (null != themeRange) {
			themeRange.setPrecision(0.1);
			ThemeRangeContainer themeRangeContainer = new ThemeRangeContainer((DatasetVector) getDataset(), themeRange);
			ThemeMainContainer container = addPanelToThemeMainContainer(themeRangeContainer);
			Layers layers = getMapControl().getMap().getLayers();
			for (int i = 0; i < layers.getCount(); i++) {
				Layer tempLayer = layers.get(i);
				container.addItemToComboBox(tempLayer);
			}
			container.getComboBoxThemeLayer().setSelectedItem(themeRangeContainer.getThemeRangeLayer().getName());
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
		themeLabel.setLabelExpression("SmID");
		themeLabel.setMaxLabelLength(8);
		ThemeLabelUniformContainer themeLabelUniformContainer = new ThemeLabelUniformContainer((DatasetVector) getDataset(), themeLabel);
		ThemeMainContainer container = addPanelToThemeMainContainer(themeLabelUniformContainer);
		Layers layers = getMapControl().getMap().getLayers();
		for (int i = 0; i < layers.getCount(); i++) {
			Layer tempLayer = layers.get(i);
			container.addItemToComboBox(tempLayer);
		}
		container.getComboBoxThemeLayer().setSelectedItem(themeLabelUniformContainer.getThemeLabelLayer().getName());
		getDockbarThemeContainer().setVisible(true);
	}

	/**
	 * 新建分段风格标签专题图
	 */
	public static void buildLabelRangeTheme() {
		ThemeLabel themeLabel = ThemeLabel.makeDefault((DatasetVector) getDataset(), "SmID", RangeMode.EQUALINTERVAL, 5, ColorGradientType.GREENRED);
		if (null != themeLabel) {
			themeLabel.setMaxLabelLength(8);
			ThemeLabelRangeContainer themeLabelRangeContainer = new ThemeLabelRangeContainer((DatasetVector) getDataset(), themeLabel);
			ThemeMainContainer container = addPanelToThemeMainContainer(themeLabelRangeContainer);
			Layers layers = getMapControl().getMap().getLayers();
			for (int i = 0; i < layers.getCount(); i++) {
				Layer tempLayer = layers.get(i);
				container.addItemToComboBox(tempLayer);
			}
			container.getComboBoxThemeLayer().setSelectedItem(themeLabelRangeContainer.getThemeLabelLayer().getName());
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
		try {
			ThemeGridUnique themeUnique = ThemeGridUnique.makeDefault(datasetGrid, ColorGradientType.GREENORANGEVIOLET);
			if (null != themeUnique) {
				ThemeGridUniqueContainer themeUniqueContainer = new ThemeGridUniqueContainer((DatasetGrid) getDataset(), themeUnique);
				ThemeMainContainer container = addPanelToThemeMainContainer(themeUniqueContainer);
				Layers layers = getMapControl().getMap().getLayers();
				for (int i = 0; i < layers.getCount(); i++) {
					Layer tempLayer = layers.get(i);
					container.addItemToComboBox(tempLayer);
				}
				container.getComboBoxThemeLayer().setSelectedItem(themeUniqueContainer.getThemeUniqueLayer().getName());
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
				ThemeMainContainer container = addPanelToThemeMainContainer(themeGridRangeContainer);
				Layers layers = getMapControl().getMap().getLayers();
				for (int i = 0; i < layers.getCount(); i++) {
					Layer tempLayer = layers.get(i);
					container.addItemToComboBox(tempLayer);
				}
				container.getComboBoxThemeLayer().setSelectedItem(themeGridRangeContainer.getThemeRangeLayer().getName());
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
	 * 修改单值专题图
	 * 
	 * @param layer
	 */
	public static void resetUniqueTheme(Layer layer) {
		if (null != layer.getDataset()) {
			ThemeUniqueContainer themeUniqueContainer = new ThemeUniqueContainer(layer);
			addPanelToThemeMainContainer(themeUniqueContainer);
		}
	}

	/**
	 * 刷新地图和图层
	 */
	public static void refreshMapAndLayer(Map map, String layerName, boolean isExpandPath) {
		map.refresh();
		LayersTree tree = UICommonToolkit.getLayersManager().getLayersTree();
		tree.reload();
		TreePath layerPath = getSelectionPath(tree, layerName);
		tree.setSelectionPath(layerPath);
		if (isExpandPath) {
			tree.expandPath(layerPath);
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
	 * 修改分段专题图
	 * 
	 * @param layer
	 */
	public static void resetRangeTheme(Layer layer) {
		if (null != layer.getDataset()) {
			ThemeRangeContainer themeRangeContainer = new ThemeRangeContainer(layer);
			addPanelToThemeMainContainer(themeRangeContainer);
		}
	}

	/**
	 * 修改标签统一风格专题图
	 * 
	 * @param layer
	 */
	public static void resetLabelUniform(Layer layer) {
		if (null != layer.getDataset()) {
			ThemeLabelUniformContainer themeLabelUniformContainer = new ThemeLabelUniformContainer(layer);
			addPanelToThemeMainContainer(themeLabelUniformContainer);
		}
	}

	/**
	 * 修改标签分段风格专题图
	 * 
	 * @param layer
	 */
	public static void resetLabelRange(Layer layer) {
		if (null != layer.getDataset()) {
			ThemeLabelRangeContainer themeLabelRangeContainer = new ThemeLabelRangeContainer(layer);
			addPanelToThemeMainContainer(themeLabelRangeContainer);
		}
	}

	/**
	 * 修改栅格单值专题图
	 * 
	 * @param layer
	 */
	public static void resetGridUnique(Layer layer) {
		if (null != layer.getDataset()) {
			ThemeGridUniqueContainer themeGridUniqueContainer = new ThemeGridUniqueContainer(layer);
			addPanelToThemeMainContainer(themeGridUniqueContainer);
		}
	}

	/**
	 * 修改栅格分段专题图
	 * 
	 * @return
	 */
	public static void resetGridRange(Layer layer) {
		if (null != layer.getDataset()) {
			ThemeGridRangeContainer themeGridUniqueContainer = new ThemeGridRangeContainer(layer);
			addPanelToThemeMainContainer(themeGridUniqueContainer);
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
