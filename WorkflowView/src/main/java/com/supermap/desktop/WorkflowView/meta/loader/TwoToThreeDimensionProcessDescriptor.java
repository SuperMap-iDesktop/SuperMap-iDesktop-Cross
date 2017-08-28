package com.supermap.desktop.WorkflowView.meta.loader;

import com.supermap.desktop.process.loader.DefaultProcessDescriptor;

import java.util.Map;

/**
 * Created By Chens on 2017/8/12 0012
 */
public class TwoToThreeDimensionProcessDescriptor extends DefaultProcessDescriptor {
	public final static String PROPERTY_INPUT_TYPE = "InputType";
	private String inputType;

	public String getInputType() {
		return inputType;
	}

	@Override
	public void init(Map<String, String> properties) {
		super.init(properties);

		if (properties.containsKey(PROPERTY_INPUT_TYPE)) {
			this.inputType = properties.get(PROPERTY_INPUT_TYPE);
		}
	}
}
