package com.supermap.desktop.ui;

import com.supermap.desktop.Application;
import com.supermap.desktop.PluginInfo;
import com.supermap.desktop.enums.XMLCommandType;
import org.w3c.dom.Element;

public class XMLMenuButton extends XMLMenuCommand {

	public XMLMenuButton(PluginInfo pluginInfo, XMLMenuGroup group) {
		super(pluginInfo, group);
		this.commandType = XMLCommandType.MENUBUTTON;
	}

	@Override
	public boolean getIsContainer() {
		return false;
	}

	boolean isSelected = false;

	public boolean getSelected() {
		return this.isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	@Override
	public boolean initialize(Element xmlNodeCommand) {
		super.initialize(xmlNodeCommand);

		try {
			if (xmlNodeCommand.getAttribute(g_AttributionCheckState).equalsIgnoreCase(g_ValueTrue)) {
				this.setSelected(true);
			} else {
				this.setSelected(false);
			}
		} catch (Exception ex) {
			// do nothing
		}

		return true;
	}

	@Override
	public XMLCommandBase clone(XMLCommandBase parent) {
		XMLMenuButton result = null;
		try {
			result = (XMLMenuButton) super.clone(parent);
			result.setSelected(this.getSelected());
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return result;
	}

	@Override
	public XMLCommandBase saveToPluginInfo(PluginInfo pluginInfo, XMLCommandBase parent) {
		XMLMenuButton result = null;
		try {
			result = (XMLMenuButton) super.saveToPluginInfo(pluginInfo, parent);
			if (result != null) {
				result.setSelected(this.getSelected());
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return result;
	}

}
