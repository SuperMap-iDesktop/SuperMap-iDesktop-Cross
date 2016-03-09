package com.supermap.desktop.newtheme.themeUnique;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.newtheme.commonPanel.ThemeChangePanel;
import com.supermap.desktop.newtheme.commonUtils.ThemeGuideFactory;
import com.supermap.desktop.newtheme.commonUtils.ThemeItemLabelDecorator;
import com.supermap.desktop.newtheme.commonUtils.UniqueValueCountUtil;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.*;
import com.supermap.desktop.utilties.MapUtilties;
import com.supermap.desktop.utilties.StringUtilties;
import com.supermap.mapping.*;
import com.supermap.ui.MapControl;

import javax.swing.*;
import javax.swing.border.TitledBorder;
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
import java.util.ArrayList;

/**
 * 单值专题图容器及属性设置接口类
 *
 * @author xie
 */
public class ThemeUniqueContainer extends ThemeChangePanel {

	private static final long serialVersionUID = 1L;
	private JTabbedPane tabbedPaneInfo = new JTabbedPane(JTabbedPane.TOP);
	private JPanel panelProperty = new JPanel();
	private JPanel panelAdvance = new JPanel();
	private JLabel labelExpression = new JLabel();
	private JComboBox<String> comboBoxExpression = new JComboBox<String>();
	private JLabel labelColorStyle = new JLabel();
	private ColorsComboBox comboboxColor = new ColorsComboBox();
	private JToolBar toolBar = new JToolBar();
	private JScrollPane scollPane = new JScrollPane();
	private JTable tableUniqueInfo = new JTable();
	private JButton buttonVisble = new JButton();
	private JButton buttonGeoStyle = new JButton();
	private JButton buttonAdd = new JButton();
	private JButton buttonDelete = new JButton();
	private JButton buttonAscend = new JButton();
	private JButton buttonDescend = new JButton();
	private JButton buttonAntitone = new JButton();
	private JPanel panelOffsetSet = new JPanel();
	private JLabel labelOffsetUnity = new JLabel();
	private JComboBox<String> comboBoxOffsetUnity = new JComboBox<String>();
	private JLabel labelOffsetX = new JLabel();
	private JLabel labelOffsetXUnity = new JLabel();
	private JComboBox<String> comboBoxOffsetX = new JComboBox<String>();
	private JLabel labelOffsetY = new JLabel();
	private JLabel labelOffsetYUnity = new JLabel();
	private JComboBox<String> comboBoxOffsetY = new JComboBox<String>();
	private AddItemPanel addItemPanel;
	private String[] nameStrings = { MapViewProperties.getString("String_Title_Visible"), MapViewProperties.getString("String_Title_Sytle"),
			MapViewProperties.getString("String_ThemeGraphItemManager_UniqueValue"), MapViewProperties.getString("String_ThemeGraphTextFormat_Caption") };
	private transient ThemeUnique themeUnique;
	private transient DatasetVector datasetVector;
	private SQLExpressionDialog sqlDialog;
	private transient Layer themeUniqueLayer;
	private transient Map map;
	private ArrayList<ThemeUniqueItem> deleteItems = new ArrayList<ThemeUniqueItem>();
	private boolean isRefreshAtOnce = true;
	private String expression;
	private boolean isNewTheme = false;
	private LayersTree layersTree = UICommonToolkit.getLayersManager().getLayersTree();
	private String layerName;

	private static int TABLE_COLUMN_VISIBLE = 0;
	private static int TABLE_COLUMN_GEOSTYLE = 1;
	private static int TABLE_COLUMN_UNIQUE = 2;
	private static int TABLE_COLUMN_CAPTION = 3;

	private static int EXPRESSION_TYPE = 1;
	private static int OFFSETX_TYPE = 2;
	private static int OFFSETY_TYPE = 3;

	private transient LocalComboBoxItemListener comboBoxItemListener = new LocalComboBoxItemListener();
	private transient LocalActionListener actionListener = new LocalActionListener();
	private transient LocalTableMouseListener localTableMouseListener = new LocalTableMouseListener();
	private transient LocalKeyListener localKeyListener = new LocalKeyListener();
	private transient LocalPopmenuListener popmenuListener = new LocalPopmenuListener();
	private transient LocalTableModelListener tableModelListener = new LocalTableModelListener();
	private LayersTreeChangeListener layersTreePropertyChangeListener = new LayersTreeChangeListener();

	/**
	 * @wbp.parser.constructor
	 */
	public ThemeUniqueContainer(DatasetVector datasetVector, ThemeUnique themeUnique) {
		this.datasetVector = datasetVector;
		this.themeUnique = new ThemeUnique(themeUnique);
		this.map = initCurrentTheme(datasetVector);
		this.isNewTheme = true;
		initComponents();
		initResources();
		registActionListener();
	}

