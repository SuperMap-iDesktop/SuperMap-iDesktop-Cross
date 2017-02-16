package com.supermap.desktop.ui.controls.TextFields;

import com.supermap.desktop.controls.utilities.ControlsResources;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 获得带“默认值”的文本框控件
 *
 * @author YuanR
 *         2017/2/16
 */

public class DefaultValueTextField extends JTextField {

	private ImageIcon searchIcon;
	private String defaultValue;
	private String defaultLabelWarning;


	/**
	 * @param text
	 */
	@Override
	public void setText(String text) {
		super.setText(text);
		defaultValue = text;
		this.setForeground(Color.gray);
	}

	/**
	 * @param text
	 */
	@Override
	public void setToolTipText(String text) {
		super.setToolTipText(text);
		defaultLabelWarning = text;
		repaint();
	}

	/**
	 * @param g
	 */
	@Override
	public void paintComponent(Graphics g) {
		// 当有提示信息时，显示提示图标
		if (!defaultLabelWarning.equals("")) {
			super.paintComponent(g);
//			int iconWidth = this.searchIcon.getIconWidth();
			int iconHeight = this.searchIcon.getIconHeight();
			this.searchIcon.paintIcon(this, g, 5, (this.getHeight() - (iconHeight - 1)) / 2);
			this.setMargin(new Insets(0, 23, 0, 0));
		} else {
			super.paintComponent(g);
		}
	}

	/**
	 *默认构造方法
	 */
	public DefaultValueTextField() {
		this("", "");
	}

	/**
	 * 构造函数
	 *
	 * @param defaultValue
	 * @param defaultLabelWarning
	 */
	public DefaultValueTextField(String defaultValue, String defaultLabelWarning) {
		super();
		this.defaultValue = defaultValue;
		this.defaultLabelWarning = defaultLabelWarning;
		this.searchIcon = (ImageIcon) ControlsResources.getIcon("/controlsresources/Image_Property.png");

		initComponents();
		initListener();
	}

	/**
	 * 初始化控件属性
	 */
	private void initComponents() {
		this.setText(this.defaultValue);
		// 当有提示信息时再设置其ToolTipText
		if (!this.defaultLabelWarning.equals("")) {
			this.setToolTipText(this.defaultLabelWarning);
		}
		this.setForeground(Color.gray);
	}

	private void initListener() {
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				setForeground(Color.black);
			}
		});

	}
}
