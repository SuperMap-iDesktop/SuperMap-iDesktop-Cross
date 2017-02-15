package com.supermap.desktop.process.parameter.ParameterPanels;

import com.supermap.data.Dataset;
import com.supermap.desktop.process.parameter.interfaces.IData;

/**
 * Created by highsad on 2017/1/24.
 */
public class DatasetData implements IData<Dataset> {

	@Override
	public Dataset getData() {
		return null;
	}

	@Override
	public boolean match(IData data) {
		return false;
	}
}
