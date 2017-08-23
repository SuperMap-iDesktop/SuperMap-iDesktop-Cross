package com.supermap.desktop.process.parameters.ParameterPanels.MultiBufferRadioList;

import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.interfaces.AbstractParameter;

import java.util.ArrayList;

/**
 * Created by yuanR on 2017/8/22 0022.
 */
public class ParameterMultiBufferRadioList extends AbstractParameter {
	private ArrayList<Double> radioLists;

	public ParameterMultiBufferRadioList() {
	}

	public void setRadioList(ArrayList<Double> radioLists) {
		this.radioLists = radioLists;
	}

	public ArrayList<Double> getRadioLists() {
		return this.radioLists;
	}

	@Override
	public String getType() {
		return ParameterType.MULTI_BUFFER_RADIOLIST;
	}
}