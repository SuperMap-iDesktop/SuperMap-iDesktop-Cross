package com.supermap.desktop.controls.colorScheme;

import com.supermap.data.Colors;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.utilities.JLabelUIUtilities;
import com.supermap.desktop.controls.utilities.JTreeUIUtilities;
import com.supermap.desktop.dialog.ColorSchemeDialogs.ColorSchemeTreeNode;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.TextFieldSearch;
import com.supermap.desktop.ui.controls.TreeComboBox;
import com.supermap.desktop.utilities.LogUtilities;
import com.supermap.desktop.utilities.StringUtilities;
import com.supermap.mapping.ThemeType;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.metal.MetalComboBoxIcon;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * 颜色下拉选择器
 *
 * @author xuzw
 */
public class ColorsComboBox extends JComponent implements ItemSelectable {
	private static final long serialVersionUID = 1L;
	private Object themeType;

	private JPanel panelShow;// 用来存放label
	private JButton buttonPopup;

	private ColorScheme colorSchemeSelected;
	// 主面板
	private JPopupMenu popupMenuColorScheme;
	// 搜索
	private TextFieldSearch textFieldSearch;
	// 显示当前颜色
	private JList<ColorScheme> listColors;


	private ColorsCellRenderer colorsCellRenderer;

	private Colors currentColors = new Colors();

	private ColorSchemeTreeNode selectedColorSchemeTreeNode;

	private ArrayList<ItemListener> itemListeners;
	private boolean isUserDefineFireChange = false;
	private final ColorSchemeManagerChangedListener colorSchemeManagerChangedListener = new ColorSchemeManagerChangedListener() {
		@Override
		public void colorSchemeManagerChanged(ColorSchemeManagerChangedEvent colorSchemeManagerChangedEvent) {
			if (isUserDefineFireChange) {
				return;
			}
			if (selectedColorSchemeTreeNode != null) {
				ColorSchemeTreeNode rootTreeNode = ColorSchemeManager.getColorSchemeManager().getRootTreeNode();
				ArrayList<String> paths = new ArrayList<>();
				while (!StringUtilities.isNullOrEmpty(selectedColorSchemeTreeNode.getName())) {
					paths.add(selectedColorSchemeTreeNode.getName());
					selectedColorSchemeTreeNode = (ColorSchemeTreeNode) selectedColorSchemeTreeNode.getParent();
				}
				selectedColorSchemeTreeNode = rootTreeNode;
				for (int size = paths.size() - 1; size >= 0; size--) {
					ColorSchemeTreeNode node = selectedColorSchemeTreeNode.getChildByName(paths.get(size));
					if (node != null) {
						selectedColorSchemeTreeNode = node;
					} else {
						selectedColorSchemeTreeNode = getDefaultNode();
						break;
					}
				}
//				ColorSchemeTreeNode parent = rootTreeNode.getChild(((ColorSchemeTreeNode) selectedColorSchemeTreeNode.getParent()).getName());
//				for (int i = 0; i < parent.getChildCount(); i++) {
//					if (parent.getName().equals(selectedColorSchemeTreeNode.getName())) {
//						selectedColorSchemeTreeNode = parent;
//						break;
//					}
//				}
			}
			tree.setModel(new DefaultTreeModel(ColorSchemeManager.getColorSchemeManager().getRootTreeNode()));
			JTreeUIUtilities.expandTree(tree, true);
			reAddElements();
		}
	};
	private MyAWTEventListener myAWTEventListener = new MyAWTEventListener();
	private GridBagConstraintsHelper panelShowConstraints;
	private boolean isReAddElements = false;
	private JTree tree;
	private TreeComboBox treeComboBox;

	/**
	 * 构造函数
	 */
	public ColorsComboBox() {
		this(ThemeType.UNIQUE);
	}

