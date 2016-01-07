package com.supermap.desktop.implement;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.border.Border;

public class ControlButton extends JButton {

	private static final long serialVersionUID = 1L;

	private boolean isMouseIn;
	private JComponent parentComponent;
	private Color roverBorderColor = new Color(120, 174, 229);
	private transient Border emptyBorder = BorderFactory.createEmptyBorder(0, 0, 0, 0);
	private transient Border roverBorder = new Border() {
		@Override
		public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
			g.setColor(roverBorderColor);
			g.drawRect(x, y, width - 1, height - 1);
		}

		@Override
		public Insets getBorderInsets(Component c) {
			return new Insets(1, 1, 1, 1);
		}

		@Override
		public boolean isBorderOpaque() {
			return true;
		}
	};

	public ControlButton(JComponent parent) {
		super();
		initialize(parent);
	}

	public ControlButton(JComponent parent, String text) {
		super(text);
		initialize(parent);
	}

	public ControlButton(JComponent parent, String text, Icon icon) {
		super(text, icon);
		initialize(parent);
	}

	public ControlButton(JComponent parent, Icon icon) {
		super(icon);
		initialize(parent);
	}

	public boolean isMouseIn() {
		return this.isMouseIn;
	}

	public void setMouseIn(boolean isMouseIn) {
		this.isMouseIn = isMouseIn;
	}

	private void initialize(JComponent parent) {
		this.setBorder(emptyBorder);
		this.setOpaque(false);
		this.setBorder(emptyBorder);
		this.setContentAreaFilled(false);
		this.setFocusPainted(true);
		this.setRolloverEnabled(true);

		this.parentComponent = parent;

		this.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseExited(MouseEvent e) {
				setAbstractMouseExited();
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				setAbstractMouseEntered();
			}
		});
	}

	private void setAbstractMouseExited() {
		isMouseIn = false;
		if (parentComponent != null && parentComponent instanceof SmButtonDropdown) {
			if (!((SmButtonDropdown) parentComponent).getDisplayButton().equals(this)) {
				((SmButtonDropdown) parentComponent).getDisplayButton().setMouseIn(isMouseIn);
				((SmButtonDropdown) parentComponent).getDisplayButton().repaint();
			}

			if (!((SmButtonDropdown) parentComponent).getArrowButton().equals(this)) {
				((SmButtonDropdown) parentComponent).getArrowButton().setMouseIn(isMouseIn);
				((SmButtonDropdown) parentComponent).getArrowButton().repaint();
			}
		}
	}

	private void setAbstractMouseEntered() {
		isMouseIn = true;
		if (parentComponent != null && parentComponent instanceof SmButtonDropdown) {
			if (!((SmButtonDropdown) parentComponent).getDisplayButton().equals(this)) {
				((SmButtonDropdown) parentComponent).getDisplayButton().setMouseIn(isMouseIn);
				((SmButtonDropdown) parentComponent).getDisplayButton().repaint();
			}

			if (!((SmButtonDropdown) parentComponent).getArrowButton().equals(this)) {
				((SmButtonDropdown) parentComponent).getArrowButton().setMouseIn(isMouseIn);
				((SmButtonDropdown) parentComponent).getArrowButton().repaint();
			}
		}
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);

		if (this.isSelected() || isMouseIn) {
			g.setColor(new Color(120, 174, 229));
		} else {
			g.setColor(this.getBackground());
		}
		g.drawRect(1, 1, this.getWidth() - 2, this.getHeight() - 2);
	}

	public Border getRoverBorder() {
		return roverBorder;
	}

	public void setRoverBorder(Border roverBorder) {
		this.roverBorder = roverBorder;
	}
}
