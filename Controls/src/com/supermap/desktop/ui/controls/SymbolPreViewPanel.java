package com.supermap.desktop.ui.controls;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import com.supermap.data.Dataset;
import com.supermap.data.GeoLine;
import com.supermap.data.GeoPoint;
import com.supermap.data.GeoRegion;
import com.supermap.data.GeoStyle;
import com.supermap.data.Geometry;
import com.supermap.data.Point2D;
import com.supermap.data.Point2Ds;
import com.supermap.data.Rectangle2D;
import com.supermap.data.SymbolType;
import com.supermap.data.Workspace;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.mapping.Layer;
import com.supermap.mapping.Map;
import com.supermap.mapping.Theme;
import com.supermap.mapping.ThemeLabel;
import com.supermap.mapping.TrackingLayerDrawingEvent;
import com.supermap.mapping.TrackingLayerDrawingListener;
import com.supermap.ui.Action;
import com.supermap.ui.InteractionMode;
import com.supermap.ui.MapControl;

/**
 * 符号预览面板
 * 
 * @author xuzw
 *
 */
public class SymbolPreViewPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private transient MapControl preViewMapControl;

	private transient Workspace fieldWorkspace;

	private transient Map map;

	private transient Dataset filedDataset;

	private transient TrackingLayerDrawingListener listener;

	/**
	 * Create the panel
	 */
	public SymbolPreViewPanel(Workspace workspace, SymbolType symbolType) {
		super();
		setLayout(new BorderLayout());
		setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(Color.gray, 1),
				ControlsProperties.getString("String_Preview"), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
		setBackground(Color.WHITE);
		fieldWorkspace = workspace;

		if (symbolType.equals(SymbolType.MARKER)) {
			setPreferredSize(new Dimension(80, 180));
			setMinimumSize(new Dimension(80, 180));
		} else if (symbolType.equals(SymbolType.LINE)) {
			setPreferredSize(new Dimension(80, 180));
			setMinimumSize(new Dimension(80, 180));
		} else if (symbolType.equals(SymbolType.FILL)) {
			setPreferredSize(new Dimension(250, 140));
			setMinimumSize(new Dimension(250, 140));
			setMaximumSize(new Dimension(250, 140));
		}
		add(getPreViewMapControl(), BorderLayout.CENTER);
		preViewMapControl.setAction(Action.NULL);
		preViewMapControl.setMarginPanEnabled(true);
		preViewMapControl.setInteractionMode(InteractionMode.CUSTOMALL);
	}

	public SymbolPreViewPanel(Dataset dataset) {
		super();
		this.filedDataset = dataset;
		setLayout(new BorderLayout());
		setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(Color.gray, 1),
				ControlsProperties.getString("String_Preview"), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
		setBackground(Color.WHITE);
		fieldWorkspace = filedDataset.getDatasource().getWorkspace();
		add(getPreViewMapControl(), BorderLayout.CENTER);

		preViewMapControl.setMarginPanEnabled(true);
		preViewMapControl.setAction(Action.PAN);
	}

	public Dataset getDataset() {
		return filedDataset;
	}

	/**
	 * 获取用于预览的MapControl
	 * 
	 * @return
	 */
	public MapControl getPreViewMapControl() {
		if (preViewMapControl == null) {
			preViewMapControl = new MapControl();
			preViewMapControl.setAction(Action.PAN);
			preViewMapControl.setFocusable(false);
			if (fieldWorkspace != null) {
				preViewMapControl.getMap().setWorkspace(fieldWorkspace);
			}

			map = preViewMapControl.getMap();

		}
		return preViewMapControl;
	}

	/**
	 * 刷新预览框
	 * 
	 * @param geoStyle 要绘制的风格
	 */
	protected void refreshPreViewMapControl(SymbolType symbolType, GeoStyle geoStyle) {
		// 根据几何对象类型的不同进行相应初始化
		Geometry geometry = null;
		if (symbolType.equals(SymbolType.MARKER)) {
			geometry = new GeoPoint(0, 0);
		} else if (symbolType.equals(SymbolType.LINE)) {
			Point2Ds point2Ds = new Point2Ds();
			point2Ds.add(new Point2D(-9, 0));
			point2Ds.add(new Point2D(9, 0));
			geometry = new GeoLine(point2Ds);
		} else {
			Point2Ds point2DsRegion = new Point2Ds();
			point2DsRegion.add(new Point2D(-8, 8));
			point2DsRegion.add(new Point2D(8, 8));
			point2DsRegion.add(new Point2D(8, -8));
			point2DsRegion.add(new Point2D(-8, -8));
			geometry = new GeoRegion(point2DsRegion);
		}

		// 设置几何对象风格，刷新Label上的显示
		geometry.setStyle(geoStyle);
		map.getTrackingLayer().clear();
		map.getTrackingLayer().add(geometry, "geometry");
		map.refresh();
	}

	/**
	 * 
	 * @param theme 要添加的标签专题图图层
	 */
	public void refreshPreViewMapControl(Theme theme) {
		map.getLayers().clear();
		map.getLayers().add(filedDataset, theme, true);
		map.refresh();
	}

	/**
	 * 一个TrackingLayerDrawingListener监听器，该监听器用于设置map的ViewBounds， 否则由于初始化Map的ViewBounds不正确，符号的显示位置会有错误，该监听在第一次 点击符号展示面板上的任意一个JLabel时会被移除
	 */
	protected void addMapTrackingLayerDrawingListener() {
		listener = new TrackingLayerDrawingListener() {
			@Override
			public void trackingLayerDrawing(TrackingLayerDrawingEvent event) {
				map.setViewBounds(new Rectangle2D(-10, -10, 10, 10));
			}

		};
		map.addTrackingLayerDrawingListener(listener);
	}

	/**
	 * 移除TrackingLayerDrawingListener
	 */
	protected void removeMapTrackingLayerDrawingListener() {
		map.removeTrackingLayerDrawingListener(listener);
	}

	/**
	 * 获取资源
	 * 
	 * @return
	 */
	protected Workspace getWorkspace() {
		return fieldWorkspace;
	}

	public Layer getPreViewMapControlLayer() {
		return map.getLayers().get(0);
	}
}
