package com.supermap.desktop;

import com.supermap.data.CoordSysTranslator;
import com.supermap.data.Dataset;
import com.supermap.data.Datasource;
import com.supermap.data.Point2D;
import com.supermap.data.Point2Ds;
import com.supermap.data.PrjCoordSysType;
import com.supermap.desktop.CtrlAction.transformationForm.FormTransformationTableModel;
import com.supermap.desktop.CtrlAction.transformationForm.TransformationBean;
import com.supermap.desktop.CtrlAction.transformationForm.TransformationMain;
import com.supermap.desktop.CtrlAction.transformationForm.TransformationReference;
import com.supermap.desktop.Interface.IContextMenuManager;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.Interface.IFormTransformation;
import com.supermap.desktop.dataeditor.DataEditorProperties;
import com.supermap.desktop.enums.WindowType;
import com.supermap.desktop.event.ActiveLayersChangedListener;
import com.supermap.desktop.exception.InvalidScaleException;
import com.supermap.desktop.implement.SmStatusbar;
import com.supermap.desktop.implement.SmTextField;
import com.supermap.desktop.ui.FormBaseChild;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SortTable.SmSortTable;
import com.supermap.mapping.Layer;
import com.supermap.mapping.Map;
import com.supermap.ui.MapControl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
	private JTable tablePoints;
	private JSplitPane splitPaneMapControls;
	private JSplitPane splitPaneMain;
	private FormTransformationTableModel formTransformationTableModel;
	private TransformationMain transformationMain;
	private TransformationReference transformationReference;
	private IFormMap currentForceWindow;
	private ArrayList<Object> transformationObjects = new ArrayList<>();
	private ArrayList<Object> transformationReferenceObjects = new ArrayList<>();

	private static final int STATE_BAR_MOUSE_PLACE = 1;
	private static final int STATE_BAR_PRJCOORSYS = 2;
	private static final int STATE_BAR_CENTER_X = 4;
	private static final int STATE_BAR_CENTER_Y = 5;
	private static final int STATE_BAR_SCALE = 7;

	private MouseAdapter mapControlMouseAdapter = new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {

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
				currentForceWindow = currentForceWindow == transformationMain ? transformationReference : transformationMain;
				currentForceWindow.actived();
				isChangeForceWindow = true;
			}
			if (Application.getActiveApplication().getActiveForm() != FormTransformation.this || isChangeForceWindow) {
				Application.getActiveApplication().getMainFrame().getFormManager().resetActiveForm();
			}
			if (e.getButton() == MouseEvent.BUTTON3 && e.getClickCount() == 1) {
				getFormTransformationContextMenu().show(getMapControl(), e.getX(), e.getY());
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
		transformationMain = new TransformationMain(this);
		transformationReference = new TransformationReference(this);
		currentForceWindow = transformationMain;
		currentForceWindow.actived();
		formTransformationTableModel = new FormTransformationTableModel();
		tablePoints = new SmSortTable();
		tablePoints.setModel(formTransformationTableModel);
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
		this.splitPaneMapControls.setLeftComponent(transformationMain.getMapControl());
		this.splitPaneMapControls.setRightComponent(transformationReference.getMapControl());
		splitPaneMapControls.setResizeWeight(0.5);

		this.splitPaneMain = new JSplitPane();
		this.splitPaneMain.setOrientation(JSplitPane.VERTICAL_SPLIT);
		this.splitPaneMain.setLeftComponent(this.splitPaneMapControls);
		this.splitPaneMain.setRightComponent(new JScrollPane(tablePoints));
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
							transformationMain.addDatas(transformationObjects);
							transformationMain.getMapControl().getMap().viewEntire();
							transformationObjects.clear();
						}
						initCenter(getMapControl());
						initScale(getMapControl());
						initPrjCoorSys(getMapControl());
					}
				});
				splitPaneMain.removeComponentListener(this);
			}
		});
		//endregion
		addMapControlListener();
	}

	private void addMapControlListener() {
		transformationMain.getMapControl().addMouseListener(mapControlMouseAdapter);
		transformationReference.getMapControl().addMouseListener(mapControlMouseAdapter);

		transformationMain.getMapControl().addMouseMotionListener(mapControlMouseAdapter);
		transformationReference.getMapControl().addMouseMotionListener(mapControlMouseAdapter);

		transformationMain.getMapControl().addMouseWheelListener(mapControlMouseAdapter);
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
		transformationMain.clean();
		transformationReference.clean();
	}


	@Override
	public void addTransformationDataset(Dataset transformationDataset, Datasource resultDatasource, String resultDatasetName) {
		TransformationBean transformationBean = new TransformationBean(transformationDataset, resultDatasource, resultDatasetName);
		ArrayList<Object> datas = new ArrayList<>();
		datas.add(transformationBean);
		if (getWidth() != 0) {
			transformationMain.addDatas(datas);
		} else {
			transformationObjects = new ArrayList<>();
			transformationObjects.add(transformationBean);
		}
	}

	@Override
	public void addTransformationMap(Map map) {
		TransformationBean transformationBean = new TransformationBean(map);
		ArrayList<Object> datas = new ArrayList<>();
		datas.add(transformationBean);
		if (getWidth() != 0) {
			transformationMain.addDatas(datas);
		} else {
			transformationObjects = new ArrayList<>();
			transformationObjects.add(transformationBean);
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
		transformationMain.removeActiveLayersByDatasets(datasets);
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
		return statusbar;
	}

	//region 不支持的方法
	@Override
	public void dontShowPopupMenu() {

	}

	@Override
	public void showPopupMenu() {

	}

	@Override
	public int getIsShowPopupMenu() {
		return 0;
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
	//endregion
}
