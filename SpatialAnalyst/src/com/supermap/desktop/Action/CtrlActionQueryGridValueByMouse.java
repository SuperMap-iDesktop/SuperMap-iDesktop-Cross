package com.supermap.desktop.Action;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.text.MessageFormat;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetGrid;
import com.supermap.data.DatasetImage;
import com.supermap.data.Datasource;
import com.supermap.data.PixelFormat;
import com.supermap.data.Point2D;
import com.supermap.data.Rectangle2D;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.event.ActiveFormChangedEvent;
import com.supermap.desktop.event.ActiveFormChangedListener;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.spatialanalyst.SpatialAnalystProperties;
import com.supermap.mapping.Layer;
import com.supermap.mapping.Layers;
import com.supermap.mapping.Map;
import com.supermap.mapping.MapClosedEvent;
import com.supermap.mapping.MapClosedListener;
import com.supermap.ui.Action;
import com.supermap.ui.MapControl;

public class CtrlActionQueryGridValueByMouse extends CtrlAction {
	private transient TransparentBackground transparentBackground;
	private transient MapControl mapControl;
	private IFormMap formMap;
	private final DecimalFormat format = new DecimalFormat("######0.000000");
	private String avtiveFormMap = "";

	private void hideTransparentBackground() {
		// 允许弹出右键菜单
		formMap.showPopupMenu();
		transparentBackground.setVisible(false);
		TransparentBackground.queryGridMap.remove(mapControl.getMap().getName());
	}

	KeyAdapter keyAdapter = new KeyAdapter() {
		@Override
		public void keyReleased(KeyEvent e) {
			if (e.getKeyChar() == KeyEvent.VK_ESCAPE) {
				hideTransparentBackground();
			}
		}
	};

