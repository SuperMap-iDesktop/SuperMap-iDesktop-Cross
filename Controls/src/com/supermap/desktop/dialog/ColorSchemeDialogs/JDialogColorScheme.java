package com.supermap.desktop.dialog.ColorSchemeDialogs;

import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.colorScheme.ColorScheme;
import com.supermap.desktop.controls.colorScheme.ColorSchemeEditorDialog;
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
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;

/**
 * @author XiaJT
 */
public class JDialogColorScheme extends SmDialog {

	private JToolBar toolBar;
	private SmButton buttonAdd;
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
	//存放删除的颜色方案，确定时删除
	private java.util.List<ColorScheme> deletedList;

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
		buttonEdit = new SmButton();
		buttonRevert = new SmButton();
		buttonDel = new SmButton();
		buttonSelectAll = new SmButton();
		buttonSelectInvert = new SmButton();
		buttonImport = new SmButton();
		buttonExport = new SmButton();
		tableColorScheme = new JTableColorScheme();
		panelButton = new JPanel();
		buttonOk = new SmButton();
		buttonCancle = new SmButton();
		this.componentList.add(buttonOk);
		this.componentList.add(buttonCancle);
		deletedList = new ArrayList<>();
		this.getRootPane().setDefaultButton(buttonOk);
		this.setSize(new Dimension(800, 456));
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

