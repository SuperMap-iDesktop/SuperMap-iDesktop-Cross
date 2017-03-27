package com.supermap.desktop.process.graphics.interaction.canvas;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;

/**
 * Created by highsad on 2017/3/2.
 */
public interface CanvasEventHandler extends MouseListener, MouseMotionListener, MouseWheelListener, KeyListener {
	boolean isEnabled();

	void setEnabled(boolean enabled);

	void clean();
}
