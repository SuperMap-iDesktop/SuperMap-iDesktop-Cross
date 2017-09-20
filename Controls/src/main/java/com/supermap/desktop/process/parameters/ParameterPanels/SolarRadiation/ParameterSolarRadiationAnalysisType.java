package com.supermap.desktop.process.parameters.ParameterPanels.SolarRadiation;

import com.supermap.analyst.spatialanalyst.SolarRadiationParameter;
import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.interfaces.AbstractParameter;
import com.supermap.desktop.process.parameter.interfaces.IMultiSelectionParameter;

/**
 * Created by lixiaoyao on 2017/9/7.
 */
public class ParameterSolarRadiationAnalysisType extends AbstractParameter implements IMultiSelectionParameter {

	private String describe="";

	public ParameterSolarRadiationAnalysisType(){}

	public ParameterSolarRadiationAnalysisType(String describe){
		this.describe=describe;
	}

	private SolarRadiationParameter solarRadiationParameter;

	@Override
	public void setSelectedItem(Object item) {

	}

	@Override
	public Object getSelectedItem() {
		return null;
	}

	@Override
	public String getType() {
		return ParameterType.SOLAR_RADIATION;
	}

	@Override
	public String getDescribe() {
		return this.describe;
	}

	public SolarRadiationParameter getSolarRadiationParameter() {
		return solarRadiationParameter;
	}

	public void setSolarRadiationParameter(SolarRadiationParameter solarRadiationParameter) {
		this.solarRadiationParameter = solarRadiationParameter;
	}

}