	public ThemeUniqueContainer(Layer layer) {
		this.themeUniqueLayer = layer;
		this.layerName = this.themeUniqueLayer.getName();
		this.themeUnique = new ThemeUnique((ThemeUnique) themeUniqueLayer.getTheme());
		this.datasetVector = (DatasetVector) layer.getDataset();
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
	private Map initCurrentTheme(DatasetVector dataset) {
		MapControl mapControl = ThemeGuideFactory.getMapControl();
		if (null != mapControl) {
			this.themeUniqueLayer = mapControl.getMap().getLayers().add(dataset, themeUnique, true);
			this.layerName = this.themeUniqueLayer.getName();
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
		this.tabbedPaneInfo.add(MapViewProperties.getString("String_Theme_Advanced"), this.panelAdvance);
		this.add(tabbedPaneInfo, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setWeight(1, 1));
		getFieldComboBox(this.comboBoxExpression, EXPRESSION_TYPE);
		this.comboboxColor.setSelectedIndex(21);
		if (isNewTheme) {
			refreshColor();
		}
		initPanelProperty();
		initPanelAdvance();
	}

	/**
	 * 资源化
	 */
	private void initResources() {
		this.labelExpression.setText(MapViewProperties.getString("String_label_Expression"));
		this.labelColorStyle.setText(MapViewProperties.getString("String_Label_ColorScheme"));
		this.buttonVisble.setToolTipText(MapViewProperties.getString("String_Title_Visible"));
		this.buttonGeoStyle.setToolTipText(MapViewProperties.getString("String_Title_Sytle"));
		this.buttonAdd.setToolTipText(MapViewProperties.getString("String_Title_Add"));
		this.buttonDelete.setToolTipText(MapViewProperties.getString("String_Title_Delete"));
		this.buttonAscend.setToolTipText(MapViewProperties.getString("String_Title_Ascend"));
		this.buttonDescend.setToolTipText(MapViewProperties.getString("String_Title_Descend"));
		this.buttonAntitone.setToolTipText(MapViewProperties.getString("String_Title_Antitone"));
		this.panelOffsetSet.setBorder(new TitledBorder(null, MapViewProperties.getString("String_GroupBoxOffset"), TitledBorder.LEADING, TitledBorder.TOP,
				null, null));
		this.labelOffsetUnity.setText(MapViewProperties.getString("String_LabelOffsetUnit"));
		this.labelOffsetX.setText(MapViewProperties.getString("String_LabelOffsetX"));
		this.labelOffsetY.setText(MapViewProperties.getString("String_LabelOffsetY"));
		this.comboBoxOffsetX.setEditable(true);
		this.comboBoxOffsetY.setEditable(true);
	}

	class LayersTreeChangeListener implements PropertyChangeListener {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			int[] selectRows = tableUniqueInfo.getSelectedRows();
			// 属性修改后原有的map，themeUniqueLayer,themeUnique已经不存在，需要重新赋值
			map = ThemeGuideFactory.getMapControl().getMap();
			themeUniqueLayer = MapUtilties.findLayerByName(map, layerName);
			if (null != themeUniqueLayer && null != themeUniqueLayer.getTheme() && themeUniqueLayer.getTheme() instanceof ThemeUnique) {
				themeUnique = new ThemeUnique((ThemeUnique) themeUniqueLayer.getTheme());
				getTable();
				map.refresh();
				for (int i = 0; i < selectRows.length; i++) {
					tableUniqueInfo.addRowSelectionInterval(selectRows[i], selectRows[i]);
				}
			}
		}
	}

	/**
	 * 控件注册事件
	 */
	public void registActionListener() {
		unregistActionListener();
		this.comboBoxExpression.addItemListener(this.comboBoxItemListener);
		this.comboBoxOffsetX.addItemListener(this.comboBoxItemListener);
		this.comboBoxOffsetY.addItemListener(this.comboBoxItemListener);
		this.comboboxColor.addItemListener(this.comboBoxItemListener);
		this.comboBoxOffsetUnity.addItemListener(this.comboBoxItemListener);
		this.buttonVisble.addActionListener(this.actionListener);
		this.buttonAdd.addActionListener(this.actionListener);
		this.buttonDelete.addActionListener(this.actionListener);
		this.buttonGeoStyle.addActionListener(this.actionListener);
		this.buttonAntitone.addActionListener(this.actionListener);
		this.tableUniqueInfo.addMouseListener(this.localTableMouseListener);
		this.tableUniqueInfo.addKeyListener(this.localKeyListener);
		this.tableUniqueInfo.putClientProperty("terminateEditOnFocusLost", true);
		this.tableUniqueInfo.getModel().addTableModelListener(this.tableModelListener);
		this.layersTree.addPropertyChangeListener("LayerChange", this.layersTreePropertyChangeListener);
	}

	/**
	 * 修改单值项
	 */
	private void setUniqueItemUnique(String uniqueValue) {
		int index = 0;
		if (0 <= this.tableUniqueInfo.getSelectedRow() && !uniqueValue.isEmpty() && !hasUnique(uniqueValue)) {
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
		this.comboBoxExpression.removeItemListener(this.comboBoxItemListener);
		this.comboBoxOffsetX.removeItemListener(this.comboBoxItemListener);
		this.comboBoxOffsetY.removeItemListener(this.comboBoxItemListener);
		this.comboboxColor.removeItemListener(this.comboBoxItemListener);
		this.comboBoxOffsetUnity.removeItemListener(this.comboBoxItemListener);
		this.buttonVisble.removeActionListener(this.actionListener);
		this.buttonAdd.removeActionListener(this.actionListener);
		this.buttonDelete.removeActionListener(this.actionListener);
		this.buttonGeoStyle.removeActionListener(this.actionListener);
		this.buttonAntitone.removeActionListener(this.actionListener);
		this.tableUniqueInfo.removeMouseListener(this.localTableMouseListener);
		this.tableUniqueInfo.removeKeyListener(this.localKeyListener);
		this.tableUniqueInfo.getModel().removeTableModelListener(this.tableModelListener);
		this.layersTree.removePropertyChangeListener("LayerChange", this.layersTreePropertyChangeListener);
	}

	/**
	 * 高级面板布局
	 */
	private void initPanelAdvance() {
		this.panelAdvance.setLayout(new GridBagLayout());
		initComboBoxOffsetUnity();
		initComboBoxOffsetX();
		initComboBoxOffsetY();
		//@formatter:off
		this.panelAdvance.add(this.panelOffsetSet, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.NORTH).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 10, 5, 10));
		this.panelOffsetSet.setLayout(new GridBagLayout());
		this.panelOffsetSet.add(this.labelOffsetUnity,    new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(5,10,5,10).setWeight(20, 1).setIpad(20, 0));
		this.panelOffsetSet.add(this.comboBoxOffsetUnity, new GridBagConstraintsHelper(1, 0, 2, 1).setAnchor(GridBagConstraints.CENTER).setInsets(5,10,5,10).setWeight(80, 1).setFill(GridBagConstraints.HORIZONTAL));
		this.panelOffsetSet.add(this.labelOffsetX,        new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(0,10,5,10).setWeight(20, 1).setIpad(20, 0));
		this.panelOffsetSet.add(this.comboBoxOffsetX,     new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(0,10,5,10).setWeight(60, 1).setFill(GridBagConstraints.HORIZONTAL));
		this.panelOffsetSet.add(this.labelOffsetXUnity,   new GridBagConstraintsHelper(2, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(0,10,5,10).setWeight(10, 1).setIpad(20, 0));
		this.panelOffsetSet.add(this.labelOffsetY,        new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(0,10,5,10).setWeight(20, 1).setIpad(20, 0));
		this.panelOffsetSet.add(this.comboBoxOffsetY,     new GridBagConstraintsHelper(1, 2, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(0,10,5,10).setWeight(60, 1).setFill(GridBagConstraints.HORIZONTAL));
		this.panelOffsetSet.add(this.labelOffsetYUnity,   new GridBagConstraintsHelper(2, 2, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(0,10,5,10).setWeight(20, 1).setIpad(20, 0));
		//@formatter:on
	}

	/**
	 * 初始化偏移量单位
	 */
	private void initComboBoxOffsetUnity() {
		this.comboBoxOffsetUnity.setModel(new DefaultComboBoxModel<String>(new String[] {
				MapViewProperties.getString("String_MapBorderLineStyle_LabelDistanceUnit"), MapViewProperties.getString("String_ThemeLabelOffsetUnit_Map") }));
		if (this.themeUnique.isOffsetFixed()) {
			this.comboBoxOffsetUnity.setSelectedIndex(0);
		} else {
			this.comboBoxOffsetUnity.setSelectedIndex(1);
			this.labelOffsetXUnity.setText(String.valueOf(map.getCoordUnit()));
			this.labelOffsetYUnity.setText(String.valueOf(map.getCoordUnit()));
		}
	}

	/**
	 * 初始化水平偏移量
	 */
	private void initComboBoxOffsetX() {
		getFieldComboBox(this.comboBoxOffsetX, OFFSETX_TYPE);
		this.comboBoxOffsetX.insertItemAt("0", this.comboBoxOffsetX.getItemCount() - 2);
		String offsetX = themeUnique.getOffsetX();
		if (StringUtilties.isNullOrEmpty(offsetX)) {
			offsetX = "0";
		}
		this.comboBoxOffsetX.setSelectedItem(offsetX);
		if (!offsetX.equals(this.comboBoxOffsetX.getSelectedItem())) {
			this.comboBoxOffsetX.addItem(offsetX);
			this.comboBoxOffsetX.setSelectedItem(offsetX);
		}

	}

	/**
	 * 初始化垂直偏移量
	 */
	private void initComboBoxOffsetY() {
		getFieldComboBox(this.comboBoxOffsetY, OFFSETY_TYPE);
		this.comboBoxOffsetY.addItem("0");
		String offsetY = themeUnique.getOffsetY();
		if (StringUtilties.isNullOrEmpty(offsetY)) {
			offsetY = "0";
		}
		this.comboBoxOffsetY.setSelectedItem(offsetY);
		if (!offsetY.equals(this.comboBoxOffsetY.getSelectedItem())) {
			this.comboBoxOffsetY.addItem(offsetY);
			this.comboBoxOffsetY.setSelectedItem(offsetY);
		}

	}

	/**
	 * 初始化工具条
	 */
	private void initToolBar() {
		this.toolBar.setFloatable(false);
		this.toolBar.add(this.buttonVisble);
		this.toolBar.add(this.buttonGeoStyle);
		this.toolBar.addSeparator();
		this.toolBar.add(this.buttonAdd);
		this.toolBar.add(this.buttonDelete);
		this.toolBar.add(this.buttonAntitone);
		this.buttonVisble.setIcon(InternalImageIconFactory.VISIBLE);
		if (CommonToolkit.DatasetTypeWrap.isRegion(datasetVector.getType())) {
			this.buttonGeoStyle.setIcon(InternalImageIconFactory.REGION_STYLE);
		} else if (CommonToolkit.DatasetTypeWrap.isLine(datasetVector.getType())) {
			this.buttonGeoStyle.setIcon(InternalImageIconFactory.LINE_STYLE);
		} else {
			this.buttonGeoStyle.setIcon(InternalImageIconFactory.POINT_STYLE);
		}
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
		initToolBar();
		initComboBoxExpression();
		//@formatter:off
		this.panelProperty.add(this.labelExpression,    new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 10, 5, 0).setWeight(20, 0).setIpad(60, 0));
		this.panelProperty.add(this.comboBoxExpression, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 10, 5, 10).setWeight(60, 0).setFill(GridBagConstraints.HORIZONTAL));
		this.panelProperty.add(this.labelColorStyle,    new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 0).setWeight(20, 0).setIpad(60, 0));
		this.panelProperty.add(this.comboboxColor,      new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 10).setWeight(60, 0).setFill(GridBagConstraints.HORIZONTAL));
		this.panelProperty.add(this.toolBar,            new GridBagConstraintsHelper(0, 2, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 10, 5, 0).setWeight(100, 0));
		this.panelProperty.add(this.scollPane,          new GridBagConstraintsHelper(0, 3, 2, 1).setAnchor(GridBagConstraints.NORTH).setInsets(0, 10, 5, 10).setFill(GridBagConstraints.BOTH).setWeight(100, 3));
		getTable();
		this.tableUniqueInfo.setRowSelectionInterval(0, 0);
		this.scollPane.setViewportView(tableUniqueInfo);		
		//@formatter:on
	}

	/**
	 * 初始化表达式
	 */
	private void initComboBoxExpression() {
		this.comboBoxExpression.setEditable(true);
		String expression = themeUnique.getUniqueExpression();
		if (StringUtilties.isNullOrEmpty(expression)) {
			expression = "0";
		}
		this.comboBoxExpression.setSelectedItem(expression);
		if (!expression.equals(this.comboBoxExpression.getSelectedItem())) {
			this.comboBoxExpression.addItem(expression);
			this.comboBoxExpression.setSelectedItem(expression);
		}
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
					return getValueAt(0, 0).getClass();
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
		// 先注销事件再注册事件，避免重复注册事件
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
			ThemeUniqueItem uniqueItem = themeUnique.getItem(i);
			boolean isVisible = uniqueItem.isVisible();
			ImageIcon visibleIcon = InternalImageIconFactory.VISIBLE;
			if (!isVisible) {
				visibleIcon = InternalImageIconFactory.INVISIBLE;
			}
			this.tableUniqueInfo.setValueAt(visibleIcon, i, TABLE_COLUMN_VISIBLE);
			GeoStyle geoStyle = uniqueItem.getStyle();
			this.tableUniqueInfo.setValueAt(ThemeItemLabelDecorator.buildGeoStyleIcon(datasetVector, geoStyle), i, TABLE_COLUMN_GEOSTYLE);
			this.tableUniqueInfo.setValueAt(uniqueItem.getUnique(), i, TABLE_COLUMN_UNIQUE);
			this.tableUniqueInfo.setValueAt(uniqueItem.getCaption(), i, TABLE_COLUMN_CAPTION);
		}
		boolean defualtVisibleIcon = this.themeUnique.isDefaultStyleVisible();
		ImageIcon icon = InternalImageIconFactory.VISIBLE;
		if (!defualtVisibleIcon) {
			icon = InternalImageIconFactory.INVISIBLE;
		}
		GeoStyle defaultStyle = this.themeUnique.getDefaultStyle();
		this.tableUniqueInfo.setValueAt(icon, uniqueCount, TABLE_COLUMN_VISIBLE);
		this.tableUniqueInfo.setValueAt(ThemeItemLabelDecorator.buildGeoStyleIcon(datasetVector, defaultStyle), uniqueCount, TABLE_COLUMN_GEOSTYLE);
		this.tableUniqueInfo.setValueAt(MapViewProperties.getString("String_defualt_style"), uniqueCount, TABLE_COLUMN_CAPTION);
	}

	/**
	 * 表达式
	 *
	 * @return m_fieldComboBox
	 */
	private JComboBox<String> getFieldComboBox(JComboBox<String> comboBox, int type) {
		int count = datasetVector.getFieldCount();
		if (type == EXPRESSION_TYPE) {
			for (int j = 0; j < count; j++) {
				FieldInfo tempFieldInfo = datasetVector.getFieldInfos().get(j);
				String item = datasetVector.getName() + "." + tempFieldInfo.getName();
				comboBox.addItem(item);
			}
		} else {
			for (int j = 0; j < count; j++) {
				FieldInfo fieldInfo = datasetVector.getFieldInfos().get(j);
				if (fieldInfo.getType() == FieldType.INT16 || fieldInfo.getType() == FieldType.INT32 || fieldInfo.getType() == FieldType.INT64
						|| fieldInfo.getType() == FieldType.DOUBLE || fieldInfo.getType() == FieldType.SINGLE) {
					String item = datasetVector.getName() + "." + fieldInfo.getName();
					comboBox.addItem(item);
				}
			}
		}
		comboBox.addItem(MapViewProperties.getString("String_Combobox_Expression"));
		return comboBox;
	}

	class LocalTableMouseListener extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {
			if (1 == e.getClickCount() && tableUniqueInfo.getSelectedColumn() == TABLE_COLUMN_VISIBLE && tableUniqueInfo.getSelectedRows().length == 1) {
				if (tableUniqueInfo.getSelectedRow() != tableUniqueInfo.getRowCount() - 1) {
					int selectRow = tableUniqueInfo.getSelectedRow();
					ThemeUniqueItem item = themeUnique.getItem(selectRow);
					boolean isVisible = item.isVisible();
					if (isVisible) {
						item.setVisible(false);
						tableUniqueInfo.setValueAt(InternalImageIconFactory.INVISIBLE, selectRow, TABLE_COLUMN_VISIBLE);
					} else {
						item.setVisible(true);
						tableUniqueInfo.setValueAt(InternalImageIconFactory.VISIBLE, selectRow, TABLE_COLUMN_VISIBLE);
					}
				} else {
					boolean isDefaultStyle = themeUnique.isDefaultStyleVisible();
					if (isDefaultStyle) {
						themeUnique.setDefaultStyleVisible(false);
						tableUniqueInfo.setValueAt(InternalImageIconFactory.INVISIBLE, tableUniqueInfo.getRowCount() - 1, TABLE_COLUMN_VISIBLE);
					} else {
						themeUnique.setDefaultStyleVisible(true);
						tableUniqueInfo.setValueAt(InternalImageIconFactory.VISIBLE, tableUniqueInfo.getRowCount() - 1, TABLE_COLUMN_VISIBLE);
					}
				}
			} else if (2 == e.getClickCount() && tableUniqueInfo.getSelectedColumn() == TABLE_COLUMN_GEOSTYLE) {
				int selectRow = tableUniqueInfo.getSelectedRow();
				setItemGeoSytle();
				tableUniqueInfo.setRowSelectionInterval(selectRow, selectRow);
				refreshAtOnce();
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
			refreshAtOnce();
		}
	}

	/**
	 * 判断单值项是否已经存在
	 *
	 * @param uniqueValue
	 * @return
	 */
	private boolean hasUnique(String uniqueValue) {
		boolean itemHasExist = false;
		for (int i = 0; i < this.themeUnique.getCount(); i++) {
			if (this.themeUnique.getItem(i).getUnique().equals(uniqueValue)) {
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
				setGeoStyleColor(themeUnique.getItem(0).getStyle(), colors.get(0));
				setGeoStyleColor(themeUnique.getItem(rangeCount - 1).getStyle(), colors.get(colorCount - 1));
				for (int i = 1; i < rangeCount - 1; i++) {
					int colorIndex = Math.round(i * ratio);
					if (colorIndex == colorCount) {
						colorIndex--;
					}
					setGeoStyleColor(themeUnique.getItem(i).getStyle(), colors.get(colorIndex));
				}
			}
		}
	}

	/**
	 * 根据当前数据集类型设置颜色方案
	 *
	 * @param geoStyle 需要设置的风格
	 * @param color 设置的颜色
	 */
	private void setGeoStyleColor(GeoStyle geoStyle, Color color) {
		DatasetType datasetType = datasetVector.getType();
		if (CommonToolkit.DatasetTypeWrap.isPoint(datasetType) || CommonToolkit.DatasetTypeWrap.isLine(datasetType)) {
			geoStyle.setLineColor(color);
		} else if (CommonToolkit.DatasetTypeWrap.isRegion(datasetType)) {
			geoStyle.setFillForeColor(color);
		}
	}

	class LocalPopmenuListener implements PopupMenuListener {

		@Override
		public void popupMenuCanceled(PopupMenuEvent e) {
			getTable();
			refreshAtOnce();
			tableUniqueInfo.setRowSelectionInterval(0, 0);
			if (tableUniqueInfo.getRowCount() > 1) {
				buttonDelete.setEnabled(true);
			} else {
				buttonDelete.setEnabled(false);
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
				if (e.getSource() == comboBoxExpression) {
					// sql表达式
					boolean itemHasChanged = getSqlExpression(comboBoxExpression, EXPRESSION_TYPE);
					// 修改表达式
					if (!itemHasChanged) {
						// 如果sql表达式中修改了选项
						setFieldInfo();
						tableUniqueInfo.setRowSelectionInterval(0, 0);
					}
				} else if (e.getSource() == comboBoxOffsetX) {
					getSqlExpression(comboBoxOffsetX, OFFSETX_TYPE);
					// 修改水平偏移量
					setOffsetX();
				} else if (e.getSource() == comboBoxOffsetY) {
					getSqlExpression(comboBoxOffsetY, OFFSETY_TYPE);
					// 修改垂直偏移量
					setOffsetY();
				} else if (e.getSource() == comboboxColor) {
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
				} else if (e.getSource() == comboBoxOffsetUnity) {
					// 修改偏移量单位
					setOffsetUnity();
				}
				refreshAtOnce();
			}
		}

		/**
		 * 字段表达式
		 */
		private void setFieldInfo() {
			expression = comboBoxExpression.getSelectedItem().toString();
			if (expression.isEmpty()) {
				comboBoxExpression.setSelectedIndex(0);
			}
			if (UniqueValueCountUtil.isCountBeyond(datasetVector, expression)) {
				// 字段记录数大于3000条时建议不做专题图
				JOptionPane.showMessageDialog(null, MapViewProperties.getString("String_ThemeGridUnique_MessageBoxInfo"),
						CoreProperties.getString("String_MessageBox_Title"), JOptionPane.INFORMATION_MESSAGE);
				comboBoxExpression.setSelectedItem(datasetVector.getName() + ".SmUserID");
			} else {
				ThemeUnique theme = ThemeUnique.makeDefault(datasetVector, expression, ColorGradientType.GREENORANGEVIOLET);
				if (null != theme) {
					themeUnique = new ThemeUnique(theme);
					themeUnique.setUniqueExpression(expression);
					refreshColor();
					getTable();
				} else {
					JOptionPane.showMessageDialog(null, MapViewProperties.getString("String_Theme_UpdataFailed"),
							CoreProperties.getString("String_MessageBox_Title"), JOptionPane.INFORMATION_MESSAGE);
					comboBoxExpression.setSelectedItem(themeUnique.getUniqueExpression());
				}
			}
		}

		/**
		 * 修改水平偏移量
		 */
		private void setOffsetX() {
			String expressionOffsetX = comboBoxOffsetX.getSelectedItem().toString();
			themeUnique.setOffsetX(expressionOffsetX);

		}

		/**
		 * 修改垂直偏移量
		 */
		private void setOffsetY() {
			String expressionOffsetY = comboBoxOffsetY.getSelectedItem().toString();
			themeUnique.setOffsetY(expressionOffsetY);
		}

		private void setOffsetUnity() {
			if (MapViewProperties.getString("String_MapBorderLineStyle_LabelDistanceUnit").equals(comboBoxOffsetUnity.getSelectedItem().toString())) {
				themeUnique.setOffsetFixed(true);
				labelOffsetXUnity.setText(MapViewProperties.getString("String_Combobox_MM"));
				labelOffsetYUnity.setText(MapViewProperties.getString("String_Combobox_MM"));
			} else {
				themeUnique.setOffsetFixed(false);
				labelOffsetXUnity.setText(String.valueOf(map.getCoordUnit()));
				labelOffsetYUnity.setText(String.valueOf(map.getCoordUnit()));
			}
		}

		/**
		 * 获取表达式项
		 *
		 * @param jComboBoxField
		 */
		private boolean getSqlExpression(JComboBox<String> jComboBoxField, int type) {
			// 判断是否为“表达式”项
			boolean itemHasChanged = false;
			if (MapViewProperties.getString("String_Combobox_Expression").equals(jComboBoxField.getSelectedItem())) {
				sqlDialog = new SQLExpressionDialog();
				int allItems = jComboBoxField.getItemCount();
				Dataset[] datasets = new Dataset[1];
				datasets[0] = datasetVector;
				DialogResult dialogResult = null;
				ArrayList<FieldType> fieldTypes = new ArrayList<FieldType>();
				fieldTypes.add(FieldType.INT16);
				fieldTypes.add(FieldType.INT32);
				fieldTypes.add(FieldType.INT64);
				fieldTypes.add(FieldType.DOUBLE);
				fieldTypes.add(FieldType.SINGLE);
				if (type == EXPRESSION_TYPE) {
					dialogResult = sqlDialog.showDialog(themeUnique.getUniqueExpression(), datasets);
				} else if (type == OFFSETX_TYPE) {
					dialogResult = sqlDialog.showDialog(datasets, fieldTypes, themeUnique.getOffsetX());
				} else {
					dialogResult = sqlDialog.showDialog(datasets, fieldTypes, themeUnique.getOffsetY());
				}
				if (null != dialogResult && dialogResult == DialogResult.OK) {
					String filter = sqlDialog.getQueryParameter().getAttributeFilter();
					if (null != filter && !filter.isEmpty()) {
						jComboBoxField.insertItemAt(filter, allItems - 1);
						jComboBoxField.setSelectedIndex(allItems - 1);
						itemHasChanged = true;
					} else {
						resetComboBoxSelectItem(type, jComboBoxField);
						itemHasChanged = true;
					}
				} else {
					resetComboBoxSelectItem(type, jComboBoxField);
					itemHasChanged = true;
				}

			}
			return itemHasChanged;
		}

		private void resetComboBoxSelectItem(int type, JComboBox<String> jComboBoxField) {
			if (type == EXPRESSION_TYPE) {
				jComboBoxField.setSelectedItem(themeUnique.getUniqueExpression());
			} else if (type == OFFSETX_TYPE) {
				if (!StringUtilties.isNullOrEmpty(themeUnique.getOffsetX())) {
					jComboBoxField.setSelectedItem(themeUnique.getOffsetX());
				} else {
					jComboBoxField.setSelectedItem(0);
				}
			} else {
				if (!StringUtilties.isNullOrEmpty(themeUnique.getOffsetY())) {
					jComboBoxField.setSelectedItem(themeUnique.getOffsetY());
				} else {
					jComboBoxField.setSelectedItem(0);
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
				if (selectColumn == TABLE_COLUMN_UNIQUE && null != tableUniqueInfo.getValueAt(selectRow, selectColumn)
						&& !hasUnique(tableUniqueInfo.getValueAt(selectRow, selectColumn).toString())) {
					String uniqueValue = tableUniqueInfo.getValueAt(selectRow, selectColumn).toString();
					setUniqueItemUnique(uniqueValue);
					setUniqueItemCaption(uniqueValue);
				}
				if (selectColumn == TABLE_COLUMN_CAPTION && !StringUtilties.isNullOrEmptyString(tableUniqueInfo.getValueAt(selectRow, selectColumn))) {
					String caption = tableUniqueInfo.getValueAt(selectRow, selectColumn).toString();
					setUniqueItemCaption(caption);
				}
				getTable();
				tableUniqueInfo.addRowSelectionInterval(selectRow, selectRow);
				refreshAtOnce();
			} catch (Exception ex) {
				Application.getActiveApplication().getOutput().output(ex);
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
			int[] selectRows = null;
			if (tableUniqueInfo.getSelectedRows().length > 0) {
				selectRows = tableUniqueInfo.getSelectedRows();
			}
			if (e.getSource() == buttonVisble) {
				// 批量修改单值段的可见状态
				setItemVisble();
			} else if (e.getSource() == buttonGeoStyle) {
				// 批量修改单值段的符号方案
				setItemGeoSytle();
			} else if (e.getSource() == buttonAdd) {
				// 添加单值段
				addThemeItem();
			} else if (e.getSource() == buttonDelete) {
				// 删除单值段
				deleteItem();
			} else if (e.getSource() == buttonAntitone) {
				// 颜色方案反序
				setGeoStyleAntitone();
			}
			refreshAtOnce();
			if (null != selectRows && e.getSource() != buttonDelete) {
				for (int i = 0; i < selectRows.length; i++) {
					tableUniqueInfo.addRowSelectionInterval(selectRows[i], selectRows[i]);
				}
			} else {
				tableUniqueInfo.addRowSelectionInterval(0, 0);
			}
		}

		/**
		 * 添加单值段
		 */
		private void addThemeItem() {
			addItemPanel = new AddItemPanel(ThemeType.UNIQUE);
			addItemPanel.setDataset(datasetVector);
			addItemPanel.setThemeUnique(themeUnique);
			addItemPanel.setDeleteUniqueItems(deleteItems);
			addItemPanel.init();
			addItemPanel.addPopupMenuListener(popmenuListener);
			addItemPanel.show(buttonAdd, -addItemPanel.getWidth() / 2, buttonAdd.getHeight());
			addItemPanel.setVisible(true);
		}

		/**
		 * 判断单值项是否需要添加到未添加项中
		 *
		 * @param deleteItem
		 * @return
		 */
		private boolean isNeedAddToDeleteItems(ThemeUniqueItem deleteItem) {
			String deleteItemUnique = deleteItem.getUnique();
			ThemeUnique themeUniqueTemp = ThemeUnique.makeDefault(datasetVector, themeUnique.getUniqueExpression(), ColorGradientType.YELLOWGREEN);
			for (int i = 0; i < themeUniqueTemp.getCount(); i++) {
				if (themeUniqueTemp.getItem(i).getUnique().equals(deleteItemUnique)) {
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
			int uniqueCount = themeUnique.getCount();
			if (selectedRow[selectedRow.length - 1] != tableUniqueInfo.getRowCount() - 1 && selectedRow.length == 1) {
				ThemeUniqueItem item = themeUnique.getItem(selectedRow[0]);

				if (isNeedAddToDeleteItems(item)) {
					ThemeUniqueItem itemClone = new ThemeUniqueItem(item);
					deleteItems.add(itemClone);
				}

				themeUnique.remove(selectedRow[0]);

			} else if (selectedRow[selectedRow.length - 1] != tableUniqueInfo.getRowCount() - 1 && uniqueCount > 0) {
				for (int i = selectedRow.length - 1; i >= 0; i--) {
					ThemeUniqueItem item = themeUnique.getItem(selectedRow[i]);
					if (isNeedAddToDeleteItems(item)) {
						ThemeUniqueItem itemClone = new ThemeUniqueItem(item);
						deleteItems.add(itemClone);
					}
					themeUnique.remove(selectedRow[i]);
				}
			} else if (selectedRow[selectedRow.length - 1] == tableUniqueInfo.getRowCount() - 1) {
				for (int i = selectedRow.length - 2; i >= 0; i--) {
					ThemeUniqueItem item = themeUnique.getItem(selectedRow[i]);
					if (isNeedAddToDeleteItems(item)) {
						ThemeUniqueItem itemClone = new ThemeUniqueItem(item);
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
			themeUnique.reverseStyle();
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
						themeUnique.getItem(selectedRow[i]).setVisible(false);
					}
				} else {
					for (int i = 0; i < selectedRow.length - 1; i++) {
						themeUnique.getItem(selectedRow[i]).setVisible(false);
					}
					themeUnique.setDefaultStyleVisible(false);
				}
			} else {
				for (int i = 0; i < selectedRow.length; i++) {
					resetVisible(selectedRow[i]);
				}
			}
			getTable();
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
			if (selectedRows[selectedRows.length - 1] != tableUniqueInfo.getRowCount() - 1) {
				for (int i = 0; i < selectedRows.length; i++) {
					if (!themeUnique.getItem(selectedRows[i]).isVisible()) {
						count++;
					}
				}
			} else {
				for (int i = 0; i < selectedRows.length - 1; i++) {
					if (!themeUnique.getItem(selectedRows[i]).isVisible()) {
						count++;
					}
				}
				if (!themeUnique.isDefaultStyleVisible()) {
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
			if (selectedRows[selectedRows.length - 1] != tableUniqueInfo.getRowCount() - 1) {
				for (int i = 0; i < selectedRows.length; i++) {
					if (!themeUnique.getItem(selectedRows[i]).isVisible()) {
						hasInvisible = true;
					}
				}
			} else {
				if (!themeUnique.isDefaultStyleVisible()) {
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
			if (selectRow != tableUniqueInfo.getRowCount() - 1) {
				boolean visible = themeUnique.getItem(selectRow).isVisible();
				if (visible) {
					themeUnique.getItem(selectRow).setVisible(false);
					tableUniqueInfo.setValueAt(InternalImageIconFactory.INVISIBLE, selectRow, TABLE_COLUMN_VISIBLE);
				} else {
					themeUnique.getItem(selectRow).setVisible(true);
					tableUniqueInfo.setValueAt(InternalImageIconFactory.VISIBLE, selectRow, TABLE_COLUMN_VISIBLE);
				}
			} else {
				boolean visible = themeUnique.isDefaultStyleVisible();
				if (visible) {
					themeUnique.setDefaultStyleVisible(false);
					tableUniqueInfo.setValueAt(InternalImageIconFactory.INVISIBLE, selectRow, TABLE_COLUMN_VISIBLE);
				} else {
					themeUnique.setDefaultStyleVisible(true);
					tableUniqueInfo.setValueAt(InternalImageIconFactory.VISIBLE, selectRow, TABLE_COLUMN_VISIBLE);
				}
			}
		}

	}

	/**
	 * 设置文本风格
	 */
	private void setItemGeoSytle() {
		int[] selectedRow = this.tableUniqueInfo.getSelectedRows();
		SymbolDialog textStyleDialog = new SymbolDialog();
		String name = this.tableUniqueInfo.getColumnName(TABLE_COLUMN_VISIBLE);
		int width = this.tableUniqueInfo.getColumn(name).getWidth();
		int height = this.tableUniqueInfo.getTableHeader().getHeight();
		int x = this.tableUniqueInfo.getLocationOnScreen().x + width;
		int y = this.tableUniqueInfo.getLocationOnScreen().y - height;
		textStyleDialog.setLocation(x, y);
		Resources resources = Application.getActiveApplication().getWorkspace().getResources();
		SymbolType symbolType = null;
		GeoStyle geoStyle = new GeoStyle();
		if (CommonToolkit.DatasetTypeWrap.isPoint(datasetVector.getType())) {
			symbolType = SymbolType.MARKER;
		} else if (CommonToolkit.DatasetTypeWrap.isLine(datasetVector.getType())) {
			symbolType = SymbolType.LINE;
		} else if (CommonToolkit.DatasetTypeWrap.isRegion(datasetVector.getType())) {
			symbolType = SymbolType.FILL;
		}

		if (selectedRow.length == 1) {
			if (selectedRow[0] != this.tableUniqueInfo.getRowCount() - 1) {
				geoStyle = this.themeUnique.getItem(selectedRow[0]).getStyle();
			} else {
				geoStyle = this.themeUnique.getDefaultStyle();
			}
			DialogResult dialogResult = textStyleDialog.showDialog(resources, geoStyle, symbolType);
			if (dialogResult.equals(DialogResult.OK)) {
				GeoStyle nowGeoStyle = textStyleDialog.getStyle();
				if (selectedRow.length > 0) {
					for (int i = 0; i < selectedRow.length; i++) {
						if (selectedRow[i] != tableUniqueInfo.getRowCount() - 1) {
							resetGeoSytle(selectedRow[i], nowGeoStyle);
						} else {
							this.themeUnique.setDefaultStyle(nowGeoStyle);
						}
					}
				}
			}
		} else {
			java.util.List<GeoStyle> geoStyleList = new ArrayList<>();
			GeoStyle style;
			for (int i = 0; i < selectedRow.length; i++) {
				if (selectedRow[i] != this.tableUniqueInfo.getRowCount() - 1) {
					style = themeUnique.getItem(selectedRow[i]).getStyle();
				} else {
					style = themeUnique.getDefaultStyle();
				}
				geoStyleList.add(style);
			}
			JDialogSymbolsChange jDialogSymbolsChange = new JDialogSymbolsChange(symbolType, geoStyleList);
			jDialogSymbolsChange.showDialog();
		}
		getTable();
	}

	/**
	 * 重置文本风格
	 *
	 * @param selectRow 要重置文本风格的行
	 * @param nowGeoStyle 新的文本风格
	 */
	private void resetGeoSytle(int selectRow, GeoStyle nowGeoStyle) {
		ThemeUniqueItem item = this.themeUnique.getItem(selectRow);
		item.setStyle(nowGeoStyle);
		ImageIcon nowGeoStyleIcon = ThemeItemLabelDecorator.buildGeoStyleIcon(this.datasetVector, nowGeoStyle);
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

	public void refreshMapAndLayer() {
		this.map = ThemeGuideFactory.getMapControl().getMap();
		this.themeUniqueLayer = MapUtilties.findLayerByName(map, layerName);
		if (null != themeUniqueLayer && null != themeUniqueLayer.getTheme()) {
			ThemeUnique nowThemeUnique = ((ThemeUnique) this.themeUniqueLayer.getTheme());
			((ThemeUnique) this.themeUniqueLayer.getTheme()).clear();
			for (int i = 0; i < this.themeUnique.getCount(); i++) {
				nowThemeUnique.add(this.themeUnique.getItem(i));
			}
			nowThemeUnique.setUniqueExpression(this.themeUnique.getUniqueExpression());
			nowThemeUnique.setOffsetFixed(this.themeUnique.isOffsetFixed());
			nowThemeUnique.setOffsetX(this.themeUnique.getOffsetX());
			nowThemeUnique.setOffsetY(this.themeUnique.getOffsetY());
			UICommonToolkit.getLayersManager().getLayersTree().refreshNode(this.themeUniqueLayer);
			this.map.refresh();
		}
	}

	// 获取当前的专题图
	@Override
	public Theme getCurrentTheme() {
		return themeUnique;
	}

	@Override
	public void setRefreshAtOnce(boolean isRefreshAtOnce) {
		this.isRefreshAtOnce = isRefreshAtOnce;
	}

	@Override
	public Layer getCurrentLayer() {
		return themeUniqueLayer;
	}
}
