package com.supermap.desktop.ui;

import com.supermap.desktop.utilties.StringUtilties;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.ParseException;

public class SMFormattedTextField extends JFormattedTextField implements DocumentListener, FocusListener {

	private static final long serialVersionUID = 1L;

	public SMFormattedTextField(AbstractFormatter formatter) {
		super(formatter);
		getDocument().addDocumentListener(this);
		addFocusListener(this);
	}

	public SMFormattedTextField(java.text.Format format) {
		super(format);
		getDocument().addDocumentListener(this);
		addFocusListener(this);
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				updateEdit();
			}
		});
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				updateEdit();
			}
		});
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				updateEdit();
			}
		});
	}

	/*
	 * 获得焦点的时候，设置光标在文本框末尾
	 */
	@Override
	public void focusGained(FocusEvent e) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				if (StringUtilties.isNullOrEmpty(getText())) {
					setCaretPosition(0);
				} else {
					setCaretPosition(getText().length());
				}
			}
		});
	}

	@Override
	public void focusLost(FocusEvent e) {
		// 无事可做
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
