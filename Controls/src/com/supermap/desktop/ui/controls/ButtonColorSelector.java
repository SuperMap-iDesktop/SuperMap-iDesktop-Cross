package com.supermap.desktop.ui.controls;

import com.supermap.desktop.controls.ControlDefaultValues;
import com.supermap.desktop.ui.controls.button.SmButton;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.plaf.metal.MetalComboBoxIcon;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ButtonColorSelector extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String PROPERTY_COLOR = "Color";

	private static final int DEFAULT_ARROW_WIDTH = 16;

	private transient ColorSelectionPanel colorSelectionPanel;
	private SmButton buttonColorDisplay;
	private transient ControlButton buttonArrow;
	private JPopupMenu popupMenu;

	private Color color;
	private transient ColorSwatch colorSwatch;

	private transient PropertyChangeListener colorSelectionPanelPropertyChangeListener = new PropertyChangeListener() {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			popupMenu.setVisible(false);
			Color newColor = (Color) evt.getNewValue();
			ColorSelectionPropertyChange(newColor);
		}
	};
	private transient ComponentListener buttonComponentListener = new ComponentAdapter() {
		@Override
		public void componentResized(ComponentEvent e) {
			componentResize();
		}
	};

	public ButtonColorSelector() {
		this.color = Color.WHITE;
		initializeComponents();
		registerEvents();
		this.colorSelectionPanel.selectColor(this.color);
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.colorSelectionPanel.selectColor(color);
	}

	private void initializeComponents() {
		this.colorSelectionPanel = new ColorSelectionPanel();
		this.buttonColorDisplay = new SmButton();
		this.colorSwatch = new ColorSwatch(this.color, 16, 20);
		this.buttonColorDisplay.setIcon(this.colorSwatch);
		this.buttonColorDisplay.setPreferredSize(ControlDefaultValues.DEFAULT_PREFERREDSIZE);

		this.buttonArrow = new ControlButton();
		this.buttonArrow.setIcon(new MetalComboBoxIcon());
		Insets insets = this.buttonArrow.getMargin();
		this.buttonArrow.setMargin(new Insets(insets.top, 1, insets.bottom, 1));

		this.popupMenu = new JPopupMenu();
		this.popupMenu.setBorderPainted(false);
		this.popupMenu.add(this.colorSelectionPanel, BorderLayout.CENTER);
		this.colorSelectionPanel.setPreferredSize(new Dimension(170, 205));

		this.buttonArrow.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				popupMenu.show(buttonArrow, 0, buttonColorDisplay.getHeight());
			}

		});
		this.buttonArrow.setComponentPopupMenu(this.popupMenu);

		this.buttonColorDisplay.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				popupMenu.show(buttonColorDisplay, 0, buttonColorDisplay.getHeight());
			}

		});
		this.buttonColorDisplay.setComponentPopupMenu(this.popupMenu);

		// @formatter:off
		GroupLayout groupLayout = new GroupLayout(this);
		setLayout(groupLayout);

		groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
				.addComponent(this.buttonColorDisplay, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addComponent(this.buttonArrow, DEFAULT_ARROW_WIDTH, DEFAULT_ARROW_WIDTH, DEFAULT_ARROW_WIDTH));

		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.CENTER)
				.addComponent(this.buttonColorDisplay, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addComponent(this.buttonArrow, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
		// @formatter:on
	}

	private void registerEvents() {
		this.colorSelectionPanel.addPropertyChangeListener("m_selectionColor", this.colorSelectionPanelPropertyChangeListener);
		this.buttonColorDisplay.addComponentListener(this.buttonComponentListener);
	}

	private void ColorSelectionPropertyChange(Color newColor) {
		if (this.color != newColor) {
			this.color = newColor;
			repaintButtonColorDisplay();
			firePropertyChange(PROPERTY_COLOR, null, this.color);
		}
	}

	private void componentResize() {
		repaintButtonColorDisplay();
	}

	private void repaintButtonColorDisplay() {
		this.colorSwatch.setColor(color);
		Dimension size = this.buttonColorDisplay.getSize();
		Insets insets = this.buttonColorDisplay.getInsets();
		size.width -= insets.left + insets.right;
		size.height -= insets.top + insets.bottom;
		this.colorSwatch.setIconHeight(size.height);
		this.colorSwatch.setIconWidth(size.width);
		this.buttonColorDisplay.repaint();
	}

	@Override
	public void setEnabled(boolean enabled) {
		this.buttonColorDisplay.setEnabled(enabled);
		this.buttonArrow.setEnabled(enabled);
		super.setEnabled(enabled);
	}
}
