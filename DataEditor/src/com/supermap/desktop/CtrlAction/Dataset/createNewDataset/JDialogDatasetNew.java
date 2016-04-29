package com.supermap.desktop.CtrlAction.Dataset.createNewDataset;

import com.supermap.desktop.controls.utilties.ToolbarUtilties;
import com.supermap.desktop.dataeditor.DataEditorProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.utilties.TableUtilties;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
		setSize(677, 405);
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
		this.table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		this.newDatasetTableModel = new NewDatasetTableModel();
		this.table.setModel(newDatasetTableModel);
	}

	private void initLayout() {
		initToolBar();
		initPanelButton();
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.add(toolBar, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 0).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.WEST));
		panel.add(new JScrollPane(table), new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER));
		panel.add(panelButton, new GridBagConstraintsHelper(0, 2, 1, 1).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.CENTER));

		this.setLayout(new GridBagLayout());
		this.add(panel, new GridBagConstraintsHelper(0, 0, 1, 1).setFill(GridBagConstraints.BOTH).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER));
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
		int selectedRowCount = table.getSelectedRowCount();
		buttonSelectAll.setEnabled(selectedRowCount > 0);
		buttonDelete.setEnabled(table.getSelectedRow() != -1 && table.getSelectedRow() != table.getRowCount() - 1);
		buttonOk.setEnabled(table.getRowCount() > 0);
	}

	private void buttonSetting_Click() {
		JDialogSetAll dialogSetAll = new JDialogSetAll();
		if (dialogSetAll.getDialogResult() == DialogResult.OK) {
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
					newDatasetTableModel.setValueAt(encodingType, i, NewDatasetTableModel.COLUMN_INDEX_Charset);
				}
				if (addToMap != null) {
					newDatasetTableModel.setValueAt(encodingType, i, NewDatasetTableModel.COLUMN_INDEX_WindowMode);
				}
			}
		}
	}

	private void createDataset() {
		// TODO: 2016/4/28 新建数据集
	}

	private void initComponentStates() {
		checkboxAutoClose.setSelected(true);
		newDatasetTableModel.addEmptyRow();
	}
}
