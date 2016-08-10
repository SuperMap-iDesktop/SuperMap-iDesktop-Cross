package com.supermap.desktop.dialog;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.*;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.ui.controls.mutiTable.DDLExportTableModel;
import com.supermap.desktop.ui.controls.mutiTable.component.MutiTable;
import com.supermap.desktop.utilities.CoreResources;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class JDialogSizeableTemplate extends SmDialog {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private ArrayList<IForm> allForms;

	final JPanel contentPanel = new JPanel();
	private JToolBar toolBar;
	private SmButton buttonSelectAll;
	private SmButton buttonSelectInvert;
	private SmButton buttonDelete;
	private SmButton buttonSetting;
	private MutiTable table;
	private JCheckBox chckbxAutoClose;
	private SmButton buttonOk;
	private SmButton buttonCancel;

	/**
	 * Create the dialog.
	 */
	public JDialogSizeableTemplate() {
		this.allForms = new ArrayList<IForm>();

		this.setModal(true);
		setTitle("Template");
		setBounds(100, 100, 554, 361);
		getContentPane().setLayout(new BorderLayout());
		this.contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);

		this.toolBar = new JToolBar();
		this.toolBar.setFloatable(false);

		this.table = new MutiTable();
		this.table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		DDLExportTableModel tableModel = new DDLExportTableModel(new String[]{"", "Form Name", "Form Type"}) {
			boolean[] columnEditables = new boolean[]{true, true, false};

			@Override
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		};
		this.table.setModel(tableModel);

		this.table.setCheckHeaderColumn(0);

		this.table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				tableChildForms_valueChanged(e);
			}
		});

		JScrollPane scrollPaneTable = new JScrollPane(table);

		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addComponent(toolBar, GroupLayout.DEFAULT_SIZE, 528, Short.MAX_VALUE)
				.addComponent(scrollPaneTable, GroupLayout.DEFAULT_SIZE, 528, Short.MAX_VALUE));
		gl_contentPanel.setVerticalGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING).addGroup(
				gl_contentPanel.createSequentialGroup().addGap(7)
						.addComponent(toolBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(scrollPaneTable, GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE)));

		buttonSelectAll = new SmButton("SelectAll");
		buttonSelectAll.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_SelectAll.png"));
		this.buttonSelectAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonSelectAll_Click();
			}
		});
		this.buttonSelectAll.setToolTipText("SelectAll");
		this.toolBar.add(buttonSelectAll);
		buttonSelectInvert = new SmButton("SelectInvert");
		buttonSelectInvert.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_SelectInverse.png"));
		this.buttonSelectInvert.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonSelectInvert_Click();
			}
		});
		buttonSelectInvert.setToolTipText("SelectInvert");
		toolBar.add(buttonSelectInvert);
		toolBar.addSeparator();
		buttonDelete = new SmButton("Active");
		buttonDelete
				.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_Delete.png"));
		this.buttonDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonActive_Click();
			}
		});
		buttonDelete.setToolTipText("Delete");
		buttonDelete.setHorizontalAlignment(SwingConstants.LEFT);
		toolBar.add(buttonDelete);
		toolBar.addSeparator();
		buttonSetting = new SmButton("Rename");
		buttonSetting.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_Setting.PNG"));
		this.buttonSetting.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonRename_Click();
			}
		});
		this.buttonSetting.setToolTipText("Setting");
		this.toolBar.add(buttonSetting);
		this.contentPanel.setLayout(gl_contentPanel);

		JPanel buttonPane = new JPanel();
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		this.chckbxAutoClose = new JCheckBox("Is Save Layer3D KML");
		this.chckbxAutoClose.setVerticalAlignment(SwingConstants.TOP);
		this.chckbxAutoClose.setHorizontalAlignment(SwingConstants.LEFT);

		buttonOk = new SmButton("UnSave");
		buttonOk.setPreferredSize(new Dimension(75, 23));
		buttonOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonOkClick();
			}
		});
		this.buttonOk.setToolTipText("");
		this.buttonOk.setActionCommand("OK");

		buttonCancel = new SmButton("Cancel");
		buttonCancel.setPreferredSize(new Dimension(75, 23));
		buttonCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonCancelClick();
			}
		});
		this.buttonCancel.setToolTipText("");
		this.buttonCancel.setActionCommand("Cancel");

		GroupLayout gl_buttonPane = new GroupLayout(buttonPane);
		gl_buttonPane.setHorizontalGroup(gl_buttonPane.createParallelGroup(Alignment.TRAILING).addGroup(
				gl_buttonPane.createSequentialGroup().addContainerGap().addComponent(chckbxAutoClose)
						.addPreferredGap(ComponentPlacement.RELATED, 215, Short.MAX_VALUE)
						.addComponent(buttonOk, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(buttonCancel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addContainerGap()));
		gl_buttonPane.setVerticalGroup(gl_buttonPane.createParallelGroup(Alignment.LEADING).addGroup(
				gl_buttonPane
						.createSequentialGroup()
						.addGap(5)
						.addGroup(
								gl_buttonPane.createParallelGroup(Alignment.BASELINE, false).addComponent(buttonOk).addComponent(chckbxAutoClose)
										.addComponent(buttonCancel)).addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		buttonPane.setLayout(gl_buttonPane);
		this.getRootPane().setDefaultButton(this.buttonOk);
		initializeResources();
		this.componentList.add(this.buttonOk);
		this.componentList.add(this.buttonCancel);
		this.setFocusTraversalPolicy(policy);
	}

	private void initializeResources() {
		try {
			this.setTitle(CoreProperties.getString("String_Save"));
			this.buttonCancel.setText(CommonProperties.getString("String_Button_Cancel"));
			this.buttonOk.setText(CommonProperties.getString("String_Button_OK"));

			table.getColumnModel().getColumn(1).setHeaderValue(CoreProperties.getString("String_Name"));
			table.getColumnModel().getColumn(2).setHeaderValue(CoreProperties.getString("String_DataType"));

			this.buttonDelete.setText(CommonProperties.getString("String_ToolBar_Delete"));
			this.buttonSetting.setText(CommonProperties.getString("String_ToolBar_SetBatch"));
			this.buttonSelectAll.setText(CommonProperties.getString("String_ToolBar_SelectAll"));
			this.buttonSelectInvert.setText(CommonProperties.getString("String_ToolBar_SelectInverse"));
			this.chckbxAutoClose.setText(CommonProperties.getString("String_CheckBox_CloseDialog"));
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	public IForm[] getAllForms() {
		return allForms.toArray(new IForm[allForms.size()]);
	}

	public void setAllForms(IForm[] forms) {
		allForms.clear();
		if (forms != null) {
			Object[][] datas = new Object[forms.length][2];
			for (int i = 0; i < forms.length; i++) {
				IForm form = forms[i];
				allForms.add(form);
				String type = CoreProperties.getString("String_Other");
				if (form instanceof IFormMap) {
					type = CoreProperties.getString("String_WorkspaceNodeMap");
				} else if (form instanceof IFormScene) {
					type = CoreProperties.getString("String_WorkspaceNodeScene");
				} else if (form instanceof IFormLayout) {
					type = CoreProperties.getString("String_WorkspaceNodeLayout");
				}
				datas[i] = new Object[]{true, form.getText(), type};
			}

			try {
				this.table.refreshContents(datas);
			} catch (Exception ex) {
				Application.getActiveApplication().getOutput().output(ex);
			}

			this.chckbxAutoClose.setEnabled(false);
		}
	}

	public boolean isSaveLayer3DKML() {
		return this.chckbxAutoClose.isSelected();
	}

	public IForm[] getSelectedForms() {
		ArrayList<IForm> selectedForms = new ArrayList<IForm>();
		try {
			IFormManager formManager = Application.getActiveApplication().getMainFrame().getFormManager();
			for (int i = 0; i < table.getRowCount(); i++) {
				boolean isChecked = Boolean.parseBoolean(table.getModel().getValueAt(i, 0).toString());
				if (isChecked) {
					selectedForms.add(formManager.get(i));
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return selectedForms.toArray(new IForm[selectedForms.size()]);
	}

	public IForm[] getUnselectedForms() {
		ArrayList<IForm> selectedForms = new ArrayList<IForm>();
		try {
			IFormManager formManager = Application.getActiveApplication().getMainFrame().getFormManager();
			for (int i = 0; i < table.getRowCount(); i++) {
				boolean isChecked = Boolean.parseBoolean(table.getModel().getValueAt(i, 0).toString());
				if (!isChecked) {
					selectedForms.add(formManager.get(i));
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return selectedForms.toArray(new IForm[selectedForms.size()]);
	}

	private void tableChildForms_valueChanged(ListSelectionEvent e) {
		try {
			if (e.getValueIsAdjusting()) {

				this.buttonDelete.setEnabled(false);

				// 如果选中的是一条记录的话
				if (e.getFirstIndex() == e.getLastIndex()) {
					Object value = table.getModel().getValueAt(e.getFirstIndex(), 1);
					if (!Application.getActiveApplication().getMainFrame().getFormManager().getActiveForm().getText().equals(value.toString())) {
						this.buttonDelete.setEnabled(true);
					}
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private void buttonSelectAll_Click() {
		try {
			for (int ii = 0; ii < table.getRowCount(); ii++) {
				table.getModel().setValueAt(true, ii, table.getCheckHeaderColumn());
			}
			table.checkColumnHeader();
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private void buttonSelectInvert_Click() {
		try {
			for (int ii = 0; ii < table.getRowCount(); ii++) {
				Object value = table.getModel().getValueAt(ii, table.getCheckHeaderColumn());
				boolean isChecked = Boolean.parseBoolean(value.toString());
				table.getModel().setValueAt(!isChecked, ii, table.getCheckHeaderColumn());
			}
			table.checkColumnHeader();
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private void buttonActive_Click() {
		try {
			if (table.getSelectedRowCount() > 0) {
				IForm form = Application.getActiveApplication().getMainFrame().getFormManager().get(table.getSelectedRow());
				Application.getActiveApplication().getMainFrame().getFormManager().setActiveForm(form);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private void buttonRename_Click() {
		try {
			UICommonToolkit.showMessageDialog(ControlsProperties.getString("String_UnDo"));
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private void buttonOkClick() {
		try {
			this.dispose();
			this.dialogResult = DialogResult.NO;
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private void buttonCancelClick() {
		try {
			this.setVisible(false);
			this.dialogResult = DialogResult.CANCEL;
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

}
