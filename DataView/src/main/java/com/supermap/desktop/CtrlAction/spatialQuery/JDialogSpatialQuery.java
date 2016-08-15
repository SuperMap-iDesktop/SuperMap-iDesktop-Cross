package com.supermap.desktop.CtrlAction.spatialQuery;

import com.supermap.data.DatasetType;
import com.supermap.data.Datasource;
import com.supermap.data.Datasources;
import com.supermap.data.SpatialQueryMode;
import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.controls.utilities.ControlsResources;
import com.supermap.desktop.controls.utilities.ToolbarUIUtilities;
import com.supermap.desktop.dataview.DataViewProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.TristateCheckBox;
import com.supermap.desktop.ui.controls.ComponentBorderPanel.CompTitledPane;
import com.supermap.desktop.ui.controls.DataCell;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.TextFields.ISmTextFieldLegit;
import com.supermap.desktop.ui.controls.TextFields.SmTextFieldLegit;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.utilities.CoreResources;
import com.supermap.desktop.utilities.MapUtilities;
import com.supermap.desktop.utilities.StringUtilities;
import com.supermap.mapping.Layer;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;

/**
 * @author XiaJT
 */
public class JDialogSpatialQuery extends SmDialog {

	private JToolBar toolBar = new JToolBar();
	private SmButton buttonSelectAll = new SmButton();
	private SmButton buttonInvert = new SmButton();
	private SmButton buttonReset = new SmButton();
	private JLabel labelSearchLayer = new JLabel();
	private JComboBox<Layer> comboBoxSearchLayer = new JComboBox<>();
	private JLabel labelSelectedCount = new JLabel();
	private JScrollPane scrollPane = new JScrollPane();
	private JTable tableLayers = new JTable();
	private TableModelSpatialQuery tableModelSpatialQuery;
	private JPanel panelDescribe = new JPanel();

	private CompTitledPane compTitledPaneSearchResult;
	private JPanel panelSearchResult = new JPanel();
	private TristateCheckBox checkBoxSaveResult = new TristateCheckBox();
	private JLabel labelDatasource = new JLabel();
	private JComboBox<Datasource> comboBoxDatasource = new JComboBox<>();
	private JLabel labelDataset = new JLabel();
	private SmTextFieldLegit smTextFieldLegitDataset = new SmTextFieldLegit();
	private TristateCheckBox checkBoxOnlySaveSpatial = new TristateCheckBox();

	private JPanel panelResultShowWay = new JPanel();
	private TristateCheckBox checkBoxShowInTabular = new TristateCheckBox();
	private TristateCheckBox checkBoxShowInMap = new TristateCheckBox();
	//	private JCheckBox checkBoxShowInScene = new JCheckBox();
	private TristateCheckBox checkBoxSelectedAll = new TristateCheckBox();

	private JPanel panelButton = new JPanel();
	private JCheckBox checkBoxAutoClose = new JCheckBox();
	private SmButton smButtonOK = new SmButton();
	private SmButton smButtonCancel = new SmButton();

	private ArrayList<DatasetType> supportSearchDatasetTypes = new ArrayList<>();
	private boolean isIgnore = false;
	private boolean checkBoxSelectAllLock = false;

	public JDialogSpatialQuery() {
		super(((JFrame) Application.getActiveApplication().getMainFrame()), false);
		init();
	}

	private void init() {
		supportSearchDatasetTypes.add(DatasetType.POINT);
		supportSearchDatasetTypes.add(DatasetType.LINE);
		supportSearchDatasetTypes.add(DatasetType.REGION);


		initComponent();
		initLayout();
		initListener();
		initResources();
		initComponentState();
	}

