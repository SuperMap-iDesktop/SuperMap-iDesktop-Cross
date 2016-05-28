package com.supermap.desktop.geometryoperation.editor;

import com.supermap.data.DatasetType;
import com.supermap.desktop.geometryoperation.EditEnvironment;
import com.supermap.desktop.utilties.ListUtilties;

public class OffsetEditor extends AbstractEditor {

	@Override
	public void activate(EditEnvironment environment) {
		// 按需重写
	}

	@Override
	public void deactivate(EditEnvironment environment) {
		// 按需重写
	}

	@Override
	public boolean enble(EditEnvironment environment) {
		return ListUtilties.isListContainAny(environment.getEditProperties().getEditableDatasetTypes(), DatasetType.LINE, DatasetType.REGION, DatasetType.CAD);
	}

	@Override
	public boolean check(EditEnvironment environment) {
		return environment.getEditor() instanceof OffsetEditor;
	}
}
