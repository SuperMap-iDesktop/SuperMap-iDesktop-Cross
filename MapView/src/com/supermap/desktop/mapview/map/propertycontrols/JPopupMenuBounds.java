package com.supermap.desktop.mapview.map.propertycontrols;

import com.supermap.data.GeoCompound;
import com.supermap.data.GeoEllipse;
import com.supermap.data.GeoPie;
import com.supermap.data.GeoRegion;
import com.supermap.data.Geometry;
import com.supermap.data.Recordset;
import com.supermap.data.Rectangle2D;
import com.supermap.desktop.Application;
import com.supermap.desktop.FormMap;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.utilties.SystemPropertyUtilties;
import com.supermap.mapping.Layer;
import com.supermap.mapping.Layers;
import com.supermap.mapping.Map;
import com.supermap.ui.Action;
import com.supermap.ui.MapControl;
import com.supermap.ui.TrackMode;
import com.supermap.ui.TrackedEvent;
import com.supermap.ui.TrackedListener;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class JPopupMenuBounds extends JPopupMenu {

	private static final long serialVersionUID = 1L;

	private JMenuItem menuItemSelectTarget;
	private JMenuItem menuItemSelectRectangle;
	private JMenuItem menuItemSelectPolygon;
	private JMenuItem menuItemSelectSector;
	private JMenuItem menuItemSelectCircle;
	private JMenuItem menuItemClear;

	private JPanel panelSelectTargetInfo;
	private JLabel labelSelectTargetInfo;

	private String moduleName;

	private transient GeoRegion geoRegion;
	private transient Rectangle2D rectangle2d;

	private static final String SELECT_TARGET = "SelectTarget";
	private static final String SELECT_RECTANGLE = "SelectRectangle";
	private static final String SELECT_POLYGON = "SelectPolygon";
	private static final String SELECT_SECTOR = "SelectSector";
	private static final String SELECT_CIRCLE = "SelectCircle";
	private static final String CLEAR = "Clear";

	private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);

	public static final String CLIP_REGION = "ClipRegion";
	public static final String VIEW_BOUNDS_LOCKED = "ViewBoundsLocked";
	public static final String CUSTOM_BOUNDS = "CustomBounds";
	public static final int SEGMENTCOUNT = 50;

	private transient TrackedListener trackedListener = new TrackedListener() {

		@Override
		public void tracked(TrackedEvent arg0) {
			abstractTracked(arg0);
		}

	};

	private void abstractTracked(TrackedEvent arg0) {
		if (arg0.getGeometry() != null) {
			if (moduleName.equals(JPopupMenuBounds.CLIP_REGION)) {
				if (arg0.getGeometry() instanceof GeoRegion) {
					geoRegion = (GeoRegion) arg0.getGeometry().clone();
				} else if (arg0.getGeometry() instanceof GeoPie) {
					geoRegion = ((GeoPie) arg0.getGeometry()).convertToRegion(JPopupMenuBounds.SEGMENTCOUNT).clone();
				}
				changeSupport.firePropertyChange(moduleName, null, geoRegion);
			} else {
				rectangle2d = arg0.getGeometry().getBounds().clone();
				changeSupport.firePropertyChange(moduleName, null, rectangle2d);
			}
			((IFormMap) Application.getActiveApplication().getActiveForm()).getMapControl().removeTrackedListener(trackedListener);
			exitEdit();
		} else {
			((IFormMap) Application.getActiveApplication().getActiveForm()).getMapControl().addMouseListener(controlMouseListener);
		}
	}

	;

	private transient MouseListener controlMouseListener = new MouseAdapter() {

		@Override
		public void mouseClicked(MouseEvent e) {
			MapControl control = ((IFormMap) Application.getActiveApplication().getActiveForm()).getMapControl();
			if (e.getButton() == MouseEvent.BUTTON3) {
				control.removeMouseListener(this);
				exitEdit();
			}
		}
	};

	public JPopupMenuBounds(String moduleName, Rectangle2D rectangle2d) {
		super();
		this.moduleName = moduleName;
		this.rectangle2d = rectangle2d;
		initComponents();
		initListeners();
		initResources();
	}

	public JPopupMenuBounds(String moduleName, GeoRegion geoRegion) {
		super();
		this.moduleName = moduleName;
		this.geoRegion = geoRegion;
		initComponents();
		initListeners();
		initResources();
	}

	private void initComponents() {
		Dimension dimension = new Dimension(200, 20);

		panelSelectTargetInfo = new JPanel();
		if (SystemPropertyUtilties.isWindows()) {
			panelSelectTargetInfo.setSize(220, 30);
		} else {
			panelSelectTargetInfo.setSize(280, 30);
		}
		panelSelectTargetInfo.setBorder(new LineBorder(Color.LIGHT_GRAY));
		panelSelectTargetInfo.setBackground(new Color(255, 255, 255, 220));
		labelSelectTargetInfo = new JLabel("Select one or more geoRegion.");

		panelSelectTargetInfo.setLayout(new GridBagLayout());
		panelSelectTargetInfo.add(labelSelectTargetInfo, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 1).setFill(GridBagConstraints.BOTH));

		menuItemSelectTarget = new JMenuItem("SelcetTarget");
		menuItemSelectRectangle = new JMenuItem("SelectRectangle");
		menuItemSelectPolygon = new JMenuItem("SelectPolygon");
		menuItemSelectSector = new JMenuItem("SelectSector");
		menuItemSelectCircle = new JMenuItem("SelectCircle");
		menuItemClear = new JMenuItem("Clear");

		if (moduleName.equals(JPopupMenuBounds.VIEW_BOUNDS_LOCKED)) {
			menuItemClear.setText("SetMapBounds");
		}
		menuItemSelectTarget.setSize(dimension);
		menuItemSelectRectangle.setSize(dimension);
		menuItemSelectPolygon.setSize(dimension);
		menuItemSelectSector.setSize(dimension);
		menuItemSelectCircle.setSize(dimension);
		menuItemClear.setSize(dimension);

		this.add(menuItemSelectTarget);
		this.addSeparator();
		this.add(menuItemSelectRectangle);
		this.add(menuItemSelectPolygon);
		this.add(menuItemSelectSector);
		this.add(menuItemSelectCircle);
		this.addSeparator();
		this.add(menuItemClear);
	}

	private void initListeners() {
		menuItemSelectTarget.setActionCommand(JPopupMenuBounds.SELECT_TARGET);
		menuItemSelectRectangle.setActionCommand(JPopupMenuBounds.SELECT_RECTANGLE);
		menuItemSelectPolygon.setActionCommand(JPopupMenuBounds.SELECT_POLYGON);
		menuItemSelectSector.setActionCommand(JPopupMenuBounds.SELECT_SECTOR);
		menuItemSelectCircle.setActionCommand(JPopupMenuBounds.SELECT_CIRCLE);
		menuItemClear.setActionCommand(JPopupMenuBounds.CLEAR);

		menuItemSelectTarget.addActionListener(actionListener);
		menuItemSelectRectangle.addActionListener(actionListener);
		menuItemSelectPolygon.addActionListener(actionListener);
		menuItemSelectSector.addActionListener(actionListener);
		menuItemSelectCircle.addActionListener(actionListener);
		menuItemClear.addActionListener(actionListener);
	}

	private void initResources() {
		labelSelectTargetInfo.setText(MapViewProperties.getString("String_SelectOneOrMoreRegion"));

		menuItemSelectTarget.setText(MapViewProperties.getString("String_Button_SelectObject"));
		menuItemSelectRectangle.setText(MapViewProperties.getString("String_Button_DrawRectangle"));
		menuItemSelectPolygon.setText(MapViewProperties.getString("String_Button_DrawPolygon"));
		menuItemSelectSector.setText(MapViewProperties.getString("String_Button_DrawPie"));
		menuItemSelectCircle.setText(MapViewProperties.getString("String_Button_DrawCircle"));

		if (this.moduleName.equals(JPopupMenuBounds.CLIP_REGION)) {
			menuItemClear.setText(MapViewProperties.getString("String_Button_ClearClipRegion"));
		} else if (this.moduleName.equals(JPopupMenuBounds.VIEW_BOUNDS_LOCKED)) {
			menuItemClear.setText(MapViewProperties.getString("String_Button_ClearLockedViewBounds"));
		} else if (this.moduleName.equals(JPopupMenuBounds.CUSTOM_BOUNDS)) {
			menuItemClear.setText(MapViewProperties.getString("String_Button_ClearCustomBounds"));
		}
	}

	private transient ActionListener actionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			buttonClicked(e.getActionCommand());
		}
	};

	private void buttonClicked(String actionCommand) {

		if (actionCommand.equals(JPopupMenuBounds.CLEAR)) {
			if (this.moduleName.equals(JPopupMenuBounds.CLIP_REGION)) {
				clearCutBundsClicked();
			} else if (this.moduleName.equals(JPopupMenuBounds.VIEW_BOUNDS_LOCKED)) {
				setMapBoundsClicked();
			} else if (this.moduleName.equals(JPopupMenuBounds.CUSTOM_BOUNDS)) {
				clearFullShowBoundsClicked();
			}
		} else if (actionCommand.equals(JPopupMenuBounds.SELECT_TARGET)) {
			selectButtonClicked();
		} else {
			drawButtonClicked(actionCommand);
		}
	}

	/**
	 * 清除裁剪范围
	 */
	private void clearCutBundsClicked() {
		this.geoRegion.setEmpty();
		this.changeSupport.firePropertyChange(this.moduleName, null, this.geoRegion);
	}

	/**
	 * 设为地图范围
	 */
	private void setMapBoundsClicked() {
		Map activeMap = getCurrentMap();
		this.rectangle2d = activeMap.getBounds().clone();
		this.changeSupport.firePropertyChange(this.moduleName, null, this.rectangle2d);
	}

	private Map getCurrentMap() {
		MapControl activeMapControl = ((IFormMap) Application.getActiveApplication().getActiveForm()).getMapControl();
		return activeMapControl.getMap();
	}

	/**
	 * 清除全幅显示
	 */
	private void clearFullShowBoundsClicked() {
		Map currentMap = getCurrentMap();
		if (moduleName.equals(JPopupMenuBounds.VIEW_BOUNDS_LOCKED)) {
			if (currentMap != null && currentMap.getBounds().getHeight() > 0 && currentMap.getBounds().getWidth() > 0) {
				this.rectangle2d = currentMap.getBounds();
			} else {
				return;
			}
		} else {
			this.rectangle2d = Rectangle2D.getEMPTY();
		}
		this.changeSupport.firePropertyChange(this.moduleName, null, this.rectangle2d);
	}

	/**
	 * 绘制对象选择
	 *
	 * @param actionCommand
	 */
	private void drawButtonClicked(String actionCommand) {
		final MapControl activeMapControl = ((IFormMap) Application.getActiveApplication().getActiveForm()).getMapControl();
		activeMapControl.setTrackMode(TrackMode.TRACK);
		activeMapControl.setAction(getSuitableAction(actionCommand));
		activeMapControl.addMouseListener(controlMouseListener);
		activeMapControl.addTrackedListener(trackedListener);
	}

	/**
	 * 当前是否已选中对象
	 */
