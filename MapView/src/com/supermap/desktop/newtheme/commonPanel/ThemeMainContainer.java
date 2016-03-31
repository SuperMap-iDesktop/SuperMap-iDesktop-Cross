package com.supermap.desktop.newtheme.commonPanel;

import com.supermap.desktop.Application;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.newtheme.commonUtils.ThemeGuideFactory;
import com.supermap.desktop.newtheme.themeLabel.ThemeLabelRangeContainer;
import com.supermap.desktop.newtheme.themeLabel.ThemeLabelUniformContainer;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.LayersTree;
import com.supermap.desktop.ui.controls.TreeNodeData;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.mapping.Layer;
import com.supermap.mapping.Map;
import com.supermap.mapping.Theme;
import com.supermap.mapping.ThemeLabel;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

//import com.supermap.desktop.utilties.MapUtilties;

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

	private Layer newLayer;
	// 标记位，用于标记当前
	private boolean layerPropertyChanged = false;
	public Layer oldLayer;
	public ThemeChangePanel lastChangePanel;
	public Theme lastTheme;

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
		this.layersTree.addMouseListener(this.localMouseListener);
		this.layersTree.getSelectionModel().addTreeSelectionListener(this.treeSelectListener);
		this.buttonApply.addActionListener(this.actionListener);
		this.checkBoxRefreshAtOnce.addActionListener(this.refreshAtOnceListener);
		this.layerRemoveListener = new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				setLayerPropertyChanged(false);
			}
		};
		this.layersTree.addPropertyChangeListener("LayerRemoved", layerRemoveListener);
		this.layersTree.addPropertyChangeListener("LayerChange", new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) layersTree.getLastSelectedPathComponent();
				Object obj = node.getUserObject();
				TreeNodeData controlNodeData = (TreeNodeData) obj;
				Object itemObj = controlNodeData.getData();
				if (itemObj instanceof Layer) {
					textFieldThemeLayer.setText(((Layer) itemObj).getCaption());
				}
			}
		});
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

	class LocalTreeMouseListener extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {
			if (2 == e.getClickCount() && null != newLayer && null != newLayer.getTheme()) {
				ThemeGuideFactory.getDockbarThemeContainer().setVisible(true);
			}
		}

	}

	class LocalTreeSelectListener implements TreeSelectionListener {
		@Override
		public void valueChanged(TreeSelectionEvent e) {
			try {
				newLayer = getLayerByPath(e.getNewLeadSelectionPath());
				// isSetCombobox = false;
				if (null != e.getOldLeadSelectionPath()) {
					updateLayerProperty(e.getOldLeadSelectionPath());
				}
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

	public void updateLayerProperty(final TreePath path) {
		LayersTree tree = UICommonToolkit.getLayersManager().getLayersTree();
		if (tree.getRowForPath(path) < 0) {
			// 树的当前节点已经被删除，修改layerPropertyChanged
			setLayerPropertyChanged(false);
		}
		oldLayer = getLayerByPath(path);
		// 新线程解决关闭数据集是lay对象释放问题
		if (null == oldLayer || oldLayer.isDisposed()) {
			setLayerPropertyChanged(false);
		}
		if (null != panel && null != oldLayer && !oldLayer.isDisposed() && !checkBoxRefreshAtOnce.isSelected() && isLayerPropertyChanged()) {
			if (JOptionPane.OK_OPTION != UICommonToolkit.showConfirmDialog(MapViewProperties.getString("String_ThemeProperty_Message"))) {
				// 不保存修改
				ThemeChangePanel panel = ThemeGuideFactory.themeTypeContainer.get(oldLayer.getCaption());
				if (null != panel) {
					panel.unregistActionListener();
					panel = null;
					ThemeGuideFactory.themeTypeContainer.remove(oldLayer.getCaption());
				}
				setLayerPropertyChanged(false);
			} else {
				// 保存修改并刷新
				panel = ThemeGuideFactory.themeTypeContainer.get(oldLayer.getCaption());
				if (panel instanceof ThemeLabelUniformContainer) {
					panel.refreshMapAndLayer();
				} else if (panel instanceof ThemeLabelRangeContainer) {
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
				} else {
					oldLayer.getTheme().fromXML(panel.getCurrentTheme().toXML());
				}
				ThemeGuideFactory.getMapControl().getMap().refresh();
				TreePath treePath = layersTree.getSelectionPath();
				int row = layersTree.getRowForPath(treePath);
				setLayerPropertyChanged(false);
				layersTree.reload();
				layersTree.setSelectionRow(row);
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

}