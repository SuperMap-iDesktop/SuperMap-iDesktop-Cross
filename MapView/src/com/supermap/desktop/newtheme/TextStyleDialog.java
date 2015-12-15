package com.supermap.desktop.newtheme;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import com.supermap.data.TextStyle;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.TextStyleContainer;

public class TextStyleDialog extends SmDialog {

	private static final long serialVersionUID = 1L;
	private JButton buttonSure;
	private JButton buttonQuite;
	private TextStyle textStyle;
	TextStyleContainer textStyleContainer;
	private LocalActionListener actionListener = new LocalActionListener();

	public TextStyleDialog(TextStyle textStyle) {
		this.textStyle = textStyle.clone();
		initComponents();
		initResources();
		registActionListener();
	}

	public TextStyleDialog() {
		this.textStyle = new TextStyle();
		initComponents();
		initResources();
		registActionListener();
	}

	/**
	 * 注册事件
	 */
	private void registActionListener() {
		this.buttonSure.addActionListener(this.actionListener);
		this.buttonQuite.addActionListener(this.actionListener);
	}

	/**
	 * 注销事件
	 */
	private void unRegistActionListener() {
		this.buttonSure.removeActionListener(this.actionListener);
		this.buttonQuite.removeActionListener(this.actionListener);
	}

	/**
	 * 构建界面
	 */
	private void initComponents() {
		textStyleContainer = new TextStyleContainer(textStyle);
		buttonQuite = new JButton();
		buttonSure = new JButton();
		setTitle(ControlsProperties.getString("String_Form_SetTextStyle"));
		setSize(460, 460);
		//  @formatter:off
		getContentPane().setLayout(new GridBagLayout());
		getContentPane().add(textStyleContainer,new GridBagConstraintsHelper(0, 0, 2, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setWeight(2, 1));
		getContentPane().add(buttonSure,        new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.EAST).setFill(GridBagConstraints.NONE).setWeight(100, 0).setInsets(0,0,5,10));
		getContentPane().add(buttonQuite,       new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.EAST).setFill(GridBagConstraints.NONE).setWeight(1, 0).setInsets(0,0,5,20));
		// @formatter:on
	}

	/**
	 * 资源化
	 */
	private void initResources() {
		this.buttonQuite.setText(ControlsProperties.getString("String_Button_Cancel"));
		this.buttonSure.setText(ControlsProperties.getString("String_Button_Ok"));
	}

	class LocalActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == buttonSure) {
				TextStyleDialog.this.dispose();
				unRegistActionListener();
				setDialogResult(DialogResult.OK);
				textStyle = textStyleContainer.getTextStyle();
			} else {
				TextStyleDialog.this.dispose();
				unRegistActionListener();
				setDialogResult(DialogResult.CANCEL);
			}
		}
	}

	public TextStyle getTextStyle() {
		return textStyle;
	}

	public void setTextStyle(TextStyle textStyle) {
		this.textStyle = textStyle;
	}

}
