package com.supermap.desktop.process.meta.metaProcessImplements;

import com.supermap.desktop.process.meta.MetaProcess;

import java.util.ArrayList;

/**
 * @author XiaJT
 */
public abstract class MetaProcessGroup extends MetaProcess {
	@Override
	public String getTitle() {
		return null;
	}

	public abstract int getProcessCount();

	public abstract MetaProcess getMetaProcess(int index);

	public abstract ArrayList<MetaProcess> getSubMetaProcess(MetaProcess process);

	public abstract ArrayList<MetaProcess> getParentMetaProcess(MetaProcess process);


}
