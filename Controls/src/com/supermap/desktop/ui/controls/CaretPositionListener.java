package com.supermap.desktop.ui.controls;

import java.awt.event.*;

import javax.swing.*;

import com.supermap.desktop.Application;

/**
 * This class will retain the caret positioning at the character where the mouse was clicked.
 */
public class CaretPositionListener implements MouseListener {
	private boolean dynamicFormatting;

	/**
	 * Default constructor.
	 */
	public CaretPositionListener() {
		// 默认实现，后续进行初始化设置
	}

	/**
	 * Convenience constructor. This class is automatically added as a MouseListener to the specified formatted text fields.
	 */
	public CaretPositionListener(JFormattedTextField... components) {
		registerComponent(components);
	}

	/**
	 *
	 */
	public boolean isDynamicFormatting() {
		return dynamicFormatting;
	}

	/**
	 * Indicates that the formatting of the text in the formatted text field can change depending on whether the text field has focus or not. The listner must
	 * be aware of this so the proper caret position can be calculated.
	 *
	 * @param dynamicFormatting when true dynamic formatting must be considered
	 */
	public void setDynamicFormatting(boolean dynamicFormatting) {
		this.dynamicFormatting = dynamicFormatting;
	}

	/**
	 * Remove listeners from the specified component
	 *
	 * @param component the component the listeners are removed from
	 */
	public void deregisterComponent(JFormattedTextField... components) {
		for (JFormattedTextField component : components)
			component.removeMouseListener(this);
	}

	/**
	 * Add the required listeners to the specified component
	 *
	 * @param component the component the listeners are added to
	 */
	public void registerComponent(JFormattedTextField... components) {
		for (JFormattedTextField component : components)
			component.addMouseListener(this);
	}

	@Override
	public void mousePressed(final MouseEvent me) {
		// Ignore double click to allow default behavour which will
		// select the entire text

		if (me.getClickCount() > 1)
			return;

		final JFormattedTextField ftf = (JFormattedTextField) me.getSource();

		if (dynamicFormatting && ftf.getValue() != null) {
			determineCaretPosition(ftf);
		} else {
			int offset = ftf.getCaretPosition();
			setCaretPosition(ftf, offset);
		}
	}

	private void determineCaretPosition(final JFormattedTextField ftf) {
		int offset = ftf.getCaretPosition();
		String text = ftf.getText();
		String value = ftf.getValue().toString();

		if (text.equals(value)) {
			setCaretPosition(ftf, offset);
			return;
		}

		int i = 0;
		int j = 0;

		// Exclude formatting characters

		while (j < offset) {
			if (text.charAt(i) == value.charAt(j)) {
				i++;
				j++;
			} else {
				offset--;
				i++;
			}
		}

		setCaretPosition(ftf, offset);
	}

	private void setCaretPosition(final JFormattedTextField ftf, final int offset) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					ftf.setCaretPosition(offset);
				} catch (IllegalArgumentException e) {
					Application.getActiveApplication().getOutput().output(e);
				}
			}
		});
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// do nothing
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// do nothing
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// do nothing
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// do nothing
	}
}