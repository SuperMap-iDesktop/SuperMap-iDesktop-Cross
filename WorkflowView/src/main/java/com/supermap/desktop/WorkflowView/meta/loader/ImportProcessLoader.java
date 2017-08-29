package com.supermap.desktop.WorkflowView.meta.loader;

import com.supermap.desktop.WorkflowView.meta.dataconversion.MetaProcessImportFactory;
import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.process.loader.AbstractProcessLoader;
import com.supermap.desktop.utilities.StringUtilities;

import java.util.Map;

/**
 * Created by highsad on 2017/8/1.
 */
public class ImportProcessLoader extends AbstractProcessLoader {
	private final static String IMPORT = "Import";

	public ImportProcessLoader(Map<String, String> properties, String index) {
		super(properties, index);
	}


	@Override
	public IProcess loadProcess() {
		if (getProperties() == null) {
			return null;
		}

		if (StringUtilities.isNullOrEmpty(getClassName())) {
			return null;
		}

		if (!getKey().contains(IMPORT)) {
			return null;
		}

		String importType = getKey().replace(IMPORT, "");
		return MetaProcessImportFactory.createMetaProcessImport(importType);
	}
}
