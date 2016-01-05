package com.supermap.desktop.newtheme;

import com.supermap.desktop.Application;
import com.supermap.desktop.event.ActiveFormChangedEvent;
import com.supermap.desktop.event.ActiveFormChangedListener;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.LayersTree;
import com.supermap.desktop.ui.controls.TreeNodeData;
import com.supermap.desktop.utilties.MapUtilties;
import com.supermap.mapping.Layer;
import com.supermap.mapping.LayerGroup;
import com.supermap.mapping.Layers;
import com.supermap.mapping.Map;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.*;

public class ThemeMainContainer extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel labelThemeLayer = new JLabel();
	private JComboBox<String> comboBoxThemeLayer = new JComboBox<String>();
	private JPanel panelThemeInfo = new JPanel();
	private JCheckBox checkBoxRefreshAtOnce = new JCheckBox();
	private JButton buttonApply = new JButton();

	private Map map;

	private LayersTree layersTree = UICommonToolkit.getLayersManager().getLayersTree();
	private LocalItemListener itemListener = new LocalItemListener();
	private LocalTreeMouseListener localMouseListener = new LocalTreeMouseListener();
	private LocalActiveFormChangedListener activeFormChangedListener = new LocalActiveFormChangedListener();
	private LocalTreeSelectListener treeSelectListener = new LocalTreeSelectListener();
	
	private Layer newLayer;
	private Layer oldLayer;
	private boolean isThemeChange;
	
	public ThemeMainContainer() {
		initComponents();
		initResources();
		registActionListener();
	}

	/**
	 * 资源化
	 */
	private void initResources() {
		this.labelThemeLayer.setText(MapViewProperties.getString("String_Themelayers"));
		this.checkBoxRefreshAtOnce.setText(MapViewProperties.getString("String_RefreshAtOnce"));
		this.buttonApply.setText(MapViewProperties.getString("String_Button_Apply"));
	}

	/**
	 * 界面布局入口
	 */
	private void initComponents() {
		initContentPanel();
	}

	/**
	 * 界面布局
	 */
	private void initContentPanel() {
		GridBagLayout layout = new GridBagLayout();
		this.setLayout(layout);
		this.comboBoxThemeLayer.setEditable(true);
		this.setLayout(new GridBagLayout());
		// @formatter:off
		this.add(labelThemeLayer,       new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(20, 0).setInsets(10, 10, 5, 10).setAnchor(GridBagConstraints.WEST));
		this.add(comboBoxThemeLayer,    new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(80, 0).setInsets(10, 10, 5, 10).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL));
		this.add(panelThemeInfo,        new GridBagConstraintsHelper(0, 1, 2, 1).setWeight(100, 75).setInsets(5).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH));
		this.add(checkBoxRefreshAtOnce, new GridBagConstraintsHelper(0, 2, 1, 1).setWeight(0, 0).setInsets(0,10,5,10).setAnchor(GridBagConstraints.WEST));
		this.add(buttonApply,           new GridBagConstraintsHelper(1, 2, 1, 1).setWeight(100, 0).setInsets(0,10,5,10).setAnchor(GridBagConstraints.EAST));
		// @formatter:on
	}

	/**
	 * 得到中间界面
	 *
	 * @return
	 */
	public JPanel getPanelThemeInfo() {
		return panelThemeInfo;
	}

	public void setPanelThemeInfo(JPanel panelThemeInfo) {
		this.panelThemeInfo = panelThemeInfo;
	}

	/**
	 * 注册事件
	 */
	private void registActionListener() {
		this.comboBoxThemeLayer.addItemListener(this.itemListener);
		this.layersTree.addMouseListener(this.localMouseListener);
		this.layersTree.getSelectionModel().addTreeSelectionListener(this.treeSelectListener);
		Application.getActiveApplication().getMainFrame().getFormManager().addActiveFormChangedListener(this.activeFormChangedListener);

	}

	/**
	 * 注销事件
	 */
	public void unregistActionListener() {
		this.comboBoxThemeLayer.removeItemListener(this.itemListener);
		this.layersTree.removeMouseListener(this.localMouseListener);
		this.layersTree.getSelectionModel().removeTreeSelectionListener(this.treeSelectListener);
		Application.getActiveApplication().getMainFrame().getFormManager().removeActiveFormChangedListener(this.activeFormChangedListener);
	}

	class LocalActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == checkBoxRefreshAtOnce) {
				
			} else {
				refreshMap();
			}
		}

		/**
		 * 刷新地图
		 */
		private void refreshMap() {
			map = ThemeGuideFactory.getMapControl().getMap();
			map.refresh();
		}
	}

	public JComboBox<String> getComboBoxThemeLayer() {
		return comboBoxThemeLayer;
	}

	public void setComboBoxThemeLayer(JComboBox<String> comboBoxThemeLayer) {
		this.comboBoxThemeLayer = comboBoxThemeLayer;
	}

	/**
	 * 刷新专题图主界面
	 *
	 * @param layer
	 */
	private void refreshThemeMainContainer(Layer layer) {
		if (null != layer) {
			if (null != layer.getTheme()) {
				ThemeGuideFactory.modifyTheme(layer);
			} else {
				updateThemeMainContainer();
			}
			ThemeMainContainer.this.updateUI();
			if (null != ThemeGuideFactory.getMapControl()) {
				ThemeGuideFactory.getMapControl().getMap().refresh();
			}
		}
	}

	class LocalItemListener implements ItemListener {
		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				String layerCaption = (String) comboBoxThemeLayer.getSelectedItem();
				if (null != ThemeGuideFactory.getMapControl()) {
					map = ThemeGuideFactory.getMapControl().getMap();
					Layer layer = MapUtilties.findLayerByCaption(map, layerCaption);
					refreshThemeMainContainer(layer);
				}
			}
		}
	}

	/**
	 * 移除所有的控件
	 */
	private void updateThemeMainContainer() {
		for (int i = ThemeMainContainer.this.getComponentCount() - 1; i >= 0; i--) {
			if (ThemeMainContainer.this.getComponent(i) instanceof ThemeChangePanel) {
				ThemeChangePanel themeChangePanel = (ThemeChangePanel) ThemeMainContainer.this.getComponent(i);
				themeChangePanel.unregistActionListener();
				ThemeMainContainer.this.remove(i);
			}
		}
		ThemeMainContainer.this.add(panelThemeInfo, new GridBagConstraintsHelper(0, 1, 2, 1).setWeight(3, 3).setInsets(5).setAnchor(GridBagConstraints.CENTER)
				.setIpad(0, 0).setFill(GridBagConstraints.BOTH));
		ThemeMainContainer.this.repaint();
		ThemeMainContainer.this.comboBoxThemeLayer.removeAllItems();
	}

	/**
	 * 刷新标签项下拉框中显示的子项
	 */
	private void refreshComboBoxThemeLayer() {
		comboBoxThemeLayer.removeAllItems();
		map = ThemeGuideFactory.getMapControl().getMap();
		Layers layers = map.getLayers();
		for (int i = 0; i < layers.getCount(); i++) {
			addItemToComboBox(layers.get(i));
		}
	}

	/**
	 * 利用递归将当前地图的专题图图层标题添加到combobox中
	 *
	 * @param layer
	 */
	public void addItemToComboBox(Layer layer) {
		if (layer instanceof LayerGroup) {
			LayerGroup tempLayer = (LayerGroup) layer;
			for (int i = 0; i < tempLayer.getCount(); i++) {
				addItemToComboBox(tempLayer.get(i));
			}
		} else if (null != layer.getTheme()) {
			comboBoxThemeLayer.addItem(layer.getCaption());
		}
	}

	private void resetThemeMainContainer(Layer layer) {
		if (null != layer) {
			refreshComboBoxThemeLayer();
			if (comboBoxThemeLayer.getItemCount() > 0 && null != layer.getTheme()) {
				// 有专题图图层，comboBoxThemeLayer中有对应的专题图图层，刷新ThemeMainContainer
				comboBoxThemeLayer.setSelectedItem(layer.getCaption());
			} else {
				// 没有专题图图层，重置ThemeMainContainer
				comboBoxThemeLayer.removeAllItems();
				comboBoxThemeLayer.setSelectedItem("");
				updateThemeMainContainer();
				ThemeMainContainer.this.updateUI();
			}
		} else {
			updateThemeMainContainer();
		}
	}

	class LocalTreeMouseListener extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {
			if (1 == e.getClickCount()) {
				isThemeChange = false;
			}
			if (2 == e.getClickCount() && null != newLayer && null != newLayer.getTheme()) {
				resetThemeMainContainer(newLayer);
				ThemeGuideFactory.getDockbarThemeContainer().setVisible(true);
			}
		}
	}

	class LocalActiveFormChangedListener implements ActiveFormChangedListener {

		@Override
		public void activeFormChanged(ActiveFormChangedEvent e) {
			if (null == newLayer) {
				layersTree.setSelectionRow(0);
			}
			resetThemeMainContainer(newLayer);
		}
	}

	class LocalTreeSelectListener implements TreeSelectionListener {
		@Override
		public void valueChanged(TreeSelectionEvent e) {
			newLayer = getLayerByPath(e.getNewLeadSelectionPath());
			oldLayer = getLayerByPath(e.getOldLeadSelectionPath());
			boolean isResetThemeMain = false;
			if (null != ThemeGuideFactory.getMapControl()) {
				map = ThemeGuideFactory.getMapControl().getMap();
			}
			// 当地图中不存在图层时刷新专题图
			if (null == map.getLayers() || 0 == map.getLayers().getCount()) {
				updateThemeMainContainer();
			}
			if (isThemeChange) {
				return;
			}
			if (null == oldLayer || (null != newLayer && null != oldLayer && !newLayer.equals(oldLayer))) {
				isResetThemeMain = true;
			}
			if (isResetThemeMain) {
				resetThemeMainContainer(newLayer);
			}
		}
	}

	/**
	 * 根据选中的treePath获取图层
	 *
	 * @param path
	 * @return
	 */
	private Layer getLayerByPath(TreePath path) {
		Layer layer = null;
		if (null != path && null != path.getLastPathComponent()) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
			Object obj = node.getUserObject();
			TreeNodeData controlNodeData = (TreeNodeData) obj;
			Object itemObj = controlNodeData.getData();
			if (itemObj instanceof Layer) {
				layer = (Layer) itemObj;
			} else {
				DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) path.getParentPath().getLastPathComponent();
				Object parentUserObj = parentNode.getUserObject();
				TreeNodeData nodeData = (TreeNodeData) parentUserObj;
				Object parentObj = nodeData.getData();
				if (parentObj instanceof Layer) {
					layer = (Layer) parentObj;
				}
			}
		}
		return layer;
	}

	// 判断专题图属性是否改变的字段
	public boolean isThemeChange() {
		return isThemeChange;
	}

	// 设置专题图属性是否改变
	public void setThemeChange(boolean isThemeChange) {
		this.isThemeChange = isThemeChange;
	}

}