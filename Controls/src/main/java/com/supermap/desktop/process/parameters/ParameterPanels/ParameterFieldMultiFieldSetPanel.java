package com.supermap.desktop.process.parameters.ParameterPanels;

import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.interfaces.IParameter;
import com.supermap.desktop.process.parameter.interfaces.ParameterPanelDescribe;
import com.supermap.desktop.process.parameter.ipls.ParameterMultiFieldSet;
import com.supermap.desktop.ui.controls.ChooseTable.SmMultiFieldsChooseTable;

/**
 * Created by xie on 2017/8/7.
 */
@ParameterPanelDescribe(parameterPanelType = ParameterType.MULTIFIELDSET)
public class ParameterFieldMultiFieldSetPanel extends SwingPanel {
	private ParameterMultiFieldSet multiFieldSet;
	private SmMultiFieldsChooseTable multiFieldsChooseTable;
	public ParameterFieldMultiFieldSetPanel(IParameter parameter) {
		super(parameter);
		this.multiFieldSet = (ParameterMultiFieldSet) parameter;
		init();
	}

	private void init() {
		multiFieldsChooseTable = new SmMultiFieldsChooseTable(this.multiFieldSet.getDatasetInfo().getDataset());

	}
}
