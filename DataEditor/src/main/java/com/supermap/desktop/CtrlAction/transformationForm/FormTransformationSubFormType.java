package com.supermap.desktop.CtrlAction.transformationForm;

import com.supermap.desktop.dataeditor.DataEditorProperties;

/**
 * @author XiaJT
 */
public enum FormTransformationSubFormType {
	Target, Reference;

	@Override
	public String toString() {
		if (this == Target) {
			return DataEditorProperties.getString("String_Transfernation_TargetLayer");
		}
		return DataEditorProperties.getString("String_Transfernation_ReferLayer");
	}
}
