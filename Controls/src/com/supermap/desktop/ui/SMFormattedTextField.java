package com.supermap.desktop.ui;

import com.supermap.desktop.utilties.StringUtilties;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.text.ParseException;

public class SMFormattedTextField extends JFormattedTextField implements DocumentListener {

	private static final long serialVersionUID = 1L;

	public SMFormattedTextField(AbstractFormatter formatter) {
		super(formatter);
		getDocument().addDocumentListener(this);
	}

	public SMFormattedTextField(java.text.Format format) {
		super(format);
		getDocument().addDocumentListener(this);
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		// FIXME: 2016/4/25
		// 使用invokeLater会导致导致 移除事件 -> 修改属性 -> 发送改变事件 -> 添加事件 变成
		// 移除事件 -> 修改属性 ->添加事件 ->  发送改变事件 导致错误触发事件
//		SwingUtilities.invokeLater(new Runnable() {
//
//			@Override
//			public void run() {
				updateEdit();
//			}
//		});
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
//		SwingUtilities.invokeLater(new Runnable() {
//
//			@Override
//			public void run() {
				updateEdit();
//			}
//		});
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
//		SwingUtilities.invokeLater(new Runnable() {
//
//			@Override
//			public void run() {
				updateEdit();
//			}
//		});
	}


	private void updateEdit() {
		Object lastValue = getValue();

		try {
			if (!StringUtilties.isNullOrEmpty(getText())) {
				commitEdit();
			}
		} catch (ParseException e1) {
			setValue(lastValue);
		}
	}
}
