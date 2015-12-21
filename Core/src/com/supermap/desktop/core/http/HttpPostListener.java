package com.supermap.desktop.core.http;

import java.util.EventListener;

public interface HttpPostListener extends EventListener {
	void httpPost(HttpPostEvent e);
}
