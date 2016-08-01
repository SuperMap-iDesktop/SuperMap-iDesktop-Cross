package com.supermap.desktop.CtrlAction.settings;

import com.supermap.desktop.ui.controls.FileChooserControl;
import com.supermap.desktop.ui.controls.TextFields.SmTextFieldLegit;

import javax.swing.*;

/**
 * @author XiaJT
 */
public class JPanelSettingEnvironment extends BaseSettingPanel {

	private JLabel labelTitle;// 桌面标题
	private SmTextFieldLegit smTextFieldLegitTitle;

	private JLabel labelOMPNumThreads; // 并行计算线程数
	private JSpinner spinnerOMPNumThreads;

	private JLabel labelAnalystMemorySize;// 分析内存模式
	private JComboBox<String> comboBoxAnalystMemorySize;

	private JCheckBox checkBoxSceneAntialias;// 场景反走样系数
	private JSpinner spinnerSceneAntialiasValue;

	private JCheckBox checkBoxCUDAComputingEnabled;//是否启用GPU

	private JLabel labelFileCache;
	private FileChooserControl fileChooserControlFileCache;

	private JPanel panelOutput;
	private JCheckBox checkBoxOutputInfo;
	private JCheckBox checkBoxOutputException;



	@Override

	public void apply() {

	}

	@Override
	public void dispose() {

	}
}
