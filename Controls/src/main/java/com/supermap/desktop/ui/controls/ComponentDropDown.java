package com.supermap.desktop.ui.controls;

import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.implement.InnerMetaComboBoxIcon;
import com.supermap.desktop.implement.SmButtonDropdown;
import com.supermap.desktop.implement.SmPopupMenu;
import com.supermap.desktop.ui.XMLCommand;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Created by xie on 2017/4/18.
 */
public class ComponentDropDown extends SmButtonDropdown {
    private int popupType;
    public final static int COLOR_TYPE = 0;
    public final static int TEXT_TYPE = 1;
    public final static int IMAGE_TYPE = 2;
    private JPanel panelColorDisplay;
    private Color color;
    private transient ColorSelectionPanel colorSelectionPanel;
    public static String CHANGECOLOR = "changeColor";

    private ActionListener popuListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

            switch (popupType) {
                case COLOR_TYPE:
                    popupMenu = new SmPopupMenu();
                    popupMenu.setBorderPainted(false);
                    popupMenu.add(colorSelectionPanel, BorderLayout.CENTER);
                    colorSelectionPanel.setPreferredSize(new Dimension(170, 205));
                    popupMenu.show(ComponentDropDown.this, 0, getHeight());
                    colorSelectionPanel.addPropertyChangeListener("m_selectionColor", new PropertyChangeListener() {
                        @Override
                        public void propertyChange(PropertyChangeEvent evt) {
                            color = (Color) evt.getNewValue();
                            selectColor(color);
                            panelColorDisplay.setBackground(color);
                            panelColorDisplay.repaint();
                            popupMenu.setVisible(false);
                        }
                    });
                    break;
                case TEXT_TYPE:
                    //When TEXT_TYPE ComponentDropDown this is added to a button,so we need to get arrow button's parent's parent compnent
                    Component button = ((Component) e.getSource()).getParent().getParent();
                    popupMenu.show(button, 0, button.getHeight());
                    break;
                case IMAGE_TYPE:
                    Component dropDown = ((Component) e.getSource()).getParent();
                    popupMenu.show(dropDown, 0, dropDown.getHeight());
                    break;
                default:
                    break;
            }

        }
    };

    public ComponentDropDown(IForm formClass, XMLCommand xmlCommand, JComponent parent) {
        super(formClass, xmlCommand, parent);
    }

    public ComponentDropDown(int popupType) {
        super(popupType);
        this.popupType = popupType;
        initComponents();
        initLayout();
        registEvents();
    }

    private void registEvents() {
        removeEvents();
        if (popupType == COLOR_TYPE || popupType == TEXT_TYPE) {
            this.displayButton.addActionListener(this.popuListener);
            this.arrowButton.addActionListener(this.popuListener);
        } else {
            this.arrowButton.addActionListener(this.popuListener);
        }
    }

    private void removeEvents() {
        if (popupType == COLOR_TYPE || popupType == TEXT_TYPE) {
            this.displayButton.removeActionListener(this.popuListener);
            this.arrowButton.removeActionListener(this.popuListener);
        } else {
            this.arrowButton.removeActionListener(this.popuListener);
        }
    }

    private void initLayout() {
        this.setLayout(new GridBagLayout());
        this.add(displayButton, new GridBagConstraintsHelper(0, 0, 1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.WEST).setWeight(1, 1));
        this.add(arrowButton, new GridBagConstraintsHelper(1, 0, 1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER).setWeight(0, 1).setIpad(4, 0));
    }


    private void initComponents() {
        this.displayButton = new com.supermap.desktop.implement.ControlButton(this);
        this.displayButton.setBorder(null);
        this.displayButton.setBorderPainted(false);
        this.arrowButton = new com.supermap.desktop.implement.ControlButton(this);
        Insets insets = arrowButton.getMargin();
        this.arrowButton.setMargin(new Insets(insets.top, 1, insets.bottom, 1));
        this.arrowButton.setForeground(Color.lightGray);

        this.arrowButton.setIcon(new InnerMetaComboBoxIcon());
        switch (popupType) {
            case COLOR_TYPE:
                this.panelColorDisplay = new JPanel();
                this.displayButton.setLayout(new GridBagLayout());
                this.displayButton.add(this.panelColorDisplay, new GridBagConstraintsHelper(0, 0, 1, 1).setFill(GridBagConstraints.BOTH).setWeight(1, 1).setInsets(3));
                this.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.gray));
                this.arrowButton.setPreferredSize(new Dimension(15, 18));
                this.colorSelectionPanel = new ColorSelectionPanel();
                break;
            case TEXT_TYPE:
                this.setBorder(null);
                break;
            default:
                break;
        }

    }

    public Color getColor() {
        return color;
    }

    public void selectColor(Color newColor) {
        // 为了每次点击颜色按钮都触发事件，所以传入新颜色和null
        firePropertyChange(CHANGECOLOR, null, newColor);
    }

    public void setColor(Color color) {
        this.color = color;
        this.panelColorDisplay.setBackground(color);
        this.panelColorDisplay.repaint();
    }

    public void setIcon(Icon icon) {
        this.displayButton.setIcon(icon);
        this.displayButton.repaint();
    }

    public void setToolTip(String toolTip) {
        this.displayButton.setToolTipText(toolTip);
        this.arrowButton.setToolTipText(toolTip);
    }

    public void setText(String text) {
        this.displayButton.setText(text);
    }

    public class RightArrowButton extends JButton {
        public RightArrowButton(String text) {
            initRightArrowButton(text);
        }

        private void initRightArrowButton(String text) {
            JLabel labelText = new JLabel(text);
            JLabel labelImage = new JLabel(new InnerMetaComboBoxIcon());
            JPanel panel = new JPanel();
            this.setLayout(new GridBagLayout());
            panel.setLayout(new GridBagLayout());
            this.add(labelText, new GridBagConstraintsHelper(0, 0, 1, 1).setFill(GridBagConstraints.BOTH).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setInsets(1, 10, 1, 2).setIpad(0, 5));
            this.add(labelImage, new GridBagConstraintsHelper(1, 0, 1, 1).setFill(GridBagConstraints.BOTH).setWeight(0, 0).setAnchor(GridBagConstraints.WEST).setInsets(5).setIpad(0, 5));
            this.add(panel, new GridBagConstraintsHelper(0, 0, 1, 1).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 1).setAnchor(GridBagConstraints.WEST));
            this.setBorder(BorderFactory.createDashedBorder(this.getBackground()));
        }
    }
}
