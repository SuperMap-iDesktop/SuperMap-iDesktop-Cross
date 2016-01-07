package com.supermap.desktop.core.compress;

import java.util.EventListener;

public interface CompressListener extends EventListener {
	void compressing(CompressEvent e);
}
