package com.supermap.desktop.process.parameter.ipls;

import com.supermap.desktop.GridAnalystSettingInstance;

/**
 * @author XiaJT
 */
public class ParameterGridAnalystSetting extends ParameterCombine {
	private ParameterComboBox resultBoundsComboBox = new ParameterComboBox();
	private ParameterBounds parameterBounds = new ParameterBounds();

	private ParameterDatasource parameterDatasource = new ParameterDatasource();
	private ParameterSingleDataset parameterDataset = new ParameterSingleDataset();

	private GridAnalystSettingInstance gridAnalystSettingInstance;


	public ParameterGridAnalystSetting() {
		gridAnalystSettingInstance = GridAnalystSettingInstance.getInstance();
		initComponents();
		initComponentState();
	}

	private void initComponents() {

	}

	private void initComponentState() {

	}

	public void run() {
		gridAnalystSettingInstance.run();
	}

	@Override
	public boolean isReady() {
		return true;
	}
}
