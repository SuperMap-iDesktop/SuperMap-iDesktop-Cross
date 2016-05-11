package com.supermap.desktop.geometryoperation;

import java.awt.event.KeyEvent;
import java.util.EventObject;

import com.supermap.desktop.geometryoperation.editor.EraseEditor;
import com.supermap.desktop.utilties.MapControlUtilties;
import com.supermap.ui.GeometrySelectChangedEvent;
import com.supermap.ui.GeometrySelectedEvent;
import com.supermap.ui.MapControl;

/**
 * 用来解决单例多窗口事件响应的相关问题
 * 
 * @author highsad
 *
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

	public void geometrySelected(EditEnvironment environment, GeometrySelectedEvent arg0);

	public void geometrySelectChanged(EditEnvironment environment, GeometrySelectChangedEvent arg0);

	public void undone(EditEnvironment environment, EventObject arg0);

	public void redone(EditEnvironment environment, EventObject arg0);
}
