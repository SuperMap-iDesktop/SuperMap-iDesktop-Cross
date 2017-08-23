package com.supermap.desktop.CtrlAction.Dataset.createNewDataset;

import com.supermap.data.Charset;
import com.supermap.data.DatasetType;
import com.supermap.data.EncodeType;
import com.supermap.desktop.Application;
import com.supermap.desktop.CtrlAction.Dataset.AddToWindowMode;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.controls.utilities.ToolbarUIUtilities;
import com.supermap.desktop.dataeditor.DataEditorProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.CellRenders.TableDataCellRender;
import com.supermap.desktop.ui.controls.*;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.ui.controls.mutiTable.component.ComboBoxCellEditor;
import com.supermap.desktop.utilities.CharsetUtilities;
import com.supermap.desktop.utilities.CoreResources;
import com.supermap.desktop.utilities.EncodeTypeUtilities;
import com.supermap.desktop.utilities.TableUtilities;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * @author XiaJT
 * 新建数据集
 * 增加创建栅格以及影像数据集，以及用模版进行创建-yuanR 2017.8.15
 */
public class JDialogDatasetNew extends SmDialog {

	private JToolBar toolBar;
	private JButton buttonSelectAll;
	private JButton buttonSelectInvert;
	private JButton buttonDelete;
	private JButton buttonSetting;
	private JTable table;
	private NewDatasetTableModel newDatasetTableModel;

	private JPanel panelButton;
	private JCheckBox checkboxAutoClose;
	private SmButton buttonOk;
	private SmButton buttonCancel;

	private PanelDatasetNewProperty propertyPanel;
	private PanelModel panelModel;
	private DatasetTypeComboBox comboBoxDatasetType;

	public JDialogDatasetNew() {
		initComponents();
		initLayout();
		initResources();
		addListeners();
		initComponentStates();
	}

	private void initComponents() {
		this.setModal(true);
		setSize(750, 450);
		this.setLocationRelativeTo(null);
		toolBar = new JToolBar();
		toolBar.setFloatable(false);
		buttonSelectAll = new JButton();
		buttonSelectInvert = new JButton();
		buttonDelete = new JButton();
		buttonSetting = new JButton();
		initTable();
		panelButton = new JPanel();
		checkboxAutoClose = new JCheckBox();
		buttonOk = new SmButton();
		buttonCancel = new SmButton();

		propertyPanel = new PanelDatasetNewProperty();
		panelModel = new PanelModel(propertyPanel);

		this.componentList.add(this.buttonOk);
		this.componentList.add(this.buttonCancel);
		this.setFocusTraversalPolicy(this.policy);
	}

