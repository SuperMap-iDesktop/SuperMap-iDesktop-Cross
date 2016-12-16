package com.supermap.desktop.geometryoperation;

import com.supermap.desktop.event.ActiveFormChangedEvent;
import com.supermap.desktop.event.DockbarClosedEvent;
import com.supermap.ui.ActionChangedEvent;
import com.supermap.ui.GeometryEvent;
import com.supermap.ui.GeometrySelectChangedEvent;
import com.supermap.ui.GeometrySelectedEvent;
import com.supermap.ui.TrackedEvent;
import com.supermap.ui.TrackingEvent;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.EventObject;

/**
 * 用来解决单例多窗口事件响应的相关问题
 *
 * @author highsad
 */
public interface IEditController {

	// @formatter:off
    /**
     * Invoked when a key has been typed.
     * See the class description for {@link KeyEvent} for a definition of
     * a key typed event.
     */
	// @formatter:on
    public void keyTyped(EditEnvironment environment, KeyEvent e);

	// @formatter:off
    /**
     * Invoked when a key has been pressed.
     * See the class description for {@link KeyEvent} for a definition of
     * a key pressed event.
     */
    // @formatter:on
    public void keyPressed(EditEnvironment environment, KeyEvent e);

	// @formatter:off
    /**
     * Invoked when a key has been released.
     * See the class description for {@link KeyEvent} for a definition of
     * a key released event.
     */
    // @formatter:on
    public void keyReleased(EditEnvironment environment, KeyEvent e);

	/**
	 * Invoked when the mouse button has been clicked (pressed and released) on a component.
	 */
	public void mouseClicked(EditEnvironment environment, MouseEvent e);

	/**
	 * Invoked when a mouse button has been pressed on a component.
	 */
	public void mousePressed(EditEnvironment environment, MouseEvent e);

	/**
	 * Invoked when a mouse button has been released on a component.
	 */
	public void mouseReleased(EditEnvironment environment, MouseEvent e);

	/**
	 * Invoked when the mouse enters a component.
	 */
	public void mouseEntered(EditEnvironment environment, MouseEvent e);

	/**
	 * Invoked when the mouse exits a component.
	 */
	public void mouseExited(EditEnvironment environment, MouseEvent e);

	/**
	 * Invoked when the mouse cursor has been moved onto a component but no buttons have been pushed.
	 */
	public void mouseMoved(EditEnvironment environment, MouseEvent e);

	public void actionChanged(EditEnvironment environment, ActionChangedEvent e);

	public void geometrySelected(EditEnvironment environment, GeometrySelectedEvent arg0);

	public void geometrySelectChanged(EditEnvironment environment, GeometrySelectChangedEvent arg0);

	/**
	 * @author lixiaoyao
	 * @description 新增对选中的对象进行增删移动时，进行监听
	 */
	public void geometryModified(EditEnvironment environment, GeometryEvent arg0);

	public void undone(EditEnvironment environment, EventObject arg0);

	public void redone(EditEnvironment environment, EventObject arg0);

	public void tracking(EditEnvironment environment, TrackingEvent e);

	public void tracked(EditEnvironment environment, TrackedEvent e);

	public void dockbarClosed(EditEnvironment editEnvironment, DockbarClosedEvent e);
}
