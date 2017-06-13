package com.supermap.desktop.process.graphics.interaction.canvas;

import com.supermap.desktop.process.graphics.GraphCanvas;

import javax.swing.event.EventListenerList;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by highsad on 2017/4/15.
 */
public class CanvasActionsManager implements CanvasAction, CanvasActionProcessListener {
	private GraphCanvas canvas;
	private ConcurrentHashMap<Class, CanvasAction> actions = new ConcurrentHashMap<>();
	private ConcurrentHashMap<Class, List<Class>> mutex = new ConcurrentHashMap<>();

	private List<CanvasAction> lockedActions = new ArrayList<>();

	private EventListenerList listenerList = new EventListenerList();

	/**
	 * 管理所已安装的 CanvasAction 的优先级。数值越小，优先级越高。
	 * Managing the priorities of all installed {@link ActionType}.
	 * The smaller the number value,the higher the priority.
	 */
	private ConcurrentHashMap<ActionType, ConcurrentHashMap<Class, Integer>> priorities = new ConcurrentHashMap<>();
	private ConcurrentHashMap<ActionType, List<Class>> sortedPriorities = new ConcurrentHashMap<>();

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

		if (this.mutex.containsKey(c)) {
			this.mutex.remove(c);
			action.removeCanvasActionProcessListener(this);
		}
	}

	/**
	 * @param action
	 * @param waiting
	 */
	public void addMutexAction(Class action, Class waiting) {
		if (action == waiting) {
			return;
		}

		if (!this.actions.containsKey(action) || !this.actions.containsKey(waiting)) {
			return;
		}

		if (!this.mutex.containsKey(action)) {
			CanvasAction ca = this.actions.get(action);
			ca.addCanvasActionProcessListener(this);
			this.mutex.put(action, new ArrayList<Class>());
		}

		List<Class> waitingActions = this.mutex.get(action);
		if (!waitingActions.contains(waiting)) {
			waitingActions.add(waiting);
		}
	}

	public void removeMutexAction(Class action, Class waiting) {
		if (action == waiting) {
			return;
		}

		if (!this.actions.containsKey(action) || !this.actions.containsKey(waiting)) {
			return;
		}

		if (!this.mutex.containsKey(action)) {
			return;
		}

		List<Class> waitingActions = this.mutex.get(action);
		if (waitingActions.contains(waiting)) {
			waitingActions.remove(waiting);
		}

		if (waitingActions.size() == 0) {
			this.mutex.remove(action);
			CanvasAction ca = this.actions.get(action);
			ca.removeCanvasActionProcessListener(this);
		}
	}

	/**
	 * 设置优先级
	 *
	 * @param actionType
	 * @param action
	 * @param priority
	 */
	public void setPriority(ActionType actionType, Class action, int priority) {
		if (!this.actions.containsKey(action)) {
			return;
		}

		if (!this.priorities.containsKey(actionType) || this.priorities.get(actionType) == null) {
			this.priorities.put(actionType, new ConcurrentHashMap<Class, Integer>());
			this.sortedPriorities.put(actionType, new ArrayList<Class>());
		}

		ConcurrentHashMap<Class, Integer> appointedPrios = this.priorities.get(actionType);
		appointedPrios.put(action, priority);
		this.sortedPriorities.get(actionType).add(action);
		prioritizeActions(actionType);
	}

	/**
	 * 取消优先级设置
	 *
	 * @param actionType
	 * @param action
	 */
	public void clearPriority(ActionType actionType, Class action) {
		if (!this.actions.containsKey(action)) {
			return;
		}

		ConcurrentHashMap<Class, Integer> appointedPrios = this.priorities.get(actionType);
		if (appointedPrios != null && appointedPrios.containsKey(action)) {
			appointedPrios.remove(action);
			this.sortedPriorities.get(actionType).remove(action);
		}
	}

	private void prioritizeActions(ActionType actionType) {
		if (!this.priorities.containsKey(actionType)
				|| this.priorities.get(actionType) == null
				|| this.priorities.get(actionType).size() == 0) {
			return;
		}

		final ConcurrentHashMap<Class, Integer> appointedPrios = this.priorities.get(actionType);

		Collections.sort(this.sortedPriorities.get(actionType), new Comparator<Class>() {
			@Override
			public int compare(Class o1, Class o2) {
				if (appointedPrios.get(o1) < appointedPrios.get(o2)) {
					return -1;
				} else if (appointedPrios.get(o1) > appointedPrios.get(o2)) {
					return 1;
				} else {
					return 0;
				}
			}
		});
	}

	private List<Class> getSortedActions(ActionType actionType) {
		List<Class> result = new ArrayList<>();
		List<Class> prioritized = this.sortedPriorities.containsKey(actionType) ? this.sortedPriorities.get(actionType) : null;

		if (prioritized != null) {
			result.addAll(prioritized);
		}

		Set<Map.Entry<Class, CanvasAction>> set = this.actions.entrySet();
		for (Map.Entry<Class, CanvasAction> entry : set) {
			if (!result.contains(entry.getKey())) {
				result.add(entry.getKey());
			}
		}
		return result;
	}

	public CanvasAction getAction(Class c) {
		if (!this.actions.containsKey(c)) {
			return null;
		}

		return this.actions.get(c);
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
	public void clean() {
		Set<Class> keys = this.actions.keySet();

		if (keys.size() > 0) {
			for (Class c :
					keys) {
				this.actions.get(c).clean();
			}

			this.actions.clear();
		}
	}

	@Override
	public void addCanvasActionProcessListener(CanvasActionProcessListener listener) {
		this.listenerList.add(CanvasActionProcessListener.class, listener);
	}

	@Override
	public void removeCanvasActionProcessListener(CanvasActionProcessListener listener) {
		this.listenerList.remove(CanvasActionProcessListener.class, listener);
	}

	@Override
	public void keyTyped(KeyEvent e) {
//		Set<Map.Entry<Class, CanvasAction>> set = this.actions.entrySet();
//
//		for (Map.Entry<Class, CanvasAction> entry : set) {
//			if (entry.getValue().isEnabled()) {
//				entry.getValue().keyTyped(e);
//			}
//		}

		List<Class> keys = getSortedActions(ActionType.KEY_TYPED);
		for (int i = 0, size = keys.size(); i < size; i++) {
			CanvasAction action = this.actions.get(keys.get(i));
			if (!lockedActions.contains(action) && action.isEnabled()) {
				action.keyTyped(e);
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		List<Class> keys = getSortedActions(ActionType.KEY_PRESSED);
		for (int i = 0, size = keys.size(); i < size; i++) {
			CanvasAction action = this.actions.get(keys.get(i));
			if (!lockedActions.contains(action) && action.isEnabled()) {
				action.keyPressed(e);
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		List<Class> keys = getSortedActions(ActionType.KEY_RELEASED);
		for (int i = 0, size = keys.size(); i < size; i++) {
			CanvasAction action = this.actions.get(keys.get(i));
			if (!lockedActions.contains(action) && action.isEnabled()) {
				action.keyReleased(e);
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		List<Class> keys = getSortedActions(ActionType.MOUSE_CLICKED);
		for (int i = 0, size = keys.size(); i < size; i++) {
			CanvasAction action = this.actions.get(keys.get(i));
			if (!lockedActions.contains(action) && action.isEnabled()) {
				action.mouseClicked(e);
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		List<Class> keys = getSortedActions(ActionType.MOUSE_PRESSED);
		for (int i = 0, size = keys.size(); i < size; i++) {
			CanvasAction action = this.actions.get(keys.get(i));
			if (!lockedActions.contains(action) && action.isEnabled()) {
				action.mousePressed(e);
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		List<Class> keys = getSortedActions(ActionType.MOUSE_RELEASED);
		for (int i = 0, size = keys.size(); i < size; i++) {
			CanvasAction action = this.actions.get(keys.get(i));
			if (!lockedActions.contains(action) && action.isEnabled()) {
				action.mouseReleased(e);
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		List<Class> keys = getSortedActions(ActionType.MOUSE_ENTERED);
		for (int i = 0, size = keys.size(); i < size; i++) {
			CanvasAction action = this.actions.get(keys.get(i));
			if (!lockedActions.contains(action) && action.isEnabled()) {
				action.mouseEntered(e);
			}
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		List<Class> keys = getSortedActions(ActionType.MOUSE_EXITED);
		for (int i = 0, size = keys.size(); i < size; i++) {
			CanvasAction action = this.actions.get(keys.get(i));
			if (!lockedActions.contains(action) && action.isEnabled()) {
				action.mouseExited(e);
			}
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		List<Class> keys = getSortedActions(ActionType.MOUSE_DRAGGED);
		for (int i = 0, size = keys.size(); i < size; i++) {
			CanvasAction action = this.actions.get(keys.get(i));
			if (!lockedActions.contains(action) && action.isEnabled()) {
				action.mouseDragged(e);
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		List<Class> keys = getSortedActions(ActionType.MOUSE_MOVED);
		for (int i = 0, size = keys.size(); i < size; i++) {
			CanvasAction action = this.actions.get(keys.get(i));
			if (!lockedActions.contains(action) && action.isEnabled()) {
				action.mouseMoved(e);
			}
		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		List<Class> keys = getSortedActions(ActionType.MOUSE_WHEEL_MOVED);
		for (int i = 0, size = keys.size(); i < size; i++) {
			CanvasAction action = this.actions.get(keys.get(i));
			if (!lockedActions.contains(action) && action.isEnabled()) {
				action.mouseWheelMoved(e);
			}
		}
	}

	@Override
	public void canvasActionProcess(CanvasActionProcessEvent e) {
		fireCanvasActionProcess(e);

		CanvasAction action = e.getAction();
		Class key = getKey(action);

		if (!this.mutex.containsKey(key)) {
			return;
		}

		List<Class> waitings = this.mutex.get(key);
		if (e.getStatus() == CanvasActionProcessEvent.START) {
			for (int i = 0, size = waitings.size(); i < size; i++) {
				CanvasAction ca = this.actions.get(waitings.get(i));
				if (!this.lockedActions.contains(ca)) {
					this.lockedActions.add(ca);
				}
			}
		} else if (e.getStatus() == CanvasActionProcessEvent.STOP) {
			for (int i = 0, size = waitings.size(); i < size; i++) {
				CanvasAction ca = this.actions.get(waitings.get(i));
				if (this.lockedActions.contains(ca)) {
					this.lockedActions.remove(ca);
				}
			}
		}
	}

	private Class getKey(CanvasAction action) {
		Class result = null;

		for (Map.Entry<Class, CanvasAction> entry :
				this.actions.entrySet()) {
			if (entry.getValue() == action) {
				result = entry.getKey();
				break;
			}
		}
		return result;
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
