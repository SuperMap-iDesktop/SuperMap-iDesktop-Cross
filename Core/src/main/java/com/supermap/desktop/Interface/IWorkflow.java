package com.supermap.desktop.Interface;

import com.supermap.desktop.event.WorkFlowsChangedListener;

/**
 * @author XiaJT
 */
public interface IWorkflow {
	String getName();

	void setName(String name);

	String toXML();

	void fromXML(String xmlDescription);
}
