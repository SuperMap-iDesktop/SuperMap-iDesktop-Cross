package com.supermap.desktop.implement;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.ICtrlAction;
import com.supermap.desktop.Interface.IPopupMenu;
import com.supermap.desktop.ui.XMLCommand;
import com.supermap.desktop.ui.XMLMenu;
import com.supermap.desktop.ui.XMLMenuButton;
import com.supermap.desktop.ui.XMLMenuButtonDropdown;
import com.supermap.desktop.ui.XMLMenuGroup;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SmPopupMenu extends JPopupMenu implements IPopupMenu {
	private static final long serialVersionUID = 1L;
	private boolean buildFinished = false;
	private transient XMLMenu xmlMenu;
	private boolean smVisible;

	public SmPopupMenu() {
		// do nothing
	}

	public SmPopupMenu(XMLMenu xmlMenu) {
		if (xmlMenu != null) {
			this.xmlMenu = xmlMenu;
			initialize();
		}
	}

	protected boolean initialize() {

		boolean result = false;
		if (this.xmlMenu != null) {

			setText(this.xmlMenu.getLabel());
			this.index = this.xmlMenu.getIndex();
			// 右键菜单不能这样设置
			smVisible = this.xmlMenu.getVisible();
			this.loadMenu();

			result = true;
		}

		return result;
	}

	public ArrayList<IBaseItem> items() {
		ArrayList<IBaseItem> items = new ArrayList<IBaseItem>();
		for (int i = 0; i < this.getCount(); i++) {
			items.add(this.getAt(i));
		}
		return items;
	}

	public String getText() {
		return super.getLabel();
	}

	public void setText(String text) {
		super.setLabel(text);
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
		// TODO Auto-generated method stub
		return super.isVisible();
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (!visible) {
			Toolkit.getDefaultToolkit().removeAWTEventListener(PopupMenuMousePressEventListener.getInstance());
		} else {
			Toolkit.getDefaultToolkit().removeAWTEventListener(PopupMenuMousePressEventListener.getInstance());
			Toolkit.getDefaultToolkit().addAWTEventListener(PopupMenuMousePressEventListener.getInstance(), AWTEvent.MOUSE_EVENT_MASK);
		}
	}

	private int index = -1;

	@Override
	public int getIndex() {
		return this.index;
	}

	@Override
	public void setIndex(int index) {
		this.index = index;
	}

	@Override
	public String getID() {
		return this.xmlMenu.getID();
	}

	@Override
	public ICtrlAction getCtrlAction() {
		return null;
	}

	@Override
	public void setCtrlAction(ICtrlAction ctrlAction) {
		// do nothing
	}

	@Override
	public IBaseItem getAt(int index) {
		IBaseItem item = null;
		try {
			if (index >= 0 && index < this.getCount()) {
				Component[] component = super.getComponents();
				item = (IBaseItem) component[index];
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return item;
	}

	@Override
	public int getCount() {
		return super.getComponentCount();
	}

	@Override
	public int add(IBaseItem item) {
		if (item instanceof SmMenuItem) {
			super.add((SmMenuItem) item);
		} else if (item instanceof SmMenuSeparator) {
			super.addSeparator();
		}

		return this.getCount() - 1;
	}

	@Override
	public void addRange(IBaseItem[] items) {
		try {
			for (IBaseItem item : items) {
				this.add(item);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public void clear() {
		super.removeAll();
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
	public void removeAt(int index) {
		super.remove(index);
	}

	public void build() {
		if (!this.buildFinished) {
			// 加载相应的右键菜单
			loadMenu();

			this.buildFinished = true;
		}
	}

	private void loadMenu() {
		for (int i = 0; i < this.xmlMenu.groups().size(); i++) {
			XMLMenuGroup group = this.xmlMenu.groups().get(i);
			loadMenuGroup(group, this);
		}

		// 删除分割线
		if (this.getCount() > 0) {
			this.removeAt(this.getCount() - 1);
		}
	}

	private void loadMenuGroup(XMLMenuGroup group, SmPopupMenu parent) {
		try {
			if (group.getVisible() && !group.items().isEmpty()) {
				for (int indexTemp = 0; indexTemp < group.items().size(); indexTemp++) {
					XMLCommand xmlCommand = group.items().get(indexTemp);

					if (xmlCommand instanceof XMLMenuButtonDropdown) {
						loadMenuItemButtonDropdown((XMLMenuButtonDropdown) xmlCommand, parent);
					} else if (xmlCommand instanceof XMLMenuButton) {
						loadMenuItemButton((XMLMenuButton) xmlCommand, parent);
					}
				}

				SmMenuSeparator menuSeparator = new SmMenuSeparator();
				this.add((IBaseItem) menuSeparator);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private void loadMenuItemButton(XMLMenuButton xmlMenuButton, SmPopupMenu parent) {
		try {
			SmMenuItem menuItem = new SmMenuItem(null, xmlMenuButton, parent);
			if (menuItem != null) {
				parent.items().add(menuItem);
				this.add((IBaseItem) menuItem);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private void loadMenuItemButtonDropdown(XMLMenuButtonDropdown xmlMenuButtonDropdown, SmPopupMenu parent) {
		try {
			SmMenu menuButtonDropdown = new SmMenu(xmlMenuButtonDropdown);
			if (menuButtonDropdown != null) {
				parent.items().add(menuButtonDropdown);
				((JPopupMenu) parent).add(menuButtonDropdown);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public void show(Component invoker, int x, int y) {
		super.show(invoker, x, y);
	}
}
