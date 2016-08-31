package com.supermap.desktop.newtheme.themeLabel;

import com.supermap.data.ColorGradientType;
import com.supermap.data.Colors;
import com.supermap.data.Dataset;
import com.supermap.data.DatasetVector;
import com.supermap.data.TextStyle;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.colorScheme.ColorsComboBox;
import com.supermap.desktop.enums.UnitValue;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.newtheme.commonPanel.TextStyleDialog;
import com.supermap.desktop.newtheme.commonPanel.ThemeChangePanel;
import com.supermap.desktop.newtheme.commonUtils.ThemeGuideFactory;
import com.supermap.desktop.newtheme.commonUtils.ThemeUtil;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.InternalImageIconFactory;
import com.supermap.desktop.ui.controls.LayersTree;
import com.supermap.desktop.utilities.MapUtilities;
import com.supermap.desktop.utilities.MathUtilities;
import com.supermap.desktop.utilities.StringUtilities;
import com.supermap.mapping.Layer;
import com.supermap.mapping.Map;
import com.supermap.mapping.RangeMode;
import com.supermap.mapping.Theme;
import com.supermap.mapping.ThemeLabel;
import com.supermap.mapping.ThemeLabelItem;
import com.supermap.mapping.ThemeType;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 标签分段风格专题图
 *
 * @author xie
 */
public class ThemeLabelRangeContainer extends ThemeChangePanel {

	private static final long serialVersionUID = 1L;
	private transient ThemeLabel themeLabel;
	private transient Layer themeLabelLayer;
	private transient ThemeLabelPropertyPanel panelProperty;
	private transient ThemeLabelAdvancePanel panelAdvance;
	private JTabbedPane tabbedPane = new JTabbedPane();
	private JPanel panelStyle = new JPanel();

	private JLabel labelExpression = new JLabel();
	private JComboBox<String> comboBoxExpression = new JComboBox<String>();
	private JLabel labelRangeMethod = new JLabel();
	private JComboBox<String> comboBoxRangeMethod = new JComboBox<String>();
	private JLabel labelRangeCount = new JLabel();
	private JComboBox<String> comboBoxRangeCount = new JComboBox<String>();
	private JLabel labelRangeLength = new JLabel();
	private JSpinner spinnerRangeLength = new JSpinner();

	private JLabel labelRangeFormat = new JLabel();
	private JComboBox<String> comboBoxRangeFormat = new JComboBox<String>();
	private JLabel labelColorStyle = new JLabel();
	private ColorsComboBox comboBoxColorStyle = new ColorsComboBox(ThemeType.RANGE);
	private JToolBar toolBar = new JToolBar();
	private JButton buttonMerge = new JButton();
	private JButton buttonSplit = new JButton();
	private JButton buttonVisible = new JButton();
	private JButton buttonStyle = new JButton();
	private JScrollPane scrollPane = new JScrollPane();
	private JTable tableLabelInfo = new JTable();
	private transient TextStyleDialog textStyleDialog;
	private ArrayList<String> comboBoxArray = new ArrayList<String>();

	private static String[] nameStrings = { MapViewProperties.getString("String_Title_Visible"), MapViewProperties.getString("String_Title_RangeValue"),
			MapViewProperties.getString("String_ThemeGraphTextFormat_Caption") };
	private transient DatasetVector datasetVector;
	private transient Map map;
	private String rangeExpression;
	private transient RangeMode rangeMode = RangeMode.EQUALINTERVAL;
	private int labelCount = 5;
	private String captiontype = "<=X<";
	private boolean isRefreshAtOnce = true;
	private boolean isCustom = false;
	private boolean isMergeOrSplit = false;
	private boolean isResetComboBox = false;
	private LayersTree layersTree = UICommonToolkit.getLayersManager().getLayersTree();
	private String layerName;
	private boolean isNewTheme;

	private static final int TABLE_COLUMN_VISIBLE = 0;
	private static final int TABLE_COLUMN_RANGEVALUE = 1;
	private static final int TABLE_COLUMN_CAPTION = 2;

	private transient LocalActionListener actionListener = new LocalActionListener();
	private transient LocalMouseListener mouseListener = new LocalMouseListener();
	private transient LocalComboBoxItemListener itemListener = new LocalComboBoxItemListener();
	private transient LocalSpinnerChangeListener changeListener = new LocalSpinnerChangeListener();
	private transient LocalTableModelListener tableModelListener = new LocalTableModelListener();
	private transient LocalPropertyChangeListener propertyChangeListener = new LocalPropertyChangeListener();
	private PropertyChangeListener layersTreePropertyChangeListener = new LayerChangeListener();
	private MouseAdapter mouseAdapter = new MouseAdapter() {
		@Override
		public void mouseReleased(MouseEvent e) {
			initComboBoxRangeExpression();
		}
	};
	private ItemListener unityListener;

	public ThemeLabelRangeContainer(Layer layer, boolean isNewTheme) {
		this.themeLabelLayer = layer;
		this.isNewTheme = isNewTheme;
		this.layerName = this.themeLabelLayer.getName();
		this.datasetVector = (DatasetVector) layer.getDataset();
		this.themeLabel = new ThemeLabel((ThemeLabel) layer.getTheme());
		this.map = ThemeGuideFactory.getMapControl().getMap();
		initComponents();
		initResources();
		registActionListener();
	}

