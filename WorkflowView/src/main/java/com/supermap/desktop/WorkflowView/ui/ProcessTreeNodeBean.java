package com.supermap.desktop.WorkflowView.ui;

import com.supermap.desktop.process.ProcessResources;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import java.util.ArrayList;

/**
 * @author XiaJT
 */
public class ProcessTreeNodeBean {
	private String iconPath;
	private String key;
	private String name;

	private static final Icon firstIcon = ProcessResources.getIcon("/processresources/Tree_Node1.png");
	private static final Icon secondIcon = ProcessResources.getIcon("/processresources/Tree_Node2.png");
	private static final Icon thirdIcon = ProcessResources.getIcon("/processresources/Tree_Node3.png");

	private ArrayList<ProcessTreeNodeBean> processTreeNodeBeanArrayList = new ArrayList<>();

	public String getIconPath() {
		return iconPath;
	}

	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getChildCount() {
		return processTreeNodeBeanArrayList.size();
	}

	public void addChild(ProcessTreeNodeBean child) {
		processTreeNodeBeanArrayList.add(child);
	}

	public void removeChild(ProcessTreeNodeBean child) {
		processTreeNodeBeanArrayList.remove(child);
	}

	public void removeChild(int index) {
		processTreeNodeBeanArrayList.remove(index);
	}

	public ProcessTreeNodeBean getChildAt(int index) {
		return processTreeNodeBeanArrayList.get(index);
	}

	public Icon getIcon() {
		if (!StringUtilities.isNullOrEmpty(iconPath)) {
			return ProcessResources.getIcon(iconPath);
		}
		int level = getLevel();
		if (level == 1) {
			return thirdIcon;
		} else if (level == 2) {
			return secondIcon;
		}
		return firstIcon;
	}

	private int getLevel() {
		int level = 0;
		for (ProcessTreeNodeBean bean : processTreeNodeBeanArrayList) {
			level = Math.max(0, bean.getLevel());
		}
		return level + 1;
	}


}
