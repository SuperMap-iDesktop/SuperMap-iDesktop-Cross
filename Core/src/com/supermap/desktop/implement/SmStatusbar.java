package com.supermap.desktop.implement;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IStatusbar;
import com.supermap.desktop.ui.XMLCommand;
import com.supermap.desktop.ui.XMLStatusbar;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.util.ArrayList;

public class SmStatusbar extends JToolBar implements IStatusbar {
	private static final long serialVersionUID = 1L;
	private boolean buildFinished = false;
	private transient XMLStatusbar xmlStatusbar = null;
	private ArrayList<IBaseItem> items = null;
	private String formClassName = "";

	public SmStatusbar(XMLStatusbar xmlStatusbar) {
		this.xmlStatusbar = xmlStatusbar;
		this.items = new ArrayList<IBaseItem>();
		initialize();
	}

	private void initialize() {
		this.formClassName = this.xmlStatusbar.getFormClassName();
	}

	public void build(IForm form) {
		if (!this.buildFinished) {
			super.setVisible(this.xmlStatusbar.getVisible());
			this.setRollover(true);
			this.setFloatable(false);
			this.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
			this.xmlStatusbar.setAssociatedForm(form);
			createStatusbarItems();
			createLayout();
			this.buildFinished = true;
		}
	}

	public ArrayList<IBaseItem> items() {
		return this.items;
	}

	@Override
	public IBaseItem get(int index) {
		IBaseItem result = null;

		try {
			if (index >= 0 && index < this.getCount()) {
				result = this.items.get(index);
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return result;
	}

	@Override
	public IBaseItem get(Class<?> controlType) {
		IBaseItem result = null;

		try {
			for (int i = 0; i < this.getCount(); i++) {
				if (this.get(i) != null && this.get(i).getCtrlAction() != null && this.get(i).getCtrlAction().getClass().equals(controlType)) {
					result = this.get(i);
					break;
				}
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return result;
	}

	@Override
	public String getFormClassName() {
		return this.formClassName;
	}

	@Override
	public int getCount() {
		return this.items.size();
	}

	private void createStatusbarItems() {
		try {
			this.items.clear();
			IBaseItem item = null;
			for (int i = 0; i < this.xmlStatusbar.items().size(); i++) {
				XMLCommand xmlCommand = this.xmlStatusbar.items().get(i);
				xmlCommand.setCtrlAction(null);
				item = SmComponentFactory.create(xmlCommand, this);
				if (item != null) {
					this.items.add(item);
				}
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	private void createLayout() {
		if (this.items != null && !this.items.isEmpty()) {
//			GroupLayout groupLayout = new GroupLayout(this);
//			GroupLayout.SequentialGroup hGroup = groupLayout.createSequentialGroup();
//			for (int i = 0; i < this.items.size(); i++) {
//				Component component = (Component) this.items.get(i);
//				hGroup.addComponent(component);
//				hGroup.addGap(5);
//			}
//			groupLayout.setHorizontalGroup(hGroup);
			this.setLayout(new GridBagLayout());
			for (int i = 0; i < this.items.size(); i++) {
				Component component = (Component) this.items.get(i);
				GridBagConstraints gridBagConstraints = new GridBagConstraints();
				gridBagConstraints.gridx = i;
				gridBagConstraints.gridy = 0;
				gridBagConstraints.gridheight = 1;
				gridBagConstraints.gridwidth = 1;
				if (component instanceof JLabel) {
					gridBagConstraints.weightx = 0;
				} else if (component instanceof JTextField) {
					gridBagConstraints.weightx = 1;
				} else if (component instanceof JComboBox) {
					gridBagConstraints.weightx = 0.1;
				} else {
					gridBagConstraints.weightx = 1;
				}

				gridBagConstraints.fill = GridBagConstraints.BOTH;
				gridBagConstraints.anchor = GridBagConstraints.CENTER;
				this.add(component, gridBagConstraints);
			}
		}
	}

	private void addItems(IBaseItem item) {
		try {
			if (item != null) {
				this.add((Component) item);
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}
}
