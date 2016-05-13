package com.supermap.desktop.newtheme.themeLabel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.MessageFormat;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.supermap.data.DatasetVector;
import com.supermap.data.TextStyle;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.newtheme.commonPanel.TextStyleDialog;
import com.supermap.desktop.newtheme.commonPanel.ThemeChangePanel;
import com.supermap.desktop.newtheme.commonUtils.LimitedDmt;
import com.supermap.desktop.newtheme.commonUtils.ThemeGuideFactory;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.InternalImageIconFactory;
import com.supermap.desktop.utilties.MapUtilties;
import com.supermap.desktop.utilties.MathUtilties;
import com.supermap.desktop.utilties.StringUtilties;
import com.supermap.mapping.*;
import com.supermap.ui.MapControl;

/**
 * 标签复合风格专题图
 * 
 * @author xie
 *
 */
public class ThemeLabelComplicatedContainer extends ThemeChangePanel {

	private static final long serialVersionUID = 1L;
	private DatasetVector datasetVector;
	private ThemeLabel themeLabel;
	private Layer themeLabelLayer;
	private Map map;
	private MixedTextStyle mixedTextStyle = new MixedTextStyle();
	private JTabbedPane tabbedPane;
	private ThemeLabelPropertyPanel panelProperty;
	private JPanel panelStyle;
	private ThemeLabelAdvancePanel panelAdvance;

	private JLabel labelSeparatorMode;
	private JLabel labelSeparator;
	private JLabel labelSeparatNumber;
	private JLabel labelDefualtStyle;
	private JComboBox<String> comboBoxSeparatorMethod;// 标签分割方法
	private JTextField textFieldSeparator;// 分割符
	private JTextField textFieldSeparatNumber;// 分割段数
	private JButton buttonDefualtStyle;// 默认风格
	private JToolBar toolBar;// 工具条
	private JButton buttonMerge;// 合并
	private JButton buttonSplit;// 拆分
	private JButton buttonStyle;// 字体样式
	private JTable tableComplicated;
	private String[] nameStrings = { MapViewProperties.getString("String_ThemeLabelRangeItem"), MapViewProperties.getString("String_SplitRange") };
	protected TextStyleDialog textStyleDialog;

	private boolean isRefreshAtOnce;
	private String layerName;
	private ItemListener separatorListener = new SeparatorListener();
	private KeyListener separatorChangeListener = new SeparatorChangeListener();
	private KeyListener separatorCountListener = new SeparatorCountListener();
	private ActionListener buttonListener = new ButtonListener();
	private PropertyChangeListener propertyChangeListener = new LocalPropertyChangeListener();
	private FocusListener separatorCountFocusListener;

	public ThemeLabelComplicatedContainer(Layer layer) {
		this.themeLabelLayer = layer;
		this.datasetVector = (DatasetVector) layer.getDataset();
		this.themeLabel = new ThemeLabel((ThemeLabel) layer.getTheme());
		System.out.println("LayerName:" + layer.getName());
		System.out.println("Length:" + ((ThemeLabel) layer.getTheme()).getUniformMixedStyle().getStyles().length);

		mixStyleClone(themeLabel);
		this.layerName = layer.getName();
		this.map = ThemeGuideFactory.getMapControl().getMap();
		initComponents();
		initResources();
		registActionListener();
	}

	private void mixStyleClone(ThemeLabel themeLabel) {
		this.mixedTextStyle.setDefaultStyle(themeLabel.getUniformMixedStyle().getDefaultStyle());
		this.mixedTextStyle.setSeparator(themeLabel.getUniformMixedStyle().getSeparator());
		this.mixedTextStyle.setSeparatorEnabled(themeLabel.getUniformMixedStyle().isSeparatorEnabled());
		this.mixedTextStyle.setSplitIndexes(themeLabel.getUniformMixedStyle().getSplitIndexes());
		this.mixedTextStyle.setStyles(themeLabel.getUniformMixedStyle().getStyles());
	}

