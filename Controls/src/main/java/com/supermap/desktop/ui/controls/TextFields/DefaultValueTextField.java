package com.supermap.desktop.ui.controls.TextFields;

import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * 获得带“提示信息”的文本框控件
 *
 * @author YuanR
 *         2017/2/16
 *         对控件进行重构，使其类似于“QQ”登录界面输入账号和密码的文本框--yuanR 2017.2.25
 */

public class DefaultValueTextField extends JTextField {

	private ImageIcon searchIcon;
	private String defaultValue;
	private String defaultLabelWarning;

	/**
	 * @param text
	 */
	public void setDefaultValue(String text) {
		this.defaultValue = text;
		this.setText(this.defaultValue);
		this.setForeground(Color.gray);

	}

	/**
	 * @param text
	 */
	public void setDefaulToolTipText(String text) {
		if (!StringUtilities.isNullOrEmpty(text)) {
			this.defaultLabelWarning = text;
			setToolTipText(this.defaultLabelWarning);
		}
	}

//	/**
//	 * @param g
//	 */
//	@Override
//	public void paintComponent(Graphics g) {
//		super.paintComponent(g);
//		// 当有提示信息时，显示提示图标
//		if (!StringUtilities.isNullOrEmpty(defaultLabelWarning)) {
//			super.paintComponent(g);
////			int iconWidth = this.searchIcon.getIconWidth();
//			int iconHeight = this.searchIcon.getIconHeight();
//			this.searchIcon.paintIcon(this, g, 5, (this.getHeight() - (iconHeight - 1)) / 2);
//			this.setMargin(new Insets(0, 23, 0, 0));
//		} else {
//			super.paintComponent(g);
//			this.setMargin(new Insets(0, 0, 0, 0));
//		}
//		repaint();
//	}

	/**
	 * 默认构造方法
	 */
	public DefaultValueTextField() {
		this("", "");
	}

	/**
	 * 构造方法
	 */
	public DefaultValueTextField(String defaultValue) {
		this(defaultValue, null);
	}

	/**
	 * 构造函数
	 *
	 * @param Value
	 * @param LabelWarning
	 */
	public DefaultValueTextField(String Value, String LabelWarning) {
		super();
		this.defaultValue = Value;
		this.defaultLabelWarning = LabelWarning;
//		this.searchIcon = (ImageIcon) ControlsResources.getIcon("/controlsresources/Image_Property.png");

		initComponents();
		initListener();
	}

	/**
	 * 初始化控件属性
	 */
	private void initComponents() {
		this.setText(this.defaultValue);
		// 当有提示信息时再设置其ToolTipText
		if (!StringUtilities.isNullOrEmpty(this.defaultLabelWarning)) {
			this.setToolTipText(this.defaultLabelWarning);
		}
		this.setForeground(Color.gray);
	}

	private void initListener() {
		this.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				setText("");
				setForeground(Color.black);
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (StringUtilities.isNullOrEmptyString(getText())) {
					setText(defaultValue);
					setForeground(Color.gray);
				}
			}
		});
	}
}
