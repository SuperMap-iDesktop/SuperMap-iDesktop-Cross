package com.supermap.desktop.CtrlAction.Map.MapMeasure.Measure;

import com.supermap.ui.Action;

/**
 * Created by Administrator on 2016/1/28.
 */
public class MeasureAngle extends Measure {


	private void outputMeasure() {
// 第一次只有方位角，第二次还有夹角
//		Application.getActiveApplication().getOutput().output(MessageFormat.format(CoreProperties.getString("String_Map_MeasureTotalDistance"), decimalFormat.format(totleLength), getLengthUnit().toString()));
	}


	@Override
	protected Action getMeasureAction() {
		return Action.CREATEPOLYLINE;
	}

}
