package com.supermap.desktop.ui;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.Interface.IFormTabular;
import com.supermap.desktop.controls.utilities.ComponentUIUtilities;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.tabularview.TabularViewProperties;
import com.supermap.desktop.ui.controls.DatasourceComboBox;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.ui.controls.smTables.TableFactory;
import com.supermap.desktop.ui.controls.smTables.tables.TableFieldNameCaptionType;
import com.supermap.desktop.utilities.CoreResources;
import com.supermap.desktop.utilities.EncodeTypeUtilities;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.*;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lixiaoyao on 2017/8/9.
 */
public class JDialogOutputDataset extends SmDialog {

	private JToolBar toolBar;
	private JButton buttonSelectAll;
	private JButton buttonSelectInverse;
	//private JButton buttonDelete;
	private JButton buttonSelectAllSystemField;
	private JButton buttonSelectAllNonSystemField;
	private JScrollPane scrollPane;
	private JPanel panelSetting;
	private JLabel labelDatasource;
	private DatasourceComboBox datasourceComboBox;
	private JLabel labelDataset;
	private JTextField smTextFieldLegit;
	private JLabel labelResultType;
	private JComboBox comboBoxResultType;
	private JLabel labelCodeType;
	private JComboBox comboBoxCodeType;
	private JCheckBox checkBoxIsSaveRows;
	private SmButton buttonOK;
	private SmButton buttonCancel;
	private TableFieldNameCaptionType tableFieldNameCaptionType;
	//private TableFieldName tableFieldName;
	private IFormTabular tabular;
	private final String urlStr = "/coreresources/ToolBar/";
	private static final int MIN_SIZE = 23;
	private final Color WORNINGCOLOR = Color.red;
	private final Color DEFUALTCOLOR = Color.black;

