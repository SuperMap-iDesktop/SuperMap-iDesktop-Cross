package com.supermap.desktop.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JToolBar;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.SwingConstants;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormLayout;
import com.supermap.desktop.Interface.IFormManager;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.Interface.IFormScene;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.mutiTable.DDLExportTableModel;
import com.supermap.desktop.ui.controls.mutiTable.component.MutiTable;
import javax.swing.JCheckBox;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;

public class JDialogSizeableTemplate extends SmDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ArrayList<IForm> allForms;

	final JPanel contentPanel = new JPanel();
	private JToolBar toolBar;
	private JButton buttonSelectAll;
	private JButton buttonSelectInvert;
	private JButton buttonDelete;
	private JButton buttonSetting;
	private MutiTable table;
	private JCheckBox chckbxAutoClose;
	private JButton okButton;
	private JButton cancelButton;

	/**
	 * Create the dialog.
	 */
	public JDialogSizeableTemplate() {
		allForms = new ArrayList<IForm>();

		this.setModal(true);
		setTitle("Template");
		setBounds(100, 100, 554, 361);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);

		toolBar = new JToolBar();
		toolBar.setFloatable(false);

		table = new MutiTable();
		table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		DDLExportTableModel tableModel = new DDLExportTableModel(new String[] { "", "Form Name", "Form Type" }) {
			boolean[] columnEditables = new boolean[] { true, true, false };

			@Override
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		};
		table.setModel(tableModel);

		table.setCheckHeaderColumn(0);

		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
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

		buttonSelectAll = new JButton("SelectAll");
		buttonSelectAll.setIcon(new ImageIcon(JDialogSizeableTemplate.class
				.getResource("/com/supermap/desktop/coreresources/ToolBar/Image_ToolButton_SelectAll.png")));
		buttonSelectAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonSelectAll_Click();
			}
		});
		buttonSelectAll.setToolTipText("SelectAll");
		toolBar.add(buttonSelectAll);

		buttonSelectInvert = new JButton("SelectInvert");
		buttonSelectInvert.setIcon(new ImageIcon(JDialogSizeableTemplate.class
				.getResource("/com/supermap/desktop/coreresources/ToolBar/Image_ToolButton_SelectInverse.png")));
		buttonSelectInvert.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonSelectInvert_Click();
			}
		});
		buttonSelectInvert.setToolTipText("SelectInvert");
		toolBar.add(buttonSelectInvert);
		addSeparator();
		buttonDelete = new JButton("Active");
		buttonDelete
				.setIcon(new ImageIcon(JDialogSizeableTemplate.class.getResource("/com/supermap/desktop/coreresources/ToolBar/Image_ToolButton_Delete.png")));
		buttonDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonActive_Click();
			}
		});
		buttonDelete.setToolTipText("Delete");
		buttonDelete.setHorizontalAlignment(SwingConstants.LEFT);
		toolBar.add(buttonDelete);
		addSeparator();
		buttonSetting = new JButton("Rename");
		buttonSetting.setIcon(new ImageIcon(JDialogSizeableTemplate.class
				.getResource("/com/supermap/desktop/coreresources/ToolBar/Image_ToolButton_Setting.PNG")));
		buttonSetting.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonRename_Click();
			}
		});
		buttonSetting.setToolTipText("Setting");
		toolBar.add(buttonSetting);
		contentPanel.setLayout(gl_contentPanel);

		JPanel buttonPane = new JPanel();
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		chckbxAutoClose = new JCheckBox("Is Save Layer3D KML");
		chckbxAutoClose.setVerticalAlignment(SwingConstants.TOP);
		chckbxAutoClose.setHorizontalAlignment(SwingConstants.LEFT);

		okButton = new JButton("UnSave");
		okButton.setPreferredSize(new Dimension(75, 23));
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonUnSave_Click();
			}
		});
		okButton.setToolTipText("");
		okButton.setActionCommand("OK");

		cancelButton = new JButton("Cancel");
		cancelButton.setPreferredSize(new Dimension(75, 23));
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonClose_Click();
			}
		});
		cancelButton.setToolTipText("");
		cancelButton.setActionCommand("Cancel");

		GroupLayout gl_buttonPane = new GroupLayout(buttonPane);
		gl_buttonPane.setHorizontalGroup(gl_buttonPane.createParallelGroup(Alignment.TRAILING).addGroup(
				gl_buttonPane.createSequentialGroup().addContainerGap().addComponent(chckbxAutoClose)
						.addPreferredGap(ComponentPlacement.RELATED, 215, Short.MAX_VALUE)
						.addComponent(okButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(cancelButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addContainerGap()));
		gl_buttonPane.setVerticalGroup(gl_buttonPane.createParallelGroup(Alignment.LEADING).addGroup(
				gl_buttonPane
						.createSequentialGroup()
						.addGap(5)
						.addGroup(
								gl_buttonPane.createParallelGroup(Alignment.BASELINE, false).addComponent(okButton).addComponent(chckbxAutoClose)
										.addComponent(cancelButton)).addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		buttonPane.setLayout(gl_buttonPane);

		initializeResources();
	}

	private void addSeparator() {
		JToolBar.Separator separator = new JToolBar.Separator();
		separator.setOrientation(SwingConstants.VERTICAL);
		toolBar.add(separator);
	}

	private void initializeResources() {
		try {
			this.setTitle(CoreProperties.getString("String_Save"));
			this.cancelButton.setText(CommonProperties.getString("String_Button_Cancel"));
			this.okButton.setText(CommonProperties.getString("String_Button_OK"));

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
				datas[i] = new Object[] { true, form.getText(), type };
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

	private void buttonUnSave_Click() {
		try {
			this.dispose();
			this.dialogResult = DialogResult.NO;
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private void buttonClose_Click() {
		try {
			this.setVisible(false);
			this.dialogResult = DialogResult.CANCEL;
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}
}
