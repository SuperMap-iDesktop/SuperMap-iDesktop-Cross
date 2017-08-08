package com.supermap.desktop.ui;

import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.JFileChooserControl;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.ui.controls.table.SmTable;

import javax.swing.*;
import java.awt.*;

/**
 * @author XiaJT
 */
public class JDialogOutputExcel extends SmDialog {

	private JToolBar toolBar = new JToolBar();
	private SmButton buttonSelectAll = new SmButton();
	private SmButton buttonSelectInvert = new SmButton();
	private SmButton buttonSelectSystemField = new SmButton();
	private SmButton buttonSelectUnSystemField = new SmButton();

	private JFileChooserControl fileChooserControl = new JFileChooserControl();

	private JCheckBox checkBoxOnlySaveSelectedRow = new JCheckBox();
	private SmButton buttonOk = new SmButton();
	private SmButton buttonCancle = new SmButton();

	private SmTable table = new SmTable();
	private TableModelOutputExcel tableModelOutputExcel = new TableModelOutputExcel();

	public JDialogOutputExcel() {
		initComponents();
		initLayouts();
		initListeners();
		initComponentStates();
	}

	private void initComponents() {
		toolBar.add(buttonSelectAll);
		toolBar.add(buttonSelectInvert);
		toolBar.add(buttonSelectSystemField);
		toolBar.add(buttonSelectUnSystemField);


	}

	private void initLayouts() {
		setLayout(new GridBagLayout());
		this.add(toolBar, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 0).setInsets(10, 10, 0, 10).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.WEST));
		this.add(table, new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(1, 1).setInsets(5, 10, 0, 10).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER));
		this.add(fileChooserControl, new GridBagConstraintsHelper(0, 2, 1, 1).setWeight(1, 0).setInsets(5, 10, 0, 10).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE));
	}

	private void initListeners() {

	}

	private void initComponentStates() {

	}

}
