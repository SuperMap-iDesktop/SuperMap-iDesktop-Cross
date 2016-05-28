package com.supermap.desktop.mapview.geometry.property.geometryNode;

import com.supermap.data.Recordset;
import com.supermap.desktop.geometry.Abstract.IGeometry;
import com.supermap.desktop.mapview.geometry.property.geometryNode.parameterizationModels.GeometryNodeParameterTableModel;
import com.supermap.desktop.mapview.geometry.property.geometryNode.parameterizationModels.IGeometryP;

import javax.swing.*;

/**
 * @author XiaJT
 */
public class JPanelGeometryNodeParameterization extends JPanel implements IGeometryNode {
	private IGeometry geometry;
	private IGeometryP iGeometryP;
	private JLabel labelGeometryType = new JLabel();
	private JTextField textFieldGeometryType = new JTextField();
	private JTable table = new JTable();
	private GeometryNodeParameterTableModel tableModel = new GeometryNodeParameterTableModel();

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

	@Override
	public void refreshData() {

	}

	@Override
	public JPanel getPanel() {
		return this;
	}

	@Override
	public void dispose() {

	}

	@Override
	public boolean isModified() {
		return false;
	}

	@Override
	public void reset() {

	}

	@Override
	public void apply(Recordset recordset) {

	}

	@Override
	public void addModifiedChangedListener(ModifiedChangedListener modifiedChangedListener) {

	}

	@Override
	public void removeModifiedChangedListener(ModifiedChangedListener modifiedChangedListener) {

	}

	@Override
	public void hidden() {

	}
}
