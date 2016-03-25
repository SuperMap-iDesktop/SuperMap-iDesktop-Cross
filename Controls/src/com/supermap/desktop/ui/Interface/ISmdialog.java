package com.supermap.desktop.ui.Interface;

import javax.swing.JRootPane;

import com.supermap.desktop.ui.controls.DialogResult;

/**
 * 集成Smdialog的子窗体实现此接口用于处理键盘响应事件在实 现此接口的同时，覆盖JDialog的createRootPane（） 方法将接口内的方法添加进去以实现创建自己的类后自动响应 Enter,Esc键的功能
 * 
 * @author xie
 *
 */
public interface ISmdialog {
	/**
	 * 
	 * 覆盖父类的方法。实现自己的ENTER点击事件
	 * 
	 * @return
	 */
	public JRootPane enterPressed();

	/**
	 * 覆盖父类的方法。实现自己的ESC点击事件
	 * 
	 * @return
	 */
	public JRootPane escPressed();

	/**
	 * 获取结果
	 * 
	 * @return
	 */
	public DialogResult getDialogResult();

	/**
	 * 设置结果
	 * 
	 * @return
	 */
	public void setDialogResult(DialogResult dialogResult);
}
