package com.supermap.desktop.implement;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.ICtrlAction;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.ui.XMLCommand;
import com.supermap.desktop.utilities.CtrlActionUtilities;
import com.supermap.desktop.utilities.JOptionPaneUtilities;
import com.supermap.desktop.utilities.PathUtilities;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

public class SmMenuItem extends JCheckBoxMenuItem implements IBaseItem {
    private static final long serialVersionUID = 1L;
    private transient IForm formClass = null;
    private transient XMLCommand xmlCommand = null;

    public SmMenuItem(IForm formClass, XMLCommand xmlCommand, JComponent parent) {
        super(xmlCommand.getLabel());
        super.setToolTipText(xmlCommand.getTooltip());

        try {
            if (xmlCommand.getImageFile() == null || xmlCommand.getImageFile() == "") {
                xmlCommand.setImageFile("../Resources/MenuHeight16.png");
            }

            String[] pathPrams = new String[]{PathUtilities.getRootPathName(), xmlCommand.getImageFile()};
            String path = PathUtilities.combinePath(pathPrams, false);
            File file = new File(path);
            if (file.exists()) {
                this.setIcon(new ImageIcon(path));
            }
            this.setName(xmlCommand.getLabel());
            this.formClass = formClass;
            this.xmlCommand = xmlCommand;

            // 这里临时做重复判断，以后再统一优化
            // 同时建议，框架菜单、弹出菜单和工具条的CtrlAction统一管理
            ICtrlAction ctrlAction = Application.getActiveApplication().getCtrlAction(xmlCommand.getPluginInfo().getBundleName(),
                    xmlCommand.getCtrlActionClass());
            if (ctrlAction == null) {
                ctrlAction = CtrlActionUtilities.getCtrlAction(xmlCommand, this, this.formClass);
            }

            if (ctrlAction != null) {
                ctrlAction.setFormClass(this.formClass);
                setCtrlAction(ctrlAction);
                Application.getActiveApplication().setCtrlAction(xmlCommand.getPluginInfo().getBundleName(), xmlCommand.getCtrlActionClass(), ctrlAction);
                // 此处改为使用SmMenu中的popupMenuListener，也许有问题，暂不删除。
                this.addPropertyChangeListener(new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        MenuItem_PropertyChange(evt);
                    }
                });
            } else {
                this.setText(this.getText() + "(**)");
            }

            this.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    MenuItem_ActionPerformed();
                }
            });
        } catch (Exception ex) {
            Application.getActiveApplication().getOutput().output(ex);
        }
    }

    private void MenuItem_ActionPerformed() {
        try {
            if (this.getCtrlAction() != null) {
                this.getCtrlAction().setCaller(this);
                this.getCtrlAction().run();
            } else {
                Application.getActiveApplication().getOutput().output("CtrlAction Unimplemented!");
                JOptionPaneUtilities.showMessageDialog(this.xmlCommand.getCtrlActionClass() + " Unimplemented!");
            }
        } catch (Exception ex) {
            Application.getActiveApplication().getOutput().output(ex);
        }
    }

    public void checkNowState() {
        if (this.getCtrlAction() != null) {
            this.getCtrlAction().setCaller(this);
            boolean enable = this.getCtrlAction().enable();
            this.setEnabled(enable);

            boolean check = this.getCtrlAction().check();
            this.setSelected(check);
        } else {
            this.setSelected(false);
        }
    }

    public void MenuItem_PropertyChange(PropertyChangeEvent evt) {
        try {
            // 给菜单项设置可用、选中状态
            // oldVale为null，newValue存在invalid信息
            if (evt.getOldValue() == null && evt.getNewValue() != null) {
                if (this.getCtrlAction() != null) {
                    this.getCtrlAction().setCaller(this);
                    boolean enable = this.getCtrlAction().enable();
                    this.setEnabled(enable);

                    boolean check = this.getCtrlAction().check();
                    this.setSelected(check);
                } else {
                    this.setSelected(false);
                }
            }
        } catch (Exception ex) {
            Application.getActiveApplication().getOutput().output(ex);
        }
    }

    @Override
    public boolean isEnabled() {
        return super.isEnabled();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
    }

    @Override
    public boolean isChecked() {
        return super.isSelected();
    }

    @Override
    public void setChecked(boolean checked) {
        super.setSelected(checked);
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
    public String getText() {
        return super.getText();
    }

    @Override
    public void setText(String text) {
        super.setText(text);
    }

    @Override
    public ICtrlAction getCtrlAction() {
        return this.xmlCommand.getCtrlAction();
    }

    @Override
    public void setCtrlAction(ICtrlAction ctrlAction) {
        this.xmlCommand.setCtrlAction(ctrlAction);
    }
}
