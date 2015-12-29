package com.supermap.desktop.newtheme;

import com.supermap.data.ColorGradientType;
import com.supermap.data.Colors;
import com.supermap.data.DatasetGrid;
import com.supermap.desktop.Application;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.ColorSelectionPanel;
import com.supermap.desktop.ui.controls.ColorsComboBox;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.InternalImageIconFactory;
import com.supermap.desktop.utilties.MathUtilties;
import com.supermap.desktop.utilties.StringUtilties;
import com.supermap.mapping.*;
import com.supermap.ui.MapControl;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.text.MessageFormat;

public class ThemeGridRangeContainer extends ThemeChangePanel {
	private static final long serialVersionUID = 1L;
	private static final int TABLE_COLUMN_VISIBLE = 0;
	private static final int TABLE_COLUMN_GEOSTYLE = 1;
	private static final int TABLE_COLUMN_RANGEVALUE = 2;
	private static final int TABLE_COLUMN_CAPTION = 3;

	private JTabbedPane tabbedPaneInfo = new JTabbedPane();
	private JPanel panelProperty = new JPanel();
	private JLabel labelRangeMethod = new JLabel();
	private JComboBox<String> comboBoxRangeMethod = new JComboBox<String>();
	private JLabel labelRangeCount = new JLabel();
	private JComboBox<String> comboBoxRangeCount = new JComboBox<String>();
	private JLabel labelRangeLength = new JLabel();
	private JSpinner spinnerRangeLength = new JSpinner();
	private JLabel labelRangeFormat = new JLabel();
	private JComboBox<String> comboBoxRangeFormat = new JComboBox<String>();
	private JLabel labelColorStyle = new JLabel();
	private ColorsComboBox comboBoxColorStyle = new ColorsComboBox();
	private JToolBar toolBar = new JToolBar();
	private JButton buttonMerge = new JButton();
	private JButton buttonSplit = new JButton();
	private JButton buttonVisible = new JButton();
	private JButton buttonForeGroundColor = new JButton();
	private JScrollPane scrollPane = new JScrollPane();
	private JTable tableRangeInfo = new JTable();

	private static String[] nameStrings = {MapViewProperties.getString("String_Title_Visible"), MapViewProperties.getString("String_Title_Sytle"),
			MapViewProperties.getString("String_Title_RangeValue"), MapViewProperties.getString("String_ThemeGraphTextFormat_Caption")};
	private transient DatasetGrid datasetGrid;
	private transient Map map;
	private transient ThemeGridRange themeGridRange;
	private transient Layer themeRangeLayer;
	private transient RangeMode rangeMode = RangeMode.EQUALINTERVAL;
	private transient int rangeCount = 5;
	private String captiontype = "";
	private boolean isRefreshAtOnce = true;
	private boolean isCustom = false;
	private boolean isNewTheme = false;
	private boolean isMergeOrSplit = false;

	private transient LocalActionListener actionListener = new LocalActionListener();
	private transient LocalMouseListener mouseListener = new LocalMouseListener();
	private transient LocalComboBoxItemListener itemListener = new LocalComboBoxItemListener();
	private transient LocalSpinnerChangeListener changeListener = new LocalSpinnerChangeListener();
	private transient LocalTableModelListener tableModelListener = new LocalTableModelListener();
	private transient LocalDefualTableModel tableModel;

	public ThemeGridRangeContainer(DatasetGrid datasetGrid, ThemeGridRange themeGridRange) {
		this.datasetGrid = datasetGrid;
		this.themeGridRange = themeGridRange;
		this.map = initCurrentTheme(datasetGrid);
		this.isNewTheme = true;
		initComponents();
		initResources();
		registActionListener();
	}

	/**
	 * @wbp.parser.constructor
	 */
	public ThemeGridRangeContainer(Layer layer) {
		this.themeRangeLayer = layer;
		this.datasetGrid = (DatasetGrid) layer.getDataset();
		this.themeGridRange = (ThemeGridRange) layer.getTheme();
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
			this.themeRangeLayer = mapControl.getMap().getLayers().add(datasetGrid, themeGridRange, true);
			this.themeGridRange = (ThemeGridRange) themeRangeLayer.getTheme();
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
		initPanelProperty();
		this.comboBoxColorStyle.setSelectedIndex(21);
		if (isNewTheme) {
			refreshColor();
		}
	}

