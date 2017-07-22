package com.supermap.desktop.process.demo;

import com.supermap.desktop.process.ProcessProperties;

import javax.swing.*;
import java.awt.*;

/**
 * Created by yuanR on 2017/7/19 0019.
 */
public class DemoParameterButton extends JButton {

	private final String filePath;


	public DemoParameterButton(String text, Icon icon, String filePath) {
		super(text, icon);
		this.filePath = filePath;
		// 黑体，加粗、14号字
		setFont(new Font(ProcessProperties.getString("String_KAIU_TTF"), Font.BOLD, 14));
		Dimension preferredSizeNew = new Dimension(180, 130);
		setPreferredSize(preferredSizeNew);
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
		String text = filePath.split("\\.")[0];
		return text;
	}
}