	private void initComponent() {
		initTable();
		smTextFieldLegitDataset.setSmTextFieldLegit(new ISmTextFieldLegit() {
			@Override
			public boolean isTextFieldValueLegit(String textFieldValue) {
				if (isIgnore) {
					return true;
				}
				if (StringUtilities.isNullOrEmpty(textFieldValue)) {
					return false;
				}
				if (comboBoxDatasource.getSelectedItem() == null) {
					return false;
				}
				if (!((Datasource) comboBoxDatasource.getSelectedItem()).getDatasets().isAvailableDatasetName(textFieldValue)) {
					return false;
				}
				tableModelSpatialQuery.setDatasetName(tableLayers.getSelectedRow(), textFieldValue);
				return true;
			}

			@Override
			public String getLegitValue(String currentValue, String backUpValue) {
				return null;
			}
		});
		comboBoxSearchLayer.setMinimumSize(new Dimension(200, 23));
		comboBoxSearchLayer.setPreferredSize(new Dimension(200, 23));
		comboBoxSearchLayer.setRenderer(new ListCellRenderer<Layer>() {
			@Override
			public Component getListCellRendererComponent(JList<? extends Layer> list, Layer value, int index, boolean isSelected, boolean cellHasFocus) {
				DataCell dataCell = new DataCell(value);
				if (isSelected) {
					dataCell.setBackground(list.getSelectionBackground());
				} else {
					dataCell.setBackground(list.getBackground());
				}
				return dataCell;
			}
		});
		comboBoxDatasource.setRenderer(new ListCellRenderer<Datasource>() {
			@Override
			public Component getListCellRendererComponent(JList<? extends Datasource> list, Datasource value, int index, boolean isSelected, boolean cellHasFocus) {
				DataCell dataCell = new DataCell(value);
				if (isSelected) {
					dataCell.setBackground(list.getSelectionBackground());
				} else {
					dataCell.setBackground(list.getBackground());
				}
				return dataCell;
			}
		});
		Datasources datasources = Application.getActiveApplication().getWorkspace().getDatasources();
		for (int i = 0; i < datasources.getCount(); i++) {
			if (datasources.get(i).isOpened() && !datasources.get(i).isReadOnly()) {
				comboBoxDatasource.addItem(datasources.get(i));
			}
		}
		checkBoxSelectedAll.setSelected(false);
//		comboBoxSearchLayer.setRenderer(new ListCellRenderer<Layer>() {
//			@Override
//			public Component getListCellRendererComponent(JList<? extends Layer> list, Layer value, int index, boolean isSelected, boolean cellHasFocus) {
//				JLabel jLabel = new JLabel();
//				if (value != null) {
//					jLabel.setText(value.getCaption());
//				}
//				if (isSelected) {
//					jLabel.setOpaque(true);
//					jLabel.setBackground(list.getSelectionBackground());
//				}
//				return jLabel;
//			}
//		});
		compTitledPaneSearchResult = new CompTitledPane(checkBoxSaveResult, panelSearchResult);

		this.setSize(new Dimension(700, 500));
		this.setLocationRelativeTo(null);

		this.getRootPane().setDefaultButton(smButtonOK);
	}

