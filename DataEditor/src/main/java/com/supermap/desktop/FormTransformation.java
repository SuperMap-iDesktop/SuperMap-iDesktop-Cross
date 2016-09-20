package com.supermap.desktop;

import com.supermap.data.CoordSysTranslator;
import com.supermap.data.Dataset;
import com.supermap.data.Datasource;
import com.supermap.data.GeoCompound;
import com.supermap.data.GeoLine;
import com.supermap.data.GeoPoint;
import com.supermap.data.GeoStyle;
import com.supermap.data.GeoText;
import com.supermap.data.Geometry;
import com.supermap.data.Point2D;
import com.supermap.data.Point2Ds;
import com.supermap.data.PrjCoordSysType;
import com.supermap.data.Rectangle2D;
import com.supermap.data.Size2D;
import com.supermap.data.SymbolMarker;
import com.supermap.data.TextAlignment;
import com.supermap.data.TextPart;
import com.supermap.data.TextStyle;
import com.supermap.data.Transformation;
import com.supermap.data.TransformationError;
import com.supermap.data.TransformationMode;
import com.supermap.desktop.CtrlAction.transformationForm.FormTransformationTableModel;
import com.supermap.desktop.CtrlAction.transformationForm.TransformationBase;
import com.supermap.desktop.CtrlAction.transformationForm.TransformationReference;
import com.supermap.desktop.CtrlAction.transformationForm.TransformationTarget;
import com.supermap.desktop.CtrlAction.transformationForm.beans.FormTransformationSubFormType;
import com.supermap.desktop.CtrlAction.transformationForm.beans.TransformationAddObjectBean;
import com.supermap.desktop.Interface.IContextMenuManager;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.Interface.IFormTransformation;
import com.supermap.desktop.controls.utilities.ToolbarUIUtilities;
import com.supermap.desktop.dataeditor.DataEditorProperties;
import com.supermap.desktop.enums.WindowType;
import com.supermap.desktop.event.ActiveLayersChangedListener;
import com.supermap.desktop.exception.InvalidScaleException;
import com.supermap.desktop.implement.SmStatusbar;
import com.supermap.desktop.implement.SmTextField;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.FormBaseChild;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SortTable.SmSortTable;
import com.supermap.desktop.utilities.DoubleUtilities;
import com.supermap.desktop.utilities.MapControlUtilities;
import com.supermap.mapping.Layer;
import com.supermap.mapping.Map;
import com.supermap.mapping.TrackingLayer;
import com.supermap.ui.Action;
import com.supermap.ui.ActionChangedEvent;
import com.supermap.ui.ActionChangedListener;
import com.supermap.ui.MapControl;
import com.supermap.ui.TrackMode;
import com.supermap.ui.TrackedEvent;
import com.supermap.ui.TrackedListener;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author XiaJT
 */
public class FormTransformation extends FormBaseChild implements IFormTransformation, IFormMap {
	private JPopupMenu tableContextMenu;
	private JPopupMenu formTransformationContextMenu;
	private SmSortTable tablePoints;
	private JSplitPane splitPaneMapControls;
	private JSplitPane splitPaneMain;
	private FormTransformationTableModel formTransformationTableModel;
	private TransformationTarget transformationTarget;
	private TransformationReference transformationReference;
	private IFormMap currentForceWindow;
	private ArrayList<Object> transformationObjects = new ArrayList<>();
	private ArrayList<Object> transformationReferenceObjects = new ArrayList<>();

	private static final int STATE_BAR_MOUSE_PLACE = 1;
	private static final int STATE_BAR_PRJCOORSYS = 2;
	private static final int STATE_BAR_CENTER_X = 4;
	private static final int STATE_BAR_CENTER_Y = 5;
	private static final int STATE_BAR_SCALE = 7;
	private static final int STATE_BAR_Error = 9;

	private Color selectedColor = Color.blue;
	private Color unSelectedColor = Color.red;
	private Color UnUseColor = Color.gray;

	private TransformationMode transformationMode = TransformationMode.LINEAR;
	private Transformation transformation;

	private static final int MARKETWIDTH = 128;

	private MouseAdapter mapControlMouseAdapter = new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
			if ((e.getButton() == MouseEvent.BUTTON1 && MapControlUtilities.isCreateGeometry(getMapControl()))
					&& (getMapControl().getMap().getPrjCoordSys() != null && getMapControl().getMap().getPrjCoordSys().getType() == PrjCoordSysType.PCS_EARTH_LONGITUDE_LATITUDE)) {
				Point2D mousePosition = getMapControl().getMap().pixelToMap(e.getPoint());

				if (mousePosition.getX() > 180 || mousePosition.getX() < -180 || mousePosition.getY() > 90 || mousePosition.getY() < -90) {
					Application.getActiveApplication().getOutput().output(CommonProperties.getString("String_ExceedBounds"));
				}
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			initCenter(getMapControl());
			initScale(getMapControl());
		}

