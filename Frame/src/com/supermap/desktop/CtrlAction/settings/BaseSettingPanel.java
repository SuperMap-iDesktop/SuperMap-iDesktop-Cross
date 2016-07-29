package com.supermap.desktop.CtrlAction.settings;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

/**
 * @author XiaJT
 */
public abstract class BaseSettingPanel extends JPanel {

	protected HashMap<Component, Object> changedValues;

	public BaseSettingPanel() {
		super();
		changedValues = new HashMap<>();
		init();
	}

	private void init() {
		initComponents();
		initLayout();
		initListeners();
		initResources();
		initComponentStates();
	}

	protected void initComponents() {

	}

	protected void initLayout() {

	}

	protected void initListeners() {

	}

	protected void initResources() {

	}

	protected void initComponentStates() {

	}

	protected abstract void apply();

	protected abstract void dispose();
}
