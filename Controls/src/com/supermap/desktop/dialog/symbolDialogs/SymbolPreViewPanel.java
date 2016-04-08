package com.supermap.desktop.dialog.symbolDialogs;

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
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.mapping.Map;
import com.supermap.ui.Action;
import com.supermap.ui.InteractionMode;
import com.supermap.ui.MapControl;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * @author xuzw
 */
public class SymbolPreViewPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private transient MapControl preViewMapControl;

	private transient Workspace fieldWorkspace;

	private transient Map map;

	private transient Geometry geometry;

	private SymbolType symbolType;


	/**
	 * Create the panel
	 */
	public SymbolPreViewPanel(SymbolType symbolType) {
		super();
		this.symbolType = symbolType;
		setLayout(new BorderLayout());
		setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(Color.gray, 1),
				ControlsProperties.getString("String_Preview"), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
		setBackground(Color.WHITE);
		fieldWorkspace = Application.getActiveApplication().getWorkspace();
		add(getPreViewMapControl(), BorderLayout.CENTER);
		preViewMapControl.setAction(Action.NULL);
		preViewMapControl.setMarginPanEnabled(true);
		preViewMapControl.setInteractionMode(InteractionMode.CUSTOMALL);

		map.setViewBounds(new Rectangle2D(-10, -10, 10, 10));
	}

	public void setGeoStyle(GeoStyle style) {
		geometry.setStyle(style);
	}


	/**
	 * 获取用于预览的MapControl
	 *
	 * @return 用于预览的MapControl
	 */
	private MapControl getPreViewMapControl() {
		if (preViewMapControl == null) {
			preViewMapControl = new MapControl();
			preViewMapControl.setAction(Action.PAN);
			preViewMapControl.setFocusable(false);
			if (fieldWorkspace != null) {
				preViewMapControl.getMap().setWorkspace(fieldWorkspace);
			}
			map = preViewMapControl.getMap();
			initGeometry();

		}
		return preViewMapControl;
	}

	/**
	 * 初始化预览图像中的对象
	 */
	private void initGeometry() {
		geometry = SymbolPreViewPanel.getGeometry(symbolType);
		// 设置几何对象风格，刷新Label上的显示
		map.getTrackingLayer().clear();
		map.getTrackingLayer().add(geometry, "geometry");
		map.refresh();
	}

	/**
	 * 根据符号类型获得对应的示范对象
	 *
	 * @param symbolType 符号类型
	 * @return 示范对象
	 */
	private static Geometry getGeometry(SymbolType symbolType) {
		Geometry geometry;

		if (symbolType == SymbolType.MARKER) {
			geometry = new GeoPoint(0, 0);
		} else if (symbolType == SymbolType.LINE) {
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
		return geometry;
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(200, 200);
	}
}