		@Override
		public void mousePressed(MouseEvent e) {
			Object source = e.getSource();
			boolean isChangeForceWindow = false;
			if (source != currentForceWindow.getMapControl()) {
				currentForceWindow.deactived();
				currentForceWindow = currentForceWindow == transformationTarget ? transformationReference : transformationTarget;
				currentForceWindow.actived();
				isChangeForceWindow = true;
			}
			if (e.isControlDown() && e.getButton() == 1) {
				int selectedModelRow = tablePoints.getSelectedModelRow();
				if (selectedModelRow != -1) {
					formTransformationTableModel.removePoint(selectedModelRow, getCurrentSubFormType());
				}
			}
			if (Application.getActiveApplication().getActiveForm() != FormTransformation.this || isChangeForceWindow) {
				Application.getActiveApplication().getMainFrame().getFormManager().resetActiveForm();
			}
			if (e.getButton() == MouseEvent.BUTTON3 && e.getClickCount() == 1) {
				// 右键
				if (isAddPointing()) {
					stopAddPoint();
				} else if ((getMapControl().getAction() == Action.SELECT || getMapControl().getAction() == Action.SELECT2 || getMapControl().getAction() == Action.SELECTCIRCLE) && getMapControl().getTrackMode() == TrackMode.EDIT && getIsShowPopupMenu() <= 0) {
					getFormTransformationContextMenu().show(getMapControl(), e.getX(), e.getY());
				}
			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			if (e.getSource() instanceof MapControl) {
				MapControl mapControl = (MapControl) e.getSource();
				initPrjCoorSys(mapControl);
				initScale(mapControl);
				initCenter(mapControl);
			}
		}

		@Override
		public void mouseExited(MouseEvent e) {
			if (e.getSource() instanceof MapControl) {
				initCenter((MapControl) e.getSource());
				initScale((MapControl) e.getSource());
			}
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			initCenter(getMapControl());
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			updatePrjCoorSysPlace(e);
		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			initCenter(getMapControl());
			initScale(getMapControl());
		}
	};
	private boolean isAddPointing;
	private TrackedListener addPointTrackedListener = new TrackedListener() {
		@Override
		public void tracked(TrackedEvent trackedEvent) {
			if (trackedEvent.getSource() != null && trackedEvent.getSource() instanceof MapControl) {
				MapControl source = (MapControl) trackedEvent.getSource();
				TransformationBase form = getFormByMapControl(source);
				addPoint(form, trackedEvent.getGeometry().getInnerPoint());
				pointValueChanged();
			}
		}
	};

	private void pointValueChanged() {
		changeTransformation(null);
		ToolbarUIUtilities.updataToolbarsState();
	}

	private ActionChangedListener addPointActionChangeListener = new ActionChangedListener() {
		@Override
		public void actionChanged(ActionChangedEvent actionChangedEvent) {
			if (actionChangedEvent.getNewAction() != Action.PAN && actionChangedEvent.getNewAction() != Action.PAN2 && actionChangedEvent.getNewAction() != Action.CREATEPOINT) {
				stopAddPoint();
			}
		}
	};
	private JScrollPane scrollPane;
	private static final String TRANSFORMATION_TRACKING_LAYER_TAG = "TransformationPoint_";

	private void initPrjCoorSys(MapControl mapControl) {
		SmTextField statusbarPrjCoorSys = (SmTextField) getStatusbar(STATE_BAR_PRJCOORSYS);
		statusbarPrjCoorSys.setText(mapControl.getMap().getPrjCoordSys().getName());
		statusbarPrjCoorSys.setCaretPosition(0);
	}

	public FormTransformation() {
		this(null);
	}

	public FormTransformation(String name) {
		this(name, null, null);
	}

