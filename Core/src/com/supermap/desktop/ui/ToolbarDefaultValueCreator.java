package com.supermap.desktop.ui;

import java.util.ArrayList;

import com.supermap.desktop.Application;
import com.supermap.desktop.PluginInfo;
import com.supermap.desktop.Interface.IDefaultValueCreator;

public class ToolbarDefaultValueCreator implements IDefaultValueCreator {

	private XMLToolbars rootParent;

	public ToolbarDefaultValueCreator(XMLToolbars rootParent) {
		this.rootParent = rootParent;
	}

	@Override
	public String getDefaultLabel(String text) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDefaultID(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 新建工具条子项时，需要 id 在对应的控件上不重复
	 * 
	 * @param newToolbar
	 * @param id
	 * @return
	 */
	public String getDefaultID(XMLToolbar newToolbar, String id) {
		String result = id;
		try {
			int count = 0;
			ArrayList<XMLToolbar> toolbars = this.rootParent.getToolbars();
			while (!isIDEnabled(newToolbar, toolbars, result)) {
				count++;
				result = id + String.valueOf(count);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return result;
	}

	@Override
	public Boolean isIDEnabled(String id) {
		// TODO Auto-generated method stub
		return false;
	}

	public Boolean isIDEnabled(XMLToolbar newToolbar, ArrayList<XMLToolbar> toolbars, String id) {
		Boolean result = true;
		return result;
	}

	@Override
	public int getDefaultIndex() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getDefaultIndex(XMLToolbar newToolbar) {
		ArrayList<XMLToolbar> toolbars = this.rootParent.getToolbars();
		return getDefaultIndex(newToolbar, toolbars, 0);
	}

	private int getDefaultIndex(XMLToolbar newToolbar, ArrayList<XMLToolbar> toolbars, int index) {
		int result = index;
		try {
			while (!isIndexEnabled(newToolbar, toolbars, result)) {
				result++;
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return result;
	}

	@Override
	public PluginInfo getDefaultPluginInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	private Boolean isIndexEnabled(XMLToolbar newToolbar, ArrayList<XMLToolbar> toolbars, int index) {
		Boolean result = true;

		return result;
	}
}
