package com.supermap.desktop;

import java.util.ArrayList;
import com.supermap.desktop.enums.XMLCommandType;
import com.supermap.desktop.ui.*;

public class PluginInfoCollection extends ArrayList<PluginInfo> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PluginInfoCollection() {
		// do nothing
	}

	private Boolean isDesigning;

	public Boolean getIsDesigning() {
		return this.isDesigning;
	}

	public void setIsDesigning(Boolean isDesigning) {
		this.isDesigning = isDesigning;
	}

	public Boolean getIsLoadded() {
		return this.xmlFrameMenus != null && this.xmlContextMenus != null && this.xmlStatusbars != null && this.xmlToolbars != null && this.xmlDockbars != null;
	}

	public Boolean isReadOnly() {
		return false;
	}

	private transient XMLMenus xmlFrameMenus = null;

	public XMLMenus getFrameMenus() {
		return this.xmlFrameMenus;
	}

	public void setFrameMenus(XMLMenus xmlFrameMenus) {
		this.xmlFrameMenus = xmlFrameMenus;
	}

	private transient XMLToolbars xmlToolbars = null;

	public XMLToolbars getToolbars() {
		return this.xmlToolbars;
	}

	public void setXMLToolbars(XMLToolbars xmlToolBars) {
		this.xmlToolbars = xmlToolBars;
	}

	private transient XMLStatusbars xmlStatusbars = null;

	public XMLStatusbars getStatusbars() {
		return this.xmlStatusbars;
	}

	public void setXMLStatusbars(XMLStatusbars xmlStatusbars) {
		this.xmlStatusbars = xmlStatusbars;
	}

	private transient XMLDockbars xmlDockbars = null;

	public XMLDockbars getDockbars() {
		return this.xmlDockbars;
	}

	public void setDockBars(XMLDockbars xmlDockbars) {
		this.xmlDockbars = xmlDockbars;
	}

	private transient XMLMenus xmlContextMenus = null;

	public XMLMenus getContextMenus() {
		return this.xmlContextMenus;
	}

	public void setXMLContextMenus(XMLMenus xmlContextMenus) {
		this.xmlContextMenus = xmlContextMenus;
	}

	public void mergeUIElements() {
		this.mergeFrameMenus();
		this.mergeContextMenus();
		this.mergeStatusbars();
		this.mergeToolBars();
		this.mergeDockBars();
	}

	public Boolean mergeFrameMenus() {
		this.xmlFrameMenus = new XMLMenus(XMLCommandType.FRAMEMENUS);

		for (int i = 0; i < this.size(); i++) {
			PluginInfo info = this.get(i);
			this.xmlFrameMenus.merge(info.getFrameMenus());
		}

		return true;
	}

	private Boolean mergeContextMenus() {
		this.xmlContextMenus = new XMLMenus(XMLCommandType.CONTEXTMENUS);

		for (int i = 0; i < this.size(); i++) {

			PluginInfo info = this.get(i);
			this.xmlContextMenus.merge(info.getContextMenus());
		}

		return true;
	}

	private Boolean mergeStatusbars() {
		this.xmlStatusbars = new XMLStatusbars();

		for (int i = 0; i < this.size(); i++) {
			PluginInfo info = this.get(i);
			this.xmlStatusbars.merge(info.getStatusbars());
		}
		return true;
	}

	private Boolean mergeToolBars() {
		this.xmlToolbars = new XMLToolbars();

		for (int i = 0; i < this.size(); i++) {

			PluginInfo info = this.get(i);
			this.xmlToolbars.merge(info.getToolbars());
		}

		return true;
	}

	private Boolean mergeDockBars() {
		this.xmlDockbars = new XMLDockbars();

		for (int i = 0; i < this.size(); i++) {
			PluginInfo pluginInfo = this.get(i);
			if (pluginInfo.getDockbars() != null) {
				this.xmlDockbars.merge(pluginInfo.getDockbars());
			}
		}

		this.xmlDockbars.sort();
		return true;
	}
}
