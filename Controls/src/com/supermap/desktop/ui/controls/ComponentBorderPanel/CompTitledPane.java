package com.supermap.desktop.ui.controls.ComponentBorderPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;

/**
 * 
 * @author xie
 *
 */
public class CompTitledPane extends JPanel {

	private static final long serialVersionUID = 1L;
	protected CompTitledBorder border;
	protected JComponent component;
	protected JPanel panel;

	public CompTitledPane(JComponent component, JPanel panel) {
		this.component = component;
		this.panel = panel;
		initComponents();
	}

	private void initComponents() {
		this.border = new CompTitledBorder(component);
		setBorder(this.border);
		add(this.component, BorderLayout.NORTH);
		add(this.panel, BorderLayout.CENTER);
	}

	public JComponent getTitleComponent() {
		return component;
	}

	public void setTitleComponent(JComponent newComponent) {
		remove(this.component);
		add(newComponent);
		this.border.setTitleComponent(newComponent);
		component = newComponent;
	}

	public JPanel getContentPane() {
		return this.panel;
	}

	public void doLayout() {
		Insets insets = getInsets();
		Rectangle rect = getBounds();
		rect.x = 0;
		rect.y = 0;

		Rectangle compR = border.getComponentRect(rect, insets);
		component.setBounds(compR);
		rect.x += insets.left;
		rect.y += insets.top;
		rect.width -= insets.left + insets.right;
		rect.height -= insets.top + insets.bottom;
		panel.setBounds(rect);
	}
}

/**
 * 自定义的TitledBorder，可以在将标题栏存放控件
 * 
 * @author xie
 *
 */
class CompTitledBorder extends TitledBorder {

	private static final long serialVersionUID = 1L;
	protected JComponent component;

	public CompTitledBorder(JComponent component) {
		this(null, component, LEFT, TOP);
	}

	public CompTitledBorder(Border border) {
		this(border, null, LEFT, TOP);
	}

	public CompTitledBorder(Border border, JComponent component) {
		this(border, component, LEFT, TOP);
	}

	public CompTitledBorder(Border border, JComponent component, int titleJustification, int titlePosition) {
		super(border, null, titleJustification, titlePosition, null, null);
		this.component = component;
		if (border == null) {
			this.border = super.getBorder();
		}
	}

	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		Rectangle borderR = new Rectangle(x + EDGE_SPACING, y + EDGE_SPACING, width - (EDGE_SPACING * 2), height - (EDGE_SPACING * 2));
		Insets borderInsets;
		if (border != null) {
			borderInsets = border.getBorderInsets(c);
		} else {
			borderInsets = new Insets(0, 0, 0, 0);
		}

		Rectangle rect = new Rectangle(x, y, width, height);
		Insets insets = getBorderInsets(c);
		Rectangle compR = getComponentRect(rect, insets);
		int diff;
		switch (titlePosition) {
		case ABOVE_TOP:
			diff = compR.height + TEXT_SPACING;
			borderR.y += diff;
			borderR.height -= diff;
			break;
		case TOP:
		case DEFAULT_POSITION:
			diff = insets.top / 2 - borderInsets.top - EDGE_SPACING;
			borderR.y += diff;
			borderR.height -= diff;
			break;
		case BELOW_TOP:
		case ABOVE_BOTTOM:
			break;
		case BOTTOM:
			diff = insets.bottom / 2 - borderInsets.bottom - EDGE_SPACING;
			borderR.height -= diff;
			break;
		case BELOW_BOTTOM:
			diff = compR.height + TEXT_SPACING;
			borderR.height -= diff;
			break;
		}
		border.paintBorder(c, g, borderR.x, borderR.y, borderR.width, borderR.height);
		Color col = g.getColor();
		g.setColor(c.getBackground());
		g.fillRect(compR.x, compR.y, compR.width, compR.height);
		g.setColor(col);
		component.repaint();
	}

	public Insets getBorderInsets(Component c, Insets insets) {
		Insets borderInsets;
		if (border != null) {
			borderInsets = border.getBorderInsets(c);
		} else {
			borderInsets = new Insets(0, 0, 0, 0);
		}
		insets.top = EDGE_SPACING + TEXT_SPACING + borderInsets.top;
		insets.right = EDGE_SPACING + TEXT_SPACING + borderInsets.right;
		insets.bottom = EDGE_SPACING + TEXT_SPACING + borderInsets.bottom;
		insets.left = EDGE_SPACING + TEXT_SPACING + borderInsets.left;

		if (c == null || component == null) {
			return insets;
		}

		int compHeight = 0;
		if (component != null) {
			compHeight = component.getPreferredSize().height;
		}

		switch (titlePosition) {
		case ABOVE_TOP:
			insets.top += compHeight + TEXT_SPACING;
			break;
		case TOP:
		case DEFAULT_POSITION:
			insets.top += Math.max(compHeight, borderInsets.top) - borderInsets.top;
			break;
		case BELOW_TOP:
			insets.top += compHeight + TEXT_SPACING;
			break;
		case ABOVE_BOTTOM:
			insets.bottom += compHeight + TEXT_SPACING;
			break;
		case BOTTOM:
			insets.bottom += Math.max(compHeight, borderInsets.bottom) - borderInsets.bottom;
			break;
		case BELOW_BOTTOM:
			insets.bottom += compHeight + TEXT_SPACING;
			break;
		}
		return insets;
	}

	public JComponent getTitleComponent() {
		return component;
	}

	public void setTitleComponent(JComponent component) {
		this.component = component;
	}

	public Rectangle getComponentRect(Rectangle rect, Insets borderInsets) {
		Dimension compD = component.getPreferredSize();
		Rectangle compR = new Rectangle(0, 0, compD.width, compD.height);
		switch (titlePosition) {
		case ABOVE_TOP:
			compR.y = EDGE_SPACING;
			break;
		case TOP:
		case DEFAULT_POSITION:
			compR.y = EDGE_SPACING + (borderInsets.top - EDGE_SPACING - TEXT_SPACING - compD.height) / 2;
			break;
		case BELOW_TOP:
			compR.y = borderInsets.top - compD.height - TEXT_SPACING;
			break;
		case ABOVE_BOTTOM:
			compR.y = rect.height - borderInsets.bottom + TEXT_SPACING;
			break;
		case BOTTOM:
			compR.y = rect.height - borderInsets.bottom + TEXT_SPACING + (borderInsets.bottom - EDGE_SPACING - TEXT_SPACING - compD.height) / 2;
			break;
		case BELOW_BOTTOM:
			compR.y = rect.height - compD.height - EDGE_SPACING;
			break;
		}
		switch (titleJustification) {
		case LEFT:
		case DEFAULT_JUSTIFICATION:
			compR.x = TEXT_INSET_H + borderInsets.left;
			break;
		case RIGHT:
			compR.x = rect.width - borderInsets.right - TEXT_INSET_H - compR.width;
			break;
		case CENTER:
			compR.x = (rect.width - compR.width) / 2;
			break;
		}
		return compR;
	}

}
