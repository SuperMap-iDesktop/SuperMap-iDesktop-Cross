package com.supermap.desktop.WorkflowView.meta.loader;

import com.supermap.desktop.WorkflowView.meta.dataconversion.MetaProcessImportFactory;
import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.process.loader.AbstractProcessLoader;
import com.supermap.desktop.process.loader.IProcessDescriptor;
import com.supermap.desktop.utilities.StringUtilities;

/**
 * Created by highsad on 2017/8/1.
 */
public class ImportProcessLoader extends AbstractProcessLoader {
	private final static String IMPORT = "Import";
//	public final static ImportProcessLoader INSTANCE = new ImportProcessLoader();

	public ImportProcessLoader(IProcessDescriptor descriptor) {
		super(descriptor);
	}

	@Override
	public IProcess loadProcess() {
		if (getProcessDescriptor() == null) {
			return null;
		}

		if (StringUtilities.isNullOrEmpty(getProcessDescriptor().getClassName())) {
			return null;
		}

		if (!getProcessDescriptor().getKey().contains(IMPORT)) {
			return null;
		}

		String importType = getProcessDescriptor().getKey().replace(IMPORT, "");
		return MetaProcessImportFactory.createMetaProcessImport(importType);
	}
}
