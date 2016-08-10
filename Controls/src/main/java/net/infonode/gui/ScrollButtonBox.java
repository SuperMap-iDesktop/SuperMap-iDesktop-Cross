/*
 * Copyright (C) 2004 NNL Technology AB
 * Visit www.infonode.net for information about InfoNode(R) 
 * products and how to contact NNL Technology AB.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, 
 * MA 02111-1307, USA.
 */

// $Id: ScrollButtonBox.java,v 1.17 2005/12/04 13:46:04 jesper Exp $
package net.infonode.gui;

import net.infonode.gui.icon.button.ArrowIcon;
import net.infonode.gui.layout.DirectionLayout;
import net.infonode.gui.panel.SimplePanel;
import net.infonode.util.Direction;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;

public class ScrollButtonBox extends SimplePanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private AbstractButton upButton;
	private AbstractButton downButton;
	private AbstractButton leftButton;
	private AbstractButton rightButton;

	private boolean button1Enabled;
	private boolean button2Enabled;

	private boolean vertical;

	private ArrayList listeners;

	private transient ActionListener button1Listener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			fireButton1();
		}
	};

	private transient ActionListener button2Listener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			fireButton2();
		}
	};

	public ScrollButtonBox(boolean vertical, int iconSize) {
		this(vertical, ButtonFactory.createFlatHighlightButton(new ArrowIcon(iconSize, Direction.UP), "", 0, null), ButtonFactory.createFlatHighlightButton(
				new ArrowIcon(iconSize, Direction.DOWN), "", 0, null), ButtonFactory.createFlatHighlightButton(new ArrowIcon(iconSize, Direction.LEFT), "", 0,
				null), ButtonFactory.createFlatHighlightButton(new ArrowIcon(iconSize, Direction.RIGHT), "", 0, null));
	}

	public ScrollButtonBox(final boolean vertical, AbstractButton upButton, AbstractButton downButton, AbstractButton leftButton, AbstractButton rightButton) {
		this.vertical = vertical;
		setLayout(new DirectionLayout(vertical ? Direction.DOWN : Direction.RIGHT));

		addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				if (e.getWheelRotation() < 0)
					fireButton1();
				else
					fireButton2();
			}
		});

		setButtons(upButton, downButton, leftButton, rightButton);
	}

	public void setButton1Enabled(boolean enabled) {
		this.button1Enabled = enabled;
		if (getComponentCount() > 0)
			((AbstractButton) getComponent(0)).setEnabled(enabled);
	}

	public void setButton2Enabled(boolean enabled) {
		this.button2Enabled = enabled;
		if (getComponentCount() > 0)
			((AbstractButton) getComponent(1)).setEnabled(enabled);
	}

	public boolean isButton1Enabled() {
		return button1Enabled;
	}

	public boolean isButton2Enabled() {
		return button2Enabled;
	}

	public void addListener(ScrollButtonBoxListener listener) {
		if (listeners == null)
			listeners = new ArrayList(2);

		listeners.add(listener);
	}

	public void removeListener(ScrollButtonBoxListener listener) {
		if (listeners != null) {
			listeners.remove(listener);

			if (listeners.isEmpty())
				listeners = null;
		}
	}

	public boolean isVertical() {
		return vertical;
	}

	public void setVertical(boolean vertical) {
		if (vertical != this.vertical) {
			this.vertical = vertical;
			initialize();
		}
	}

	public void setButtons(AbstractButton upButton, AbstractButton downButton, AbstractButton leftButton, AbstractButton rightButton) {
		if (upButton != this.upButton || downButton != this.downButton || leftButton != this.leftButton || rightButton != this.rightButton) {
			this.upButton = upButton;
			this.downButton = downButton;
			this.leftButton = leftButton;
			this.rightButton = rightButton;

			initialize();
		}
	}

	public AbstractButton getUpButton() {
		return upButton;
	}

	public AbstractButton getDownButton() {
		return downButton;
	}

	public AbstractButton getLeftButton() {
		return leftButton;
	}

	public AbstractButton getRightButton() {
		return rightButton;
	}

	private void fireButton1() {
		if (listeners != null) {
			Object[] l = listeners.toArray();

			for (int i = 0; i < l.length; i++)
				((ScrollButtonBoxListener) l[i]).scrollButton1();
		}
	}

	private void fireButton2() {
		if (listeners != null) {
			Object[] l = listeners.toArray();

			for (int i = 0; i < l.length; i++)
				((ScrollButtonBoxListener) l[i]).scrollButton2();
		}
	}

	private void initialize() {
		if (getComponentCount() > 0) {
			((AbstractButton) getComponent(0)).removeActionListener(button1Listener);
			((AbstractButton) getComponent(1)).removeActionListener(button2Listener);
			removeAll();
		}

		((DirectionLayout) getLayout()).setDirection(vertical ? Direction.DOWN : Direction.RIGHT);

		AbstractButton button1;
		AbstractButton button2;

		if (vertical) {
			button1 = upButton;
			button2 = downButton;
		} else {
			button1 = leftButton;
			button2 = rightButton;
		}

		if (button1 != null && button2 != null) {
			add(button1);
			add(button2);

			button1.setFocusable(false);
			button2.setFocusable(false);

			button1.setEnabled(button1Enabled);
			button2.setEnabled(button2Enabled);

			button1.addActionListener(button1Listener);
			button2.addActionListener(button2Listener);
		}

		if (getParent() != null)
			ComponentUtil.validate(getParent());

	}

}
