package com.supermap.desktop.ui;

import com.supermap.desktop.Interface.IDefaultValueCreator;
import com.supermap.desktop.Interface.IXMLCreator;
import com.supermap.desktop.enums.XMLCommandType;

public class XMLMenuGroupItemCreator implements IXMLCreator {

	private XMLMenuGroup parent = null;
	public XMLMenuGroup getParent() {
		return parent;
	}

	public void setParent(XMLMenuGroup parent) {
		this.parent = parent;
	}

	private IDefaultValueCreator defaultValueCreator = null;

	public XMLMenuGroupItemCreator(XMLMenuGroup parent) {
		this.parent = parent;
	}

	@Override
	public XMLCommandBase createElement(XMLCommandType commandType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IDefaultValueCreator getDefaultValueCreator() {
		// TODO Auto-generated method stub
		return this.defaultValueCreator;
	}

	@Override
	public void setDefaultValueCreator(IDefaultValueCreator defaultValueCreator) {
		// TODO Auto-generated method stub
		this.defaultValueCreator = defaultValueCreator;
	}
}
