package com.supermap.desktop.dialog.ColorSchemeDialogs;

import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.colorScheme.ColorScheme;
import com.supermap.desktop.controls.colorScheme.ColorSchemeManager;
import com.supermap.desktop.controls.utilties.ToolbarUtilties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.SmFileChoose;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.utilties.ListUtilties;
import com.supermap.desktop.utilties.PathUtilties;
import com.supermap.desktop.utilties.StringUtilties;
import com.supermap.desktop.utilties.TableUtilties;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * @author XiaJT
 */
public class JDialogColorScheme extends SmDialog {

	private JToolBar toolBar;
	private SmButton buttonAdd;
	private SmButton buttonReset;
	private SmButton buttonEdit;
	private SmButton buttonRevert;
	private SmButton buttonDel;
	private SmButton buttonSelectAll;
	private SmButton buttonSelectInvert;
	private SmButton buttonImport;
	private SmButton buttonExport;
	private JTableColorScheme tableColorScheme;
	private JPanel panelButton;
	private SmButton buttonOk;
	private SmButton buttonCancle;
	private boolean isModified = false;

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
		this.componentList.add(buttonOk);
		this.componentList.add(buttonCancle);
		this.getRootPane().setDefaultButton(buttonOk);
		this.setSize(new Dimension(800, 450));
		this.setLocationRelativeTo(null);
	}

	private void initLayout() {
		initToolBarLayout();
		initPanelButton();
		this.setLayout(new GridBagLayout());
		this.add(toolBar, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(10, 10, 0, 10));
		this.add(new JScrollPane(tableColorScheme), new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER).setInsets(5, 10, 0, 10));
		this.add(panelButton, new GridBagConstraintsHelper(0, 2, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL));
	}

	private void initPanelButton() {
		panelButton.setLayout(new GridBagLayout());
		panelButton.add(buttonOk, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.EAST).setFill(GridBagConstraints.NONE).setInsets(5, 10, 10, 5));
		panelButton.add(buttonCancle, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(0, 1).setAnchor(GridBagConstraints.EAST).setFill(GridBagConstraints.NONE).setInsets(5, 0, 10, 10));
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
		toolBar.setFloatable(false);
		toolBar.setRollover(true);
	}

	private void initListeners() {
		this.buttonAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO: 2016/5/6  to be continued
			}
		});

		this.buttonReset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tableColorScheme.setColorSchemeList(ListUtilties.listDeepCopy(ColorSchemeManager.getColorSchemeManager().getDefaultColorSchemeList()));
				isModified = true;
				checkButtonState();
			}
		});

		this.buttonEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO: 2016/5/6
			}
		});

		this.buttonRevert.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tableColorScheme.setColorSchemeList(ListUtilties.listDeepCopy(ColorSchemeManager.getColorSchemeManager().getDefaultColorSchemeList()));
				isModified = false;
				checkButtonState();
			}
		});

		this.buttonDel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tableColorScheme.deleteSelectedRow();
				isModified = true;
				checkButtonState();
			}
		});

		this.buttonSelectAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tableColorScheme.setRowSelectionInterval(0, tableColorScheme.getRowCount() - 1);
				checkButtonState();
			}
		});

		this.buttonSelectInvert.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				TableUtilties.invertSelection(tableColorScheme);
				checkButtonState();
			}
		});

		this.buttonImport.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonImportClicked();
			}
		});

		this.buttonExport.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonExportClicked();
			}
		});

		this.tableColorScheme.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				checkButtonState();
			}
		});

		this.buttonOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (isModified) {
					ColorSchemeManager.getColorSchemeManager().setColorSchemeList(tableColorScheme.getColorSchemeList());
				}
				dialogResult = DialogResult.OK;
				dispose();
			}
		});

		this.buttonCancle.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialogResult = DialogResult.CANCEL;
				dispose();
			}
		});
	}

	private void buttonImportClicked() {
		if (!SmFileChoose.isModuleExist("ColorSchemeImport")) {
			String fileFilters = SmFileChoose.createFileFilter(ControlsProperties.getString("String_ColorSchemeSaveFileFilter"), "scs");
			SmFileChoose.addNewNode(fileFilters, PathUtilties.getFullPathName(ControlsProperties.getString("String_ColorSchemeBasicDirectory"), true),
					CommonProperties.getString(CommonProperties.open), "ColorSchemeImport", "OpenMany");
		}
		SmFileChoose fileChooser = new SmFileChoose("ColorSchemeImport");
		int result = fileChooser.showDefaultDialog();
		File[] selectFiles = fileChooser.getSelectFiles();
		int rowCount = tableColorScheme.getRowCount();
		if (result == JFileChooser.APPROVE_OPTION && selectFiles != null && selectFiles.length > 0) {
			for (File selectFile : selectFiles) {
				ColorScheme colorScheme = new ColorScheme();
				colorScheme.fromXML(selectFile);
				tableColorScheme.addColorScheme(colorScheme);
			}
		}
		if (tableColorScheme.getRowCount() > rowCount) {
			tableColorScheme.setRowSelectionInterval(rowCount, tableColorScheme.getRowCount() - 1);
			tableColorScheme.scrollRectToVisible(tableColorScheme.getCellRect(rowCount, 0, true));
		}
	}

	private void buttonExportClicked() {
		if (!SmFileChoose.isModuleExist("ColorSchemeExport")) {
			SmFileChoose.addNewNode("", PathUtilties.getFullPathName(ControlsProperties.getString("String_ColorSchemeBasicDirectory"), true),
					CommonProperties.getString(CommonProperties.open), "ColorSchemeExport", "GetDirectories");
		}
		SmFileChoose fileChooser = new SmFileChoose("ColorSchemeExport");
		int result = fileChooser.showDefaultDialog();
		String directories = fileChooser.getFilePath();
		if (result == JFileChooser.APPROVE_OPTION && !StringUtilties.isNullOrEmpty(directories)) {
			for (int i : tableColorScheme.getSelectedRows()) {
				ColorScheme colorScheme = tableColorScheme.getColorScheme(i);
				colorScheme.saveAs(directories);
			}
		}
	}

	private void checkButtonState() {
		this.buttonEdit.setEnabled(tableColorScheme.getSelectedRowCount() == 1);
		this.buttonRevert.setEnabled(isModified);
		this.buttonDel.setEnabled(tableColorScheme.getSelectedRowCount() > 0);
		this.buttonExport.setEnabled(tableColorScheme.getSelectedRowCount() > 0);
		this.buttonOk.setEnabled(tableColorScheme.getRowCount() > 0);
	}

	private void initResources() {
		this.buttonAdd.setIcon(new ImageIcon(this.getClass().getResource("/com/supermap/desktop/controlsresources/ToolBar/ColorScheme/add.png")));
		this.buttonAdd.setToolTipText(ControlsProperties.getString("String_AddColorScheme"));
		this.buttonReset.setIcon(new ImageIcon(this.getClass().getResource("/com/supermap/desktop/controlsresources/ToolBar/ColorScheme/basic.png")));
		this.buttonReset.setToolTipText(ControlsProperties.getString("String_DefaultColorSchemes"));
		this.buttonEdit.setIcon(new ImageIcon(this.getClass().getResource("/com/supermap/desktop/controlsresources/ToolBar/ColorScheme/edit.png")));
		this.buttonEdit.setToolTipText(ControlsProperties.getString("String_EditColorScheme"));
		this.buttonRevert.setIcon(new ImageIcon(this.getClass().getResource("/com/supermap/desktop/controlsresources/ToolBar/ColorScheme/undo.png")));
		this.buttonRevert.setToolTipText(ControlsProperties.getString("String_ResetColorSchemes"));
		this.buttonDel.setIcon(new ImageIcon(this.getClass().getResource("/com/supermap/desktop/coreresources/ToolBar/Image_ToolButton_Delete.png")));
		this.buttonDel.setToolTipText(ControlsProperties.getString("String_RemoveColorScheme"));

		this.buttonSelectAll.setIcon(new ImageIcon(this.getClass().getResource("/com/supermap/desktop/coreresources/ToolBar/Image_ToolButton_SelectAll.png")));
		this.buttonSelectAll.setToolTipText(CommonProperties.getString(CommonProperties.selectAll));
		this.buttonSelectInvert.setIcon(new ImageIcon(this.getClass().getResource("/com/supermap/desktop/coreresources/ToolBar/Image_ToolButton_SelectInverse.png")));
		this.buttonSelectInvert.setToolTipText(CommonProperties.getString(CommonProperties.selectInverse));

		this.buttonImport.setIcon(new ImageIcon(this.getClass().getResource("/com/supermap/desktop/coreresources/ToolBar/Image_ToolButton_Import.png")));
		this.buttonImport.setToolTipText(CommonProperties.getString(CommonProperties.IMPORT));
		this.buttonExport.setIcon(new ImageIcon(this.getClass().getResource("/com/supermap/desktop/coreresources/ToolBar/Image_ToolButton_Export.png")));
		this.buttonExport.setToolTipText(CommonProperties.getString(CommonProperties.IMPORT));

		this.buttonOk.setText(CommonProperties.getString(CommonProperties.OK));
		this.buttonCancle.setText(CommonProperties.getString(CommonProperties.Cancel));

		this.setTitle(ControlsProperties.getString("String_ColorSchemeManageForm"));
	}

	private void initComponentState() {
		ColorSchemeManager colorSchemeManager = ColorSchemeManager.getColorSchemeManager();
		tableColorScheme.setColorSchemeList(ListUtilties.listDeepCopy(colorSchemeManager.getColorSchemeList()));
		if (tableColorScheme.getRowCount() > 0) {
			tableColorScheme.setRowSelectionInterval(0, 0);
		}
	}
}
