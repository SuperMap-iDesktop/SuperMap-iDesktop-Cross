package com.supermap.desktop.Interface;

/**
 * @author XiaJT
 */
public interface IWorkflow {
	String getName();

	void setName(String name);

	String serializeTo();

	void serializeFrom(String xmlDescription);

	boolean isReady();
}
