package com.supermap.desktop.CtrlAction.Map.MapMeasure.Measure;

import com.supermap.desktop.Application;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.ui.Action;

import java.text.MessageFormat;

/**
 * Created by Administrator on 2016/1/28.
 */
public class MeasureArea extends Measure {
	private double totleArea;


	@Override
	protected void outputMeasure() {
		Application.getActiveApplication().getOutput().output(MessageFormat.format(CoreProperties.getString("String_Map_MeasureTotalDistance"), decimalFormat.format(totleArea), getLengthUnit().toString()));


	}

	@Override
	protected void resetValue() {
		totleArea = 0;

	}

	@Override
	protected Action getMeasureAction() {
		return Action.CREATEPOLYGON;
	}
}