	MouseAdapter mouseAdapter = new MouseAdapter() {
		@Override
		public void mousePressed(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON3) {
				hideTransparentBackground();
			}
		}
	};

	MouseAdapter mapControlMouseMoved = new MouseAdapter() {
		@Override
		public void mouseMoved(MouseEvent arg0) {
			abstractMapcontrolMouseMoved(format, arg0);
		}
	};

	public CtrlActionQueryGridValueByMouse(IBaseItem caller, IForm formClass) {
		super(caller, formClass);

	}

	@Override
	public void run() {
		try {
			formMap = (IFormMap) Application.getActiveApplication().getActiveForm();
			if (null != formMap) {
				mapControl = formMap.getMapControl();
				avtiveFormMap = mapControl.getMap().getName();
				TransparentBackground.queryGridMap.put(avtiveFormMap, mapControl.getMap());
				transparentBackground = TransparentBackground.getInstance();
				queryGridValue();
			}
			Application.getActiveApplication().getMainFrame().getFormManager().addActiveFormChangedListener(new ActiveFormChangedListener() {

				@Override
				public void activeFormChanged(ActiveFormChangedEvent e) {
					if (null != e.getNewActiveForm()) {
						formMap = (IFormMap) e.getNewActiveForm();
						mapControl = formMap.getMapControl();
						avtiveFormMap = mapControl.getMap().getName();
						if (null != TransparentBackground.queryGridMap.get(avtiveFormMap)) {
							queryGridValue();
						}
					}
				}
			});
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	private void queryGridValue() {
		mapControl.setAction(Action.SELECT);
		mapControl.addMouseMotionListener(this.mapControlMouseMoved);
		// 不弹出菜单
		formMap.dontShowPopupMenu();
		mapControl.add(transparentBackground);
		// 添加监听事件
		mapControl.addMouseListener(mouseAdapter);
		mapControl.addKeyListener(keyAdapter);
		mapControl.setLayout(null);
		mapControl.getMap().addMapClosedListener(new MapClosedListener() {

			@Override
			public void mapClosed(MapClosedEvent arg0) {
				TransparentBackground.queryGridMap.remove(avtiveFormMap);
			}
		});
	}

	private void abstractMapcontrolMouseMoved(final DecimalFormat format, MouseEvent arg0) {
		try {
			if (null == TransparentBackground.queryGridMap.get(mapControl.getMap().getName())) {
				return;
			}

			Point point = arg0.getPoint();
			Point2D point2D = mapControl.getMap().pixelToMap(point);
			Rectangle rectangle = mapControl.getBounds();
			int count = 10;
			if (count >= rectangle.getMaxX() - arg0.getX() || count >= arg0.getX() || arg0.getY() <= count || count >= rectangle.getMaxY() - arg0.getY()) {
				transparentBackground.setVisible(false);
			} else {
				transparentBackground.setLocation(arg0.getX() + 15, arg0.getY());
				transparentBackground.setVisible(true);
				Map map = mapControl.getMap();
				Layers layers = map.getLayers();
				String currentDatasource = transparentBackground.getjLabelDatasource().getText();
				String currentDataset = transparentBackground.getjLabelDataset().getText();
				String currentPointX = transparentBackground.getjLabelPointX().getText();
				String currentPointY = transparentBackground.getjLabelPointY().getText();
				String currentRow = "";
				String currentColumn = "";
				String currentValue = "";
				Dataset dataset = null;
				// 添加不同的栅格数据集到同一副地图时，通过栅格图层的边界来判断鼠标指向的是哪一个栅格数据集
				if (haveSameBounds(layers, point2D)) {
					dataset = layers.get(0).getDataset();
				} else {
					for (int i = 0; i < layers.getCount(); i++) {
						Layer layer = layers.get(i);
						Dataset tempDataset = layer.getDataset();
						if ((tempDataset instanceof DatasetGrid || tempDataset instanceof DatasetImage) && layer.getBounds().contains(point2D)) {
							dataset = tempDataset;
						}
					}
				}

				String pointX = format.format(point2D.getX());
				String pointY = format.format(point2D.getY());
				if (null != dataset) {
					Datasource datasource = dataset.getDatasource();
					currentDatasource = getTargetString(currentDatasource, datasource.getAlias());
					currentDataset = getTargetString(currentDataset, dataset.getName());
					currentPointX = getTargetString(currentPointX, " " + pointX);
					currentPointY = getTargetString(currentPointY, " " + pointY);
					if (dataset instanceof DatasetGrid) {
						Point grid = ((DatasetGrid) dataset).xyToGrid(point2D);
						int row = (int) grid.getX();
						int column = (int) grid.getY();
						currentRow = MessageFormat.format(SpatialAnalystProperties.getString("String_QueryGridValue_Row"), row);
						currentColumn = MessageFormat.format(SpatialAnalystProperties.getString("String_QueryGridValue_Collunm"), column);
						currentValue = MessageFormat.format(SpatialAnalystProperties.getString("String_QueryGridValue_GridValue"),
								((DatasetGrid) dataset).getValue(row, column));

					} else {
						Point grid = ((DatasetImage) dataset).xyToImage(point2D);
						int row = (int) grid.getY();
						int column = (int) grid.getX();

						currentRow = MessageFormat.format(SpatialAnalystProperties.getString("String_QueryImageColor_Row"), row);
						currentColumn = MessageFormat.format(SpatialAnalystProperties.getString("String_QueryImageColor_Column"), column);

						DatasetImage datasetImage = ((DatasetImage) dataset);
						PixelFormat firstBandPixel = datasetImage.getPixelFormat(0);
						if (firstBandPixel == PixelFormat.RGB || firstBandPixel == PixelFormat.RGBA) {
							for (int i = 0; i < datasetImage.getBandCount(); i++) {
								int argb = (int) datasetImage.getValue(column, row, 0);
								int[] argbs = new int[4];
								argbs[0] = argb >> 24 & 0xFF;
								argbs[1] = argb >> 16 & 0xFF;
								argbs[2] = argb >> 8 & 0xFF;
								argbs[3] = argb & 0xFF;
								currentValue = SpatialAnalystProperties.getString("String_QueryImageBandColor").replace("{0}",
										"[A=" + argbs[0] + ", R=" + argbs[1] + ", G=" + argbs[2] + ", B=" + argbs[3] + "]");
							}
						} else {
							currentValue = "<html>";
							for (int i = 0; i < datasetImage.getBandCount(); i++) {
								currentValue += MessageFormat.format(SpatialAnalystProperties.getString("String_QueryImageBandValue"), i + 1,
										datasetImage.getValue(row, column, i))
										+ "<br>";
							}
							currentValue += "<br></html>";
						}
					}
					transparentBackground.getjLabelDatasource().setText(currentDatasource);
					transparentBackground.getjLabelDataset().setText(currentDataset);
					transparentBackground.getjLabelPointX().setText(currentPointX);
					transparentBackground.getjLabelPointY().setText(currentPointY);
					transparentBackground.getjLabelRowOfGrid().setText(currentRow);
					transparentBackground.getjLabelColumnOfGrid().setText(currentColumn);
					transparentBackground.getjLabelGridValue().setText(currentValue);
				} else {
					currentDatasource = getTargetString(currentDatasource, "-");
					currentDataset = getTargetString(currentDataset, "-");
					currentPointX = getTargetString(currentPointX, pointX);
					currentPointY = getTargetString(currentPointY, pointY);
					currentRow = MessageFormat.format(SpatialAnalystProperties.getString("String_QueryGridValue_Row"), "-");
					currentColumn = MessageFormat.format(SpatialAnalystProperties.getString("String_QueryGridValue_Collunm"), "-");
					currentValue = MessageFormat.format(SpatialAnalystProperties.getString("String_QueryGridValue_GridValue"), "-");
					transparentBackground.getjLabelDatasource().setText(currentDatasource);
					transparentBackground.getjLabelDataset().setText(currentDataset);
					transparentBackground.getjLabelPointX().setText(currentPointX);
					transparentBackground.getjLabelPointY().setText(currentPointY);
					transparentBackground.getjLabelRowOfGrid().setText(currentRow);
					transparentBackground.getjLabelColumnOfGrid().setText(currentColumn);
					transparentBackground.getjLabelGridValue().setText(currentValue);
					transparentBackground.repaint();
				}
			}
		} catch (Exception e) {
		}

	}

	private boolean haveSameBounds(Layers layers, Point2D point2D) {
		boolean haveSameBounds = false;
		Rectangle2D rectangle = layers.get(0).getBounds();
		for (int i = 0; i < layers.getCount(); i++) {
			Layer layer = layers.get(i);
			if (layer.getBounds().equals(rectangle) && layer.getBounds().contains(point2D)) {
				haveSameBounds = true;
			}
		}
		return haveSameBounds;
	}

	private String getTargetString(String targetString, String replaceInfo) {
		return targetString.replace(targetString.substring(targetString.indexOf(":") + 1, targetString.length()), " " + replaceInfo);
	}

	@Override
	public boolean enable() {
		boolean enable = false;
		IFormMap formMap = null;
		if (null != Application.getActiveApplication().getActiveForm() && (Application.getActiveApplication().getActiveForm() instanceof IFormMap)) {
			formMap = (IFormMap) Application.getActiveApplication().getActiveForm();
			Map map = formMap.getMapControl().getMap();
			if (null != map.getLayers()) {
				for (int i = 0; i < map.getLayers().getCount(); i++) {
					Layer layer = map.getLayers().get(i);
					Dataset dataset = layer.getDataset();
					if (dataset instanceof DatasetGrid || dataset instanceof DatasetImage) {
						enable = true;
					}
				}
			}
		}

		return enable;
	}
}