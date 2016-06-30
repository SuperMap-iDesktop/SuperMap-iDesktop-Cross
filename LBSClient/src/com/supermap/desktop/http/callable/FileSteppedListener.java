package com.supermap.desktop.http.callable;

import java.util.EventListener;

public interface FileSteppedListener extends EventListener {
	public void stepped(FileEvent event);
}