	public FormTransformation(String name, Icon icon, Component component) {
		super(name, icon, component);
		setText(name);
		transformationTarget = new TransformationTarget(this);
		transformationReference = new TransformationReference(this);
		currentForceWindow = transformationTarget;
		currentForceWindow.actived();
		formTransformationTableModel = new FormTransformationTableModel();
		tablePoints = new SmSortTable();
		tablePoints.setModel(formTransformationTableModel);
		tablePoints.getColumnModel().getColumn(0).setMaxWidth(40);
		tablePoints.getColumnModel().getColumn(1).setMaxWidth(40);
		((DefaultTableCellRenderer) tablePoints.getDefaultRenderer(Integer.class)).setHorizontalAlignment(SwingConstants.CENTER);
		((JTextField) ((DefaultCellEditor) tablePoints.getDefaultEditor(Double.class)).getComponent()).setHorizontalAlignment(SwingConstants.CENTER);
		tablePoints.setDefaultRenderer(Double.class, new TableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				JLabel label = new JLabel();
				label.setHorizontalAlignment(SwingConstants.CENTER);
				if (value != null) {
					Double aDouble = (Double) value;
					label.setText(DoubleUtilities.toString(aDouble, 6));
				}
				if (isSelected) {
					label.setOpaque(true);
					label.setBackground(table.getSelectionBackground());
				}
				return label;
			}
		});
		if (Application.getActiveApplication().getMainFrame() != null) {
			IContextMenuManager manager = Application.getActiveApplication().getMainFrame().getContextMenuManager();
			this.formTransformationContextMenu = (JPopupMenu) manager.get("SuperMap.Desktop.FormTransformation.TransformationMapsContextMenu");
			this.tableContextMenu = (JPopupMenu) manager.get("SuperMap.Desktop.FormTransformation.TransformationItemsContextMenu");
		}
		initLayout();
		initListener();
		initCenter(getMapControl());
		initScale(getMapControl());
	}

	private void initLayout() {
		this.setLayout(new GridBagLayout());
		this.splitPaneMapControls = new JSplitPane();
		this.splitPaneMapControls.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		this.splitPaneMapControls.setLeftComponent(transformationTarget.getMapControl());
		this.splitPaneMapControls.setRightComponent(transformationReference.getMapControl());
		splitPaneMapControls.setResizeWeight(0.5);

		this.splitPaneMain = new JSplitPane();
		this.splitPaneMain.setOrientation(JSplitPane.VERTICAL_SPLIT);
		this.splitPaneMain.setLeftComponent(this.splitPaneMapControls);
		scrollPane = new JScrollPane(tablePoints);
		this.splitPaneMain.setRightComponent(scrollPane);
		splitPaneMain.setResizeWeight(1);

		this.add(this.splitPaneMain, new GridBagConstraintsHelper(0, 0, 1, 1).setFill(GridBagConstraints.BOTH).setWeight(1, 1));

		this.add(getStatusbar(), new GridBagConstraintsHelper(0, 1, 1, 1).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
	}

	private void initListener() {
		//region 一次性事件
		splitPaneMain.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				splitPaneMain.setDividerLocation(0.8);
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						if (transformationReferenceObjects.size() > 0) {
							transformationReference.addDatas(transformationReferenceObjects);
							transformationReferenceObjects.clear();
							transformationReference.getMapControl().getMap().viewEntire();
						}
						if (transformationObjects.size() > 0) {
							transformationTarget.addDatas(transformationObjects);
							transformationTarget.getMapControl().getMap().viewEntire();
							transformationObjects.clear();
						}
						initCenter(getMapControl());
						initScale(getMapControl());
						initPrjCoorSys(getMapControl());
						startAddPoint();
					}
				});
				splitPaneMain.removeComponentListener(this);
			}
		});
		//endregion
		addMapControlListener();
		formTransformationTableModel.addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				tableValueChanged(e);
			}
		});
		tablePoints.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3 && e.getClickCount() == 1) {
					int x = e.getX();
					tableContextMenu.show(tablePoints, x, e.getY());
				}
			}
		});
		tablePoints.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() == KeyEvent.VK_DELETE) {
					deleteTableSelectedRow();
				}
			}
		});
		tablePoints.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				tableSelectedChanged();
			}
		});
	}

	private void tableValueChanged(TableModelEvent e) {
		int lastRow = e.getLastRow();
		if (e.getType() == TableModelEvent.DELETE) {
			removeTrackingObject(lastRow, transformationTarget.getMapControl().getMap());
			removeTrackingObject(lastRow, transformationReference.getMapControl().getMap());
		} else if (e.getType() == TableModelEvent.UPDATE) {
			int column = e.getColumn();
			if (column == FormTransformationTableModel.COLUMN_ReferX || column == FormTransformationTableModel.COLUMN_ReferY ||
					column == FormTransformationTableModel.COLUMN_OriginalX || column == FormTransformationTableModel.COLUMN_OriginalY) {
				Object valueAt = tablePoints.getValueAt(lastRow, column);
				TransformationBase form = transformationTarget;
				if (column == FormTransformationTableModel.COLUMN_ReferX || column == FormTransformationTableModel.COLUMN_ReferY) {
					form = transformationReference;
				}
				removeTrackingObject(lastRow, form.getMapControl().getMap());

				if (valueAt != null) {
					Point2D point2D;
					if (form == transformationTarget) {
						point2D = formTransformationTableModel.getReferPoint(lastRow);
					} else {
						point2D = formTransformationTableModel.getOriginalPoint(lastRow);
					}
					addPoint(form, point2D);
				}
			} else if (column == FormTransformationTableModel.COLUMN_IS_SELECTED) {
			}
		}
		pointValueChanged();
	}

	private void tableSelectedChanged() {
		refreshFormTrackingLayer(transformationTarget);
		refreshFormTrackingLayer(transformationReference);
	}

	private void refreshFormTrackingLayer(TransformationBase form) {
		TrackingLayer trackingLayer = form.getMapControl().getMap().getTrackingLayer();

		// 清除当前选中
		List<String> lastSelectedCompounds = form.getLastSelectedGeometry();
		if (lastSelectedCompounds.size() > 0) {
			for (String lastSelectedCompound : lastSelectedCompounds) {
				for (int i = trackingLayer.getCount() - 1; i >= 0; i--) {
					String tag = trackingLayer.getTag(i);
					if (tag.equals(lastSelectedCompound)) {
						Geometry geoCompound = trackingLayer.get(i);
						setGeoCompoundColor(geoCompound, ((Boolean) tablePoints.getValueAt(getTrackingLayerNumber(tag) - 1, FormTransformationTableModel.COLUMN_IS_SELECTED)) ? unSelectedColor : UnUseColor);
						trackingLayer.remove(i);
						trackingLayer.add(geoCompound, tag);
						break;
					}
				}
			}
		}
		form.setSelectedGeoCompoundTags();

		int[] selectedModelRows = tablePoints.getSelectedModelRows();
		if (selectedModelRows.length > 0) {
			List<String> selectedTags = new ArrayList<>();
			for (int i = selectedModelRows.length - 1; i >= 0; i--) {
				String tag = getTag(selectedModelRows[i] + 1);
				int index = getIndexByTag(trackingLayer, tag);
				if (index != -1) {
					Geometry geometry = trackingLayer.get(index);
					setGeoCompoundColor(geometry, selectedColor);
					trackingLayer.remove(index);
					trackingLayer.add(geometry, tag);
					selectedTags.add(tag);
				}
			}
			form.setSelectedGeoCompoundTags(selectedTags.toArray(new String[selectedTags.size()]));
		}
		form.getMapControl().getMap().refreshTrackingLayer();
	}

	private int getIndexByTag(TrackingLayer trackingLayer, String tag) {
		for (int i = trackingLayer.getCount() - 1; i >= 0; i--) {
			if (trackingLayer.getTag(i).equals(tag)) {
				return i;
			}
		}
		return -1;
	}

	private void removeTrackingObject(int index, Map map) {
		TrackingLayer targetTrackingLayer = map.getTrackingLayer();
		for (int i = targetTrackingLayer.getCount() - 1; i >= 0; i--) {
			int number = getTrackingLayerNumber(targetTrackingLayer.getTag(i));
			if (number == index + 1) {
				targetTrackingLayer.remove(i);
			} else if (number > index + 1) {
				Geometry geometry = targetTrackingLayer.get(i);
				Geometry trackingGeometry = getTrackingGeometry(number - 1, geometry.getInnerPoint());
				targetTrackingLayer.remove(i);
				targetTrackingLayer.add(trackingGeometry, getTag(number - 1));
			}
		}
		map.refreshTrackingLayer();
	}

	private int getTrackingLayerNumber(String tag) {
		if (tag == null) {
			return -1;
		}
		String[] split = tag.split(TRANSFORMATION_TRACKING_LAYER_TAG);
		if (split.length == 2) {
			try {
				return Integer.valueOf(split[1]);
			} catch (NumberFormatException e) {
				return -1;
			}
		}
		return -1;
	}

	public void deleteTableSelectedRow() {
		formTransformationTableModel.remove(tablePoints.getSelectedModelRows());
	}

	@Override
	public void setAction(Action action) {
		if (isAddPointing()) {
			stopAddPoint();
		}
		transformationTarget.getMapControl().setAction(action);
		transformationReference.getMapControl().setAction(action);
	}

	private void addMapControlListener() {
		MouseListener[] mouseListeners = transformationTarget.getMapControl().getMouseListeners();
		transformationTarget.getMapControl().addMouseListener(mapControlMouseAdapter);
		for (MouseListener mouseListener : mouseListeners) {
			transformationTarget.getMapControl().removeMouseListener(mouseListener);
			transformationTarget.getMapControl().addMouseListener(mouseListener);
		}
		MouseListener[] mouseListeners1 = transformationReference.getMapControl().getMouseListeners();
		transformationReference.getMapControl().addMouseListener(mapControlMouseAdapter);
		for (MouseListener mouseListener : mouseListeners1) {
			transformationReference.getMapControl().removeMouseListener(mouseListener);
			transformationReference.getMapControl().addMouseListener(mouseListener);
		}
		transformationTarget.getMapControl().addMouseMotionListener(mapControlMouseAdapter);
		transformationReference.getMapControl().addMouseMotionListener(mapControlMouseAdapter);

		transformationTarget.getMapControl().addMouseWheelListener(mapControlMouseAdapter);
		transformationReference.getMapControl().addMouseWheelListener(mapControlMouseAdapter);
	}

	private void updatePrjCoorSysPlace(MouseEvent e) {
		try {
			if (!(e.getSource() instanceof MapControl)) {
				return;
			}
			MapControl mapControl = (MapControl) e.getSource();
			final DecimalFormat format = new DecimalFormat("######0.000000");
			PrjCoordSysType coordSysType = this.getMapControl().getMap().getPrjCoordSys().getType();
			Point pointMouse = e.getPoint();
			Point2D point = mapControl.getMap().pixelToMap(pointMouse);

			String x;
			if (Double.isInfinite(point.getX())) {
				x = DataEditorProperties.getString("String_Infinite");
			} else if (Double.isNaN(point.getX())) {
				x = DataEditorProperties.getString("String_NotANumber");
			} else {
				x = format.format(point.getX());
			}
			String y;
			if (Double.isInfinite(point.getY())) {
				y = DataEditorProperties.getString("String_Infinite");
			} else if (Double.isNaN(point.getY())) {
				y = DataEditorProperties.getString("String_NotANumber");
			} else {
				y = format.format(point.getY());
			}

			// XY坐标信息

			String XYInfo = MessageFormat.format(DataEditorProperties.getString("String_String_PrjCoordSys_XYInfo"), x, y);

			// 经纬度信息

			String latitudeInfo = MessageFormat.format(DataEditorProperties.getString("String_PrjCoordSys_LongitudeLatitude"), getFormatCoordinates(point.getX()),
					getFormatCoordinates(point.getY()));

			if (coordSysType == PrjCoordSysType.PCS_NON_EARTH) {
				// 平面
				SmTextField statusbar = (SmTextField) getStatusbar(STATE_BAR_MOUSE_PLACE);
				statusbar.setText(XYInfo);
			} else if (coordSysType == PrjCoordSysType.PCS_EARTH_LONGITUDE_LATITUDE) {
				// 地理
				SmTextField statusbar = (SmTextField) getStatusbar(STATE_BAR_MOUSE_PLACE);
				statusbar.setText(latitudeInfo);
			} else {
				// 投影
				Point2Ds point2Ds = new Point2Ds();
				point2Ds.add(point);

				CoordSysTranslator.inverse(point2Ds, this.getMapControl().getMap().getPrjCoordSys());
				latitudeInfo = MessageFormat.format(DataEditorProperties.getString("String_PrjCoordSys_LongitudeLatitude"),
						getFormatCoordinates(point2Ds.getItem(0).getX()), getFormatCoordinates(point2Ds.getItem(0).getY()));
				SmTextField statusbar = (SmTextField) getStatusbar(STATE_BAR_MOUSE_PLACE);
				statusbar.setText(XYInfo + latitudeInfo);
			}
			// 设置光标位置

			((SmTextField) getStatusbar(STATE_BAR_MOUSE_PLACE)).setCaretPosition(0);

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

		return MessageFormat.format(DataEditorProperties.getString("String_LongitudeLatitude"), angles, min, format.format(pointTemp));
	}

	private void initCenter(MapControl mapControl) {
		DecimalFormat format = new DecimalFormat("######0.####");
		String x = Double.isNaN(mapControl.getMap().getCenter().getX()) ? DataEditorProperties.getString("String_NotANumber") : format.format(mapControl.getMap()
				.getCenter().getX());
		String y = Double.isNaN(mapControl.getMap().getCenter().getY()) ? DataEditorProperties.getString("String_NotANumber") : format.format(mapControl.getMap()
				.getCenter().getY());
		((SmTextField) getStatusbar(STATE_BAR_CENTER_X)).setText(x);
		((SmTextField) getStatusbar(STATE_BAR_CENTER_X)).setCaretPosition(0);
		((SmTextField) getStatusbar(STATE_BAR_CENTER_Y)).setText(y);
		((SmTextField) getStatusbar(STATE_BAR_CENTER_Y)).setCaretPosition(0);

	}

	private void initScale(MapControl mapControl) {
		String scale = null;
		try {
			scale = new ScaleModel(mapControl.getMap().getScale()).toString();
		} catch (InvalidScaleException e) {
			e.printStackTrace();
		}
		if ("NONE".equals(scale)) {
			scale = String.valueOf(mapControl.getMap().getScale());
		}
		((SmTextField) getStatusbar(STATE_BAR_SCALE)).setText(scale);
		((SmTextField) getStatusbar(STATE_BAR_SCALE)).setCaretPosition(0);
	}

	@Override
	public String getText() {
		return null;
	}


	@Override
	public WindowType getWindowType() {
		return WindowType.TRANSFORMATION;
	}

	@Override
	public boolean save() {
		return true;
	}

	@Override
	public boolean save(boolean notify, boolean isNewWindow) {
		return true;
	}

	@Override
	public boolean saveFormInfos() {
		return false;
	}

	@Override
	public boolean saveAs(boolean isNewWindow) {
		return false;
	}

	@Override
	public boolean isNeedSave() {
		return false;
	}

	@Override
	public void setNeedSave(boolean needSave) {

	}

	@Override
	public boolean isActivated() {
		return false;
	}

	@Override
	public void actived() {
		currentForceWindow.actived();
		Application.getActiveApplication().getMainFrame().getPropertyManager().setProperty(null);
	}

	@Override
	public void deactived() {
		currentForceWindow.deactived();
	}

	@Override
	public void windowShown() {


	}

	@Override
	public void windowHidden() {

	}

	@Override
	public void clean() {
		transformationTarget.clean();
		transformationReference.clean();
	}


	@Override
	public void addTransformationDataset(Dataset transformationDataset, Datasource resultDatasource, String resultDatasetName) {
		TransformationAddObjectBean transformationAddObjectBean = new TransformationAddObjectBean(transformationDataset, resultDatasource, resultDatasetName);
		ArrayList<Object> datas = new ArrayList<>();
		datas.add(transformationAddObjectBean);
		if (getWidth() != 0) {
			transformationTarget.addDatas(datas);
		} else {
			if (transformationObjects == null) {
				transformationObjects = new ArrayList<>();
			}
			transformationObjects.add(transformationAddObjectBean);
		}
	}

	@Override
	public void addTransformationMap(Map map) {
		TransformationAddObjectBean transformationAddObjectBean = new TransformationAddObjectBean(map);
		ArrayList<Object> datas = new ArrayList<>();
		datas.add(transformationAddObjectBean);
		if (getWidth() != 0) {
			transformationTarget.addDatas(datas);
		} else {
			if (transformationObjects == null) {
				transformationObjects = new ArrayList<>();
			}
			transformationObjects.add(transformationAddObjectBean);
		}
	}

	@Override
	public void addReferenceObjects(List<Object> listObjects) {
		if (getWidth() == 0) {
			for (Object listObject : listObjects) {
				transformationReferenceObjects.add(listObject);
			}
		} else {
			transformationReference.addDatas(listObjects);
		}
	}

	@Override
	public void addTargetObjects(ArrayList<Object> targetObject) {
		for (int i = 0; i < targetObject.size(); i++) {
			Object item = targetObject.get(i);
			if (item instanceof Map) {
				targetObject.set(i, new TransformationAddObjectBean(((Map) item)));
			} else if (item instanceof Dataset) {
				targetObject.set(i, new TransformationAddObjectBean(((Dataset) item), null, null));
			}
		}
		transformationTarget.addDatas(targetObject);
	}

	@Override
	public void startAddPoint() {
		if (!isAddPointing()) {
			isAddPointing = true;
			transformationTarget.getMapControl().setTrackMode(TrackMode.TRACK);
			transformationReference.getMapControl().setTrackMode(TrackMode.TRACK);
			transformationTarget.getMapControl().setAction(Action.CREATEPOINT);
			transformationReference.getMapControl().setAction(Action.CREATEPOINT);
			initAddPointListeners();
		}
	}

	private void stopAddPoint() {
		transformationTarget.getMapControl().setTrackMode(TrackMode.EDIT);
		transformationReference.getMapControl().setTrackMode(TrackMode.EDIT);
		transformationTarget.getMapControl().setAction(Action.SELECT2);
		transformationReference.getMapControl().setAction(Action.SELECT2);
		removeAddPointListeners();
		isAddPointing = false;
	}

	public FormTransformationSubFormType getCurrentSubFormType() {
		return getSubFormTypeByForm((TransformationBase) currentForceWindow);
	}

	private FormTransformationSubFormType getSubFormTypeByForm(TransformationBase form) {
		return form == transformationTarget ? FormTransformationSubFormType.Target : FormTransformationSubFormType.Reference;
	}

	private void initAddPointListeners() {
		transformationTarget.getMapControl().addTrackedListener(addPointTrackedListener);
		transformationReference.getMapControl().addTrackedListener(addPointTrackedListener);
		transformationTarget.getMapControl().addActionChangedListener(addPointActionChangeListener);
		transformationReference.getMapControl().addActionChangedListener(addPointActionChangeListener);
	}

	private void removeAddPointListeners() {
		transformationTarget.getMapControl().removeTrackedListener(addPointTrackedListener);
		transformationReference.getMapControl().removeTrackedListener(addPointTrackedListener);
		transformationTarget.getMapControl().removeActionChangedListener(addPointActionChangeListener);
		transformationReference.getMapControl().removeActionChangedListener(addPointActionChangeListener);
	}

	private TransformationBase getFormByMapControl(MapControl mapControl) {
		return transformationTarget.getMapControl() == mapControl ? transformationTarget : transformationReference;
	}

	private void addPoint(TransformationBase form, Point2D point) {
		int index = formTransformationTableModel.getPointCount(getSubFormTypeByForm(form)) + 1;
		Geometry trackingGeometry = getTrackingGeometry(index, point);
		TrackingLayer trackingLayer = form.getMapControl().getMap().getTrackingLayer();
		String tag = getTag(index);
		trackingLayer.add(trackingGeometry, tag);
		formTransformationTableModel.addPoint(getSubFormTypeByForm(form), point);
		form.getMapControl().getMap().refreshTrackingLayer();
		scrollPane.scrollRectToVisible(tablePoints.getCellRect(index - 1, 0, true));
		tablePoints.clearSelection();
		tablePoints.setRowSelectionInterval(index - 1, index - 1);
	}

	private String getTag(int index) {
		return TRANSFORMATION_TRACKING_LAYER_TAG + index;
	}

	private void setGeoCompoundColor(Geometry geoCompound, Color currentColor) {
		if (!(geoCompound instanceof GeoCompound)) {
			return;
		}
		for (int i = 0; i < ((GeoCompound) geoCompound).getPartCount(); i++) {
			Geometry part = ((GeoCompound) geoCompound).getPart(i);
			if (part instanceof GeoPoint) {
				part.getStyle().setSymbolMarker(getCrossMarket(currentColor));
			} else if (part instanceof GeoText) {
				((GeoText) part).getTextStyle().setForeColor(currentColor);
			}
		}
	}

	//region 获取跟踪层添加对象
	private Geometry getTrackingGeometry(int index, Point2D point) {
		GeoCompound geoCompound = new GeoCompound();
		GeoPoint geoPoint = new GeoPoint(point);
		geoPoint.setStyle(getPointStyle());
		GeoText geoText = marketLabel(index, point, unSelectedColor);
		geoCompound.addPart(geoPoint);
		geoCompound.addPart(geoText);
		return geoCompound;
	}

	private GeoStyle getPointStyle() {
		GeoStyle style = new GeoStyle();
		style.setSymbolMarker(getCrossMarket(unSelectedColor));
		style.setMarkerSize(new Size2D(10, 10));
		return style;
	}

	private SymbolMarker getCrossMarket(Color color) {
		SymbolMarker sm = new SymbolMarker();

		Rectangle2D rect = new Rectangle2D(0, 0, MARKETWIDTH, MARKETWIDTH);
		GeoCompound compound = new GeoCompound();
		try {
			int start = 0;
			int end = MARKETWIDTH - start;
			Point2Ds pnts = new Point2Ds();
			pnts.add(new Point2D(start, MARKETWIDTH / 2));
			pnts.add(new Point2D(end, MARKETWIDTH / 2));
			GeoLine line = new GeoLine(pnts);
			GeoStyle lineStyle = new GeoStyle();
			lineStyle.setLineColor(color);
			lineStyle.setLineWidth(5);
			line.setStyle(lineStyle);
			compound.addPart(line);

			pnts = new Point2Ds();
			pnts.add(new Point2D(MARKETWIDTH / 2, start));
			pnts.add(new Point2D(MARKETWIDTH / 2, end));
			line = new GeoLine(pnts);
			line.setStyle(lineStyle);
			compound.addPart(line);
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		sm.fromGeometry(compound, rect);
		return sm;
	}

	private GeoText marketLabel(int index, Point2D innerPoint, Color color) {
		GeoText text = new GeoText();
		TextPart part = new TextPart();
		part.setText(" " + String.valueOf(index));
		part.setAnchorPoint(innerPoint);

		text.addPart(part);
		getTextStyle(color, text);
		return text;
	}

	private void getTextStyle(Color color, GeoText text) {
		TextStyle textStyle = new TextStyle();
		textStyle.setForeColor(color);
		textStyle.setSizeFixed(true);
		textStyle.setAlignment(TextAlignment.BOTTOMLEFT);
		textStyle.setFontWidth(5);
		textStyle.setFontHeight(5);
		text.setTextStyle(textStyle);
	}
	//endregion

	@Override
	public boolean isAddPointing() {
		return isAddPointing;
	}

	@Override
	public JTable getTable() {
		return tablePoints;
	}

	@Override
	public void centerOriginal() {
		if (tablePoints.getSelectedRow() != -1) {
			int selectedRow = tablePoints.getSelectedRow();
			Point2D originalPoint = formTransformationTableModel.getOriginalPoint(selectedRow);
			if (originalPoint != null && !transformationTarget.getMapControl().getMap().getViewBounds().contains(originalPoint)) {
				transformationTarget.getMapControl().getMap().setCenter(originalPoint);
				transformationTarget.getMapControl().getMap().refresh();
				if (currentForceWindow == transformationTarget) {
					initCenter(transformationTarget.getMapControl());
				}
			}

			Point2D referPoint = formTransformationTableModel.getReferPoint(selectedRow);
			if (referPoint != null && !transformationReference.getMapControl().getMap().getViewBounds().contains(referPoint)) {
				transformationReference.getMapControl().getMap().setCenter(referPoint);
				transformationReference.getMapControl().getMap().refresh();
				if (currentForceWindow == transformationReference) {
					initCenter(transformationReference.getMapControl());
				}
			}
		}
	}

	@Override
	public MapControl getMapControl() {
		return currentForceWindow.getMapControl();
	}

	@Override
	public Layer[] getActiveLayers() {
		return currentForceWindow.getActiveLayers();
	}

	@Override
	public void setActiveLayers(Layer... activeLayers) {
		currentForceWindow.setActiveLayers(activeLayers);
	}

	@Override
	public void addActiveLayersChangedListener(ActiveLayersChangedListener listener) {
		currentForceWindow.addActiveLayersChangedListener(listener);
	}

	@Override
	public void removeActiveLayersChangedListener(ActiveLayersChangedListener listener) {
		currentForceWindow.removeActiveLayersChangedListener(listener);
	}

	@Override
	public void removeActiveLayersByDatasets(Dataset... datasets) {
		transformationTarget.removeActiveLayersByDatasets(datasets);
		transformationReference.removeActiveLayersByDatasets(datasets);
	}

	private JPopupMenu getFormTransformationContextMenu() {
		return formTransformationContextMenu;
	}

	private JPopupMenu getTableContextMenu() {
		return tableContextMenu;
	}

	private JComponent getStatusbar(int i) {
		return ((JComponent) super.getStatusbar().get(i));
	}

	@Override
	public SmStatusbar getStatusbar() {

		SmStatusbar statusbar = super.getStatusbar();
		java.util.List<Component> list = new ArrayList<>();
		for (int i = 0; i < statusbar.getCount(); i++) {
			list.add(((Component) statusbar.get(i)));
		}
		((JTextField) list.get(1)).setEditable(false);
		((JTextField) list.get(2)).setEditable(false);
		((JTextField) list.get(4)).setEditable(false);
		((JTextField) list.get(5)).setEditable(false);
		((JTextField) list.get(7)).setEditable(false);
		((JTextField) list.get(9)).setEditable(false);
		statusbar.removeAll();
		statusbar.setLayout(new GridBagLayout());
		// label鼠标位置:
		statusbar.add(list.get(0), new GridBagConstraintsHelper(0, 0, 1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER)
				.setWeight(0, 1));
		// textfield 鼠标位置
		statusbar.add(list.get(1), new GridBagConstraintsHelper(1, 0, 1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER)
				.setWeight(1, 1));
		// textfield 投影系统名称
		statusbar.add(list.get(2), new GridBagConstraintsHelper(2, 0, 1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER)
				.setWeight(1, 1));
		// label 中心点:
		statusbar.add(list.get(3), new GridBagConstraintsHelper(3, 0, 1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER)
				.setWeight(0, 1));
		// textfield 中心点X
		Dimension preferredSize = new Dimension(80, list.get(4).getHeight());
		list.get(4).setMinimumSize(preferredSize);
		list.get(4).setPreferredSize(preferredSize);
		list.get(4).setMaximumSize(preferredSize);
		statusbar.add(list.get(4), new GridBagConstraintsHelper(4, 0, 1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER)
				.setWeight(0, 1));
		// textfield 中心点Y
		list.get(5).setMinimumSize(preferredSize);
		list.get(5).setPreferredSize(preferredSize);
		list.get(5).setMaximumSize(preferredSize);
		statusbar.add(list.get(5), new GridBagConstraintsHelper(5, 0, 1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER)
				.setWeight(0, 1));
		// label 比例尺:
		statusbar.add(list.get(6), new GridBagConstraintsHelper(6, 0, 1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER)
				.setWeight(0, 1));
		// textfield 比例尺
		list.get(7).setMaximumSize(preferredSize);
		list.get(7).setPreferredSize(preferredSize);
		list.get(7).setMinimumSize(preferredSize);
		statusbar.add(list.get(7), new GridBagConstraintsHelper(7, 0, 1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER)
				.setWeight(0, 1));
		// label 总均方根误差:
		statusbar.add(list.get(8), new GridBagConstraintsHelper(8, 0, 1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER).setWeight(0, 1));
		// textfield 总均方根误差
		list.get(9).setMaximumSize(preferredSize);
		list.get(9).setMinimumSize(preferredSize);
		list.get(9).setPreferredSize(preferredSize);
		statusbar.add(list.get(9), new GridBagConstraintsHelper(9, 0, 1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER).setWeight(0, 1));
		return statusbar;
	}

	@Override
	public void dontShowPopupMenu() {
		currentForceWindow.dontShowPopupMenu();
	}

	@Override
	public void showPopupMenu() {
		currentForceWindow.showPopupMenu();
	}

	@Override
	public Color getSelectedColor() {
		return selectedColor;
	}

	@Override
	public void setSelectedColor(Color selectedColor) {
		if (selectedColor != this.selectedColor) {
			this.selectedColor = selectedColor;
			// TODO: 2016/9/18 颜色改变了！
		}
	}

	@Override
	public Color getUnSelectedColor() {
		return unSelectedColor;
	}

	@Override
	public void setUnSelectedColor(Color unSelectedColor) {
		this.unSelectedColor = unSelectedColor;
	}

	@Override
	public Color getUnUseColor() {
		return UnUseColor;
	}

	@Override
	public void setUnUseColor(Color unUseColor) {
		UnUseColor = unUseColor;
	}

	@Override
	public TransformationMode getTransformationMode() {
		return transformationMode;
	}

	@Override
	public void setTransformationMode(TransformationMode transformationMode) {
		this.transformationMode = transformationMode;
		changeTransformation(null);
	}

	@Override
	public Transformation getTransformation() {
		return transformation;
	}

	@Override
	public void setTransformation(Transformation transformation) {
		changeTransformation(transformation);
	}

	@Override
	public Object[] getTransformationObjects() {
		return transformationTarget.getTransformationObjects();
	}


	private void changeTransformation(Transformation transformation) {
		if (this.transformation != null) {
			this.transformation.dispose();
		}
		this.transformation = transformation;
		initStateBarError();
		ToolbarUIUtilities.updataToolbarsState();
	}

	private void initStateBarError() {
		if (transformation == null) {
			((SmTextField) getStatusbar(STATE_BAR_Error)).setText("");
		} else {
			TransformationError error = transformation.getError();
			((SmTextField) getStatusbar(STATE_BAR_Error)).setText(DoubleUtilities.toString(error.getTotalRMS()));
			((SmTextField) getStatusbar(STATE_BAR_Error)).setCaretPosition(0);
			error.dispose();
		}
	}

	@Override
	public String toXml() {

		return null;
	}

	@Override
	public boolean fromXml() {
		return false;
	}

	//region 不支持的方法
	@Override
	public int getIsShowPopupMenu() {
		return currentForceWindow.getIsShowPopupMenu();
	}

	@Override
	public void updataSelectNumber() {

	}

	@Override
	public void setSelectedGeometryProperty() {

	}

	@Override
	public void openMap(String mapName) {

	}

	@Override
	public int getSelectedCount() {
		return 0;
	}

	@Override
	public void removeLayers(Layer[] activeLayers) {
		currentForceWindow.removeLayers(activeLayers);
	}

	@Override
	public void setText(String text) {

	}

	@Override
	public void setVisibleScales(double[] scales) {
		getMapControl().getMap().setVisibleScales(scales);
		// TODO: 2016/9/10 下拉框刷新
	}

	@Override
	public void setVisibleScalesEnabled(boolean isVisibleScalesEnabled) {
		getMapControl().getMap().setVisibleScalesEnabled(isVisibleScalesEnabled);
		// TODO: 2016/9/10 下拉框刷新
	}
	//endregion
}
