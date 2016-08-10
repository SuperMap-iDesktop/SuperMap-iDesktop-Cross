package com.supermap.desktop.dialog;

import com.supermap.desktop.controls.utilities.ComponentFactory;
import com.supermap.desktop.lbsclient.LBSClientProperties;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JDialogTaskManager extends SmDialog {

	private JLabel labelDownloadTask;
	private JLabel labelUploadTask;
	private JTextField textFieldDownloadTask;
	private JTextField textFieldUploadTask;
	private JButton buttonOk;
	private JButton buttonCancel;
	private JCheckBox checkboxRecoverDownloadTask;
	private JCheckBox checkboxRecoverUploadTask;
	private boolean isRecoverTask;
	private static JDialogTaskManager taskManager;

	private ActionListener buttonOkListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			isRecoverTask = checkboxRecoverDownloadTask.isSelected() || checkboxRecoverUploadTask.isSelected();
			dialogResult = DialogResult.OK;
			JDialogTaskManager.this.dispose();
		}
	};

	public static JDialogTaskManager getInstance() {
		if (null == taskManager) {
			taskManager = new JDialogTaskManager();
		}
		return taskManager;
	}

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

	private JDialogTaskManager() {
		this(null, true);
	}

	public JDialogTaskManager(JFrame frame, boolean model) {
		super(frame, model);
		initComponents();
		initResources();
		registEvents();
		this.setSize(400, 160);
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
		this.labelDownloadTask.setText(LBSClientProperties.getString("String_DownloadTask"));
		this.labelUploadTask.setText(LBSClientProperties.getString("String_UploadTask"));
		this.checkboxRecoverUploadTask.setText(LBSClientProperties.getString("String_Recover"));
		this.checkboxRecoverDownloadTask.setText(LBSClientProperties.getString("String_Recover"));
		this.setTitle(LBSClientProperties.getString("String_TaskManager"));
	}

	private void initComponents() {
		this.labelDownloadTask = new JLabel();
		this.labelUploadTask = new JLabel();
		this.textFieldDownloadTask = new JTextField();
		this.textFieldUploadTask = new JTextField();
		this.textFieldDownloadTask.setEditable(false);
		this.textFieldUploadTask.setEditable(false);
		this.buttonOk = ComponentFactory.createButtonOK();
		this.buttonCancel = ComponentFactory.createButtonCancel();
		this.checkboxRecoverDownloadTask = new JCheckBox();
		this.checkboxRecoverUploadTask = new JCheckBox();
		this.checkboxRecoverDownloadTask.setSelected(true);
		this.checkboxRecoverUploadTask.setSelected(true);
		this.buttonOk.setEnabled(false);
		//@formatter:off
		this.setLayout(new GridBagLayout());
		JPanel panelButton = new JPanel();
		panelButton.setLayout(new GridBagLayout());
		panelButton.add(this.buttonOk,                         new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.EAST).setWeight(0, 0).setInsets(2, 0, 10, 10));
		panelButton.add(this.buttonCancel,                     new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.EAST).setWeight(0, 0).setInsets(2, 0, 10, 10));
		getContentPane().add(this.labelDownloadTask,           new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5).setWeight(1, 1));
		getContentPane().add(this.textFieldDownloadTask,       new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(5).setIpad(200, 0).setWeight(1, 1));
		getContentPane().add(this.checkboxRecoverDownloadTask, new GridBagConstraintsHelper(2, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5).setWeight(1, 1));
		getContentPane().add(this.labelUploadTask,             new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5).setWeight(1, 1));
		getContentPane().add(this.textFieldUploadTask,         new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(5).setIpad(200, 0).setWeight(1, 1));
		getContentPane().add(this.checkboxRecoverUploadTask,   new GridBagConstraintsHelper(2, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5).setWeight(1, 1));
		getContentPane().add(panelButton,                      new GridBagConstraintsHelper(0, 2, 3, 1).setAnchor(GridBagConstraints.EAST).setWeight(0, 0));
		//@formatter:on
	}

	public boolean isRecoverTask() {
		return isRecoverTask;
	}

	public void setDownloadTaskCount(int i) {
		if (i > 0) {
			this.buttonOk.setEnabled(true);
		}
		this.textFieldDownloadTask.setText(String.valueOf(i));
	}

	public void setUploadTaskCount(int i) {
		if (i > 0) {
			this.buttonOk.setEnabled(true);
		}
		this.textFieldUploadTask.setText(String.valueOf(i));
	}

	public static JDialogTaskManager getTaskManager() {
		return taskManager;
	}

	public static void setTaskManager(JDialogTaskManager taskManager) {
		JDialogTaskManager.taskManager = taskManager;
	}
	
}
