package com.supermap.desktop.process.meta.metaProcessImplements;

import com.supermap.desktop.process.meta.MetaKeys;
import com.supermap.desktop.process.meta.MetaProcess;
import com.supermap.desktop.process.parameter.interfaces.datas.types.Type;

/**
 * @author XiaJT
 */
public class EmptyMetaProcess extends MetaProcess {

	private String title;

	public EmptyMetaProcess(String title) {
		this.title = title;
		parameters.getInputs().addData("input1", Type.UNKOWN);
		parameters.getInputs().addData("input2", Type.UNKOWN);

		parameters.getOutputs().addData("Output", Type.UNKOWN);
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public void run() {

	}

	@Override
	public String getKey() {
		return MetaKeys.Empty;
	}
}
