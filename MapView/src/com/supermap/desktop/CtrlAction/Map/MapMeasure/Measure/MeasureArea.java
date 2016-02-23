package com.supermap.desktop.CtrlAction.Map.MapMeasure.Measure;

import com.supermap.data.GeoRegion;
import com.supermap.data.GeoStyle;
import com.supermap.data.GeoText;
import com.supermap.data.Geometry;
import com.supermap.data.TextPart;
import com.supermap.data.TextStyle;
import com.supermap.desktop.Application;
import com.supermap.desktop.enums.AreaUnit;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.utilties.FontUtilties;
import com.supermap.ui.Action;
import com.supermap.ui.TrackedEvent;
import com.supermap.ui.TrackedListener;
import com.supermap.ui.TrackingEvent;
import com.supermap.ui.TrackingListener;

import java.awt.*;
import java.text.MessageFormat;

/**
 * Created by Administrator on 2016/1/28.
 */
public class MeasureArea extends Measure {
	private static final String measureAreaTag = "measureAreaTag";

	public MeasureArea() {
		textTagTitle = "AreaText";
		trackingListener = new TrackingListener() {
			@Override
			public void tracking(TrackingEvent trackingEvent) {

			}
		};
		trackedListener = new TrackedListener() {
			@Override
			public void tracked(TrackedEvent trackedEvent) {
				Geometry geometry = trackedEvent.getGeometry();
				if (geometry != null) {
					if (geometry.getStyle() == null) {
						geometry.setStyle(new GeoStyle());
						geometry.getStyle().setLineWidth(0.1);
					}
					geometry.getStyle().setFillSymbolID(1);
					geometry.getStyle().setLineColor(Color.BLUE);
					mapControl.getMap().getTrackingLayer().add(geometry, measureAreaTag);

					GeoRegion geoRegion = ((GeoRegion) geometry);
					double totleArea = getTotleArea(trackedEvent.getArea());
					String info = decimalFormat.format(totleArea) + getAreaUnit().toString();

					TextPart part = new TextPart(info, geoRegion.getInnerPoint());
					GeoText geotext = new GeoText(part);

					TextStyle textStyle = geotext.getTextStyle();
					textStyle.setFontHeight(FontUtilties.fontSizeToMapHeight(textFontHeight * 0.283,
							mapControl.getMap(), textStyle.isSizeFixed()));
					mapControl.getMap().getTrackingLayer().add(geotext, textTagTitle + "FinishedMeasure");
					outputMeasure(totleArea);
				}
			}
		};
	}

	private void outputMeasure(double area) {
		Application.getActiveApplication().getOutput().output(MessageFormat.format(CoreProperties.getString("String_Map_MeasureTotalDistance"), decimalFormat.format(area), getAreaUnit().toString()));
	}

	private double getTotleArea(double area) {
		return AreaUnit.convertArea(mapControl.getMap().getPrjCoordSys(), getAreaUnit().getUnit(), area);
	}


	@Override
	protected Action getMeasureAction() {
		return Action.CREATEPOLYGON;
	}

	@Override
	protected void removeLastAdded() {
		// 面积不需要
	}
}
