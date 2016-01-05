package com.supermap.desktop.ui.controls;

/**
 * 对话框操作结果枚举类型
 * @author xuzw
 *
 */
public class DialogResult extends com.supermap.data.Enum{

	protected DialogResult(int value, int ugcValue) {
		super(value, ugcValue);
	}
	
	public static final DialogResult YES = new DialogResult(0, 0);
	
	public static final DialogResult NO = new DialogResult(1, 1);
	
	public static final DialogResult OK = new DialogResult(0, 0);
	
	public static final DialogResult CANCEL = new DialogResult(2, 2);
	
	public static final DialogResult APPLY = new DialogResult(3, 3);
	
	public static final DialogResult CLOSED = new DialogResult(-1, -1);
}
