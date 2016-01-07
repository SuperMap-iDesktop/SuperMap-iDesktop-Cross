package com.supermap.desktop.ui.controls.comboBox;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.data.Datasource;
import com.supermap.desktop.Application;

public class ComboBoxDataset extends UIComboBox {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private transient boolean isFirstItemEmpty;
	private transient Datasource datasource;
	private transient DatasetType[] datasetTypes;
	private boolean autoSelect = true;

	public ComboBoxDataset() {
		this.isFirstItemEmpty = false;
	}

	/**
	 * 获取或设置组合框第一项是否可为空，默认值为false。
	 */
	public boolean getIsFirstItemEmpty() {
		return this.isFirstItemEmpty;
	}

	public void setIsFirstItemEmpty(boolean value) {
		try {
			if (this.isFirstItemEmpty != value) {
				this.isFirstItemEmpty = value;
			}

			if (this.getItemCount() > 0) {
				ComboBoxItem item = (ComboBoxItem) this.getItemAt(0);
				Dataset dataset = itemObjectToDataset(item);
				if (this.isFirstItemEmpty && dataset != null) {
					ComboBoxItem itemNull = buildItemObject(null);
					this.insertItemAt(itemNull, 0);
				} else {
					if (dataset == null) {
						this.remove(0);
					}
				}
			} else if (this.getItemCount() == 0 && this.isFirstItemEmpty) {
				ComboBoxItem itemNull = buildItemObject(null);
				this.insertItemAt(itemNull, 0);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	/**
	 * 获取或设置 ComboBox 选中的数据集
	 * 
	 * @return
	 */
	public Dataset getSelectedDataset() {
		Dataset dataset = null;

		try {
			dataset = itemObjectToDataset((ComboBoxItem) this.getSelectedItem());
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return dataset;
	}

	public void setSelectedDataset(Dataset value) {
		try {
			if (value != null) {
				this.setSelectedItem(datasetToItemObject(value));
			} else {
				this.setSelectedItem(null);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	/**
	 * 获取或设置关联的数据源
	 */
	public Datasource getDatasource() {
		return this.datasource;
	}

	public void setDatasource(Datasource value) {
		this.datasource = value;
		updateItems();
	}

	/**
	 * 获取或设置支持展示的数据集类型
	 */
	public DatasetType[] getDatasetTypes() {
		return this.datasetTypes;
	}

	public void setDatasetTypes(DatasetType... value) {
		this.datasetTypes = value;
		updateItems();
	}

	/**
	 * 获取或设置是否自动选中一个子项。如果为true，则会自动选中工作空间中的当前激活数据集中的第一个，默认值为true。
	 */
	public boolean getAutoSelectActiveDataset() {
		return this.autoSelect;
	}

	public void setAutoSelectActiveDataset(boolean value) {
		this.autoSelect = value;
	}

	/**
	 * 将一个数据集封装成一个ComboBoxItem。
	 * 
	 * @param dataset
	 * @return
	 */
	private ComboBoxItem buildItemObject(Dataset dataset) {
		String name = "";
		if (dataset != null) {
			name = dataset.getName();
		}
		return new ComboBoxItem(dataset, name);
	}

	/**
	 * 获取ComboBox下拉列表中某个ComboBoxItem对应的数据集。
	 * 
	 * @param itemObject
	 * @return
	 */
	private Dataset itemObjectToDataset(ComboBoxItem itemObject) {
		Dataset dataset = null;

		try {
			if (itemObject != null) {// 得加个判断，因为有些数据集是在下拉项中不存在的，比如说网络数据集的子数据集
				dataset = (Dataset) itemObject.getData();
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return dataset;
	}

	/**
	 * 获取ComboBox下拉列表中某个数据集对应的ComboBoxItem。
	 * 
	 * @param dataset 待查找的数据集对象
	 * @return 数据集下拉项对应的ComboBoxItem
	 */
	public ComboBoxItem datasetToItemObject(Dataset dataset) {
		ComboBoxItem itemObject = null;
		try {
			for (int i = 0; i < this.getItemCount(); i++) {
				ComboBoxItem item = (ComboBoxItem) this.getItemAt(i);
				Dataset itemDataset = itemObjectToDataset(item);
				if (itemDataset == dataset) {
					itemObject = item;
					break;
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return itemObject;
	}

	/**
	 * 更新组合框的子项
	 */
	private void updateItems() {
		try {
			this.removeAllItems();
			if (this.getDatasource() != null) {
				try {
					for (int i = 0; i < this.getDatasource().getDatasets().getCount(); i++) {
						Dataset dataset = this.getDatasource().getDatasets().get(i);
						DatasetType type = dataset.getType();
						if (this.getDatasetTypes() != null && this.getDatasetTypes().length > 0 && !isSupportDatasetType(type)) {
							continue;
						} else {
							this.add(dataset);
						}
					}
				} catch (Exception ex) {
					return;
				}
			}

		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	/**
	 * 根据传入的数据集类型判断此类型是否支持显示
	 * 
	 * @param type 数据集类型
	 * @return
	 */
	private boolean isSupportDatasetType(DatasetType type) {
		boolean isSupport = false;
		try {
			if (this.datasetTypes == null || (this.datasetTypes != null && this.datasetTypes.length == 0)) {
				isSupport = true;
			} else {
				for (int i = 0; i < this.datasetTypes.length; i++) {
					if (this.datasetTypes[i] == type) {
						isSupport = true;
						break;
					}
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return isSupport;
	}

	/**
	 * 从下拉列表中移除指定名称的数据集。
	 * 
	 * @param datasetName 待移除数据集的名称
	 */
	public void remove(String datasetName) {
		try {
			for (int i = 0; i < this.getItemCount(); i++) {
				ComboBoxItem itemObject = (ComboBoxItem) this.getItemAt(i);
				if (itemObject.getName().equals(datasetName)) {
					this.remove(i);
					break;
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	/**
	 * 从下拉列表中移除指定的数据集。
	 * 
	 * @param dataset 待移除数据集
	 */
	public void remove(Dataset dataset) {
		try {
			this.remove(dataset.getName());
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	/**
	 * 向下拉列表添加一个数据集。
	 * 
	 * @param dataset 待添加数据集
	 * @return 新增数据集的索引
	 */
	public int add(Dataset dataset) {
		int index = -1;
		try {
			this.addItem(buildItemObject(dataset));
			index = this.getItemCount() - 1;
			if (this.autoSelect && this.getSelectedDataset() == null) {
				this.setSelectedIndex(index);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return index;
	}

	public void refreshType() {
		this.removeAllItems();

	}
}
