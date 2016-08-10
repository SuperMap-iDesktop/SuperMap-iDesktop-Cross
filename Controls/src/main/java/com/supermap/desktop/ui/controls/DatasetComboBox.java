package com.supermap.desktop.ui.controls;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.data.Datasets;
import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.properties.CommonProperties;

import javax.swing.*;

// @formatter:off
/**
 * 带有图标的数据集下拉选择框
 * 太乱，需要重构 by wuxb
 *
 * @author xie
 *
 */
// @formatter:on
public class DatasetComboBox extends JComboBox<Object> {

	private static final long serialVersionUID = 1L;
	private transient DatasetType[] datasetTypes;
	private transient Datasets datasets;

	/**
	 * 包含各种数据集类型的下拉选择框
	 */
	public DatasetComboBox() {
		super(initDatasetComboBoxItem());
		this.setBorder(BorderFactory.createEtchedBorder(1));
		ListCellRenderer<Object> renderer = new CommonListCellRenderer();
		setRenderer(renderer);
	}

	/**
	 * 根据给定数据集类型集合创建下拉选择框
	 *
	 * @param datasetTypes
	 */
	public DatasetComboBox(String[] datasetTypes) {
		super(initDatasetComboBoxItem(datasetTypes));
		this.setBorder(BorderFactory.createEtchedBorder(1));
		ListCellRenderer<Object> renderer = new CommonListCellRenderer();
		setRenderer(renderer);
	}

	/**
	 * 根据给定的数据集集合类创建下拉选择框
	 *
	 * @param datasets
	 */
	public DatasetComboBox(Datasets datasets) {
		super(initDatasetComboBoxItem(datasets));
		this.datasets = datasets;
		this.setBorder(BorderFactory.createEtchedBorder(1));
		ListCellRenderer<Object> renderer = new CommonListCellRenderer();
		setRenderer(renderer);
	}

	/**
	 * 根据给定的数据集集合创建下拉选择框
	 */
	public DatasetComboBox(Dataset[] datasets) {
		super(initDatasetComboBoxItem(datasets));
		this.setBorder(BorderFactory.createEtchedBorder(1));
		ListCellRenderer<Object> renderer = new CommonListCellRenderer();
		setRenderer(renderer);
	}

