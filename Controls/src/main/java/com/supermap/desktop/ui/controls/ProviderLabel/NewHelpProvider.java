package com.supermap.desktop.ui.controls.ProviderLabel;

import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;

import javax.swing.*;
import java.awt.*;

/**
 * Created by yuanR on 2017/9/7 0007.
 * 新的带有提示icon的lable
 */
public class NewHelpProvider extends JPanel {

	private String tipText = "";
	private String text = "";

	public NewHelpProvider(String text, String tipText) {
		this.text = text;
		this.tipText = tipText;
		initLayout();
	}

	private void initLayout() {
		this.setLayout(new GridBagLayout());
		// 需要用提示icon来显示提示信息
		this.add(new JLabel(text), new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(0, 1).setAnchor(GridBagConstraintsHelper.WEST));
		this.add(new WarningOrHelpProvider(tipText, false), new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraintsHelper.WEST));
	}
}