	private void initTable() {
		tableModelSpatialQuery = new TableModelSpatialQuery();
		tableLayers.setModel(tableModelSpatialQuery);
		tableLayers.getTableHeader().setReorderingAllowed(false);
		tableLayers.setRowHeight(23);
		tableLayers.getTableHeader().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (tableLayers.columnAtPoint(e.getPoint()) == 0) {
					checkBoxSelectAllLock = true;
					checkBoxSelectedAll.setSelected(checkBoxSelectedAll.isSelectedEx() != null && !checkBoxSelectedAll.isSelectedEx());
					for (int i = 0; i < tableLayers.getRowCount(); i++) {
						tableLayers.setValueAt(checkBoxSelectedAll.isSelectedEx(), i, 0);
					}
					tableLayers.getTableHeader().repaint();
					checkBoxSelectAllLock = false;
				}
			}
		});
		tableLayers.getColumnModel().getColumn(0).setMaxWidth(30);
		tableLayers.getColumnModel().getColumn(0).setHeaderRenderer(new TableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				JPanel jPanel = new JPanel();
				jPanel.add(checkBoxSelectedAll);
				return jPanel;
			}
		});
		tableLayers.getColumnModel().getColumn(1).setMaxWidth(40);
		tableLayers.setDefaultRenderer(DatasetType.class, new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				if (value != null && value instanceof DatasetType) {
					JLabel jLabel = new JLabel();
					String datasetImagePath = CommonToolkit.DatasetImageWrap.getImageIconPath(((DatasetType) value));
					URL url = ControlsResources.getResourceURL(datasetImagePath);
					jLabel.setIcon(new ImageIcon(url));
					jLabel.setHorizontalAlignment(CENTER);
					if (isSelected) {
						jLabel.setOpaque(true);
						jLabel.setBackground(table.getSelectionBackground());
					}
					return jLabel;
				} else {
					return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				}
			}
		});
		tableLayers.getColumnModel().getColumn(2).setPreferredWidth(100);
		tableLayers.getColumnModel().getColumn(TableModelSpatialQuery.ROW_INDEX_SPATIAL_QUERY_MODE).setCellRenderer(new TableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				JLabel jLabel = new JLabel();
				if (value != null && value instanceof SpatialQueryMode) {
					String[] spatialQueryDescribe = getSpatialQueryDescribe(((DatasetType) table.getValueAt(row, TableModelSpatialQuery.ROW_INDEX_DATASET_TYPE)), ((SpatialQueryMode) value));
					if (spatialQueryDescribe != null && spatialQueryDescribe.length > 0) {
						jLabel.setText(spatialQueryDescribe[0]);
					}
				}
				if (isSelected) {
					jLabel.setOpaque(true);
					jLabel.setBackground(table.getSelectionBackground());
				}
				return jLabel;
			}
		});
	}

	private String[] getSpatialQueryDescribe(DatasetType currentDatasetType, SpatialQueryMode... value) {
		ArrayList<String> results = new ArrayList<>();
		Layer selectedLayer = (Layer) comboBoxSearchLayer.getSelectedItem();
		if (selectedLayer != null) {
			DatasetType type = selectedLayer.getDataset().getType();
			for (SpatialQueryMode spatialQueryMode : value) {

			}
			String suffixes;
			if (type != DatasetType.CAD) {

			}
		}
		return null;

	}

	//region 初始化布局
	private void initLayout() {
		initToolBar();
		initPanelSearchResult();
		initPanelResultShowWay();
		initPanelButtons();
		scrollPane.setViewportView(tableLayers);
		this.setLayout(new GridBagLayout());
		this.add(toolBar, new GridBagConstraintsHelper(0, 0, 2, 1).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.WEST).setInsets(10, 10, 0, 0));
		this.add(scrollPane, new GridBagConstraintsHelper(0, 1, 2, 1).setWeight(1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER).setInsets(5, 10, 0, 10));
		this.add(compTitledPaneSearchResult, new GridBagConstraintsHelper(0, 2, 1, 1).setWeight(0.5, 0).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER).setInsets(5, 10, 0, 0));
		this.add(panelResultShowWay, new GridBagConstraintsHelper(1, 2, 1, 1).setWeight(0.5, 0).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER).setInsets(5, 5, 0, 10));
		this.add(panelButton, new GridBagConstraintsHelper(0, 3, 2, 1).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 10, 10, 10).setAnchor(GridBagConstraints.CENTER));

	}

	private void initToolBar() {
		toolBar.setFloatable(false);
		toolBar.setLayout(new GridBagLayout());
		toolBar.add(buttonSelectAll, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(0, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER).setInsets(0, 0, 0, 0));
		toolBar.add(buttonInvert, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(0, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER).setInsets(0, 0, 0, 0));
		toolBar.add(buttonReset, new GridBagConstraintsHelper(2, 0, 1, 1).setWeight(0, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER).setInsets(0, 0, 0, 0));
		toolBar.add(ToolbarUIUtilities.getVerticalSeparator(), new GridBagConstraintsHelper(3, 0, 1, 1).setWeight(0, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER).setIpad(4, 0));
		toolBar.add(labelSearchLayer, new GridBagConstraintsHelper(4, 0, 1, 1).setWeight(0, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.CENTER).setInsets(0, 5, 0, 0));
		toolBar.add(comboBoxSearchLayer, new GridBagConstraintsHelper(5, 0, 1, 1).setWeight(0, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.CENTER).setInsets(0, 5, 0, 0));
		toolBar.add(labelSelectedCount, new GridBagConstraintsHelper(6, 0, 1, 1).setWeight(0, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.CENTER).setInsets(0, 5, 0, 0));
		toolBar.add(new JPanel(), new GridBagConstraintsHelper(7, 0, 1, 1).setWeight(1, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.CENTER).setInsets(0, 5, 0, 0));
	}

	private void initPanelSearchResult() {
		panelSearchResult.setLayout(new GridBagLayout());
		panelSearchResult.add(labelDatasource, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.WEST).setInsets(0, 20, 0, 0));
		panelSearchResult.add(comboBoxDatasource, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setInsets(0, 20, 0, 10).setFill(GridBagConstraints.HORIZONTAL));

		panelSearchResult.add(labelDataset, new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.WEST).setInsets(5, 20, 0, 0).setFill(GridBagConstraints.NONE));
		panelSearchResult.add(smTextFieldLegitDataset, new GridBagConstraintsHelper(1, 1, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setInsets(5, 20, 0, 10).setFill(GridBagConstraints.HORIZONTAL));

		panelSearchResult.add(checkBoxOnlySaveSpatial, new GridBagConstraintsHelper(0, 2, 2, 1).setWeight(1, 0).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 0, 0).setFill(GridBagConstraints.HORIZONTAL));

	}

	private void initPanelResultShowWay() {
		panelResultShowWay.setBorder(BorderFactory.createTitledBorder(DataViewProperties.getString("String_DisplayResult")));

		panelResultShowWay.setLayout(new GridBagLayout());
		panelResultShowWay.add(checkBoxShowInTabular, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 0, 0));
		panelResultShowWay.add(checkBoxShowInMap, new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 0, 0));
