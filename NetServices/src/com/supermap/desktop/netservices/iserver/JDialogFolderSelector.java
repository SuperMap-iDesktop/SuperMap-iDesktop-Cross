package com.supermap.desktop.netservices.iserver;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;

import com.supermap.desktop.netservices.NetServicesProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.SmDialog;

public class JDialogFolderSelector extends SmDialog implements ActionListener {

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
		setSize(new Dimension(600, 300));
		setLocationRelativeTo(null);
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
		this.buttonOK.addActionListener(this);
		this.buttonCancel = new JButton("Cancel");
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

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.buttonOK) {
			this.dialogResult = DialogResult.OK;
		} else if (e.getSource() == this.buttonCancel) {
			this.dialogResult = DialogResult.CANCEL;
		}
		setVisible(false);
	}
}
