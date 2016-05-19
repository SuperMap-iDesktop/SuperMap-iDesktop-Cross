package com.supermap.desktop.ui.controls.TextFields;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/**
 * 文本输入框
 * <li>1.输入的字符串不合法时变为红色</li>
 * <li>2.丢失焦点时获得一个合法值</li>
 *
 * @author Xiajt
 */
public class SmTextFieldLegit extends JTextField {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String backUpValue = "";

	private ISmTextFieldLegit smTextFieldLegit;

	private static final Color IL_LEGAL_COLOR = Color.red;
	private static final Color LEGAL_COLOR = Color.black;

	public SmTextFieldLegit() {
		this(null);
	}

	public SmTextFieldLegit(String text) {
		super(text);
		smTextFieldLegit = new ISmTextFieldLegit() {
			@Override
			public boolean isTextFieldValueLegit(String textFieldValue) {
				return true;
			}

			@Override
			public String getLegitValue(String currentValue, String backUpValue) {
				return backUpValue;
			}
		};
		this.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				checkTextFieldState();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				checkTextFieldState();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				checkTextFieldState();
			}
		});

		// 丢失焦点还原为最近一次修改

		this.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				backUpValue = SmTextFieldLegit.this.getText();
			}

			@Override
			public void focusLost(FocusEvent e) {
				// 丢失焦点时获得一个合法值
				if (!isLegitValue(SmTextFieldLegit.this.getText())) {
					SmTextFieldLegit.this.setForeground(LEGAL_COLOR);
					SmTextFieldLegit.this.setText(smTextFieldLegit.getLegitValue(SmTextFieldLegit.this.getText(), SmTextFieldLegit.this.backUpValue));
				}
			}
		});
	}

	/**
	 * 返回输入的值是否符合要求
	 *
	 * @param value 需要判断的值
	 * @return 是否符合
	 */
	public boolean isLegitValue(String value) {
		return smTextFieldLegit.isTextFieldValueLegit(value);
	}

	/**
	 * 设置用来判断是否合法的接口
	 *
	 * @param smTextFieldLegit 接口
	 */
	public void setSmTextFieldLegit(ISmTextFieldLegit smTextFieldLegit) {
		this.smTextFieldLegit = smTextFieldLegit;
	}

	/**
	 * 获得最近一次合法值
	 *
	 * @return 最近一次合法值
	 */
	public String getBackUpValue() {
		return backUpValue;
	}

	/**
	 * 根据当前值是否合适来设置字体颜色
	 */
	private void checkTextFieldState() {
		String text = this.getText();
		if (isLegitValue(text)) {
			backUpValue = text;
			this.setForeground(LEGAL_COLOR);
		} else {
			this.setForeground(IL_LEGAL_COLOR);
		}
	}

}