	/**
	 * 根据专题图类型初始化，或者自行输入节点名称初始化
	 *
	 * @param themeType 专题图类型或节点名称
	 */
	public ColorsComboBox(Object themeType) {
		super();
		this.themeType = themeType;
		this.setMinimumSize(new Dimension(20, 24));
		this.setPreferredSize(new Dimension(20, 24));
		itemListeners = new ArrayList<>();
		colorsCellRenderer = new ColorsCellRenderer(this);
		ColorSchemeManager.getColorSchemeManager().addColorSchemeManagerChangedListener(colorSchemeManagerChangedListener);
		initComponents();
		initLayout();
		initListeners();
		initComponentState();
	}

	private void initComponents() {
		panelShow = new JPanel();
		panelShow.setBorder(new LineBorder(Color.GRAY));
		panelShow.setMinimumSize(new Dimension(20, 23));
		buttonPopup = new JButton();
		buttonPopup.setIcon(new MetalComboBoxIcon());
		buttonPopup.setMaximumSize(new Dimension(20, 23));
		buttonPopup.setMinimumSize(new Dimension(20, 23));
		buttonPopup.setPreferredSize(new Dimension(20, 23));
		colorSchemeSelected = null;
		popupMenuColorScheme = new JPopupMenu() {
			@Override
			public void menuSelectionChanged(boolean isIncluded) {
				// 不隐藏
			}

			@Override
			public void setVisible(boolean b) {
				if (b) {
					Toolkit.getDefaultToolkit().addAWTEventListener(myAWTEventListener, AWTEvent.MOUSE_EVENT_MASK | sun.awt.SunToolkit.GRAB_EVENT_MASK);
				} else {
					Toolkit.getDefaultToolkit().removeAWTEventListener(myAWTEventListener);
				}
				popupMenuColorScheme.setPopupSize(panelShow.getWidth() + 20, 500);
				super.setVisible(b);
			}
		};
		textFieldSearch = new TextFieldSearch();

		tree = new JTree(ColorSchemeManager.getColorSchemeManager().getRootTreeNode());
		tree.setRootVisible(false);
		tree.setShowsRootHandles(true);
		((DefaultTreeCellRenderer) tree.getCellRenderer()).setLeafIcon(((DefaultTreeCellRenderer) tree.getCellRenderer()).getDefaultClosedIcon());
		JTreeUIUtilities.expandTree(tree, true);
		treeComboBox = new TreeComboBox(tree);
		treeComboBox.setPopupMenuPreferredHeight(450);
		listColors = new JList<>();
		listColors.setModel(new DefaultListModel<ColorScheme>());
		selectedColorSchemeTreeNode = null;
		listColors.setCellRenderer(colorsCellRenderer);
		panelShowConstraints = new GridBagConstraintsHelper(1, 1).setFill(GridBagConstraints.BOTH).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER);
	}

	private void initLayout() {
		panelShow.setLayout(new GridBagLayout());
		panelShow.add(new Label(), panelShowConstraints);
		this.setLayout(new GridBagLayout());
		this.add(panelShow, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER));
		this.add(buttonPopup, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(0, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER));

		popupMenuColorScheme.setLayout(new GridBagLayout());
		popupMenuColorScheme.add(textFieldSearch, new GridBagConstraintsHelper(0, 0, 1, 1).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.CENTER).setWeight(1, 0));
		popupMenuColorScheme.add(treeComboBox, new GridBagConstraintsHelper(0, 1, 1, 1).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.CENTER).setWeight(1, 0));
		popupMenuColorScheme.add(new JScrollPane(listColors), new GridBagConstraintsHelper(0, 2, 1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1));

	}

	private void initListeners() {
		buttonPopup.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				popupMenuColorScheme.show(ColorsComboBox.this, 0, ColorsComboBox.this.getHeight());
			}
		});

		panelShow.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (panelShow.isEnabled()) {
					popupMenuColorScheme.show(ColorsComboBox.this, 0, ColorsComboBox.this.getHeight());
				}
			}
		});

		treeComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					selectedColorSchemeTreeNode = (ColorSchemeTreeNode) treeComboBox.getSelectedItem();
					reAddElements();
					textFieldSearch.requestFocus();
				}
			}
		});

		listColors.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!isReAddElements) {
					int selectedIndex = listColors.getSelectedIndex();
					if (selectedIndex != -1 && selectedIndex == listColors.getModel().getSize() - 1) {
						popupMenuColorScheme.setVisible(false);
						showUserDefineDialog();
					} else if (selectedIndex != -1) {
						popupMenuColorScheme.setVisible(false);
						currentColors = null;
						colorSchemeSelected = listColors.getSelectedValue();
						setSelectedItem(colorSchemeSelected.getColors());
						fireItemStateChanged(new ItemEvent(ColorsComboBox.this, ItemEvent.ITEM_STATE_CHANGED, listColors.getSelectedValue(), ItemEvent.SELECTED));
					}
				}
			}
		});
		panelShow.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				setSelectedItem(currentColors);
			}
		});

		textFieldSearch.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				textFieldSearchChanged();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				textFieldSearchChanged();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				textFieldSearchChanged();
			}

			private void textFieldSearchChanged() {
				reAddElements();
			}

		});

	}

	private void initComponentState() {
		if (selectedColorSchemeTreeNode == null) {
			selectedColorSchemeTreeNode = getDefaultNode();
		}
		if (selectedColorSchemeTreeNode != null) {
			treeComboBox.setSelectedItem(JTreeUIUtilities.getPath(selectedColorSchemeTreeNode));
		}
//		reAddElements();
		if (listColors.getModel().getSize() >= 2) {
			listColors.setSelectedIndex(0);
		}
	}

	private void reAddElements() {
		LogUtilities.outPut("ColorsComboBox reAddElements");
		isReAddElements = true;
		((DefaultListModel) listColors.getModel()).removeAllElements();
		if (selectedColorSchemeTreeNode != null) {
			java.util.List<ColorScheme> colorSchemes = selectedColorSchemeTreeNode.getColorSchemes();
			if (colorSchemes != null) {
				for (ColorScheme colorScheme : colorSchemes) {
					if (StringUtilities.isContain(colorScheme.getName(), textFieldSearch.getText())) {
						((DefaultListModel<ColorScheme>) listColors.getModel()).addElement(colorScheme);
					}
				}
			}
//			buttonColorGroup.setText(selectedColorSchemeTreeNode.getShowName());
		}
		isReAddElements = false;
		((DefaultListModel) listColors.getModel()).addElement(null);
//		listColors.setSelectedIndex(-1);
	}

	private ColorSchemeTreeNode getDefaultNode() {
		ColorSchemeTreeNode defaultNode = (ColorSchemeTreeNode) ColorSchemeManager.getColorSchemeManager().getRootTreeNode().getChildAt(0);
		if (themeType == ThemeType.UNIQUE) {
			return defaultNode.getChildByName(ControlsProperties.getString("String_ColorSchemeManager_Map_ThemeUnique"));
		} else if (themeType == ThemeType.RANGE) {
			return defaultNode.getChildByName(ControlsProperties.getString("String_ColorSchemeManager_Map_ThemeRange"));
		} else if (themeType == ThemeType.GRADUATEDSYMBOL) {
			return defaultNode.getChildByName(ControlsProperties.getString("String_ColorSchemeManager_Map_ThemeGraduatedSymbol"));
		} else if (themeType == ThemeType.GRAPH) {
			return defaultNode.getChildByName(ControlsProperties.getString("String_ColorSchemeManager_Map_ThemeGraph"));
		} else if (themeType != null && themeType instanceof String) {
			return defaultNode.getChildByName((String) themeType);
		}
		return null;
	}


	private void showUserDefineDialog() {
		ColorSchemeEditorDialog dialog = new ColorSchemeEditorDialog();

		ColorSchemeTreeNode userDefineNode = ((ColorSchemeTreeNode) ColorSchemeManager.getColorSchemeManager().getRootTreeNode().getChildAt(1)).getChildByName("UserDefine");
		if (userDefineNode == null) {
			userDefineNode = ((ColorSchemeTreeNode) ColorSchemeManager.getColorSchemeManager().getRootTreeNode().getChildAt(1)).getChild("UserDefine");
			((DefaultTreeModel) tree.getModel()).nodesWereInserted(userDefineNode.getParent(), new int[]{userDefineNode.getParent().getIndex(userDefineNode)});
		}
		java.util.List<ColorScheme> colorSchemes = userDefineNode.getColorSchemes();
		ArrayList<String> names = new ArrayList<>();
		if (colorSchemes != null && colorSchemes.size() > 0) {
			for (ColorScheme colorScheme : colorSchemes) {
				names.add(colorScheme.getName());
			}
		}
		dialog.setExitNames(names);
		if (dialog.showDialog() == DialogResult.OK) {
			userDefineNode.addColorScheme(dialog.getColorScheme());
			dialog.getColorScheme().save();
			isUserDefineFireChange = true;
			ColorSchemeManager.getColorSchemeManager().fireColorSchemeManagerChanged();
			selectedColorSchemeTreeNode = userDefineNode;
			treeComboBox.setSelectedItem(null);
			treeComboBox.setSelectedItem(JTreeUIUtilities.getPath(userDefineNode));
			listColors.setSelectedIndex(listColors.getModel().getSize() - 2);
			isUserDefineFireChange = false;
		} else {
			listColors.clearSelection();
		}
	}


	@Override
	public Object[] getSelectedObjects() {
		return new Object[]{listColors.getSelectedValue()};
	}

	public void addItemListener(ItemListener itemListener) {
		itemListeners.add(itemListener);
	}

	public void removeItemListener(ItemListener itemListener) {
		itemListeners.remove(itemListener);
	}

	public void dispose() {
		removeColorChangedListener();
	}

	/**
	 * 移除颜色方案改变时的监听事件，使用完一定要记得移除
	 */
	public void removeColorChangedListener() {
		ColorSchemeManager.getColorSchemeManager().removeColorSchemeManagerChangedListener(colorSchemeManagerChangedListener);
	}

	/**
	 * 添加颜色方案改变时的监听事件
	 */
	public void addColorChangedListener() {
		removeColorChangedListener();
		ColorSchemeManager.getColorSchemeManager().addColorSchemeManagerChangedListener(colorSchemeManagerChangedListener);
	}


	private void fireItemStateChanged(ItemEvent e) {
		if (itemListeners != null && itemListeners.size() > 0) {
			for (int i = itemListeners.size() - 1; i >= 0; i--) {
				itemListeners.get(i).itemStateChanged(e);
			}
		}
	}

	public Colors getSelectedItem() {
		return currentColors;
	}

	public void setSelectedItem(Colors colors) {
		this.currentColors = colors;
		panelShow.removeAll();
		panelShow.add(ColorScheme.getColorsLabel(currentColors, panelShow.getWidth(), panelShow.getHeight()), panelShowConstraints);
		panelShow.getParent().validate();
		panelShow.getParent().repaint();
//		panelShow.repaint();
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		this.panelShow.setEnabled(enabled);
		this.buttonPopup.setEnabled(enabled);
	}

	private class MyAWTEventListener implements AWTEventListener {

		@Override
		public void eventDispatched(AWTEvent event) {
			if (event instanceof sun.awt.UngrabEvent) {
				cancelPopupMenu();
				return;
			}
			if (!(event instanceof MouseEvent)) {
				// We are interested in MouseEvents only
				return;
			}
			MouseEvent me = (MouseEvent) event;
			Component src = me.getComponent();
			switch (me.getID()) {
				case MouseEvent.MOUSE_PRESSED:
//					checkPopupMenuChooseColorGroupState(src);
					if (isInPopup(src) ||
							(src instanceof JMenu && ((JMenu) src).isSelected())) {
						return;
					}
					// Cancel popup only if this property was not set.
					// If this property is set to TRUE component wants
					// to deal with this event by himself.
					cancelPopupMenu();
					src.dispatchEvent(event);
					// Ask UIManager about should we consume event that closes
					// popup. This made to match native apps behaviour.
					boolean consumeEvent =
							UIManager.getBoolean("PopupMenu.consumeEventOnClose");
					// Consume the event so that normal processing stops.
					if (consumeEvent && !(src instanceof MenuElement)) {
						me.consume();
					}
					break;

				case MouseEvent.MOUSE_RELEASED:
					if (!(src instanceof MenuElement)) {
						// Do not forward event to MSM, let component handle it
						if (isInPopup(src)) {
							break;
						}
					}
					if (src instanceof JMenu || !(src instanceof JMenuItem)) {
						MenuSelectionManager.defaultManager().
								processMouseEvent(me);
					}
					break;
				case MouseEvent.MOUSE_DRAGGED:
					if (!(src instanceof MenuElement)) {
						// For the MOUSE_DRAGGED event the src is
						// the Component in which mouse button was pressed.
						// If the src is in popupMenu,
						// do not forward event to MSM, let component handle it.
						if (isInPopup(src)) {
							break;
						}
					}
					MenuSelectionManager.defaultManager().
							processMouseEvent(me);
					break;
				case MouseEvent.MOUSE_WHEEL:
//					checkPopupMenuChooseColorGroupState(src);
					if (isInPopup(src)) {
						return;
					}
					cancelPopupMenu();
					break;
			}
		}


		private void cancelPopupMenu() {
			treeComboBox.hidePopup();
			popupMenuColorScheme.setVisible(false);
		}

		private boolean isInPopup(Component src) {
			return getParentPopupMenu(src) != null;
		}

		private JPopupMenu getParentPopupMenu(Component src) {
			for (Component c = src; c != null; c = c.getParent()) {
				if (c instanceof Applet || c instanceof Window) {
					break;
				} else if (c instanceof JPopupMenu) {
					return ((JPopupMenu) c);
				}
			}
			return null;
		}
	}

	class ColorsCellRenderer implements ListCellRenderer {
		private static final long serialVersionUID = 1L;
		private final ColorsComboBox colorsComboBox;
		private JLabel emptyLabel = new JLabel();

		public ColorsCellRenderer(ColorsComboBox colorsComboBox) {
			this.colorsComboBox = colorsComboBox;
		}

		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			if (isReAddElements) {
				return emptyLabel;
			}
			LogUtilities.outPut("ColorsComboBox CellRender Do Cell");
			JLabel colorsLabel = new JLabel();
			JLabel labelName = new JLabel();
			ColorScheme colorScheme = (ColorScheme) value;
			if (colorScheme != null) {

				Colors colors = colorScheme.getColors();
				int imageWidth = panelShow.getWidth();
				int imageHeight = 24;
				colorsLabel = ColorScheme.getColorsLabel(colors, imageWidth, imageHeight);
				labelName.setText(colorScheme.getName());
			} else {
				colorsLabel.setMinimumSize(new Dimension(panelShow.getWidth(), 24));
				colorsLabel.setPreferredSize(new Dimension(panelShow.getWidth(), 24));

				labelName.setText(ControlsProperties.getString("String_CustomColor"));
			}
			labelName.setOpaque(true);

			if (isSelected) {
				colorsLabel.setBorder(new LineBorder(Color.black, 1, false));
				labelName.setBackground(list.getSelectionBackground());
			} else {
				colorsLabel.setBorder(new LineBorder(Color.white, 1, false));
				labelName.setBackground(list.getBackground());
			}
			JPanel jPanel = new JPanel();
			jPanel.setLayout(new GridBagLayout());
			if (!StringUtilities.isNullOrEmpty(textFieldSearch.getText())) {
				JLabelUIUtilities.highLightText(labelName, textFieldSearch.getText());
			}
			jPanel.add(labelName, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(0, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.NONE));
			jPanel.add(colorsLabel, new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH));
			return jPanel;
		}


	}
}
