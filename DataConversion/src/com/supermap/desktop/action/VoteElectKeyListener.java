package com.supermap.desktop.action;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * @author Administrator 只允许文本框中输入数字，不能输入字符等其他类型
 */
public class VoteElectKeyListener implements KeyListener {

	@Override
	public void keyTyped(KeyEvent e) {
		int keyChar = e.getKeyChar();
		if (keyChar >= KeyEvent.VK_0 && keyChar <= KeyEvent.VK_9) {
			return;
		} else {
			e.consume();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

}