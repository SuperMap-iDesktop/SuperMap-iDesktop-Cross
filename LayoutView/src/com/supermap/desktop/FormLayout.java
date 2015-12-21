package com.supermap.desktop;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Icon;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;

import com.supermap.data.Geometry;
import com.supermap.data.GeometryType;
import com.supermap.data.Rectangle2D;
import com.supermap.data.Workspace;
import com.supermap.desktop.Interface.IContextMenuManager;
import com.supermap.desktop.Interface.IFormLayout;
import com.supermap.desktop.dialog.DialogSaveAsLayout;
import com.supermap.desktop.enums.WindowType;
import com.supermap.desktop.ui.FormBaseChild;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.layout.LayoutElements;
import com.supermap.layout.LayoutSelection;
import com.supermap.ui.Action;
import com.supermap.ui.MapLayoutControl;

public class FormLayout extends FormBaseChild implements IFormLayout {

	private MapLayoutControl mapLayoutControl = null;
	private String title = "";
	JScrollPane jScrollPaneChildWindow = null;

	// 布局窗口右键菜单
	private JPopupMenu formLayoutContextMenu;

	public JPopupMenu getFormLayoutContextMenu() {
		return formLayoutContextMenu;
	}

	// 复合布局元素菜单
	private JPopupMenu layoutElementsContextMenu;

	public JPopupMenu getLayoutElementsContextMenu() {
		return layoutElementsContextMenu;
	}

	// 对象右键菜单
	private JPopupMenu layoutGeometryContextMenu;

	public JPopupMenu getLayoutGeometryContextMenu() {
		return layoutGeometryContextMenu;
	}

	private JPopupMenu layoutGeopictureContextMenu;

	public JPopupMenu getLayoutGeopictureContextMenu() {
		return layoutGeopictureContextMenu;
	}

	private JPopupMenu layoutTextObjContextMenu;

	public JPopupMenu getLayoutTextObjContextMenu() {
		return layoutTextObjContextMenu;
	}

	private JPopupMenu layoutMapObjContextMenu;

	public JPopupMenu getLayoutMapObjContextMenu() {
		return layoutMapObjContextMenu;
	}

	private JPopupMenu layoutMapLegendObjContextMenu;

	public JPopupMenu getLayoutMapLegendObjContextMenu() {
		return layoutMapLegendObjContextMenu;
	}

	private JPopupMenu layoutMapScaleObjContextMenu;

	public JPopupMenu getLayoutMapScaleObjContextMenu() {
		return layoutMapScaleObjContextMenu;
	}

	private JPopupMenu layoutMapNorthArrowObjContextMenu;

	public JPopupMenu getLayoutMapNorthArrowObjContextMenuMap() {
		return layoutMapNorthArrowObjContextMenu;
	}

	private JPopupMenu layoutGeoArtTextObjContextMenu;

	public JPopupMenu getLayoutGeoArtTextObjContextMenu() {
		return this.layoutGeoArtTextObjContextMenu;
	}

	private transient MouseListener layoutControl_MouseListener = new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
			int buttonType = e.getButton();
			int clickCount = e.getClickCount();

