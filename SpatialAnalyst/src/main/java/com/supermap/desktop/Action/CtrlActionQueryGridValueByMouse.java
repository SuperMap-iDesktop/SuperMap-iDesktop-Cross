package com.supermap.desktop.Action;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetGrid;
import com.supermap.data.DatasetImage;
import com.supermap.data.Datasource;
import com.supermap.data.GeoPoint;
import com.supermap.data.GeoStyle;
import com.supermap.data.GeoText;
import com.supermap.data.PixelFormat;
import com.supermap.data.Point2D;
import com.supermap.data.Rectangle2D;
import com.supermap.data.Size2D;
import com.supermap.data.TextAlignment;
import com.supermap.data.TextPart;
import com.supermap.data.TextStyle;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.event.ActiveFormChangedEvent;
import com.supermap.desktop.event.ActiveFormChangedListener;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.spatialanalyst.SpatialAnalystProperties;
import com.supermap.desktop.utilities.DatasourceUtilities;
import com.supermap.desktop.utilities.MapUtilities;
import com.supermap.mapping.Layer;
import com.supermap.mapping.Map;
import com.supermap.mapping.MapClosedEvent;
import com.supermap.mapping.MapClosedListener;
import com.supermap.mapping.TrackingLayer;
import com.supermap.ui.Action;
import com.supermap.ui.MapControl;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class CtrlActionQueryGridValueByMouse extends CtrlAction {
	private transient TransparentBackground transparentBackground;
	private transient MapControl mapControl;
	private IFormMap formMap;
	private final DecimalFormat format = new DecimalFormat("######0.000000");
	public static HashMap<MapControl, ArrayList<Integer>> queryArrayMap = new HashMap<>();
	private static final String trackingObjectName = "QueryGridValue";

	private void hideTransparentBackground() {
		// 允许弹出右键菜单
		formMap.showPopupMenu();
		removeListener();
		mapControl.remove(transparentBackground);
		mapControl.setAction(Action.SELECT2);
		TransparentBackground.queryGridMap.remove(mapControl);
	}

	private KeyAdapter keyAdapter = new KeyAdapter() {
		@Override
		public void keyReleased(KeyEvent e) {
			if (e.getKeyChar() == KeyEvent.VK_ESCAPE) {
				if (TransparentBackground.queryGridMap.containsKey(mapControl)) {
					hideTransparentBackground();
				}
				TrackingLayer trackingLayer = mapControl.getMap().getTrackingLayer();
				for (int count = trackingLayer.getCount() - 1; count >= 0; count--) {
					if (trackingLayer.getTag(count).startsWith(trackingObjectName)) {
						trackingLayer.remove(count);
					}
				}
				mapControl.getMap().refreshTrackingLayer();
				mapControl.removeKeyListener(keyAdapter);
			}
		}
	};

	private MouseAdapter mouseAdapter = new MouseAdapter() {
		@Override
		public void mousePressed(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON3) {
				hideTransparentBackground();
			} else if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 1) {
				getQueryInfo(e);
			}
		}
	};

	private MouseAdapter mouseMotionListener = new MouseAdapter() {
		@Override
		public void mouseMoved(MouseEvent arg0) {
			abstractMapcontrolMouseMoved(format, arg0);
		}
	};

	private MapClosedListener mapClosedListener = new MapClosedListener() {

		@Override
		public void mapClosed(MapClosedEvent arg0) {
			hideTransparentBackground();
		}
	};

	public CtrlActionQueryGridValueByMouse(IBaseItem caller, IForm formClass) {
		super(caller, formClass);

	}

	protected void getQueryInfo(MouseEvent e) {

		GeoPoint geoPoint = new GeoPoint(mapControl.getMap().pixelToMap(e.getPoint()));
		GeoStyle geoStyle = new GeoStyle();
		geoStyle.setLineColor(Color.RED);
		geoStyle.setMarkerSize(new Size2D(3, 3));
		geoPoint.setStyle(geoStyle);
		TrackingLayer trackingLayer = mapControl.getMap().getTrackingLayer();
		int geoCount = 0;

		for (int i = 0; i < trackingLayer.getCount(); i++) {
			if (trackingLayer.getTag(i).equals(trackingObjectName + "point")) {
				geoCount++;
			}
		}
		int pointCount = 1;
		if (0 != geoCount) {
			pointCount = 1 + geoCount;
		}
		// 在跟踪层上画点
		Point2D point2DNumber = mapControl.getMap().pixelToMap(e.getPoint());
		TextPart textPartNumber = new TextPart();
		textPartNumber.setAnchorPoint(point2DNumber);
		textPartNumber.setText(String.valueOf(pointCount));
		// 在跟踪层上绘制数字
		GeoText geoTextNumber = new GeoText(textPartNumber);
		TextStyle textStyleNumber = new TextStyle();
		textStyleNumber.setBold(true);
		textStyleNumber.setAlignment(TextAlignment.TOPLEFT);
		geoTextNumber.setTextStyle(textStyleNumber);

		trackingLayer.add(geoPoint, trackingObjectName + "point");
		trackingLayer.add(geoTextNumber, trackingObjectName + "pointCount");
		Application
				.getActiveApplication()
				.getOutput()
				.output(MessageFormat.format(SpatialAnalystProperties.getString("String_GridValueMessage"), pointCount) + "\n"
						+ transparentBackground.getjLabelDatasource().getText() + "\n" + transparentBackground.getjLabelDataset().getText() + "\n"
						+ transparentBackground.getjLabelPointX().getText() + "\n" + transparentBackground.getjLabelPointY().getText() + "\n"
						+ transparentBackground.getjLabelRowOfGrid().getText() + "\n" + transparentBackground.getjLabelColumnOfGrid().getText() + "\n"
						+ transparentBackground.getjLabelGridValue().getText().replace("<html>", "").replace("<br>", "").replace("<html>", "") + "\n");
	}

	@Override
	public void run() {
		try {
			formMap = (IFormMap) Application.getActiveApplication().getActiveForm();
			if (null != formMap) {
				mapControl = formMap.getMapControl();
				if (null != TransparentBackground.queryGridMap.get(mapControl)) {
					hideTransparentBackground();
				} else {
					transparentBackground = new TransparentBackground();
					TransparentBackground.queryGridMap.put(mapControl, transparentBackground);
					queryGridValue();
				}
			}
			Application.getActiveApplication().getMainFrame().getFormManager().addActiveFormChangedListener(new ActiveFormChangedListener() {

				@Override
				public void activeFormChanged(ActiveFormChangedEvent e) {
					if (null != e.getNewActiveForm() && e.getNewActiveForm() instanceof IFormMap) {
						formMap = (IFormMap) e.getNewActiveForm();
						mapControl = formMap.getMapControl();
						if (null != TransparentBackground.queryGridMap.get(mapControl)) {
							transparentBackground = TransparentBackground.queryGridMap.get(mapControl);
							formMap.showPopupMenu();
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
		mapControl.add(transparentBackground);
		// 添加监听事件
		addListener();
		mapControl.setLayout(null);

	}

	private void removeListener() {
		mapControl.removeMouseMotionListener(this.mouseMotionListener);
		mapControl.removeMouseListener(this.mouseAdapter);
		mapControl.getMap().removeMapClosedListener(mapClosedListener);
	}

	private void addListener() {
		removeListener();
		formMap.dontShowPopupMenu();
		mapControl.addMouseMotionListener(this.mouseMotionListener);
		mapControl.addMouseListener(this.mouseAdapter);
		mapControl.addKeyListener(this.keyAdapter);
		mapControl.getMap().addMapClosedListener(mapClosedListener);
	}

	private void abstractMapcontrolMouseMoved(final DecimalFormat format, MouseEvent arg0) {
		try {
			if (null == TransparentBackground.queryGridMap.get(mapControl)) {
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
				ArrayList<Layer> layers = MapUtilities.getLayers(map);
				String currentDatasource = transparentBackground.getjLabelDatasource().getText();
				String currentDataset = transparentBackground.getjLabelDataset().getText();
				String currentPointX = transparentBackground.getjLabelPointX().getText();
				String currentPointY = transparentBackground.getjLabelPointY().getText();
				String currentRow = "";
				String currentColumn = "";
				String currentValue = "";
				Dataset dataset = null;

				if (haveSameBounds(layers, point2D)) {
					for (int i = 0; i < layers.size(); i++) {
						if ((layers.get(i).getDataset() instanceof DatasetGrid || layers.get(i).getDataset() instanceof DatasetImage)
								&& layers.get(i).isVisible()) {
							dataset = layers.get(i).getDataset();
							break;
						}
					}
				} else {
					for (int i = 0; i < layers.size(); i++) {
						Layer layer = layers.get(i);
						Dataset tempDataset = layer.getDataset();
						// 添加不同的栅格数据集到同一副地图时，通过栅格图层的边界来判断鼠标指向的是哪一个栅格数据集
						if ((tempDataset instanceof DatasetGrid || tempDataset instanceof DatasetImage) && layer.getBounds().contains(point2D)) {
							dataset = tempDataset;
							break;
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

	private boolean haveSameBounds(ArrayList<Layer> layers, Point2D point2D) {
		boolean haveSameBounds = false;
		Rectangle2D rectangle = layers.get(0).getBounds();
		for (int i = 0; i < layers.size(); i++) {
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
			if (null != MapUtilities.getLayers(map)) {
				ArrayList<Layer> layers = MapUtilities.getLayers(map);
				for (int i = 0; i < layers.size(); i++) {
					Layer layer = layers.get(i);
					if (null != layer && layer.isVisible() && null != layer.getDataset()) {
						Dataset dataset = layer.getDataset();
						if (null != layer.getDataset().getDatasource() && !DatasourceUtilities.isWebType(layer.getDataset().getDatasource())
								&& (dataset instanceof DatasetGrid || dataset instanceof DatasetImage)) {
							enable = true;
							break;
						}
					}
				}
			}
		}

		return enable;
	}
}