	private void initComponents() {
		this.setLayout(new GridBagLayout());
		this.tabbedPane = new JTabbedPane();
		this.panelProperty = new ThemeLabelPropertyPanel(themeLabelLayer);
		this.panelAdvance = new ThemeLabelAdvancePanel(themeLabelLayer);
		this.panelStyle = new JPanel();
		this.tabbedPane.add(MapViewProperties.getString("String_Theme_Property"), this.panelProperty);
		this.tabbedPane.add(MapViewProperties.getString("String_Theme_Style"), this.panelStyle);
		this.tabbedPane.add(MapViewProperties.getString("String_Theme_Advanced"), this.panelAdvance);
		this.tabbedPane.setSelectedIndex(1);
		this.add(this.tabbedPane, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH)
				.setWeight(1, 1));
		initPanelStyle();
	}

	private void initPanelStyle() {
		this.labelSeparatorMode = new JLabel();
		this.labelSeparator = new JLabel();
		this.labelSeparatNumber = new JLabel();
		this.labelDefualtStyle = new JLabel();
		this.comboBoxSeparatorMethod = new JComboBox<String>();
		this.textFieldSeparatNumber = new JTextField();
		this.textFieldSeparatNumber.setDocument(new LimitedDmt(9, true));
		this.textFieldSeparator = new JTextField();
		this.textFieldSeparator.setDocument(new LimitedDmt(1, false));
		this.buttonDefualtStyle = new JButton("...");
		JScrollPane scrollPane = new JScrollPane();
		this.tableComplicated = new JTable();
		initTextFieldSeparator();
		initTextFieldSeparatorCount();
		initComboBoxSeparatorMethod();
		initToolBar();
		//@formatter:off
		this.panelStyle.setLayout(new GridBagLayout());
		this.panelStyle.add(this.labelSeparatorMode,      new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 10, 5, 0).setWeight(20, 0).setIpad(40, 0));
		this.panelStyle.add(this.comboBoxSeparatorMethod, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 10, 5, 10).setWeight(60, 0).setFill(GridBagConstraints.HORIZONTAL));
		this.panelStyle.add(this.labelSeparator,          new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 0).setWeight(20, 0).setIpad(40, 0));
		this.panelStyle.add(this.textFieldSeparator,      new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 10).setWeight(60, 0).setFill(GridBagConstraints.HORIZONTAL));
		this.panelStyle.add(this.labelSeparatNumber,      new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 0).setWeight(20, 0).setIpad(40, 0));
		this.panelStyle.add(this.textFieldSeparatNumber,  new GridBagConstraintsHelper(1, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 10).setWeight(60, 0).setFill(GridBagConstraints.HORIZONTAL));
		this.panelStyle.add(this.labelDefualtStyle,       new GridBagConstraintsHelper(0, 3, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 0).setWeight(20, 0).setIpad(40, 0));
		this.panelStyle.add(this.buttonDefualtStyle,      new GridBagConstraintsHelper(1, 3, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 10).setWeight(60, 0).setFill(GridBagConstraints.HORIZONTAL));
		this.panelStyle.add(this.toolBar,                 new GridBagConstraintsHelper(0, 4, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 0).setWeight(100, 0).setIpad(40, 0));
		this.panelStyle.add(scrollPane,                   new GridBagConstraintsHelper(0, 5, 2, 1).setAnchor(GridBagConstraints.NORTH).setInsets(0, 10, 5, 10).setWeight(100, 3).setFill(GridBagConstraints.BOTH));
		scrollPane.setViewportView(this.tableComplicated);
		getTable();
		this.tableComplicated.setRowSelectionInterval(0, 0);
		resetToolBarButtonState();
		//@formatter:on
	}

	private void initTextFieldSeparatorCount() {
		this.textFieldSeparatNumber.setText(String.valueOf(mixedTextStyle.getSplitIndexes().length + 1));
	}

	private void initTextFieldSeparator() {
		this.textFieldSeparator.setText(mixedTextStyle.getSeparator());
	}

	private void initComboBoxSeparatorMethod() {
		this.comboBoxSeparatorMethod.setModel(new DefaultComboBoxModel<String>(new String[] { MapViewProperties.getString("String_SplitWithSeparator"),
				MapViewProperties.getString("String_SplitWithPosition") }));
		if (mixedTextStyle.isSeparatorEnabled()) {
			this.comboBoxSeparatorMethod.setSelectedIndex(0);
			this.textFieldSeparator.setEnabled(true);

		} else {
			this.comboBoxSeparatorMethod.setSelectedIndex(1);
			this.textFieldSeparator.setEnabled(false);
		}
	}

	/**
	 * 初始化工具条
	 */
	private void initToolBar() {
		this.toolBar = new JToolBar();
		this.buttonMerge = new JButton();
		this.buttonSplit = new JButton();
		this.buttonStyle = new JButton();
		this.toolBar.setFloatable(false);
		this.toolBar.add(this.buttonMerge);
		this.toolBar.add(this.buttonSplit);
		this.toolBar.addSeparator();
		this.toolBar.add(this.buttonStyle);
		this.toolBar.addSeparator();
		this.buttonMerge.setIcon(InternalImageIconFactory.Merge);
		this.buttonSplit.setIcon(InternalImageIconFactory.Split);
		this.buttonStyle.setIcon(InternalImageIconFactory.STYLE);
	}

	private void initResources() {
		this.toolBar.setFloatable(false);
		this.labelSeparatorMode.setText(MapViewProperties.getString("String_labelSplitMethod"));
		this.labelSeparatNumber.setText(MapViewProperties.getString("String_labelSplitCount"));
		this.labelSeparator.setText(MapViewProperties.getString("String_labelSeparator"));
		this.labelDefualtStyle.setText(MapViewProperties.getString("String_DefaultTextStyle"));
	}


	@Override
	public Theme getCurrentTheme() {
		return themeLabel;
	}

	@Override
	public Layer getCurrentLayer() {
		return themeLabelLayer;
	}

	@Override
	public void setCurrentLayer(Layer layer) {
		this.themeLabelLayer = layer;
	}

	@Override
	public void registActionListener() {
		this.separatorCountFocusListener = new FocusAdapter() {

			@Override
			public void focusLost(FocusEvent e) {
				setSeparatorCount();
			}
		};

		this.panelProperty.addPropertyChangeListener("ThemeChange", this.propertyChangeListener);
		this.panelAdvance.addPropertyChangeListener("ThemeChange", this.propertyChangeListener);
		this.comboBoxSeparatorMethod.addItemListener(this.separatorListener);
		this.textFieldSeparator.addKeyListener(this.separatorChangeListener);
		this.textFieldSeparatNumber.addKeyListener(this.separatorCountListener);
		this.textFieldSeparatNumber.addFocusListener(this.separatorCountFocusListener);
		this.buttonDefualtStyle.addActionListener(this.buttonListener);
		this.buttonMerge.addActionListener(this.buttonListener);
		this.buttonSplit.addActionListener(this.buttonListener);
		this.buttonStyle.addActionListener(this.buttonListener);
		this.tableComplicated.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseReleased(MouseEvent e) {
				resetToolBarButtonState();
			}

		});
	}

	@Override
	public void unregistActionListener() {

	}

	class SeparatorChangeListener extends KeyAdapter {
		@Override
		public void keyReleased(KeyEvent e) {
			String separator = textFieldSeparator.getText();
			mixedTextStyle.setSeparator(separator);
			refreshAtOnce();
		}
	}

	class SeparatorListener implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				int separator = comboBoxSeparatorMethod.getSelectedIndex();
				switch (separator) {
				case 0:
					mixedTextStyle.setSeparatorEnabled(true);
					textFieldSeparator.setEnabled(true);
					getTable();
					refreshAtOnce();
					break;
				case 1:
					mixedTextStyle.setSeparatorEnabled(false);
					textFieldSeparator.setEnabled(false);
					getTable();
					refreshAtOnce();
				default:
					break;
				}
			}
		}

	}

	class SeparatorCountListener extends KeyAdapter {
		@Override
		public void keyReleased(KeyEvent e) {
			char keyChar = e.getKeyChar();
			if (keyChar == KeyEvent.VK_ENTER) {
				setSeparatorCount();
			}
		}
	}

	class LocalPropertyChangeListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			ThemeLabelComplicatedContainer.this.firePropertyChange("ThemeChange", null, null);
			map = ThemeGuideFactory.getMapControl().getMap();
			map.refresh();
		}
	}

	private void resetToolBarButtonState() {
		int[] selectRows = tableComplicated.getSelectedRows();
		int tableRows = tableComplicated.getRowCount();
		// 选中连续的选项时可以合并
		this.buttonMerge.setEnabled(MathUtilties.isContinuouslyArray(selectRows));
		// 1.只能选中一条数据 2.选中第一项时判断第一项时若区间为min<x<1时不能拆分
		// 3.选中非第一项且不是最后一项时，区间差不能小于1可以拆分。4.选中最后一项默认添加2可以拆分
		if (selectRows.length == 1) {
			int split = 0;
			if (selectRows[0] == 0) {
				split = mixedTextStyle.getSplitIndexes()[0];
				this.buttonSplit.setEnabled(1 != split);
			}
			if (selectRows[0] == tableRows - 1) {
				this.buttonSplit.setEnabled(true);
			}
			if (selectRows[0] != tableRows - 1 && selectRows[0] != 0) {
				split = mixedTextStyle.getSplitIndexes()[selectRows[0]];
				int forward = mixedTextStyle.getSplitIndexes()[selectRows[0] - 1];
				this.buttonSplit.setEnabled(split - forward > 1);
			}
		} else {
			this.buttonSplit.setEnabled(false);
		}
	}

	class ButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == buttonDefualtStyle) {
				setDefualtStyle();
				return;
			}
			if (e.getSource() == buttonMerge) {
				mergeSeparators();
				return;
			}
			if (e.getSource() == buttonSplit) {
				splitSeparators();
				return;
			}
			if (e.getSource() == buttonStyle) {
				setItemStyle();
				return;
			}
		}

		private void splitSeparators() {
			// 拆分项
			// 当选择第一项时
			int selectedRow = tableComplicated.getSelectedRow();
			int[] newSplits = new int[mixedTextStyle.getSplitIndexes().length + 1];
			TextStyle[] newTextStyle = new TextStyle[newSplits.length + 1];
			int[] tempSplits = mixedTextStyle.getSplitIndexes();
			TextStyle[] tempTextStyles = mixedTextStyle.getStyles();
			if (selectedRow == 0) {
				for (int i = 0; i < newSplits.length; i++) {
					if (i == 0) {
						newSplits[0] = tempSplits[0] / 2;// 插入索引
					} else {
						newSplits[i] = tempSplits[i - 1];// 复制其他索引
					}
				}
				for (int i = 1; i < newTextStyle.length; i++) {
					newTextStyle[i] = tempTextStyles[i - 1];// 复制其他风格
				}
				newTextStyle[0] = tempTextStyles[0];// 新风格
				resetSplits(newSplits, newTextStyle);
				return;
			}
			if (selectedRow == tableComplicated.getRowCount() - 1) {
				for (int i = 0; i < newSplits.length; i++) {
					if (i == selectedRow) {
						newSplits[i] = tempSplits[i - 1] + 2;
					} else {
						newSplits[i] = tempSplits[i];
					}
				}
				for (int i = 0; i < newSplits.length; i++) {
					newTextStyle[i] = tempTextStyles[i];
				}
				newTextStyle[newTextStyle.length - 1] = tempTextStyles[tempTextStyles.length - 1];
				resetSplits(newSplits, newTextStyle);
				return;
			}
			if (selectedRow != 0 && selectedRow != tableComplicated.getRowCount() - 1) {
				for (int i = 0; i < newSplits.length; i++) {
					if (i == selectedRow) {
						newSplits[i] = (tempSplits[i] + tempSplits[i - 1]) / 2;
					}
					if (i < selectedRow) {
						newSplits[i] = tempSplits[i];
					}
					if (i > selectedRow) {
						newSplits[i] = tempSplits[i - 1];
					}
				}
				for (int i = 0; i < newTextStyle.length; i++) {
					if (i <= selectedRow) {
						newTextStyle[i] = tempTextStyles[i];
					}
					if (i > selectedRow) {
						newTextStyle[i] = tempTextStyles[i - 1];
					}
				}
				resetSplits(newSplits, newTextStyle);
				return;
			}
		}

		private void resetSplits(int[] newSplits, TextStyle[] newTextStyle) {
			mixedTextStyle.setSplitIndexes(newSplits);
			mixedTextStyle.setStyles(newTextStyle);
			textFieldSeparatNumber.setText(String.valueOf(newSplits.length + 1));
			getTable();
			refreshAtOnce();
			tableComplicated.setRowSelectionInterval(0, 0);
			resetToolBarButtonState();
		}

		private void mergeSeparators() {
			// 合并项
			int[] selectRows = tableComplicated.getSelectedRows();
			int splitCount = mixedTextStyle.getSplitIndexes().length + 1;
			int[] newSplits = new int[splitCount - selectRows.length];
			TextStyle[] newTextStyles = new TextStyle[newSplits.length];

			for (int i = 0; i < newSplits.length; i++) {
				if (selectRows[0] > 0 && i < selectRows[0]) {
					newTextStyles[i] = mixedTextStyle.getStyles()[i];
					newSplits[i] = mixedTextStyle.getSplitIndexes()[i];
				} else {
					newSplits[i] = mixedTextStyle.getSplitIndexes()[i + selectRows.length - 1];
					newTextStyles[i] = mixedTextStyle.getStyles()[i + selectRows.length - 1];
				}
			}
			mixedTextStyle.setSplitIndexes(newSplits);
			mixedTextStyle.setStyles(newTextStyles);
			textFieldSeparatNumber.setText(String.valueOf(newSplits.length + 1));
			getTable();
			refreshAtOnce();
			tableComplicated.setRowSelectionInterval(0, 0);
			resetToolBarButtonState();
		}

		private void setDefualtStyle() {
			// 设置默认样式
			int width = buttonDefualtStyle.getWidth();
			int height = buttonDefualtStyle.getHeight();
			int x = (int) (buttonDefualtStyle.getLocationOnScreen().x - 0.6 * width);
			int y = buttonDefualtStyle.getLocationOnScreen().y + height;
			textStyleDialog = new TextStyleDialog(mixedTextStyle.getDefaultStyle(), map, themeLabelLayer);
			textStyleDialog.setMixedTextStyle(mixedTextStyle);
			setTextStyle(x, y, 2);
		}
	}

	protected void setTextStyle(int x, int y, int styleType) {
		textStyleDialog.getTextStyleContainer().setTextStyleType(styleType);
		textStyleDialog.getTextStyleContainer().addPropertyChangeListener("ThemeChange", new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				firePropertyChange("ThemeChange", null, null);
			}
		});
		textStyleDialog.setRefreshAtOnce(isRefreshAtOnce);
		textStyleDialog.setLocation(x, y);
		textStyleDialog.setVisible(true);
	}

	private void setSeparatorCount() {
		String separatNumber = textFieldSeparatNumber.getText();
		if (!StringUtilties.isNullOrEmptyString(separatNumber)) {
			int sparatorCount = Integer.parseInt(separatNumber);
			int[] splits = new int[sparatorCount - 1];
			splits[0] = 1;
			for (int i = 1; i < sparatorCount - 1; i++) {
				splits[i] = splits[i - 1] + 2;
			}
			mixedTextStyle.setSplitIndexes(splits);
			TextStyle[] tempTextStyles = new TextStyle[sparatorCount];
			for (int j = 0; j < sparatorCount; j++) {
				if (j <= mixedTextStyle.getStyles().length - 1) {
					tempTextStyles[j] = mixedTextStyle.getStyles()[j];
				} else {
					tempTextStyles[j] = mixedTextStyle.getStyles()[mixedTextStyle.getStyles().length - 1];
				}
			}
			mixedTextStyle.setStyles(tempTextStyles);
			getTable();
			refreshAtOnce();
		}
	}

	private void setItemStyle() {
		// 设置选中样式
		int[] selectRows = tableComplicated.getSelectedRows();
		int height = buttonStyle.getHeight();
		int x = (int) (buttonStyle.getLocationOnScreen().x - 20);
		int y = buttonStyle.getLocationOnScreen().y + height;
		textStyleDialog = new TextStyleDialog(mixedTextStyle.getStyles()[selectRows[selectRows.length - 1]].clone(), map, themeLabelLayer);
		textStyleDialog.setMixedTextStyle(mixedTextStyle);
		textStyleDialog.setRows(selectRows);
		setTextStyle(x, y, 3);
	}

	/**
	 * 表格初始化
	 *
	 * @return m_table
	 */
	private JTable getTable() {
		int textStyleLength = mixedTextStyle.getSplitIndexes().length;
		DefaultTableModel defaultTableModel = new DefaultTableModel(new Object[textStyleLength + 1][2], nameStrings) {
			@Override
			public Class getColumnClass(int column) {
				return String.class;
			}

			@Override
			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return columnIndex == 1;
			}
		};
		this.tableComplicated.setModel(defaultTableModel);
		initColumn();
		this.tableComplicated.setRowHeight(20);
		this.tableComplicated.getColumnModel().getColumn(0).setCellRenderer(new TableColorCellRenderer(mixedTextStyle.getStyles()));
		return this.tableComplicated;
	}

	private void initColumn() {
		int[] splits = mixedTextStyle.getSplitIndexes();
		int splitsCount = splits.length + 1;
		for (int i = 0; i < splitsCount; i++) {
			this.tableComplicated.setValueAt(MessageFormat.format(MapViewProperties.getString("String_SplitInfo"), i + 1), i, 0);
			if (!mixedTextStyle.isSeparatorEnabled() && splits.length > 0) {
				String rangeInfo = "";
				if (0 == i && splits.length > 0) {
					rangeInfo = "min < x < " + splits[i];
				}
				if (i == splitsCount - 1) {
					rangeInfo = splits[i - 1] + " =< x < max";
				}
				if (0 != i && i != splitsCount - 1) {
					rangeInfo = splits[i - 1] + " =< x < " + splits[i];
				}
				this.tableComplicated.setValueAt(rangeInfo, i, 1);
			}
		}

	}

	@Override
	public void setRefreshAtOnce(boolean isRefreshAtOnce) {
		this.isRefreshAtOnce = isRefreshAtOnce;
		this.panelProperty.setRefreshAtOnce(isRefreshAtOnce);
		this.panelAdvance.setRefreshAtOnce(isRefreshAtOnce);
	}

	private void refreshAtOnce() {
		firePropertyChange("ThemeChange", null, null);
		if (isRefreshAtOnce) {
			refreshMapAndLayer();
		}
	}

	@Override
	public void refreshMapAndLayer() {
		this.panelAdvance.refreshMapAndLayer();
		this.panelProperty.refreshMapAndLayer();
		if (null != ThemeGuideFactory.getMapControl()) {
			this.map = ThemeGuideFactory.getMapControl().getMap();
		}
		this.themeLabelLayer = MapUtilties.findLayerByName(map, layerName);
		if (null != themeLabelLayer && null != themeLabelLayer.getTheme() && themeLabelLayer.getTheme().getType() == ThemeType.LABEL) {
			((ThemeLabel) this.themeLabelLayer.getTheme()).setUniformMixedStyle(this.mixedTextStyle);
			this.map.refresh();
		}
	}

	public ThemeLabelPropertyPanel getPanelProperty() {
		return panelProperty;
	}

	public ThemeLabelAdvancePanel getPanelAdvance() {
		return panelAdvance;
	}

}
