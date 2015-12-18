package com.supermap.desktop;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JTextField;

public class FileChooserControl extends JComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JTextField textEditor;

	private JFileChooser fileChooser;

	private String result;

	private JButton button;

	// 初始化带有默认文件路径的文件选择控件
	public FileChooserControl(String filePath) {
		initCompanent();
		setText(filePath);
	}

	public FileChooserControl() {
		this(null);
	}

	public void setText(String filePath) {
		this.textEditor.setText(filePath);
	}

	public void setButtonEnabled(boolean flag) {
		this.button.setEnabled(flag);
	}

	public void setTextEnabled(boolean flag) {
		this.textEditor.setEnabled(flag);
	}

	@Override
	public void setEnabled(boolean flag) {
		this.textEditor.setEnabled(flag);
		this.button.setEnabled(flag);
	}

	// 初始化控件信息
	public void initCompanent() {
		this.textEditor = new JTextField(40);
		this.textEditor.setBackground(Color.white);
		this.textEditor.setAutoscrolls(true);
		this.textEditor.setHorizontalAlignment(JTextField.LEFT);

		this.button = new JButton();
		button.setIcon(new ImageIcon(
				FileChooserControl.class
						.getResource("/com/supermap/desktop/controlsresources/Image_DatasetGroup_Normal.png")));
		this.button.setBorder(BorderFactory.createEtchedBorder(1));
		this.button.setFocusPainted(false);
		this.button.setFocusable(false);

		this.setLayout(new BorderLayout());
		this.add(this.textEditor, BorderLayout.CENTER);
		this.add(this.button, BorderLayout.EAST);
	}

	public void setIcon(ImageIcon image) {
		this.button.setIcon(image);
	}

	public String getResult() {
		return result;
	}

	// 用相对路径来设置按钮的背景图片

	public JFileChooser getFileChooser() {
		return fileChooser;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public JButton getButton() {
		return button;
	}

	public JTextField getEditor() {
		return textEditor;
	}
}
