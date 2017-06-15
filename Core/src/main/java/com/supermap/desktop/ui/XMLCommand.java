package com.supermap.desktop.ui;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.ICtrlAction;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.PluginInfo;
import com.supermap.desktop.enums.XMLCommandType;
import org.w3c.dom.Element;

import java.lang.reflect.Type;
import java.util.Comparator;

public class XMLCommand extends XMLCommandBase implements Comparator<XMLCommand>, Comparable<XMLCommand> {

	private boolean enable = true;
	private String ctrlActionClass = "";
	private ICtrlAction ctrlAction = null;

	public XMLCommand(PluginInfo pluginInfo) {
		super(pluginInfo);
		this.commandType = XMLCommandType.COMMAND;
	}

	public XMLCommand(XMLCommandBase parent) {
		super(parent);
		this.commandType = XMLCommandType.COMMAND;
	}

	public XMLCommand(PluginInfo pluginInfo, XMLCommandBase parent) {
		super(pluginInfo, parent);
		this.commandType = XMLCommandType.COMMAND;
	}

	@Override
	public boolean initialize(Element xmlNodeCommand) {
		super.initialize(xmlNodeCommand);

		// 是否可用
		try {
			if (xmlNodeCommand.getAttribute(g_AttributionEnabled).equalsIgnoreCase(g_ValueFalse)) {
				this.setEnabled(false);
			} else {
				this.setEnabled(true);
			}
		} catch (Exception ex) {
			// do nothing
		}

		try {
			this.setCtrlActionClass(xmlNodeCommand.getAttribute(g_OnAction));
		} catch (Exception ex) {
			// do nothing
		}

		// 提示信息
		try {
			String tooltipTemp = xmlNodeCommand.getAttribute(g_AttributionScreenTip);
			if (tooltipTemp == null || tooltipTemp == "") {
				tooltipTemp = this.getLabel();
			}
			this.setTooltip(tooltipTemp);
		} catch (Exception ex) {
			// do nothing
		}

		// 快捷方式
		try {
			this.setShortCutKeys(xmlNodeCommand.getAttribute(g_AttributionShortcutKey));
		} catch (Exception ex) {
			// do nothing
		}

		// 帮助链接
		try {
			this.setHelpURL(xmlNodeCommand.getAttribute(g_AttributionHelpURL));
		} catch (Exception ex) {
			// do nothing
		}

		// 图片信息
		try {
			this.setImageFile(xmlNodeCommand.getAttribute(g_AttributionImage));
		} catch (Exception ex) {
			// do nothing
		}

		// 描述信息
		try {
			this.setDescription(xmlNodeCommand.getAttribute(g_AttributionDescription));
		} catch (Exception ex) {
			// do nothing
		}

		return true;
	}

	public boolean getEnabled() {
		return this.enable;
	}

	public void setEnabled(boolean enable) {
		this.enable = enable;
	}

	public String getCtrlActionClass() {
		return this.ctrlActionClass;
	}

	public void setCtrlActionClass(String ctrlActionClass) {
		this.ctrlActionClass = ctrlActionClass;
	}

	public ICtrlAction getCtrlAction() {
		return this.ctrlAction;
	}

	public void setCtrlAction(ICtrlAction ctrlAction) {
		this.ctrlAction = ctrlAction;
	}

	String tooltip = "";

