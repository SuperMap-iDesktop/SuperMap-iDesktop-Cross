package com.supermap.desktop.mapview.geometry.property.geometryNode;

import com.supermap.desktop.geometry.Abstract.IGeometry;

import javax.swing.*;

/**
 * @author XiaJT
 */
public class JPanelGeometryNodeParameterization extends JPanel {
	private IGeometry geometry;

	public JPanelGeometryNodeParameterization(IGeometry geometry) {
		this.geometry = geometry;
		init();
	}

	private void init() {
		initComponents();
		initListener();
		initLayout();
		initResources();
		initComponentState();
	}

	private void initComponents() {

	}

	private void initLayout() {

	}

	private void initListener() {

	}

	private void initResources() {

	}

	private void initComponentState() {

	}
}
