package com.supermap.desktop.WorkflowView.meta.loader;

import com.supermap.desktop.WorkflowView.meta.dataconversion.MetaProcessImportFactory;
import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.process.loader.IProcessDescriptor;
import com.supermap.desktop.process.loader.IProcessLoader;
import com.supermap.desktop.utilities.StringUtilities;

/**
 * Created by highsad on 2017/8/1.
 */
public class ImportProcessLoader implements IProcessLoader {
	private final static String IMPORT = "Import";
	public final static ImportProcessLoader INSTANCE = new ImportProcessLoader();

	@Override
	public IProcess loadProcess(IProcessDescriptor descriptor) {
		if (descriptor == null) {
			return null;
		}

		if (StringUtilities.isNullOrEmpty(descriptor.getClassName())) {
			return null;
		}

		if (!descriptor.getKey().contains(IMPORT)) {
			return null;
		}

		IProcess process = null;

		String importType = descriptor.getKey().replace(IMPORT, "");
		return MetaProcessImportFactory.createMetaProcessImport(importType);
	}
}
