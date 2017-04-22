package com.supermap.desktop.implement;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ControlButton extends JButton {

    private static final long serialVersionUID = 1L;

    private boolean isMouseIn;
    private JComponent parentComponent;
    private Color roverBorderColor = new Color(120, 174, 229);
    private transient Border emptyBorder = BorderFactory.createEmptyBorder(0, 0, 0, 0);

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
        if (!isEnabled())
            return;
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
        this.setFocusable(false);
    }

    private void setAbstractMouseEntered() {
        if (!isEnabled())
            return;
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
        this.setFocusable(true);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        if (this.isSelected() || isMouseIn) {
            g.setColor(Color.lightGray);
        } else {
            g.setColor(this.getBackground());
        }
        g.drawRoundRect(1, 1, this.getWidth() - 2, this.getHeight() - 2, 5, 5);
//		g.drawRect(1, 1, this.getWidth() - 2, this.getHeight() - 2);
    }

}
