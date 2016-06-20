package com.supermap.desktop.http;

import java.util.EventListener;

public interface FileSteppedListener extends EventListener {
	public void stepped(FileEvent event);
}
