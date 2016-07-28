package com.supermap.desktop.CtrlAction.settings;

import javax.swing.*;

/**
 * 常用选项
 *
 * @author XiaJT
 */
public class JPanelSettingCommon extends JPanel implements ISetting {

	//	private JCheckBox checkBoxIsAutoCloseEmptyForm;
	private JCheckBox checkBoxIsShowFormClosingInfo;// 关闭窗口提示保存
	private JCheckBox checkBoxWorkspaceCloseNotify;// 工作空间关闭提示保存
	private JCheckBox checkBoxCloseMemoryDatasourceNotify;// 关闭工作空间提示内存数据源
//	private JCheckBox


	@Override
	public JPanel getPanel() {
		return this;
	}

	@Override
	public void apply() {

	}

	@Override
	public void dispose() {

	}
}
