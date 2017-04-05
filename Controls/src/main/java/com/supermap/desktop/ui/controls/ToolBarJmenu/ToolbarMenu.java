package com.supermap.desktop.ui.controls.ToolBarJmenu;

import javax.swing.*;
import javax.swing.plaf.metal.MetalComboBoxIcon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by lixiaoyao on 2017/3/29.
 */
public class ToolbarMenu extends JPanel {
    private JLabel labelShowFunction;
    private JLabel labelArraw;
    private JPopupMenu jPopupMenu;
    private ToolbarJmenuListener listeners;
    private int initWidth = this.getWidth();
    private int initHeight = this.getHeight();


    public ToolbarMenu(Icon showFunctionIcon, String showFunctionTip, String arrawTip) {
        this.labelShowFunction = new JLabel(showFunctionIcon);
        this.labelShowFunction.setToolTipText(showFunctionTip);
        this.labelArraw = new JLabel(new MetalComboBoxIcon());
        this.labelArraw.setToolTipText(arrawTip);
        this.jPopupMenu = new JPopupMenu();
        this.setBorder(BorderFactory.createEmptyBorder(initWidth + 2, initHeight + 2, initWidth + 2, initHeight + 2));
        initLayout();
        removeEvents();
        registEvents();
    }

    public ToolbarMenu(String showFunctionText){
        this.labelShowFunction = new JLabel();
        this.labelShowFunction.setText(showFunctionText);
        this.labelArraw = new JLabel(new MetalComboBoxIcon());
        this.jPopupMenu = new JPopupMenu();
        this.setBorder(BorderFactory.createEmptyBorder(initWidth + 2, initHeight + 2, initWidth + 2, initHeight + 2));
        initLayout();
        removeEvents();
        registEvents();
    }

    private void initLayout() {
        GroupLayout groupLayout = new GroupLayout(this);
        groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
                .addGap(3)
                .addComponent(this.labelShowFunction)
                .addGap(5)
                .addComponent(this.labelArraw)
                .addContainerGap(1, 1)
        );
        groupLayout.setVerticalGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addComponent(this.labelShowFunction)
                .addComponent(this.labelArraw)
        );
        this.setLayout(groupLayout);
    }

    private void registEvents() {
        this.labelArraw.addMouseListener(this.mouseListener);
        this.labelShowFunction.addMouseListener(this.mouseListener);
    }

    private void removeEvents() {
        this.labelArraw.removeMouseListener(this.mouseListener);
        this.labelShowFunction.removeMouseListener(this.mouseListener);
    }

    private MouseListener mouseListener = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getSource() == labelArraw && labelShowFunction.isEnabled()) {
                jPopupMenu.show(ToolbarMenu.this, 0, getHeight());
            } else if (e.getSource() == labelShowFunction && labelShowFunction.isEnabled()) {
                fireListener();
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if (labelShowFunction.isEnabled()) {
                ToolbarMenu.this.setBorder(BorderFactory.createEtchedBorder());
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (labelShowFunction.isEnabled()) {
                ToolbarMenu.this.setBorder(BorderFactory.createEmptyBorder(initWidth + 2, initHeight + 2, initWidth + 2, initHeight + 2));
            }
        }
    };

    public void addFunctionClickListener(ToolbarJmenuListener toolbarJmenuListener) {
        this.listeners = toolbarJmenuListener;
    }

    public void removeFunctionClickListener(ToolbarJmenuListener toolbarJmenuListener) {
        this.listeners = null;
    }

    private void fireListener() {
        if (this.listeners != null) {
            this.listeners.toolbarMenuClick();
        }
    }

    public void addJmentItem(JMenuItem jMenuItem) {
        this.jPopupMenu.add(jMenuItem);
    }

    public void setToolbarMenuEnabled(boolean enabled) {
        this.labelShowFunction.setEnabled(enabled);
        //this.labelArraw.setEnabled(enabled);
    }

    public boolean isToolbarMenuEnabled() {
        return this.labelShowFunction.isEnabled();
    }

    public void removeAllJmenuItem(){
        this.jPopupMenu.removeAll();
    }

    public void addSeparator(){
        this.jPopupMenu.addSeparator();
    }

    public void setShowFunctionText(String text){
        if (text!=null &&!text.isEmpty()){
            this.labelShowFunction.setText(text);
        }
    }
}
