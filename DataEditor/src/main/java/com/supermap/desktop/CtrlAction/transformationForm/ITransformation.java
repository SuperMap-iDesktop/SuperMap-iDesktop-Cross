package com.supermap.desktop.CtrlAction.transformationForm;

import com.supermap.ui.MapControl;

import java.util.List;

/**
 * @author XiaJT
 */
public interface ITransformation {
	MapControl getMapControl();

	void addDatas(List<Object> datas);
}
