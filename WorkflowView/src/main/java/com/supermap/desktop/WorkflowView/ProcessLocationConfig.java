package com.supermap.desktop.WorkflowView;

import com.supermap.desktop.utilities.StringUtilities;

import java.awt.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by highsad on 2017/7/20.
 */
public class ProcessLocationConfig {
	private String processKey;
	private int serialID;
	private Point location;

	private Map<String, Point> outputsLoc = new ConcurrentHashMap<>();

	public ProcessLocationConfig(String processKey, int serialID) {
		this(processKey, serialID, null);
	}

	public ProcessLocationConfig(String processKey, int serialID, Point location) {
		if (StringUtilities.isNullOrEmpty(processKey)) {
			throw new IllegalArgumentException();
		}

		this.processKey = processKey;
		this.serialID = serialID;
		this.location = location;
	}

	public String getProcessKey() {
		return processKey;
	}

	public int getSerialID() {
		return this.serialID;
	}

	public Point getLocation() {
		return this.location;
	}

	public void setLocation(Point location) {
		this.location = location;
	}

	public Point getOutputLocation(String outputName) {
		if (this.outputsLoc.containsKey(outputName)) {
			return this.outputsLoc.get(outputName);
		}

		return null;
	}

	public void setOutputLocation(String outputName, Point location) {
		if (!StringUtilities.isNullOrEmpty(outputName)) {
			this.outputsLoc.put(outputName, location);
		}
	}

	public void fromXML(String xml) {

	}

	public String toXML() {
		return null;
	}
}
