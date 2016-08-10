package com.supermap.desktop.ui;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IDefaultValueCreator;
import com.supermap.desktop.Interface.IXMLCreator;
import com.supermap.desktop.PluginInfo;
import com.supermap.desktop.enums.XMLCommandType;

public class XMLMenuCreator implements IXMLCreator {
	
    private XMLMenus parent;    
	private IDefaultValueCreator defaultValueCreator = null;

	public XMLMenuCreator(XMLMenus parent) {
		this.parent = parent;
	}

	@Override
	public IDefaultValueCreator getDefaultValueCreator() {
		return this.defaultValueCreator;
	}

	@Override
	public void setDefaultValueCreator(IDefaultValueCreator defaultValueCreator) {
		this.defaultValueCreator = defaultValueCreator;
	}

	@Override
	public XMLCommandBase createElement(XMLCommandType commandType) {
		XMLMenu result = null;
		try {
			PluginInfo pluginInfo = this.getDefaultValueCreator()
					.getDefaultPluginInfo();
			result = new XMLMenu(pluginInfo, this.parent);
			result.setID(((MenuDefaultValueCreator) this
					.getDefaultValueCreator()).getDefaultID(result, "Menu"));
			result.setLabel(result.getID());
			result.setIndex(((MenuDefaultValueCreator) this
					.getDefaultValueCreator()).getDefaultIndex(result));
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return result;
	}    
    
}