package com.supermap.desktop.ui.mdi;

import com.supermap.desktop.ui.mdi.util.MdiResource;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class MdiPage {

	private String title;
	private ImageIcon icon;
	private boolean isClosed = false;
	private boolean isMaximum = false;

	private MdiGroup group;
	private Component c;

	private EventListenerList propertyChangeListeners = new EventListenerList();

	public static final String TITLE_PROPERTY = "TITLE_PROPERTY";

	private MdiPage(Component c, String title, boolean isClosable, boolean isFloatable) {
		this.c = c;
		setTitle(title);
		this.icon = MdiResource.getIcon(MdiResource.FILE);
	}

	public static MdiPage createMdiPage(Component c) {
		return createMdiPage(c, "MdiPage", true, true);
	}

	public static MdiPage createMdiPage(Component c, String title) {
		return createMdiPage(c, title, true, true);
	}

	public static MdiPage createMdiPage(Component c, String title, boolean isClosable, boolean isFloatable) {
		if (c == null) {
			return null;
		} else {
			return new MdiPage(c, title, isClosable, isFloatable);
		}
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		if (!StringUtilities.stringEquals(this.title, title, false)) {
			String oldValue = MdiPage.this.title;
			this.title = title;
			firePropertyChangedListener(new PropertyChangeEvent(this, TITLE_PROPERTY, oldValue, MdiPage.this.title));
		}
	}

	public ImageIcon getIcon() {
		return icon;
	}

	public void setIcon(ImageIcon icon) {
		this.icon = icon;
	}

	public void close() {
		if (this.group != null) {
			this.group.close(this);
		}
	}

	public boolean isClosed() {
		return this.group == null || !this.group.isContain(this);
	}

	public boolean isFloating() {
		return this.group != null && this.group.isPageFloating(this);
	}

	public boolean isMaximum() {
		return isMaximum;
	}

	public void setMaximum(boolean isMaximum) {
		this.isMaximum = isMaximum;
	}

	public boolean isActive() {
		return this.group != null && this.group.isActive(this);
	}

	public void active() {
		if (this.group != null) {
			this.group.activePage(this);
		}
	}

	public IMdiContainer getContainer() {
		return this.group == null ? null : this.group.getMdiContainer();
	}

	public MdiGroup getGroup() {
		return this.group;
	}

	public void setGroup(MdiGroup group) {
		this.group = group;
	}

	public Component getComponent() {
		return this.c;
	}

	@Override
	public String toString() {
		return getTitle();
	}

	public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
		this.propertyChangeListeners.add(PropertyChangeListener.class, propertyChangeListener);
	}

	public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
		this.propertyChangeListeners.remove(PropertyChangeListener.class, propertyChangeListener);
	}

	private void firePropertyChangedListener(PropertyChangeEvent propertyChangeEvent) {
		Object[] listeners = this.propertyChangeListeners.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == PropertyChangeListener.class) {
				((PropertyChangeListener) listeners[i + 1]).propertyChange(propertyChangeEvent);
			}
		}
	}
}
