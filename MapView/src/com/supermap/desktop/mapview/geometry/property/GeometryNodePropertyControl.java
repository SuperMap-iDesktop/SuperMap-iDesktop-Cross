package com.supermap.desktop.mapview.geometry.property;

import com.supermap.data.Recordset;
import com.supermap.desktop.controls.property.AbstractPropertyControl;
import com.supermap.desktop.enums.PropertyType;
import com.supermap.desktop.geometry.Implements.DGeometryFactory;
import com.supermap.desktop.mapview.geometry.property.geometryNode.GeometryNodeFactory;
import com.supermap.desktop.mapview.geometry.property.geometryNode.IGeometryNode;
import com.supermap.desktop.mapview.geometry.property.geometryNode.ModifiedChangedListener;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.button.SmButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author XiaJT
 */
public class GeometryNodePropertyControl extends AbstractPropertyControl {
	private final ModifiedChangedListener modifiedChangedListener = new ModifiedChangedListener() {
		@Override
		public void modified(boolean newValue) {
			initButtonStates();
		}
	};
	private Recordset recordset = null;
	private IGeometryNode geometryNode = null;
	private JPanel panelButtons = new JPanel();
	private SmButton buttonApply = new SmButton();
	private SmButton buttonReset = new SmButton();

	public GeometryNodePropertyControl(Recordset recordset) {
		super(CoreProperties.getString("String_NodeInfo"));
		this.recordset = recordset;
		initPanelButtons();
		init();
		addListeners();
		initResources();
	}

	@Override
	public void refreshData() {
		if (geometryNode != null) {
			geometryNode.refreshData();
		}
	}

	private void addListeners() {
		buttonApply.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				geometryNode.apply(recordset);
			}
		});
		buttonReset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				geometryNode.reset();
			}
		});
	}

	@Override
	public PropertyType getPropertyType() {
		return PropertyType.GEOMETRY_NODE;
	}

	public void setRecordset(Recordset recordset) {
		if (this.recordset != null && !this.recordset.isClosed()) {
			this.recordset.close();
			this.recordset.dispose();
		}
		if (geometryNode != null) {
			geometryNode.removeModifiedChangedListener(modifiedChangedListener);
			geometryNode.dispose();
		}
		this.recordset = recordset;
		init();
	}

	private void init() {
		initComponent();
		initLayout();
		initButtonStates();
	}

	private void initResources() {
		buttonApply.setText(CommonProperties.getString(CommonProperties.Apply));
		buttonReset.setText(CommonProperties.getString(CommonProperties.Reset));
	}

	private void initComponent() {
		this.geometryNode = GeometryNodeFactory.getGeometryNode(DGeometryFactory.create(recordset.getGeometry()));
		if (geometryNode != null) {
			geometryNode.addModifiedChangedListener(modifiedChangedListener);
		}
	}

	//region 初始化布局
	private void initLayout() {
		this.removeAll();
		this.setLayout(new GridBagLayout());
		this.add(geometryNode == null ? new JPanel() : geometryNode.getPanel(), new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setInsets(10, 10, 0, 0).setWeight(1, 1));
		this.add(panelButtons, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0).setInsets(5, 10, 10, 10));
	}

	private void initPanelButtons() {
		panelButtons.setLayout(new GridBagLayout());
		panelButtons.add(buttonReset, new GridBagConstraintsHelper(0, 0, 1, 1).setFill(GridBagConstraints.NONE).setWeight(1, 1).setAnchor(GridBagConstraints.EAST));
		panelButtons.add(buttonApply, new GridBagConstraintsHelper(1, 0, 1, 1).setFill(GridBagConstraints.NONE).setWeight(0, 1).setAnchor(GridBagConstraints.EAST).setInsets(0, 5, 0, 0));
	}
	//endregion

	private void initButtonStates() {
		if (geometryNode != null) {
			buttonApply.setEnabled(geometryNode.isModified());
			buttonReset.setEnabled(geometryNode.isModified());
		} else {
			buttonApply.setEnabled(false);
			buttonReset.setEnabled(false);
		}
	}

	@Override
	public void dispose() {
		if (geometryNode != null) {
			geometryNode.dispose();
		}
	}

	@Override
	public void hidden() {
		if (geometryNode != null) {
			geometryNode.hidden();
		}
	}
}
