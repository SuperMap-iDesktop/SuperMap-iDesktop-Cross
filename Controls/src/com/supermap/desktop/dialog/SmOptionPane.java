package com.supermap.desktop.dialog;

import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 弹出提示框
 *
 * @author xiajt
 */
public class SmOptionPane extends SmDialog {

	private JPanel panelButton;
	private JButton buttonOk;
	private JButton buttonCancle;

	private JTextArea textAreaMessage;
	private JLabel labelIcon;


	/**
	 * 与JOptionPane结果保持一致，此处用int类型存储结果
	 */
	private int result = JOptionPane.CLOSED_OPTION;

	public SmOptionPane() {
		this(null);
	}

	public SmOptionPane(JDialog owner) {
		super(owner);
		initComponents();
		initLayout();
		addListeners();
		initResources();
		initComponentState();
	}

	private void initComponents() {
		this.panelButton = new JPanel();
		this.buttonOk = new JButton();
		this.buttonCancle = new JButton();
		this.labelIcon = new JLabel();
		this.textAreaMessage = new JTextArea();
		this.textAreaMessage.setLineWrap(true);
		this.textAreaMessage.setEditable(false);
		this.textAreaMessage.setBackground(this.getBackground());
	}

	private void initLayout() {
		this.panelButton.setLayout(new GridBagLayout());
		this.panelButton.add(buttonOk, new GridBagConstraintsHelper(0, 0).setFill(GridBagConstraints.NONE).setWeight(90, 1).setAnchor(GridBagConstraints.EAST).setInsets(2));
		this.panelButton.add(buttonCancle, new GridBagConstraintsHelper(1, 0).setFill(GridBagConstraints.NONE).setWeight(10, 1).setAnchor(GridBagConstraints.EAST).setInsets(2));

		Panel panel = new Panel();
		panel.setLayout(new GridBagLayout());
		panel.add(labelIcon, new GridBagConstraintsHelper(0, 0).setFill(GridBagConstraints.NONE).setWeight(30, 1).setAnchor(GridBagConstraints.CENTER).setInsets(2));
		panel.add(textAreaMessage, new GridBagConstraintsHelper(1, 0).setFill(GridBagConstraints.BOTH).setWeight(70, 1).setAnchor(GridBagConstraints.CENTER).setInsets(2));
		panel.add(panelButton, new GridBagConstraintsHelper(0, 1).setFill(GridBagConstraints.BOTH).setWeight(100, 0).setAnchor(GridBagConstraints.CENTER));

		this.setLayout(new GridBagLayout());
		this.add(panel, new GridBagConstraintsHelper(0, 0).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setInsets(10));
	}

	private void addListeners() {
		buttonOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				result = JOptionPane.OK_OPTION;
				dispose();
			}
		});

		buttonCancle.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				result = JOptionPane.CANCEL_OPTION;
				dispose();
			}
		});
	}

	private void initResources() {
		buttonOk.setText(CommonProperties.getString(CommonProperties.OK));
		buttonCancle.setText(CommonProperties.getString(CommonProperties.Cancel));
	}

	private void initComponentState() {
//		this.buttonOk.setC
		this.buttonOk.setBackground(new Color(70, 127, 217));
	}

	public int showMessageDialog(String message) {
		return showMessageDialog(message, CoreProperties.getString("String_MessageBox_Title"));
	}

	public int showMessageDialog(String message, String title) {
		this.textAreaMessage.setText(message);
		this.setTitle(title);
		this.setVisible(true);
		return result;
	}


}
