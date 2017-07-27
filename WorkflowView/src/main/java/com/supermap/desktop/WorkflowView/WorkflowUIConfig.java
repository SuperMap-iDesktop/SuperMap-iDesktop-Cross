package com.supermap.desktop.WorkflowView;

import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.utilities.StringUtilities;
import com.supermap.desktop.utilities.XmlUtilities;
import org.w3c.dom.Element;

import java.awt.*;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by highsad on 2017/7/5.
 */
public class WorkflowUIConfig {
	private Vector<ProcessLocationConfig> processLocations = new Vector<>();

	public void addLocationConfig(ProcessLocationConfig locationConfig) {
		if (!this.processLocations.contains(locationConfig) && !contains(locationConfig.getProcessKey(), locationConfig.getSerialID())) {
			this.processLocations.add(locationConfig);
		}
	}

	public Point getProcessLocation(IProcess process) {
		if (process != null) {
			return getProcessLocation(process.getKey(), process.getSerialID());
		}

		return null;
	}

	public Point getProcessLocation(String processKey, int serialID) {
		for (int i = 0; i < this.processLocations.size(); i++) {
			ProcessLocationConfig config = this.processLocations.get(i);

			if (StringUtilities.stringEquals(config.getProcessKey(), processKey) && config.getSerialID() == serialID) {
				return config.getLocation();
			}
		}

		return null;
	}

	public ProcessLocationConfig getProcessConfig(String processKey, int serialID) {
		for (int i = 0; i < this.processLocations.size(); i++) {
			ProcessLocationConfig config = this.processLocations.get(i);

			if (StringUtilities.stringEquals(config.getProcessKey(), processKey) && config.getSerialID() == serialID) {
				return config;
			}
		}

		return null;
	}

	public boolean contains(String processKey, int serialID) {
		for (int i = 0; i < this.processLocations.size(); i++) {
			ProcessLocationConfig config = this.processLocations.get(i);

			if (StringUtilities.stringEquals(config.getProcessKey(), processKey) && config.getSerialID() == serialID) {
				return true;
			}
		}

		return false;
	}

	public static WorkflowUIConfig serializeFrom(Element locationsNode) {
		WorkflowUIConfig config = null;

		Element[] processess = XmlUtilities.getChildElementNodesByName(locationsNode, "process");
		if (processess != null) {
			config = new WorkflowUIConfig();

			for (int i = 0; i < processess.length; i++) {
				String processKey = processess[i].getAttribute("Key");
				int serialID = Integer.valueOf(processess[i].getAttribute("SerialID"));
				int locationX = Integer.valueOf(processess[i].getAttribute("LocationX"));
				int locationY = Integer.valueOf(processess[i].getAttribute("LocationY"));
				ProcessLocationConfig locationConfig = new ProcessLocationConfig(processKey, serialID, new Point(locationX, locationY));

				Element[] outputs = XmlUtilities.getChildElementNodesByName(processess[i], "Output");
				if (outputs != null) {
					for (int j = 0; j < outputs.length; j++) {
						String outputName = outputs[j].getAttribute("Key");
						locationX = Integer.valueOf(outputs[j].getAttribute("LocationX"));
						locationY = Integer.valueOf(outputs[j].getAttribute("LocationY"));
						locationConfig.setOutputLocation(outputName, new Point(locationX, locationY));
					}
				}
				config.addLocationConfig(locationConfig);
			}
		}

		return config;
	}

	public class ProcessSizeConfig {
		private String processKey;
		private int width;
		private int height;

		private Map<String, Integer> outputsWidth = new ConcurrentHashMap<>();
		private Map<String, Integer> outputsHeight = new ConcurrentHashMap<>();

		public ProcessSizeConfig(String processKey, int width, int height) {
			if (StringUtilities.isNullOrEmpty(processKey)) {
				throw new IllegalArgumentException();
			}

			this.processKey = processKey;
			this.width = width;
			this.height = height;
		}

		public String getProcessKey() {
			return processKey;
		}

		public int getWidth() {
			return this.width;
		}

		public void setWidth(int width) {
			this.width = width;
		}

		public int getHeight() {
			return this.height;
		}

		public void setHeight(int height) {
			this.height = height;
		}

		public int getOutputWidth(String outputName) {
			if (this.outputsWidth.containsKey(outputName)) {
				return this.outputsWidth.get(outputName);
			}

			return Integer.MIN_VALUE;
		}

		public void setOutputWidth(String outputName, int width) {
			if (!StringUtilities.isNullOrEmpty(outputName)) {
				this.outputsWidth.put(outputName, width);
			}
		}

		public int getOutputHeight(String outputName) {
			if (this.outputsHeight.containsKey(outputName)) {
				return this.outputsHeight.get(outputName);
			}

			return Integer.MIN_VALUE;
		}

		public void setOutputHeight(String outputName, int height) {
			if (!StringUtilities.isNullOrEmpty(outputName)) {
				this.outputsHeight.put(outputName, height);
			}
		}
	}
}
