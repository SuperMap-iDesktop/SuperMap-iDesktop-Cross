package com.supermap.desktop.newtheme;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IFormMap;
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

public class ThemeMainContainer extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel labelThemeLayer = new JLabel();
	private JComboBox<String> comboBoxThemeLayer = new JComboBox<String>();
	private JPanel panelThemeInfo = new JPanel();
	private Map map;
	private LayersTree layersTree = UICommonToolkit.getLayersManager().getLayersTree();
	private LocalActiveFormChangedListener activeFormChangedListener = new LocalActiveFormChangedListener();
	private LocalItemListener itemListener = new LocalItemListener();
	private LocalTreeMouseListener localMouseListener = new LocalTreeMouseListener();
	private LocalTreeKeyListener localTreeKeyListener = new LocalTreeKeyListener();

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
		this.add(labelThemeLayer,
				new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(3, 0).setInsets(5, 20, 5, 20).setAnchor(GridBagConstraints.WEST).setIpad(30, 0));
		this.add(comboBoxThemeLayer, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(3, 0).setInsets(5).setAnchor(GridBagConstraints.CENTER).setIpad(30, 0)
				.setFill(GridBagConstraints.HORIZONTAL));
		this.add(panelThemeInfo, new GridBagConstraintsHelper(0, 1, 2, 1).setWeight(3, 3).setInsets(5).setAnchor(GridBagConstraints.CENTER).setIpad(0, 0)
				.setFill(GridBagConstraints.BOTH));
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
		// 地图删除时移除掉
		Application.getActiveApplication().getMainFrame().getFormManager().addActiveFormChangedListener(this.activeFormChangedListener);
		this.layersTree.addMouseListener(this.localMouseListener);
		this.layersTree.addKeyListener(this.localTreeKeyListener);
		this.layersTree.addPropertyChangeListener("LayerChange", new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				resetThemeMainContainer(getSelectLayer());
			}
		});
	}

	/**
	 * 注销事件
	 */
	public void unregistActionListener() {
		// do nothing
	}

	class LocalActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// if (e.getSource() == checkBoxRefreshAtOnce) {
			// isRefreshAtOnce = checkBoxRefreshAtOnce.isSelected();
			// if (isRefreshAtOnce) {
			// refreshMap();
			// }
			// } else {
			// refreshMap();
			// }
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
			if (ThemeMainContainer.this.getComponent(i) instanceof JPanel) {
				ThemeMainContainer.this.remove(i);
			}
		}
		ThemeMainContainer.this.add(panelThemeInfo, new GridBagConstraintsHelper(0, 1, 2, 1).setWeight(3, 3).setInsets(5).setAnchor(GridBagConstraints.CENTER)
				.setIpad(0, 0).setFill(GridBagConstraints.BOTH));
	}

	class LocalActiveFormChangedListener implements ActiveFormChangedListener {

		@Override
		public void activeFormChanged(ActiveFormChangedEvent e) {
			if (!hasTheme()) {
				updateThemeMainContainer();
				ThemeMainContainer.this.updateUI();
				ThemeMainContainer.this.comboBoxThemeLayer.removeAllItems();
				unregistActionListener();
			}else if(null!=getSelectLayer()){
				resetThemeMainContainer(getSelectLayer());
				
			}
		}

		private boolean hasTheme() {
			boolean hasTheme = false;
				if (null!=ThemeGuideFactory.getMapControl()) {
					map = ThemeGuideFactory.getMapControl().getMap();
					for (int i = 0; i < map.getLayers().getCount(); i++) {
						if (null != map.getLayers().get(i).getTheme()) {
							hasTheme = true;
						}
					}
				}
			return hasTheme;
		}
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
		}
	}

	public Layer getSelectLayer() {
		Layer layer = null;
		if (null != layersTree.getLastSelectedPathComponent()) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) layersTree.getLastSelectedPathComponent();
			Object obj = node.getUserObject();
			TreeNodeData controlNodeData = (TreeNodeData) obj;
			Object itemObj = controlNodeData.getData();
			if (itemObj instanceof Layer) {
				layer = (Layer) itemObj;
			}
		}
		return layer;
	}

	class LocalTreeMouseListener extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {

			if (1 == e.getClickCount()) {
				resetThemeMainContainer(getSelectLayer());
			} else if (2 == e.getClickCount() && null != getSelectLayer() && null != getSelectLayer().getTheme()) {
				resetThemeMainContainer(getSelectLayer());
				ThemeGuideFactory.getDockbarThemeContainer().setVisible(true);
			}
		}
	}

	class LocalTreeKeyListener extends KeyAdapter {

		@Override
		public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {
				resetThemeMainContainer(getSelectLayer());
			}
		}

	}
}