//	private boolean isSelect

	/**
	 * 选择面对象
	 */
	private void selectButtonClicked() {
		// TODO 如果已有选中对象则直接使用
//		if(is)
		final IFormMap activeForm = (IFormMap) Application.getActiveApplication().getActiveForm();
		final MapControl activeMapControl = activeForm.getMapControl();
		activeMapControl.setAction(Action.SELECT);
		activeMapControl.setLayout(null);
		activeMapControl.add(panelSelectTargetInfo);

		final MouseMotionListener mouseMotionListener = new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				Point point = e.getPoint();
				panelSelectTargetInfo.setLocation(point.x + 15, point.y);
				panelSelectTargetInfo.setVisible(true);
			}
		};
		activeMapControl.addMouseMotionListener(mouseMotionListener);
		//选择时不弹出右键菜单
		activeForm.dontShowPopupMenu();
		activeMapControl.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) {
					activeForm.showPopupMenu();
					activeMapControl.removeMouseMotionListener(mouseMotionListener);
					panelSelectTargetInfo.setVisible(false);
					abstractActiveMapcontrol(activeMapControl);
					exitEdit();
					((IFormMap) activeForm).getMapControl().removeMouseListener(this);
					((FormMap) Application.getActiveApplication().getActiveForm()).clearSelection();
				}
			}
		});
	}

	/**
	 * 得到复合对象的面对象
	 */

	private GeoRegion getCompoundGeoRegion(GeoCompound geoCompound) {
		GeoRegion geoRegionResult = new GeoRegion();
		GeoRegion geoRegionTemp = null;
		for (int i = 0; i < geoCompound.getPartCount(); i++) {
			Geometry geometry = geoCompound.getPart(i);
			if (geometry instanceof GeoPie) {
				geoRegionTemp = ((GeoPie) geometry).convertToRegion(JPopupMenuBounds.SEGMENTCOUNT);
			} else if (geometry instanceof GeoRegion) {
				geoRegionTemp = (GeoRegion) geometry;
			} else if (geometry instanceof GeoEllipse) {
				geoRegionTemp = ((GeoEllipse) geometry).convertToRegion(JPopupMenuBounds.SEGMENTCOUNT);
			}
		}
		if (geoRegionTemp != null && geoRegionTemp.getPartCount() > 0) {
			for (int j = 0; j < geoRegionTemp.getPartCount(); j++) {
				geoRegionResult.addPart(geoRegionTemp.getPart(j).clone());
			}
		}
		return geoRegionResult;
	}

	private void abstractActiveMapcontrol(final MapControl activeMapControl) {

		boolean isChanged = false;
		if (moduleName.equals(JPopupMenuBounds.CLIP_REGION)) {
			GeoRegion geoClipRegion = new GeoRegion();
			Layers layers = activeMapControl.getMap().getLayers();
			for (int i = 0; i < layers.getCount(); i++) {
				Layer layer = layers.get(i);
				if (!layer.isSelectable()) {
					continue;
				}
				Recordset recordset = layer.getSelection().toRecordset();
				for (int k = 0; k < recordset.getRecordCount(); k++, recordset.moveNext()) {
					Geometry geometry = recordset.getGeometry();
					GeoRegion geoRegionTemp = null;
					if (geometry instanceof GeoPie) {
						geoRegionTemp = ((GeoPie) geometry).convertToRegion(JPopupMenuBounds.SEGMENTCOUNT);
					} else if (geometry instanceof GeoRegion) {
						geoRegionTemp = (GeoRegion) geometry;
					} else if (geometry instanceof GeoEllipse) {
						geoRegionTemp = ((GeoEllipse) geometry).convertToRegion(JPopupMenuBounds.SEGMENTCOUNT);
					} else if (geometry instanceof GeoCompound) {
						geoRegionTemp = getCompoundGeoRegion((GeoCompound) geometry);
					}
					if (geoRegionTemp != null && geoRegionTemp.getPartCount() > 0) {
						for (int j = 0; j < geoRegionTemp.getPartCount(); j++) {
							geoClipRegion.addPart(geoRegionTemp.getPart(j).clone());
							isChanged = true;
						}
					}
				}
			}
//			System.out.println("Time:"+(System.currentTimeMillis() - startTime));
			if (isChanged) {
				JPopupMenuBounds.this.geoRegion = geoClipRegion;
				changeSupport.firePropertyChange(moduleName, null, JPopupMenuBounds.this.geoRegion);
			}
		} else {
			Rectangle2D rectangle2dResult = null;
			Layers layers = activeMapControl.getMap().getLayers();

			for (int i = 0; i < layers.getCount(); i++) {
				Layer layer = layers.get(i);
				if (!layer.isSelectable()) {
					continue;
				}
				Recordset recordset = layer.getSelection().toRecordset();
				for (int k = 0; k < recordset.getRecordCount(); k++, recordset.moveNext()) {
					Geometry geometry = recordset.getGeometry();
					if (geometry != null) {
						if (rectangle2dResult == null) {
							rectangle2dResult = geometry.getBounds().clone();
							isChanged = true;
						} else {
							rectangle2dResult.union(geometry.getBounds().clone());
							isChanged = true;
						}
					}
				}
			}
			if (isChanged && rectangle2dResult != null && rectangle2dResult.getHeight() > 0 && rectangle2dResult.getWidth() > 0) {
				JPopupMenuBounds.this.rectangle2d = rectangle2dResult;
				changeSupport.firePropertyChange(moduleName, null, JPopupMenuBounds.this.rectangle2d);
			}
		}
	}

	private Action getSuitableAction(String actionCommand) {
		Action result = Action.NULL;
		if (actionCommand.equals(JPopupMenuBounds.SELECT_TARGET)) {
			result = Action.SELECT;
		} else if (actionCommand.equals(JPopupMenuBounds.SELECT_RECTANGLE)) {
			result = Action.CREATERECTANGLE;
		} else if (actionCommand.equals(JPopupMenuBounds.SELECT_POLYGON)) {
			result = Action.CREATEPOLYGON;
		} else if (actionCommand.equals(JPopupMenuBounds.SELECT_SECTOR)) {
			result = Action.CREATEPIE;
		} else if (actionCommand.equals(JPopupMenuBounds.SELECT_CIRCLE)) {
			result = Action.CREATECIRCLE;
		}
		return result;
	}

	private void exitEdit() {
		MapControl activeMapControl = ((IFormMap) Application.getActiveApplication().getActiveForm()).getMapControl();
		activeMapControl.setAction(Action.SELECT2);
		activeMapControl.setTrackMode(TrackMode.EDIT);

	}

	public void setRectangle2D(Rectangle2D rectangle2d) {
		this.rectangle2d = rectangle2d;
	}

	public Rectangle2D getRectangle2D() {
		return this.rectangle2d;
	}

	public void setGeoRegion(GeoRegion geoRegion) {
		this.geoRegion = geoRegion;
	}

	public GeoRegion getGeoRegion() {
		return this.geoRegion;
	}

	public void addPropertyChangeListeners(PropertyChangeListener changeListener) {
		changeSupport.addPropertyChangeListener(changeListener);
	}

	public void removePropertyChangeListeners(PropertyChangeListener changeListener) {
		changeSupport.removePropertyChangeListener(changeListener);
	}
}
