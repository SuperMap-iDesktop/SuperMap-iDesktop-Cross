package com.supermap.desktop.dialog.ColorSchemeDialogs;

import com.supermap.desktop.controls.utilties.ToolbarUtilties;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.button.SmButton;

import javax.swing.*;

/**
 * @author XiaJT
 */
public class JDialogColorScheme extends SmDialog {

	JToolBar toolBar;
	SmButton buttonAdd;
	SmButton buttonReset;
	SmButton buttonEdit;
	SmButton buttonRevert;
	SmButton buttonDel;
	SmButton buttonSelectAll;
	SmButton buttonSelectInvert;
	SmButton buttonImport;
	SmButton buttonExport;
	JTableColorScheme tableColorScheme;
	JPanel panelButton;
	SmButton buttonOk;
	SmButton buttonCancle;

	public JDialogColorScheme() {
		initComponents();
		initLayout();
		initListeners();
		initResources();
		initComponentState();
	}

	private void initComponents() {
		toolBar = new JToolBar();
		buttonAdd = new SmButton();
		buttonAdd.setUseDefaultSize(false);
		buttonReset = new SmButton();
		buttonReset.setUseDefaultSize(false);
		buttonEdit = new SmButton();
		buttonEdit.setUseDefaultSize(false);
		buttonRevert = new SmButton();
		buttonRevert.setUseDefaultSize(false);
		buttonDel = new SmButton();
		buttonDel.setUseDefaultSize(false);
		buttonSelectAll = new SmButton();
		buttonSelectAll.setUseDefaultSize(false);
		buttonSelectInvert = new SmButton();
		buttonSelectInvert.setUseDefaultSize(false);
		buttonImport = new SmButton();
		buttonImport.setUseDefaultSize(false);
		buttonExport = new SmButton();
		buttonExport.setUseDefaultSize(false);
		tableColorScheme = new JTableColorScheme();
		panelButton = new JPanel();
		buttonOk = new SmButton();
		buttonCancle = new SmButton();
	}

	private void initLayout() {
		initToolBarLayout();
		
	}

	private void initToolBarLayout() {
		toolBar.add(buttonAdd);
		toolBar.add(buttonReset);
		toolBar.add(buttonEdit);
		toolBar.add(ToolbarUtilties.getVerticalSeparator());
		toolBar.add(buttonRevert);
		toolBar.add(buttonDel);
		toolBar.add(ToolbarUtilties.getVerticalSeparator());
		toolBar.add(buttonSelectAll);
		toolBar.add(buttonSelectInvert);
		toolBar.add(ToolbarUtilties.getVerticalSeparator());
		toolBar.add(buttonImport);
		toolBar.add(buttonExport);

	}

	private void initListeners() {

	}

	private void initResources() {

	}

	private void initComponentState() {
	}
}
