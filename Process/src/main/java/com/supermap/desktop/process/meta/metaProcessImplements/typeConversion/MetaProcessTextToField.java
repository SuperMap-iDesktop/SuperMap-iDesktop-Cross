package com.supermap.desktop.process.meta.metaProcessImplements.typeConversion;

import com.supermap.analyst.spatialanalyst.Generalization;
import com.supermap.desktop.Application;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.meta.MetaKeys;
import com.supermap.desktop.process.meta.MetaProcess;
import com.supermap.desktop.process.parameter.interfaces.IParameters;

/**
 * Created by yuanR on 2017/7/26 0026.
 * 文本转字段
 */
public class MetaProcessTextToField extends MetaProcess {


	@Override
	public boolean execute() {
		boolean isSuccessful = false;
		try {
			fireRunning(new RunningEvent(MetaProcessTextToField.this, 0, "start"));

			fireRunning(new RunningEvent(MetaProcessTextToField.this, 100, "finished"));

		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			Generalization.removeSteppedListener(steppedListener);
		}
		return isSuccessful;
	}

	@Override
	public IParameters getParameters() {
		return parameters;
	}

	@Override
	public String getKey() {
		return MetaKeys.CONVERSION_TEXT_TO_FILED;
	}

	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_Title_TextToField");
	}


}
