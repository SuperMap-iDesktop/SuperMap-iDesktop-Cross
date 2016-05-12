package com.supermap.desktop.newtheme.commonPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import com.supermap.desktop.Application;
import com.supermap.desktop.FormMap;
import com.supermap.desktop.Interface.IDockbar;
import com.supermap.desktop.Interface.IFormManager;
import com.supermap.desktop.event.ActiveFormChangedEvent;
import com.supermap.desktop.event.ActiveFormChangedListener;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.newtheme.commonUtils.ThemeGuideFactory;
import com.supermap.desktop.newtheme.themeLabel.ThemeLabelComplicatedContainer;
import com.supermap.desktop.newtheme.themeLabel.ThemeLabelRangeContainer;
import com.supermap.desktop.newtheme.themeLabel.ThemeLabelUniformContainer;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.LayersTree;
import com.supermap.desktop.ui.controls.TreeNodeData;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.mapping.Layer;
import com.supermap.mapping.Map;
import com.supermap.mapping.ThemeLabel;

/**
 * 屏蔽掉专题图下拉显示项
 *
 * @author Administrator
 */
public class ThemeMainContainer extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel labelThemeLayer = new JLabel();
	private JTextField textFieldThemeLayer = new JTextField();
	private JScrollPane scrollPane = new JScrollPane();
	private JPanel panelThemeInfo = new JPanel();
	private JCheckBox checkBoxRefreshAtOnce = new JCheckBox();
	private JButton buttonApply = new SmButton();
	private ThemeChangePanel panel;

	private Map map;

	private LayersTree layersTree = UICommonToolkit.getLayersManager().getLayersTree();
	private LocalTreeMouseListener localMouseListener = new LocalTreeMouseListener();
	private LocalTreeSelectListener treeSelectListener = new LocalTreeSelectListener();
	private LocalActionListener actionListener = new LocalActionListener();
	private ActionListener refreshAtOnceListener = new RefreshAtOnceListener();
	private PropertyChangeListener layerRemoveListener;
	private IFormManager formManager = Application.getActiveApplication().getMainFrame().getFormManager();

	private Layer newLayer;
	// 标记位，用于标记当前
	private boolean layerPropertyChanged = false;
	public Layer oldLayer;
	private ActiveFormChangedListener activeFormChangedListener;
	private PropertyChangeListener layerChangeListener;

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
		GridBagLayout layout = new GridBagLayout();
		this.setLayout(layout);
		this.textFieldThemeLayer.setEditable(false);
		this.textFieldThemeLayer.setBackground(Color.white);
		this.buttonApply.setEnabled(false);
		this.setLayout(new GridBagLayout());
		this.scrollPane.setBorder(null);
		// @formatter:off
		this.checkBoxRefreshAtOnce.setSelected(true);
		this.add(this.labelThemeLayer,       new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(10, 0).setInsets(10, 10, 5, 10).setAnchor(GridBagConstraints.WEST));
		this.add(this.textFieldThemeLayer,   new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(90, 0).setInsets(10, 10, 5, 10).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL));
		this.add(this.scrollPane,            new GridBagConstraintsHelper(0, 1, 2, 1).setWeight(100, 75).setInsets(5).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH));
		this.add(this.checkBoxRefreshAtOnce, new GridBagConstraintsHelper(0, 2, 1, 1).setWeight(0, 0).setInsets(0,10,5,10).setAnchor(GridBagConstraints.WEST));
		this.add(this.buttonApply,           new GridBagConstraintsHelper(1, 2, 1, 1).setWeight(0, 0).setInsets(0,10,5,10).setAnchor(GridBagConstraints.EAST));
		this.scrollPane.setViewportView(this.panelThemeInfo);
		// @formatter:on
	}

	/**
	 * 注册事件
	 */
	private void registActionListener() {
		this.layerRemoveListener = new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				// 删除图层时销毁已有地图
				if (null != panel && !panel.getCurrentLayer().isDisposed() && panel.getCurrentLayer().getName().equals(evt.getNewValue())) {
					panel.getCurrentTheme().dispose();
					panel.unregistActionListener();
					setLayerPropertyChanged(false);
				}
			}
		};
		this.layerChangeListener = new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) layersTree.getLastSelectedPathComponent();
				if (null != node) {
					Object obj = node.getUserObject();
					TreeNodeData controlNodeData = (TreeNodeData) obj;
					Object itemObj = controlNodeData.getData();
					if (itemObj instanceof Layer) {
						textFieldThemeLayer.setText(((Layer) itemObj).getCaption());
					}
				}
			}
		};
		this.activeFormChangedListener = new ActiveFormChangedListener() {

			@Override
			public void activeFormChanged(ActiveFormChangedEvent e) {
				if (null != e.getOldActiveForm() && e.getOldActiveForm() instanceof FormMap && null != ((FormMap) e.getOldActiveForm()).getMapControl()
						&& ((FormMap) e.getOldActiveForm()).getActiveLayers().length > 0) {
					Layer tempLayer = ((FormMap) e.getOldActiveForm()).getActiveLayers()[0];
					if (null != tempLayer && !tempLayer.isDisposed()) {
						updateProperty(tempLayer);
					}
				}
			}
		};
		unregistActionListener();
		this.layersTree.addMouseListener(this.localMouseListener);
		this.layersTree.getSelectionModel().addTreeSelectionListener(this.treeSelectListener);
		this.buttonApply.addActionListener(this.actionListener);
		this.checkBoxRefreshAtOnce.addActionListener(this.refreshAtOnceListener);
		this.layersTree.addPropertyChangeListener("LayerRemoved", layerRemoveListener);
		this.layersTree.addPropertyChangeListener("LayerChange", layerChangeListener);
		this.formManager.addActiveFormChangedListener(this.activeFormChangedListener);
	}

	/**
	 * 注销事件
	 */
	public void unregistActionListener() {
		this.layersTree.removeMouseListener(this.localMouseListener);
		this.layersTree.getSelectionModel().removeTreeSelectionListener(this.treeSelectListener);
		this.buttonApply.removeActionListener(this.actionListener);
		this.checkBoxRefreshAtOnce.removeActionListener(this.refreshAtOnceListener);
		this.layersTree.removePropertyChangeListener("LayerRemoved", layerRemoveListener);
		this.formManager.removeActiveFormChangedListener(this.activeFormChangedListener);
	}

	/**
	 * 移除所有的控件
	 */
	private void updateThemeMainContainer() {
		if (null != this.panel) {
			ThemeMainContainer.this.remove(this.panel);
		}
		scrollPane.setViewportView(panelThemeInfo);
		ThemeMainContainer.this.repaint();
	}

	public void updateLayerProperty(final TreePath path) {
		LayersTree tree = UICommonToolkit.getLayersManager().getLayersTree();
		if (null != path && tree.getRowForPath(path) < 0) {
			// 树的当前节点已经被删除，修改layerPropertyChanged
			setLayerPropertyChanged(false);
		}
		if (null != oldLayer && !oldLayer.isDisposed()) {
			updateProperty(oldLayer);
		}
	}

	private void updateProperty(Layer oldLayer) {
		if (null != oldLayer.getTheme() && !checkBoxRefreshAtOnce.isSelected() && isLayerPropertyChanged()) {
			if (JOptionPane.OK_OPTION != UICommonToolkit.showConfirmDialog(MapViewProperties.getString("String_ThemeProperty_Message"))) {
				// 不保存修改
				panel.unregistActionListener();
				ThemeGuideFactory.themeTypeContainer.remove(ThemeGuideFactory.getThemeTypeString(oldLayer));
				setLayerPropertyChanged(false);
			} else {
				// 保存修改并刷新
				boolean isThemeLabelUniform = false;
				boolean isThemeLabelRange = false;
				boolean isThemeLabelComplicated = false;
				if (panel instanceof ThemeLabelUniformContainer) {
					panel.refreshMapAndLayer();
					isThemeLabelUniform = true;
				}
				if (panel instanceof ThemeLabelRangeContainer) {
					((ThemeLabelRangeContainer) panel).getPanelAdvance().refreshMapAndLayer();
					((ThemeLabelRangeContainer) panel).getPanelProperty().refreshMapAndLayer();
					ThemeLabel themeLabel = (ThemeLabel) panel.getCurrentTheme();
					ThemeLabel nowThemeLabel = ((ThemeLabel) oldLayer.getTheme());
					nowThemeLabel.clear();
					if (0 < themeLabel.getCount()) {
						for (int i = 0; i < themeLabel.getCount(); i++) {
							if (null != themeLabel.getItem(i)) {
								nowThemeLabel.addToTail(themeLabel.getItem(i), true);
							}
						}
					}
					nowThemeLabel.setRangeExpression(themeLabel.getRangeExpression());
					isThemeLabelRange = true;
				}
				if (panel instanceof ThemeLabelComplicatedContainer) {
					((ThemeLabelComplicatedContainer) panel).getPanelAdvance().refreshMapAndLayer();
					((ThemeLabelComplicatedContainer) panel).getPanelProperty().refreshMapAndLayer();
					ThemeLabel themeLabel = (ThemeLabel) panel.getCurrentTheme();
					ThemeLabel nowThemeLabel = ((ThemeLabel) oldLayer.getTheme());
					nowThemeLabel.setUniformMixedStyle(themeLabel.getUniformMixedStyle());
					isThemeLabelComplicated = true;
				}
				if (!isThemeLabelComplicated && !isThemeLabelRange && !isThemeLabelUniform) {
					oldLayer.getTheme().fromXML(panel.getCurrentTheme().toXML());
				}
				TreePath treePath = layersTree.getSelectionPath();
				int row = layersTree.getRowForPath(treePath);
				setLayerPropertyChanged(false);
				layersTree.reload();
				layersTree.setSelectionRow(row);
				buttonApply.setEnabled(false);
				ThemeGuideFactory.getMapControl().getMap().refresh();
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

	public ThemeChangePanel getPanel() {
		return panel;
	}

	public void setPanel(ThemeChangePanel panel) {
		if (null != panelThemeInfo) {
			remove(panelThemeInfo);
		}
		for (int i = getComponents().length - 1; i >= 0; i--) {
			if (getComponent(i) instanceof JPanel) {
				remove(getComponent(i));
			}
		}
		panel.setPreferredSize(new Dimension(400, 840));
		this.scrollPane.setViewportView(panel);
		repaint();
		this.panel = panel;
		if (null != panel) {
			this.panel.setRefreshAtOnce(this.checkBoxRefreshAtOnce.isSelected());
			this.buttonApply.setEnabled(false);
		}
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

	public Map getMap() {
		return map;
	}

	public boolean isLayerPropertyChanged() {
		return layerPropertyChanged;
	}

	public void setLayerPropertyChanged(boolean layerPropertyChanged) {
		this.layerPropertyChanged = layerPropertyChanged;
	}

	public JCheckBox getCheckBoxRefreshAtOnce() {
		return checkBoxRefreshAtOnce;
	}

	public JButton getButtonApply() {
		return buttonApply;
	}

	public JScrollPane getScrollPane() {
		return scrollPane;
	}

	public void setScrollPane(JScrollPane scrollPane) {
		this.scrollPane = scrollPane;
	}

	class LocalTreeMouseListener extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {
			if (2 == e.getClickCount() && null != newLayer && null != newLayer.getTheme()) {
				ThemeGuideFactory.modifyTheme(newLayer);
				ThemeGuideFactory.getDockbarThemeContainer().setVisible(true);
			}
		}

	}

	class LocalTreeSelectListener implements TreeSelectionListener {
		@Override
		public void valueChanged(TreeSelectionEvent e) {
			try {
				IDockbar dockbarThemeContainer = ThemeGuideFactory.getDockbarThemeContainer();
				// 专题图dockbar不存在时不做处理
				if (null == dockbarThemeContainer.getComponent()) {
					return;
				}
				oldLayer = getLayerByPath(e.getOldLeadSelectionPath());
				if (null != panel && null != e.getNewLeadSelectionPath()) {
					updateLayerProperty(e.getOldLeadSelectionPath());
				}
				newLayer = getLayerByPath(e.getNewLeadSelectionPath());
				if (null != newLayer && null != newLayer.getTheme()) {
					textFieldThemeLayer.setText(newLayer.getCaption());
					ThemeGuideFactory.modifyTheme(newLayer);
				} else {
					textFieldThemeLayer.setText("");
					updateThemeMainContainer();
				}
			} catch (Exception ex) {
				Application.getActiveApplication().getOutput().output(ex);
			}

		}
	}

	class LocalActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (null != panel) {
				panel.refreshMapAndLayer();
				buttonApply.setEnabled(false);
				setLayerPropertyChanged(false);
			}
		}
	}

	class RefreshAtOnceListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			boolean selected = checkBoxRefreshAtOnce.isSelected();
			if (null != panel && selected) {
				panel.setRefreshAtOnce(selected);
				panel.refreshMapAndLayer();
				buttonApply.setEnabled(false);
			} else if (null != panel && false == selected) {
				panel.setRefreshAtOnce(selected);
			}
		}
	}
}