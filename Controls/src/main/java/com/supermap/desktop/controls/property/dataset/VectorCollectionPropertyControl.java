package com.supermap.desktop.controls.property.dataset;

import com.supermap.data.*;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.property.AbstractPropertyControl;
import com.supermap.desktop.controls.utilities.ComponentUIUtilities;
import com.supermap.desktop.enums.PropertyType;
import com.supermap.desktop.ui.controls.CollectionDataset.JDialogCreateCollectionDataset;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.utilities.DatasetTypeUtilities;
import com.supermap.desktop.utilities.DatasourceUtilities;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by xie on 2017/9/4.
 */
public class VectorCollectionPropertyControl extends AbstractPropertyControl {

	private JLabel labelDatasetCount;
	private JTextField textFieldDatasetCount;
	private SmButton buttonSetDatasetCount;
	private JLabel labelDatasetType;
	private JTextField textFieldDatasetType;
	private DatasetVector datasetVector;
	private ActionListener actionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			buttonAddDatasetToCollection();
		}
	};

	/**
	 * Create the panel.
	 *
	 * @param datasetVector
	 */
	public VectorCollectionPropertyControl(DatasetVector datasetVector) {
		super(ControlsProperties.getString("String_Vector"));
		initializeComponents();
		initializeResources();
		setComponentName();
		setDatasetVector(datasetVector);
	}

	private void setComponentName() {
		ComponentUIUtilities.setName(this.labelDatasetCount, "VectorPropertyControl_labelDatasetCount");
		ComponentUIUtilities.setName(this.textFieldDatasetCount, "VectorPropertyControl_textFieldDatasetCount");
		ComponentUIUtilities.setName(this.buttonSetDatasetCount, "VectorPropertyControl_buttonSetDatasetCount");
		ComponentUIUtilities.setName(this.labelDatasetType, "VectorPropertyControl_labelDatasetType");
		ComponentUIUtilities.setName(this.textFieldDatasetType, "VectorPropertyControl_textFieldDatasetType");
	}

	private void initializeResources() {
		this.labelDatasetCount.setText(ControlsProperties.getString("String_SubDatasetCount"));
		this.buttonSetDatasetCount.setText(ControlsProperties.getString("String_Button_Setting"));
		this.labelDatasetType.setText(ControlsProperties.getString("String_SubDatasetType"));
	}

	private void initializeComponents() {
		this.labelDatasetCount = new JLabel();
		this.textFieldDatasetCount = new JTextField();
		this.buttonSetDatasetCount = new SmButton();
		this.labelDatasetType = new JLabel();
		this.textFieldDatasetType = new JTextField();
		JPanel panelBasic = new JPanel();
		panelBasic.setLayout(new GridBagLayout());
		panelBasic.add(labelDatasetCount, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(0, 0).setFill(GridBagConstraints.NONE).setInsets(10, 10, 0, 0));
		panelBasic.add(textFieldDatasetCount, new GridBagConstraintsHelper(1, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 5, 0, 0));
		panelBasic.add(buttonSetDatasetCount, new GridBagConstraintsHelper(3, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(0, 0).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 5, 0, 10));
		panelBasic.add(labelDatasetType, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(0, 0).setFill(GridBagConstraints.NONE).setInsets(5, 10, 0, 0));
		panelBasic.add(textFieldDatasetType, new GridBagConstraintsHelper(1, 1, 3, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 5, 0, 10));
		panelBasic.setBorder(new TitledBorder(ControlsProperties.getString("String_VectorCollection")));
		this.setLayout(new GridBagLayout());
		this.add(panelBasic, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL).setInsets(10));
		this.add(new JPanel(), new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1));
	}

	@Override
	public PropertyType getPropertyType() {
		return PropertyType.VECTOR_COLLECTION;
	}

	@Override
	public void refreshData() {
		setDatasetVector(this.datasetVector);
	}

	public void setDatasetVector(DatasetVector datasetVector) {
		this.datasetVector = datasetVector;
		fillComponents();
		setComponentsEnabled();
		registerEvents();
	}

	private void registerEvents() {
		removeEvents();
		this.buttonSetDatasetCount.addActionListener(this.actionListener);
	}

	private void removeEvents() {
		this.buttonSetDatasetCount.removeActionListener(this.actionListener);
	}

	private void setComponentsEnabled() {
		this.textFieldDatasetCount.setEnabled(false);
		this.textFieldDatasetType.setEnabled(false);
	}

	private void fillComponents() {
		try {
			this.textFieldDatasetCount.setEnabled(false);
			if (this.datasetVector.getCollectionDatasetCount() > 0) {
				this.textFieldDatasetCount.setText(String.valueOf(this.datasetVector.getCollectionDatasetCount()));
				this.textFieldDatasetType.setText(DatasetTypeUtilities.toString(this.datasetVector.GetSubCollectionDatasetType()));
			}
		} catch (Exception ex) {
			//GetSubCollectionDatasetType异常
		}

	}

	private void buttonAddDatasetToCollection() {
		ArrayList<DatasetVector> datasetVectors = new ArrayList<>();
		ArrayList<CollectionDatasetInfo> collectionDatasetInfos = this.datasetVector.getCollectionDatasetInfos();
		for (int i = 0; i < collectionDatasetInfos.size(); i++) {
			CollectionDatasetInfo collectionDatasetInfo = collectionDatasetInfos.get(i);
			DatasourceConnectionInfo connectionInfo = collectionDatasetInfo.getDatasourceConnectInfo();
			if (null != connectionInfo) {
				Datasource datasource = DatasourceUtilities.getDatasource(connectionInfo);
				if (null != datasource) {
					Dataset childDataset = datasource.getDatasets().get(collectionDatasetInfo.getDatasetName());
					if (null != childDataset && childDataset instanceof DatasetVector) {
						datasetVectors.add((DatasetVector) childDataset);
					}
				}
			}
		}
		JDialogCreateCollectionDataset createCollectionDataset = new JDialogCreateCollectionDataset(0, this.datasetVector, datasetVectors.toArray(new DatasetVector[datasetVectors.size()]));
		createCollectionDataset.isSetDatasetCollectionCount(true);
		createCollectionDataset.showDialog();
	}
}
