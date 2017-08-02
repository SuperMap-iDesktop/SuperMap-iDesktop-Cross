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
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.utilities.MapUtilities;
import com.supermap.mapping.Layer;
import com.supermap.mapping.Layers;
import com.supermap.mapping.Map;
import com.supermap.ui.Action;
import com.supermap.ui.MapControl;
import com.supermap.ui.TrackMode;
import com.supermap.ui.TrackedEvent;
import com.supermap.ui.TrackedListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 重构
 * 自定义JMenuItem的添加
 * 修改自定义范围PopupMenu，目前实现：
 * 增添构造方法，将用到此PopupMenu的窗体，作为参数传入，实现当点击了相应的MenuItem，隐藏主窗体，当结束编辑时，再show出主窗体
 * 待优化：传入窗体的方式，破坏了JPopupMenuBounds类的封装性，虽然可以满足功能的要求，但并不是最好的解决方式，有待优化--yuanR 2017.3.20
 * <p>
 * 遇到的问题：
 * 将  dialog.setVisible(true);放置在绘制事件结束，会导致无法删除绘制出的矩形框，原因可能是：线程冲突，待解决
 */
public class JPopupMenuBounds extends JPopupMenu {

	private static final long serialVersionUID = 1L;

	private JMenuItem menuItemSelectTarget;
	private JMenuItem menuItemSelectRectangle;
	private JMenuItem menuItemSelectPolygon;
	private JMenuItem menuItemSelectSector;
	private JMenuItem menuItemSelectCircle;
	private JMenuItem menuItemClear;

	private ArrayList<String> menuItemsText;

	private MapActionSelectTargetInfoPanel panelSelectTargetInfo;

	private String moduleName;

	private transient GeoRegion geoRegion;
	private transient Rectangle2D rectangle2d;
	private java.util.Map<Layer, List<Geometry>> selectedGeometryAndLayer = new HashMap<>();

	private SmDialog dialog;

	private static final String SELECT_TARGET = "SelectTarget";
	private static final String SELECT_RECTANGLE = "SelectRectangle";
	private static final String SELECT_POLYGON = "SelectPolygon";
	private static final String SELECT_SECTOR = "SelectSector";
	private static final String SELECT_CIRCLE = "SelectCircle";
	private static final String CLEAR = "Clear";