	public String getTooltip() {
		return this.tooltip;
	}

	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}

	String shortCutKeys = "";

	public String getShortCutKeys() {
		return this.shortCutKeys;
	}

	public void setShortCutKeys(String shortCutKeys) {
		this.shortCutKeys = shortCutKeys;
	}

	String helpURL = "";

	public String getHelpURL() {
		return this.helpURL;
	}

	public void setHelpURL(String helpURL) {
		this.helpURL = helpURL;
	}

	String imageFile = "";

	public String getImageFile() {
		return this.imageFile;
	}

	public void setImageFile(String imageFile) {
		this.imageFile = imageFile;
	}

	String description = "";

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	protected Boolean canMerge = false;
	private XMLCommandBase baseContainer;

	public Boolean canMerge() {
		return this.canMerge;
	}

	public Boolean buildCtrlAction(IBaseItem caller) {
		Boolean result = false;
		try {
			setBaseContainer(findBaseContainer());
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return result;
	}

	public Boolean buildCtrlAction(IBaseItem caller, IForm associatedForm) {

		return this.ctrlAction != null;
	}

	public Boolean loadTooltipImage() {
		return false;
	}

	public Boolean loadImage() {
		return false;
	}

	public Boolean loadLargeImage() {
		return false;
	}

	public void merge(XMLCommand otherCommand) {
		// do nothing
	}

	/**
	 * 使用指定的parent构造一个跟自己一个类型的对象
	 * 
	 * @param parent
	 * @return
	 */
	private XMLCommandBase createNew(XMLCommandBase parent) {
		XMLCommand result = null;
		try {
			if (this instanceof XMLMenuCommand) {
				if (this instanceof XMLMenuButtonDropdown) {
					result = new XMLMenuButtonDropdown(this.getPluginInfo(), (XMLMenuGroup) parent);
				} else if (this instanceof XMLMenuButton) {
					result = new XMLMenuButton(this.getPluginInfo(), (XMLMenuGroup) parent);
				}
			} else if (this instanceof XMLButton) {
				result = new XMLButton(this.getPluginInfo(), (XMLCommand) parent);
			} else if (this instanceof XMLLabel) {
				result = new XMLLabel(this.getPluginInfo(), (XMLCommand) parent);
			} else if (this instanceof XMLTextbox) {
				result = new XMLTextbox(this.getPluginInfo(), (XMLCommand) parent);
			} else if (this instanceof XMLComboBox) {
				result = new XMLComboBox(this.getPluginInfo(), (XMLCommand) parent);
			} else if (this instanceof XMLSeparator) {
				result = new XMLSeparator(this.getPluginInfo(), (XMLCommand) parent);
			} else if (this instanceof XMLCheckBox) {
				result = new XMLCheckBox(this.getPluginInfo(), (XMLCommand) parent);

			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return result;
	}

	@Override
	public XMLCommandBase saveToPluginInfo(PluginInfo pluginInfo, XMLCommandBase parent) {
		XMLCommand result = null;
		return result;
	}

	@Override
	public void clearResources() {
		// do nothing
	}

	@Override
	public boolean containsAction(Type ctrlActionType) {
		Boolean result = false;
		return result;
	}

	protected void setAttributeCodeType(Element xmlElement) {
		// do nothing
	}

	@Override
	public XMLCommandBase clone(XMLCommandBase parent) {
		XMLCommand result = null;
		try {
			result = (XMLCommand) createNew(parent);

			result.setIsDesigning(this.getIsDesigning());
			result.setIndex(this.getIndex());
			result.setLabel(this.getLabel());
			result.setID(this.getID());
			result.setImageFile(this.getImageFile());
			result.setVisible(this.getVisible());
			result.setEnabled(this.getEnabled());
			result.setCtrlActionClass(this.getCtrlActionClass());
			result.setTooltip(this.getTooltip());
			result.setHelpURL(this.getHelpURL());
			result.setShortCutKeys(this.getShortCutKeys());
			result.setDescription(this.getDescription());
			result.setCustomProperty(this.getCustomProperty());
			result.setPlatform(getPlatform());
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return result;
	}

	@Override
	public int compare(XMLCommand o1, XMLCommand o2) {
		return o1.getIndex() - o2.getIndex();
	}

	@Override
	public int compareTo(XMLCommand o) {
		return this.getIndex() - o.getIndex();
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}
	@Override
	public int hashCode(){
		return super.hashCode();
	}

	public XMLCommandBase getBaseContainer() {
		return baseContainer;
	}

	public void setBaseContainer(XMLCommandBase baseContainer) {
		this.baseContainer = baseContainer;
	}
}
