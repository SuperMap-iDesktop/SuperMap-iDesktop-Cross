package com.supermap.desktop.implement;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.ICtrlAction;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.XMLButtonDropdown;
import com.supermap.desktop.ui.XMLCommand;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.utilities.CtrlActionUtilities;
import com.supermap.desktop.utilities.JOptionPaneUtilities;
import com.supermap.desktop.utilities.PathUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.io.File;

public class SmButtonDropdown extends JComponent implements IBaseItem {

    private static final long serialVersionUID = 1L;
    private final MouseAdapter popupMenuMouseListener = new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent e) {
            if (getParent() instanceof JPopupMenu) {
                Point p = new Point(getWidth() - 2 + popupMenu.getWidth(), getY());
                SwingUtilities.convertPointToScreen(p, getParent());

                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                int x = getWidth() - 2;
                if (p.getX() > screenSize.getWidth()) {
                    x = -popupMenu.getWidth() + 2;
                }
                popupMenu.show(getParent(), x, getY());
            }
        }
    };
    private transient IForm formClass = null;
    private transient XMLCommand xmlCommand = null;

    protected transient ControlButton displayButton;
    protected transient ControlButton arrowButton;
    protected transient JPopupMenu popupMenu;

    public SmButtonDropdown(int popupType) {

    }

    public SmButtonDropdown(IForm formClass, XMLCommand xmlCommand, JComponent parent) {
        super.setToolTipText(xmlCommand.getTooltip());

        displayButton = new ControlButton(this);
        displayButton.setPreferredSize(new Dimension(31, 31));
        this.setPreferredSize(new Dimension(51, 31));
        this.setMaximumSize(new Dimension(51, 31));
        this.setMinimumSize(new Dimension(51, 31));
        String[] pathPrams = new String[]{PathUtilities.getRootPathName(), xmlCommand.getImageFile()};
        String path = PathUtilities.combinePath(pathPrams, false);
        File file = new File(path);
        if (file.exists() && file.isFile()) {
            displayButton.setIcon(new ImageIcon(path));
        } else {
            displayButton.setText(xmlCommand.getLabel());
        }
        displayButton.setToolTipText(xmlCommand.getLabel());

        arrowButton = new ControlButton(this);
        arrowButton.setIcon(new InnerMetaComboBoxIcon());

        displayButton.addMouseListener(popupMenuMouseListener);
        arrowButton.addMouseListener(popupMenuMouseListener);
        Insets insets = arrowButton.getMargin();
        arrowButton.setMargin(new Insets(insets.top, 1, insets.bottom, 1));

        XMLButtonDropdown xmlButtonDropdown = (XMLButtonDropdown) xmlCommand;
        popupMenu = new SmPopupMenu(xmlButtonDropdown.getDropdownMenu());

        arrowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showPopupMenu();
            }

        });
        reLayout(false);

        this.formClass = formClass;
        this.xmlCommand = xmlCommand;

        try {
            ICtrlAction ctrlAction = Application.getActiveApplication().getCtrlAction(xmlCommand.getPluginInfo().getBundleName(),
                    xmlCommand.getCtrlActionClass());
            if (ctrlAction == null) {
                ctrlAction = CtrlActionUtilities.getCtrlAction(xmlCommand, this, this.formClass);
            }

            if (ctrlAction != null) {
                ctrlAction.setFormClass(this.formClass);
                setCtrlAction(ctrlAction);
                Application.getActiveApplication().setCtrlAction(xmlCommand.getPluginInfo().getBundleName(), xmlCommand.getCtrlActionClass(), ctrlAction);
            } else {
                this.setToolTipText(this.getToolTipText() + CommonProperties.getString("String_UnDo"));
            }

            displayButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    item_ActionPerformed();
                }
            });

        } catch (Exception ex) {
            Application.getActiveApplication().getOutput().output(ex);
        }
    }

    /**
     * 获取m_popupMenu
     *
     * @return
     */
    protected JPopupMenu getPopupMenu() {
        return this.popupMenu;
    }

    /**
     * 获取m_arrow
     *
     * @return
     */
    public ControlButton getDisplayButton() {
        return this.displayButton;
    }

    /**
     * 获取m_arrow
     *
     * @return
     */
    public ControlButton getArrowButton() {
        return this.arrowButton;
    }


    /**
     * 根据在的地方不同重新布局
     *
     * @param isPopupMenu 是否在弹出菜单中显示
     */
    protected void reLayout(boolean isPopupMenu) {
        int anchor = isPopupMenu ? GridBagConstraints.WEST : GridBagConstraints.CENTER;
        int ipadx = isPopupMenu ? 0 : 10;
        this.removeAll();
        this.setLayout(new GridBagLayout());
        this.add(displayButton, new GridBagConstraintsHelper(0, 0, 1, 1).setFill(GridBagConstraints.BOTH).setAnchor(anchor).setWeight(1, 1).setIpad(ipadx, 0));
        this.add(arrowButton, new GridBagConstraintsHelper(1, 0, 1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER).setWeight(0, 1).setIpad(4, 0));
    }


    private void showPopupMenu() {
        try {
            if (getParent() instanceof JPopupMenu) {
                return;
            }
            int y = (int) displayButton.getLocation().getY() + displayButton.getHeight();
            popupMenu.show(displayButton, 0, y);
        } catch (Exception ex) {
            Application.getActiveApplication().getOutput().output(ex);
        }
    }

    private void item_ActionPerformed() {
        try {
            if (this.getCtrlAction() != null) {
                this.getCtrlAction().setCaller(this);
                this.getCtrlAction().run();
            } else if (this.getPopupMenu() != null) {
                showPopupMenu();
            } else {
                Application.getActiveApplication().getOutput().output("CtrlAction Unimplemented!");
                JOptionPaneUtilities.showMessageDialog(this.xmlCommand.getCtrlActionClass() + " Unimplemented!");
            }
        } catch (Exception ex) {
            Application.getActiveApplication().getOutput().output(ex);
        }
    }

    public void Button_PropertyChange(PropertyChangeEvent evt) {
        // do nothing
    }

    @Override
    public boolean isEnabled() {
        return this.getDisplayButton().isEnabled();
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.getDisplayButton().setEnabled(enabled);
        this.getArrowButton().setEnabled(enabled);
    }

    @Override
    public boolean isChecked() {
        return this.getDisplayButton().isSelected();
    }

    @Override
    public void setChecked(boolean checked) {
        this.getDisplayButton().setSelected(checked);
        this.getArrowButton().setSelected(checked);
    }

    @Override
    public boolean isVisible() {
        return super.isVisible();
    }

    @Override
    public void setVisible(boolean enabled) {
        super.setVisible(enabled);
    }

    @Override
    public int getIndex() {
        return this.xmlCommand.getIndex();
    }

    @Override
    public void setIndex(int index) {
        this.xmlCommand.setIndex(index);
    }

    @Override
    public String getID() {
        return this.xmlCommand.getID();
    }

    @Override
    public ICtrlAction getCtrlAction() {
        return this.xmlCommand.getCtrlAction();
    }

    @Override
    public void setCtrlAction(ICtrlAction ctrlAction) {
        this.xmlCommand.setCtrlAction(ctrlAction);
    }

    public void setPopupMenu(JPopupMenu popupMenu) {
        this.popupMenu = popupMenu;
    }

    @Override
    public synchronized void addMouseListener(MouseListener l) {
        if (getDisplayButton() != null) {
            getDisplayButton().addMouseListener(l);
        }
        if (getArrowButton() != null) {
            getArrowButton().addMouseListener(l);
        }
        super.addMouseListener(l);
    }

    @Override
    public synchronized void removeMouseListener(MouseListener l) {
        if (getDisplayButton() != null) {
            getDisplayButton().removeMouseListener(l);
        }
        if (getArrowButton() != null) {
            getArrowButton().removeMouseListener(l);
        }
        super.removeMouseListener(l);
    }
}
