package com.supermap.desktop.CtrlAction.transformationForm.CtrlAction;

import com.supermap.data.Transformation;
import com.supermap.desktop.CtrlAction.transformationForm.beans.TransformationAddObjectBean;
import com.supermap.desktop.progress.Interface.UpdateProgressCallable;

/**
 * @author XiaJT
 */
public class TransformCallable extends UpdateProgressCallable {

	private TransformationAddObjectBean[] transformationAddObjectBeans;
	private Transformation transformation;
	private int totleSize = 0;

	public TransformCallable(Transformation transformation, TransformationAddObjectBean[] transformationAddObjectBeans) {
		this.transformation = transformation;
		this.transformationAddObjectBeans = transformationAddObjectBeans;
		if (transformationAddObjectBeans.length == 0) {
			totleSize = 1;
		}
	}

	@Override
	public Boolean call() throws Exception {
		return null;
	}
}
