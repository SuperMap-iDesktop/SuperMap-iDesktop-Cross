package com.supermap.desktop.ui;

import com.supermap.desktop.Application;
import com.supermap.desktop.PluginInfo;
import com.supermap.desktop.Interface.IDefaultValueCreator;
import com.supermap.desktop.Interface.IXMLCreator;
import com.supermap.desktop.enums.XMLCommandType;

public class XMLToolbarCreator implements IXMLCreator {
	
	private XMLToolbars parent;
	private IDefaultValueCreator defaultValueCreator = null;

	public XMLToolbarCreator(XMLToolbars parent) {
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
		XMLToolbar result = null;
		try {
			PluginInfo pluginInfo = this.getDefaultValueCreator()
					.getDefaultPluginInfo();
			result = new XMLToolbar(pluginInfo, this.parent);
			result.setID(((ToolbarDefaultValueCreator) this
					.getDefaultValueCreator()).getDefaultID(result, "Toolbar"));
			result.setLabel(result.getID());
			result.setIndex(((ToolbarDefaultValueCreator) this
					.getDefaultValueCreator()).getDefaultIndex(result));
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return result;
	}  
}
