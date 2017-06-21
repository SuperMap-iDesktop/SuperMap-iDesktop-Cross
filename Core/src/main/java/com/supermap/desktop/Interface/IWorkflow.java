package com.supermap.desktop.Interface;

/**
 * @author XiaJT
 */
public interface IWorkflow {
	String getName();

	void setName(String name);

	String toXML();

	void fromXML(String xmlDescription);
}