//		panelResultShowWay.add(checkBoxShowInScene, new GridBagConstraintsHelper(0, 2, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 0, 0));
		panelResultShowWay.add(new JPanel(), new GridBagConstraintsHelper(0, 3, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH));
	}

	private void initPanelButtons() {
		panelButton.setLayout(new GridBagLayout());
		panelButton.add(checkBoxAutoClose, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 0, 0));
		panelButton.add(smButtonOK, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(0, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 0, 0));
		panelButton.add(smButtonCancel, new GridBagConstraintsHelper(2, 0, 1, 1).setWeight(0, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.EAST).setInsets(0, 5, 0, 0));
	}
	//endregion

	private void initListener() {
		tableModelSpatialQuery.addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				// 全选，非选实现
				if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 0 && !checkBoxSelectAllLock) {
					Boolean result = (Boolean) tableModelSpatialQuery.getValueAt(0, 0);
					for (int i = 0; i < tableModelSpatialQuery.getRowCount(); i++) {
						if (tableModelSpatialQuery.getValueAt(i, 0) != result) {
							result = null;
							break;
						}
					}
					checkBoxSelectedAll.setSelectedEx(result);
					tableLayers.getTableHeader().repaint();
					tableLayers.setRowSelectionInterval(e.getFirstRow(), e.getLastRow());
				} else if (e.getType() == TableModelEvent.DELETE || e.getType() == TableModelEvent.INSERT) {
					checkToolBarButtonState();
				}
				checkSmButtonOkState();
			}
		});
		comboBoxSearchLayer.addItemListener(new ItemListener() {
			private Layer lastSelected;

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					Object selectedItem = comboBoxSearchLayer.getSelectedItem();
					boolean isResetQueryMode = true;
					if (selectedItem != null) {
						Layer layer = (Layer) selectedItem;
						labelSelectedCount.setText(MessageFormat.format(DataViewProperties.getString("String_CountofFeaturesSelecte"), layer.getSelection().getCount()));
						if (lastSelected != null && layer.getDataset().getType() == lastSelected.getDataset().getType()) {
							isResetQueryMode = false;
						}
					}
					// 新选择的图层类型不同或者为空重置查询方式
					if (isResetQueryMode) {
						for (int i = 0; i < tableLayers.getRowCount(); i++) {
							tableLayers.setValueAt(null, i, TableModelSpatialQuery.ROW_INDEX_SPATIAL_QUERY_MODE);
						}
					}
				} else {
					lastSelected = ((Layer) e.getItem());
				}
			}
		});
		checkBoxSaveResult.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				boolean selected = checkBoxSaveResult.isSelected();
				comboBoxDatasource.setEnabled(selected);
				smTextFieldLegitDataset.setEditable(selected && comboBoxDatasource.getSelectedItem() != null && tableLayers.getSelectedRowCount() == 1);
				checkBoxOnlySaveSpatial.setEnabled(selected);
				if (!isIgnore) {
					tableModelSpatialQuery.setIsSave(tableLayers.getSelectedRows(), selected);
				}
			}
		});
		comboBoxDatasource.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED && !isIgnore) {
					tableModelSpatialQuery.setDatasource(tableLayers.getSelectedRows(), ((Datasource) comboBoxDatasource.getSelectedItem()));
				}
			}
		});
		checkBoxOnlySaveSpatial.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (!isIgnore) {
					tableModelSpatialQuery.setIsOnlySaveSpatialInfo(tableLayers.getSelectedRows(), checkBoxOnlySaveSpatial.isSelected());
				}
			}
		});
		checkBoxShowInTabular.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (!isIgnore) {
					tableModelSpatialQuery.setShowInTabular(tableLayers.getSelectedRows(), checkBoxShowInTabular.isSelected());
				}
			}
		});
		checkBoxShowInMap.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (!isIgnore) {
					tableModelSpatialQuery.setShowInMap(tableLayers.getSelectedRows(), checkBoxShowInMap.isSelected());
				}
			}
		});
		smButtonCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		tableLayers.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				reLoadValue();
			}
		});
	}

	private void reLoadValue() {
		if (!isIgnore) {
			int[] selectedRows = tableLayers.getSelectedRows();
			isIgnore = true;
			if (selectedRows.length > 0) {
				checkBoxSaveResult.setSelectedEx(tableModelSpatialQuery.isSave(selectedRows));
				comboBoxDatasource.setSelectedItem(tableModelSpatialQuery.getResultDatasource(selectedRows));
				smTextFieldLegitDataset.setText(tableModelSpatialQuery.getDatasetName(selectedRows));
				checkBoxOnlySaveSpatial.setSelectedEx(tableModelSpatialQuery.isOnlySaveSpatialInfo(selectedRows));
				checkBoxShowInTabular.setSelectedEx(tableModelSpatialQuery.isShowInTabular(selectedRows));
				checkBoxShowInMap.setSelectedEx(tableModelSpatialQuery.isShowInMap(selectedRows));
			}
			checkPropertiesButtonState();
			isIgnore = false;
		}

	}

	//region 刷新按钮状态的方法。增加行或删除行时判断工具条按钮状态;选择行改变时更改属性按钮状态;属性改变时判断OK按钮状态

	/**
	 * 刷新工具条上按钮的状态
	 */
	private void checkToolBarButtonState() {
		boolean enabled = tableLayers.getRowCount() > 0;
		if (buttonSelectAll.isEnabled() ^ enabled) {
			buttonSelectAll.setEnabled(enabled);
			buttonInvert.setEnabled(enabled);
			buttonReset.setEnabled(enabled);
		}
	}

	/**
	 * 刷新属性按钮的可用性状态
	 */
	private void checkPropertiesButtonState() {
		boolean enabled = tableLayers.getSelectedRowCount() > 0;
		if (enabled ^ checkBoxSaveResult.isEnabled()) {
			checkBoxSaveResult.setEnabled(enabled);
//			comboBoxDatasource.setEnabled(enabled);
//			smTextFieldLegitDataset.setEditable(enabled);
//			checkBoxOnlySaveSpatial.setEnabled(enabled);
			checkBoxShowInTabular.setEnabled(enabled);
			checkBoxShowInMap.setEnabled(enabled);
		}
		smTextFieldLegitDataset.setEditable(enabled && comboBoxDatasource.getSelectedItem() != null && tableLayers.getSelectedRowCount() == 1);
	}

	/**
	 * 刷新确认按钮的状态
	 */
	private void checkSmButtonOkState() {
		if (checkBoxSelectedAll.isSelectedEx() == Boolean.FALSE) {
			smButtonOK.setEnabled(false);
			return;
		}
		for (int i = 0; i < tableModelSpatialQuery.getRowCount(); i++) {
			if (((Boolean) tableModelSpatialQuery.getValueAt(i, 0)) && tableModelSpatialQuery.getValueAt(i, TableModelSpatialQuery.ROW_INDEX_SPATIAL_QUERY_MODE) != null
					&& (tableModelSpatialQuery.isSave(i) || tableModelSpatialQuery.isShowInMap(i) || tableModelSpatialQuery.isShowInTabular(i)
					|| tableModelSpatialQuery.isShowInScene(i))) {
				smButtonOK.setEnabled(true);
				break;
			}
		}
		smButtonOK.setEnabled(false);
	}
	//endregion

	private void initResources() {
		this.setTitle(DataViewProperties.getString("String_SpatialQuery"));
		panelDescribe.setBorder(BorderFactory.createTitledBorder(DataViewProperties.getString("String_GroupBoxSQModeDescription")));
		labelSearchLayer.setText(DataViewProperties.getString("String__LabelSearchingLayer"));
		labelSelectedCount.setText(MessageFormat.format(DataViewProperties.getString("String_CountofFeaturesSelecte"), 0));
		checkBoxSaveResult.setText(DataViewProperties.getString("String_SaveResult"));
		labelDatasource.setText(DataViewProperties.getString("String_SQLQueryLabelDatasource"));
		labelDataset.setText(DataViewProperties.getString("String_SQLQueryLabelDataset"));
		checkBoxOnlySaveSpatial.setText(DataViewProperties.getString("String_OnlySaveSpatialInfo"));
		checkBoxShowInTabular.setText(DataViewProperties.getString("String_ShowResultInTabular"));
		checkBoxShowInMap.setText(DataViewProperties.getString("String_ShowResultInMap"));
		checkBoxAutoClose.setText(CommonProperties.getString("String_CheckBox_CloseDialog"));
		smButtonOK.setText(DataViewProperties.getString("String_Query"));
		smButtonCancel.setText(CommonProperties.getString(CommonProperties.Close));

		this.buttonSelectAll.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_SelectAll.png"));
		this.buttonSelectAll.setToolTipText(CommonProperties.getString(CommonProperties.selectAll));
		this.buttonInvert.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_SelectInverse.png"));
		this.buttonInvert.setToolTipText(CommonProperties.getString(CommonProperties.selectInverse));
		this.buttonReset.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_UndoSysDefault.png"));
		this.buttonReset.setToolTipText(CommonProperties.getString(CommonProperties.Reset));
	}

	private void initComponentState() {
		isIgnore = true;
		IForm activeForm = Application.getActiveApplication().getActiveForm();
		if (activeForm instanceof IFormMap) {
			ArrayList<Layer> layers = MapUtilities.getLayers(((IFormMap) activeForm).getMapControl().getMap());
			for (Layer layer : layers) {
				if (layer.getSelection().getCount() > 0 && layer.getDataset() != null && supportSearchDatasetTypes.contains(layer.getDataset().getType())) {
					comboBoxSearchLayer.addItem(layer);
				}
			}
			tableModelSpatialQuery.setLayers(MapUtilities.getLayers((((IFormMap) activeForm).getMapControl()).getMap()));
		} else {
			tableModelSpatialQuery.setLayers(null);
		}
		checkBoxSaveResult.setSelectedEx(false);
		if (tableModelSpatialQuery.getRowCount() > 0) {
			comboBoxDatasource.setSelectedItem(tableModelSpatialQuery.getResultDatasource(0));
			smTextFieldLegitDataset.setText(tableModelSpatialQuery.getDatasetName(0));
		} else {
			comboBoxDatasource.setSelectedIndex(-1);
			smTextFieldLegitDataset.setText("");
		}
		checkBoxSaveResult.setEnabled(false);
		checkBoxShowInTabular.setSelected(true);
		checkBoxShowInTabular.setEnabled(false);
		checkBoxShowInMap.setSelected(true);
		checkBoxShowInMap.setEnabled(false);
		checkBoxAutoClose.setSelected(true);
		checkBoxOnlySaveSpatial.setSelected(false);
		smButtonOK.setEnabled(false);
		isIgnore = false;
	}

	@Override
	public void dispose() {
		// TODO: 2016/8/11 clean
		super.dispose();
	}
}

