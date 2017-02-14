package com.supermap.desktop.ui.controls;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.data.Datasets;
import com.supermap.desktop.Application;
import com.supermap.desktop.implement.MyComboBoxUI;
import javax.swing.*;

/**
 * 数据集下拉列表控件
 *
 * @author YuanR 2017.2.14
 *         <p>
 *         提供的构造方法：
 *         默认构造空的ComboBox（可以通过setDatasets（）方法重新填充，并支持setSupportedDatasetTypes（））
 *         数据集集合类构造（通过  DatasetComboBox(Datasets datasets) 进行构造）
 *         <p>
 *         可获得的参数：
 *         SupportedDatasetTypes、SelectedDataset、构建的datasets
 */

public class DatasetComboBox extends JComboBox<Object> {

	private static final long serialVersionUID = 1L;
	private transient DatasetType[] datasetTypes;
	private transient Datasets datasets;

	/**
	 * 覆盖原有的updateUI方法
	 * 2016.12.26
	 */
	@Override
	public void updateUI() {
		this.setUI(new MyComboBoxUI());
	}

	/**
	 * 默认构造一个空的下来列表框
	 */
	public DatasetComboBox() {
		initRenderer();
	}

	/**
	 * 根据给定的数据集集合类创建下拉选择框
	 *
	 * @param datasets
	 */
	public DatasetComboBox(Datasets datasets) {
		super(initDatasetComboBoxItem(datasets));
		this.datasets = datasets;
		initRenderer();
	}

	/**
	 * 根据给定的数据集集合创建下拉选择框
	 */
//	public DatasetComboBox(Dataset[] dataset) {
//		super(initDatasetComboBoxItem(dataset));
//		this.dataset = dataset;
//		initRenderer();
//	}

	/**
	 * 初始化渲染方式
	 */
	private void initRenderer() {
		this.setBorder(BorderFactory.createEtchedBorder(1));
		ListCellRenderer<Object> renderer = new CommonListCellRenderer();
		setRenderer(renderer);
	}

//	/**
//	 * @param datasets
//	 * @return
//	 */
//	private static DataCell[] initDatasetComboBoxItem(Dataset[] datasets) {
//		DataCell[] result = new DataCell[datasets.length];
//		for (int i = 0; i < datasets.length; i++) {
//			result[i].initDatasetType(datasets[i]);
//		}
//		return result;
//	}

	/**
	 * @param datasets
	 * @return
	 */
	private static DataCell[] initDatasetComboBoxItem(Datasets datasets) {
		DataCell[] result = new DataCell[datasets.getCount()];
		for (int i = 0; i < datasets.getCount(); i++) {
			result[i] = new DataCell();
			result[i].initDatasetType(datasets.get(i));
		}
		return result;
	}

	/**
	 * 选中指定数据集的项
	 *
	 * @param dataset
	 */
	public void setSelectedDataset(Dataset dataset) {
		int selectIndex = -1;
		if (dataset != null) {
			for (int i = 0; i < getItemCount(); i++) {
				DataCell dataCell = (DataCell) getItemAt(i);
				if (dataCell.getData() == dataset) {
					selectIndex = i;
					break;
				}
			}
		}
		setSelectedIndex(selectIndex);
	}

	/**
	 * 设置数据集集合
	 *
	 * @param datasets
	 */
	public void setDatasets(Datasets datasets) {
		this.datasets = datasets;
		updateItems();
	}

	/**
	 * 设置支持的数据集类型
	 *
	 * @param datasetTypes
	 */
	public void setSupportedDatasetTypes(DatasetType[] datasetTypes) {
		this.datasetTypes = datasetTypes;
		updateItems();
	}

