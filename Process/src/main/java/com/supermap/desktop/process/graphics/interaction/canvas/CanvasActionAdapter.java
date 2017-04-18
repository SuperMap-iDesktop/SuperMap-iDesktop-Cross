package com.supermap.desktop.process.graphics.interaction.canvas;

import javax.swing.event.EventListenerList;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

/**
 * Created by highsad on 2017/3/3.
 */
public abstract class CanvasActionAdapter implements CanvasAction {
	private EventListenerList listenerList = new EventListenerList();

	@Override
	public boolean isEnabled() {
		return true;
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

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {

	}

	@Override
	public void keyReleased(KeyEvent e) {

	}

	@Override
	public void addCanvasActionProcessListener(CanvasActionProcessListener listener) {
		this.listenerList.add(CanvasActionProcessListener.class, listener);
	}

	@Override
	public void removeCanvasActionProcessListener(CanvasActionProcessListener listener) {
		this.listenerList.remove(CanvasActionProcessListener.class, listener);
	}

	protected void fireCanvasActionStart() {
		fireCanvasActionProcess(new CanvasActionProcessEvent(this, CanvasActionProcessEvent.START));
	}

	protected void fireCanvasActionStop() {
		fireCanvasActionProcess(new CanvasActionProcessEvent(this, CanvasActionProcessEvent.STOP));
	}

	private void fireCanvasActionProcess(CanvasActionProcessEvent e) {
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == CanvasActionProcessListener.class) {
				((CanvasActionProcessListener) listeners[i + 1]).canvasActionProcess(e);
			}
		}
	}
}
