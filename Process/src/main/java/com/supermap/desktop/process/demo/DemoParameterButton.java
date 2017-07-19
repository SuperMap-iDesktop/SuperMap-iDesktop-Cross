package com.supermap.desktop.process.demo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by yuanR on 2017/7/19 0019.
 */
public class DemoParameterButton extends JButton {

	private MouseListener mouseListener = new MouseListener() {
		@Override
		public void mouseClicked(MouseEvent e) {

		}

		@Override
		public void mousePressed(MouseEvent e) {

		}

		@Override
		public void mouseReleased(MouseEvent e) {

		}

		@Override
		public void mouseEntered(MouseEvent e) {
			setContentAreaFilled(true);
		}

		@Override
		public void mouseExited(MouseEvent e) {
			setContentAreaFilled(false);
		}
	};

	public DemoParameterButton(String text, Icon icon) {
		super(text, icon);

		Dimension preferredSizeNew = new Dimension(180, 130);
		setPreferredSize(preferredSizeNew);
		// 设置文字对于图片的位置
		setHorizontalTextPosition(SwingConstants.CENTER);
		setVerticalTextPosition(SwingConstants.BOTTOM);
		// 设置其填充区域透明
		setContentAreaFilled(false);
		addMouseListener(mouseListener);
	}
}


