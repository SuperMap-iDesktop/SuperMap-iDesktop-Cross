package com.supermap.desktop.implement;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.*;

/**
 * Created by xie on 2016/11/7.
 */
public class DefaultComboBoxUI extends BasicComboBoxUI {
	public static ComponentUI createUI(JComponent c) {
		return new DefaultComboBoxUI();
	}

	protected JButton createArrowButton() {
//		JButton button = new BasicArrowButton(BasicArrowButton.SOUTH,
//				UIManager.getColor("ComboBox.buttonBackground"),
//				UIManager.getColor("ComboBox.buttonShadow"),
//				UIManager.getColor("ComboBox.buttonDarkShadow"),
//				UIManager.getColor("ComboBox.buttonHighlight"));
//		button.setName("ComboBox.arrowButton");
//		return button;
		JButton button = new BasicArrowButton(BasicArrowButton.SOUTH,
				Color.white,
				Color.white,
				Color.black,
				UIManager.getColor("ComboBox.buttonHighlight"));
		button.setBorder(new LineBorder(Color.white));
		button.setName("ComboBox.arrowButton");
		return button;
//		JButton button = new JButton(new MetalComboBoxIcon());
//		button.setBorder(new LineBorder(Color.white));
//		button.setBackground(Color.white);
//		button.setForeground(Color.white);
//		return button;
	}
}
