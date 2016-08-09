package com.supermap.desktop.ui;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IStatusbar;
import com.supermap.desktop.Interface.IStatusbarManager;
import com.supermap.desktop.WorkEnvironment;
import com.supermap.desktop.implement.SmStatusbar;

import java.util.ArrayList;

public class StatusbarManager implements IStatusbarManager {

	private ArrayList<SmStatusbar> statusbars = null;
	private XMLStatusbars xmlStatusbars = null;

	public StatusbarManager() {
		this.statusbars = new ArrayList<SmStatusbar>();
	}

	public boolean load(WorkEnvironment workEnvironment) {
		boolean result = false;

		try {
			this.xmlStatusbars = workEnvironment.getPluginInfos().getStatusbars();
			result = true;
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return result;
	}

	@Override
	public IStatusbar get(int index) {
		return this.statusbars.get(index);
	}

	@Override
	public IStatusbar get(String formClassName) {
		IStatusbar result = null;

		try {
			int count = this.getCount();
			for (int i = 0; i < count; i++) {
				if (this.get(i).getFormClassName().equalsIgnoreCase(formClassName)) {
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
	public int getCount() {
		return this.statusbars.size();
	}

	public SmStatusbar getStatusbar(String formClassName) {
		SmStatusbar result = null;

		try {
			for (int i = 0; i < this.xmlStatusbars.getStatusbars().size(); i++) {
				XMLStatusbar xmlStatusbar = this.xmlStatusbars.getStatusbars().get(i);
				if (xmlStatusbar.getFormClassName().equalsIgnoreCase(formClassName)) {
					result = new SmStatusbar(xmlStatusbar);
					break;
				}
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return result;
	}
}