	private void initTable() {
		this.table = new JTable();
		this.table.setRowHeight(23);
		this.table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		this.newDatasetTableModel = new NewDatasetTableModel();
		this.table.setModel(newDatasetTableModel);

		this.table.getColumnModel().getColumn(NewDatasetTableModel.COLUMN_INDEX_TARGET_DATASOURCE).setCellRenderer(new TableDataCellRender());
		this.table.getColumnModel().getColumn(NewDatasetTableModel.COLUMN_INDEX_DATASET_TYPE).setCellRenderer(new TableDataCellRender());

//		this.table.getColumnModel().getColumn(NewDatasetTableModel.COLUMN_INDEX_EncodeType).setCellEditor(new EncodingTypeCellEditor());
//		this.table.getColumnModel().getColumn(NewDatasetTableModel.COLUMN_INDEX_Charset).setCellEditor(new CharsetTypeCellEditor());

		int count = Application.getActiveApplication().getWorkspace().getDatasources().getCount();
		String[] datasources = new String[count];
		int selectedIndex = -1;
		String activeDatasource = "";
		if (0 < Application.getActiveApplication().getActiveDatasources().length) {
			activeDatasource = Application.getActiveApplication().getActiveDatasources()[0].getAlias();
		}

		for (int index = 0; index < count; index++) {
			datasources[index] = Application.getActiveApplication().getWorkspace().getDatasources().get(index).getAlias();
			if (datasources[index].equals(activeDatasource)) {
				selectedIndex = index;
			}
		}
		final DatasourceComboBox datasourceComboBox = new DatasourceComboBox(Application.getActiveApplication().getWorkspace().getDatasources());
		datasourceComboBox.setSelectedIndex(selectedIndex);

		DefaultCellEditor targetDatasourceCellEditor = new DefaultCellEditor(datasourceComboBox);
		targetDatasourceCellEditor.setClickCountToStart(2);
		TableColumn targetDatasourceColumn = table.getColumnModel().getColumn(NewDatasetTableModel.COLUMN_INDEX_TARGET_DATASOURCE);
		targetDatasourceColumn.setCellEditor(targetDatasourceCellEditor);

		//2017.2.13 数据集类型下拉列表控件创建--yuanR
		DatasetType[] datasetTypes = new DatasetType[]{DatasetType.POINT, DatasetType.LINE, DatasetType.REGION, DatasetType.TEXT,
				DatasetType.CAD, DatasetType.TABULAR, DatasetType.POINT3D, DatasetType.LINE3D, DatasetType.REGION3D, DatasetType.IMAGE, DatasetType.GRID};
		comboBoxDatasetType = new DatasetTypeComboBox(datasetTypes);

		DefaultCellEditor datasetTypeCellEditor = new DefaultCellEditor(comboBoxDatasetType);
		datasetTypeCellEditor.setClickCountToStart(2);
		TableColumn datasetTypeColumn = table.getColumnModel().getColumn(NewDatasetTableModel.COLUMN_INDEX_DATASET_TYPE);
		datasetTypeColumn.setCellEditor(datasetTypeCellEditor);
		datasetTypeColumn.setPreferredWidth(100);
//		datasetTypeColumn.setCellRenderer(renderer);

		ArrayList<String> addTos = new ArrayList<String>();
		addTos.add(AddToWindowMode.toString(AddToWindowMode.NONEWINDOW));
		addTos.add(AddToWindowMode.toString(AddToWindowMode.NEWWINDOW));
		if (Application.getActiveApplication().getActiveForm() != null && Application.getActiveApplication().getActiveForm() instanceof IFormMap) {
			addTos.add(AddToWindowMode.toString(AddToWindowMode.CURRENTWINDOW));
		}

		ComboBoxCellEditor addToCellEditor = new ComboBoxCellEditor();
		addToCellEditor.getComboBox().setModel(new DefaultComboBoxModel<Object>(addTos.toArray(new String[addTos.size()])));
		TableColumn addToColumn = table.getColumnModel().getColumn(NewDatasetTableModel.COLUMN_INDEX_WINDOWMODE);
		addToCellEditor.setClickCountToStart(2);
		addToColumn.setCellEditor(addToCellEditor);
	}

