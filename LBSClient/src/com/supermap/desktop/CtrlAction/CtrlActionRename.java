package com.supermap.desktop.CtrlAction;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import com.supermap.desktop.Application;
import com.supermap.desktop.CtrlAction.WebHDFS.HDFSDefine;
import com.supermap.desktop.Interface.*;
import com.supermap.desktop.controls.utilities.ComponentFactory;
import com.supermap.desktop.dialog.HDFSTableModel;
import com.supermap.desktop.http.CreateFile;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.lbsclient.LBSClientProperties;
import com.supermap.desktop.ui.controls.*;
import com.supermap.desktop.utilities.StringUtilities;

public class CtrlActionRename extends CtrlAction {
	public CtrlActionRename(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		IFormLBSControl lbsControl = (IFormLBSControl) Application.getActiveApplication().getActiveForm();
		if (lbsControl.getSelectRow() > -1) {
			String oldName = (String) lbsControl.getTable().getValueAt(lbsControl.getSelectRow(), 0);
			RenameDialog dialog = new RenameDialog(oldName);
			if (dialog.showDialog().equals(DialogResult.OK) && !StringUtilities.isNullOrEmpty(dialog.getNewName())) {
				CreateFile createFile = new CreateFile();
				HDFSDefine define = (HDFSDefine) ((HDFSTableModel) lbsControl.getTable().getModel()).getRowTagAt(lbsControl.getTable().getSelectedRow());
				createFile.renameFile(lbsControl.getURL(), oldName, dialog.getNewName(),define.isDir());
			}
		}

	}

	@Override
	public boolean enable() {
		boolean enable = false;
		if (null != Application.getActiveApplication().getActiveForm() && Application.getActiveApplication().getActiveForm() instanceof IFormLBSControl) {
			enable = true;
		}
		return enable;
	}

	class RenameDialog extends SmDialog {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private JLabel labelNewName;
		private JTextField textFieldDirName;
		private JButton buttonSure;
		private JButton buttonCancel;
		private String name;
		private ActionListener buttonSureListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				buttonOkClicked();
			}

		};
		private ActionListener buttonCancelListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				RenameDialog.this.dispose();
			}
		};

		public RenameDialog(String name) {
			super();
			this.name = name;
			setModal(true);
			initComponents();
			initResources();
			registEvents();
			this.setLocationRelativeTo(null);
		}

		private void registEvents() {
			removeEvents();
			this.buttonSure.addActionListener(this.buttonSureListener);
			this.buttonCancel.addActionListener(this.buttonCancelListener);
			this.addWindowListener(new WindowAdapter() {

				@Override
				public void windowClosed(WindowEvent e) {
					RenameDialog.this.dispose();
				}

			});
		}

		private void removeEvents() {
			this.buttonSure.removeActionListener(this.buttonSureListener);
			this.buttonCancel.removeActionListener(this.buttonCancelListener);
		}

		private void initComponents() {
			this.labelNewName = new JLabel();
			this.textFieldDirName = new JTextField(this.name);
			this.buttonSure = ComponentFactory.createButtonOK();
			this.buttonCancel = ComponentFactory.createButtonCancel();
			//@formatter:off
			JPanel panelButton = new JPanel();
			panelButton.setLayout(new GridBagLayout());
			panelButton.add(this.buttonSure,     new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.EAST).setWeight(0, 0).setInsets(0, 0, 10, 10));
			panelButton.add(this.buttonCancel,   new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.EAST).setWeight(0, 0).setInsets(0, 0, 10, 10));
			this.setLayout(new GridBagLayout());
			this.add(this.labelNewName,    new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(0, 1).setInsets(0,10,0,0));
			this.add(this.textFieldDirName,new GridBagConstraintsHelper(1, 0, 4, 1).setAnchor(GridBagConstraints.WEST).setWeight(4, 1).setInsets(0,10,0,10).setFill(GridBagConstraints.HORIZONTAL));
			this.add(panelButton,          new GridBagConstraintsHelper(0, 1, 5, 1).setAnchor(GridBagConstraints.EAST).setWeight(5, 0));
			//@formatter:on
			this.setSize(450, 120);
		}

		private void initResources() {
			this.labelNewName.setText(LBSClientProperties.getString("String_FileName"));
			this.setTitle(LBSClientProperties.getString("String_TitleRename"));
		}

		public String getNewName() {
			return this.textFieldDirName.getText();
		}

		private void buttonOkClicked() {
			if (StringUtilities.isNullOrEmpty(textFieldDirName.getText())) {
				textFieldDirName.requestFocus();
			} else {
				dialogResult = DialogResult.OK;
				RenameDialog.this.dispose();
			}
		}
	}
}
