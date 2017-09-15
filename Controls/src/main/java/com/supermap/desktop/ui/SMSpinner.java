package com.supermap.desktop.ui;

import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.text.ParseException;

/**
 * 封装 JSpinner，使得每一次敲击键盘，都提交编辑，如果输入不合法则恢复上一次合法的值
 *
 * @author highsad
 */
public class SMSpinner extends JSpinner implements DocumentListener {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a spinner for the given model. The spinner has a set of previous/next buttons, and an editor appropriate for the model.
	 *
	 * @throws NullPointerException if the model is {@code null}
	 */
	public SMSpinner(SpinnerModel model) {
		super(model);

		if (getEditor() instanceof DefaultEditor) {
			DefaultEditor editor = (DefaultEditor) getEditor();
			JTextField text = editor.getTextField();
			text.setHorizontalAlignment(JTextField.LEFT);
			editor.getTextField().getDocument().addDocumentListener(this);
		}
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				update();
			}
		});
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				update();
			}
		});
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				update();
			}
		});
	}

	private void updateEdit() {
		if (getEditor() instanceof DefaultEditor) {
			Object lastValue = getValue();
			DefaultEditor editor = (DefaultEditor) getEditor();

			try {
				if (!StringUtilities.isNullOrEmpty(editor.getTextField().getText())) {
					commitEdit();
				}
			} catch (ParseException e1) {
				try {
					editor.getTextField().setValue(lastValue);
					//this.setValue(lastValue);
				} catch (Exception e) {
					//ignore
				}

			}
		}
	}

	/**
	 * 由于每一次文本更改导致提交都会使光标跑到最前，因此做一下光标位置的处理
	 */
	private void update() {
		DefaultEditor editor = (DefaultEditor) getEditor();

		// 记录更新之前光标位置到末尾的位置差
		int preCaretToEnd = editor.getTextField().getText().length() - editor.getTextField().getCaretPosition();
		updateEdit();

		// 设置光标所处的位置
		if (editor.getTextField().getText().length() - preCaretToEnd >= 0) {
			editor.getTextField().setCaretPosition(editor.getTextField().getText().length() - preCaretToEnd);
		}

		// When you click arrow buttons, the cursor goes to the front of the text,
		// and actually you need to set the cursor behind the text. So, with the following code.
		//However, this will cause the cursor to flash, so far there is no good solution, so for the time being.
		if (editor.getTextField().getCaretPosition()==0 && (editor.getTextField().getCaretPosition()!=editor.getTextField().getText().length()-1
		|| editor.getTextField().getText().length()==1)){
			editor.getTextField().setCaretPosition(editor.getTextField().getText().length());
		}
	}

}
