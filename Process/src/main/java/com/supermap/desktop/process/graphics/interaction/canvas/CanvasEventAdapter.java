package com.supermap.desktop.process.graphics.interaction.canvas;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

/**
 * Created by highsad on 2017/3/3.
 */
public abstract class CanvasEventAdapter implements CanvasEventHandler {
	private boolean enable = true;

	@Override
	public boolean isEnabled() {
		return this.enable;
	}

	@Override
	public void setEnabled(boolean enable) {
		this.enable = enable;
	}

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

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mouseDragged(MouseEvent e) {

	}

	@Override
	public void mouseMoved(MouseEvent e) {

	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {

	}
}
