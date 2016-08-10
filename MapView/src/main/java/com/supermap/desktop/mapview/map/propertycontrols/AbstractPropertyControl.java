package com.supermap.desktop.mapview.map.propertycontrols;

import com.supermap.desktop.mapview.layer.propertycontrols.ChangedEvent;
import com.supermap.desktop.mapview.layer.propertycontrols.ChangedListener;
import com.supermap.mapping.Map;
import com.supermap.mapping.MapClosedEvent;
import com.supermap.mapping.MapClosedListener;

import javax.swing.*;

/**
 * @author highsad
 */
public abstract class AbstractPropertyControl extends JPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private boolean isAutoApply = true;
	private String propertyTitle = "";
	private transient Map map;

	private MapClosedListener mapClosedListener = new MapClosedListener() {
		@Override
		public void mapClosed(MapClosedEvent mapClosedEvent) {
			//地图关闭时清空，不然会存在地图对象句柄为空，但地图对象存在的情况
			mapClosedEvent.getMap().removeMapClosedListener(this);
			setMap(null);
		}
	};
	/**
	 * Create the panel.
	 */
	protected AbstractPropertyControl(String propertyTitle) {
		this.propertyTitle = propertyTitle;
		initializeComponents();
		initializeResources();
	}

	public String getPropertyTitle() {
		return propertyTitle;
	}

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		unregisterEvents();
		this.map = map;
		if (map != null) {
			initializePropertyValues(map);
			fillComponents();
			setComponentsEnabled();
			registerEvents();
		}
	}

	public final boolean isAutoApply() {
		return this.isAutoApply;
	}

	public final void setAutoApply(boolean isAutoApply) {
		this.isAutoApply = isAutoApply;
		if (this.isAutoApply) {
			apply();
		}
	}

	public void addChangedListener(ChangedListener listener) {
		this.listenerList.add(ChangedListener.class, listener);
	}

	public void removeChangedListener(ChangedListener listener) {
		this.listenerList.remove(ChangedListener.class, listener);
	}

	public void verify() {
		if (this.isAutoApply) {
			apply();
		} else {
			if (verifyChange()) {
				fireChanged(new ChangedEvent(this, ChangedEvent.CHANGED));
			} else {
				fireChanged(new ChangedEvent(this, ChangedEvent.UNCHANGED));
			}
		}
	}

	public abstract void apply();

	protected abstract void initializeComponents();

	protected abstract void initializeResources();

	/**
	 * 使用 Map 初始化用来做过程记录的属性值。
	 *
	 * @param map
	 */
	protected abstract void initializePropertyValues(Map map);

	protected void registerEvents() {
		if (getMap() != null) {
			this.getMap().addMapClosedListener(this.mapClosedListener);
		}
	}

	protected void unregisterEvents() {
		if (getMap() != null) {
			this.getMap().removeMapClosedListener(this.mapClosedListener);
		}
	}


	protected abstract void fillComponents();

	protected abstract void setComponentsEnabled();

	/**
	 * @return true -- 属性有更改；false -- 属性没有更改。
	 */
	protected abstract boolean verifyChange();

	protected final void fireChanged(ChangedEvent e) {
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ChangedListener.class) {
				((ChangedListener) listeners[i + 1]).changed(e);
			}
		}
	}
}
