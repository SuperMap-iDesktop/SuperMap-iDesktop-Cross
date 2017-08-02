package com.supermap.desktop.userConferenceDemo.demo;

import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import java.awt.*;

/**
 * Created by yuanR on 2017/7/19 0019.
 */
public class DemoParameterButton extends JButton {
	private final String filePath;
	private String name;


	public DemoParameterButton(Icon icon, String filePath, String name) {
		this(null, icon, filePath);
		this.name = name;
	}

	public DemoParameterButton(String text, Icon icon, String filePath) {
		super(text, icon);
		this.filePath = filePath;
		// 黑体，加粗、14号字
		setFont(new Font(ProcessProperties.getString("String_KAIU_TTF"), Font.BOLD, 14));
		// 设置文字对于图片的位置
		setHorizontalTextPosition(SwingConstants.CENTER);
		setVerticalTextPosition(SwingConstants.BOTTOM);
		// 设置其填充区域透明
		setContentAreaFilled(false);
		// 修改鼠标样式
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	}

	public String getFilePath() {
		return filePath;
	}

	public String getName() {
		if (!StringUtilities.isNullOrEmpty(this.name)) {
			return this.name;
		} else {
			return null;
		}
	}
}


