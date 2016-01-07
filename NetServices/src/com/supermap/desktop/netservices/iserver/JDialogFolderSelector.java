package com.supermap.desktop.netservices.iserver;

import java.io.File;
import java.util.ArrayList;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;

import com.supermap.desktop.netservices.NetServicesProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.SmDialog;

public class JDialogFolderSelector extends SmDialog {

	public static void main(String[] args) {
		ArrayList<SelectableFile> files = new ArrayList<>();
		File test = new File("D:/test");
		File[] listFiles = test.listFiles();
		for (int i = 0; i < listFiles.length; i++) {
			files.add(SelectableFile.fromFile(listFiles[i], true));
		}

		JDialogFolderSelector dialog = new JDialogFolderSelector(files);
		dialog.setVisible(true);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JLabel labelMessage;
	private JButton buttonOK;
	private JButton buttonCancel;
	private PanelFolderSelector panelFolderSelector;

	public JDialogFolderSelector(ArrayList<SelectableFile> files) {
		this.panelFolderSelector = new PanelFolderSelector(files);
		initializeComponents();
		initializeResources();
	}

	public File[] selectedFiles() {
		return null;
	}

	public File[] selectedDirectories() {
		return null;
	}

	private void initializeComponents() {
		setTitle("Confirm");
		this.labelMessage = new JLabel("message");
		this.buttonOK = new JButton("OK");
		this.buttonCancel = new JButton("Cancel");

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
						.addComponent(this.buttonOK)
						.addComponent(this.buttonCancel)));
		
		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
				.addComponent(this.labelMessage,GroupLayout.PREFERRED_SIZE,groupLayout.PREFERRED_SIZE,groupLayout.PREFERRED_SIZE)
				.addComponent(this.panelFolderSelector,GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE,Short.MAX_VALUE)
				.addGroup(groupLayout.createParallelGroup()
						.addComponent(this.buttonOK)
						.addComponent(this.buttonCancel)));
		// @formatter:on
	}

	private void initializeResources() {
		setTitle(NetServicesProperties.getString("String_Title_ConfirmSelection"));
		this.labelMessage.setText(NetServicesProperties.getString("String_Message_ConfirmSelection"));
		this.buttonOK.setText(CommonProperties.getString(CommonProperties.OK));
		this.buttonCancel.setText(CommonProperties.getString(CommonProperties.Cancel));
	}
}
