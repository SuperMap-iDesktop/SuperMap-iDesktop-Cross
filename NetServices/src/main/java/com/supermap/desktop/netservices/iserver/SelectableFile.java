package com.supermap.desktop.netservices.iserver;

import com.supermap.desktop.event.SelectedChangeEvent;
import com.supermap.desktop.event.SelectedChangeListener;

import javax.swing.event.EventListenerList;
import java.io.File;

/**
 * 带选择状态的文件封装
 * 
 * @author highsad
 *
 */
public class SelectableFile extends File {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean isSelected = false;
	private EventListenerList listenerList = new EventListenerList();

	public SelectableFile(String pathname, boolean isSelected) {
		super(pathname);
		this.isSelected = isSelected;
	}

	public static SelectableFile fromFile(File file, boolean isSelected) {
		return new SelectableFile(file.getPath(), isSelected);
	}

	public boolean isSelected() {
		return this.isSelected;
	}

	public void setIsSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public void addSelectedChangeListener(SelectedChangeListener listener) {
		this.listenerList.add(SelectedChangeListener.class, listener);
	}

	public void removeSelectedChangeListener(SelectedChangeListener listener) {
		this.listenerList.remove(SelectedChangeListener.class, listener);
	}

	private void fireSelectedChange(SelectedChangeEvent e) {
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == SelectedChangeListener.class) {
				((SelectedChangeListener) listeners[i + 1]).selectedChange(e);
			}
		}
	}
}
