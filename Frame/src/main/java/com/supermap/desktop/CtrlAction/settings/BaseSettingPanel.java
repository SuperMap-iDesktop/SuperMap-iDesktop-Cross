package com.supermap.desktop.CtrlAction.settings;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * @author XiaJT
 */
public abstract class BaseSettingPanel extends JPanel {

	protected ArrayList<Component> changedValues;

	public BaseSettingPanel() {
		super();
		changedValues = new ArrayList<>();
		init();
	}

	private void init() {
		initComponents();
		initLayout();
		initResources();
		initComponentStates();
		initListeners();
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
