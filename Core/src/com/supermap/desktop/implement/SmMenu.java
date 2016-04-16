package com.supermap.desktop.implement;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.ICtrlAction;
import com.supermap.desktop.Interface.IMenu;
import com.supermap.desktop.ui.XMLCommand;
import com.supermap.desktop.ui.XMLMenu;
import com.supermap.desktop.ui.XMLMenuButton;
import com.supermap.desktop.ui.XMLMenuButtonDropdown;
import com.supermap.desktop.ui.XMLMenuGroup;
import com.supermap.desktop.utilties.DatasourceUtilties;
import com.supermap.desktop.utilties.PathUtilties;
import com.supermap.desktop.utilties.WorkspaceUtilties;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

public class SmMenu extends JMenu implements IMenu {
	private static final long serialVersionUID = 1L;
	private boolean buildFinished = false;
	private transient XMLMenu xmlMenu;

	public SmMenu(XMLMenu xmlMenu) {
		if (xmlMenu != null) {
			this.xmlMenu = xmlMenu;
			initialize();
		}
	}

	protected boolean initialize() {
		this.getPopupMenu().addPopupMenuListener(new SetPopupMenuListener());
		boolean result = false;
		if (this.xmlMenu != null) {

			setText(this.xmlMenu.getLabel());
			this.index = this.xmlMenu.getIndex();

			if (xmlMenu instanceof XMLMenuButtonDropdown) {
				if (xmlMenu.getImageFile() == null || "".equals(xmlMenu.getImageFile())) {
					xmlMenu.setImageFile("../Resources/MenuHeight16.png");
				}

				String[] pathPrams = new String[] { PathUtilties.getRootPathName(), xmlMenu.getImageFile() };
				String path = PathUtilties.combinePath(pathPrams, false);
				File file = new File(path);
				if (file.exists()) {
					this.setIcon(new ImageIcon(path));
				}
			}
			setVisible(this.xmlMenu.getVisible());
			this.load();

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

	@Override
	public String getText() {
		return super.getText();
	}

	@Override
	public void setText(String text) {
		super.setText(text);
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
	public void setVisible(boolean visible) {
		super.setVisible(visible);
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
				item = (IBaseItem) super.getItem(index);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return item;
	}

	@Override
	public int getCount() {
		return super.getItemCount();
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
			load();

			this.buildFinished = true;
		}
	}

	private void load() {

		for (int i = 0; i < this.xmlMenu.groups().size(); i++) {
			XMLMenuGroup group = this.xmlMenu.groups().get(i);
			loadMenuGroup(group, this);
		}

		// 删除分割线
		if (this.getCount() > 0) {
			this.removeAt(this.getCount() - 1);
		}
	}

	private void loadMenuGroup(XMLMenuGroup group, SmMenu parent) {
		try {
			if (group.getVisible() && !group.items().isEmpty()) {
				for (int indexTemp = 0; indexTemp < group.items().size(); indexTemp++) {
					XMLCommand xmlCommand = group.items().get(indexTemp);

					if (xmlCommand instanceof XMLMenuButtonDropdown) {
						loadMenuItemButtonDropdown((XMLMenuButtonDropdown) xmlCommand, group, parent);
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

	private void loadMenuItemButton(XMLMenuButton xmlMenuButton, SmMenu parent) {
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

	private void loadMenuItemButtonDropdown(XMLMenuButtonDropdown xmlMenuButtonDropdown, XMLMenuGroup xmlMenuGroup, SmMenu parent) {
		try {
			SmMenu menuButtonDropdown = new SmMenu(xmlMenuButtonDropdown);
			if (menuButtonDropdown != null) {
				parent.items().add(menuButtonDropdown);
				((JMenu) parent).add(menuButtonDropdown);
			}

			if ("RecentFile".equals(xmlMenuGroup.getID())) {
				if ("WorkspaceRecentFiles".equals(xmlMenuButtonDropdown.getID())) {
					if (WorkspaceUtilties.getPluginInfo() == null) {
						WorkspaceUtilties.setPluginInfo(xmlMenuButtonDropdown.getPluginInfo());
					}

					if (WorkspaceUtilties.getRecentWorkspaceMenu() == null) {
						WorkspaceUtilties.setRecentWorkspaceMenu(menuButtonDropdown);
					}
				} else if ("DatasourceRecentFiles".equals(xmlMenuButtonDropdown.getID())) {
					if (DatasourceUtilties.getPluginInfo() == null) {
						DatasourceUtilties.setPluginInfo(xmlMenuButtonDropdown.getPluginInfo());
					}

					if (DatasourceUtilties.getRecentDatasourceMenu() == null) {
						DatasourceUtilties.setRecentDatasourceMenu(menuButtonDropdown);
					}
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	class SetPopupMenuListener implements PopupMenuListener {

		@Override
		public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
			for (IBaseItem item : items()) {
				if (item instanceof SmMenuItem) {
					((SmMenuItem) item).checkNowState();
				}
			}
		}

		@Override
		public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
			// do nothing
		}

		@Override
		public void popupMenuCanceled(PopupMenuEvent e) {
			// do nothing
		}

	}
}