	public JDialogOutputDataset(IFormTabular tabular) {
		super();
		this.tabular = tabular;
		setTitle(TabularViewProperties.getString("String_FormText_OutputDataset"));
		setSize(620, 420);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = this.getSize();

		if (frameSize.height > screenSize.height) {
			frameSize.height = screenSize.height;
		}

		if (frameSize.width > screenSize.width) {
			frameSize.width = screenSize.width;
		}
		this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2 - 200);
		initComponents();
		setComponentName();
		initLayout();
		initResources();
		removeEvents();
		registEvents();
	}

	private void initComponents() {
		this.toolBar = new JToolBar();
		this.buttonSelectAll = new JButton();
		this.buttonSelectInverse = new JButton();
		//this.buttonDelete = new JButton();
		this.buttonSelectAllSystemField = new JButton();
		this.buttonSelectAllNonSystemField = new JButton();
		this.scrollPane = new JScrollPane();
		this.panelSetting = new JPanel();
		this.labelDatasource = new JLabel();
		this.datasourceComboBox = new DatasourceComboBox();
		this.labelDataset = new JLabel();
		this.smTextFieldLegit = new JTextField();
		this.labelResultType = new JLabel();
		this.comboBoxResultType = new JComboBox();
		this.labelCodeType = new JLabel();
		this.comboBoxCodeType = new JComboBox();
		this.checkBoxIsSaveRows = new JCheckBox();
		this.buttonOK = new SmButton();
		this.buttonCancel = new SmButton();
		this.tableFieldNameCaptionType = (TableFieldNameCaptionType) TableFactory.getTable("FieldNameCaptionType");
		this.tableFieldNameCaptionType.setShowSystemField(true);
		this.tableFieldNameCaptionType.setDataset(this.tabular.getDataset());
		//this.tableFieldName = (TableFieldName) TableFactory.getTable("FieldName");
		//this.tableFieldName.setDatasetVector( this.tabular.getDataset());
		this.scrollPane.setViewportView(this.tableFieldNameCaptionType);
		this.checkBoxIsSaveRows.setSelected(true);
		this.smTextFieldLegit.setText(this.datasourceComboBox.getSelectedDatasource().getDatasets().getAvailableDatasetName("NewDataset"));
		this.comboBoxResultType.addItem(CommonProperties.getString("String_AttrsTable"));
		String datasetTypeName = CommonToolkit.DatasetTypeWrap.findName(this.tabular.getDataset().getType());
		if (!datasetTypeName.replace(CommonProperties.getString("String_JustDataset"), "").equals(CommonProperties.getString("String_AttrsTable"))) {
			this.comboBoxResultType.addItem(datasetTypeName.replace(CommonProperties.getString("String_JustDataset"), ""));
		}
		changCodeType();
		hideSystemField();
	}

	private void setComponentName() {
		ComponentUIUtilities.setName(this.toolBar, "JDialogOutputDataset_toolBar");
		ComponentUIUtilities.setName(this.buttonSelectAll, "JDialogOutputDataset_buttonSelectAll");
		ComponentUIUtilities.setName(this.buttonSelectInverse, "JDialogOutputDataset_buttonSelectInverse");
		//ComponentUIUtilities.setName(this.buttonDelete, "JDialogOutputDataset_buttonDelete");
		ComponentUIUtilities.setName(this.scrollPane, "JDialogOutputDataset_scrollPane");
		ComponentUIUtilities.setName(this.tableFieldNameCaptionType, "JDialogOutputDataset_tableFieldNameCaptionType");
		ComponentUIUtilities.setName(this.panelSetting, "JDialogOutputDataset_panelSetting");
		ComponentUIUtilities.setName(this.labelDatasource, "JDialogOutputDataset_labelDatasource");
		ComponentUIUtilities.setName(this.datasourceComboBox, "JDialogOutputDataset_datasourceComboBox");
		ComponentUIUtilities.setName(this.labelDataset, "JDialogOutputDataset_labelDataset");
		ComponentUIUtilities.setName(this.smTextFieldLegit, "JDialogOutputDataset_smTextFieldLegit");
		ComponentUIUtilities.setName(this.labelResultType, "JDialogOutputDataset_labelResultType");
		ComponentUIUtilities.setName(this.comboBoxResultType, "JDialogOutputDataset_comboBoxResultType");
		ComponentUIUtilities.setName(this.labelCodeType, "JDialogOutputDataset_labelCodeType");
		ComponentUIUtilities.setName(this.comboBoxCodeType, "JDialogOutputDataset_comboBoxCodeType");
		ComponentUIUtilities.setName(this.checkBoxIsSaveRows, "JDialogOutputDataset_checkBoxIsSaveRows");
		ComponentUIUtilities.setName(this.buttonOK, "JDialogOutputDataset_buttonOK");
		ComponentUIUtilities.setName(this.buttonCancel, "JDialogOutputDataset_buttonCancel");
		ComponentUIUtilities.setName(this.buttonSelectAllSystemField, "JDialogOutputDataset_buttonSelectAllSystemField");
		ComponentUIUtilities.setName(this.buttonSelectAllNonSystemField, "JDialogOutputDataset_buttonSelectAllNonSystemField");
	}

	private void initLayout() {
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setAutoCreateGaps(true);
		groupLayout.setAutoCreateContainerGaps(true);

		groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(this.toolBar)
						.addComponent(this.scrollPane)
						.addComponent(this.panelSetting)
						.addGroup(groupLayout.createSequentialGroup()
								.addComponent(this.checkBoxIsSaveRows)
								.addGap(320, 320, Short.MAX_VALUE)
								.addComponent(this.buttonOK)
								.addComponent(this.buttonCancel)
						)
				)
		);

		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
				.addComponent(this.toolBar, 30, 30, 30)
				.addComponent(this.scrollPane)
				.addComponent(this.panelSetting)
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(this.checkBoxIsSaveRows)
						.addComponent(this.buttonOK)
						.addComponent(this.buttonCancel)
				)
		);

		this.toolBar.add(this.buttonSelectAll);
		this.toolBar.add(this.buttonSelectInverse);
		//this.toolBar.add(this.buttonDelete);
		this.toolBar.addSeparator();
		this.toolBar.add(this.buttonSelectAllSystemField);
		this.toolBar.add(this.buttonSelectAllNonSystemField);
		this.toolBar.setFloatable(false);
		getContentPane().setLayout(groupLayout);
		initPanelSettingLayout();
	}

	private void initPanelSettingLayout() {
		GroupLayout groupLayout = new GroupLayout(this.panelSetting);
		this.panelSetting.setBorder(BorderFactory.createTitledBorder(CommonProperties.getString("String_ResultDatasetInfo")));
		groupLayout.setAutoCreateGaps(true);
		groupLayout.setAutoCreateContainerGaps(true);

		groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(this.labelDatasource)
						.addComponent(this.labelDataset))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(this.datasourceComboBox)
						.addComponent(this.smTextFieldLegit))
				.addGap(35)
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(this.labelResultType)
						.addComponent(this.labelCodeType))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(this.comboBoxResultType)
						.addComponent(this.comboBoxCodeType))
		);
		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(this.labelDatasource)
						.addComponent(this.datasourceComboBox, MIN_SIZE, MIN_SIZE, MIN_SIZE)
						.addComponent(this.labelResultType)
						.addComponent(this.comboBoxResultType, MIN_SIZE, MIN_SIZE, MIN_SIZE))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(this.labelDataset)
						.addComponent(this.smTextFieldLegit, MIN_SIZE, MIN_SIZE, MIN_SIZE)
						.addComponent(this.labelCodeType)
						.addComponent(this.comboBoxCodeType, MIN_SIZE, MIN_SIZE, MIN_SIZE))
		);

		this.panelSetting.setLayout(groupLayout);
	}

	private void initResources() {
		this.buttonSelectAll.setIcon(CoreResources.getIcon(urlStr + "Image_ToolButton_SelectAll.png"));
		this.buttonSelectAll.setToolTipText(CommonProperties.getString("String_ToolBar_SelectAll"));
		this.buttonSelectInverse.setIcon(CoreResources.getIcon(urlStr + "Image_ToolButton_SelectInverse.png"));
		this.buttonSelectInverse.setToolTipText(CommonProperties.getString("String_ToolBar_SelectInverse"));
		//this.buttonDelete.setIcon(CoreResources.getIcon(urlStr + "Image_ToolButton_Delete.png"));
		//this.buttonDelete.setToolTipText(CommonProperties.getString("String_Delete"));
		this.buttonSelectAllSystemField.setIcon(CoreResources.getIcon(urlStr + "Image_ToolButton_SelectSystemField.png"));
		this.buttonSelectAllSystemField.setToolTipText(CommonProperties.getString("String_SelectAllSystemField"));
		this.buttonSelectAllNonSystemField.setIcon(CoreResources.getIcon(urlStr + "Image_ToolButton_SelectNonSystemField.png"));
		this.buttonSelectAllNonSystemField.setToolTipText(CommonProperties.getString("String_SelectAllNonSystemField"));
		this.labelDatasource.setText(CommonProperties.getString("String_SourceDatasource"));
		this.labelDataset.setText(CommonProperties.getString("String_Dataset"));
		this.labelResultType.setText(CommonProperties.getString("String_ResultDatasetType"));
		this.labelCodeType.setText(CommonProperties.getString("String_CodeType"));
		this.checkBoxIsSaveRows.setText(CommonProperties.getString("String_OnlySaveSelectedRows"));
		this.buttonOK.setText(CommonProperties.getString("String_FormEdgeCount_OK"));
		this.buttonCancel.setText(CommonProperties.getString("String_Button_Cancel"));
	}

	private void registEvents() {
		this.buttonSelectAll.addActionListener(this.actionListenerSelectAll);
		this.buttonSelectInverse.addActionListener(this.actionListenerSelectInverse);
		//this.buttonDelete.addActionListener(this.actionListenerDelete);
		this.buttonSelectAllSystemField.addActionListener(this.actionListenerSelectSystemField);
		this.buttonSelectAllNonSystemField.addActionListener(this.actionListenerSelectNonSystemField);
		this.smTextFieldLegit.addCaretListener(this.textFieldTargetDatasetCaretListener);
		this.comboBoxResultType.addItemListener(this.comboBoxResultTypeListener);
		this.buttonOK.addActionListener(this.actionListenerRun);
		this.buttonCancel.addActionListener(this.actionListenerCancel);
	}

	private void removeEvents() {
		this.buttonSelectAll.removeActionListener(this.actionListenerSelectAll);
		this.buttonSelectInverse.removeActionListener(this.actionListenerSelectInverse);
		//this.buttonDelete.removeActionListener(this.actionListenerDelete);
		this.buttonSelectAllSystemField.removeActionListener(this.actionListenerSelectSystemField);
		this.buttonSelectAllNonSystemField.removeActionListener(this.actionListenerSelectNonSystemField);
		this.smTextFieldLegit.removeCaretListener(this.textFieldTargetDatasetCaretListener);
		this.comboBoxResultType.removeItemListener(this.comboBoxResultTypeListener);
		this.buttonOK.removeActionListener(this.actionListenerRun);
		this.buttonCancel.removeActionListener(this.actionListenerCancel);
	}

	private void changCodeType() {
		this.comboBoxCodeType.removeAllItems();
		if (this.comboBoxResultType.getSelectedItem().equals(CommonProperties.getString("String_AttrsTable"))
				|| (this.tabular.getDataset().getType()!=DatasetType.LINE && this.tabular.getDataset().getType()!=DatasetType.REGION)) {
			this.comboBoxCodeType.addItem(EncodeTypeUtilities.toString(EncodeType.NONE));
		} else {
			this.comboBoxCodeType.addItem(EncodeTypeUtilities.toString(EncodeType.BYTE));
			this.comboBoxCodeType.addItem(EncodeTypeUtilities.toString(EncodeType.INT16));
			this.comboBoxCodeType.addItem(EncodeTypeUtilities.toString(EncodeType.INT24));
			this.comboBoxCodeType.addItem(EncodeTypeUtilities.toString(EncodeType.INT32));
			this.comboBoxCodeType.addItem(EncodeTypeUtilities.toString(EncodeType.NONE));
			this.comboBoxCodeType.setSelectedItem(EncodeTypeUtilities.toString(EncodeType.NONE));
		}
	}

	private void changeJtoolbar() {
		if (this.comboBoxResultType.getSelectedItem().equals(CommonProperties.getString("String_AttrsTable"))) {
			this.buttonSelectAllSystemField.setVisible(true);
			this.buttonSelectAllNonSystemField.setVisible(true);
		} else {
			this.buttonSelectAllSystemField.setVisible(false);
			this.buttonSelectAllNonSystemField.setVisible(false);
		}
	}

	private void hideSystemField() {
		if (this.comboBoxResultType.getSelectedItem().equals(CommonProperties.getString("String_AttrsTable"))) {
			this.tableFieldNameCaptionType.setShowSystemField(true);
		} else {
			this.tableFieldNameCaptionType.setShowSystemField(false);
		}
		this.tableFieldNameCaptionType.setDataset(this.tabular.getDataset());
		this.tableFieldNameCaptionType.getTableController().selectedNonSystemField(this.tableFieldNameCaptionType);
	}

	private void isCanRun() {
		boolean result = false;
		if (this.datasourceComboBox.getSelectedDatasource().getDatasets().isAvailableDatasetName(this.smTextFieldLegit.getText())) {
			result = true;
		}
		this.buttonOK.setEnabled(result);
	}

	private void run() {
		boolean isSuccessful = false;
		String resultName="";
		try {
			this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
			DatasetVectorInfo datasetVectorInfo = new DatasetVectorInfo();
			if (this.comboBoxResultType.getSelectedItem().equals(CommonProperties.getString("String_AttrsTable"))) {
				datasetVectorInfo.setType(DatasetType.TABULAR);
			} else {
				datasetVectorInfo.setType(this.tabular.getDataset().getType());
			}
			resultName=this.smTextFieldLegit.getText();
			datasetVectorInfo.setName(resultName);
			datasetVectorInfo.setEncodeType(EncodeTypeUtilities.valueOf(this.comboBoxCodeType.getSelectedItem().toString()));
			DatasetVector result = this.datasourceComboBox.getSelectedDatasource().getDatasets().create(datasetVectorInfo);
			isSuccessful = (result != null);

			if (this.tableFieldNameCaptionType.getSelectedFields() != null && this.tableFieldNameCaptionType.getSelectedFields().length != 0) {
				FieldInfos resultFieldInfos = result.getFieldInfos();
				FieldInfo[] selectedFieldInfo = this.tableFieldNameCaptionType.getSelectedFields();
				for (int i = 0; i < selectedFieldInfo.length; i++) {
					if (!selectedFieldInfo[i].getName().equals("SmID") &&!selectedFieldInfo[i].getName().equals("SmUserID")) {
						resultFieldInfos.add(selectedFieldInfo[i]);
					}
				}
			}

			Recordset originRecordset = this.tabular.getDataset().getRecordset(false,CursorType.STATIC);
			Recordset newRecordset = result.getRecordset(false, CursorType.DYNAMIC);
			newRecordset.getBatch().setMaxRecordCount(2000);
			newRecordset.getBatch().begin();
			Geometry geometry=null;


			if (this.checkBoxIsSaveRows.isSelected()) {
				int[] rows = this.tabular.getSelectedRows();
				for (int i = 0; i < rows.length; i++) {
					originRecordset.moveTo(rows[i]);
					if (result.getType()!=DatasetType.TABULAR){
						geometry=originRecordset.getGeometry().clone();
					}
					newRecordset.addNew(geometry, getFieldValues(originRecordset,newRecordset));
				}
			} else {
				originRecordset.moveFirst();
				while (!originRecordset.isEOF()){
					if (result.getType()!=DatasetType.TABULAR){
						geometry=originRecordset.getGeometry().clone();
					}
					newRecordset.addNew(geometry, getFieldValues(originRecordset,newRecordset));
					originRecordset.moveNext();
				}
			}
			newRecordset.getBatch().update();
			newRecordset.close();
			newRecordset.dispose();
			originRecordset.close();
			originRecordset.dispose();
		} catch (Exception e) {
			isSuccessful = false;
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			String outputMessage="";
			if (isSuccessful) {
				outputMessage=MessageFormat.format(CommonProperties.getString("String_SaveAsDataset_Success"),this.datasourceComboBox.getSelectedDatasource().getAlias(),
						resultName);
			} else {
				outputMessage=MessageFormat.format(CommonProperties.getString("String_SaveAsDataset_Failed"),this.datasourceComboBox.getSelectedDatasource().getAlias(),
						resultName);
			}
			Application.getActiveApplication().getOutput().output(outputMessage);
		}
	}

	private void cancelAndCloseDailog() {
		this.dispose();
	}

	public Map<String, Object> getFieldValues(Recordset recordset,Recordset newRecordset) {
		Map<String, Object> fieldValues = new HashMap<>();
		FieldInfos fieldInfos = newRecordset.getFieldInfos();

		for (int i = 0; i < fieldInfos.getCount(); i++) {
			try {
				FieldInfo fieldInfo = fieldInfos.get(i);
				if (!fieldInfo.isSystemField()) {
					fieldValues.put(fieldInfo.getName(), recordset.getFieldValue(fieldInfo.getName()));
				}
			}catch (Exception e){
				continue;
			}

		}
		return fieldValues;
	}

	private ActionListener actionListenerSelectAll = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			tableFieldNameCaptionType.getTableController().selectedAll(tableFieldNameCaptionType);
			//tableFieldName.getTableController().selectedAll(tableFieldName);
		}
	};

	private ActionListener actionListenerSelectInverse = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			tableFieldNameCaptionType.getTableController().selectedIInverse(tableFieldNameCaptionType);
			//tableFieldName.getTableController().selectedIInverse(tableFieldName);
		}
	};

	private ActionListener actionListenerSelectSystemField = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			tableFieldNameCaptionType.getTableController().selectedSystemField(tableFieldNameCaptionType);
		}
	};

	private ActionListener actionListenerSelectNonSystemField = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			tableFieldNameCaptionType.getTableController().selectedNonSystemField(tableFieldNameCaptionType);
		}
	};

	private ActionListener actionListenerDelete = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			tableFieldNameCaptionType.getTableController().delete(tableFieldNameCaptionType);
			//tableFieldName.getTableController().delete(tableFieldName);
		}
	};

	private ActionListener actionListenerRun = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			run();
		}
	};

	private ActionListener actionListenerCancel = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			cancelAndCloseDailog();
		}
	};

	private CaretListener textFieldTargetDatasetCaretListener = new CaretListener() {
		@Override
		public void caretUpdate(CaretEvent e) {
			Datasource datasource = datasourceComboBox.getSelectedDatasource();
			String text = smTextFieldLegit.getText();
			if (null != datasource && null != datasource.getDatasets()) {
				Datasets datasets = datasource.getDatasets();
				if (!datasets.getAvailableDatasetName(text).equals(text)) {
					smTextFieldLegit.setForeground(WORNINGCOLOR);
				} else {
					smTextFieldLegit.setForeground(DEFUALTCOLOR);
				}
				isCanRun();
			}
		}
	};

	private ItemListener comboBoxResultTypeListener = new ItemListener() {
		@Override
		public void itemStateChanged(ItemEvent e) {
			changCodeType();
			changeJtoolbar();
			hideSystemField();
		}
	};

}
