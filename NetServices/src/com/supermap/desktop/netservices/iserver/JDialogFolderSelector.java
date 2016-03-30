package com.supermap.desktop.netservices.iserver;

import com.supermap.desktop.netservices.NetServicesProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.button.SmButton;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
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
	private SmButton buttonOK;
	private SmButton buttonCancel;
	private PanelFolderSelector panelFolderSelector;
	private String workspacePath;

	private FileSelectedChangeListener fileSelectedChangeListener = new FileSelectedChangeListener() {

		@Override
		public void FileSelectedChange(FileSelectedChangeEvent e) {
			File workspaceFile = new File(workspacePath);
			if (workspaceFile.equals(e.getFile())) {
				buttonOK.setEnabled(e.getFile().isSelected());
				getRootPane().setDefaultButton(buttonOK);
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
	}

	private void initializeComponents() {
		setTitle("Confirm");
		this.labelMessage = new JLabel("message");
		this.buttonOK = new SmButton("OK");
		this.buttonOK.setEnabled(true);
		this.buttonOK.addActionListener(this);
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
						.addComponent(this.buttonOK)
						.addComponent(this.buttonCancel)));
		
		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
				.addComponent(this.labelMessage,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE)
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

	@Override
	protected JRootPane createRootPane() {
		return keyBoardPressed();
	}

	@Override
	public JRootPane keyBoardPressed() {
		JRootPane rootPane = new JRootPane();
		KeyStroke strokForEnter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
		rootPane.registerKeyboardAction(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dialogResult = DialogResult.OK;
			}
		}, strokForEnter, JComponent.WHEN_IN_FOCUSED_WINDOW);
		KeyStroke strokForEsc = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
		rootPane.registerKeyboardAction(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dialogResult = DialogResult.CANCEL;
			}
		}, strokForEsc, JComponent.WHEN_IN_FOCUSED_WINDOW);
		return rootPane;
	}
}
