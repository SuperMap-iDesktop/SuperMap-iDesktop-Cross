package com.supermap.desktop.controls.property.workspace;

import com.supermap.data.Workspace;
import com.supermap.data.WorkspaceType;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IPasswordCheck;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.property.AbstractPropertyControl;
import com.supermap.desktop.controls.utilities.ToolbarUIUtilities;
import com.supermap.desktop.dialog.JDialogChangePassword;
import com.supermap.desktop.enums.PropertyType;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.button.SmButton;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.metal.MetalBorders;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WorkspacePropertyControl extends AbstractPropertyControl {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private JLabel labelFileName;
	private JLabel labelWorkspaceType;
	private JLabel labelVersion;
	private JLabel labelDescription;

	private JTextField textFieldFilePath;
	private JTextField textFieldWorkspaceType;
	private JTextField textFieldVersion;
	private JTextArea textFieldDescription;

	private SmButton buttonApply;
	private SmButton buttonReset;
	private JButton buttonChangePassword;

	private transient Workspace workspace;
	private String description = "";
	private String newPassword = "";
	private boolean isChanged = false;

	private transient DocumentListener textFieldDescriptionDocumentListener = new DocumentListener() {

		@Override
		public void removeUpdate(DocumentEvent e) {
			textFieldDescriptionTextChanged();
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			textFieldDescriptionTextChanged();
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			throw new UnsupportedOperationException();
		}
	};
	private transient ActionListener buttonActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == buttonChangePassword) {
				buttonChangePasswordClick();
			} else if (e.getSource() == buttonApply) {
				buttonApplyClick();
			} else if (e.getSource() == buttonReset) {
				buttonResetClick();
			}
		}
	};

	public WorkspacePropertyControl(Workspace workspace) {
		super(ControlsProperties.getString("String_WorkspaceProperty"));
		initializeComponents();
		initializeResources();
		setWorkspace(workspace);
	}

	public Workspace getWorkspace() {
		return workspace;
	}

	public void setWorkspace(Workspace workspace) {
		this.workspace = workspace;
		this.newPassword = this.workspace.getConnectionInfo().getPassword();
		unregisterEvents();
		reset();
		fillComponents();
		registerEvents();
		setButtonApplyEnabledInEDT(checkChange());
		setButtonResetEnabledInEDT(checkChange());
	}

	public boolean isChanged() {
		return isChanged;
	}

	public void setChanged(boolean isChanged) {
		this.isChanged = isChanged;
	}

	@Override
	public void refreshData() {
		setWorkspace(this.workspace);
	}

	@Override
	public PropertyType getPropertyType() {
		return PropertyType.WORKSPACE;
	}

	private void initializeComponents() {

		labelFileName = new JLabel("FileName:");

		textFieldFilePath = new JTextField();
		textFieldFilePath.setEditable(false);
		textFieldFilePath.setColumns(10);

		labelWorkspaceType = new JLabel("WorkspaceType:");

		textFieldWorkspaceType = new JTextField();
		textFieldWorkspaceType.setEditable(false);
		textFieldWorkspaceType.setColumns(10);

		labelVersion = new JLabel("Version:");

		textFieldVersion = new JTextField();
		textFieldVersion.setEditable(false);
		textFieldVersion.setColumns(10);

		textFieldDescription = new JTextArea();
		textFieldDescription.setBorder(MetalBorders.getTextFieldBorder());

		labelDescription = new JLabel("Description:");

		buttonApply = new SmButton("Apply");

		buttonReset = new SmButton("Reset");

		buttonChangePassword = new JButton("Change Password...");

		this.setLayout(new GridBagLayout());
		this.add(labelFileName, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(10, 10, 0, 0));
		this.add(textFieldFilePath, new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(10, 10, 0, 10));

		this.add(labelWorkspaceType, new GridBagConstraintsHelper(0, 2, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 10, 0, 0));
		this.add(textFieldWorkspaceType, new GridBagConstraintsHelper(0, 3, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 10, 0, 10));

		this.add(labelVersion, new GridBagConstraintsHelper(0, 4, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 10, 0, 0));
		this.add(textFieldVersion, new GridBagConstraintsHelper(0, 5, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 10, 0, 10));

		this.add(labelDescription, new GridBagConstraintsHelper(0, 6, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 10, 0, 10));
		this.add(textFieldDescription, new GridBagConstraintsHelper(0, 7, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.BOTH).setInsets(5, 10, 0, 10));

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridBagLayout());
		buttonPanel.add(buttonChangePassword, new GridBagConstraintsHelper(0, 0, 1, 1).setFill(GridBagConstraints.NONE).setWeight(1, 0).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 0, 0));
		buttonPanel.add(buttonReset, new GridBagConstraintsHelper(1, 0, 1, 1).setFill(GridBagConstraints.NONE).setWeight(0, 0).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 0, 0));
		buttonPanel.add(buttonApply, new GridBagConstraintsHelper(2, 0, 1, 1).setFill(GridBagConstraints.NONE).setWeight(0, 0).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 0, 0));

		this.add(buttonPanel, new GridBagConstraintsHelper(0, 8, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 10, 10, 10));

