package com.supermap.desktop.process.meta.metaProcessImplements;

import com.supermap.desktop.process.meta.MetaKeys;
import com.supermap.desktop.process.meta.MetaProcess;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DatasetTypes;

/**
 * @author XiaJT
 */
public class EmptyMetaProcess extends MetaProcess {

	private String title;

	public EmptyMetaProcess(String title) {
		this.title = title;
		parameters.getInputs().addData("input", DatasetTypes.VECTOR);
		parameters.getInputs().addData("input2", DatasetTypes.VECTOR);

		parameters.getOutputs().addData("Output", DatasetTypes.VECTOR);
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public boolean execute() {
		return true;
	}

	@Override
	public String getKey() {
		return MetaKeys.Empty;
	}
}
