package com.supermap.desktop.ui;

import com.supermap.desktop.Interface.IDefaultValueCreator;
import com.supermap.desktop.Interface.IXMLCreator;
import com.supermap.desktop.enums.XMLCommandType;

public class XMLStatusbarCreator implements IXMLCreator {
	private XMLCommandBase parent;

	public XMLCommandBase getParent() {
		return parent;
	}

	public void setParent(XMLCommandBase parent) {
		this.parent = parent;
	}

	public XMLStatusbarCreator(XMLCommandBase parent) {
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
		return null;
	}

	@Override
	public void setDefaultValueCreator(IDefaultValueCreator defaultValueCreator) {
		// TODO Auto-generated method stub

	}
}