	private void initLayout() {
		initToolBar();
		initPanelButton();
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.add(toolBar, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 0).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.WEST));
		panel.add(new JScrollPane(table), new GridBagConstraintsHelper(0, 1, 1, 2).setWeight(1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER));
		panel.add(panelModel, new GridBagConstraintsHelper(1, 1, 1, 1).setWeight(0, 0).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER));
		panel.add(propertyPanel, new GridBagConstraintsHelper(1, 2, 1, 1).setWeight(0, 0).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER));
		panel.add(panelButton, new GridBagConstraintsHelper(0, 3, 2, 1).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.CENTER).setInsets(5, 0, 0, 0));

		this.setLayout(new GridBagLayout());
		this.add(panel, new GridBagConstraintsHelper(0, 0, 1, 1).setFill(GridBagConstraints.BOTH).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(10));
	}

	private void initToolBar() {
		toolBar.add(buttonSelectAll);
		toolBar.add(buttonSelectInvert);
		toolBar.add(ToolbarUIUtilities.getVerticalSeparator());
		toolBar.add(buttonDelete);
		toolBar.add(ToolbarUIUtilities.getVerticalSeparator());
		toolBar.add(buttonSetting);
	}

	private void initPanelButton() {
		panelButton.setLayout(new GridBagLayout());
		panelButton.add(checkboxAutoClose, new GridBagConstraintsHelper(0, 0, 1, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.WEST).setWeight(1, 1));
		panelButton.add(buttonOk, new GridBagConstraintsHelper(1, 0, 1, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.EAST).setWeight(0, 1));
		panelButton.add(buttonCancel, new GridBagConstraintsHelper(2, 0, 1, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.EAST).setWeight(0, 1));
	}

	private void initResources() {
		this.setTitle(DataEditorProperties.getString("String_ToolStripMenuItem_NewDataset"));
		this.buttonCancel.setText(CommonProperties.getString("String_Button_Cancel"));
		this.buttonOk.setText(CommonProperties.getString("String_Button_OK"));
		this.buttonSelectAll.setToolTipText(CommonProperties.getString("String_ToolBar_SelectAll"));
		this.buttonSelectAll.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_SelectAll.png"));
		this.buttonSelectInvert.setToolTipText(CommonProperties.getString("String_ToolBar_SelectInverse"));
		this.buttonSelectInvert.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_SelectInverse.png"));
		this.buttonDelete.setToolTipText(CommonProperties.getString("String_Delete"));
		this.buttonDelete.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_Delete.png"));
		this.buttonSetting.setToolTipText(CommonProperties.getString("String_ToolBar_SetBatch"));
		this.buttonSetting.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_Setting.PNG"));
		this.checkboxAutoClose.setText(CommonProperties.getString("String_CheckBox_CloseDialog"));
	}

	private void addListeners() {
		this.buttonOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				TableUtilities.stopEditing(table);
				createDataset();
				dialogResult = DialogResult.OK;
				if (checkboxAutoClose.isSelected()) {
					JDialogDatasetNew.this.dispose();
				}
			}
		});
		this.buttonCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialogResult = DialogResult.CANCEL;
				JDialogDatasetNew.this.dispose();
			}
		});
		this.buttonSelectAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				TableUtilities.stopEditing(table);
				table.getSelectionModel().setSelectionInterval(0, table.getRowCount() - 1);
			}
		});

		this.buttonSelectInvert.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				TableUtilities.stopEditing(table);
				TableUtilities.invertSelection(table);
			}
		});

		this.buttonDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				TableUtilities.stopEditing(table);
				int[] selectedRows = table.getSelectedRows();
				for (int i = selectedRows.length - 1; i >= 0; i--) {
					newDatasetTableModel.removeRow(selectedRows[i]);
				}
			}
		});

		this.buttonSetting.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonSetting_Click();
			}
		});

		this.table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				checkButtonState();
				// 根据选择的行，设置其“设置”面板联动
				int[] selectedRow = table.getSelectedRows();
				resetPropertyPanel(selectedRow);
				resetModelPanel(selectedRow);
			}
		});