	/**
	 * 根据给定的数据集类型集合创建下拉选择框
	 *
	 * @param datasetTypes
	 */
	public DatasetComboBox(DatasetType[] datasetTypes) {
		super(initDatasetComboBoxItem(datasetTypes));
		this.datasetTypes = datasetTypes;
		this.setBorder(BorderFactory.createEtchedBorder(1));
		ListCellRenderer<Object> renderer = new CommonListCellRenderer();
		setRenderer(renderer);
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
	 * 选中指定数据集的项
	 *
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

	private static JPanel[] initDatasetComboBoxItem(DatasetType[] datasetTypes) {
		DataCell[] result = new DataCell[datasetTypes.length];
		for (int i = 0; i < datasetTypes.length; i++) {
			String datasetType = CommonToolkit.DatasetTypeWrap.findName(datasetTypes[i]);
			result[i] = new DataCell();
			result[i].initDatasetType(datasetTypes[i], datasetType);
		}
		return result;
	}

	private static DataCell[] initDatasetComboBoxItem(Dataset[] datasets) {
		DataCell[] result = new DataCell[datasets.length];
		for (int i = 0; i < datasets.length; i++) {
			String filePath = CommonToolkit.DatasetImageWrap.getImageIconPath(datasets[i].getType());
			result[i].initDatasetType(datasets[i]);
		}
		return result;
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

	private static JPanel[] initDatasetComboBoxItem() {
		String[] temp = new String[] { CommonProperties.getString("String_DatasetType_All"), CommonProperties.getString("String_DatasetType_CAD"),
				CommonProperties.getString("String_DatasetType_Grid"), CommonProperties.getString("String_DatasetType_GridCollection"),
				CommonProperties.getString("String_DatasetType_Image"), CommonProperties.getString("String_DatasetType_ImageCollection"),
				CommonProperties.getString("String_DatasetType_Line"), CommonProperties.getString("String_DatasetType_Line3D"),
				CommonProperties.getString("String_DatasetType_LinkTable"), CommonProperties.getString("String_DatasetType_Network"),
				CommonProperties.getString("String_DatasetType_ParametricLine"), CommonProperties.getString("String_DatasetType_ParametricRegion"),
				CommonProperties.getString("String_DatasetType_Point"), CommonProperties.getString("String_DatasetType_Point3D"),
				CommonProperties.getString("String_DatasetType_Region"), CommonProperties.getString("String_DatasetType_Region3D"),
				CommonProperties.getString("String_DatasetType_LineM"), CommonProperties.getString("String_DatasetType_Tabular"),
				CommonProperties.getString("String_DatasetType_Template"), CommonProperties.getString("String_DatasetType_Text"),
				CommonProperties.getString("String_DatasetType_Topology"), CommonProperties.getString("String_DatasetType_Unknown"),
				CommonProperties.getString("String_DatasetType_WCS"), CommonProperties.getString("String_DatasetType_WMS") };
		JPanel[] result = initDatasetComboBoxItem(temp);
		return result;
	}

	/**
	 * 根据数据集类型的中文翻译集合初始化DatasetComboBox中的单元格
	 *
	 * @param datasetTypes
	 *            ：数据集类型的中文翻译集合
	 * @return
	 */
	private static JPanel[] initDatasetComboBoxItem(String[] datasetTypes) {
		DataCell[] result = new DataCell[datasetTypes.length];
		for (int i = 0; i < datasetTypes.length; i++) {
			String filePath = CommonToolkit.DatasetImageWrap.getImageIconPath(datasetTypes[i]);
			result[i] = new DataCell();
			result[i].initDataType(filePath, datasetTypes[i]);
		}
		return result;
	}

	private static JPanel[] initDatasetComboBoxItem(Datasets datasets) {
		DataCell[] result = new DataCell[datasets.getCount()];
		for (int i = 0; i < datasets.getCount(); i++) {
			Dataset dataset = datasets.get(i);
			result[i] = new DataCell();
			result[i].initDatasetType(datasets.get(i));
		}
		return result;
	}

	public DatasetType[] getDatasetTypes() {
		return datasetTypes;
	}

	public void setDatasetTypes(DatasetType[] datasetTypes) {
		this.datasetTypes = datasetTypes;
		updateItems();
	}

	public Datasets getDatasets() {
		return datasets;
	}

	public void setDatasets(Datasets datasets) {
		this.datasets = datasets;
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
						if (this.getDatasetTypes() != null && this.getDatasetTypes().length > 0 && !isSupportDatasetType(type)) {
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
	 * 根据传入的数据集类型判断此类型是否支持显示
	 *
	 * @param type
	 *            数据集类型
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

	public Dataset getDatasetAt(int index){
		Dataset dataset = null;
		if (index >= 0 && index < this.getItemCount() && this.getItemAt(index) instanceof DataCell && ((DataCell) this.getItemAt(index)).getData() instanceof Dataset){
			dataset = (Dataset) ((DataCell) this.getItemAt(index)).getData();
		}
		return dataset;
	}

	public void setSelectedDataset(String datasetName) {
		for (int i = 0; i < this.getItemCount(); i++) {
			if (this.getDatasetAt(i).getName().equals(datasetName)) {
				this.setSelectedIndex(i);
				return;
			}
		}
	}

	public void removeDataset(Dataset currentDataset) {
		removeDataset(currentDataset.getName());
	}

	public void removeDataset(String datasetName) {
		for (int i = 0; i < this.getItemCount(); i++) {
			if (this.getDatasetAt(i).getName().equals(datasetName)) {
				this.removeItem(this.getItemAt(i));

			}
		}
	}

	public void addItemAt(int index, Object item) {
		((DefaultComboBoxModel<Object>) this.getModel()).insertElementAt(item, index);
	}
}
