package com.supermap.desktop.mapview.geometry.property;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import com.supermap.data.Recordset;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.controls.property.AbstractPropertyControl;
import com.supermap.desktop.enums.PropertyType;
import com.supermap.desktop.geometry.property.geoText.*;
import com.supermap.desktop.properties.*;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.*;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.mapping.*;

public class GeometryGeoTextPropertyControl extends AbstractPropertyControl {
	private static final long serialVersionUID = 1L;
	private Recordset recordset;
	private JPanel panelButtons;
	private JButton buttonReset;
	private JButton buttonApply;
	private IGeoTextProperty panelGeoTextProperty;
	private LayersTree layersTree = UICommonToolkit.getLayersManager().getLayersTree();
	private ActionListener applyListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			panelGeoTextProperty.apply(recordset);
			initButtonStates(false);
		}
	};
	private ActionListener resetListener= new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			panelGeoTextProperty.reset();
			initButtonStates(false);
		}
	};
	private GeoInfoChangeListener geoInfoLisener= new GeoInfoChangeListener() {

		@Override
		public void modify(boolean isModified) {
			if (isModified) {
				initButtonStates(isModified);
			}
		}
	};
	private MouseAdapter layersTreeListener = new MouseAdapter() {
		
		@Override
		public void mouseClicked(MouseEvent e) {
			panelGeoTextProperty.enabled(((IFormMap) Application.getActiveApplication().getActiveForm()).getActiveLayers()[0].isEditable());
		}
	};

	public GeometryGeoTextPropertyControl(Recordset recordset) {
		super(CoreProperties.getString("String_TextInfo"));
		this.recordset = recordset;
		initComponents();
		initResources();
		registEvents();
	}

	private void registEvents() {
		removeEvents();
		this.buttonReset.addActionListener(resetListener);
		this.buttonApply.addActionListener(applyListener);
		this.panelGeoTextProperty.addGeoTextChangeListener(geoInfoLisener);
		this.layersTree.addMouseListener(layersTreeListener);
	}
	private void removeEvents(){
		this.buttonReset.removeActionListener(resetListener);
		this.buttonApply.removeActionListener(applyListener);
		this.panelGeoTextProperty.removeGeoTextChangeListener(geoInfoLisener);
		this.layersTree.removeMouseListener(layersTreeListener);
	}
	
	private void initComponents() {
		this.removeAll();
		this.setLayout(new GridBagLayout());
		initPanelButtons();
		//@formatter:off
		panelGeoTextProperty = new JPanelGeoTextProperty(recordset.getGeometry());
		panelGeoTextProperty.enabled(((IFormMap) Application.getActiveApplication().getActiveForm()).getActiveLayers()[0].isEditable());
		this.add(panelGeoTextProperty.getPanel(),new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.NORTH).setFill(GridBagConstraints.BOTH).setWeight(1, 1).setInsets(10,10,0,10));
		this.add(panelButtons,new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0).setInsets(5, 10, 10, 10));
		//@formatter:on
	}

	private void initResources() {
		buttonApply.setText(CommonProperties.getString(CommonProperties.Apply));
		buttonReset.setText(CommonProperties.getString(CommonProperties.Reset));
	};

	private void initPanelButtons() {
		if (null == panelButtons) {
			this.panelButtons = new JPanel();
		}
		if (null == buttonApply) {
			this.buttonApply = new SmButton();
		}
		if (null == buttonReset) {
			this.buttonReset = new SmButton();
		}
		initButtonStates(false);
		this.panelButtons.setLayout(new GridBagLayout());
		//@formatter:off
		this.panelButtons.add(buttonReset,new GridBagConstraintsHelper(0, 0, 1, 1).setFill(GridBagConstraints.NONE).setWeight(1, 1).setAnchor(GridBagConstraints.EAST));
		this.panelButtons.add(buttonApply,new GridBagConstraintsHelper(1, 0, 1, 1).setFill(GridBagConstraints.NONE).setWeight(0, 1).setAnchor(GridBagConstraints.EAST).setInsets(0, 5, 0, 0));
		//@formatter:on
	}

	@Override
	public PropertyType getPropertyType() {
		return PropertyType.GEOMETRY_TEXT;
	}

	@Override
	public void refreshData() {

	}

	public void setRecordset(Recordset recordset) {
		if (this.recordset != null && !this.recordset.isClosed()) {
			this.recordset.close();
			this.recordset.dispose();
		}
		this.recordset = recordset;
		initComponents();
		removeEvents();
		registEvents();
	}

	private void initButtonStates(boolean enabled) {
		this.buttonApply.setEnabled(enabled);
		this.buttonReset.setEnabled(enabled);
	}
}
