package com.supermap.desktop.ui;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IFormTabular;
import com.supermap.desktop.controls.utilities.ComponentFactory;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.tabularview.TabularViewProperties;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.JFileChooserControl;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.ui.controls.smTables.tables.TableFieldNameCaptionType;
import com.supermap.desktop.utilities.CoreResources;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author XiaJT
 */
public class JDialogOutputExcel extends SmDialog {

	//region控件
	private JToolBar toolBar;
	private SmButton buttonSelectAll;
	private SmButton buttonSelectInverse;
	private SmButton buttonSelectSystemField;
	private SmButton buttonSelectUnSystemField;
	private JScrollPane scrollPane;
	private JLabel labelFileName;
	private JLabel labelFilePath;
	private JTextField textFieldFileName;
	private JFileChooserControl fileChooserControl = new JFileChooserControl();
	private JCheckBox checkBoxOnlySaveSelectedRow = new JCheckBox();
	private SmButton buttonOk = new SmButton();
	private SmButton buttonCancel = new SmButton();
	private TableFieldNameCaptionType table;
	private IFormTabular tabular;
	//endregion

	//region监听
	private ActionListener actionListenerSelectAll = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			table.getTableController().selectAll(table);
		}
	};

	private ActionListener actionListenerSelectInverse = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			table.getTableController().selectInverse(table);
		}
	};

	private ActionListener actionListenerSelectSystemField = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			table.getTableController().selectSystemField(table);
		}
	};

	private ActionListener actionListenerSelectUnSystemField = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			table.getTableController().selectUnSystemField(table);
		}
	};

	private ActionListener actionListenerOK = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			execute();
		}
	};

	private ActionListener actionListenerCancel = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			dispose();
		}
	};

	//endregion

	public JDialogOutputExcel(IFormTabular tabular) {
		this.tabular = tabular;
		initDialogSize();
		initComponents();
		initResources();
		initLayouts();
		removeListeners();
		registerListeners();
		initComponentStates();
	}

	private void initResources() {
		buttonSelectAll.setToolTipText(CommonProperties.getString("String_ToolBar_SelectAll"));
		buttonSelectInverse.setToolTipText(CommonProperties.getString("String_ToolBar_SelectInverse"));
		buttonSelectSystemField.setToolTipText(CommonProperties.getString("String_ToolBar_SelectSystemField"));
		buttonSelectUnSystemField.setToolTipText(CommonProperties.getString("String_ToolBar_SelectUnSystemField"));

		buttonSelectAll.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_SelectAll.png"));
		buttonSelectInverse.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_SelectInverse.png"));
		buttonSelectSystemField.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_SelectSystemField.png"));
		buttonSelectUnSystemField.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_SelectNonSystemField.png"));

		labelFileName.setText(TabularViewProperties.getString("String_FileName"));
		labelFilePath.setText(TabularViewProperties.getString("String_Path"));

		checkBoxOnlySaveSelectedRow.setText(TabularViewProperties.getString("String_SaveSelectedRowOnly"));
	}

	private void initComponents() {
		buttonSelectAll = new SmButton();
		buttonSelectInverse = new SmButton();
		buttonSelectSystemField = new SmButton();
		buttonSelectUnSystemField = new SmButton();
		buttonOk = (SmButton) ComponentFactory.createButtonOK();
		buttonCancel = (SmButton) ComponentFactory.createButtonCancel();

		labelFileName = new JLabel();
		labelFilePath = new JLabel();
		textFieldFileName = new JTextField();
		checkBoxOnlySaveSelectedRow = new JCheckBox();

		toolBar = new JToolBar();
		toolBar.add(buttonSelectAll);
		toolBar.add(buttonSelectInverse);
		toolBar.addSeparator();
		toolBar.add(buttonSelectSystemField);
		toolBar.add(buttonSelectUnSystemField);

		table = new TableFieldNameCaptionType();
		scrollPane = new JScrollPane();
		scrollPane.setViewportView(table);
	}

	private void initLayouts() {
		setLayout(new GridBagLayout());
		this.add(toolBar, new GridBagConstraintsHelper(0, 0, 2, 1).setWeight(1, 0).setInsets(10, 10, 0, 10).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.WEST));
		this.add(scrollPane, new GridBagConstraintsHelper(0, 1, 2, 1).setWeight(1, 1).setInsets(5, 10, 0, 10).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER));
		this.add(getPanelFile(), new GridBagConstraintsHelper(0, 2, 2, 1).setWeight(1, 0).setInsets(5, 10, 0, 10).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.BOTH));
		this.add(checkBoxOnlySaveSelectedRow, new GridBagConstraintsHelper(0, 3, 1, 1).setWeight(1, 0).setInsets(5, 10, 10, 5).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE));
		this.add(getPanelButton(), new GridBagConstraintsHelper(1, 3, 1, 1).setWeight(1, 0).setInsets(5, 5, 10, 10).setAnchor(GridBagConstraints.EAST).setFill(GridBagConstraints.NONE));
	}

	private void removeListeners() {
		buttonSelectAll.removeActionListener(actionListenerSelectAll);
		buttonSelectInverse.removeActionListener(actionListenerSelectInverse);
		buttonSelectSystemField.removeActionListener(actionListenerSelectSystemField);
		buttonSelectUnSystemField.removeActionListener(actionListenerSelectUnSystemField);
		buttonOk.removeActionListener(actionListenerOK);
		buttonCancel.removeActionListener(actionListenerCancel);
	}

	private void registerListeners() {
		buttonSelectAll.addActionListener(actionListenerSelectAll);
		buttonSelectInverse.addActionListener(actionListenerSelectInverse);
		buttonSelectSystemField.addActionListener(actionListenerSelectSystemField);
		buttonSelectUnSystemField.addActionListener(actionListenerSelectUnSystemField);
		buttonOk.addActionListener(actionListenerOK);
		buttonCancel.addActionListener(actionListenerCancel);
	}

	private void initComponentStates() {
		table.setShowSystemField(true);
		table.setDataset(tabular.getDataset());
		checkBoxOnlySaveSelectedRow.setSelected(true);
		textFieldFileName.setText(tabular.getDataset().getName());
	}

	private void initDialogSize() {
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
	}

	private JPanel getPanelButton() {
		JPanel panelButton = new JPanel();
		this.buttonOk.setEnabled(true);
		panelButton.setLayout(new GridBagLayout());
		panelButton.add(this.buttonOk, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.EAST).setFill(GridBagConstraints.NONE).setWeight(0, 0).setInsets(10, 5, 10, 5));
		panelButton.add(this.buttonCancel, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.EAST).setFill(GridBagConstraints.NONE).setWeight(1, 0).setInsets(10, 5, 10, 10));
		return panelButton;
	}

	private JPanel getPanelFile() {
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder(TabularViewProperties.getString("String_FileInfo")));
		panel.setLayout(new GridBagLayout());
		panel.add(labelFileName, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(0, 0).setInsets(10, 10, 5, 5).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.WEST));
		panel.add(textFieldFileName, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(1, 0).setInsets(10, 5, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.WEST));
		panel.add(labelFilePath, new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(0, 0).setInsets(5, 10, 10, 5).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.WEST));
		panel.add(fileChooserControl, new GridBagConstraintsHelper(1, 1, 1, 1).setWeight(1, 0).setInsets(5, 5, 10, 10).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.WEST));
		return panel;
	}

	private boolean execute() {
		boolean isSuccessful = false;
		try {
			//TODO
			//需要openXML
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}

		return isSuccessful;
	}
}

