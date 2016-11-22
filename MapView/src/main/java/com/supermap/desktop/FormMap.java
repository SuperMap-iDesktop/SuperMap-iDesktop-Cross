package com.supermap.desktop;

import com.supermap.data.CoordSysTransMethod;
import com.supermap.data.CoordSysTransParameter;
import com.supermap.data.CoordSysTranslator;
import com.supermap.data.CursorType;
import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.GeoText;
import com.supermap.data.GeoText3D;
import com.supermap.data.Geometry;
import com.supermap.data.InternalHandle;
import com.supermap.data.Point2D;
import com.supermap.data.Point2Ds;
import com.supermap.data.PrjCoordSys;
import com.supermap.data.PrjCoordSysType;
import com.supermap.data.Recordset;
import com.supermap.data.Rectangle2D;
import com.supermap.data.Workspace;
import com.supermap.desktop.Interface.IContextMenuManager;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.Interface.IProperty;
import com.supermap.desktop.controls.utilities.MapViewUIUtilities;
import com.supermap.desktop.controls.utilities.ToolbarUIUtilities;
import com.supermap.desktop.dialog.DialogSaveAsMap;
import com.supermap.desktop.enums.AngleUnit;
import com.supermap.desktop.enums.AreaUnit;
import com.supermap.desktop.enums.LengthUnit;
import com.supermap.desktop.enums.PropertyType;
import com.supermap.desktop.enums.WindowType;
import com.supermap.desktop.event.ActiveLayersChangedEvent;
import com.supermap.desktop.event.ActiveLayersChangedListener;
import com.supermap.desktop.exception.InvalidScaleException;
import com.supermap.desktop.geometry.Abstract.IGeometry;
import com.supermap.desktop.geometry.Implements.DGeometryFactory;
import com.supermap.desktop.implement.SmComboBox;
import com.supermap.desktop.implement.SmLabel;
import com.supermap.desktop.implement.SmStatusbar;
import com.supermap.desktop.implement.SmTextField;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.mapview.geometry.property.GeometryPropertyFactory;
import com.supermap.desktop.mapview.geometry.property.geometryNode.GeometryNodeFactory;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.FormBaseChild;
import com.supermap.desktop.ui.LayersComponentManager;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.LayersTree;
import com.supermap.desktop.ui.controls.NodeDataType;
import com.supermap.desktop.ui.controls.TreeNodeData;
import com.supermap.desktop.ui.controls.scrollPanel.SmMapControlScrollPanel;
import com.supermap.desktop.utilities.ActionUtilities;
import com.supermap.desktop.utilities.DoubleUtilities;
import com.supermap.desktop.utilities.LogUtilities;
import com.supermap.desktop.utilities.MapControlUtilities;
import com.supermap.desktop.utilities.MapUtilities;
import com.supermap.desktop.utilities.TabularUtilities;
import com.supermap.mapping.Layer;
import com.supermap.mapping.LayerEditableChangedEvent;
import com.supermap.mapping.LayerEditableChangedListener;
import com.supermap.mapping.LayerGroup;
import com.supermap.mapping.LayerRemovedEvent;
import com.supermap.mapping.LayerRemovedListener;
import com.supermap.mapping.LayerSelectableChangedEvent;
import com.supermap.mapping.LayerSelectableChangedListener;
import com.supermap.mapping.Layers;
import com.supermap.mapping.Map;
import com.supermap.mapping.MapDrawingEvent;
import com.supermap.mapping.MapDrawingListener;
import com.supermap.mapping.MapDrawnEvent;
import com.supermap.mapping.MapDrawnListener;
import com.supermap.mapping.Selection;
import com.supermap.ui.Action;
import com.supermap.ui.GeometryAddedListener;
import com.supermap.ui.GeometryDeletedListener;
import com.supermap.ui.GeometryEvent;
import com.supermap.ui.GeometryModifiedListener;
import com.supermap.ui.GeometrySelectChangedEvent;
import com.supermap.ui.GeometrySelectChangedListener;
import com.supermap.ui.MapControl;
import com.supermap.ui.RedoneListener;
import com.supermap.ui.SelectionMode;
import com.supermap.ui.TrackMode;
import com.supermap.ui.UndoneListener;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.EventListenerList;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.EventObject;

public class FormMap extends FormBaseChild implements IFormMap {

	private static final long serialVersionUID = 1L;
	private final DocumentListener pointDocumentListener = new DocumentListener() {
		@Override
		public void insertUpdate(DocumentEvent e) {
			pointFieldValueChanged();
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			pointFieldValueChanged();
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			pointFieldValueChanged();
		}
	};

