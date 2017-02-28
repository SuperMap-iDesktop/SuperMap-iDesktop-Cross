package com.supermap.desktop.process.parameter.ParameterPanels;

import com.supermap.data.Dataset;
import com.supermap.desktop.process.parameter.interfaces.IData;

/**
 * Created by highsad on 2017/1/24.
 */
public class DatasetData implements IData {

	@Override
	public String getKey() {
		return null;
	}

	@Override
	public String getText() {
		return null;
	}

	@Override
	public Dataset getData() {
		return null;
	}

	@Override
	public void setData() {

	}

	@Override
	public boolean match(IData data) {
		return false;
	}
}
