package com.supermap.desktop.Interface;

import com.supermap.desktop.event.*;

public interface IFormManager {

	/**
	 * 获取指定索引的子窗体。
	 */
	IForm get(int index);

	void add(IForm form);

	void add(IForm form, int index);

	// IForm this[Int32 index]

	/**
	 * 获取应用程序内的子窗体的总数。
	 */
	int getCount();

	/**
	 * 获取或者设置当前被激活的子窗体。
	 */
	IForm getActiveForm();

	void setActiveForm(IForm form);

	/**
	 * 重新发送请求确保当前窗体信息一致
	 */
	void resetActiveForm();

	void showChildForm(IForm childForm);

	void addFormShownListener(FormShownListener listener);

	void removeFormShownListener(FormShownListener listener);

	void addFormClosingListener(FormClosingListener listener);

	void removeFormClosingListener(FormClosingListener listener);

	void addFormClosedListener(FormClosedListener listener);

	void removeFormClosedListener(FormClosedListener listener);

	void addActiveFormChangedListener(ActiveFormChangedListener listener);

	void removeActiveFormChangedListener(ActiveFormChangedListener listener);

	/**
	 * 关闭指定的子窗体。
	 *
	 * @param form 指定的要保存的子窗口。
	 * @returns 关闭成功返回 true；否则返回 false
	 */
	boolean close(IForm form);

	/**
	 * 关闭所有的子窗体。
	 *
	 * @returns 关闭成功返回 true；否则返回 false
	 */
	boolean closeAll();

	/**
	 * 关闭所有的子窗体。
	 *
	 * @param isSave 是否保存窗口内容。
	 * @returns 关闭成功返回 true；否则返回 false
	 */
	boolean closeAll(boolean isSave);

	/**
	 * 保存所有的子窗体内容。
	 *
	 * @param notify 是否弹出提示对话框，true 表示弹出对话框进行提示，否则不会提示。
	 * @returns 保存成功返回 true；否则返回 false
	 */
	boolean saveAll(boolean notify);

	boolean isContain(IForm form);
}
