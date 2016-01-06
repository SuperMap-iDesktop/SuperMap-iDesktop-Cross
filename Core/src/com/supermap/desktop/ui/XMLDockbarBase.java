package com.supermap.desktop.ui;

import java.awt.Dimension;
import java.awt.Point;

import org.w3c.dom.Element;

import com.supermap.desktop.PluginInfo;
import com.supermap.desktop.enums.DockSite;
import com.supermap.desktop.enums.DockState;

public class XMLDockbarBase extends XMLCommand {
	private static final String SPLIT_COMMA = ",";

	private DockSite dockSite = DockSite.LEFT;
	private DockState dockState = DockState.DOCK;
	private Point floatingLoation = new Point();
	private Dimension paneSize = new Dimension();
	private boolean isAutoHide = false;

	public XMLDockbarBase(PluginInfo pluginInfo, XMLCommandBase group) {
		super(pluginInfo, group);
	}

	@Override
	public boolean initialize(Element element) {
		boolean result = false;

		result = super.initialize(element);
		try {
			this.dockSite = DockSite.getDockSite(element.getAttribute(g_AttributionDocksite));
		} catch (Exception e) {
			// TODO: handle exception
		}

		try {
			this.dockState = DockState.getDockState(element.getAttribute(g_AttributionDockstate));
		} catch (Exception e) {
			// TODO: handle exception
		}

		try {
			this.floatingLoation = getPoint(element.getAttribute(g_AttributionFloatingLocation));
		} catch (Exception e) {
			// TODO: handle exception
		}

		try {
			this.paneSize = getDimension(element.getAttribute(g_AttributionSize));
		} catch (Exception e) {
			// TODO: handle exception
		}

		try {
			this.isAutoHide = Boolean.parseBoolean(element.getAttribute(g_AttributionAutoHide));
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}

	public DockSite getDockSite() {
		return dockSite;
	}

	public void setDockSite(DockSite dockSite) {
		this.dockSite = dockSite;
	}

	public DockState getDockState() {
		return dockState;
	}

	public void setDockState(DockState dockState) {
		this.dockState = dockState;
	}

	public Point getFloatingLoation() {
		return floatingLoation;
	}

	public void setFloatingLoation(Point floatingLoation) {
		this.floatingLoation = floatingLoation;
	}

	public Dimension getPaneSize() {
		return paneSize;
	}

	public void setPaneSize(Dimension paneSize) {
		this.paneSize = paneSize;
	}

	public boolean isAutoHide() {
		return isAutoHide;
	}

	public void setAutoHide(boolean isAutoHide) {
		this.isAutoHide = isAutoHide;
	}

	@Override
	public XMLCommandBase clone(XMLCommandBase parent) {
		XMLDockbarBase result = null;

		try {
			result = (XMLDockbarBase) createNew(parent);
			if (result != null) {
				result.setID(this.getID());
				result.setDockState(this.getDockState());
				result.setVisible(this.getVisible());
				result.setDockSite(this.getDockSite());
				result.setFloatingLoation(this.getFloatingLoation());
				result.setPaneSize(this.getPaneSize());
				result.setAutoHide(this.isAutoHide());
				result.setCustomProperty(this.getCustomProperty());
				result.setLabel(this.getLabel());
				result.setTooltip(this.getTooltip());
				result.setIndex(this.getIndex());
			}
		} catch (Exception e) {
			result = null;
		}
		return result;
	}

	@Override
	public XMLCommandBase saveToPluginInfo(PluginInfo pluginInfo, XMLCommandBase parent) {
		XMLDockbarBase result = null;

		try {
			if (this.getPluginInfo() == pluginInfo) {
				result = (XMLDockbarBase) createNew(pluginInfo, parent);
			}

			if (result != null) {
				result.setID(this.getID());
				result.setDockSite(this.getDockSite());
				result.setVisible(this.getVisible());
				result.setDockState(this.getDockState());
				result.setFloatingLoation(this.getFloatingLoation());
				result.setPaneSize(this.getPaneSize());
				result.setAutoHide(this.isAutoHide);
				result.setCustomProperty(this.getCustomProperty());
				result.setIndex(this.getIndex());
			}
		} catch (Exception e) {
			result = null;
		}
		return result;
	}

	public static String convertToString(Dimension size) {
		String result = null;

		try {
			result = Double.toString(size.getWidth()) + SPLIT_COMMA + Double.toString(size.getHeight());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}

	public static String convertToString(Point point) {
		String result = null;

		try {
			result = Double.toString(point.getX()) + SPLIT_COMMA + Double.toString(point.getY());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}

	@Override
	protected boolean doRemove() {
		boolean result = false;

		try {
			if (this.getParent() instanceof XMLDockbars) {
				result = ((XMLDockbars) this.getParent()).getDockbars().remove(this);
			} else if (this.getParent() instanceof XMLDockbarGroup) {
				result = ((XMLDockbarGroup) this.getParent()).getDockBars().remove(this);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}

	/**
	 * 创建一个新的对象
	 * 
	 * @param parent 指定的父容器
	 * @return
	 */
	protected XMLCommandBase createNew(XMLCommandBase parent) {
		XMLCommandBase result = null;

		if (this instanceof XMLDockbar) {
			result = new XMLDockbar(this.getPluginInfo(), parent);
		}
		return result;
	}

	protected XMLCommandBase createNew(PluginInfo pluginInfo, XMLCommandBase parent) {
		XMLCommandBase result = null;

		if (this instanceof XMLDockbar) {
			result = new XMLDockbar(pluginInfo, parent);
		}
		return result;
	}

	private Point getPoint(String pointString) {
		Point point = new Point();

		try {
			if (!pointString.isEmpty()) {
				String[] strArray = pointString.split(SPLIT_COMMA);
				if (strArray.length == 2) {
					int pointX = Integer.parseInt(strArray[0]);
					int pointY = Integer.parseInt(strArray[1]);
					point = new Point(pointX, pointY);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return point;
	}

	private Dimension getDimension(String dimensionString) {
		Dimension dimension = new Dimension();

		try {
			if (!dimensionString.isEmpty()) {
				String[] strArray = dimensionString.split(SPLIT_COMMA);
				if (strArray.length == 2) {
					int width = Integer.parseInt(strArray[0]);
					int height = Integer.parseInt(strArray[1]);
					dimension = new Dimension(width, height);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return dimension;
	}
}
