package com.supermap.desktop.ui;

import com.supermap.desktop.controls.ControlDefaultValues;
import com.supermap.desktop.ui.controls.CaretPositionListener;
import com.supermap.desktop.utilities.DoubleUtilities;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.NumberFormatter;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;
import java.text.ParseException;

public class SMFormattedTextField extends JFormattedTextField implements DocumentListener {

	private static final long serialVersionUID = 1L;

	public SMFormattedTextField() {
		super(DoubleUtilities.getDoubleFormatInstance());
		init();
	}

	public SMFormattedTextField(AbstractFormatter formatter) {
		super(formatter);
		init();
	}

	private void init() {
		// 给textField添加最适大小--yuanR 2017.3.14
		this.setPreferredSize(ControlDefaultValues.DEFAULT_PREFERREDSIZE);
		getCaretPositionListener().registerComponent(this);
		getDocument().addDocumentListener(this);
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (!StringUtilities.isNullOrEmpty(getText()) && DoubleUtilities.stringToValue(getText()) != null) {
						setText(DoubleUtilities.getFormatString(DoubleUtilities.stringToValue(getText())));
					}
				}
			}
		});
	}

	private CaretPositionListener getCaretPositionListener() {
		return new CaretPositionListener();
	}

	public SMFormattedTextField(java.text.Format format) {
		super(format);
		init();
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		// FIXME: 2016/4/25
		// 使用invokeLater会导致导致 移除事件 -> 修改属性 -> 发送改变事件 -> 添加事件 变成
		// 移除事件 -> 修改属性 ->添加事件 -> 发送改变事件 导致错误触发事件
		// SwingUtilities.invokeLater(new Runnable() {
		//
		// @Override
		// public void run() {
		updateEdit();
		// }
		// });
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		// SwingUtilities.invokeLater(new Runnable() {
		//
		// @Override
		// public void run() {
		updateEdit();
		// }
		// });
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		// SwingUtilities.invokeLater(new Runnable() {
		//
		// @Override
		// public void run() {
		updateEdit();
		// }
		// });
	}

	private void updateEdit() {
		Object lastValue = getValue();

		try {
			if (!StringUtilities.isNullOrEmpty(getText()) && null != DoubleUtilities.stringToValue(getText())) {
				commitEdit();
			}
		} catch (ParseException e1) {
			setValue(lastValue);
		}
	}

	public boolean setMaximumFractionDigits(int digits) {
		if (getFormatter() instanceof NumberFormatter && ((NumberFormatter) getFormatter()).getFormat() instanceof NumberFormat) {
			((NumberFormat) ((NumberFormatter) getFormatter()).getFormat()).setMaximumFractionDigits(digits);
			return true;
		}
		return false;
	}

	@Override
	public void setText(String t) {
		super.setText(t);
	}
}
