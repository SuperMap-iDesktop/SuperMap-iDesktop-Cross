package com.supermap.desktop.implement;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.ICtrlAction;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.ui.XMLCommand;
import com.supermap.desktop.utilities.CtrlActionUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class SmTextField extends JTextField implements IBaseItem {
	private static final long serialVersionUID = 1L;
	private transient IForm formClass = null;
	private transient XMLCommand xmlCommand = null;

	public SmTextField(IForm formClass, XMLCommand xmlCommand, JComponent parent) {
		super(xmlCommand.getLabel());
		super.setToolTipText(xmlCommand.getTooltip());

		this.formClass = formClass;
		this.xmlCommand = xmlCommand;

		this.addFocusListener(new java.awt.event.FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				runCtrlAction();
			}

			@Override
			public void focusGained(FocusEvent arg0) {
				runCtrlAction();
			}
		});

		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent evt) {
				runCtrlAction();
			}
		});

		try {
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
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	public void runCtrlAction() {
		try {
			if (this.getCtrlAction() != null) {
				this.getCtrlAction().setCaller(this);
				this.getCtrlAction().run();
			} else {
				// DO NOTHING
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		try {
			super.paintComponent(g);
			if (this.getCtrlAction() != null) {
				boolean enable = this.getCtrlAction().enable();
				this.setEnabled(enable);
				this.updateUI();
			}
		} catch (Exception ex) {
           // do nothing
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
		return false;
	}

	@Override
	public void setChecked(boolean checked) {
		// DO NOTHING
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
