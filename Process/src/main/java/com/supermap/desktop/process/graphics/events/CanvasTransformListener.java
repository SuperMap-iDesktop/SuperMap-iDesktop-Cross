package com.supermap.desktop.process.graphics.events;

import java.util.EventListener;

/**
 * Created by highsad on 2017/3/16.
 */
public interface CanvasTransformListener extends EventListener {
	void canvasTransform(CanvasTransformEvent e);
}
