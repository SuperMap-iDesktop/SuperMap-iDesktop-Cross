package com.supermap.desktop.CtrlAction.Dataset.createNewDataset;

import com.supermap.data.Charset;
import com.supermap.data.DatasetType;
import com.supermap.data.EncodeType;
import com.supermap.desktop.Application;
import com.supermap.desktop.CtrlAction.Dataset.AddToWindowMode;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.controls.utilties.ToolbarUtilties;
import com.supermap.desktop.dataeditor.DataEditorProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.CellRenders.TableDatasetTypeCellRender;
import com.supermap.desktop.ui.controls.CellRenders.TableDatasourceCellRender;
import com.supermap.desktop.ui.controls.DatasetComboBox;
import com.supermap.desktop.ui.controls.DatasourceComboBox;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.ui.controls.mutiTable.component.ComboBoxCellEditor;
import com.supermap.desktop.utilties.CharsetUtilties;
import com.supermap.desktop.utilties.EncodeTypeUtilties;
import com.supermap.desktop.utilties.TableUtilties;

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

	public JDialogDatasetNew() {
		initComponents();
		initLayout();
		initResources();
		addListeners();
		initComponentStates();
	}

	private void initComponents() {
		this.setModal(true);
		setSize(700, 420);
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


		this.table.getColumnModel().getColumn(NewDatasetTableModel.COLUMN_INDEX_TARGET_DATASOURCE).setCellRenderer(new TableDatasourceCellRender());
		this.table.getColumnModel().getColumn(NewDatasetTableModel.COLUMN_INDEX_DatasetType).setCellRenderer(new TableDatasetTypeCellRender());


		this.table.getColumnModel().getColumn(NewDatasetTableModel.COLUMN_INDEX_EncodeType).setCellEditor(new EncodingTypeCellEditor());
		this.table.getColumnModel().getColumn(NewDatasetTableModel.COLUMN_INDEX_Charset).setCellEditor(new CharsetTypeCellEditor());

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
		TableColumn targetDatasourceColumn = table.getColumnModel().getColumn(NewDatasetTableModel.COLUMN_INDEX_TARGET_DATASOURCE);
		targetDatasourceColumn.setCellEditor(targetDatasourceCellEditor);
		// 设置渲染
//		CommonListCellRenderer renderer = new CommonListCellRenderer();
//		targetDatasourceColumn.setCellRenderer(renderer);
		// 可创建的数据集类型
		ArrayList<DatasetType> datasetTypes = new ArrayList<DatasetType>();
		datasetTypes.add(DatasetType.POINT);
		datasetTypes.add(DatasetType.LINE);
		datasetTypes.add(DatasetType.REGION);
		datasetTypes.add(DatasetType.TEXT);
		datasetTypes.add(DatasetType.CAD);
		datasetTypes.add(DatasetType.TABULAR);
		datasetTypes.add(DatasetType.POINT3D);
		datasetTypes.add(DatasetType.LINE3D);
		datasetTypes.add(DatasetType.REGION3D);
		final DatasetComboBox comboBoxDatasetType = new DatasetComboBox(datasetTypes.toArray(new DatasetType[datasetTypes.size()]));
		DefaultCellEditor datasetTypeCellEditor = new DefaultCellEditor(comboBoxDatasetType);
		TableColumn datasetTypeColumn = table.getColumnModel().getColumn(NewDatasetTableModel.COLUMN_INDEX_DatasetType);
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
		TableColumn addToColumn = table.getColumnModel().getColumn(NewDatasetTableModel.COLUMN_INDEX_WindowMode);
		addToColumn.setCellEditor(addToCellEditor);
	}

	private void initLayout() {
		initToolBar();
		initPanelButton();
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.add(toolBar, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 0).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.WEST));
		panel.add(new JScrollPane(table), new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER));
		panel.add(panelButton, new GridBagConstraintsHelper(0, 2, 1, 1).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.CENTER).setInsets(5, 0, 0, 0));

		this.setLayout(new GridBagLayout());
		this.add(panel, new GridBagConstraintsHelper(0, 0, 1, 1).setFill(GridBagConstraints.BOTH).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(10));
	}

	private void initToolBar() {
		toolBar.add(buttonSelectAll);
		toolBar.add(buttonSelectInvert);
		toolBar.add(ToolbarUtilties.getVerticalSeparator());
		toolBar.add(buttonDelete);
		toolBar.add(ToolbarUtilties.getVerticalSeparator());
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
		this.buttonSelectAll.setIcon(new ImageIcon(JDialogDatasetNew.class.getResource("/com/supermap/desktop/coreresources/ToolBar/Image_ToolButton_SelectAll.png")));
		this.buttonSelectInvert.setToolTipText(CommonProperties.getString("String_ToolBar_SelectInverse"));
		this.buttonSelectInvert.setIcon(new ImageIcon(JDialogDatasetNew.class.getResource("/com/supermap/desktop/coreresources/ToolBar/Image_ToolButton_SelectInverse.png")));
		this.buttonDelete.setToolTipText(CommonProperties.getString("String_Delete"));
		this.buttonDelete.setIcon(new ImageIcon(JDialogDatasetNew.class.getResource("/com/supermap/desktop/coreresources/ToolBar/Image_ToolButton_Delete.png")));
		this.buttonSetting.setToolTipText(CommonProperties.getString("String_ToolBar_SetBatch"));
		this.buttonSetting.setIcon(new ImageIcon(JDialogDatasetNew.class.getResource("/com/supermap/desktop/coreresources/ToolBar/Image_ToolButton_Setting.PNG")));
		this.checkboxAutoClose.setText(CommonProperties.getString("String_CheckBox_CloseDialog"));
	}

	private void addListeners() {
		this.buttonOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
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
				table.getSelectionModel().setSelectionInterval(0, table.getRowCount() - 1);
			}
		});

		this.buttonSelectInvert.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				TableUtilties.invertSelection(table);
			}
		});

		this.buttonDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
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
			}
		});
	}

	private void checkButtonState() {
		buttonSetting.setEnabled(table.getSelectedRowCount() > 0);
		buttonDelete.setEnabled(table.getSelectedRow() != -1 && table.getSelectedRow() != table.getRowCount() - 1);
		buttonOk.setEnabled(table.getRowCount() > 0);
	}

	private void buttonSetting_Click() {
		JDialogSetAll dialogSetAll = new JDialogSetAll();
		if (dialogSetAll.showDialog() == DialogResult.OK) {
			int[] selectedRows = table.getSelectedRows();
			Object dialogTargetDatasource = dialogSetAll.getTargetDatasource();
			Object datasetType = dialogSetAll.getDatasetType();
			Object encodingType = dialogSetAll.getEncodingType();
			Object charset = dialogSetAll.getCharset();
			Object addToMap = dialogSetAll.getAddtoMap();
			for (int i : selectedRows) {
				if (dialogTargetDatasource != null) {
					newDatasetTableModel.setValueAt(dialogTargetDatasource, i, NewDatasetTableModel.COLUMN_INDEX_TARGET_DATASOURCE);
				}
				if (datasetType != null) {
					newDatasetTableModel.setValueAt(datasetType, i, NewDatasetTableModel.COLUMN_INDEX_DatasetType);
				}
				if (encodingType != null) {
					newDatasetTableModel.setValueAt(encodingType, i, NewDatasetTableModel.COLUMN_INDEX_EncodeType);
				}
				if (charset != null) {
					newDatasetTableModel.setValueAt(charset, i, NewDatasetTableModel.COLUMN_INDEX_Charset);
				}
				if (addToMap != null) {
					newDatasetTableModel.setValueAt(addToMap, i, NewDatasetTableModel.COLUMN_INDEX_WindowMode);
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
		newDatasetTableModel.setValueAt("", 0, NewDatasetTableModel.COLUMN_INDEX_DatasetName);
	}

	public class CharsetTypeCellEditor extends DefaultCellEditor {

		private JComboBox<String> comboboxCharsetType = new JComboBox<>();

		public CharsetTypeCellEditor() {
			super(new JComboBox());
			ArrayList<String> tempcharsharsetes = new ArrayList<String>();
			tempcharsharsetes.add(CharsetUtilties.toString(Charset.OEM));
			tempcharsharsetes.add(CharsetUtilties.toString(Charset.EASTEUROPE));
			tempcharsharsetes.add(CharsetUtilties.toString(Charset.THAI));
			tempcharsharsetes.add(CharsetUtilties.toString(Charset.RUSSIAN));
			tempcharsharsetes.add(CharsetUtilties.toString(Charset.BALTIC));
			tempcharsharsetes.add(CharsetUtilties.toString(Charset.ARABIC));
			tempcharsharsetes.add(CharsetUtilties.toString(Charset.HEBREW));
			tempcharsharsetes.add(CharsetUtilties.toString(Charset.VIETNAMESE));
			tempcharsharsetes.add(CharsetUtilties.toString(Charset.TURKISH));
			tempcharsharsetes.add(CharsetUtilties.toString(Charset.GREEK));
			tempcharsharsetes.add(CharsetUtilties.toString(Charset.CHINESEBIG5));
			tempcharsharsetes.add(CharsetUtilties.toString(Charset.JOHAB));
			tempcharsharsetes.add(CharsetUtilties.toString(Charset.HANGEUL));
			tempcharsharsetes.add(CharsetUtilties.toString(Charset.SHIFTJIS));
			tempcharsharsetes.add(CharsetUtilties.toString(Charset.MAC));
			tempcharsharsetes.add(CharsetUtilties.toString(Charset.SYMBOL));
			tempcharsharsetes.add(CharsetUtilties.toString(Charset.DEFAULT));
			tempcharsharsetes.add(CharsetUtilties.toString(Charset.ANSI));
			tempcharsharsetes.add(CharsetUtilties.toString(Charset.UTF8));
			tempcharsharsetes.add(CharsetUtilties.toString(Charset.UTF7));
			tempcharsharsetes.add(CharsetUtilties.toString(Charset.WINDOWS1252));
			tempcharsharsetes.add(CharsetUtilties.toString(Charset.KOREAN));
			tempcharsharsetes.add(CharsetUtilties.toString(Charset.UNICODE));
			tempcharsharsetes.add(CharsetUtilties.toString(Charset.CYRILLIC));
			tempcharsharsetes.add(CharsetUtilties.toString(Charset.XIA5));
			tempcharsharsetes.add(CharsetUtilties.toString(Charset.XIA5GERMAN));
			tempcharsharsetes.add(CharsetUtilties.toString(Charset.XIA5SWEDISH));
			tempcharsharsetes.add(CharsetUtilties.toString(Charset.XIA5NORWEGIAN));
			tempcharsharsetes.add(CharsetUtilties.toString(Charset.GB18030));
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
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
			comboboxEncodingType.removeAll();

			Object datasetType = table.getValueAt(row, NewDatasetTableModel.COLUMN_INDEX_DatasetType);
			ArrayList<String> tempEncodeType = new ArrayList<>();
			tempEncodeType.add(EncodeTypeUtilties.toString(EncodeType.NONE));
			if (DatasetType.LINE == datasetType || DatasetType.REGION == datasetType) {
				tempEncodeType.add(EncodeTypeUtilties.toString(EncodeType.BYTE));
				tempEncodeType.add(EncodeTypeUtilties.toString(EncodeType.INT16));
				tempEncodeType.add(EncodeTypeUtilties.toString(EncodeType.INT24));
				tempEncodeType.add(EncodeTypeUtilties.toString(EncodeType.INT32));
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
