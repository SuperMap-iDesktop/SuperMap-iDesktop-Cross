package com.supermap.desktop.netservices.iserver;

import com.supermap.desktop.controls.utilities.ComponentUIUtilities;
import com.supermap.desktop.netservices.NetServicesProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.button.SmButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

/**
 * @author highsad iServer 服务发布的文件选择器，用在发布之前确认需要上传的文件和文件夹
 */
public class JDialogFolderSelector extends SmDialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JLabel labelMessage;
	private SmButton buttonOk;
	private SmButton buttonCancel;
	private PanelFolderSelector panelFolderSelector;
	private String workspacePath;

	private FileSelectedChangeListener fileSelectedChangeListener = new FileSelectedChangeListener() {

		@Override
		public void FileSelectedChange(FileSelectedChangeEvent e) {
			File workspaceFile = new File(workspacePath);
			if (workspaceFile.equals(e.getFile())) {
				buttonOk.setEnabled(e.getFile().isSelected());
				getRootPane().setDefaultButton(buttonOk);
			}
		}
	};

	public JDialogFolderSelector(ArrayList<SelectableFile> files, String workspacePath) {
		this.panelFolderSelector = new PanelFolderSelector(files);
		this.panelFolderSelector.addFileSelectedChangeListener(this.fileSelectedChangeListener);
		this.workspacePath = workspacePath;
		initializeComponents();
		initializeResources();
		setSize(new Dimension(600, 300));
		setLocationRelativeTo(null);
		this.componentList.add(buttonOk);
		this.componentList.add(buttonCancel);
		this.setFocusTraversalPolicy(policy);
		setComponentName();
	}

	private void initializeComponents() {
		setTitle("Confirm");
		this.labelMessage = new JLabel("message");
		this.buttonOk = new SmButton("OK");
		this.buttonOk.setEnabled(true);
		this.buttonOk.addActionListener(this);
		this.buttonCancel = new SmButton("Cancel");
		this.buttonCancel.addActionListener(this);

		GroupLayout groupLayout = new GroupLayout(this.getContentPane());
		groupLayout.setAutoCreateContainerGaps(true);
		groupLayout.setAutoCreateGaps(true);
		this.getContentPane().setLayout(groupLayout);

		// @formatter:off
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup()
				.addComponent(this.labelMessage)
				.addComponent(this.panelFolderSelector,GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE,Short.MAX_VALUE)
				.addGroup(groupLayout.createSequentialGroup()
						.addGap(10, 10, Short.MAX_VALUE)
						.addComponent(this.buttonOk)
						.addComponent(this.buttonCancel)));
		
		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
				.addComponent(this.labelMessage,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE)
				.addComponent(this.panelFolderSelector,GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE,Short.MAX_VALUE)
				.addGroup(groupLayout.createParallelGroup()
						.addComponent(this.buttonOk)
						.addComponent(this.buttonCancel)));
		// @formatter:on
	}

	private void setComponentName() {
		ComponentUIUtilities.setName(this.labelMessage, "JDialogFolderSelector_labelMessage");
		ComponentUIUtilities.setName(this.buttonOk, "JDialogFolderSelector_buttonOk");
		ComponentUIUtilities.setName(this.buttonCancel, "JDialogFolderSelector_buttonCancel");
		ComponentUIUtilities.setName(this.panelFolderSelector, "JDialogFolderSelector_panelFolderSelector");
	}

	private void initializeResources() {
		setTitle(NetServicesProperties.getString("String_Title_ConfirmSelection"));
		this.labelMessage.setText(NetServicesProperties.getString("String_Message_ConfirmSelection"));
		this.buttonOk.setText(CommonProperties.getString(CommonProperties.OK));
		this.buttonCancel.setText(CommonProperties.getString(CommonProperties.Cancel));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.buttonOk) {
			this.dialogResult = DialogResult.OK;
		} else if (e.getSource() == this.buttonCancel) {
			this.dialogResult = DialogResult.CANCEL;
		}
		setVisible(false);
	}

}
