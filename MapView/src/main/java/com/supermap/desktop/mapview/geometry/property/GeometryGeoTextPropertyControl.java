package com.supermap.desktop.mapview.geometry.property;

import com.supermap.data.Recordset;
import com.supermap.desktop.Application;
import com.supermap.desktop.FormMap;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.property.AbstractPropertyControl;
import com.supermap.desktop.enums.PropertyType;
import com.supermap.desktop.geometry.property.geoText.GeoInfoChangeListener;
import com.supermap.desktop.geometry.property.geoText.IGeoTextProperty;
import com.supermap.desktop.geometry.property.geoText.JPanelGeoTextProperty;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.LayersTree;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.utilities.MapUtilities;
import com.supermap.mapping.Layer;
import com.supermap.mapping.LayerEditableChangedEvent;
import com.supermap.mapping.LayerEditableChangedListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

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
	private ActionListener resetListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			panelGeoTextProperty.reset(recordset);
			initButtonStates(false);
		}
	};
	private GeoInfoChangeListener geoInfoLisener = new GeoInfoChangeListener() {

		@Override
		public void modify(boolean isModified) {
			if (isModified) {
				initButtonStates(isModified);
			}
		}
	};
	private LayerEditableChangedListener layerEditableChangedListener = new LayerEditableChangedListener() {
		@Override
		public void editableChanged(LayerEditableChangedEvent layerEditableChangedEvent) {
			if (SwingUtilities.getWindowAncestor(GeometryGeoTextPropertyControl.this) == null || !SwingUtilities.getWindowAncestor(GeometryGeoTextPropertyControl.this).isVisible()) {
				currentForm.getMapControl().getMap().getLayers().removeLayerEditableChangedListener(this);
				return;
			}
			if (layerEditableChangedEvent.getLayer().getDataset() == recordset.getDataset()) {
				boolean editable = isEditable();
				panelGeoTextProperty.enabled(editable);
				if (!editable && panelGeoTextProperty.isModified()) {
					if (UICommonToolkit.showConfirmDialogYesNo(ControlsProperties.getString("String_PropertyInfo_Modifyed_Notify")) == JOptionPane.YES_OPTION) {
						panelGeoTextProperty.apply(recordset);
					} else {
						panelGeoTextProperty.reset(recordset);
					}
					initButtonStates(false);
				}
			}
		}
	};
	private boolean isEditable() {
		try {
			ArrayList<Layer> layers = MapUtilities.getLayers(currentForm.getMapControl().getMap());
			for (Layer layer : layers) {
				if (layer.getDataset() == recordset.getDataset() && layer.isEditable()) {
					return true;
				}
			}
		} catch (Exception ignore) {
			// 地图dispose没接口判断
		}
		return false;
	}

	private FormMap currentForm;

	public GeometryGeoTextPropertyControl(Recordset recordset) {
		super(CoreProperties.getString("String_TextInfo"));
		this.recordset = recordset;
		initComponents();
		initResources();
		registEvents();
	}

	private void registEvents() {
		removeEvents();
		this.currentForm.getMapControl().getMap().getLayers().addLayerEditableChangedListener(layerEditableChangedListener);
		this.buttonReset.addActionListener(resetListener);
		this.buttonApply.addActionListener(applyListener);
		this.panelGeoTextProperty.addGeoTextChangeListener(geoInfoLisener);
	}

	private void removeEvents() {
		this.buttonReset.removeActionListener(resetListener);
		this.buttonApply.removeActionListener(applyListener);
		this.panelGeoTextProperty.removeGeoTextChangeListener(geoInfoLisener);
		this.currentForm.getMapControl().getMap().getLayers().removeLayerEditableChangedListener(layerEditableChangedListener);
	}
	private void initComponents() {
		this.removeAll();
		if (Application.getActiveApplication().getActiveForm() instanceof FormMap) {
			this.currentForm = (FormMap) Application.getActiveApplication().getActiveForm();
		}
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