	/**
	 * 更改设置之后，更新组合框的子项
	 */
	public void updateItems() {
		try {
			this.removeAllItems();
			if (this.datasets != null) {
				try {
					for (int i = 0; i < this.datasets.getCount(); i++) {
						Dataset dataset = this.datasets.get(i);
						DatasetType type = dataset.getType();
						if (this.getSupportedDatasetTypes() != null && this.getSupportedDatasetTypes().length > 0 && !isSupportDatasetType(type)) {
							continue;
						} else {
							DataCell datasetTypeCell = new DataCell();
							datasetTypeCell.initDatasetType(dataset);
							this.addItem(datasetTypeCell);
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
	 * 由于填充的是DatasetCell 返回时要得到DatasetCell中JLabel显示的字符串
	 *
	 * @return
	 */
	public String getSelectItem() {
		if (getSelectedIndex() == -1) {
			return null;
		}
		DataCell temp = (DataCell) getSelectedItem();
		return temp.getDataName();
	}

	/**
	 * 获取选中的数据集
	 *
	 * @return
	 */
	public Dataset getSelectedDataset() {
		Dataset result = null;
		if (getSelectedItem() instanceof DataCell) {
			DataCell selected = (DataCell) getSelectedItem();
			if (selected.getData() instanceof Dataset) {
				result = (Dataset) selected.getData();
			}
		}
		return result;
	}

	/**
	 * 获得支持的数据集类型
	 *
	 * @return
	 */
	public DatasetType[] getSupportedDatasetTypes() {
		return datasetTypes;
	}

	/**
	 * 获得构造ComboBox的数据集集合类
	 *
	 * @return
	 */
	public Datasets getDatasets() {
		return datasets;
	}

//	/**
//	 * 获得ComboBox中的数据集集合数组
//	 *
//	 * @return
//	 */
//	public Dataset[] getDataset() {
//		return dataset;
//	}

	/**
	 * 根据传入的数据集类型判断此类型是否支持显示
	 *
	 * @param type 数据集类型
	 * @return
	 */
	private boolean isSupportDatasetType(DatasetType type) {
		boolean isSupport = false;
		try {
			if (this.datasetTypes == null || this.datasetTypes.length == 0) {
				isSupport = true;
			} else {
				for (DatasetType datasetType : this.datasetTypes) {
					if (datasetType == type) {
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
	 * 判断ComboBox中是否含有此名称的数据集
	 *
	 * @param datasetName
	 * @return
	 */
	public boolean hasDataset(String datasetName) {
		for (int i = 0; i < this.getItemCount(); i++) {
			if (this.getDatasetAt(i).getName().equals(datasetName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 设置ComboBox选中的数据集为该数据集
	 *
	 * @param datasetName
	 */
	public void setSelectedDataset(String datasetName) {
		for (int i = 0; i < this.getItemCount(); i++) {
			if (this.getDatasetAt(i).getName().equals(datasetName)) {
				this.setSelectedIndex(i);
				return;
			}
		}
	}

	/**
	 * 通过数据集名称移除ComboBox中该item
	 *
	 * @param datasetName
	 */
	public void removeDataset(String datasetName) {
		for (int i = 0; i < this.getItemCount(); i++) {
			if (this.getDatasetAt(i).getName().equals(datasetName)) {
				this.removeItem(this.getItemAt(i));
				updateUI();
			}
		}
	}

	/**
	 * 移除ComboBox中传入的该数据集item
	 *
	 * @param currentDataset
	 */
	public void removeDataset(Dataset currentDataset) {
		removeDataset(currentDataset.getName());
	}


	/**
	 * 通过item次序获得数据集
	 *
	 * @param index
	 * @return
	 */
	public Dataset getDatasetAt(int index) {
		Dataset dataset = null;
		if (index >= 0 && index < this.getItemCount() && this.getItemAt(index) instanceof DataCell && ((DataCell) this.getItemAt(index)).getData() instanceof Dataset) {
			dataset = (Dataset) ((DataCell) this.getItemAt(index)).getData();
		}
		return dataset;
	}

	/**
	 * 增加一数据集
	 */
	public void addItemAt(int index, Dataset item) {
		((DefaultComboBoxModel<Object>) this.getModel()).insertElementAt(item, index);
	}
}
