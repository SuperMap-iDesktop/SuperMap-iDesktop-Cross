package com.supermap.desktop.WorkflowView.meta.loader;

import com.supermap.desktop.process.loader.DefaultProcessDescriptor;

import java.util.Map;

/**
 * Created By Chens on 2017/8/12 0012
 */
public class TwoToThreeDimensionProcessDescriptor extends DefaultProcessDescriptor {
	public final static String PROPERTY_IMPORT_TYPE = "ImportType";
	private String importType;

	public String getImportType() {
		return importType;
	}

	@Override
	public void init(Map<String, String> properties) {
		super.init(properties);

		if (properties.containsKey(PROPERTY_IMPORT_TYPE)) {
			this.importType = properties.get(PROPERTY_IMPORT_TYPE);
		}
	}
}
