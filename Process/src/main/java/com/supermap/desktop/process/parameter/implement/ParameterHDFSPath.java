package com.supermap.desktop.process.parameter.implement;

import com.supermap.desktop.process.constraint.annotation.ParameterField;
import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.interfaces.ISelectionParameter;

import java.beans.PropertyChangeEvent;

/**
 * Created by xie on 2017/2/27.
 */
public class ParameterHDFSPath extends AbstractParameter implements ISelectionParameter {

	@ParameterField(name = "url")
	private String url;

	@Override
	public void setSelectedItem(Object item) {
		if (item instanceof String) {
			String oldValue = url;
			this.url = (String) item;
			firePropertyChangeListener(new PropertyChangeEvent(ParameterHDFSPath.this, "url", oldValue, item));
		}
	}

	@Override
	public Object getSelectedItem() {
		return url;
	}

	@Override
	public String getType() {
		return ParameterType.HDFS_PATH;
	}

}
