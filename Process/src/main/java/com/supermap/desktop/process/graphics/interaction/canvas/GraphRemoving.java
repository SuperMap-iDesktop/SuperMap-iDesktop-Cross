package com.supermap.desktop.process.graphics.interaction.canvas;

import java.awt.event.KeyEvent;

/**
 * Created by highsad on 2017/3/25.
 */
public class GraphRemoving extends CanvasEventAdapter {
	@Override
	public void clean() {

	}

	@Override
	public void keyTyped(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			System.out.println("esc");
		}
	}
}
