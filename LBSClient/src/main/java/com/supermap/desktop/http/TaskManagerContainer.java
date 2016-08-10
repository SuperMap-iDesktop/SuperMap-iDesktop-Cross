package com.supermap.desktop.http;

import com.supermap.Interface.ITaskManager;
import com.supermap.desktop.lbsclient.LBSClientProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.button.SmButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TaskManagerContainer extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JLabel labelTitle;
	private JTextField textFieldTitle;
	private JScrollPane scrollPane;
	private JPanel panelEmty;
	private SmButton buttonRun;

	public TaskManagerContainer() {
		initializeComponents();
		initializeResources();
	}

	private void initializeComponents() {
		this.labelTitle = new JLabel();
		this.textFieldTitle = new JTextField();
		this.scrollPane = new JScrollPane();
		this.panelEmty = new JPanel();
		this.buttonRun = new SmButton();
		this.setLayout(new GridBagLayout());
		this.textFieldTitle.setEditable(false);
		this.textFieldTitle.setBackground(Color.white);
		this.setLayout(new GridBagLayout());
		this.scrollPane.setBorder(null);
		// @formatter:off
		this.add(this.labelTitle,       new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(10, 0).setInsets(10, 10, 5, 10).setAnchor(GridBagConstraints.WEST));
		this.add(this.textFieldTitle,   new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(90, 0).setInsets(10, 10, 5, 10).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL));
		this.add(this.scrollPane,       new GridBagConstraintsHelper(0, 1, 2, 1).setWeight(100, 75).setInsets(5).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH));
		this.add(this.buttonRun,        new GridBagConstraintsHelper(1, 2, 1, 1).setWeight(0, 0).setInsets(0,10,5,10).setAnchor(GridBagConstraints.EAST));
		this.scrollPane.setViewportView(this.panelEmty);
		// @formatter:on
	}

	private void initializeResources() {
		this.buttonRun.setText(CommonProperties.getString("String_Button_OK"));
		this.labelTitle.setText(LBSClientProperties.getString("String_TaskType"));
	}

	public void setManager(final ITaskManager manager) {
		this.scrollPane.setViewportView((Component) manager);
		this.textFieldTitle.setText(manager.getTitle());
		this.buttonRun.setEnabled(manager.isEnable());
		this.buttonRun.addActionListener(new ButtonRunListener(manager));
	}

	class ButtonRunListener implements ActionListener {
		ITaskManager manager;

		public ButtonRunListener(ITaskManager manager) {
			this.manager = manager;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			manager.run();
		}
	}

}
