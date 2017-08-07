package com.supermap.desktop.process.loader;

import java.util.Map;

/**
 * Created by highsad on 2017/8/5.
 */
public class ImportProcessDescriptor extends DefaultProcessDescriptor {
	public final static String PROPERTY_IMPORT_TYPE = "ImportType";

	private String importType;

	public ImportProcessDescriptor(String className, String key) {
		super(className, key);
	}

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
