package com.supermap.desktop.ui;

import com.supermap.desktop.Interface.IDefaultValueCreator;
import com.supermap.desktop.Interface.IXMLCreator;
import com.supermap.desktop.enums.XMLCommandType;

public class XMLMenuGroupCreator implements IXMLCreator {

	private XMLCommandBase parent;

	public XMLCommandBase getParent() {
		return parent;
	}

	public void setParent(XMLCommandBase parent) {
		this.parent = parent;
	}

	public XMLMenuGroupCreator(XMLCommandBase parent) {
		this.parent = parent;
	}

	@Override
	public XMLCommandBase createElement(XMLCommandType commandType) {

		XMLMenuGroup result = null;
		return result;
	}

	private IDefaultValueCreator defaultValueCreator;

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
