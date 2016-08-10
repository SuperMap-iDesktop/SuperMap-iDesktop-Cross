package com.supermap.desktop.CtrlAction;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormLBSControl;
import com.supermap.desktop.controls.utilities.ComponentFactory;
import com.supermap.desktop.http.CreateFile;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.lbsclient.LBSClientProperties;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.MessageFormat;

public class CtrlActionMkdir extends CtrlAction {

	public CtrlActionMkdir(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		IFormLBSControl lbsControl = (IFormLBSControl) Application.getActiveApplication().getActiveForm();
		CreateDirDialog dialog = new CreateDirDialog();
		if (dialog.showDialog().equals(DialogResult.OK) && !StringUtilities.isNullOrEmpty(dialog.getDirectoryName())
				&& !isVisibleName(lbsControl, dialog.getDirectoryName())) {
			CreateFile createFile = new CreateFile();
			createFile.createDir(lbsControl.getURL(), dialog.getDirectoryName());
		}else{
			Application.getActiveApplication().getOutput().output(MessageFormat.format(LBSClientProperties.getString("String_DirectoryNameInvisible"), dialog.getDirectoryName()));
		}

	}

	private boolean isVisibleName(IFormLBSControl lbsControl, String directoryName) {
		boolean isVisibleName = false;
		for (int i = 0; i < lbsControl.getTable().getRowCount(); i++) {
			String tempName = lbsControl.getTable().getValueAt(i, 0).toString();
			if (directoryName.equals(tempName)) {
				isVisibleName = true;
				break;
			}
		}
		return isVisibleName;
	}

	@Override
	public boolean enable() {
		boolean enable = false;
		if (null != Application.getActiveApplication().getActiveForm() && Application.getActiveApplication().getActiveForm() instanceof IFormLBSControl) {
			enable = true;
		}
		return enable;
	}

	class CreateDirDialog extends SmDialog {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private JLabel labelDirName;
		private JTextField textFieldDirName;
		private JButton buttonSure;
		private JButton buttonCancel;
		private ActionListener buttonSureListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				buttonOkClicked();
			}

		};
		private ActionListener buttonCancelListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				CreateDirDialog.this.dispose();
			}
		};

		public CreateDirDialog() {
			super();
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
					CreateDirDialog.this.dispose();
				}

			});
		}

		private void removeEvents() {
			this.buttonSure.removeActionListener(this.buttonSureListener);
			this.buttonCancel.removeActionListener(this.buttonCancelListener);
		}

		private void initComponents() {
			this.labelDirName = new JLabel();
			this.textFieldDirName = new JTextField("Directory");
			this.buttonSure = ComponentFactory.createButtonOK();
			this.buttonCancel = ComponentFactory.createButtonCancel();
			//@formatter:off
			JPanel panelButton = new JPanel();
			panelButton.setLayout(new GridBagLayout());
			panelButton.add(this.buttonSure,     new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.EAST).setWeight(0, 0).setInsets(2, 0, 10, 10));
			panelButton.add(this.buttonCancel,   new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.EAST).setWeight(0, 0).setInsets(2, 0, 10, 10));
			this.setLayout(new GridBagLayout());
			this.add(this.labelDirName,    new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(0, 0).setInsets(10));
			this.add(this.textFieldDirName,new GridBagConstraintsHelper(1, 0, 4, 1).setAnchor(GridBagConstraints.WEST).setWeight(4, 0).setInsets(10).setFill(GridBagConstraints.HORIZONTAL));
			this.add(panelButton,          new GridBagConstraintsHelper(0, 1, 5, 1).setAnchor(GridBagConstraints.EAST).setWeight(5, 0));
			//@formatter:on
			this.setSize(340, 110);
		}

		private void initResources() {
			this.labelDirName.setText(LBSClientProperties.getString("String_DirectoryName"));
			this.setTitle(LBSClientProperties.getString("String_MakeDirectory"));
		}

		public String getDirectoryName() {
			return this.textFieldDirName.getText();
		}

		private void buttonOkClicked() {
			if (StringUtilities.isNullOrEmpty(textFieldDirName.getText())) {
				textFieldDirName.requestFocus();
			} else {
				dialogResult = DialogResult.OK;
				CreateDirDialog.this.dispose();
			}
		}
	}
}
