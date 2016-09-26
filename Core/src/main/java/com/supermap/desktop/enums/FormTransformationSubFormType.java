package com.supermap.desktop.enums;


import com.supermap.desktop.properties.CoreProperties;

/**
 * @author XiaJT
 */
public enum FormTransformationSubFormType {
	Target, Reference;

	@Override
	public String toString() {
		if (this == Target) {
			return CoreProperties.getString("String_Transfernation_TargetLayer");
		}
		return CoreProperties.getString("String_Transfernation_ReferLayer");
	}
}
