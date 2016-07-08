package com.supermap.desktop.controls.colorScheme;

import com.supermap.data.Colors;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.utilities.JLabelUIUtilities;
import com.supermap.desktop.dialog.ColorSchemeDialogs.ColorSchemeTreeNode;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.TextFieldSearch;
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
import java.applet.Applet;
import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * 颜色下拉选择器
 *
 * @author xuzw
 */
public class ColorsComboBox extends JComponent implements ItemSelectable {
	private static final long serialVersionUID = 1L;
	private Object themeType = ThemeType.UNIQUE;

	private JPanel panelShow;// 用来存放label
	private JButton buttonPopup;

	private ColorScheme colorSchemeSelected;
	// 主面板
	private JPopupMenu popupMenuColorScheme;
	// 搜索
	private TextFieldSearch textFieldSearch;
	// 显示当前分组按钮
	private JButton buttonColorGroup;
	// 显示当前颜色
	private JList<ColorScheme> listColors;

	// 选择颜色分组
	private JPopupMenu popupMenuChooseColorGroup;

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
				ColorSchemeTreeNode parent = rootTreeNode.getChild(((ColorSchemeTreeNode) selectedColorSchemeTreeNode.getParent()).getName());
				for (int i = 0; i < parent.getChildCount(); i++) {
					if (parent.getName().equals(selectedColorSchemeTreeNode.getName())) {
						selectedColorSchemeTreeNode = parent;
						break;
					}
				}
			}
			reAddElements();
			popupMenuChooseColorGroup = null;
		}
	};
	private ListSelectionListener listGroupSelectionListener = new ListSelectionListener() {
		private boolean lock = false;

		@Override
		public void valueChanged(ListSelectionEvent e) {
			Object source = e.getSource();
			if (source != null && source instanceof JList && !lock) {
				lock = true;
				selectedColorSchemeTreeNode = (ColorSchemeTreeNode) ((JList) source).getSelectedValue();
				popupMenuChooseColorGroup.setVisible(false);
				((JList) source).clearSelection();
				reAddElements();
				popupMenuColorScheme.setVisible(true);
				lock = false;
			}
		}
	};
	private MyAWTEventListener myAWTEventListener = new MyAWTEventListener();
	private GridBagConstraintsHelper panelShowConstraints;
	private boolean isReAddElements = false;

	/**
	 * 构造函数
	 */
	public ColorsComboBox() {
		super();
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

	/**
	 * 根据专题图类型初始化，或者自行输入节点名称初始化
	 *
	 * @param themeType 专题图类型或节点名称
	 */
	public ColorsComboBox(Object themeType) {
		this();
		this.themeType = themeType;
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
				popupMenuColorScheme.setPopupSize(panelShow.getWidth() + 25, 500);
				super.setVisible(b);
			}
		};
		textFieldSearch = new TextFieldSearch();
		buttonColorGroup = new JButton();
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
		popupMenuColorScheme.add(buttonColorGroup, new GridBagConstraintsHelper(0, 1, 1, 1).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.CENTER).setWeight(1, 0));
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
				popupMenuColorScheme.show(ColorsComboBox.this, 0, ColorsComboBox.this.getHeight());
			}
		});

		buttonColorGroup.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getPopupMenuChooseColorGroup().show(buttonColorGroup, -145, 0);
			}
		});

		listColors.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!isReAddElements) {
					int selectedIndex = listColors.getSelectedIndex();
					popupMenuColorScheme.setVisible(false);
					if (selectedIndex != -1 && selectedIndex == listColors.getModel().getSize() - 1) {
						showUserDefineDialog();
					} else if (selectedIndex != -1) {
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
				// FIXME: 2016/7/8 bug会导致无法输入中文
				popupMenuColorScheme.setVisible(true);
				textFieldSearch.requestFocus();
			}

		});
	}

	private void initComponentState() {
		if (selectedColorSchemeTreeNode == null) {
			selectedColorSchemeTreeNode = getDefaultNode();
		}
		reAddElements();
		if (listColors.getModel().getSize() > 2) {
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
			buttonColorGroup.setText(selectedColorSchemeTreeNode.getShowName());
		}
		((DefaultListModel) listColors.getModel()).addElement(null);
		isReAddElements = false;
		listColors.getParent().validate();
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

	public JPopupMenu getPopupMenuChooseColorGroup() {
		if (popupMenuChooseColorGroup == null) {
			JPanel jPanel = new JPanel();
			popupMenuChooseColorGroup = new JPopupMenu();
			jPanel.setLayout(new GridBagLayout());
			ColorSchemeTreeNode rootTreeNode = ColorSchemeManager.getColorSchemeManager().getRootTreeNode();

			int comCount = 0;
			// 收藏夹
			if (rootTreeNode.getChildAt(2).getChildCount() > 0) {
				ColorSchemeTreeNode favoriteNode = (ColorSchemeTreeNode) rootTreeNode.getChildAt(2);
				JLabel label = new JLabel(favoriteNode.getName());
				jPanel.add(label, new GridBagConstraintsHelper(0, comCount, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(0, 0));
				comCount++;
				JList<ColorSchemeTreeNode> listFavorite = new JList<>();
				listFavorite.setModel(new DefaultListModel<ColorSchemeTreeNode>());
				for (int i = 0; i < favoriteNode.getChildCount(); i++) {
					((DefaultListModel<ColorSchemeTreeNode>) listFavorite.getModel()).addElement(((ColorSchemeTreeNode) favoriteNode.getChildAt(i)));
				}
				listFavorite.addListSelectionListener(listGroupSelectionListener);
				jPanel.add(listFavorite, new GridBagConstraintsHelper(0, comCount, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 0).setFill(GridBagConstraints.BOTH));
				comCount++;
			}

			ColorSchemeTreeNode nodeDefault = (ColorSchemeTreeNode) rootTreeNode.getChildAt(0);
			JLabel label = new JLabel(nodeDefault.getName());
			jPanel.add(label, new GridBagConstraintsHelper(0, comCount, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(0, 0));
			comCount++;
			JList<ColorSchemeTreeNode> listDefault = new JList<>();
			listDefault.setModel(new DefaultListModel<ColorSchemeTreeNode>());
			for (int i = 0; i < nodeDefault.getChildCount(); i++) {
				((DefaultListModel<ColorSchemeTreeNode>) listDefault.getModel()).addElement(((ColorSchemeTreeNode) nodeDefault.getChildAt(i)));
			}
			listDefault.addListSelectionListener(listGroupSelectionListener);
			jPanel.add(listDefault, new GridBagConstraintsHelper(0, comCount, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 0).setFill(GridBagConstraints.BOTH));
			comCount++;

			if (rootTreeNode.getChildAt(1).getChildCount() > 0) {
				ColorSchemeTreeNode userDefineNode = (ColorSchemeTreeNode) rootTreeNode.getChildAt(1);
				JLabel labelUserDefine = new JLabel(userDefineNode.getName());
				jPanel.add(labelUserDefine, new GridBagConstraintsHelper(0, comCount, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(0, 0));
				comCount++;
				JList<ColorSchemeTreeNode> listUserDefine = new JList<>();
				listUserDefine.setModel(new DefaultListModel<ColorSchemeTreeNode>());
				for (int i = 0; i < userDefineNode.getChildCount(); i++) {
					((DefaultListModel<ColorSchemeTreeNode>) listUserDefine.getModel()).addElement(((ColorSchemeTreeNode) userDefineNode.getChildAt(i)));
				}
				listUserDefine.addListSelectionListener(listGroupSelectionListener);
				jPanel.add(listUserDefine, new GridBagConstraintsHelper(0, comCount, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.BOTH));
			}
			popupMenuChooseColorGroup.setLayout(new GridBagLayout());
			popupMenuChooseColorGroup.add(new JScrollPane(jPanel), new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER));
		}
		return popupMenuChooseColorGroup;
	}

	private void showUserDefineDialog() {
		ColorSchemeEditorDialog dialog = new ColorSchemeEditorDialog();
		if (dialog.showDialog() == DialogResult.OK) {
			ColorSchemeTreeNode userDefineNode = ((ColorSchemeTreeNode) ColorSchemeManager.getColorSchemeManager().getRootTreeNode().getChildAt(1)).getChild("UserDefine");
			userDefineNode.addColorScheme(dialog.getColorScheme());
			dialog.getColorScheme().save();
			isUserDefineFireChange = true;
			ColorSchemeManager.getColorSchemeManager().fireColorSchemeManagerChanged();
			selectedColorSchemeTreeNode = userDefineNode;
			reAddElements();
			listColors.setSelectedIndex(listColors.getModel().getSize() - 2);
			isUserDefineFireChange = false;
		} else {
			listColors.setSelectedIndex(-1);
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
		panelShow.repaint();
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
					checkPopupMenuChooseColorGroupState(src);
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
					checkPopupMenuChooseColorGroupState(src);
					if (isInPopup(src)) {
						return;
					}
					cancelPopupMenu();
					break;
			}
		}

		private void checkPopupMenuChooseColorGroupState(Component src) {
			if (popupMenuChooseColorGroup != null && popupMenuChooseColorGroup.isVisible() && getParentPopupMenu(src) != popupMenuChooseColorGroup) {
				popupMenuChooseColorGroup.setVisible(false);
			}
		}

		private void cancelPopupMenu() {
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
