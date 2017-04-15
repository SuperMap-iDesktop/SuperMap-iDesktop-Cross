package com.supermap.desktop.process.graphics.interaction.canvas;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;

/**
 * Created by highsad on 2017/3/2.
 * 1. 需要做优先级管理，比如对于 MouseMoved，谁先响应谁后响应。
 * 2. 需要做互斥管理，比如进行拖拽操作的时候，需要禁用选择操作。
 * 3. 当前互斥实现的方式存在一个主要的问题是，新增一种交互实现，无法很好的与已有交互实现进行互斥的管理，只能去更改已有交互的实现代码。
 */
public interface CanvasEventHandler extends MouseListener, MouseMotionListener, MouseWheelListener, KeyListener {
	boolean isEnabled();

	void setEnabled(boolean enabled);

	void clean();
}
