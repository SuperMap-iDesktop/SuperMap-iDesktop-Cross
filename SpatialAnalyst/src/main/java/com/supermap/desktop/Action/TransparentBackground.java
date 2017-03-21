package com.supermap.desktop.Action;

import com.supermap.desktop.spatialanalyst.SpatialAnalystProperties;
import com.supermap.desktop.utilities.SystemPropertyUtilities;
import com.supermap.ui.MapControl;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.HashMap;

public class TransparentBackground extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel jLabelDatasource = new JLabel("datasource:");
	private JLabel jLabelDataset = new JLabel("dataset:");
	private JLabel jLabelPointX = new JLabel("pointX:");
	private JLabel jLabelPointY = new JLabel("pointY:");
	private JLabel jLabelRowOfGrid = new JLabel("columnOfGrid:");
	private JLabel jLabelColumnOfGrid = new JLabel("lineOfGrid:");
	private JLabel jLabelGridValue = new JLabel("gridValue:");
	private static final int gapWidth = 10;
	public static HashMap<MapControl, TransparentBackground> queryGridMap = new HashMap<>();

	public TransparentBackground() {
		this.setBorder(new LineBorder(Color.LIGHT_GRAY));
		this.setBackground(new Color(255, 255, 255, 220));
		initResources();
		initComponents();
		if (SystemPropertyUtilities.isWindows()) {
			this.setSize(230, 200);
		} else {
			this.setSize(268, 220);
		}
	}

	/**
	 * 资源化
	 */
	private void initResources() {
		jLabelDatasource.setText(SpatialAnalystProperties.getString("String_QueryGridValue_Datasource").replace("{0}", "-"));
		jLabelDataset.setText(SpatialAnalystProperties.getString("String_QueryGridValue_Dataset").replace("{0}", "-"));
		jLabelPointX.setText(SpatialAnalystProperties.getString("String_QueryGridValue_CooX").replace("{0}", "-"));
		jLabelPointY.setText(SpatialAnalystProperties.getString("String_QueryGridValue_CooY").replace("{0}", "-"));
		jLabelRowOfGrid.setText(SpatialAnalystProperties.getString("String_QueryGridValue_Row").replace("{0}", "-"));
		jLabelColumnOfGrid.setText(SpatialAnalystProperties.getString("String_QueryGridValue_Collunm").replace("{0}", "-"));
		jLabelGridValue.setText(SpatialAnalystProperties.getString("String_QueryGridValue_GridValue").replace("{0}", "-"));
	}

	/**
	 * 初始化界面信息
	 */
	private void initComponents() {
		//@formatter:off
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup()
				.addGroup(groupLayout.createSequentialGroup().addGap(gapWidth).addComponent(jLabelDatasource))
				.addGroup(groupLayout.createSequentialGroup().addGap(gapWidth).addComponent(jLabelDataset))
				.addGroup(groupLayout.createSequentialGroup().addGap(gapWidth).addComponent(jLabelPointX))
				.addGroup(groupLayout.createSequentialGroup().addGap(gapWidth).addComponent(jLabelPointY))
				.addGroup(groupLayout.createSequentialGroup().addGap(gapWidth).addComponent(jLabelRowOfGrid))
				.addGroup(groupLayout.createSequentialGroup().addGap(gapWidth).addComponent(jLabelColumnOfGrid))
				.addGroup(groupLayout.createSequentialGroup().addGap(gapWidth).addComponent(jLabelGridValue)));
		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
				.addComponent(jLabelDatasource)
				.addComponent(jLabelDataset)
				.addComponent(jLabelPointX)
				.addComponent(jLabelPointY)
				.addComponent(jLabelRowOfGrid)
				.addComponent(jLabelColumnOfGrid)
				.addComponent(jLabelGridValue));
		setLayout(groupLayout);
		//@formatter:on
	}

	public JLabel getjLabelDatasource() {
		return jLabelDatasource;
	}

	public void setjLabelDatasource(JLabel jLabelDatasource) {
		this.jLabelDatasource = jLabelDatasource;
	}

	public JLabel getjLabelDataset() {
		return jLabelDataset;
	}

	public void setjLabelDataset(JLabel jLabelDataset) {
		this.jLabelDataset = jLabelDataset;
	}

	public JLabel getjLabelPointX() {
		return jLabelPointX;
	}

	public void setjLabelPointX(JLabel jLabelPointX) {
		this.jLabelPointX = jLabelPointX;
	}

	public JLabel getjLabelPointY() {
		return jLabelPointY;
	}

	public void setjLabelPointY(JLabel jLabelPointY) {
		this.jLabelPointY = jLabelPointY;
	}

	public JLabel getjLabelRowOfGrid() {
		return jLabelRowOfGrid;
	}

	public void setjLabelRowOfGrid(JLabel jLabelRowOfGrid) {
		this.jLabelRowOfGrid = jLabelRowOfGrid;
	}

	public JLabel getjLabelColumnOfGrid() {
		return jLabelColumnOfGrid;
	}

	public void setjLabelColumnOfGrid(JLabel jLabelColumnOfGrid) {
		this.jLabelColumnOfGrid = jLabelColumnOfGrid;
	}

	public JLabel getjLabelGridValue() {
		return jLabelGridValue;
	}

	public void setjLabelGridValue(JLabel jLabelGridValue) {
		this.jLabelGridValue = jLabelGridValue;
	}

}