//		// 给comboBoxDatasetType添加监听-yuanR2017.8.16
//		this.comboBoxDatasetType.addItemListener(new ItemListener() {
//			@Override
//			public void itemStateChanged(ItemEvent e) {
//				DatasetTypeComboBox datasetTypeComboBox = (DatasetTypeComboBox) e.getSource();
//				// 当选择了影像数据集类型，弹出设置面板
//				if (datasetTypeComboBox.getSelectedDatasetTypeName().contains(DatasetTypeUtilities.toString(DatasetType.IMAGE))) {
//					// 给新建影像数据集面板属性设置类，并打开面板
////					JDialogNewImageDataset dialogNewGridDataset = new JDialogNewImageDataset(newDatasetTableModel.getDatasetBean(table.getSelectedRow()));
////					dialogNewGridDataset.showDialog();
//				}
//			}
//		});
	}

	/**
	 * 重设属性面板
	 */
	private void resetPropertyPanel(int[] selectedRow) {
		if (selectedRow.length > 0) {
			// 是否都为相同数据集
			Boolean isSameDatasetType = true;
			// 是否不含影像数据集
			Boolean isNotImageDataset = true;
			// 是否只有线面数据集
			Boolean isOnlyLineRegionDataset = true;
			// 是否选中最后一行
			Boolean isSelectedLastRow = true;
			DatasetType selectedDatasetType = newDatasetTableModel.getDatasetBean(selectedRow[0]).getDatasetType();
			// 根据选中情况进行三态控制
			for (int i = 0; i < selectedRow.length; i++) {
				DatasetType datasetType = newDatasetTableModel.getDatasetBean(selectedRow[i]).getDatasetType();
				if (!(datasetType.equals(selectedDatasetType))) {
					isSameDatasetType = false;
				}
				if (selectedRow[i] > table.getRowCount() - 2) {
					isSelectedLastRow = false;
				}
				if (datasetType.equals(DatasetType.GRID) || datasetType.equals(DatasetType.IMAGE)) {
					isNotImageDataset = false;
				}
				if (!(datasetType.equals(DatasetType.LINE) || datasetType.equals(DatasetType.REGION))) {
					isOnlyLineRegionDataset = false;
				}
			}
			propertyPanel.setPanelEnable(false);
			if (isSameDatasetType && isSelectedLastRow) {
				propertyPanel.setPanelEnable(true);
				startPropertyBatchSet(selectedRow);
			} else if (isNotImageDataset && isOnlyLineRegionDataset && isSelectedLastRow) {
				// 选中的项不包含栅格和影像数据集，此时字符集控件可用
				propertyPanel.getComboboxEncodingType().setEnabled(true);
				propertyPanel.getComboboxCharest().setEnabled(true);
				startPropertyBatchSet(selectedRow);
			} else if (isNotImageDataset && isSelectedLastRow) {
				// 选中的项不包含栅格和影像数据集，此时字符集控件可用
				propertyPanel.getComboboxCharest().setEnabled(true);
				startPropertyBatchSet(selectedRow);
			} else {
				propertyPanel.setPanelEnable(false);
			}
		}
	}

	/**
	 * 重设模版面板
	 */
	private void resetModelPanel(int[] selectedRow) {
		if (selectedRow.length > 0) {
			// 是否不含影像/栅格数据集
			Boolean isNotImageDataset = true;
			// 是否全为影像数据集
			Boolean isAllImageDataset = true;
			// 是否全为栅格数据集
			Boolean isAllGridDataset = true;
			// 是否选中最后一行
			Boolean isSelectedLastRow = true;
			// 根据选中情况进行三态控制
			for (int i = 0; i < selectedRow.length; i++) {
				DatasetType datasetType = newDatasetTableModel.getDatasetBean(selectedRow[i]).getDatasetType();
				if (!datasetType.equals(DatasetType.GRID)) {
					isAllGridDataset = false;
				}
				if (!datasetType.equals(DatasetType.IMAGE)) {
					isAllImageDataset = false;
				}
				if (selectedRow[i] > table.getRowCount() - 2) {
					isSelectedLastRow = false;
				}
				if (datasetType.equals(DatasetType.GRID) || datasetType.equals(DatasetType.IMAGE)) {
					isNotImageDataset = false;
				}
			}
			panelModel.setModelPanelEnabled(false);
			if (isAllImageDataset && isSelectedLastRow) {
				// 设置数据集下拉列表类型
				panelModel.setDatasetSupportedDatasetTypes(new DatasetType[]{DatasetType.IMAGE});
				panelModel.setRadioButtonEnabled(true);
				startModelBatchSet(selectedRow);
			} else if (isNotImageDataset && isSelectedLastRow) {
				// 设置数据集下拉列表类型
				// todo 类型待补充
				panelModel.setDatasetSupportedDatasetTypes(new DatasetType[]{DatasetType.POINT, DatasetType.LINE, DatasetType.REGION, DatasetType.TEXT,
						DatasetType.CAD, DatasetType.TABULAR, DatasetType.POINT3D, DatasetType.LINE3D, DatasetType.REGION3D});
				panelModel.setRadioButtonEnabled(true);
				startModelBatchSet(selectedRow);
			} else if (isAllGridDataset && isSelectedLastRow) {
				// 设置数据集下拉列表类型
				panelModel.setDatasetSupportedDatasetTypes(new DatasetType[]{DatasetType.GRID});
				panelModel.setRadioButtonEnabled(true);
				startModelBatchSet(selectedRow);
			} else {
				panelModel.setModelPanelEnabled(false);
			}
		}
	}

	/**
	 * 开始属性面板的批量设置
	 *
	 * @param selectedRow
	 */
	public void startPropertyBatchSet(int[] selectedRow) {
		ArrayList<NewDatasetBean> newDatasetBeans = new ArrayList();
		for (int i = 0; i < selectedRow.length; i++) {
			newDatasetBeans.add((newDatasetTableModel.getDatasetBean(selectedRow[i])));
		}
		propertyPanel.initStates(newDatasetBeans);
	}

	/**
	 * 开始模版面板的批量设置
	 *
	 * @param selectedRow
	 */
	public void startModelBatchSet(int[] selectedRow) {
		Boolean isSameModelState = true;
		Boolean modelState;
		ArrayList<NewDatasetBean> newDatasetBeans = new ArrayList();
		// 获得第一个数据是否含有模版，true为含有模版
		modelState = Boolean.valueOf(newDatasetTableModel.getDatasetBean(selectedRow[0]).getTemplateDataset() != null);
		for (int i = 0; i < selectedRow.length; i++) {
			newDatasetBeans.add((newDatasetTableModel.getDatasetBean(selectedRow[i])));
			// 判断后续数据含有模版情况是否与第一个相同
			if (modelState != Boolean.valueOf(newDatasetTableModel.getDatasetBean(selectedRow[i]).getTemplateDataset() != null)) {
				isSameModelState = false;
			}
		}
		panelModel.initStates(newDatasetBeans, isSameModelState);
	}

	private void checkButtonState() {
		buttonSetting.setEnabled(table.getSelectedRowCount() > 0);
		buttonDelete.setEnabled(table.getSelectedRow() != -1 && table.getSelectedRow() != table.getRowCount() - 1);
		buttonOk.setEnabled(table.getRowCount() > 0);
	}

	private void buttonSetting_Click() {
		TableUtilities.stopEditing(table);
		JDialogSetAll dialogSetAll = new JDialogSetAll();
		if (dialogSetAll.showDialog() == DialogResult.OK) {
			int[] selectedRows = table.getSelectedRows();
			Object dialogTargetDatasource = dialogSetAll.getTargetDatasource();
			Object datasetType = dialogSetAll.getDatasetType();
//			Object encodingType = dialogSetAll.getEncodingType();
//			Object charset = dialogSetAll.getCharset();
			Object addToMap = dialogSetAll.getAddtoMap();
			for (int i : selectedRows) {
				if (dialogTargetDatasource != null) {
					newDatasetTableModel.setValueAt(dialogTargetDatasource, i, NewDatasetTableModel.COLUMN_INDEX_TARGET_DATASOURCE);
				}
				if (datasetType != null) {
					newDatasetTableModel.setValueAt(datasetType, i, NewDatasetTableModel.COLUMN_INDEX_DATASET_TYPE);
				}
//				if (encodingType != null) {
//					newDatasetTableModel.setValueAt(encodingType, i, NewDatasetTableModel.COLUMN_INDEX_EncodeType);
//				}
//				if (charset != null) {
//					newDatasetTableModel.setValueAt(charset, i, NewDatasetTableModel.COLUMN_INDEX_Charset);
//				}
				if (addToMap != null) {
					newDatasetTableModel.setValueAt(addToMap, i, NewDatasetTableModel.COLUMN_INDEX_WINDOWMODE);
				}
			}
		}
	}

	private void createDataset() {
		newDatasetTableModel.createDatasets();
	}

	private void initComponentStates() {
		checkboxAutoClose.setSelected(true);
		newDatasetTableModel.addEmptyRow();
		newDatasetTableModel.setValueAt("", 0, NewDatasetTableModel.COLUMN_INDEX_DATASET_NAME);
	}

	public class CharsetTypeCellEditor extends DefaultCellEditor {

		private JComboBox<String> comboboxCharsetType = new JComboBox<>();

		public CharsetTypeCellEditor() {
			super(new JComboBox());
			this.setClickCountToStart(2);
			ArrayList<String> tempcharsharsetes = new ArrayList<String>();

			tempcharsharsetes.add(CharsetUtilities.toString(Charset.OEM));
			tempcharsharsetes.add(CharsetUtilities.toString(Charset.EASTEUROPE));
			tempcharsharsetes.add(CharsetUtilities.toString(Charset.THAI));
			tempcharsharsetes.add(CharsetUtilities.toString(Charset.RUSSIAN));
			tempcharsharsetes.add(CharsetUtilities.toString(Charset.BALTIC));
			tempcharsharsetes.add(CharsetUtilities.toString(Charset.ARABIC));
			tempcharsharsetes.add(CharsetUtilities.toString(Charset.HEBREW));
			tempcharsharsetes.add(CharsetUtilities.toString(Charset.VIETNAMESE));
			tempcharsharsetes.add(CharsetUtilities.toString(Charset.TURKISH));
			tempcharsharsetes.add(CharsetUtilities.toString(Charset.GREEK));
			tempcharsharsetes.add(CharsetUtilities.toString(Charset.CHINESEBIG5));
			tempcharsharsetes.add(CharsetUtilities.toString(Charset.JOHAB));
			tempcharsharsetes.add(CharsetUtilities.toString(Charset.HANGEUL));
			tempcharsharsetes.add(CharsetUtilities.toString(Charset.SHIFTJIS));
			tempcharsharsetes.add(CharsetUtilities.toString(Charset.MAC));
			tempcharsharsetes.add(CharsetUtilities.toString(Charset.SYMBOL));
			tempcharsharsetes.add(CharsetUtilities.toString(Charset.DEFAULT));
			tempcharsharsetes.add(CharsetUtilities.toString(Charset.ANSI));
			tempcharsharsetes.add(CharsetUtilities.toString(Charset.UTF8));
			tempcharsharsetes.add(CharsetUtilities.toString(Charset.UTF7));
			tempcharsharsetes.add(CharsetUtilities.toString(Charset.WINDOWS1252));
			tempcharsharsetes.add(CharsetUtilities.toString(Charset.KOREAN));
			tempcharsharsetes.add(CharsetUtilities.toString(Charset.UNICODE));
			tempcharsharsetes.add(CharsetUtilities.toString(Charset.CYRILLIC));
			tempcharsharsetes.add(CharsetUtilities.toString(Charset.XIA5));
			tempcharsharsetes.add(CharsetUtilities.toString(Charset.XIA5GERMAN));
			tempcharsharsetes.add(CharsetUtilities.toString(Charset.XIA5SWEDISH));
			tempcharsharsetes.add(CharsetUtilities.toString(Charset.XIA5NORWEGIAN));
			tempcharsharsetes.add(CharsetUtilities.toString(Charset.GB18030));
			comboboxCharsetType.setModel(new DefaultComboBoxModel<>(tempcharsharsetes.toArray(new String[tempcharsharsetes.size()])));
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
			comboboxCharsetType.removeAll();
			comboboxCharsetType.setSelectedItem(value);
			return comboboxCharsetType;
		}

		@Override
		public Object getCellEditorValue() {
			return comboboxCharsetType.getSelectedItem();
		}
	}

	public class EncodingTypeCellEditor extends DefaultCellEditor {

		private JComboBox<String> comboboxEncodingType = new JComboBox<>();

		public EncodingTypeCellEditor() {
			super(new JComboBox());
			this.setClickCountToStart(2);
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
			comboboxEncodingType.removeAll();

			Object datasetType = table.getValueAt(row, NewDatasetTableModel.COLUMN_INDEX_DATASET_TYPE);
			ArrayList<String> tempEncodeType = new ArrayList<>();
			tempEncodeType.add(EncodeTypeUtilities.toString(EncodeType.NONE));
			if (DatasetType.LINE == datasetType || DatasetType.REGION == datasetType) {
				tempEncodeType.add(EncodeTypeUtilities.toString(EncodeType.BYTE));
				tempEncodeType.add(EncodeTypeUtilities.toString(EncodeType.INT16));
				tempEncodeType.add(EncodeTypeUtilities.toString(EncodeType.INT24));
				tempEncodeType.add(EncodeTypeUtilities.toString(EncodeType.INT32));
			}
			if (DatasetType.IMAGE == datasetType) {
				tempEncodeType.add(EncodeTypeUtilities.toString(EncodeType.DCT));
				tempEncodeType.add(EncodeTypeUtilities.toString(EncodeType.LZW));
			}
			if (DatasetType.GRID == datasetType) {
				tempEncodeType.add(EncodeTypeUtilities.toString(EncodeType.DCT));
				tempEncodeType.add(EncodeTypeUtilities.toString(EncodeType.SGL));
				tempEncodeType.add(EncodeTypeUtilities.toString(EncodeType.LZW));
			}
			comboboxEncodingType.setModel(new DefaultComboBoxModel<>(tempEncodeType.toArray(new String[tempEncodeType.size()])));
			comboboxEncodingType.setSelectedItem(value);
			return comboboxEncodingType;
		}

		@Override
		public Object getCellEditorValue() {
			return comboboxEncodingType.getSelectedItem();
		}
	}
}
