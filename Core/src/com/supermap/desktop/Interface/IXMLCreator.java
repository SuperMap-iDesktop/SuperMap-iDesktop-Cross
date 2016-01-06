package com.supermap.desktop.Interface;

import com.supermap.desktop.enums.XMLCommandType;
import com.supermap.desktop.ui.XMLCommandBase;

public interface IXMLCreator {
	XMLCommandBase createElement(XMLCommandType commandType);
    IDefaultValueCreator getDefaultValueCreator();
    void setDefaultValueCreator(IDefaultValueCreator defaultValueCreator);
}
