package com.supermap.desktop.process;

import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.utilities.StringUtilities;

import java.awt.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by highsad on 2017/7/5.
 */
public class WorkflowUIConfig {
	private Map<String, ProcessLocationConfig> processLocationMap = new ConcurrentHashMap<>();
	private Map<String, ProcessSizeConfig> processSizeMap = new ConcurrentHashMap<>();

	public Point getProcessLocation(IProcess process) {
		if (process != null) {
			return getProcessLocation(process.getKey());
		}

		return null;
	}

	public Point getProcessLocation(String processKey) {
		if (this.processLocationMap.containsKey(processKey)) {
			return this.processLocationMap.get(processKey).getLocation();
		}

		return null;
	}

	public ProcessLocationConfig getProcessConfig(String processKey) {
		return this.processLocationMap.get(processKey);
	}

	public class ProcessLocationConfig {
		private String processKey;
		private Point location;

		private Map<String, Point> outputsLoc = new ConcurrentHashMap<>();

		public ProcessLocationConfig(String processKey) {
			this(processKey, null);
		}

		public ProcessLocationConfig(String processKey, Point location) {
			if (StringUtilities.isNullOrEmpty(processKey)) {
				throw new IllegalArgumentException();
			}

			this.processKey = processKey;
			this.location = location;
		}

		public String getProcessKey() {
			return processKey;
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
