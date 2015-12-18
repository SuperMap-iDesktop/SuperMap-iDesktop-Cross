package com.supermap.desktop.CtrlAction.SQLQuery.components;

import com.supermap.desktop.CtrlAction.SQLQuery.JDialogSQLQuery;
import com.supermap.desktop.CtrlAction.SQLQuery.SqlUtilties;

import javax.swing.*;
import javax.xml.crypto.Data;

/**
 * SQL表达式输入区域
 */
public class SQLTextarea extends JTextArea implements ISQLBuildComponent {
	private int selectStart = 0;
	private int selectEnd = 0;

	public SQLTextarea() {
		super();
		this.setLineWrap(true);
	}


	@Override
	public void push(String data, int addMode) {
		int reSize = 0;
		StringBuilder builder = new StringBuilder(this.getText());
		if (addMode == JDialogSQLQuery.ADD_DIRECT) {
			// 直接添加，不作处理
		} else if (addMode == JDialogSQLQuery.ADD_OPERATOR) {
			reSize = SqlUtilties.resizeCursorPlace(data);
		} else if (addMode == JDialogSQLQuery.ADD_FOUNCTION_OR_FIELD) {
			if (selectStart > 0 && !builder.substring(selectStart - 1, selectStart).equals(",") && !builder.substring(selectStart - 1, selectStart).equals("(")) {
				data = "," + data;
			}
			if (selectEnd != builder.length() && !builder.substring(selectEnd, selectEnd + 1).equals(",") && !builder.substring(selectEnd, selectEnd + 1).equals(")")) {
				data = data + ",";
			}
			reSize = SqlUtilties.resizeCursorPlace(data);
		}
		builder.replace(selectStart, selectEnd, data);
		this.setText(builder.toString());
		this.selectStart = selectStart + data.length() + reSize;
		this.selectEnd = selectStart;
		this.requestFocus();
		this.setCaretPosition(selectStart);

	}

	@Override
	public String getSQLExpression() {
		return this.getText();
	}

	@Override
	public void clear() {
		this.setText("");
		selectStart = 0;
		selectEnd = 0;
	}

	@Override
	public void rememberSelectstate() {
		selectStart = this.getSelectionStart();
		selectEnd = this.getSelectionEnd();
	}
}
