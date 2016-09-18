package com.supermap.desktop.CtrlAction.transformationForm.beans;

/**
 * @author XiaJT
 */
public class AddLayerItemBean {
	private Object data;
	private FormTransformationSubFormType type = FormTransformationSubFormType.Reference;

	public AddLayerItemBean(Object data) {
		this.data = data;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public FormTransformationSubFormType getType() {
		return type;
	}

	public void setType(FormTransformationSubFormType type) {
		this.type = type;
	}
}
