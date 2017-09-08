package com.supermap.desktop.process.parameters.ParameterPanels.SolarRadiation;

import com.supermap.analyst.spatialanalyst.SolarRadiationParameter;
import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.interfaces.IParameter;
import com.supermap.desktop.process.parameter.interfaces.ParameterPanelDescribe;
import com.supermap.desktop.process.parameters.ParameterPanels.SwingPanel;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;

import java.awt.*;

/**
 * Created by lixiaoyao on 2017/9/7.
 */
@ParameterPanelDescribe(parameterPanelType = ParameterType.SOLAR_RADIATION)
public class ParameterSolarRadiationAnalysisTypePanel extends SwingPanel {

	private ParameterSolarRadiationAnalysisType parameterSolarRadiationAnalysisType;
	private SolarRadiationAnalysisTypePanel analysisTypePanel;

	public ParameterSolarRadiationAnalysisTypePanel(IParameter parameter){
		super(parameter);
		this.parameterSolarRadiationAnalysisType=(ParameterSolarRadiationAnalysisType)parameter;
		initComponent();
		initLayout();
		initListener();
	}

	private void initComponent(){
		this.analysisTypePanel=new SolarRadiationAnalysisTypePanel();
	}

	private void initLayout(){
		panel.setLayout(new GridBagLayout());
		panel.add(this.analysisTypePanel, new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setInsets(0, 0, 0, 0));
	}

	private void initListener() {
		this.analysisTypePanel.removeSolarRadiationParameterListener(solarRadiationParameterListener);
		this.analysisTypePanel.addSolarRadiationParameterListener(solarRadiationParameterListener);
	}

	private SolarRadiationParameterListener solarRadiationParameterListener=new SolarRadiationParameterListener() {
		@Override
		public void updateSolarRadiationParameter(SolarRadiationParameter solarRadiationParameter) {
			parameterSolarRadiationAnalysisType.setSolarRadiationParameter(solarRadiationParameter);
		}
	};
}
