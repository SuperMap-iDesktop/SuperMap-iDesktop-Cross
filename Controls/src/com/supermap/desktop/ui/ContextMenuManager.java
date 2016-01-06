package com.supermap.desktop.ui;

import java.util.ArrayList;
import com.supermap.desktop.Application;
import com.supermap.desktop.WorkEnvironment;
import com.supermap.desktop.Interface.IContextMenuManager;
import com.supermap.desktop.Interface.IPopupMenu;
import com.supermap.desktop.implement.SmPopupMenu;
import com.supermap.desktop.ui.XMLMenu;

public class ContextMenuManager implements IContextMenuManager {

	private ArrayList<IPopupMenu> listMenus = null;

	public ContextMenuManager() {
		this.listMenus = new ArrayList<IPopupMenu>();
	}

	@Override
	public IPopupMenu get(int index) {
		return this.listMenus.get(index);
	}

	@Override
	public IPopupMenu get(String id) {
		IPopupMenu item = null;
		for (int i = 0; i < this.listMenus.size(); i++) {
			if (this.listMenus.get(i).getID().equalsIgnoreCase(id)) {
				item = this.listMenus.get(i);
				break;
			}
		}
		return item;
	}

	@Override
	public int getCount() {
		return this.listMenus.size();
	}

	@Override
	public boolean contains(IPopupMenu item) {
		return this.listMenus.contains(item);
	}

	public void add(IPopupMenu item) {
		this.listMenus.add(item);
	}

	public void removeAt(int index) {
		this.listMenus.remove(index);
	}

	public void removeAll() {
		this.listMenus.clear();
	}

	public Boolean load(WorkEnvironment workEnvironment) {
		Boolean result = false;
		try {
			// 查找有哪些 contextMenu
			ArrayList<XMLMenu> xmlMenus = workEnvironment.getPluginInfos().getContextMenus().getMenus();
			for (int i = 0; i < xmlMenus.size(); i++) {

				SmPopupMenu menu = new SmPopupMenu(xmlMenus.get(i));
				this.listMenus.add(menu);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);

		}
		return result;
	}
}
