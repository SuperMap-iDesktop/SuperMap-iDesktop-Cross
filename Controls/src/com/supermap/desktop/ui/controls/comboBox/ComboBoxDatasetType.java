package com.supermap.desktop.ui.controls.comboBox;

import com.supermap.data.DatasetType;
import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;

public class ComboBoxDatasetType extends UIComboBox {

	/**
	 * 
	 */
	private transient static final long serialVersionUID = 1L;
	private transient DatasetType[] supportedDatasetTypes;

	public ComboBoxDatasetType() {
		supportedDatasetTypes = new DatasetType[] { DatasetType.CAD, DatasetType.GRID, DatasetType.GRIDCOLLECTION, DatasetType.IMAGE,
				DatasetType.IMAGECOLLECTION, DatasetType.LINE, DatasetType.LINE3D, DatasetType.LINEM, DatasetType.LINKTABLE, DatasetType.NETWORK,
				DatasetType.PARAMETRICLINE, DatasetType.PARAMETRICREGION, DatasetType.POINT, DatasetType.POINT3D, DatasetType.REGION, DatasetType.REGION3D,
				DatasetType.TABULAR, DatasetType.TEXT, DatasetType.TOPOLOGY, DatasetType.WCS, DatasetType.WMS };

		initialize();
	}


	public DatasetType[] getSupportedDatasetTypes() {
		return this.supportedDatasetTypes;
	}

	public void setSupportedDatasetTypes(DatasetType[] value) {
		this.supportedDatasetTypes = value;
		initialize();
	}

	protected void initialize() {
		super.removeAllItems();	
		try {
			for (DatasetType datasetType : this.supportedDatasetTypes) {
				ComboBoxItem item = new ComboBoxItem(datasetType, CommonToolkit.DatasetTypeWrap.findName(datasetType), null);
				this.addItem(item);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	public DatasetType getSelectedDatasetType() {
		DatasetType type = DatasetType.TOPOLOGY;
		try {
			ComboBoxItem item = this.getSelectedItemObject();
			type = (DatasetType) item.getData();
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return type;
	}

	public void setSelectedDatasetType(DatasetType value) {
		try {
			ComboBoxItem selectedItem = null;
			for (int i = 0; i < this.getItemCount(); i++) {
				ComboBoxItem item = (ComboBoxItem) this.getItemAt(i);
				if (value == (DatasetType) item.getData()) {
					selectedItem = item;
					break;
				}
			}
			this.setSelectedItemObject(selectedItem);
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	public ComboBoxItem getSelectedItemObject() {
		ComboBoxItem item = null;
		if (this.getSelectedItem() != null) {
			item = (ComboBoxItem) this.getSelectedItem();
		}

		return item;
	}

	public void setSelectedItemObject(ComboBoxItem value) {
		this.setSelectedItem(value);
	}

	public Object getSelectedItemData() {
		Object obj = null;
		try {
			obj = this.getSelectedItemObject().getData();
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return obj;
	}

	public void setSelectedItemData(Object value) {
		if (value != null) {
			for (int i = 0; i < this.getItemCount(); i++) {
				ComboBoxItem item = (ComboBoxItem) this.getItemAt(i);
				if (item.getData().toString().equals(value.toString())) {
					this.setSelectedItemObject(item);
					break;
				}
			}
		} else {
			this.setSelectedItemObject(null);
		}
	}
}
