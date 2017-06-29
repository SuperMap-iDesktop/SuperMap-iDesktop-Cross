package com.supermap.desktop.implement;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.ICtrlAction;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.XMLCommand;
import com.supermap.desktop.utilities.CtrlActionUtilities;
import com.supermap.desktop.utilities.JOptionPaneUtilities;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * @author XiaJT
 */
public class SmCheckBox extends JCheckBox implements IBaseItem {
	private static final long serialVersionUID = 1L;
	private transient IForm formClass = null;

	public IForm getFormClass() {
		return formClass;
	}

	public void setFormClass(IForm formClass) {
		this.formClass = formClass;
	}

	private transient XMLCommand xmlCommand = null;

	public SmCheckBox(IForm formClass, XMLCommand xmlCommand, JComponent parent) {
		super();
		this.setText(xmlCommand.getLabel());
		this.formClass = formClass;
		this.xmlCommand = xmlCommand;
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
		this.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				item_ActionPerformed();
			}
		});
	}

	private void item_ActionPerformed() {
		try {
			if (this.getCtrlAction() != null) {
				this.getCtrlAction().setCaller(this);
				this.getCtrlAction().doRun();
			} else {
				Application.getActiveApplication().getOutput().output("CtrlAction Unimplemented!");
				JOptionPaneUtilities.showMessageDialog(this.xmlCommand.getCtrlActionClass() + " Unimplemented!");
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
		return false;
	}

	@Override
	public void setChecked(boolean checked) {
		// do nothing
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
}