	/**
	 * 界面布局入口
	 */
	private void initComponents() {
		this.setLayout(new GridBagLayout());
		this.panelProperty = new ThemeLabelPropertyPanel(themeLabelLayer);
		this.panelAdvance = new ThemeLabelAdvancePanel(themeLabelLayer);
		this.tabbedPane.add(MapViewProperties.getString("String_Theme_Property"), this.panelProperty);
		this.tabbedPane.add(MapViewProperties.getString("String_Theme_Style"), this.panelStyle);
		this.tabbedPane.add(MapViewProperties.getString("String_Theme_Advanced"), this.panelAdvance);
		this.tabbedPane.setSelectedIndex(1);
		this.add(this.tabbedPane, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH)
				.setWeight(1, 1));
		initPanelStyle();
		if (isNewTheme) {
			refreshColor();
			refreshAtOnce();
		}
	}

	/**
	 * 初始化属性界面
	 */
	private void initPanelStyle() {
		//@formatter:off
		initToolBar();
		initComboBoxRangeExpression();
		initComboBoxRangMethod();
		initComboBoxRangeCount();
		initComboBoxRangeFormat();
		this.comboBoxExpression.setEditable(true);
		this.spinnerRangeLength.setModel(new SpinnerNumberModel(new Double(0), null, null, new Double(1)));
		this.spinnerRangeLength.setEnabled(false);
		this.panelStyle.setLayout(new GridBagLayout());
		this.panelStyle.add(this.labelExpression,     new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 10, 5, 0).setWeight(20, 0).setIpad(50, 0));
		this.panelStyle.add(this.comboBoxExpression,  new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 10, 5, 10).setWeight(60, 0).setFill(GridBagConstraints.HORIZONTAL));
		this.panelStyle.add(this.labelRangeMethod,    new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 0).setWeight(20, 0).setIpad(50, 0));
		this.panelStyle.add(this.comboBoxRangeMethod, new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 10).setWeight(60, 0).setFill(GridBagConstraints.HORIZONTAL));
		this.panelStyle.add(this.labelRangeCount,     new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 0).setWeight(20, 0).setIpad(50, 0));
		this.panelStyle.add(this.comboBoxRangeCount,  new GridBagConstraintsHelper(1, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 10).setWeight(60, 0).setFill(GridBagConstraints.HORIZONTAL));
		this.panelStyle.add(this.labelRangeLength,    new GridBagConstraintsHelper(0, 3, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 0).setWeight(20, 0).setIpad(50, 0));
		this.panelStyle.add(this.spinnerRangeLength,  new GridBagConstraintsHelper(1, 3, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 10).setWeight(60, 0).setFill(GridBagConstraints.HORIZONTAL));
		this.panelStyle.add(this.labelRangeFormat,    new GridBagConstraintsHelper(0, 4, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 0).setWeight(20, 0).setIpad(50, 0));
		this.panelStyle.add(this.comboBoxRangeFormat, new GridBagConstraintsHelper(1, 4, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 10).setWeight(60, 0).setFill(GridBagConstraints.HORIZONTAL));
		this.panelStyle.add(this.labelColorStyle,     new GridBagConstraintsHelper(0, 5, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 0).setWeight(20, 0).setIpad(50, 0));
		this.panelStyle.add(this.comboBoxColorStyle,  new GridBagConstraintsHelper(1, 5, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 10).setWeight(60, 0).setFill(GridBagConstraints.HORIZONTAL));
		this.panelStyle.add(this.toolBar,             new GridBagConstraintsHelper(0, 6, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 5).setWeight(100, 0).setIpad(50, 0));
		this.panelStyle.add(this.scrollPane,          new GridBagConstraintsHelper(0, 7, 2, 1).setAnchor(GridBagConstraints.NORTH).setInsets(0, 10, 5, 10).setWeight(100, 3).setFill(GridBagConstraints.BOTH));
		getTable();
		this.tableLabelInfo.addRowSelectionInterval(0, 0);
		this.scrollPane.setViewportView(tableLabelInfo);
		//@formatter:on
	}

	/**
	 * 初始化表达式
	 */
	private void initComboBoxRangeExpression() {
		ThemeUtil.initComboBox(comboBoxExpression, themeLabel.getRangeExpression(), datasetVector, this.themeLabelLayer.getDisplayFilter().getJoinItems(),
				comboBoxArray, true, false);
	}

	/**
	 * 初始化分段方法项
	 */
	private void initComboBoxRangMethod() {
		this.comboBoxRangeMethod.setModel(new DefaultComboBoxModel<String>(new String[] { MapViewProperties.getString("String_RangeMode_EqualInterval"),
				MapViewProperties.getString("String_RangeMode_SquareRoot"), MapViewProperties.getString("String_RangeMode_StdDeviation"),
				MapViewProperties.getString("String_RangeMode_Logarithm"), MapViewProperties.getString("String_RangeMode_Quantile"),
				MapViewProperties.getString("String_RangeMode_CustomInterval") }));
		if (themeLabel.getRangeMode() == RangeMode.NONE) {
			this.comboBoxRangeMethod.setSelectedIndex(0);
		} else if (themeLabel.getRangeMode() == RangeMode.SQUAREROOT) {
			this.comboBoxRangeMethod.setSelectedIndex(1);
		} else if (themeLabel.getRangeMode() == RangeMode.STDDEVIATION) {
			this.comboBoxRangeMethod.setSelectedIndex(2);
		} else if (themeLabel.getRangeMode() == RangeMode.LOGARITHM) {
			this.comboBoxRangeMethod.setSelectedIndex(3);
		} else if (themeLabel.getRangeMode() == RangeMode.QUANTILE) {
			this.comboBoxRangeMethod.setSelectedIndex(4);
		} else if (themeLabel.getRangeMode() == RangeMode.CUSTOMINTERVAL) {
			this.comboBoxRangeMethod.setSelectedIndex(5);
		}
	}

	/**
	 * 初始化段数
	 */
	private void initComboBoxRangeCount() {
		this.comboBoxRangeCount.setModel(new DefaultComboBoxModel<String>(new String[] { "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14",
				"15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32" }));
		this.comboBoxRangeCount.setEditable(true);
		int rangeCount = this.themeLabel.getCount();
		this.comboBoxRangeCount.setSelectedItem(String.valueOf(rangeCount));

	}

	/**
	 * 初始化段标题格式
	 */
	private void initComboBoxRangeFormat() {
		this.comboBoxRangeFormat.setModel(new DefaultComboBoxModel<String>(new String[] { "0-100", "0<=x<100" }));
		if (this.themeLabel.getItem(0).getCaption().contains("X")) {
			this.comboBoxRangeFormat.setSelectedIndex(1);
		} else {
			this.comboBoxRangeFormat.setSelectedIndex(0);
		}
	}

	/**
	 * 资源化
	 */
	private void initResources() {
		this.labelExpression.setText(MapViewProperties.getString("String_label_Expression"));
		this.labelRangeMethod.setText(MapViewProperties.getString("String_Label_RangeMethed"));
		this.labelRangeCount.setText(MapViewProperties.getString("String_Label_RangeCount"));
		this.labelRangeLength.setText(MapViewProperties.getString("String_Label_RangeSize"));
		this.labelRangeFormat.setText(MapViewProperties.getString("String_Label_CaptionFormat"));
		this.labelColorStyle.setText(MapViewProperties.getString("String_Label_ColorScheme"));
		this.buttonMerge.setEnabled(false);
		this.buttonMerge.setToolTipText(MapViewProperties.getString("String_Title_Merge"));
		this.buttonSplit.setToolTipText(MapViewProperties.getString("String_Title_Split"));
		this.buttonStyle.setToolTipText(MapViewProperties.getString("String_Title_Sytle"));
		this.buttonVisible.setToolTipText(MapViewProperties.getString("String_Title_Visible"));
	}

	/**
	 * 表格初始化
	 *
	 * @return m_table
	 */
	private JTable getTable() {
		this.labelCount = this.themeLabel.getCount();
		DefaultTableModel defaultTableModel = new DefaultTableModel(new Object[this.labelCount][3], nameStrings) {
			@Override
			public Class getColumnClass(int column) {// 要这样定义table，要重写这个方法0，0的意思就是别的格子的类型都跟0,0的一样。
				if (TABLE_COLUMN_VISIBLE == column) {
					return getValueAt(0, 0).getClass();
				}
				return String.class;
			}

			@Override
			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return columnIndex == TABLE_COLUMN_RANGEVALUE || columnIndex == TABLE_COLUMN_CAPTION;
			}
		};
		this.tableLabelInfo.setModel(defaultTableModel);
		// this.tableLabelInfo.setRowSelectionInterval(0, 0);
		this.tableLabelInfo.setRowHeight(20);
		TableColumn visibleColumn = this.tableLabelInfo.getColumn(MapViewProperties.getString("String_Title_Visible"));
		visibleColumn.setMaxWidth(40);
		List<ThemeLabelItem> items = initColumnIcon();
		this.tableLabelInfo.getColumnModel().getColumn(2).setCellRenderer(new TableColorCellRenderer(items, true));
		this.tableLabelInfo.getModel().removeTableModelListener(this.tableModelListener);
		this.tableLabelInfo.getModel().addTableModelListener(this.tableModelListener);
		return this.tableLabelInfo;
	}

	/**
	 * 填充图片和字段
	 */
	private List<ThemeLabelItem> initColumnIcon() {
		List<ThemeLabelItem> result = new ArrayList<ThemeLabelItem>();
		for (int i = 0; i < this.labelCount; i++) {
			ThemeLabelItem rangeItem = this.themeLabel.getItem(i);
			result.add(rangeItem);
			boolean isVisible = rangeItem.isVisible();
			ImageIcon visibleIcon = InternalImageIconFactory.VISIBLE;
			if (!isVisible) {
				visibleIcon = InternalImageIconFactory.INVISIBLE;
			}
			this.tableLabelInfo.setValueAt(visibleIcon, i, TABLE_COLUMN_VISIBLE);
			if (i == labelCount - 1) {
				this.tableLabelInfo.setValueAt("Max", i, TABLE_COLUMN_RANGEVALUE);
			} else {
				DecimalFormat format = new DecimalFormat("#.######");
				this.tableLabelInfo.setValueAt(format.format(rangeItem.getEnd()), i, TABLE_COLUMN_RANGEVALUE);
			}

			String caption = rangeItem.getCaption();
			if (this.captiontype.contains("-")) {
				caption = caption.replaceAll("<= X <", "-");
				caption = caption.replaceAll("< X <", "-");
			} else if (this.captiontype.contains("<=x<") && !caption.contains(" X <")) {
				caption = caption.replaceAll(" - ", " <= X < ");
			}
			rangeItem.setCaption(caption);
			this.tableLabelInfo.setValueAt(rangeItem.getCaption(), i, TABLE_COLUMN_CAPTION);
		}
		return result;
	}

	/**
	 * 颜色方案改变时刷新颜色
	 */
	private void refreshColor() {
		if (comboBoxColorStyle != null) {
			Colors colors = comboBoxColorStyle.getSelectedItem();

			Color[] colors1 = new Color[colors.getCount()];
			for (int i = 0; i < colors.getCount(); i++) {
				colors1[i] = colors.get(i);
			}
			int rangeCount = themeLabel.getCount();

			colors = Colors.makeGradient(rangeCount, colors1);
			if (rangeCount > 0) {
				for (int i = 0; i < rangeCount; i++) {
					setTextStyleColor(themeLabel.getItem(i).getStyle(), colors.get(i));
				}
			}
		}
	}

	/**
	 * 设置文本风格颜色
	 *
	 * @param textStyle
	 *            需要设置的风格
	 * @param color
	 *            设置的颜色
	 */
	private void setTextStyleColor(TextStyle textStyle, Color color) {
		textStyle.setForeColor(color);
	}

	/**
	 * 初始化工具条
	 */
	private void initToolBar() {
		this.toolBar.setFloatable(false);
		this.toolBar.add(this.buttonMerge);
		this.toolBar.add(this.buttonSplit);
		this.toolBar.addSeparator();
		this.toolBar.add(this.buttonVisible);
		this.toolBar.add(this.buttonStyle);
		this.toolBar.addSeparator();
		this.buttonMerge.setIcon(InternalImageIconFactory.Merge);
		this.buttonSplit.setIcon(InternalImageIconFactory.Split);
		this.buttonStyle.setIcon(InternalImageIconFactory.STYLE);
		this.buttonVisible.setIcon(InternalImageIconFactory.VISIBLE);
	}

	/**
	 * 注册事件
	 */
	public void registActionListener() {
		this.unityListener = new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					int itemCount = panelProperty.getComboBoxOffsetUnity().getSelectedIndex();
					switch (itemCount) {
					case 0:
						panelAdvance.getLabelHorizontalUnity().setText(MapViewProperties.getString("String_DistanceUnit_Millimeter"));
						panelAdvance.getLabelVerticalUnity().setText(MapViewProperties.getString("String_DistanceUnit_Millimeter"));
						break;
					case 1:
						panelAdvance.getLabelHorizontalUnity().setText(UnitValue.parseToString(map.getCoordUnit()));
						panelAdvance.getLabelVerticalUnity().setText(UnitValue.parseToString(map.getCoordUnit()));
						break;
					default:
						break;
					}
				}
			}
		};
		unregistActionListener();
		this.panelProperty.getComboBoxOffsetUnity().addItemListener(this.unityListener);
		this.panelProperty.addPropertyChangeListener("ThemeChange", this.propertyChangeListener);
		this.panelAdvance.addPropertyChangeListener("ThemeChange", this.propertyChangeListener);
		this.buttonVisible.addActionListener(this.actionListener);
		this.buttonStyle.addActionListener(this.actionListener);
		this.buttonMerge.addActionListener(this.actionListener);
		this.buttonSplit.addActionListener(this.actionListener);
		this.tableLabelInfo.addMouseListener(this.mouseListener);
		this.comboBoxColorStyle.addItemListener(this.itemListener);
		this.comboBoxColorStyle.addColorChangedListener();
		this.comboBoxExpression.addItemListener(this.itemListener);
		this.comboBoxExpression.getComponent(0).addMouseListener(this.mouseAdapter);
		this.comboBoxRangeCount.addItemListener(this.itemListener);
		this.comboBoxRangeCount.getComponent(0).addMouseListener(this.mouseListener);
		this.comboBoxExpression.getComponent(0).addMouseListener(this.mouseListener);
		this.comboBoxRangeMethod.addMouseListener(this.mouseListener);
		this.comboBoxRangeMethod.addItemListener(this.itemListener);
		this.comboBoxRangeFormat.addItemListener(this.itemListener);
		this.spinnerRangeLength.addChangeListener(this.changeListener);
		this.tableLabelInfo.getModel().addTableModelListener(this.tableModelListener);
		this.layersTree.addPropertyChangeListener("LayerChange", this.layersTreePropertyChangeListener);
	}

	/**
	 * 注销事件
	 */
	public void unregistActionListener() {
		this.panelProperty.getComboBoxOffsetUnity().removeItemListener(this.unityListener);
		this.buttonVisible.removeActionListener(this.actionListener);
		this.buttonStyle.removeActionListener(this.actionListener);
		this.buttonMerge.removeActionListener(this.actionListener);
		this.buttonSplit.removeActionListener(this.actionListener);
		this.tableLabelInfo.removeMouseListener(this.mouseListener);
		this.comboBoxColorStyle.removeItemListener(this.itemListener);
		this.comboBoxColorStyle.removeColorChangedListener();
		this.comboBoxExpression.removeItemListener(this.itemListener);
		this.comboBoxRangeCount.removeItemListener(this.itemListener);
		this.comboBoxRangeCount.getComponent(0).removeMouseListener(this.mouseListener);
		this.comboBoxExpression.getComponent(0).removeMouseListener(this.mouseListener);
		this.comboBoxRangeMethod.getComponent(0).removeMouseListener(this.mouseListener);
		this.comboBoxRangeMethod.removeItemListener(this.itemListener);
		this.comboBoxRangeFormat.removeItemListener(this.itemListener);
		this.spinnerRangeLength.removeChangeListener(this.changeListener);
		this.tableLabelInfo.getModel().removeTableModelListener(this.tableModelListener);
		this.layersTree.removePropertyChangeListener("LayerChange", this.layersTreePropertyChangeListener);
	}

	class LayerChangeListener implements PropertyChangeListener {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			int[] selectRows = tableLabelInfo.getSelectedRows();
			map = ThemeGuideFactory.getMapControl().getMap();
			themeLabelLayer = MapUtilities.findLayerByName(map, layerName);
			if (null != themeLabelLayer && null != themeLabelLayer.getTheme() && themeLabelLayer.getTheme() instanceof ThemeLabel) {
				datasetVector = (DatasetVector) themeLabelLayer.getDataset();
				themeLabel = new ThemeLabel((ThemeLabel) themeLabelLayer.getTheme());
				getTable();
				for (int i = 0; i < selectRows.length; i++) {
					tableLabelInfo.addRowSelectionInterval(selectRows[i], selectRows[i]);
				}
			}
		}
	}

	private void refreshAtOnce() {
		firePropertyChange("ThemeChange", null, null);
		if (isRefreshAtOnce) {
			refreshMapAndLayer();
		}
	}

	class LocalActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			int[] selectedRows = tableLabelInfo.getSelectedRows();
			if (e.getSource() == buttonMerge) {
				if (selectedRows.length == tableLabelInfo.getRowCount()) {
					UICommonToolkit.showConfirmDialog(MapViewProperties.getString("String_Warning_RquiredTwoFieldForRange"));
				} else {
					// 合并选中项
					mergeItem();
				}
				refreshAtOnce();
			} else if (e.getSource() == buttonSplit) {
				// 拆分选中项
				splitItem();
				refreshAtOnce();
			} else if (e.getSource() == buttonVisible) {
				// 批量修改分段的可见状态
				setItemVisble();
				refreshAtOnce();
			} else if (e.getSource() == buttonStyle) {
				// 批量修文本风格
				setItemTextSytle();
			}
		}

		/**
		 * 拆分项
		 */
		private void splitItem() {
			int selectRow = tableLabelInfo.getSelectedRow();
			if (selectRow >= 0) {
				ThemeLabelItem item = themeLabel.getItem(selectRow);
				double splitValue = (item.getEnd() + item.getStart()) / 2;
				if (selectRow == 0) {
					// 第零条数据的拆分中值
					splitValue = (item.getEnd() + ((int) item.getEnd()) - 1) / 2;
				}
				if (selectRow == tableLabelInfo.getRowCount() - 1) {
					// 最后一条的拆分中值
					splitValue = (item.getStart() + ((int) item.getStart()) + 1) / 2;
				}
				DecimalFormat format = new DecimalFormat("#.####");
				String diff = format.format(item.getEnd() - item.getStart());
				// 首尾项不同时才能进行拆分
				if (!"0.0001".equals(diff)) {
					String startCaption = "";
					String endCaption = "";
					if (selectRow == 0) {
						startCaption = MessageFormat.format(MapViewProperties.getString("String_RangeFormat1"), "Min", format.format(splitValue));
						endCaption = MessageFormat.format(MapViewProperties.getString("String_RangeFormat"), format.format(splitValue),
								format.format(item.getEnd()));
					} else if (selectRow == tableLabelInfo.getRowCount() - 1) {
						startCaption = MessageFormat.format(MapViewProperties.getString("String_RangeFormat"), format.format(item.getStart()),
								format.format(splitValue));
						endCaption = MessageFormat.format(MapViewProperties.getString("String_RangeFormat"), format.format(splitValue), "Max");
					} else {
						startCaption = MessageFormat.format(MapViewProperties.getString("String_RangeFormat"), format.format(item.getStart()),
								format.format(splitValue));
						endCaption = MessageFormat.format(MapViewProperties.getString("String_RangeFormat"), format.format(splitValue),
								format.format(item.getEnd()));
					}
					themeLabel.split(selectRow, splitValue, item.getStyle(), startCaption, item.getStyle(), endCaption);
					isMergeOrSplit = true;
					getTable();
					labelCount = themeLabel.getCount();
					comboBoxRangeCount.setSelectedItem(String.valueOf(labelCount));
					tableLabelInfo.setRowSelectionInterval(selectRow, selectRow);
				}
			}
		}

		/**
		 * 合并项
		 */
		private void mergeItem() {
			int[] selectedRows = tableLabelInfo.getSelectedRows();
			int startIndex = selectedRows[0];
			int endIndex = selectedRows[selectedRows.length - 1];
			ThemeLabelItem startItem = themeLabel.getItem(startIndex);
			ThemeLabelItem endItem = themeLabel.getItem(endIndex);
			// 合并后的子项的表达式
			String caption = "";
			if (startIndex == 0) {
				caption = MessageFormat.format(MapViewProperties.getString("String_RangeFormat1"), "Min", String.valueOf(endItem.getEnd()));
			} else if (endIndex == tableLabelInfo.getRowCount() - 1) {
				caption = MessageFormat.format(MapViewProperties.getString("String_RangeFormat"), String.valueOf(startItem.getStart()), "Max");
			} else {
				caption = MessageFormat.format(MapViewProperties.getString("String_RangeFormat"), String.valueOf(startItem.getStart()),
						String.valueOf(endItem.getEnd()));
			}
			themeLabel.merge(startIndex, selectedRows.length, startItem.getStyle(), caption);
			isMergeOrSplit = true;
			labelCount = themeLabel.getCount();
			comboBoxRangeCount.setSelectedItem(String.valueOf(labelCount));
			getTable();
			tableLabelInfo.setRowSelectionInterval(selectedRows[0], selectedRows[0]);
			buttonMerge.setEnabled(false);
			buttonSplit.setEnabled(true);
		}

		/**
		 * 设置分段项是否可见
		 */
		private void setItemVisble() {
			int[] selectedRow = tableLabelInfo.getSelectedRows();
			// 有不可见的项就全部设置为不可见，全部不可见，或者全部可见就设置为相反状态
			if (hasInvisible(selectedRow) && !allItemInvisible(selectedRow)) {
				for (int i = 0; i < selectedRow.length; i++) {
					themeLabel.getItem(selectedRow[i]).setVisible(false);
				}
			} else {
				for (int i = 0; i < selectedRow.length; i++) {
					resetVisible(selectedRow[i]);
				}
			}
			getTable();
			for (int i = 0; i < selectedRow.length; i++) {
				tableLabelInfo.addRowSelectionInterval(selectedRow[i], selectedRow[i]);
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
				if (!themeLabel.getItem(selectedRows[i]).isVisible()) {
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
				if (!themeLabel.getItem(selectedRows[i]).isVisible()) {
					hasInvisible = true;
				}
			}
			return hasInvisible;
		}

		/**
		 * 重置可见选项
		 *
		 * @param selectRow
		 *            要重置的行
		 */
		private void resetVisible(int selectRow) {
			ThemeLabelItem tempThemeRangeItem = themeLabel.getItem(selectRow);
			boolean visible = tempThemeRangeItem.isVisible();
			if (visible) {
				tempThemeRangeItem.setVisible(false);
				tableLabelInfo.setValueAt(InternalImageIconFactory.INVISIBLE, selectRow, TABLE_COLUMN_VISIBLE);
			} else {
				tempThemeRangeItem.setVisible(true);
				tableLabelInfo.setValueAt(InternalImageIconFactory.VISIBLE, selectRow, TABLE_COLUMN_VISIBLE);
			}
		}

		/**
		 * 批量设置文本风格
		 */
		private void setItemTextSytle() {
			int[] selectedRow = tableLabelInfo.getSelectedRows();
			int width = buttonStyle.getWidth();
			int height = buttonStyle.getHeight();
			int x = buttonStyle.getLocationOnScreen().x - 4 * width;
			int y = buttonStyle.getLocationOnScreen().y + height;
			if (selectedRow.length > 0) {
				textStyleDialog = new TextStyleDialog(themeLabel.getItem(selectedRow[selectedRow.length - 1]).getStyle().clone(), map, themeLabelLayer);
				textStyleDialog.setTheme(themeLabel);
				textStyleDialog.setRows(selectedRow);
				textStyleDialog.setTitle(MapViewProperties.getString("String_SetTextStyle"));
				textStyleDialog.getTextStyleContainer().addPropertyChangeListener("ThemeChange", ThemeLabelRangeContainer.this.propertyChangeListener);
				textStyleDialog.setRefreshAtOnce(isRefreshAtOnce);
				textStyleDialog.setLocation(x, y);
				textStyleDialog.setVisible(true);
			}
			getTable();
			for (int i = 0; i < selectedRow.length; i++) {
				tableLabelInfo.addRowSelectionInterval(selectedRow[i], selectedRow[i]);
			}
		}
	}

	class LocalMouseListener extends MouseAdapter {
		@Override
		public void mouseReleased(MouseEvent e) {
			int[] selectedRows = tableLabelInfo.getSelectedRows();
			if (selectedRows.length == 1) {
				buttonMerge.setEnabled(false);
				buttonSplit.setEnabled(true);
			} else if (selectedRows.length >= 2) {
				buttonSplit.setEnabled(false);
			}

			if (selectedRows.length >= 2 && MathUtilities.isContinuouslyArray(selectedRows)) {
				buttonMerge.setEnabled(true);
			} else {
				buttonMerge.setEnabled(false);
			}
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getSource() == tableLabelInfo && 1 == e.getClickCount() && tableLabelInfo.getSelectedColumn() == TABLE_COLUMN_VISIBLE
					&& tableLabelInfo.getSelectedRows().length == 1) {
				int selectRow = tableLabelInfo.getSelectedRow();
				ThemeLabelItem item = themeLabel.getItem(selectRow);
				boolean isVisible = item.isVisible();
				if (isVisible) {
					item.setVisible(false);
					tableLabelInfo.setValueAt(InternalImageIconFactory.INVISIBLE, selectRow, TABLE_COLUMN_VISIBLE);
				} else {
					item.setVisible(true);
					tableLabelInfo.setValueAt(InternalImageIconFactory.VISIBLE, selectRow, TABLE_COLUMN_VISIBLE);
				}
				tableLabelInfo.setRowSelectionInterval(selectRow, selectRow);
				refreshAtOnce();
			}
			if (e.getSource() == comboBoxRangeCount.getComponent(0)) {
				isMergeOrSplit = false;
			}
			if (e.getSource() == comboBoxExpression.getComponent(0) || e.getSource() == comboBoxRangeMethod) {
				isResetComboBox = false;
			}
		}
	}

	class LocalComboBoxItemListener implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				Dataset[] datasets = ThemeUtil.getDatasets(themeLabelLayer, datasetVector);
				if (e.getSource() == comboBoxColorStyle) {
					// 修改颜色方案
					refreshColor();
					getTable();
				} else if (e.getSource() == comboBoxExpression) {
					// sql表达式
					boolean isItemChanged = ThemeUtil.getSqlExpression(comboBoxExpression, datasets, comboBoxArray, themeLabel.getRangeExpression(), true);
					if (isItemChanged) {
						// 修改表达式
						setFieldInfo();
					}
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
				refreshAtOnce();
				tableLabelInfo.setRowSelectionInterval(0, 0);
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
			labelCount = Integer.valueOf(comboBoxRangeCount.getSelectedItem().toString());
			resetThemeInfo();
		}

		/**
		 * 设置分段方法
		 */
		private void setRangeMethod() {
			int rangeMethod = comboBoxRangeMethod.getSelectedIndex();
			switch (rangeMethod) {
			case 0:
				// 等距分段
				rangeMode = RangeMode.EQUALINTERVAL;
				comboBoxRangeCount.setEnabled(true);
				spinnerRangeLength.setEnabled(false);
				isCustom = false;
				resetThemeInfo();
				break;
			case 1:
				// 平方根分度
				if (ThemeUtil.hasNegative(datasetVector, rangeExpression)) {
					// 有负数且为平方根分段
					UICommonToolkit.showErrorMessageDialog(MessageFormat.format(MapViewProperties.getString("String_MakeTheme_Error1"), rangeExpression,
							MapViewProperties.getString("String_RangeMode_SquareRoot")));
					isResetComboBox = true;
					resetComboBoxRangeMode();
					return;
				} else {
					rangeMode = RangeMode.SQUAREROOT;
					comboBoxRangeCount.setEnabled(true);
					spinnerRangeLength.setEnabled(false);
					isCustom = false;
					resetThemeInfo();
				}
				break;
			case 2:
				// 标准差分段
				rangeMode = RangeMode.STDDEVIATION;
				comboBoxRangeCount.setEnabled(false);
				spinnerRangeLength.setEnabled(false);
				isCustom = false;
				resetThemeInfo();
				break;
			case 3:
				// 对数分段
				if (ThemeUtil.hasNegative(datasetVector, rangeExpression)) {
					// 有负数且为对数分段
					UICommonToolkit.showErrorMessageDialog(MessageFormat.format(MapViewProperties.getString("String_MakeTheme_Error1"), rangeExpression,
							MapViewProperties.getString("String_RangeMode_Logarithm")));
					isResetComboBox = true;
					resetComboBoxRangeMode();
					return;
				} else {
					rangeMode = RangeMode.LOGARITHM;
					comboBoxRangeCount.setEnabled(true);
					spinnerRangeLength.setEnabled(false);
					isCustom = false;
					resetThemeInfo();
				}
				break;
			case 4:
				// 等计数分段
				rangeMode = RangeMode.QUANTILE;
				comboBoxRangeCount.setEnabled(true);
				spinnerRangeLength.setEnabled(false);
				isCustom = false;
				resetThemeInfo();
				break;
			case 5:
				// 自定义分段
				rangeMode = RangeMode.CUSTOMINTERVAL;
				double defaultRangeCount = 0;
				if (themeLabel.getCount() > 2) {
					defaultRangeCount = Double.valueOf(new DecimalFormat("0").format(themeLabel.getItem(1).getEnd() - themeLabel.getItem(1).getStart()));
				} else {
					defaultRangeCount = Double.valueOf(new DecimalFormat("0").format(themeLabel.getItem(0).getEnd()));
				}
				spinnerRangeLength.setValue(defaultRangeCount);
				comboBoxRangeCount.setEnabled(false);
				spinnerRangeLength.setEnabled(true);
				makeDefaultAsCustom();
				break;
			default:
				break;
			}
		}

		/**
		 * 字段表达式
		 */
		private void setFieldInfo() {
			rangeExpression = comboBoxExpression.getSelectedItem().toString();
			if (ThemeUtil.hasNegative(datasetVector, rangeExpression) && rangeMode == RangeMode.SQUAREROOT) {
				// 有负数且为平方根分段
				UICommonToolkit.showErrorMessageDialog(MessageFormat.format(MapViewProperties.getString("String_MakeTheme_Error1"), rangeExpression,
						MapViewProperties.getString("String_RangeMode_SquareRoot")));
				isResetComboBox = true;
				resetComboBoxRangeExpression(themeLabel.getRangeExpression());
				return;
			}
			if (ThemeUtil.hasNegative(datasetVector, rangeExpression) && rangeMode == RangeMode.LOGARITHM) {
				// 有负数且为对数分段
				UICommonToolkit.showErrorMessageDialog(MessageFormat.format(MapViewProperties.getString("String_MakeTheme_Error1"), rangeExpression,
						MapViewProperties.getString("String_RangeMode_Logarithm")));
				isResetComboBox = true;
				resetComboBoxRangeExpression(themeLabel.getRangeExpression());
				return;
			}
			if (rangeMode == RangeMode.CUSTOMINTERVAL) {
				makeDefaultAsCustom();
				return;
			} else {
				resetThemeInfo();
				return;
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
			if (rangeMode.equals(RangeMode.STDDEVIATION)) {
				comboBoxRangeMethod.setSelectedIndex(2);
				return;
			}
			if (rangeMode.equals(RangeMode.LOGARITHM)) {
				comboBoxRangeMethod.setSelectedIndex(3);
				return;
			}
			if (rangeMode.equals(RangeMode.QUANTILE)) {
				comboBoxRangeMethod.setSelectedIndex(4);
				return;
			}
			if (rangeMode.equals(RangeMode.CUSTOMINTERVAL)) {
				comboBoxRangeMethod.setSelectedIndex(5);
				return;
			}
		}

		/**
		 * 重建专题图
		 */
		private void resetThemeInfo() {
			rangeExpression = comboBoxExpression.getSelectedItem().toString();
			if (isResetComboBox) {
				return;
			}
			if (rangeExpression.isEmpty()) {
				comboBoxExpression.setSelectedIndex(0);
			} else if (labelCount < 2 || labelCount > 32) {
				// 段数小于2，或者段数大于最大值
				comboBoxRangeCount.setSelectedItem(String.valueOf(themeLabel.getCount()));
			} else {
				ThemeLabel theme = null;
				theme = ThemeLabel.makeDefault(datasetVector, rangeExpression, rangeMode, labelCount, ColorGradientType.GREENRED, themeLabelLayer
						.getDisplayFilter().getJoinItems());
				if (null == theme) {
					// 专题图为空，提示专题图更新失败
					UICommonToolkit.showErrorMessageDialog(MapViewProperties.getString("String_Theme_UpdataFailed"));
					resetComboBoxRangeExpression(themeLabel.getRangeExpression());
				} else {
					refreshThemeLabel(theme);
				}
			}
		}

	}

	/**
	 * 刷新theme
	 *
	 * @param theme
	 */
	private void refreshThemeLabel(ThemeLabel theme) {
		if (null != theme) {
			resetThemeLabelInfo(theme);
			this.themeLabel = new ThemeLabel(theme);
			this.themeLabel.setRangeExpression(rangeExpression);
			refreshColor();
			getTable();
		} else {
			UICommonToolkit.showConfirmDialog(MapViewProperties.getString("String_Theme_UpdataFailed"));
		}
		if (2 <= this.themeLabel.getCount()) {
			this.labelCount = this.themeLabel.getCount();
			this.comboBoxRangeCount.setSelectedItem(String.valueOf(labelCount));
		}
	}

	private void resetThemeLabelInfo(ThemeLabel themeLabelTemp) {
		themeLabelTemp.setLabelExpression(this.themeLabel.getLabelExpression());
		themeLabelTemp.setBackShape(this.themeLabel.getBackShape());
		themeLabelTemp.setBackStyle(this.themeLabel.getBackStyle());
		themeLabelTemp.setOffsetFixed(this.themeLabel.isOffsetFixed());
		themeLabelTemp.setOffsetX(this.themeLabel.getOffsetX());
		themeLabelTemp.setOffsetY(this.themeLabel.getOffsetY());
		themeLabelTemp.setFlowEnabled(this.themeLabel.isFlowEnabled());
		themeLabelTemp.setTextExpression(this.themeLabel.isTextExpression());
		themeLabelTemp.setSmallGeometryLabeled(this.themeLabel.isSmallGeometryLabeled());
		themeLabelTemp.setVertical(this.themeLabel.isVertical());
		themeLabelTemp.setOverlapAvoided(this.themeLabel.isOverlapAvoided());
		themeLabelTemp.setAllDirectionsOverlappedAvoided(this.themeLabel.isAllDirectionsOverlappedAvoided());
		themeLabelTemp.setLeaderLineDisplayed(this.themeLabel.isLeaderLineDisplayed());
		themeLabelTemp.setLeaderLineStyle(this.themeLabel.getLeaderLineStyle());
		themeLabelTemp.setNumericPrecision(this.themeLabel.getNumericPrecision());
		themeLabelTemp.setAngleFixed(this.themeLabel.isAngleFixed());
		themeLabelTemp.setRepeatedLabelAvoided(this.themeLabel.isRepeatedLabelAvoided());
		themeLabelTemp.setAlongLineDirection(this.themeLabel.getAlongLineDirection());
		themeLabelTemp.setAlongLineSpaceRatio(this.themeLabel.getAlongLineSpaceRatio());
		themeLabelTemp.setLabelRepeatInterval(this.themeLabel.getLabelRepeatInterval());
		themeLabelTemp.setRepeatIntervalFixed(this.themeLabel.isRepeatIntervalFixed());
		themeLabelTemp.setOverLengthMode(this.themeLabel.getOverLengthMode());
		themeLabelTemp.setMaxLabelLength(this.themeLabel.getMaxLabelLength());
		themeLabelTemp.setMaxTextHeight(this.themeLabel.getMaxTextHeight());
		themeLabelTemp.setMinTextHeight(this.themeLabel.getMinTextHeight());
		themeLabelTemp.setMaxTextWidth(this.themeLabel.getMaxTextWidth());
		themeLabelTemp.setMinTextWidth(this.themeLabel.getMinTextWidth());
		themeLabelTemp.setTextExtentInflation(this.themeLabel.getTextExtentInflation());
	}

	/**
	 * 创建自定义的分段专题图
	 */
	private void makeDefaultAsCustom() {
		if (isResetComboBox) {
			return;
		}
		double rangeLength = (double) spinnerRangeLength.getValue();
		if (rangeLength > 0) {
			ThemeLabel theme = null;
			// 外部关联表字段制作专题图
			theme = ThemeLabel.makeDefault(datasetVector, rangeExpression, rangeMode, rangeLength, ColorGradientType.GREENRED, themeLabelLayer
					.getDisplayFilter().getJoinItems());
			if (null == theme || theme.getCount() == 0) {
				// 专题图为空，提示专题图更新失败
				UICommonToolkit.showErrorMessageDialog(MapViewProperties.getString("String_Theme_UpdataFailed"));
				resetComboBoxRangeExpression(themeLabel.getLabelExpression());
			} else {
				this.isCustom = true;
				refreshThemeLabel(theme);
			}
		}
	}

	private void resetComboBoxRangeExpression(String expression) {
		if (comboBoxArray.contains(expression)) {
			comboBoxExpression.setSelectedItem(expression);
		} else {
			comboBoxExpression.setSelectedItem(expression.substring(expression.indexOf(".") + 1, expression.length()));
		}

	}

	class LocalSpinnerChangeListener implements ChangeListener {

		@Override
		public void stateChanged(ChangeEvent e) {
			makeDefaultAsCustom();
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
			double nextValue = themeLabel.getItem(selectRow + 1).getEnd();
			if (nextValue - range > 0.0) {
				isRightValue = true;
			}
		} else if (selectRow != tableLabelInfo.getRowCount() - 1) {
			double prewValue = themeLabel.getItem(selectRow - 1).getEnd();
			double nextValue = themeLabel.getItem(selectRow + 1).getEnd();
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
				if (selectColumn == TABLE_COLUMN_RANGEVALUE && !StringUtilities.isNullOrEmptyString(tableLabelInfo.getValueAt(selectRow, selectColumn))) {
					String rangeValue = tableLabelInfo.getValueAt(selectRow, selectColumn).toString();
					if ((StringUtilities.isNumber(rangeValue) && isRightRangeValue(rangeValue, selectRow)) && (selectRow != tableLabelInfo.getRowCount() - 1)) {
						// 如果输入为数值且段值合法时修改段值
						setLabelRangeValue(selectRow, rangeValue);
					}
				} else if (selectColumn == TABLE_COLUMN_CAPTION && !StringUtilities.isNullOrEmptyString(tableLabelInfo.getValueAt(selectRow, selectColumn))) {
					String caption = tableLabelInfo.getValueAt(selectRow, selectColumn).toString();
					themeLabel.getItem(selectRow).setCaption(caption);
				}
				refreshAtOnce();
				getTable();
				tableLabelInfo.addRowSelectionInterval(selectRow, selectRow);
			} catch (Exception e) {
				Application.getActiveApplication().getOutput().output(e);
			}
		}

		private void setLabelRangeValue(int selectRow, String rangeValue) {
			DecimalFormat format = new DecimalFormat("#.######");
			String end = format.format(themeLabel.getItem(selectRow).getEnd());
			themeLabel.getItem(selectRow).setEnd(Double.valueOf(rangeValue));
			String caption = themeLabel.getItem(selectRow).getCaption();
			themeLabel.getItem(selectRow).setEnd(Double.parseDouble(rangeValue));
			double rangeEnd = Double.parseDouble(rangeValue);
			rangeValue = format.format(rangeEnd);
			// 替换当前行的标题
			if (StringUtilities.isNumber(end)) {
				caption = caption.replace(end, rangeValue);
				themeLabel.getItem(selectRow).setCaption(caption);
			}
			// 替换下一行的标题
			if (selectRow != themeLabel.getCount() - 1) {
				String nextCaption = themeLabel.getItem(selectRow + 1).getCaption();
				nextCaption = nextCaption.replace(end, rangeValue);
				themeLabel.getItem(selectRow + 1).setCaption(nextCaption);
			}
		}
	}

	public boolean isRefreshAtOnece() {
		return isRefreshAtOnce;
	}

	public void setRefreshAtOnece(boolean isRefreshAtOnece) {
		this.isRefreshAtOnce = isRefreshAtOnece;
	}

	public Layer getThemeLabelLayer() {
		return themeLabelLayer;
	}

	public void setThemeLabelLayer(Layer themeLabelLayer) {
		this.themeLabelLayer = themeLabelLayer;
	}

	@Override
	public Theme getCurrentTheme() {
		return themeLabel;
	}

	@Override
	public void setRefreshAtOnce(boolean isRefreshAtOnce) {
		this.isRefreshAtOnce = isRefreshAtOnce;
		this.panelProperty.setRefreshAtOnce(isRefreshAtOnce);
		this.panelAdvance.setRefreshAtOnce(isRefreshAtOnce);
	}

	@Override
	public void refreshMapAndLayer() {
		this.panelAdvance.refreshMapAndLayer();
		this.panelProperty.refreshMapAndLayer();
		if (null != ThemeGuideFactory.getMapControl()) {
			this.map = ThemeGuideFactory.getMapControl().getMap();
		}
		this.themeLabelLayer = MapUtilities.findLayerByName(map, layerName);
		if (null != themeLabelLayer && null != themeLabelLayer.getTheme() && themeLabelLayer.getTheme().getType() == ThemeType.LABEL) {
			ThemeLabel nowThemeLabel = ((ThemeLabel) themeLabelLayer.getTheme());
			nowThemeLabel.clear();
			if (0 < this.themeLabel.getCount()) {
				for (int i = 0; i < this.themeLabel.getCount(); i++) {
					if (null != this.themeLabel.getItem(i)) {
						nowThemeLabel.addToTail(this.themeLabel.getItem(i), true);
					}
				}
			}
			nowThemeLabel.setRangeExpression(this.themeLabel.getRangeExpression());
			this.map.refresh();
			UICommonToolkit.getLayersManager().getLayersTree().refreshNode(themeLabelLayer);
		}
	}

	class LocalPropertyChangeListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			ThemeLabelRangeContainer.this.firePropertyChange("ThemeChange", null, null);
			map = ThemeGuideFactory.getMapControl().getMap();
			map.refresh();
		}
	}

	@Override
	public Layer getCurrentLayer() {
		return themeLabelLayer;
	}

	public ThemeLabelPropertyPanel getPanelProperty() {
		return panelProperty;
	}

	public ThemeLabelAdvancePanel getPanelAdvance() {
		return panelAdvance;
	}

	@Override
	public void setCurrentLayer(Layer layer) {
		this.themeLabelLayer = layer;
	}

}