		buttonAdd.setFocusable(false);
		buttonEdit.setFocusable(false);
		buttonRevert.setFocusable(false);
		buttonDel.setFocusable(false);
		buttonSelectAll.setFocusable(false);
		buttonSelectInvert.setFocusable(false);
		buttonImport.setFocusable(false);
		buttonExport.setFocusable(false);
	}

	private void initListeners() {
		this.buttonAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				TableUtilties.stopEditing(tableColorScheme);
				ColorSchemeEditorDialog colorSchemeEditorDialog = new ColorSchemeEditorDialog();
				DialogResult dialogResult = colorSchemeEditorDialog.showDialog();
				if (dialogResult == DialogResult.OK) {
					tableColorScheme.addColorScheme(colorSchemeEditorDialog.getColorScheme());
					tableColorScheme.setRowSelectionInterval(tableColorScheme.getRowCount() - 1, tableColorScheme.getRowCount() - 1);
					tableColorScheme.scrollRectToVisible(tableColorScheme.getCellRect(tableColorScheme.getRowCount() - 1, 0, true));
				}
				colorSchemeEditorDialog.dispose();
			}
		});

		this.buttonRevert.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				deletedList.addAll(tableColorScheme.getColorSchemeList());
				TableUtilties.stopEditing(tableColorScheme);
				tableColorScheme.setColorSchemeList(ListUtilties.listDeepCopy(ColorSchemeManager.getColorSchemeManager().getDefaultColorSchemeList()));
				if (tableColorScheme.getRowCount() > 0) {
					tableColorScheme.setRowSelectionInterval(0, 0);
				}
			}
		});

		this.buttonEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				TableUtilties.stopEditing(tableColorScheme);
				int selectedRow = tableColorScheme.getSelectedRow();
				editColorSchemeAtRow(selectedRow);
			}
		});


		this.buttonDel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				TableUtilties.stopEditing(tableColorScheme);
				int[] selectedRows = tableColorScheme.getSelectedRows();

				for (int selectedRow : selectedRows) {
					deletedList.add(tableColorScheme.getColorScheme(selectedRow));
				}

				tableColorScheme.deleteSelectedRow();
				if (selectedRows[0] < tableColorScheme.getRowCount()) {
					tableColorScheme.setRowSelectionInterval(selectedRows[0], selectedRows[0]);
				} else if (tableColorScheme.getRowCount() > 0) {
					tableColorScheme.setRowSelectionInterval(tableColorScheme.getRowCount() - 1, tableColorScheme.getRowCount() - 1);
				}
			}
		});

		this.buttonSelectAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				TableUtilties.stopEditing(tableColorScheme);
				tableColorScheme.setRowSelectionInterval(0, tableColorScheme.getRowCount() - 1);
			}
		});

		this.buttonSelectInvert.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				TableUtilties.stopEditing(tableColorScheme);
				TableUtilties.invertSelection(tableColorScheme);
			}
		});

		this.buttonImport.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				TableUtilties.stopEditing(tableColorScheme);
				buttonImportClicked();
			}
		});

		this.buttonExport.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				TableUtilties.stopEditing(tableColorScheme);
				buttonExportClicked();

			}
		});

		this.tableColorScheme.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				TableUtilties.stopEditing(tableColorScheme);
				checkButtonState();
			}
		});

		this.buttonOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (ColorScheme colorScheme : deletedList) {
					colorScheme.delete();
				}
				for (ColorScheme colorScheme : tableColorScheme.getColorSchemeList()) {
					colorScheme.save();
				}
				ColorSchemeManager.getColorSchemeManager().setColorSchemeList(tableColorScheme.getColorSchemeList());

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
		this.tableColorScheme.getModel().addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				checkButtonState();
			}
		});

		this.tableColorScheme.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1
						&& tableColorScheme.columnAtPoint(e.getPoint()) == ColorSchemeTableModel.COLUMN_COLOR_RAMP && tableColorScheme.rowAtPoint(e.getPoint()) != -1) {
					editColorSchemeAtRow(tableColorScheme.getSelectedRow());
				}
			}
		});
	}

	private void editColorSchemeAtRow(int selectedRow) {
		ColorScheme colorSchemeClone = tableColorScheme.getColorScheme(selectedRow).clone();
		ColorSchemeEditorDialog colorSchemeEditorDialog = new ColorSchemeEditorDialog(colorSchemeClone);
		if (colorSchemeEditorDialog.showDialog() == DialogResult.OK) {
			tableColorScheme.setColorSchemeAtRow(selectedRow, colorSchemeEditorDialog.getColorScheme());
			tableColorScheme.setRowSelectionInterval(selectedRow, selectedRow);
		}
		colorSchemeEditorDialog.dispose();
	}

	private void buttonImportClicked() {

		if (!SmFileChoose.isModuleExist("ColorSchemeImport")) {
			String fileFilters = SmFileChoose.createFileFilter(ControlsProperties.getString("String_ColorSchemeSaveFileFilter"), "scs", "SCS");
			SmFileChoose.addNewNode(fileFilters, PathUtilties.getFullPathName(ControlsProperties.getString("String_ColorSchemeBasicDirectory"), true),
					ControlsProperties.getString("String_ImportColorScheme"), "ColorSchemeImport", "OpenMany");
		}
		SmFileChoose fileChooser = new SmFileChoose("ColorSchemeImport");
		int result = fileChooser.showDefaultDialog();
		File[] selectFiles = fileChooser.getSelectFiles();
		int rowCount = tableColorScheme.getRowCount();
		if (result == JFileChooser.APPROVE_OPTION && selectFiles != null && selectFiles.length > 0) {
			for (File selectFile : selectFiles) {
				ColorScheme colorScheme = new ColorScheme();
				if (colorScheme.fromXML(selectFile, true)) {
					tableColorScheme.addColorScheme(colorScheme);
				}
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
					ControlsProperties.getString("String_ExportColorScheme"), "ColorSchemeExport", "GetDirectories");
		}
		SmFileChoose fileChooser = new SmFileChoose("ColorSchemeExport");
		int result = fileChooser.showDefaultDialog();
		String directories = fileChooser.getFilePath();
		if (result == JFileChooser.APPROVE_OPTION && !StringUtilties.isNullOrEmpty(directories)) {
			for (int i : tableColorScheme.getSelectedRows()) {
				ColorScheme colorScheme = tableColorScheme.getColorScheme(i);
				colorScheme.saveAsDirectories(directories);
			}
			Application.getActiveApplication().getOutput().output(MessageFormat.format(ControlsProperties.getString("String_BatchExportColorSchemeSuccess"), directories));
		}
	}

	private void checkButtonState() {
		this.buttonEdit.setEnabled(tableColorScheme.getSelectedRowCount() == 1);
		this.buttonDel.setEnabled(tableColorScheme.getSelectedRowCount() > 0);
		this.buttonExport.setEnabled(tableColorScheme.getSelectedRowCount() > 0);
		this.buttonOk.setEnabled(tableColorScheme.getRowCount() > 0);
	}

	private void initResources() {
		this.buttonAdd.setIcon(new ImageIcon(this.getClass().getResource("/com/supermap/desktop/controlsresources/ToolBar/ColorScheme/add.png")));
		this.buttonAdd.setToolTipText(ControlsProperties.getString("String_AddColorScheme"));
		this.buttonEdit.setIcon(new ImageIcon(this.getClass().getResource("/com/supermap/desktop/controlsresources/ToolBar/ColorScheme/edit.png")));
		this.buttonEdit.setToolTipText(ControlsProperties.getString("String_EditColorScheme"));
		this.buttonRevert.setIcon(new ImageIcon(this.getClass().getResource("/com/supermap/desktop/controlsresources/ToolBar/ColorScheme/basic.png")));
		this.buttonRevert.setToolTipText(ControlsProperties.getString("String_DefaultColorSchemes"));
		this.buttonDel.setIcon(new ImageIcon(this.getClass().getResource("/com/supermap/desktop/coreresources/ToolBar/Image_ToolButton_Delete.png")));
		this.buttonDel.setToolTipText(ControlsProperties.getString("String_RemoveColorScheme"));

		this.buttonSelectAll.setIcon(new ImageIcon(this.getClass().getResource("/com/supermap/desktop/coreresources/ToolBar/Image_ToolButton_SelectAll.png")));
		this.buttonSelectAll.setToolTipText(CommonProperties.getString(CommonProperties.selectAll));
		this.buttonSelectInvert.setIcon(new ImageIcon(this.getClass().getResource("/com/supermap/desktop/coreresources/ToolBar/Image_ToolButton_SelectInverse.png")));
		this.buttonSelectInvert.setToolTipText(CommonProperties.getString(CommonProperties.selectInverse));

		this.buttonImport.setIcon(new ImageIcon(this.getClass().getResource("/com/supermap/desktop/coreresources/ToolBar/Image_ToolButton_Import.png")));
		this.buttonImport.setToolTipText(CommonProperties.getString(CommonProperties.IMPORT));
		this.buttonExport.setIcon(new ImageIcon(this.getClass().getResource("/com/supermap/desktop/coreresources/ToolBar/Image_ToolButton_Export.png")));
		this.buttonExport.setToolTipText(CommonProperties.getString(CommonProperties.EXPORT));

		this.buttonOk.setText(CommonProperties.getString(CommonProperties.OK));
		this.buttonCancle.setText(CommonProperties.getString(CommonProperties.Cancel));

		this.setTitle(ControlsProperties.getString("String_ColorSchemeManageForm"));
	}

	private void initComponentState() {
		ColorSchemeManager colorSchemeManager = ColorSchemeManager.getColorSchemeManager();
		tableColorScheme.setColorSchemeList(ListUtilties.listDeepCopy(colorSchemeManager.getColorSchemeList()));
		if (tableColorScheme.getRowCount() > 0) {
			tableColorScheme.setRowSelectionInterval(0, 0);
		} else {
			checkButtonState();
		}
	}
}
