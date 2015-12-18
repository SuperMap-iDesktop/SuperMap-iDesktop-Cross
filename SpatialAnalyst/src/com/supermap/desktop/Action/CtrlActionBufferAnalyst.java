package com.supermap.desktop.Action;

import com.supermap.data.Datasources;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.spatialanalyst.vectoranalyst.BufferFactory;

public class CtrlActionBufferAnalyst extends CtrlAction {
	private BufferFactory bufferFactory;

	public CtrlActionBufferAnalyst(IBaseItem caller, IForm formClass) {
		super(caller, formClass);

	}

	@Override
	public void run() {
		 this.setBufferFactory(new BufferFactory());
	}

	@Override
	public boolean enable() {
		boolean visible = true;
		Datasources datasources = Application.getActiveApplication().getWorkspace().getDatasources();
		if (datasources.getCount() <= 0) {
			visible = false;
		}

		return visible;
	}

	public BufferFactory getBufferFactory() {
		return bufferFactory;
	}

	public void setBufferFactory(BufferFactory bufferFactory) {
		this.bufferFactory = bufferFactory;
	}

}
