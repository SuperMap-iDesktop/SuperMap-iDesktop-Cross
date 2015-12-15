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

// $Id: ButtonFactory.java,v 1.23 2005/12/04 13:46:04 jesper Exp $
package net.infonode.gui;

import net.infonode.gui.border.HighlightBorder;
import net.infonode.util.ColorUtil;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ButtonUI;

import java.awt.*;
import java.awt.event.*;
import java.net.URL;

public class ButtonFactory {
	private static ButtonHighlighter buttonHighlighter;

	private ButtonFactory() {
	}

	private static class ButtonHighlighter implements ComponentListener, HierarchyListener {
		private JButton button;
		private Border pressedBorder;
		private Border highlightedBorder;
		private Border normalBorder;
		private boolean rollover;
		private long rolloverStart;

		ButtonHighlighter(JButton button, int padding) {
			this.button = button;

			normalBorder = new EmptyBorder(padding + 2, padding + 2, padding + 2, padding + 2);
			pressedBorder = new EmptyBorder(padding + 2, padding + 2, padding, padding);
			highlightedBorder = new EmptyBorder(padding + 1, padding + 1, padding + 1, padding + 1);

			button.setContentAreaFilled(false);
			setNormalState();

			button.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					rollover = (System.currentTimeMillis() - rolloverStart) > 20 && ButtonHighlighter.this.button.getModel().isRollover();
					update();

					if (ButtonHighlighter.this.button.getModel().isRollover())
						rolloverStart = 0;
				}
			});

			button.addHierarchyListener(this);
			button.addComponentListener(this);
		}

		private void setNormalState() {
			button.setBackground(null);
			button.setOpaque(false);
			button.setBorder(normalBorder);
			rollover = false;
		}

		@Override
		public void componentHidden(ComponentEvent e) {
			setNormalState();
			rolloverStart = System.currentTimeMillis();
		}

		@Override
		public void componentMoved(ComponentEvent e) {
			setNormalState();
			rolloverStart = System.currentTimeMillis();
		}

		@Override
		public void componentResized(ComponentEvent e) {
			setNormalState();
			rolloverStart = System.currentTimeMillis();
		}

		@Override
		public void componentShown(ComponentEvent e) {
			setNormalState();
			rolloverStart = System.currentTimeMillis();
		}

		@Override
		public void hierarchyChanged(HierarchyEvent e) {
			setNormalState();
			rolloverStart = System.currentTimeMillis();
		}

		private void update() {
			boolean pressed = button.getModel().isArmed();

			if (button.isEnabled() && (rollover || pressed)) {
				button.setOpaque(true);
				Color backgroundColor = ComponentUtil.getBackgroundColor(button.getParent());
				backgroundColor = backgroundColor == null ? UIManagerUtil.getColor("control", Color.LIGHT_GRAY) : backgroundColor;
				button.setBackground(ColorUtil.mult(backgroundColor, pressed ? 0.8 : 1.15));

				button.setBorder(pressed ? new CompoundBorder(new LineBorder(ColorUtil.mult(backgroundColor, 0.3)), pressedBorder) : new CompoundBorder(
						new LineBorder(ColorUtil.mult(backgroundColor, 0.5)), highlightedBorder));
			} else {
				setNormalState();
			}
		}

	}

	private static final Border normalBorder = new CompoundBorder(new LineBorder(new Color(70, 70, 70)), new CompoundBorder(new HighlightBorder(),
			new EmptyBorder(1, 6, 1, 6)));
	private static final Border pressedBorder = new CompoundBorder(new LineBorder(new Color(70, 70, 70)), new CompoundBorder(new HighlightBorder(true),
			new EmptyBorder(2, 7, 0, 5)));

	private static JButton initButton(final JButton button) {
		button.setMargin(null);
		button.setBorder(normalBorder);
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				button.setBorder(pressedBorder);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				button.setBorder(normalBorder);
			}
		});

		return button;
	}

	private static JButton newButton(String text) {
		return initButton(new JButton(text));
	}

	private static JButton newButton(Icon icon) {
		return initButton(new JButton(icon));
	}

	private static JButton newButton(Icon icon, String text) {
		return initButton(new JButton(text, icon));
	}

	public static final JButton createDialogButton(String text, ActionListener action) {
		JButton button = new JButton(text);
		button.setFont(button.getFont().deriveFont(Font.BOLD));
		button.addActionListener(action);
		return button;
	}

	public static final JButton createButton(String text, ActionListener action) {
		return createButton(text, true, action);
	}

	public static final JButton createButton(String text, boolean opaque, ActionListener action) {
		JButton button = newButton(text);
		button.setOpaque(opaque);
		button.addActionListener(action);
		return button;
	}

	public static final JButton createButton(String iconResource, String text, ActionListener action) {
		URL iconURL = ButtonFactory.class.getClassLoader().getResource(iconResource);
		return createButton(iconURL == null ? null : new ImageIcon(iconURL), text, action);
	}

	public static final JButton createButton(Icon icon, String text, ActionListener action) {
		JButton button;

		if (icon != null) {
			button = newButton(icon);
			button.setToolTipText(text);
		} else {
			button = newButton(text);
		}

		button.addActionListener(action);
		return button;
	}

	public static final JButton createButton(Icon icon, String tooltipText, boolean opaque, ActionListener action) {
		JButton button = newButton(icon);
		button.setToolTipText(tooltipText);
		button.addActionListener(action);
		button.setOpaque(opaque);
		return button;
	}

	public static final JButton createFlatHighlightButton(Icon icon, String tooltipText, int padding, ActionListener action) {
		final JButton button = new JButton(icon) {
			@Override
			public void setUI(ButtonUI ui) {
				super.setUI(new FlatIconButtonUI());
			}
		};
		button.setVerticalAlignment(SwingConstants.CENTER);
		button.setToolTipText(tooltipText);
		button.setMargin(new Insets(0, 0, 0, 0));
		setButtonHighlighter(new ButtonHighlighter(button, padding));
		button.setRolloverEnabled(true);

		if (action != null)
			button.addActionListener(action);

		return button;
	}

	public static final void applyButtonHighlighter(JButton button, int padding) {
		button.setVerticalAlignment(SwingConstants.CENTER);
		button.setMargin(new Insets(0, 0, 0, 0));
		button.setRolloverEnabled(true);
	}

	public static final JButton createFlatHighlightButton(Icon icon, String tooltipText, int padding, boolean focusable, ActionListener action) {
		final JButton button = createFlatHighlightButton(icon, tooltipText, padding, action);
		button.setFocusable(focusable);
		return button;
	}

	public static final JButton createHighlightButton(String text, ActionListener action) {
		JButton button = newButton(text);
		button.addActionListener(action);
		return button;
	}

	public static final JButton createHighlightButton(Icon icon, ActionListener action) {
		JButton button = newButton(icon);
		button.addActionListener(action);
		return button;
	}

	public static final JButton createHighlightButton(Icon icon, String text, ActionListener action) {
		JButton button = newButton(icon, text);
		button.addActionListener(action);
		return button;
	}

	public static final JButton createFlatIconHoverButton(Icon icon, Icon hovered, Icon pressed) {
		final JButton button = new JButton(icon) {
			@Override
			public void setUI(ButtonUI ui) {
				super.setUI(new FlatIconButtonUI());
			}
		};
		button.setPressedIcon(pressed);
		button.setRolloverEnabled(true);
		button.setRolloverIcon(hovered);
		button.setVerticalAlignment(SwingConstants.CENTER);
		return button;
	}

	public static ButtonHighlighter getButtonHighlighter() {
		return buttonHighlighter;
	}

	public static void setButtonHighlighter(ButtonHighlighter buttonHighlighter) {
		ButtonFactory.buttonHighlighter = buttonHighlighter;
	}
}
