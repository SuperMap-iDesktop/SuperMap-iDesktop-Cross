package com.supermap.desktop.geometryoperation;

import java.awt.event.KeyEvent;
import java.util.EventObject;

import com.supermap.ui.GeometrySelectChangedEvent;
import com.supermap.ui.GeometrySelectedEvent;

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

}
