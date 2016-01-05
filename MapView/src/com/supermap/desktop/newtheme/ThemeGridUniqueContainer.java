package com.supermap.desktop.newtheme;

import com.supermap.data.ColorGradientType;
import com.supermap.data.Colors;
import com.supermap.data.DatasetGrid;
import com.supermap.desktop.Application;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.ColorSelectionPanel;
import com.supermap.desktop.ui.controls.ColorsComboBox;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.InternalImageIconFactory;
import com.supermap.desktop.utilties.StringUtilties;
import com.supermap.mapping.*;
import com.supermap.ui.MapControl;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class ThemeGridUniqueContainer extends ThemeChangePanel {
	private static final long serialVersionUID = 1L;
	private JTabbedPane tabbedPaneInfo = new JTabbedPane(JTabbedPane.TOP);
	private JPanel panelProperty = new JPanel();
	private JLabel labelColorStyle = new JLabel();
	private ColorsComboBox comboboxColor = new ColorsComboBox();
	private JToolBar toolBar = new JToolBar();
	private JScrollPane scollPane = new JScrollPane();
	private JTable tableUniqueInfo = new JTable();
	private JButton buttonVisble = new JButton();
	private JButton buttonForeGroundColor = new JButton();
	private JButton buttonAdd = new JButton();
	private JButton buttonDelete = new JButton();
	private JButton buttonAscend = new JButton();
	private JButton buttonDescend = new JButton();
	private JButton buttonAntitone = new JButton();

	private AddItemPanel addItemPanel;
	private String[] nameStrings = {MapViewProperties.getString("String_Title_Visible"), MapViewProperties.getString("String_Title_Sytle"),
			MapViewProperties.getString("String_ThemeGraphItemManager_UniqueValue"), MapViewProperties.getString("String_ThemeGraphTextFormat_Caption")};
	private transient ThemeGridUnique themeUnique;
	private transient DatasetGrid datasetGrid;
	private transient Layer themeUniqueLayer;
	private transient Map map;
	private ArrayList<ThemeGridUniqueItem> deleteItems = new ArrayList<ThemeGridUniqueItem>();
	private boolean isRefreshAtOnce = true;
	private boolean isNewTheme = false;

	private static int TABLE_COLUMN_VISIBLE = 0;
	private static int TABLE_COLUMN_GEOSTYLE = 1;
	private static int TABLE_COLUMN_UNIQUE = 2;
	private static int TABLE_COLUMN_CAPTION = 3;

	private transient LocalComboBoxItemListener comboBoxItemListener = new LocalComboBoxItemListener();
	private transient LocalActionListener actionListener = new LocalActionListener();
	private transient LocalTableMouseListener localTableMouseListener = new LocalTableMouseListener();
	private transient LocalKeyListener localKeyListener = new LocalKeyListener();
	private transient LocalPopmenuListener popmenuListener = new LocalPopmenuListener();
	private transient LocalTableModelListener tableModelListener = new LocalTableModelListener();

	public ThemeGridUniqueContainer(DatasetGrid datasetGrid, ThemeGridUnique themeUnique) {
		this.datasetGrid = datasetGrid;
		this.themeUnique = themeUnique;
		this.map = initCurrentTheme(datasetGrid);
		this.isNewTheme = true;
		initComponents();
		initResources();
		registActionListener();
	}

	/**
	 * @wbp.parser.constructor
	 */
	public ThemeGridUniqueContainer(Layer layer) {
		this.themeUniqueLayer = layer;
		this.themeUnique = (ThemeGridUnique) themeUniqueLayer.getTheme();
		this.datasetGrid = (DatasetGrid) layer.getDataset();
		this.map = ThemeGuideFactory.getMapControl().getMap();
		initComponents();
		initResources();
		registActionListener();
	}

	/**
	 * 初始化单值专题图
	 *
	 * @param dataset
	 * @return
	 */
	private Map initCurrentTheme(DatasetGrid datasetGrid) {
		MapControl mapControl = ThemeGuideFactory.getMapControl();
		if (null != mapControl) {
			this.themeUniqueLayer = mapControl.getMap().getLayers().add(datasetGrid, themeUnique, true);
			this.themeUnique = (ThemeGridUnique) themeUniqueLayer.getTheme();
			UICommonToolkit.getLayersManager().getLayersTree().setSelectionRow(0);
			mapControl.getMap().refresh();
		}
		return mapControl.getMap();
	}

	/**
	 * 界面布局入口
	 */
	private void initComponents() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		this.setLayout(gridBagLayout);

		this.tabbedPaneInfo.add(MapViewProperties.getString("String_Theme_Property"), this.panelProperty);
		this.add(tabbedPaneInfo, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setWeight(1, 1));
		this.comboboxColor.setSelectedIndex(21);
		if (isNewTheme) {
			refreshColor();
		}

		initPanelProperty();
	}

	/**
	 * 资源化
	 */
	private void initResources() {
		this.labelColorStyle.setText(MapViewProperties.getString("String_Label_ColorScheme"));
		this.buttonVisble.setToolTipText(MapViewProperties.getString("String_Title_Visible"));
		this.buttonForeGroundColor.setToolTipText(MapViewProperties.getString("String_Title_Sytle"));
		this.buttonAdd.setToolTipText(MapViewProperties.getString("String_Title_Add"));
		this.buttonDelete.setToolTipText(MapViewProperties.getString("String_Title_Delete"));
		this.buttonAscend.setToolTipText(MapViewProperties.getString("String_Title_Ascend"));
		this.buttonDescend.setToolTipText(MapViewProperties.getString("String_Title_Descend"));
		this.buttonAntitone.setToolTipText(MapViewProperties.getString("String_Title_Antitone"));
	}

	/**
	 * 控件注册事件
	 */
	void registActionListener() {
		unregistActionListener();
		this.comboboxColor.addItemListener(this.comboBoxItemListener);
		this.buttonVisble.addActionListener(this.actionListener);
		this.buttonAdd.addActionListener(this.actionListener);
		this.buttonDelete.addActionListener(this.actionListener);
		this.buttonForeGroundColor.addActionListener(this.actionListener);
		this.buttonAntitone.addActionListener(this.actionListener);
		this.tableUniqueInfo.addMouseListener(this.localTableMouseListener);
		this.tableUniqueInfo.addKeyListener(this.localKeyListener);
		this.tableUniqueInfo.putClientProperty("terminateEditOnFocusLost", true);
		this.tableUniqueInfo.getModel().addTableModelListener(this.tableModelListener);
	}

	/**
	 * 修改单值项
	 */
	private void setUniqueItemUnique(double uniqueValue) {
		int index = 0;
		if (0 <= this.tableUniqueInfo.getSelectedRow() && !hasUnique(uniqueValue)) {
			index = this.tableUniqueInfo.getSelectedRow();
			if (index != tableUniqueInfo.getRowCount() - 1) {
				this.themeUnique.getItem(index).setUnique(uniqueValue);
			}
		}
	}

	/**
	 * 修改单值标题
	 */
	private void setUniqueItemCaption(String caption) {
		int index = 0;
		if (0 <= this.tableUniqueInfo.getSelectedRow() && !caption.isEmpty()) {
			index = this.tableUniqueInfo.getSelectedRow();
			if (index != tableUniqueInfo.getRowCount() - 1) {
				this.themeUnique.getItem(index).setCaption(caption);
			}
		}
	}

	/**
	 * 销毁事件
	 */
	public void unregistActionListener() {
		this.comboboxColor.removeItemListener(this.comboBoxItemListener);
		this.buttonVisble.removeActionListener(this.actionListener);
		this.buttonAdd.removeActionListener(this.actionListener);
		this.buttonDelete.removeActionListener(this.actionListener);
		this.buttonForeGroundColor.removeActionListener(this.actionListener);
		this.buttonAntitone.removeActionListener(this.actionListener);
		this.tableUniqueInfo.removeMouseListener(this.localTableMouseListener);
		this.tableUniqueInfo.removeKeyListener(this.localKeyListener);
		this.tableUniqueInfo.getModel().removeTableModelListener(this.tableModelListener);
	}

	/**
	 * 初始化工具条
	 */
	private void initToolBar() {
		this.buttonVisble.setIcon(InternalImageIconFactory.VISIBLE);
		this.buttonForeGroundColor.setIcon(InternalImageIconFactory.FOREGROUND_COLOR);
		this.buttonAdd.setIcon(InternalImageIconFactory.ADD_ITEM);
		this.buttonDelete.setIcon(new ImageIcon(ThemeUniqueContainer.class
				.getResource("/com/supermap/desktop/coreresources/ToolBar/Image_ToolButton_Delete.png")));
		this.buttonAntitone.setIcon(InternalImageIconFactory.Rever);
	}

	/**
	 * 属性面板布局
	 */
	private void initPanelProperty() {
		this.panelProperty.setLayout(new GridBagLayout());
		toolBar.setFloatable(false);
		this.toolBar.add(this.buttonVisble);
		this.toolBar.add(this.buttonForeGroundColor);
		this.toolBar.addSeparator();
		this.toolBar.add(this.buttonAdd);
		this.toolBar.add(this.buttonDelete);
		this.toolBar.add(this.buttonAntitone);
		initToolBar();
		//@formatter:off
		this.panelProperty.add(this.labelColorStyle,    new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 10, 5, 0).setWeight(20, 0).setIpad(60, 0));
		this.panelProperty.add(this.comboboxColor,      new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 10, 5, 10).setWeight(60, 0).setFill(GridBagConstraints.HORIZONTAL));
		this.panelProperty.add(this.toolBar,            new GridBagConstraintsHelper(0, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 0).setWeight(100, 0));
		this.panelProperty.add(this.scollPane,          new GridBagConstraintsHelper(0, 2, 2, 1).setAnchor(GridBagConstraints.NORTH).setInsets(0, 10, 5, 10).setFill(GridBagConstraints.BOTH).setWeight(100, 3));
		getTable();
		this.tableUniqueInfo.setRowSelectionInterval(0, 0);
		this.scollPane.setViewportView(tableUniqueInfo);		
		//@formatter:on
	}

	/**
	 * 表格初始化
	 *
	 * @return m_table
	 */
	private JTable getTable() {
		int uniqueCount = this.themeUnique.getCount();
		DefaultTableModel defaultTableModel = new DefaultTableModel(new Object[uniqueCount + 1][4], nameStrings) {
			private static final long serialVersionUID = 1L;

			@SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
			public Class getColumnClass(int column) {// 要这样定义table，要重写这个方法0，0的意思就是别的格子的类型都跟0,0的一样。
				if (TABLE_COLUMN_VISIBLE == column || TABLE_COLUMN_GEOSTYLE == column) {
					return getValueAt(0, 1).getClass();
				}
				return String.class;
			}

			@Override
			public boolean isCellEditable(int rowIndex, int columnIndex) {
				if (columnIndex == TABLE_COLUMN_UNIQUE || columnIndex == TABLE_COLUMN_CAPTION) {
					return true;
				}
				return false;
			}

		};
		this.tableUniqueInfo.setModel(defaultTableModel);
		initColumnIcon();
		this.tableUniqueInfo.setRowHeight(20);

		TableColumn visibleColumn = tableUniqueInfo.getColumn(MapViewProperties.getString("String_Title_Visible"));
		TableColumn viewColumn = tableUniqueInfo.getColumn(MapViewProperties.getString("String_Title_Sytle"));
		visibleColumn.setMaxWidth(40);
		viewColumn.setMaxWidth(100);
		this.tableUniqueInfo.getModel().removeTableModelListener(this.tableModelListener);
		this.tableUniqueInfo.getModel().addTableModelListener(this.tableModelListener);
		return this.tableUniqueInfo;
	}

	/**
	 * 填充图片
	 */
	private void initColumnIcon() {
		int uniqueCount = this.themeUnique.getCount();
		for (int i = 0; i < uniqueCount; i++) {
			ThemeGridUniqueItem uniqueItem = themeUnique.getItem(i);
			boolean isVisible = uniqueItem.isVisible();
			ImageIcon visibleIcon = InternalImageIconFactory.VISIBLE;
			if (!isVisible) {
				visibleIcon = InternalImageIconFactory.INVISIBLE;
			}
			this.tableUniqueInfo.setValueAt(visibleIcon, i, TABLE_COLUMN_VISIBLE);
			Color color = uniqueItem.getColor();
			this.tableUniqueInfo.setValueAt(ThemeItemLabelDecorator.buildColorIcon(datasetGrid, color), i, TABLE_COLUMN_GEOSTYLE);
			DecimalFormat format = new DecimalFormat("0.######");
			String unique = format.format(uniqueItem.getUnique());
			if (StringUtilties.isNumber(uniqueItem.getCaption())) {
				String caption = format.format(Double.parseDouble(uniqueItem.getCaption()));
				this.tableUniqueInfo.setValueAt(caption, i, TABLE_COLUMN_CAPTION);
			} else {
				this.tableUniqueInfo.setValueAt(uniqueItem.getCaption(), i, TABLE_COLUMN_CAPTION);
			}
			this.tableUniqueInfo.setValueAt(unique, i, TABLE_COLUMN_UNIQUE);
		}
		Color defualtColor = themeUnique.getDefaultColor();
		this.tableUniqueInfo.setValueAt(ThemeItemLabelDecorator.buildColorIcon(datasetGrid, defualtColor), uniqueCount, TABLE_COLUMN_GEOSTYLE);
		this.tableUniqueInfo.setValueAt(MapViewProperties.getString("String_defualt_style"),
				uniqueCount, TABLE_COLUMN_CAPTION);
	}

	class LocalTableMouseListener extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {
			if (1 == e.getClickCount() && tableUniqueInfo.getSelectedColumn() == TABLE_COLUMN_VISIBLE && tableUniqueInfo.getSelectedRows().length == 1
					&& tableUniqueInfo.getSelectedRow() != tableUniqueInfo.getRowCount() - 1) {
				updateButtonDeleteState();
				int selectRow = tableUniqueInfo.getSelectedRow();
				ThemeGridUniqueItem item = themeUnique.getItem(selectRow);
				boolean isVisible = item.isVisible();
				if (isVisible) {
					item.setVisible(false);
					tableUniqueInfo.setValueAt(InternalImageIconFactory.INVISIBLE, selectRow, TABLE_COLUMN_VISIBLE);
				} else {
					item.setVisible(true);
					tableUniqueInfo.setValueAt(InternalImageIconFactory.VISIBLE, selectRow, TABLE_COLUMN_VISIBLE);
				}
			} else if (2 == e.getClickCount() && tableUniqueInfo.getSelectedColumn() == TABLE_COLUMN_GEOSTYLE) {
				int selectRow = tableUniqueInfo.getSelectedRow();
				setItemColor(e.getX(), e.getY());
				tableUniqueInfo.setRowSelectionInterval(selectRow, selectRow);
				if (isRefreshAtOnce) {
					firePropertyChange("ThemeChange", null, null);
					ThemeGuideFactory.refreshMapAndLayer(map, themeUniqueLayer.getName(), true);
				}
			}
			// 包含最后一行不能做删除操作
			int[] selectRows = tableUniqueInfo.getSelectedRows();
			if (selectRows[selectRows.length - 1] == tableUniqueInfo.getRowCount() - 1) {
				buttonDelete.setEnabled(false);
			} else {
				buttonDelete.setEnabled(true);
			}
		}
	}

	/**
	 * 更改删除键状态
	 */
	private void updateButtonDeleteState() {
		int selectRow = tableUniqueInfo.getSelectedRow();
		if (selectRow == tableUniqueInfo.getRowCount() - 1) {
			buttonDelete.setEnabled(false);
		} else {
			buttonDelete.setEnabled(true);
		}
	}

	class LocalKeyListener extends KeyAdapter {

		@Override
		public void keyReleased(KeyEvent e) {
			if (e.getSource() == tableUniqueInfo && (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN)) {
				updateButtonDeleteState();
			}
		}
	}

	class LocalFocusLostListener extends FocusAdapter {

		@Override
		public void focusLost(FocusEvent e) {
			// 修改单值项的单值
			if (isRefreshAtOnce) {
				firePropertyChange("ThemeChange", null, null);
				ThemeGuideFactory.refreshMapAndLayer(map, themeUniqueLayer.getName(), true);
			}
		}
	}

	/**
	 * 判断单值项是否已经存在
	 *
	 * @param uniqueValue
	 * @return
	 */
	private boolean hasUnique(double uniqueValue) {
		boolean itemHasExist = false;
		for (int i = 0; i < this.themeUnique.getCount(); i++) {
			if (this.themeUnique.getItem(i).getUnique() - uniqueValue == 0.0) {
				itemHasExist = true;
			}
		}
		return itemHasExist;
	}

	/**
	 * 颜色方案改变时刷新颜色
	 */
	private void refreshColor() {
		if (comboboxColor != null) {
			int colorCount = ((Colors) comboboxColor.getSelectedItem()).getCount();
			Colors colors = (Colors) comboboxColor.getSelectedItem();
			int rangeCount = themeUnique.getCount();
			if (rangeCount > 0) {
				float ratio = (1f * colorCount) / (1f * rangeCount);
				themeUnique.getItem(0).setColor(colors.get(0));
				themeUnique.getItem(rangeCount - 1).setColor(colors.get(colorCount - 1));
				for (int i = 1; i < rangeCount - 1; i++) {
					int colorIndex = Math.round(i * ratio);
					if (colorIndex == colorCount) {
						colorIndex--;
					}
					themeUnique.getItem(i).setColor(colors.get(colorIndex));
				}
			}
		}
	}

	class LocalPopmenuListener implements PopupMenuListener {

		@Override
		public void popupMenuCanceled(PopupMenuEvent e) {
			getTable();
			if (isRefreshAtOnce) {
				firePropertyChange("ThemeChange", null, null);
				ThemeGuideFactory.refreshMapAndLayer(map, themeUniqueLayer.getName(), true);
				tableUniqueInfo.setRowSelectionInterval(0, 0);
			}
		}

		@Override
		public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
			// Do something

		}

		@Override
		public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
			// Do something

		}

	}

	/**
	 * 下拉项发生变化时的事件处理类
	 *
	 * @author Administrator
	 */
	class LocalComboBoxItemListener implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				if (e.getSource() == comboboxColor) {
					// 修改颜色方案
					int[] selectRows = null;
					if (tableUniqueInfo.getSelectedRows().length > 0) {
						selectRows = tableUniqueInfo.getSelectedRows();
					}
					refreshColor();
					getTable();
					if (null != selectRows) {
						tableUniqueInfo.setRowSelectionInterval(selectRows[0], selectRows[selectRows.length - 1]);
					} else {
						tableUniqueInfo.setRowSelectionInterval(0, 0);
					}
				}
				if (isRefreshAtOnce) {
					firePropertyChange("ThemeChange", null, null);
					ThemeGuideFactory.refreshMapAndLayer(map, themeUniqueLayer.getName(), true);
				}
			}
		}
	}

	class LocalTableModelListener implements TableModelListener {

		@Override
		public void tableChanged(TableModelEvent e) {
			try {
				int selectColumn = e.getColumn();
				int selectRow = e.getFirstRow();
				if (selectColumn == TABLE_COLUMN_UNIQUE && !StringUtilties.isNullOrEmptyString(tableUniqueInfo.getValueAt(selectRow, selectColumn))
						&& StringUtilties.isNumber(tableUniqueInfo.getValueAt(selectRow, selectColumn).toString())) {
					setUniqueItemUnique(Double.valueOf(tableUniqueInfo.getValueAt(selectRow, selectColumn).toString()));
					setUniqueItemCaption(tableUniqueInfo.getValueAt(selectRow, selectColumn).toString());
				}
				if (selectColumn == TABLE_COLUMN_CAPTION && !StringUtilties.isNullOrEmptyString(tableUniqueInfo.getValueAt(selectRow, selectColumn))) {
					String caption = tableUniqueInfo.getValueAt(selectRow, selectColumn).toString();
					setUniqueItemCaption(caption);
				}
				getTable();
				tableUniqueInfo.addRowSelectionInterval(selectRow, selectRow);
				if (isRefreshAtOnce) {
					firePropertyChange("ThemeChange", null, null);
					ThemeGuideFactory.refreshMapAndLayer(map, themeUniqueLayer.getName(), true);
				}
			} catch (Exception ex) {
				Application.getActiveApplication().getOutput().output(ex);
			}
		}

	}

	class LocalActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == buttonVisble) {
				// 批量修改单值段的可见状态
				setItemVisble();
			} else if (e.getSource() == buttonForeGroundColor) {
				// 批量修改单值段的颜色
				setItemColor(buttonForeGroundColor.getWidth(), buttonForeGroundColor.getHeight() - 60);
			} else if (e.getSource() == buttonAdd) {
				// 添加单值段
				addThemeItem();
			} else if (e.getSource() == buttonDelete) {
				// 删除单值段
				deleteItem();
				tableUniqueInfo.setRowSelectionInterval(0, 0);
			} else if (e.getSource() == buttonAntitone) {
				// 颜色方案反序
				setGeoStyleAntitone();
				tableUniqueInfo.setRowSelectionInterval(0, 0);
			}
			if (isRefreshAtOnce) {
				firePropertyChange("ThemeChange", null, null);
				ThemeGuideFactory.refreshMapAndLayer(map, themeUniqueLayer.getName(), true);
			}
		}

		/**
		 * 添加单值段
		 */
		private void addThemeItem() {
			addItemPanel = new AddItemPanel(ThemeType.GRIDUNIQUE);
			addItemPanel.setDataset(datasetGrid);
			addItemPanel.setThemeGridUnique(themeUnique);
			addItemPanel.setDeleteGridUniqueItems(deleteItems);
			addItemPanel.init();
			addItemPanel.addPopupMenuListener(popmenuListener);
			addItemPanel.show(buttonAdd, -addItemPanel.getWidth() / 2,
					buttonAdd.getHeight());
			addItemPanel.setVisible(true);
		}

		/**
		 * 判断单值项是否需要添加到未添加项中
		 *
		 * @param deleteItem
		 * @return
		 */
		private boolean isNeedAddToDeleteItems(ThemeGridUniqueItem deleteItem) {
			double deleteItemUnique = deleteItem.getUnique();
			ThemeGridUnique themeUniqueTemp = ThemeGridUnique.makeDefault(datasetGrid, ColorGradientType.YELLOWGREEN);
			for (int i = 0; i < themeUniqueTemp.getCount(); i++) {
				if (themeUniqueTemp.getItem(i).getUnique() - deleteItemUnique == 0.0) {
					return true;
				}
			}
			return false;
		}

		/**
		 * 删除单值段
		 */
		private void deleteItem() {
			int[] selectedRow = tableUniqueInfo.getSelectedRows();
			themeUnique = (ThemeGridUnique) themeUniqueLayer.getTheme();
			if (selectedRow[selectedRow.length - 1] != tableUniqueInfo.getRowCount() - 1 && selectedRow.length == 1) {
				ThemeGridUniqueItem item = themeUnique.getItem(selectedRow[0]);

				if (isNeedAddToDeleteItems(item)) {
					ThemeGridUniqueItem itemClone = new ThemeGridUniqueItem(item);
					deleteItems.add(itemClone);
				}

				themeUnique.remove(selectedRow[0]);

			} else if (selectedRow[selectedRow.length - 1] != tableUniqueInfo.getRowCount() - 1) {
				for (int i = selectedRow.length - 1; i >= 0; i--) {
					ThemeGridUniqueItem item = themeUnique.getItem(selectedRow[i]);
					if (isNeedAddToDeleteItems(item)) {
						ThemeGridUniqueItem itemClone = new ThemeGridUniqueItem(item);
						deleteItems.add(itemClone);
					}
					themeUnique.remove(selectedRow[i]);
				}
			} else if (selectedRow[selectedRow.length - 1] == tableUniqueInfo.getRowCount() - 1) {
				for (int i = selectedRow.length - 2; i >= 0; i--) {
					ThemeGridUniqueItem item = themeUnique.getItem(selectedRow[i]);
					if (isNeedAddToDeleteItems(item)) {
						ThemeGridUniqueItem itemClone = new ThemeGridUniqueItem(item);
						deleteItems.add(itemClone);
					}
					themeUnique.remove(selectedRow[i]);
				}
			}
			getTable();
			if (tableUniqueInfo.getRowCount() == 1) {
				buttonDelete.setEnabled(false);
			} else {
				buttonDelete.setEnabled(true);
			}
		}

		/**
		 * 设置颜色方案与当前颜色方案反序
		 */
		private void setGeoStyleAntitone() {
			themeUnique = (ThemeGridUnique) themeUniqueLayer.getTheme();
			themeUnique.reverseColor();
			getTable();
		}

		/**
		 * 设置单值项是否可见
		 */
		private void setItemVisble() {
			int[] selectedRow = tableUniqueInfo.getSelectedRows();
			// 有不可见的项就全部设置为不可见，全部不可见，或者全部可见就设置为相反状态
			if (hasInvisible(selectedRow) && !allItemInvisible(selectedRow)) {
				if (selectedRow[selectedRow.length - 1] != tableUniqueInfo.getRowCount() - 1) {
					for (int i = 0; i < selectedRow.length; i++) {
						((ThemeGridUnique) themeUniqueLayer.getTheme()).getItem(selectedRow[i]).setVisible(false);
					}
				} else {
					for (int i = 0; i < selectedRow.length - 1; i++) {
						((ThemeGridUnique) themeUniqueLayer.getTheme()).getItem(selectedRow[i]).setVisible(false);
					}
				}
			} else {
				for (int i = 0; i < selectedRow.length; i++) {
					resetVisible(selectedRow[i]);
				}
			}
			getTable();
			for (int i = 0; i < selectedRow.length; i++) {
				tableUniqueInfo.addRowSelectionInterval(selectedRow[i], selectedRow[i]);
			}
		}

		/**
		 * 判断选中项是否全部不可见
		 *
		 * @param selectedRows
		 * @return
		 */
		private boolean allItemInvisible(int[] selectedRows) {
			int count = 0;
			boolean allItemInvisible = false;
			int rowCounts = tableUniqueInfo.getRowCount() - 1;
			if (selectedRows[selectedRows.length - 1] != rowCounts) {
				for (int i = 0; i < selectedRows.length; i++) {
					if (!((ThemeGridUnique) themeUniqueLayer.getTheme()).getItem(selectedRows[i]).isVisible()) {
						count++;
					}
				}
			} else {
				for (int i = 0; i < selectedRows.length - 1; i++) {
					if (!((ThemeGridUnique) themeUniqueLayer.getTheme()).getItem(selectedRows[i]).isVisible()) {
						count++;
					}
				}
			}
			if (selectedRows[selectedRows.length - 1] != rowCounts && count == selectedRows.length) {
				allItemInvisible = true;
			} else if (selectedRows[selectedRows.length - 1] == rowCounts && count == selectedRows.length - 1) {
				allItemInvisible = true;
			}
			return allItemInvisible;
		}

		/**
		 * 判断选中项中是否存在不可见子项
		 *
		 * @param selectedRows
		 * @return
		 */
		private boolean hasInvisible(int[] selectedRows) {
			boolean hasInvisible = false;
			if (selectedRows[selectedRows.length - 1] != tableUniqueInfo.getRowCount() - 1) {
				for (int i = 0; i < selectedRows.length; i++) {
					if (!((ThemeGridUnique) themeUniqueLayer.getTheme()).getItem(selectedRows[i]).isVisible()) {
						hasInvisible = true;
					}
				}
			} else {
				for (int i = 0; i < selectedRows.length - 1; i++) {
					if (!((ThemeGridUnique) themeUniqueLayer.getTheme()).getItem(selectedRows[i]).isVisible()) {
						hasInvisible = true;
					}
				}
			}
			return hasInvisible;
		}

		/**
		 * 重置可见选项
		 *
		 * @param selectRow 要重置的行
		 */
		private void resetVisible(int selectRow) {
			if (selectRow != tableUniqueInfo.getRowCount() - 1) {
				boolean visible = ((ThemeGridUnique) themeUniqueLayer.getTheme()).getItem(selectRow).isVisible();
				if (visible) {
					((ThemeGridUnique) themeUniqueLayer.getTheme()).getItem(selectRow).setVisible(false);
					tableUniqueInfo.setValueAt(InternalImageIconFactory.INVISIBLE, selectRow, TABLE_COLUMN_VISIBLE);
				} else {
					((ThemeGridUnique) themeUniqueLayer.getTheme()).getItem(selectRow).setVisible(true);
					tableUniqueInfo.setValueAt(InternalImageIconFactory.VISIBLE, selectRow, TABLE_COLUMN_VISIBLE);
				}
			}
		}

	}

	/**
	 * 设置填充颜色
	 */
	private void setItemColor(int x, int y) {
		final JPopupMenu popupMenu = new JPopupMenu();
		ColorSelectionPanel colorSelectionPanel = new ColorSelectionPanel();
		popupMenu.add(colorSelectionPanel, BorderLayout.CENTER);
		colorSelectionPanel.setPreferredSize(new Dimension(170, 205));
		popupMenu.show(this.tableUniqueInfo, x, y);
		final int[] selectRows = tableUniqueInfo.getSelectedRows();
		colorSelectionPanel.addPropertyChangeListener("m_selectionColor", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				Color color = (Color) evt.getNewValue();
				if (selectRows.length > 0) {
					for (int i = 0; i < selectRows.length; i++) {
						int selectRow = selectRows[i];
						if (selectRow != tableUniqueInfo.getRowCount() - 1) {
							resetColor(selectRow, color);
						} else {
							themeUnique.setDefaultColor(color);
							ImageIcon nowGeoStyleIcon = ThemeItemLabelDecorator.buildColorIcon(datasetGrid, color);
							tableUniqueInfo.setValueAt(nowGeoStyleIcon, selectRow, TABLE_COLUMN_GEOSTYLE);
						}
					}
					getTable();
					for (int i = 0; i < selectRows.length; i++) {
						tableUniqueInfo.addRowSelectionInterval(selectRows[i], selectRows[i]);
					}
				}
				popupMenu.setVisible(false);
			}
		});
	}

	/**
	 * 重置选择项颜色
	 *
	 * @param selectRow 要重置颜色的行
	 * @param nowColor 新的颜色
	 */
	private void resetColor(int selectRow, Color nowColor) {
		ThemeGridUniqueItem item = this.themeUnique.getItem(selectRow);
		item.setColor(nowColor);
		ImageIcon nowGeoStyleIcon = ThemeItemLabelDecorator.buildColorIcon(this.datasetGrid, nowColor);
		this.tableUniqueInfo.setValueAt(nowGeoStyleIcon, selectRow, TABLE_COLUMN_GEOSTYLE);
	}

	/**
	 * 获取是否及时刷新
	 *
	 * @return
	 */
	public boolean isRfreshAtOnece() {
		return isRefreshAtOnce;
	}

	/**
	 * 设置及时刷新
	 *
	 * @param isRfreshAtOnece
	 */
	public void setRfreshAtOnece(boolean isRfreshAtOnece) {
		this.isRefreshAtOnce = isRfreshAtOnece;
	}

	/**
	 * 获取当前的单值专题图图层
	 *
	 * @return
	 */
	public Layer getThemeUniqueLayer() {
		return themeUniqueLayer;
	}

	/**
	 * 设置当前的单值专题图图层
	 *
	 * @param themeUniqueLayer
	 */
	public void setThemeUniqueLayer(Layer themeUniqueLayer) {
		this.themeUniqueLayer = themeUniqueLayer;
	}

	@Override
	public Theme getCurrentTheme() {
		return themeUnique;
	}
}
