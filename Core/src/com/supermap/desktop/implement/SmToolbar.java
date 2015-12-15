package com.supermap.desktop.implement;

import java.awt.Component;
import java.util.ArrayList;
import javax.swing.JToolBar;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.ICtrlAction;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IToolbar;
import com.supermap.desktop.ui.XMLButton;
import com.supermap.desktop.ui.XMLButtonDropdown;
import com.supermap.desktop.ui.XMLComboBox;
import com.supermap.desktop.ui.XMLCommand;
import com.supermap.desktop.ui.XMLLabel;
import com.supermap.desktop.ui.XMLSeparator;
import com.supermap.desktop.ui.XMLTextbox;
import com.supermap.desktop.ui.XMLToolbar;

public class SmToolbar extends JToolBar implements IToolbar {

	private static final long serialVersionUID = 1L;
	private boolean buildFinished = false;
	private transient XMLToolbar xmlToolbar;

	public SmToolbar(XMLToolbar xmlToolbar) {
		if (xmlToolbar != null) {
			this.xmlToolbar = xmlToolbar;
			initialize();
		}
	}

	protected boolean initialize() {
		boolean result = false;
		if (this.xmlToolbar != null) {
			this.setFloatable(false);
			this.setToolTipText(this.xmlToolbar.getTooltip());
			setVisible(this.xmlToolbar.getVisible());
			this.load();

			result = true;
		}

		return result;
	}

	public ArrayList<IBaseItem> items() {
		ArrayList<IBaseItem> items = new ArrayList<IBaseItem>();
		for (int i = 0; i < super.getComponentCount(); i++) {
			if (super.getComponent(i) instanceof IBaseItem) {
				IBaseItem item = (IBaseItem) this.getComponent(i);
				items.add(item);
			}
		}
		return items;
	}

	@Override
	public boolean isVisible() {
		return super.isVisible();
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
	}

	@Override
	public int getIndex() {
		return this.xmlToolbar.getIndex();
	}

	@Override
	public void setIndex(int index) {
		this.xmlToolbar.setIndex(index);
	}

	@Override
	public String getID() {
		return this.xmlToolbar.getID();
	}

	public void build(IForm form) {
		if (!this.buildFinished) {
			// 加载相应的右键菜单
			load();
			this.buildFinished = true;
		}
	}

	private void load() {
		try {
			for (int index = 0; index < this.xmlToolbar.items().size(); index++) {
				XMLCommand xmlItem = xmlToolbar.items().get(index);
				IBaseItem item = null;
				if (xmlItem instanceof XMLButton) {
					item = new SmButton(null, xmlItem, this);
				} else if (xmlItem instanceof XMLButtonDropdown) {
					item = new SmButtonDropdown(null, xmlItem, this);
				} else if (xmlItem instanceof XMLLabel) {
					item = new SmLabel(null, xmlItem, this);
				} else if (xmlItem instanceof XMLTextbox) {
					item = new SmTextField(null, xmlItem, this);
				} else if (xmlItem instanceof XMLComboBox) {
					item = new SmComboBox(null, xmlItem, this);
				} else if (xmlItem instanceof XMLSeparator) {
					item = new SmSeparator(null, xmlItem, this);
				}

				if (item != null) {
					this.add(item);
				}
			}
			this.setName(xmlToolbar.getLabel());
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public boolean isEnabled() {
		return super.isEnabled();
	}

	@Override
	public boolean isChecked() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setChecked(boolean checked) {
		// TODO Auto-generated method stub

	}

	@Override
	public IBaseItem getAt(int index) {
		IBaseItem item = null;
		try {
			if (index >= 0 && index < this.getCount()) {
				Component[] component = this.getComponents();
				item = (IBaseItem) component[index];
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return item;
	}

	@Override
	public int add(IBaseItem item) {
		try {
			super.add((Component) item);
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return super.getComponentCount();
	}

	@Override
	public void addRange(IBaseItem[] items) {
		try {
			for (IBaseItem item : items) {
				super.add((Component) item);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public boolean contains(IBaseItem item) {
		boolean result = false;
		try {
			for (int i = 0; i < this.getCount(); i++) {
				if (this.getAt(i).equals(item)) {
					result = true;
					break;
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return result;
	}

	@Override
	public int indexOf(IBaseItem item) {
		int result = -1;
		try {
			for (int i = 0; i < this.getCount(); i++) {
				if (this.getAt(i).equals(item)) {
					result = i;
					break;
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return result;
	}

	@Override
	public void insert(IBaseItem item, int index) {
		try {
			super.add((Component) item, index);
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public void remove(IBaseItem item) {
		try {
			super.remove((Component) item);
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public ICtrlAction getCtrlAction() {
		return null;
	}

	@Override
	public void setCtrlAction(ICtrlAction ctrlAction) {
		// DO NOTHING
	}

	@Override
	public int getCount() {
		return super.getComponentCount();
	}

	@Override
	public void clear() {
		try {
			super.removeAll();
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public void removeAt(int index) {
		try {
			super.remove(index);
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	public XMLToolbar getXMLToolbar() {
		return this.xmlToolbar;
	}

}
