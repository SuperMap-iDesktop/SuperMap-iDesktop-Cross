package com.supermap.desktop.ui;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IXMLCreator;
import com.supermap.desktop.PluginInfo;
import com.supermap.desktop._XMLTag;
import com.supermap.desktop.enums.XMLCommandType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.lang.reflect.Type;
import java.util.ArrayList;

public abstract class XMLCommandBase extends _XMLTag {

	private PluginInfo pluginInfo = null;
	private boolean isDesigning = false;
	private int index = 0;
	private String id = "";
	private boolean visible = true;
	private String label = "";
	private String customProperty = "";

	protected XMLCommandType commandType = XMLCommandType.BUTTON;

	public XMLCommandBase() {
	}

	public XMLCommandBase(PluginInfo pluginInfo) {
		this.pluginInfo = pluginInfo;
	}

	public XMLCommandBase(XMLCommandBase parent) {
		this.parent = parent;
	}

	public XMLCommandBase(PluginInfo pluginInfo, XMLCommandBase parent) {
		this.pluginInfo = pluginInfo;
		this.parent = parent;
	}

	public boolean initialize(Element element) {
		// id
		try {
			this.setID(element.getAttribute(g_AttributionID));
		} catch (Exception ex) {
			// do nothing
		}

		// label
		try {
			this.label = element.getAttribute(g_AttributionLabel);
		} catch (Exception ex) {
			// do nothing
		}

		// index
		try {
			String attribute = element.getAttribute(g_AttributionIndex);
			this.setIndex(Integer.valueOf(attribute));
		} catch (Exception ex) {
			// do nothing
		}

		// visible
		try {
			String attribute = element.getAttribute(g_AttributionVisible);
			this.visible = !attribute.equalsIgnoreCase(g_ValueFalse);
		} catch (Exception ex) {
			// do nothing
		}

		// CustomProperty
		try {
			this.customProperty = element.getAttribute(g_AttributionCustomProperty);
		} catch (Exception ex) {
			// do nothing
		}

		return true;
	}

	public PluginInfo getPluginInfo() {
		return this.pluginInfo;
	}

	public void setPluginInfo(PluginInfo pluginInfo) {
		this.pluginInfo = pluginInfo;
	}

	public boolean getIsDesigning() {
		return this.isDesigning;
	}

	public void setIsDesigning(boolean isDesigning) {
		this.isDesigning = isDesigning;
	}

	public int getIndex() {
		return this.index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getID() {
		return this.id;
	}

	public void setID(String id) {
		this.id = id;
	}

	public boolean getVisible() {
		return this.visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public boolean getIsContainer() {
		return true;
	}

	public String getCustomProperty() {
		return this.customProperty;
	}

	public void setCustomProperty(String customProperty) {
		this.customProperty = customProperty;
	}

	protected XMLCommandBase parent = null;

	public XMLCommandBase getParent() {
		return this.parent;
	}

	public XMLCommandType getCommandType() {
		return this.commandType;
	}

	public IXMLCreator getXMLCreator() {
		return null;
	}

	public void remove() {
		// do nothing
	}

	public XMLCommandBase createElement(XMLCommandType commandType) {
		return null;
	}

	/**
	 * 拷贝一份自己，包括自己的子节点也执行拷贝, 并添加为指定元素的子项
	 * 
	 * @param parent 指定父节点，拷贝之后可能会改变父节点
	 * @return
	 */
	public XMLCommandBase copyTo(XMLCommandBase parent) {
		XMLCommandBase result = null;

		try {
			result = clone(parent);
			if (result != null) {
				parent.addSubItem(result);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return result;
	}

	/**
	 * 改变自己的父节点, 插入到指定父元素的指定子项之后, 由于XMLCommand集合的Index、XMLCommand本身的Index不同, 而XMLCommand本身的Index还可以相同，因此不使用Index来作为, 插入位置，插入过程需要自动对集合中的XMLCommand本身的Index做处理
	 * 
	 * @param preCommand
	 * @param subCommand
	 * @return
	 */
	public XMLCommandBase Insert(XMLCommandBase preCommand, XMLCommandBase subCommand) {
		XMLCommandBase result = null;
		return result;
	}

	/**
	 * 查找承载此元素的根容器
	 * 
	 * @return
	 */
	public XMLCommandBase findBaseContainer() {
		XMLCommandBase result = null;
		return result;
	}

	public XMLCommandBase clone(XMLCommandBase parent) {
		return null;
	}

	public void insertSubItem(XMLCommandBase preItem, XMLCommandBase insertItem) {

	}

	/**
	 * 添加子项, 内部仅根据Command的IIndex加顺序进行处理, 不处理PluginInfoList
	 * 
	 * @param subItem
	 */
	public void addSubItem(XMLCommandBase subItem) {

	}

	/**
	 * 将自己从所父节点中移除
	 * 
	 * @return
	 */
	protected boolean doRemove() {
		return false;
	}

	public XMLCommandBase saveToPluginInfo(PluginInfo pluginInfo, XMLCommandBase parent) {
		return null;
	}

	/**
	 * 清理资源（图片等）,在进行拖拽操作的时候，比如拖拽一个Tab, 实现过程中，XMLRibbonTab并没有拷贝, 而是更改了父节点，插入到了新的父节点子项中, 于是拖拽之后XMLRibbonTab不再使用了, 但是由于有些独占的资源（比如图片等）已经构造, 于是在拖拽操作之后，保存修改再次打开界面设计器,
	 * 会有各种奇怪的现象，比如无法调试跟踪的异常, 所以需要在一些特殊情况下，手动清理独占资源
	 */
	public void clearResources() {

	}

	public Element toXML(Document document) {
		return null;
	}

	public String getEnabledKey(String key) {
		String enabledKey = "";
		try {
			if (this.getXMLCreator() != null && this.getXMLCreator().getDefaultValueCreator() != null) {
				enabledKey = this.getXMLCreator().getDefaultValueCreator().getDefaultID(key);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return enabledKey;
	}

	/**
	 * 为RibbonManager服务, 现在RibbonTab在使用到的时候才加载, 因此在使用RibbonManager查找Item的时候, 需要使用这个方法先判断对应的Tab下是否, 存在指定Type CtrlAction的XMLCommand, 有则Build一下并继续后面的操作,
	 * 后续可以有计划的从结构上优化RibbonManger的内容。
	 * 
	 * @param ctrlActionType
	 * @return
	 */
	public boolean containsAction(Type ctrlActionType) {
		return false;
	}

	public <T extends XMLCommand> void insertCommand(T command, ArrayList<T> commands) {
		if (command != null && commands != null) {
			int insertPos = commands.size();
			for (int i = commands.size() - 1; i >= 0; i--) {
				if (commands.get(i).getIndex() > command.getIndex()) {
					insertPos--;
				} else {
					break;
				}
			}

			if (insertPos < 0 || insertPos == commands.size()) {
				commands.add(command);
			} else {
				commands.add(insertPos, command);
			}
		}
	}
}