	private MapControl mapControl = null;
	private LayerRemovedListener layerRemovedListner = new LayerRemovedListener() {
		@Override
		public void layerRemoved(LayerRemovedEvent layerRemovedEvent) {
			if (GlobalParameters.isAutoCloseEmptyWindow() && mapControl.getMap().getLayers().getCount() == 0) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						FormMap.this.close();
					}
				});
			}
		}
	};
	JScrollPane jScrollPaneChildWindow = null;
	private LayersTree layersTree = null;
	private transient EventListenerList eventListenerList = new EventListenerList();
	private ArrayList<Layer> activeLayersList = new ArrayList<>();
	private SmComboBox scaleBox;
	private SmTextField pointXField;
	private SmTextField pointYField;
	private transient DropTarget dropTargeted;
	private transient PrjCoordSysType prjCoordSysType = null;
	private int SELECT_NUMBER = 1;
	private int SCALE = 3;
	private int LOCATION = 4;
	private int PRJCOORSYS = 5;
	private int CENTER_X = 7;
	private int CENTER_Y = 8;
	private boolean isRegisterEvents = false;
	private boolean isResetComboBox = false;

	private LengthUnit lengthUnit = LengthUnit.METER;
	private AreaUnit areaUnit = AreaUnit.METER;
	private AngleUnit angleUnit = AngleUnit.DEGREE;

	private Layer[] rememberActiveLayers = null;

	// 地图窗口右键菜单

	private JPopupMenu formMapContextMenu;
	private DocumentListener scaleBoxListener = new DocumentListener() {
		@Override
		public void insertUpdate(DocumentEvent e) {
			scaleChanged();
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			scaleChanged();
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			scaleChanged();
		}
	};

	public JPopupMenu getFormMapContextMenu() {
		return this.formMapContextMenu;
	}

	// 几何对象右键菜单

	private JPopupMenu geometryContextMenu;

	public JPopupMenu getGeometryContextMenu() {
		return this.geometryContextMenu;
	}

	// 文本对象右键菜单

	private JPopupMenu geometryTextContextMenu;

	public JPopupMenu getGeometryTextContextMenu() {
		return this.geometryTextContextMenu;
	}

	// 参数化几何对象右键菜单

	private JPopupMenu geometryParmaContextMenu;

	public JPopupMenu getGeometryParmaContextMenu() {
		return this.geometryParmaContextMenu;
	}

	private int isShowPopupMenu = 0;
	private boolean isMouseClickLeft = false;
	private MouseAdapter mouseAdapter = new MouseAdapter() {

		@Override
		public void mousePressed(MouseEvent e) {
			int buttonType = e.getButton();
			int clickCount = e.getClickCount();

			if (getMapControl().getAction() == Action.SELECTLINE || getMapControl().getAction() == Action.SELECTREGION
					|| getMapControl().getAction() == Action.SELECTRECTANGLE) {
				if (buttonType == MouseEvent.BUTTON1) {
					isMouseClickLeft = true;
				} else if (buttonType == MouseEvent.BUTTON3) {
					if (!isMouseClickLeft) {
						showPopupMenu(e);
					}
					isMouseClickLeft = false;
				} else {
					isMouseClickLeft = false;
				}
			}

			if (buttonType == MouseEvent.BUTTON3
					&& clickCount == 1
					&& (getMapControl().getAction() == Action.SELECT || getMapControl().getAction() == Action.SELECT2 || getMapControl().getAction() == Action.SELECTCIRCLE)
					&& getMapControl().getTrackMode() == TrackMode.EDIT && isShowPopupMenu <= 0) {
				showPopupMenu(e);
			}
		}

		@Override
		public void mouseClicked(MouseEvent e) {

			// 地图点击判断

			setMapcontrolMouseClick(e);
			// 绘制几何对象时，如果地图是地理坐标，进行超范围提示

			if ((e.getButton() == MouseEvent.BUTTON1 && MapControlUtilities.isCreateGeometry(FormMap.this.mapControl))
					&& (FormMap.this.mapControl.getMap().getPrjCoordSys() != null && FormMap.this.mapControl.getMap().getPrjCoordSys().getType() == PrjCoordSysType.PCS_EARTH_LONGITUDE_LATITUDE)) {
				Point2D mousePosition = FormMap.this.mapControl.getMap().pixelToMap(e.getPoint());

				if (mousePosition.getX() > 180 || mousePosition.getX() < -180 || mousePosition.getY() > 90 || mousePosition.getY() < -90) {
					Application.getActiveApplication().getOutput().output(CommonProperties.getString("String_ExceedBounds"));
				}
			}
		}
	};
	private MapDrawingListener mapDrawingListener = new MapDrawingListener() {

		@Override
		public void mapDrawing(MapDrawingEvent arg0) {
			initCenter();
			// 初始化比例尺下拉框

			initScaleComboBox();
			((SmTextField) getStatusbar().getComponent(PRJCOORSYS)).setText(mapControl.getMap().getPrjCoordSys().getName());
			((SmTextField) getStatusbar().getComponent(PRJCOORSYS)).setCaretPosition(0);
		}
	};
	private KeyAdapter keyAdapter = new KeyAdapter() {
		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyChar() == KeyEvent.VK_ENTER) {
				resetCenter();
			}
		}
	};
	private final FocusAdapter pointFocusListener = new FocusAdapter() {
		@Override
		public void focusLost(FocusEvent e) {
			resetCenter();
		}
	};
	private ItemListener itemListener = new ItemListener() {
		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				scaleBox_ItemChange();
			}
		}
	};

	private MouseWheelListener localMouseWheelListener = new MouseWheelListener() {

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			try {
				double scale = mapControl.getMap().getScale();
				String text = ((JTextField) scaleBox.getEditor().getEditorComponent()).getText();
				if (!ScaleModel.isLegitScaleString(scale) && ScaleModel.isLegitScaleString(text)) {
					mapControl.getMap().setScale(new ScaleModel(text).getScale());
				}
				initCenter();
				initScaleComboBox();
			} catch (Exception e1) {
				Application.getActiveApplication().getOutput().output(e1);
			}
		}
	};
	private MouseMotionListener motionListener = new MouseMotionListener() {

		@Override
		public void mouseMoved(MouseEvent e) {
			updatePrjCoorSysPlace(e);
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			initCenter();
		}
	};
	private transient LayersTreeSelectionListener layersTreeSelectionListener = new LayersTreeSelectionListener();

	private GeometrySelectChangedListener geometrySelectChangedListener = new GeometrySelectChangedListener() {

		@Override
		public void geometrySelectChanged(GeometrySelectChangedEvent arg0) {
			mapControlGeometrySelected(arg0.getCount());
			if (Application.getActiveApplication().getMainFrame().getPropertyManager().isUsable()) {
				setSelectedGeometryProperty();
			}
		}
	};

	private GeometryDeletedListener geometryDeletedListener = new GeometryDeletedListener() {

		/*
		 *
		 * 待组件改好缺陷 UGDJ-186 之后，就移除这个实现
		 */
		@Override
		public void geometryDeleted(GeometryEvent arg0) {
			if (Application.getActiveApplication().getMainFrame().getPropertyManager().isUsable()) {
				setSelectedGeometryProperty();
			}
			refreshOpenedTabularForms();
		}
	};

	private GeometryAddedListener geometryAddedListener = new GeometryAddedListener() {

		/*
		 *
		 * 待组件改好缺陷 UGDJ-186 之后，就移除这个实现
		 */
		@Override
		public void geometryAdded(GeometryEvent arg0) {
			if (Application.getActiveApplication().getMainFrame().getPropertyManager().isUsable()) {
				setSelectedGeometryProperty();
			}
			refreshOpenedTabularForms();
		}
	};

	private GeometryModifiedListener geometryModifiedListener = new GeometryModifiedListener() {

		@Override
		public void geometryModified(GeometryEvent arg0) {
			if (Application.getActiveApplication().getMainFrame().getPropertyManager().isUsable()) {
				setSelectedGeometryProperty();
			}
		}
	};

	private transient KeyListener mapKeyListener = new KeyAdapter() {

		@Override
		public void keyPressed(KeyEvent e) {
			if (e.isConsumed()) {
				return;
			}
			if (KeyEvent.VK_ESCAPE == e.getKeyCode()) {
				clearSelection();
			} else if (KeyEvent.VK_A == e.getKeyCode() && e.isControlDown()) {
				selectAll();
			} else if ((KeyEvent.VK_Y == e.getKeyCode() || KeyEvent.VK_Z == e.getKeyCode()) && e.isControlDown()
					&& Application.getActiveApplication().getMainFrame().getPropertyManager().isUsable()) {
				setSelectedGeometryProperty();
			}
		}
	};

	private LayerEditableChangedListener layerEditableChangedListener = new LayerEditableChangedListener() {
		@Override
		public void editableChanged(LayerEditableChangedEvent layerEditableChangedEvent) {
			if (layerEditableChangedEvent.isEditable()) {
				if (!ActionUtilities.isSupportDatasetType(mapControl.getAction(), layerEditableChangedEvent.getLayer().getDataset().getType())) {
					mapControl.setAction(Action.SELECT2);
				}
			} else {
				mapControl.setAction(Action.SELECT2);
			}
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					// 先挂起，最后刷新工具条
					ToolbarUIUtilities.updataToolbarsState();
				}
			});

			// 刷新地图。如果选中对象时，更改编辑状态，理应要刷新地图显示
			FormMap.this.mapControl.getMap().refreshTrackingLayer();
		}
	};

	private LayerSelectableChangedListener layerSelectableChangedListener = new LayerSelectableChangedListener() {

		@Override
		public void selectableChanged(LayerSelectableChangedEvent arg0) {
			// 刷新地图。如果选中对象时，更改可选择状态，理应要刷新地图显示
			FormMap.this.mapControl.getMap().refreshTrackingLayer();
		}
	};

	private MapDrawnListener mapDrawnListener = new MapDrawnListener() {

		@Override
		public void mapDrawn(MapDrawnEvent mapDrawnEvent) {
			ToolbarUIUtilities.updataToolbarsState();
		}
	};

	private void setMapcontrolMouseClick(MouseEvent e) {
		int buttonType = e.getButton();
		int clickCount = e.getClickCount();

		if (buttonType == MouseEvent.BUTTON1) {
			// 重新计算选中值

//			Layers layers = mapControl.getMap().getLayers();
//			int count = 0;
//			for (int i = 0; i < layers.getCount(); i++) {
//				if (layers.get(i).getSelection() != null) {
//					count += layers.get(i).getSelection().getCount();
//				}
//			}
//			((SmLabel) getStatusbar().get(SELECT_NUMBER)).setText(String.valueOf(count));
			if (clickCount == 2 && mapControl.getMap().findSelection(true).length > 0) {

				// 双击显示对象属性
				Application.getActiveApplication().getMainFrame().getPropertyManager().setPropertyVisible(true);
				setSelectedGeometryProperty();
			}
		}
	}

	private UndoneListener undoneListener = new UndoneListener() {

		@Override
		public void undone(EventObject arg0) {
			refreshOpenedTabularForms();
		}
	};

	private RedoneListener redoneListener = new RedoneListener() {

		@Override
		public void redone(EventObject arg0) {
			refreshOpenedTabularForms();
		}
	};

	public FormMap() {
		this("");
	}

	public FormMap(String name) {
		this(name, null, null);
	}

	public FormMap(String title, Icon icon, Component component) {
		super(title, icon, component);
		resetSmStatusbarLayout();
		initComponents();

		Map map = this.mapControl.getMap();
		map.setWorkspace(Application.getActiveApplication().getWorkspace());
		map.setName(title);

		this.setComponent(this.jScrollPaneChildWindow);

		if (Application.getActiveApplication().getMainFrame() != null) {
			IContextMenuManager manager = Application.getActiveApplication().getMainFrame().getContextMenuManager();

			this.formMapContextMenu = (JPopupMenu) manager.get("SuperMap.Desktop._FormMap.FormMapContextMenu");
			this.geometryContextMenu = (JPopupMenu) manager.get("SuperMap.Desktop._FormMap.GeometryContextMenu");
			this.geometryTextContextMenu = (JPopupMenu) manager.get("SuperMap.Desktop._FormMap.GeometryTextContextMenu");
			this.geometryParmaContextMenu = (JPopupMenu) manager.get("SuperMap.Desktop._FormMap.GeometryParmaContextMenu");
		}

		((SmLabel) getStatusbar().getComponent(SELECT_NUMBER)).setText("0");
		((SmTextField) getStatusbar().getComponent(LOCATION)).setSize(300, 20);
		registerEvents();
		// 初始化中心点

		initCenter();
		// 坐标和投影 不可编辑

		initUneditableStatus();
		initGloableSetting();
	}


	private void initGloableSetting() {
		int selectedIndex = GlobalParameters.getPositiveSelect();
		SelectionMode selectionMode = SelectionMode.CONTAIN_INNER_POINT;
		if (selectedIndex == 1) {
			selectionMode = SelectionMode.INTERSECT;
		} else if (selectedIndex == 2) {
			selectionMode = SelectionMode.CONTAIN_OBJECT;
		}
		this.getMapControl().setSelectionMode(selectionMode);

		this.mapControl.getMap().setMaxVisibleVertex(GlobalParameters.getMaxVisibleVertex());
	}

	private void resetSmStatusbarLayout() {
		SmStatusbar statusbar = getStatusbar();
		java.util.List<Component> list = new ArrayList<Component>();
		for (int i = 0; i < statusbar.getCount(); i++) {
			list.add(((Component) statusbar.get(i)));
		}
		statusbar.removeAll();
		statusbar.setLayout(new GridBagLayout());

		if (list.get(0) != null) {
			statusbar.add(list.get(0), new GridBagConstraintsHelper(0, 0, 1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER)
					.setWeight(0, 1));
		}
		if (list.get(1) != null) {
			statusbar.add(list.get(1), new GridBagConstraintsHelper(1, 0, 1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER)
					.setWeight(0, 1));
		}
		if (list.get(2) != null) {
			statusbar.add(list.get(2), new GridBagConstraintsHelper(2, 0, 1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER)
					.setWeight(0, 1));
		}
		if (list.get(3) != null) {
			statusbar.add(list.get(3), new GridBagConstraintsHelper(3, 0, 1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER)
					.setWeight(0, 1).setIpad(50, 0));
		}
		if (list.get(4) != null) {
			statusbar.add(list.get(4), new GridBagConstraintsHelper(4, 0, 1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER)
					.setWeight(1, 1).setIpad(100, 0));
		}
		if (list.get(5) != null) {
			list.get(5).setMinimumSize(new Dimension(200, list.get(5).getHeight()));
			statusbar.add(list.get(5), new GridBagConstraintsHelper(5, 0, 1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER)
					.setWeight(0, 1));
		}
		if (list.get(6) != null) {
			statusbar.add(list.get(6), new GridBagConstraintsHelper(6, 0, 1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER)
					.setWeight(0, 1));
		}
		if (list.get(7) != null) {
			list.get(7).setMinimumSize(new Dimension(60, list.get(7).getHeight()));
			statusbar.add(list.get(7), new GridBagConstraintsHelper(7, 0, 1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER)
					.setWeight(0, 1));
		}
		if (list.get(8) != null) {
			list.get(8).setMinimumSize(new Dimension(60, list.get(8).getHeight()));
			statusbar.add(list.get(8), new GridBagConstraintsHelper(8, 0, 1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER)
					.setWeight(0, 1));
		}

	}

	private Dimension newSize;

	private void initComponents() {
		this.mapControl = new MapControl();
		this.mapControl.setWaitCursorEnabled(false);
		jScrollPaneChildWindow = new SmMapControlScrollPanel(mapControl);
		this.layersTree = UICommonToolkit.getLayersManager().getLayersTree();
		this.scaleBox = (SmComboBox) getStatusbar().getComponent(SCALE);
		this.scaleBox.setEditable(true);
		this.pointXField = (SmTextField) getStatusbar().getComponent(CENTER_X);
		Dimension size = new Dimension(70, 23);
		this.pointXField.setPreferredSize(size);
		this.pointXField.setMinimumSize(size);
		this.pointXField.setMaximumSize(size);
		this.pointYField = (SmTextField) getStatusbar().getComponent(CENTER_Y);
		this.pointYField.setPreferredSize(size);
		this.pointYField.setMinimumSize(size);
		this.pointYField.setMaximumSize(size);
	}

	private void registerEvents() {
		if (!this.isRegisterEvents) {
			// 防止多次注册
			this.isRegisterEvents = true;
			this.mapControl.getMap().getLayers().removeLayerRemovedListener(layerRemovedListner);
			this.mapControl.getMap().getLayers().addLayerRemovedListener(layerRemovedListner);
			this.mapControl.addKeyListener(this.mapKeyListener);
			this.mapControl.addGeometrySelectChangedListener(this.geometrySelectChangedListener);
			this.mapControl.addGeometryAddedListener(this.geometryAddedListener);
			this.mapControl.addGeometryDeletedListener(this.geometryDeletedListener);
			this.mapControl.addGeometryModifiedListener(this.geometryModifiedListener);
			this.mapControl.addMouseMotionListener(this.motionListener);
			this.mapControl.addUndoneListener(this.undoneListener);
			this.mapControl.addRedoneListener(this.redoneListener);
			MouseListener[] mouseListeners = this.mapControl.getMouseListeners();
			this.mapControl.addMouseListener(this.mouseAdapter);
			for (MouseListener mouseListener : mouseListeners) {
				// 确保最先触发
				this.mapControl.removeMouseListener(mouseListener);
				this.mapControl.addMouseListener(mouseListener);
			}

			this.mapControl.addMouseWheelListener(this.localMouseWheelListener);
			this.mapControl.getMap().addDrawnListener(this.mapDrawnListener);
			this.mapControl.getMap().addDrawingListener(this.mapDrawingListener);
			this.mapControl.getMap().getLayers().addLayerEditableChangedListener(this.layerEditableChangedListener);
			this.mapControl.getMap().getLayers().addLayerSelectableChangedListener(this.layerSelectableChangedListener);

			// 比例尺下拉框添加选择事件
			this.scaleBox.removeItemListener(this.itemListener);
			this.scaleBox.addItemListener(this.itemListener);
			this.pointXField.addKeyListener(this.keyAdapter);
			this.pointXField.addFocusListener(this.pointFocusListener);
			this.pointXField.getDocument().addDocumentListener(this.pointDocumentListener);
			this.pointYField.addKeyListener(this.keyAdapter);
			this.pointYField.addFocusListener(this.pointFocusListener);
			this.pointYField.getDocument().addDocumentListener(this.pointDocumentListener);

			addDrag();
			this.layersTree.addTreeSelectionListener(this.layersTreeSelectionListener);

			((JTextField) this.scaleBox.getEditor().getEditorComponent()).getDocument().addDocumentListener(scaleBoxListener);
		}
	}

	private void unRegisterEvents() {
		this.isRegisterEvents = false;
		this.scaleBox.removeItemListener(this.itemListener);
		this.pointXField.removeKeyListener(this.keyAdapter);
		this.pointYField.removeKeyListener(this.keyAdapter);
		this.pointXField.removeFocusListener(this.pointFocusListener);
		this.pointYField.removeFocusListener(this.pointFocusListener);
		this.pointXField.getDocument().removeDocumentListener(this.pointDocumentListener);
		this.pointYField.getDocument().removeDocumentListener(this.pointDocumentListener);
		removeDrag();
		if (this.mapControl != null) {
			this.mapControl.removeKeyListener(this.mapKeyListener);
			this.mapControl.removeGeometrySelectChangedListener(this.geometrySelectChangedListener);
			this.mapControl.removeGeometryAddedListener(this.geometryAddedListener);
			this.mapControl.removeGeometryDeletedListener(this.geometryDeletedListener);
			this.mapControl.removeGeometryModifiedListener(this.geometryModifiedListener);
			this.mapControl.removeMouseMotionListener(this.motionListener);
			this.mapControl.removeUndoneListener(this.undoneListener);
			this.mapControl.removeRedoneListener(this.redoneListener);
			this.mapControl.removeMouseListener(this.mouseAdapter);
			this.mapControl.removeMouseWheelListener(this.localMouseWheelListener);

			if (this.mapControl.getMap() != null) {
				this.mapControl.getMap().removeDrawnListener(this.mapDrawnListener);
				this.mapControl.getMap().removeDrawingListener(this.mapDrawingListener);
				this.mapControl.getMap().getLayers().removeLayerEditableChangedListener(this.layerEditableChangedListener);
				this.mapControl.getMap().getLayers().removeLayerSelectableChangedListener(this.layerSelectableChangedListener);
			}
		}

		if (null != layersTree) {
			this.layersTree.removeTreeSelectionListener(this.layersTreeSelectionListener);
			// this.layersTree.removeMouseListener(this.layersTreeMouseAdapter);

		}

	}

	private void pointFieldValueChanged() {
		pointXField.setForeground(DoubleUtilities.isDoubleWithoutD(pointXField.getText()) ? Color.black : Color.RED);
		pointYField.setForeground(DoubleUtilities.isDoubleWithoutD(pointYField.getText()) ? Color.black : Color.RED);
	}

	private void scaleChanged() {
		JTextField textField = (JTextField) scaleBox.getEditor().getEditorComponent();
		if (!ScaleModel.isLegitScaleString(textField.getText())) {
			textField.setForeground(Color.red);
		} else {
			textField.setForeground(Color.black);
		}
	}

	/**
	 * 初始化不可编辑的状态栏
	 */
	private void initUneditableStatus() {
		((SmTextField) getStatusbar().getComponent(PRJCOORSYS)).setEditable(false);
		((SmTextField) getStatusbar().getComponent(LOCATION)).setEditable(false);
	}

	protected void resetCenter() {
		try {
			String pointXFieldText = pointXField.getText();
			String pointYFieldText = pointYField.getText();
			if (DoubleUtilities.isDoubleWithoutD(pointXFieldText) && DoubleUtilities.isDoubleWithoutD(pointYFieldText)) {
				double pointX = DoubleUtilities.stringToValue(pointXFieldText);
				double pointY = DoubleUtilities.stringToValue(pointYFieldText);
				Point2D point2d = new Point2D(pointX, pointY);
				mapControl.getMap().setCenter(point2d);
				mapControl.getMap().refresh();
			} else {
				initCenter();
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	/**
	 * 监听地图控件鼠标移动事件，获得鼠标所在位置对应的经纬度
	 *
	 * @param e
	 */
	protected void updatePrjCoorSysPlace(MouseEvent e) {
		try {
			PrjCoordSysType coordSysType = this.getMapControl().getMap().getPrjCoordSys().getType();
			Point pointMouse = e.getPoint();
			Point2D point = mapControl.getMap().pixelToMap(pointMouse);

			String x = "";
			if (Double.isInfinite(point.getX())) {
				x = MapViewProperties.getString("String_Infinite");
			} else if (Double.isNaN(point.getX())) {
				x = MapViewProperties.getString("String_NotANumber");
			} else {
				x = DoubleUtilities.getFormatString(point.getX());
			}
			String y = "";
			if (Double.isInfinite(point.getY())) {
				y = MapViewProperties.getString("String_Infinite");
			} else if (Double.isNaN(point.getY())) {
				y = MapViewProperties.getString("String_NotANumber");
			} else {
				y = DoubleUtilities.getFormatString(point.getY());
			}

			// XY坐标信息

			String XYInfo = MessageFormat.format(MapViewProperties.getString("String_String_PrjCoordSys_XYInfo"), x, y);

			// 经纬度信息

			String latitudeInfo = MessageFormat.format(MapViewProperties.getString("String_PrjCoordSys_LongitudeLatitude"), getFormatCoordinates(point.getX()),
					getFormatCoordinates(point.getY()));

			if (coordSysType == PrjCoordSysType.PCS_NON_EARTH) {
				// 平面

				((SmTextField) getStatusbar().getComponent(LOCATION)).setText(XYInfo);
			} else if (coordSysType == PrjCoordSysType.PCS_EARTH_LONGITUDE_LATITUDE) {
				// 地理

				((SmTextField) getStatusbar().getComponent(LOCATION)).setText(latitudeInfo);
			} else {
				// 投影

				Point2Ds point2Ds = new Point2Ds();
				point2Ds.add(point);

				CoordSysTranslator.inverse(point2Ds, this.getMapControl().getMap().getPrjCoordSys());
				latitudeInfo = MessageFormat.format(MapViewProperties.getString("String_PrjCoordSys_LongitudeLatitude"),
						getFormatCoordinates(point2Ds.getItem(0).getX()), getFormatCoordinates(point2Ds.getItem(0).getY()));
				((SmTextField) getStatusbar().getComponent(LOCATION)).setText(XYInfo + latitudeInfo);
			}
			// 设置光标位置

			((SmTextField) getStatusbar().getComponent(LOCATION)).setCaretPosition(0);
			// 投影改变时更新一下

			if (!coordSysType.equals(this.prjCoordSysType)) {
				this.prjCoordSysType = coordSysType;
			}

		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private String getFormatCoordinates(double point) {
		// 度
		double pointTemp = point;
		int angles = (int) pointTemp;
		pointTemp = Math.abs(pointTemp);

		pointTemp = (pointTemp - Math.abs(angles)) * 60;
		// 分

		int min = (int) pointTemp;
		// 秒

		pointTemp = (pointTemp - min) * 60;
		DecimalFormat format = new DecimalFormat("######0.00");

		return MessageFormat.format(MapViewProperties.getString("String_LongitudeLatitude"), angles, min, format.format(pointTemp));
	}

	/**
	 */
	protected void scaleBox_ItemChange() {
		String scaleString = (String) scaleBox.getSelectedItem();
		try {
			if (isResetComboBox && ScaleModel.isLegitScaleString(scaleString)) {
				ScaleModel model = new ScaleModel(scaleString);
				double value = model.getScale();
				if (Double.compare(value, mapControl.getMap().getScale()) != 0) {
					mapControl.getMap().setScale(model.getScale());
					mapControl.getMap().refresh();
				}
			} else {
				if (ScaleModel.isLegitScaleString(mapControl.getMap().getScale())) {
					((JTextField) scaleBox.getEditor().getEditorComponent()).setText(new ScaleModel(mapControl.getMap().getScale()).toString());
				}
			}
			if (scaleBox.getSelectedIndex() != -1) {
				scaleBox.removeItemListener(itemListener);
				final String text = ((JTextField) scaleBox.getEditor().getEditorComponent()).getText();
				scaleBox.setSelectedIndex(-1);
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						((JTextField) scaleBox.getEditor().getEditorComponent()).setText(text);
					}
				});
				scaleBox.addItemListener(itemListener);
			}
		} catch (InvalidScaleException ex) {
			Application.getActiveApplication().getOutput().output(ex);
		} catch (Exception e2) {
			Application.getActiveApplication().getOutput().output(e2);
		}
	}

	/**
	 * 初始化比例尺下拉菜单
	 */
	@SuppressWarnings("unchecked")
	private void initScaleComboBox() {
		try {
			if (!isResetComboBox) {
				initScaleBoxItem();
				isResetComboBox = true;
			}
			String scale = new ScaleModel(mapControl.getMap().getScale()).toString();
			if ("NONE".equals(scale)) {
				scale = String.valueOf(mapControl.getMap().getScale());
			}
			((JTextField) this.scaleBox.getEditor().getEditorComponent()).setText(scale);
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}

	}

	private void initScaleBoxItem() {
		scaleBox.removeItemListener(itemListener);
		this.scaleBox.removeAllItems();

		double[] visibleScales = getMapControl().getMap().getVisibleScales();
		if (!getMapControl().getMap().isVisibleScalesEnabled() || visibleScales.length <= 0) {

			this.scaleBox.addItem(ScaleModel.SCALE_5000);
			this.scaleBox.addItem(ScaleModel.SCALE_10000);
			this.scaleBox.addItem(ScaleModel.SCALE_25000);
			this.scaleBox.addItem(ScaleModel.SCALE_50000);
			this.scaleBox.addItem(ScaleModel.SCALE_100000);
			this.scaleBox.addItem(ScaleModel.SCALE_250000);
			this.scaleBox.addItem(ScaleModel.SCALE_500000);
			this.scaleBox.addItem(ScaleModel.SCALE_1000000);
		} else {
			for (double visibleScale : visibleScales) {
				try {
					this.scaleBox.addItem(new ScaleModel(visibleScale).getScaleCaption());
				} catch (InvalidScaleException e) {
					// ignore
				}
			}
		}
		scaleBox.addItemListener(itemListener);
	}

	@Override
	public void setVisibleScalesEnabled(boolean isVisibleScalesEnabled) {
		getMapControl().getMap().setVisibleScalesEnabled(isVisibleScalesEnabled);
		initScaleBoxItem();
	}

	@Override
	public void setVisibleScales(double[] scales) {
		getMapControl().getMap().setVisibleScales(scales);
		initScaleBoxItem();
	}

	private void initCenter() {
		String x = Double.isNaN(mapControl.getMap().getCenter().getX()) ? MapViewProperties.getString("String_NotANumber") : DoubleUtilities.getFormatString(mapControl.getMap()
				.getCenter().getX());
		String y = Double.isNaN(mapControl.getMap().getCenter().getY()) ? MapViewProperties.getString("String_NotANumber") : DoubleUtilities.getFormatString(mapControl.getMap()
				.getCenter().getY());
		this.pointXField.setText(x);
		this.pointXField.setCaretPosition(0);
		this.pointYField.setText(y);
		this.pointYField.setCaretPosition(0);

	}

	@Override
	public MapControl getMapControl() {
		return this.mapControl;
	}

	@Override
	public Layer[] getActiveLayers() {
		Layer[] layers = new Layer[this.activeLayersList.size()];
		this.activeLayersList.toArray(layers);
		return layers;
	}

	@Override
	public void setActiveLayers(Layer... activeLayers) {
		Layer[] oldLayers = getActiveLayers();

		if (activeLayers != null && activeLayers.length > 0) {
			this.activeLayersList.clear();
			ArrayList<TreePath> paths = new ArrayList<TreePath>();

			for (Layer layer : activeLayers) {
				try {
					if (this.mapControl.getMap().getLayers().contains(layer.getName())) {
						this.activeLayersList.add(layer);

						DefaultMutableTreeNode root = (DefaultMutableTreeNode) this.layersTree.getModel().getRoot();
						for (int i = 0; i < root.getChildCount(); i++) {
							DefaultMutableTreeNode node = (DefaultMutableTreeNode) root.getChildAt(i);
							TreeNodeData nodeData = (TreeNodeData) node.getUserObject();

							if (isNodeLayer(nodeData.getType()) && nodeData.getData() == layer) {
								paths.add(new TreePath(node.getPath()));
								break;
							}
						}
					}
				} catch (Exception e) {
					// 有可能图层被删除但引用还存在，这种情况用layer==null判断不出来，用try catch做处理吧。

					continue;
				}
			}

//			this.layersTree.setSelectionPaths(paths.toArray(new TreePath[paths.size()]));
		} else {
			this.layersTree.clearSelection();
			this.activeLayersList.clear();
		}

		if (oldLayers != null && oldLayers.length > 0 && !this.activeLayersList.isEmpty()) {
			fireActiveLayersChanged(new ActiveLayersChangedEvent(this, oldLayers, getActiveLayers()));
		}
	}

	@Override
	public String getText() {
		return super.getTitle();
	}

	@Override
	public void setText(String text) {
		super.setTitle(text);
	}

	@Override
	public WindowType getWindowType() {
		return WindowType.MAP;
	}

	@Override
	public void clean() {
		unRegisterEvents();
		if (this.mapControl != null && this.mapControl.getMap() != null && this.mapControl.getMap().getLayers() != null) {
			this.mapControl.getMap().getLayers().removeLayerRemovedListener(layerRemovedListner);
		}
		if (layersTree.getMap() == this.getMapControl().getMap()) {
			this.layersTree.setMap(null);
		}
		Layers layers = this.mapControl.getMap().getLayers();
		this.mapControl.getMap().close();
		this.mapControl.delete();
		this.mapControl.dispose();
		this.mapControl = null;
		this.layersTree = null;
		try {
			Method method = InternalHandle.class.getDeclaredMethod("clearHandle");
			method.setAccessible(true);
			method.invoke(layers);
			method.setAccessible(false);
		} catch (Exception e) {
			LogUtilities.outPut("clean handle failed while formMap exit.");
		}
	}

	@Override
	public boolean save() {
		boolean result = false;
		try {
			if (this.isNeedSave()) {
				Workspace workspace = Application.getActiveApplication().getWorkspace();
				if (workspace != null) {
					if (workspace.getMaps().indexOf(this.getText()) >= 0) {
						result = workspace.getMaps().setMapXML(this.getText(), this.mapControl.getMap().toXML());
					} else {
						result = save(true, true);
					}
				}
				if (result) {
					this.mapControl.getMap().setModified(false);
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return result;
	}

	@Override
	public boolean save(boolean notify, boolean isNewWindow) {
		boolean result = false;
		try {
			if (this.isNeedSave()) {
				Workspace workspace = Application.getActiveApplication().getWorkspace();
				if (workspace != null) {
					if (notify) {
						result = this.saveAs(isNewWindow);
					} else {
						result = workspace.getMaps().add(this.getText(), this.mapControl.getMap().toXML()) >= 0;
					}
				}

				if (result) {
					this.mapControl.getMap().setModified(false);
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
			Workspace workspace = Application.getActiveApplication().getWorkspace();
			DialogSaveAsMap dialogSaveAs = new DialogSaveAsMap();
			dialogSaveAs.setIsNewWindow(isNewWindow);
			dialogSaveAs.setMaps(workspace.getMaps());
			dialogSaveAs.setMapName(this.getText(), isNewWindow);
			if (dialogSaveAs.showDialog() == DialogResult.YES) {
				this.mapControl.getMap().setName(dialogSaveAs.getMapName());
				result = workspace.getMaps().add(dialogSaveAs.getMapName(), this.mapControl.getMap().toXML()) >= 0;
				if (result) {
					this.setText(dialogSaveAs.getMapName());
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
		return this.mapControl.getMap().isModified();
	}

	@Override
	public void setNeedSave(boolean needSave) {

		if (null != this.mapControl && null != this.mapControl.getMap()) {
			this.mapControl.getMap().setModified(needSave);
		}
	}

	@Override
	public boolean saveFormInfos() {
		return false;
	}

	@Override
	public final void addActiveLayersChangedListener(ActiveLayersChangedListener listener) {
		eventListenerList.add(ActiveLayersChangedListener.class, listener);
	}

	@Override
	public final void removeActiveLayersChangedListener(ActiveLayersChangedListener listener) {
		eventListenerList.remove(ActiveLayersChangedListener.class, listener);
	}

	@Override
	public void removeActiveLayersByDatasets(Dataset... datasets) {
		for (int i = activeLayersList.size() - 1; i >= 0; i--) {
			for (Dataset dataset : datasets) {
				if ((dataset.getType() == DatasetType.NETWORK || dataset.getType() == DatasetType.NETWORK3D)
						&& (activeLayersList.get(i).getDataset() == ((DatasetVector) dataset).getChildDataset())) {
					activeLayersList.remove(i);
					break;
				}
				if (activeLayersList.get(i).getDataset() == dataset) {
					activeLayersList.remove(i);
					break;
				}
			}
		}
	}

	@Override
	public void dontShowPopupMenu() {
		this.isShowPopupMenu++;
	}

	@Override
	public void showPopupMenu() {
		this.isShowPopupMenu--;
	}

	@Override
	public int getIsShowPopupMenu() {
		return isShowPopupMenu;
	}

	protected void fireActiveLayersChanged(ActiveLayersChangedEvent e) {
		Object[] listeners = eventListenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ActiveLayersChangedListener.class) {
				((ActiveLayersChangedListener) listeners[i + 1]).acitiveLayersChanged(e);
			}
		}
	}

	private void LayersTreeSelectionChanged() {
		TreePath[] selectedPaths = this.layersTree.getSelectionPaths();
		Layer[] oldActiveLayers = getActiveLayers();

		this.activeLayersList.clear();
		if (selectedPaths != null) {
			for (TreePath path : selectedPaths) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();

				if (node != null) {
					TreeNodeData nodeData = (TreeNodeData) node.getUserObject();

					if (isNodeLayer(nodeData.getType()) && nodeData.getData() instanceof Layer) {
						this.activeLayersList.add((Layer) nodeData.getData());
					}
				}
			}
		}

		if (!this.activeLayersList.isEmpty()) {
			fireActiveLayersChanged(new ActiveLayersChangedEvent(this, oldActiveLayers, getActiveLayers()));
		} else if (oldActiveLayers != null) {
			fireActiveLayersChanged(new ActiveLayersChangedEvent(this, oldActiveLayers, null));
		}
	}

	private boolean isNodeLayer(NodeDataType nodeDataType) {
		return nodeDataType == NodeDataType.LAYER || nodeDataType == NodeDataType.LAYER_IMAGE || nodeDataType == NodeDataType.LAYER_THEME
				|| nodeDataType == NodeDataType.LAYER_GRID || nodeDataType == NodeDataType.THEME_UNIQUE || nodeDataType == NodeDataType.THEME_RANGE
				|| nodeDataType == NodeDataType.THEME_LABEL_ITEM || nodeDataType == NodeDataType.THEME_UNIQUE_ITEM
				|| nodeDataType == NodeDataType.THEME_RANGE_ITEM || nodeDataType == NodeDataType.LAYER_GROUP
				|| nodeDataType == NodeDataType.DATASET_IMAGE_COLLECTION || nodeDataType == NodeDataType.DATASET_GRID_COLLECTION
				|| nodeDataType == NodeDataType.THEME_CUSTOM;
	}

	private void showPopupMenu(MouseEvent e) {
		Recordset recordset = null;
		try {
			boolean selected = false;
			Selection selection = null;
			ArrayList<Layer> layers = MapUtilities.getLayers(this.getMapControl().getMap());
			for (Layer layer : layers) {
				selection = layer.getSelection();
				if (selection != null && selection.getCount() > 0) {
					selected = true;
					break;
				}
			}

			if (selected) {
				recordset = selection.toRecordset();
				recordset.moveFirst();
				Geometry geo = recordset.getGeometry();
				if (geo != null && geo instanceof GeoText) {
					this.getGeometryTextContextMenu().show((Component) this.getMapControl(), (int) e.getPoint().getX(), (int) e.getPoint().getY());
				} else {
					this.getGeometryContextMenu().show((Component) this.getMapControl(), (int) e.getPoint().getX(), (int) e.getPoint().getY());
				}
				if (geo != null) {
					geo.dispose();
				}
			} else {
				this.getFormMapContextMenu().show((Component) this.getMapControl(), (int) e.getPoint().getX(), (int) e.getPoint().getY());
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		} finally {
			if (recordset != null) {
				recordset.dispose();
			}
		}

	}

	@Override
	public void actived() {
		try {
			registerEvents();
			LayersComponentManager layersComponentManager = UICommonToolkit.getLayersManager();
			if (layersComponentManager != null) {
				boolean exist = false;
				for (int i = 0; i < Application.getActiveApplication().getMainFrame().getFormManager().getCount(); i++) {
					if (Application.getActiveApplication().getMainFrame().getFormManager().get(i) instanceof FormMap) {
						FormMap formMap = (FormMap) Application.getActiveApplication().getMainFrame().getFormManager().get(i);
						if (formMap != null && formMap.getText() == this.getText()) {
							exist = true;
							break;
						}
					}
				}

				if (exist) {
					layersComponentManager.setMap(this.getMapControl().getMap());
					setActiveLayers(rememberActiveLayers);
					this.getMapControl().getMap().refresh();
				} else {
					layersComponentManager.setMap(null);
				}
			}
			if (this.mapControl != null) {
				this.mapControl.requestFocus();
			}
			if (Application.getActiveApplication().getMainFrame().getPropertyManager().isUsable()) {
				if (PropertyType.isGeometryPropertyType(Application.getActiveApplication().getMainFrame().getPropertyManager().getPropertyType())) {
					setSelectedGeometryProperty();
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public void deactived() {
		try {
			unRegisterEvents();

			this.rememberActiveLayers = getActiveLayers();

			if (this.layersTree != null) {
				this.layersTree.setMap(null);
			}
			if (Application.getActiveApplication().getMainFrame().getPropertyManager().isUsable()) {
				if (PropertyType.isGeometryPropertyType(Application.getActiveApplication().getMainFrame().getPropertyManager().getPropertyType())) {
					setSelectedGeometryProperty();
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	/**
	 * 窗体被激活时候触发
	 */
	@Override
	public void windowShown() {
		// do nothing

	}

	/**
	 * 窗体被隐藏时候触发
	 */
	@Override
	public void windowHidden() {
		// do nothing

	}

	public void removeLayers(Layer[] layers) {
		try {
			if (layers != null && layers.length > 0) {
				ArrayList<String> removingLayers = new ArrayList<String>();
				String message = "";
				if (layers.length == 1) {
					message = String.format(MapViewProperties.getString("String_validateRemoveLayerMessage"), layers[0].getCaption());
				} else {
					message = MessageFormat.format(MapViewProperties.getString("String_validateRemoveRangeMessage"), layers.length);
				}

				int result = UICommonToolkit.showConfirmDialog(message);
				if (result == JOptionPane.OK_OPTION) {
					for (Layer layer : layers) {
						if (layer instanceof LayerGroup) {
							ArrayList<Layer> childLayers = MapUtilities.getLayers((LayerGroup) layer);
							for (Layer childLayer : childLayers) {
								Dataset dataset = childLayer.getDataset();
								if (dataset == null) {
									if (childLayer.getBounds().getWidth() > 0 || childLayer.getBounds().getHeight() > 0) {
										break;
									}
								} else {
									// 有可能存在一个点的数据集，所以还是用记录集来判断吧

									if (dataset instanceof DatasetVector && ((DatasetVector) dataset).getRecordCount() > 0) {
										break;
									}
								}
							}
						} else {
							Dataset dataset = layer.getDataset();
							if (dataset == null) {
								if (layer.getBounds().getWidth() > 0 || layer.getBounds().getHeight() > 0) {
									break;
								}
							} else {
								// 有可能存在一个点的数据集，所以还是用记录集来判断吧

								if (dataset instanceof DatasetVector && ((DatasetVector) dataset).getRecordCount() > 0) {
									// do nothing

								}
							}
						}

						removingLayers.add(layer.getName());
					}

					for (int i = 0; i < removingLayers.size(); i++) {
						MapUtilities.removeLayer(this.getMapControl().getMap(), removingLayers.get(i));
					}

					this.getMapControl().getMap().refresh();
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	public void geometryViewEntire() {
		try {
			Recordset recordset = null;
			Rectangle2D rect = Rectangle2D.getEMPTY();
			ArrayList<Layer> layers = MapUtilities.getLayers(this.getMapControl().getMap());
			for (Layer layer : layers) {
				if (layer.getSelection() != null && layer.getSelection().getCount() > 0) {
					recordset = layer.getSelection().toRecordset();
					if (recordset != null) {
						Rectangle2D layerSelectionBounds = recordset.getBounds();
						if (this.getMapControl().getMap().isDynamicProjection()) {
							PrjCoordSys recordCoordSys = recordset.getDataset().getPrjCoordSys();
							PrjCoordSys mapCoordSys = this.getMapControl().getMap().getPrjCoordSys();
							if (recordCoordSys.getType() != mapCoordSys.getType()) {
								Point2Ds points = new Point2Ds(new Point2D[]{new Point2D(layerSelectionBounds.getLeft(), layerSelectionBounds.getBottom()),
										new Point2D(layerSelectionBounds.getRight(), layerSelectionBounds.getTop())});
								CoordSysTransParameter transParameter = new CoordSysTransParameter();
								try {
									CoordSysTranslator.convert(points, recordCoordSys, mapCoordSys, transParameter, CoordSysTransMethod.MTH_COORDINATE_FRAME);
									layerSelectionBounds = new Rectangle2D(points.getItem(0), points.getItem(1));
								} finally {
									transParameter.dispose();
								}
							}
						}

						// 直接用记录集的Bounds modified by zengwh 2012-1-6

						if (rect.isEmpty()) {
							rect = layerSelectionBounds;
						} else {
							rect.union(layerSelectionBounds);
						}
						recordset.dispose();
						recordset = null;
					}
				}
			}

			if (!rect.isEmpty()) {
				this.getMapControl().getMap().setViewBounds(rect);
				this.getMapControl().getMap().refresh();
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	public void clearSelection() {
		mapControlGeometrySelected(MapViewUIUtilities.clearAllSelection(this));
	}

	/**
	 * 全选
	 */
	public void selectAll() {
		mapControlGeometrySelected(MapViewUIUtilities.selectAllGeometry(this));
	}

	/**
	 * 反选
	 */
	public void reverseSelection() {
		mapControlGeometrySelected(MapViewUIUtilities.reverseSelection(this));
	}

	private void mapControlGeometrySelected(int selectGeometryCount) {
		try {
			// 设置状态栏选中对象数

			((SmLabel) getStatusbar().getComponent(SELECT_NUMBER)).setText(String.valueOf(selectGeometryCount));
			if (Application.getActiveApplication().getMainFrame().getPropertyManager().isUsable()) {
				setSelectedGeometryProperty();
			}
			updataLayersTreeSelection();
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	@Override
	public void updataSelectNumber() {
		((SmLabel) getStatusbar().getComponent(SELECT_NUMBER)).setText(String.valueOf(MapViewUIUtilities.calculateSelectNumber(this)));
		updataLayersTreeSelection();
	}

	private void updataLayersTreeSelection() {
		// 图上选择之后，选中图层管理器上对应图层的节点

		if (this.mapControl.getMap() != null && this.mapControl.getMap().getLayers().getCount() > 0) {
			ArrayList<TreePath> pathArray = new ArrayList<TreePath>();

			for (int i = 0; i < this.layersTree.getRowCount(); i++) {
				TreePath path = this.layersTree.getPathForRow(i);
				DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) path.getLastPathComponent();

				if (treeNode.getUserObject() instanceof TreeNodeData) {
					TreeNodeData nodeData = (TreeNodeData) treeNode.getUserObject();

					if (nodeData.getData() instanceof Layer) {
						Layer layer = (Layer) nodeData.getData();

						if (layer.isSelectable() && layer.getSelection() != null && layer.getSelection().getCount() > 0) {
							pathArray.add(path);
						}
					}
				}
			}

			if (!pathArray.isEmpty()) {
				this.layersTree.clearSelection();
				this.layersTree.setSelectionPaths(pathArray.toArray(new TreePath[pathArray.size()]));
				this.layersTree.updateUI();
			}
		}
	}

	@Override
	public void openMap(String mapName) {
		unRegisterEvents();
		Map map = this.getMapControl().getMap();
		map.open(mapName);
		registerEvents();
		map.refresh();
		UICommonToolkit.getLayersManager().setMap(map);
	}

	@Override
	public int getSelectedCount() {
		return Integer.valueOf(((SmLabel) getStatusbar().getComponent(SELECT_NUMBER)).getText());
	}

	/**
	 * 由于数据源/数据集/工作空间/对象等的属性面板结构上还不完善，目前这个方法开放为 public 供右键菜单属性的时候更新数据，优化方案正在思考中
	 */
	public void setSelectedGeometryProperty() {
		// 取出所有有选择对象的图层的选择集
		if (this.mapControl != null && this.mapControl.getMap() != null) {
			Selection[] selections = this.mapControl.getMap().findSelection(true);
			if (selections.length > 0) {
				// 默认取第一个选择集的第一个选中对象

				Selection selection = selections[0];
				int firstSelectedID = selection.get(0);
				DatasetVector datasetVector = selection.getDataset();
				Recordset recordset = RecordsetFinalizer.INSTANCE.queryRecordset(datasetVector, new int[]{firstSelectedID}, CursorType.DYNAMIC);

				if (recordset.getGeometry() != null) {
					Geometry geometry = recordset.getGeometry();
					IGeometry iGeometry = DGeometryFactory.create(geometry);
					ArrayList<IProperty> properties = new ArrayList<IProperty>();
					properties.add(GeometryPropertyFactory.getGeometryRecordsetPropertyControl(recordset));
					properties.add(GeometryPropertyFactory.getGeometrySpatialPropertyControl(geometry, datasetVector.getPrjCoordSys()));
					if (iGeometry != null && GeometryNodeFactory.isSupportGeometry(iGeometry)) {
						properties.add(GeometryPropertyFactory.getGeometryNodePropertyControl(recordset));
					}
					if (geometry instanceof GeoText || geometry instanceof GeoText3D) {
						properties.add(GeometryPropertyFactory.getGeometryGeoTextPropertyControl(recordset));
					}
					Application.getActiveApplication().getMainFrame().getPropertyManager().setProperty(properties.toArray(new IProperty[properties.size()]));
				} else {

					// 如果在 GeometrySelected 事件里删除了被选中对象，执行到这里 geometry 就是 null，此时应清空对象属性面板
					Application.getActiveApplication().getMainFrame().getPropertyManager().setProperty(null);
				}
			} else {
				Application.getActiveApplication().getMainFrame().getPropertyManager().setProperty(null);
			}
		} else {
			Application.getActiveApplication().getMainFrame().getPropertyManager().setProperty(null);
		}
	}

	private class LayersTreeSelectionListener implements TreeSelectionListener {

		@Override
		public void valueChanged(TreeSelectionEvent e) {
			LayersTreeSelectionChanged();
		}
	}

	/**
	 * Ctrl + Z / Ctrl + Y 等操作需要刷新操作数据已打开的属性表
	 */
	private void refreshOpenedTabularForms() {
		Layer[] activeLayers = getActiveLayers();

		if (activeLayers != null && activeLayers.length > 0) {
			for (int i = 0; i < activeLayers.length; i++) {
				if (activeLayers[i].getDataset() instanceof DatasetVector) {
					TabularUtilities.refreshTabularForm((DatasetVector) activeLayers[i].getDataset());
				}
			}
		}
	}

	/**
	 * 拖动实现将数据集添加到当前地图图层
	 */
	private void addDrag() {
		if (this.dropTargeted == null) {
			this.dropTargeted = new DropTarget(this, new FormMapDropTargetAdapter());
		} else if (this.dropTargeted.getComponent() == null) {
			this.dropTargeted.setComponent(this);
		}
	}

	private void removeDrag() {
		if (this.dropTargeted != null && this.dropTargeted.getComponent() != null) {
			this.dropTargeted.setComponent(null);
		}
	}

	/**
	 * 用于提供所涉及的 DropTarget 的 DnD 操作的通知
	 *
	 * @author xie
	 */
	private class FormMapDropTargetAdapter extends DropTargetAdapter {

		@Override
		public void drop(DropTargetDropEvent dtde) {
			try {
				// 将数据集添加到当前地图图层

				Transferable transferable = dtde.getTransferable();
				DataFlavor[] dataFlavors = dtde.getCurrentDataFlavors();
				for (int i = 0; i < dataFlavors.length; i++) {
					if (null != dataFlavors[i] && !dataFlavors[i].equals(DataFlavor.javaFileListFlavor) && null != transferable.getTransferData(dataFlavors[i])) {
						Dataset[] datasets = Application.getActiveApplication().getActiveDatasets();
						if (Application.getActiveApplication().getActiveForm() instanceof IFormMap) {
							IFormMap formMap = (IFormMap) Application.getActiveApplication().getActiveForm();
							Map map = formMap.getMapControl().getMap();
							MapViewUIUtilities.addDatasetsToMap(map, datasets, true);
						}
					}
				}
			} catch (Exception e) {
				Application.getActiveApplication().getOutput().output(e);
			}

		}

	}

	public AngleUnit getAngleUnit() {
		return angleUnit;
	}

	public void setAngleUnit(AngleUnit angleUnit) {
		this.angleUnit = angleUnit;
	}

	public AreaUnit getAreaUnit() {
		return areaUnit;
	}

	public void setAreaUnit(AreaUnit areaUnit) {
		this.areaUnit = areaUnit;
	}

	public LengthUnit getLengthUnit() {
		return lengthUnit;
	}

	public void setLengthUnit(LengthUnit lengthUnit) {
		this.lengthUnit = lengthUnit;
	}

}