			if ((buttonType == MouseEvent.BUTTON3 && clickCount == 1)
					&& (getMapLayoutControl().getLayoutAction() == Action.SELECT || getMapLayoutControl().getLayoutAction() == Action.SELECT2 || getMapLayoutControl()
							.getLayoutAction() == Action.SELECTCIRCLE)) {
				showPopupMenu(e);
			}
		}
	};

	public FormLayout() {
		this("");
	}

	public FormLayout(String name) {
		this(name, null, null);
	}

	public FormLayout(String title, Icon icon, Component component) {
		super(title, icon, component);

		this.title = title;
		this.mapLayoutControl = new MapLayoutControl();
		this.mapLayoutControl.getMapLayout().setWorkspace(Application.getActiveApplication().getWorkspace());
		jScrollPaneChildWindow = new JScrollPane(this.mapLayoutControl);
		this.setComponent(jScrollPaneChildWindow);

		this.mapLayoutControl.addMouseListener(layoutControl_MouseListener);

		if (Application.getActiveApplication().getMainFrame() != null) {
			IContextMenuManager manager = Application.getActiveApplication().getMainFrame().getContextMenuManager();
			this.formLayoutContextMenu = (JPopupMenu) manager.get("SuperMap.Desktop._FormLayout.FormLayoutContextMenu");
			this.layoutElementsContextMenu = (JPopupMenu) manager.get("SuperMap.Desktop._FormLayout.LayoutElementsContextMenu");
			this.layoutGeometryContextMenu = (JPopupMenu) manager.get("SuperMap.Desktop._FormLayout.LayoutGeometryContextMenu");
			this.layoutGeopictureContextMenu = (JPopupMenu) manager.get("SuperMap.Desktop._FormLayout.LayoutGeopictureContextMenu");
			this.layoutTextObjContextMenu = (JPopupMenu) manager.get("SuperMap.Desktop._FormLayout.LayoutTextObjContextMenu");
			this.layoutMapObjContextMenu = (JPopupMenu) manager.get("SuperMap.Desktop._FormLayout.LayoutMapObjContextMenu");
			this.layoutMapLegendObjContextMenu = (JPopupMenu) manager.get("SuperMap.Desktop._FormLayout.LayoutMapLegendObjContextMenu");
			this.layoutMapScaleObjContextMenu = (JPopupMenu) manager.get("SuperMap.Desktop._FormLayout.LayoutMapScaleObjContextMenu");
			this.layoutMapNorthArrowObjContextMenu = (JPopupMenu) manager.get("SuperMap.Desktop._FormLayout.LayoutMapNorthArrowObjContextMenuMap");
			this.layoutGeoArtTextObjContextMenu = (JPopupMenu) manager.get("SuperMap.Desktop._FormLayout.LayoutGeoArtTextObjContextMenu");
		}
	}

	@Override
	public String getText() {
		return this.title;
	}

	@Override
	public void setText(String text) {
		this.title = text;
	}

	@Override
	public WindowType getWindowType() {
		return WindowType.LAYOUT;
	}

	@Override
	public MapLayoutControl getMapLayoutControl() {
		return this.mapLayoutControl;
	}

	@Override
	public boolean save() {
		Boolean result = false;
		try {
			if (this.isNeedSave()) {
				Workspace workspace = this.mapLayoutControl.getMapLayout().getWorkspace();

				if (workspace.getLayouts().indexOf(this.getText()) >= 0) {
					result = workspace.getLayouts().setLayoutXML(this.getText(), this.mapLayoutControl.getMapLayout().toXML());
				} else {
					result = save(true, true);
				}

				if (result) {
					this.mapLayoutControl.getMapLayout().setModified(false);
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return result;
	}

	@Override
	public boolean save(boolean notify, boolean isNewWindow) {
		Boolean result = false;
		try {
			if (this.isNeedSave()) {
				Workspace workspace = this.mapLayoutControl.getMapLayout().getWorkspace();
				if (workspace != null) {
					if (notify) {
						result = this.saveAs(isNewWindow);
					} else {
						result = workspace.getLayouts().add(this.getText(), this.mapLayoutControl.getMapLayout().toXML()) >= 0;
					}
				}

				if (result) {
					this.mapLayoutControl.getMapLayout().setModified(false);
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return result;
	}

	@Override
	public boolean saveAs(boolean isNewWindow) {
		boolean result = false;
		try {
			Workspace workspace = this.mapLayoutControl.getMapLayout().getWorkspace();
			DialogSaveAsLayout dialogSaveAs = new DialogSaveAsLayout();
			dialogSaveAs.setLayouts(workspace.getLayouts());
			dialogSaveAs.setLayoutName(this.getText());
			dialogSaveAs.setIsNewWindow(isNewWindow);

			if (dialogSaveAs.showDialog() == DialogResult.YES) {
				this.mapLayoutControl.getMapLayout().setName(dialogSaveAs.getLayoutName());
				result = workspace.getLayouts().add(dialogSaveAs.getLayoutName(), this.mapLayoutControl.getMapLayout().toXML()) >= 0;
				if (result) {
					this.setText(dialogSaveAs.getLayoutName());
				}
			} else {
				result = false;
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return result;
	}

	@Override
	public boolean isNeedSave() {
		return this.mapLayoutControl.getMapLayout().isModified();
	}

	@Override
	public void setNeedSave(boolean needSave) {
		this.mapLayoutControl.getMapLayout().setModified(needSave);
	}

	@Override
	public boolean saveFormInfos() {
		// TODO Auto-generated method stub
		return false;
	}

	public void geometryViewEntire() {
		Geometry geoSelected = null;
		try {
			Rectangle2D rcViewBounds = Rectangle2D.getEMPTY();
			LayoutSelection selection = this.getMapLayoutControl().getMapLayout().getSelection();
			LayoutElements elements = this.getMapLayoutControl().getMapLayout().getElements();
			for (int i = 0; i < selection.getCount(); i++) {
				elements.seekID(selection.get(i));
				geoSelected = elements.getGeometry();
				if (rcViewBounds.isEmpty()) {
					rcViewBounds = geoSelected.getBounds();
				} else {
					rcViewBounds.union(geoSelected.getBounds());
				}
			}
			if (rcViewBounds != Rectangle2D.getEMPTY()) {
				this.getMapLayoutControl().getMapLayout().setViewBounds(rcViewBounds);
				this.getMapLayoutControl().getMapLayout().refresh();
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		if (geoSelected != null) {
			geoSelected.dispose();
		}
	}

	private void showPopupMenu(MouseEvent e) {
		Geometry geoSelElement = null;
		try {
			JPopupMenu contextMenuStrip = null;
			int selCount = this.getMapLayoutControl().getMapLayout().getSelection().getCount();
			if (selCount > 0) {
				this.getMapLayoutControl().getMapLayout().getElements().seekID(this.getMapLayoutControl().getMapLayout().getSelection().get(0));
				geoSelElement = this.getMapLayoutControl().getMapLayout().getElements().getGeometry();
			}

			Boolean bSameType = true;
			if (geoSelElement == null) {
				contextMenuStrip = formLayoutContextMenu;
				bSameType = false;
			} else if (selCount > 1) {
				contextMenuStrip = layoutGeometryContextMenu;
				for (int i = 1; i < selCount; i++) {
					this.getMapLayoutControl().getMapLayout().getElements().seekID(this.getMapLayoutControl().getMapLayout().getSelection().get(i));
					Geometry geoElement = this.getMapLayoutControl().getMapLayout().getElements().getGeometry();
					if (geoSelElement.getType() != geoElement.getType()) {
						if (geoSelElement.getType() == GeometryType.GEOMAP || geoSelElement.getType() == GeometryType.GEOLEGEND
								|| geoSelElement.getType() == GeometryType.GEOMAPSCALE || geoSelElement.getType() == GeometryType.GEONORTHARROW) {
							contextMenuStrip = layoutElementsContextMenu;
							bSameType = false;
							break;
						} else if (geoElement.getType() == GeometryType.GEOMAP || geoElement.getType() == GeometryType.GEOLEGEND
								|| geoElement.getType() == GeometryType.GEOMAPSCALE || geoElement.getType() == GeometryType.GEONORTHARROW) {
							contextMenuStrip = layoutElementsContextMenu;
							bSameType = false;
							break;
						}
					}
				}
			}
			// else
			if (bSameType) {
				if (geoSelElement.getType() == GeometryType.GEOTEXT) {
					contextMenuStrip = layoutTextObjContextMenu;
				} else if (geoSelElement.getType() == GeometryType.GEOMAP) {
					contextMenuStrip = layoutMapObjContextMenu;
				} else if (geoSelElement.getType() == GeometryType.GEOLEGEND) {
					contextMenuStrip = layoutMapLegendObjContextMenu;
				} else if (geoSelElement.getType() == GeometryType.GEOMAPSCALE) {
					contextMenuStrip = layoutMapScaleObjContextMenu;
				} else if (geoSelElement.getType() == GeometryType.GEONORTHARROW) {
					contextMenuStrip = layoutMapNorthArrowObjContextMenu;
				} else if (geoSelElement.getType() == GeometryType.GEOARC) {
					contextMenuStrip = layoutGeoArtTextObjContextMenu;
				} else if (geoSelElement.getType() == GeometryType.GEOPICTURE) {
					contextMenuStrip = layoutGeopictureContextMenu;
				} else {
					contextMenuStrip = layoutGeometryContextMenu;
				}
			}

			contextMenuStrip.show((Component) this.getMapLayoutControl(), (int) e.getPoint().getX(), (int) e.getPoint().getY());
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		if (geoSelElement != null) {
			geoSelElement.dispose();
			geoSelElement = null;
		}
	}
}