	/**
	 * 初始化属性界面
	 */
	private void initPanelProperty() {
		//@formatter:off
		initToolBar();
		initComboBoxRangMethod();
		initComboBoxRangeCount();
		initComboBoxRangeFormat();
		this.panelProperty.setLayout(new GridBagLayout());
		this.panelProperty.add(this.labelRangeMethod,    new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(2,10,2,10).setWeight(1, 0));
		this.panelProperty.add(this.comboBoxRangeMethod, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(2,20,2,10).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL));
		this.panelProperty.add(this.labelRangeCount,     new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(2,10,2,10).setWeight(1, 0));
		this.panelProperty.add(this.comboBoxRangeCount,  new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(2,20,2,10).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL));
		this.panelProperty.add(this.labelRangeLength,    new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(2,10,2,10).setWeight(1, 0));
		spinnerRangeLength.setModel(new SpinnerNumberModel(new Double(0), null, null, new Double(1)));
		this.spinnerRangeLength.setEnabled(false);
		this.panelProperty.add(this.spinnerRangeLength,  new GridBagConstraintsHelper(1, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(2,20,2,10).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL));
		this.panelProperty.add(this.labelRangeFormat,      new GridBagConstraintsHelper(0, 3, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(2,10,2,10).setWeight(1, 0));
		this.panelProperty.add(this.comboBoxRangeFormat,   new GridBagConstraintsHelper(1, 3, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(2,20,2,10).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL));
		this.panelProperty.add(this.labelColorStyle,       new GridBagConstraintsHelper(0, 4, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(2,10,2,10).setWeight(1, 0));
		this.panelProperty.add(this.comboBoxColorStyle,    new GridBagConstraintsHelper(1, 4, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(2,20,2,10).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL));
		this.panelProperty.add(this.toolBar,               new GridBagConstraintsHelper(0, 5, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(2).setWeight(1, 0));
		this.panelProperty.add(this.scrollPane,            new GridBagConstraintsHelper(0, 6, 2, 1).setAnchor(GridBagConstraints.NORTH).setInsets(2).setWeight(1, 3).setFill(GridBagConstraints.BOTH));
		getTable();
		this.tableRangeInfo.setRowSelectionInterval(0, 0);
		
		this.scrollPane.setViewportView(tableRangeInfo);
		//@formatter:on
	}

	/**
	 * 初始化分段方法项
	 */
	private void initComboBoxRangMethod() {
		this.comboBoxRangeMethod.setModel(new DefaultComboBoxModel<String>(new String[]{MapViewProperties.getString("String_RangeMode_EqualInterval"),
				MapViewProperties.getString("String_RangeMode_SquareRoot"),
				MapViewProperties.getString("String_RangeMode_Logarithm"),
				MapViewProperties.getString("String_RangeMode_CustomInterval") }));
		this.comboBoxRangeMethod.setEditable(true);
		if (themeGridRange.getRangeMode() == RangeMode.NONE) {
			this.comboBoxRangeMethod.setSelectedIndex(0);
		} else if (themeGridRange.getRangeMode() == RangeMode.SQUAREROOT) {
			this.comboBoxRangeMethod.setSelectedIndex(1);
		} else if (themeGridRange.getRangeMode() == RangeMode.LOGARITHM) {
			this.comboBoxRangeMethod.setSelectedIndex(2);
		} else if (themeGridRange.getRangeMode() == RangeMode.CUSTOMINTERVAL) {
			this.comboBoxRangeMethod.setSelectedIndex(3);
		}
	}

	/**
	 * 初始化段数
	 */
	private void initComboBoxRangeCount() {
		this.comboBoxRangeCount.setModel(new DefaultComboBoxModel<String>(new String[]{"2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14",
				"15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32" }));
		this.comboBoxRangeCount.setEditable(true);
		int rangeCountNumber = themeGridRange.getCount();
		this.comboBoxRangeCount.setSelectedItem(String.valueOf(rangeCountNumber));
	}

	/**
	 * 初始化段标题格式
	 */
	private void initComboBoxRangeFormat() {
		this.comboBoxRangeFormat.setModel(new DefaultComboBoxModel<String>(new String[] { "0-100", "0<=x<100" }));
		this.comboBoxRangeFormat.setSelectedIndex(1);
	}

	/*
	 * 资源化
	 */
	private void initResources() {
		this.labelRangeMethod.setText(MapViewProperties.getString("String_Label_RangeMethed"));
		this.labelRangeCount.setText(MapViewProperties.getString("String_Label_RangeCount"));
		this.labelRangeLength.setText(MapViewProperties.getString("String_Label_RangeSize"));
		this.labelRangeFormat.setText(MapViewProperties.getString("String_Label_CaptionFormat"));
		this.labelColorStyle.setText(MapViewProperties.getString("String_Label_ColorScheme"));
		this.buttonMerge.setEnabled(false);
		this.buttonMerge.setToolTipText(MapViewProperties.getString("String_Title_Merge"));
		this.buttonSplit.setToolTipText(MapViewProperties.getString("String_Title_Split"));
		this.buttonForeGroundColor.setToolTipText(MapViewProperties.getString("String_Title_Sytle"));
		this.buttonVisible.setToolTipText(MapViewProperties.getString("String_Title_Visible"));
	}

	/**
	 * 表格初始化
	 *
	 * @return m_table
	 */
	private JTable getTable() {
		this.rangeCount = this.themeGridRange.getCount();
		tableModel = new LocalDefualTableModel(new Object[rangeCount][4], nameStrings);
		this.tableRangeInfo.setModel(tableModel);
		initColumnIcon();
		this.tableRangeInfo.setRowHeight(20);

		TableColumn visibleColumn = this.tableRangeInfo.getColumn(MapViewProperties.getString("String_Title_Visible"));
		TableColumn viewColumn = this.tableRangeInfo.getColumn(MapViewProperties.getString("String_Title_Sytle"));
		TableColumn rangeValueColumn = this.tableRangeInfo.getColumn(MapViewProperties.getString("String_Title_RangeValue"));
		visibleColumn.setMaxWidth(40);
		viewColumn.setMaxWidth(100);
		rangeValueColumn.setMaxWidth(200);
		this.tableRangeInfo.getModel().removeTableModelListener(this.tableModelListener);
		this.tableRangeInfo.getModel().addTableModelListener(this.tableModelListener);
		return this.tableRangeInfo;
	}

	/**
	 * 填充图片和字段
	 */
	private void initColumnIcon() {
		for (int i = 0; i < this.rangeCount; i++) {
			ThemeGridRangeItem gridRangeItem = this.themeGridRange.getItem(i);
			boolean isVisible = gridRangeItem.isVisible();
			ImageIcon visibleIcon = InternalImageIconFactory.VISIBLE;
			if (!isVisible) {
				visibleIcon = InternalImageIconFactory.INVISIBLE;
			}
			this.tableRangeInfo.setValueAt(visibleIcon, i, TABLE_COLUMN_VISIBLE);
			Color geoStyle = gridRangeItem.getColor();
			this.tableRangeInfo.setValueAt(ThemeItemLabelDecorator.buildColorIcon(datasetGrid, geoStyle), i, TABLE_COLUMN_GEOSTYLE);
			if (i == rangeCount - 1) {
				this.tableRangeInfo.setValueAt("Max", i, TABLE_COLUMN_RANGEVALUE);
			} else {
				DecimalFormat format = new DecimalFormat("#.######");
				String itemEnd = format.format(gridRangeItem.getEnd());
				this.tableRangeInfo.setValueAt(itemEnd, i, TABLE_COLUMN_RANGEVALUE);
			}

			String caption = gridRangeItem.getCaption();
			if (this.captiontype.contains("-")) {
				caption = caption.replaceAll("<= X <", "-");
				caption = caption.replaceAll("< X <", "-");
			} else if (this.captiontype.contains("<") && !caption.contains("X")) {
				caption = caption.replaceAll("-", "<= X <");
			}
			gridRangeItem.setCaption(caption);
			this.tableRangeInfo.setValueAt(gridRangeItem.getCaption(), i, TABLE_COLUMN_CAPTION);
		}
	}

	/**
	 * 颜色方案改变时刷新颜色
	 */
	private void refreshColor() {
		if (comboBoxColorStyle != null) {
			int colorCount = ((Colors) comboBoxColorStyle.getSelectedItem()).getCount();
			Colors colors = (Colors) comboBoxColorStyle.getSelectedItem();
			int themeRangeCount = themeGridRange.getCount();
			if (themeRangeCount > 0) {
				float ratio = (1f * colorCount) / (1f * themeRangeCount);
				themeGridRange.getItem(0).setColor(colors.get(0));
				themeGridRange.getItem(themeRangeCount - 1).setColor(colors.get(colorCount - 1));
				for (int i = 1; i < themeRangeCount - 1; i++) {
					int colorIndex = Math.round(i * ratio);
					if (colorIndex == colorCount) {
						colorIndex--;
					}
					themeGridRange.getItem(i).setColor(colors.get(colorIndex));
				}
			}
		}
	}

	/**
	 * 初始化工具条
	 */
	private void initToolBar() {
		this.toolBar.add(this.buttonMerge);
		this.toolBar.add(this.buttonSplit);
		this.toolBar.addSeparator();
		this.toolBar.add(this.buttonVisible);
		this.toolBar.add(this.buttonForeGroundColor);
		this.toolBar.addSeparator();
		this.buttonMerge.setIcon(InternalImageIconFactory.Merge);
		this.buttonSplit.setIcon(InternalImageIconFactory.Split);
		this.buttonForeGroundColor.setIcon(InternalImageIconFactory.FOREGROUND_COLOR);
		this.buttonVisible.setIcon(InternalImageIconFactory.VISIBLE);
	}

	/**
	 * 注册事件
	 */
	void registActionListener() {
		unregistActionListener();
		this.buttonVisible.addActionListener(this.actionListener);
		this.buttonForeGroundColor.addActionListener(this.actionListener);
		this.buttonMerge.addActionListener(this.actionListener);
		this.buttonSplit.addActionListener(this.actionListener);
		this.tableRangeInfo.addMouseListener(this.mouseListener);
		this.comboBoxColorStyle.addItemListener(this.itemListener);
		this.comboBoxRangeCount.addItemListener(this.itemListener);
		this.comboBoxRangeCount.getComponent(0).addMouseListener(this.mouseListener);
		this.comboBoxRangeMethod.addItemListener(this.itemListener);
		this.comboBoxRangeFormat.addItemListener(this.itemListener);
		this.spinnerRangeLength.addChangeListener(this.changeListener);
		this.tableRangeInfo.putClientProperty("terminateEditOnFocusLost", true);
		this.tableRangeInfo.getModel().addTableModelListener(this.tableModelListener);
	}

	/**
	 * 注销事件
	 */
	public void unregistActionListener() {
		this.buttonVisible.removeActionListener(this.actionListener);
		this.buttonForeGroundColor.removeActionListener(this.actionListener);
		this.buttonMerge.removeActionListener(this.actionListener);
		this.buttonSplit.removeActionListener(this.actionListener);
		this.tableRangeInfo.removeMouseListener(this.mouseListener);
		this.comboBoxColorStyle.removeItemListener(this.itemListener);
		this.comboBoxRangeCount.removeItemListener(this.itemListener);
		this.comboBoxRangeCount.getComponent(0).removeMouseListener(this.mouseListener);
		this.comboBoxRangeMethod.removeItemListener(this.itemListener);
		this.comboBoxRangeFormat.removeItemListener(this.itemListener);
		this.spinnerRangeLength.removeChangeListener(this.changeListener);
		this.tableRangeInfo.getModel().removeTableModelListener(this.tableModelListener);
	}

	/**
	 * 批量设置文本风格
	 */
	private void setItemColor(int x, int y) {
		final JPopupMenu popupMenu = new JPopupMenu();
		ColorSelectionPanel colorSelectionPanel = new ColorSelectionPanel();
		popupMenu.add(colorSelectionPanel, BorderLayout.CENTER);
		colorSelectionPanel.setPreferredSize(new Dimension(170, 205));
		popupMenu.show(this.tableRangeInfo, x, y);
		final int[] selectRows = tableRangeInfo.getSelectedRows();
		colorSelectionPanel.addPropertyChangeListener("m_selectionColor", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				Color color = (Color) evt.getNewValue();
				if (selectRows.length > 0) {
					for (int i = 0; i < selectRows.length; i++) {
						int selectRow = selectRows[i];
						resetColor(selectRow, color);
					}
				}
				popupMenu.setVisible(false);
			}
		});
		getTable();
		for (int i = 0; i < selectRows.length; i++) {
			tableRangeInfo.addRowSelectionInterval(selectRows[i], selectRows[i]);
		}
	}

	/**
	 * 重置选择项颜色
	 *
	 * @param selectRow 要重置颜色的行
	 * @param nowColor 新的颜色
	 */
	private void resetColor(int selectRow, Color nowColor) {
		ThemeGridRangeItem item = this.themeGridRange.getItem(selectRow);
		item.setColor(nowColor);
		ImageIcon nowGeoStyleIcon = ThemeItemLabelDecorator.buildColorIcon(this.datasetGrid, nowColor);
		this.tableRangeInfo.setValueAt(nowGeoStyleIcon, selectRow, TABLE_COLUMN_GEOSTYLE);
	}

	class LocalActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			int[] selectRows = tableRangeInfo.getSelectedRows();
			if (e.getSource() == buttonMerge) {
				if (selectRows.length == tableRangeInfo.getRowCount()) {
					UICommonToolkit.showMessageDialog(MapViewProperties.getString("String_Warning_RquiredTwoFieldForRange"));
				} else {
					// 合并选中项
					mergeItem();
				}
			} else if (e.getSource() == buttonSplit) {
				// 拆分选中项
				splitItem();
			} else if (e.getSource() == buttonVisible) {
				// 批量修改分段的可见状态
				setItemVisble();
			} else if (e.getSource() == buttonForeGroundColor) {
				// 批量修改单值段的符号方案
				setItemColor(buttonForeGroundColor.getWidth(), buttonForeGroundColor.getHeight() - 60);
			}
			if (isRefreshAtOnce) {
				firePropertyChange("ThemeChange", null, null);
				ThemeGuideFactory.refreshMapAndLayer(map, themeRangeLayer.getName(), true);
			}
		}

		/**
		 * 拆分
		 */
		private void splitItem() {
			int selectRow = tableRangeInfo.getSelectedRow();
			if (selectRow >= 0) {
				ThemeGridRangeItem item = themeGridRange.getItem(selectRow);
				double splitValue = (item.getEnd() + item.getStart()) / 2;
				if (selectRow == 0) {
					// 第零条数据的拆分中值
					splitValue = (item.getEnd() + ((int) item.getEnd()) - 1) / 2;
				}
				if (selectRow == tableRangeInfo.getRowCount() - 1) {
					// 最后一条的拆分中值
					splitValue = (item.getStart() + ((int) item.getStart()) + 1) / 2;
				}
				String diff = new DecimalFormat("#.####").format(item.getEnd() - item.getStart());
				// 首尾项不同时才能进行拆分
				if (!"0.0001".equals(diff)) {
					String startCaption = MessageFormat.format(MapViewProperties.getString("String_RangeFormat"), String.valueOf(item.getStart()),
							String.valueOf(splitValue));
					String endCaption = MessageFormat.format(MapViewProperties.getString("String_RangeFormat"), String.valueOf(splitValue),
							String.valueOf(item.getEnd()));
					themeGridRange.split(selectRow, splitValue, item.getColor(), startCaption, item.getColor(), endCaption);
					isMergeOrSplit = true;
					rangeCount = themeGridRange.getCount();
					comboBoxRangeCount.setSelectedItem(String.valueOf(rangeCount));
					getTable();
				}
				tableRangeInfo.addRowSelectionInterval(selectRow, selectRow);
			}
		}

		/**
		 * 合并项
		 */
		private void mergeItem() {
			int[] selectedRows = tableRangeInfo.getSelectedRows();
			int startIndex = selectedRows[0];
			int endIndex = selectedRows[selectedRows.length - 1];
			ThemeGridRangeItem startItem = themeGridRange.getItem(startIndex);
			ThemeGridRangeItem endItem = themeGridRange.getItem(endIndex);
			String caption = MessageFormat.format(MapViewProperties.getString("String_RangeFormat"), String.valueOf(startItem.getStart()),
					String.valueOf(endItem.getEnd()));
			themeGridRange.merge(startIndex, selectedRows.length, startItem.getColor(), caption);
			isMergeOrSplit = true;
			getTable();
			rangeCount = themeGridRange.getCount();
			tableRangeInfo.setRowSelectionInterval(selectedRows[0], selectedRows[0]);
			comboBoxRangeCount.setSelectedItem(String.valueOf(rangeCount));
			buttonMerge.setEnabled(false);
			buttonSplit.setEnabled(true);
		}

		/**
		 * 设置分段项是否可见
		 */
		private void setItemVisble() {
			int[] selectedRow = tableRangeInfo.getSelectedRows();
			// 有不可见的项就全部设置为不可见，全部不可见，或者全部可见就设置为相反状态
			if (hasInvisible(selectedRow) && !allItemInvisible(selectedRow)) {
				for (int i = 0; i < selectedRow.length; i++) {
					((ThemeGridRange) themeRangeLayer.getTheme()).getItem(selectedRow[i]).setVisible(false);
				}
			} else {
				for (int i = 0; i < selectedRow.length; i++) {
					resetVisible(selectedRow[i]);
				}
			}
			getTable();
			for (int i = 0; i < selectedRow.length; i++) {
				tableRangeInfo.addRowSelectionInterval(selectedRow[i], selectedRow[i]);
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
			for (int i = 0; i < selectedRows.length; i++) {
				if (!((ThemeGridRange) themeRangeLayer.getTheme()).getItem(selectedRows[i]).isVisible()) {
					count++;
				}
			}
			if (count == selectedRows.length) {
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
			for (int i = 0; i < selectedRows.length; i++) {
				if (!((ThemeGridRange) themeRangeLayer.getTheme()).getItem(selectedRows[i]).isVisible()) {
					hasInvisible = true;
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
			ThemeGridRangeItem tempThemeRangeItem = ((ThemeGridRange) themeRangeLayer.getTheme()).getItem(selectRow);
			boolean visible = tempThemeRangeItem.isVisible();
			if (visible) {
				tempThemeRangeItem.setVisible(false);
				tableRangeInfo.setValueAt(InternalImageIconFactory.INVISIBLE, selectRow, TABLE_COLUMN_VISIBLE);
			} else {
				tempThemeRangeItem.setVisible(true);
				tableRangeInfo.setValueAt(InternalImageIconFactory.VISIBLE, selectRow, TABLE_COLUMN_VISIBLE);
			}
		}

	}

	class LocalMouseListener extends MouseAdapter {
		@Override
		public void mouseReleased(MouseEvent e) {
			int[] selectedRows = tableRangeInfo.getSelectedRows();
			if (selectedRows.length == 1) {
				buttonMerge.setEnabled(false);
				buttonSplit.setEnabled(true);
			} else if (selectedRows.length >= 2) {
				buttonSplit.setEnabled(false);
			}

			if (selectedRows.length >= 2 && MathUtilties.isContiuityArray(selectedRows)) {
				buttonMerge.setEnabled(true);
			} else {
				buttonMerge.setEnabled(false);
			}
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getSource() == tableRangeInfo && 1 == e.getClickCount() && tableRangeInfo.getSelectedColumn() == TABLE_COLUMN_VISIBLE
					&& tableRangeInfo.getSelectedRows().length == 1) {
				int selectRow = tableRangeInfo.getSelectedRow();
				ThemeGridRangeItem item = themeGridRange.getItem(selectRow);
				boolean isVisible = item.isVisible();
				if (isVisible) {
					item.setVisible(false);
					tableRangeInfo.setValueAt(InternalImageIconFactory.INVISIBLE, selectRow, TABLE_COLUMN_VISIBLE);
				} else {
					item.setVisible(true);
					tableRangeInfo.setValueAt(InternalImageIconFactory.VISIBLE, selectRow, TABLE_COLUMN_VISIBLE);
				}
				if (isRefreshAtOnce) {
					firePropertyChange("ThemeChange", null, null);
					ThemeGuideFactory.refreshMapAndLayer(map, themeRangeLayer.getName(), true);
				}
			} else if (e.getSource() == tableRangeInfo && 2 == e.getClickCount() && tableRangeInfo.getSelectedColumn() == TABLE_COLUMN_GEOSTYLE) {
				setItemColor(e.getX(), e.getY());
				if (isRefreshAtOnce) {
					firePropertyChange("ThemeChange", null, null);
					ThemeGuideFactory.refreshMapAndLayer(map, themeRangeLayer.getName(), true);
				}
			}
			if (e.getSource() == comboBoxRangeCount.getComponent(0)) {
				isMergeOrSplit = false;
			}
		}
	}

	class LocalComboBoxItemListener implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				if (e.getSource() == comboBoxColorStyle) {
					// 修改颜色方案
					refreshColor();
					getTable();
				} else if (e.getSource() == comboBoxRangeCount && !isCustom && !isMergeOrSplit) {
					// 修改段数
					setRangeCount();
				} else if (e.getSource() == comboBoxRangeMethod) {
					// 设置分段方法
					setRangeMethod();
				} else if (e.getSource() == comboBoxRangeFormat) {
					// 设置标题格式
					setRangeFormat();
				}
				if (isRefreshAtOnce) {
					firePropertyChange("ThemeChange", null, null);
					ThemeGuideFactory.refreshMapAndLayer(map, themeRangeLayer.getName(), true);
					tableRangeInfo.setRowSelectionInterval(0, 0);
				}
			}
		}

		/**
		 * 设置标题格式
		 */
		private void setRangeFormat() {
			int count = comboBoxRangeFormat.getSelectedIndex();
			if (0 == count) {
				captiontype = "-";
			} else {
				captiontype = "<=x<";
			}
			getTable();
		}

		private void setRangeCount() {
			rangeCount = Integer.valueOf(comboBoxRangeCount.getSelectedItem().toString());
			resetThemeInfo();
		}

		/**
		 * 设置分段方法
		 */
		private void setRangeMethod() {
			String rangeMethod = comboBoxRangeMethod.getSelectedItem().toString();
			double minValue = datasetGrid.getGridStatisticsResult().getMinValue();
			if (rangeMethod.equals(MapViewProperties.getString("String_RangeMode_EqualInterval"))) {
				// 等距分段
				rangeMode = RangeMode.EQUALINTERVAL;
				comboBoxRangeCount.setEnabled(true);
				spinnerRangeLength.setEnabled(false);
				isCustom = false;
				resetThemeInfo();
			} else if (rangeMethod.equals(MapViewProperties.getString("String_RangeMode_SquareRoot"))) {
				if (Double.compare(minValue, 0) < 0) {
					// 有负数且为平方根分段
					JOptionPane.showMessageDialog(
							null,
							MapViewProperties.getString("String_UnMakeGridRangeThemeSquareRoot"),
							"",
							JOptionPane.ERROR_MESSAGE);
					resetComboBoxRangeMode();
					return;
				} else {
					rangeMode = RangeMode.SQUAREROOT;
					comboBoxRangeCount.setEnabled(true);
					spinnerRangeLength.setEnabled(false);
					isCustom = false;
					resetThemeInfo();
				}
			} else if (rangeMethod.equals(MapViewProperties.getString("String_RangeMode_Logarithm"))) {
				if (Double.compare(minValue, 0) < 0) {
					// 有负数且为对数分段
					JOptionPane.showMessageDialog(
							null,
							MapViewProperties.getString("String_UnMakeGridRangeTheme"),
							"",
							JOptionPane.ERROR_MESSAGE);
					resetComboBoxRangeMode();
					return;
				} else {
					rangeMode = RangeMode.LOGARITHM;
					comboBoxRangeCount.setEnabled(true);
					spinnerRangeLength.setEnabled(false);
					isCustom = false;
					resetThemeInfo();
				}
			}
			if (rangeMethod.equals(MapViewProperties.getString("String_RangeMode_CustomInterval"))) {
				// 自定义分段
				rangeMode = RangeMode.CUSTOMINTERVAL;
				double defaultRangeCount = 0;
				if (themeGridRange.getCount() > 2) {
					defaultRangeCount = Double
							.valueOf(new DecimalFormat("0").format(themeGridRange.getItem(1).getEnd() - themeGridRange.getItem(1).getStart()));
				} else {
					defaultRangeCount = Double.valueOf(new DecimalFormat("0").format(themeGridRange.getItem(0).getEnd()));
				}
				spinnerRangeLength.setValue(defaultRangeCount);
				comboBoxRangeCount.setEnabled(false);
				spinnerRangeLength.setEnabled(true);
				makeDefaultAsCustom();
			}
		}

		private void resetComboBoxRangeMode() {
			if (rangeMode.equals(RangeMode.EQUALINTERVAL)) {
				comboBoxRangeMethod.setSelectedIndex(0);
				return;
			}
			if (rangeMode.equals(RangeMode.SQUAREROOT)) {
				comboBoxRangeMethod.setSelectedIndex(1);
				return;
			}
			if (rangeMode.equals(RangeMode.LOGARITHM)) {
				comboBoxRangeMethod.setSelectedIndex(2);
				return;
			}
			if (rangeMode.equals(RangeMode.CUSTOMINTERVAL)) {
				comboBoxRangeMethod.setSelectedIndex(3);
				return;
			}
		}

		/**
		 * 重建专题图
		 */
		private void resetThemeInfo() {
			if (rangeCount < 2 || rangeCount > 32) {
				// 段数小于2，或者段数大于最大值
				comboBoxRangeCount.setSelectedItem(String.valueOf(themeGridRange.getCount()));
			} else {
				ThemeGridRange theme = ThemeGridRange.makeDefault(datasetGrid, rangeMode, rangeCount, ColorGradientType.GREENRED);
				if (null == theme) {
					// 专题图为空，提示专题图更新失败
					JOptionPane.showMessageDialog(null, MapViewProperties.getString("String_Theme_UpdataFailed"), CommonProperties.getString("String_Error"),
							JOptionPane.ERROR_MESSAGE);
				} else {
					refreshThemeRange(theme);
				}
			}
		}
	}

	/**
	 * 判断段值是否合法
	 *
	 * @return
	 */
	public boolean isRightRangeValue(String rangeValue, int selectRow) {
		boolean isRightValue = false;
		double range = Double.parseDouble(rangeValue);
		if (selectRow == 0) {
			double nextValue = themeGridRange.getItem(selectRow + 1).getEnd();
			if (nextValue - range > 0.0) {
				isRightValue = true;
			}
		} else if (selectRow != tableRangeInfo.getRowCount() - 1) {
			double prewValue = themeGridRange.getItem(selectRow - 1).getEnd();
			double nextValue = themeGridRange.getItem(selectRow + 1).getEnd();
			if (nextValue - range > 0.0 && range - prewValue > 0.0) {
				isRightValue = true;
			}
		}
		return isRightValue;
	}

	class LocalTableModelListener implements TableModelListener {

		@Override
		public void tableChanged(TableModelEvent arg0) {
			int selectRow = arg0.getFirstRow();
			int selectColumn = arg0.getColumn();
			try {
				if (selectColumn == TABLE_COLUMN_RANGEVALUE && !StringUtilties.isNullOrEmptyString(tableRangeInfo.getValueAt(selectRow, selectColumn))) {
					String rangeValue = tableRangeInfo.getValueAt(selectRow, selectColumn).toString();
					if ((StringUtilties.isNumber(rangeValue) && isRightRangeValue(rangeValue, selectRow))) {
						// 如果输入为数值且段值合法时修改段值
						themeGridRange.getItem(selectRow).setEnd(Double.valueOf(rangeValue));
						String endStr = String.valueOf(themeGridRange.getItem(selectRow).getEnd());
						String caption = themeGridRange.getItem(selectRow).getCaption();
						caption = caption.replace(caption.substring(caption.lastIndexOf("<") + 1, caption.length()), endStr);
						themeGridRange.getItem(selectRow).setCaption(caption);
						if (selectRow != themeGridRange.getCount() - 1) {
							String nextCaption = themeGridRange.getItem(selectRow + 1).getCaption();
							nextCaption = nextCaption.replace(nextCaption.substring(0, nextCaption.indexOf("<")), endStr);
							themeGridRange.getItem(selectRow + 1).setCaption(nextCaption);
						}
					}
				} else if (selectColumn == TABLE_COLUMN_CAPTION && !StringUtilties.isNullOrEmptyString(tableRangeInfo.getValueAt(selectRow, selectColumn))) {
					String caption = tableRangeInfo.getValueAt(selectRow, selectColumn).toString();
					themeGridRange.getItem(selectRow).setCaption(caption);
				}
				getTable();
				tableRangeInfo.addRowSelectionInterval(selectRow, selectRow);
				if (isRefreshAtOnce) {
					firePropertyChange("ThemeChange", null, null);
					ThemeGuideFactory.refreshMapAndLayer(map, themeRangeLayer.getName(), true);
				}
			} catch (Exception e) {
				Application.getActiveApplication().getOutput().output(e);
			}
		}

	}

	/**
	 * 刷新theme
	 *
	 * @param theme
	 */
	private void refreshThemeRange(ThemeGridRange theme) {
		try {
			((ThemeGridRange) themeRangeLayer.getTheme()).clear();
			if (0 < theme.getCount()) {
				for (int i = 0; i < theme.getCount(); i++) {
					((ThemeGridRange) themeRangeLayer.getTheme()).addToTail(theme.getItem(i), true);
				}
			}
			this.themeGridRange = (ThemeGridRange) themeRangeLayer.getTheme();
			refreshColor();
			getTable();
			if (2 <= themeGridRange.getCount()) {
				this.rangeCount = this.themeGridRange.getCount();
				this.comboBoxRangeCount.setSelectedItem(String.valueOf(rangeCount));
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	/**
	 * 创建自定义的分段专题图
	 */
	private void makeDefaultAsCustom() {
		double rangeLength = (double) spinnerRangeLength.getValue();
		if (rangeLength > 0) {
			ThemeGridRange theme = ThemeGridRange.makeDefault(datasetGrid, rangeMode, rangeLength, ColorGradientType.GREENRED);
			if (null == theme || theme.getCount() == 0) {
				// 专题图为空，提示专题图更新失败
				JOptionPane.showMessageDialog(null, MapViewProperties.getString("String_Theme_UpdataFailed"), CommonProperties.getString("String_Error"),
						JOptionPane.ERROR_MESSAGE);
			} else {
				this.isCustom = true;
				refreshThemeRange(theme);
			}
		}
	}

	class LocalSpinnerChangeListener implements ChangeListener {

		@Override
		public void stateChanged(ChangeEvent e) {
			makeDefaultAsCustom();
			if (isRefreshAtOnce) {
				firePropertyChange("ThemeChange", null, null);
				ThemeGuideFactory.refreshMapAndLayer(map, themeRangeLayer.getName(), true);
			}
		}

	}

	class LocalDefualTableModel extends DefaultTableModel {
		private static final long serialVersionUID = 1L;

		public LocalDefualTableModel(Object[][] obj, String[] name) {
			super(obj, name);
		}

		@Override
		public Class getColumnClass(int column) {// 要这样定义table，要重写这个方法0，0的意思就是别的格子的类型都跟0,0的一样。
			if (TABLE_COLUMN_VISIBLE == column || TABLE_COLUMN_GEOSTYLE == column) {
				return getValueAt(0, 0).getClass();
			}
			return String.class;
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			if (columnIndex == TABLE_COLUMN_RANGEVALUE || columnIndex == TABLE_COLUMN_CAPTION) {
				return true;
			}
			return false;
		}
	}

	/**
	 * 获取是否及时刷新值
	 *
	 * @return
	 */
	public boolean isRefreshAtOnece() {
		return isRefreshAtOnce;
	}

	/**
	 * 设置是否及时刷新
	 *
	 * @param isRefreshAtOnece
	 */
	public void setRefreshAtOnece(boolean isRefreshAtOnece) {
		this.isRefreshAtOnce = isRefreshAtOnece;
	}

	/**
	 * 获取分段专题图图层
	 *
	 * @return
	 */
	public Layer getThemeRangeLayer() {
		return themeRangeLayer;
	}

	/**
	 * 设置分段专题图图层
	 *
	 * @param themeRangeLayer
	 */
	public void setThemeRangeLayer(Layer themeRangeLayer) {
		this.themeRangeLayer = themeRangeLayer;
	}

	@Override
	public Theme getCurrentTheme() {
		return themeGridRange;
	}

}
