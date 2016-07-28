package com.supermap.desktop.dialog;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import com.supermap.desktop.controls.utilities.ComponentFactory;
import com.supermap.desktop.lbsclient.LBSClientProperties;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmDialog;

public class JDialogTaskManager extends SmDialog {

	private JLabel labelDownloadTask;
	private JTextField textFieldDownloadTask;
	private JButton buttonOk;
	private JButton buttonCancel;
	private JRadioButton checkboxRecover;
	private boolean isRecoverDownloadTask;
	private ActionListener buttonOkListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			isRecoverDownloadTask = checkboxRecover.isSelected();
			dialogResult = DialogResult.OK;
			JDialogTaskManager.this.dispose();
		}
	};
	private ActionListener buttonCancelListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			JDialogTaskManager.this.dispose();
		}
	};
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public JDialogTaskManager(JFrame frame, boolean model) {
		super(frame, model);
		initComponents();
		initResources();
		registEvents();
		this.setSize(300, 120);
		this.setLocationRelativeTo(null);
	}

	private void registEvents() {
		removeEvents();
		this.buttonOk.addActionListener(this.buttonOkListener);
		this.buttonCancel.addActionListener(this.buttonCancelListener);
	}

	private void removeEvents() {
		this.buttonOk.removeActionListener(this.buttonOkListener);
		this.buttonCancel.removeActionListener(this.buttonCancelListener);
	}

	private void initResources() {
		this.labelDownloadTask.setText(LBSClientProperties.getString("String_DownLoadTask"));
		this.checkboxRecover.setText(LBSClientProperties.getString("String_Recover"));
	}

	private void initComponents() {
		this.labelDownloadTask = new JLabel();
		this.textFieldDownloadTask = new JTextField();
		this.textFieldDownloadTask.setEditable(false);
		this.buttonOk = ComponentFactory.createButtonOK();
		this.buttonCancel = ComponentFactory.createButtonCancel();
		this.checkboxRecover = new JRadioButton();
		//@formatter:off
		this.setLayout(new GridBagLayout());
		JPanel panelButton = new JPanel();
		panelButton.setLayout(new GridBagLayout());
		panelButton.add(this.buttonOk,                   new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.EAST).setWeight(0, 0).setInsets(2, 0, 10, 10));
		panelButton.add(this.buttonCancel,               new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.EAST).setWeight(0, 0).setInsets(2, 0, 10, 10));
		getContentPane().add(this.labelDownloadTask,     new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5).setWeight(0, 0));
		getContentPane().add(this.textFieldDownloadTask, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5).setIpad(100, 0).setWeight(0, 0));
		getContentPane().add(this.checkboxRecover,       new GridBagConstraintsHelper(2, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5).setWeight(0, 0));
		getContentPane().add(panelButton,                new GridBagConstraintsHelper(0, 1, 3, 1).setAnchor(GridBagConstraints.EAST).setWeight(0, 0));
		//@formatter:on
	}

	public boolean isRecoverDownloadTask() {
		return isRecoverDownloadTask;
	}

	public void setDownloadTaskNumber(int i) {
		this.textFieldDownloadTask.setText(String.valueOf(i));
	}
}
