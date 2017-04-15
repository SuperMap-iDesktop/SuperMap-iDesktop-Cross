package com.supermap.desktop.process.graphics.interaction.canvas;

import com.supermap.desktop.process.graphics.GraphCanvas;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by highsad on 2017/4/15.
 */
public class CanvasActionsManager implements CanvasAction {
	private GraphCanvas canvas;
	private ConcurrentHashMap<Class, CanvasAction> actions = new ConcurrentHashMap<>();

	public CanvasActionsManager(GraphCanvas canvas) {
		this.canvas = canvas;
	}

	public GraphCanvas getCanvas() {
		return this.canvas;
	}

	public void installAction(CanvasAction action) {
		if (action == null) {
			return;
		}

		installAction(action.getClass(), action);
	}

	public void installAction(Class c, CanvasAction action) {
		if (c == null || action == null) {
			return;
		}

		if (this.actions.containsKey(c)) {
			this.actions.get(c).clean();
		}

		this.actions.put(c, action);
	}

	public void uninstallAction(Class c) {
		if (!this.actions.containsKey(c)) {
			return;
		}

		CanvasAction action = this.actions.get(c);
		action.clean();
		this.actions.remove(c);
	}

	public CanvasAction getAction(Class c) {
		if (!this.actions.containsKey(c)) {
			return null;
		}

		return this.actions.get(c);
	}

	public void setActionEnabled(Class c, boolean enabled) {
		CanvasAction action = getAction(c);
		if (action != null) {
			action.setEnabled(enabled);
		}
	}

	public Class[] getActionKeys() {
		if (this.actions.size() > 0) {
			Class[] keys = new Class[this.actions.size()];
			this.actions.keySet().toArray(keys);
			return keys;
		} else {
			return null;
		}
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public void setEnabled(boolean enabled) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clean() {
		Set<Class> keys = this.actions.keySet();

		if (keys != null && keys.size() > 0) {
			for (Class c :
					keys) {
				this.actions.get(c).clean();
			}

			this.actions.clear();
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		Set<Map.Entry<Class, CanvasAction>> set = this.actions.entrySet();
		Iterator<Map.Entry<Class, CanvasAction>> iterator = set.iterator();

		while (iterator.hasNext()) {
			Map.Entry<Class, CanvasAction> entry = iterator.next();
			if (entry.getValue().isEnabled()) {
				entry.getValue().keyTyped(e);
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		Set<Map.Entry<Class, CanvasAction>> set = this.actions.entrySet();
		Iterator<Map.Entry<Class, CanvasAction>> iterator = set.iterator();

		while (iterator.hasNext()) {
			Map.Entry<Class, CanvasAction> entry = iterator.next();
			if (entry.getValue().isEnabled()) {
				entry.getValue().keyPressed(e);
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		Set<Map.Entry<Class, CanvasAction>> set = this.actions.entrySet();
		Iterator<Map.Entry<Class, CanvasAction>> iterator = set.iterator();

		while (iterator.hasNext()) {
			Map.Entry<Class, CanvasAction> entry = iterator.next();
			if (entry.getValue().isEnabled()) {
				entry.getValue().keyReleased(e);
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Set<Map.Entry<Class, CanvasAction>> set = this.actions.entrySet();
		Iterator<Map.Entry<Class, CanvasAction>> iterator = set.iterator();

		while (iterator.hasNext()) {
			Map.Entry<Class, CanvasAction> entry = iterator.next();
			if (entry.getValue().isEnabled()) {
				entry.getValue().mouseClicked(e);
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		Set<Map.Entry<Class, CanvasAction>> set = this.actions.entrySet();
		Iterator<Map.Entry<Class, CanvasAction>> iterator = set.iterator();

		while (iterator.hasNext()) {
			Map.Entry<Class, CanvasAction> entry = iterator.next();
			if (entry.getValue().isEnabled()) {
				entry.getValue().mousePressed(e);
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		Set<Map.Entry<Class, CanvasAction>> set = this.actions.entrySet();
		Iterator<Map.Entry<Class, CanvasAction>> iterator = set.iterator();

		while (iterator.hasNext()) {
			Map.Entry<Class, CanvasAction> entry = iterator.next();
			if (entry.getValue().isEnabled()) {
				entry.getValue().mouseReleased(e);
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		Set<Map.Entry<Class, CanvasAction>> set = this.actions.entrySet();
		Iterator<Map.Entry<Class, CanvasAction>> iterator = set.iterator();

		while (iterator.hasNext()) {
			Map.Entry<Class, CanvasAction> entry = iterator.next();
			if (entry.getValue().isEnabled()) {
				entry.getValue().mouseEntered(e);
			}
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		Set<Map.Entry<Class, CanvasAction>> set = this.actions.entrySet();
		Iterator<Map.Entry<Class, CanvasAction>> iterator = set.iterator();

		while (iterator.hasNext()) {
			Map.Entry<Class, CanvasAction> entry = iterator.next();
			if (entry.getValue().isEnabled()) {
				entry.getValue().mouseExited(e);
			}
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		Set<Map.Entry<Class, CanvasAction>> set = this.actions.entrySet();
		Iterator<Map.Entry<Class, CanvasAction>> iterator = set.iterator();

		while (iterator.hasNext()) {
			Map.Entry<Class, CanvasAction> entry = iterator.next();
			if (entry.getValue().isEnabled()) {
				entry.getValue().mouseDragged(e);
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		Set<Map.Entry<Class, CanvasAction>> set = this.actions.entrySet();
		Iterator<Map.Entry<Class, CanvasAction>> iterator = set.iterator();

		while (iterator.hasNext()) {
			Map.Entry<Class, CanvasAction> entry = iterator.next();
			if (entry.getValue().isEnabled()) {
				entry.getValue().mouseMoved(e);
			}
		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		Set<Map.Entry<Class, CanvasAction>> set = this.actions.entrySet();
		Iterator<Map.Entry<Class, CanvasAction>> iterator = set.iterator();

		while (iterator.hasNext()) {
			Map.Entry<Class, CanvasAction> entry = iterator.next();
			if (entry.getValue().isEnabled()) {
				entry.getValue().mouseWheelMoved(e);
			}
		}
	}
}
