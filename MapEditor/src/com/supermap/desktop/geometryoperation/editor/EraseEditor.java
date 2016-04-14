package com.supermap.desktop.geometryoperation.editor;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPopupMenu;

import com.supermap.desktop.geometry.Abstract.ICompoundFeature;
import com.supermap.desktop.geometry.Abstract.IRegionFeature;
import com.supermap.desktop.geometryoperation.EditEnvironment;
import com.supermap.desktop.utilties.ListUtilties;
import com.supermap.ui.MapControl;

public class EraseEditor extends AbstractEditor {


	private MouseListener mouseListener = new MouseAdapter() {

		@Override
		public void mouseEntered(MouseEvent e) {
			EraseEditor.this.mouseEntered();
		}

		@Override
		public void mouseExited(MouseEvent e) {
			EraseEditor.this.mouseExited();
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			EraseEditor.this.mouseClicked();
		}
	};

	private KeyListener keyListener = new KeyAdapter() {

		@Override
		public void keyPressed(KeyEvent e) {
			EraseEditor.this.keyPressed();
		}
	};

	@Override
	public void activate(EditEnvironment environment) {
		registerEvents(environment);
	}

	@Override
	public void deactivate(EditEnvironment environment) {
		unregisterEvents(environment);
	}

	@Override
	public boolean enble(EditEnvironment environment) {
		return environment.getEditProperties().getEditableSelectedGeometryCount() > 0
				&& ListUtilties.isListContainAny(environment.getEditProperties().getSelectedGeometryFeatures(), IRegionFeature.class, ICompoundFeature.class);
	}

	@Override
	public boolean check(EditEnvironment environment) {
		return environment.getEditor() instanceof EraseEditor;
	}

	private void registerEvents(EditEnvironment environment) {
		MapControl mapControl = environment.getMapControl();
		mapControl.addMouseListener(this.mouseListener);
		mapControl.addKeyListener(this.keyListener);
	}

	private void unregisterEvents(EditEnvironment environment) {
		MapControl mapControl = environment.getMapControl();
		mapControl.removeMouseListener(this.mouseListener);
		mapControl.removeKeyListener(this.keyListener);
	}

	private void initialTooltip() {

	}

	private void mouseClicked() {

	}

	private void mouseEntered() {

	}

	private void mouseExited() {

	}

	private void keyPressed() {

	}
}
