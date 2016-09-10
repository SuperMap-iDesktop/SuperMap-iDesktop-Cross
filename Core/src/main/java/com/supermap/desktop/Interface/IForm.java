package com.supermap.desktop.Interface;

import com.supermap.desktop.enums.WindowType;

/**
 * 定义窗体所具有的基本功能的接口
 */
public interface IForm {

	/**
	 * Get the text
	 */
	String getText();

	/**
	 * Set the text
	 */
	void setText(String text);

	/**
	 * 获取窗口的类型
	 */
	WindowType getWindowType();

	/**
	 * 保存窗口中的内容。
	 * 
	 * @return 保存成功返回 true；否则返回 false。
	 */
	boolean save();

	/**
	 * 保存窗口中的内容。
	 * 
	 * @param notify
	 *            是否弹出提示对话框。
	 * @param newWindow
	 *            判断窗体是否为新窗体，ture 表示是新窗体，false 表示不是新窗体。
	 * @return 保存成功返回 true；否则返回 false。
	 */
	boolean save(boolean notify, boolean isNewWindow);

	/**
	 * 保存窗口的参数信息，目前场景窗口用到，保存KML信息
	 * 
	 * @return 保存成功返回 true；否则返回 false。
	 */
	boolean saveFormInfos();

	/**
	 * 另存窗口中的内容。
	 * 
	 * @return 另存成功，返回 true，否则返回 false。
	 */
	boolean saveAs(boolean isNewWindow);

	/**
	 * 获取一个值，指示窗口内容是否需要保存。
	 * 
	 * @return
	 */
	boolean isNeedSave();

	/**
	 * 设置一个值，指示窗口内容是否需要保存。
	 * 
	 * @param needSave
	 */
	void setNeedSave(boolean needSave);

	/**
	 * 获取窗口是否处于激活状态。
	 * 
	 * @return
	 */
	boolean isActivated();

	/**
	 * 窗口时触发
	 * 
	 * @return
	 */
	void actived();

	/**
	 * 窗口失去激活状态时触发
	 * 
	 * @return
	 */
	void deactived();

	/**
	 * 窗体由不可见到可见时触发
	 */
	void windowShown();

	/**
	 * 窗体被隐藏时候触发
	 */
	void windowHidden();

	/**
	 * 关闭时清理
	 */
	void clean();

	boolean isClosed();


}