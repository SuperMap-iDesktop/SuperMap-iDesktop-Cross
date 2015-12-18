package com.supermap.desktop.CtrlAction.SQLQuery.components;

import com.supermap.desktop.CtrlAction.SQLQuery.JDialogSQLQuery;
import com.supermap.desktop.CtrlAction.SQLQuery.SqlUtilties;

import javax.swing.*;

/**
 * Created by Administrator on 2015/12/2.
 */
public class SQLTextfield extends JTextField implements ISQLBuildComponent {

	private int selectStart = 0;
	private int selectEnd = 0;
	@Override
	public void push(String data, int addMode) {
		// 只能添加字段
		int reSize = 0;
		StringBuilder builder = new StringBuilder(this.getText());
//		if (addMode == JDialogSQLQuery.ADD_DIRECT) {
//			// 直接添加，不作处理
//		} else if (addMode == JDialogSQLQuery.ADD_OPERATOR) {
//			reSize = SqlUtilties.resizeCursorPlace(data);
//		} else if (addMode == JDialogSQLQuery.ADD_FOUNCTION_OR_FIELD) {
//
//		}
		if (selectStart > 0 && !builder.substring(selectStart - 1, selectStart).equals(",") && !builder.substring(selectStart - 1, selectStart).equals("(")) {
			data = "," + data;
		}
		if (selectEnd != builder.length() && !builder.substring(selectEnd, selectEnd + 1).equals(",") && !builder.substring(selectEnd, selectEnd + 1).equals(")")) {
			data = data + ",";
		}
		reSize = SqlUtilties.resizeCursorPlace(data);
		builder.replace(selectStart, selectEnd, data);
		this.setText(builder.toString());
		selectStart = selectStart + data.length() + reSize;
		selectEnd = selectStart;
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
		selectStart =0;
		selectEnd =0;
	}

	@Override
	public void rememberSelectstate() {
		selectStart = this.getSelectionStart();
		selectEnd = this.getSelectionEnd();
	}
}