	private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
	private String currentActionCommand="";

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
			if (this.moduleName.equals(JPopupMenuBounds.CLIP_REGION)) {
				if (arg0.getGeometry() instanceof GeoRegion) {
					this.geoRegion = (GeoRegion) arg0.getGeometry().clone();
				} else if (arg0.getGeometry() instanceof GeoPie) {
					this.geoRegion = ((GeoPie) arg0.getGeometry()).convertToRegion(JPopupMenuBounds.SEGMENTCOUNT).clone();
				}
				this.changeSupport.firePropertyChange(this.moduleName, null, this.geoRegion);
			} else {
				this.rectangle2d = arg0.getGeometry().getBounds().clone();
				this.changeSupport.firePropertyChange(this.moduleName, null, this.rectangle2d);
			}
			((IFormMap) Application.getActiveApplication().getActiveForm()).getMapControl().removeTrackedListener(this.trackedListener);
			exitEdit();
		} else {
			((IFormMap) Application.getActiveApplication().getActiveForm()).getMapControl().addMouseListener(this.controlMouseListener);
			((IFormMap) Application.getActiveApplication().getActiveForm()).getMapControl().addKeyListener(this.controlKeyListener);
		}
	}

	/**
	 *
	 */
	private KeyListener controlKeyListener = new KeyAdapter() {
		@Override
		public void keyTyped(KeyEvent e) {

			if (e.getKeyCode() == 0) {
				if (flag == 1) {
					flag--;
				}
			}
		}
	};

	private int flag;

	private MouseListener controlMouseListener = new MouseAdapter() {

		@Override
		public void mousePressed(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON3) {
				doSome();
			} else if (e.getButton() == MouseEvent.BUTTON1) {
				flag++;
				if (flag == 2) {
					doSome();
				}
			}
		}

		private void doSome() {
			exitEdit();
			//设置完之后，show出主窗体
			if (dialog != null) {
				dialog.setVisible(true);
			}
			((IFormMap) Application.getActiveApplication().getActiveForm()).getMapControl().removeMouseListener(controlMouseListener);
			((IFormMap) Application.getActiveApplication().getActiveForm()).getMapControl().removeKeyListener(controlKeyListener);
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


	public JPopupMenuBounds(SmDialog smDialog, String moduleName, Rectangle2D rectangle2d) {
		super();
		this.dialog = smDialog;
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

		this.panelSelectTargetInfo = new MapActionSelectTargetInfoPanel();

		this.menuItemSelectTarget = new JMenuItem("SelcetTarget");
		this.menuItemSelectRectangle = new JMenuItem("SelectRectangle");
		this.menuItemSelectPolygon = new JMenuItem("SelectPolygon");
		this.menuItemSelectSector = new JMenuItem("SelectSector");
		this.menuItemSelectCircle = new JMenuItem("SelectCircle");
		this.menuItemClear = new JMenuItem("Clear");
		// 构建可以指定特定显示项的数组，，，默认为全部
		this.menuItemsText = new ArrayList<>();

		if (this.moduleName.equals(JPopupMenuBounds.VIEW_BOUNDS_LOCKED)) {
			this.menuItemClear.setText("SetMapBounds");
		}
		this.menuItemSelectTarget.setSize(dimension);
		this.menuItemSelectRectangle.setSize(dimension);
		this.menuItemSelectPolygon.setSize(dimension);
		this.menuItemSelectSector.setSize(dimension);
		this.menuItemSelectCircle.setSize(dimension);
		this.menuItemClear.setSize(dimension);

		setPopupItems(this.menuItemsText);
	}

	/**
	 * 自定义显示的项--yuanR 2017.3.20
	 *
	 * @param arrayList
	 */
	public void setPopupItems(ArrayList arrayList) {
		this.removeAll();
		this.menuItemsText = arrayList;
		if (this.menuItemsText.size() <= 0) {
			this.add(this.menuItemSelectTarget);
			this.addSeparator();
			this.add(this.menuItemSelectRectangle);
			this.add(this.menuItemSelectPolygon);
			this.add(this.menuItemSelectSector);
			this.add(this.menuItemSelectCircle);
			this.addSeparator();
			this.add(this.menuItemClear);
		} else {
			for (int i = 0; i < this.menuItemsText.size(); i++) {
				if (this.menuItemsText.get(i).equals(MapViewProperties.getString("String_Button_SelectObject"))) {
					this.add(this.menuItemSelectTarget);
				} else if (this.menuItemsText.get(i).equals(MapViewProperties.getString("String_Button_DrawRectangle"))) {
					this.add(this.menuItemSelectRectangle);
				} else if (this.menuItemsText.get(i).equals(MapViewProperties.getString("String_Button_DrawPolygon"))) {
					this.add(this.menuItemSelectPolygon);
				} else if (this.menuItemsText.get(i).equals(MapViewProperties.getString("String_Button_DrawPie"))) {
					this.add(this.menuItemSelectSector);
				} else if (this.menuItemsText.get(i).equals(MapViewProperties.getString("String_Button_DrawCircle"))) {
					this.add(this.menuItemSelectCircle);
				}
			}
		}
	}

	private void initListeners() {
		this.menuItemSelectTarget.setActionCommand(JPopupMenuBounds.SELECT_TARGET);
		this.menuItemSelectRectangle.setActionCommand(JPopupMenuBounds.SELECT_RECTANGLE);
		this.menuItemSelectPolygon.setActionCommand(JPopupMenuBounds.SELECT_POLYGON);
		this.menuItemSelectSector.setActionCommand(JPopupMenuBounds.SELECT_SECTOR);
		this.menuItemSelectCircle.setActionCommand(JPopupMenuBounds.SELECT_CIRCLE);
		this.menuItemClear.setActionCommand(JPopupMenuBounds.CLEAR);

		this.menuItemSelectTarget.addActionListener(actionListener);
		this.menuItemSelectRectangle.addActionListener(actionListener);
		this.menuItemSelectPolygon.addActionListener(actionListener);
		this.menuItemSelectSector.addActionListener(actionListener);
		this.menuItemSelectCircle.addActionListener(actionListener);
		this.menuItemClear.addActionListener(actionListener);
	}

	private void initResources() {
		this.menuItemSelectTarget.setText(MapViewProperties.getString("String_Button_SelectObject"));
		this.menuItemSelectRectangle.setText(MapViewProperties.getString("String_Button_DrawRectangle"));
		this.menuItemSelectPolygon.setText(MapViewProperties.getString("String_Button_DrawPolygon"));
		this.menuItemSelectSector.setText(MapViewProperties.getString("String_Button_DrawPie"));
		this.menuItemSelectCircle.setText(MapViewProperties.getString("String_Button_DrawCircle"));

		if (this.moduleName.equals(JPopupMenuBounds.CLIP_REGION)) {
			this.menuItemClear.setText(MapViewProperties.getString("String_Button_ClearClipRegion"));
		} else if (this.moduleName.equals(JPopupMenuBounds.VIEW_BOUNDS_LOCKED)) {
			this.menuItemClear.setText(MapViewProperties.getString("String_Button_ClearLockedViewBounds"));
		} else if (this.moduleName.equals(JPopupMenuBounds.CUSTOM_BOUNDS)) {
			this.menuItemClear.setText(MapViewProperties.getString("String_Button_ClearCustomBounds"));
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
			// 当点击了“选择对象”PopupMenu，隐藏主窗体
			this.currentActionCommand=JPopupMenuBounds.SELECT_TARGET;
			if (this.dialog != null) {
				this.dialog.setVisible(false);
			}
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
		if (this.moduleName.equals(JPopupMenuBounds.VIEW_BOUNDS_LOCKED)) {
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
		activeMapControl.addMouseListener(this.controlMouseListener);
		// 给mapControl添加键盘监听，主要监听esc事件，当按下esc时，取消绘制的起点，重新绘制
		activeMapControl.addKeyListener(this.controlKeyListener);
		activeMapControl.addTrackedListener(this.trackedListener);
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
		// 选择对象可框选--yuanR 2017.3.30
		activeMapControl.setAction(Action.SELECT2);
		activeMapControl.setLayout(null);
		activeMapControl.add(this.panelSelectTargetInfo);

		final MouseMotionListener mouseMotionListener = new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				Point point = e.getPoint();
				panelSelectTargetInfo.setLocation(point.x + 15, point.y);
				panelSelectTargetInfo.setVisible(true);
				panelSelectTargetInfo.updateUI();
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
					activeForm.getMapControl().removeMouseListener(this);
					if (activeForm instanceof FormMap) {
						((FormMap) Application.getActiveApplication().getActiveForm()).clearSelection();
					}
					//设置完之后，show出主窗体
					if (dialog != null) {
						System.out.println(dialog.getLocation());
						dialog.setVisible(true);
					}
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
		if (this.moduleName.equals(JPopupMenuBounds.CLIP_REGION)) {
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
				this.changeSupport.firePropertyChange(this.moduleName, null, JPopupMenuBounds.this.geoRegion);
			}
		} else {
			Rectangle2D rectangle2dResult = null;
			// 当有图层分组的地图时，这样的获得方式会导致无法获得全部图层--yuanR
//			Layers layers = activeMapControl.getMap().getLayers();
			this.selectedGeometryAndLayer.clear();
			ArrayList<Layer> arrayList;
			arrayList = MapUtilities.getLayers(activeMapControl.getMap(), true);

			for (int i = 0; i < arrayList.size(); i++) {
				Layer layer = arrayList.get(i);
				if (!layer.isSelectable() || layer.getDataset() == null) {
					continue;
				}
				Recordset recordset = layer.getSelection().toRecordset();
				List<Geometry> selectedGeometry=new ArrayList<>();
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
						selectedGeometry.add(geometry.clone());
					}
				}
				if (selectedGeometry.size()>0 && this.currentActionCommand.equals(JPopupMenuBounds.SELECT_TARGET)) {
					this.selectedGeometryAndLayer.put(layer,selectedGeometry);
				}
			}
			if (isChanged && rectangle2dResult != null && rectangle2dResult.getHeight() > 0 && rectangle2dResult.getWidth() > 0) {
				JPopupMenuBounds.this.rectangle2d = rectangle2dResult;
				this.changeSupport.firePropertyChange(this.moduleName, null, JPopupMenuBounds.this.rectangle2d);
			}
		}
	}

	private Action getSuitableAction(String actionCommand) {
		Action result = Action.NULL;
		if (actionCommand.equals(JPopupMenuBounds.SELECT_TARGET)) {
			result = Action.SELECT;
		} else if (actionCommand.equals(JPopupMenuBounds.SELECT_RECTANGLE)) {
			// 当点击了“绘制矩形”PopupMenu，隐藏主窗体
			if (this.dialog != null) {
				this.dialog.setVisible(false);
				this.flag = 0;
			}
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

	public java.util.Map<Layer, List<Geometry>> getSelectedGeometryAndLayer(){
		return this.selectedGeometryAndLayer;
	}

	public void addPropertyChangeListeners(PropertyChangeListener changeListener) {
		this.changeSupport.addPropertyChangeListener(changeListener);
	}

	public void removePropertyChangeListeners(PropertyChangeListener changeListener) {
		this.changeSupport.removePropertyChangeListener(changeListener);
	}
}
