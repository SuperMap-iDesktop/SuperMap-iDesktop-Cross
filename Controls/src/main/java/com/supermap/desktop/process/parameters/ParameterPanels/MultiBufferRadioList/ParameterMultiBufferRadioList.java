package com.supermap.desktop.process.parameters.ParameterPanels.MultiBufferRadioList;

import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.interfaces.AbstractParameter;

/**
 * Created by yuanR on 2017/8/22 0022.
 */
public class ParameterMultiBufferRadioList extends AbstractParameter {
	private Double[] radioList;

	public ParameterMultiBufferRadioList() {
		// do nothing
	}

	public void setRadioList(Double[] radioList) {
		this.radioList = radioList;
	}

	public Double[] getRadioList() {
		return this.radioList;
	}

	@Override
	public String getType() {
		return ParameterType.MULTI_BUFFER_RADIOLIST;
	}
}