package com.supermap.desktop.geometryoperation;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import com.supermap.ui.GeometrySelectChangedEvent;
import com.supermap.ui.GeometrySelectedEvent;
import com.supermap.ui.TrackedEvent;
import com.supermap.ui.TrackingEvent;

public abstract class EditControllerAdapter implements IEditController {

	@Override
	public void keyTyped(EditEnvironment environment, KeyEvent e) {

	}

	@Override
	public void keyPressed(EditEnvironment environment, KeyEvent e) {

	}

	@Override
	public void keyReleased(EditEnvironment environment, KeyEvent e) {

	}

	/**
	 * {@inheritDoc}
	 */
	public void mouseClicked(EditEnvironment environment, MouseEvent e) {
	}

	/**
	 * {@inheritDoc}
	 */
	public void mousePressed(EditEnvironment environment, MouseEvent e) {
	}

	/**
	 * {@inheritDoc}
	 */
	public void mouseReleased(EditEnvironment environment, MouseEvent e) {
	}

	/**
	 * {@inheritDoc}
	 */
	public void mouseEntered(EditEnvironment environment, MouseEvent e) {
	}

	/**
	 * {@inheritDoc}
	 */
	public void mouseExited(EditEnvironment environment, MouseEvent e) {
	}

	/**
	 * Invoked when the mouse cursor has been moved onto a component but no buttons have been pushed.
	 */
	public void mouseMoved(EditEnvironment environment, MouseEvent e) {
	}

	@Override
	public void geometrySelected(EditEnvironment environment, GeometrySelectedEvent arg0) {

	}

	@Override
	public void geometrySelectChanged(EditEnvironment environment, GeometrySelectChangedEvent arg0) {

	}

	@Override
	public void undone(EditEnvironment environment, EventObject arg0) {

	}

	@Override
	public void redone(EditEnvironment environment, EventObject arg0) {

	}

	@Override
	public void tracking(EditEnvironment environment, TrackingEvent e) {

	}

	@Override
	public void tracked(EditEnvironment environment, TrackedEvent e) {

	}
}