//		GroupLayout groupLayout = new GroupLayout(this);
//		groupLayout.setAutoCreateContainerGaps(true);
//		groupLayout.setAutoCreateGaps(true);
//		this.setLayout(groupLayout);
//
//		// 	@formatter:off
//		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.CENTER)
//				.addGroup(groupLayout.createSequentialGroup()
//						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
//								.addComponent(this.labelFileName)
//								.addComponent(this.labelWorkspaceType)
//								.addComponent(this.labelVersion)
//								.addComponent(this.labelDescription))
//						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
//								.addComponent(this.textFieldFilePath, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
//								.addComponent(this.textFieldWorkspaceType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
//								.addComponent(this.textFieldVersion, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
//								.addComponent(this.textFieldDescription, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
//				.addGroup(groupLayout.createSequentialGroup()
//						.addComponent(this.buttonChangePassword,GroupLayout.PREFERRED_SIZE,GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE)
//						.addGap(GroupLayout.PREFERRED_SIZE, 100, Short.MAX_VALUE)
//						.addComponent(this.buttonReset, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
//						.addComponent(this.buttonApply, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)));
//
//		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
//				.addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
//						.addComponent(this.labelFileName)
//						.addComponent(this.textFieldFilePath, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
//				.addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
//						.addComponent(this.labelWorkspaceType)
//						.addComponent(this.textFieldWorkspaceType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
//				.addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
//						.addComponent(this.labelVersion)
//						.addComponent(this.textFieldVersion, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
//				.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
//						.addComponent(this.labelDescription)
//						.addComponent(this.textFieldDescription, 80, 150, Short.MAX_VALUE))
//				.addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
//						.addComponent(this.buttonChangePassword, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
//						.addComponent(this.buttonReset, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
//						.addComponent(this.buttonApply, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)));
		// 	@formatter:on
	}

	private void initializeResources() {
		this.labelFileName.setText(ControlsProperties.getString("String_LabelFileName"));
		this.labelWorkspaceType.setText(ControlsProperties.getString("String_LabelFileType"));
		this.labelVersion.setText(ControlsProperties.getString("String_LabelFileVersion"));
		this.labelDescription.setText(ControlsProperties.getString("String_LabelFileDescription"));
		this.buttonChangePassword.setText(ControlsProperties.getString("String_ButtonChangePassword"));
		this.buttonApply.setText(CommonProperties.getString("String_Button_Apply"));
		this.buttonReset.setText(CommonProperties.getString("String_Button_Reset"));
	}

	private void fillComponents() {
		this.textFieldFilePath.setText(workspace.getConnectionInfo().getServer());
		this.textFieldVersion.setText(workspace.getConnectionInfo().getVersion().toString());
		this.textFieldWorkspaceType.setText(workspace.getConnectionInfo().getType().toString());
		this.textFieldDescription.setText(workspace.getDescription());
		if (workspace.getConnectionInfo().getType() == WorkspaceType.SXWU) {
			this.buttonChangePassword.setEnabled(false);
		} else {
			this.buttonChangePassword.setEnabled(true);
		}
	}

	private void registerEvents() {
		this.textFieldDescription.getDocument().addDocumentListener(this.textFieldDescriptionDocumentListener);
		this.buttonChangePassword.addActionListener(this.buttonActionListener);
		this.buttonApply.addActionListener(this.buttonActionListener);
		this.buttonReset.addActionListener(this.buttonActionListener);
	}

	private void unregisterEvents() {
		this.textFieldDescription.getDocument().removeDocumentListener(this.textFieldDescriptionDocumentListener);
		this.buttonChangePassword.removeActionListener(this.buttonActionListener);
		this.buttonApply.removeActionListener(this.buttonActionListener);
		this.buttonReset.removeActionListener(this.buttonActionListener);
	}

	private void textFieldDescriptionTextChanged() {
		this.description = this.textFieldDescription.getText();
		setButtonApplyEnabledInEDT(checkChange());
		setButtonResetEnabledInEDT(checkChange());
	}

	private void buttonChangePasswordClick() {
		JDialogChangePassword dialogChangePassword = new JDialogChangePassword("");
		dialogChangePassword.setPasswordCheck(new IPasswordCheck() {

			@Override
			public boolean checkPassword(String password) {
				return workspace.getConnectionInfo().getPassword().equals(password);
			}
		});
		dialogChangePassword.setVisible(true);
		if (dialogChangePassword.getDialogResult() == DialogResult.OK) {
			this.newPassword = dialogChangePassword.getNewPassword();
			if (!this.workspace.getConnectionInfo().getPassword().equals(this.newPassword)) {
				this.workspace.changePassword(workspace.getConnectionInfo().getPassword(), this.newPassword);
			}
			Application.getActiveApplication().getOutput().output(CoreProperties.getString("String_ChangeWorkspacePassword_Success"));
			ToolbarUIUtilities.updataToolbarsState();
		}
	}

	private void buttonApplyClick() {
		try {
			this.workspace.setDescription(this.description);
		} catch (Exception e2) {
			Application.getActiveApplication().getOutput().output(e2);
		}
		setButtonResetEnabledInEDT(false);
		setButtonApplyEnabledInEDT(false);
		ToolbarUIUtilities.updataToolbarsState();
	}

	private void buttonResetClick() {
		reset();
	}

	private void reset() {
		this.newPassword = this.workspace.getConnectionInfo().getPassword();
		this.description = this.workspace.getDescription();
		this.textFieldDescription.setText(this.description);
		setButtonResetEnabledInEDT(false);
		setButtonApplyEnabledInEDT(false);
	}

	private void setButtonApplyEnabledInEDT(final boolean enabled) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				buttonApply.setEnabled(enabled);
			}
		});
	}

	private void setButtonResetEnabledInEDT(final boolean enabled) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				buttonReset.setEnabled(enabled);
			}
		});
	}

	private boolean checkChange() {
		try {
			if (!this.workspace.getDescription().equals(this.description) || !this.workspace.getConnectionInfo().getPassword().equals(this.newPassword)) {
				this.isChanged = true;
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return this.isChanged;
	}
}
