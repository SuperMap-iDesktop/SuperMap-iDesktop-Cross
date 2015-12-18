package com.supermap.desktop.CtrlAction.SQLQuery.components;

/**
 * sql表达式输入控件接口
 * @author xiajt
 */
public interface ISQLBuildComponent {
	void push(String data,int addMode);
	String getSQLExpression();
	void clear();
	void rememberSelectstate();
}
